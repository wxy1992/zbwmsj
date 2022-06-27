<script type="text/javascript" src="${request.contextPath}/js/jquery.min.js" ></script>
<script type="text/javascript" src="${request.contextPath}/js/bootstrap.min.js" ></script>
<script type="text/javascript" src="${request.contextPath}/js/bootstrap-table-1.11.0/bootstrap-table.js" ></script>
<script type="text/javascript" src="${request.contextPath}/js/bootstrap-table-1.11.0/locale/bootstrap-table-zh-CN.js" charset="UTF-8" ></script>
<script type="text/javascript" src="${request.contextPath}/js/bootstrap-select-1.12.4/dist/js/bootstrap-select.js"  ></script>
<script type="text/javascript" src="${request.contextPath}/js/jquery-validation/jquery.validate.js"  ></script>
<script type="text/javascript" src="${request.contextPath}/js/jquery-validation/localization/messages_zh.js"  ></script>
<script type="text/javascript" src="${request.contextPath}/js/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"  ></script>
<script type="text/javascript" src="${request.contextPath}/js/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"  ></script>
<script type="text/javascript" src="${request.contextPath}/js/jstree-3.2.1/dist/jstree.min.js"  ></script>
<script type="text/javascript" src="${request.contextPath}/js/workspace/jquery.easyui.min.js"  ></script>
<script type="text/javascript" src="${request.contextPath}/js/workspace/jquery.nanoscroller.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/workspace/sidebar.js"></script>

<script>
    $(function(){
        $.ajaxSetup({
            cache:false,
            error : function(jqXHR, textStatus, errorThrown){
                if (jqXHR.status==401){
                    window.location.href="${request.contextPath}/login";
                }
            },
            traditional : true
        });
        countTodoTask();

        $('#rolemenuUl li a').click(function () {
            var _this=$(this);
            $('#rolemenuUl li').removeClass('active');
            _this.parents('li').addClass('active');
            // console.log(this);
        });
    });
    function loadRemotePage(url, obj, classname) {
        // $('#rolemenuUl *').removeClass('active');
        // $(this).parent().addClass('active');
        if (typeof(obj) == "undefined") {
            obj = {};
        }
        $.post(url, obj, function (data, textStatus) {
            $('#mainBodyDiv').html('');
            $('#mainBodyDiv').html(data);
            $('.bootstrapTable').bootstrapTable({escape:true});
            $('.selectpicker').selectpicker({
                noneResultsText:"没有找到{0}",
                noneSelectedText: "请选择"
            });
        }, "html");
    }
    //气泡数字计算
    function countTodoTask(){
        $.post("${request.contextPath}/cms/countMyTask", {}, function (data, textStatus) {
            $('.todoTradeNum').text(data['todoTradeNum']);
            $('.todoNewsNum').text(data['todoNewsNum']);
        }, "json");
    }

    window.alert=function(e){
        $("#messageModal").modal('show');
        $("#messageModalBody").html(e);
    }
</script>
