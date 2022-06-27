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

</script>
