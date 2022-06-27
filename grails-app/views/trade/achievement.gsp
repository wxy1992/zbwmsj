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
                <form id="achievementForm">
                    <input type="hidden" name="tradeId" value="${params.tradeId}"/>
                    <div class="row">
                        <div class="col-md-12">
                            <input type="text" class="form-control text-center font-weight-bold" name="title" required
                                   value="${achievement?.title?:"${trade?.title}服务成果"}" maxlength="200" placeholder="${}"/>
                        </div>
                        <div class="col-md-12">
                            <script id="content" name="content" type="text/plain" style="width:100%;height:360px;">${raw(achievement?.content)}</script>
                        </div>

                        <div class="col-md-12 text-center">
                            <button class="btn btn-primary" type="button" onclick="saveAchievement();">保存</button>
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
