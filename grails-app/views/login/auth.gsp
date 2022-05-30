<%@ page import="com.bjrxkj.business.Trade; com.bjrxkj.business.TradeStatusEnum; com.bjrxkj.core.BaseUser; com.bjrxkj.core.BaseUserBaseRole" %>
<html>
<head>
    <title>登录</title>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/workspace/fonts.css"/>
    <style type="text/css" media="screen">
.footer {
    width: 100%;
    heigt: 260px;
    color: #fff;
    text-align: center;
    line-height: 28px;
    font-size: 14px;
    padding-top: 20px;

}
    .footer div{
        display: block;
        width: 100%;
    }
    </style>
</head>

<body style="background: #FF3528;">
<g:set var="cmsTitle" value="${grailsApplication.config.project.setting.name}"/>
<div class="container-fluid pr-0 pl-0">
    <div class="row mr-0 ml-0">
        <h1 class="text-white text-center container">${cmsTitle}</h1>
    </div>
    <div class="row mr-0 ml-0" style="background: #eeca5c;">
        <div style="margin: 50px auto;width: 20%;min-width: 300px;">
            <form action="${request.contextPath}/login/loginVerify" method="POST" id="loginForm" class="form-horizontal" autocomplete="off">
                <input value="${params.loginType?:flash?.loginType?:'show'}" type="hidden" name="loginType"/>
                <div class="row">
                    <div class="col-lg-12 text-white text-center">${flash?.message}</div>
                    <div class="col-lg-12 mt-lg-4">
                        <input type="text" class="form-control" name="username" id="username" placeholder="用户名" value="${flash?.username}">
                    </div>
                    <div class="col-lg-12 mt-lg-4">
                        <input type="password" class="form-control" name="password" id="password" placeholder="密码">
                    </div>
                    <div class="col-lg-12 text-center mt-lg-5">
                        <button class="btn btn-primary btn-lg form-control" style="background: #0f7297;border: #3895b8;" type="submit">登　　录</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="row footer text-center mr-0 ml-0">
        <div>${cmsTitle}</div>
    </div>
</div>
<script>
    (function() {
        document.forms['loginForm'].elements['${usernameParameter ?: 'username'}'].focus();
    })();
</script>
</body>
</html>
