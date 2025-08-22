var Query_turnOff=true;

var Query_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#querylist_formSearch #tb_list').bootstrapTable({
            url: $("#querylist_formSearch #urlPrefix").val()+'/systemmain/query/pageGetListQuery',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#querylist_formSearch #toolbar', //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName:"cx.cxmc,cx.lrsj",					//排序字段
            sortOrder: "asc",                   //排序方式
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
            uniqueId: "cxid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
				width: '4%'
            }, {
                field: 'cxid',
                title: '查询ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'cxmc',
                title: '查询名称',
                titleTooltip:'查询名称',
                width: '40%',
                align: 'left',
				sortable: true,
                visible: true
            }, {
                field: 'cxdm',
                title: '查询代码',
                titleTooltip:'查询代码',
                width: '11%',
                align: 'left',
                visible: true
            }, {
                field: 'bz',
                title: '备注',
                titleTooltip:'备注',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'cxqfmc',
                title: '查询区分',
                titleTooltip:'查询区分',
                width: '8%',
                align: 'left',
				sortable: true,
                visible: true
            }, {
                field: 'xsfsmc',
                title: '显示方式',
                titleTooltip:'显示方式',
                width: '8%',
                align: 'left',
				sortable: true,
                visible: true
            }, {
                field: 'cxbm',
                title: '查询编码',
                titleTooltip:'用于匹配统计图',
                width: '8%',
                align: 'left',
				sortable: true,
                visible: true
            }, {
                field: 'lbqf',
                title: '类别区分',
                titleTooltip:'做角色查看统计权限',
                width: '8%',
                align: 'left',
				sortable: true,
                visible: true
            }, {
                field: 'px',
                title: '查询排序',
                width: '5%',
                align: 'left',
				sortable: true,
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	QueryDealById(row.cxid, 'view',$("#querylist_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#querylist_formSearch #tb_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true}        
        );
    };

    //得到查询的参数
    oTableInit.queryParams = function(params){
    	//请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
    	//例如 toolbar 中的参数 
    	//如果 queryParamsType = ‘limit’ ,返回参数必须包含 
    	//limit, offset, search, sort, order 
    	//否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder. 
    	//返回false将会终止请求
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   //页面大小
        	pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "cx.cxid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var cxbt = $("#querylist_formSearch #cxtj").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#querylist_formSearch #cxnr').val());

    	// '0':'审核名称','1':'描述',
    	if (cxbt == "0") {
    		map["cxmc"] = cxnr;
    	}else if (cxbt == "1") {
    		map["cxbm"] = cxnr;
    	}else if (cxbt == "2") {
    		map["lbqf"] = cxnr;
    	}
		var cxqfs = jQuery('#querylist_formSearch #cxqf_id_tj').val();
		map["cxqfs"] = cxqfs;
		var xsfss = jQuery('#querylist_formSearch #xsfs_id_tj').val();
		map["xsfss"] = xsfss;
    	return map;
    };
    return oTableInit;
};

$("#querylist_formSearch #sl_searchMore").on("click", function(ev){
	var ev=ev||event;
	if(Query_turnOff){
		$("#querylist_formSearch #searchMore").slideDown("low");
		Query_turnOff=false;
		this.innerHTML="基本筛选";
	}else{
		$("#querylist_formSearch #searchMore").slideUp("low");
		Query_turnOff=true;
		this.innerHTML="高级筛选";
	}
	ev.cancelBubble=true;
});

//按钮动作函数
function QueryDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl=$("#querylist_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?cxid=" +id;
		$.showDialog(url,'查看临时查询',viewQueryConfig);
	}else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'新增临时查询',addQueryConfig);
	}else if(action =='mod'){
		var url=tourl + "?cxid=" +id;
		$.showDialog(url,'编辑临时查询',addQueryConfig);
	}else if(action =='execute'){
		var url=tourl + "?cxid=" +id;
		$.showDialog(url,'执行临时查询',executeQueryConfig);
	}else if(action =='distinguish'){
		var url=tourl + "?cids=" +id;
		$.showDialog(url,'用户区分',distinguishQueryConfig);
	}else if(action =='download'){
		var url=tourl + "?cxid=" +id;
		$.showDialog(url,'下载',downloadQueryConfig);
	}
}




var executeQueryConfig = {
		width		: "1200px",
		modalName	: "addQueryModal",
		formName	: "ajaxForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
//			execute : {
//				label : "执 行",
//				className : "btn-success",
//				callback : function() {
//					debugger
//					if(!$("#ajaxForm").valid()){
//						return false;
//					}
//					var $this = this;
//					var opts = $this["options"]||{};
//					var cxid= $("#ajaxForm #cxid").val();
//					$.ajax({ 
//					    type:'post',  
////					    url:$("#querylist_formSearch #urlPrefix").val()+"/systemmain/query/executeQuery",
//					    url:$("#querylist_formSearch #urlPrefix").val()+"/sale/query/executeQuery",
//					    cache: false,
//					    data: {"cxid":cxid,"access_token":$("#ac_tk").val()},  
//					    dataType:'json', 
//					    success:function(data){
//					    	//返回值
//					    	if(data["error"]!=null){
//					    		$.error(data["error"]);
//					    	}else{
//					    		$("#bt").empty();
//					    		$("#nr").empty();
//					    		var zdm=[];
//						    	var bthtml="";
//						    	var nrhtml="";
//						    	for(var i=0;i<data.headList.length;i++){
//						    		bthtml=bthtml+
//					    			"<th title='"+data.headList[i]+"' style='width:10%;'>"+data.headList[i]+"</th>";
//						    	}
//				    			for(var i=0;i<data.resultList.length;i++){
//				    				nrhtml=nrhtml+"<tr>";
//				    				for(var j=0;j<data.headList.length;j++){
//				    					if(data.resultList[i][data.headList[j]]=="undefined" || data.resultList[i][data.headList[j]]==null){
//				    						nrhtml=nrhtml+"<td title=''></td>";
//				    					}else{
//				    						nrhtml=nrhtml+"<td title='"+data.resultList[i][data.headList[j]]+"'>"+data.resultList[i][data.headList[j]]+"</td>";
//				    					}
//				    				}
//				    				nrhtml=nrhtml+"</tr>";
//						    	}
//						    	$("#bt").append(bthtml);
//						    	$("#nr").append(nrhtml);
//					    	}
//					    }
//					});
//					return false;
//				}
//			},
			selectexecute : { //临时查询表的执行方法调用销售的执行方法
				label : "执 行",
				className : "btn-success",
				callback : function() {
					$(".shadow").css({'display':'block'});
					$('.addBox').show();
					$("#btn_selectexecute").attr("disabled","disabled");
					setTimeout(5000);
					executionSelect(); 
					return false;
				}
			},
		
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
var downloadQueryConfig = {
	width		: "1200px",
	modalName	: "downloadQueryModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		download : { //临时查询表的执行方法调用销售的执行方法
			label : "下 载",
			className : "btn-success",
			callback : function() {
				$(".shadow").css({'display':'block'});
				$('.addBox').show();
				$("#btn_download").attr("disabled","disabled");
				setTimeout(5000);
				executionDownload();
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};


var viewQueryConfig = {
	width		: "600px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var addQueryConfig = {
	width		: "600px",
	modalName	: "addQueryModal",
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
				if($("#ajaxForm #yzbj").val()=="1"){
					if($("#ajaxForm #cxbm").val()==null || $("#ajaxForm #cxbm").val()==""){
						$.alert("查询编码不能为空！");
						return false;
					}
					if($("#ajaxForm #lbqf").val()==null || $("#ajaxForm #lbqf").val()==""){
						$.alert("类别区分不能为空！");
						return false;
					}
					if($("#ajaxForm #px").val()==null || $("#ajaxForm #px").val()==""){
						$.alert("排序不能为空！");
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
								searchQueryResult();
							}
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				});
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};



var distinguishQueryConfig = {
		width		: "800px",
		modalName	: "distinguishQueryModal",
		formName	: "configUserForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#configUserForm").valid()){
						return false;
					}
					
					var $this = this;
					var opts = $this["options"]||{};
					
					$("#configUserForm input[name='access_token']").val($("#ac_tk").val());
					$.closeModal(opts.modalName);
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};






var Query_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_add = $("#querylist_formSearch #btn_add");
    	var btn_mod = $("#querylist_formSearch #btn_mod");
    	var btn_del = $("#querylist_formSearch #btn_del");
    	var btn_view = $("#querylist_formSearch #btn_view");
    	var btn_execute= $("#querylist_formSearch #btn_execute");
    	var btn_download= $("#querylist_formSearch #btn_download");
    	var btn_query = $("#querylist_formSearch #btn_query");
    	var btn_distinguish = $("#querylist_formSearch #btn_distinguish");

    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchQueryResult(true);
    		});
    	}
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#querylist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			QueryDealById(sel_row[0].cxid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
    	btn_execute.unbind("click").click(function(){
    		var sel_row = $('#querylist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			QueryDealById(sel_row[0].cxid,"execute",btn_execute.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_download.unbind("click").click(function(){
    		var sel_row = $('#querylist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			QueryDealById(sel_row[0].cxid,"download",btn_download.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
    	btn_distinguish.unbind("click").click(function(){
    		var sel_row = $('#querylist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var cids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			cids = cids + ","+ sel_row[i].cxid;
    		}
    		cids = cids.substr(1);
				
       		QueryDealById(cids,"distinguish",btn_distinguish.attr("tourl"));
    	});
    	btn_add.unbind("click").click(function(){
    		QueryDealById(null,"add",btn_add.attr("tourl"));
    	});
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#querylist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			QueryDealById(sel_row[0].cxid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#querylist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			QueryDealById(sel_row[0].cxid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#querylist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].cxid;
    		}
    		ids = ids.substr(1);
    		$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= $("#querylist_formSearch #urlPrefix").val()+btn_del.attr("tourl");
    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								searchQueryResult();
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

function searchQueryResult(isTurnBack){
	if(isTurnBack){
		$('#querylist_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#querylist_formSearch #tb_list').bootstrapTable('refresh');
	}
}

$(function(){
    //1.初始化Table
    var oTable = new Query_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new Query_ButtonInit();
    oButtonInit.Init();

	$("#querylist_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	//所有下拉框添加choose样式
	jQuery('#querylist_formSearch .chosen-select').chosen({width: '100%'});
});
