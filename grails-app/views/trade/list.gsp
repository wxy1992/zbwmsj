<%@ page import="com.wmsj.core.Organization" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <g:if test="${!request.xhr}">
        <meta name="layout" content="main">
    </g:if>
    <style>
        .dropdown-menu li a{
            font-size: 14px;
            line-height: 36px;
            color: #006eec;
        }
    </style>
</head>
<body>
<div class="row">
    <div class="col-lg-12">
        <div class="card">
            <div class="row form-control-inline">
                <sec:ifAnyGranted roles="ROLE_ADMIN">
                    <div class="col-lg-4">
                        <g:select from="${com.wmsj.core.Organization.list([sort: 'id'])}" optionKey="id" optionValue="name"
                                  class="form-control" name="organization" noSelection="['':'发单机构']"></g:select>
                    </div>
                </sec:ifAnyGranted>

                <div class="col-lg-2">
                    <g:select from="${com.wmsj.business.Trade.STATUSMAP}" optionKey="key" optionValue="value"
                              class="form-control" name="status" noSelection="['':'状态']"></g:select>
                </div>
                <div class="col-lg-2">
                    <input type="text" class="form-control" id="title" name="title" placeholder="标题">
                </div>
                <div class="col-lg-4 text-right">
                    <button class="btn btn-info" type="button" onclick="doSearch();"><i class="glyphicon glyphicon-search"></i> 搜索</button>
                    <div class="btn-group">
                        <button type="button" class="btn btn-primary"> 发单 </button>
                        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                            <g:each in="${tradeTypes}" var="type">
                                <li><a href="javascript://" onclick="createOrEdit('','${type?.id}','${type?.name}');">${type?.name}</a></li>
                            </g:each>
                        </ul>
                    </div>
                    <sec:ifAllGranted roles="ROLE_SUBADMIN">
                        <button class="btn btn-success" type="button" onclick="changeTradeStatus('提交');"><i class="glyphicon glyphicon-ok"></i> 提交</button>
                    </sec:ifAllGranted>
                    <sec:ifAnyGranted roles="ROLE_ADMIN">
                        <button class="btn btn-success" type="button" onclick="changeTradeStatus('发布');"><i class="glyphicon glyphicon-ok"></i> 发布</button>
                        <button class="btn btn-danger" type="button" onclick="changeTradeStatus('退回');"><i class="glyphicon glyphicon-ban-circle"></i> 退回</button>
                    </sec:ifAnyGranted>
                </div>
            </div>
            <div class="row form-control-inline">
            <div class="col-md-12">
                <table id="tradeTable" data-toggle="table" data-url="${request.contextPath}/trade/json?operation=${params.operation}" data-pagination="true"
                       data-id-field="id"  data-unique-id="id" data-sort-order="desc" data-show-columns="false"
                       class="table table-striped table-hover bootstrapTable" data-side-pagination="server" data-cache="false"
                       data-query-params="queryParams">
                    <thead>
                    <tr>
                        <th data-checkbox="true"></th>
                        <th data-align="center" data-field="title" data-width="300">标题</th>
                        <th data-align="center" data-field="organization">发单机构</th>
                        <th data-align="center" data-field="typeName">服务类型</th>
                        <th data-align="center" data-field="statusName">状态</th>
                        <g:if test="${params.operation=='todo'}">
                            <th data-align="center" data-field="backreason">修改意见</th>
                        </g:if>
                        <th data-align="center" data-field="beginDate">开始时间</th>
                        <th data-align="center" data-field="endDate">结束时间</th>
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
