<script type="text/javascript">
    $(function(){
        var tree=$("#fileTreeDiv");
        initTree(tree,"${request.contextPath}/cms/fileTreeJson");
    });
    function initTree(tree,url){
        tree.jstree({
            'core' : {
                'data': {
                    'url': url,
                    "dataType": "json",
                    'data' : function (node) {
                        return {'id':node.id,type:node.state.type};
                    }
                }
            },
            'state': {
                "opened":false
            },
            "types" : {
                "directory" : {
                    "icon" : "glyphicon glyphicon-folder-open"
                },
                "file" : {
                    "icon" : "glyphicon glyphicon glyphicon-file"
                }
            },
            "plugins": ["types", "state"]
        });
        //选中节点时
        tree.on('activate_node.jstree', function (event, data) {
            showFileDetail(data.node.id,data.node.type);
        });
        //加载时
        tree.on("loaded.jstree", function(event, data) {
            data.instance.clear_state(); // <<< 这句清除jstree保存的选中状态
        });
        //站点折叠的时候重新加载
        tree.on("after_close.jstree", function(event, data) {
            if(data.node.type=='directory'){
                tree.jstree().refresh();
            }
        });
    }
</script>
<div id="fileTreeDiv" style="margin-top: 10px;margin-left:10px;overflow: scroll;min-height: 900px;"></div>