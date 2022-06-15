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
                    <input type="text" class="form-control" id="organizationName" placeholder="单位名称">
                </div>
                <div class="col-lg-2">
                    <input type="text" class="form-control" id="organizationShortName" placeholder="单位简称">
                </div>
                <div class="col-lg-3 text-right">
                    <button class="btn btn-info" type="button" onclick="doSearch();"><i class="glyphicon glyphicon-search"></i>搜索</button>
                    <button class="btn btn-primary" type="button" onclick="createOrEdit('');"><i class="glyphicon glyphicon-plus"></i>新增</button>
                </div>
            </div>
            <div class="row form-control-inline">
            <div class="col-md-12">
                <table id="organizationTable" data-toggle="table" data-url="${request.contextPath}/organization/json" data-pagination="true"
                       data-id-field="id"  data-unique-id="id" data-sort-order="desc" data-show-columns="false"
                       class="table table-striped table-hover bootstrapTable" data-side-pagination="server" data-cache="false"
                       data-query-params="queryParams">
                    <thead>
                    <tr>
                        <th data-align="center" data-field="name" data-width="300">单位名称</th>
                        <th data-align="center" data-field="shortName">单位简称</th>
                        <th data-align="center" data-field="parentName">上级单位</th>
                        <th data-align="center" data-field="id"  data-width="200" data-formatter="optionFormatter">操作</th>
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
