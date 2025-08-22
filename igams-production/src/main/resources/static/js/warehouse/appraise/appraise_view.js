var appraiseView_TableInit = function () {
	//初始化Table
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#appraiseViewForm #pjtb_list").bootstrapTable({
			url: $("#appraiseViewForm #urlPrefix").val()+'/appraise/appraise/pagedataGetAppraise',         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#appraiseViewForm #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   //是否显示分页（*）
			paginationShowPageGo: false,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "gfpjmx.wlbm",				//排序字段
			sortOrder: "desc",                   //排序方式
			queryParams: oTableInit.queryParams,//传递参数（*）
			sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
			pageNumber:1,                       //初始化加载第一页，默认第一页
			pageSize: 15,                       //每页的记录行数（*）
			pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
			paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
			search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			showColumns: false,                  //是否显示所有的列
			showRefresh: false,                  //是否显示刷新按钮
			minimumCountColumns: 2,             //最少允许的列数
			clickToSelect: false,                //是否启用点击选中行
			//height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "pjmxid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: [{
				title: '序',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '2%',
				align: 'left',
				visible:true
			}, {
				field: 'wlbm',
				title: '物料编码',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: true,
				formatter:wlbmformat
			}, {
				field: 'wlmc',
				title:'物料名称',
				width: '12%',
				align: 'left',
				sortable: true,
				visible: true,
				formatter:wlmcformat
			},{
				field: 'gg',
				title: '规格',
				width: '12%',
				align: 'left',
				sortable: true,
				visible: true
			},{
                field: 'xmh',
                title: '项目号',
                width: '20%',
                align: 'left',
                sortable: true,
                visible: true
            },{
				field: 'jszb',
				title: '技术指标',
				width: '30%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'zlyq',
				title: '质量要求',
				width: '30%',
				align: 'left',
				sortable: true,
				visible: true
			}],
			onLoadSuccess:function(map){
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
		})
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			pageSize: 1,   // 页面大小
			pageNumber: 1,  // 页码
			access_token:$("#ac_tk").val(),
			sortName: params.sort,      // 排序列名
			sortOrder: params.order, // 排位命令（desc，asc）
			sortLastName: "gfpjmx.wlbm", // 防止同名排位用
			sortLastOrder: "asc", // 防止同名排位用
			gfpjid: $("#appraiseViewForm #gfpjid").val() // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return map;
	};
	return oTableInit
}
function wlmcformat(value,row,index){
    var html="";
    if(!row.wlmc){
        html = row.fwmc;
        if(row.fwmc == null){
            html = "";
        }
    }else{
        html=row.wlmc;
    }
    return html;
}

function wlbmformat(value,row,index){
	var html = "";
	if(row.wlbm==null){
		html += "<span></span>"
	}else{
		html += "<a href='javascript:void(0);' onclick=\"queryByWlbh('"+row.wlid+"')\">"+row.wlbm+"</a>";
	}
	return html;
}
function queryByWlbh(wlid){
	var url=$("#appraiseViewForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'查看页面',viewWlxxConfig);
}
var viewWlxxConfig = {
	width		: "800px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};


function queryByGysid(){
	var url=$("#appraiseViewForm #urlPrefix").val()+"/warehouse/supplier/viewSupplier?gysid="+$("#appraiseViewForm #gysid").val()+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'查看页面',viewGysxxConfig);
}
var viewGysxxConfig = {
	width		: "1400px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};





var yzmxView_TableInit = function () {
	//初始化Table
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#appraiseViewForm #yztb_list").bootstrapTable({
			url: $("#appraiseViewForm #urlPrefix").val()+'/evaluation/evaluation/pagedataQEvaluation',         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#appraiseViewForm #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   //是否显示分页（*）
			paginationShowPageGo: false,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "yzmx.wlbm",				//排序字段
			sortOrder: "desc",                   //排序方式
			queryParams: oTableInit.queryParams,//传递参数（*）
			sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
			pageNumber:1,                       //初始化加载第一页，默认第一页
			pageSize: 15,                       //每页的记录行数（*）
			pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
			paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
			search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			showColumns: false,                  //是否显示所有的列
			showRefresh: false,                  //是否显示刷新按钮
			minimumCountColumns: 2,             //最少允许的列数
			clickToSelect: false,                //是否启用点击选中行
			//height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "yzmxid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: [{
				title: '序',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '2%',
				align: 'left',
				visible:true
			}, {
				field: 'wlbm',
				title: '物料编码',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: true,
				formatter:yzwlbmformat
			}, {
				field: 'wlmc',
				title:'物料名称',
				width: '12%',
				align: 'left',
				sortable: true,
				visible: true,
				formatter:yzwlmcformat
			},{
				field: 'gg',
				title: '规格',
				width: '12%',
				align: 'left',
				sortable: true,
				visible: true
			},{
                field: 'xmh',
                title: '项目号',
                width: '20%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
				field: 'jszb',
				title: '技术指标',
				width: '30%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'zlyq',
				title: '质量要求',
				width: '30%',
				align: 'left',
				sortable: true,
				visible: true
			}],
			onLoadSuccess:function(map){
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
		})
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			pageSize: 1,   // 页面大小
			pageNumber: 1,  // 页码
			access_token:$("#ac_tk").val(),
			sortName: params.sort,      // 排序列名
			sortOrder: params.order, // 排位命令（desc，asc）
			sortLastName: "yzmx.wlbm", // 防止同名排位用
			sortLastOrder: "asc", // 防止同名排位用
			gfyzid: $("#appraiseViewForm #gfyzid").val() // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return map;
	};
	return oTableInit
}
function yzwlmcformat(value,row,index){
    var html="";
    if(!row.wlmc){
        html = row.fwmc;
        if(row.fwmc == null){
            html = "";
        }
    }else{
        html=row.wlmc;
    }
    return html;
}

function yzwlbmformat(value,row,index){
	var html = "";
	if(row.wlbm==null){
		html += "<span></span>"
	}else{
		html += "<a href='javascript:void(0);' onclick=\"queryByWlbh('"+row.wlid+"')\">"+row.wlbm+"</a>";
	}
	return html;
}

$(function(){
	//0.界初始化
	//1.初始化Table
	var oTable = new appraiseView_TableInit();
	oTable.Init();

	var yzmxOTable = new yzmxView_TableInit();
    yzmxOTable.Init();
})