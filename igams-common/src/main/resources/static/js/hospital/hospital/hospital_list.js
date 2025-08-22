var yyxx_turnOff=true;
var yyxx_TableInit = function () { 
	 var oTableInit = new Object();
	    
	// 初始化Table
	oTableInit.Init = function () {
		$('#yyxx_formSearch #tb_list').bootstrapTable({
			url: '/hospital/yyxx/pageGetListYyxx',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#yyxx_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"sfmc,dwmc,csmc",					// 排序字段
			sortOrder: "ASC",                   // 排序方式
			queryParams: oTableInit.queryParams,// 传递参数（*）
			sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
			pageNumber:1,                       // 初始化加载第一页，默认第一页
			pageSize: 15,                       // 每页的记录行数（*）
			pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
			paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
			search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			showColumns: true,                  // 是否显示所有的列
			showRefresh: true,                  // 是否显示刷新按钮
			minimumCountColumns: 2,             // 最少允许的列数
			clickToSelect: true,                // 是否启用点击选中行
			// height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "dwid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮                 								
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true,
				width: '4%'
			}, {
				'field': '',
			    'title': '序号',
			    'align': 'center',
			    'width': '4%',
			    'formatter': function (value, row, index) {
			    　　var options = $('#yyxx_formSearch #tb_list').bootstrapTable('getOptions'); 
			    　　return options.pageSize * (options.pageNumber - 1) + index + 1; 
			    }
			}, {
				field: 'dwmc',
				title: '单位名称',
				width: '20%',
				align: 'left',
				visible: true
			}, {
				field: 'dwjc',
				title: '单位简称',
				width: '15%',
				align: 'left',
				visible: true
			}, {
				field: 'dwqtmc',
				title: '单位其他名称',
				width: '17%',
				align: 'left',
				visible: true
			}, {
				field: 'dwdjmc',
				title: '单位等级名称',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'tjmc',
				title: '单位统计名称',
				width: '10%',
				align: 'left',
				visible: false
			}, {
				field: 'dwlbmc',
				title: '单位类别名称',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'sfmc',
				title: '省份',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'csmc',
				title: '城市',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'cskz1',
				title: '是否显示输入框',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'cskz2',
				title: '医院重点等级',
				width: '8%',
				align: 'left',
				visible: false
			},{
				field: 'cskz3',
				title: 'LIS系统参数',
				width: '8%',
				align: 'left',
				visible: false
			},{
				field: 'jcxmpp',
				title: '检测项目匹配',
				width: '8%',
				align: 'left',
				visible: false
			},{
				field: 'shxydm',
				title: '社会信用代码',
				width: '8%',
				align: 'left',
				visible: true
			},{
				field: 'lxr',
				title: '联系人',
				width: '8%',
				align: 'left',
				visible: true
			},{
				field: 'lxdh',
				title: '联系电话',
				width: '8%',
				align: 'left',
				visible: true
			},{
				field: 'virtualhost',
				title: 'virtualhost',
				width: '8%',
				align: 'left',
				visible: false
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				yyxxById(row.dwid,'view',$("#yyxx_formSearch #btn_view").attr("tourl"));
			},
		});
	};
	// 得到查询的参数
	oTableInit.queryParams = function(params){
	// 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
	// 例如 toolbar 中的参数
	// 如果 queryParamsType = ‘limit’ ,返回参数必须包含
	// limit, offset, search, sort, order
	// 否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
	// 返回false将会终止请求
    var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
    	pageSize: params.limit,   // 页面大小
    	pageNumber: (params.offset / params.limit) + 1,  // 页码
        access_token:$("#ac_tk").val(),
        sortName: params.sort,      // 排序列名
        sortOrder: params.order, // 排位命令（desc，asc）
        sortLastName: "dwlbmc", // 防止同名排位用
        sortLastOrder: "desc" // 防止同名排位用
        // 搜索框使用
        // search:params.search
    };
    return getYyxxSearchData(map);
	};
	return oTableInit;
};

function getYyxxSearchData(map){
	var cxtj=$("#yyxx_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#yyxx_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["dwmc"]=cxnr
	}else if(cxtj=="1"){
		map["dwjc"]=cxnr
	}else if(cxtj=="2"){
		map["dwqtmc"]=cxnr
	}else if(cxtj=="3"){
		map["tjmc"]=cxnr
	}else if(cxtj=="4"){
		map["dwdjmc"]=cxnr
	}else if(cxtj=="5"){
		map["dwlbmc"]=cxnr
	}else if(cxtj=="6"){
		map["sfmc"]=cxnr
	}else if(cxtj=="7"){
		map["csmc"]=cxnr
	}else if (cxtj == "8") {
    	map["entire"] = cxnr;
	}
	
	//单位类别  基础数据
	var dwlb = jQuery('#yyxx_formSearch #dwlb_id_tj').val();
	map["dwlbs"] = dwlb;

	
	//单位等级 基础数据
	var dwdj = jQuery('#yyxx_formSearch #dwdj_id_tj').val();
	map["dwdjs"] = dwdj;
	//医院重点等级
	var dwzddj = jQuery('#yyxx_formSearch #dwzddj_id_tj').val().replace("＋","+");
	map["cskz2s"] = dwzddj;
	
	return map;
}
//提供给导出用的回调函数
function YyxxSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="yyxx.sf";
	map["sortLastOrder"]="asc";
	map["sortName"]="yyxx.cs";
	map["sortOrder"]="asc";
	return getYyxxSearchData(map);
}


function yyxxResult(isTurnBack){
	if(isTurnBack){
		$('#yyxx_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#yyxx_formSearch #tb_list').bootstrapTable('refresh');
	}
}



function yyxxById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?dwid=" +id;
		$.showDialog(url,'医院详细信息',viewyyxxConfig);
	}else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'增加医院信息',addYyxxConfig);
	}else if(action =='mod'){
		var url=tourl + "?dwid=" +id;
		$.showDialog(url,'修改医院信息',modYyxxConfig);
	}
}


	var yyxx_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    	oInit.Init = function () {
		var btn_view = $("#yyxx_formSearch #btn_view");
		var btn_query = $("#yyxx_formSearch #btn_query");
		var btn_searchexport = $("#yyxx_formSearch #btn_searchexport");
    	var btn_selectexport = $("#yyxx_formSearch #btn_selectexport");
		var btn_add = $("#yyxx_formSearch #btn_add");
		var btn_mod = $("#yyxx_formSearch #btn_mod");
		var btn_del = $("#yyxx_formSearch #btn_del");
		
    	
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			yyxxResult(true);
    		});
    	}
  
        //---------------------------查看列表-----------------------------------
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#yyxx_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			yyxxById(sel_row[0].dwid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
    	
    	/*---------------------------新增医院信息-----------------------------------*/
    	btn_add.unbind("click").click(function(){
    		yyxxById(null,"add",btn_add.attr("tourl"));
    	});
    	/*---------------------------编辑医院信息-----------------------------------*/
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#yyxx_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			yyxxById(sel_row[0].dwid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    
   // 	  ---------------------------导出-----------------------------------
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#yyxx_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].dwid;
        		}
        		ids = ids.substr(1);
        		$.showDialog(exportPrepareUrl+"?ywid=YYXX_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});

    	btn_searchexport.unbind("click").click(function(){
    		$.showDialog(exportPrepareUrl+"?ywid=YYXX_SEARCH&expType=search&callbackJs=YyxxSearchData"
    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});
    	 // 	  ---------------------------删除----------------------------------
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#yyxx_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].dwid;
    		}
    		ids = ids.substr(1);
    		$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= btn_del.attr("tourl");
    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								searchYyxxResult();
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
    	
    	
       	/**显示隐藏**/      
    	$("#yyxx_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(yyxx_turnOff){
    			$("#yyxx_formSearch #searchMore").slideDown("low");
    			yyxx_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#yyxx_formSearch #searchMore").slideUp("low");
    			yyxx_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
    	
    };
    	return oInit;
    };
    

    function searchYyxxResult(isTurnBack){
    	//关闭高级搜索条件
    	$("#yyxx_formSearch #searchMore").slideUp("low");
    	yyxx_turnOff=true;
    	$("#yyxx_formSearch #sl_searchMore").html("高级筛选");
    	if(isTurnBack){
    		$('#yyxx_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
    	}else{
    		$('#yyxx_formSearch #tb_list').bootstrapTable('refresh');
    	}
    }


    
var sfbc=0;//是否继续保存 
var viewyyxxConfig = {
		width		: "800px",
    		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    		buttons		: {
    			cancel : {
    				label : "关 闭",
    				className : "btn-default"
    			}
    		} 
    	};

var addYyxxConfig = {
		width		: "1200px",
		modalName	: "addYyxxModal",
		formName	: "ajaxForm",
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
					/*验证手机号码*/
					var phoneNum = $("#ajaxForm #lxdh").val();
					if (phoneNum != null && phoneNum != '') {
						var reg = /^(?:(?:0\d{2,3}-\d{7,8})|\d{7,8}|1\d{10})$/;
						if (!reg.test(phoneNum)) {
							$.error('请输入正确的手机号码!');
							return false;
						}
					}

					var $this = this;
					var opts = $this["options"]||{};
					
					$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
					
					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									yyxxResult ();
								}
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

var modYyxxConfig = {
		width		: "1200px",
		modalName	: "modYyxxModal",
		formName	: "ajaxForm",
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

					/*验证手机号码*/
					var phoneNum = $("#ajaxForm #lxdh").val();
					if (phoneNum != null && phoneNum != '') {
						var reg = /^(?:(?:0\d{2,3}-\d{7,8})|\d{7,8}|1\d{10})$/;
						if (!reg.test(phoneNum)) {
							$.error('请输入正确的手机号码!');
							return false;
						}
					}
					
					var $this = this;
					var opts = $this["options"]||{};
					
					$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
					
					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									yyxxResult();
								}
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
    	

$(function(){

	//0.界面初始化
	// 1.初始化Table
	var oTable = new yyxx_TableInit();
	oTable.Init();
	
    //2.初始化Button的点击事件
    var oButtonInit = new yyxx_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#yyxx_formSearch .chosen-select').chosen({width: '100%'});
	
	
	$("#yyxx_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	
});