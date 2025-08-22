var Information_Dj_turnOff=true;
var JsonData=[];
var Information_Dj_TableInit = function (JsonData) {
	var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#information_dj_formSearch #tb_list').bootstrapTable({
			url: '/wechat/information/pageGetListRegistration',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#information_dj_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			sortable: true,                     // 是否启用排序
			sortName:"lrsj",					// 排序字段
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
			uniqueId: "xxid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: JsonData,
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				InformationDjById(row.xxid,'view',$("#information_dj_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#information_dj_formSearch #tb_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true,
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
        sortLastName: "lrsj", // 防止同名排位用
        sortLastOrder: "desc" // 防止同名排位用
        // 搜索框使用
        // search:params.search
    };
	
    return getInformationDjData(map);
	};
	return oTableInit;
};


function getInformationDjData(map){
	var cxtj=$("#information_dj_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#information_dj_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr
	}

	//信息类型
	var xxlx = jQuery("#information_dj_formSearch #xxlx_id_tj").val();
	map["xxlx"] = xxlx;
	 return map;
}

//提供给导出用的回调函数
function InformationDjSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="dj.xxid";
	map["sortLastOrder"]="asc";
	map["sortName"]="dj.lrsj";
	map["sortOrder"]="asc";
	return getInformationDjData(map);
}

function InformationDjResult(){

	//关闭高级搜索条件
	$("#information_dj_formSearch #searchMore").slideUp("low");
	Information_Dj_turnOff=true;
	$("#information_dj_formSearch #sl_searchMore").html("高级筛选");
	$('#information_dj_formSearch #tb_list').bootstrapTable('destroy');
	var oTable = new Information_Dj_TableInit(JsonData);
	oTable.Init();
}

function InformationDjById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var xxlx = jQuery("#information_dj_formSearch #xxlx_id_tj").val();
		var url= tourl + "?xxid=" +id+"&xxlx="+xxlx;
		$.showDialog(url,'信息登记详细信息',viewInformationDjConfig);
	}
}


var InformationDj_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#information_dj_formSearch #btn_view");
		var btn_query = $("#information_dj_formSearch #btn_query");
		var btn_del = $("#information_dj_formSearch #btn_del");
		var btn_searchexport = $("#information_dj_formSearch #btn_searchexport");//搜索导出
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			JsonData=[];
    			xxlx=jQuery("#information_dj_formSearch #xxlx_id_tj").val();
    			$.ajax({ 
    			    type:'post',  
    			    url:"/wechat/information/pagedataXxpz",
    			    cache: false,
    			    data: {"xxlx":xxlx,"access_token":$("#ac_tk").val()},  
    			    dataType:'json', 
    			    success:function(data){
    			    	//返回值
    			    	var checkbox={};
    			    	checkbox["checkbox"]=true;
    			    	JsonData.push(checkbox);
    			    	var xxid={};//信息ID
    			    	xxid["field"]="xxid";
    			    	xxid["title"]="信息ID";
    			    	xxid["width"]="6%";
    			    	xxid["align"]="left";
    			    	xxid["visible"]=false;
    			    	JsonData.push(xxid);
    			    	for(var i=0;i<data.pzs.length;i++){//动态添加信息配置中设置的字段
    			    		var datas={};
    			    		datas["field"]=data.pzs[i].wbxx;
    			    		datas["title"]=data.pzs[i].xsmc;
    			    		datas["width"]="6%";
    			    		datas["align"]="left";
    			    		datas["visible"]=true;
    			    		JsonData.push(datas);
    			    	}
    			    	InformationDjResult();
    			    }
    			});
    		});
    	}
        /*---------------------------查看信息登记信息-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#information_dj_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			InformationDjById(sel_row[0].xxid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------删除信息登记信息-----------------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#information_dj_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].xxid;
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
    								InformationDjResult();
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
    	/*---------------------------------------搜索导出-----------------------------------*/
    	btn_searchexport.unbind("click").click(function(){
    		var xxlx = jQuery("#information_dj_formSearch #xxlx_id_tj").val();
    		if(xxlx==null || xxlx==""){
    			$.confirm("请先选择信息类型!");
    		}else{
    			$.showDialog(exportPrepareUrl+"?ywid=INFORMATION_REGISTER_SEARCH&expType=search&callbackJs=InformationDjSearchData"
        				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}
    	});
    	
    	/**显示隐藏**/      
    	$("#information_dj_formSearch #sl_searchMore").on("click", function(ev){ 
    		var ev=ev||event; 
    		if(Information_Dj_turnOff){
    			$("#information_dj_formSearch #searchMore").slideDown("low");
    			Information_Dj_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#information_dj_formSearch #searchMore").slideUp("low");
    			Information_Dj_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});
    };
    return oInit;
};

var viewInformationDjConfig = {
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
	var oTable = new Information_Dj_TableInit(JsonData);
	oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new InformationDj_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#information_dj_formSearch .chosen-select').chosen({width: '100%'});
	// 初始绑定显示更多的事件
	//var showMore = function() {
	$("#information_dj_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	
});