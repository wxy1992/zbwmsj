<%@ page import="com.bjrxkj.cms.Site" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <g:if test="${!request.xhr}">
        <meta name="layout" content="main">
    </g:if>
    <g:render template="webJs"/>
</head>
<body>
<div class="row">
    <div class="col-lg-12">
        <div class="card">
            <div class="row">
                <div class="col-lg-3">
                    <g:render template="/cms/fileTree"></g:render>
                </div>
                <div class="col-lg-9" id="fileListContent"></div>
            </div>

        </div>
    </div>
</div>

</body>
</html>