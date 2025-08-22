var dataplat_TableInit = function () {
	var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#dataplat_formSearch #tb_list').bootstrapTable({
			url: '/dmp/data/listDataPlat',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#dataplat_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			sortable: true,                     // 是否启用排序
			sortName:"zyxx.zyid",					// 排序字段
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
			uniqueId: "zyid",                     // 每一行的唯一标识，一般为主键列
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
				field: 'zyid',
				title: '资源ID',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'tgfid',
				title: '提供方ID',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'zymc',
				title: '资源名称',
				titleTooltip:'资源名称',
				width: '22%',
				align: 'left',
				visible: true
			}, {
				field: 'djfs',
				title: '对接方式',
				titleTooltip:'对接方式',
				width: '12%',
				align: 'left',
				visible: true
			}, {
				field: 'zydm',
				title: '资源代码',
				titleTooltip:'资源代码',
				width: '12%',
				align: 'left',
				visible: true
			}, {
				field: 'tgfmc',
				title: '提供方',
				titleTooltip:'提供方',
				width: '8%',
				align: 'left',
				visible: true
			}, {
				field: 'tgfdm',
				title: '提供代码',
				titleTooltip:'提供代码',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: '',
				title: ' ',
				titleTooltip:' ',
				width: '8%',
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
				dataplatDealById(row.zyid, 'view',$("#dataplat_formSearch #btn_view").attr("tourl"));
			},
		});
        $("#dataplat_formSearch #tb_list").colResizable({
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
			sortLastName: "zyxx.zyid", // 防止同名排位用
			sortLastOrder: "asc" // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};

		var cxtj = $("#dataplat_formSearch #cxtj").val();
		// 查询条件
		var cxnr = $.trim(jQuery('#dataplat_formSearch #cxnr').val());
		// '0':'资源名称','1':'资源代码',
		if (cxtj == "0") {
			map["zymc"] = cxnr;
		}else if (cxtj == "1") {
			map["zydm"] = cxnr;
		}
		return map;
	};
	return oTableInit;
};
//序号格式化
function xhFormat(value, row, index) {
	//获取每页显示的数量
	var pageSize=$('#dataplat_formSearch #tb_list').bootstrapTable('getOptions').pageSize;
	//获取当前是第几页
	var pageNumber=$('#dataplat_formSearch #tb_list').bootstrapTable('getOptions').pageNumber;
	//返回序号，注意index是从0开始的，所以要加上1
	return pageSize * (pageNumber - 1) + index + 1;
}

//操作格式化
function dealFormatter(value, row, index) {
	
    return;
}

var dataplat_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
		var btn_add = $("#dataplat_formSearch #btn_add");
		var btn_view = $("#dataplat_formSearch #btn_view");
    	var btn_query = $("#dataplat_formSearch #btn_query");

    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchDataPlatResult();
    		});
    	}
    	btn_add.unbind("click").click(function(){
    		dataplatDealById(null,"add",btn_add.attr("tourl"));
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#dataplat_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			dataplatDealById(sel_row[0].zyid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});

    };

    return oInit;
};

//按钮动作函数
function dataplatDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?zyid=" +id;
		$.showDialog(url,'查看数据信息',viewDataPlatConfig);
	}else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'新增数据信息',addDataPlatConfig);
	}
}

var viewDataPlatConfig = {
	width		: "1000px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var addDataPlatConfig = {
	width		: "900px",
	modalName	: "addDataPlatModal",
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
								searchDataPlatResult();
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

function searchDataPlatResult(){
	$('#dataplat_formSearch #tb_list').bootstrapTable('refresh');
}

$(function(){
	// 1.初始化Table
	var oTable = new dataplat_TableInit();
	oTable.Init();
	
    //2.初始化Button的点击事件
    var oButtonInit = new dataplat_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#dataplat_formSearch .chosen-select').chosen({width: '100%'});
});