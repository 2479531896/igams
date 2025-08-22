var datauser_TableInit = function () {
	var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#datauser_formSearch #tb_list').bootstrapTable({
			url: '/dmp/data/listDataUser',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#datauser_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			sortable: true,                     // 是否启用排序
			sortName:"jk.rzid",					// 排序字段
			sortOrder: "asc",                   // 排序方式
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
			uniqueId: "rzid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true,
				width: '4%'
			}, {
				field: 'no',
				title: '序号',
				titleTooltip:'序号',
				width: '4%',
				align: 'left',
				visible: true,
				formatter:xhFormat
			}, {
				field: 'rzid',
				title: '认证ID',
				width: '10%',
				align: 'left',
				visible: false
			}, {
				field: 'rzyhm',
				title: '认证用户名',
				titleTooltip:'认证用户名',
				width: '18%',
				align: 'left',
				visible: true
			}, {
				field: 'rzyhdm',
				title: '认证用户代码',
				titleTooltip:'认证用户代码',
				width: '12%',
				align: 'left',
				visible: true
			}, {
				field: 'rzkey',
				title: '认证键',
				titleTooltip:'认证键',
				width: '12%',
				align: 'left',
				visible: true
			}, {
				field: 'bz',
				title: '备注',
				titleTooltip:'备注',
				width: '20%',
				align: 'left',
				visible: true
			}, {
				title: '操作',
				titleTooltip:'操作',
				width: '20%',
				align: 'center',
				visible: true,
				formatter:dealFormatter
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
			},
			onDblClickRow: function (row, $element) {
				datauserDealById(row.rzid, 'view',$("#datauser_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#datauser_formSearch #tb_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true}        
        );
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
			sortLastName: "jk.rzid", // 防止同名排位用
			sortLastOrder: "asc" // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};

		var cxtj = $("#datauser_formSearch #cxtj").val();
		// 查询条件
		var cxnr = $.trim(jQuery('#datauser_formSearch #cxnr').val());
		// '0':'用户名称',
		if (cxtj == "0") {
			map["rzyhm"] = cxnr;
		}
		
		return map;
	};
	return oTableInit;
};
//序号格式化
function xhFormat(value, row, index) {
	//获取每页显示的数量
	var pageSize=$('#datauser_formSearch #tb_list').bootstrapTable('getOptions').pageSize;
	//获取当前是第几页
	var pageNumber=$('#datauser_formSearch #tb_list').bootstrapTable('getOptions').pageNumber;
	//返回序号，注意index是从0开始的，所以要加上1
	return pageSize * (pageNumber - 1) + index + 1;
}
//修改权限按钮样式
function dealFormatter(value, row, index) {
	var id = row.rzid;
    var result = "<div class='btn-group'>";
    result += "<button class='btn btn-default' type='button' onclick=\"datauserDealById('" + id + "', 'modCompetence','/dmp/user/addDataUserCompetence')\" title='权限'><span class='glyphicon glyphicon-plus'>权限</span></button>";
    result += "</div>";
    
    return result;
}

var datauser_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
		var btn_add = $("#datauser_formSearch #btn_add");
		var btn_view = $("#datauser_formSearch #btn_view");
		var btn_mod = $("#datauser_formSearch #btn_mod");
		var btn_del = $("#datauser_formSearch #btn_del");
    	var btn_query = $("#datauser_formSearch #btn_query");

    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchDataUserResult();
    		});
    	}
    	btn_add.unbind("click").click(function(){
    		datauserDealById(null,"add",btn_add.attr("tourl"));
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#datauser_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			datauserDealById(sel_row[0].rzid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#datauser_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			datauserDealById(sel_row[0].rzid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#datauser_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].rzid;
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
    								searchDataUserResult();
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
    };

    return oInit;
};

//按钮动作函数
function datauserDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?rzid=" +id;
		$.showDialog(url,'查看用户信息',viewDataUserConfig);
	}else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'用户注册',addDataUserConfig);
	}else if(action =='mod'){
		var url= tourl + "?rzid=" +id;
		$.showDialog(url,'用户修改',modDataUserConfig);
	}else if(action =='modCompetence'){
		var url= tourl + "?rzid=" +id+ "&formAction=saveCompetence";
		$.showDialog(url,'用户权限修改',modCompetenceDataUserConfig);
	}
}

var viewDataUserConfig = {
	width		: "600px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var addDataUserConfig = {
	width		: "700px",
	modalName	: "addDataUserModal",
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
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchDataUserResult();
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

var modDataUserConfig = {
	width		: "500px",
	modalName	: "modDataUserModal",
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
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchDataUserResult();
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

var modCompetenceDataUserConfig = {
	width		: "600px",
	modalName : "modCompetenceDataUserModal",
	buttons : {
		success : {
			label : "确定",
			className : "btn-primary",
			callback : function	() {
				//调用指定函数方法
				var formAction = $("#competencetreeDiv #formAction").val();
				if(formAction){
					eval(formAction+"()"); 
				}
				jQuery.closeModal("modCompetenceDataUserModal");
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
 * 数据用户权限保存方法
 */
function saveCompetence(){
	var competenceArray = $('#competencetreeDiv #competenceTree').jstree().get_bottom_selected();
	var url = '/dmp/user/dataUserCompetenceSaveTree';
	var map = {};
	map["competenceArray"] = JSON.stringify(competenceArray);
	map["rzid"] = jQuery("#competencetreeDiv #rzid").val();
	map["access_token"] = $("#ac_tk").val();
	jQuery.ajaxSetup( {
		async : false
	});
	jQuery.post(url, map, function(data) {
		$.success(data.message);
	}, 'json');
	jQuery.ajaxSetup( {
		async : true
	});
}

function searchDataUserResult(){
	$('#datauser_formSearch #tb_list').bootstrapTable('refresh');
}

$(function(){
	// 1.初始化Table
	var oTable = new datauser_TableInit();
	oTable.Init();
	
    //2.初始化Button的点击事件
    var oButtonInit = new datauser_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#datauser_formSearch .chosen-select').chosen({width: '100%'});
});