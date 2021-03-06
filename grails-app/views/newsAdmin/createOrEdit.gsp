<%@ page import="com.wmsj.cms.News; grails.plugin.springsecurity.SpringSecurityUtils" %>
<g:render template="ueditor_config"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/workspace/newsForm.css"/>
<script type="text/javascript">
    $(function() {
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
        var ueoutline = UE.getEditor('outline', {
            toolbars: [[
                'selectall','autotypeset','formatmatch','pasteplain', 'removeformat','cleardoc','|',
                'fontfamily', 'fontsize','bold','italic', 'underline','strikethrough', '|',
                'customstyle', 'paragraph','indent', '|',
                'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify','source'
            ]],
            autoHeightEnabled: true,
            autoFloatEnabled: true
        });
        ueoutline.ready(function() {
            ueoutline.setHeight(200);
        });
    });
</script>
<form method="post" id="newsForm" enctype="multipart/form-data">
    <div class="row">
        <div class="col-lg-12">
            <ul class="nav nav-tabs" role="tablist">
                <li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#basic" role="tab"><span
                        class="hidden-sm-up"><i class="glyphicon glyphicon glyphicon-file"></i></span> <span
                        class="hidden-xs-down">????????????</span></a></li>
                <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#profile" role="tab"><span
                        class="hidden-sm-up"><i class="glyphicon glyphicon-new-window"></i></span> <span
                        class="hidden-xs-down">????????????</span></a></li>
                <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#messages" role="tab"><span
                        class="hidden-sm-up"><i class="glyphicon glyphicon-floppy-disk"></i></span> <span
                        class="hidden-xs-down">???????????????</span></a></li>
            </ul>

            <div class="tab-content tabcontent-border">
                <div class="tab-pane p-20 active" id="basic" role="tabpanel">
                    <div class="row">
                        <div class="col-lg-12">
                            <input type="hidden" name="catalog.id" class="catalogId" value="${newsInstance?.catalogId?:params['catalog.id']}"/>
                            <input type="hidden" name="id" value="${newsInstance?.id}"/>
                            <input type="hidden" name="state" value="${newsInstance?.state}"/>
                            <div class="row">
                                <div class="col-lg-12">
                                    <input type="text" class="form-control text-center font-weight-bold" name="title"
                                           value="${newsInstance?.title}" maxlength="200" placeholder="????????????????????????"/>
                                </div>

                                <div class="col-lg-12">
                                    <input type="text" class="form-control text-center font-weight-bolder" name="subtitle"
                                           value="${newsInstance?.subtitle}" maxlength="200" placeholder="?????????????????????"/>
                                </div>
                            </div>
                            <div class="row form-control-inline">
                                <div class="col-lg-4">
                                    ?????? <input type="text" class="form-control-inline" name="source" value="${newsInstance?.source}"
                                              maxlength="50"/>
                                </div>

                                <div class="col-lg-4">
                                    ?????? <input type="text" class="form-control-inline" name="author" value="${newsInstance?.author}"
                                              maxlength="50"/>
                                </div>

                                <div class="col-lg-4">
                                    ?????? <input type="text" readonly class="form-control-inline datetimepicker" name="publishDate"
                                              value="${newsInstance?.publishDate?.format('yyyy-MM-dd HH:mm')}"/>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-12">
                                    ?????????
                                    <div class="bg-light border pictureBox text-center">
                                        <g:if test="${newsInstance?.picture}">
                                            <img src="${request.contextPath}/cms/picture/${newsInstance?.id}"/>
                                        </g:if><g:else>
                                        <img src="${request.contextPath}/template/default.png">
                                        </g:else>
                                    </div>
                                    <input type="file" name="pictureFile" style="display: none;"
                                           accept="image/bmp,image/jpeg,image/png,image/jpg"/>
                                    <button type="button" class="btn btn-default glyphicon glyphicon-picture pictureUploadButton "></button>
                                    <p class="text-danger">
                                        ?????????<label id="pictureTypeLabel">${grailsApplication.config.project.setting.uploadImageExts}</label>?????????255*150??????
                                    </p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-12">
                                    <script id="content" name="content" type="text/plain" style="width:100%;height:360px;">${raw(newsInstance?.content)}</script>
%{--                                    <ueditor:editor id="content" style="width:100%;height:360px;">${newsInstance?.content}</ueditor:editor>--}%
%{--                                    <ckeditor:editor id="content" name="content" toolbar="Mytoolbar" userSpace="${user.username}">--}%
%{--                                        ${newsInstance?.content}--}%
%{--                                    </ckeditor:editor>--}%
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="tab-pane p-20" id="profile" role="tabpanel">
                    <div class="row">
                        <div class="col-lg-12">
                            ??????
                            <script id="outline" name="outline" type="text/plain" style="width:100%;height:360px;">${raw(newsInstance?.outline)}</script>
                        </div>

                        <div class="col-lg-12">
                            ?????????
                            <input type="text" class="form-control" name="keywords" value="${newsInstance?.keywords}"
                                   maxlength="100" />
                        </div>

                        <div class="col-lg-12">
                            ????????????
                            <input type="text" readonly class="form-control datetimepicker" name="expireDate"
                                   value="${newsInstance?.expireDate?.format('yyyy-MM-dd')}"/>
                        </div>

                        <div class="col-lg-12">
                            ????????????
                            <input type="text" class="form-control" name="redirectURL"
                                   value="${newsInstance?.redirectURL}" maxlength="200"/>
                        </div>
%{--                        <div class="col-lg-12">--}%
%{--                            ????????????--}%
%{--                            <g:select from="${News.constraints.seeRange.inList}" class="form-control" name="seeRange"--}%
%{--                                      value="${newsInstance?.seeRange}"></g:select>--}%
%{--                        </div>--}%
                        <div class="col-lg-12">
                            ?????????
                            <input type="number" min="0" class="form-control" name="sequencer"
                                   value="${newsInstance?.sequencer}"/>
                        </div>
                        <div class="col-lg-12">
                            ??????????????????
                            <div class="row">
                                <div class="col-md-12">
                                    <input type="radio" name="allowComment" value="true"
                                           <g:if test="${newsInstance?.allowComment}">checked</g:if>>???

                                    <input type="radio" name="allowComment" value="false"
                                           <g:if test="${!newsInstance?.allowComment}">checked</g:if>>???
                                </div>
                            </div>

                        </div>
                    </div>
                </div>

                <div class="tab-pane p-20" id="messages" role="tabpanel">
                    <div class="row">
                        <div class="col-lg-12">
                            <label class="text-danger">?????????????????????????????????????????????????????????????????????????????????</label>
                        </div>
                    </div>

                    <div class="row">

                        <div class="col-lg-12">
                            <div class="row bg-light border">
                                <div class="col-lg-12 text-right">
                                    <input type="file" name="videoFile" style="display: none;"/>
                                    <button type="button"
                                            class="btn btn-primary glyphicon glyphicon-film videoUploadButton"></button>

                                    <div class="progress m-t-20">
                                        <div class="progress-bar bg-warning" id="videoProcessBar" style="width: 0%;"
                                             role="progressbar">????????????</div>
                                    </div>
                                </div>
                            </div>
                            <div id="videoUploadFileNameDiv">
                                <g:if test="${newsInstance?.id}">
                                    <g:set var="videoList" value="${newsInstance?.attachments(2)}"/>
                                    <g:each in="${videoList}" var="a">
                                        <div class="row attachmentRow${a.id} bg-light border">
                                            <div class="col-lg-9">
                                                <input type="hidden" name="attachmentId" value="${a.id}"/>${a.name}</div>

                                            <div class="col-lg-3 text-right">
                                                <button type="button" class="btn btn-warning glyphicon glyphicon-save"
                                                        onclick="downloadAttachment('${a.id}');"></button>
                                                <button type="button" class="btn btn-info glyphicon glyphicon-eye-open"
                                                        onclick="previewVideo('#previewVideoBox','${a.id}');"></button>
                                                <button type="button" class="btn btn-danger glyphicon glyphicon-trash"
                                                        onclick="removeAttachment('${a.id}');"></button>
                                            </div>
                                        </div>
                                    </g:each>
                                </g:if>
                            </div>
                            ?????????????????????<label class="text-danger" id="videoTypeLabel">${grailsApplication.config.project.setting.uploadVideoExts}</label>
                            <div class="col-md-12 text-center" id="previewVideoBox">

                            </div>
                        </div>

                        <div class="col-lg-12">
                            <div class="row bg-light border">
                                <div class="col-lg-12 text-right">
                                    <input type="file" name="attachmentFile" style="display: none;"/>
                                    <button type="button"
                                            class="btn btn-primary glyphicon glyphicon-open attachmentBtn"></button>

                                    <div class="progress m-t-20">
                                        <div class="progress-bar bg-warning" id="attchmentProcessBar" style="width: 0%;"
                                             role="progressbar">????????????</div>
                                    </div>
                                </div>
                            </div>

                            <div id="attachmentUploadFileNameDiv">
                                <g:if test="${newsInstance?.id}">
                                    <g:set var="attachments" value="${newsInstance?.attachments(1)}"/>
                                    <g:each in="${attachments}" var="a" status="i">
                                        <div class="row attachmentRow${a.id} bg-light border">
                                            <div class="col-lg-9">
                                                <input type="hidden" name="attachmentId" value="${a.id}"/>${a?.sq?:i}???${a.name}</div>

                                            <div class="col-lg-3 text-right">
                                                <button type="button" class="btn btn-warning glyphicon glyphicon-save"
                                                        onclick="downloadAttachment('${a.id}');"></button>

                                                <button type="button" class="btn btn-danger glyphicon glyphicon-trash"
                                                        onclick="removeAttachment('${a.id}');"></button>
                                            </div>
                                        </div>
                                    </g:each>
                                </g:if>
                            </div>
                            ?????????????????????<label class="text-danger"
                                          id="attachmentTypeLabel">${grailsApplication.config.project.setting.uploadFileExts}</label>
                        </div>
                    </div>

                </div>
            </div>


        </div>

        <div class="col-lg-12 text-center">
            <button type="button" class="btn btn-primary" onclick="saveOrUpdate('??????',true);">??????</button>
            <button type="button" class="btn btn-primary" onclick="saveOrUpdate('??????',true);">??????</button>
            <g:if test="${params.operation=='attachmentList'}">
                <button type="button" class="btn btn-info" onclick="loadRemotePage('${request.contextPath}/attachment/list?type=${params.attachmentType}');">??????</button>
            </g:if><g:else>
            <button type="button" class="btn btn-info" onclick="backToList('${params['catalog.id']}','${newsInstance?.state}','${params.operation}','${params.pageNum}');">??????</button>
        </g:else>
        </div>
    </div>
</form>
<script type="text/javascript">
    $(function(){
        $('input[name=title]').focus();
        $('#newsForm').validate({
            rules: {
                title: {required: true,maxlength:200},
                'catalog.id': {checkCatalog: true},
                subtitle: {maxlength:200},
                source: {maxlength:50},
                author: {maxlength:50},
                keywords: {maxlength:100},
                outline: {maxlength:990},
                publishDate: {required: true},
                expireDate: {required: true},
                redirectURL: {maxlength: 200}
            }
        });

        // setInterval("saveOrUpdate('??????',false)",1000*20);
    });

    function saveOrUpdate(state,ifalert){
        if ($("#newsForm").valid()) {
            $('input[name=state]').val(state);
            var newsForm=document.getElementById('newsForm');
            var formData = new FormData(newsForm);
            $.ajax({
                url:'${request.contextPath}/newsAdmin/saveOrUpdate',
                dataType:'json',
                type:'POST',
                data: formData,
                processData : false, // ?????????????????????
                contentType : false, // ????????????Content-Type?????????
                success: function(data){
                    alert(data.message);
                    if(data.result){
                        backToList('${params['catalog.id']}','${newsInstance?.state}','${params.operation}','${params.pageNum}');
                    }
                },
                error:function(response){
                    alert(response);
                }
            });
        }
    }


    var pictureLimit = 10;
    var videoLimit = 1;
    var attachmentLimit = 10;
    var pictureType=$('label#pictureTypeLabel').text().split(',');
    var attachmentType=$('label#attachmentTypeLabel').text().split(',');
    var videoType=$('label#videoTypeLabel').text().split(',');

    $(function(){
        $('#catalogId').val('${newsInstance?.catalogId}')
        $( ".datetimepicker" ).datetimepicker({language: 'zh-CN',autoclose: true,todayBtn: true,format: 'yyyy-mm-dd hh:ii'});
        // ueoutline.ready(function() {});
        // var uessss = UE.getContent();
        // console.log(uessss);
        // uecontent.ready(function() {
        //     //????????????????????????
        //     ueoutline.setContent('hello');
        //     //??????html???????????????: <p>hello</p>
        //     var html = ue.getContent();
        //     //??????????????????????????????: hello
        //     var txt = ue.getContentTxt();
        // });
    });
    $('.pictureUploadButton').bind("click",function(){
        $(this).siblings('input[type=file]').click();
    });
    $('.attachmentBtn').bind("click",function(){
        attachmentProgress('0%');
        $(this).siblings('input[type=file]').click();
    });
    $('.videoUploadButton').bind("click",function(){
        videoProgress('0%');
        $(this).siblings('input[type=file]').click();
    });

    //??????
    $("input[name^=pictureFile]").change(function(){  // ??? id ??? file ????????????????????????
        var fileName = this.files[0].name;
        var extName = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length).toLowerCase();
        if (pictureType.indexOf(extName)<0) {
            alert('??????????????????????????????');
            this.value="";
            return false;
        }
        var fileSize = this.files[0].size;
        var size = fileSize / 1024 / 1024;
        if (size > pictureLimit) {
            alert('????????????????????????'+pictureLimit+'M,????????????????????????????????????');
            this.value="";
            return false;
        }
        var reader = new FileReader();
        reader.readAsDataURL(this.files[0]);
        reader.onloadend = function () {
            $('.pictureBox img').attr('src',reader.result);
        };
    });
    //??????
    $("input[name^=videoFile]").change(function(){  // ??? id ??? file ????????????????????????
        var fileName = this.files[0].name;
        var extName = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length).toLowerCase();
        if (videoType.indexOf(extName)<0) {
            alert('??????????????????????????????');
            this.value="";
            return false;
        }
        var fileSize = this.files[0].size;
        var size = fileSize / 1024 / 1024 /1024;
        if (size > videoLimit) {
            alert('????????????????????????'+videoLimit+'G,????????????????????????????????????');
            this.value="";
            return false;
        }
        uploadAttachment('videoFile','${newsInstance?.id}','2');
    });
    //????????????
    $("input[name^=attachmentFile]").change(function(){  // ??? id ??? file ????????????????????????
        var fileName = this.files[0].name;
        var extName = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length).toLowerCase();
        if (attachmentType.indexOf(extName)<0) {
            alert('??????????????????????????????');
            this.value="";
            return false;
        }
        var fileSize = this.files[0].size;
        var size = fileSize / 1024 / 1024 /1024;
        if (size > attachmentLimit) {
            alert('??????????????????'+attachmentLimit+'G,????????????????????????????????????');
            this.value="";
            return false;
        }
        uploadAttachment('attachmentFile','${newsInstance?.id}','1');
    });


    function removePicture(){
        if(window.confirm("?????????????????????????????????????????????")&&'${newsInstance?.id}') {
            $.post('${request.contextPath}/newsAdmin/deletePic', {id: '${newsInstance?.id}'}, function (data) {
                if (data.result) {
                    $('.pictureRow').remove();
                }
            }, 'json');
        }
    }


    //??????
    function videoProgress(num){
        $("#videoProcessBar").css("width",num);
        $("#videoProcessBar").text('?????????');
    }
    function attachmentProgress(num){
        $("#attchmentProcessBar").css("width",num);
        $("#attchmentProcessBar").text('?????????');
    }

    //??????????????????
    function uploadAttachment(fileInput,newsId,type){
        var sq = prompt('??????????????????', '1');
        if(!sq){
            sq=1;
        }
        var attachmentUploadFile=$('input[name='+fileInput+']')[0].files[0];
        var formData=new FormData();
        formData.append('file_data',attachmentUploadFile);
        formData.append('newsId',newsId);
        formData.append('type',type);
        formData.append('dataType','news');
        formData.append('sq',sq);
        $.ajax({
            url:'${request.contextPath}/attachment/fileUpload',
            dataType:'json',
            type:'POST',
            async:true,
            data: formData,
            processData : false, // ?????????????????????
            contentType : false, // ????????????Content-Type?????????
            xhr:function(){
                myXhr = $.ajaxSettings.xhr();
                if(myXhr.upload){ // check if upload property exists
                    myXhr.upload.addEventListener('progress',function(e){
                        var loaded = e.loaded;                  //????????????????????????
                        var total = e.total;                      //???????????????
                        var percent = Math.floor(100*loaded/total)+"%";     //????????????????????????
                        if(type=='2'){//??????
                            videoProgress(percent);
                        }else{
                            attachmentProgress(percent);
                        }
                    }, false); // for handling the progress of the upload
                }
                return myXhr;
            },
            success: function(data){
                if (data.status == 2) {
                    var fileid=data.file_id;
                    var file_name=attachmentUploadFile.name;
                    var buttonstr='<div class="row attachmentRow'+fileid+' bg-light border"><div class="col-lg-9"><input type="hidden" name="attachmentId" value="'+fileid+'"/>'+sq+'???'+file_name+'</div>' +
                        '<div class="col-lg-3 text-right"><button type="button" class="btn btn-warning glyphicon glyphicon-save" onclick="downloadAttachment(\''+fileid+'\');"></button> ';
                    if(type=='2'){
                        buttonstr+='<button type="button" class="btn btn-info glyphicon glyphicon-eye-open" onclick="previewVideo(\'#previewVideoBox\',\''+fileid+'\');"></button> ';
                    }
                    buttonstr+='<button type="button" class="btn btn-danger glyphicon glyphicon-trash" onclick="removeAttachment(\''+fileid+'\');"></button></div></div>';
                    if(type=='2'){
                        $('#videoUploadFileNameDiv').append(buttonstr);
                        $("#videoProcessBar").text('????????????');
                    }else{
                        $('#attachmentUploadFileNameDiv').append(buttonstr);
                        $("#attchmentProcessBar").text('????????????');
                    }
                    $('input[name='+fileInput+']').val(null);
                }
            },
            error:function(response){
                alert(response);
            }
        });
    }

    //?????????????????????
    function downloadAttachment(aid){
        window.location.href='${request.contextPath}/web/getFile?id='+aid;
    }
    function removeAttachment(aid){
        if(window.confirm("?????????????????????????????????????????????")){
            $.post('${request.contextPath}/attachment/deleteAttachment',{id:aid},function(data){
                if(data.result){
                    $('.attachmentRow'+aid).remove();
                }
            },'json');
        }
    }
    function previewVideo(boxname,vid){
        var str=[];
        str.push('<video width="600" height="400" controls="controls" autoplay="autoplay">')
        str.push('<source type="video/mp4" src="${request.contextPath}/web/getFile/'+vid+'" type="video/mp4"')
        str.push('</video>');
        $(boxname).html(str.join())
    }
    jQuery.validator.addMethod("checkCatalog", function(value, element) {
        if(!value){
            alert("?????????????????????????????????");
            return false;
        }else {
            return true;
        }
    }, "");

</script>

