<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <style>
    .f16{font-size:16px;}
    .fc{color:#0a8ed6;}
    </style>
    <script type="text/javascript">
        $(function() {
            $( ".form_date" ).datetimepicker({minView: "month",language: 'zh-CN',autoclose: true,format: 'yyyy-mm-dd',todayBtn:true });
        });
        function queryParamsAtt(params){
            $("input:not(:empty),.bootstrap-table-search-keyword").each(function(){
                params[$(this).attr('name')]=$(this).val();
            });
            return params;
        }
        function doSearch(){
            $("#attachmentTable").bootstrapTable("refresh",[]);
        }
        //操作
        function optionFormatterD(value, row, index) {
            var str=[];
            // 编辑
            // str.push('<button type="button" class="btn btn-info" onclick="createOrEditNews(\''+row.newsId+'\',\''+row.type+'\');"><i class="glyphicon glyphicon-pencil"></i></button>&nbsp;&nbsp;');
            //删除
            str.push('<button type="button" class="btn btn-danger" onclick="delAttachment(\''+row.id+'\');"><i class="glyphicon glyphicon-trash"></i></button>');
            return str.join('');
        }
        function createOrEditNews(id,type){
            loadRemotePage('${request.contextPath}/newsAdmin/createOrEdit?operation=attachmentList&attachmentType='+type+'&id='+id);
        }
        //点击标题预览
        function optionFormatterTitle(value, row, index) {
            var str='<a href="javascript://" onclick="showNews(\''+row['newsId']+'\')">'+value+'</a>';
            return str;
        }
        function showNews(id){
            window.open('${request.contextPath}/news/detail/'+id+'.html');
        }
        //文件名-视频则预览
        function optionFormatterA(value, row, index) {
            var str=[];
            if(row.type==2){
                str.push('<video width="320" height="240" controls="controls" autoplay="autoplay">')
                str.push('<source type="video/mp4" src="${request.contextPath}/web/getFile/'+row.id+'" type="video/mp4"/>')
                str.push('</video>')
            }
            str.push('<a href="${request.contextPath}/web/getFile?id='+row.id+'" class="fc" target="_blank">'+row.name+'</a>');
            return str.join('');
        }
        function delAttachment(id){
            if(confirm("确认永久删除文件?")){
                $.post("${request.contextPath}/attachment/deleteAttachment",{id:id},function(data){
                    alert(data.message);
                    if(data.result){
                        $("#attachmentTable").bootstrapTable("refresh",[]);
                    }
                },"json");
            }
        }

    </script>
</head>
<body>
<div class="row">
    <div class="col-lg-12">
        <div class="card">
            <div calss="row">
                <div class="col-lg-12">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="row form-control-inline">
                                <div class="col-lg-3">
                                    <input type="hidden" value="${params.type}" name="type" class="bootstrap-table-search-keyword"/>
                                    <input type="text" class="form-control bootstrap-table-search-keyword" name="title" placeholder="关键词">
                                </div>
                                <div class="col-lg-3">
                                    <input type="text" class="form-control bootstrap-table-search-keyword form_date" name="beginDate" placeholder="创建时间">
                                </div>
                                <div class="col-lg-6 text-right">
                                    <button class="btn btn-info" type="button" onclick="doSearch();"><i class="glyphicon glyphicon-search"></i><span class="pd">搜索</span></button>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <table id="attachmentTable" data-toggle="table" data-url="${request.contextPath}/attachment/listJson" data-pagination="true"
                                           data-id-field="id"  data-unique-id="id" data-sort-order="desc" data-show-columns="false"
                                           class="table table-striped table-hover bootstrapTable" data-side-pagination="server" data-cache="false"
                                           data-query-params="queryParamsAtt">
                                        <thead>
                                        <tr>
                                            <th data-align="center"  data-valign="middle" data-width="10%" data-field="xh">序号</th>
                                            <th data-valign="middle" data-width="30%" data-field="name" data-formatter="optionFormatterA">文件名称</th>
                                            <th data-valign="middle" data-width="30%" data-field="title" data-formatter="optionFormatterTitle">标题</th>
                                            <th data-valign="middle" data-align="center" data-width="15%" data-field="dateCreated">创建时间</th>
                                            <th data-align="center" data-valign="middle" data-width="15%" data-field="id" data-formatter="optionFormatterD">操作</th>
                                        </tr>
                                        </thead>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
