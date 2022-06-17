<%@ page import="sun.misc.BASE64Encoder; com.cms.utils.CommonUtils" %>
<form id="fileDetailForm" enctype="multipart/form-data">
    <div class="row">
        <div class="col-lg-12">
            <ul class="nav nav-tabs" role="tablist">
                <li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#basic" role="tab"><span
                        class="hidden-sm-up"><i class="glyphicon glyphicon glyphicon-file"></i></span> <span
                        class="hidden-xs-down">文件内容</span></a></li>
                <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#profile" role="tab"><span
                        class="hidden-sm-up"><i class="glyphicon glyphicon-new-window"></i></span> <span
                        class="hidden-xs-down">详细信息</span></a></li>
            </ul>

            <div class="tab-content tabcontent-border">
                <div class="tab-pane p-20 active" id="basic" role="tabpanel">
                    <div class="row">
                        <div class="col-lg-12">
                            <g:set var="extname" value="${CommonUtils.getFileExtention(file.name)}"/>
                            <g:if test="${['gsp','html','txt','css','js'].contains(extname)}">
                                <textarea name="filecontent" class="form-control bg-dark" rows="20" style="color: #dddddd;">
                                    ${file.text}
                                </textarea>
                            </g:if><g:elseif test="${['jpg','jpeg','png'].contains(extname)}">
                            <%
                                sun.misc.BASE64Encoder encoder = new BASE64Encoder();
                                def strNetImageToBase64 = encoder.encode(file.bytes);
                                // 删除 \r\n
                                strNetImageToBase64 = strNetImageToBase64.replaceAll("\n", "").replaceAll("\r", "");
                                strNetImageToBase64 = "data:image/jpeg;base64," + strNetImageToBase64;
%>
                            <div class="col-md-12 text-center bg-dark" style="border: 1px solid #f5f5f5;min-height: 300px;">
                                <img src="${strNetImageToBase64}" style="max-width: 100%;"/>
                            </div>
                        </g:elseif>
                        </div>

                        <div class="col-lg-12 text-center">
                            <button type="button" class="btn btn-primary" onclick="saveOrUploadFile('fileDetailForm',$('#fileDetailForm #filename').val(),'save');">保存</button>
                            <button type="button" class="btn btn-info"
                                    onclick="showFileDetail('','directory');">取消</button>
                        </div>
                    </div>
                </div>

                <div class="tab-pane p-20" id="profile" role="tabpanel">
                    <div class="col-md-12">
                        <div class="col-md-2 text-primary">路径</div>
                        <div class="col-md-12"><input id="filename" value="${raw(file.path)}" readonly class="form-control"/></div>
                    </div>

                    <div class="col-md-12">
                        <div class="col-md-2 text-primary">修改时间</div>
                        <div class="col-md-12">${new Date(file.lastModified()).format("yyyy-MM-dd HH:mm")}</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>

