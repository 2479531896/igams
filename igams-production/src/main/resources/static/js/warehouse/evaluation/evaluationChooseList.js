var evaChoose_TableInit = function () {
	 var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#evaChooseFormSearch #evaChoose_list').bootstrapTable({
			url: $("#evaChooseFormSearch #urlPrefix").val()+'/evaluation/evaluation/pagedataQueryEvaluation',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#evaChooseFormSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"gfyz.lrsj,gfyz.yzshsj",					// 排序字段
			sortOrder: "desc",                   // 排序方式
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
			uniqueId: "gfyzid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true,
				width: '4%'
			},{
            	title: '序号',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '4%',
                align: 'center',
                visible:true
            },{
				field: 'gfmc',
				titleTooltip:'供应商名称',
				title: '供应商名称',
				width: '15%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'gfbh',
				titleTooltip:'供方编号',
				title: '供方编号',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
               field: 'sqrmc',
               titleTooltip:'申请人',
               title: '申请人',
               width: '10%',
               align: 'left',
               sortable: true,
               visible: true
            },{
              field: 'yzsqsj',
              titleTooltip:'申请时间',
              title: '申请时间',
              width: '10%',
              align: 'left',
              sortable: true,
              visible: true
          },{
				field: 'dz',
				titleTooltip:'地址',
				title: '地址',
				width: '15%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'lxr',
				titleTooltip:'联系人',
				title: '联系人',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: false
			}, {
				field: 'cz',
				titleTooltip:'传真',
				title: '传真',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'dh',
				titleTooltip:'电话',
				title: '电话',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'fzrq',
				titleTooltip:'发展日期',
				title: '发展日期',
				width: '8%',
				align: 'left',
				visible: false
			},{
				field: 'yzshsj',
				titleTooltip:'验证审核时间',
				title: '验证审核时间',
				width: '8%',
				align: 'center',
				sortable: true,
				visible: true
			}, {
                field: 'cbpj',
                titleTooltip:'初步评价',
                title: '初步评价',
                width: '20%',
                align: 'left',
                visible: true
            }, {
				field: 'yzshpj',
				titleTooltip:'验证审核评价',
				title: '验证审核评价',
				width: '20%',
				align: 'left',
				visible: true
			}, {
                field: 'jl',
                titleTooltip:'结论',
                title: '结论',
                width: '20%',
                align: 'left',
                visible: true
            }, {
                 field: 'cz',
                 titleTooltip:'操作',
                 title: '操作',
                 width: '6%',
                 align: 'left',
                 formatter:edit_czformat,
                 visible: true
             }],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {

			},
		});
		$("#evaChooseFormSearch #evaChoose_list").colResizable({
			liveDrag:true,
			gripInnerHtml:"<div class='grip'></div>",
			draggingClass:"dragging",
			resizeMode:'fit',
			postbackSafe:false,
			partialRefresh:true
		})
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
        sortLastName: "gfyz.lrsj", // 防止同名排位用
        sortLastOrder: "desc", // 防止同名排位用
        yzzt:"80",
        chooseFlg:"0",
        gfpjid: $('#appraiseEditForm #gfpjid').val()

        // 搜索框使用
        // search:params.search
    };
    return evaChooseSearchData(map);
	};
	return oTableInit;
};

function edit_czformat(value,row,index){
    var gfyzid = row.gfyzid;
    var html="";
    html = "<span style='margin-left:5px;' class='btn btn-success' title='查看' onclick=\"viewButton('" + gfyzid + "')\" >查看</span>";
    return html;
}
function viewButton(gfyzid) {
    var url=$('#evaChooseFormSearch #urlPrefix').val() + "/evaluation/evaluation/viewEvaluation?access_token=" + $("#ac_tk").val()+"&gfyzid="+gfyzid;
    $.showDialog(url,'查看',viewButtonConfig);
}
var viewButtonConfig = {
	width		: "1600px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function evaChooseSearchData(map){
	var cxtj=$("#evaChooseFormSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#evaChooseFormSearch #cxnr').val());
	if(cxtj=="0"){
		map["gfmc"]=cxnr;
	}else if(cxtj=="1"){
		map["lxr"]=cxnr;
	}else if(cxtj=="2"){
		map["cz"]=cxnr;
	}else if(cxtj=="3"){
		map["dh"]=cxnr;
	}else if(cxtj=="4"){
		map["gfbh"]=cxnr;
	}else if(cxtj=="5"){
        map["entire"]=cxnr;
    }else if(cxtj=="6"){
        map["sqrmc"]=cxnr;
    }else if(cxtj=="7"){
        map["wlmc"]=cxnr;
    }else if(cxtj=="8"){
        map["wlbm"]=cxnr;
    }else if(cxtj=="9"){
        map["xmh"]=cxnr;
    }

	// 验证申请开始日期
	var yzsqsjstart = jQuery('#evaChooseFormSearch #yzsqsjstart').val();
	map["yzsqsjstart"] = yzsqsjstart;

	// 验证申请结束日期
	var yzsqsjend = jQuery('#evaChooseFormSearch #yzsqsjend').val();
	map["yzsqsjend"] = yzsqsjend;

	// 验证审核开始日期
	var yzshsjstart = jQuery('#evaChooseFormSearch #yzshsjstart').val();
	map["yzshsjstart"] = yzshsjstart;

	// 验证审核结束日期
	var yzshsjend = jQuery('#evaChooseFormSearch #yzshsjend').val();
	map["yzshsjend"] = yzshsjend;

	return map;
}



var evaChoose_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_query = $("#evaChooseFormSearch #btn_query");
		//添加日期控件
    	laydate.render({
    	   elem: '#yzshsjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#yzshsjend'
    	  ,theme: '#2381E9'
    	});

    	laydate.render({
    	   elem: '#yzsqsjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#yzsqsjend'
    	  ,theme: '#2381E9'
    	});
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			evaChooseResult(true);
    		});
    	}
    };
    	return oInit;
};

function evaChooseResult(isTurnBack){
	if(isTurnBack){
		$('#evaChooseFormSearch #evaChoose_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#evaChooseFormSearch #evaChoose_list').bootstrapTable('refresh');
	}
}


$(function(){
	// 1.初始化Table
	var oTable = new evaChoose_TableInit();
	oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new evaChoose_ButtonInit();
    oButtonInit.Init();

	// 所有下拉框添加choose样式
	jQuery('#evaChooseFormSearch .chosen-select').chosen({width: '100%'});


});