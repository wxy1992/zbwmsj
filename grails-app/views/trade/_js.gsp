<script>

    $(function () {
        $(".datetimepicker" ).datetimepicker({language: 'zh-CN',autoclose: true,todayBtn: true,format: 'yyyy-mm-dd hh:ii',endDate:'${new Date().format('yyyy-MM-dd')}'});
        $("#tradeForm input:required").after('<span class="error-span">*</span>');
    });

    function createOrEdit(id,typeId,typeName){
        $.post("${request.contextPath}/trade/createOrEdit",{id:id,typeId:typeId,typeName:typeName},function(data){
            $('#mainBodyDiv').html('');
            $('#mainBodyDiv').html(data);
        },"html");
    }

    //保存
    function saveOrUpdate(operation) {
        $("#tradeForm #operation").val(operation);
        if ($("#tradeForm").valid()) {
            $.post("${request.contextPath}/trade/saveOrUpdate", $("#tradeForm").serialize(), function (data) {
                $("#messageModalBody").html(data.message);
                $("#messageModal").modal('show');
                if (data.result) {
                    backToList();
                }
            }, "json");
        }
    }

    function queryParams(params){
        params.organization=$("#organization option:selected").val();
        params.title=$("#title").val();
        params.status=$("#status option:selected").val();
        params.max=params.limit;
        return params;
    }

    function doSearch(){
        $("#tradeTable").bootstrapTable("refresh",[]);
    }

    function statusFormatter(value, row, index) {

    }

    //操作
    function optionFormatter(value, row, index) {
        var str=[];
        str.push('<div class="operation_column"><a class="text-primary" href="javascript://" onclick="javascript:createOrEdit(\''+row.id+'\',\'\',\'\')">编辑</a>');
        str.push('<a class="text-danger" href="javascript://" onclick="javascript:deleteTrade(\''+row.id+'\')">删除</a>');
        str.push('<a class="text-primary" href="javascript://" onclick="javascript:showApply(\''+row.id+'\',\'\',\'\')">报名列表</a>');
        str.push('<a class="text-primary" href="javascript://" onclick="javascript:showCommentary(\''+row.id+'\',\'\',\'\')">评论列表</a></div>');
        return str.join('');
    }

    function deleteTrade(id){
        if(id&&window.confirm("删除选中的服务？")){
            $.post("${request.contextPath}/trade/deleteTrade", {id:id}, function (data) {
                alert(data.message);
                if (data.result) {
                    doSearch();
                }
            }, "json");
        }
    }


    function changeTradeStatus(operation) {
        var selectRows = $('#tradeTable').bootstrapTable('getSelections');
        if (selectRows.length <= 0) {
            alert("请至少选择一条数据");
            return;
        }

        if (operation == '结束') {
            var goingRows = $.grep(selectRows, function (obj, i) {
                return obj['status'] != 20;  //return为过滤的条件
            });
            if(goingRows.length > 0){
                alert("仅可结束进行中的服务");
                return;
            }
        }
        if (window.confirm(operation + "选中的服务？")) {
            var fields = '';
            for (var i = 0; i < selectRows.length; i++) {
                if (fields != '') fields += ',';
                fields += selectRows[i].id;
            }
            var obj = new Object();
            obj.fields = fields;
            obj.operation = operation;
            if (operation == '退回') {
                obj.backreason = prompt('意见建议', '');
            }
            $.post('${request.contextPath}/trade/changeTradeStatus', obj, function (data) {
                alert(data.message);
                doSearch();
                countTodoTask();
            }, "json");
        }
    }

    function backToList(){
        loadRemotePage('${request.contextPath}/trade/list');
    }

    function showCommentary(tradeId){
        loadRemotePage('${request.contextPath}/commentary/list?tradeId='+tradeId);
    }

    function showApply(tradeId){
        loadRemotePage('${request.contextPath}/apply/list?tradeId='+tradeId);
    }
</script>
