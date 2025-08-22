var DaoHuoSpyq_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#daohuospyq_formSearch #daohuospyq_list").bootstrapTable({
			url: $("#daohuospyq_formSearch #urlPrefix").val()+'/systemmain/audit/getDaoHuoSpyqData',        // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#daohuospyq_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "xtsh.shid",				// 排序字段
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
            uniqueId: "shid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'shid',
                title: '审核ID',
                width: '18%',
                align: 'left',
                visible:false            
            },{
                field: 'shlb',
                title: '审核类别',
                width: '20%',
                align: 'left',
                visible:false
            },{
                field: 'shlbmc',
                title: '审核类别名称',
                width: '14%',
                align: 'left',
                visible:true
            },{
                field: 'ywid',
                title: '业务ID',
                width: '18%',
                align: 'left',
                visible: true
            },{
                field: 'ywmc',
                title: '业务名称',
                width: '18%',
                align: 'left',
                visible: true
            },{
                field: 'spgwmc',
                title: '当前审批岗位',
                width: '14%',
                align: 'left',
                visible:true,               
            },{
                field: 'yqrs',
                title: '延期天数',
                width: '10%',
                align: 'left',
                visible:true,              
            },{
                field: 'yqxs',
                title: '延期小时数',
                width: '10%',
                align: 'left',
                visible:true,              
            },{
                field: 'tjsj',
                title: '提交时间',
                width: '20%',
                align: 'left',
                visible:true,              
            },{
                field: 'shsj',
                title: '审核时间',
                width: '20%',
                align: 'left',
                visible:true,              
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                //todo
            	DaoHuoSpyq_DealById(row.shid,'view',$("#daohuospyq_formSearch #btn_view").attr("tourl"),row.ywid);
             },
		});
        $("#daohuospyq_formSearch #daohuospyq_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true
         });
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "yqrs", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
        };
		return getDaoHuoSpyqSearchData(map);
	};
	return oTableInit;
}


function getDaoHuoSpyqSearchData(map){
	var daohuospyq_select=$("#daohuospyq_formSearch #daohuospyq_select").val();
	var daohuospyq_input=$.trim(jQuery('#daohuospyq_formSearch #daohuospyq_input').val());
	if(daohuospyq_select=="0"){
		map["txlbmc"]=daohuospyq_input
	}
	//提醒类别
	var txlbs= jQuery('#daohuospyq_formSearch #txlb_id_tj').val();
	map["txlbs"] = txlbs;
	return map;
}


//
//function searchDaoHuoSpyqResult(isTurnBack){
//	if(isTurnBack){
//		$('#daohuospyq_formSearch #daohuospyq_list').bootstrapTable('refresh',{pageNumber:1});
//	}else{
//		$('#daohuospyq_formSearch #daohuospyq_list').bootstrapTable('refresh');
//	}
//}
function DaoHuoSpyq_DealById(id,action,tourl,ywid){
	if(!tourl){
		return;
	}
	tourl = $("#daohuospyq_formSearch #urlPrefix").val() + tourl;
	if(action=="view"){
		//todo
		var url=tourl+"?shid="+id+"&ywid="+ywid;
		$.showDialog(url,'查看消息详细信息',viewDaoHuoSpyqConfig);
	}
}

var viewDaoHuoSpyqConfig = {
		width		: "800px",
		modalName	:"viewDaoHuoSpyqModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};


var DaoHuoSpyq_oButton=function(){
	var oButtonInit = new Object();
	var postdata = {};
	oButtonInit.Init=function(){
		var btn_query=$("#daohuospyq_formSearch #btn_query");
		var btn_view = $("#daohuospyq_formSearch #btn_view");
/*-----------------------模糊查询ok------------------------------------*/
//		if(btn_query!=null){
//			btn_query.unbind("click").click(function(){
//				searchDaoHuoSpyqResult(true); 
//    		});
//		}
/*-----------------------查看------------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#daohuospyq_formSearch #daohuospyq_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			DaoHuoSpyq_DealById(sel_row[0].shid,"view",btn_view.attr("tourl"),sel_row[0].ywid);
    		}else{
    			$.error("请选中一行");
    		}
    	});

/*-------------------------------------------------------------*/	
	}
	return oButtonInit;
}


$(function(){
	var oTable = new DaoHuoSpyq_TableInit();
	    oTable.Init();
	
	var oButton = new DaoHuoSpyq_oButton();
	    oButton.Init();
   
//    jQuery('#daohuospyq_formSearch .chosen-select').chosen({width: '100%'});
    
})