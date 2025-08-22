var partner_turnOff=true;
var Partner_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#partner_formSearch #partner_list").bootstrapTable({
			url: '/partner/partner/pageGetListView',         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#partner_formSearch #toolbar',                //工具按钮用哪个容器
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
			uniqueId: "hbid",                     //每一行的唯一标识，一般为主键列
			showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: [{
				checkbox: true
			},{
				field: 'hbid',
				title: '伙伴id',
				width: '10%',
				align: 'left',
				visible:false
			},{
				field: 'hbmc',
				title: '合作伙伴',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true,
			},{
				field: 'wxid',
				title: '微信id',
				width: '10%',
				align: 'left',
				visible:false
			},{
				field: 'wxm',
				title: '微信名称',
				width: '10%',
				align: 'left',
				visible: false
			},{
				field: 'hblxmc',
				title: '伙伴类型',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'xtyhm',
				title: '用户',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'bgmb',
				title: '报告模板',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'province',
				title: '省份',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'yptgsmc',
				title: '平台归属',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true,
				cellStyle: function(value, row, index) {
					return {css: {"background-color": 'RGB(240,255,255)'}};
				}
			},{
				field: 'ptgsmc',
				title: '销售部门',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true,
				cellStyle: function(value, row, index) {
					return {css: {"background-color": 'RGB(240,255,255)'}};
				}
			},{
				field: 'hzgsmc',
				title: '合作公司',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true,
				cellStyle: function(value, row, index) {
					return {css: {"background-color": 'RGB(240,255,255)'}};
				}
			},{
				field: 'flmc',
				title: '分类',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true,
				cellStyle: function(value, row, index) {
					return {css: {"background-color": 'RGB(240,255,255)'}};
				}
			},{
				field: 'zflmc',
				title: '子分类',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true,
				cellStyle: function(value, row, index) {
					return {css: {"background-color": 'RGB(240,255,255)'}};
				}
			},{
				field: 'sjqfmc',
				title: '送检区分',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true,
				cellStyle: function(value, row, index) {
					return {css: {"background-color": 'RGB(240,255,255)'}};
				}
			},{
				field: 'swmc',
				title: '商务名称',
				width: '10%',
				align: 'left',
				visible: true,
				cellStyle: function(value, row, index) {
					return {css: {"background-color": 'RGB(240,255,255)'}};
				}
			},{
				field: 'tjmc',
				title: '统计名称',
				width: '10%',
				align: 'left',
				visible: true,
				cellStyle: function(value, row, index) {
					return {css: {"background-color": 'RGB(240,255,255)'}};
				}
			},{
				field: 'jcdwstr',
				title: '检测单位',
				width: '30%',
				align: 'left',
				visible: true
			},{
				field: 'scbj',
				title: '状态',
				width: '10%',
				formatter:ztformat,
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'cskz3',
				title: 'onco权限',
				width: '10%',
				formatter:cskz3format,
				align: 'left',
				sortable: true,
				visible: true
			},{
                field: 'zdfs',
                title: '自动发送mNGS',
                width: '10%',
                formatter:zdfsformat,
                align: 'left',
                sortable: true,
                visible: true
            },{
                 field: 'zdfst',
                 title: '自动发送tNGS',
                 width: '10%',
                 formatter:zdfstformat,
                 align: 'left',
                 sortable: true,
                 visible: true
             },{
				field: 'city',
				title: '城市',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'bgfsmc',
				title: '报告方式',
				width: '8%',
				align: 'left',
				visible: false
			},{
				field: 'fsfs',
				title: '发送方式',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'yx',
				title: '邮箱',
				width: '10%',
				align: 'left',
				visible: false
			},{
				field: 'cskz2',
				title: '外部接口',
				width: '10%',
				align: 'left',
				visible: false
			},{
				field: 'gzlxmc',
				title: '盖章类型',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: false
			}],
			onLoadSuccess:function(){
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				Partner_DealById(row.hbid,'view',$("#partner_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#partner_formSearch #partner_list").colResizable({
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
			sortLastName: "hb.hbid", // 防止同名排位用
			sortLastOrder: "asc" // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return getPartnerSearchData(map);
	};
	return oTableInit;
}

//状态格式化
function ztformat(value,row,index){
	if(row.scbj=='1'){
		return '已删除';
	}else if(row.scbj=='0'){
		return '正常';
	}else if(row.scbj=='2'){
		return '停用';
	}
}
//状态格式化
function cskz3format(value,row,index){
    if(row.cskz3=='1'){
        return '是';
    }else {
        return '否';
    }
}
function zdfsformat(value,row,index){
    if(row.zdfs=='1'){
        return '是';
    }if(row.zdfs=='0'){
         return '否';
     }else {
        return '未设置';
    }
}
function zdfstformat(value,row,index){
    if(row.zdfst=='1'){
        return '是';
    }if(row.zdfst=='0'){
         return '否';
     }else {
        return '未设置';
    }
}
function getPartnerSearchData(map){
	var cxtj=$("#partner_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#partner_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["hbmc"]=cxnr
	}else if(cxtj=="1"){
		map["sf"]=cxnr
	}else if(cxtj=="2"){
		map["cs"]=cxnr
	}else if(cxtj=="3"){
		map["xtyhm"]=cxnr
	}else if(cxtj=="4"){
		map["tjmc"]=cxnr
	}else if(cxtj=="5"){
		map["swmc"]=cxnr
	}else if(cxtj=="6"){
		map["hzgsmc"]=cxnr
	}
	// 删除标记
	var scbjs = jQuery('#partner_formSearch #scbj_id_tj').val();
	map["scbjs"] = scbjs;
	// 分类
	var fls = jQuery('#partner_formSearch #fl_id_tj').val();
	map["fls"] = fls;
	// 子分类
	var zfls = jQuery('#partner_formSearch #zfl_id_tj').val();
	map["zfls"] = zfls;
	// 报告模板
	var bgmbs = jQuery('#partner_formSearch #bgmb_id_tj').val();
	map["bgmbs"] = bgmbs;
	// 业务部门
	var ptgss = jQuery('#partner_formSearch #ptgs_id_tj').val();
	map["ptgss"] = ptgss.replace(/'/g, "");
	// 平台归属
	var yptgss = jQuery('#partner_formSearch #yptgs_id_tj').val();
	map["yptgss"] = yptgss.replace(/'/g, "");
	// 平台归属
	var sfs = jQuery('#partner_formSearch #sf_id_tj').val();
	map["sfs"] = sfs.replace(/'/g, "");
	// onco权限
    var oncoqx = jQuery('#partner_formSearch #oncoqx_id_tj').val();
    map["cskz3"] = oncoqx;
    // 自动发送
    var zdfs = jQuery('#partner_formSearch #zdfs_id_tj').val();
    map["zdfs"] = zdfs;
    var zdfst = jQuery('#partner_formSearch #zdfs_id_tj').val();
    map["zdfst"] = zdfst;
	return map;
}
//提供给导出用的回调函数
function PartnerSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="hb.hbid";
	map["sortLastOrder"]="asc";
	map["sortName"]="hb.hbmc";
	map["sortOrder"]="asc";
	return getPartnerSearchData(map);
}
//按钮动作函数
function Partner_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action=="add"){
		var url=tourl;
		$.showDialog(url,'新增合作伙伴',addPartnerConfig);
	}else if(action=="mod"){
		var url=tourl+"?hbid="+id
		$.showDialog(url,'修改合作伙伴',modPartnerConfig);
	}else if(action=="view"){
		var url=tourl+"?hbid="+id
		$.showDialog(url,'查看详细信息',viewPartnerConfig);
	}else if(action=="resume"){
		var url=tourl+"?hbid="+id
		$.showDialog(url,'恢复合作伙伴',resumePartnerConfig);
	}else if (action=="batchmod"){
		var url=tourl+"?ids="+id
		$.showDialog(url,'批量修改收费标准',batchmodSfbzPartnerConfig);
	}else if (action=="setmessage"){
        var url=tourl+"?hbid="+id
        $.showDialog(url,'消息订阅',setmessageConfig);
    }
}

//按钮
var Partner_oButtton= function (){
	var oInit=new Object();
	var postdata = {};

	oInit.Init=function(){
		var btn_query=$("#partner_formSearch #btn_query");
		var btn_add = $("#partner_formSearch #btn_add");
		var btn_mod = $("#partner_formSearch #btn_mod");
		var btn_view = $("#partner_formSearch #btn_view");
		var btn_del = $("#partner_formSearch #btn_del");
		var btn_resume= $("#partner_formSearch #btn_resume");
		var btn_enable= $("#partner_formSearch #btn_enable");
		var btn_disable= $("#partner_formSearch #btn_disable");
		var flBind = $("#partner_formSearch #fl_id ul li a");
		var btn_oncopower= $("#partner_formSearch #btn_oncopower");
		var btn_maintenancedqxx= $("#partner_formSearch #btn_maintenancedqxx");
		var btn_selectexport = $("#partner_formSearch #btn_selectexport");//选中导出
		var btn_searchexport = $("#partner_formSearch #btn_searchexport");//搜索导出
		var btn_batchmod = $("#partner_formSearch #btn_batchmod");//搜索导出
        var btn_setmessage = $("#partner_formSearch #btn_setmessage");//消息订阅
		/*-----------------------模糊查询------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchPartnerResult(true);
			});
		}
		/*-----------------------新增------------------------------------*/
		btn_add.unbind("click").click(function(){
			Partner_DealById(null,"add",btn_add.attr("tourl"));
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
									searchPartnerResult();
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
						},1);

					},'json');
					jQuery.ajaxSetup({async:true});
				}
			});
		});
		/*-----------------------修改------------------------------------*/
		btn_mod.unbind("click").click(function(){
			var sel_row = $('#partner_formSearch #partner_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				Partner_DealById(sel_row[0].hbid,"mod",btn_mod.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*-----------------------消息订阅------------------------------------*/
		btn_setmessage.unbind("click").click(function(){
			var sel_row = $('#partner_formSearch #partner_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				Partner_DealById(sel_row[0].hbid+"&yhid="+sel_row[0].yhid,"setmessage",btn_setmessage.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});

		/*-----------------------批量修改收费标准 ------------------------------------*/
		btn_batchmod.unbind("click").click(function(){
			var sel_row = $('#partner_formSearch #partner_list').bootstrapTable('getSelections');//获取选择行数据
			if (sel_row.length==0) {
				$.error("请至少选中一行");
				return;
			}else {
				var ids = "";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + "," + sel_row[i].hbid;
				}
				ids = ids.substr(1);
				Partner_DealById(ids, "batchmod", btn_batchmod.attr("tourl"));
			}
		});
		/*-----------------------恢复------------------------------------*/
		btn_resume.unbind("click").click(function(){
			var sel_row = $('#partner_formSearch #partner_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				Partner_DealById(sel_row[0].hbid,"resume",btn_resume.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*-----------------------恢复------------------------------------*/
		btn_maintenancedqxx.unbind("click").click(function(){
			$.confirm('您确定要维护大区信息吗？',function(result){
				if(result){
					jQuery.ajaxSetup({async:false});
					var url= btn_maintenancedqxx.attr("tourl");
					jQuery.post(url,{"access_token":$("#ac_tk").val()},function(responseText){
						setTimeout(function(){
							if(responseText["status"] == 'success'){
								$.success(responseText["message"],function() {
									searchPartnerResult();
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
		/*-----------------------查看------------------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row = $('#partner_formSearch #partner_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				Partner_DealById(sel_row[0].hbid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*-----------------------选中导出------------------------------------*/
		btn_selectexport.unbind("click").click(function(){
			var sel_row = $('#partner_formSearch #partner_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].hbid;
				}
				ids = ids.substr(1);
				$.showDialog(exportPrepareUrl+"?ywid=PARTNER_SELECT&expType=select&ids="+ids
					,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
			}else{
				$.error("请选择数据");
			}
		});
		/*-----------------------搜索导出------------------------------------*/
		btn_searchexport.unbind("click").click(function(){
			$.showDialog(exportPrepareUrl+"?ywid=PARTNER_SEARCH&expType=search&callbackJs=PartnerSearchData"
				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
		});
		/*-----------------------删除------------------------------------*/
		btn_del.unbind("click").click(function(){
			var sel_row = $('#partner_formSearch #partner_list').bootstrapTable('getSelections');//获取选择行数据
			if (sel_row.length==0) {
				$.error("请至少选中一行");
				return;
			}else{
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids= ids + ","+ sel_row[i].hbid;
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
										searchPartnerResult();
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
			var sel_row = $('#partner_formSearch #partner_list').bootstrapTable('getSelections');//获取选择行数据
			if (sel_row.length==0) {
				$.error("请至少选中一行");
				return;
			}else{
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids= ids + ","+ sel_row[i].hbid;
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
										searchPartnerResult();
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
			var sel_row = $('#partner_formSearch #partner_list').bootstrapTable('getSelections');//获取选择行数据
			if (sel_row.length==0) {
				$.error("请至少选中一行");
				return;
			}else{
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids= ids + ","+ sel_row[i].hbid;
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
										searchPartnerResult();
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
		$("#partner_formSearch #sl_searchMore").on("click", function(ev){
			var ev=ev||event;
			if(partner_turnOff){
				$("#partner_formSearch #searchMore").slideDown("low");
				partner_turnOff=false;
				this.innerHTML="基本筛选";
			}else{
				$("#partner_formSearch #searchMore").slideUp("low");
				partner_turnOff=true;
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

var partner_PageInit = function(){
	var oInit = new Object();
	var postdata = {};

	oInit.Init = function () {
		var scbj = $("#partner_formSearch a[id^='scbj_id_']");
		$.each(scbj, function(i, n){
			var id = $(this).attr("id");
			var code = id.substring(id.lastIndexOf('_')+1,id.length);
			if(code == '0'){
				addTj('scbj',code,'partner_formSearch');
			}
		});

	}
	return oInit;
}
//批量修改
var batchmodSfbzPartnerConfig = {
	width		: "1000px",
	modalName	:"batchmodSfbzPartnerModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm_batchmodSfbz").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				if (t_map.rows.length <= 0){
					$.alert("请至少选择一条项目");
					return false;
				}
				//收费标准处理
				var xmhasChild = [];
				var zxmList = JSON.parse($("#ajaxForm_batchmodSfbz #zxmList").val());
				for(var i=0 ; i<zxmList.length ; i++){
					if(zxmList[i].fcsid != null && zxmList[i].fcsid != undefined && zxmList[i].fcsid != ''){
						if(!xmhasChild.contains(zxmList[i].fcsid)){
							xmhasChild.push(zxmList[i].fcsid);
						}
					}
				}

				if(t_map.rows != null && t_map.rows.length > 0){
					var json = [];
					var repeatXm =[];
					var repeatZxm =[];
					for(var i=0;i<t_map.rows.length;i++){
						if (t_map.rows[i].xm == ''||t_map.rows[i].xm == undefined||t_map.rows[i].xm == null||t_map.rows[i].sfbz == ''||t_map.rows[i].sfbz == undefined||t_map.rows[i].sfbz == null){
							$.alert("收费标准：第"+(i+1)+"行，项目不能为空！或 收费标准不能为空！");
							return false;
						}
						if (xmhasChild.contains(t_map.rows[i].xm)){
							//有子项目
							if (t_map.rows[i].zxm == ''||t_map.rows[i].zxm == undefined||t_map.rows[i].zxm == null){
								$.alert("收费标准：第"+(i+1)+"行，子项目未填！");
								return false;
							}
							if(repeatXm.contains(t_map.rows[i].xm)){
								//存在项目重复，比较子项目
								if(repeatZxm.contains(t_map.rows[i].zxm)){
									$.alert("收费标准：第"+(i+1)+"行，子项目重复！");
									return false;
								}else {
									repeatZxm.push(t_map.rows[i].zxm);
								}
							}else {
								//项目不重复
								repeatXm.push(t_map.rows[i].xm);
								repeatZxm.push(t_map.rows[i].zxm);
							}
						}else {
							//无子项目
							if(repeatXm.contains(t_map.rows[i].xm)){
								$.alert("收费标准：第"+(i+1)+"行，项目重复！");
								return false;
							}else {
								repeatXm.push(t_map.rows[i].xm);
							}
						}

					}
					for(var i=0;i<t_map.rows.length;i++){
						var sz = {"hbid":'',"xm":'',"zxm":'',"sfbz":'',"tqje":'',"ksrq":'',"jsrq":''};
						sz.hbid = t_map.rows[i].hbid;
						sz.xm = t_map.rows[i].xm;
						sz.zxm = t_map.rows[i].zxm;
						sz.sfbz = t_map.rows[i].sfbz;
						sz.tqje = t_map.rows[i].tqje;
						sz.ksrq = t_map.rows[i].ksrq;
						sz.jsrq = t_map.rows[i].jsrq;
						json.push(sz);
					}
					$("#ajaxForm_batchmodSfbz #sfbz_json").val(JSON.stringify(json));
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#ajaxForm_batchmodSfbz input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"ajaxForm_batchmodSfbz",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							searchPartnerResult();
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
//增加
var addPartnerConfig = {
	width		: "1000px",
	modalName	:"addPartnerModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				$("#ajaxForm #hbmc").val($("#ajaxForm #hbmc").val().trim());
				if(!$("#ajaxForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				var kzsz={"zdfs":$('#ajaxForm input[name="zdfs"]:checked').val(),"zdfst":$('#ajaxForm input[name="zdfst"]:checked').val()};
                $("#ajaxForm #kzsz").val(JSON.stringify(kzsz));
				var email=$("#ajaxForm #yx").val().split(",");

				var emailRegExp= /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
				if(email.length>1||email[0]!=""){
					for (let i = 0; i < email.length; i++) {
						var trim = $.trim(email[i]);
						if(!emailRegExp.test(trim))	{
							$.alert(trim+"不是正确的邮箱地址！");
							return false;
						}
					}
				}
				var yx="";
				if(email.length>1||email[0]!=""){
					for (let i = 0; i < email.length; i++) {
						 yx=yx+$.trim(email[i])+",";
					}
				}
				if (yx!=""){
					yx = yx.substring(0, yx.lastIndexOf(','));
				}
				$("#ajaxForm #yx").val(yx);
				var selkh=$("#ajaxForm #khids").tagsinput('items');
				var hzgsmc="";
				for(var i=0;i<selkh.length;i++){
					hzgsmc+=","+selkh[i].text;
				}
				if(hzgsmc){
					$("#ajaxForm  #hzgsmc").val(hzgsmc.substring(1));
				}
				var $this = this;
				var opts = $this["options"]||{};
				var jcdwids="";
				$("#ajaxForm #tb_partner_list").find("tbody").find("tr").each(function() {
					if($(this).attr("data-uniqueid")){
						jcdwids = jcdwids + ","+ $(this).attr("data-uniqueid");
					}
				});
				jcdwids = jcdwids.substr(1);
				$("#ajaxForm  #jcdwids").val(jcdwids);
				var json_t=[];
				var val = $("#ajaxForm  #bgmb_json").val();
				var flag=false;
				if(val!="" && val!=null){
					json_t=JSON.parse(val);
					for (var i = 0; i < json_t.length; i++) {
						var xh=i+1;
						var bgmbid=$("#ajaxForm  #bgmbid_"+xh).val();
						json_t[i].bgmbid=bgmbid;
						var bgmbid2=$("#ajaxForm  #bgmbid2_"+xh).val();
						json_t[i].bgmbid2=bgmbid2;
						if(bgmbid!=null&&bgmbid!=''){
							flag=true;
						}
					}
					$("#ajaxForm #bgmb_json").val(JSON.stringify(json_t));
				}
				if(!flag){
					$.alert("请至少选择一个项目的模板！");
					return false;
				}

				//
//					var alldate = $("#ajaxForm #tb_partner_list").bootstrapTable('getData');
//					$("#ajaxForm  #jcdws").val(JSON.stringify(alldate));
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
                //下面为收费标准处理
				var xmhasChild = [];
				var zxmList = JSON.parse($("#ajaxForm #zxmList").val());
				for(var i=0 ; i<zxmList.length ; i++){
					if(zxmList[i].fcsid != null && zxmList[i].fcsid != undefined && zxmList[i].fcsid != ''){
						if(!xmhasChild.contains(zxmList[i].fcsid)){
							xmhasChild.push(zxmList[i].fcsid);
						}
					}
				}

				if(t_map.rows != null && t_map.rows.length > 0){
					var json = [];
					var repeatXm =[];
					var repeatZxm =[];
					for(var i=0;i<t_map.rows.length;i++){
						if (t_map.rows[i].xm == ''||t_map.rows[i].xm == undefined||t_map.rows[i].xm == null||t_map.rows[i].sfbz == ''||t_map.rows[i].sfbz == undefined||t_map.rows[i].sfbz == null){
							$.alert("收费标准：第"+(i+1)+"行，项目不能为空！或 收费标准不能为空！");
							return false;
						}
						if (xmhasChild.contains(t_map.rows[i].xm)){
							//有子项目
							if (t_map.rows[i].zxm == ''||t_map.rows[i].zxm == undefined||t_map.rows[i].zxm == null){
								$.alert("收费标准：第"+(i+1)+"行，子项目未填！");
								return false;
							}
							if(repeatXm.contains(t_map.rows[i].xm)){
								//存在项目重复，比较子项目
								if(repeatZxm.contains(t_map.rows[i].zxm)){
									$.alert("收费标准：第"+(i+1)+"行，子项目重复！");
									return false;
								}else {
									repeatZxm.push(t_map.rows[i].zxm);
								}
							}else {
								//项目不重复
								repeatXm.push(t_map.rows[i].xm);
								repeatZxm.push(t_map.rows[i].zxm);
							}
						}else {
							//无子项目
							if(repeatXm.contains(t_map.rows[i].xm)){
								$.alert("收费标准：第"+(i+1)+"行，项目重复！");
								return false;
							}else {
								repeatXm.push(t_map.rows[i].xm);
							}
						}

					}
					for(var i=0;i<t_map.rows.length;i++){
						var sz = {"hbid":'',"xm":'',"zxm":'',"sfbz":'',"tqje":'',"ksrq":'',"jsrq":''};
						sz.hbid = t_map.rows[i].hbid;
						sz.xm = t_map.rows[i].xm;
						sz.zxm = t_map.rows[i].zxm;
						sz.sfbz = t_map.rows[i].sfbz;
						sz.tqje = t_map.rows[i].tqje;
						sz.ksrq = t_map.rows[i].ksrq;
						sz.jsrq = t_map.rows[i].jsrq;
						json.push(sz);
					}
					$("#ajaxForm #sfbz_json").val(JSON.stringify(json));
				}
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							searchPartnerResult();
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
var setmessageConfig = {
	width		: "1200px",
	modalName	:"setmessageModal",
	formName	: "setMessageForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				$("#setMessageForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"setMessageForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							searchPartnerResult();
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
var modPartnerConfig = {
	width		: "1000px",
	modalName	:"modPartnerModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				$("#ajaxForm #hbmc").val($("#ajaxForm #hbmc").val().trim());
				if(!$("#ajaxForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				var email=$("#ajaxForm #yx").val().split(",");
				var kzsz={"zdfs":$('#ajaxForm input[name="zdfs"]:checked').val(),"zdfst":$('#ajaxForm input[name="zdfst"]:checked').val()};
                $("#ajaxForm #kzsz").val(JSON.stringify(kzsz));
				var emailRegExp= /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
				if(email.length>1||email[0]!=""){
					for (let i = 0; i < email.length; i++) {
						var trim = $.trim(email[i]);
						if(!emailRegExp.test(trim))	{
							$.alert(trim+"不是正确的邮箱地址！");
							return false;
						}
					}
				}
				var yx="";
				if(email.length>1||email[0]!=""){
					for (let i = 0; i < email.length; i++) {
						yx=yx+$.trim(email[i])+",";
					}
				}
				if (yx!=""){
					yx = yx.substring(0, yx.lastIndexOf(','));
				}
				$("#ajaxForm #yx").val(yx);
				var selkh=$("#ajaxForm #khids").tagsinput('items');
				var hzgsmc="";
				for(var i=0;i<selkh.length;i++){
					hzgsmc+=","+selkh[i].text;
				}
				if(hzgsmc){
					$("#ajaxForm  #hzgsmc").val(hzgsmc.substring(1));
				}
				var $this = this;
				var opts = $this["options"]||{};
				var jcdwids="";
				$("#ajaxForm #tb_partner_list").find("tbody").find("tr").each(function() {
					if($(this).attr("data-uniqueid")){
						jcdwids = jcdwids + ","+ $(this).attr("data-uniqueid");
					}
				});
//					if(jcdwids.length == 0){
//						$.alert("请至少选中一条信息！");
//						return false;
//					}
				jcdwids = jcdwids.substr(1);
				$("#ajaxForm  #jcdwids").val(jcdwids);
				var json_t=[];
				var val = $("#ajaxForm  #bgmb_json").val();
				var flag=false;
				if(val!="" && val!=null){
					json_t=JSON.parse(val);
					for (var i = 0; i < json_t.length; i++) {
						var xh=i+1;
						var bgmbid=$("#ajaxForm  #bgmbid_"+xh).val();
						json_t[i].bgmbid=bgmbid;
						var bgmbid2=$("#ajaxForm  #bgmbid2_"+xh).val();
						json_t[i].bgmbid2=bgmbid2;
						if(bgmbid!=null&&bgmbid!=''){
							flag=true;
						}
					}
					$("#ajaxForm #bgmb_json").val(JSON.stringify(json_t));
				}
				if(!flag){
					$.alert("请至少选择一个项目的模板！");
					return false;
				}
//					
//					var alldate = $("#ajaxForm #tb_partner_list").bootstrapTable('getData');
//					$("#ajaxForm  #jcdws").val(JSON.stringify(alldate));

				//收费标准处理
				var xmhasChild = [];
				var zxmList = JSON.parse($("#ajaxForm #zxmList").val());
				for(var i=0 ; i<zxmList.length ; i++){
					if(zxmList[i].fcsid != null && zxmList[i].fcsid != undefined && zxmList[i].fcsid != ''){
						if(!xmhasChild.contains(zxmList[i].fcsid)){
							xmhasChild.push(zxmList[i].fcsid);
						}
					}
				}

				if(t_map.rows != null && t_map.rows.length > 0){
					var json = [];
					var repeatXm =[];
					var repeatZxm =[];
					for(var i=0;i<t_map.rows.length;i++){
						if (t_map.rows[i].xm == ''||t_map.rows[i].xm == undefined||t_map.rows[i].xm == null||t_map.rows[i].sfbz == ''||t_map.rows[i].sfbz == undefined||t_map.rows[i].sfbz == null){
							$.alert("收费标准：第"+(i+1)+"行，项目不能为空！或 收费标准不能为空！");
							return false;
						}
						if (xmhasChild.contains(t_map.rows[i].xm)){
							//有子项目
							if (t_map.rows[i].zxm == ''||t_map.rows[i].zxm == undefined||t_map.rows[i].zxm == null){
								$.alert("收费标准：第"+(i+1)+"行，子项目未填！");
								return false;
							}
							if(repeatXm.contains(t_map.rows[i].xm)){
								//存在项目重复，比较子项目
								if(repeatZxm.contains(t_map.rows[i].zxm)){
									$.alert("收费标准：第"+(i+1)+"行，子项目重复！");
									return false;
								}else {
									repeatZxm.push(t_map.rows[i].zxm);
								}
							}else {
								//项目不重复
								repeatXm.push(t_map.rows[i].xm);
								repeatZxm.push(t_map.rows[i].zxm);
							}
						}else {
							//无子项目
							if(repeatXm.contains(t_map.rows[i].xm)){
								$.alert("收费标准：第"+(i+1)+"行，项目重复！");
								return false;
							}else {
								repeatXm.push(t_map.rows[i].xm);
							}
						}

					}
					for(var i=0;i<t_map.rows.length;i++){
						var sz = {"hbid":'',"xm":'',"zxm":'',"sfbz":'',"tqje":'',"ksrq":'',"jsrq":''};
						sz.hbid = t_map.rows[i].hbid;
						sz.xm = t_map.rows[i].xm;
						sz.zxm = t_map.rows[i].zxm;
						sz.sfbz = t_map.rows[i].sfbz;
						sz.tqje = t_map.rows[i].tqje;
						sz.ksrq = t_map.rows[i].ksrq;
						sz.jsrq = t_map.rows[i].jsrq;
						json.push(sz);
					}
					$("#ajaxForm #sfbz_json").val(JSON.stringify(json));
				}

				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							searchPartnerResult();
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
//恢复
var resumePartnerConfig = {
	width		: "800px",
	modalName	:"modPartnerModal",
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
				var $this = this;
				var opts = $this["options"]||{};
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							searchPartnerResult();
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
var viewPartnerConfig = {
	width		: "800px",
	modalName	:"viewPartnerModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
//子分类的取得
function getZfls() {
	// 分类
	var fl = jQuery('#partner_formSearch #fl_id_tj').val();
	if (!isEmpty(fl)) {
		fl = "'" + fl + "'";
		jQuery("#partner_formSearch #zfl_id").removeClass("hidden");
	}else{
		jQuery("#partner_formSearch #zfl_id").addClass("hidden");
	}
	// 项目类别不为空
	if (!isEmpty(fl)) {
		var url = "/systemmain/data/ansyGetJcsjListInStop";
		$.ajax({
			type : "POST",
			url : url,
			data : {"fcsids":fl,"access_token":$("#ac_tk").val()},
			dataType : "json",
			success : function(data){
				if(data.length > 0) {
					var html = "";
					$.each(data,function(i){
						html += "<li>";
						html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('zfl','" + data[i].csid + "','partner_formSearch');\" id=\"zfl_id_" + data[i].csid + "\"><span>" + data[i].csmc + "</span></a>";
						html += "</li>";
					});
					jQuery("#partner_formSearch #ul_zfl").html(html);
					jQuery("#partner_formSearch #ul_zfl").find("[name='more']").each(function(){
						$(this).on("click", s_showMoreFn);
					});
				} else {
					jQuery("#partner_formSearch #ul_zfl").empty();

				}
				jQuery("#partner_formSearch [id^='zfl_li_']").remove();
				$("#partner_formSearch #zfl_id_tj").val("");
			}
		});
	} else {
		jQuery("#partner_formSearch #div_zfl").empty();
		jQuery("#partner_formSearch [id^='zfl_li_']").remove();
		$("#partner_formSearch #zfl_id_tj").val("");
	}
}

function searchPartnerResult(isTurnBack){
	//关闭高级搜索条件
	$("#partner_formSearch #searchMore").slideUp("low");
	partner_turnOff=true;
	$("#partner_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#partner_formSearch #partner_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#partner_formSearch #partner_list').bootstrapTable('refresh');
	}
}
$(function(){
	//0.界面初始化
	var oInit = new partner_PageInit();
	oInit.Init();
	var oTable=new Partner_TableInit();
	oTable.Init();
	//2.初始化Button的点击事件
	var oButtonInit = new Partner_oButtton();
	oButtonInit.Init();
	$("#mater_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	jQuery('#partner_formSearch .chosen-select').chosen({width: '100%'});
	$("#partner_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
})
