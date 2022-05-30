<%@ page import="com.bjrxkj.core.BaseRole; com.bjrxkj.cms.Site" contentType="text/html;charset=UTF-8" %>
<html>
<head>
</head>
<body>
<div class="row">
    <div class="col-lg-12">
        <div class="card">
            <div class="col-md-12">
                <form id="userForm">
                    <input type="hidden" name="id" value="${baseUser?.id}"/>
                    <div class="row">
                        <div class="col-md-1">用户名</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" required name="username" maxlength="50" value="${baseUser?.username}"
                                   autocomplete="false" <g:if test="${baseUser?.id}">readonly</g:if> >
                        </div>
                        <div class="col-md-1">姓名</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" required name="realName" maxlength="50" value="${baseUser?.realName}">
                        </div>
                        <div class="col-md-1">Ukey唯一标识</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="ukey" maxlength="50" value="${baseUser?.ukey}">
                        </div>
                        <div class="col-md-1">密码</div>
                        <div class="col-md-5">
                            <input type="password" class="form-control" required name="password" id="password" value="${baseUser?.password}" autocomplete="false">
                        </div>
                        <div class="col-md-1">确认密码</div>
                        <div class="col-md-5">
                            <input type="password" class="form-control" required id="password2" autocomplete="false" value="${baseUser?.password}">
                        </div>
                        <div class="col-md-1">邮箱</div>
                        <div class="col-md-5">
                            <input type="email" class="form-control" name="email" value="${baseUser?.email}">
                        </div>
                        <div class="col-md-1">可维护站点及栏目</div>
                        <div class="col-md-5">
                            <input type="hidden" class="form-control" name="sitestr" value="${baseUser?.sitestr}">
                            <input type="hidden" class="form-control" name="catalogstr" value="${baseUser?.catalogstr}">
                            <input type="text" name="catalogName" readonly class="form-control" value="${catalogNames}" onclick="showCatalogMenuModal();"/>
                        </div>
                        <div class="col-md-1">角色</div>
                        <div class="col-md-5">
                            <g:set var="cuRole" value="[]"/>
                            <g:if test="${baseUser?.id}">
                                <g:set var="cuRole" value="${baseUser?.getAuthorities().collect {it.id}}"/>
                            </g:if>
                            <ul class="row">
                            <g:each in="${BaseRole.list([sort:'name', order:'asc'])}" var="role">
                                <li class="col-md-6">
                                    <input name="roles" type="checkbox" value="${role.id}" <g:if test="${cuRole?.contains(role.id)}">checked</g:if> />${role?.name}
                                </li>
                            </g:each>
                            </ul>
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
<g:render template="/catalog/catalogTreeModal" model="['usage':'baseUser','baseUser':baseUser,'checkbox':true]"></g:render>
<g:render template="js"/>
</body>
</html>