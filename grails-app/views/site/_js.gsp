<script>

    $(function () {
        if($("#jtfb").val()=="true"){
            $('input:checkbox').attr("checked", true)
        }else {
            $('input:checkbox').attr("checked", false)
        };
        $("#jtfb").click(function () {
            if ($(this).prop("checked")) {
                $("#jtfb").val(true);
            } else {
                $("#jtfb").val(true);
            }
        });
        $("#siteForm").validate({
            rules:{
                name:{
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

    function createOrEdit(id){
        $.post("${request.contextPath}/site/createOrEdit",{id:id},function(data){
            $('#mainBodyDiv').html('');
            $('#mainBodyDiv').html(data);
        },"html");
    }

    //保存
    function saveOrUpdate() {
        if ($("#siteForm").valid()) {
            $.post("${request.contextPath}/site/saveOrUpdate", $("#siteForm").serialize(), function (data) {
                $("#messageModalBody").html(data.message);
                $("#messageModal").modal('show');
                if (data.result) {
                    loadRemotePage('${request.contextPath}/site/list');
                }
            }, "json");
        }
    }

    function queryParams(params){
        params.siteName=$("#siteName").val();
        params.siteDomain=$("#siteDomain").val();
        params.max=params.limit;
        return params;
    }

    function doSearch(){
        $("#siteTable").bootstrapTable("refresh",[]);
    }

    //操作
    function optionFormatter(value, row, index) {
        var str=[];
        str.push('<button class="btn btn-primary" onclick="javascript:createOrEdit(\''+row.id+'\')"><i class="glyphicon glyphicon-pencil"></i></button>&nbsp;&nbsp;');
        str.push('<a class="btn btn-info" href="'+row.url+'" target="_blank"><i class="glyphicon glyphicon-eye-open text-white"></i></a>&nbsp;&nbsp;');
        str.push('<button class="btn btn-danger" onclick="javascript:deleteSite(\''+row.id+'\')"><i class="glyphicon glyphicon-trash"></i></button>');
        return str.join('');
    }

    function deleteSite(id){
        if(id&&window.confirm("彻底删除站点及相关数据、资源？")){
            $.post("${request.contextPath}/site/deleteSite", {id:id}, function (data) {
                alert(data.message);
                if (data.result) {
                    doSearch();
                }
            }, "json");
        }
    }

    function backToList(){
        loadRemotePage('${request.contextPath}/site/list');
    }
</script>
