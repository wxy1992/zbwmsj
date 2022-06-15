<script>

    $(function () {

    });

    function createOrEdit(id){
        $.post("${request.contextPath}/organization/createOrEdit",{id:id},function(data){
            $('#mainBodyDiv').html('');
            $('#mainBodyDiv').html(data);
        },"html");
    }

    //保存
    function saveOrUpdate() {
        if ($("#organizationForm").valid()) {
            $.post("${request.contextPath}/organization/saveOrUpdate", $("#organizationForm").serialize(), function (data) {
                alert(data.message);
                if (data.result) {
                    backToList();
                }
            }, "json");
        }
    }

    function queryParams(params){
        params.name=$("#organizationName").val();
        params.shortName=$("#organizationShortName").val();
        params.max=params.limit;
        return params;
    }

    function doSearch(){
        $("#organizationTable").bootstrapTable("refresh",[]);
    }

    //操作
    function optionFormatter(value, row, index) {
        var str=[];
        str.push('<button class="btn btn-primary" onclick="javascript:createOrEdit(\''+row.id+'\')"><i class="glyphicon glyphicon-pencil"></i></button>&nbsp;&nbsp;');
        str.push('<button class="btn btn-danger" onclick="javascript:deleteSite(\''+row.id+'\')"><i class="glyphicon glyphicon-trash"></i></button>');
        return str.join('');
    }

    function deleteSite(id){
        if(id&&window.confirm("彻底删除该单位？")){
            $.post("${request.contextPath}/organization/deleteOrganization", {id:id}, function (data) {
                alert(data.message);
                if (data.result) {
                    doSearch();
                }
            }, "json");
        }
    }

    function backToList(){
        loadRemotePage('${request.contextPath}/organization/list');
    }
</script>
