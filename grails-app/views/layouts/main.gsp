<%@ page import="com.wmsj.core.BaseUser" %>
<g:set var="cmsTitle" value="${grailsApplication.config.project.setting.name}"/>
<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="${cmsTitle}"/>
    </title>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/workspace/sidebar.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/workspace/style.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/workspace/fonts.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/js/bootstrap-table-1.11.0/bootstrap-table.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/js/jstree-3.2.1/dist/themes/default/style.min.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/js/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
    <g:render template="/layouts/jsMain"/>
    <g:layoutHead/>
</head>
<body>
<g:set var="currentUser" value="${BaseUser.findByUsername(sec.username())}"/>
<g:if test="${!(params.controller=='news'&&params.action=='createOrEdit')}">
    <div class="sidebar sidebar-hide-to-small sidebar-shrink sidebar-gestures">
        <div class="nano">
            <div class="nano-content">
                <g:render template="/layouts/roleMenu" model="[currentUser:currentUser]"/>
            </div>
        </div>
    </div>
    <div class="header">
        <div class="container-fluid">
            <div class="row headerTop">
                <div class="col-md-6">
                    <div class="hamburger sidebar-toggle">
                        <span class="line bg-white"></span>
                        <span class="line bg-white"></span>
                        <span class="line bg-white"></span>
                    </div>
                </div>
                <div class="col-md-6 text-right">
                    <div class="dropdown dib">
                        <div class="header-icon" data-toggle="dropdown">

                            <a href="javascript://" onclick="window.open('${request.contextPath}/')">
                                <i class="glyphicon glyphicon-home"></i>
                                <span>????????????</span>
                            </a>

                            <a href="javascript://" onclick="loadRemotePage('${request.contextPath}/cms/changePasswd')">
                                <i class="glyphicon glyphicon-wrench"></i>
                                <span>????????????</span>
                            </a>
                            <a class="user-avatar">
                            ${currentUser?.toString()}
                            </a>

                            <a href="javascript://" onclick="window.location.href = '${request.contextPath}/logout';">
                                <i class="glyphicon glyphicon-log-out"></i>
                                <span>??????</span>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</g:if>

<div class="content-wrap" >
    <div class="main">
%{--        <div class="alert alert-primary alert-dismissible" id="messageModal" role="alert" style="display: none;">--}%
%{--            <button type="button" class="close" onclick="javascript:$(this).parent().hide();"><span aria-hidden="true">&times;</span></button>--}%
%{--            <strong>??????!</strong> <span id="messageModalBody">...</span>--}%
%{--        </div>--}%
        <div class="container-fluid">
            <div class="" id="mainBodyDiv" style="min-height: 800px;">
                <g:layoutBody/>
            </div>
        </div>
    </div>
</div>


<!-- ???????????? -->
<div class="modal fade" id="messageModal" tabindex="-1" role="dialog" aria-labelledby="messageModalHead" aria-hidden="true">
    <div class="modal-dialog" style="width: 300px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true" ><i class="glyphicon glyphicon-remove"></i></span><span class="sr-only">Close</span></button>
            </div>
            <div class="modal-body" id="messageModalBody" style="text-align: center; height: 100px; line-height: 65px;">

            </div>

            <div class="modal-footer">
                <button class="btn btn-default margin" data-dismiss="modal" type="button">
                    <span class="glyphicon glyphicon-remove"></span>
                    ??????
                </button>
            </div>
        </div>
    </div>
</div>
%{--<script src="${request.contextPath}/js/workspace/jquery.nanoscroller.min.js" type="text/javascript"></script>--}%
%{--<script src="${request.contextPath}/js/workspace/sidebar.js" type="text/javascript"></script>--}%

</body>
</html>
