/**
 * 列表初始化
 * @returns {Object}
 * @constructor
 */
var KsInfo_TableInit=function(){
	var oTableInit=new Object();
	// 初始化table
	oTableInit.Init=function(){
		$("#ksInfo_formSearch #ksInfo_list").bootstrapTable({
			url: '/wechat/his/pageGetListWechatHis',         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#ksInfo_formSearch #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   //是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "xh",					//排序字段
			sortOrder: "asc",               	//排序方式
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
			//height: 500,                     	//行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "dwid",                	//每一行的唯一标识，一般为主键列
			showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                	//是否显示父子表
			columns: [{
				checkbox: true,
				width: '5%',
			},{
				field: 'xh',
				title: '序号',
				width: '5%',
				align: 'left',
				sortable: true,
				visible:true
			},{
				field: 'dwmc',
				title: '科室名称',
				width: '25%',
				align: 'left',
				visible: true
			},{
				field: 'dwdm',
				title: '科室代码',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'dh',
				title: '电话',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'kzcs',
				title: '是否显示输入框',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true,
				formatter:inputformat
			},{
				field: 'kzcs2',
				title: '是否允许接收onco报告',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true,
				formatter:oncoformat
			},{
				field: 'kzcs3',
				title: '科室匹配名称',
				width: '20%',
				align: 'left',
				sortable: true,
				visible: true
			},],
			onLoadSuccess:function(){
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				//双击事件
				ksInfoDealById(row.dwid,'view',$("#ksInfo_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#ksInfo_formSearch #ksInfo_list").colResizable({
			liveDrag:true,
			gripInnerHtml:"<div class='grip'></div>",
			draggingClass:"dragging",
			resizeMode:'fit',
			postbackSafe:true,
			partialRefresh:true
		})
	};
	//得到查询的参数
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			pageSize: params.limit,   // 页面大小
			pageNumber: (params.offset / params.limit) + 1,  // 页码
			access_token:$("#ac_tk").val(),
			sortName: params.sort,      // 排序列名
			sortOrder: params.order, // 排位命令（desc，asc）
			sortLastName: "dwid", // 防止同名排位用
			sortLastOrder: "desc" // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return ksInfoSearchData(map);
	};
	return oTableInit
};

function inputformat(value,row,index){
	var html;
	if (row.kzcs == '1'){
		html = '<span style="color: green">显示</span>'
	}else {
		html = '<span style="color: red">不显示</span>'
	}
	return html;
}
function oncoformat(value,row,index){
	var html;
	if (row.kzcs2 == '1'){
		html = '<span style="color: green">允许</span>'
	}else {
		html = '<span style="color: red">不允许</span>'
	}
	return html;
}

/**
 * 条件搜索
 * @param map
 * @returns {*}
 */
function ksInfoSearchData(map){
	var cxtj=$("#ksInfo_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#ksInfo_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["dwmc"]=cxnr;
	}
	return map;
}

/**
 * 按钮初始化
 * @returns {Object}
 */
var ksInfo_ButtonInit= function (){
	var oInit=new Object();
	var postdata = {};

	oInit.Init=function(){
		//初始化页面上面的按钮事件
		var btn_query=$("#ksInfo_formSearch #btn_query");
		var btn_add=$("#ksInfo_formSearch #btn_add");
		var btn_view=$("#ksInfo_formSearch #btn_view");
		var btn_del = $("#ksInfo_formSearch #btn_del");
		var btn_mod = $("#ksInfo_formSearch #btn_mod");
		var btn_push = $("#ksInfo_formSearch #btn_push");
		/*--------------------------------模糊查询---------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchKsInfoResult(true);
			});
		}
		/*--------------------------------新增版本信息---------------------------*/
		btn_add.unbind("click").click(function(){
			ksInfoDealById(null,"add",btn_add.attr("tourl"));
		});
		/*--------------------------------修改科室信息---------------------------*/
		// /wechat/his/pagedataModHis
		btn_mod.unbind("click").click(function(){
			var sel_row = $('#ksInfo_formSearch #ksInfo_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				ksInfoDealById(sel_row[0].dwid, "mod", btn_mod.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/* ------------------------------查看科室信息-----------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row=$('#ksInfo_formSearch #ksInfo_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				ksInfoDealById(sel_row[0].dwid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/* ------------------------------删除科室信息-----------------------------*/
		btn_del.unbind("click").click(function(){
			var sel_row = $('#ksInfo_formSearch #ksInfo_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==0){
				$.error("请至少选中一行");
				return;
			}else {
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids= ids + ","+ sel_row[i].dwid;
				}
				ids=ids.substr(1);
				$.confirm('您确定要删除所选择的科室吗？',function(result){
					if(result){
						jQuery.ajaxSetup({async:false});
						var url= btn_del.attr("tourl");
						jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
							setTimeout(function(){
								if(responseText["status"] == 'success'){
									$.success(responseText["message"],function() {
										searchKsInfoResult(true);
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
		/* ------------------------------推送科室信息-----------------------------*/
		// /wechat/his/pagedataCreatHis
		btn_push.unbind("click").click(function(){
			$.confirm('您确定要推送吗？',function(result){
				if(result){
					jQuery.ajaxSetup({async:false});
					jQuery.post(btn_push.attr("tourl"),{"access_token":$("#ac_tk").val()},function(responseText){
						setTimeout(function(){
							if(responseText["status"] == 'success'){
								$.success(responseText["message"],function() {
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
	}
	return oInit;
}

/**
 * 科室信息处理方法
 * @param id
 * @param action
 * @param tourl
 */
function ksInfoDealById(id,action,tourl){
	var url= tourl;
	if(!tourl){
		return;
	}
	if(action =='add'){
		var url=tourl;
		$.showDialog(url,'新增科室信息',addKsInfoConfig);
	}else if(action =='mod'){
		var url= tourl+"?dwid="+id;
		$.showDialog(url,'修改科室信息',modKsInfoConfig);
	}else if(action =='view'){
		var url= tourl+"?dwid="+id;
		$.showDialog(url,'查看科室信息',viewKsInfoConfig);
	}
}
/**
 * 增加科室信息
 */
var addKsInfoConfig = {
	width		: "800px",
	modalName	: "addKsInfoModal",
	formName	: "editKsInfoForm",
	offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#editKsInfoForm").valid()){// 表单验证
					$.alert("请填写完整信息");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#editKsInfoForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"editKsInfoForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}

							searchKsInfoResult(true);
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
 * 修改科室信息
 */
var modKsInfoConfig = {
	width		: "800px",
	modalName	: "modKsInfoModal",
	formName	: "editKsInfoForm",
	offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#editKsInfoForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#editKsInfoForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"editKsInfoForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							searchKsInfoResult(true);
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
 * 查看科室信息
 */
var viewKsInfoConfig={
	width		: "1000px",
	modalName	:"viewKsInfoModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

/**
 * 列表刷新
 */
function searchKsInfoResult(isTurnBack){
	/*	//关闭高级搜索条件
        $("#ksInfo_formSearch #searchMore").slideUp("low");
        VersionInfo_turnOff=true;
        $("#ksInfo_formSearch #sl_searchMore").html("高级筛选");*/
	if(isTurnBack){
		$('#ksInfo_formSearch #ksInfo_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#ksInfo_formSearch #ksInfo_list').bootstrapTable('refresh');
	}
}

$(function(){
	//初始化Table
	var oTable = new KsInfo_TableInit();
	oTable.Init();
	//2.初始化Button的点击事件
	var oButtonInit = new ksInfo_ButtonInit();
	oButtonInit.Init();

	jQuery('#ksInfo_formSearch .chosen-select').chosen({width: '100%'});
});

function customHis(node){
   var items = {
   		'addyy': {
               'label' : '新增同级单位','icon': "glyphicon glyphicon-plus",
               'action' : function (node) {
              	var dwid = node.reference.prevObject[0].id;
               	var url= "/wechat/his/pagedataGetWechatHis?dwid=" +dwid;
         		$.showDialog(url,'新增单位',addWechatHisConfig);
              }
          },
          'addzcd' : {
              'label' : '设置次级菜单',
				'icon': "glyphicon glyphicon-plus",
              'action' : function (node) { 
              	var dwid = node.reference.prevObject[0].id;
              	var url= "/wechat/his/pagedataGetWechatHis?fdwid=" +dwid;
          		$.showDialog(url,'设置子菜单',addWechatHisConfig);
              }
          },
          'mod' : {
              'label' : '修改单位信息',
				'icon': "glyphicon glyphicon-pencil",
              'action' : function (node) { 
              	var dwid = node.reference.prevObject[0].id;
              	var url= "/wechat/his/pagedataModHis?dwid=" +dwid;
          		$.showDialog(url,'修改菜单信息',modHisConfig);
              }
          },
          'del' : {
              'label' : '删除单位',
				'icon': "glyphicon glyphicon-remove",
              'action' : function (node) { 
              	var dwid = node.reference.prevObject[0].id;
              	var url= "/wechat/his/pagedataDelHis"
              	$.confirm('您确定要删除所选择的单位吗？',function(result){
  	    			if(result){
  	    				jQuery.ajaxSetup({async:false});
  	    				jQuery.post(url,{"dwid":dwid,"access_token":$("#ac_tk").val()},function(responseText){
  	    					setTimeout(function(){
  	    						if(responseText["status"] == 'success'){
  	    							$.success(responseText["message"],function() {
  	    								$('#wechatHis').jstree(true).refresh();
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
          },
          'cancle' : {
		        'label' : '取消',
		        'icon': "glyphicon glyphicon-ban-circle",
		        'action' : function () {
		        	$('#wechatHis').jstree(true).refresh();
		        }
		    }
     }

	return items;    //注意要有返回值
}




