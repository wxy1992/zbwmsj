<script>

    $(function () {

    });



    function queryParams(params){
        params.name=$("#name").val();
        params.telephone=$("#telephone").val();
        params.idcard=$("#idcard").val();
        params.max=params.limit;
        return params;
    }

    function doSearch(){
        $("#applyTable").bootstrapTable("refresh",[]);
    }
    //退单原因
    function backreasonFormatter(value, row, index) {
        var str=value;
        if(row['status']!=0){
            str="-";
        }
        return str;
    }
    //操作
    function optionFormatter(value, row, index) {
        var str=[];
        // str.push('<button class="btn btn-danger" onclick="javascript:deleteapply(\''+row.id+'\')"><i class="glyphicon glyphicon-trash"></i></button>');
        return str.join('');
    }


    function backToTradeList(){
        loadRemotePage('${request.contextPath}/trade/list');
    }


    //改变报名状态
    function changeApplyStatus(status) {
        var selectRows=$('#applyTable').bootstrapTable('getSelections');
        if (selectRows.length <= 0) {
            alert("请至少选择一条报名信息！");
        }else if(window.confirm("确定对选中的报名进行此操作？")){
            if (status == '20'||status == '0') {
                var filterRows = $.grep(selectRows, function (obj, i) {
                    return obj['status'] != 10;  //return为过滤的条件
                });
                if(filterRows.length > 0){
                    alert("仅可完成或退回进行中的报名");
                    return;
                }
            }
            var fields='';
            for (var i = 0; i < selectRows.length; i++) {
                if (fields != '') fields += ',';
                fields += selectRows[i].id;
            }
            var obj=new Object();
            obj.fields=fields;
            obj.status=status;
            if (status == '0') {
                obj.backreason = prompt('退单原因', '');
            }
            $.post('${request.contextPath}/apply/changeApplyStatus',obj,function (data){
                alert(data.message);
                doSearch();
            },"json");
        }
    }
</script>
