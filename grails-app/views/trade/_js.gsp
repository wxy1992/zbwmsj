<script>

    $(function () {
        $( ".datetimepicker" ).datetimepicker({language: 'zh-CN',autoclose: true,todayBtn: true,format: 'yyyy-mm-dd hh:ii',endDate:'${new Date().format('yyyy-MM-dd')}'});

        $("#tradeForm").validate({
            rules:{
                title:{
                    required :true,
                },
                domainName:{
                    required :true,
                }
            },
            message:{
                name:{
                    required:"请输入站点名称",
                },
                domainName:{
                    required:"请输入站点域名",
                }
            }
        });
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
        str.push('<button class="btn btn-primary" onclick="javascript:createOrEdit(\''+row.id+'\',\'\',\'\')"><i class="glyphicon glyphicon-pencil"></i></button>&nbsp;&nbsp;');
        str.push('<a class="btn btn-info" href="'+row.url+'" target="_blank"><i class="glyphicon glyphicon-eye-open text-white"></i></a>&nbsp;&nbsp;');
        str.push('<button class="btn btn-danger" onclick="javascript:deleteTrade(\''+row.id+'\')"><i class="glyphicon glyphicon-trash"></i></button>');
        return str.join('');
    }

    function deleteTrade(id){
        if(id&&window.confirm("删除选中订单数据？")){
            $.post("${request.contextPath}/trade/deleteTrade", {id:id}, function (data) {
                alert(data.message);
                if (data.result) {
                    doSearch();
                }
            }, "json");
        }
    }


    function changeTradeStatus(operation){
        var selectRows=$('#tradeTable').bootstrapTable('getSelections');
        if (selectRows.length <= 0) {
            alert("请至少选择一条数据！");
        }else {
            var fields='';
            for (var i = 0; i < selectRows.length; i++) {
                if (fields != '') fields += ',';
                fields += selectRows[i].id;
            }
            var obj=new Object();
            obj.fields=fields;
            obj.operation=operation;
            if(operation=='退回'){
                obj.backreason = prompt('意见建议', '');
            }
            $.post('${request.contextPath}/trade/changeTradeStatus',obj,function (data){
                alert(data.message);
                doSearch();
            },"json");
        }
    }

    function backToList(){
        loadRemotePage('${request.contextPath}/trade/list');
    }
</script>
