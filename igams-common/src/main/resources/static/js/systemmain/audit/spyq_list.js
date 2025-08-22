
var AllSpyq_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#allspyq_formSearch #allspyq_list").bootstrapTable({
			url: $("#allspyq_formSearch #urlPrefix").val()+'/systemmain/audit/pageGetListAllSpyq',        // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#allspyq_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "sp.txyqrs",				// 排序字段
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
            uniqueId: "txid",                     // 每一行的唯一标识，一般为主键列
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
                field: 'txlb',
                title: '审核类别',
                width: '20%',
                align: 'left',
                visible:false
            },{
                field: 'txlbmc',
                title: '审核类别名称',
                width: '14%',
                align: 'left',
                visible:true
            },{
                field: 'ywid',
                title: '业务ID',
                width: '18%',
                align: 'left',
                visible: false
            },{
                field: 'ywmc',
                title: '业务名称',
                width: '18%',
                align: 'left',
                visible: true
            },{
                field: 'gwmc',
                title: '当前审批岗位',
                width: '14%',
                align: 'left',
                visible:true,
                formatter:gwformat
            },{
                field: 'txyqrs',
                title: '默认延期小时',
                width: '11%',
                align: 'left',
                visible:true,              
            },{
                field: 'yqrs',
                title: '延期天数',
                width: '10%',
                align: 'left',
                visible:false,              
            },{
                field: 'yqxs',
                title: '延期小时数',
                width: '10%',
                align: 'left',
                visible:true,              
            },{
                field: 'tjrq',
                title: '提交日期',
                width: '20%',
                align: 'left',
                visible:true,              
            },{
                field: 'scshsj',
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
            	AllSpyq_DealById(row.shid,'view',$("#allspyq_formSearch #btn_view").attr("tourl"),row.ywid,row.ywmc);
             },
		});
        $("#allspyq_formSearch #allspyq_list").colResizable({
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
            sortLastName: "yqrs", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getAllSpyqSearchData(map);
	};
	return oTableInit;
}


function getAllSpyqSearchData(map){
	var allspyq_select=$("#allspyq_formSearch #allspyq_select").val();
	var allspyq_input=$.trim(jQuery('#allspyq_formSearch #allspyq_input').val());
	if(allspyq_select=="0"){
		map["txlbmc"]=allspyq_input
	}
	//提醒类别
	var txlbs= jQuery('#allspyq_formSearch #txlb_id_tj').val();
	map["txlbs"] = txlbs;
	return map;
}
/**
 * 岗位名称格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function gwformat(value,row,index){
    var html = "";
    if($('#ac_tk').length > 0){
        html += "<a href='javascript:void(0);' onclick=\"showGwInfo('"+row.gwid+"','"+row.shlb+"','"+row.ywid+"')\">"+row.gwmc+"</a>";
    }else{
        html += row.gwmc;
    }
    return html;
}
function showGwInfo(gwid,shlb,ywid){
    var url=$("#allspyq_formSearch #urlPrefix").val()+"/systemmain/configMember/pagedataViewMember?gwid="+gwid+"&ywid=" + ywid+"&shlb=" + shlb+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'岗位成员信息',viewGWConfig);
}
var viewGWConfig = {
    width		: "1000px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


function searchAllSpyqResult(isTurnBack){
	if(isTurnBack){
		$('#allspyq_formSearch #allspyq_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#allspyq_formSearch #allspyq_list').bootstrapTable('refresh');
	}
}
function AllSpyq_DealById(id,action,tourl,ywid,ywmc){
	if(!tourl){
		return;
	}
	tourl = $("#allspyq_formSearch #urlPrefix").val() + tourl;
	if(action=="view"){
		//todo
		var url=tourl+"?shid="+id+"&ywid="+ywid+"&ywmc="+ywmc
		$.showDialog(url,'查看消息详细信息',viewAllSpyqConfig);
	}
}

var viewAllSpyqConfig = {
		width		: "800px",
		modalName	:"viewAllSpyqModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};


var AllSpyq_oButton=function(){
	var oButtonInit = new Object();
	var postdata = {};
	oButtonInit.Init=function(){
		var btn_query=$("#allspyq_formSearch #btn_query");
		var btn_view = $("#allspyq_formSearch #btn_view");
/*-----------------------模糊查询ok------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchAllSpyqResult(true); 
    		});
		}
/*-----------------------查看------------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#allspyq_formSearch #allspyq_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			AllSpyq_DealById(sel_row[0].shid,"view",btn_view.attr("tourl"),sel_row[0].ywid, sel_row[0].ywmc);
    		}else{
    			$.error("请选中一行");
    		}
    	});

/*-------------------------------------------------------------*/	
	}
	return oButtonInit;
}


$(function(){
	var oTable = new AllSpyq_TableInit();
	    oTable.Init();
	
	var oButton = new AllSpyq_oButton();
	    oButton.Init();
	    
    jQuery('#allspyq_formSearch .chosen-select').chosen({width: '100%'});
    
})