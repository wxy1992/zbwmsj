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
                <div class="col-lg-6">
                    <input type="text" class="form-control" id="createdBy" placeholder="评论人">
                </div>
                <div class="col-lg-6 text-right">
                    <button class="btn btn-info" type="button" onclick="doSearch();"><i class="glyphicon glyphicon-search"></i>搜索</button>
                    <button class="btn btn-success" type="button" onclick="auditCommentary('1');">审核通过</button>
                    <button class="btn btn-danger" type="button" onclick="auditCommentary('2');">审核不通过</button>
                </div>
            </div>
            <div class="row form-control-inline">
            <div class="col-md-12">
                <table id="commentaryTable" data-toggle="table" data-url="${request.contextPath}/commentary/json?tradeId=${params.tradeId}" data-pagination="true"
                       data-id-field="id"  data-unique-id="id" data-sort-order="desc" data-show-columns="false"
                       class="table table-striped table-hover bootstrapTable" data-side-pagination="server" data-cache="false"
                       data-query-params="queryParams">
                    <thead>
                    <tr>
                        <th data-checkbox="true"></th>
                        <th data-align="center" data-field="createdBy" data-width="300">评论人</th>
                        <th data-align="center" data-field="score">评分</th>
                        <th data-align="center" data-field="content">内容</th>
                        <th data-align="center" data-field="dateCreated">时间</th>
                        <th data-align="center" data-field="state" data-formatter="approvedFormatter">审核状态</th>
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
