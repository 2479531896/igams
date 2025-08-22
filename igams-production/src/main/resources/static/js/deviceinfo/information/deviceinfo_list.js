var Device_turnOff=true;

var Device_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#deviceinfo_formSearch #deviceinfo_list").bootstrapTable({
			url: '/deviceinfo/deviceinfo/getPageList',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#deviceinfo_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "sbxx.lrsj",				//排序字段
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
            uniqueId: "sbid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'sbid',
                title: '设备编码',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'sbbh',
                title: '设备编号',
                width: '7%',
                align: 'left',
                visible:true
            },{
                field: 'sbmc',
                title: '设备名称',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'bhlbmc',
                title: '编号类别',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'gllbmc',
                title: '管理类别',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'ssxm',
                title: '所属项目',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'ssgs',
                title: '所属公司',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'sqrmc',
                title: '申请人',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'sqbmmc',
                title: '申请部门',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'sgbmmc',
                title: '申购部门',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'ggxh',
                title: '规格型号',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'djjs',
                title: '单价(含税)',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'sl',
                title: '数量',
                width: '4%',
                align: 'left',
                visible: true
            },{
                field: 'jejs',
                title: '金额(含税)',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'dhrq',
                title: '到货日期',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'sccj',
                title: '生产厂家',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'cjdh',
                title: '厂家电话',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'gys',
                title: '供应商',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'gysdh',
                title: '供应商电话',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'bxrq',
                title: '保修日期',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'glrq',
                title: '购量日期',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'dlwz',
                title: '定量位置',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'sydd',
                title: '使用地点',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'zt',
                title: '状态',
                width: '6%',
                align: 'left',
                formatter:sbxxFormat,
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'lrrymc',
                title: '录入人员',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'cz',
                title: '操作',
                width: '7%',
                align: 'left',
                formatter:czFormat,
                titleTooltip:'操作',
                visible:true,
                sortable: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	DeviceDealById(row.sbid,'view',$("#deviceinfo_formSearch #btn_view").attr("tourl"));
             },
	 })
		$("#deviceinfo_formSearch #deviceinfo_list").colResizable({
			liveDrag:true, 
			gripInnerHtml:"<div class='grip'></div>", 
			draggingClass:"dragging", 
			resizeMode:'fit', 
			postbackSafe:true,
			partialRefresh:true
		})
	}
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "sbxx.sbid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getDeviceSearchData(map);
	};
return oTableInit
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
	if (row.zt == '10') {
		return "<span class='btn btn-warning' onclick=\"recallSbxx('" + row.sbid + "',event)\" >撤回</span>";
	}else{
		return "";
	}
}

//撤回设备审核
function recallSbxx(sbid,event){
	var auditType = $("#deviceinfo_formSearch #auditType").val();
	var msg = '您确定要撤回审核吗？';
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,sbid,function(){
				searchDeviceResult();
			});
		}
	});
}
/**
 * 设备列表的状态格式化函数
 * @returns
 */
function sbxxFormat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sbid + "\",event,\"AUDIT_DEVICEINFO\")' >审核通过</a>";
    }else if (row.zt == '15') {
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sbid + "\",event,\"AUDIT_DEVICEINFO\")' >审核未通过</a>";
    }else if (row.zt == '20') {
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sbid + "\",event,\"AUDIT_DEVICEINFO\")' >" + row.shxx_dqgwmc + "审核中</a>";
    }else if (row.zt == '30') {
    	return "质量校准审核中"
    }else if (row.zt == '90') {
    	return "质量校准不通过"
    }else {
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sbid + "\",event,\"AUDIT_DEVICEINFO\")' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}

/**
 * 计量校准状态
 * @returns
 */
function jlztFormat(value,row,index) {
	if (row.lbcsdm == "A"||row.lbcsdm == "B"||row.lbcsdm == "C") {
		
        if(row.jlzt==""||row.jlzt==null){
        	return "";
        }else if(row.jlzt=="00"){
        	return "未提交"
        }else if (row.jlzt == '80') {
        	return "审核通过";
        }else if (row.jlzt == '15') {
        	return "审核未通过";
        }else{
        	return "审核中</a>";
        }
    }else{
    	return "无需计量校准"
    }
    
}
/**
 * 检索条件
 * @param map
 * @returns
 */
function getDeviceSearchData(map){
	var cxtj=$("#deviceinfo_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#deviceinfo_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["sbbh"]=cxnr
	}else if(cxtj=="1"){
		map["sbmc"]=cxnr
	}else if(cxtj=="2"){
		map["sqrmc"]=cxnr
	}else if(cxtj=="3"){
		map["sqbmmc"]=cxnr
	}else if(cxtj=="4"){
		map["sgbmmc"]=cxnr
	}else if(cxtj=="5"){
		map["ssxm"]=cxnr
	}else if(cxtj=="6"){
		map["ssgs"]=cxnr
	}else if(cxtj=="7"){
		map["gys"]=cxnr
	}else if(cxtj=="8"){
		map["sccj"]=cxnr
	}else if(cxtj=="9"){
		map["sybmmc"]=cxnr
	}else if(cxtj=="10"){
		map["zrbmmc"]=cxnr
	}else if(cxtj=="11"){
		map["zrrmc"]=cxnr
	}
	//编号类别
	var bhlbs = jQuery('#deviceinfo_formSearch #bhlb_id_tj').val();
		map["bhlbs"] = bhlbs;
	//管理类别
	var gllbs = jQuery('#deviceinfo_formSearch #gllb_id_tj').val();
		map["gllbs"] = gllbs;
	//验收结果
	var ysjgs = jQuery('#deviceinfo_formSearch #ysjg_id_tj').val();
		map["ysjgs"] = ysjgs;
	//续保处理
	var xbcls = jQuery('#deviceinfo_formSearch #xbcl_id_tj').val();
		map["xbcls"] = xbcls;
	//档案
	var das = jQuery('#deviceinfo_formSearch #da_id_tj').val();
		map["das"] = das;
	return map;
}

var Device_ButtonInit= function (){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#deviceinfo_formSearch #btn_query");
		var btn_add = $("#deviceinfo_formSearch #btn_add");
		var btn_mod = $("#deviceinfo_formSearch #btn_mod");
		var btn_view = $("#deviceinfo_formSearch #btn_view");
		var btn_del = $("#deviceinfo_formSearch #btn_del");
		var btn_resubmit= $("#deviceinfo_formSearch #btn_resubmit");
		var btn_selectexport = $("#deviceinfo_formSearch #btn_selectexport");//选中导出
    	var btn_searchexport = $("#deviceinfo_formSearch #btn_searchexport");//搜索导出
		
         /*-----------------------模糊查询------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchDeviceResult(true); 
    		});
		}
		 /* ------------------------------添加设备信息-----------------------------*/
    	btn_add.unbind("click").click(function(){
    		DeviceDealById(null,"add",btn_add.attr("tourl"));
    	});
    	 /* ------------------------------修改设备信息-----------------------------*/
    	btn_mod.unbind("click").click(function(){
			var sel_row=$('#deviceinfo_formSearch #deviceinfo_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				if(sel_row[0].zt!="10"){
					DeviceDealById(sel_row[0].sbid,"mod",btn_mod.attr("tourl"));
				}else{
					$.error("审核中，不允许修改！");
				}
			}else{
				$.error("请选中一行");
			}
    	});
    	 /* ------------------------------查看设备信息-----------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row=$('#deviceinfo_formSearch #deviceinfo_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			DeviceDealById(sel_row[0].sbid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*----------------------------删除设备信息--------------------------*/
     	btn_del.unbind("click").click(function(){
     		var sel_row = $('#deviceinfo_formSearch #deviceinfo_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else {
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].sbid;
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
        								searchDeviceResult();
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
     	})
     	/*--------------------------------------重新提交-----------------------------------------*/  
    	btn_resubmit.unbind("click").click(function(){
			var sel_row=$('#deviceinfo_formSearch #deviceinfo_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				if(sel_row[0].zt=="00"){
					DeviceDealById(sel_row[0].sbid,"resubmit",btn_resubmit.attr("tourl"));
				}else{
					$.error("当前状态不能提交！");
				}
			}else{
				$.error("请选中一行");
			}
		});
    	//---------------------------------选中导出---------------------------------------
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#deviceinfo_formSearch #deviceinfo_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].sbid;
        		}
        		ids = ids.substr(1);
        		$.showDialog(exportPrepareUrl+"?ywid=DEVICEINFO_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});
    	//搜索导出
    	btn_searchexport.unbind("click").click(function(){
    		$.showDialog(exportPrepareUrl+"?ywid=DEVICEINFO_SEARCH&expType=search&callbackJs=deviceinfoSearchData"
    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});
		/**显示隐藏**/      
    	$("#deviceinfo_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(Device_turnOff){
    			$("#deviceinfo_formSearch #searchMore").slideDown("low");
    			Device_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#deviceinfo_formSearch #searchMore").slideUp("low");
    			Device_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});
	}
	return oInit;
}

//提供给导出用的回调函数
function deviceinfoSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="sbxx.sbid";
	map["sortLastOrder"]="asc";
	map["sortName"]="sbxx.lrsj";
	map["sortOrder"]="asc";
	return getDeviceSearchData(map);
}
/**
 * 按钮点击操作
 * @param id
 * @param action
 * @param tourl
 * @returns
 */
function DeviceDealById(id,action,tourl){
	var url= tourl;
	if(!tourl){
		return;
	}
	if(action =='add'){
		var url= tourl;
		$.showDialog(url,'新增设备信息',addDeviceConfig);
	}else if(action =='mod'){
		var url= tourl+"?sbid="+id;
		$.showDialog(url,'修改设备信息',modDeviceConfig);
	}else if(action =='view'){
		var url= tourl+"?sbid="+id;
		$.showDialog(url,'查看设备信息',viewDeviceConfig);
	}else if(action=="resubmit"){
		var url=tourl+"?sbid="+id;
		$.showDialog(url,'重新提交',resubmitDeviceConfig);
	}
}
	/**
	 * 新增时弹出框
	 */
var addDeviceConfig = {
		width		: "800px",
		modalName	: "addDeviceModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#deviceinfoForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#deviceinfoForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"deviceinfoForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							var auditType = responseText["auditType"];
							var ywid=responseText["ywid"];
							showAuditFlowDialog(auditType,ywid,function(){
								searchDeviceResult();
							});
						}else if(responseText["status"] == "fail"){
							$.error(responseText["message"],function() {
							});
						} else{
							$.alert(responseText["message"],function() {
							});
						}
					});
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
 * 修改弹出框
 */
var modDeviceConfig = {
		width		: "800px",
		modalName	:"modDeviceModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					var $this = this;
					var opts = $this["options"]||{};
					$("#measurement_mod_Form input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"measurement_mod_Form",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchDeviceResult();
							});	
						}else if(responseText["status"] == "fail"){
							$.error(responseText["message"],function() {
							});
						} else{
							$.alert(responseText["message"],function() {
							});
						}
					});
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
 * 查看页面弹出框
 */
var viewDeviceConfig = {
		width		: "800px",
		modalName	:"viewDeviceModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			 cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

/**
 * 重新提交弹出框
 */
var resubmitDeviceConfig={
		width		: "800px",
		modalName	:"resubmitDeviceModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#deviceinfo_resubmitForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#deviceinfo_resubmitForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"deviceinfo_resubmitForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							var auditType = responseText["auditType"];
							var ywid=responseText["ywid"];
							showAuditFlowDialog(auditType,ywid,function(){
								searchDeviceResult();
							});
						}else if(responseText["status"] == "fail"){
							$.error(responseText["message"],function() {
							});
						} else{
							$.alert(responseText["message"],function() {
							});
						}
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
/**
 * 列表刷新
 * @param isTurnBack
 * @returns
 */
function searchDeviceResult(isTurnBack){
	//关闭高级搜索条件
	$("#deviceinfo_formSearch #searchMore").slideUp("low");
	Device_turnOff=true;
	$("#deviceinfo_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#deviceinfo_formSearch #deviceinfo_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#deviceinfo_formSearch #deviceinfo_list').bootstrapTable('refresh');
	}
}
$(function(){
	//0.界面初始化
    // 1.初始化Table
    var oTable = new Device_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new Device_ButtonInit();
    oButtonInit.Init();
	// 所有下拉框添加choose样式
    jQuery('#deviceinfo_formSearch .chosen-select').chosen({width: '100%'});
    
  //添加日期控件
	laydate.render({
	   elem: '#deviceinfo_formSearch #lrsjstart'
	  ,theme: '#2381E9'
	});
	//添加日期控件
	laydate.render({
	   elem: '#deviceinfo_formSearch #lrsjend'
	  ,theme: '#2381E9'
	});
});
