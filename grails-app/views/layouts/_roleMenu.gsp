<%@ page import="com.bjrxkj.cms.Site" %>
<ul id="rolemenuUl">
    <div class="logo"><a><span>${cmsTitle}</span></a></div>

    <li class="label">待办事项</li>
    <li><a onclick="loadRemotePage('${request.contextPath}/newsAdmin/list')">
        <i class="glyphicon glyphicon-edit"></i> 稿件 </a></li>
    <li><a onclick="loadRemotePage('${request.contextPath}/newsAdmin/list')">
    <i class="glyphicon glyphicon-share"></i> 点单 </a></li>

    <li class="label">采编/运维</li>
    <li><a onclick="loadRemotePage('${request.contextPath}/newsAdmin/list')"><i class="glyphicon glyphicon-duplicate"></i> 新闻管理 </a></li>
    <sec:ifAnyGranted roles="ROLE_ADMIN">
        <li><a onclick="loadRemotePage('${request.contextPath}/catalog/list')"><i class="glyphicon glyphicon-th-list"></i> 栏目管理 </a></li>
        <li><a onclick="loadRemotePage('${request.contextPath}/site/list')"><i class="glyphicon glyphicon-home"></i> 站点管理 </a></li>
    </sec:ifAnyGranted>
    <li><a onclick="loadRemotePage('${request.contextPath}/trade/list')"><i class="glyphicon glyphicon-list-alt"></i> 点单管理 </a></li>


    <sec:ifAnyGranted roles="ROLE_ADMIN">
        <li class="label">资源库</li>
        <li><a onclick="loadRemotePage('${request.contextPath}/newsAdmin/pictureList')"><i class="glyphicon glyphicon-picture"></i> 图片 </a></li>
        <li><a onclick="loadRemotePage('${request.contextPath}/attachment/list?type=2')"><i class="glyphicon glyphicon-film"></i> 视频 </a></li>
        <li><a onclick="loadRemotePage('${request.contextPath}/attachment/list?type=1')"><i class="glyphicon glyphicon-file"></i> 附件 </a></li>

        <li class="label">系统</li>
        <li><a onclick="loadRemotePage('${request.contextPath}/baseUser/list')"><i class="glyphicon glyphicon-user"></i> 用户 </a></li>
        <li><a onclick="loadRemotePage('${request.contextPath}/baseRole/list')"><i class="glyphicon glyphicon-asterisk"></i> 角色 </a></li>
        <li class="label">统计</li>
        <li><a onclick="loadRemotePage('${request.contextPath}/chart/newsVisitBarChart')"><i class="glyphicon glyphicon-signal"></i> 访问量 </a></li>
    </sec:ifAnyGranted>
</ul>
