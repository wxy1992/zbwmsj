<%@ page import="grails.plugin.springsecurity.SpringSecurityUtils" %>
<div class="row form-control-inline">
    <div class="col-lg-7">
        <input type="text" class="form-control" name="title" id="title" placeholder="按标题搜索">
        <input type="hidden" class="form-control" value="${params.state?:'发布'}" id="state" >
        <input type="hidden" class="form-control" value="${params.catalogId}" id="catalogId" >
    </div>
    <div class="col-lg-5 text-right">
        <button class="btn btn-info" type="button" onclick="doSearch();"><i class="glyphicon glyphicon-search"></i> 搜索</button>
        <sec:ifAnyGranted roles="ROLE_SUBADMIN,ROLE_ADMIN">
            <button class="btn btn-primary" type="button" onclick="createOrEdit('');"><i class="glyphicon glyphicon-plus"></i>新增</button>
        </sec:ifAnyGranted>
    </div>
</div>
    <div class="row">
        <div class="col-md-12" id="newsContentDiv">
            <div class="newsList">
                <div class="row">
                    <div class="col-md-7 changeStateButtonGroup">
                        <div class="btn-group">
                            <a href="javascript:searchNewsTable({'#state':'草稿'});" class="btn btn-info draft" title="草稿库">草稿库</a>
                            <a href="javascript:searchNewsTable({'#state':'发布'});" class="btn btn-info publish active" title="发布库">发布库</a>
                            <g:set var="isAudit" value="${catalog?.needPreview}"/>
                            <g:if test="${isAudit}">
                                <div class="btn-group">
                                    <button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown">审核库<span class="caret"></span></button>
                                    <ul class="dropdown-menu" role="menu">
                                        <li><a href="javascript:searchNewsTable({'#state':'已提交'});" class="audit" title="已提交">已提交</a></li>
                                        <li><a href="javascript:searchNewsTable({'#state':'退回'});" class="audit" title="退回">退回</a></li>
                                    </ul>
                                </div>
                            </g:if>
                            <a href="javascript:searchNewsTable({'#state':'回收站'});" class="btn btn-info refuse" title="回收站">回收站</a>
                        </div>
                    </div>
                    <div class="col-md-5 text-right">
                        <div class="btn-group btn-group-justified operateButtonGroup">
                        <!--审核库-->
                            <sec:ifAnyGranted roles="ROLE_ADMIN">
                                <button onclick="changeNewsState('发布');" class="btn btn-warning pubaudit"
                                        title="审核通过">审核通过</button>
                                <button onclick="changeNewsState('退回');" class="btn btn-danger pubaudit"
                                        title="退回">退回</button>
                            </sec:ifAnyGranted>

                            <sec:ifNotGranted roles="ROLE_AUDITOR,ROLE_PUBAUDITOR">
                                <!--草稿库-->
                                <g:if test="${catalog?.needPreview&&!SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN")}">
                                    <button onclick="changeNewsState('已提交');" class="btn btn-warning draft" title="已提交">提交</button>
                                </g:if><g:else>
                                <button onclick="changeNewsState('发布');" class="btn btn-warning draft" title="发布">发布</button>
                                </g:else>
                                <!--发布库-->
                                <button onclick="changeNewsState('草稿');" class="btn btn-warning publish" title="撤稿">撤稿</button>

                                <div class="btn-group draft publish">
                                    <button type="button" class="btn btn-info dropdown-toggle draft publish" data-toggle="dropdown">复制/移动<span class="caret"></span></button>
                                    <ul class="dropdown-menu" role="menu">
                                        <li><a href="javascript:showCopyPanel('copy');" class="btn btn-info w-100" title="复制">复制</a></li>
                                        <li> <hr> <input id="newsOperationType" type="hidden" ></li>
                                        <li><a href="javascript:showCopyPanel('move');" class="btn btn-info w-100" title="移动">移动</a></li>
                                    </ul>
                                </div>
                                <!--回收站-->
                                <button onclick="changeNewsState('草稿');" class="btn btn-warning refuse" title="还原">还原</button>
                                <button onclick="deleteAll();" class="btn btn-danger draft refuse" title="删除">删除</button>
                            </sec:ifNotGranted>
                        </div>
                    </div>
                </div>
                <table id="newsTable" data-toggle="table" data-url="${request.contextPath}/newsAdmin/json" data-pagination="true"
                       data-id-field="id"  data-unique-id="id" data-sort-order="desc" data-show-columns="false" data-page-size="20"
                       class="table table-striped table-hover bootstrapTable" data-side-pagination="server" data-cache="false"
                       data-query-params="queryParams" data-pagination-detail="true" data-pagination-h-align="right" data-pagination-detail-h-align="left">
                    <thead>
                    <tr>
                        <th data-checkbox="true"></th>
                        <th data-align="left" data-field="title" data-formatter="titleFormatter" data-width="350">标题</th>
                        <th data-align="center" data-field="sequencer" data-width="100">排序值</th>
                        <th data-align="center" data-field="publishDate" data-width="120">发布时间</th>
                        <th data-align="center" data-field="catalogName" data-width="100" data-formatter="catalogFormatter">栏目</th>
                        <th data-align="center" data-field="state" data-width="100">状态</th>
                        <th data-align="center" data-field="id" data-width="150" data-formatter="optionFormatter">操作</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
<script>
    $(function(){
        $('.bootstrapTable').bootstrapTable({escape:true});
        if('${params.pageNum}'){
            $('input.pageInput').val('${params.pageNum}');
            $('button.toPageNum').click();
        }
        operateGroup();
    });
    function queryParams(params){
        params.title=$("#title").val();
        params['catalog.id']=$("#catalogId").val();
        params.state=$("#state").val();
        params.max=params.limit;
        return params;
    }
    //控制列表查询显示
    function searchNewsTable(objJSON){
        for( var inputID in objJSON ){
            var inpVal=objJSON[inputID];
            $(inputID).val(inpVal);
        }
        doSearch();
    }

    //控制撤稿、复制等按钮显示
    function operateGroup(){
        if($('#state').val()){
            var stateVal=$('#state').val();
            var btns=buttonWithState[stateVal];
            $('.changeStateButtonGroup a').removeClass('active');
            $('.operateButtonGroup button').hide();
            $('.changeStateButtonGroup .'+btns).addClass('active');
            $('.operateButtonGroup .'+btns).show();
        }
    }

    //保存新闻
    function createOrEdit(id){
        var catalogId='${catalog?.id}';
        if(!id&&!catalogId){
            alert('请先选择左侧栏目');
            return;
        }
        var pageNum=$('#newsTable').bootstrapTable('getOptions').pageNumber;
        $.post('${request.contextPath}/newsAdmin/createOrEdit',{id:id,'catalog.id':catalogId,pageNum:pageNum,
            operation:'${params.operation}',catalogName:'${catalog?.name}'},function(data){
            $('#newsListTable').html('');
            $('#newsListTable').html(data);
        },'html');
    }
</script>
