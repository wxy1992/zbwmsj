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
        $('#newsPicListTable').bootstrapTable({
            pageSize : '5',
        })
        function queryParamsPic(params){
            $("input:not(:empty),.bootstrap-table-search-keyword").each(function(){
                params[$(this).attr('name')]=$(this).val();
            });
            return params;
        }
        function doSearch(){
            $("#newsPicListTable").bootstrapTable("refresh",[]);
        }
        function optionFormatterD(value, row, index) {
            var str=[];
            str.push('<button type="button" class="btn btn-danger" onclick="delNewsPic(\''+row.id+'\');"><i class="glyphicon glyphicon-trash"></i><span class="pd">删除</span></button>');
            return str.join('');
        }
        function optionFormatterT(value, row, index) {
            var str=[];
            str.push('<a href="javascript://" onclick="createOrEdit(\''+row.id+'\');"  class="fc">'+row.title+'</a>');
            return str.join('');
        }
        function createOrEdit(id){
            window.open('${request.contextPath}/news/detail/'+id+'.html');
        }
        function optionFormatterP(value, row, index) {
            var str=[];
            str.push('<a href="${request.contextPath}/news/picture/'+row.id+'.jpg" target="_blank"><img src="${request.contextPath}/news/picture/'+row.id+'.jpg" width="150" height="100"/></a>');
            return str.join('');
        }
        function delNewsPic(id){
            if(confirm("确认删除?")){
                $.post("${request.contextPath}/newsAdmin/deletePic",{id:id},function(data){
                    alert(data.message);
                    if(data.result){
                        $("#newsPicListTable").bootstrapTable("refresh",[]);
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
                                    <input type="text" class="form-control bootstrap-table-search-keyword" name="title" placeholder="关键词">
                                </div>
                                <div class="col-lg-3">
                                    <input type="text" class="form-control bootstrap-table-search-keyword form_date" name="beginDate" placeholder="开始时间">
                                </div>
                                <div class="col-lg-3">
                                   <input type="text" class="form-control bootstrap-table-search-keyword form_date" name="endDate" placeholder="结束时间">
                                </div>
                                <div class="col-lg-3 text-right">
                                    <button class="btn btn-info" type="button" onclick="doSearch();"><i class="glyphicon glyphicon-search"></i><span class="pd">搜索</span></button>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <table id="newsPicListTable" data-toggle="table" data-url="${request.contextPath}/newsAdmin/pictureJson" data-pagination="true"
                                           data-id-field="id"  data-unique-id="id" data-sort-order="desc" data-show-columns="false" data-pagination-detail="true"
                                           class="table table-striped table-hover bootstrapTable" data-side-pagination="server" data-cache="false"
                                           data-query-params="queryParamsPic">
                                        <thead>
                                        <tr>
                                            <th data-valign="middle" data-width="40%" data-field="title" data-formatter="optionFormatterT">标题</th>
                                            <th data-align="center"  data-valign="middle"  data-width="30%" data-field="picture" data-formatter="optionFormatterP">图片</th>
                                            <th data-valign="middle" data-align="center" data-width="15%" data-field="publishDate">发布时间</th>
                                            <th data-valign="middle" data-align="center"  data-width="15%" data-field="id" data-formatter="optionFormatterD">操作</th>
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
