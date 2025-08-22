var jkdymx_turnOff = true;
var jkdymx_TableInit = function() {
	var oTableInit = new Object();

	// 初始化Table
	oTableInit.Init = function() {
		$('#jkdymx_formSearch #jkdymx_list').bootstrapTable({
			url: '/wechat/jkdymx/pagedataJkdymx', // 请求后台的URL（*）
			method: 'get', // 请求方式（*）
			toolbar: '#jkdymx_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true, // 是否显示行间隔色
			cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一s下这个属性（*）
			pagination: true, // 是否显示分页（*）
			paginationShowPageGo: true, //增加跳转页码的显示
			sortable: true, // 是否启用排序
			sortName: "dysj", // 排序字段
			sortOrder: "desc", // 排序方式
			queryParams: oTableInit.queryParams, // 传递参数（*）
			sidePagination: "server", // 分页方式：client客户端分页，server服务端分页（*）
			pageNumber: 1, // 初始化加载第一页，默认第一页
			pageSize: 15, // 每页的记录行数（*）
			pageList: [15, 30, 50, 100], // 可供选择的每页的行数（*）
			paginationPreText: '‹', // 指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›', // 指定分页条中下一页按钮的图标或文字,这里是>
			search: false, // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			showColumns: true, // 是否显示所有的列
			showRefresh: true, // 是否显示刷新按钮
			minimumCountColumns: 2, // 最少允许的列数
			clickToSelect: true, // 是否启用点击选中行
			// height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "dyid", // 每一行的唯一标识，一般为主键列
			showToggle: true, // 是否显示详细视图和列表视图的切换按钮
			cardView: false, // 是否显示详细视图
			detailView: false, // 是否显示父子表
			columns: [{
				checkbox: true,
				width: '5%'
			}, {
				'field': '',
				'title': '序号',
				'align': 'center',
				'width': '5%',
				'sortable': 'true',
				'formatter': function(value, row, index) {
					var options = $('#jkdymx_formSearch #jkdymx_list').bootstrapTable(
						'getOptions');
					return options.pageSize * (options.pageNumber - 1) + index + 1;
				}
			}, {
				field: 'dydz',
				title: '调用地址',
				width: '10%',
				align: 'left',
				visible: true,
				sortable: true
			}, {
				field: 'lxqf',
				title: '类型区分',
				width: '5%',
				align: 'left',
				visible: true,
				sortable: true
			}, {
				field: 'dysj',
				title: '调用时间',
				width: '10%',
				align: 'left',
				visible: true,
				sortable: true
			}, {
				field: 'nr',
				title: '调用内容',
				width: '10%',
				align: 'left',
				visible: true,
				sortable: true,
			}, {
				field: 'fhsj',
				title: '返回时间',
				width: '10%',
				align: 'left',
				visible: true,
				sortable: true
			}, {
				field: 'fhnr',
				title: '返回内容',
				width: '10%',
				align: 'left',
				visible: true,
				sortable: true,
			}, {
				field: 'qtxx',
				title: '其他信息',
				width: '10%',
				align: 'left',
				visible: true,
				sortable: true
			}, {
				field: 'sfcg',
				title: '是否成功',
				width: '5%',
				align: 'left',
				visible: true,
				sortable: true
			}, {
				field: 'ywid',
				title: '业务id',
				width: '10%',
				align: 'left',
				visible: true,
				sortable: true
			}, {
				field: 'dyfl',
				title: '调用分类',
				width: '10%',
				align: 'left',
				visible: false,
				sortable: true
			}, {
				field: 'dyzfl',
				title: '调用子分类',
				width: '10%',
				align: 'left',
				visible: false,
				sortable: true
			}, ],
			onLoadSuccess: function() {},
			onLoadError: function() {
				alert("数据加载失败！");
			},
			onDblClickRow: function(row, $element) {
				jkdymxByCode(row.dyid, 'view', $("#jkdymx_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#jkdymx_formSearch #jkdymx_list").colResizable({
			liveDrag: true,
			gripInnerHtml: "<div class='grip'></div>",
			draggingClass: "dragging",
			resizeMode: 'fit',
			postbackSafe: true,
			partialRefresh: true
		});
	};
	// 得到查询的参数
	oTableInit.queryParams = function(params) {
		// 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
		// 例如 toolbar 中的参数
		// 如果 queryParamsType = ‘limit’ ,返回参数必须包含
		// limit, offset, search, sort, order
		// 否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
		// 返回false将会终止请求
		var map = { // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			pageSize: params.limit, // 页面大小
			pageNumber: (params.offset / params.limit) + 1, // 页码
			access_token: $("#ac_tk").val(),
			sortName: params.sort, // 排序列名
			sortOrder: params.order, // 排位命令（desc，asc）
			sortLastName: "dyid", // 防止同名排位用
			sortLastOrder: "desc" // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return getJkdymxSearchData(map);
	};
	return oTableInit;
};


function getJkdymxSearchData(map) {
	var cxtj = $("#jkdymx_formSearch #cxtj").val();
	var cxnr = $.trim(jQuery('#jkdymx_formSearch #cxnr').val());
	if (cxtj == "0") {
		map["entire"] = cxnr
	} else if (cxtj == '1') {
		map["ywid"] = cxnr
	} else if (cxtj == '2') {
		map["dydz"] = cxnr
	} else if (cxtj == '3') {
		map["nr"] = cxnr
	}
	return map;
}

function jkdymxResult(isTurnBack) {
	if (isTurnBack) {
		$('#jkdymx_formSearch #jkdymx_list').bootstrapTable('refresh', {
			pageNumber: 1
		});
	} else {
		$('#jkdymx_formSearch #jkdymx_list').bootstrapTable('refresh');
	}
}

function jkdymxByCode(dyid, action, tourl) {
	if (!tourl) {
		return;
	}
	if (action == 'view') {
		var url = tourl + "?dyid=" + dyid;
		$.showDialog(url, '详细信息', viewJkdymxConfig);
	}
}


var jkdymx_ButtonInit = function() {
	var oInit = new Object();
	var postdata = {};

	oInit.Init = function() {
		var btn_view = $("#jkdymx_formSearch #btn_view");
		var btn_query = $("#jkdymx_formSearch #btn_query");

		/*-----------------------模糊查询------------------------------------*/
		if (btn_query != null) {
			btn_query.unbind("click").click(function() {
				searchJkdymxResult(true);
			});
		}
		//---------------------------查看列表-----------------------------------
		btn_view.unbind("click").click(function() {
			var sel_row = $('#jkdymx_formSearch #jkdymx_list').bootstrapTable(
			'getSelections'); //获取选择行数据
			if (sel_row.length == 1) {
				jkdymxByCode(sel_row[0].dyid, "view", btn_view.attr("tourl"));
			} else {
				$.error("请选中一行");
			}
		});

		/**显示隐藏**/
		$("#jkdymx_formSearch #sl_searchMore").on("click", function(ev) {
			var ev = ev || event;
			if (jkdymx_turnOff) {
				$("#jkdymx_formSearch #searchMore").slideDown("low");
				jkdymx_turnOff = false;
				this.innerHTML = "基本筛选";
			} else {
				$("#jkdymx_formSearch #searchMore").slideUp("low");
				jkdymx_turnOff = true;
				this.innerHTML = "高级筛选";
			}
			ev.cancelBubble = true;
		});

	};
	return oInit;
};


function searchJkdymxResult(isTurnBack) {
	//关闭高级搜索条件
	$("#jkdymx_formSearch #searchMore").slideUp("low");
	jkdymx_turnOff = true;
	$("#jkdymx_formSearch #sl_searchMore").html("高级筛选");
	if (isTurnBack) {
		$('#jkdymx_formSearch #jkdymx_list').bootstrapTable('refresh', {
			pageNumber: 1
		});
	} else {
		$('#jkdymx_formSearch #jkdymx_list').bootstrapTable('refresh');
	}
}

var viewJkdymxConfig = {
	width: "800px",
	offAtOnce: true, //当数据提交成功，立刻关闭窗口
	buttons: {
		cancel: {
			label: "关 闭",
			className: "btn-default"
		}
	}
};

$(function() {

	//0.界面初始化
	// 1.初始化Table
	var oTable = new jkdymx_TableInit();
	oTable.Init();
	//2.初始化Button的点击事件
	var oButtonInit = new jkdymx_ButtonInit();
	oButtonInit.Init();

	// 所有下拉框添加choose样式
	jQuery('#jkdymx_formSearch .chosen-select').chosen({
		width: '100%'
	});


	$("#jkdymx_formSearch [name='more']").each(function() {
		$(this).on("click", s_showMoreFn);
	});

});