<%@ page import="com.bjrxkj.cms.Catalog" %>
<div class="modal fade" id="catalogMenuModal" tabindex="-1" role="dialog" aria-labelledby="messageModalHead" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true"><i class="glyphicon glyphicon-remove"></i></span><span class="sr-only">Close</span></button>
            </div>
            <div class="modal-body" style="height: 400px;overflow-y: scroll;">
                <div id="copyMenuDiv" style="margin-top: 10px;margin-left:10px;"></div>
            </div>

            <div class="modal-footer">
                <g:if test="${checkbox==true}">
                    <button class="btn btn-primary" onclick="confirmCatalog();">确定</button>
                    <button class="btn btn-danger" onclick="cleanCatalog();">清空</button>
                </g:if>
                <button class="btn btn-default margin" data-dismiss="modal" type="button">
                    <span class="glyphicon glyphicon-remove"></span>
                    关闭
                </button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    var treeJsonUrl="${request.contextPath}/newsAdmin/menuJson?usage=${usage}";
    if("${usage}"=="baseUser"){
        treeJsonUrl+="&userId=${baseUser?.id}";
    }
    function initTree(tree){
        tree.jstree({
            'core' : {
                'data': {
                    'url': treeJsonUrl,
                    "dataType": "json",
                    'data' : function (node) {
                        return { 'id' : node.id,type:node.state.type};
                    }
                }
            },
            'state': {
                "opened":false
            },
            "types" : {
                "site" : {
                    "icon" : "glyphicon glyphicon-home"
                },
                "catalog" : {
                    "icon" : "glyphicon glyphicon-folder-open"
                },
                "isleaf" : {
                    "icon" : "glyphicon glyphicon glyphicon-file"
                }
            },
            <g:if test="${checkbox==true}">
            "checkbox":{  // 去除checkbox插件的默认效果
                // 'three_state':false,
                'tie_selection': true,
                'keep_selected_style': true,
                'whole_node': true
            },
            </g:if>
            "plugins": ["types", "state"<g:if test="${checkbox==true}">,"checkbox"</g:if>]
        });
        tree.on('activate_node.jstree', function (event, data) {
            <g:if test="${usage=="createNews"}">
            $('input[name="catalog.id"]').val(data.node.id);
            $('#changeCatalogLink').text(data.node.text);
            </g:if><g:elseif test="${usage=="catalogParent"}">
            $('input[name="parent.id"]').val(data.node.id);
            $('input[name="parentName"]').val(data.node.text);
            $("#catalogMenuModal").modal('hide');
            </g:elseif>
        });
        //加载时清楚选中状态
        tree.on("loaded.jstree", function(event, data) {
            data.instance.clear_state();
            <g:if test="${["baseRole","baseUser"].contains(usage)}">
            data.instance.load_all();
            </g:if>

        });

    }


    //栏目弹出框的确定按钮
    function confirmCatalog(){
        var operation=$('#newsOperationType').val();
        if(operation=='move'||operation=='copy'){
            submitCopyNews();
        }else {
            var tree=$("#copyMenuDiv").jstree();
            var catalogIds=tree.get_checked(true);
            var catIds=[],leafIds=[],siteIds=[],catnames='';
            for(i=0;i<catalogIds.length;i++){
                var cp=catalogIds[i];
                if(catnames){
                    catnames+=',';
                }
                catnames+=cp.text;
                var parr=cp.parents;
                if(!leafIds.includes(cp.id)&&cp.type==''){

                }
                for(j=0;j<parr.length;j++){
                    var v=parr[j];
                    if(v!='#'){
                        if(v.startsWith('P_')){
                            var siteid=v.replace('P_','');
                            if(!siteIds.includes(siteid)){
                                siteIds.push(siteid);
                            }
                        }else if(!catIds.includes(v)){
                            catIds.push(v);
                        }
                    }
                }
                if(!catIds.includes(cp.id)&&!cp.id.startsWith('P_')){
                    catIds.push(cp.id);
                }
            }
            $("input[name=sitestr]").val(siteIds.toString());
            $("input[name=catalogstr]").val(catIds.toString());
            // $("input[name=catalogName]").val('选中'+catIds.length+'个栏目');
            $("input[name=catalogName]").val(catnames);
        }
        $("#catalogMenuModal").modal('hide');
    }

    function cleanCatalog(){
        $("input[name=sitestr]").val('');
        $("input[name=catalogstr]").val('');
        $("input[name=catalogName]").val('');
    }

    function showCatalogMenuModal(){
        $('#catalogMenuModal').modal('show');
        initTree($('#copyMenuDiv'));
    }
</script>
