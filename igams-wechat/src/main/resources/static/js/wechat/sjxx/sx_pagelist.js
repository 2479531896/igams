var Sjxxsxb_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		var ybbh=$("#ybbh").val();
		$("#OtherSjxxPage_formSearch #tb_list").bootstrapTable({
			url: '/ws/getsjxxpage?ybbh='+ybbh,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#OtherSjxxPage_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "sjid",				//排序字段
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
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "sjid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'sjid',
                title: '送检id',
                width: '10%',
                align: 'left',
                titleTooltip:'送检id',
                visible:false
            },{
                field: 'ybbh',
                title: '标本编号',
                width: '10%',
                align: 'left',
                titleTooltip:'标本编号',
                visible: true
            },{
                field: 'hzxm',
                title: '患者姓名',
                width: '10%',
                align: 'left',
                titleTooltip:'患者姓名',
                visible: true
            },{
                field: 'xb',
                title: '性别',
                width: '6%',
                formatter:xbformat,
                align: 'left',
                titleTooltip:'性别',
                visible:true
            },{
                field: 'nl',
                title: '年龄',
                width: '6%',
                align: 'left',
                titleTooltip:'年龄',
                visible: true
            },{
                field: 'dh',
                title: '电话',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'sjdw',
                title: '医院名称',
                width: '10%',
                align: 'left',
                titleTooltip:'医院名称',
                visible: true
            },{
                field: 'ksmc',
                title: '科室',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'sjys',
                title: '主治医师',
                width: '5%',
                align: 'left',
                titleTooltip:'主治医师',
                visible: false
            },{
                field: 'db',
                title: '合作伙伴',
                width: '7%',
                align: 'left',
                titleTooltip:'合作伙伴',
                visible: true
            },{
                field: 'yblxmc',
                title: '标本类型',
                width: '7%',
                align: 'left',
                titleTooltip:'标本类型',
                visible: true
            },{
                field: 'cyrq',
                title: '采样日期',
                width: '10%',
                align: 'left',
                titleTooltip:'采样日期',
                visible: true
            },{
                field: 'jsrq',
                title: '接收日期',
                width: '10%',
                align: 'left',
                titleTooltip:'接收日期',
                visible: true
            },{
                field: 'bgrq',
                title: '报告日期',
                width: '10%',
                align: 'left',
                titleTooltip:'报告日期',
                visible: true
            }],
            onLoadSuccess:function(row){
            	if(row.error!=null){
            		alert(row.error);
            	}
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	Sjxxsxb_DealById(row.sjid,'view','/ws/sjxxpageview',row.ybbh);
             },
		});
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
	        sortLastName: "sjid", // 防止同名排位用
	        sortLastOrder: "asc" // 防止同名排位用
	        // 搜索框使用
	        // search:params.search
	    };
	    return getSxblistSearchData(map);
		};
	return oTableInit;
}

function xbformat(value,row,index){
	if(row.xb=='1'){
		return '男';
	}else if(row.xb=='2'){
		return '女';
	}else{
		return '未知';
	}
}

function getSxblistSearchData(map){
	var cxtj=$("#OtherSjxxPage_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#OtherSjxxPage_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["sjdw"]=cxnr;
	}else if(cxtj=="1"){
		map["sjys"]=cxnr;
	}else if(cxtj=="2"){
		map["yblxmc"]=cxnr;
	}
	return map;
}
function Sjxxsxb_DealById(id,action,tourl,ybbh){
	if(!tourl){
		return;
	}
	if(action=="view"){
		var url=tourl+"?ids="+id+"&&ybbhs="+ybbh;
		$.showDialog(url,'查看详细信息',viewSjxxsxbConfig);
	}
}
var Sjxxsxb_oButtton= function (){
		var oInit=new Object();
		var postdata = {};
		oInit.Init=function(){
			var btn_query=$("#OtherSjxxPage_formSearch #btn_query");
	/*-----------------------模糊查询------------------------------------*/
			if(btn_query!=null){
				btn_query.unbind("click").click(function(){
					searchSjxxsxbResult(true); 
	    		});
			}
		}
	 	return oInit;
}

//查看多条
function getViewPage(){
	var sel_row = $('#OtherSjxxPage_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
	if(sel_row.length ==0){
		$.error("请至少选中一行");
		return;
	}
	var ids="";
	var ybbhs="";
	for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
		ybbhs = ybbhs + ","+ sel_row[i].ybbh;
		ids = ids + ","+ sel_row[i].sjid;
	}
	ybbhs = ybbhs.substr(1);
	ids = ids.substr(1);
	Sjxxsxb_DealById(ids,'view','/ws/sjxxpageview',ybbhs);
}
var viewSjxxsxbConfig = {
		width		: "800px",
		modalName	:"viewPartnerModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
function searchSjxxsxbResult(isTurnBack){
	if(isTurnBack){
		$('#OtherSjxxPage_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#OtherSjxxPage_formSearch #tb_list').bootstrapTable('refresh');
	}
}

$(function(){
	var oTable=new Sjxxsxb_TableInit();
		oTable.Init();
	//2.初始化Button的点击事件
    var oButtonInit = new Sjxxsxb_oButtton();
    	oButtonInit.Init();
    	jQuery('#OtherSjxxPage_formSearch .chosen-select').chosen({width: '100%'});
})