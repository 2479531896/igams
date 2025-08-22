var ReceiveMateriel_turnOff=true;
var xmdllist = '';
var xmbmlist = '';
var ReceiveMaterielAll_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#receiveMaterielListAll_formSearch #receiveMaterielAll_list").bootstrapTable({
			url: $("#receiveMaterielListAll_formSearch #urlPrefix").val()+'/storehouse/requisition/pageGetListRequisitionAll',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#receiveMaterielListAll_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "lldh",				//排序字段
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
            uniqueId: "llid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
            	checkbox: true,
            	width: '4%'
            },{
            	title: '序号',
            	width: '4%',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '4%',
                align: 'left',
                visible:true
            },{
                field: 'llid',
                title: '领料ID',
                width: '12%',
                align: 'left',
                sortable: true,   
                visible: false
            },{
                field: 'lldh',
                title: '领料单号',
                width: '18%',
                align: 'left',
                sortable: true,   
                visible: true
            },{
                field: 'jlbh',
                title: '记录编号',
                width: '6%',
                align: 'left',
                sortable: true,   
                visible: false
            },{
				field: 'khjc',
				title: '客户简称',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'zd',
				title: '终端',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'zdqymc',
				title: '终端区域',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'htdh',
				title: '合同单号',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'gsrmc',
				title: '归属人',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'fzdq',
				title: '负责大区',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'zsyy',
				title: '赠送原因',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'shdz',
				title: '收货地址',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'shlxfs',
				title: '收货联系方式',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'kdxx',
				title: '快递信息',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'sfqr',
				title: '是否确认',
				width: '6%',
				align: 'left',
				formatter: sfqrformat,
				sortable: true,
				visible: false
			},{
                field: 'sqbm',
                title: '申请部门',
                width: '6%',
                align: 'left',
                sortable: true,   
                visible: true
            },{
                field: 'sqry',
                title: '申请人员',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'sqrq',
                title: '申请日期',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'cklb',
                title: '出库类别',
                width: '6%',
                align: 'left',
                sortable: true,   
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '20%',
                align: 'left',
                visible: true
            },{
                field: 'wcbj',
                title: '完成标记',
                width: '6%',
                align: 'left',
                formatter: wcbjAllformat,
                visible: true
            },{
                field: 'ckzt',
                title: '出库状态',
                width: '6%',
                align: 'left',
                formatter: ckztAllformat,
                visible: true
            },{
                field: 'zt',
                title: '状态',
                width: '10%',
                align: 'left',
                formatter: ztAllformat,
                visible: true
            }, {
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '6%',
				align: 'left',
				formatter:czAllFormat,
				visible: true
			}],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	ReceiveMaterielAllDealById(row.llid,'view',$("#receiveMaterielListAll_formSearch #btn_view").attr("tourl"));
             },
		});
		$("#receiveMaterielListAll_formSearch #receiveMaterielAll_list").colResizable({
			liveDrag:true, 
			gripInnerHtml:"<div class='grip'></div>", 
			draggingClass:"dragging", 
			resizeMode:'fit', 
			postbackSafe:true,
			partialRefresh:true
		})
	}
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "llgl.llid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getReceiveMaterielSearchAllData(map);
	}
	return oTableInit
}


//领料列表的提交状态格式化函数
function ztAllformat(value,row,index) {
	//var param = {prefix:$('#receiveMaterielList_formSearch #urlPrefix').val()};
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80'){
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.llid + "\",event,\"AUDIT_GOODS_APPLY\",{prefix:\"" + $('#receiveMaterielListAll_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.llid + "\",event,\"AUDIT_GOODS_APPLY\",{prefix:\"" + $('#receiveMaterielListAll_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else{
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.llid + "\",event,\"AUDIT_GOODS_APPLY\",{prefix:\"" + $('#receiveMaterielListAll_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}
function sfqrformat(value,row,index) {
	if (row.sfqr == '1') {
		return "是";
	}else if (row.sfqr == '0') {
		return "否";
	}else {
		return "";
	}
}
//领料列表的出库状态格式化函数
function ckztAllformat(value,row,index) {
	var param = {prefix:$('#receiveMaterielListAll_formSearch #urlPrefix').val()};
    if (row.ckzt == '80') {
        var html="<span style='color:green;'>"+"出库"+"</span>";
    }else{
    	var html="<span style='color:red;'>"+"未出库"+"</span>";
    }
    return html;
}
//领料列表的完成标记格式化函数
function wcbjAllformat(value,row,index) {
    if (row.wcbj == '0') {
    	var html="<span style='color:red;'>"+"未完成"+"</span>";
    }else if (row.wcbj == '1'){
    	var html="<span style='color:green;'>"+"已完成"+"</span>";    
    }
    return html;
}

//操作按钮格式化函数
function czAllFormat(value,row,index) {
	//var param = {prefix:$('#receiveMaterielList_formSearch #urlPrefix').val()};
	if (row.zt == '10' && row.scbj =='0' ) {
		return "<span class='btn btn-warning' onclick=\"recallReceiveMateriel('" + row.llid + "',event)\" >撤回</span>";
	}else if(row.zt == '10' && row.scbj ==null ){
		return "<span class='btn btn-warning' onclick=\"recallReceiveMateriel('" + row.llid + "',event)\" >撤回</span>";
	}else{
		return "";
	}		
}
//撤回项目提交
function recallReceiveMateriel(llid,event){
	var auditType = $("#receiveMaterielListAll_formSearch #auditType").val();
	var msg = '您确定要撤回该领料单吗？';
	var receiveMateriel_params = [];
	receiveMateriel_params.prefix = $("#receiveMaterielListAll_formSearch #urlPrefix").val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,llid,function(){
				searchReceiveMaterielAllResult();
			},receiveMateriel_params);
		}
	});
}

//条件搜索
function getReceiveMaterielSearchAllData(map){
	var cxtj=$("#receiveMaterielListAll_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#receiveMaterielListAll_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr
	}else if(cxtj=="1"){
		map["wlbm"]=cxnr
	}else if(cxtj=="2"){
		map["wlmc"]=cxnr
	}else if(cxtj=="3"){
		map["zsh"]=cxnr
	}else if(cxtj=="4"){
		map["bgdh"]=cxnr
	}else if(cxtj=="5"){
		map["zjry"]=cxnr
	}else if(cxtj=="6"){
		map["lldh"]=cxnr
	}
	// 高级筛选
	// 分组
	var fzs = jQuery('#receiveMaterielListAll_formSearch #fz_id_tj').val();
	map["fzs"] = fzs;
	// 类别
	var lbs = jQuery('#receiveMaterielListAll_formSearch #lb_id_tj').val();
	map["lbs"] = lbs;
	// 审核状态
	var zts = jQuery('#receiveMaterielListAll_formSearch #zt_id_tj').val();
	map["zts"] = zts;
	// 申请开始日期
	var sqrqstart = jQuery('#receiveMaterielListAll_formSearch #sqrqstart').val();
	map["sqrqstart"] = sqrqstart;
	// 申请结束日期
	var sqrqend = jQuery('#receiveMaterielListAll_formSearch #sqrqend').val();
	map["sqrqend"] = sqrqend;
	// 发料日期开始日期
	var flrqstart = jQuery('#receiveMaterielListAll_formSearch #flrqstart').val();
	map["flrqstart"] = flrqstart;
	// 发料日期结束日期
	var flrqend = jQuery('#receiveMaterielListAll_formSearch #flrqend').val();
	map["flrqend"] = flrqend;
	return map;
}
//提供给导出用的回调函数
function ReceiveMaterielSearchAllData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="llgl.llid";
	map["sortLastOrder"]="asc";
	map["sortName"]="llgl.lldh";
	map["sortOrder"]="asc";
	return getReceiveMaterielSearchAllData(map);
}

var ReceiveMaterielAll_ButtonInit= function (){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#receiveMaterielListAll_formSearch #btn_query");// 模糊查询
		var btn_resubmit=$("#receiveMaterielListAll_formSearch #btn_resubmit");// 重新提交
		var btn_view=$("#receiveMaterielListAll_formSearch #btn_view");// 查看详情
		var btn_del = $("#receiveMaterielListAll_formSearch #btn_del");// 删除
	 	var btn_mod=$("#receiveMaterielListAll_formSearch #btn_mod");// 修改
	 	var btn_add=$("#receiveMaterielListAll_formSearch #btn_add");
		var btn_selectexport = $("#receiveMaterielListAll_formSearch #btn_selectexport");//选中导出
    	var btn_searchexport = $("#receiveMaterielListAll_formSearch #btn_searchexport");//搜索导出
    	var btn_delivery=$("#receiveMaterielListAll_formSearch #btn_delivery"); //出库
		var btn_ckdprint = $("#receiveMaterielListAll_formSearch #btn_ckdprint");//出库单打印
		var btn_lldprint = $("#receiveMaterielListAll_formSearch #btn_lldprint");//领料单打印

		//添加日期控件
    	laydate.render({
    	   elem: '#receiveMaterielListAll_formSearch #sqrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#receiveMaterielListAll_formSearch #sqrqend'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#receiveMaterielListAll_formSearch #flrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#receiveMaterielListAll_formSearch #flrqend'
    	  ,theme: '#2381E9'
    	});
		/*--------------------------------模糊查询---------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchReceiveMaterielAllResult(true);
    		});
		}
		/*--------------------------------新增领料信息---------------------------*/
		btn_add.unbind("click").click(function(){
			ReceiveMaterielAllDealById(null,"add",btn_add.attr("tourl"));
    	});

		/*--------------------------------重新提交领料信息---------------------------*/
		btn_resubmit.unbind("click").click(function(){
			var sel_row=$('#receiveMaterielListAll_formSearch #receiveMaterielAll_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
    				ReceiveMaterielAllDealById(sel_row[0].llid,"resubmit",btn_resubmit.attr("tourl"));
    			}else{
    				$.alert("该状态不允许提交!");
    			}
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/* --------------------------- 修改领料信息 -----------------------------------*/
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#receiveMaterielListAll_formSearch #receiveMaterielAll_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length == 1){
    			if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
    				ReceiveMaterielAllDealById(sel_row[0].llid, "mod", btn_mod.attr("tourl"));
    			}else{
    				$.alert("该记录在审核中或已审核，不允许修改!");
    			}
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
    	/* --------------------------- 领料出库 -----------------------------------*/
    	btn_delivery.unbind("click").click(function(){
    		var sel_row = $('#receiveMaterielListAll_formSearch #receiveMaterielAll_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length == 1){
    			if(sel_row[0].zt=="80"){
    				if(sel_row[0].ckzt=="80"){
    					$.alert("该记录审已出库!");
    				}else{ 				
    					ReceiveMaterielAllDealById(sel_row[0].llid, "delivery", btn_delivery.attr("tourl"));
    				}  				
    			}else{
    				$.alert("该记录审核未通过，不允许出库!");
    			}
    		}else{
    			$.error("请选中一行");
    		}
    	});
		 /* ------------------------------查看领料信息-----------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row=$('#receiveMaterielListAll_formSearch #receiveMaterielAll_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			ReceiveMaterielAllDealById(sel_row[0].llid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/* ------------------------------删除领料信息-----------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#receiveMaterielListAll_formSearch #receiveMaterielAll_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else {
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].llid;
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定要删除所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= $("#receiveMaterielListAll_formSearch #urlPrefix").val()+btn_del.attr("tourl");
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								searchReceiveMaterielAllResult();
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
    	/*-----------------------选中导出------------------------------------*/
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#receiveMaterielListAll_formSearch #receiveMaterielAll_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].llid;
        		}
        		ids = ids.substr(1);
        		$.showDialog($('#receiveMaterielListAll_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=REQUISITION_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));//gnm功能名
    		}else{
    			$.error("请选择数据");
    		}
    	});
    	/*-----------------------搜索导出------------------------------------*/
    	btn_searchexport.unbind("click").click(function(){
    		$.showDialog($('#receiveMaterielListAll_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=REQUISITION_SEARCH&expType=search&callbackJs=ReceiveMaterielSearchAllData"
    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});
		/*-----------------------显示隐藏------------------------------------*/
    	$("#receiveMaterielListAll_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(ReceiveMateriel_turnOff){
    			$("#receiveMaterielListAll_formSearch #searchMore").slideDown("low");
    			ReceiveMateriel_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#receiveMaterielListAll_formSearch #searchMore").slideUp("low");
    			ReceiveMateriel_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
		/* --------------------------- 打印出库单 -----------------------------------*/
		btn_ckdprint.unbind("click").click(function(){
			var sel_row = $('#receiveMaterielListAll_formSearch #receiveMaterielAll_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==0){
				$.error("请至少选中一行");
				return;
			}
			var ids="";
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
				ids = ids + ","+ sel_row[i].llid;
			}
			ids = ids.substr(1);
			var url=$('#receiveMaterielListAll_formSearch #urlPrefix').val()+btn_ckdprint.attr("tourl")+"?llids="+ids.toString()+"&access_token="+$("#ac_tk").val();
			window.open(url);
		});
		/* --------------------------- 打印领料单 -----------------------------------*/
		btn_lldprint.unbind("click").click(function(){
			var sel_row = $('#receiveMaterielListAll_formSearch #receiveMaterielAll_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==0){
				$.error("请至少选中一行");
				return;
			}
			var ids="";
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
				ids = ids + ","+ sel_row[i].llid;
			}
			ids = ids.substr(1);
			var url=$('#receiveMaterielListAll_formSearch #urlPrefix').val()+btn_lldprint.attr("tourl")+"?llids="+ids.toString()+"&access_token="+$("#ac_tk").val();
			window.open(url);
		});
	}
	return oInit;
}
/**
 * 根据项目大类获取项目编码
 * @returns
 */
function getAllxmbm(){
	var xmdl=$("#receiveMaterielListAll_formSearch #xmdl_id_tj").val();
	if(!isEmpty(xmdl)){
		$("#receiveMaterielListAll_formSearch #xmbm_id").removeClass("hidden");
	}else{
		$("#receiveMaterielListAll_formSearch #xmbm_id").addClass("hidden");
	}
	if(!isEmpty(xmdl)){
		var url = $("#receiveMaterielListAll_formSearch #urlPrefix").val()+"/systemmain/data/ansyGetJcsjListInStop";
		$.ajax({
			type:'post',
			url:url,
			data:{"fcsids":xmdl,"access_token":$("#ac_tk").val()},
			dataType:'JSON',
			success:function(data){
				if(data.length>0){
					var html="";
					$.each(data,function(i){
						html +="<li>";
						html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('xmbm','" + data[i].csid + "','receiveMaterielListAll_formSearch');\" id=\"xmbm_id_" + data[i].csid + "\"><span>" + data[i].csmc + "</span></a>";
						html +="</li>"
					});
					$("#receiveMaterielListAll_formSearch #ul_xmbm").html(html);
				}else{
					$("#receiveMaterielListAll_formSearch #ul_xmbm").empty();
				}
				$("#receiveMaterielListAll_formSearch #xmbm_id_tj").val("");
			}
		});
	}else{
		$("#receiveMaterielListAll_formSearch [id^='xmbm_li_']").remove();
		$("#receiveMaterielListAll_formSearch #xmbm_id_tj").val("");
	}
}
//按钮操作
function ReceiveMaterielAllDealById(id,action,tourl){
	var url= tourl;
	if(!tourl){
		return;
	}
	tourl = $("#receiveMaterielListAll_formSearch #urlPrefix").val()+tourl;
	if(action =='mod'){
		var url= tourl+"?llid="+id;
		$.showDialog(url,'修改领料信息',modRequisitionConfig);
	}else if(action =='view'){
		var url= tourl+"?llid="+id;
		$.showDialog(url,'查看领料信息',viewReceiveMaterielConfig);
	}else if(action=="resubmit"){
		var url=tourl+"?llid="+id;
		$.showDialog(url,'重新提交领料信息',resubmitReceiveMaterielConfig);
	}else if(action=="del"){
		var url=tourl+"?llid="+id;
		$.showDialog(url,'删除领料信息',delReceiveMaterielConfig);
	}else if(action =='add'){
		var url=tourl;
		$.showDialog(url,'新增领料信息',addRequisitionConfig);
	}else if(action =='delivery'){
		var url= tourl+"?llid="+id;
		$.showDialog(url,'领料出库',deliveryRequisitionConfig);
	}
}

/**
 * 领料出库模态框
 */
var deliveryRequisitionConfig={
		width		: "1600px",
		modalName	: "deliveryRequisitionModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "保存",
				className : "btn-success",
				callback : function() {					
					if(!$("#deliveryForm").valid()){
						$.alert("请填写完整信息！");
						return false;
					}
					var jy_json = $("#deliveryForm #jy_json").val();
					if(jy_json!=null && jy_json!=""){
						var json = JSON.parse(jy_json);
						for(i = 0; i < json.length; i++) {
							if(json[i].id!=null && json[i].id!=""){
								$.alert(json[i].message);
								return false;
							}
						}
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#deliveryForm input[name='access_token']").val($("#ac_tk").val());

					submitForm(opts["formName"]||"deliveryForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									searchReceiveMaterielAllResult();
								}
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
//增加
var addRequisitionConfig = {
		width		: "1600px",
		modalName	: "editReceiveMaterielModal",
		formName	: "editPickingForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#editPickingForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					var message = $("#editPickingForm #message").val();
					if(message!=null && message!=""){
						$.alert(message);
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					var json = [];
					if(t_map.rows != null && t_map.rows.length > 0){
						for(var i=0;i<t_map.rows.length;i++){
							if(t_map.rows[i].qlsl==null || t_map.rows[i].qlsl==''){
								$.alert("领料数量不能为空！");
								return false;
							}
							if(parseFloat(t_map.rows[i].qlsl)>parseFloat(t_map.rows[i].kqls)){
								$.alert("请领数不能大于可请领数！");
								return false;
							}
							if(t_map.rows[i].xmdl==null || t_map.rows[i].xmdl==''){
								$.alert("项目大类不能为空！");
								return false;
							}
							if(t_map.rows[i].xmbm==null || t_map.rows[i].xmbm==''){
								$.alert("项目编码不能为空！");
								return false;
							}
							if(t_map.rows[i].qlsl==0){
								$.alert("请领数量不能为0！");
								return false;
							}
							var sz = {"hwllid":'',"wlid":'',"qlsl":'',"xmdl":'',"xmbm":'',"qlyds":''};
	    					sz.hwllid = t_map.rows[i].hwllid;
	    					sz.wlid = t_map.rows[i].wlid;
	    					sz.qlsl = t_map.rows[i].qlsl;
	    					sz.xmdl = t_map.rows[i].xmdl;
	    					sz.xmbm = t_map.rows[i].xmbm;
	    					sz.qlyds = t_map.rows[i].qlyds;
	    					json.push(sz);					
						}
						$("#editPickingForm #llmx_json").val(JSON.stringify(json));
					}else{
						$.alert("领料信息不能为空！");
						return false;
					}
					xmdllist = $("#editPickingForm #xmdllist").val();
					xmbmlist = $("#editPickingForm #xmbmlist").val();
					$("#editPickingForm #xmdllist").val('')
					$("#editPickingForm #xmbmlist").val('')
					$("#editPickingForm input[name='access_token']").val($("#ac_tk").val());			
					submitForm(opts["formName"]||"editPickingForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							var auditType = $("#editPickingForm #auditType").val();
							var receiveMateriel_params=[];
							receiveMateriel_params.prefix=$('#editPickingForm #urlPrefix').val();
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);								
							}
							//提交审核
							showAuditFlowDialog(auditType, responseText["ywid"],function(){
								searchReceiveMaterielAllResult();
							},null,receiveMateriel_params);
						}else if(responseText["status"] == "fail"){
							$("#editPickingForm #xmdllist").val(xmdllist)
							$("#editPickingForm #xmbmlist").val(xmbmlist)
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
							$("#editPickingForm #xmdllist").val(xmdllist)
							$("#editPickingForm #xmbmlist").val(xmbmlist)
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

//增加
var resubmitReceiveMaterielConfig = {
		width		: "1600px",
		modalName	: "editPickingFormFormModal",
		formName	: "editPickingForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#editPickingForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					var json = [];
					if(t_map.rows != null && t_map.rows.length > 0){
						for(var i=0;i<t_map.rows.length;i++){
							if(t_map.rows[i].qlsl==null || t_map.rows[i].qlsl==''){
								$.alert("领料数量不能为空！");
								return false;
							}
							if(parseFloat(t_map.rows[i].qlsl)>parseFloat(t_map.rows[i].kqls)){
								$.alert("请领数不能大于可请领数！");
								return false;
							}
							if(t_map.rows[i].xmdl==null || t_map.rows[i].xmdl==''){
								$.alert("项目大类不能为空！");
								return false;
							}
							if(t_map.rows[i].xmbm==null || t_map.rows[i].xmbm==''){
								$.alert("项目编码不能为空！");
								return false;
							}
							if(t_map.rows[i].qlsl==0){
								$.alert("请领数量不能为0！");
								return false;
							}
							var sz = {"hwllid":'',"wlid":'',"qlsl":'',"xmdl":'',"xmbm":'',"qlyds":''};
	    					sz.hwllid = t_map.rows[i].hwllid;
	    					sz.wlid = t_map.rows[i].wlid;
	    					sz.qlsl = t_map.rows[i].qlsl;
	    					sz.xmdl = t_map.rows[i].xmdl;
	    					sz.xmbm = t_map.rows[i].xmbm;
	    					sz.qlyds = t_map.rows[i].qlyds;
	    					json.push(sz);						
						}
						$("#editPickingForm #llmx_json").val(JSON.stringify(json));
					}else{
						$.alert("领料信息不能为空！");
						return false;
					}
					xmdllist = $("#editPickingForm #xmdllist").val();
					xmbmlist = $("#editPickingForm #xmbmlist").val();
					$("#editPickingForm #xmdllist").val('')
					$("#editPickingForm #xmbmlist").val('')
					var $this = this;
					var opts = $this["options"]||{};
					$("#editPickingForm input[name='access_token']").val($("#ac_tk").val());			
					submitForm(opts["formName"]||"editPickingForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							if(opts.offAtOnce){
								//新增提交审
								var Picking_params=[];
								Picking_params.prefix=$('#editPickingForm #urlPrefix').val();
								var auditType = $("#editPickingForm #auditType").val();
								var ywid = $("#editPickingForm #llid").val();
								showAuditFlowDialog(auditType,ywid,function(){
									$.closeModal(opts.modalName);
									searchReceiveMaterielAllResult();
								},null,Picking_params);
								$.closeModal(opts.modalName);
							}
						}else if(responseText["status"] == "fail"){
							$("#editPickingForm #xmdllist").val(xmdllist)
							$("#editPickingForm #xmbmlist").val(xmbmlist)
							$.error(responseText["message"],function() {
								preventResubmitForm(".modal-footer > button", false);
							});
						} else{
							$("#editPickingForm #xmdllist").val(xmdllist)
							$("#editPickingForm #xmbmlist").val(xmbmlist)
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


/**
 * 领料修改模态框
 */
var modRequisitionConfig={
		width		: "1600px",
		modalName	: "modRequisitionModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "保存",
				className : "btn-success",
				callback : function() {
					
					if(!$("#editPickingForm").valid()){
						$.alert("请填写完整信息！");
						return false;
					}
					var json = [];
					if(t_map.rows != null && t_map.rows.length > 0){
						for(var i=0;i<t_map.rows.length;i++){
							if(t_map.rows[i].qlsl==null || t_map.rows[i].qlsl==''){
								$.alert("领料数量不能为空！");
								return false;
							}
							if(parseFloat(t_map.rows[i].qlsl)>parseFloat(t_map.rows[i].kqls)){
								$.alert("请领数不能大于可请领数！");
								return false;
							}
							if(t_map.rows[i].xmdl==null || t_map.rows[i].xmdl==''){
								$.alert("项目大类不能为空！");
								return false;
							}
							if(t_map.rows[i].xmbm==null || t_map.rows[i].xmbm==''){
								$.alert("项目编码不能为空！");
								return false;
							}
							if(t_map.rows[i].qlsl==0){
								$.alert("请领数量不能为0！");
								return false;
							}
							var sz = {"hwllid":'',"wlid":'',"qlsl":'',"xmdl":'',"xmbm":'',"qlyds":''};
	    					sz.hwllid = t_map.rows[i].hwllid;
	    					sz.wlid = t_map.rows[i].wlid;
	    					sz.qlsl = t_map.rows[i].qlsl;
	    					sz.xmdl = t_map.rows[i].xmdl;
	    					sz.xmbm = t_map.rows[i].xmbm;
	    					sz.qlyds = t_map.rows[i].qlyds;
	    					json.push(sz);						
						}
						$("#editPickingForm #llmx_json").val(JSON.stringify(json));
					}else{
						$.alert("领料信息不能为空！");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#editPickingForm input[name='access_token']").val($("#ac_tk").val());
					xmdllist = $("#editPickingForm #xmdllist").val();
					xmbmlist = $("#editPickingForm #xmbmlist").val();
					$("#editPickingForm #xmdllist").val('')
					$("#editPickingForm #xmbmlist").val('')
					submitForm(opts["formName"]||"editPickingForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchReceiveMaterielAllResult();
							});
						}else if(responseText["status"] == "fail"){
							$("#editPickingForm #xmdllist").val(xmdllist)
							$("#editPickingForm #xmbmlist").val(xmbmlist)
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						}else{
							$("#editPickingForm #xmdllist").val(xmdllist)
							$("#editPickingForm #xmbmlist").val(xmbmlist)
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
/**
 * 查看页面模态框
 */
var viewReceiveMaterielConfig={
	width		: "1600px",
	modalName	:"viewReceiveMaterielModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}


//列表刷新
function searchReceiveMaterielAllResult(isTurnBack){
	//关闭高级搜索条件
	$("#receiveMaterielListAll_formSearch #searchMore").slideUp("low");
	ReceiveMateriel_turnOff=true;
	$("#receiveMaterielListAll_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#receiveMaterielListAll_formSearch #receiveMaterielAll_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#receiveMaterielListAll_formSearch #receiveMaterielAll_list').bootstrapTable('refresh');
	}
}

$(function(){
	 // 1.初始化Table
	var oTable = new ReceiveMaterielAll_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new ReceiveMaterielAll_ButtonInit();
    oButtonInit.Init();
    jQuery('#receiveMaterielListAll_formSearch .chosen-select').chosen({width: '100%'});
})