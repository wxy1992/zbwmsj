<script>

    $(function () {
        $("#userForm").validate({
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
        $.post("${request.contextPath}/baseUser/createOrEdit",{id:id},function(data){
            $('#mainBodyDiv').html('');
            $('#mainBodyDiv').html(data);
        },"html");
    }

    //保存
    function saveOrUpdate() {
        if($('#password').val()!=$('#password2').val()){
            alert('两次密码输入不一致！');
            return false;
        }
        if ($("#userForm").valid()) {
            $.post("${request.contextPath}/baseUser/saveOrUpdate", $("#userForm").serialize(), function (data) {
                alert(data.message);
                if (data.result) {
                    loadRemotePage('${request.contextPath}/baseUser/list');
                }
            }, "json");
        }
    }


    
    function queryParams(params){
        params.username=$("#username").val();
        params.realName=$("#realName").val();
        params.max=params.limit;
        return params;
    }

    function doSearch(){
        $("#userTable").bootstrapTable("refresh",[]);
    }

    //操作
    function optionFormatter(value, row, index) {
        var str=[];
        str.push('<button class="btn btn-primary" title="编辑" onclick="javascript:createOrEdit(\''+row.id+'\')"><i class="glyphicon glyphicon-pencil"></i></button>&nbsp;&nbsp;');
        if(row['enabled']){
            str.push('<button class="btn btn-danger" title="禁用" onclick="javascript:changeUserEnable(\''+row.id+'\',false)"><i class="glyphicon glyphicon-ban-circle"></i></button>');
        }else{
            str.push('<button class="btn btn-success" title="启用" onclick="javascript:changeUserEnable(\''+row.id+'\',true)"><i class="glyphicon glyphicon-ok-circle"></i></button>');
        }
        return str.join('');
    }
    function booleanFormatter(value, row, index) {
        return value?'启用':'禁用';
    }

    function backToList(){
        loadRemotePage('${request.contextPath}/baseUser/list');
    }

    function changeUserEnable(id,state){
        $.post("${request.contextPath}/baseUser/changeUserEnable", {id:id,state:state}, function (data) {
            alert(data.message);
            if (data.result) {
                loadRemotePage('${request.contextPath}/baseUser/list');
            }
        }, "json");
    }

    function removeBaseUsers(){
        var selectRows=$('#userTable').bootstrapTable('getSelections');
        if (selectRows.length <= 0) {
            alert("请至少选择一条记录！");
        }else if(window.confirm("确定删除选中的记录？")){
            var fields='';
            for (var i = 0; i < selectRows.length; i++) {
                if (fields != '') fields += ',';
                fields += selectRows[i].id;
            }
            $.post('${request.contextPath}/baseUser/deleteAll',{fields:fields},function (data){
                alert(data.message);
                doSearch();
            },"json");
        }
    }
</script>