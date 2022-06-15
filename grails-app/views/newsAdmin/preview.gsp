<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${news?.title}</title>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/workspace/fonts.css"/>
</head>

<body class="bg-secondary">
    <div class="container bg-white">
        <div class="row">
            <h3 class="col-md-12 text-center" style="line-height: 150px;">${news?.title}</h3>
        </div>
        <div class="row">
            <div class="col-md-4 text-center">来源：${news?.source}</div>
            <div class="col-md-4 text-center">作者：${news?.author}</div>
            <div class="col-md-4 text-center">时间：${news?.publishDate}</div>
        </div>
        <div class="row mt-lg-5">
            <div class="col-md-12" style="min-height: 700px;">
                ${raw(news?.content)}
            </div>
        </div>
    </div>
</body>
</html>
