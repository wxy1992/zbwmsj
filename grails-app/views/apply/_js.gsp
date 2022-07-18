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

    function deleteapply(id){
        if(id&&window.confirm("彻底删除该评论信息？")){
            $.post("${request.contextPath}/apply/deleteapply", {id:id}, function (data) {
                alert(data.message);
                if (data.result) {
                    doSearch();
                }
            }, "json");
        }
    }

    function backToTradeList(){
        loadRemotePage('${request.contextPath}/trade/list');
    }

</script>
