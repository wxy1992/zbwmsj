<div class="modal fade" id="fileTreeModal" tabindex="-1" role="dialog" aria-labelledby="messageModalHead" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true"><i class="glyphicon glyphicon-remove"></i></span><span class="sr-only">Close</span></button>
            </div>
            <div class="modal-body" style="height: 400px;overflow-y: scroll;">
                <input name="optype" type="hidden">
                <div id="copyFileDiv" style="margin-top: 10px;margin-left:10px;"></div>
            </div>

            <div class="modal-footer">
                <button class="btn btn-default margin" data-dismiss="modal" type="button">
                    <span class="glyphicon glyphicon-remove"></span>
                    关闭
                </button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function(){
        var tree=$("#copyFileDiv");
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
            if(data.node.type=='directory'){
                submitCopyFile(data.node.id);
            }else{
                alert("请选择文件夹进行此操作");
            }
        });
        // 加载时
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

    //复制、移动文件
    function submitCopyFile(targetPath){
        var selectRows=$('#fileListTable').bootstrapTable('getSelections');
        if (selectRows.length <= 0) {
            alert("请至少选择一条新闻！");
        }else if(!targetPath){
            alert("请选择所要粘贴到的文件夹！");
        }else{
            var opType=$('#fileTreeModal input[name=optype]').val();
            if(window.confirm("确认将选中信息"+(opType=='move'?'移动':'拷贝')+"到该目录？")){
                var oldpaths='';
                for (var i = 0; i < selectRows.length; i++) {
                    if (oldpaths != '') oldpaths += ',';
                    oldpaths += selectRows[i].path;
                }
                $.post('${request.contextPath}/cms/moveOrCopyFile',{oldPaths:oldpaths,targetPath:targetPath,opType:opType},function (data,status){
                    alert(data.message);
                    $('#fileTreeModal').modal('hide');
                    if(data.result){
                        doSearch();
                        $("#copyFileDiv").jstree().refresh();
                        $("#fileTreeDiv").jstree().refresh();
                    }
                },"json");
            }
        }
    }
</script>
