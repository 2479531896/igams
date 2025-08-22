var Recheck_turnOff=true;

var Recheck_TableInit=function(){
	var auth_flag=$("#recheck_formSearch #auth_flag").val();
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#recheck_formSearch #recheck_list").bootstrapTable({
			url: '/recheck/recheck/pagedataListRecheck?auth_flag='+auth_flag,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#recheck_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "fjsq.lrsj",				//排序字段
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
            /*rowStyle:rowStyle,                  //通过自定义函数设置行样式
*/          columns: [{
                checkbox: true
            },{
            	title: '序号',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'fjid',
                title: '复测id',
                width: '5%',
                align: 'left',
                sortable: true,   
                visible:false
            },{
                field: 'sjid',
                title: '送检id',
                width: '5%',
                align: 'left',
                sortable: true,   
                visible: false
            },{
                field: 'ybbh',
                title: '标本编号',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'nbbm',
                title: '内部编码',
                width: '10%',
                align: 'left',
                sortable: true,   
                visible:true
            },{
                field: 'hzxm',
                title: '患者姓名',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'nl',
                title: '年龄',
                width: '6%',
                align: 'left',
                sortable: true,   
                visible: false
            },{
                field: 'yblxmc',
                title: '标本类型',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'jsrq',
                title: '接收日期',
                width: '8%',
                sortable: true,    
                align: 'left',
                visible: false
            },{
                field: 'db',
                title: '合作伙伴',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'lxmc',
                title: '复测类型',
                width: '6%',
                align: 'left',
                sortable: true,   
                visible: true
            },{
                field: 'jcdwmc',
                title: '检测单位',
                width: '8%',
                align: 'left',
                sortable: true,   
                visible: true
            },{
                field: 'bgbj',
                title: '报告重发',
                width: '10%',
                align: 'left',
                formatter:bgbjFormat,
                sortable: true,   
                visible:false
            },{
                field: 'jcxmmc',
                title: '加测项目',
                width: '13%',
                align: 'left',
                formatter:xmmcFormat,
                sortable: true,   
                visible: true
            },{
                field: 'sfff',
                title: '收费',
                width: '5%',
                align: 'left',
                sortable: true,
                formatter:sfffFormat,
                visible: true
            },{
                 field: 'bgrq',
                 title: '报告日期',
                 width: '12%',
                 align: 'left',
                 sortable: true,
                 visible: true
             },{
                field: 'bz',
                title: '备注',
                width: '13%',
                align: 'left',
                sortable: true, 
                formatter:bzFormat,
                visible: true
            },{
                field: 'sfsc',
                title: '上传',
                width: '3%',
                align: 'left',
                formatter:sfscFormat,
                visible: true
            },{
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
                visible: false,
                sortable: true   
            },{
                field: 'zt',
                title: '状态',
                width: '10%',
                formatter:ztFormat,
                align: 'left',
                visible: true
            },{
                field: 'cz',
                title: '操作',
                width: '8%',
                formatter:czFormat,
                align: 'left',
                visible: true
            },{
                field: 'yy',
                title: '原因',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'syrq',
                title: 'RNA实验日期',
                width: '14%',
                align: 'left',
                visible: false
            },{
                field: 'dsyrq',
                title: 'DNA实验日期',
                width: '14%',
                align: 'left',
                visible: false
            },{
                field: 'sfgs',
                title: '改善',
                width: '7%',
                align: 'center',
                visible: false
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
				if ($("#recheck_formSearch #btn_viewmore").length>0){
					Recheck_DealById(row.fjid,'viewmore',$("#recheck_formSearch #btn_viewmore").attr("tourl"));
				}else{
					Recheck_DealById(row.fjid,'view',$("#recheck_formSearch #btn_view").attr("tourl"));
				}
             },
		});
		  $("#recheck_formSearch #recheck_list").colResizable({
	            liveDrag:true, 
	            gripInnerHtml:"<div class='grip'></div>", 
	            draggingClass:"dragging", 
	            resizeMode:'fit', 
	            postbackSafe:true,
	            partialRefresh:true}        
	        );
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
			return getRecheckSearchData(map);
			};
	
	return oTableInit;
	}
function getRecheckSearchData(map){
	var cxtj=$("#recheck_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#recheck_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr
	}else if(cxtj=="1"){
		map["hzxm"]=cxnr
	}else if(cxtj=="2"){
		map["nbbm"]=cxnr
	}else if(cxtj=="3"){
		map["ybbh"]=cxnr
	}else if(cxtj=="4"){
		map["bz"]=cxnr
	}else if(cxtj=="5"){
		map["yy"]=cxnr
	}else if(cxtj=="6"){
		map["jcxm"]=cxnr
	}else if(cxtj=="7"){
		map["db"]=cxnr
	}
	//类型
	var lxs = jQuery('#recheck_formSearch #lx_id_tj').val();
		map["lxs"] = lxs;
	//发送报告
	var bgbjs = jQuery('#recheck_formSearch #bgbj_id_tj').val();
		map["bgbjs"] = bgbjs;
	//审核状态
	var zts = jQuery('#recheck_formSearch #zt_id_tj').val();
		map["zts"] = zts;
	//是否付费
	var sfffs = jQuery('#recheck_formSearch #sfff_id_tj').val();
		map["sfffs"] = sfffs;
	//检测单位
	var jcdws = jQuery('#recheck_formSearch #jcdw_id_tj').val();
		map["jcdws"] = jcdws;
	//标本类型
	var yblxs = jQuery('#recheck_formSearch #yblx_id_tj').val();
		map["yblxs"] = yblxs;	
	//检测项目
	var jcxmss = jQuery('#recheck_formSearch #jcxm_id_tj').val();
		map["jcxmss"] = jcxmss;	
	// 实验开始日期
	var syrqstart = jQuery('#recheck_formSearch #syrqstart').val();
		map["syrqstart"] = syrqstart;
	 // 实验结束日期
	var syrqend = jQuery('#recheck_formSearch #syrqend').val();
		map["syrqend"] = syrqend;
	// 录入开始日期
	var lrsjstart = jQuery('#recheck_formSearch #lrsjstart').val();
	map["lrsjstart"] = lrsjstart;
	// 录入结束日期
	var lrsjend = jQuery('#recheck_formSearch #lrsjend').val();
	map["lrsjend"] = lrsjend;
	return map;
}


//提供给导出用的回调函数
function recheckSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="fjsq.fjid";
	map["sortLastOrder"]="asc";
	map["sortName"]="fjsq.lrsj";
	map["sortOrder"]="asc";
	map["auth_flag"]=$("#recheck_formSearch #auth_flag").val();
	map["yhid"]=$("#recheck_formSearch #yhid").val();
	return getRecheckSearchData(map);
}
/**
 * 备注格式化
 * @returns
 */
function bzFormat(value,row,index){
	if(row.bz==null&&row.yy!=null){
		return row.yy;
	}else if(row.bz!=null&&row.yy!=null){
		return row.bz + "  " + row.yy;
	}else if(row.bz!=null&&row.yy==null){
		return row.bz;
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
 * 发送报告格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function bgbjFormat(value,row,index){
	if(row.bgbj=='0'){
		return '不发送';
	}else {
		return '发送';
		}
}

/**
 * 是否付费格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function sfffFormat(value,row,index){
	if(row.sfff=='0'){
		return '不收费';
	}else if(row.sfff=='1'){
		return '收费';
	}
}

/**
 * 是否付费格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function xmmcFormat(value,row,index){
    if(!row.jczxmmc || row.jczxmmc==''){
        return row.jcxmmc;
    }else{
        return row.jcxmmc + "-" + row.jczxmmc;
    }
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
	if (row.zt == '10') {
		return "<span class='btn btn-warning' onclick=\"recallRecheck('" + row.fjid + "','" + row.shlx + "',event)\" >撤回</span>";
	}else{
		return "";
	}
}

/**
 * 货物列表的状态格式化函数
 * @returns
 */
function ztFormat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
    	if(row.shlx){
    		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fjid + "\",event,\"" + row.shlx + "\")' >审核通过</a>";
    	}else{
    		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fjid + "\",event,\"AUDIT_RECHECK\")' >审核通过</a>";
    	}
    }else if (row.zt == '15') {
    	if(row.shlx){
    		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fjid + "\",event,\"" + row.shlx + "\")' >审核未通过</a>";
    	}else{
    		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fjid + "\",event,\"AUDIT_RECHECK\")' >审核未通过</a>";
    	}
    }else{
		if(row.shxx_dqgwmc=='null' || row.shxx_dqgwmc==null){
			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fjid + "\",event,\"" + row.shlx + "\")' >审核终止</a>";
		}else if(row.shlx){
    		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fjid + "\",event,\"" + row.shlx + "\")' >" + row.shxx_dqgwmc + "审核中</a>";
    	}else{
    		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fjid + "\",event,\"AUDIT_RECHECK\")' >" + row.shxx_dqgwmc + "审核中</a>";
    	}
    }
}
//撤回提交
function recallRecheck(fjid,shlx,event){
	var auditType = null;
	if(shlx){
		auditType = shlx;
	}else{
		auditType = $("#recheck_formSearch #auditType").val()
	}
	var msg = '您确定要撤回复测申请吗？';
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,fjid,function(){searchRecheckResult();});
		}
	});
}

/**
 * 按钮回调方法
 * @param id
 * @param action
 * @param tourl
 * @returns
 */
function Recheck_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action=="submit"){
		var url=tourl+"?fjid="+id;
		$.showDialog(url,'提交审核',submitRecheckConfig);
	}else if(action=="mod"){
		var url=tourl+"?fjid="+id;
		$.showDialog(url,'修改信息',modRecheckConfig);
	}else if(action=="view"){
		var url=tourl+"?fjid="+id;
		$.showDialog(url,'查看信息',viewRecheckConfig);
	}else if(action=="viewmore"){
		var url=tourl+"?fjid="+id;
		$.showDialog(url,'详细信息',viewRecheckConfig);
	}else if(action=="refund"){
    	var url=tourl + "?fjid=" +id;
    	$.showDialog(url,'退款申请',payRefundConfig);
    }else if(action=='detection'){
		var url=tourl + "?ids=" +id;
		$.showDialog(url,'修改检测状态',modDetectionFjConfig);
    }else if(action=="advancedmod"){
		var url=tourl+"?fjid="+id;
		$.showDialog(url,'高级修改',modRecheckConfig);
	}else if(action=="rfssend"){
		var url=tourl+"?sjid="+id;
		$.showDialog(url,'ResFirst重发报告',resendConfig);
    }else if(action =='advancedmod'){
		var url=tourl + "?fjid=" +id+"&xg_flg=1";
		$.showDialog(url,'修改送检信息',modRecheckConfig);
	}else if(action =='extend'){
     		var url=tourl + "?fjid=" +id;
     		$.showDialog(url,'修改扩展信息',modExtendConfig);
     }else if(action=="sendreport"){
		var url=tourl+"?sjid="+id;
		$.showDialog(url,'结核耐药发送报告',fcsendReportConfig);
	}else if(action =='upload'){
		var url=tourl + "?fjid=" +id;
		$.showDialog(url,'检测结果上传',uploadInspectionConfig);
	}
}

var uploadInspectionConfig = {
	width		: "900px",
	modalName	: "uploadInspectionModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					return false;
				}
				if(!$("#ajaxForm #fjids").val() && !$("#ajaxForm #w_fjids").val() && !$("#ajaxForm #fjids_q").val() && !$("#ajaxForm #w_fjids_q").val() && !$("#ajaxForm #w_fjids_z").val()){
					$.error("请上传检测报告！")
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
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

/*   发送报告模态框*/
var fcsendReportConfig = {
	width		: "1200px",
	modalName	: "sendReportModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
	success1 : {
    			label : "保 存",
    			className : "btn-success",
    			callback : function() {
    				var $this = this;
    				var opts = $this["options"]||{};
    				$("#sendReportAjaxForm input[name='access_token']").val($("#ac_tk").val());
    				$("#sendReportAjaxForm #bcbj").val("1");
    				var json = [];
    				var nyjson = [];
    				var sjwzxx_jsonStr = $("#sjwzxx_json").val();
    				var sjnyx_jsonStr = $("#sjnyx_json").val();
    				if (sjwzxx_jsonStr){
    					var sjwzxx_json = JSON.parse(sjwzxx_jsonStr);
    					for(var i=0;i<sjwzxx_json.length;i++){
    						if(sjwzxx_json[i].wzid != "" && sjwzxx_json[i].wzid != null){
    							var sz = {"sjwzid" : "","jglx" : "","wzfl" : "","wzid" : "","wzzwm" : "","wzywm" : "","mappingReads" : "","reads" : "","refANI" : "","target" : "","sfhb":""};
                                    sz.sjwzid = sjwzxx_json[i].sjwzid;
                                    sz.wzid = sjwzxx_json[i].wzid;
                                    sz.mappingReads = sjwzxx_json[i].mappingReads;
                                    sz.reads = sjwzxx_json[i].reads;
                                    sz.refANI = sjwzxx_json[i].refANI;
                                    sz.target = sjwzxx_json[i].target;
                                    sz.wzzwm = sjwzxx_json[i].wzzwm;
                                    sz.wzywm = sjwzxx_json[i].wzywm;
                                    sz.jglx = sjwzxx_json[i].jglx;
                                    sz.wzfl = sjwzxx_json[i].wzfl;
                                    sz.sfhb = sjwzxx_json[i].sfhb;
                                    json.push(sz);
    						}
    					}
    				}
    				if (sjnyx_jsonStr){
                        var sjnyx_json = JSON.parse(sjnyx_jsonStr);
                        for(var i=0;i<sjnyx_json.length;i++){
                            var sz = {"sjnyxid":"","wkbh":"","tbjy" : "","tbjg" : "","nyx" : "","tbsd" : "","tbpl" : "","bjtb" : "","sfhb":"","fl":""};
                            sz.sjnyxid = sjnyx_json[i].sjnyxid;
                            sz.wkbh = sjnyx_json[i].wkbh;
                            sz.tbjy = sjnyx_json[i].tbjy;
                            sz.tbjg = sjnyx_json[i].tbjg;
                            sz.nyx = sjnyx_json[i].nyx;
                            sz.tbsd = sjnyx_json[i].tbsd;
                            sz.tbpl = sjnyx_json[i].tbpl;
                            sz.bjtb = sjnyx_json[i].bjtb;
                            sz.sfhb = sjnyx_json[i].sfhb;
                            sz.fl = sjnyx_json[i].fl;
                            nyjson.push(sz);
                        }
                    }
    				$('#sendReportAjaxForm #jyr').removeAttr("validate");
    				$('#sendReportAjaxForm #shr').removeAttr("validate");
    				$("#sendReportAjaxForm #sjwzxx_json").val(JSON.stringify(json));
    				$("#sendReportAjaxForm #sjnyx_json").val(JSON.stringify(nyjson));
    				submitForm(opts["formName"]||"sendReportAjaxForm",function(responseText,statusText){
    					if(responseText["status"] == 'success'){
    						$.success(responseText["message"],function() {
    							if(opts.offAtOnce){
    								$.closeModal(opts.modalName);
    							}
    						});
    					}else if(responseText["status"] == "fail"){
    						preventResubmitForm(".modal-footer > button", false);
    						$.error(responseText["message"],function() {
    							// 恢复必填验证规则
    							$('#sendReportAjaxForm #jyr').attr("validate","{required:true}");
    							$('#sendReportAjaxForm #shr').attr("validate","{required:true}");
    						});
    					} else{
    						preventResubmitForm(".modal-footer > button", false);
    						$.alert(responseText["message"],function() {
    							// 恢复必填验证规则
    							$('#sendReportAjaxForm #jyr').attr("validate","{required:true}");
    							$('#sendReportAjaxForm #shr').attr("validate","{required:true}");
    						});
    					}
    				},".modal-footer > button");
    				return false;
    			}
    		},
    		success : {
    			label : "TBtNGS发送",
    			className : "btn-primary",
    			callback : function() {
    				var $this = this;
    				var opts = $this["options"]||{};
    				$("#sendReportAjaxForm input[name='access_token']").val($("#ac_tk").val());
    				$("#sendReportAjaxForm #bcbj").val("0");
    				var json = [];
    				var nyjson = [];
    				var sjwzxx_jsonStr = $("#sjwzxx_json").val();
    				var sjnyx_jsonStr = $("#sjnyx_json").val();
    				if (sjwzxx_jsonStr){
    					var sjwzxx_json = JSON.parse(sjwzxx_jsonStr);
    					for(var i=0;i<sjwzxx_json.length;i++){
    						if(sjwzxx_json[i].wzid != "" && sjwzxx_json[i].wzid != null){
    							var sz = {"sjwzid" : "","jglx" : "","wzfl" : "","wzid" : "","wzzwm" : "","wzywm" : "","mappingReads" : "","reads" : "","refANI" : "","target" : "","sfhb":""};
                                    sz.sjwzid = sjwzxx_json[i].sjwzid;
                                    sz.wzid = sjwzxx_json[i].wzid;
                                    sz.mappingReads = sjwzxx_json[i].mappingReads;
                                    sz.reads = sjwzxx_json[i].reads;
                                    sz.refANI = sjwzxx_json[i].refANI;
                                    sz.target = sjwzxx_json[i].target;
                                    sz.wzzwm = sjwzxx_json[i].wzzwm;
                                    sz.wzywm = sjwzxx_json[i].wzywm;
                                    sz.jglx = sjwzxx_json[i].jglx;
                                    sz.wzfl = sjwzxx_json[i].wzfl;
                                    sz.sfhb = sjwzxx_json[i].sfhb;
                                    json.push(sz);
    						}
    					}
    				}
    				if (sjnyx_jsonStr){
                        var sjnyx_json = JSON.parse(sjnyx_jsonStr);
                        for(var i=0;i<sjnyx_json.length;i++){
                            var sz = {"sjnyxid":"","wkbh":"","tbjy" : "","tbjg" : "","nyx" : "","tbsd" : "","tbpl" : "","bjtb" : "","sfhb":"","fl":""};
                            sz.sjnyxid = sjnyx_json[i].sjnyxid;
                            sz.wkbh = sjnyx_json[i].wkbh;
                            sz.tbjy = sjnyx_json[i].tbjy;
                            sz.tbjg = sjnyx_json[i].tbjg;
                            sz.nyx = sjnyx_json[i].nyx;
                            sz.tbsd = sjnyx_json[i].tbsd;
                            sz.tbpl = sjnyx_json[i].tbpl;
                            sz.bjtb = sjnyx_json[i].bjtb;
                            sz.sfhb = sjnyx_json[i].sfhb;
                            sz.fl = sjnyx_json[i].fl;
                            nyjson.push(sz);
                        }
                    }
    				$("#sendReportAjaxForm #sjwzxx_json").val(JSON.stringify(json));
    				$("#sendReportAjaxForm #sjnyx_json").val(JSON.stringify(nyjson));
    				submitForm(opts["formName"]||"sendReportAjaxForm",function(responseText,statusText){
    					if(responseText["status"] == 'success'){
    						$.success(responseText["message"],function() {
    							if(opts.offAtOnce){
    								$.closeModal(opts.modalName);
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
    		success2 : {
                label : "TBtNGS + tNGS发送",
                className : "btn-primary",
                callback : function() {
                    var $this = this;
                    var opts = $this["options"]||{};
                    $("#sendReportAjaxForm input[name='access_token']").val($("#ac_tk").val());
                    $("#sendReportAjaxForm #bcbj").val("2");
                    var json = [];
                    var nyjson = [];
                    var sjwzxx_jsonStr = $("#sjwzxx_json").val();
                    var sjnyx_jsonStr = $("#sjnyx_json").val();
                    if (sjwzxx_jsonStr){
                        var sjwzxx_json = JSON.parse(sjwzxx_jsonStr);
                        for(var i=0;i<sjwzxx_json.length;i++){
                            if(sjwzxx_json[i].wzid != "" && sjwzxx_json[i].wzid != null){
                                var sz = {"sjwzid" : "","jglx" : "","wzfl" : "","wzid" : "","wzzwm" : "","wzywm" : "","mappingReads" : "","reads" : "","refANI" : "","target" : "","sfhb":""};
                                    sz.sjwzid = sjwzxx_json[i].sjwzid;
                                    sz.wzid = sjwzxx_json[i].wzid;
                                    sz.mappingReads = sjwzxx_json[i].mappingReads;
                                    sz.reads = sjwzxx_json[i].reads;
                                    sz.refANI = sjwzxx_json[i].refANI;
                                    sz.target = sjwzxx_json[i].target;
                                    sz.wzzwm = sjwzxx_json[i].wzzwm;
                                    sz.wzywm = sjwzxx_json[i].wzywm;
                                    sz.jglx = sjwzxx_json[i].jglx;
                                    sz.wzfl = sjwzxx_json[i].wzfl;
                                    sz.sfhb = sjwzxx_json[i].sfhb;
                                    json.push(sz);
                            }
                        }
                    }
                    if (sjnyx_jsonStr){
                        var sjnyx_json = JSON.parse(sjnyx_jsonStr);
                        for(var i=0;i<sjnyx_json.length;i++){
                            var sz = {"sjnyxid":"","wkbh":"","tbjy" : "","tbjg" : "","nyx" : "","tbsd" : "","tbpl" : "","bjtb" : "","sfhb":"","fl":""};
                            sz.sjnyxid = sjnyx_json[i].sjnyxid;
                            sz.wkbh = sjnyx_json[i].wkbh;
                            sz.tbjy = sjnyx_json[i].tbjy;
                            sz.tbjg = sjnyx_json[i].tbjg;
                            sz.nyx = sjnyx_json[i].nyx;
                            sz.tbsd = sjnyx_json[i].tbsd;
                            sz.tbpl = sjnyx_json[i].tbpl;
                            sz.bjtb = sjnyx_json[i].bjtb;
                            sz.sfhb = sjnyx_json[i].sfhb;
                            sz.fl = sjnyx_json[i].fl;
                            nyjson.push(sz);
                        }
                    }
                    $("#sendReportAjaxForm #sjwzxx_json").val(JSON.stringify(json));
                    $("#sendReportAjaxForm #sjnyx_json").val(JSON.stringify(nyjson));
                    submitForm(opts["formName"]||"sendReportAjaxForm",function(responseText,statusText){
                        if(responseText["status"] == 'success'){
                            $.success(responseText["message"],function() {
                                if(opts.offAtOnce){
                                    $.closeModal(opts.modalName);
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

var resendConfig = {
	width		: "800px",
	modalName	: "resendModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#resend_formSearch #resend_list').bootstrapTable('getSelections');// 获取选择行数据
				if(sel_row.length==1){
					var $this = this;
					var opts = $this["options"]||{};
					$("#resend_formSearch input[name='access_token']").val($("#ac_tk").val());
					$("#resend_formSearch input[name='fjid']").val(sel_row[0].fjid);
					$("#resend_formSearch input[name='flg_qf']").val(sel_row[0].flg);
					submitForm(opts["formName"]||"resend_formSearch",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchRecheckResult();
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
				}else{
					$.error("请选中一行");
					return false;
				}
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var Recheck_oButtton= function (){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#recheck_formSearch #btn_query");
		var btn_mod = $("#recheck_formSearch #btn_mod");
		var btn_view = $("#recheck_formSearch #btn_view");
		var btn_viewmore = $("#recheck_formSearch #btn_viewmore");
		var btn_del = $("#recheck_formSearch #btn_del");
		var btn_submit = $("#recheck_formSearch #btn_submit");
		var btn_selectexport = $("#recheck_formSearch #btn_selectexport");//选中导出
    	var btn_searchexport = $("#recheck_formSearch #btn_searchexport");//搜索导出
    	var btn_refund = $("#recheck_formSearch #btn_refund"); // 退款
		var btn_rfssend=$("#recheck_formSearch #btn_rfssend");//重发报告
		var btn_detection=$("#recheck_formSearch #btn_detection")//实验
		var btn_advancedmod=$("#recheck_formSearch #btn_advancedmod");//高级修改
		var btn_extend=$("#recheck_formSearch #btn_extend");//扩展修改
		var btn_sendreport=$("#recheck_formSearch #btn_sendreport");//耐药发送报告
		var btn_upload=$("#recheck_formSearch #btn_upload");//上传
    	
    	//添加日期控件-实验开始日期
    	laydate.render({
    	   elem: '#syrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件-实验结束日期
    	laydate.render({
    	   elem: '#syrqend'
    	  ,theme: '#2381E9'
    	});
		//添加日期控件-录入开始日期
		laydate.render({
			elem: '#lrsjstart'
			,theme: '#2381E9'
		});
		//添加日期控件-录入结束日期
		laydate.render({
			elem: '#lrsjend'
			,theme: '#2381E9'
		});
    	//添加日期控件-D实验开始日期
    	laydate.render({
    	   elem: '#dsyrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件-D实验结束日期
    	laydate.render({
    	   elem: '#dsyrqend'
    	  ,theme: '#2381E9'
    	});

		//---------------------------------检验结果上传----------------------------------
		btn_upload.unbind("click").click(function(){
			var sel_row = $('#recheck_formSearch #recheck_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				Recheck_DealById(sel_row[0].fjid,"upload",btn_upload.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
/*-----------------------模糊查询------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchRecheckResult(true); 
    		});
		}

		/*---------------------------送检验证-----------------------------------*/
		btn_rfssend.unbind("click").click(function(){
			var sel_row = $('#recheck_formSearch #recheck_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				Recheck_DealById(sel_row[0].sjid,"rfssend",btn_rfssend.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
/*-----------------------修改------------------------------------*/
		btn_mod.unbind("click").click(function(){
    		var sel_row = $('#recheck_formSearch #recheck_list').bootstrapTable('getSelections');//获取选择行数据
			// if (sel_row[0].jcxmcskz != "F"){
				if(sel_row.length==1){
					Recheck_DealById(sel_row[0].fjid,"mod",btn_mod.attr("tourl"));
				}else{
					$.error("请选中一行");
				}
			// }else{
			// 	$.error("该检测项目不允许修改！");
			// }


    	});
		/*-----------------------高级修改------------------------------------*/
		btn_advancedmod.unbind("click").click(function(){
			var sel_row = $('#recheck_formSearch #recheck_list').bootstrapTable('getSelections');//获取选择行数据
			// if (sel_row[0].jcxmcskz != "F"){
				if(sel_row.length==1){
					Recheck_DealById(sel_row[0].fjid,"advancedmod",btn_advancedmod.attr("tourl"));
				}else{
					$.error("请选中一行");
				}
			// }else{
			// 	$.error("该检测项目不允许修改！");
			// }


		});
		//扩展修改
		btn_extend.unbind("click").click(function(){
        			var sel_row = $('#recheck_formSearch #recheck_list').bootstrapTable('getSelections');//获取选择行数据
        			// if (sel_row[0].jcxmcskz != "F"){
        				if(sel_row.length==1){
        					Recheck_DealById(sel_row[0].fjid,"extend",btn_extend.attr("tourl"));
        				}else{
        					$.error("请选中一行");
        				}
        			// }else{
        			// 	$.error("该检测项目不允许修改！");
        			// }


        		});

/*-----------------------查看------------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#recheck_formSearch #recheck_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			Recheck_DealById(sel_row[0].fjid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/*-----------------------详细查看------------------------------------*/
		btn_viewmore.unbind("click").click(function(){
			var sel_row = $('#recheck_formSearch #recheck_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				Recheck_DealById(sel_row[0].fjid,"viewmore",btn_viewmore.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
/*-----------------------删除------------------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#recheck_formSearch #recheck_list').bootstrapTable('getSelections');//获取选择行数据
    		if (sel_row.length==0) {
				$.error("请至少选中一行");
				return;
			}else{
				var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].fjid;
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定要删除所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= btn_del.attr("tourl");
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								searchRecheckResult();
        							});
        						}else if(responseText["status"] == "fail"){
        							$.error(responseText["message"],function() {
        							});
        						} else{
        							$.alert(responseText["message"],function() {
        							});
        						}
        					},1);
        					
        				},'json');
        				jQuery.ajaxSetup({async:true});
        			}
        		});
			}
    	});
    	/*-----------------------提交------------------------------------*/
    	btn_submit.unbind("click").click(function(){
    		var sel_row = $('#recheck_formSearch #recheck_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			if(sel_row[0].zt=="10"){
    				$.alert("审核中！请勿重复提交！");
    			}else{
    				Recheck_DealById(sel_row[0].fjid,"submit",btn_submit.attr("tourl"));
    			}
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*--------------------------------------显示隐藏-----------------------------------------*/     
    	$("#recheck_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(Recheck_turnOff){
    			$("#recheck_formSearch #searchMore").slideDown("low");
    			Recheck_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#recheck_formSearch #searchMore").slideUp("low");
    			Recheck_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});
    	//---------------------------------选中导出---------------------------------------
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#recheck_formSearch #recheck_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].fjid;
        		}
        		ids = ids.substr(1);
        		$.showDialog(exportPrepareUrl+"?ywid=RECHECK_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});
    	//-----------------------实验(功能：修改DNA/RNA的实验状态)-------------------------------------------------
    	btn_detection.unbind("click").click(function(){
    		var sel_row = $('#recheck_formSearch #recheck_list').bootstrapTable('getSelections');//获取选择行数据
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
    			    		Recheck_DealById(ids,"detection",btn_detection.attr("tourl"));
    			    	}
    			    }
    			});
    		}
    	});

    	/*--------------------------- 退款 -----------------------------------*/
    	btn_refund.unbind("click").click(function(){
    		var sel_row = $('#recheck_formSearch #recheck_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			Recheck_DealById(sel_row[0].fjid,"refund",btn_refund.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	})
    	
    	//搜索导出
    	btn_searchexport.unbind("click").click(function(){
    		$.showDialog(exportPrepareUrl+"?ywid=RECHECK_SEARCH&expType=search&callbackJs=recheckSearchData"
    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});
    	
    	btn_sendreport.unbind("click").click(function(){
			var sel_row = $('#recheck_formSearch #recheck_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length!=1){
				$.error("请选中一行");
				return;
			}else {
				Recheck_DealById(sel_row[0].sjid,"sendreport",btn_sendreport.attr("tourl"));
			}
		});
	}
	return oInit;
}
/**
 * 修改检测状态，实验按钮
 */
var modDetectionFjConfig = {
		width		: "800px",
		modalName	: "modDetectionFjConfig",
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

/**
 * 修改模态框
 */
var modRecheckConfig = {
		width		: "800px",
		modalName	:"modRecheckModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#recheck_mod_Form").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					var bgbjval = $("#recheck_mod_Form input:radio[name='bgbj']:checked").val()//重发为是：1，否：0
					var sfffval = $("#recheck_mod_Form #sfff").val()//收费1：不收费0
					if ( (sfffval=='1' && bgbjval=='0')){
						$.alert("报告重发和是否付费匹配不符合规范！");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#recheck_mod_Form input[name='access_token']").val($("#ac_tk").val());

					if($("#recheck_mod_Form .yy")!=null && $("#recheck_mod_Form .yy").length>0){
						var yys="";
						var yymcs="";
						for(var i=0;i<$("#recheck_mod_Form .yy").length;i++){
							if($("#recheck_mod_Form .yy")[i].checked){
								yys=yys+","+$("#recheck_mod_Form .yy")[i].value;
								yymcs=yymcs+";"+$("#recheck_mod_Form #yy_"+$("#recheck_mod_Form .yy")[i].value+" span").text();
							}
						}
						$("#recheck_mod_Form #yys").val(yys.substring(1));
						$("#recheck_mod_Form #yy").val(yymcs.substring(1));
					}
					submitForm(opts["formName"]||"recheck_mod_Form",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchRecheckResult();
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
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
var modExtendConfig = {
		width		: "800px",
		modalName	:"modExtebdModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#recheckExtend_Form").valid()){
						$.alert("请填写完整信息");
						return false;
					}

					var $this = this;
					var opts = $this["options"]||{};
                    $("#recheckExtend_Form input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"recheckExtend_Form",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchRecheckResult();
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
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
/**
 * 查看页面模态框
 */
var viewRecheckConfig={
	width		: "800px",
	modalName	:"viewRecheckModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}

/**
 * 提交模态框
 */
var submitRecheckConfig={
		width		: "800px",
		modalName	:"submitRecheckConfig",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "提交",
				className : "btn-primary",
				callback : function() {
					var $this = this;
					var opts = $this["options"]||{};
					var auditType=null;
					var shlx=$("#recheckview_formSearch #shlx").val();
					if(shlx){
						auditType = shlx;
					}else{
						auditType = $("#recheckview_formSearch #auditType").val();
					}
					var ywid=$("#recheckview_formSearch #fjid").val();
					var ybztcskz = $("#recheckview_formSearch #ybztcskz").val();
					if(ybztcskz=="1"){						
						$.alert("量仅一次");
						return false;
					}
					showAuditFlowDialog(auditType,ywid,function(){
						searchRecheckResult();
						$.closeModal(opts.modalName);
					});
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	}

//退款模态框
var payRefundConfig = {
	width		: "1200px",
	modalName	: "payRefundModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};


/**
 * 刷新bootstraptable 
 */
function searchRecheckResult(isTurnBack){
	//关闭高级搜索条件
	$("#recheck_formSearch #searchMore").slideUp("low");
	Recheck_turnOff=true;
	$("#recheck_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#recheck_formSearch #recheck_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#recheck_formSearch #recheck_list').bootstrapTable('refresh');
	}
}
$(function(){
    var oTable=new Recheck_TableInit();
		oTable.Init();
	//2.初始化Button的点击事件
    var oButtonInit = new Recheck_oButtton();
    	oButtonInit.Init();
	jQuery('#recheck_formSearch .chosen-select').chosen({width: '100%'});
	// 初始绑定显示更多的事件
	$("#recheck_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
})