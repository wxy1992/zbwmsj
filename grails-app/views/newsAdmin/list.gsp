<%@ page import="com.bjrxkj.cms.Catalog" contentType="text/html;charset=UTF-8" %>
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
            <div class="row">
                <div class="col-lg-2 pl-0">
                    <g:render template="/layouts/menuTree" model='[type:"newsList",catalogInputId:".catalogId"]'></g:render>
                </div>
                <div class="col-lg-10" id="newsListTable"></div>
            </div>

        </div>
    </div>
</div>
<!-- 复制新闻栏目树 -->
<g:render template="/catalog/catalogTreeModal" model="['usage':'copyNews','checkbox':true]"></g:render>
<g:render template="js"/>

</body>
</html>
