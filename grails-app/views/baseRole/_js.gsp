<script>

    $(function () {
        $("#roleForm").validate({
            rules:{
                username:{
                    required :true
                },
                realName:{
                    required :true,
                }
            },
            message:{
                name:{
                    required:"请输入用户名"
                },
                domainName:{
                    required:"请输入真实姓名"
                }
            }
        });
    });

    function createOrEdit(id){
        $.post("${request.contextPath}/baseRole/createOrEdit",{id:id},function(data){
            $('#mainBodyDiv').html('');
            $('#mainBodyDiv').html(data);
        },"html");
    }

    //保存
    function saveOrUpdate() {
        if ($("#roleForm").valid()) {
            $.post("${request.contextPath}/baseRole/saveOrUpdate", $("#roleForm").serialize(), function (data) {
                alert(data.message);
                if (data.result) {
                    loadRemotePage('${request.contextPath}/baseRole/list');
                }
            }, "json");
        }
    }
    
    function queryParams(params){
        params.keywords=$("#keywords").val();
        params.max=params.limit;
        return params;
    }

    function doSearch(){
        $("#roleTable").bootstrapTable("refresh",[]);
    }

    //操作
    function optionFormatter(value, row, index) {
        var str=[];
        str.push('<button class="btn btn-primary" title="编辑" onclick="javascript:createOrEdit(\''+row.id+'\')"><i class="glyphicon glyphicon-pencil"></i></button>&nbsp;&nbsp;');
        return str.join('');
    }

    function backToList(){
        loadRemotePage('${request.contextPath}/baseRole/list');
    }

    function removeBaseRoles(){
        var selectRows=$('#roleTable').bootstrapTable('getSelections');
        if (selectRows.length <= 0) {
            alert("请至少选择一条记录！");
        }else if(window.confirm("确定删除选中的记录？")){
            var fields='';
            for (var i = 0; i < selectRows.length; i++) {
                if (fields != '') fields += ',';
                fields += selectRows[i].id;
            }
            $.post('${request.contextPath}/baseRole/deleteAll',{fields:fields},function (data){
                alert(data.message);
                doSearch();
            },"json");
        }
    }

</script>