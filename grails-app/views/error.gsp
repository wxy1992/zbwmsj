<!doctype html>
<html>
<head>
    <title>500</title>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap.min.css"/>
</head>
<body>
<div class="container-fluid pr-0 pl-0">
    <div class="row mr-0 ml-0">
        <g:if env="development">
            <g:if test="${Throwable.isInstance(exception)}">
                <g:renderException exception="${exception}" />
            </g:if>
            <g:elseif test="${request.getAttribute('javax.servlet.error.exception')}">
                <g:renderException exception="${request.getAttribute('javax.servlet.error.exception')}" />
            </g:elseif>
            <g:else>
                <ul class="errors">
                    <li>An error has occurred</li>
                    <li>Exception: ${exception}</li>
                    %{--                    <li>Message: ${message}</li>--}%
                    %{--                    <li>Path: ${path}</li>--}%
                </ul>
            </g:else>
        </g:if><g:else>
        <div class="col-lg-12 text-white text-center" style="font-size: 200px;">500</div>
    </g:else>

    </div>

</div>
</body>
</html>
