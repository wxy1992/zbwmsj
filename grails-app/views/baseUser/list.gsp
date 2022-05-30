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
                    <input type="text" class="form-control" id="username" placeholder="用户名">
                </div>
                <div class="col-lg-2">
                    <input type="text" class="form-control" id="realName" placeholder="姓名">
                </div>
                <div class="col-lg-3 text-right">
                    <button class="btn btn-info" type="button" onclick="doSearch();"><i class="glyphicon glyphicon-search"></i>搜索</button>
                    <button class="btn btn-primary" type="button" onclick="createOrEdit('');"><i class="glyphicon glyphicon-plus"></i>新增</button>
                    <button class="btn btn-danger" type="button" onclick="removeBaseUsers();"><i class="glyphicon glyphicon-trash"></i>批量删除</button>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <table id="userTable" data-toggle="table" data-url="${request.contextPath}/baseUser/json" data-pagination="true"
                           data-id-field="id"  data-unique-id="id" data-sort-order="desc" data-show-columns="false"
                           class="table table-striped table-hover bootstrapTable" data-side-pagination="server" data-cache="false"
                           data-query-params="queryParams">
                        <thead>
                        <tr>
                            <th data-checkbox="true"></th>
                            <th data-align="center" data-field="realName" data-width="200">姓名</th>
                            <th data-align="center" data-field="username">用户名</th>
                            <th data-align="center" data-field="roleName">角色</th>
                            <th data-align="center" data-field="enabled" data-formatter="booleanFormatter">启用/禁用</th>
                            <th data-align="center" data-field="site.name">所属站点</th>
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