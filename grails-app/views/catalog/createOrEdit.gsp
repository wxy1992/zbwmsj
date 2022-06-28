<style>
.basicAttributes, .advanceAttributes {
    border-bottom: 1px solid #ededed;
    margin-bottom: 10px;
}

.btnSave {
    margin-top: 20px;
}
</style>

<form id="catalogCreate" method="post">
    <input type="hidden" name="site.id" value="${site?.id}"/>
    <input type="hidden" name="catalogId" value="${catalogInstance?.id}"/>

    <div class="row">
        <div class="col-lg-12 basicAttributes"><span class="hidden-sm-up"><i class="glyphicon glyphicon-edit"></i>
        </span> <span class="hidden-xs-down">基本属性</span></div>

        <div class="col-lg-2" align="right">站点名称</div>

        <div class="col-lg-4" align="left">
            <input type="text" class="form-control input-default" readonly
                                                  value="${site?.companyName}"/></div>

        <div class="col-lg-2" align="right">栏目代号</div>

        <div class="col-lg-4" align="left"><input type="text" class="form-control input-default" readonly
                                                  value="${catalogInstance ? catalogInstance.id : '系统自动生成'}"/></div>

        <div class="col-lg-2" align="right">栏目名称</div>

        <div class="col-lg-4"><input type="text" class="form-control input-default" required name="name"
                                     value="${catalogInstance?.name}"/></div>

        <div class="col-lg-2" align="right">上级栏目</div>

        <div class="col-lg-4">
            <input type="hidden" class="form-control" id="parentId" name="parent.id"
                   value="${catalogInstance?.parentId}">
            <input type="text" name="parentName" readonly class="form-control" value="${catalogInstance?.parent?.name}"
                   onclick="showCatalogMenuModal();"/>
        </div>

        <div class="col-lg-2" align="right">列表模板</div>

        <div class="col-lg-4"><input type="text" class="form-control input-default" name="templateList"
                                     value="${catalogInstance?.templateList}"/></div>

        <div class="col-lg-2" align="right">内容模板</div>

        <div class="col-lg-4"><input type="text" class="form-control input-default" name="templateDetail"
                                     value="${catalogInstance?.templateDetail}"/></div>

        <div class="col-lg-2" align="right">排序号</div>

        <div class="col-lg-4"><input type="number" class="form-control input-default" name="sequencer"
                                     value="${catalogInstance?.sequencer}"/></div>

    </div>

    <div class="row">
        <div class="col-lg-12 advanceAttributes"><span class="hidden-sm-up"><i
                class="glyphicon glyphicon-new-window"></i></span> <span class="hidden-xs-down">高级属性</span></div>

        <div class="col-lg-2" align="right">需要审核</div>

        <div class="col-lg-4">
            <div class="row">
                <div class="col-lg-6 pt-0 pb-0"><label><input type="radio" name="needPreview" value="true"
                                                              <g:if test="${catalogInstance?.needPreview}">checked</g:if>/> 是</label></div>

                <div class="col-lg-6 pt-0 pb-0"><label><input type="radio" name="needPreview" value="false"
                                                              <g:if test="${!catalogInstance?.needPreview}">checked</g:if>/> 否</label></div>
            </div>
        </div>


        <div class="col-lg-2" align="right">是否启用</div>

        <div class="col-lg-4">
            <div class="row">
                <div class="col-lg-6 pt-0 pb-0"><label><input type="radio" name="enabled" value="true"
                                                              <g:if test="${catalogInstance?.enabled}">checked</g:if> /> 是</label></div>
                <div class="col-lg-6 pt-0 pb-0"><label><input type="radio" name="enabled" value="false"
                                                              <g:if test="${!catalogInstance?.enabled}">checked</g:if> /> 否</label></div>
            </div>
        </div>

        <div class="col-lg-2" align="right">首页展示</div>

        <div class="col-lg-4">
            <div class="row">
                <div class="col-lg-6 pt-0 pb-0"><label><input type="radio" name="positions" value="1"
                                                              <g:if test="${catalogInstance?.positions}">checked</g:if>>是
                </label></div>

                <div class="col-lg-6 pt-0 pb-0"><label><input type="radio" name="positions" value="0"
                                                              <g:if test="${!catalogInstance?.positions}">checked</g:if>>否
                </label></div>
            </div>
        </div>

        <div class="col-lg-2" align="right">作为详情展示</div>

        <div class="col-lg-4">
            <div class="row">
                <div class="col-lg-6 pt-0 pb-0"><label><input type="radio" name="showFirst" value="true"
                                                              <g:if test="${catalogInstance?.showFirst}">checked</g:if>>是
                </label></div>

                <div class="col-lg-6 pt-0 pb-0"><label><input type="radio" name="showFirst" value="false"
                                                              <g:if test="${!catalogInstance?.showFirst}">checked</g:if>>否
                </label></div>
            </div>
        </div>

        <div class="col-lg-2" align="right">显示日期、来源和作者</div>

        <div class="col-lg-4">
            <div class="row">
                <div class="col-lg-6 pt-0 pb-0"><label><input type="radio" name="showTimeAuthor" value="true"
                                                       <g:if test="${catalogInstance?.showTimeAuthor}">checked</g:if>>是</label>
                </div>

                <div class="col-lg-6 pt-0 pb-0"><label><input type="radio" name="showTimeAuthor" value="false"
                                                       <g:if test="${!catalogInstance?.showTimeAuthor}">checked</g:if>>否</label>
                </div>
            </div>
        </div>

        <div class="col-lg-2" align="right">不显示子栏目信息</div>

        <div class="col-lg-4">
            <div class="row">
                <div class="col-lg-6 pt-0 pb-0"><label><input type="radio" name="nochildren" value="true"
                                                       <g:if test="${catalogInstance?.nochildren}">checked</g:if>>是</label>
                </div>

                <div class="col-lg-6 pt-0 pb-0"><label><input type="radio" name="nochildren" value="false"
                                                       <g:if test="${!catalogInstance?.nochildren}">checked</g:if>>否</label>
                </div>
            </div>
        </div>

        <div class="col-lg-2" align="right">允许评论收藏</div>

        <div class="col-lg-4">
            <div class="row">
                <div class="col-lg-6 pt-0 pb-0"><label><input type="radio" name="allowComment" value="true"
                                                       <g:if test="${catalogInstance?.allowComment}">checked</g:if>>是</label>
                </div>

                <div class="col-lg-6 pt-0 pb-0"><label><input type="radio" name="allowComment" value="false"
                                                       <g:if test="${!catalogInstance?.allowComment}">checked</g:if>>否</label>
                </div>
            </div>
        </div>

    </div>

    <div class="row">
        <div class="col-lg-12 text-center btnSave">
            <button type="button" class="btn btn-primary" onclick="saveOrUpdate();"><span class="pd">确定</span></button>
            <button type="button" class="btn btn-info" onclick="backToList(${site?.id})"><span class="pd">返回</span>
            </button>
        </div>
    </div>
</form>
<script type="text/javascript">
    $(function () {
        $('#catalogCreate').validate({
            rules: {
                name: {required: true}
            }
        });
    });

    function saveOrUpdate() {
        if ($("#catalogCreate").valid()) {
            var catalogForm = document.getElementById('catalogCreate');
            var formData = new FormData(catalogForm);
            $.ajax({
                url: '${request.contextPath}/catalog/saveOrUpdate',
                dataType: 'json',
                type: 'POST',
                data: formData,
                processData: false, // 使数据不做处理
                contentType: false, // 不要设置Content-Type请求头
                success: function (data) {
                    alert(data.message);
                    if (data.result == true) {
                        setTimeout(function () {
                            backToList(${site?.id})
                        }, 2000);
                    }
                },
                error: function (response) {
                    alert(response);
                }
            });
        }
    }

</script>
<g:render template="/catalog/catalogTreeModal" model="['usage':'catalogParent','checkbox':false]"></g:render>
