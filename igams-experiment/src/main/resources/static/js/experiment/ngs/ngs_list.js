var ngs_turnOff=true;
var ngs_TableInit = function () {
	 var oTableInit = new Object();
	    $('#tab_020309').on('scroll',function(){
	    	if($('#mater_formSearch #tb_list').offset().top - 122 < 0){
	    		if(!$('#mater_formSearch .js-affix').hasClass("affix"))
	    		{
	    			$('#mater_formSearch .js-affix').removeClass("affix-top").addClass("affix");
	    		}
	    		$('#mater_formSearch .js-affix').css({
			    	'top': '100px',
			        "z-index":1000,
			        "width":'100%'
			      });
	    	}else{
	    		if(!$('#mater_formSearch .js-affix').hasClass("affix-top"))
	    		{
	    			$('#mater_formSearch .js-affix').removeClass("affix").addClass("affix-top");
	    		}
	    		$('#mater_formSearch .js-affix').css({
			    	'top': '0px',
			        "z-index":1,
			        "width":'100%'
			      });
	    	}
	    })
	// 初始化Table
	oTableInit.Init = function () {
		$('#ngs_formSearch #tbs_list').bootstrapTable({
			url: '/experiment/ngs/pageGetListNgs',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#ngs_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"kssj",					// 排序字段
			sortOrder: "desc",                   // 排序方式
			queryParams: oTableInit.queryParams,// 传递参数（*）
			sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
			pageNumber:1,                       // 初始化加载第一页，默认第一页
			pageSize: 15,                       // 每页的记录行数（*）
			pageList: [15, 30, 50, 100],       // 可供选择的每页的行数（*）
			paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
			search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			showColumns: true,                  // 是否显示所有的列
			showRefresh: true,                  // 是否显示刷新按钮
			minimumCountColumns: 2,             // 最少允许的列数
			clickToSelect: true,                // 是否启用点击选中行
			// height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "ngsid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮                 								
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true
			}, {
				'field': '',
			    'title': '序号',
			    'align': 'center',
			    'width': '6%',
			    'formatter': function (value, row, index) {
			    　　var options = $('#ngs_formSearch #tbs_list').bootstrapTable('getOptions'); 
			    　　return options.pageSize * (options.pageNumber - 1) + index + 1; 
			    }
			}, {				
				field: 'ngsid',
				title: 'ngsId',
				width: '10%',
				align: 'left',
				visible: false
			}, {
				field: 'jcdw',
				title: '检测单位',
				width: '10%',
				align: 'left',
				visible: false
			}, {
				field: 'jcdwmc',
				title: '检测单位名称',
				width: '12%',
				align: 'left',
				visible: true
			}, {
				field: 'jcid',
				title: '检测ID',
				width: '20%',
				align: 'left',
				visible: false
			}, {
				field: 'sylx',
				title: '实验类型',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'zbf',
				title: '制备法',
				width: '25%',
				align: 'left',
				visible: true
			}, {
				field: 'zbbm',
				title: '制备编码',
				width: '10%',
				align: 'left',
				visible: false
			}, {
				field: 'nbbm',
				title: '内部编码',
				width: '15%',
				align: 'left',
				visible: true
			}, {
				field: 'tcsjph',
				title: '卡盒试剂批号',
				width: '15%',
				align: 'left',
				visible: true
			}, {
				field: 'jksjph',
				title: '建库试剂批号',
				width: '15%',
				align: 'left',
				visible: true
			}, {
				field: 'jth',
				title: '接头号',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'kssj',
				title: '开始时间',
				width: '20%',
				align: 'left',
				visible: true
			}, {
				field: 'jssj',
				title: '结束时间',
				width: '20%',
				align: 'left',
				visible: true
			}, {
				field: 'bz',
				title: '备注',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'sbid',
				title: '设备ID',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'czqx',
				title: '操作权限',
				width: '10%',
				align: 'left',
				visible: false
			}, {
				field: 'cjsj',
				title: '创建时间',
				width: '10%',
				align: 'left',
				visible: false
			}, {
				field: 'sjh',
				title: '试剂号',
				width: '10%',
				align: 'left',
				visible: false
			}, {
				field: 'sfcg',
				title: '是否成功',
				width: '10%',
				align: 'left',
				formatter:sfcgformat,
				visible: true
			}, {
				field: 'wgdz',
				title: '网关地址',
				width: '40%',
				align: 'left',
				visible: false
			}, {
				field: 'rz',
				title: '日志',
				width: '40%',
				align: 'left',
				visible: false
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				NgsById(row.ngsid,'view',$("#ngs_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#ngs_formSearch #tbs_list").colResizable(
    		{
                liveDrag:true, 
                gripInnerHtml:"<div class='grip'></div>", 
                draggingClass:"dragging", 
                resizeMode:'fit', 
                postbackSafe:true,
                partialRefresh:true,
                pid:"document_formSearch"
    		}
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
        sortLastName: "ngsid", // 防止同名排位用
        sortLastOrder: "desc" // 防止同名排位用
        // 搜索框使用
        // search:params.search
    };
    return getNgsSearchData(map);
	};
	return oTableInit;
};

//状态格式化
function sfcgformat(value,row,index){
	if(row.sfcg==3){
		var sfcg="<span style='color:green;'>"+"成功"+"</span>";
	}else if(row.sfcg==2){
		var sfcg="<span style='color:red;'>"+"失败"+"</span>";
	}else if(row.sfcg==0){
		var sfcg="<span style='color:gray;'>"+"未分配"+"</span>";
	}else if(row.sfcg==1){
		var sfcg="<span style='color:orange;'>"+"就绪"+"</span>";
	}else if(row.sfcg==4){
		var sfcg="<span style='color:blue;'>"+"进行中"+"</span>";
	}
	return sfcg;
}

function getNgsSearchData(map){
	var cxtj=$("#ngs_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#ngs_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["jcdwmc"]=cxnr
	}else if(cxtj=="1"){
		map["nbbm"]=cxnr
	}else if(cxtj=="2"){
		map["zbf"]=cxnr
	}else if (cxtj == "3") {
    	map["entire"] = cxnr;
	}else if (cxtj == "5") {
    	map["tcsjph"] = cxnr;
	}else if (cxtj == "6") {
    	map["jksjph"] = cxnr;
	}else if (cxtj == "7") {
    	map["jth"] = cxnr;
	}else if (cxtj == "8") {
		map["sbid"] = cxnr;
	}
	// 检查单位名称
	var jcdw = jQuery('#ngs_formSearch #jcdwmc_id_tj').val();
	map["jcdws"] = jcdw;

	
	// 检查是否成功
	var sfcg = jQuery('#ngs_formSearch #sfcg_id_tj').val();
	map["sfcgs"] = sfcg;
	// 生效开始日期
	var sxrqstart = jQuery('#ngs_formSearch #sxrqstart').val();
	map["sxrqstart"] = sxrqstart;
	
	// 生效结束日期
	var sxrqend = jQuery('#ngs_formSearch #sxrqend').val();
	map["sxrqend"] = sxrqend;
	
	return map;
}
//提供给导出用的回调函数
function NgsSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="ngs.ngsid";
	map["sortLastOrder"]="asc";
	map["sortName"]="ngs.kssj";
	map["sortOrder"]="asc";
	return getNgsSearchData(map);
}


function NgsResult(isTurnBack){
	if(isTurnBack){
		$('#ngs_formSearch #tbs_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#ngs_formSearch #tbs_list').bootstrapTable('refresh');
	}
}



function NgsById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?ngsid=" +id;
		$.showDialog(url,'NGS详细信息',viewNgsConfig);
	}
}


	var ngs_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#ngs_formSearch #btn_view");
		var btn_query = $("#ngs_formSearch #btn_query");
		var btn_searchexport = $("#ngs_formSearch #btn_searchexport");
    	var btn_selectexport = $("#ngs_formSearch #btn_selectexport");
		
    	//添加日期控件
    	laydate.render({
    	   elem: '#sxrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sxrqend'
    	  ,theme: '#2381E9'
    	});
    	
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			NgsResult(true);
    		});
    	}
  
       /* ---------------------------查看列表-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#ngs_formSearch #tbs_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			NgsById(sel_row[0].ngsid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
    		
    	 /* ---------------------------导出-----------------------------------*/
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#ngs_formSearch #tbs_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].ngsid;
        		}
        		ids = ids.substr(1);
        		$.showDialog(exportPrepareUrl+"?ywid=NGS_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});

    	btn_searchexport.unbind("click").click(function(){
    		$.showDialog(exportPrepareUrl+"?ywid=NGS_SEARCH&expType=search&callbackJs=NgsSearchData"
    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});
    	
    	
    	
    	
       	/**显示隐藏**/      
    	$("#ngs_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(ngs_turnOff){
    			$("#ngs_formSearch #searchMore").slideDown("low");
    			ngs_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#ngs_formSearch #searchMore").slideUp("low");
    			ngs_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
    	
    };
    	return oInit;
    };
    

    function searchMaterResult(isTurnBack){
    	//关闭高级搜索条件
    	$("#ngs_formSearch #searchMore").slideUp("low");
    	mater_turnOff=true;
    	$("#ngs_formSearch #sl_searchMore").html("高级筛选");
    	if(isTurnBack){
    		$('#ngs_formSearch #tbs_list').bootstrapTable('refresh',{pageNumber:1});
    	}else{
    		$('#ngs_formSearch #tbs_list').bootstrapTable('refresh');
    	}
    }


    
    var sfbc=0;//是否继续保存 
    	var viewNgsConfig = {
    			width		: "800px",
    			offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    			buttons		: {
    				cancel : {
    					label : "关 闭",
    					className : "btn-default"
    				}
    			} 
    		};   	
    	
    	

$(function(){
	
	//0.界面初始化
	// 1.初始化Table
	var oTable = new ngs_TableInit();
	oTable.Init();
	
    //2.初始化Button的点击事件
    var oButtonInit = new ngs_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#ngs_formSearch .chosen-select').chosen({width: '100%'});
	
	
	$("#ngs_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	
});