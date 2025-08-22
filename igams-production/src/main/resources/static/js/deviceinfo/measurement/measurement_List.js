var Measurement_turnOff=true;

var Measurement_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#measurement_formSearch #measurement_list").bootstrapTable({
			url: '/measurement/measurement/getListSbxx',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#measurement_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "sbjl.lrsj",				//排序字段
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
            uniqueId: "jlid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'sbbh',
                title: '设备编号',
                width: '7%',
                align: 'left',
                visible: true
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
                visible: true
            },{
                field: 'gllbmc',
                title: '管理类别',
                width: '7%',
                align: 'left',
                visible: true
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
                visible: false
            },{
                field: 'djjs',
                title: '单价(含税)',
                width: '6%',
                align: 'left',
                visible: false
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
                visible: false
            },{
                field: 'dhrq',
                title: '到货日期',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'sccj',
                title: '生产厂家',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'cjdh',
                title: '厂家电话',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'gys',
                title: '供应商',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'gysdh',
                title: '供应商电话',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'jldw',
                title: '计量单位',
                width: '7%',
                align: 'left',
                visible:true
            },{
                field: 'zsbh',
                title: '证书编号',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'zqd',
                title: '准确度',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'jdrq',
                title: '检定日期',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'yxrq',
                title: '有效日期',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'jlksrq',
                title: '计量开始日期',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'yjjsrq',
                title: '预计结束日期',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'jljsrq',
                title: '计量结束日期',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'jdjg',
                title: '检定结果',
                width: '7%',
                formatter:jdjgFormat,
                align: 'left',
                visible: true
            },{
                field: 'jdffmc',
                title: '检定方法',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'zt',
                title: '状态',
                formatter:sbjlztFormat,
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'cz',
                title: '操作',
                formatter:czFormat,
                width: '7%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	MeasurementDealById(row.jlid,'view',$("#measurement_formSearch #btn_view").attr("tourl"));
             },
		})
		$("#measurement_formSearch #measurement_list").colResizable({
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
            sortLastName: "sbjl.jlid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getMeasurementSearchData(map);
	};
	return oTableInit;
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
	if (row.zt == '10') {
		return "<span class='btn btn-warning' onclick=\"recallMeasurement('" + row.jlid + "',event)\" >撤回</span>";
	}else{
		return "";
	}
}

//撤回提交
function recallMeasurement(jlid,event){
	var auditType = $("#measurement_formSearch #auditType").val()
	var msg = '您确定要撤回复测申请吗？';
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,jlid,function(){searchMeasurementResult();});
		}
	});
}

function getMeasurementSearchData(map){
	var cxtj=$("#measurement_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#measurement_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["sbbh"]=cxnr
	}else if(cxtj=="1"){
		map["sbmc"]=cxnr
	}else if(cxtj=="2"){
		map["ssgs"]=cxnr
	}else if(cxtj=="3"){
		map["ssxm"]=cxnr
	}else if(cxtj=="4"){
		map["sqbm"]=cxnr
	}else if(cxtj=="5"){
		map["sgbm"]=cxnr
	}else if(cxtj=="6"){
		map["sccj"]=cxnr
	}else if(cxtj=="7"){
		map["gys"]=cxnr
	}else if(cxtj=="8"){
		map["zsbh"]=cxnr
	}
	//编号类别
	var bhlbs = jQuery('#measurement_formSearch #bhlb_id_tj').val();
		map["bhlbs"] = bhlbs;
	//管理类别
	var gllbs = jQuery('#measurement_formSearch #gllb_id_tj').val();
		map["gllbs"] = gllbs;
	//验收结果
	var ysjgs = jQuery('#measurement_formSearch #ysjg_id_tj').val();
		map["ysjgs"] = ysjgs;
	//检定方法
	var jdffs = jQuery('#measurement_formSearch #jdff_id_tj').val();
		map["jdffs"] = jdffs;
	//申请部门
	var sqbms = jQuery('#measurement_formSearch #sqbm_id_tj').val();
		map["sqbms"] = sqbms;
	//检定结果
	var jdjgs = jQuery('#measurement_formSearch #jdjg_id_tj').val();
		map["jdjgs"] = jdjgs;
		
	return map;
}
/**
 * 设备计量校准列表的状态格式化函数
 * @returns
 */
function sbjlztFormat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.jlid + "\",event,\"AUDIT_MEASURE\")' >审核通过</a>";
    }else if (row.zt == '15') {
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.jlid + "\",event,\"AUDIT_MEASURE\")' >审核未通过</a>";
    }else if (row.zt == '20') {
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.jlid + "\",event,\"AUDIT_MEASURE\")' >" + row.shxx_dqgwmc + "审核中</a>";
    }else{
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.jlid + "\",event,\"AUDIT_MEASURE\")' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}

/**
 * 检定结果
 * @param value
 * @param row
 * @param index
 * @returns
 */
function jdjgFormat(value,row,index) {
	  if (row.jdjg == '0') {
		  return "<span style='color:red'>不合格</span>"
	  }else if(row.jdjg == '1'){
		  return "<span style='color:green'>合格</span>"
	  }
}
/**
 * 按钮点击操作
 * @param id
 * @param action
 * @param tourl
 * @returns
 */
function MeasurementDealById(id,action,tourl){
	var url= tourl;
	if(!tourl){
		return;
	}
	if(action =='mod'){
		var url= tourl+"?jlid="+id;
		$.showDialog(url,'修改计量校准',modMeasurementConfig);
	}else if(action =='view'){
		var url= tourl+"?jlid="+id;
		$.showDialog(url,'查看计量校准',viewMeasurementConfig);
	}else if(action=="resubmit"){
		var url=tourl+"?jlid="+id;
		$.showDialog(url,'重新提交',resubmitMeasurementConfig);
	}
}
	

/**
 * 按钮初始化
 */
var Measurement_ButtonInit= function (){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#measurement_formSearch #btn_query");
		var btn_mod=$("#measurement_formSearch #btn_mod");
		var btn_view=$("#measurement_formSearch #btn_view");
		var btn_del = $("#measurement_formSearch #btn_del");
		var btn_resubmit= $("#measurement_formSearch #btn_resubmit");
		/*-----------------------模糊查询------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchMeasurementResult(true); 
    		});
		}
		/*---------------------------修改---------------------------------*/
		btn_mod.unbind("click").click(function(){
			var sel_row=$('#measurement_formSearch #measurement_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				if(sel_row[0].zt!="10"){
					MeasurementDealById(sel_row[0].jlid,"mod",btn_mod.attr("tourl"));
				}else{
					$.error("审核中，不允许修改!");
				}
			}else{
				$.error("请选中一行");
			}
    	});
		 /* ------------------------------查看设备计量信息-----------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row=$('#measurement_formSearch #measurement_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			MeasurementDealById(sel_row[0].jlid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*--------------------------------------重新提交-----------------------------------------*/  
    	btn_resubmit.unbind("click").click(function(){
			var sel_row=$('#measurement_formSearch #measurement_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				if(sel_row[0].zt =="00"){
					MeasurementDealById(sel_row[0].jlid,"resubmit",btn_resubmit.attr("tourl"));
				}else{
					$.error("当前状态不能提交！");
				}
			}else{
				$.error("请选中一行");
			}
		});
    	/*----------------------------删除设备信息--------------------------*/
     	btn_del.unbind("click").click(function(){
     		var sel_row = $('#measurement_formSearch #measurement_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else {
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].jlid;
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
        								searchMeasurementResult();
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
		/**显示隐藏**/   
    	$("#measurement_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(Measurement_turnOff){
    			$("#measurement_formSearch #searchMore").slideDown("low");
    			Measurement_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#measurement_formSearch #searchMore").slideUp("low");
    			Measurement_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});
		
	}
	return oInit;
}

/**
 * 修改弹出框
 */
var modMeasurementConfig = {
		width		: "800px",
		modalName	:"modMeasurementModal",
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
								searchMeasurementResult();
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
var viewMeasurementConfig = {
		width		: "800px",
		modalName	:"viewMeasurementModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			 cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};


/**
 * 重新提交
 */
var resubmitMeasurementConfig={
		width		: "800px",
		modalName	:"resubmitMeasurementConfig",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "提交",
				className : "btn-primary",
				callback : function() {
					var auditType = $("#Measurement_view_Form #auditType").val();
					var ywid = $("#Measurement_view_Form #jlid").val();
					showAuditFlowDialog(auditType,ywid,function(){
						searchMeasurementResult();
					});
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
function searchMeasurementResult(isTurnBack){
	//关闭高级搜索条件
	$("#measurement_formSearch #searchMore").slideUp("low");
	Measurement_turnOff=true;
	$("#measurement_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#measurement_formSearch #measurement_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#measurement_formSearch #measurement_list').bootstrapTable('refresh');
	}
}

$(function(){
	//0.界面初始化
    // 1.初始化Table
    var oTable = new Measurement_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new Measurement_ButtonInit();
    oButtonInit.Init();
 // 所有下拉框添加choose样式
    jQuery('#measurement_formSearch .chosen-select').chosen({width: '100%'});
})