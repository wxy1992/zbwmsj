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
                    <input type="text" class="form-control" id="siteName" name="siteName" placeholder="站点名称">
                </div>
                <div class="col-lg-2">
                    <input type="text" class="form-control" id="siteDomain" name="siteDomain" placeholder="域名">
                </div>
                <div class="col-lg-3 text-right">
                    <button class="btn btn-info" type="button" onclick="doSearch();"><i class="glyphicon glyphicon-search"></i>搜索</button>
                </div>
            </div>
            <div class="row form-control-inline">
                <div class="col-md-12">
                    <table id="siteTable" data-toggle="table" data-url="${request.contextPath}/chart/basicSiteInfoJson" data-pagination="true"
                           data-id-field="id"  data-unique-id="id" data-sort-order="desc" data-show-columns="false"
                           class="table table-striped table-hover bootstrapTable" data-side-pagination="server" data-cache="false"
                           data-query-params="queryParams">
                        <thead>
                        <tr>
                            <th data-align="center" data-field="companyName" data-width="300">站点名称</th>
                            <th data-align="center" data-field="name">站点代码</th>
                            <th data-align="center" data-field="domainName">域名</th>
                            <th data-align="center" data-field="adminEmail">站长信箱</th>
                            <th data-align="center" data-field="id"  data-width="200" data-formatter="optionFormatter">操作</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<g:render template="basicSiteJs"/>
</body>
</html>