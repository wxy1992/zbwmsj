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
                <form id="organizationForm">
                    <input type="hidden" name="id" value="${organization?.id}"/>
                    <div class="row">
                        <div class="col-md-2">单位名称</div>
                        <div class="col-md-10">
                            <input type="text" class="form-control" name="name" maxlength="10" value="${organization?.name}">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-2">单位简称</div>
                        <div class="col-md-10">
                            <input type="text" class="form-control" name="shortName" maxlength="40" value="${organization?.shortName}">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-2">上级单位</div>
                        <div class="col-md-10">
                            <g:select from="${com.wmsj.core.Organization.list([sort: 'id'])}" optionKey="id" optionValue="name"
                                      class="form-control" name="parent.id" noSelection="['':'请选择']"
                                      value="${organization?.parentId}"></g:select>
                        </div>
                    </div>
                    <div class="row">
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
