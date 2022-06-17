<%@ page import="com.wmsj.core.*" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>
<body>
<div class="row">
    <div class="col-lg-12">
        <div class="card">
            <div class="col-md-12">
                <form id="userForm" enctype="multipart/form-data">
                    <input type="hidden" name="id" value="${baseUser?.id}"/>
                    <div class="row">
                        <div class="col-md-1">用户名</div>
                        <div class="col-md-5">
                            ${baseUser.username}
                        </div>
                        <div class="col-md-1">真实姓名</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" maxlength="50" name="realName" value="${baseUser?.realName}">
                        </div>
                        <div class="col-md-1">性别</div>
                        <div class="col-md-5">
                            <g:select from="['男','女']" class="form-control" name="sex" value="${baseUser?.sex}"></g:select>
                        </div>
                        <div class="col-md-1">头像</div>
                        <div class="col-md-5">
                            <input type="file" class="form-control" name="picture">
                            <br>
                            <img src="${request.contextPath}/web/userHeadimg/${baseUser?.id}" style="width: 50px;height: 50px;border-radius: 50%;">

                        </div>
                        <div class="col-md-1">邮箱</div>
                        <div class="col-md-5">
                            <input type="email" class="form-control" name="email" value="${baseUser?.email}">
                        </div>
                        <div class="col-md-1">机构</div>
                        <div class="col-md-5">
                            <g:select from="${Organization.list()}" optionKey="id" optionValue="name" class="form-control" name="organization" value="${baseUser?.organizationId}"></g:select>
                        </div>
                        <div class="col-md-1">部门</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="department" value="${baseUser?.department}">
                        </div>
                        <div class="col-md-1">工号</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="workId" value="${baseUser?.workId}">
                        </div>
                        <div class="col-md-1">证书编号</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="ukey" value="${baseUser?.ukey}">
                        </div>
                        <div class="col-md-1">旧密码</div>
                        <div class="col-md-5">
                            <input type="password" class="form-control" required maxlength="50" id="oldPass" name="oldPass" value="">
                        </div>
                        <div class="col-md-1">新密码</div>
                        <div class="col-md-5">
                            <input type="password" class="form-control" required maxlength="50" id="newPass" name="newPass" value="">
                        </div>
                        <div class="col-md-1">确认密码</div>
                        <div class="col-md-5">
                            <input type="password" class="form-control" required maxlength="50" id="newPass1" name="newPass1" value="">
                        </div>
                        <div class="col-md-12 text-center">
                            <button class="btn btn-primary" type="button" onclick="savePasswd();">确定</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    //修改密码
    function savePasswd() {
        if ($("#userForm").valid()) {
            var form = document.getElementById("userForm");
            var formData = new FormData(form);
            formData.append('picture',$("input[name=picture]").val());
            $.ajax({
                url:'${request.contextPath}/cms/changePasswdSave',
                dataType:'json',
                type:'POST',
                async:true,
                data: formData,
                processData : false, // 使数据不做处理
                contentType : false, // 不要设置Content-Type请求头
                success: function(data){
                    alert(data.message);
                    window.location.href='${request.contextPath}/cms/workSpace';
                },
                error:function(response){
                    console.log(response);
                }
            });
        }
    }
</script>
</body>
</html>
