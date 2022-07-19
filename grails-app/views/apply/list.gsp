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
                    <input type="text" class="form-control" id="name" placeholder="报名人">
                </div>
                <div class="col-lg-3">
                    <input type="text" class="form-control" id="telephone" placeholder="联系电话">
                </div>
                <div class="col-lg-3">
                    <input type="text" class="form-control" id="idcard" placeholder="身份证号">
                </div>
                <div class="col-lg-4 text-right">
                    <button class="btn btn-info" type="button" onclick="doSearch();"><i class="glyphicon glyphicon-search"></i> 搜索</button>
                    <button class="btn btn-success" type="button" onclick="changeApplyStatus('20');"><i class="glyphicon glyphicon-ok"></i> 完成</button>
                    <button class="btn btn-danger" type="button" onclick="changeApplyStatus('0');"><i class="glyphicon glyphicon-ban-circle"></i> 退单</button>
                    <button class="btn btn-info" type="button" onclick="backToTradeList();"><i class="glyphicon glyphicon-share-alt"></i> 返回</button>
                </div>
            </div>
            <div class="row form-control-inline">
            <div class="col-md-12">
                <table id="applyTable" data-toggle="table" data-url="${request.contextPath}/apply/json?tradeId=${params.tradeId}" data-pagination="true"
                       data-id-field="id"  data-unique-id="id" data-sort-order="desc" data-show-columns="false"
                       class="table table-striped table-hover bootstrapTable" data-side-pagination="server" data-cache="false"
                       data-query-params="queryParams">
                    <thead>
                    <tr>
                        <th data-checkbox="true"></th>
                        <th data-align="center" data-field="name">报名人</th>
                        <th data-align="center" data-field="idcard" data-width="300">身份证号</th>
                        <th data-align="center" data-field="telephone">手机号</th>
                        <th data-align="center" data-field="address" data-width="200">地址</th>
                        <th data-align="center" data-field="dateCreated">报名时间</th>
                        <th data-align="center" data-field="statusName">状态</th>
%{--                        <th data-align="center" data-field="id"  data-width="200" data-formatter="optionFormatter">操作</th>--}%
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
