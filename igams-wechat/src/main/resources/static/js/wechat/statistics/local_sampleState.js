var Sample_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#local_sample_formSearch  #sample_list").bootstrapTable({
			url: '/ws/sample/sampleStatePage',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#local_sample_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "sjxx.jsrq",				//排序字段
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
                field: 'yblxmc',
                title: '标本类型',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'hzxm',
                title: '患者姓名',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'nl',
                title: '年龄',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'xb',
                title: '性别',
                width: '8%',
                align: 'left',
                formatter:xbformat,
                visible:false
            },{
                field: 'db',
                title: '合作伙伴',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'sjdw',
                title: '医院名称',
                width: '8%',
                align: 'left',
                visible:false
            },{
                field: 'qtks',
                title: '科室',
                width: '8%',
                align: 'left',
                visible:false
            },{
                field: 'jsrq',
                title: '接收日期',
                width: '8%',
                align: 'left',
                visible:true
            },{
                field: 'jcxmmc',
                title: '检测项目',
                width: '8%',
                align: 'left',
                visible:false
            },{
                field: 'bz',
                title: '备注',
                width: '8%',
                align: 'left',
                visible:true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	/*Partner_DealById(row.hbid,'view',$("#recheck_formSearch #recheck_list").attr("tourl"));*/
             },
		});
	}
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "sjxx.sjid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		 map["zt"]=$("#zt").val();
		 map["jsrq"]=$("#jsrq").val();
		 map["ztflg"]=$("#ztflg").val();
		 map["jsrqstart"]=$("#jsrqstart").val();
		 map["jsrqend"]=$("#jsrqend").val();
		 map["jsrqMstart"]=$("#jsrqMstart").val();
		 map["jsrqMend"]=$("#jsrqMend").val();
		 map["jsrqYstart"]=$("#jsrqYstart").val();
		 map["jsrqYend"]=$("#jsrqYend").val();
		return map;
		};
	return oTableInit;
}
//性别格式化
function xbformat(value,row,index){
	if(row.xb=='1'){
		return '男';
	}else if(row.xb=='2'){
		return '女';
	}else{
		return '未知';
	}
}

function percentage(){
	$.ajax({
		url:'/ws/count/getpercentage',
		type:'post',
		data:{"jsrq":$("#jsrq").val(),"zt":$("#zt").val(),"jsrqstart":$("#jsrqstart").val(),"jsrqend":$("#jsrqend").val(),"jsrqMstart":$("#jsrqMstart").val(),"jsrqMend":$("#jsrqMend").val(),"jsrqYstart":$("#jsrqYstart").val(),"jsrqYend":$("#jsrqYend").val()},
		dataType:'JSON',
		success:function(data){
			if(data.sjxxNum!=null && data.ybztNum!=null){
				var num=parseInt(data.ybztNum) / parseInt(data.sjxxNum);
				var str=Number(num*100).toFixed(2);
				str+="%";
				$("#local_sample_formSearch #num").text($("#ybztmc").text()+"标本："+data.ybztNum+"例");
				$("#local_sample_formSearch #tage").text(""+"占比："+str);
			}
		}
	})
}
/**
 * 返回按钮
 * @returns
 */
function ybzt_back_view(){
	$("#weeklyStatis").load("/wechat/statistics/pageListLocal_weekly");
}
$(function(){
// 1.初始化Table
    var oTable = new Sample_TableInit();
    oTable.Init();
    percentage();
})