var RFS_audit_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#RFSAudit_audit_formSearch #RFS_auditing_list").bootstrapTable({
            url: '/experimentS/experimentS/pageGetListRFSAudit',
            method: 'get',                      // 请求方式（*）
            toolbar: '#RFSAudit_audit_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "jsrq",				//排序字段
            sortOrder: "desc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "sjid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'ybbh',
                title: '标本编号',
                sortable: true,
                width: '12%',
                align: 'left',
                titleTooltip:'标本编号',
                visible:true
            },{
                field: 'nbbm',
                title: '内部编号',
                sortable: true,
                width: '14%',
                align: 'left',
                titleTooltip:'内部编号',
                visible: true
            },{
                field: 'hzxm',
                title: '患者姓名',
                width: '6%',
                align: 'left',
                titleTooltip:'患者姓名',
                visible:true
            },{
                field: 'nl',
                title: '年龄',
                width: '4%',
                align: 'left',
                titleTooltip:'年龄',
                visible:false
            },{
                field: 'yblxmc',
                title: '标本类型',
                width: '6%',
                align: 'left',
                titleTooltip:'标本类型',
                visible:true
            },{
                field: 'jcxmmc',
                title: '检测项目',
                width: '10%',
                align: 'left',
                titleTooltip:'检测项目',
                visible:true
            },{
                field: 'jsrq',
                title: '接收日期',
                sortable: true,
                width: '12%',
                align: 'left',
                titleTooltip:'接收日期',
                visible:true

            },{
                field: 'jcxmkzcs',
                title: '项目分类标识',
                width: '6%',
                align: 'left',
                titleTooltip:'项目分类标识',
                visible:false
            },{
                field: 'syrq',
                title: '实验日期',
                width: '12%',
                align: 'left',
                sortable: true,
                titleTooltip:'实验日期',
                visible:true
            },{
                field: 'jcdwmc',
                title: '检测单位',
                width: '6%',
                align: 'left',
                titleTooltip:'检测单位',
                visible:true
            },{
                field: 'flg_qf',
                title: '区分标记',
                width: '6%',
                align: 'left',
                titleTooltip:'区分标记',
                visible:false
            },{
                field: 'qf',
                title: '类型区分',
                width: '6%',
                align: 'left',
                titleTooltip:'类型区分',
                visible:true
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '12%',
                sortable: true,
                align: 'left',
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'zt',
                title: '状态',
                width: '10%',
                align: 'left',
                formatter:ztFormat,
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                if ($("#RFSAudit_audit_formSearch #btn_viewmore").length>0){
                    RFS_audit_DealById(row.sjid,'viewmore',$("#RFSAudit_audit_formSearch #btn_viewmore").attr("tourl"),row.qf,row.flg_qf);
                }else{
                    RFS_audit_DealById(row.sjid,'view',$("#RFSAudit_audit_formSearch #btn_view").attr("tourl"),row.qf,row.flg_qf);
                }
            },
        });
        $("#RFSAudit_audit_formSearch #RFS_auditing_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true}
        );
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "nbbm", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            zt: '10', // 防止同名排位用
            limitColumns: "{'sjxxDto':'sjid,ybbh,nbbm,hzxm,nl,yblxmc,jsrq,jcxmmc,jcxmkzcs,ksmc,jcdwmc,jcdw,flg_qf,qf,syrq,shxx_sqsj,sqr,shxx_shsj,shxx_sftg,shxx_lrryxm,shxx_gwmc,shxx_shxxid,shxx_shyj'}" //筛选出这部分字段用于列表显示
            // 搜索框使用
            // search:params.search
        };
//		return getMaterialAuditSearchData(map);
        var experiment_select=$("#RFSAudit_audit_formSearch #cxtj").val();
        var experiment_input=$.trim(jQuery('#RFSAudit_audit_formSearch #cxnr').val());
        if(experiment_select=="0"){
            map["ybbh"]=experiment_input
        }else if(experiment_select=="1"){
            map["nbbm"]=experiment_input
        }else if(experiment_select=="2"){
            map["hzxm"]=experiment_input
        }

        //检测单位
        var jcdws=$("#RFSAudit_audit_formSearch  #jcdw_id_tj").val();
        map["jcdws"]=jcdws
        map["dqshzt"] = 'dsh';
        return map;
    };
    return oTableInit;
}


var RFS_audited_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#RFSAudit_audited_formSearch #RFS_audited_list").bootstrapTable({
            url: '/experimentS/experimentS/pageGetListRFSAudit',
            method: 'get',                      // 请求方式（*）
            toolbar: '#RFSAudit_audited_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "shxx.shsj",				//排序字段
            sortOrder: "desc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "sjid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'ybbh',
                title: '标本编号',
                width: '12%',
                sortable: true,
                align: 'left',
                titleTooltip:'标本编号',
                visible:true
            },{
                field: 'nbbm',
                title: '内部编号',
                width: '14%',
                align: 'left',
                sortable: true,
                titleTooltip:'内部编号',
                visible: true
            },{
                field: 'hzxm',
                title: '患者姓名',
                width: '8%',
                align: 'left',
                titleTooltip:'患者姓名',
                visible:true
            },{
                field: 'nl',
                title: '年龄',
                width: '4%',
                align: 'left',
                titleTooltip:'年龄',
                visible:false
            },{
                field: 'yblxmc',
                title: '标本类型',
                width: '8%',
                align: 'left',
                titleTooltip:'标本类型',
                visible:true
            },{
                field: 'jcxmmc',
                title: '检测项目',
                width: '8%',
                align: 'left',
                titleTooltip:'检测项目',
                visible:true
            },{
                field: 'qf',
                title: '类型区分',
                width: '6%',
                align: 'left',
                titleTooltip:'类型区分',
                visible:true
            },{
                field: 'sqrxm',
                title: '申请人员',
                titleTooltip:'申请人员',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                titleTooltip:'申请时间',
                width: '14%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_lrryxm',
                title: '审核人',
                titleTooltip:'审核人',
                align: 'left',
                width: '8%',
                visible: true
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                titleTooltip:'审核时间',
                width: '14%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                titleTooltip:'审核意见',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_sftgmc',
                title: '是否通过',
                titleTooltip:'是否通过',
                width: '6%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                if ($("#RFSAudit_audit_formSearch #btn_viewmore").length>0){
                    RFS_audit_DealById(row.sjid,'viewmore',$("#RFSAudit_audit_formSearch #btn_viewmore").attr("tourl"),row.qf,row.flg_qf);
                }else{
                    RFS_audit_DealById(row.sjid,'view',$("#RFSAudit_audit_formSearch #btn_view").attr("tourl"),row.qf,row.flg_qf);
                }
            },
        });
        $("#RFSAudit_audited_formSearch #RFS_audited_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true}
        );
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "nbbm", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            limitColumns: "{'sjxxDto':'sjid,ybbh,nbbm,hzxm,nl,yblxmc,jsrq,jcxmmc,jcxmkzcs,ksmc,jcdwmc,jcdw,flg_qf,qf,syrq,shxx_sqsj,sqr,shxx_shsj,shxx_sftg,shxx_lrryxm,shxx_gwmc,sqrxm,shxx_sftgmc,shxx_shxxid,shxx_shyj'}" //筛选出这部分字段用于列表显示
            // 搜索框使用
            // search:params.search
        };

//		return getMaterialAuditSearchData(map);
        var experiment_select=$("#RFSAudit_audited_formSearch #cxtj").val();
        var experiment_input=$.trim(jQuery('#RFSAudit_audited_formSearch #cxnr').val());
        if(experiment_select=="0"){
            map["ybbh"]=experiment_input
        }else if(experiment_select=="1"){
            map["nbbm"]=experiment_input
        }else if(experiment_select=="2"){
            map["hzxm"]=experiment_input
        }
        //检测单位
        var jcdws=$("#RFSAudit_audited_formSearch #jcdw_id_tj").val();
        map["jcdws"]=jcdws


        map["dqshzt"] = 'ysh';
        return map;
    };
    return oTableInit;
}


var recheckExperimentConfig={
    width		: "800px",
    modalName	: "recheckExperimentModal",
    formName	: "recheck_Form",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		:	{
        success : 	{
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#recheck_Form").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#recheck_Form input[name='access_token']").val($("#ac_tk").val());
                var ybztcskz = $("#recheck_Form #ybztcskz").val();
                if(ybztcskz=="1"){
                    $.alert("量仅一次");
                    return false;
                }
                if($("#recheck_Form .yy")!=null && $("#recheck_Form .yy").length>0){
                    var yys="";
                    var yymcs="";
                    for(var i=0;i<$("#recheck_Form .yy").length;i++){
                        if($("#recheck_Form .yy")[i].checked){
                            yys=yys+","+$("#recheck_Form .yy")[i].value;
                            yymcs=yymcs+";"+$("#recheck_Form #yy_"+$("#recheck_Form .yy")[i].value+" span").text();
                        }
                    }
                    $("#recheck_Form #yys").val(yys.substring(1));
                    $("#recheck_Form #yy").val(yymcs.substring(1));
                }
                submitForm(opts["formName"]||"recheck_Form",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        //提交审核
                        if(responseText["auditType"]!=null){
                            showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                searchRFS_audit_Result();
                            });
                        }else{
                            searchRFS_audit_Result();
                        }
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"],function() {
                        });
                    }
                },".modal-footer > button");
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}

var viewExperimentConfig = {
    width		: "900px",
    modalName	:"viewExperimentModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var uploadDetectionConfig = {
    width		: "900px",
    modalName	: "uploadDetectionModal",
    formName	: "uploadFileAjaxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {

                if(!$("#uploadFileAjaxForm #fjids").val()){
                    $.error("请上传文件！")
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#uploadFileAjaxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"uploadFileAjaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.closeModal(opts.modalName);
                        $.showDialog("/experimentS/experimentS/pagedataUpView?fjid="+responseText["fjids"],'上传进度',viewUpViewConfig);
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"],function() {
                        });
                    }
                },".modal-footer > button");
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var viewUpViewConfig = {
    width		: "900px",
    modalName	:"viewUpViewModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
//待审核列表的按钮路径
function RFS_audit_DealById(id,action,tourl,qf,flg_qf){
    if(!tourl){
        return;
    }

    if(action=="view"){
        var url=tourl+"?sjid="+id+"&qf="+qf+"&flg_qf="+flg_qf;
        $.showDialog(url,'查看信息',viewExperimentConfig);
    }else if(action=="upload"){
        var url=tourl+"?sjid="+id+"&qf="+qf+"&flg_qf="+flg_qf;
        $.showDialog(url,'上传文件',uploadDetectionConfig);
    }else if(action=="viewmore"){
        var url=tourl+"?sjid="+id+"&qf="+qf+"&flg_qf="+flg_qf;
        $.showDialog(url,'详细信息',viewExperimentConfig);
    }else if(action=="recheck"){
        var url=tourl+"?sjid="+id;
        $.showDialog(url,'复测加测申请',recheckExperimentConfig);
    }else if(action=="audit"){
        let type = "";
        if (flg_qf == "1"){
            type = "AUDIT_RFS_FC"
        }else{
            type = "AUDIT_RFS_SJ"
        }
        $("#RFSAudit_formAudit  #flag").val(flg_qf);
        showAuditDialogWithLoading({
            id: id,
            type: type,
            url:tourl,
            data:{'ywzd':'sjid'},
            title:"ResFirst审核",
            preSubmitCheck:'preSubmitputmaterial',

            callback:function(){
                searchRFS_audit_Result(true);//回调
            },
            dialogParam:{width:1200}
        });
    }
}


function preSubmitputmaterial(){
    let string = "";
    let size_hui = $("#auditAjaxForm  #size_hui").val();
    let size_yang = $("#auditAjaxForm  #size_yang").val();
    let size_yin = $("#auditAjaxForm  #size_yin").val();
    if (size_hui*1 > 0){
        for (let i = 0; i < size_hui; i++) {
            string+= "," + $("#auditAjaxForm input[name='hui_dtoList_"+i+"']:checked").val()
        }
    }
    if (size_yang*1 > 0){
        for (let i = 0; i < size_yang; i++) {
            string+= "," + $("#auditAjaxForm input[name='yang_dtoList_"+i+"']:checked").val()
        }
    }
    if (size_yin*1 > 0){
        for (let i = 0; i < size_yin; i++) {
            string+= "," + $("#auditAjaxForm input[name='yin_dtoList_"+i+"']:checked").val()
        }
    }
    if (string){
        $("#auditAjaxForm  #strings").val(string.substring(1));
    }
    return true;
}

var RFS_audit_oButtton=function(){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query=$("#RFSAudit_audit_formSearch #btn_query");//模糊查询（待审核）
        var btn_queryAudited = $("#RFSAudit_audited_formSearch #btn_query");//模糊查询（审核历史）
        var btn_cancelAudit = $("#RFSAudit_audited_formSearch #btn_cancelAudit");//取消审核（审核历史）
        var btn_audited_viewmore = $("#RFSAudit_audited_formSearch #btn_audited_viewmore");//查看详细
        var btn_view = $("#RFSAudit_audit_formSearch #btn_view");//查看页面（待审核）
        var btn_viewmore = $("#RFSAudit_audit_formSearch #btn_viewmore");//查看页面（待审核）
        var btn_audit = $("#RFSAudit_audit_formSearch #btn_audit");//审核（待审核）
        var btn_upload = $("#RFSAudit_audit_formSearch #btn_upload");
        var btn_recheck = $("#RFSAudit_audit_formSearch #btn_recheck");


        /*-----------------------模糊查询(审核列表)------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchRFS_audit_Result(true);
            });
        }

        /*-----------------------模糊查询(审核记录)------------------------------------*/
        if(btn_queryAudited!=null){
            btn_queryAudited.unbind("click").click(function(){
                searchProduce_audited_Result(true);
            });
        }

        //---------------------------------检验结果上传----------------------------------
        btn_upload.unbind("click").click(function(){
            var sel_row = $('#RFSAudit_audit_formSearch #RFS_auditing_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].jcxmkzcs == 'F'){
                    RFS_audit_DealById(sel_row[0].sjid,"upload",btn_upload.attr("tourl"),sel_row[0].qf,sel_row[0].flg_qf);
                }else {
                    $.error("请选择ResFirst项目的数据！");
                }
            }else{
                RFS_audit_DealById(null,"upload",btn_upload.attr("tourl"));

            }
        });

        /*---------------------------查看页面--------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#RFSAudit_audit_formSearch #RFS_auditing_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                RFS_audit_DealById(sel_row[0].sjid,"view",btn_view.attr("tourl"),sel_row[0].qf,sel_row[0].flg_qf);
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------查看详情页面--------------------------------*/
        btn_viewmore.unbind("click").click(function(){
            var sel_row=$('#RFSAudit_audit_formSearch #RFS_auditing_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                RFS_audit_DealById(sel_row[0].sjid,"viewmore",btn_viewmore.attr("tourl"),sel_row[0].qf,sel_row[0].flg_qf);
            }else{
                $.error("请选中一行");
            }
        });

        /*---------------------------查看详情页面--------------------------------*/
        btn_audited_viewmore.unbind("click").click(function(){
            var sel_row=$('#RFSAudit_audited_formSearch #RFS_audited_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                RFS_audit_DealById(sel_row[0].sjid,"viewmore",btn_viewmore.attr("tourl"),sel_row[0].qf,sel_row[0].flg_qf);
            }else{
                $.error("请选中一行");
            }
        });

        /*---------------------------审核--------------------------------*/
        btn_audit.unbind("click").click(function(){
            var sel_row = $('#RFSAudit_audit_formSearch #RFS_auditing_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                RFS_audit_DealById(sel_row[0].sjid,"audit",btn_audit.attr("tourl"),sel_row[0].qf,sel_row[0].flg_qf);
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------查看审核记录--------------------------------*/
        //选项卡切换事件回调
        $('#RFSAudit_formAudit #RFSAudit_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#",'');
            if(!e.target.isLoaded){//只调用一次
                if(_hash=='RFSAudit_auditing'){
                    var oTable= new RFS_audit_TableInit();
                    oTable.Init();

                }else{
                    var oTable= new RFS_audited_TableInit();
                    oTable.Init();
                }
                e.target.isLoaded = true;
            }else{//重新加载
                //$(_gridId + ' #RFS_audited_list').bootstrapTable('refresh');
            }
        }).first().trigger('shown.bs.tab');//触发第一个选中事件

        /*-------------------------------------取消审核-----------------------------------*/
        if(btn_cancelAudit!=null){
            btn_cancelAudit.unbind("click");
            btn_cancelAudit.click(function(){
                var materialAudit_params=[];

                cancelAudit($('#RFSAudit_audited_formSearch #RFS_audited_list').bootstrapTable('getSelections'),function(){
                    searchProduce_audited_Result();
                },null,materialAudit_params);
            })
        }

        /*-----------------------------------------------------------------------------*/
        /*-----------------------显示隐藏------------------------------------*/
        $("#RFSAudit_audit_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(RFS_audit_turnOff){
                $("#RFSAudit_audit_formSearch #searchMore").slideDown("low");
                RFS_audit_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#RFSAudit_audit_formSearch #searchMore").slideUp("low");
                RFS_audit_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
        //-------------------------------------复检---------------------------------------
        btn_recheck.unbind("click").click(function(){
            var sel_row = $('#RFSAudit_audit_formSearch #RFS_auditing_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].flg_qf!='0'){
                    $.error("请选择正常送检的数据!");
                    return;
                }else{
                    RFS_audit_DealById(sel_row[0].sjid,"recheck",btn_recheck.attr("tourl"));
                }

            }else{
                $.error("请选中一行!");
                return;
            }
        })
        /*-----------------------显示隐藏------------------------------------*/
        $("#RFSAudit_audited_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(RFS_audited_turnOff){
                $("#RFSAudit_audited_formSearch #searchMore").slideDown("low");
                RFS_audited_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#RFSAudit_audited_formSearch #searchMore").slideUp("low");
                RFS_audited_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    }
    return oInit;
}
var RFS_audit_turnOff=true;
var RFS_audited_turnOff=true;

function searchRFS_audit_Result(isTurnBack){
    //关闭高级搜索条件
    $("#RFSAudit_audit_formSearch #searchMore").slideUp("low");
    RFS_audit_turnOff=true;
    $("#RFSAudit_audit_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#RFSAudit_audit_formSearch #RFS_auditing_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#RFSAudit_audit_formSearch #RFS_auditing_list').bootstrapTable('refresh');
    }
}

function searchProduce_audited_Result(isTurnBack){
    //关闭高级搜索条件
    $("#RFSAudit_audited_formSearch #searchMore").slideUp("low");
    RFS_audited_turnOff=true;
    $("#RFSAudit_audited_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#RFSAudit_audited_formSearch #RFS_audited_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#RFSAudit_audited_formSearch #RFS_audited_list').bootstrapTable('refresh');
    }
}

$(function(){
    var oTable= new RFS_audit_TableInit();
    oTable.Init();

    var oButtonInit = new RFS_audit_oButtton();
    oButtonInit.Init();
    $("#RFSAudit_audit_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
    $("#RFSAudit_audited_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
    // 所有下拉框添加choose样式
    jQuery('#RFSAudit_audit_formSearch .chosen-select').chosen({width: '100%'});
    jQuery('#RFSAudit_audited_formSearch .chosen-select').chosen({width: '100%'});

    if ($("#RFSAudit_audit_formSearch #btn_viewmore").length <=0){
        $("#RFSAudit_audited_formSearch #btn_audited_viewmore").attr("style","display: none;")
    }
})

/**
 * 状态
 * @returns
 */
function ztFormat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sjid + "\",event,\"\")' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sjid + "\",event,\"\")' >审核未通过</a>";
    }else{
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sjid + "\",event,\"\")' >" + row.shxx_gwmc + "审核中</a>";
    }
}

/**
 * 查看审核信息
 * @param xmid
 * @param event
 * @param shlb
 */
function showAuditInfo(ywid,event,shlb,params){
    if(shlb.split(",").length>1){
        $.showDialog(
            (params?(params.prefix?params.prefix:""):"") + auditViewUrl,
            '审核信息',
            $.extend({},viewConfig,{"width":"960px",data:{'ywid':ywid,'shlbs':shlb}})
        );
    }else{
        $.showDialog(
            (params?(params.prefix?params.prefix:""):"") + auditViewUrl,
            '审核信息',
            $.extend({},viewConfig,{"width":"960px",data:{'ywid':ywid,'shlb':shlb}})
        );
    }
    event.stopPropagation();
}