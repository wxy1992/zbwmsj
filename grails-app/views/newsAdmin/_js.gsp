<script>
    var buttonWithState={'草稿':'draft','初步审核':'audit','拟发审核':'pubaudit','退回':'reback','发布':'publish','回收站':'refuse'};
    $(function(){
        showNewsList('');
    });

    function doSearch(){
        $("#newsTable").bootstrapTable("refresh",[]);
        operateGroup();
    }

    //点击左侧栏目树方法
    function showNewsList(cid,state,operation,pageNum){
        var obj=new Object();
        obj.catalogId=cid;
        obj.state=(state?state:'${params.state}');
        obj.operation=(operation?operation:'${params.operation}');
        if(pageNum){
            obj.pageNum=pageNum;
        }
        $.post("${request.contextPath}/newsAdmin/newsList",obj,function(data){
            $('#newsListTable').html('');
            $('#newsListTable').html(data);
        },"html");
    }
    //预览新闻
    function previewIndex(id){
        window.open('${request.contextPath}/newsAdmin/preview/'+id);
    }

    function titleFormatter(value, row, index) {
        var bgcolor='';
        var str='<a href="javascript://" class="'+bgcolor+'" onclick="previewIndex(\''+row.id+'\');">'+row['title']+'</a>';
        return str;
    }
    function catalogFormatter(value, row, index) {
        var str='<a href="javascript://" class="" onclick="previewIndex(\''+row.id+'\');">'+value+'</a>';
        return str;
    }

    function optionFormatter(value, row, index) {
        var str=[];
        <sec:ifNotGranted roles="ROLE_AUDITOR,ROLE_PUBAUDITOR">
        if(row['state']=='草稿'||row['state']=='退回'){
            str.push('<button type="button" class="btn btn-primary" title="编辑" onclick="createOrEdit(\''+row.id+'\',\'\');"><i class="glyphicon glyphicon-pencil"></i></button>&nbsp;&nbsp;');
        }
        </sec:ifNotGranted>
        str.push('<button type="button" class="btn btn-info" title="预览" onclick="previewIndex(\''+row.id+'\');"><i class="glyphicon glyphicon-eye-open"></i></button>');
        return str.join('');
    }

    function backToList(cid,state,operation,pageNum){
        showNewsList(cid,state,operation,pageNum);
    }
    //改变新闻状态
    function changeNewsState(state) {
        var selectRows=$('#newsTable').bootstrapTable('getSelections');
        if (selectRows.length <= 0) {
            alert("请至少选择一条新闻！");
        }else {
            var fields='';
            for (var i = 0; i < selectRows.length; i++) {
                if (fields != '') fields += ',';
                fields += selectRows[i].id;
            }
            var obj=new Object();
            obj.fields=fields;
            obj.state=state;
            if(state=='退回'){
                obj.backreason = prompt('意见建议', '');
            }
            $.post('${request.contextPath}/newsAdmin/changeNewsState',obj,function (data){
                alert(data.message);
                doSearch();
                countTodoTask();
            },"json");
        }
    }

    //删除
    function deleteAll() {
        var selectRows=$('#newsTable').bootstrapTable('getSelections');
        if (selectRows.length <= 0) {
            alert("请至少选择一条记录！");
        }else if(window.confirm("确定删除选中的记录？")){
            var fields='';
            for (var i = 0; i < selectRows.length; i++) {
                if (fields != '') fields += ',';
                fields += selectRows[i].id;
            }
            $.post('${request.contextPath}/newsAdmin/deleteAll',{fields:fields},function (data){
                alert(data.message);
                doSearch();
            },"json");
        }
    }
    //复制选择新闻
    function showCopyPanel(operation){
        var selectRows=$('#newsTable').bootstrapTable('getSelections');
        if (selectRows.length <= 0) {
            alert("请至少选择一条记录！");
        }else {
            $('#newsOperationType').val(operation);
            $('#catalogMenuModal').modal('show');
            $('#copyMenuDiv').jstree("destroy");
            initTree($("#copyMenuDiv"));
        }
    }
    //复制新闻
    function submitCopyNews(){
        var cid='';
        var tree=$("#copyMenuDiv").jstree();
        var catalogIds=tree.get_checked(true);
        for(i=0;i<catalogIds.length;i++){
            var cucat=catalogIds[i];
            if(cucat.type=='isleaf'){//栏目
                if (cid != '') cid += ',';
                cid += cucat.id;
            }
        }
        var selectRows=$('#newsTable').bootstrapTable('getSelections');
        if (selectRows.length <= 0) {
            alert("请至少选择一条新闻！");
        }else if(!cid){
            alert("请选择所要粘贴到的栏目！");
        }else{
            var operation=$('#newsOperationType').val();
            if(window.confirm("确认将选中信息"+(operation=='move'?'移动':'复制')+"到该栏目？")){
                var fields='';
                for (var i = 0; i < selectRows.length; i++) {
                    if (fields != '') fields += ',';
                    fields += selectRows[i].id;
                }
                $.post('${request.contextPath}/newsAdmin/copyNews',{fields:fields,cid:cid,operation:operation},function (data){
                    $('#catalogMenuModal').modal('hide');
                    alert(data.message);
                    doSearch();
                },"json");
            }
        }
    }

    //导出
    function exportSelectNews() {
        var selectRows = $('#newsTable').bootstrapTable('getSelections');
        if (selectRows.length <= 0) {
            alert("请至少选择一条新闻！");
        } else {
            var fields = '';
            for (var i = 0; i < selectRows.length; i++) {
                if (fields != '') fields += ',';
                fields += selectRows[i].id;
            }
            $('#exportNewsId').val(fields);
            $('#exportNewsForm').submit();

        }
    }


</script>

