<%@ page import="com.bjrxkj.cms.Site" %>
<ul id="rolemenuUl">
    <div class="logo"><a href="${request.contextPath}/"><span>${cmsTitle}</span></a></div>

    <li class="label">待办事项</li>
    <li><a onclick="loadRemotePage('${request.contextPath}/trade/list?operation=todo')">
        <i class="glyphicon glyphicon-share"></i> 服务 </a></li>
    <li><a onclick="loadRemotePage('${request.contextPath}/newsAdmin/todoList')">
        <i class="glyphicon glyphicon-edit"></i> 稿件 </a></li>

    <li class="label">采编/运维</li>
    <li><a onclick="loadRemotePage('${request.contextPath}/trade/list')"><i class="glyphicon glyphicon-list-alt"></i> 服务管理 </a></li>
    <li><a onclick="loadRemotePage('${request.contextPath}/newsAdmin/list')"><i class="glyphicon glyphicon-duplicate"></i> 稿件管理 </a></li>
    <sec:ifAnyGranted roles="ROLE_ADMIN">
        <li><a onclick="loadRemotePage('${request.contextPath}/catalog/list')"><i class="glyphicon glyphicon-th-list"></i> 栏目管理 </a></li>
%{--        <li><a onclick="loadRemotePage('${request.contextPath}/site/list')"><i class="glyphicon glyphicon-home"></i> 站点管理 </a></li>--}%
    </sec:ifAnyGranted>

    <sec:ifAnyGranted roles="ROLE_ADMIN">
        <li class="label">系统</li>
        <li><a onclick="loadRemotePage('${request.contextPath}/baseUser/list')"><i class="glyphicon glyphicon-user"></i> 用户管理 </a></li>
        <li><a onclick="loadRemotePage('${request.contextPath}/baseRole/list')"><i class="glyphicon glyphicon-asterisk"></i> 角色管理 </a></li>
        <li><a onclick="loadRemotePage('${request.contextPath}/organization/list')"><i class="glyphicon glyphicon-home"></i> 单位管理 </a></li>
        <li class="label">统计</li>
        <li><a onclick="loadRemotePage('${request.contextPath}/chart/newsVisitBarChart')"><i class="glyphicon glyphicon-signal"></i> 访问量 </a></li>
    </sec:ifAnyGranted>
</ul>
