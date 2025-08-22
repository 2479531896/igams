var sel_turnOff = true; 

var Payment_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#payment_formSearch #payment_list").bootstrapTable({
			url: '/payment/payment/pageGetListPayment',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#payment_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "payinfo.lrsj",				// 排序字段
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
            uniqueId: "zfid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true
            },{
            	field: 'ybbh',
                title: '标本编号',
                width: '7%',
                align: 'left',
                visible:true
            },{
                field: 'nbbm',
                title: '内部编码',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'hzxm',
                title: '患者姓名',
                width: '5%',
                align: 'left',
                visible:true,
                
            },{
                field: 'ddbh',
                title: '订单编号',
                width: '15%',
                align: 'left',
                visible: false
			},{
				field: 'ptddh',
				title: '平台订单号',
				width: '15%',
				align: 'left',
				visible: true
            },{
                field: 'dyjk',
                title: '调用接口',
                width: '9%',
                align: 'left',
                visible: true
            },{
                field: 'jcxmmc',
                title: '检查项目',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'jg',
                title: '支付结果',
                width: '5%',
                align: 'left',
                formatter:zfjgFormat,
                visible: true
            },{
                field: 'fkje',
                title: '付款金额',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'yfje',
                title: '应付金额',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'jyje_yuan',
                title: '交易金额',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'jysj',
                title: '交易时间',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'zffs',
                title: '支付方式',
                width: '6%',
                align: 'left',
                formatter:zffsFormat,
                visible: true
            },{
                field: 'sjhb',
                title: '合作伙伴',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'cwxx',
                title: '错误信息',
                width: '25%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	Payment_DealById(row.zfid,'view',$("#payment_formSearch #btn_view").attr("tourl"));
             },
		});
        $("#payment_formSearch #payment_list").colResizable({
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
            sortLastName: "payinfo.jyje", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getPaymentSearchData(map);
	};
	return oTableInit;
}

function zfjgFormat(value, row, index){
	if(row.jg == "1"){
		return "<span style='color:green;'>成功</span>";
	}else if(row.jg == "9"){
		return "<span style='color:red;'>异常</span>";
	}
}

function zffsFormat(value, row, index){
	if(row.zffs == "ZF"){
		return "支付宝";
	}else if(row.zffs == "WX"){
		return "微信";
	}else if(row.zffs == "YL"){
		return "银联";
	}
}

function getPaymentSearchData(map){
	var payment_select=$("#payment_formSearch #payment_select").val();
	var payment_input=$.trim(jQuery('#payment_formSearch #payment_input').val());
	if(payment_select=="0"){
		map["hzxm"]=payment_input
	}else if(payment_select=="1"){
		map["ybbh"]=payment_input
	}else if(payment_select=="2"){
		map["ptddh"]=payment_input
	} else if(payment_select=="3"){
		map["ddbh"]=payment_input
	}else if(payment_select=="4"){
		map["sjhb"]=payment_input
	}
	//交易时间
	var jysjstart = jQuery('#payment_formSearch #jysjstart').val();
		map["jysjstart"] = jysjstart;
	var jysjend = jQuery('#payment_formSearch #jysjend').val();
		map["jysjend"] = jysjend;
	//支付结果状态
	var jgs=jQuery('#payment_formSearch #jg_id_tj').val();
	map["jgs"] = jgs;
	return map;
}

function showJyzje(){
	var payment_select=$("#payment_formSearch #payment_select").val();
	if(payment_select=="4"){
		$("#payment_formSearch #money").removeClass("hidden");
	}else{
		$("#payment_formSearch #money").addClass("hidden");
	}
}
function searchPaymentResult(isTurnBack){
	var payment_select=$("#payment_formSearch #payment_select").val();
	if(payment_select=="4"){
		var payment_input=$.trim(jQuery('#payment_formSearch #payment_input').val());
		if (payment_input){
			$.ajax({
				url: '/payment/payment/pagedataPayYuan',
				type: 'post',
				dataType: 'JSON',
				data: { "access_token": $("#ac_tk").val(),
					"jysjstart" : jQuery('#payment_formSearch #jysjstart').val(),
					"jysjend" : jQuery('#payment_formSearch #jysjend').val(),
					"sjhb" : payment_input },
				success: function(data) {
					if (data.status == "success"){
						$("#payment_formSearch #jyzje").text(data.jyzje)
					}
				}
			})
		}
	}
	//关闭高级筛选条件
	$("#payment_formSearch #searchMore").slideUp("low");
	sel_turnOff = true;
	$("#payment_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#payment_formSearch #payment_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#payment_formSearch #payment_list').bootstrapTable('refresh');
	}
}
function Payment_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action=="view"){
		var url=tourl+"?zfid="+id
		$.showDialog(url,'查看支付详细信息',viewPaymentConfig);
	}
}

//提供给导出用的回调函数
function paymentSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="payinfo.zfid";
	map["sortLastOrder"]="asc";
	map["sortName"]="payinfo.lrsj";
	map["sortOrder"]="asc";
	map["yhid"]=$("#yhid").val();
	return getPaymentSearchData(map);
}

var viewPaymentConfig = {
		width		: "1200px",
		modalName	:"viewPaymentModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};


var Payment_oButton=function(){
	var oButtonInit = new Object();
	var postdata = {};
	oButtonInit.Init=function(){
		var btn_query=$("#payment_formSearch #btn_query");//模糊查询
		var btn_view = $("#payment_formSearch #btn_view");//查看
		var btn_selectexport = $("#payment_formSearch #btn_selectexport");//选中导出
    	var btn_searchexport = $("#payment_formSearch #btn_searchexport");//搜索导出
		//添加日期控件
	      laydate.render({
	         elem: '#jysjstart'
	        ,theme: '#2381E9'
	      });
		//添加日期控件
	      laydate.render({
	         elem: '#jysjend'
	        ,theme: '#2381E9'
	      });
/*-----------------------模糊查询------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchPaymentResult(true); 
    		});
		}
/*-----------------------选中导出------------------------------------*/
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#payment_formSearch #payment_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].zfid;
        		}
        		ids = ids.substr(1);
        		$.showDialog(exportPrepareUrl+"?ywid=PAYMENT_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));//gnm功能名
    		}else{
    			$.error("请选择数据");
    		}
    	});
/*-----------------------搜索导出------------------------------------*/
    	btn_searchexport.unbind("click").click(function(){
    		$.showDialog(exportPrepareUrl+"?ywid=PAYMENT_SEARCH&expType=search&callbackJs=paymentSearchData"
    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});
/*-----------------------查看------------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#payment_formSearch #payment_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			Payment_DealById(sel_row[0].zfid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
/*-----------------------高级筛选的显示和隐藏--------------------------------------*/
		$("#payment_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(sel_turnOff){
    			$("#payment_formSearch #searchMore").slideDown("low");
    			sel_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#payment_formSearch #searchMore").slideUp("low");
    			sel_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});
	}
	return oButtonInit;
}


$(function(){
	var oTable = new Payment_TableInit();
	    oTable.Init();
	
	var oButton = new Payment_oButton();
	    oButton.Init();
	    
    jQuery('#payment_formSearch .chosen-select').chosen({width: '100%'});
})