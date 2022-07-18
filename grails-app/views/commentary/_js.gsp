<script>

    $(function () {

    });



    function queryParams(params){
        params.createdBy=$("#createdBy").val();
        params.max=params.limit;
        return params;
    }

    function doSearch(){
        $("#commentaryTable").bootstrapTable("refresh",[]);
    }

    //操作
    function optionFormatter(value, row, index) {
        var str=[];
        str.push('<button class="btn btn-danger" onclick="javascript:deleteCommentary(\''+row.id+'\')"><i class="glyphicon glyphicon-trash"></i></button>');
        return str.join('');
    }
    //审核状态
    function approvedFormatter(value, row, index) {
        var str=[];
        if(value==0){
            str.push('<span class="text-success">未审核</span>');
        }else if(value==1){
            str.push('<span class="text-success">通过</span>');
        }else{
            str.push('<span class="text-danger">不通过</span>');
        }
        return str.join('');
    }

    function deleteCommentary(id){
        if(id&&window.confirm("彻底删除该评论信息？")){
            $.post("${request.contextPath}/commentary/deleteCommentary", {id:id}, function (data) {
                alert(data.message);
                if (data.result) {
                    doSearch();
                }
            }, "json");
        }
    }

    //改变评论状态
    function auditCommentary(state) {
        var selectRows=$('#commentaryTable').bootstrapTable('getSelections');
        if (selectRows.length <= 0) {
            alert("请至少选择一条评论！");
        }else {
            var fields='';
            for (var i = 0; i < selectRows.length; i++) {
                if (fields != '') fields += ',';
                fields += selectRows[i].id;
            }
            var obj=new Object();
            obj.fields=fields;
            obj.state=state;
            $.post('${request.contextPath}/commentary/auditCommentary',obj,function (data){
                alert(data.message);
                doSearch();
            },"json");
        }
    }

    function backToTradeList(){
        loadRemotePage('${request.contextPath}/trade/list');
    }

</script>
