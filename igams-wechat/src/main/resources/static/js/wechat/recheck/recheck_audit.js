var Recheck_turnOff=true;
var Recheck_audit_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#recheck_audit_formSearch  #recheck_list").bootstrapTable({
			url: '/recheck/recheck/pageGetListRecheck_Audit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#recheck_audit_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "fjsq.fjid",				//排序字段
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
            uniqueId: "fjid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            },{title: '序号',
				formatter: function (value, row, index) {
					return index+1;
				},
				width: '3%',
                align: 'left',
                visible:true
             },{
                 field: 'fjid',
                 title: '复测id',
                 width: '10%',
                 align: 'left',
                 visible:false
             },{
                 field: 'sjid',
                 title: '送检id',
                 width: '10%',
                 align: 'left',
                 visible: false
             },{
                 field: 'nbbm',
                 title: '内部编码',
                 width: '12%',
                 align: 'left',
                 visible: true
             },{
                 field: 'ybbh',
                 title: '标本编号',
                 width: '12%',
                 align: 'left',
                 visible: true
             },{
                 field: 'hzxm',
                 title: '患者姓名',
                 width: '8%',
                 align: 'left',
                 visible: true
             },{
                 field: 'yblxmc',
                 title: '标本类型',
                 width: '8%',
                 align: 'left',
                 visible: true
             },{
                 field: 'jsrq',
                 title: '接收日期',
                 width: '12%',
                 align: 'left',
                 visible: true
             },{
                 field: 'lxmc',
                 title: '复测类型',
                 width: '8%',
                 align: 'left',
                 visible: true
             },{
                 field: 'bgbjmc',
                 title: '报告重发',
                 width: '6%',
                 align: 'left',
                 visible:true
             },{
                 field: 'jcxmmc',
                 title: '检测项目',
                 width: '14%',
                 align: 'left',
                 visible: true
             },{
                field: 'jczxmmc',
                title: '子项目',
                width: '14%',
                align: 'left',
                visible: true
            },{
                 field: 'sfsc',
                 title: '是否上传',
                 width: '6%',
                 align: 'left',
                 formatter:sfscFormat,
                 visible: true
             },{
                 field: 'bz',
                 title: '备注',
                 width: '10%',
                 align: 'left',
                 formatter:bzFormat,
                 visible: true
             }/*,{
                 field: 'yy',
                 title: '复测原因',
                 width: '10%',
                 align: 'left',
                 visible: true
             },{
                 field: 'bz',
                 title: '检测备注',
                 width: '10%',
                 align: 'left',
                 visible: true
             }*/,{
                 field: 'lrrymc',
                 title: '录入人员',
                 width: '10%',
                 align: 'left',
                 visible: false
             },{
                 field: 'lrsj',
                 title: '录入时间',
                 width: '10%',
                 align: 'left',
                 sortable: true,   
                 visible: false
             },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '8%',
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
            	recheck_audit_DealById(row.fjid,"view",$("#recheck_audit_formSearch #btn_view").attr("tourl"));
             },
	});
		$("#recheck_audit_formSearch #recheck_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true        
	  });
	}
		oTableInit.queryParams=function(params){
			var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
	        	pageSize: params.limit,   // 页面大小
	        	pageNumber: (params.offset / params.limit) + 1,  // 页码
	            access_token:$("#ac_tk").val(),
	            sortName: params.sort,      // 排序列名
	            sortOrder: params.order, // 排位命令（desc，asc）
	            sortLastName: "fjsq.fjid", // 防止同名排位用
	            sortLastOrder: "asc", // 防止同名排位用
	            zt: "10"
	            // 搜索框使用
	            // search:params.search
	        };
			return getRecheckSearchData(map);
		};
		return oTableInit;
}

/**
 * 审核列表的状态格式化函数
 * @returns
 */
function ztFormat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return '审核通过';
    }
    else{
        return row.shxx_gwmc + '审核中';
    }
}

//是否上传格式化
function sfscFormat(value,row,index){
	if(row.sfsc=="1"){
		var sfsc="<span>是</span>"
		return sfsc;
	}else{
		var sfsc="<span style='color:red;'>否</span>"
		return sfsc;
	}
}
/**
 * 审核记录列表
 */
var recheck_Audited_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#recheck_audited_formSearch #tb_list").bootstrapTable({
			url: '/recheck/recheck/pageGetListRecheck_Audit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#recheck_audited_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "shxx_shsj",				//排序字段
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
            uniqueId: "fjid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            isForceTable:true,
            columns: [{
                checkbox: true
            },{
                field: 'nbbm',
                title: '内部编码',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'ybbh',
                title: '标本编号',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'hzxm',
                title: '患者姓名',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'yblxmc',
                title: '标本类型',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'lxmc',
                title: '复测类型',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'jcxmmc',
                title: '检测项目',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '10%',
                align: 'left',
                formatter:bzFormat,
                visible: true
            },{
                field: 'sqrxm',
                title: '申请人员',
                titleTooltip:'申请人员',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                titleTooltip:'申请时间',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_lrryxm',
                title: '审核人',
                titleTooltip:'审核人',
                align: 'left',
                width: '6%',
                visible: true
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                titleTooltip:'审核时间',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                titleTooltip:'审核意见',
                width: '6%',
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
            },{
                field: 'shxx_shxxid',
                align: 'center',
                title: '审核信息ID',
                visible: false
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	recheck_audit_DealById(row.fjid,"view",$("#recheck_audit_formSearch #btn_view").attr("tourl"));
             },
		});
		$("#recheck_audited_formSearch #tb_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true        
		});
	}
		oTableInit.queryParams=function(params){
			var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
	        	pageSize: params.limit,   // 页面大小
	        	pageNumber: (params.offset / params.limit) + 1,  // 页码
	            access_token:$("#ac_tk").val(),
	            sortName: params.sort,      // 排序列名
	            sortOrder: params.order, // 排位命令（desc，asc）
	            sortLastName: "fjsq.fjid", // 防止同名排位用
	            sortLastOrder: "asc" // 防止同名排位用
	            // 搜索框使用
	            // search:params.search
	        };
            var cxtj_audited=$("#recheck_audited_formSearch #cxtj_audited").val();
            var cxnr_audited=$.trim(jQuery('#recheck_audited_formSearch #cxnr_audited').val());
            if(cxtj_audited=="0"){
                map["yy"]=cxnr_audited;
            }else if(cxtj_audited=="1"){
                map["bz"]=cxnr_audited;
            }else if(cxtj_audited=="2"){
                map["jcxm"]=cxnr_audited;
            }else if(cxtj_audited=="3"){
                map["hzxm"]=cxnr_audited;
            }else if(cxtj_audited=="4"){
                map["nbbm"]=cxnr_audited;
            }
            //类型
            var lxs = jQuery('#recheck_audited_formSearch #lx_id_tj').val();
            map["lxs"] = lxs;
            //发送报告
            var bgbjs = jQuery('#recheck_audited_formSearch #bgbj_id_tj').val();
            map["bgbjs"] = bgbjs;
            //审核状态
            var zts = jQuery('#recheck_audited_formSearch #zt_id_tj').val();
            map["zts"] = zts;
            //是否付费
            var sfffs = jQuery('#recheck_audited_formSearch #sfff_id_tj').val();
            map["sfffs"] = sfffs;
            //检测单位
            var jcdws = jQuery('#recheck_audited_formSearch #jcdw_id_tj').val();
            map["jcdws"] = jcdws;
            //标本类型
            var yblxs = jQuery('#recheck_audited_formSearch #yblx_id_tj').val();
            map["yblxs"] = yblxs;
            //检测项目
            var jcxmss = jQuery('#recheck_audited_formSearch #jcxm_id_tj').val();
            map["jcxmss"] = jcxmss;
            // 实验开始日期
            var syrqstart = jQuery('#recheck_audited_formSearch #syrqstart').val();
            map["syrqstart"] = syrqstart;
            // 实验结束日期
            var syrqend = jQuery('#recheck_audited_formSearch #syrqend').val();
            map["syrqend"] = syrqend;
            // D实验开始日期
            var dsyrqstart = jQuery('#recheck_audited_formSearch #dsyrqstart').val();
            map["dsyrqstart"] = dsyrqstart;
            // D实验结束日期
            var dsyrqend = jQuery('#recheck_audited_formSearch #dsyrqend').val();
            map["dsyrqend"] = dsyrqend;
            // 申请开始日期
            var sqsjstart = jQuery('#recheck_audited_formSearch #sqsjstart').val();
            map["sqsjstart"] = sqsjstart;
            // 申请结束日期
            var sqsjend = jQuery('#recheck_audited_formSearch #sqsjend').val();
            map["sqsjend"] = sqsjend;
            map["dqshzt"] = 'ysh';
            return map;
		}
		return oTableInit;
}
function getRecheckSearchData(map){
    var cxtj=$("#recheck_audit_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#recheck_audit_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["yy"]=cxnr;
    }else if(cxtj=="1"){
        map["bz"]=cxnr;
    }else if(cxtj=="2"){
        map["jcxm"]=cxnr;
    }else if(cxtj=="3"){
        map["hzxm"]=cxnr;
    }else if(cxtj=="4"){
        map["lrrymc"]=cxnr;
    }else if(cxtj=="5"){
        map["nbbm"]=cxnr;
    }
    //类型
    var lxs = jQuery('#recheck_audit_formSearch #lx_id_tj').val();
    map["lxs"] = lxs;
    //发送报告
    var bgbjs = jQuery('#recheck_audit_formSearch #bgbj_id_tj').val();
    map["bgbjs"] = bgbjs;
    //审核状态
    var zts = jQuery('#recheck_audit_formSearch #zt_id_tj').val();
    map["zts"] = zts;
    //是否付费
    var sfffs = jQuery('#recheck_audit_formSearch #sfff_id_tj').val();
    map["sfffs"] = sfffs;
    //检测单位
    var jcdws = jQuery('#recheck_audit_formSearch #jcdw_id_tj').val();
    map["jcdws"] = jcdws;
    //标本类型
    var yblxs = jQuery('#recheck_audit_formSearch #yblx_id_tj').val();
    map["yblxs"] = yblxs;
    //检测项目
    var jcxmss = jQuery('#recheck_audit_formSearch #jcxm_id_tj').val();
    map["jcxmss"] = jcxmss;
    // 实验开始日期
    var syrqstart = jQuery('#recheck_audit_formSearch #syrqstart').val();
    map["syrqstart"] = syrqstart;
    // 实验结束日期
    var syrqend = jQuery('#recheck_audit_formSearch #syrqend').val();
    map["syrqend"] = syrqend;
    // D实验开始日期
    var dsyrqstart = jQuery('#recheck_audit_formSearch #dsyrqstart').val();
    map["dsyrqstart"] = dsyrqstart;
    // D实验结束日期
    var dsyrqend = jQuery('#recheck_audit_formSearch #dsyrqend').val();
    map["dsyrqend"] = dsyrqend;
    return map;
}

/**
 * 备注格式化
 * @returns
 */
function bzFormat(value,row,index){
	if(row.bz==null){
		return row.yy;
	}else{
		return row.bz;
	}
}
/**
 * 按钮点击函数
 * @param id
 * @param action
 * @param tourl
 * @returns
 */
function recheck_audit_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action=="view"){
		var url=tourl+"?fjid="+id;
		$.showDialog(url,'查看信息',ViewRecheck_audit_Config);
	}else if(action=="audit"){
		var url= tourl;
		showAuditDialogWithLoading({
			id: id,
			type: 'AUDIT_RECHECK',
			url:$("#recheck_audit_formSearch #btn_audit").attr("tourl"),
			data:{'ywzd':'fjid'},
			title:"复测审核",
			preSubmitCheck:'preSubmitRecheck',
			callback:function(){
				search_recheck_Audit();//回调
			},
			dialogParam:{width:1000}
		});
	}else if(action=='detection'){
        var url=tourl + "?ids=" +id;
        $.showDialog(url,'修改检测状态',modDetectionConfig);
    }else if(action=="confirm"){
        var url= tourl;
        showAuditDialogWithLoading({
            id: id,
            type: 'AUDIT_RECHECK',
            url:$("#recheck_audit_formSearch #btn_confirm").attr("tourl"),
            data:{'ywzd':'fjid'},
            title:"复测审核",
            preSubmitCheck:'preConfirmRecheck',
            callback:function(){
                search_recheck_Audit();//回调
            },
            dialogParam:{width:1000}
        });
	}
}

/**
 * 修改检测状态，实验按钮
 */
var modDetectionConfig = {
    width		: "800px",
    modalName	: "modDetectionConfig",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                var sfsytz="0";
                if(($("#detectionFjsqForm #syrqFj").val()==null || $("#detectionFjsqForm #syrqFj").val()=='') && ($("#detectionFjsqForm #syrq").val()!=null && $("#detectionFjsqForm #syrq").val()!='')){
                    $("#detectionFjsqForm #sfsytz").val("1");
                }
                if(($("#detectionFjsqForm #dsyrqFj").val()==null || $("#detectionFjsqForm #dsyrqFj").val()=='') && ($("#detectionFjsqForm #dsyrq").val()!=null && $("#detectionFjsqForm #dsyrq").val()!='')){
                    $("#detectionFjsqForm #sfsytz").val("1");
                }
                if(($("#detectionFjsqForm #qtsyrqFj").val()==null || $("#detectionForm #qtsyrqfirst").val()=='') && ($("#detectionForm #qtsyrq").val()!=null && $("#detectionForm #qtsyrq").val()!='')){
                    $("#detectionFjsqForm #sfsytz").val("1");
                }
                $("#detectionFjsqForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"detectionFjsqForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            if(responseText["ywid"]!=null && responseText["ywid"]!=''){
                                //提交审核
                                var auditType = responseText["auditType"];
                                showAuditFlowDialog(auditType,responseText["ywid"],function(){
                                    searchRecheckResult();
                                });
                            }else{
                                searchRecheckResult();
                            }
                        });
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
function preSubmitRecheck(){

	if($("#fixtop #sftg").val()=="1"&&$("#fixtop #lastStep").val()=="true"){
		if($("#recheck_mod_Form  #dsyrq").prop("disabled")!=true&&($("#recheck_mod_Form  #dsyrq").val()==null || $("#recheck_mod_Form  #dsyrq").val()=="")){
			$.error("请选择DNA实验日期！");
			return false;
		}
		if($("#recheck_mod_Form  #syrq").prop("disabled")!=true&&($("#recheck_mod_Form  #syrq").val()==null || $("#recheck_mod_Form  #syrq").val()=="")){
			$.error("请选择RNA实验日期！");
			return false;
		}
		if($("#recheck_mod_Form  #qtsyrq").prop("disabled")!=true&&($("#recheck_mod_Form  #qtsyrq").val()==null || $("#recheck_mod_Form  #qtsyrq").val()=="")){
			$.error("请选择其他实验日期！");
			return false;
		}
	}
	return true;
}

function preConfirmRecheck(){

    if($("#fixtop #sftg").val()=="1"&&$("#fixtop #lastStep").val()=="true"){
        if($("#recheckAjaxForm  #dsyrq").prop("disabled")!=true&&($("#recheckAjaxForm  #dsyrq").val()==null || $("#recheckAjaxForm  #dsyrq").val()=="")){
            $.error("请选择DNA实验日期！");
            return false;
        }
        if($("#recheckAjaxForm  #syrq").prop("disabled")!=true&&($("#recheckAjaxForm  #syrq").val()==null || $("#recheckAjaxForm  #syrq").val()=="")){
            $.error("请选择RNA实验日期！");
            return false;
        }
        if($("#recheckAjaxForm  #qtsyrq").prop("disabled")!=true&&($("#recheckAjaxForm  #qtsyrq").val()==null || $("#recheckAjaxForm  #qtsyrq").val()=="")){
            $.error("请选择其他实验日期！");
            return false;
        }
    }
    return true;
}

var ViewRecheck_audit_Config={
		width		: "1000px",
		modalName	:"ViewRecheck_audit_Config",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
}
/**
 * 初始化按钮
 */
var Recheck_audit_oButtton=function(){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#recheck_audit_formSearch #btn_query");//模糊查询
		var btn_view = $("#recheck_audit_formSearch #btn_view");//查看页面
		var btn_audit = $("#recheck_audit_formSearch #btn_audit");//审核
        var btn_confirm = $("#recheck_audit_formSearch #btn_confirm");//审核
		var btn_queryAudited = $("#recheck_audited_formSearch #btn_queryAudited");//审核记录列表模糊查询
    	var btn_cancelAudit = $("#recheck_audited_formSearch #btn_cancelAudit");//取消审核
        var btn_detection = $("#recheck_audit_formSearch #btn_detection")//实验
        var btn_selectexport = $("#recheck_audit_formSearch #btn_selectexport");//选中导出
        var btn_searchexport = $("#recheck_audit_formSearch #btn_searchexport");//搜索导出
    	//添加日期控件
    	laydate.render({
    	   elem: '#recheck_audited_formSearch #sqsjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#recheck_audited_formSearch #sqsjend'
    	  ,theme: '#2381E9'
    	});
        //添加日期控件-实验开始日期
        laydate.render({
            elem: '#recheck_audited_formSearch #syrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件-实验结束日期
        laydate.render({
            elem: '#recheck_audited_formSearch #syrqend'
            ,theme: '#2381E9'
        });
        //添加日期控件-D实验开始日期
        laydate.render({
            elem: '#recheck_audited_formSearch #dsyrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件-D实验结束日期
        laydate.render({
            elem: '#recheck_audited_formSearch #dsyrqend'
            ,theme: '#2381E9'
        });


        //添加日期控件-实验开始日期
        laydate.render({
            elem: '#recheck_audit_formSearch #syrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件-实验结束日期
        laydate.render({
            elem: '#recheck_audit_formSearch #syrqend'
            ,theme: '#2381E9'
        });
        //添加日期控件-D实验开始日期
        laydate.render({
            elem: '#recheck_audit_formSearch #dsyrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件-D实验结束日期
        laydate.render({
            elem: '#recheck_audit_formSearch #dsyrqend'
            ,theme: '#2381E9'
        });
        //-----------------------实验(功能：修改DNA/RNA的实验状态)-------------------------------------------------
        btn_detection.unbind("click").click(function(){
            var sel_row = $('#recheck_audit_formSearch #recheck_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else{
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {
                    ids= ids + ","+ sel_row[i].fjid;
                }
                ids=ids.substr(1);
                var checkjcxm=true;
                $.ajax({
                    type:'post',
                    url:"/recheck/recheck/pagedataCheckJcxm",
                    cache: false,
                    data: {"ids":ids,"access_token":$("#ac_tk").val()},
                    dataType:'json',
                    success:function(data){
                        //返回值
                        if(data==false){
                            $.error("检测项目必须相同!");
                        }else{
                            recheck_audit_DealById(ids,"detection",btn_detection.attr("tourl"));
                        }
                    }
                });
            }
        });

        /*-----------------------模糊查询(审核列表)------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				search_recheck_Audit();
    		});
		}
		//-----------------------模糊查询(审核记录)------------------------------------
		if(btn_queryAudited!=null){
			btn_queryAudited.unbind("click").click(function(){
				search_recheck_Audited();
    		});
		}
        //---------------------------确认--------------------------------
        btn_confirm.unbind("click").click(function(){
            var sel_row = $('#recheck_audit_formSearch #recheck_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                recheck_audit_DealById(sel_row[0].fjid,"confirm",btn_confirm.attr("tourl"));
            }else if(sel_row.length==0){
                recheck_audit_DealById(null,"confirm",btn_confirm.attr("tourl"));
            }
        });
		//---------------------------审核--------------------------------
		btn_audit.unbind("click").click(function(){
    		var sel_row = $('#recheck_audit_formSearch #recheck_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			recheck_audit_DealById(sel_row[0].fjid,"audit",btn_audit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		//---------------------------查看页面--------------------------------
		btn_view.unbind("click").click(function(){
			var sel_row=$('#recheck_audit_formSearch #recheck_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				recheck_audit_DealById(sel_row[0].fjid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
        /*--------------------------------------显示隐藏-----------------------------------------*/
        $("#recheck_audit_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(Recheck_turnOff){
                $("#recheck_audit_formSearch #searchMore").slideDown("low");
                Recheck_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#recheck_audit_formSearch #searchMore").slideUp("low");
                Recheck_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
        /*--------------------------------------显示隐藏-----------------------------------------*/
        $("#recheck_audited_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(Recheck_turnOff){
                $("#recheck_audited_formSearch #searchMore").slideDown("low");
                Recheck_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#recheck_audited_formSearch #searchMore").slideUp("low");
                Recheck_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
        /*---------------------------查看审核记录--------------------------------*/
        // 选项卡切换事件回调
        $('#recheck_formAudit #recheck_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#",'');
            if(!e.target.isLoaded){// 只调用一次
                if(_hash=='recheck_auditing'){
                    var oTable= new Recheck_audit_TableInit();
                    oTable.Init();

                }else{
                    var oTable= new recheck_Audited_TableInit();
                    oTable.Init();
                }
                e.target.isLoaded = true;
            }else{// 重新加载
                // $(_gridId + ' #tb_list').bootstrapTable('refresh');
            }
        }).first().trigger('shown.bs.tab');// 触发第一个选中事件
    	/*-------------------------------------取消审核-----------------------------------*/
    	if(btn_cancelAudit!=null){
    		btn_cancelAudit.unbind("click");
    		btn_cancelAudit.click(function(){
    			cancelAudit($('#recheck_audited_formSearch  #tb_list').bootstrapTable('getSelections'),function(){
    				search_recheck_Audited();
    			});
    		})
    	}
        //---------------------------------选中导出---------------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#recheck_audit_formSearch #recheck_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length!=0){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].fjid;
                }
                ids = ids.substr(1);
                $.showDialog(exportPrepareUrl+"?ywid=RECHECK_AUDIT_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });
        //搜索导出
        btn_searchexport.unbind("click").click(function(){
            $.showDialog(exportPrepareUrl+"?ywid=RECHECK_AUDIT_SEARCH&expType=search&callbackJs=recheckSearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
	}
	return oInit;
}
//提供给导出用的回调函数
function recheckSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="fjsq.fjid";
    map["sortLastOrder"]="asc";
    map["sortName"]="fjsq.ybbh";
    map["sortOrder"]="asc";
    return getRecheckSearchData(map);
}
/**
 * 刷新列表
 * @returns
 */
function search_recheck_Audit(){
    $("#recheck_audit_formSearch #searchMore").slideUp("low");
    Recheck_turnOff=true;
    $("#recheck_audit_formSearch #sl_searchMore").html("高级筛选");
	$('#recheck_audit_formSearch #recheck_list').bootstrapTable('refresh');
}
/**
 * 刷新列表
 * @returns
 */
function search_recheck_Audited(){
    $("#recheck_audited_formSearch #searchMore").slideUp("low");
    Recheck_turnOff=true;
    $("#recheck_audited_formSearch #sl_searchMore").html("高级筛选");
    $('#recheck_audited_formSearch #tb_list').bootstrapTable('refresh');
}
$(function(){
	var oTable= new Recheck_audit_TableInit();
		oTable.Init();
	var oButtonInit = new Recheck_audit_oButtton();
		oButtonInit.Init();
    $("#recheck_audit_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
    $("#recheck_audited_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
	jQuery('#recheck .chosen-select').chosen({width: '100%'});
})