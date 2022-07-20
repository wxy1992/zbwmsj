

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>

<body>
<div class="row">
    <div class="col-lg-12">
        <div class="card">
            <label class="text-danger">
                注：文章发布量仅统计“发布”状态；服务发布量仅统计状态为“进行中”和“已结束”；
            </label>
           <table id="publishRankTable" data-toggle="table"
                  class="table table-striped table-hover" data-pagination="false">

           </table>
        </div>
    </div>
</div>
<script>
    $(function(){
        $('#publishRankTable').bootstrapTable({
            url:'${request.contextPath}/chart/publishRankJson',
            columns: [{
                field: 'name',
                title: '单位',
                sortable:true
            }, {
                field: 'newsNum',
                title: '文章发布量',
                sortable:true
            }, {
                field: 'tradeNum',
                title: '服务发布量',
                align:'left',
                sortable:true
            }]
        })
    });

</script>
</body>
</html>
