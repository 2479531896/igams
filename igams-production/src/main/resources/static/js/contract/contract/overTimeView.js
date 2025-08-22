var overTime_TableInit = function () {
    //初始化Table
    var flg=$("#contractOverTable_Form #flg").val();
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#contractOverTable_Form #contractOverTable_view").bootstrapTable({
			url: $("#contractOverTable_Form #urlPrefix").val()+'/contract/contract/pagedataGetOverTable?flg='+flg,         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#contractOverTable_Form #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   //是否显示分页（*）
			paginationShowPageGo: false,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "htgl.timeLag",				//排序字段
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
			clickToSelect: true,                //是否启用点击选中行
			//height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "htid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: [{
				title: '序',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '3%',
				align: 'left',
				visible:true
			},{
				field: 'djh',
				title: '请购单号',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'sqrmc',
				title: '申请人',
				titleTooltip:'申请人',
				width: '5%',
				align: 'left',
				visible: true
			},{
				field: 'sqbmmc',
				title: '申请部门',
				width: '8%',
				align: 'left',
				visible: true
			},{
                field: 'qgfzrmc',
                title: '请购负责人',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'htnbbh',
                title: '合同内部编号',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'fzrmc',
                title: '合同负责人',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'gysmc',
                title: '供应商',
                width: '12%',
                align: 'left',
                visible: true
            },{
				field: 'qgshsj',
				title: '请购审核通过时间',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'timeLag',
				title: '时间(单位:小时)',
				width: '10%',
				align: 'left',
				visible: true
			},{
                field: 'sqly',
                title: '申请理由',
                width: '15%',
                align: 'left',
                visible: true
            },{
                field: 'zt',
                title: '状态',
                width: '15%',
                align: 'left',
                formatter:ztFormat,
                visible: true
            }],
			onLoadSuccess:function(map){

			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
                $.showDialog($("#contractOverTable_Form #urlPrefix").val()+"/contract/contract/pagedataQueryOverTableMx?qgid="+row.qgid+"&htid="+row.htid+"&access_token="+$("#ac_tk").val(),"采购物料信息",OverTimeViewConfig);
            },
		});
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			pageSize: 1,   // 页面大小
			pageNumber: 1,  // 页码
			access_token:$("#ac_tk").val(),
			sortName: params.sort,      // 排序列名
			sortOrder: params.order, // 排位命令（desc，asc）
			sortLastName: "htgl.timeLag", // 防止同名排位用
			sortLastOrder: "desc" // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return map;
	};
	return oTableInit
}

var OverTimeViewConfig = {
	width		: "1500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function ztFormat(value,row,index) {
    if(row.zt!=null && row.zt!=""){
    	if (row.zt == '00') {
            return '未提交';
        }else if (row.zt == '80'){
    		return row.htlx=='1'?"<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htid + "\",event,\"AUDIT_CONTRACT_OA\",{prefix:\"" + $('#contractOverTable_Form #urlPrefix').val() + "\"})' >审核通过</a>":
    			"<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htid + "\",event,\"AUDIT_CONTRACT\",{prefix:\"" + $('#contractOverTable_Form #urlPrefix').val() + "\"})' >审核通过</a>";
        }else if (row.zt == '15') {
    		return row.htlx=='1'?"<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htid + "\",event,\"AUDIT_CONTRACT_OA\",{prefix:\"" + $('#contractOverTable_Form #urlPrefix').val() + "\"})' >审核未通过</a>":
    			"<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htid + "\",event,\"AUDIT_CONTRACT\",{prefix:\"" + $('#contractOverTable_Form #urlPrefix').val() + "\"})' >审核未通过</a>";
        }else{
            var shr = row.shxx_dqgwmc;
            if(row.dqshr!=null && row.dqshr!=""){
                shr = row.dqshr;
            }
    		return row.htlx=='1'?"<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htid + "\",event,\"AUDIT_CONTRACT_OA\",{prefix:\"" + $('#contractOverTable_Form #urlPrefix').val() + "\"})' >" + shr + "审核中</a>":
    			"<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.htid + "\",event,\"AUDIT_CONTRACT\",{prefix:\"" + $('#contractOverTable_Form #urlPrefix').val() + "\"})' >" + shr + "审核中</a>";
        }
    }
}
$(function(){
	var oTable = new overTime_TableInit();
	oTable.Init();
})