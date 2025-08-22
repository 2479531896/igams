var Recheck_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#recheck_formSearch_daily  #recheck_list").bootstrapTable({
			url: '/ws/ststictis/recheckListDaily',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#recheck_formSearch_daily #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "fjsq.fjid",				//排序字段
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
            uniqueId: "fjid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [
            {
                field: 'ybbh',
                title: '标本编号',
                width: '8%',
                align: 'left',
                visible:false
            },{
                field: 'nbbm',
                title: '内部编码',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'hzxm',
                title: '患者姓名',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'yblxmc',
                title: '标本类型',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'jcxmmc',
                title: '检测项目',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'db',
                title: '合作伙伴',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'lrsj',
                title: '录入时间',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '20%',
                align: 'left',
                formatter:bzFormat,
                visible:true
            },{
                field: 'jcbj',
                title: 'RNA实验',
                width: '8%',
                align: 'left',
                formatter:jcbjformat,
                visible:false
            },{
                field: 'syrq',
                title: 'RNA实验日期',
                width: '8%',
                align: 'left',
                visible:false
            },{
                field: 'djcbj',
                title: 'DNA实验',
                width: '8%',
                align: 'left',
                formatter:djcbjformat,
                visible:false
            },{
                field: 'dsyrq',
                title: 'DNA实验日期',
                width: '8%',
                align: 'left',
                visible:false
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
             },
		});
	}
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "fjsq.fjid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		 map["lx"]=$("#recheck_formSearch_daily #lx").val();
		 map["lrsj"]=$("#recheck_formSearch_daily #lrsj").val();
		 map["xh"]=$("#recheck_formSearch_daily #xh").val();
		 map["lrsjstart"]=$("#recheck_formSearch_daily #lrsjstart").val();
		 map["lrsjend"]=$("#recheck_formSearch_daily #lrsjend").val();
		 map["lrsjMstart"]=$("#recheck_formSearch_daily #lrsjMstart").val();
		 map["lrsjMend"]=$("#recheck_formSearch_daily #lrsjMend").val();
		 map["lrsjYstart"]=$("#recheck_formSearch_daily #lrsjYstart").val();
		 map["lrsjYend"]=$("#recheck_formSearch_daily #lrsjYend").val();
		 map["yhid"]=$("#recheck_formSearch_daily #yhid").val();
		return map;
		};
	return oTableInit;
}
/**
 * 备注格式化
 * @returns
 */
function bzFormat(value,row,index){
	if(row.bz==null){
		return row.yy;
	}else{
		return row.bz;
	}
}

//RNA检测标记格式化
function jcbjformat(value,row,index){
	if(row.jcbj=='0'|| row.jcbj==null){
		var sfjc="<span style='color:red;'>否</span>";
		return sfjc;
	}else if(row.jcbj=='1'){
		return '是';
	}
}

//DNA检测标记格式化
function djcbjformat(value,row,index){
	if(row.djcbj=='0'|| row.djcbj==null){
		var dsfjc="<span style='color:red;'>否</span>";
		return dsfjc;
	}else if(row.djcbj=='1'){
		return '是';
	}
}

function go_backPage(){
	$("#statisticwxyh_daily").load("/ws/ststictis/go_back_dailyByYhid?jsrq="+$("#recheck_formSearch_daily #jsrq").val()+"&yhid="+$("#recheck_formSearch_daily #yhid").val());
}

$(function(){
	// 1.初始化Table
	    var oTable = new Recheck_TableInit();
	    oTable.Init();
	})