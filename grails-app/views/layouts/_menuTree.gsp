<script type="text/javascript">
    $(function(){
        var tree=$("#menuTreeDiv");
        initTree(tree,"${request.contextPath}/newsAdmin/menuJson");
    });
    function initTree(tree,url){
        tree.jstree({
            'core' : {
                'data': {
                    'url': url,
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
            "plugins": ["types","contextmenu"],
            "contextmenu" : {
                "items" : customMenu
            }
        });
        //选中节点时
        tree.on('activate_node.jstree', function (event, data) {
            <g:if test="${type=='newsList'}">
            showNewsList(data.node.id);
            </g:if><g:elseif test="${type=='catalog'}">
            if(data.node.parent=="#"){
                loadRemotePage('${request.contextPath}/catalog/list',{siteId:data.node.id});
            }else{
                $.post("${request.contextPath}/catalog/createOrEdit",{id:data.node.id,site:data.node.parent,parents:data.node.parents},function(data){
                    $('#catalogContentDiv').html('');
                    $('#catalogContentDiv').html(data);
                },"html");
            }
            </g:elseif>
        });
        //加载时清除选中
        tree.on("loaded.jstree", function(event, data) {
            tree.jstree('deselect_all', true);
        });
        //站点折叠的时候重新加载
        tree.on("after_close.jstree", function(event, data) {
            if(data.node.type=='site'){
                tree.jstree().refresh();
            }
        });
    }
    function customMenu(clickedNode) {
        var items = {
            "edit": {
                "label": "编辑",
                "action": function (obj) {
                    if (clickedNode.state.type == 'site') {//预览站点
                        var siteId=clickedNode.id.startsWith('P_')?clickedNode.id.substr(2):clickedNode.id;
                        loadRemotePage('${request.contextPath}/site/createOrEdit',{id:siteId});
                    } else {//预览栏目
                        // console.log(obj);
                    }
                }
            }
        };

        if ("${type}" == "newsList") {//稿件管理
            if (clickedNode.state.type == 'catalog') {
                items["createNews"] = {
                    "label": "新增稿件",
                    "action": function (obj) {
                        createOrEdit('',clickedNode.id);
                    }
                };
                items["importNews"] = {
                    "label": "导入稿件",
                    "action": function (obj) {
                        $("#impCatalogId").val(clickedNode.id);
                        $("#impCatalogId").attr('readonly','readonly');
                        $("#uploadModal").modal('show');
                    }
                };
            }
        }
        return items;    //注意要有返回值
    }
</script>
<div id="menuTreeDiv" style="overflow: scroll;min-height: 900px;"></div>
