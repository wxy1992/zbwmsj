<%@ page import="com.wmsj.cms.Catalog" %>
<div class="row">
    <div class="col-lg-12">
        当前位置：${raw(Catalog.getMapName(currentCatalog,[]).collect{
    "<a href='${request.contextPath}/news/top/${it.id}.html'>${it.name}</a>"}.join('>'))}
    </div>
</div>
