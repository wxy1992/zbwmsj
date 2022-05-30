<!DOCTYPE html>
<html>
<head>
    <g:if test="${!request.xhr}">
        <meta name="layout" content="main">
    </g:if>
    <style>
        .fl{float:left;}
        .pd{padding-left:5px;}
        .f16{font-size:16px;}
    </style>
    <g:render template="js"/>
</head>
<body>
<div class="row">
    <div class="col-lg-12">
        <div class="card">
            <div class="row">
                <div class="col-lg-2 pl-0">
                    <g:render template="/layouts/menuTree" model='[type:"catalog"]'></g:render>
                </div>
                <div class="col-lg-10">
                    <div class="row">
                        <div class="col-md-12" id="catalogContentDiv">
                            <div class="row form-control-inline">
                                <div class="col-lg-7">
                                    <input type="text" class="form-control" name="keywords" placeholder="关键词">
                                </div>
                                <div class="col-lg-5 text-right">
                                    <button class="btn btn-info" type="button" onclick="doSearch();"><i class="glyphicon glyphicon-search"></i><span class="pd">搜索</span></button>
                                    <button class="btn btn-primary" type="button" onclick="createOrEditCatlog('',$('input[name=siteId]').val());"><i class="glyphicon glyphicon-plus"></i><span class="pd">新增</span></button>
                                    <button class="btn btn-danger" type="button" onclick="deleteAll();"><i class="glyphicon glyphicon-trash"></i><span class="pd">删除</span></button>
                                </div>
                            </div>
                            <div class="row">
                                <input type="hidden" name="siteId" class="bootstrap-table-search-keyword" value="${params.siteId}"/>
                                <div class="col-md-12">
                                    <table id="catalogListTable" data-toggle="table" data-url="${request.contextPath}/catalog/json" data-pagination="true"
                                           data-id-field="id"  data-unique-id="id" data-sort-order="desc" data-show-columns="false"
                                           class="table table-striped table-hover bootstrapTable" data-side-pagination="server" data-cache="false"
                                           data-query-params="queryParams" data-pagination-detail="true" data-pagination-h-align="left" data-pagination-detail-h-align="right">
                                        <thead>
                                        <tr>
                                            <th data-checkbox="true"></th>
                                            <th data-align="center" data-field="id">ID</th>
                                            <th data-align="center" data-field="name">栏目名称</th>
                                            <th data-align="center" data-field="parent">父级栏目</th>
                                            <th data-align="center" data-field="showIndex">展示位置</th>
                                            <th data-align="center" data-field="enabled">是否启用</th>
                                            <th data-align="center" data-field="id" data-formatter="optionFormatter">操作</th>
                                        </tr>
                                        </thead>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
