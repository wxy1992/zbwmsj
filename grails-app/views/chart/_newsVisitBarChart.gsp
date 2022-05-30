<div class="row">
    <div class="col-lg-4">
        <div class="card">
            <div id="catalogVisitPie" style="width:100%;height:400px;"></div>
        </div>
    </div>
    <div class="col-lg-8">
        <div class="card">
            <div id="newsVisitBarChart" style="width:100%;height:400px;"></div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function(){
        $.post('${request.contextPath}/chart/catalogVisitPieJson',{},function(data){
            var catalogVisitPie = echarts.init(document.getElementById('catalogVisitPie'));
            var catalogVisitPieOption = {
                title: {
                    text: '栏目访问量TOP10'
                },
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    x: 'right',
                    y:'bottom',
                    data: data.catalogsName
                },
                series: [
                    {
                        name: '访问量',
                        type: 'pie',
                        // radius: ['60%', '30%'],
                        center: ['50%', '40%'],
                        avoidLabelOverlap: false,
                        label: {
                            normal: {
                                show: false,
                                position: 'center'
                            },
                            emphasis: {
                                show: true,
                                textStyle: {
                                    color:'#fff',
                                    fontSize: '20',
                                    fontWeight: 'bold'
                                }
                            }
                        },
                        labelLine: {
                            normal: {
                                show: false
                            }
                        },
                        data: data.seriesData
                    }
                ]
            };
            catalogVisitPie.setOption(catalogVisitPieOption);
        },'json');
        // 基于准备好的dom，初始化echarts实例
        $.post('${request.contextPath}/chart/newsVisitBarChartJson',{},function(data){
            var newsVisitBarChart = echarts.init(document.getElementById('newsVisitBarChart'));
            var newsVisitBarChartOption = {
                title: {
                    text: '新闻访问量TOP10'
                },
                color: ['#3398DB'],
                tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis : [
                    {
                        type : 'category',
                        data : data.newsName,
                        axisTick: {
                            alignWithLabel: true
                        }
                    }
                ],
                yAxis : [
                    {
                        type : 'value'
                    }
                ],
                series : [
                    {
                        name:'访问量',
                        type:'bar',
                        barWidth: '60%',
                        data:data.seriesData
                    }
                ]
            };
            newsVisitBarChart.setOption(newsVisitBarChartOption);
        },'json');
    });
</script>