<script>
    $(function () {
        showFileDetail("","directory");
    });
    function fileListTableQueryParams(params){
        params.filename=$("input[name=filename]").val();
        params.path=$("#path").val();
        params.max=params.limit;
        return params;
    }

    function doSearch(){
        $("#fileListTable").bootstrapTable("refresh",[]);
    }

    //操作
    function optionFormatter(value, row, index) {
        var str=[],fileType=(row['type']=="文件"?"file":"directory");
        str.push('<button class="btn btn-primary" onclick="javascript:showFileDetail(\''+path+'\',\''+fileType+'\')"><i class="glyphicon glyphicon-level-up"></i></button>&nbsp;&nbsp;');
        return str.join('');
    }

    //文件节点详细内容：若为文件夹则显示子文件，若为文件则显示文件信息
    function showFileDetail(path,fileType){
        $.post("${request.contextPath}/cms/fileDetails",{path:path,fileType:fileType},function (data,state) {
            $('#fileListContent').empty();
            $('#fileListContent').html(data);
        },"html");
    }
    //新建文件夹、文件
    function createNewFile(fileType) {
        var filepath=$('#path').val();
        var nametip=(fileType=="file"?"请输入文件名称":"请输入文件夹名称");
        var filename=prompt(nametip);
        if(filename){//(/^[a-zA-Z][a-zA-Z0-9]*$/.test(filename))
            $.post("${request.contextPath}/cms/createNewFileOrDir",{filename:filename,filepath:filepath,fileType:fileType},function(data,state){
                if(data.result){
                    alert("操作成功");
                    $("#fileTreeDiv").jstree().refresh();
                    showFileDetail(data.path,fileType);
                }else{
                    alert("操作失败");
                }
            },"json");
        }else if(filename==""){
            alert("请输入全英文名称");
        }
    }
    //上传文件
    function uploadFiles(){
        if(!$('#path').val()){
            alert("请选择要上传的目录");
        }else{
            $('#uploadModal').modal('show');
        }
    }

    //拷贝、移动文件和文件夹
    function moveOrCopyFile(opType){
        $('#fileTreeModal').modal('show');
        $('#fileTreeModal input[name=optype]').val(opType);
    }

    //保存文件
    function saveOrUploadFile(formId,path,opType){
        var newsForm=document.getElementById(formId);
        var formData = new FormData(newsForm);
        formData.append('opType',opType);
        formData.append('path',path);
        $.ajax({
            url:'${request.contextPath}/cms/saveOrUploadFile',
            dataType:'json',
            type:'POST',
            data: formData,
            processData : false, // 使数据不做处理
            contentType : false, // 不要设置Content-Type请求头
            success: function(data){
                alert(data.message);
                $('#uploadModal').modal('hide');
                if(data.result){
                    doSearch();
                    $("#fileTreeDiv").jstree().refresh();
                }
            },
            error:function(response){
                alert(response);
            }
        });
    }

</script>