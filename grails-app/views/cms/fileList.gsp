<div class="row">
    <div class="col-lg-12">
        <div class="card">
            <div class="row form-control-inline">
                <div class="col-lg-1">
                    文件名
                </div>
                <div class="col-lg-3">
                    <input type="hidden" id="path" value="${params.path}">
                    <input name="filename" class="form-control" type="text">
                </div>
                <div class="col-lg-8 text-right">
                    <button class="btn btn-info" type="button" onclick="doSearch();"><i class="glyphicon glyphicon-search"></i>搜索</button>
                    <button class="btn btn-primary" type="button" onclick="uploadFiles();"><i class="glyphicon glyphicon-open-file"></i>上传</button>
                    <div class="btn-group">
                        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">新建<span class="caret"></span></button>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="javascript:createNewFile('directory','${params.path}');" title="文件夹">文件夹</a></li>
                            <li><a href="javascript:createNewFile('file','${params.path}');" title="文件">文件</a></li>
                        </ul>
                    </div>
                    <div class="btn-group">
                        <button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown"> 拷贝/移动 <span class="caret"></span></button>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="javascript:moveOrCopyFile('copy');" title="拷贝">拷贝</a></li>
                            <li><a href="javascript:moveOrCopyFile('move');" title="移动">移动</a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="row form-control-inline">
                <div class="col-md-12">
                    <table id="fileListTable" data-toggle="table" data-url="${request.contextPath}/cms/webPageJson" data-pagination="true"
                           data-id-field="id"  data-unique-id="id" data-sort-order="desc" data-show-columns="false"
                           class="table table-striped table-hover bootstrapTable" data-side-pagination="server" data-cache="false"
                           data-query-params="fileListTableQueryParams">
                        <thead>
                        <tr>
                            <th data-checkbox="true"></th>
                            <th data-align="center" data-field="name" data-width="300">文件名</th>
                            <th data-align="center" data-field="type">文件类型</th>
                            <th data-align="center" data-field="updateTime">修改时间</th>
                            <th data-align="center" data-field="name"  data-width="200" data-formatter="optionFormatter">操作</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="uploadModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <form id="uploadFileForm" enctype="multipart/form-data">
                    <input type="file" name="uploadFile" class="form-control">
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="saveOrUploadFile('uploadFileForm',$('#path').val(),'upload');">上传</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>
<g:render template="fileTreeModal"/>
<script>
    $(function(){
        $('#fileListTable').bootstrapTable({escape:false});
    });
</script>