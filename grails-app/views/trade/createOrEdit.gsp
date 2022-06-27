<%@ page import="com.wmsj.business.Trade" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>
<body>
<style>
input[type="radio"], input[type="checkbox"] {
    box-sizing: border-box;
    padding: 0;
    zoom: 150%;
}
    .templatePic{
        display: inline-block;
        position: relative;
    }
.templatePic a{
    position: absolute;
    top: 3px;
    right: 3px;
    z-index: 9999;
    background: rgba(255,255,255,0.8);
    border-radius: 50%;
    width: 20px;
    text-align: center;
}
.templatePic a i{
    color: #343a40;
}
</style>

<div class="row">
    <div class="col-lg-12">
        <div class="card">
            <div class="col-md-12">
                <form id="tradeForm">
                    <input type="hidden" name="id" value="${trade?.id}"/>
                    <input type="hidden" name="operation" id="operation" value=""/>
                    <div class="row">
                        <div class="col-md-12">
                            <input type="text" class="form-control text-center font-weight-bold" name="title" required
                                   value="${trade?.title}" maxlength="200" placeholder="在此输入服务名称"/>
                        </div>
                        <div class="col-md-12">
                            <script id="content" name="content" type="text/plain" style="width:100%;height:360px;">${raw(trade?.content)}</script>
                        </div>
                        <div class="col-md-1">发单类型</div>
                        <div class="col-md-5">
                            <g:select from="${tradeTypes}" optionKey="id" optionValue="name"
                                      class="form-control" name="tradeType.id" noSelection="['':'请选择类型']"
                                      value="${trade?.tradeTypeId?:params?.typeId}"></g:select>
                        </div>
                        <div class="col-md-1">发单机构</div>
                        <div class="col-md-5">
                            <sec:ifAnyGranted roles="ROLE_ADMIN">
                                <g:select from="${com.wmsj.core.Organization.list([sort: 'id'])}" optionKey="id" optionValue="name"
                                          class="form-control" name="organization.id"
                                          value="${trade?.organizationId}"></g:select>
                            </sec:ifAnyGranted>
                            <sec:ifAllGranted roles="ROLE_SUBADMIN">
                                <input type="text" disabled class="form-control" maxlength="40" value="${trade?.organization?.name?:user?.organization?.name}">
                            </sec:ifAllGranted>
                        </div>
                        <div class="col-md-1">服务方式</div>
                        <div class="col-md-5">
                            <g:select from="${Trade.WAYMAP}" optionKey="key" optionValue="value"
                                      class="form-control" name="way" value="${trade?.way?:1}"></g:select>
                        </div>
                        <div class="col-md-1">服务地址</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="address" required maxlength="40" value="${trade?.address}">
                        </div>
                        <div class="col-md-1">人数设置</div>
                        <div class="col-md-5">
                            <input type="peopleNum" class="form-control" name="peopleNum" value="${trade?.peopleNum}" required min="1">
                        </div>
                        <div class="col-md-1">联系方式</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="telephone" required maxlength="40" value="${trade?.telephone}">
                        </div>
                        <div class="col-md-1">报名开始时间</div>
                        <div class="col-md-5">
                            <input type="text" readonly class="form-control datetimepicker" name="beginDate" required
                            value="${(trade?.beginDate?:today)?.format('yyyy-MM-dd HH:mm')}"/>
                        </div>
                        <div class="col-md-1">报名结束时间</div>
                        <div class="col-md-5">
                            <input type="text" readonly class="form-control datetimepicker" name="endDate" required
                                   value="${(trade?.endDate?:(today+10))?.format('yyyy-MM-dd HH:mm')}"/>
                        </div>

                        <div class="col-md-12 text-center">
                            <button class="btn btn-primary" type="button" onclick="saveOrUpdate('草稿');">保存草稿</button>
                            <button class="btn btn-primary" type="button" onclick="saveOrUpdate('发布');">提交发布</button>
                            <button class="btn btn-info" type="button" onclick="backToList();">返回</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<g:render template="/newsAdmin/ueditor_config"/>
<script>
    var uecontent = UE.getEditor('content', {
        toolbars: [[
            'selectall','autotypeset','formatmatch','pasteplain', 'removeformat','cleardoc','|','undo', 'redo', '|',
            'fontfamily', 'fontsize','bold','italic', 'underline','strikethrough', 'superscript', 'subscript', 'fontborder','|',
            'forecolor', 'backcolor','|','insertorderedlist', 'insertunorderedlist','insertframe','|',
            'rowspacingtop', 'rowspacingbottom', 'lineheight', '|','customstyle', 'paragraph','indent', '|',
            'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|',
            'link', 'unlink', '|',
            'simpleupload', 'insertimage', 'insertvideo','|',
            'horizontal', 'date', 'time', 'spechars', '|','preview','fullscreen', 'source','searchreplace', '|',
            'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols'
        ]],
        autoHeightEnabled: true,
        autoFloatEnabled: true
    });
    uecontent.ready(function() {
        uecontent.setHeight(400);
    });
</script>
<g:render template="js"/>
</body>
</html>
