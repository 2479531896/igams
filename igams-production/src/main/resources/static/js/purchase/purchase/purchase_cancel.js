var qxqg="取消采购";
var shzt="";//当前单据审核状态
var t_map=[];
var qglbdm=$("#purchaseCancelForm #qglbdm").val();

// 请购高级修改，采购部审核显示字段
var qxgjxgColumnsArray = [
	{
		title: '序号',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序号',
		width: '1%',
        align: 'left',
        visible:true
	},{
	    field: 'wlbm',
	    title: '物料编码',
	    width: '8%',
	    align: 'left',
	    sortable: true,
	    visible: purchaseCancelwlbmVisible()
	},{
	    field: 'wlmc',
	    title: '物料名称',
	    width: '8%',
	    align: 'left',
	    formatter:purchaseCancelwlmcformat,
	    sortable: true,
	    visible: true
	},{
	    field: 'qglbmc',
	    title: '类别',
	    width: '3%',
	    align: 'left',
	    sortable: true,
	    visible: true
	},{
	    field: 'hwbz',
	    title: purchaseCancelhwbztitle(),
	    width: '5%',
	    align: 'left',
	    sortable: true,
		formatter:purchaseCancelhwbzformat,
	    visible: purchaseCancelhwbzVisible()
	},{
	    field: 'hwyt',
	    title: purchaseCancelhwyttitle(),
	    width: '5%',
	    align: 'left',
	    sortable: true,
	    visible: purchaseCancelhwytVisible()
	},{
	    field: 'wllbmc',
	    title: '物料类别',
	    width: '8%',
	    align: 'left',
	    sortable: true,
	    visible: purchaseCancelwllbVisible()
	},{
	    field: 'wlzlbmc',
	    title: '物料子类别',
	    width: '8%',
	    align: 'left',
	    sortable: true,
	    visible: purchaseCancelwlzlbVisible()
	}, {
        field: 'wlzlbtcmc',
        title: '子类别统称',
        titleTooltip:'子类别统称',
        width: '6%',
        align: 'left',
        visible: purchaseCancelwlzlbtcVisible()
    }, {
	    field: 'qxsl',
	    title: '取消数量',
	    width: '5%',
	    align: 'left',
	    formatter:qxslformat,
	    sortable: true,
	    visible: true
	}, {
	    field: 'kqxsl',
	    title: '可取消数量',
	    width: '5%',
	    align: 'left',
	    sortable: true,
	    visible: true
	}, {
	    field: 'sl',
	    title: '请购数量',
	    width: '5%',
	    align: 'left',
	    sortable: true,
	    visible: true
	},{
	    field: 'jldw',
	    title: '单位',
	    width: '5%',
	    align: 'left',
	    formatter:purchaseCanceljldwformat,
	    sortable: true,
		visible: jldwvisible()
	},{
	    field: 'jg',
	    title: '价格',
	    width: '5%',
	    align: 'left',
	    sortable: true,
	    visible: false
	},{
	    field: 'bz',
	    title: '备注',
	    width: '5%',
	    align: 'left',
	    formatter:bzformat,
	    sortable: true,
	    visible: true
	},{
	    field: 'zt',
	    title: '状态',
	    width: '5%',
	    align: 'left',
	    formatter:ztformat,
	    sortable: true,
	    visible: true
	}]

var ShoppingCar_TableInit=function(){
	var url = $("#purchaseCancelForm #url").val();
	if($("#purchaseCancelForm #fwbj").val() != null && $("#purchaseCancelForm #fwbj").val() != ""){
		url = $("#purchaseCancelForm #fwbj").val()+url;
	}
	var flag=$("#purchaseCancelForm #flag").val();
	var sortName=null;
	var uniqueId=null;
	var sortLastName=null;
	var columnsArray=null;
	sortName = "qgmx.xh";
	uniqueId = "qgmxid";
	sortLastName = "qgmx.qgmxid";
	columnsArray = qxgjxgColumnsArray;
	
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#purchaseCancelForm #tb_list").bootstrapTable({
			url: $("#purchaseCancelForm #urlPrefix").val()+url,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#purchaseCancelForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: sortName,				//排序字段
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
            uniqueId: uniqueId,                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            paginationDetailHAlign:' hidden',
            columns: columnsArray,
            onLoadSuccess:function(map){
            	t_map=map;
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
             },
		});
		$("#purchaseCancelForm #tb_list").colResizable({
			liveDrag:true, 
			gripInnerHtml:"<div class='grip'></div>", 
			draggingClass:"dragging", 
			resizeMode:'fit', 
			postbackSafe:true,
			partialRefresh:true
		})
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: sortLastName, // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getShoppingSearchData(map);
	};
	return oTableInit;
}

function purchaseCancelhwytVisible(){
	if(qglbdm!='MATERIAL' && qglbdm!='ADMINISTRATION' && qglbdm!=null && qglbdm!=''){
		return true;
	}
	return false;
}

//货物用途标题格式化
function purchaseCancelhwyttitle(){
	if(qglbdm=='DEVICE'){
		return "设备用途";
	}else if(qglbdm=='SERVICE'){
		return "服务用途";
	}
}

function purchaseCancelhwbzVisible(){
	if(qglbdm!='MATERIAL' && qglbdm!=null && qglbdm!=''){
		return true;
	}
	return false;
}
function jldwvisible(){
	if(qglbdm=='SERVICE')
		return false;
	return true;
}
//货物标准标题格式化
function purchaseCancelhwbztitle(){
	if(qglbdm=='DEVICE'){
		return "规格型号";
	}else if(qglbdm=='SERVICE'){
		return "服务期限";
	}else if(qglbdm=='ADMINISTRATION'){
		return "规格型号";
	}
}

//单位格式化
function purchaseCanceljldwformat(value,row,index){
	if(qglbdm=='MATERIAL' || qglbdm==null || qglbdm==''){
		return row.jldw;
	}
	return row.hwjldw;
}
//控制请购类型只有物料时显示物料子类别统称
function purchaseCancelwlzlbtcVisible(){
	if(qglbdm=='MATERIAL' || qglbdm==null || qglbdm==''){
		return true;
	}
	return false;
}
//控制请购类型只有物料时显示物料子类别
function purchaseCancelwlzlbVisible(){
	if(qglbdm=='MATERIAL' || qglbdm==null || qglbdm==''){
		return true;
	}
	return false;
}
//控制请购类型只有物料时显示物料类别
function purchaseCancelwllbVisible(){
	if(qglbdm=='MATERIAL' || qglbdm==null || qglbdm==''){
		return true;
	}
	return false;
}
//控制请购类型只有物料时显示物料编码
function purchaseCancelwlbmVisible(){
	if(qglbdm=='MATERIAL' || qglbdm==null || qglbdm==''){
		return true;
	}
	return false;
}

//物料名称显示内容格式化
function purchaseCancelwlmcformat(value,row,index){
	// if(qglbdm=='MATERIAL' || qglbdm==null || qglbdm==''){
	// 	return row.wlmc;
	// }
	if(row.wlmc!=null && row.wlmc!=''){
		return row.wlmc;
	}
	return row.hwmc;
}
//货物标准显示内容格式化
function purchaseCancelhwbzformat(value,row,index){
	if(row.gg!=null && row.gg!=''){
		return row.gg;
	}
	return row.hwbz;
}
/**
 * 组装列表查询条件(未使用)
 * @param map
 * @returns
 */
function getShoppingSearchData(map){
	var cxtj=$("#purchaseCancelForm #cxtj").val();
	var cxnr=$.trim(jQuery('#purchaseCancelForm #cxnr').val());
	if(cxtj=="0"){
		map["wlbm"]=cxnr;
	}else if(cxtj=="1"){
		map["wlmc"]=cxnr;
	}else if(cxtj=="2"){
		map["scs"]=cxnr;
	}else if(cxtj=="3"){
		map["ychh"]=cxnr;
	}
	return map;
}
function searchShoppingResult(isTurnBack){
	if(isTurnBack){
		$('#purchaseCancelForm #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#purchaseCancelForm #tb_list').bootstrapTable('refresh');
	}
}


/**
 * 备注格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function bzformat(value,row,index){
	if(row.bz == null){
		row.bz = "" ;
	}
	var html="<input id='bz_"+index+"' name='bz_"+index+"' value='"+row.bz+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'bz\')\"></input>";
	return html;
}

/**
 * 状态格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function ztformat(value,row,index){
	var html="";
	if(row.zt=='15'){
		html="<span id='mxzt_"+index+"' cskz='15' style='color:red;'>拒绝审批</span>";
	}else if(row.zt=='30'){
		html="<span id='mxzt_"+index+"' cskz='30' style='color:orange;'>取消采购</span>";
	}else if(row.zt=='80'){
		html="<span id='mxzt_"+index+"' cskz='80' style='color:green;'>正常采购</span>";
	}else{
		html="<span id='mxzt_"+index+"' cskz='80' style='color:green;'>正常采购</span>";
	}
	return html;
}


/**
 * 取消数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function qxslformat(value,row,index){
	if(row.qxsl == null){
		row.qxsl = "";
	}
	var html = "<input id='qxsl_"+index+"' value='"+row.qxsl+"' name='qxsl_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'qxsl\')\"></input>";
	return html;
}

//function sjslformat(value,row,index){
//	var html ="";
//	if(row.qxsl == null || row.qxsl == ""){
//		html ="<span id='sjsl_"+index+"' text='"+row.sl+"' value='"+row.sl+"' name='sjsl_"+index+"'>"+row.sl+"</span>";
//	}else{
//		html ="<span id='sjsl_"+index+"' text='"+row.sjsl+"' value='"+row.sjsl+"' name='sjsl_"+index+"'>"+row.sjsl+"</span>";
//	}
//	return html;
//}

/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wlbmformat(value,row,index){
	var html="";
	if(row.wlbm!=null && row.wlbm!=''){
		html="<span>"+row.wlbm+"</span><button type='button' class='glyphicon glyphicon-chevron-up' style='font-weight:100;float:right;width:35px;height:30px;border-radius:5px;border:0.5px solid black;' onclick='showImg(\""+index+"\")'></button>";
	}
	return html;
}



/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @param bj
 * @returns
 */
function checkSum(index,e,zdmc){
	var data=JSON.parse($("#purchaseCancelForm #qgqxmx_json").val());
	 
	for(var i=0;i<data.length;i++){
		if(data[i].index == index){
			if(zdmc=='bz'){
				data[i].bz=e.value;
				t_map.rows[i].bz=e.value;
			}
			if(zdmc=='qxsl'){
				//取消数量处理
				data[i].qxsl=e.value;
				t_map.rows[i].qxsl=e.value;
				if(parseFloat(e.value)>parseFloat(data[i].kqxsl)){
					$.confirm("取消请购数量不能大于可取消数量!");
					$("#purchaseCancelForm #qxsl_"+i).val("");
					return;
				}
			}
		}
	}
	$("#purchaseCancelForm #qgqxmx_json").val(JSON.stringify(data));
}


/**
 * 选择申请人
 * @returns
 */
function chooseSqr() {
	var url = $("#purchaseCancelForm #urlPrefix").val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择申请人', chooseQrrConfig);
}
var chooseQrrConfig = { 
	width : "800px",
	height : "500px",
	modalName	: "chooseSqrModal",
	formName	: "purchaseCancelForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#taskListFzrForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#purchaseCancelForm #sqr').val(sel_row[0].yhid);
					$('#purchaseCancelForm #sqrmc').val(sel_row[0].zsxm);
					$.closeModal(opts.modalName);
					//保存操作习惯
					$.ajax({ 
					    type:'post',  
					    url:$("#purchaseCancelForm #urlPrefix").val()+"/common/habit/commonModUserHabit",
					    cache: false,
					    data: {"dxid":sel_row[0].yhid,"access_token":$("#ac_tk").val()},  
					    dataType:'json', 
					    success:function(data){}
					});
	    		}else{
	    			$.error("请选中一行");
	    			return;
	    		}
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
 * 选择申请部门
 * @returns
 */
function chooseBm() {
	var url = $("#purchaseCancelForm #urlPrefix").val()+"/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择部门', chooseBmConfig);
}
var chooseBmConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseBmModal",
	formName	: "purchaseCancelForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#listDepartmentForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#purchaseCancelForm #sqbm').val(sel_row[0].jgid);
					$('#purchaseCancelForm #sqbmmc').val(sel_row[0].jgmc);
					$('#purchaseCancelForm #sqbmdm').val(sel_row[0].jgdm);
					$.closeModal(opts.modalName);
	    		}else{
	    			$.error("请选中一行");
	    			return;
	    		}
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function btnBind(){
	// 审核状态初始化
	shzt=$("#purchaseCancelForm #shzt").val();
	if(shzt=='10'){
		qxqg="拒绝审批";
	}
	// 默认人初始化
	getMrsqr();
	
	// 初始化明细json字段
	setTimeout(function() {
		setQxqgqxmx_json();
    	}, 1000);
}

/**
 * 添加默认申请人信息
 * @returns
 */
function getMrsqr(){
	var mrsqr=$("#purchaseCancelForm #sqr").val();
	var mrsqrmc=$("#purchaseCancelForm #sqrmc").val();
	var mrsqbm=$("#purchaseCancelForm #mrsqbm").val();
	var mrsqbmmc=$("#purchaseCancelForm #mrsqbmmc").val();
	var mrsqbmdm=$("#purchaseCancelForm #mrsqbmdm").val();
	$("#purchaseCancelForm #sqr").val(mrsqr);
	$("#purchaseCancelForm #sqrmc").val(mrsqrmc);
	$("#purchaseCancelForm #sqbm").val(mrsqbm);
	$("#purchaseCancelForm #sqbmmc").val(mrsqbmmc);
	$("#purchaseCancelForm #sqbmdm").val(mrsqbmdm);
}
/**
 * 初始化明细json字段
 * @returns
 */
function setQxqgqxmx_json(){
	var json=[];
	var data=$('#purchaseCancelForm #tb_list').bootstrapTable('getData');//获取选择行数据
	for(var i=0;i<data.length;i++){
		var kqxsl="";
		var ydsl=data[i].ydsl;
		if(ydsl==null || ydsl==''){
			ydsl=0;
		}
		if(ydsl != null && ydsl != ""){
			kqxsl=parseFloat(data[i].sysl)-parseFloat(ydsl);
		}else{
			kqxsl=data[i].sysl;
		}
		var sz={"index":i,"wlid":'',"yssl":'','qgmxid':'','zt':'',"qxsl":'',"sjsl":'',"sysl":'',"yssjsl":'',"yssysl":'',"kqxsl":'',"qgqxmxid":'',"qgid":'',"qgqxid":''};
		sz.wlid=data[i].wlid;
		sz.yssl=data[i].sl;
		sz.sjsl=data[i].sl;
		sz.qgmxid=data[i].qgmxid;
		sz.qgid=data[i].qgid;
		sz.qgqxid=data[i].qgqxid;
		sz.zt=data[i].zt;
		sz.sysl=data[i].sysl;
		sz.yssjsl=data[i].sl;
		sz.yssysl=data[i].sysl;
		sz.kqxsl=data[i].kqxsl;
		sz.qxsl=data[i].qxsl;
		sz.qgqxmxid=data[i].qgqxmxid;
		json.push(sz);
	}
	$("#purchaseCancelForm #qgqxmx_json").val(JSON.stringify(json));
}



$('body').on('click' , '.qwdhrq' , function() {
	var qgmxid=$(this).attr("qgmxid");
	$("#purchaseCancelForm #xzqgmx").val(qgmxid);
})

$(function(){
	//添加日期控件
	laydate.render({
	   elem: '#purchaseCancelForm #sqrq'
	  ,theme: '#2381E9'
	});
	//0.初始化fileinput
	var oFileInput = new FileInput();
	var purchaseCancel_params=[];
	purchaseCancel_params.prefix=$('#purchaseCancelForm #urlPrefix').val();
	oFileInput.Init("purchaseCancelForm","displayUpInfo",2,1,"pro_file",null,purchaseCancel_params);
	//初始化列表
	var oTable=new ShoppingCar_TableInit();
	oTable.Init();
	btnBind();
	jQuery('#purchaseCancelForm .chosen-select').chosen({width: '100%'});
})