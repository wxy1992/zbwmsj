<%@ page import="com.bjrxkj.core.Organization" contentType="text/html;charset=UTF-8" %>
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
                <div class="col-lg-12 text-right">
                    <sec:ifAllGranted roles="ROLE_SUBADMIN">
                        <button class="btn btn-success" type="button" onclick="changeNewsState('已提交');"><i class="glyphicon glyphicon-ok"></i> 提交</button>
                    </sec:ifAllGranted>
                    <sec:ifAnyGranted roles="ROLE_ADMIN">
                        <button class="btn btn-success" type="button" onclick="changeNewsState('发布');"><i class="glyphicon glyphicon-ok"></i> 发布</button>
                        <button class="btn btn-danger" type="button" onclick="changeNewsState('退回');"><i class="glyphicon glyphicon-ban-circle"></i> 退回</button>
                    </sec:ifAnyGranted>
                </div>
            </div>
            <div class="row form-control-inline">
                <div class="col-md-12">
                    <table id="newsTable" data-toggle="table" data-url="${request.contextPath}/newsAdmin/todoJson" data-pagination="true"
                           data-id-field="id"  data-unique-id="id" data-sort-order="desc" data-show-columns="false" data-page-size="20"
                           class="table table-striped table-hover bootstrapTable" data-side-pagination="server" data-cache="false"
                           data-query-params="queryParams" data-pagination-detail="true" data-pagination-h-align="right" data-pagination-detail-h-align="left">
                        <thead>
                        <tr>
                            <th data-checkbox="true"></th>
                            <th data-align="left" data-field="title" data-formatter="titleFormatter" data-width="350">标题</th>
                            <th data-align="center" data-field="catalogName" data-width="100" data-formatter="catalogFormatter">栏目</th>
                            <th data-align="center" data-field="organizationName" data-width="100">发布单位</th>
                            <th data-align="center" data-field="publishDate" data-width="120">发布时间</th>
                            <th data-align="center" data-field="state" data-width="100">状态</th>
                            <th data-align="center" data-field="backreason" data-width="200">修改意见</th>
                            <th data-align="center" data-field="id" data-width="150" data-formatter="optionFormatter">操作</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<g:render template="/newsAdmin/js"/>
</body>
</html>
