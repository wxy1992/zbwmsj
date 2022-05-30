<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>
<body>
<style>
input[type="radio"], input[type="checkbox"] {
    box-sizing: border-box;
    padding: 0;
    zoom: 150%;
}
    .templatePic{
        display: inline-block;
        position: relative;
    }
.templatePic a{
    position: absolute;
    top: 3px;
    right: 3px;
    z-index: 9999;
    background: rgba(255,255,255,0.8);
    border-radius: 50%;
    width: 20px;
    text-align: center;
}
.templatePic a i{
    color: #343a40;
}
</style>
<div class="row">
    <div class="col-lg-12">
        <div class="card">
            <div class="col-md-12">
                <form id="siteForm">
                    <input type="hidden" name="id" value="${site?.id}"/>
                    <div class="row">
                        <div class="col-md-1">站点代码</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="name" maxlength="10" value="${site?.name}">
                        </div>
                        <div class="col-md-1">域名</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="domainName" maxlength="40" value="${site?.domainName}">
                        </div>
                        <div class="col-md-1">机构名称</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="companyName" maxlength="40" value="${site?.companyName}">
                        </div>
                        <div class="col-md-1">显示名称</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="shortName" maxlength="40" value="${site?.shortName}">
                        </div>
                        <div class="col-md-1">机构地址</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="address" maxlength="40" value="${site?.address}">
                        </div>
                        <div class="col-md-1">站长信箱</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="adminEmail" maxlength="40" value="${site?.adminEmail}">
                        </div>
                        <div class="col-md-1">版权信息</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="copyright" maxlength="100" value="${site?.copyright}">
                        </div>
                        <div class="col-md-1">备案号</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="code" maxlength="100" value="${site?.code}">
                        </div>
                        <div class="col-md-1">排序号</div>
                        <div class="col-md-5">
                            <input type="number" class="form-control" name="sequencer" maxlength="9" value="${site?.sequencer}">
                        </div>
                        <div class="col-md-1">设为主站</div>
                        <div class="col-md-5">
                            <input type="radio" name="homesite" value="true" <g:if test="${site?.homesite}">checked</g:if> >是
                            <input type="radio" name="homesite" value="false" <g:if test="${!site?.homesite}">checked</g:if> >否
                        </div>
                        <div class="col-md-1">站点模板</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="template" maxlength="100" value="${site?.template}">
                        </div>
                        <div class="col-md-1">访问地址</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="url" maxlength="500" value="${site?.url}">
                        </div>
                        <div class="col-md-12 text-center">
                            <button class="btn btn-primary" type="button" onclick="saveOrUpdate();">确定</button>
                            <button class="btn btn-info" type="button" onclick="backToList();">返回</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<g:render template="js"/>
</body>
</html>
