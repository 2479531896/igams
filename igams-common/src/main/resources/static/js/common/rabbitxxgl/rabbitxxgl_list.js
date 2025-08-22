var Rabbit_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#rabbit_formSearch #rabbit_list").bootstrapTable({
			url: '/rabbitxxgl/rabbitxxgl/pageGetListRabbit',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#rabbit_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "xxdlcwgl.cwsj",				// 排序字段
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
            uniqueId: "cwid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '1%'
            },{
                field: 'cwid',
                title: '错误ID',
                width: '28%',
                align: 'left',
                visible:false
            },{
                field: 'cwlx',
                title: '错误类型',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'cwsj',
                title: '错误时间',
                width: '18%',
                align: 'left',
                visible:true,
                
            },{
                field: 'ysnr',
                title: '原始内容',
                width: '31%',
                align: 'left',
                visible:true,
                
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	Rabbit_DealById(row.cwid,'view',$("#rabbit_formSearch #btn_view").attr("tourl"));
             },
		});
        $("#rabbit_formSearch #rabbit_list").colResizable({
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
            sortLastName: "xxdlcwgl.cwid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getRabbitSearchData(map);
	};
	return oTableInit;
}


function getRabbitSearchData(map){
	var rabbit_select=$("#rabbit_formSearch #rabbit_select").val();
	var rabbit_input=$.trim(jQuery('#rabbit_formSearch #rabbit_input').val());
	if(rabbit_select=="0"){
		map["cwid"]=rabbit_input
	}else if(rabbit_select=="1"){
		map["cwlx"]=rabbit_input
	}else if(rabbit_select=="2"){
		map["ysnr"]=rabbit_input
	}
	return map;
}



function searchRabbitResult(isTurnBack){
	if(isTurnBack){
		$('#rabbit_formSearch #rabbit_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#rabbit_formSearch #rabbit_list').bootstrapTable('refresh');
	}
}
function Rabbit_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action=="view"){
		var url=tourl+"?cwid="+id
		$.showDialog(url,'查看消息详细信息',viewRabbitConfig);
	}
}

var viewRabbitConfig = {
		width		: "800px",
		modalName	:"viewRabbitModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};


var Rabbit_oButton=function(){
	var oButtonInit = new Object();
	var postdata = {};
	oButtonInit.Init=function(){
		var btn_query=$("#rabbit_formSearch #btn_query");
		var btn_view = $("#rabbit_formSearch #btn_view");
/*-----------------------模糊查询ok------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchRabbitResult(true); 
    		});
		}
/*-----------------------查看------------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#rabbit_formSearch #rabbit_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			Rabbit_DealById(sel_row[0].cwid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
/*-------------------------------------------------------------*/
	}
	return oButtonInit;
}


$(function(){
	var oTable = new Rabbit_TableInit();
	    oTable.Init();
	
	var oButton = new Rabbit_oButton();
	    oButton.Init();
	    
    jQuery('#rabbit_formSearch .chosen-select').chosen({width: '100%'});
})