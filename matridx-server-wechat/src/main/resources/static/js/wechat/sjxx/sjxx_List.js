var Inspection_turnOff=true;
    	
var Inspection_TableInit = function (fieldList,firstFlg) {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function (){
        $('#sjxx_formSearch #sjxx_list').bootstrapTable({
            url: '/inspection/inspection/pageGetListInspection',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#sjxx_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "fjsrq desc,sj.sfjs desc,coalesce(sj.dsyrq,sj.syrq) desc,sj.bgrq desc,sj.jsrq desc,sj.lrsj ",//排序字段
            sortOrder:"DESC",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 50,                       //每页的记录行数（*）
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
            columns: fieldList,
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            },
            onDblClickRow: function (row, $element) {
       	        InspectionDealById(row.sjid,null,'view',$("#sjxx_formSearch #btn_view").attr("tourl"));
            },
        });
//        $("#sjxx_formSearch #sjxx_list").colResizable({
//            liveDrag:true, 
//            gripInnerHtml:"<div class='grip'></div>", 
//            draggingClass:"dragging", 
//            resizeMode:'fit', 
//            postbackSafe:true,
//            partialRefresh:true,
//            isFirst:firstFlg}        
//        );
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
        sortLastName: "sj.sjid", // 防止同名排位用
        sortLastOrder: "asc" // 防止同名排位用
        // 搜索框使用
        // search:params.search
    };
    return getSjxxSearchData(map);
	};
    return oTableInit;
}

function getSjxxSearchData(map){
	var cxtj=$("#sjxx_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#sjxx_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["hzxm"]=cxnr
	}else if(cxtj=="1"){
		map["db"]=cxnr
	}else if(cxtj=="2"){
		map["hospitalname"]=cxnr
	}else if(cxtj=="3"){
		map["cskz5"]=cxnr
	}else if(cxtj=="4"){
		map["zyh"]=cxnr
	}else if(cxtj=="5"){
		map["ybbh"]=cxnr
	}else if(cxtj=="6"){
		map["nbbm"]=cxnr
	}else if(cxtj=="7"){
		map["zsxm"]=cxnr
	}else if(cxtj=="8"){
		map["lcfk"]=cxnr
	}else if(cxtj=="9"){
		map["kdh"]=cxnr
	}
	// 接收日期开始日期
	var jsrqstart = jQuery('#sjxx_formSearch #jsrqstart').val();
		map["jsrqstart"] = jsrqstart;
	// 接收日期结束日期
	var jsrqend = jQuery('#sjxx_formSearch #jsrqend').val();
		map["jsrqend"] = jsrqend;
	//科室
	var dwids = jQuery('#sjxx_formSearch #sjdw_id_tj').val();
		map["dwids"] = dwids;
	//标本类型
	var yblxs = jQuery('#sjxx_formSearch #yblx_id_tj').val();
		map["yblxs"] = yblxs;
    //检查项目
	var jcxm=jQuery('#sjxx_formSearch #jcxm_id_tj').val()
		map["jcxms"]=jcxm
	//扩展参数
	var sjkzcs1=jQuery('#sjxx_formSearch #cskz1_id_tj').val()
		map["sjkzcs1"]=sjkzcs1
	var sftj=jQuery('#sjxx_formSearch #sftj_id_tj').val()
		map["sftj"]=sftj;
		//结果	
	var jyjgs=jQuery('#sjxx_formSearch #jyjg_id_tj').val()
		map["jyjgs"]=jyjgs
		//是否合作关系
	var dbm= jQuery('#sjxx_formSearch #dbm_id_tj').val();
			map["dbm"] = dbm;
	//是否付款
	var fkbj=jQuery('#sjxx_formSearch #fkbj_id_tj').val()
		map["fkbj"]=fkbj;
	//是否收费
	var sfsf=jQuery('#sjxx_formSearch #sfsf_id_tj').val()
		map["sfsf"]=sfsf;
	var sfjs=jQuery('#sjxx_formSearch #sfjs_id_tj').val()
		map["sfjs"]=sfjs;
	var sjkzcs2=jQuery('#sjxx_formSearch #cskz2_id_tj').val()
	   map["sjkzcs2"]=sjkzcs2
    var sjkzcs3=jQuery('#sjxx_formSearch #cskz3_id_tj').val()
	   map["sjkzcs3"]=sjkzcs3
    var sjkzcs4=jQuery('#sjxx_formSearch #cskz4_id_tj').val()
	   map["sjkzcs4"]=sjkzcs4
	var sfzq=jQuery('#sjxx_formSearch #sfzq_id_tj').val();
		map["sfzq"]=sfzq
		//快递类型
	var kdlxs=$("#sjxx_formSearch #kdlx_id_tj").val();
		map["kdlxs"]=kdlxs
		//盖章类型
	var gzlxs=$("#sjxx_formSearch #gzlx_id_tj").val();
		map["gzlxs"]=gzlxs
		//检测单位
	var jcdws=$("#sjxx_formSearch #jcdw_id_tj").val();
		map["jcdws"]=jcdws
	 // 报告日期开始日期
	var bgrqstart = jQuery('#sjxx_formSearch #bgrqstart').val();
		map["bgrqstart"] = bgrqstart;
	    // 报告日期结束日期
	var bgrqend = jQuery('#sjxx_formSearch #bgrqend').val();
		map["bgrqend"] = bgrqend;
		// 起运日期开始时间
	var qyrqstart = jQuery('#sjxx_formSearch #qyrqstart').val();
		map["qyrqstart"] = qyrqstart;
	    // 起运日期结束时间
	var qyrqend = jQuery('#sjxx_formSearch #qyrqend').val();
		map["qyrqend"] = qyrqend;
		// 运达日期开始时间
	var ydrqstart = jQuery('#sjxx_formSearch #ydrqstart').val();
		map["ydrqstart"] = ydrqstart;
	    // 运达日期结束时间
	var ydrqend = jQuery('#sjxx_formSearch #ydrqend').val();
		map["ydrqend"] = ydrqend;
		// 实验日期开始日期
	var syrqstart = jQuery('#sjxx_formSearch #syrqstart').val();
		map["syrqstart"] = syrqstart;
	    // 实验日期结束日期
	var syrqend = jQuery('#sjxx_formSearch #syrqend').val();
		map["syrqend"] = syrqend;
	if(jQuery('#sjxx_formSearch #syrqflg').is(":checked")){
		map["syrqflg"] = "1";
	}
	   
	return map;
}

//性别格式化
function ztformat(value,row,index){
	if(row.zt=='00'){
		return "<span style='color:black;font-weight:bold;'>未提交</span>";
	}else if(row.zt=='10'){
		return "<span style='color:blue;font-weight:bold;'>审核中</span>";
	}else if(row.zt=='15'){
		return "<span style='color:red;font-weight:bold;'>审核不通过</span>";
	}else if(row.zt=='80'){
		return "<span style='color:green;font-weight:bold;'>审核通过</span>";
	}else{
		return '--';
	}
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
//RNA检测标记格式化
function jcbjformat(value,row,index){
	if(row.jcxmkzcs=="D" || row.jcxmkzcs=="Z" || row.jcxmkzcs=="F"){
		var sfjc="<span style='color:red;'>--</span>";
		return sfjc;
	}else {
		if(row.jcbj=='0'|| row.jcbj==null){
			var sfjc="<span style='color:red;'>否</span>";
			return sfjc;
		}else if(row.jcbj=='1'){
			return '是';
		}
	}
}
//检验结果格式化
function jyjgformat(value,row,index){
	if(row.jyjg=="1"){
		var jyjg="<span style='color:red;font-weight:bold;'>阳性</span>";
		return jyjg;
	}else if(row.jyjg=="2"){
		var jyjg="<span style='color:#0000cc;font-weight:bold;'>疑似</span>";
		return jyjg;
	}else if(row.jyjg=="0"){
		var jyjg="<span style='font-weight:bold;'>阴性</span>";
		return jyjg;
	}
}
//DNA检测标记格式化
function djcbjformat(value,row,index){
	if(row.jcxmkzcs=="R" || row.jcxmkzcs=="Z" || row.jcxmkzcs=="F"){
		var dsfjc="<span style='color:red;'>--</span>";
		return dsfjc;
	}else{
		if(row.djcbj=='0'|| row.djcbj==null){
			var dsfjc="<span style='color:red;'>否</span>";
			return dsfjc;
		}else if(row.djcbj=='1'){
			return '是';
		}
	}
}
//自免检测标记格式化
function qtjcbjformat(value,row,index){
	if(row.jcxmkzcs!="Z" && row.jcxmkzcs!="F"){
		var qtsfjc="<span style='color:red;'>--</span>";
		return qtsfjc;
	}else{
		if(row.qtjcbj=='0'|| row.qtjcbj==null){
			var qtsfjc="<span style='color:red;'>否</span>";
			return qtsfjc;
		}else if(row.qtjcbj=='1'){
			return '是';
		}
	}
}
function sfjsformat(value,row,index){
	if(row.sfjs=='0'){
		var sfjs="<span style='color:red;'>否</span>";
		return sfjs;
	}else if(row.sfjs=='1'){
		return '是';
	}
}
function sftjformat(value,row,index){
	if(row.sftj=='0'){
		var sftj="<span style='color:red;'>否</span>";
		return sftj;
	}else if(row.sftj=='1'){
		return '是';
	}
}
//付款标记格式化
function fkbjformat(value,row,index){
	if(row.fkbj=='1'){
		return '是';
		
	}else{
		return '否';
	}
}
//是否收費格式化
function sfsfformat(value,row,index){
	if(row.sfsf=='1'){
		return '是';
	}else if(row.sfsf=='0'){
		var sfsf="<span style='color:red;'>否</span>"
		return sfsf;
	}else if(row.sfsf=='2'){
		var sfsf="<span style='color:#8E388E;font-weight:bold'>免            D</span>"
		return sfsf;
	}else if(row.sfsf=='3'){
		var sfsf="<span style='color:#8470FF;font-weight:bold'>免         R</span>"
		return sfsf;
	}
}
	//是否上传格式化
function wjformat(value,row,index){
	if(row.sfsc=="1"){
		var sfsc="<span>是</span>"
		return sfsc;
	}else{
		var sfsc="<span style='color:red;'>否</span>"
		return sfsc;
	}
}

/**
 * 反馈结果格式化
 */
function lcfkformat(value,row,index){
	if(row.sfzq=="1"){
		var sfzq="<span>正确</span>"
			return sfzq;
	}else if(row.sfzq=="0"){
		var sfzq="<span style='color:red;'>错误</span>"
			return sfzq;
	}else{
		var sfzq="<span'>未反馈</span>"
			return sfzq;
	}
}

/**
 * 检测项目格式化
 */
function jcxmformat(value,row,index){
	/*if(row.jcxmkzcs=="D"){
		return null;
	}else{*/
		return row.jcxmmc;
	//}
}

    // 按钮动作函数
function InspectionDealById(id,ks,action,tourl){
	var url= tourl;
	if(!tourl){
		return;
	}else if(action =='view'){
		var url= tourl + "?sjid=" +id +"&ks=" +ks;
		$.showDialog(url,'送检详细信息',	viewSjxxConfig);
	}
}
/*查看送检信息模态框*/
var viewSjxxConfig = {
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

var Inspection_ButtonInit = function(){
    	var oInit = new Object();
    	var postdata = {};
    	oInit.Init = function () {
//        初始化页面上面的按钮事件
   	    var btn_view = $("#sjxx_formSearch #btn_view");//
    	var btn_query =$("#sjxx_formSearch #btn_query");//模糊查询
    	
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #jsrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #jsrqend'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #bgrqstart'
		   ,type: 'date'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #bgrqend'
		   ,type: 'date'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #qyrqstart'
    	   ,type: 'datetime'
		   ,ready: function(date){
				if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
					var myDate = new Date(); //实例一个时间对象；
					this.dateTime.hours=myDate.getHours();
					this.dateTime.minutes=myDate.getMinutes();
		        	this.dateTime.seconds=myDate.getSeconds();
				}
   	    	}
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #qyrqend'
    	   ,type: 'datetime'
		   ,ready: function(date){
				if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
					var myDate = new Date(); //实例一个时间对象；
					this.dateTime.hours=myDate.getHours();
					this.dateTime.minutes=myDate.getMinutes();
		        	this.dateTime.seconds=myDate.getSeconds();
				}
		   }
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #ydrqstart'
    	   ,type: 'datetime'
		   ,ready: function(date){
				if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
					var myDate = new Date(); //实例一个时间对象；
					this.dateTime.hours=myDate.getHours();
					this.dateTime.minutes=myDate.getMinutes();
		        	this.dateTime.seconds=myDate.getSeconds();
				}
		   }
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #ydrqend'
    	   ,type: 'datetime'
		   ,ready: function(date){
				if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
					var myDate = new Date(); //实例一个时间对象；
					this.dateTime.hours=myDate.getHours();
					this.dateTime.minutes=myDate.getMinutes();
		        	this.dateTime.seconds=myDate.getSeconds();
				}
		    }
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #syrqstart'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #syrqend'
    	});
    	
    	if(btn_query!=null){
    		btn_query.unbind("click").click(function(){
    			searchSjxxResult(true); 
    		});
    	}; 
    	
    	/**显示隐藏**/      
    	$("#sjxx_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(Inspection_turnOff){
    			$("#sjxx_formSearch #searchMore").slideDown("low");
    			Inspection_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#sjxx_formSearch #searchMore").slideUp("low");
    			Inspection_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});
		/*---------------------------查看送检信息表-----------------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				InspectionDealById(sel_row[0].sjid,sel_row[0].ks,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*--------------------------------------------------------------------------*/
    };
    return oInit;
};

function searchSjxxResult(isTurnBack){
	//关闭高级搜索条件
	$("#sjxx_formSearch #searchMore").slideUp("low");
	Inspection_turnOff=true;
	$("#sjxx_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#sjxx_formSearch #sjxx_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#sjxx_formSearch #sjxx_list').bootstrapTable('refresh');
	}
}

//$("#sjxx_formSearch #cxnr").on('keydown',function(e){
//	if(e.keyCode==74 && $("#cxnr").val().length >=9){
//		return false;
//	}
//});
//


$(function(){
    // 1.初始化Table
    var oTable = new Inspection_TableInit(sjxxFieldList,true);
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new Inspection_ButtonInit();
    oButtonInit.Init();
//	runEvery10Sec();
	// 所有下拉框添加choose样式
    jQuery('#sjxx_formSearch .chosen-select').chosen({width: '100%'});
	// 初始绑定显示更多的事件
	$("#sjxx_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
});




