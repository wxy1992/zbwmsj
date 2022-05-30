<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <g:if test="${!request.xhr}">
        <meta name="layout" content="main">
    </g:if>
</head>
<body>
<div class="row">
    <div class="col-lg-12">
        <div class="card">
            <div class="row form-control-inline">
                <div class="col-lg-2">
                    <input type="text" class="form-control" id="keywords" placeholder="关键字">
                </div>
                <div class="col-lg-3 text-right">
                    <button class="btn btn-info" type="button" onclick="doSearch();"><i class="glyphicon glyphicon-search"></i>搜索</button>
                    <button class="btn btn-primary" type="button" onclick="createOrEdit('');"><i class="glyphicon glyphicon-plus"></i>新增</button>
                    <button class="btn btn-danger" type="button" onclick="removeBaseRoles();"><i class="glyphicon glyphicon-trash"></i>批量删除</button>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <table id="roleTable" data-toggle="table" data-url="${request.contextPath}/baseRole/json" data-pagination="true"
                           data-id-field="id"  data-unique-id="id" data-sort-order="desc" data-show-columns="false"
                           class="table table-striped table-hover bootstrapTable" data-side-pagination="server" data-cache="false"
                           data-query-params="queryParams">
                        <thead>
                        <tr>
                            <th data-checkbox="true"></th>
                            <th data-align="center" data-field="name">名称</th>
                            <th data-align="center" data-field="authority">角色标识</th>
                            <th data-align="center" data-field="description">角色描述</th>
                            <th data-align="center" data-field="id" data-formatter="optionFormatter">操作</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<g:render template="js"/>
</body>
</html>