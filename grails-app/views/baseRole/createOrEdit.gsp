<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
</head>
<body>
<div class="row">
    <div class="col-lg-12">
        <div class="card">
            <div class="col-md-12">
                <form id="roleForm">
                    <input type="hidden" name="id" value="${baseRole?.id}"/>
                    <div class="row">
                        <div class="col-md-1">名称</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="name" required maxlength="50" value="${baseRole?.name}">
                        </div>
                        <div class="col-md-1">角色标识</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="authority" required maxlength="50" value="${baseRole?.authority}">
                        </div>
                        <div class="col-md-1">角色描述</div>
                        <div class="col-md-11">
                            <input type="text" class="form-control" name="description" required maxlength="200" value="${baseRole?.description}">
                        </div>
%{--                        <div class="col-md-1">可维护栏目及站点</div>--}%
%{--                        <div class="col-md-11">--}%
%{--                            <input type="hidden" class="form-control" name="catalogstr" value="${baseRole?.catalogstr}">--}%
%{--                            <input type="text" name="catalogName" readonly class="form-control" value="${catalogNames}" onclick="showCatalogMenuModal();"/>--}%
%{--                        </div>--}%
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
%{--<g:render template="/catalog/catalogTreeModal" model="['usage':'baseRole']"></g:render>--}%
<g:render template="js"/>
</body>
</html>