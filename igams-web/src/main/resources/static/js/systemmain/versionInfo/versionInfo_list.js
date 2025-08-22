var VersionInfo_turnOff=true;

var VersionInfo_TableInit=function(){
	var oTableInit=new Object();
	// 初始化table
	oTableInit.Init=function(){
		$("#versionInfo_formSearch #versionInfo_list").bootstrapTable({
			url: $("#versionInfo_formSearch #urlPrefix").val()+'/systemmain/versionInfo/pageGetListVersionInfo',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#versionInfo_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "sxsj",					//排序字段
            sortOrder: "desc",               	//排序方式
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
            //height: 500,                     	//行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "bbid",                	//每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                	//是否显示父子表
            columns: [{
                checkbox: true,
               	width: '1%',
            },{
            	title: '序号',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '2%',
                align: 'left',
                visible:true
            },/*{
                field: 'bbid',
                title: '版本ID',
                width: '3%',
                align: 'left',
                sortable: true,   
                visible: true
            },*/{
                field: 'bbbq',
                title: '版本标签',
                width: '8%',
                align: 'left',
                sortable: true, 
                visible:true
            },{
                field: 'gxnr',
                title: '更新内容',
                width: '40%',
                align: 'left',   
                visible: true
            },{
                field: 'czry',
                title: '操作人员',
                width: '6%',
                align: 'left',
                sortable: true,   
                visible: true
            },{
                field: 'sxsj',
                title: '上线时间',
                width: '8%',
                align: 'left',
                sortable: true,   
                visible: true
            },],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	//双击事件
            	VersionInfoDealById(row.bbid,'view',$("#versionInfo_formSearch #btn_view").attr("tourl"));
             },
		});
		$("#versionInfo_formSearch #versionInfo_list").colResizable({
			liveDrag:true, 
			gripInnerHtml:"<div class='grip'></div>", 
			draggingClass:"dragging", 
			resizeMode:'fit', 
			postbackSafe:true,
			partialRefresh:true
		})
	};
	//得到查询的参数
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "bbid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return versionInfoSearchData(map);
	};
	return oTableInit
};

//条件搜索
function versionInfoSearchData(map){
	var cxtj=$("#versionInfo_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#versionInfo_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["bbbq"]=cxnr;
	}else if(cxtj=="1"){
		map["gxnr"]=cxnr
	}
	return map;
}


/**
 * 删除标记格式化函数
 * @param value
 * @param row
 * @param index
 * @returns
 */
/*function scbjformat(value,row,index){
	if(row.scbj==0){
		var html="<span style='color:green;'>"+"使用中"+"</span>";
	}else if (row.scbj==1){
		var html="<span style='color:red;'>"+"删除"+"</span>";
	}else {
		var html="<span style='color:red;'>"+"停用"+"</span>";		
	}
	return html;
}*/


var VersionInfo_ButtonInit= function (){
	var oInit=new Object();
	var postdata = {};
	
	oInit.Init=function(){
		//初始化页面上面的按钮事件
		var btn_query=$("#versionInfo_formSearch #btn_query");
		var btn_add=$("#versionInfo_formSearch #btn_add");
		var btn_view=$("#versionInfo_formSearch #btn_view");
		var btn_del = $("#versionInfo_formSearch #btn_del");
		var btn_mod = $("#versionInfo_formSearch #btn_mod");
//		//添加日期控件
//    	laydate.render({
//    	   elem: '#versionInfo_formSearch #sxsjstart'
//    	  ,theme: '#2381E9'
//    	});
//    	//添加日期控件
//    	laydate.render({
//    	   elem: '#versionInfo_formSearch #sxsjend'
//    	  ,theme: '#2381E9'
//    	});
		/*--------------------------------模糊查询---------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchVersionInfoResult(true); 
    		});
		}
		/*--------------------------------新增版本信息---------------------------*/
		btn_add.unbind("click").click(function(){
			VersionInfoDealById(null,"add",btn_add.attr("tourl"));
    	});
		/*--------------------------------修改版本信息---------------------------*/
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#versionInfo_formSearch #versionInfo_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length == 1){
    			VersionInfoDealById(sel_row[0].bbid, "mod", btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		 /* ------------------------------查看版本信息-----------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row=$('#versionInfo_formSearch #versionInfo_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			VersionInfoDealById(sel_row[0].bbid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/* ------------------------------删除版本信息-----------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#versionInfo_formSearch #versionInfo_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else {
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].bbid;
        		}
			ids=ids.substr(1);
			$.confirm('您确定要删除所选择的信息吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= $("#versionInfo_formSearch #urlPrefix").val()+btn_del.attr("tourl");
    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								searchVersionInfoResult(true);
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
	}
	return oInit;
}

function VersionInfoDealById(id,action,tourl){
	var url= tourl;
	if(!tourl){
		return;
	}
	tourl = $("#versionInfo_formSearch #urlPrefix").val()+tourl;
	if(action =='add'){
		var url=tourl;
		$.showDialog(url,'新增版本信息',addVersionInfoConfig);
	}else if(action =='mod'){
		var url= tourl+"?bbid="+id;
		$.showDialog(url,'修改版本信息',modVersionInfoConfig);
	}else if(action =='view'){
		var url= tourl+"?bbid="+id;
		$.showDialog(url,'查看版本信息',viewVersionInfoConfig);
	}
}
/**
 * 增加版本信息
 */
var addVersionInfoConfig = {
		width		: "800px",
		modalName	: "addVersionInfoModal",
		formName	: "addVersionInfoForm",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#addVersionInfoForm").valid()){// 表单验证
						$.alert("请填写完整信息");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					
					$("#addVersionInfoForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"addVersionInfoForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								
								searchVersionInfoResult(true);
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
/**
 * 修改版本信息
 */
var modVersionInfoConfig = {
		width		: "800px",
		modalName	: "modVersionInfoModal",
		formName	: "addVersionInfoForm",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
//					$("#addVersionInfoForm #bbid").val($("#addVersionInfoForm #bbid").val());
					if(!$("#addVersionInfoForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					
					$("#addVersionInfoForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"addVersionInfoForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchVersionInfoResult(true);
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

/**
 * 查看版本信息
 */
var viewVersionInfoConfig={
	width		: "800px",
	modalName	:"viewVersionInfoModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

/**
 * 列表刷新
 */
function searchVersionInfoResult(isTurnBack){
/*	//关闭高级搜索条件
	$("#versionInfo_formSearch #searchMore").slideUp("low");
	VersionInfo_turnOff=true;
	$("#versionInfo_formSearch #sl_searchMore").html("高级筛选");*/
	if(isTurnBack){
		$('#versionInfo_formSearch #versionInfo_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#versionInfo_formSearch #versionInfo_list').bootstrapTable('refresh');
	}
}

$(function(){
	//初始化Table
	var oTable = new VersionInfo_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new VersionInfo_ButtonInit();
    oButtonInit.Init();
    
    jQuery('#versionInfo_formSearch .chosen-select').chosen({width: '100%'});
});