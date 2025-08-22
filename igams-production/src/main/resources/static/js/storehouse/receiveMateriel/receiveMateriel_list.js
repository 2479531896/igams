var xzbj = $("#receiveMaterielList_formSearch #xzbj").val()
var ReceiveMateriel_turnOff=true;
var xmdllist = '';
var xmbmlist = '';
var ReceiveMateriel_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#receiveMaterielList_formSearch #receiveMateriel_list").bootstrapTable({
			url: $("#receiveMaterielList_formSearch #urlPrefix").val()+'/storehouse/receiveMateriel/pageGetListReceiveMateriel',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#receiveMaterielList_formSearch #toolbar',                //工具按钮用哪个容器
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
                width: '10%',
                align: 'left',
                sortable: true,   
                visible: true
            },{
				field: 'zldh',
				title: '指令单号',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: false,
				formatter:zldhformater
			},{
				field: 'lllx',
				title: '领料类型',
				width: '10%',
				align: 'left',
				formatter: lllxformat,
				sortable: true,
				visible: false
			},{
				field: 'xslxmc',
				title: '销售类型',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: false
			},{
                field: 'jlbh',
                title: '记录编号',
                width: '6%',
                align: 'left',
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
				field: 'gsrbmmc',
				title: '归属人部门',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'khjc',
				title: '客户简称',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'ysfsmc',
				title: '运输方式',
				width: '5%',
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
				visible: false,
				formatter:viewYxhtFormatter
			},{
                field: 'gsrmc',
                title: '归属人',
                width: '6%',
                align: 'left',
                sortable: true,   
                visible: true
            },{
				field: 'fzrmc',
				title: '负责人',
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
                field: 'bmsbfzrmc',
                title: '部门设备负责人',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'sydd',
                title: '使用地点',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'bz',
                title: '备注',
                width: '20%',
                align: 'left',
                visible: true
            },{
                field: 'scbj',
                title: '是否废弃',
                width: '6%',
                align: 'left',
                formatter: sffqformat,
                visible: true
            },{
                field: 'ckzt',
                title: '出库状态',
                width: '6%',
                align: 'left',
                formatter: ckztformat,
                visible: true
            },{
                field: 'zt',
                title: '状态',
                width: '10%',
                align: 'left',
                formatter: ztformat,
                visible: true
            }, {
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '6%',
				align: 'left',
				formatter:czFormat,
				visible: true
			}],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	ReceiveMaterielDealById(row.llid,'view',$("#receiveMaterielList_formSearch #btn_view").attr("tourl"));
             },
		});
		$("#receiveMaterielList_formSearch #receiveMateriel_list").colResizable({
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
		return getReceiveMaterielSearchData(map);
	}
	return oTableInit
}


//领料列表的提交状态格式化函数
function ztformat(value,row,index) {
	var  name = row.shxx_dqgwmc;
	//var param = {prefix:$('#receiveMaterielList_formSearch #urlPrefix').val()};
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80'){
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.llid + "\",event,\"AUDIT_GOODS_DEVICE,AUDIT_GOODS_APPLY_DING,AUDIT_GOODS_APPLY\",{prefix:\"" + $('#receiveMaterielList_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.llid + "\",event,\"AUDIT_GOODS_DEVICE,AUDIT_GOODS_APPLY_DING,AUDIT_GOODS_APPLY\",{prefix:\"" + $('#receiveMaterielList_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else{
		return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.llid + "\",event,\"AUDIT_GOODS_DEVICE,AUDIT_GOODS_APPLY_DING,AUDIT_GOODS_APPLY\",{prefix:\"" + $('#receiveMaterielList_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}
//领料列表的出库状态格式化函数
function ckztformat(value,row,index) {
	var param = {prefix:$('#receiveMaterielList_formSearch #urlPrefix').val()};
    if (row.ckzt == '80') {
        var html="<span style='color:green;'>"+"出库"+"</span>";
    }else{
    	var html="<span style='color:red;'>"+"未出库"+"</span>";
    }
    return html;
}
//领料列表的完成标记格式化函数
function sffqformat(value,row,index) {
    if (row.scbj == '0') {
    	var html="<span style='color:green;'>"+"正常"+"</span>";
    }else if (row.scbj == '2'){
    	var html="<span style='color:#f59610;'>"+"废弃"+"</span>";    
    }
    return html;
}
function lllxformat(value,row,index) {
    if (row.lllx == '1') {
    	return "OA领料";
    }else if (row.lllx == '2') {
		return "留样领料";
	}else if (row.lllx == '3') {
		return "销售领料";
	}else if (row.lllx == '4') {
		return "OA留样领料";
	}else if (row.lllx == '5') {
		return "生产领料";
	}else {
    	return "普通领料";   
    }
}
function zldhformater(value,row,index) {
	var html = "";
	if(row.zldh==null){
		html = "";
	}else{
		html += "<a href='javascript:void(0);' onclick=\"viewSczlxx('"+row.sczlid+"')\">"+row.zldh+"</a>";

	}
	return html;
}
function viewSczlxx(sczlid){
	var url=$("#receiveMaterielList_formSearch #urlPrefix").val()+"/progress/produce/pagedataProduceForll?sczlid="+sczlid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'生产指令信息',viewProduceConfig);
}
var viewProduceConfig = {
	width		: "1600px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
function sfqrformat(value,row,index) {
    if (row.sfqr == '1') {
    	return "是";
    }else if (row.sfqr == '0') {
		return "否";
	}else {
    	return "";
    }
}

//操作按钮格式化函数
function czFormat(value,row,index) {
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
	// var auditType = $("#receiveMaterielList_formSearch #auditType").val();
	// var msg = '您确定要撤回该领料单吗？';
	// var receiveMateriel_params = [];
	// receiveMateriel_params.prefix = $("#receiveMaterielList_formSearch #urlPrefix").val();
	// $.confirm(msg,function(result){
	// 	if(result){
	// 		doAuditRecall(auditType,llid,function(){
	// 			searchReceiveMaterielResult();
	// 		},receiveMateriel_params);

	$.ajax({
		type:'post',
		url: $('#receiveMaterielList_formSearch #urlPrefix').val()+'/storehouse/requisition/pagedataRetractConfirmAuditType',
		cache: false,
		data: {"llid":llid,"access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			if (data.status == "success"){
				var msg = '您确定要撤回该领料单吗？';
				var receiveMateriel_params = [];
				receiveMateriel_params.prefix = $("#receiveMaterielList_formSearch #urlPrefix").val();
				$.confirm(msg,function(result){
					if(result){
						doAuditRecall(data.auditType,llid,function(){
							searchReceiveMaterielResult();
						},receiveMateriel_params);
					}
				});
				$("#receiveMaterielList_formSearch #auditType").val();
			}else{
				$.alert(data.message);
			}
		}
	});
}

//条件搜索
function getReceiveMaterielSearchData(map){
	var cxtj=$("#receiveMaterielList_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#receiveMaterielList_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr
	}else if(cxtj=="1"){
		map["wlbm"]=cxnr
	}else if(cxtj=="2"){
		map["wlmc"]=cxnr
	}else if(cxtj=="3"){
		map["zsh"]=cxnr
	}else if(cxtj=="4"){
		map["sqbmmc"]=cxnr
	}else if(cxtj=="5"){
		map["sqrmc"]=cxnr
	}else if(cxtj=="6"){
		map["scph"]=cxnr
	}else if(cxtj=="7"){
		map["lldh"]=cxnr
	}else if(cxtj=="8"){
		map["khjc"]=cxnr
	}else if(cxtj=="9"){
		map["zd"]=cxnr
	}else if(cxtj=="10"){
		map["zdqymc"]=cxnr
	}else if(cxtj=="11"){
		map["fzdq"]=cxnr
	}else if(cxtj=="12"){
		map["htdh"]=cxnr
	}else if(cxtj=="13"){
		map["shdz"]=cxnr
	}else if(cxtj=="14"){
		map["shlxfs"]=cxnr
	}else if(cxtj=="15"){
		map["bmsbfzr"]=cxnr
	}else if(cxtj=="16"){
		map["sydd"]=cxnr
	}else if(cxtj=="17"){
		map["zldh"]=cxnr
	}
	// 高级筛选
	// 分组
	var fzs = jQuery('#receiveMaterielList_formSearch #fz_id_tj').val();
	map["fzs"] = fzs;
	// 类别
	var lbs = jQuery('#receiveMaterielList_formSearch #lb_id_tj').val();
	map["lbs"] = lbs;
	// 审核状态
	var zts = jQuery('#receiveMaterielList_formSearch #zt_id_tj').val();
	map["zts"] = zts;
	// 领料类型
	var lllx = jQuery('#receiveMaterielList_formSearch #lllx_id_tj').val();
	map["lllx"] = lllx;
	// 客户类别
	var khlbs = jQuery('#receiveMaterielList_formSearch #khlb_id_tj').val();
	map["khlbs"] = khlbs;
	// 是否签收
	var sfqs = jQuery('#receiveMaterielList_formSearch #sfqs_id_tj').val();
	map["sfqs"] = sfqs;
	// 是否确认
	var sfqr = jQuery('#receiveMaterielList_formSearch #sfqr_id_tj').val();
	map["sfqr"] = sfqr;
	// 申请开始日期
	var sqrqstart = jQuery('#receiveMaterielList_formSearch #sqrqstart').val();
	map["sqrqstart"] = sqrqstart;
	// 申请结束日期
	var sqrqend = jQuery('#receiveMaterielList_formSearch #sqrqend').val();
	map["sqrqend"] = sqrqend;
	// 发料日期开始日期
	var flrqstart = jQuery('#receiveMaterielList_formSearch #flrqstart').val();
	map["flrqstart"] = flrqstart;
	// 发料日期结束日期
	var flrqend = jQuery('#receiveMaterielList_formSearch #flrqend').val();
	map["flrqend"] = flrqend;
	return map;
}
function viewYxhtFormatter(value,row,index) {
	var html = "";
	if(row.htdh==null){
		html = "";
	}else{
		html += "<a href='javascript:void(0);' onclick=\"viewYxht('"+row.htid+"')\">"+row.htdh+"</a>";

	}
	return html;
}
function viewYxht(htid){
	var url=$("#receiveMaterielList_formSearch #urlPrefix").val()+"/marketingContract/marketingContract/pagedataViewMarketingContract?htid="+htid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'营销合同信息',viewMarketingContractConfig);
}
var viewMarketingContractConfig = {
	width		: "1600px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
//提供给导出用的回调函数
function ReceiveMaterielSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="llgl.llid";
	map["sortLastOrder"]="asc";
	map["sortName"]="llgl.lldh";
	map["sortOrder"]="asc";
	return getReceiveMaterielSearchData(map);
}
//提供给导出用的回调函数
function ProductionReceiveMaterielSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="llgl.llid";
	map["sortLastOrder"]="asc";
	map["sortName"]="llgl.lldh";
	map["sortOrder"]="asc";
	map["dcflag"]="sclldc";
	return getReceiveMaterielSearchData(map);
}
var ReceiveMateriel_ButtonInit= function (){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#receiveMaterielList_formSearch #btn_query");// 模糊查询
		var btn_resubmit=$("#receiveMaterielList_formSearch #btn_resubmit");// 重新提交
		var btn_view=$("#receiveMaterielList_formSearch #btn_view");// 查看详情
		var btn_del = $("#receiveMaterielList_formSearch #btn_del");// 删除
	 	var btn_mod=$("#receiveMaterielList_formSearch #btn_mod");// 修改
		var btn_advancedmod=$("#receiveMaterielList_formSearch #btn_advancedmod");// 修改
	 	var btn_add=$("#receiveMaterielList_formSearch #btn_add");
		var btn_selectexport = $("#receiveMaterielList_formSearch #btn_selectexport");//选中导出
    	var btn_searchexport = $("#receiveMaterielList_formSearch #btn_searchexport");//搜索导出
    	var btn_delivery=$("#receiveMaterielList_formSearch #btn_delivery"); //出库
		var btn_lldprint = $("#receiveMaterielList_formSearch #btn_lldprint");//领料单打印
		var btn_discard = $("#receiveMaterielList_formSearch #btn_discard");//领料废弃
		var btn_copy = $("#receiveMaterielList_formSearch #btn_copy");//领料废弃
		var btn_system =  $("#receiveMaterielList_formSearch #btn_system");//OA新增
		var btn_samplepick =  $("#receiveMaterielList_formSearch #btn_samplepick");//留样领料
		var btn_oasamplepick =  $("#receiveMaterielList_formSearch #btn_oasamplepick");//OA留样领料
		var btn_logisticsuphold =  $("#receiveMaterielList_formSearch #btn_logisticsuphold");//物料维护
		var btn_signfor =  $("#receiveMaterielList_formSearch #btn_signfor");//物流签收
		var btn_signforconfirm =  $("#receiveMaterielList_formSearch #btn_signforconfirm");//物流签收确认
		var btn_salepick =  $("#receiveMaterielList_formSearch #btn_salepick");//销售领料
		var btn_selfuseequipment =  $("#receiveMaterielList_formSearch #btn_selfuseequipment");//自用设备新增
		var btn_productionmaterialselexport = $("#receiveMaterielList_formSearch #btn_productionmaterialselexport");//生产领料选中导出
		var btn_productionmaterialseaexport = $("#receiveMaterielList_formSearch #btn_productionmaterialseaexport");//生产领料搜索导出
		var btn_produce =  $("#receiveMaterielList_formSearch #btn_produce");//生产领料

		//添加日期控件
    	laydate.render({
    	   elem: '#receiveMaterielList_formSearch #sqrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#receiveMaterielList_formSearch #sqrqend'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#receiveMaterielList_formSearch #flrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#receiveMaterielList_formSearch #flrqend'
    	  ,theme: '#2381E9'
    	});
		/*--------------------------------模糊查询---------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchReceiveMaterielResult(true); 
    		});
		}
		/*--------------------------------新增领料信息---------------------------*/
		btn_add.unbind("click").click(function(){
			ReceiveMaterielDealById(null,"add",btn_add.attr("tourl"));
    	});
		/*--------------------------------自用设备新增---------------------------*/
		btn_selfuseequipment.unbind("click").click(function(){
			ReceiveMaterielDealById(null,"add",btn_selfuseequipment.attr("tourl"));
		});
		/*--------------------------------OA新增---------------------------*/
		btn_system.unbind("click").click(function(){
			ReceiveMaterielDealById(null,"system",btn_system.attr("tourl"));
    	});
		/*--------------------------------留样领料---------------------------*/
		btn_samplepick.unbind("click").click(function(){
			ReceiveMaterielDealById(null,"samplepick",btn_samplepick.attr("tourl"));
		});
		/*--------------------------------OA留样领料---------------------------*/
		btn_oasamplepick.unbind("click").click(function(){
			ReceiveMaterielDealById(null,"oasamplepick",btn_oasamplepick.attr("tourl"));
		});
		/*--------------------------------销售领料---------------------------*/
		btn_salepick.unbind("click").click(function(){
			ReceiveMaterielDealById(null,"salepick",btn_salepick.attr("tourl"));
		});
		/*--------------------------------生产领料---------------------------*/
		btn_produce.unbind("click").click(function(){
			ReceiveMaterielDealById(null,"produce",btn_produce.attr("tourl"));
		});

		/*--------------------------------重新提交领料信息---------------------------*/
		btn_resubmit.unbind("click").click(function(){
			var sel_row=$('#receiveMaterielList_formSearch #receiveMateriel_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
				if (sel_row[0].scbj=='2'){
					$.alert("此领料单已废弃！");
					return;
				}
    			if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
    				ReceiveMaterielDealById(sel_row[0].llid,"resubmit",btn_resubmit.attr("tourl"));
    			}else{
    				$.alert("该状态不允许提交!");
    			}
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/* --------------------------- 修改领料信息 -----------------------------------*/
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#receiveMaterielList_formSearch #receiveMateriel_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length == 1){
				if (sel_row[0].scbj=='2'){
					$.alert("此领料单已废弃！");
					return;
				}
    			if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
    				ReceiveMaterielDealById(sel_row[0].llid, "mod", btn_mod.attr("tourl"));
    			}else{
    				$.alert("该记录在审核中或已审核，不允许修改!");
    			}
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/* --------------------------- 复制领料信息 -----------------------------------*/
		btn_copy.unbind("click").click(function(){
			var sel_row = $('#receiveMaterielList_formSearch #receiveMateriel_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				ReceiveMaterielDealById(sel_row[0].llid, "copy", btn_copy.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/* --------------------------- 修改领料信息btn_advancedmod -----------------------------------*/
		btn_advancedmod.unbind("click").click(function(){
			var sel_row = $('#receiveMaterielList_formSearch #receiveMateriel_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				ReceiveMaterielDealById(sel_row[0].llid,"advancedmod",btn_advancedmod.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
    	
    	/* --------------------------- 领料出库 -----------------------------------*/
    	btn_delivery.unbind("click").click(function(){
    		var sel_row = $('#receiveMaterielList_formSearch #receiveMateriel_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length == 1){
    			if(sel_row[0].zt=="80"){
    				if(sel_row[0].ckzt=="80"){
    					$.alert("该记录审已出库!");
    				}else{ 				
    					ReceiveMaterielDealById(sel_row[0].llid, "delivery", btn_delivery.attr("tourl"));
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
    		var sel_row=$('#receiveMaterielList_formSearch #receiveMateriel_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			ReceiveMaterielDealById(sel_row[0].llid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/* ------------------------------废弃领料信息-----------------------------*/
		btn_discard.unbind("click").click(function(){
			var sel_row = $('#receiveMaterielList_formSearch #receiveMateriel_list').bootstrapTable('getSelections');//获取选择行数据
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
				if(sel_row[i].zt !='00' && sel_row[i].zt !='15'){
					$.error("该记录在审核中或已审核，不允许废弃!");
					return;
				}
			}
			if(sel_row.length==0){
				$.error("请至少选中一行");
				return;
			}else{
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids= ids + ","+ sel_row[i].llid;
				}
				ids=ids.substr(1);
				$.confirm('您确定要废弃所选择的信息吗？',function(result){
					if(result){
						jQuery.ajaxSetup({async:false});
						var url= $("#receiveMaterielList_formSearch #urlPrefix").val()+ btn_discard.attr("tourl");
						jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
							setTimeout(function(){
								if(responseText["status"] == 'success'){
									$.success(responseText["message"],function() {
										searchReceiveMaterielResult();
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
    	/* ------------------------------删除领料信息-----------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#receiveMaterielList_formSearch #receiveMateriel_list').bootstrapTable('getSelections');//获取选择行数据
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
        				var url= $("#receiveMaterielList_formSearch #urlPrefix").val()+btn_del.attr("tourl");
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								searchReceiveMaterielResult();
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
    		var sel_row = $('#receiveMaterielList_formSearch #receiveMateriel_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].llid;
        		}
        		ids = ids.substr(1);
        		$.showDialog($('#receiveMaterielList_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=REQUISITION_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));//gnm功能名
    		}else{
    			$.error("请选择数据");
    		}
    	});
    	/*-----------------------搜索导出------------------------------------*/
    	btn_searchexport.unbind("click").click(function(){
    		$.showDialog($('#receiveMaterielList_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=REQUISITION_SEARCH&expType=search&callbackJs=ReceiveMaterielSearchData"
    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});
		/*-----------------------生产领料选中导出------------------------------------*/
		btn_productionmaterialselexport.unbind("click").click(function(){
			var sel_row = $('#receiveMaterielList_formSearch #receiveMateriel_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].llid;
				}
				ids = ids.substr(1);
				$.showDialog($('#receiveMaterielList_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=PRODUCTIONMATERIAL_SELECT&expType=select&ids="+ids
					,btn_productionmaterialselexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));//gnm功能名
			}else{
				$.error("请选择数据");
			}
		});
		/*-----------------------生产领料搜索导出------------------------------------*/
		btn_productionmaterialseaexport.unbind("click").click(function(){
			$.showDialog($('#receiveMaterielList_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=PRODUCTIONMATERIAL_SEARCH&dcflag=sclldc&expType=search&callbackJs=ProductionReceiveMaterielSearchData"
				,btn_productionmaterialseaexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
		});
		/*-----------------------显示隐藏------------------------------------*/
    	$("#receiveMaterielList_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(ReceiveMateriel_turnOff){
    			$("#receiveMaterielList_formSearch #searchMore").slideDown("low");
    			ReceiveMateriel_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#receiveMaterielList_formSearch #searchMore").slideUp("low");
    			ReceiveMateriel_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
		/* --------------------------- 打印领料单 -----------------------------------*/
		btn_lldprint.unbind("click").click(function(){
			var sel_row = $('#receiveMaterielList_formSearch #receiveMateriel_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==0){
				$.error("请至少选中一行");
				return;
			}
			var ids="";
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
				ids = ids + ","+ sel_row[i].llid;
			}
			ids = ids.substr(1);
			var url=$('#receiveMaterielList_formSearch #urlPrefix').val()+btn_lldprint.attr("tourl")+"?llids="+ids.toString()+"&access_token="+$("#ac_tk").val();
			window.open(url);
		});
		/* --------------------------- 物流维护 -----------------------------------*/
		btn_logisticsuphold.unbind("click").click(function(){
			var sel_row = $('#receiveMaterielList_formSearch #receiveMateriel_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				if ("3"==sel_row[0].lllx){
					ReceiveMaterielDealById(sel_row[0].llid, "logisticsuphold", btn_logisticsuphold.attr("tourl"),sel_row[0].lldh,'');
				}else {
					ReceiveMaterielDealById(sel_row[0].llid, "logisticsuphold", btn_logisticsuphold.attr("tourl"),sel_row[0].lldh,'0');
				}
			}else{
				$.error("请选中一行");
			}
		});
		/* --------------------------- 物流签收 -----------------------------------*/
		btn_signfor.unbind("click").click(function(){
			var sel_row = $('#receiveMaterielList_formSearch #receiveMateriel_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
			    if ("3"==sel_row[0].lllx){
			        ReceiveMaterielDealById(sel_row[0].llid, "signfor", btn_signfor.attr("tourl"),sel_row[0].lldh,'');
			    }else{
			        ReceiveMaterielDealById(sel_row[0].llid, "signfor", btn_signfor.attr("tourl"),sel_row[0].lldh,'0');
			    }
			}else{
				$.error("请选中一行");
			}
		});
		/* --------------------------- 物流签收确认 -----------------------------------*/
		btn_signforconfirm.unbind("click").click(function(){
			var sel_row = $('#receiveMaterielList_formSearch #receiveMateriel_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
			    if ("3"==sel_row[0].lllx){
			        ReceiveMaterielDealById(sel_row[0].llid, "signforconfirm", btn_signforconfirm.attr("tourl"),sel_row[0].lldh,'');
			    }else{
			        ReceiveMaterielDealById(sel_row[0].llid, "signforconfirm", btn_signforconfirm.attr("tourl"),sel_row[0].lldh,'0');
			    }
			}else{
				$.error("请选中一行");
			}
		});
	}
	return oInit;
}
/**
 * 根据项目大类获取项目编码
 * @returns
 */
function getxmbm(){
	var xmdl=$("#receiveMaterielList_formSearch #xmdl_id_tj").val();
	if(!isEmpty(xmdl)){
		$("#receiveMaterielList_formSearch #xmbm_id").removeClass("hidden");
	}else{
		$("#receiveMaterielList_formSearch #xmbm_id").addClass("hidden");
	}
	if(!isEmpty(xmdl)){
		var url = $("#receiveMaterielList_formSearch #urlPrefix").val()+"/systemmain/data/ansyGetJcsjListInStop";
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
						html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('xmbm','" + data[i].csid + "','receiveMaterielList_formSearch');\" id=\"xmbm_id_" + data[i].csid + "\"><span>" + data[i].csmc + "</span></a>";
						html +="</li>"
					});
					$("#receiveMaterielList_formSearch #ul_xmbm").html(html);
				}else{
					$("#receiveMaterielList_formSearch #ul_xmbm").empty();
				}
				$("#receiveMaterielList_formSearch #xmbm_id_tj").val("");
			}
		});
	}else{
		$("#receiveMaterielList_formSearch [id^='xmbm_li_']").remove();
		$("#receiveMaterielList_formSearch #xmbm_id_tj").val("");
	}
}
//按钮操作
function ReceiveMaterielDealById(id,action,tourl,lldh,xxbj){
	var url= tourl;
	if(!tourl){
		return;
	}
	tourl = $("#receiveMaterielList_formSearch #urlPrefix").val()+tourl;
	if(action =='mod'){
		var url= tourl+"?llid="+id+"&xzbj="+xzbj;
		$.showDialog(url,'修改领料信息',modRequisitionConfig);
	}else if(action =='view'){
		var url= tourl+"?llid="+id;
		$.showDialog(url,'查看领料信息',viewReceiveMaterielConfig);
	}else if(action=="resubmit"){
		var url=tourl+"?llid="+id+"&xzbj="+xzbj;
		$.showDialog(url,'重新提交领料信息',resubmitReceiveMaterielConfig);
	}else if(action=="del"){
		var url=tourl+"?llid="+id;
		$.showDialog(url,'删除领料信息',delReceiveMaterielConfig);
	}else if(action =='add'){
		var url=tourl+"?xzbj="+xzbj;
		$.showDialog(url,'新增领料信息',addRequisitionConfig);
	}else if(action =='delivery'){
		var url= tourl+"?llid="+id+"&xzbj="+xzbj;
		$.showDialog(url,'领料出库',deliveryRequisitionConfig);
	}else if(action =='advancedmod'){
		var url= tourl+"?llid="+id+"&xzbj="+xzbj;
		$.showDialog(url,'领料高级修改',modRequisitionConfig);
	}else if(action =='copy'){
		var url= tourl+"?llid="+id+"&xzbj="+xzbj;
		$.showDialog(url,'复制',addRequisitionConfig);
	}else if(action =='system'){
		var url= tourl+"?lllx=1"+"&xzbj="+xzbj;
		$.showDialog(url,'OA新增',addRequisitionConfig);
	}else if(action =='samplepick'){
		var url= tourl+"?lllx=2"+"&xzbj="+xzbj;
		$.showDialog(url,'留样领料',addRequisitionConfig);
	}else if(action =='salepick'){
		var url= tourl+"?lllx=3"+"&xzbj="+xzbj;
		$.showDialog(url,'销售领料',addRequisitionConfig);
	}else if(action =='produce'){
		var url= tourl+"?lllx=5"+"&xzbj="+xzbj;
		$.showDialog(url,'生产领料',addRequisitionConfig);
	}else if(action =='logisticsuphold'){
		var url= tourl+"?ywid="+id+"&ywlx=ll&ywdh="+lldh+"&xxbj="+xxbj;
		$.showDialog(url,'物流维护',addlogisticsUpholdFormConfig);
	}else if(action =='signfor'){
		var url= tourl+"?ywid="+id+"&ywlx=ll&ywdh="+lldh+"&xxbj="+xxbj;
		$.showDialog(url,'物流签收',addlogisticsUpholdFormConfig);
	}else if(action =='signforconfirm'){
		var url= tourl+"?ywid="+id+"&ywlx=ll&ywdh="+lldh;
		$.showDialog(url,'物流签收确认',addlogisticsUpholdFormConfig);
	}else if(action =='oasamplepick'){
		var url= tourl+"?lllx=4"+"&xzbj="+xzbj;
		$.showDialog(url,'OA留样领料',addRequisitionConfig);
	}else if(action =='selfuseequipment'){
		var url= tourl+"?xzbj="+xzbj;
		$.showDialog(url,'自用设备新增',addRequisitionConfig);
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
					let jgdm= $("#deliveryForm #jgdm").val()
					if(!jgdm){
						$.alert("所选部门存在异常！");
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
									searchReceiveMaterielResult();
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
					let jgdm= $("#editPickingForm #jgdm").val()
					if(!jgdm){
						$.alert("所选部门存在异常！");
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
					var lllx = $('#editPickingForm #lllx').val();
					var sczlid = $('#editPickingForm #sczlid').val();
					if (lllx=='5'&&(sczlid==null || sczlid=='')) {
						$.error('生产领料需要关联生产单！')
						return false;
					}
					if(t_map.rows != null && t_map.rows.length > 0){
						let wlids = "";
						var queryWlids = "";
						for(var i=0;i<t_map.rows.length;i++){
							if(t_map.rows[i].qlsl==null || t_map.rows[i].qlsl==''){
								$.alert("领料数量不能为空！");
								return false;
							}

							if(parseFloat(t_map.rows[i].qlsl)>parseFloat(t_map.rows[i].kqls)){
								$.alert(t_map.rows[i].ckmc+"-"+t_map.rows[i].wlbm+"-"+t_map.rows[i].wlmc+"请领数不能大于可请领数！");
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
							var sz = {"xh":'',"hwllid":'',"ckhwid":'',"ckid":'',"wlid":'',"qlsl":'',"bz":'',"xmdl":'',"xmbm":'',"qlyds":'',"cplx":'',"hwllmx_json":''};
	    					sz.xh = i+1;
	    					sz.hwllid = t_map.rows[i].hwllid;
							sz.ckhwid = t_map.rows[i].ckhwid;
							sz.ckid = t_map.rows[i].ckid;
	    					sz.wlid = t_map.rows[i].wlid;
	    					sz.qlsl = t_map.rows[i].qlsl;
							sz.bz = t_map.rows[i].hwllxxbz;
	    					sz.xmdl = t_map.rows[i].xmdl;
	    					sz.xmbm = t_map.rows[i].xmbm;
	    					sz.qlyds = t_map.rows[i].qlyds;
	    					sz.cplx = t_map.rows[i].cplx;
	    					sz.hwllmx_json=t_map.rows[i].hwllmx_json;
	    					json.push(sz);
							wlids = wlids + "," + t_map.rows[i].wlid;
							queryWlids = queryWlids + "," + t_map.rows[i].wlid;
	    					wlids = wlids + "," + t_map.rows[i].wlid;
						}
						wlids = wlids.substr(1);
						queryWlids = queryWlids.substr(1);
						confirmAuditType(wlids);
						$("#editPickingForm #queryWlids").val(queryWlids);
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
							// var auditType = $("#editPickingForm #auditType").val();
							var receiveMateriel_params=[];
							receiveMateriel_params.prefix=$('#editPickingForm #urlPrefix').val();
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);								
							}
							//提交审核
							showAuditFlowDialog(responseText["auditType"], responseText["ywid"],function(){
								searchReceiveMaterielResult();
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
//物流维护
var addlogisticsUpholdFormConfig = {
	width		: "1000px",
	modalName	: "logisticsUpholdFormModal",
	formName	: "logisticsUpholdForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				var json = [];
				if(t_map.rows != null && t_map.rows.length > 0){
					for(var i=0;i<t_map.rows.length;i++){
						if(t_map.rows[i].kdgs==null || t_map.rows[i].kdgs==''){
							$.alert("快递公司不能为空！");
							return false;
						}
						if(t_map.rows[i].wldh==null || t_map.rows[i].wldh==''){
							$.alert("快递单号不能为空！");
							return false;
						}
						var fjids = $("#wlfj_"+i).val().split(",");
						var sz = {"kdgs":'',"wldh":'',"ywid":'',"wlxxid":'',"qsrq":'',"sfqr":'',"lrsj":'',"fjids":'',"fhrq":'',"yf":''};
						sz.kdgs = t_map.rows[i].kdgs;
						sz.wldh = t_map.rows[i].wldh;
						sz.ywid = t_map.rows[i].ywid;
						sz.wlxxid = t_map.rows[i].wlxxid;
						sz.qsrq = t_map.rows[i].qsrq;
						sz.sfqr = t_map.rows[i].sfqr;
						sz.lrsj = t_map.rows[i].lrsj;
						sz.fhrq = t_map.rows[i].fhrq;
						sz.yf = t_map.rows[i].yf;
						sz.fjids = fjids;
						json.push(sz);
					}
					$("#logisticsUpholdForm #wlmx_json").val(JSON.stringify(json));
				}
				$("#logisticsUpholdForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"logisticsUpholdForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							searchReceiveMaterielResult();
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					}else{
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
//列表刷新
function confirmAuditType(wlids){
	$.ajax({
		type:'post',
		url: $('#receiveMaterielList_formSearch #urlPrefix').val()+'/storehouse/requisition/pagedataConfirmAuditType',
		cache: false,
		data: {"wlids":wlids,"access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			if (data.status == "success"){
				$("#editPickingForm #auditType").val(data.auditType)
			}
		}
	});
}
//增加
var resubmitReceiveMaterielConfig = {
		width		: "1600px",
		modalName	: "editPickingFormModal",
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
					let jgdm= $("#editPickingForm #jgdm").val()
					if(!jgdm){
						$.alert("所选部门存在异常！");
						return false;
					}
					var json = [];
					var lllx = $('#editPickingForm #lllx').val();
					var sczlid = $('#editPickingForm #sczlid').val();
					if (lllx=='5'&&(sczlid==null || sczlid=='')) {
						$.error('生产领料需要关联生产单！')
						return false;
					}
					var message = $("#editPickingForm #message").val();
					if(message!=null && message!=""){
						$.alert(message);
						return false;
					}
					if(t_map.rows != null && t_map.rows.length > 0){
						let wlids = "";
						var queryWlids = "";
						for(var i=0;i<t_map.rows.length;i++){
							if(t_map.rows[i].qlsl==null || t_map.rows[i].qlsl==''){
								$.alert("领料数量不能为空！");
								return false;
							}
							if(parseFloat(t_map.rows[i].qlsl)>parseFloat(t_map.rows[i].kqls)){
								$.alert(t_map.rows[i].ckmc+"-"+t_map.rows[i].wlbm+"-"+t_map.rows[i].wlmc+"请领数不能大于可请领数！");
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
							var sz = {"hwllid":'',"ckhwid":'',"ckid":'',"wlid":'',"qlsl":'',"bz":'',"xmdl":'',"xmbm":'',"qlyds":'',"cplx":'',"hwllmx_json":''};
	    					sz.hwllid = t_map.rows[i].hwllid;
							sz.ckhwid = t_map.rows[i].ckhwid;
							sz.ckid = t_map.rows[i].ckid;
	    					sz.wlid = t_map.rows[i].wlid;
	    					sz.qlsl = t_map.rows[i].qlsl;
							sz.bz = t_map.rows[i].hwllxxbz;
	    					sz.xmdl = t_map.rows[i].xmdl;
	    					sz.xmbm = t_map.rows[i].xmbm;
	    					sz.qlyds = t_map.rows[i].qlyds;
	    					sz.cplx = t_map.rows[i].cplx;
	    					sz.hwllmx_json = t_map.rows[i].hwllmx_json;
	    					json.push(sz);
							wlids = wlids + "," + t_map.rows[i].wlid;
							queryWlids = queryWlids + "," + t_map.rows[i].wlid;
						}
						wlids = wlids.substr(1);
						queryWlids = queryWlids.substr(1);
						confirmAuditType(wlids);
						$("#editPickingForm #queryWlids").val(queryWlids);
						$("#editPickingForm #llmx_json").val(JSON.stringify(json));
						$("#editPickingForm #tjbj").val("1");
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
								// var auditType = $("#editPickingForm #auditType").val();
								var ywid = $("#editPickingForm #llid").val();
								showAuditFlowDialog(responseText["auditType"],ywid,function(){
									$.closeModal(opts.modalName);
									searchReceiveMaterielResult();
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
 * 领料修改模态框
 */
var modRequisitionConfig={
		width		: "1600px",
		modalName	: "modRequisitionModal",
		formName	: "editPickingForm",
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
					let jgdm= $("#editPickingForm #jgdm").val()
					if(!jgdm){
						$.alert("所选部门存在异常！");
						return false;
					}
					var json = [];
					var lllx = $('#editPickingForm #lllx').val();
					var sczlid = $('#editPickingForm #sczlid').val();
					if (lllx=='5'&&(sczlid==null || sczlid=='')) {
						$.error('生产领料需要关联生产单！')
						return false;
					}
					var message = $("#editPickingForm #message").val();
					if(message!=null && message!=""){
						$.alert(message);
						return false;
					}
					if(t_map.rows != null && t_map.rows.length > 0){
						let wlids = "";
						var queryWlids = "";
						for(var i=0;i<t_map.rows.length;i++){
							if(t_map.rows[i].qlsl==null || t_map.rows[i].qlsl==''){
								$.alert("领料数量不能为空！");
								return false;
							}
							if(parseFloat(t_map.rows[i].qlsl)>parseFloat(t_map.rows[i].kqls)){
								$.alert(t_map.rows[i].ckmc+"-"+t_map.rows[i].wlbm+"-"+t_map.rows[i].wlmc+"请领数不能大于可请领数！");
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
							var sz = {"hwllid":'',"ckhwid":'',"ckid":'',"wlid":'',"qlsl":'',"bz":'',"xmdl":'',"xmbm":'',"qlyds":'',"cplx":'',"hwllmx_json":''};
	    					sz.hwllid = t_map.rows[i].hwllid;
							sz.ckhwid = t_map.rows[i].ckhwid;
							sz.ckid = t_map.rows[i].ckid;
	    					sz.wlid = t_map.rows[i].wlid;
	    					sz.qlsl = t_map.rows[i].qlsl;
							sz.bz = t_map.rows[i].hwllxxbz;
	    					sz.xmdl = t_map.rows[i].xmdl;
	    					sz.xmbm = t_map.rows[i].xmbm;
	    					sz.qlyds = t_map.rows[i].qlyds;
	    					sz.cplx = t_map.rows[i].cplx;
	    					sz.hwllmx_json=t_map.rows[i].hwllmx_json;
	    					json.push(sz);
							wlids = wlids + "," + t_map.rows[i].wlid;
							queryWlids = queryWlids + "," + t_map.rows[i].wlid;
						}
						wlids = wlids.substr(1);
						queryWlids = queryWlids.substr(1);
						confirmAuditType(wlids);
						$("#editPickingForm #queryWlids").val(queryWlids);
						$("#editPickingForm #llmx_json").val(JSON.stringify(json));
						$("#editPickingForm #tjbj").val("2");
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
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchReceiveMaterielResult();
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
* 领料复制模态框
*/
var copyRequisitionConfig={
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
				let jgdm= $("#editPickingForm #jgdm").val()
				if(!jgdm){
					$.alert("所选部门存在异常！");
					return false;
				}
				var message = $("#editPickingForm #message").val();
				if(message!=null && message!=""){
					$.alert(message);
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
							$.alert(t_map.rows[i].ckmc+"-"+t_map.rows[i].wlbm+"-"+t_map.rows[i].wlmc+"请领数不能大于可请领数！");
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
						var sz = {"hwllid":'',"ckhwid":'',"ckid":'',"wlid":'',"qlsl":'',"bz":'',"xmdl":'',"xmbm":'',"qlyds":'',"cplx":'',"hwllmx_json":''};
						sz.hwllid = t_map.rows[i].hwllid;
						sz.ckhwid = t_map.rows[i].ckhwid;
						sz.ckid = t_map.rows[i].ckid;
						sz.wlid = t_map.rows[i].wlid;
						sz.qlsl = t_map.rows[i].qlsl;
						sz.bz = t_map.rows[i].hwllxxbz;
						sz.xmdl = t_map.rows[i].xmdl;
						sz.xmbm = t_map.rows[i].xmbm;
						sz.qlyds = t_map.rows[i].qlyds;
						sz.cplx = t_map.rows[i].cplx;
						sz.hwllmx_json=t_map.rows[i].hwllmx_json;
						json.push(sz);
					}
					$("#editPickingForm #llmx_json").val(JSON.stringify(json));
					$("#editPickingForm #tjbj").val("2");
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
							var auditType = $("#editPickingForm #auditType").val();
							var receiveMateriel_params=[];
							receiveMateriel_params.prefix=$('#editPickingForm #urlPrefix').val();
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							//提交审核
							showAuditFlowDialog(auditType, responseText["ywid"],function(){
								searchReceiveMaterielResult();
							},null,receiveMateriel_params);
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
function searchReceiveMaterielResult(isTurnBack){
	//关闭高级搜索条件
	$("#receiveMaterielList_formSearch #searchMore").slideUp("low");
	ReceiveMateriel_turnOff=true;
	$("#receiveMaterielList_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#receiveMaterielList_formSearch #receiveMateriel_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#receiveMaterielList_formSearch #receiveMateriel_list').bootstrapTable('refresh');
	}
}

$(function(){
	 // 1.初始化Table
	var oTable = new ReceiveMateriel_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new ReceiveMateriel_ButtonInit();
    oButtonInit.Init();
    jQuery('#receiveMaterielList_formSearch .chosen-select').chosen({width: '100%'});
})