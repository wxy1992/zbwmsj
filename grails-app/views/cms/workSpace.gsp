<%@ page import="com.wmsj.cms.News" contentType="text/html;charset=UTF-8" %>
<html>
<head>
<g:if test="${!request.xhr}">
    <meta name="layout" content="main"/>
</g:if>
    <script type="text/javascript" src="${request.contextPath}/js/workspace/echarts.js" />
    <script>
        $(function () {
            <g:if test="${params.redirectUrl}">
            loadRemotePage('${params.redirectUrl}');
            </g:if>
        });
    </script>
</head>
<body>
<section>
    <div class="row">
        <div class="col-lg-3">
            <div class="card">
                <div class="stat-widget-one">
                    <div class="stat-icon dib"><i class="glyphicon glyphicon-list-alt color-success border-success"></i></div>
                    <div class="stat-content dib">
                        <div class="stat-text">新闻总量</div>
                        <div class="stat-digit">${result.num1}</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-3">
            <div class="card">
                <div class="stat-widget-one">
                    <div class="stat-icon dib"><i class="glyphicon glyphicon-th-list color-warning border-warning"></i></div>
                    <div class="stat-content dib">
                        <div class="stat-text">栏目总量</div>
                        <div class="stat-digit">${result.num2}</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-3">
            <div class="card">
                <div class="stat-widget-one">
                    <div class="stat-icon dib"><i class="glyphicon glyphicon-eye-open color-primary border-primary"></i></div>
                    <div class="stat-content dib">
                        <div class="stat-text">访问量</div>
                        <div class="stat-digit">${result.num3}</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-3">
            <div class="card">
                <div class="stat-widget-one">
                    <div class="stat-icon dib"><i class="glyphicon glyphicon-arrow-up color-danger border-danger"></i></div>
                    <div class="stat-content dib">
                        <div class="stat-text">今日更新量</div>
                        <div class="stat-digit">${result.num4}</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <g:render template="/chart/newsVisitBarChart"/>
    <div class="row">
        <div class="col-lg-8">
            <div class="card">
                <div class="card-title">
                    <h4>一周内新闻发布量Top20</h4>
                </div>
                <div class="card-body">
                    <div id="catalogVisitAWeek" style="width:100%;height:390px;"></div>
                </div>
            </div>
        </div>
        <div class="col-lg-4">
            <div class="card" style="height: 460px;overflow-y: scroll;">
                <div class="card-title">
                    <h4>新闻发布动态</h4>
                </div>
                <div class="card-body">
                    <ul class="timeline">
                        <g:each in="${result.newsupdate}" var="news" status="i">
                            <%
                                def currentTime=result.current.getTime();
                                long diff=currentTime-news.dateCreated.getTime();
                                int secsdiff=(diff/1000).intValue();//秒
                                int minsdiff=(secsdiff/60).intValue();//分钟
                                def diffstr="${minsdiff}分钟前";//分钟
                                if(minsdiff==0){//不够1分钟
                                    diffstr="${secsdiff}秒前"
                                }else if(minsdiff>=(60*24)){//大于等于1天
                                    diffstr="${news.dateCreated?.format('yyyy-MM-dd')}"
                                }else if(minsdiff>=60){//大于等于1小时
                                    diffstr="${(minsdiff/60).intValue()}小时前";
                                }
                            %>
                            <li>
                                <div class="timeline-badge ${result.colors[i%4]}"><i class="glyphicon glyphicon-time"></i></div>
                                <div class="timeline-panel">
                                    <div class="timeline-heading">
                                        <h5 class="timeline-title">${news?.title}</h5>
                                    </div>
                                    <div class="timeline-body">
                                        <p>${diffstr} </p>
                                    </div>
                                </div>
                            </li>
                        </g:each>
                    </ul>
                </div>
            </div>
            <!-- /# card -->
        </div>
    </div>

</section>
<script type="text/javascript">
    $.post('${request.contextPath}/cms/catalogVisitAWeekJson', {}, function (data) {
        var catalogVisitAWeek = echarts.init(document.getElementById('catalogVisitAWeek'));
        var catalogVisitAWeekOption = {
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data:data.dataLegend
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: data.dateX
            },
            yAxis: {
                type: 'value'
            },
            series: data.seriesData
        };
        catalogVisitAWeek.setOption(catalogVisitAWeekOption);
    }, 'json');
</script>
</body>
</html>
