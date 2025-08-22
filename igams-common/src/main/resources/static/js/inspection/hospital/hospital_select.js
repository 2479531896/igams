var Hospital_TableInit=function(){
	var url=null;
	if($("#hospital_formSearch #out_token").val()){
		url="/wechat/hospital/pagedataListHospital?access_token="+$("#hospital_formSearch #out_token").val();
	}else{
		url="/wechat/hospital/pagedataListHospital?access_token="+$("#ac_tk").val();
	}
	 var oTableInit = new Object();
	 oTableInit.Init = function (){
		 $('#hospital_formSearch #hospital_list').bootstrapTable({
			 	url: url,         //请求后台的URL（*）
	            method: 'get',                      //请求方式（*）
	            toolbar: '#hospital_formSearch #toolbar',                //工具按钮用哪个容器
	            striped: true,                      //是否显示行间隔色
	            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
	            pagination: true,                   //是否显示分页（*）
	            paginationShowPageGo: true,         //增加跳转页码的显示
	            sortable: true,                     //是否启用排序
	            sortName: "yyxx.lrsj",				//排序字段
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
	            uniqueId: "dwid",                     //每一行的唯一标识，一般为主键列
	            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
	            cardView: false,                    //是否显示详细视图
	            detailView: false,                   //是否显示父子表
	            /*rowStyle:rowStyle,*/               //通过自定义函数设置行样式
	            columns: [{
	                  radio: true,
	                  width: '3%'
	            },{
	                field: 'dwid',
	                title: '单位ID',
	                width: '10%',
	                align: 'left',
	                visible:false
	            },{
	                field: 'dwmc',
	                title: '单位名称',
	                width: '20%',
	                align: 'left',
	                visible:true
	            },{
	                field: 'dwjc',
	                title: '单位简称',
	                width: '20%',
	                align: 'left',
	                visible:true
	            },{
	                field: 'dwqtmc',
	                title: '单位其他名称',
	                width: '20%',
	                align: 'left',
	                visible:false
	            },{
	                field: 'tjmc',
	                title: '单位统计名称',
	                width: '20%',
	                align: 'left',
	                visible:false
	            },{
	                field: 'dwdjmc',
	                title: '单位等级',
	                width: '20%',
	                align: 'left',
	                visible:true
	            },{
	                field: 'dwlbmc',
	                title: '单位类别',
	                width: '20%',
	                align: 'left',
	                visible:true
	            },{
	                field: 'sfmc',
	                title: '省份',
	                width: '20%',
	                align: 'left',
	                visible:true
	            },{
	                field: 'csmc',
	                title: '城市',
	                width: '20%',
	                align: 'left',
	                visible:true
	            },{
	                field: 'cs',
	                title: '城市',
	                width: '20%',
	                align: 'left',
	                visible:false
	            },{
	                field: 'cskz1',
	                title: '参数扩展1',
	                width: '20%',
	                align: 'left',
	                visible:false
	            },{
	                field: 'cskz2',
	                title: '参数扩展2',
	                width: '20%',
	                align: 'left',
	                visible:false
	            },{
	                field: 'cskz3',
	                title: '参数扩展3',
	                width: '20%',
	                align: 'left',
	                visible:false
	            }],
	            onLoadSuccess:function(){
	            },
	            onLoadError:function(){
	            	alert("数据加载失败！");
	            },
		 });
		 $("#hospital_formSearch #hospital_list").colResizable({
			 liveDrag:true, 
			 gripInnerHtml:"<div class='grip'></div>", 
			 draggingClass:"dragging", 
			 resizeMode:'fit', 
			 postbackSafe:true,
			 partialRefresh:true}    
	        );
	 }
	 oTableInit.queryParams=function(params){
			var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
	        	pageSize: params.limit,   // 页面大小
	        	pageNumber: (params.offset / params.limit) + 1,  // 页码
	            sortName: params.sort,      // 排序列名
	            sortOrder: params.order, // 排位命令（desc，asc）
	            sortLastName: "yyxx.dwid", // 防止同名排位用
	            sortLastOrder: "asc" // 防止同名排位用
	            // 搜索框使用
	            // search:params.search
	        };
			return getHospitalSearchData(map);
		};
		return oTableInit
}

function getHospitalSearchData(map){
	var cxnr=$.trim(jQuery('#hospital_formSearch #cxnr').val());
	map["searchParam"]=cxnr
	var dwFlag=$("#hospital_formSearch #dwFlag").val();
	map["dwFlag"]=dwFlag
	return map
}

function searchHospitaleResult(isTurnBack){
	if(isTurnBack){
		$('#hospital_formSearch #hospital_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#hospital_formSearch #hospital_list').bootstrapTable('refresh');
	}
}

$(function(){
	 var oTable = new Hospital_TableInit();
	 	oTable.Init();
})