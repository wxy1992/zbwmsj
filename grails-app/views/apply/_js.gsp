<script>

    $(function () {

    });



    function queryParams(params){
        params.name=$("#name").val();
        params.telephone=$("#telephone").val();
        params.idcard=$("#idcard").val();
        params.max=params.limit;
        return params;
    }

    function doSearch(){
        $("#applyTable").bootstrapTable("refresh",[]);
    }

    //操作
    function optionFormatter(value, row, index) {
        var str=[];
        // str.push('<button class="btn btn-danger" onclick="javascript:deleteapply(\''+row.id+'\')"><i class="glyphicon glyphicon-trash"></i></button>');
        return str.join('');
    }


    function backToTradeList(){
        loadRemotePage('${request.contextPath}/trade/list');
    }


    //改变评论状态
    function changeApplyStatus(status) {
        var selectRows=$('#applyTable').bootstrapTable('getSelections');
        if (selectRows.length <= 0) {
            alert("请至少选择一条报名信息！");
        }else if(window.confirm("确定对选中的记录进行此操作？")){
            var fields='';
            for (var i = 0; i < selectRows.length; i++) {
                if (fields != '') fields += ',';
                fields += selectRows[i].id;
            }
            var obj=new Object();
            obj.fields=fields;
            obj.status=status;
            $.post('${request.contextPath}/apply/changeApplyStatus',obj,function (data){
                alert(data.message);
                doSearch();
            },"json");
        }
    }
</script>
