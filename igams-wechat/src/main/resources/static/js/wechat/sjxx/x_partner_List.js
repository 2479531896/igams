var x_partner_turnOff=true;
var xPartner_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#x_partner_formSearch #x_partner_list").bootstrapTable({
			url: '/partner/partnerX/pageGetListPartnerX',         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#x_partner_formSearch #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   //是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "hb.hbmc",				//排序字段
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
			uniqueId: "hbxxzid",                     //每一行的唯一标识，一般为主键列
			showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: [{
				checkbox: true
			},{
				field: 'hbxxzid',
				title: '伙伴x限制id',
				width: '10%',
				align: 'left',
				visible:false
			},{
				field: 'hbid',
				title: '伙伴id',
				width: '10%',
				align: 'left',
				visible:false
			},{
				field: 'sysid',
				title: '实验室ID',
				width: '10%',
				align: 'left',
				visible:false
			},{
				field: 'hbmc',
				title: '合作伙伴',
				width: '40%',
				align: 'left',
				sortable: true,
				visible: true,
			},{
				field: 'jcdwmc',
				title: '检测单位',
				width: '40%',
				align: 'left',
				visible:true
			},{
                field: 'jcxmcskz3',
                title: '检测项目参数扩展3',
                width: '40%',
                align: 'left',
                visible:true
            },{
                 field: 'xzlx',
                 title: '限制类型',
                 width: '40%',
                 align: 'left',
                 visible:true
             }],
			onLoadSuccess:function(){
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				x_Partner_DealById(row.hbxxzid,'view',$("#x_partner_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#x_partner_formSearch #x_partner_list").colResizable({
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
			sortLastName: "hb.hbid,hbxxz.sysid", // 防止同名排位用
			sortLastOrder: "asc" // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return getXPartnerSearchData(map);
	};
	return oTableInit;
}

function getXPartnerSearchData(map){
	var cxtj=$("#x_partner_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#x_partner_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["hbmc"]=cxnr
	}else if(cxtj=="1"){
		map["jcdwmc"]=cxnr
	}
	// 删除标记
	var jcdws = jQuery('#x_partner_formSearch #jcdw_id_tj').val();
	map["jcdws"] = jcdws;
	return map;
}
//提供给导出用的回调函数
function xPartnerSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="hb.hbid,hbxxz.sysid";
	map["sortLastOrder"]="asc";
	map["sortName"]="hb.hbmc";
	map["sortOrder"]="asc";
	return getXPartnerSearchData(map);
}
//按钮动作函数
function x_Partner_DealById(hbxxzid,action,tourl){
	if(!tourl){
		return;
	}
	if(action=="add"){
		var url=tourl;
		$.showDialog(url,'新增伙伴X限制',addXPartnerConfig);
	}else if(action=="mod"){
		var url=tourl+"?hbxxzid="+hbxxzid
		$.showDialog(url,'修改伙伴X限制',modXPartnerConfig);
	}else if(action=="view"){
		var url=tourl+"?hbxxzid="+hbxxzid
		$.showDialog(url,'查看详细信息',viewXPartnerConfig);
	}
}

//按钮
var xPartner_oButtton= function (){
	var oInit=new Object();
	var postdata = {};

	oInit.Init=function(){
		var btn_query=$("#x_partner_formSearch #btn_query");
		var btn_add = $("#x_partner_formSearch #btn_add");
		var btn_mod = $("#x_partner_formSearch #btn_mod");
		var btn_view = $("#x_partner_formSearch #btn_view");
		var btn_del = $("#x_partner_formSearch #btn_del");

		/*-----------------------模糊查询------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchXPartnerResult(true);
			});
		}
		/*-----------------------新增------------------------------------*/
		btn_add.unbind("click").click(function(){
			x_Partner_DealById(null,"add",btn_add.attr("tourl"));
		});
		/*-----------------------修改------------------------------------*/
		btn_mod.unbind("click").click(function(){
			var sel_row = $('#x_partner_formSearch #x_partner_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				x_Partner_DealById(sel_row[0].hbxxzid,"mod",btn_mod.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*-----------------------查看------------------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row = $('#x_partner_formSearch #x_partner_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				x_Partner_DealById(sel_row[0].hbxxzid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*-----------------------删除------------------------------------*/
		btn_del.unbind("click").click(function(){
			var sel_row = $('#x_partner_formSearch #x_partner_list').bootstrapTable('getSelections');//获取选择行数据
			if (sel_row.length==0) {
				$.error("请至少选中一行");
				return;
			}else{
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids= ids + ","+ sel_row[i].hbxxzid;
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
										searchXPartnerResult();
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
		$("#x_partner_formSearch #sl_searchMore").on("click", function(ev){
			var ev=ev||event;
			if(x_partner_turnOff){
				$("#x_partner_formSearch #searchMore").slideDown("low");
				x_partner_turnOff=false;
				this.innerHTML="基本筛选";
			}else{
				$("#x_partner_formSearch #searchMore").slideUp("low");
				x_partner_turnOff=true;
				this.innerHTML="高级筛选";
			}
			ev.cancelBubble=true;
		});
	};
	return oInit;
};

var xpartner_PageInit = function(){
	var oInit = new Object();
	oInit.Init = function () {}
	return oInit;
}
//增加
var addXPartnerConfig = {
	width		: "1000px",
	modalName	:"addXPartnerModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				// 判断必填项是否选择
				if(!$("#ajaxForm #hbid option:selected").val()){
					$.alert("请选择伙伴！");
					return false;
				}
				if(!$("#ajaxForm #sysid option:selected").val()){
					$.alert("请选择实验室！");
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
							}
							searchXPartnerResult();
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

//修改
var modXPartnerConfig = {
	width		: "1000px",
	modalName	:"modXPartnerModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				// 判断必填项是否选择
				if(!$("#ajaxForm #hbid option:selected").val()){
					$.alert("请选择伙伴！");
					return false;
				}
				if(!$("#ajaxForm #sysid option:selected").val()){
					$.alert("请选择实验室！");
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
							}
							searchXPartnerResult();
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

//查看
var viewXPartnerConfig = {
	width		: "800px",
	modalName	:"viewXPartnerModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function searchXPartnerResult(isTurnBack){
	//关闭高级搜索条件
	$("#x_partner_formSearch #searchMore").slideUp("low");
	x_partner_turnOff=true;
	$("#x_partner_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#x_partner_formSearch #x_partner_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#x_partner_formSearch #x_partner_list').bootstrapTable('refresh');
	}
}
$(function(){
	//0.界面初始化
	var oInit = new xpartner_PageInit();
	oInit.Init();
	var oTable=new xPartner_TableInit();
	oTable.Init();
	//2.初始化Button的点击事件
	var oButtonInit = new xPartner_oButtton();
	oButtonInit.Init();
	jQuery('#x_partner_formSearch .chosen-select').chosen({width: '100%'});
	// $("#x_partner_formSearch [name='more']").each(function(){
	// 	$(this).on("click", s_showMoreFn);
	// });
})
