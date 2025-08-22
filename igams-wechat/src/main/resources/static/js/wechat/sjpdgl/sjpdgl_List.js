var sjpdgl_turnOff=true;
var Sjpd_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#sjpdgl_formSearch #sjpdgl_list").bootstrapTable({
			url: '/logistics/sjpdgl/pageGetListView',         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#sjpdgl_formSearch #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   //是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "sjpdgl.scbj , sjpdgl.zt ",				//排序字段
			sortOrder: "",                   //排序方式
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
			clickToSelect: true,                //是否启用点击选中行
			//height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "sjpdid",                     //每一行的唯一标识，一般为主键列
			showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: [{
				checkbox: true
			},{
				field: 'pdh',
				title: '派单号',
				width: '10%',
				align: 'left',
				visible:true
			},{
				field: 'pdrmc',
				title: '派单人',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'jdrmc',
				title: '接单人',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'hzxm',
				title: '患者姓名',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'bbbh',
				title: '标本编号',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'bblxmc',
				title: '标本类型',
				width: '10%',
				align: 'left',
				visible: false
			},{
				field: 'jsfsmc',
				title: '寄送方式',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'jcdwmc',
				title: '目的地',
				width: '8%',
				align: 'left',
				visible: true
			},{
				field: 'qydz',
				title: '取样地址',
				width: '10%',
				align: 'left',
				visible:true
			},{
				field: 'pdbz',
				title: '派单备注',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'sfsfmc',
				title: '是否代收费用',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'sfje',
				title: '代收金额',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'jdbjmc',
				title: '接单状态',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'pdsj',
				title: '派单时间',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'yssc',
				title: '运输时长',
				width: '14%',
				align: 'left',
				visible: true,
				sortable: true
			},{
				field: 'pdbjmc',
				title: '取消派单',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'qxpdyy',
				title: '取消派单原因',
				width: '10%',
				align: 'left',
				visible: false
			},{
				field: 'zt',
				title: '状态',
				width: '6%',
				formatter:ztformat,
				align: 'left',
				sortable: true,
				visible: true
			}],
			onLoadSuccess:function(){
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				Sjpdgl_DealById(row.sjpdid,'view',$("#sjpdgl_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#sjpdgl_formSearch #sjpdgl_list").colResizable({
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
			sortLastName: "sjpdgl.lrsj", // 防止同名排位用
			sortLastOrder: "DESC" ,// 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return getSjpdSearchData(map);
	};
	return oTableInit;
}

//状态格式化
function ztformat(value,row,index){
	if(row.zt=='00'){
		var color="<span style='color:darkgreen'>"+"未派单"+"</span>"
	}else if(row.zt=='10'){
		var color="<span style='color:red'>"+"已派单"+"</span>"
	}
	else if(row.zt=='35'){
		var color="<span style='color:brown'>"+"运输中"+"</span>"
	}
	else if(row.zt=='50'){
		var color="<span style='color:black'>"+"已完成"+"</span>"
	}
	else if(row.zt=='20'){
		var color="<span style='color:brown'>"+"已接单"+"</span>"
	}
	return color;
}

function getSjpdSearchData(map){
	var cxtj=$("#sjpdgl_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#sjpdgl_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["pdh"]=cxnr
	}else if(cxtj=="1"){
		map["qydz"]=cxnr
	}else if(cxtj=="2"){
		map["pdrmc"]=cxnr
	}else if(cxtj=="3"){
		map["bbbh"]=cxnr
	}
	else if(cxtj=="4"){
		map["hzxm"]=cxnr
	}
	else if(cxtj=="5"){
		map["entire"]=cxnr
	}

	var zts = jQuery('#sjpdgl_formSearch #zt_id_tj').val();
	map["zts"] = zts;
	//是否收费
	var sfsfs=jQuery('#sjpdgl_formSearch #sfsf_id_tj').val()
	map["sfsfs"]=sfsfs;
	//检测单位
	var jcdws=$('#sjpdgl_formSearch #jcdw_id_tj').val();
	map["jcdws"]=jcdws;
	//标本类型
	var bblxs = jQuery('#sjpdgl_formSearch #bblx_id_tj').val();
	map["bblxs"]=bblxs;
	//接单状态
	var jsfss = jQuery('#sjpdgl_formSearch #jsfs_id_tj').val();
	map["jsfss"]=jsfss;

	//寄送方式
	var jdbjs = jQuery('#sjpdgl_formSearch #jdbj_id_tj').val();
	map["jdbjs"]=jdbjs;
	var lrsjstart = jQuery('#sjpdgl_formSearch #lrsjstart').val();
	map["lrsjstart"]=lrsjstart;

	//结束时间
	var lrsjend = jQuery('#sjpdgl_formSearch #lrsjend').val();
	map["lrsjend"]=lrsjend;

	return map;
}
//提供给导出用的回调函数
function SJPDGLSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="sjpdgl.lrsj";
	map["sortLastOrder"]="asc";
	map["sortName"]="sjpdgl.zt";
	map["sortOrder"]="asc";
	return getSjpdSearchData(map);
}
//按钮动作函数
function Sjpdgl_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action=="add"){
		var url=tourl;
		$.showDialog(url,'新增',addSjpdglConfig);
	}else if(action=="mod"){
		var url=tourl+"?sjpdid="+id
		$.showDialog(url,'修改',addSjpdglConfig);
	}else if(action=="view"){
		var url=tourl+"?sjpdid="+id
		$.showDialog(url,'查看',viewSjpdglConfig);
	}
}

//按钮
var Sjpd_oButtton= function (){
	var oInit=new Object();
	var postdata = {};

	oInit.Init=function(){
		var btn_query=$("#sjpdgl_formSearch #btn_query");
		var btn_add = $("#sjpdgl_formSearch #btn_add");
		var btn_mod = $("#sjpdgl_formSearch #btn_mod");
		var btn_view = $("#sjpdgl_formSearch #btn_view");
		var btn_del = $("#sjpdgl_formSearch #btn_del");
		var btn_resume= $("#sjpdgl_formSearch #btn_resume");
		var btn_enable= $("#sjpdgl_formSearch #btn_enable");
		var btn_disable= $("#sjpdgl_formSearch #btn_disable");
		var flBind = $("#sjpdgl_formSearch #fl_id ul li a");
		var btn_oncopower= $("#sjpdgl_formSearch #btn_oncopower");
		var btn_selectexport = $("#sjpdgl_formSearch #btn_selectexport");//选中导出
		var btn_searchexport = $("#sjpdgl_formSearch #btn_searchexport");//搜索导出

		/*-----------------------模糊查询------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchSjpdResult(true);
			});
		}
		/*-----------------------新增------------------------------------*/
		btn_add.unbind("click").click(function(){
			Sjpdgl_DealById(null,"add",btn_add.attr("tourl"));
		});
		/*-----------------------直销onco报告接收权限更新------------------------------------*/
		btn_oncopower.unbind("click").click(function(){
			$.confirm('您确定要更新直销的onco报告接收权限吗？',function(result){
				if(result){
					jQuery.ajaxSetup({async:false});
					var url= btn_oncopower.attr("tourl");
					jQuery.post(url,{"access_token":$("#ac_tk").val()},function(responseText){
						setTimeout(function(){
							if(responseText["status"] == 'success'){
								$.success(responseText["message"],function() {
									searchSjpdResult();
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
		});
		/*-----------------------修改------------------------------------*/
		btn_mod.unbind("click").click(function(){
			var sel_row = $('#sjpdgl_formSearch #sjpdgl_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				Sjpdgl_DealById(sel_row[0].sjpdid,"mod",btn_mod.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*-----------------------恢复------------------------------------*/
		btn_resume.unbind("click").click(function(){
			var sel_row = $('#sjpdgl_formSearch #sjpdgl_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				Sjpdgl_DealById(sel_row[0].sjpdid,"resume",btn_resume.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*-----------------------查看------------------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row = $('#sjpdgl_formSearch #sjpdgl_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				Sjpdgl_DealById(sel_row[0].sjpdid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*-----------------------选中导出------------------------------------*/
		btn_selectexport.unbind("click").click(function(){
			var sel_row = $('#sjpdgl_formSearch #sjpdgl_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].sjpdid;
				}
				ids = ids.substr(1);
				$.showDialog(exportPrepareUrl+"?ywid=SJPDGL_SELECT&expType=select&ids="+ids
					,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
			}else{
				$.error("请选择数据");
			}
		});
		/*-----------------------搜索导出------------------------------------*/
		btn_searchexport.unbind("click").click(function(){
			$.showDialog(exportPrepareUrl+"?ywid=SJPDGL_SEARCH&expType=search&callbackJs=SJPDGLSearchData"
				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
		});
		/*-----------------------删除------------------------------------*/
		btn_del.unbind("click").click(function(){
			var sel_row = $('#sjpdgl_formSearch #sjpdgl_list').bootstrapTable('getSelections');//获取选择行数据
			if (sel_row.length==0) {
				$.error("请至少选中一行");
				return;
			}else{
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids= ids + ","+ sel_row[i].sjpdid;
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
										searchSjpdResult();
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

		/*-----------------------启用------------------------------------*/
		btn_enable.unbind("click").click(function(){
			var sel_row = $('#sjpdgl_formSearch #sjpdgl_list').bootstrapTable('getSelections');//获取选择行数据
			if (sel_row.length==0) {
				$.error("请至少选中一行");
				return;
			}else{
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids= ids + ","+ sel_row[i].sjpdid;
				}
				ids=ids.substr(1);
				$.confirm('您确定要启用所选择的信息吗？',function(result){
					if(result){
						jQuery.ajaxSetup({async:false});
						var url= btn_enable.attr("tourl");
						jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
							setTimeout(function(){
								if(responseText["status"] == 'success'){
									$.success(responseText["message"],function() {
										searchSjpdResult();
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

		/*-----------------------停用------------------------------------*/
		btn_disable.unbind("click").click(function(){
			var sel_row = $('#sjpdgl_formSearch #sjpdgl_list').bootstrapTable('getSelections');//获取选择行数据
			if (sel_row.length==0) {
				$.error("请至少选中一行");
				return;
			}else{
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids= ids + ","+ sel_row[i].sjpdid;
				}
				ids=ids.substr(1);
				$.confirm('您确定要停用所选择的信息吗？',function(result){
					if(result){
						jQuery.ajaxSetup({async:false});
						var url= btn_disable.attr("tourl");
						jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
							setTimeout(function(){
								if(responseText["status"] == 'success'){
									$.success(responseText["message"],function() {
										searchSjpdResult();
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
		/**显示隐藏**/
		$("#sjpdgl_formSearch #sl_searchMore").on("click", function(ev){
			var ev=ev||event;
			if(sjpdgl_turnOff){
				$("#sjpdgl_formSearch #searchMore").slideDown("low");
				sjpdgl_turnOff=false;
				this.innerHTML="基本筛选";
			}else{
				$("#sjpdgl_formSearch #searchMore").slideUp("low");
				sjpdgl_turnOff=true;
				this.innerHTML="高级筛选";
			}
			ev.cancelBubble=true;
		});

		//绑定分类的单击事件
		if(flBind!=null){
			flBind.on("click", function(){
				setTimeout(function(){
					getZfls();
				}, 10);
			});
		}
	};
	return oInit;
};

//增加
var addSjpdglConfig = {
	width		: "1000px",
	modalName	:"addPartnerModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#addSjpdForm").valid()){
					return false;
				}
				if ($("#addSjpdForm #jsfsdm").val()=='SF'||$("#addSjpdForm #jsfsdm").val()=='JD'||$("#addSjpdForm #jsfsdm").val()=='TC'){
					if($("#addSjpdForm #bbbh").val()==""||$("#addSjpdForm #bbbh").val()==null){
						$.alert("请输入样本编号！");
						return false;
					}else if($("#addSjpdForm #yjddsj").val()==""||$("#addSjpdForm #yjddsj").val()==null){
						$.alert("请输入预计到达时间！");
						return false;
					}
				}
				if ($("#addSjpdForm #jsfsdm").val()=='ZS'){
					if($("#addSjpdForm #bbbh").val()==""||$("#addSjpdForm #bbbh").val()==null){
						$.alert("请输入样本编号！");
						return false;
					}else if($("#addSjpdForm #yjddsj").val()==""||$("#addSjpdForm #yjddsj").val()==null){
						$.alert("请输入预计到达时间！");
						return false;
					}
				}
				if ($("#addSjpdForm #jsfsdm").val()=='GT'){
					if($("#addSjpdForm #yjsj").val()==""||$("#addSjpdForm #yjsj").val()==null){
						$.alert("请输入预估取样时间！");
						return false;
					}
				}
				if ($("#addSjpdForm #jsfsdm").val()=='QYY'){
					if($("#addSjpdForm #yjsj").val()==""||$("#addSjpdForm #yjsj").val()==null){
						$.alert("请输入预估取样时间！");
						return false;
					}
				}
				if ($("#addSjpdForm input:radio[name='sfsf']:checked").val()=='1'){
					if($("#addSjpdForm #sfje").val()==""||$("#addSjpdForm #sfje").val()==null){
						$.alert("请输入收费金额！");
						return false;
					}
				}

				if ($("#addSjpdForm #sjpdid").val()){
					if (!$("#addSjpdForm #qylxr").val()){
						$("#addSjpdForm #qylxr").val("");
					}
					if (!$("#addSjpdForm #hzxm").val()){
						$("#addSjpdForm #hzxm").val("");
					}
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#addSjpdForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"addSjpdForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							searchResult();
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
function searchResult(isTurnBack){
	//关闭高级搜索条件
	$("#sjpdgl_formSearch #searchMore").slideUp("low");
	partner_turnOff=true;
	$("#sjpdgl_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#sjpdgl_formSearch #sjpdgl_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#sjpdgl_formSearch #sjpdgl_list').bootstrapTable('refresh');
	}
}
// //修改
// var modSjpdglConfig = {
// 	width		: "800px",
// 	modalName	:"modPartnerModal",
// 	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
// 	buttons		: {
// 		success : {
// 			label : "确 定",
// 			className : "btn-primary",
// 			callback : function() {
// 				$("#ajaxForm #hbmc").val($("#ajaxForm #hbmc").val().trim());
// 				if(!$("#ajaxForm").valid()){
// 					$.alert("请填写完整信息");
// 					return false;
// 				}
// 				var $this = this;
// 				var opts = $this["options"]||{};
// 				var jcdwids="";
// 				$("#ajaxForm #tb_partner_list").find("tbody").find("tr").each(function() {
// 					if($(this).attr("data-uniqueid")){
// 						jcdwids = jcdwids + ","+ $(this).attr("data-uniqueid");
// 					}
// 				});
// //					if(jcdwids.length == 0){
// //						$.alert("请至少选中一条信息！");
// //						return false;
// //					}
// 				jcdwids = jcdwids.substr(1);
// 				$("#ajaxForm  #jcdwids").val(jcdwids);
// //					debugger
// //					var alldate = $("#ajaxForm #tb_partner_list").bootstrapTable('getData');
// //					$("#ajaxForm  #jcdws").val(JSON.stringify(alldate));
//
// 				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
// 				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
// 					if(responseText["status"] == 'success'){
// 						$.success(responseText["message"],function() {
// 							if(opts.offAtOnce){
// 								$.closeModal(opts.modalName);
// 							}
// 							searchPartnerResult();
// 						});
// 					}else if(responseText["status"] == "fail"){
// 						preventResubmitForm(".modal-footer > button", false);
// 						$.error(responseText["message"],function() {
// 						});
// 					} else{
// 						$.alert(responseText["message"],function() {
// 						});
// 					}
// 				},".modal-footer > button");
// 				return false;
// 			}
// 		},
//
// 		cancel : {
// 			label : "关 闭",
// 			className : "btn-default"
// 		}
// 	}
// };
// //恢复
// var resumeSjpdglConfig = {
// 	width		: "800px",
// 	modalName	:"modPartnerModal",
// 	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
// 	buttons		: {
// 		success : {
// 			label : "确 定",
// 			className : "btn-primary",
// 			callback : function() {
// 				if(!$("#ajaxForm").valid()){
// 					$.alert("请填写完整信息");
// 					return false;
// 				}
// 				var $this = this;
// 				var opts = $this["options"]||{};
// 				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
// 				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
// 					if(responseText["status"] == 'success'){
// 						$.success(responseText["message"],function() {
// 							if(opts.offAtOnce){
// 								$.closeModal(opts.modalName);
// 							}
// 							searchPartnerResult();
// 						});
// 					}else if(responseText["status"] == "fail"){
// 						preventResubmitForm(".modal-footer > button", false);
// 						$.error(responseText["message"],function() {
// 						});
// 					} else{
// 						$.alert(responseText["message"],function() {
// 						});
// 					}
// 				},".modal-footer > button");
// 				return false;
// 			}
// 		},
//
// 		cancel : {
// 			label : "关 闭",
// 			className : "btn-default"
// 		}
// 	}
// };
//查看
var viewSjpdglConfig = {
	width		: "1000px",
	height		: "1000px",
	modalName	:"viewPartnerModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};


var sjpd_PageInit = function(){
	var oInit = new Object();
	var postdata = {};

	oInit.Init = function () {
		var zt = $("#sjpdgl_formSearch a[id^='zt_id_']");
		$.each(zt, function(i, n){
			var id = $(this).attr("id");
			var code = id.substring(id.lastIndexOf('_')+1,id.length);
			if(code == '0'){
				addTj('zt',code,'sjpdgl_formSearch');
			}
		});
	}
	return oInit;
}
// //增加
// var addSjpdglConfig = {
// 	width		: "800px",
// 	modalName	:"addPartnerModal",
// 	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
// 	buttons		: {
// 		success : {
// 			label : "确 定",
// 			className : "btn-primary",
// 			callback : function() {
// 				$("#ajaxForm #hbmc").val($("#ajaxForm #hbmc").val().trim());
// 				if(!$("#ajaxForm").valid()){
// 					$.alert("请填写完整信息");
// 					return false;
// 				}
// 				var $this = this;
// 				var opts = $this["options"]||{};
// 				var jcdwids="";
// 				$("#ajaxForm #tb_sjpdgl_list").find("tbody").find("tr").each(function() {
// 					if($(this).attr("data-uniqueid")){
// 						jcdwids = jcdwids + ","+ $(this).attr("data-uniqueid");
// 					}
// 				});
//
// 				jcdwids = jcdwids.substr(1);
// 				$("#ajaxForm  #jcdwids").val(jcdwids);
// 				//
// //					var alldate = $("#ajaxForm #tb_sjpdgl_list").bootstrapTable('getData');
// //					$("#ajaxForm  #jcdws").val(JSON.stringify(alldate));
// 				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
// 				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
// 					if(responseText["status"] == 'success'){
// 						$.success(responseText["message"],function() {
// 							if(opts.offAtOnce){
// 								$.closeModal(opts.modalName);
// 							}
// 							searchSjpdResult();
// 						});
// 					}else if(responseText["status"] == "fail"){
// 						preventResubmitForm(".modal-footer > button", false);
// 						$.error(responseText["message"],function() {
// 						});
// 					} else{
// 						$.alert(responseText["message"],function() {
// 						});
// 					}
// 				},".modal-footer > button");
// 				return false;
// 			}
// 		},
// 		cancel : {
// 			label : "关 闭",
// 			className : "btn-default"
// 		}
// 	}
// };
// //修改
// var modSjpdConfig = {
// 	width		: "800px",
// 	modalName	:"modPartnerModal",
// 	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
// 	buttons		: {
// 		success : {
// 			label : "确 定",
// 			className : "btn-primary",
// 			callback : function() {
// 				$("#ajaxForm #hbmc").val($("#ajaxForm #hbmc").val().trim());
// 				if(!$("#ajaxForm").valid()){
// 					$.alert("请填写完整信息");
// 					return false;
// 				}
// 				var $this = this;
// 				var opts = $this["options"]||{};
// 				var jcdwids="";
// 				$("#ajaxForm #tb_sjpdgl_list").find("tbody").find("tr").each(function() {
// 					if($(this).attr("data-uniqueid")){
// 						jcdwids = jcdwids + ","+ $(this).attr("data-uniqueid");
// 					}
// 				});
// //					if(jcdwids.length == 0){
// //						$.alert("请至少选中一条信息！");
// //						return false;
// //					}
// 				jcdwids = jcdwids.substr(1);
// 				$("#ajaxForm  #jcdwids").val(jcdwids);
// //					debugger
// //					var alldate = $("#ajaxForm #tb_sjpdgl_list").bootstrapTable('getData');
// //					$("#ajaxForm  #jcdws").val(JSON.stringify(alldate));
//
// 				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
// 				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
// 					if(responseText["status"] == 'success'){
// 						$.success(responseText["message"],function() {
// 							if(opts.offAtOnce){
// 								$.closeModal(opts.modalName);
// 							}
// 							searchSjpdResult();
// 						});
// 					}else if(responseText["status"] == "fail"){
// 						preventResubmitForm(".modal-footer > button", false);
// 						$.error(responseText["message"],function() {
// 						});
// 					} else{
// 						$.alert(responseText["message"],function() {
// 						});
// 					}
// 				},".modal-footer > button");
// 				return false;
// 			}
// 		},
//
// 		cancel : {
// 			label : "关 闭",
// 			className : "btn-default"
// 		}
// 	}
// };
// //恢复
// var resumePartnerConfig = {
// 	width		: "800px",
// 	modalName	:"modPartnerModal",
// 	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
// 	buttons		: {
// 		success : {
// 			label : "确 定",
// 			className : "btn-primary",
// 			callback : function() {
// 				if(!$("#ajaxForm").valid()){
// 					$.alert("请填写完整信息");
// 					return false;
// 				}
// 				var $this = this;
// 				var opts = $this["options"]||{};
// 				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
// 				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
// 					if(responseText["status"] == 'success'){
// 						$.success(responseText["message"],function() {
// 							if(opts.offAtOnce){
// 								$.closeModal(opts.modalName);
// 							}
// 							searchSjpdResult();
// 						});
// 					}else if(responseText["status"] == "fail"){
// 						preventResubmitForm(".modal-footer > button", false);
// 						$.error(responseText["message"],function() {
// 						});
// 					} else{
// 						$.alert(responseText["message"],function() {
// 						});
// 					}
// 				},".modal-footer > button");
// 				return false;
// 			}
// 		},
//
// 		cancel : {
// 			label : "关 闭",
// 			className : "btn-default"
// 		}
// 	}
// };
// //查看
// var viewSjpdConfig = {
// 	width		: "800px",
// 	modalName	:"viewPartnerModal",
// 	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
// 	buttons		: {
// 		cancel : {
// 			label : "关 闭",
// 			className : "btn-default"
// 		}
// 	}
// };

function searchSjpdResult(isTurnBack){
	//关闭高级搜索条件
	$("#sjpdgl_formSearch #searchMore").slideUp("low");
	sjpdgl_turnOff=true;
	$("#sjpdgl_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#sjpdgl_formSearch #sjpdgl_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#sjpdgl_formSearch #sjpdgl_list').bootstrapTable('refresh');
	}
}
$(function(){
	//0.界面初始化
	var oInit = new sjpd_PageInit();
	oInit.Init();
	var oTable=new Sjpd_TableInit();
	oTable.Init();
	//2.初始化Button的点击事件
	var oButtonInit = new Sjpd_oButtton();
	oButtonInit.Init();
	$("#mater_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	laydate.render({
		elem: '#sjpdgl_formSearch #lrsjstart'
		,type: 'date'
		// ,type: 'datetime'
		// ,ready: function(date){
		// 	if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
		// 		var myDate = new Date(); //实例一个时间对象；
		// 		this.dateTime.hours=myDate.getHours();
		// 		this.dateTime.minutes=myDate.getMinutes();
		// 		this.dateTime.seconds=myDate.getSeconds();
		// 	}
		// }
	});
	laydate.render({
		elem: '#sjpdgl_formSearch #lrsjend'
		,type: 'date'
		// ,type: 'datetime'
		// ,ready: function(date){
		// 	if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
		// 		var myDate = new Date(); //实例一个时间对象；
		// 		this.dateTime.hours=myDate.getHours();
		// 		this.dateTime.minutes=myDate.getMinutes();
		// 		this.dateTime.seconds=myDate.getSeconds();
		// 	}
		// }
	});
	jQuery('#sjpdgl_formSearch .chosen-select').chosen({width: '100%'});
	$("#sjpdgl_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
})
