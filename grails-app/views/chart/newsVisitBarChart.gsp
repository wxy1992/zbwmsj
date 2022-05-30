<%@ page import="com.bjrxkj.cms.Catalog" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>新闻管理</title>
    <g:if test="${!request.xhr}">
        <meta name="layout" content="main">
    </g:if>
    <script type="text/javascript" src="${request.contextPath}/js/workspace/echarts.js" />
</head>

<body>
<g:render template="newsVisitBarChart"/>
</body>
</html>
