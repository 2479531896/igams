var Inspection_turnOff=true;
    	
var Inspection_TableInit = function (fieldList,firstFlg) {
    var oTableInit = new Object();
    //初始化Table 
    oTableInit.Init = function (){
        $('#sjhbview_formSearch #sjxx_list').bootstrapTable({
            url: '/inspection/inspection/pageGetListSjhb',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#sjhbview_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "sj.jsrq DESC,sj.jcbj ASC,sj.bgrq ASC,sj.lrsj",				//排序字段
            sortOrder: "DESC",                   //排序方式
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
            	alert("数据加载失败！");
            },
           onDblClickRow: function (row, $element) {
        	   InspectionDealById(row.sjid,null,'view',$("#sjhbview_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#sjhbview_formSearch #sjxx_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true,
            isFirst:firstFlg}        
        );
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
            sortLastOrder: "asc", // 防止同名排位用
			limitColumns: $("#limitColumns").val()
            // 搜索框使用
            // search:params.search
        };
        return getSjxxSearchData(map);
    	};
    return oTableInit;
}

function getSjxxSearchData(map){
	var cxtj=$("#sjhbview_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#sjhbview_formSearch #cxnr').val());
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
		map["kdh"]=cxnr
	}
	// 付款日期开始时间
	var fkrqstart = jQuery('#sjhbview_formSearch #fkrqstart').val();
	map["fkrqstart"] = fkrqstart;
	// 付款日期结束时间
	var fkrqend = jQuery('#sjhbview_formSearch #fkrqend').val();
	map["fkrqend"] = fkrqend;
		// 通过开始日期
	var jsrqstart = jQuery('#sjhbview_formSearch #jsrqstart').val();
		map["jsrqstart"] = jsrqstart;
	 // 通过结束日期
	var jsrqend = jQuery('#sjhbview_formSearch #jsrqend').val();
		map["jsrqend"] = jsrqend;
	// 起运日期开始时间
	var qyrqstart = jQuery('#sjhbview_formSearch #qyrqstart').val();
		map["qyrqstart"] = qyrqstart;
	    // 起运日期结束时间
	var qyrqend = jQuery('#sjhbview_formSearch #qyrqend').val();
		map["qyrqend"] = qyrqend;
		// 运达日期开始时间
	var ydrqstart = jQuery('#sjhbview_formSearch #ydrqstart').val();
		map["ydrqstart"] = ydrqstart;
	    // 运达日期结束时间
	var ydrqend = jQuery('#sjhbview_formSearch #ydrqend').val();
		map["ydrqend"] = ydrqend;
	//科室
	var dwids = jQuery('#sjhbview_formSearch #sjdw_id_tj').val();
		map["dwids"] = dwids;
	//标本类型
	var yblxs = jQuery('#sjhbview_formSearch #yblx_id_tj').val();
		map["yblxs"] = yblxs;	
	//是否合作关系
	var dbm= jQuery('#sjhbview_formSearch #dbm_id_tj').val();
		map["dbm"] = dbm;
	//是否付款
	var fkbj=jQuery('#sjhbview_formSearch #fkbj_id_tj').val()
		map["fkbj"]=fkbj;
	//是否收费
	var sfsf=jQuery('#sjhbview_formSearch #sfsf_id_tj').val()
		map["sfsf"]=sfsf;
	var sfjs=jQuery('#sjhbview_formSearch #sfjs_id_tj').val()
		map["sfjs"]=sfjs;
	// var sftj=jQuery('#sjhbview_formSearch #sftj_id_tj').val()
	// 	map["sftj"]=sftj;
	//扩展参数
	var sjkzcs1=jQuery('#sjhbview_formSearch #cskz1_id_tj').val()
		map["sjkzcs1"]=sjkzcs1
	var sjkzcs2=jQuery('#sjhbview_formSearch #cskz2_id_tj').val()
		map["sjkzcs2"]=sjkzcs2
	var sjkzcs3=jQuery('#sjhbview_formSearch #cskz3_id_tj').val()
		map["sjkzcs3"]=sjkzcs3
	// var sjkzcs4=jQuery('#sjhbview_formSearch #cskz4_id_tj').val()
	// 	map["sjkzcs4"]=sjkzcs4
	var jcxm=jQuery('#sjhbview_formSearch #jcxm_id_tj').val()
		map["jcxm"]=jcxm
	// var jyjgs=jQuery('#sjhbview_formSearch #jyjg_id_tj').val()
	// 	map["jyjgs"]=jyjgs
		//快递类型
	var kdlxs=$("#sjhbview_formSearch #kdlx_id_tj").val();
		map["kdlxs"]=kdlxs
		//检测单位
	// var jcdws=$("#sjhbview_formSearch #jcdw_id_tj").val();
	// 	map["jcdws"]=jcdws
	   // 分类
	var sjhbfls = jQuery('#sjhbview_formSearch #sjhbfl_id_tj').val();
	map["sjhbfls"] = sjhbfls;
	   // 子分类
	var sjhbzfls = jQuery('#sjhbview_formSearch #sjhbzfl_id_tj').val();
	map["sjhbzfls"] = sjhbzfls;
	return map;
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
//检验结果格式化
function jyjgformat(value,row,index){
	if(row.jyjg=="1"){
		var jyjg="<span style='color:red;font-weight:bold;'>阳性</span>";
		return jyjg;
	}else if(row.jyjg=="2"){
		var jyjg="<span font-weight:bold;'>疑似</span>";
		return jyjg;
	}else if(row.jyjg=="0"){
		var jyjg="<span font-weight:bold;'>阴性</span>";
		return jyjg;
	}
}
/**
 * 反馈结果格式化
 * @param value
 * @param row
 * @param index
 * @returns
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

function sfvipformat(value,row,index){
	if(row.sfvip=='0'){
		var sfvip="<span style='color:red;'>否</span>";
		return sfvip;
	}else if(row.sfvip=='1'){
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
	if(row.sfsc!=null){
		var sfsc="<span>是</span>"
		return sfsc;
	}else{
		var sfsc="<span style='color:red;'>否</span>"
		return sfsc;
	}
}
//检测项目格式化
function jcxmformat(value,row,index){
	if(row.jcxmkzcs=="D"){
		return null;
	}else{
		return row.jcxmmc;
	}
}
    // 按钮动作函数
function InspectionDealById(id,ks,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?sjid=" +id +"&ks=" +ks;
		$.showDialog(url,'送检详细信息',	viewSjxxConfig);
	}else if(action=="reportdownload"){
    	var url=tourl + "?ids=" +id;
    	$.showDialog(url,'报告下载',reportDownloadConfig);
    }else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'新增送检信息',addSjxxFromSjhbPageConfig);
	}
}

var Inspection_ButtonInit = function(){
    	var oInit = new Object();
    	var postdata = {};
    	oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_view = $("#sjhbview_formSearch #btn_view");//查看
    	var btn_query =$("#sjhbview_formSearch #btn_query");//模糊查询
    	var btn_selectexport = $("#sjhbview_formSearch #btn_selectexport");//选中导出
    	var btn_searchexport = $("#sjhbview_formSearch #btn_searchexport");//搜索导出
    	var initTableField_sjhb = $("#sjhbview_formSearch #initTableField_sjhb");//字段选择
    	var btn_reportdownload = $("#sjhbview_formSearch #btn_reportdownload");//报告下载
    	var sjhbflBind = $("#sjhbview_formSearch #sjhbfl_id ul li a");
		var btn_add = $("#sjhbview_formSearch #btn_add");//新增
		var btn_discard = $("#sjhbview_formSearch #btn_discard");//废弃

		//添加日期控件
		laydate.render({
			elem: '#fkrqstart'
			,theme: '#2381E9'
		});
		//添加日期控件
		laydate.render({
			elem: '#fkrqend'
			,theme: '#2381E9'
		});
    	//添加日期控件
    	laydate.render({
    	   elem: '#jsrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#jsrqend'
    	  ,theme: '#2381E9'
    	});
    	if(btn_query!=null){
    		btn_query.unbind("click").click(function(){
    			searchSjxxResult(true); 
    		});
    	};
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjhbview_formSearch #qyrqstart'
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
    	   elem: '#sjhbview_formSearch #qyrqend'
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
    	   elem: '#sjhbview_formSearch #ydrqstart'
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
    	   elem: '#sjhbview_formSearch #ydrqend'
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
		/* ------------------------------废弃送检信息-----------------------------*/
			btn_discard.unbind("click").click(function(){
				var sel_row = $('#sjhbview_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==0){
					$.error("请至少选中一行");
					return;
				}else {
					var ids="";
					for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
						if(sel_row[i].jsrq ){
							$.error("标本编号为"+sel_row[i].ybbh+ "的数据已被接收，不允许废弃");
							return
						}
						ids= ids + ","+ sel_row[i].sjid;
					}
					ids=ids.substr(1);
					$.confirm('您确定要废弃所选择的信息吗？',function(result){
						if(result){
							jQuery.ajaxSetup({async:false});
							var url= btn_discard.attr("tourl");
							jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
								setTimeout(function(){
									if(responseText["status"] == 'success'){
										$.success(responseText["message"],function() {
											searchSjxxResult();
										});
									}else if(responseText["status"] == "fail"){
										$.error(responseText["message"],function() {
										});
									} else{
										$.alert(responseText["message"],function() {
										});
									}
								},1);

							},'json');
							jQuery.ajaxSetup({async:true});
						}
					});
				}
			});
		/* ------------------------------添加送检信息-----------------------------*/
			btn_add.unbind("click").click(function(){
				InspectionDealById(null,null,"add",btn_add.attr("tourl"));
			});
        /*---------------------------查看送检信息表-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#sjhbview_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			InspectionDealById(sel_row[0].sjid,sel_row[0].ks,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
    	if(initTableField_sjhb!=null){
    		initTableField_sjhb.unbind("click").click(function(){
    			$.showDialog(titleSelectUrl+"?ywid=PARTNER"
        				,"列表字段设置", $.extend({},setSjxxListFieldsConfig,{"width":"1020px"}));
    		});
    	}
    	//---------------------------------选中导出---------------------------------------
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#sjhbview_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].sjid;
        		}
        		ids = ids.substr(1);
        		$.showDialog(exportPrepareUrl+"?ywid=INSPECT_CLINIC_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});
    	
    	//------------------------------------- 报告下载 ---------------------------------------
    	btn_reportdownload.unbind("click").click(function(){
    		//判断有选中的采用选中导出，没有采用选择导出
    		var sel_row = $('#sjhbview_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].sjid;
    		}
    		ids = ids.substr(1);
    		InspectionDealById(ids,null,"reportdownload",btn_reportdownload.attr("tourl"));
    	});
    	
    	//搜索导出
    	btn_searchexport.unbind("click").click(function(){
    		$.showDialog(exportPrepareUrl+"?ywid=INSPECT_CLINIC_SEARCH&expType=search&callbackJs=inspectSearchData"
    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});
    	
    	/**显示隐藏**/      
    	$("#sjhbview_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(Inspection_turnOff){
    			$("#sjhbview_formSearch #searchMore").slideDown("low");
    			Inspection_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#sjhbview_formSearch #searchMore").slideUp("low");
    			Inspection_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
    	
    	//绑定分类的单击事件
    	if(sjhbflBind!=null){
    		sjhbflBind.on("click", function(){
    			setTimeout(function(){
    				getZfls();
    			}, 10);
    		});
    	}
    	
    };
    return oInit;
};

//子分类的取得
function getZfls() {
	// 分类
	var fl = jQuery('#sjhbview_formSearch #sjhbfl_id_tj').val();
	if (!isEmpty(fl)) {
		fl = "'" + fl + "'";
		jQuery("#sjhbview_formSearch #sjhbzfl_id").removeClass("hidden");
	}else{
		jQuery("#sjhbview_formSearch #sjhbzfl_id").addClass("hidden");
	}
	// 项目类别不为空
	if (!isEmpty(fl)) {
		var url = "/systemmain/data/ansyGetJcsjListInStop";
		$.ajax({
			type : "POST",
			url : url,
			data : {"fcsids":fl,"access_token":$("#ac_tk").val()},
			dataType : "json",
			success : function(data){
				if(data.length > 0) {
					var html = "";
					$.each(data,function(i){
						html += "<li>";
						html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('sjhbzfl','" + data[i].csid + "','sjhbview_formSearch');\" id=\"sjhbzfl_id_" + data[i].csid + "\"><span>" + data[i].csmc + "</span></a>";
						html += "</li>";
					});
					jQuery("#sjhbview_formSearch #ul_sjhbzfl").html(html);
					jQuery("#sjhbview_formSearch #ul_sjhbzfl").find("[name='more']").each(function(){
						$(this).on("click", s_showMoreFn);
					});
				} else {
					jQuery("#sjhbview_formSearch #ul_sjhbzfl").empty();
					
				}
				jQuery("#sjhbview_formSearch [id^='sjhbzfl_li_']").remove();
				$("#sjhbview_formSearch #sjhbzfl_id_tj").val("");
			}
		});
	} else {
		jQuery("#sjhbview_formSearch #div_sjhbzfl").empty();
		jQuery("#sjhbview_formSearch [id^='sjhbzfl_li_']").remove();
		$("#sjhbview_formSearch #sjhbzfl_id_tj").val("");
	}
}

//提供给导出用的回调函数
function inspectSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="sj.sjid";
	map["sortLastOrder"]="asc";
	map["sortName"]="sj.lrsj";
	map["sortOrder"]="asc";
	map["yhid"]=$("#yhid").val();
	return getSjxxSearchData(map);
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

var reportDownloadConfig={
		width		: "800px",
		modalName	: "reportDownloadConfig",
		formName	: "recheck_Form",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		:	{
		success 	: 	{
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var bglxbj = $("#ajaxForm input[type='radio']:checked").val();
	    		var map = {
	    			access_token:$("#ac_tk").val(),
	    			bglxbj:bglxbj,
	    		}; 
	    		//判断有选中的采用选中导出，没有采用选择导出
	    		var sel_row = $('#sjhbview_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
	    		var ids="";
	    		if(sel_row.length>=1){
	        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
	        			ids = ids + ","+ sel_row[i].sjid;
	        		}
	        		ids = ids.substr(1);
	        		map["ids"] = ids;
	    		}else{
	    			getSjxxSearchData(map);
	    		}
	    		var url= $("#ajaxForm #action").val();
	    		map["flg"] = $("#ajaxForm #flg").val();
				$.post(url,map,function(data){
					if(data){
						if(data.status == 'success'){
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							//创建objectNode
							var cardiv =document.createElement("div");
							cardiv.id="cardiv";
							var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span id="totalCount" style="color:red;font-weight:600;">/' + data.count + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
							cardiv.innerHTML=s_str;
							//将上面创建的P元素加入到BODY的尾部
							document.body.appendChild(cardiv);
							setTimeout("checkReportZipStatus(2000,'"+ data.redisKey + "')",1000);
							//绑定导出取消按钮事件
							$("#exportCancel").click(function(){
								//先移除导出提示，然后请求后台
								if($("#cardiv").length>0) $("#cardiv").remove();
								$.ajax({
									type : "POST",
									url : "/common/export/commCancelExport",
									data : {"wjid" : data.redisKey+"","access_token":$("#ac_tk").val()},
									dataType : "json",
									success:function(res){
										if(res != null && res.result==false){
											if(res.msg && res.msg!="")
												$.error(res.msg);
										}

									}
								});
							});
						}else if(data.status == 'fail'){
							$.error(data.error,function() {
							});
						}else{
							if(data.error){
								$.alert(data.error,function() {
								});
							}
						}
					}
				},'json');
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}	
}

//添加送检信息模态框
var addSjxxFromSjhbPageConfig = {
	width		: "1000px",
	modalName	: "addSjxxFromSjhbPageModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				//去除空格
				$("#ajaxForm #db").val($("#ajaxForm #db").val().replace(/\s+/g,""));
				$("#ajaxForm #sjys").val($("#ajaxForm #sjys").val().trim());

				var cskz2=$("#ajaxForm #kdlx").find("option:selected").attr("cskz2");

				if($("#ajaxForm #ybbh").val()==""||$("#ajaxForm #ybbh").val()==null){
					$.alert("请输入标本编号！");
					return false;
				}else if($("#ajaxForm #hzxm").val()==""||$("#ajaxForm #hzxm").val()==null){
					$.alert("请输入患者姓名！");
					return false;
				}else if($("#ajaxForm input:radio[name='xb']:checked").val()==null){
					$.alert("请选择性别！");
					return false;
				}else if($("#ajaxForm #nl").val()==""||$("#ajaxForm #nl").val()==null){
					$.alert("请输入年龄！");
					return false;
				}else if($("#ajaxForm #sjdw").val()==""||$("#ajaxForm #sjdw").val()==null){
					$.alert("医院名称不正确！");
					return false;
				}else if($("#ajaxForm #sjdwmc").val()==""||$("#ajaxForm #sjdwmc").val()==null){
					$.alert("请输入报告显示单位！");
					return false;
				}else if($("#ajaxForm #sjys").val()==""||$("#ajaxForm #sjys").val()==null){
					$.alert("请输入主治医师！");
					return false;
				}else if($("#ajaxForm #ks option:selected").text()=="--请选择--"){
					$.alert("请选择科室！");
					return false;
				}else if($("#ajaxForm #qqzd").val()==""||$("#ajaxForm #qqzd").val()==null){
					$.alert("请输入前期诊断！");
					return false;
				}else if($("#ajaxForm #db").val()==""||$("#ajaxForm #db").val()==null){
					$.alert("请输入合作伙伴！");
					return false;
				}else if($("#ajaxForm input:radio[name='jcxmids']:checked").val()==null){
					$.alert("请选择检测项目！");
					return false;
				}else if($("#ajaxForm input:radio[name='sjqf']:checked").val()==null){
					$.alert("请选择送检区分！");
					return false;
				}else if($("#ajaxForm input:radio[name='sjqf']:checked").val()==null){
					$.alert("请选择送检区分！");
					return false;
				}else if($("#ajaxForm #yblx option:selected").text()=="--请选择--"){
					$.alert("请选择标本类型！");
					return false;
				}else if($("#ajaxForm #ybtj").val()==""||$("#ajaxForm #ybtj").val()==null){
					$.alert("请输入标本体积！");
					return false;
				}else if($("#ajaxForm #cyrq").val()==""||$("#ajaxForm #cyrq").val()==null){
					$.alert("请输入采样日期！");
					return false;
				}else if($("#ajaxForm #kdlx").val()==""||$("#ajaxForm #kdlx").val()==null){
					$.alert("请选择快递类型！");
					return false;
				}else if($("#ajaxForm #lczz").val()==""||$("#ajaxForm #lczz").val()==null){
					$.alert("请输入主诉！");
					return false;
				}else if(!cskz2){
					if($("#ajaxForm #kdh").val()==""){
						$.alert("请填写快递单号！");
						return false;
					}
				}

				var $this = this;
				var opts = $this["options"]||{};
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							PCRAuditSjhb(responseText["bys"],responseText["jcdw"],responseText["sjid"],responseText["jcxmids"],"");
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
						$.alert(responseText["message"],function() {
						});
					}
				},".modal-footer > button");
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function PCRAuditSjhb(bys,jcdw,sjid,jcxmids,ygzbys) {
	if (bys!=null && bys!= ''){
		$.ajax({
			type: "post",
			url: "/inspection/auditProcess/pagedataPCRAudit",
			data: {"bys" : bys.toString(),"jcdw" : jcdw.toString(),"sjid" : sjid.toString(),"jcxmids" : jcxmids.toString(),"access_token":$("#ac_tk").val(),"ygzbys":ygzbys.toString()},
			dataType: "json",
			success: function (responseText) {
				if(responseText["status"] == 'success'){
					preventResubmitForm(".modal-footer > button", false);
					$.success(responseText["message"],function() {
					});
				}else if(responseText["status"] == "fail"){
					preventResubmitForm(".modal-footer > button", false);
					$.error(responseText["message"],function() {
					});
				} else{
				}
			},
			fail: function (result) {

			}
		})
	}
	searchSjxxResult();
}

//自动检查报告压缩进度
var checkReportZipStatus = function(intervalTime,redisKey){
	$.ajax({
		type : "POST",
		url : "/common/export/commCheckExport",
		data : {"wjid" : redisKey +"","access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.cancel){//取消导出则直接return
				return;
			}
			if(data.result == false){
				if(data.msg && data.msg!=""){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}else{
					if(intervalTime < 5000)
						intervalTime = intervalTime + 1000;
					if($("#exportCount")){
						$("#exportCount").html(data.currentCount)
						if(("/"+data.currentCount) == $("#totalCount").text()){
							$("#totalCount").html($("#totalCount").text()+" 压缩中....")
						}
					}
					setTimeout("checkReportZipStatus("+intervalTime+",'"+ redisKey +"')",intervalTime);
				}
			}else{
				if(data.msg && data.msg!=""){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}
				else{
					if($("#cardiv")) $("#cardiv").remove();
					window.open("/common/export/commDownloadExport?wjid="+redisKey + "&access_token="+$("#ac_tk").val());
				}
			}
		}
	});
}

var setSjxxListFieldsConfig = {
	width : "1020px",
	modalName : "setSjxxListFieldsConfig",
	buttons : {
		reelect : {
			label : "恢复默认",
			className : "btn-info",
			callback : function() {
				$("#ajaxForm").attr("action","/common/title/commTitleDefaultSave");
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				submitForm("ajaxForm", function(responseText,statusText) {
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							//关闭Modal
							$.closeModal("setSjxxListFieldsConfig");
							$('#sjhbview_formSearch #sjxx_list').bootstrapTable('destroy');
							t_sjxxfieldList = getSjxxDataColumn(responseText.choseList,responseText.waitList);
							var oTable = new Inspection_TableInit(t_sjxxfieldList,false);
							oTable.Init();
						});
					}else{
						$.error(responseText["message"],function() {
                                                                    });
					}					
					preventResubmitForm(".modal-footer > button", false);
				}, ".modal-footer > button");
				return false;
			}
		},
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var _choseListDiv = $("#ajaxForm #list2 div[id='choseListDiv']");
				if(_choseListDiv.length < 1 ){
					$.error("请选择显示字段。")
					return false;
				}
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				submitForm("ajaxForm", function(responseText,statusText) {
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							//关闭Modal
							$.closeModal("setSjxxListFieldsConfig");
							$('#sjhbview_formSearch #sjxx_list').bootstrapTable('destroy');
							t_sjxxfieldList = getSjxxDataColumn(responseText.choseList,responseText.waitList);
							var oTable = new Inspection_TableInit(t_sjxxfieldList,false);
							oTable.Init();
						});
					}else{
						$.error(data.msg);
					}					
					preventResubmitForm(".modal-footer > button", false);
				}, ".modal-footer > button");
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
//获取表格显示形式
function getSjxxDataColumn(zdList,waitList){
	var flelds = [];
	var map = {};
	var item = null;
	map = {};
	map["checkbox"] = true;
	flelds.push(map);
	$.each(zdList,function(i){
		item = zdList[i];
		map = {};
		map["title"] = item.xszdmc;
		map["titleTooltip"] = item.zdsm;
		map["field"] = item.xszd;
		if(item.pxzd&&item.pxzd!="")
			map["sortable"] = true;
		map["width"] = item.xskd + "%";
		if(item.xsgs){
			map["formatter"] = eval(item.xsgs);
		}
		map["visible"]=true;
		flelds.push(map);
	});
	$.each(waitList,function(i){
		item = waitList[i];
		map = {};
		map["title"] = item.xszdmc;
		map["titleTooltip"] = item.zdsm;
		map["field"] = item.xszd;
		if(item.pxzd&&item.pxzd!="")
			map["sortable"] = true;
		map["width"] = item.xskd + "%";
		if(item.xsgs){
			map["formatter"] = eval(item.xsgs);
		}
		if(zdList.length ==0){
			if(waitList[i].mrxs == '0'){
				map["visible"] = false;
			}else if(waitList[i].mrxs == '9'){
				map["visible"] = false;
			}
		}else
			map["visible"]=false;
		flelds.push(map);
	});
	return flelds;
}

function searchSjxxResult(isTurnBack){
	//关闭高级搜索条件
	$("#sjhbview_formSearch #searchMore").slideUp("low");
	Inspection_turnOff=true;
	$("#sjhbview_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#sjhbview_formSearch #sjxx_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#sjhbview_formSearch #sjxx_list').bootstrapTable('refresh');
	}
}

$(function(){
	//0.界面初始化
    // 1.初始化Table
    var oTable = new Inspection_TableInit(sjxxFieldList,true);
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new Inspection_ButtonInit();
    oButtonInit.Init();
	// 所有下拉框添加choose样式
    jQuery('#sjhbview_formSearch .chosen-select').chosen({width: '100%'});
    $("#sjhbview_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});

	$('#sjhbview_formSearch #cskz4_id').attr("style","display:none;");
	$('#sjhbview_formSearch #jcwd_id').attr("style","display:none;");
});
