var ddfbsgl_turnOff=true;
var Ddfbs_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#ddfbsgl_formSearch #ddfbsgl_list").bootstrapTable({
			url: $("#ddfbsgl_formSearch #urlPrefix").val()+'/ddfbsgl/ddfbsgl/pageGetListDdfbsgl',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#ddfbsgl_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "fbsgl.lrsj",				// 排序字段
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
            uniqueId: "processinstanceid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                  // 是否显示父子表
            columns: [{
                checkbox: true,
				width: '4%'
            },{
                field: 'ywmc',
                title: '业务名称',
                width: '16%',
                align: 'left',
                sortable: true,  
                visible: true
            },{
                field: 'processinstanceid',
                title: '审批实例ID',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'ywlxmc',
                title: '业务类型',
                width: '16%',
                align: 'left',
                sortable: true,  
                visible:true
            },{
                field: 'gldh',
                title: '关联单号',
                width: '16%',
                align: 'left',
                sortable: true,  
                visible: true
            },{
                field: 'cljg',
                title: '审批结果',
                formatter:cljgformat,
                width: '16%',
                align: 'left',
                sortable: true,  
                visible: true
            },{
                field: 'jszt',
                title: '结束状态',
                formatter:jsztformat,
                width: '16%',
                align: 'left',
                sortable: true,  
                visible: true
            },{
                field: 'lrsj',
                title: '录入时间',
                width: '16%',
                align: 'left',
                sortable: true,  
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	Ddfbsgl_DealById(row.processinstanceid,'view',$("#ddfbsgl_formSearch #btn_view").attr("tourl"));
             },
		});
        $("#ddfbsgl_formSearch #ddfbsgl_list").colResizable({
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
            sortLastName: "fbsgl.processinstanceid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getDingspglSearchData(map);
	};
	return oTableInit;
}

function cljgformat(value,row,index){
	var cljg="";
	if(row.cljg=='1'){
		cljg="<span style='color:green;'>"+"正常"+"</span>";
	}else if(row.cljg=='0'){
		cljg="<span style='color:red;'>"+"异常"+"</span>";
	}
	return cljg;
}

function jsztformat(value,row,index){
	var jszt="";
	if(row.jszt=='1'){
		jszt="<span style='color:green;'>"+"已结束"+"</span>";
	}else if(row.jszt=='0'){
		jszt="<span style='color:orange;'>"+"未结束"+"</span>";
	}
	return jszt;
}

function getDingspglSearchData(map){
	var ddfbsgl_select=$("#ddfbsgl_formSearch #ddfbsgl_select").val();
	var ddfbsgl_input=$.trim(jQuery('#ddfbsgl_formSearch #ddfbsgl_input').val());
	if(ddfbsgl_select=="0"){
		map["ywmc"]=ddfbsgl_input
	}else if(ddfbsgl_select=="1"){
		map["ywlxmc"]=ddfbsgl_input
	}else if(ddfbsgl_select=="2"){
		map["gldh"]=ddfbsgl_input
	}
	//审批类型
	var ywlxs = jQuery('#ddfbsgl_formSearch #ywlx_id_tj').val();
		map["ywlxs"] = ywlxs;	
	return map;
}



function searchDdfbsglResult(isTurnBack){
	//关闭高级搜索条件
	$("#ddfbsgl_formSearch #searchMore").slideUp("low");
	ddfbsgl_turnOff=true;
	$("#ddfbsgl_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#ddfbsgl_formSearch #ddfbsgl_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#ddfbsgl_formSearch #ddfbsgl_list').bootstrapTable('refresh');
	}
}
function Ddfbsgl_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl=$("#ddfbsgl_formSearch #urlPrefix").val()+tourl;
	if(action=="view"){
		var url=tourl+"?processinstanceid="+id
		$.showDialog(url,'查看钉钉审批管理详细信息',viewDdfbsglConfig);
	}
}

var viewDdfbsglConfig = {
		width		: "1200px",
		modalName	:"viewDingspglModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

var Dingfbsgl_oButton=function(){
	var oButtonInit = new Object();
	var postdata = {};
	oButtonInit.Init=function(){
		var btn_query=$("#ddfbsgl_formSearch #btn_query");
		var btn_view = $("#ddfbsgl_formSearch #btn_view");
		var btn_execute = $("#ddfbsgl_formSearch #btn_execute");
/*-----------------------模糊查询------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchDdfbsglResult(true); 
    		});
		}
/*-----------------------查看------------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#ddfbsgl_formSearch #ddfbsgl_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			Ddfbsgl_DealById(sel_row[0].processinstanceid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
/*-----------------------执行审核回调------------------------------------*/
    	btn_execute.unbind("click").click(function(){
    		var sel_row = $('#ddfbsgl_formSearch #ddfbsgl_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0 || sel_row.length>1){
    			$.error("请选中一行");
    			return;
    		}else {
    			if(sel_row[0].jszt!='1'){
    				$.error("请选择审批已结束的数据！");
    				return;
    			}
    			if(sel_row[0].cljg!='0'){
    				$.error("请选择审批异常的数据！");
    				return;
    			}
    			$.confirm('您确定要执行审批回调吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= $("#ddfbsgl_formSearch #urlPrefix").val()+btn_execute.attr("tourl");
        				jQuery.post(url,{processinstanceid:sel_row[0].processinstanceid,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								searchDdfbsglResult(true);
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

    	/**显示隐藏**/      
    	$("#ddfbsgl_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(ddfbsgl_turnOff){
    			$("#ddfbsgl_formSearch #searchMore").slideDown("low");
    			ddfbsgl_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#ddfbsgl_formSearch #searchMore").slideUp("low");
    			ddfbsgl_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
	}
	return oButtonInit;
}

var ddfbsgl_PageInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
    	var ywlx = $("#ddfbsgl_formSearch a[id^='ywlx_id_']");
    	$.each(ywlx, function(i, n){
    		var id = $(this).attr("id");
    		var code = id.substring(id.lastIndexOf('_')+1,id.length);
    		addTj('ywlx',code,'ddfbsgl_formSearch');
    	});
    }
    return oInit;
}

$(function(){
	//0.界面初始化
    var oInit = new ddfbsgl_PageInit();
    oInit.Init();
	var oTable = new Ddfbs_TableInit();
	    oTable.Init();
	
	var oButton = new Dingfbsgl_oButton();
	    oButton.Init();
	    
    jQuery('#ddfbsgl_formSearch .chosen-select').chosen({width: '100%'});
})