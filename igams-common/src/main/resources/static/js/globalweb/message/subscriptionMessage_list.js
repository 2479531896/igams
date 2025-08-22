var subscriptionMessage_turnOff=true;
var subscriptionMessage_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#subscription_formSearch #subscription_list").bootstrapTable({
			url: $("#subscription_formSearch #urlPrefix").val()+'/subscribe/subscribe/pageGetListSubscribe',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#subscription_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "tmp.yhmc,tmp.szlb,tmp.glxx",				// 排序字段
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
            uniqueId: "szid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
				width: '4%'
            },{
                field: 'yhmc',
                title: '用户',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'szlbmc',
                title: '设置类别名称',
                width: '16%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'glxx',
                title: '关联信息',
                width: '16%',
                align: 'left',
                sortable: true,
                visible:true,
			},{
				field: 'szz',
				title: '是否订阅',
				width: '6%',
				align: 'left',
				sortable: true,
				visible:true,
				formatter:sfdyformat
            },{
                field: 'xxnr',
                title: '消息',
                width: '26%',
                align: 'left',
                sortable: true,
                visible:true,
              }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	Subscription_DealById(row.szid,'view',$("#subscription_formSearch #btn_view").attr("tourl"));
             },
		});
        $("#subscription_formSearch #subscription_list").colResizable({
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
            sortLastName: "tmp.szid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getSubSearchData(map);
	};
	return oTableInit;
}
function sfdyformat(value,row,index){
	if(row.szz==1){
		var html="<span style='color:green;'>"+"是"+"</span>";
	}else{
		var html="<span style='color:red;'>"+"否"+"</span>";
	}
	return html;
}
function getSubSearchData(map){
	var subscription_select=$("#subscription_formSearch #subscription_select").val();
	var subscription_input=$.trim(jQuery('#subscription_formSearch #xxnr_input').val());
	if(subscription_select=="0"){
		map["entire"]=subscription_input;
	}else if(subscription_select=="1"){
		map["glxx"]=subscription_input
	}else if(subscription_select=="2"){
		map["xxnr"]=subscription_input
	}else if(subscription_select=="3"){
        map["yhmc"]=subscription_input
    }
	var xxlx = jQuery('#subscription_formSearch #xxlx_id_tj').val();

	xxlx = xxlx.replace(/'/g, "");
    map["xxlxs"] = xxlx;

    var sfdy = jQuery('#subscription_formSearch #sfdy_id_tj').val();
    map["szz"] = sfdy;
	return map;
}



function searchSubResult(isTurnBack){
    $("#subscription_formSearch #searchMore").slideUp("low");
	subscriptionMessage_turnOff=true;
	$("#subscription_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#subscription_formSearch #subscription_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#subscription_formSearch #subscription_list').bootstrapTable('refresh');
	}
}
function Subscription_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#subscription_formSearch #urlPrefix").val()+tourl;
	if(action=="view"){
		var url=tourl+"?szid="+id
		$.showDialog(url,'查看订阅消息',viewSubConfig);
	}
}

var viewSubConfig = {
		width		: "800px",
		modalName	:"viewSubModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};


var SubscriptionMessage_oButton=function(){
	var oButtonInit = new Object();
	var postdata = {};
	oButtonInit.Init=function(){
		var btn_query=$("#subscription_formSearch #btn_query");
		var btn_view = $("#subscription_formSearch #btn_view");
/*-----------------------模糊查询ok------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchSubResult(true);
    		});
		}
/*-----------------------查看------------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#subscription_formSearch #subscription_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			Subscription_DealById(sel_row[0].szid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
/*-------------------------------------------------------------*/
        $("#subscription_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(subscriptionMessage_turnOff){
                $("#subscription_formSearch #searchMore").slideDown("low");
                subscriptionMessage_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#subscription_formSearch #searchMore").slideUp("low");
                subscriptionMessage_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
	}
	return oButtonInit;
}


$(function(){
	var oTable = new subscriptionMessage_TableInit();
	    oTable.Init();

	var oButton = new SubscriptionMessage_oButton();
	    oButton.Init();

    jQuery('#subscription_formSearch .chosen-select').chosen({width: '100%'});
})