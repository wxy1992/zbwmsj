<script>
    function createOrEditCatlog(id,site){
        if(site||id){
            $.post("${request.contextPath}/catalog/createOrEdit",{id:id,site:site},function(data){
                $('#catalogContentDiv').html('');
                $('#catalogContentDiv').html(data);
            },"html");
        }else{
            alert("请先选择左侧站点");
        }
    }

    function deleteCatalog(id) {
        if(confirm("确认删除?")) {
            $.post("${request.contextPath}/catalog/delCatalog", {id: id}, function (data) {
                alert(data.message);
                if (data.result) {
                    backToList(data.site);
                }
            }, "json");
        }
    }

    //删除
    function deleteAll() {
        var selectRows=$('#catalogListTable').bootstrapTable('getSelections');
        if (selectRows.length <= 0) {
            alert("请至少选择一条栏目！");
        }else if(window.confirm("确定删除选中的栏目？")){
            var fields='';
            for (var i = 0; i < selectRows.length; i++) {
                if (fields != '') fields += ',';
                fields += selectRows[i].id;
            }
            $.post('${request.contextPath}/catalog/batchDelete',{fields:fields},function (data){
                alert(data.message);
                if(data.result){
                    doSearch();
                }
            },"json");
        }
    }

    function queryParams(params){
        $("input:not(:empty),.bootstrap-table-search-keyword").each(function(){
            params[$(this).attr('name')]=$(this).val();
        });
        params.keywords=$('input[name="keywords"]').val();
        return params;
    }

    function doSearch(){
        $("#catalogListTable").bootstrapTable("refresh",[]);
    }

    function optionFormatter(value, row, index) {
        var str=[];
        str.push('<button class="btn btn-primary" onclick="javascript:createOrEditCatlog(\''+row.id+'\',${siteId})"><i class="glyphicon glyphicon-pencil"></i></button>&nbsp;&nbsp;');
        str.push('<button class="btn btn-danger" onclick="javascript:deleteCatalog(\''+row.id+'\')"><i class="glyphicon glyphicon-trash"></i></button>');
        return str.join('');
    }

    function backToList(site){
        loadRemotePage('${request.contextPath}/catalog/list',{site:site});
    }
</script>
