var t_map=[];
var qglbdm=$("#veriPurchase_Form #qglbdm").val();
var number = 0;
var sum=0;
var kcxxMap=[];
var cgmx_TableInit = function () {
    //初始化Table
    var qgid=$("#veriPurchase_Form #qgid").val();
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#veriPurchase_Form #cgmx_view").bootstrapTable({
			url: $("#veriPurchase_Form #urlPrefix").val()+'/purchase/purchase/pagedataPurchaseInfo?qgid='+qgid,         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#veriPurchase_Form #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   //是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "qgmx.xh",				//排序字段
			sortOrder: "asc",                   //排序方式
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
			uniqueId: "qgmxid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: [{
				title: '序',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '1%',
				align: 'left',
				visible:true
			},{
				field: 'qgmxid',
				title: '明细id',
				width: '5%',
				align: 'left',
				visible: false
			},{
				field: 'wlbm',
				title: '物料编码',
				titleTooltip:'物料编码',
				width: '5%',
				formatter:wlbmformat,
				align: 'left',
				visible: wlbmvisible(),
				sortable: true
			},{
				field: 'wlmc_t',
				title: wlmctitle(),
				width: '8%',
				align: 'left',
				formatter:wlmcformat,
				visible: true,
				sortable: true
			},{
				field: 'wllbmc',
				title: '物料类别',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: wllbmcvisible()
			},{
				field: 'gys',
				title: '生产厂家',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: gysvisible()
			},{
				field: 'wlzlbmc',
				title: '物料子类别',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: wlzlbvisible()
			},{
				field: 'gg_t',
				title: '规格',
				width: '4%',
				align: 'left',
				formatter:ggformat,
				sortable: true,
				visible: ggvisible()
			},{
				field: 'ychh',
				title: '货号',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: ychhvisible()
			},{
				field: 'sl',
				title: '数量',
				width: '4%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'rksl',
				title: '入库数量',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: rkslvisible()
			},{
				field: 'jldw_t',
				title: '单位',
				width: '1%',
				align: 'left',
				formatter:jldwformat,
				sortable: true,
				visible: jldwvisible()
			},{
				field: 'cpzch',
				title: '产品注册号',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: cpzchvisible()
			},{
				field: 'qwrq',
				title: '期望到货',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: qwdhvisible()
			},{
				field: 'hwyt',
				title: '货物用途',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: hwytvisible()
			},{
				field: 'hwbz',
				title: '服务期限',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: fwqxvisible()
			},{
				field: 'yq',
				title: '服务要求',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: fwyqvisible()
			},{
				field: 'wbyq',
				title: '维保要求',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: wbyqvisible()
			},{
				field: 'zlyqmc',
				title: '质量要求',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: zlyqvisible()
			},{
				field: 'pzyq',
				title: '配置要求',
				width: '8%',
				align: 'left',
				sortable: true,
				visible:pzyqvisible()
			},{
				field: 'bz',
				title: '备注',
				width: '12%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'fj',
				title: '附',
				width: '1%',
				formatter:fjformat,
				align: 'left',
				visible: true
			},{
				field: 'qxqg',
				title: '请购',
				width: '1%',
				formatter:qxqgformat,
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'zt',
				title: '审核',
				width: '1%',
				formatter:shformat,
				align: 'left',
				sortable: true,
				visible: true
			}],
			onLoadSuccess:function(map){
				t_map=map;
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
			rowStyle: function (row, index) {
				//这里有5个取值代表5中颜色['active', 'success', 'info', 'warning', 'danger'];
				var strclass = "";
				if (row.zt == "15") {
					strclass = 'danger';//还有一个active
				}
				else if (row.zt == "30") {
					strclass = 'warning';
				}
				else if (row.zt == "80") {
					strclass = 'success';
				}
				else {
					return {};
				}
				return { classes: strclass }
			},
		})
		$("#veriPurchase_Form #cgmx_view").colResizable({
			liveDrag:true,
			gripInnerHtml:"<div class='grip'></div>",
			draggingClass:"dragging",
			resizeMode:'fit',
			postbackSafe:true,
			partialRefresh:true}
		);
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			pageSize: params.limit,   // 页面大小
			pageNumber: (params.offset / params.limit) + 1,  // 页码
			access_token:$("#ac_tk").val(),
			sortName: params.sort,      // 排序列名
			sortOrder: params.order, // 排位命令（desc，asc）
			sortLastName: "qgmx.qgmxid", // 防止同名排位用
			sortLastOrder: "asc" // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return map;
	};
	return oTableInit
}

var sbxx_TableInit = function () {
	//初始化Table
	var qgid=$("#veriPurchase_Form #qgid").val();
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#veriPurchase_Form #sbxxTab_list").bootstrapTable({
			url: $("#veriPurchase_Form #urlPrefix").val()+'/inspectionGoods/inspectionGoods/pagedataPurchaseEquipment?',         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#veriPurchase_Form #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   //是否显示分页（*）
			paginationShowPageGo: false,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "sbys.lrsj",				//排序字段
			sortOrder: "asc",                   //排序方式
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
			uniqueId: "sbysid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: [{
				title: '序',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '2%',
				align: 'left',
				visible:true
			},{
				field: 'wlbm',
				title: '物料编码',
				titleTooltip:'物料编码',
				width: '4%',
				align: 'left',
				visible: true,
				sortable: true
			},{
				field: 'wlmc',
				title: "物料名称",
				width: '8%',
				align: 'left',
				visible: true,
				sortable: true,
				formatter:sbmcformat,
			},{
				field: 'ggxh',
				title: "规格",
				width: '8%',
				align: 'left',
				visible: true,
				sortable: true
			},{
				field: 'sbbh',
				title: "设备编号",
				width: '4%',
				align: 'left',
				visible: true,
				sortable: true
			},{
				field: 'gdzcbh',
				title: '设备固定资产编号',
				titleTooltip:'设备固定资产编号',
				width: '6%',
				align: 'left',
				visible: true,
				sortable: true
			},{
				field: 'xlh',
				title: '设备序列号',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'sbccbh',
				title: '设备出厂编号',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'scph',
				title: '生产批号',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'sydd',
				title: '使用地点',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}],
			onLoadSuccess:function(map){
//				t_map=map;
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
		})
		// $("#veriPurchase_Form #sbxxTab_list").colResizable({
		// 	liveDrag:true,
		// 	gripInnerHtml:"<div class='grip'></div>",
		// 	draggingClass:"dragging",
		// 	resizeMode:'fit',
		// 	postbackSafe:true,
		// 	partialRefresh:true}
		// );
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			pageSize: 1,   // 页面大小
			pageNumber: 1,  // 页码
			access_token:$("#ac_tk").val(),
			sortName: params.sort,      // 排序列名
			sortOrder: params.order, // 排位命令（desc，asc）
			sortLastName: "", // 防止同名排位用
			sortLastOrder: "" ,// 防止同名排位用
			qgid:qgid
			// 搜索框使用
			// search:params.search
		};
		return map;
	};
	return oTableInit
}


function wlmctitle(){
	if(qglbdm=='SERVICE'){
		return "服务名称";
	}else if(qglbdm=='DEVICE'){
		return "设备名称";
	}else if(qglbdm=='ADMINISTRATION'){
		return "货物名称";
	}
	return "物料名称";
}
function wbyqvisible(){
	if(qglbdm=='DEVICE')
		return true;
	return false;
}

function pzyqvisible(){
	if(qglbdm=='DEVICE')
		return true;
	return false;
}
function fwqxvisible(){
	if(qglbdm=='SERVICE')
		return true;
	return false;
}

function rkslvisible(){
	if(qglbdm=='SERVICE'||qglbdm=='ADMINISTRATION')
		return false;
	return true;
}
function zlyqvisible(){
	if(qglbdm=='DEVICE')
		return true;
	return false;
}
function fwyqvisible(){
	if(qglbdm=='SERVICE')
		return true;
	return false;
}
function hwytvisible(){
	if(qglbdm=='SERVICE')
		return true;
	return false;
}

function gysvisible(){
	if(qglbdm=='SERVICE')
		return true;
	return false;
}

function jgvisible(){
	if(qglbdm=='SERVICE')
		return false;
	return true;
}

function jldwvisible(){
	if(qglbdm=='SERVICE')
		return false;
	return true;
}
function cpzchvisible(){
	if(qglbdm=="MATERIAL"  || qglbdm==null ||qglbdm=='DEVICE' ||qglbdm=="OUTSOURCE"){
		return true;
	}
	return false;
}

function ggvisible(){
	if(qglbdm=='SERVICE')
		return false;
	return true;
}
function qwdhvisible(){
	if(qglbdm=="MATERIAL"  || qglbdm==null ||qglbdm=='ADMINISTRATION' ||qglbdm=="OUTSOURCE"){
		return true;
	}
	return false;
}

function ychhvisible(){
	if(qglbdm=="MATERIAL"  || qglbdm=='DEVICE' || qglbdm==''||qglbdm=="OUTSOURCE"){
		return true;
	}
	return false;
}

function wlzlbtcvisible(){
	if(qglbdm=="MATERIAL" ){
		return true;
	}
	return false;
}

function wlzlbvisible(){
	if(qglbdm=="MATERIAL"  || qglbdm==null || qglbdm=='DEVICE' ||qglbdm=="OUTSOURCE"){
		return true;
	}
	return false;
}

function wllbmcvisible(){
	if(qglbdm=="MATERIAL"  || qglbdm==null || qglbdm=='DEVICE' ||qglbdm=="OUTSOURCE"){
		return true;
	}
	return false;
}

function wlbmvisible(){
	if(qglbdm=="MATERIAL"  || qglbdm=="DEVICE" ||qglbdm=="OUTSOURCE" || qglbdm==""){
		return true;
	}
	return false;
}

function fjformat(value,row,index){
	var html = "<div id='fj"+row.wlid+"'>"; 
	html += "<input type='hidden'>";
	if(row.fjbj == "0"){
		html += "<a href='javascript:void(0);' onclick='uploadShoppingFile(\"" + index + "\")' >是</a>";
	}else{
		html += "<a href='javascript:void(0);' onclick='uploadShoppingFile(\"" + index + "\")' >否</a>";
	}
	html += "</div>";
	return html;
}

function qgqxmxTabshzt(ywid,shlb){
	var url = $("#veriPurchase_Form #urlPrefix").val()+"/systemcheck/auditProcess/auditView?ywid="+ywid+"&shlb="+shlb+"&access_token="+$("#ac_tk").val();
	$.showDialog(url,'查看审核',viewMaterCompareConfig);
}
var viewMaterCompareConfig = {
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

function veriPurchase_queryByWlbm(wlid){
	var url=$("#veriPurchase_Form #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'物料信息',WlConfig);	
}
var WlConfig = {
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

 // 请购格式化
function qxqgformat(value,row,index){
	var html="";
		if(row.qxqg=='0'){
			html="<span style='color:green;'>正常</span>";
		}else{
			html="<span style='color:red;'>取消</span>";
	}

	return html;	
}
//审核格式化zt
function shformat(value,row,index){
	var html="";
		if(row.zt=='00'||row.zt=='15'){
			html="<span style='color:red;'>拒绝</span>";
		}else{			
			html="<span style='color:green;'>正常</span>";
	}

	return html;	
}

function wlmcformat(value,row,index){
	return row.wlmc==null?row.hwmc:row.wlmc;
}

function ggformat(value,row,index){
	return row.gg==null?row.hwbz:row.gg;
}

function jldwformat(value,row,index){
	return row.jldw==null?row.hwjldw:row.jldw;
}

function wlbmformat(value,row,index){
	var html = "";
	if(row.wlbm==null){
		html += "<span></span>"
	}else{
		html += "<a href='javascript:void(0);' onclick=\"queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
	}
	return html;
}
function sbmcformat(value,row,index) {
	var html = "";
	if(row.wlmc==null){
		html += "<span></span>"
	}else{
		html += "<a href='javascript:void(0);' onclick=\"queryByGdzcbh('"+row.sbysid+"')\">"+row.wlmc+"</a>";
	}
	return html;
}
function queryByWlbm(wlid){
	var url=$("#veriPurchase_Form #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'物料信息',viewWlConfig);	
}
function queryByGdzcbh(sbysid){
	var url=$("#veriPurchase_Form #urlPrefix").val()+"/inspectionGoods/inspectionGoods/pagedataEquipmentAcceptanceOA?sbysid="+sbysid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'设备信息',viewSbConfig);
}
var viewWlConfig = {
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
var viewSbConfig = {
	width		: "1300px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
//请购明细上传附件
function uploadShoppingFile(index) {
	var url = "/production/purchase/viewFilePage?access_token=" + $("#ac_tk").val();
	if(index){
		var qgid = $("#veriPurchase_Form #qgid").val();
		var wlid = t_map.rows[index].wlid;
		var fjids = $("#veriPurchase_Form #fj" + wlid+" input").val();
		var qgmxid = t_map.rows[index].qgmxid;
		if(!fjids){
			url="/production/purchase/pagedataGetUploadFile?access_token=" + $("#ac_tk").val()+"&qgmxid="+qgmxid+"&qgid="+qgid+"&wlid="+wlid+"&ckbj=1";
		}else{
			url="/production/purchase/pagedataGetUploadFile?access_token=" + $("#ac_tk").val()+"&qgmxid="+qgmxid+"&qgid="+qgid+"&wlid="+wlid+"&ckbj=1"+"&fjids="+fjids;
		}
	}
	$.showDialog($("#veriPurchase_Form #urlPrefix").val()+url, '查看附件', viewFileConfig);
}

var viewFileConfig = { 
		width : "800px",
		height : "500px",
		modalName	: "uploadFileModal",
		formName	: "ajaxForm",
		offAtOnce : true, // 当数据提交成功，立刻关闭窗口
		buttons : {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					var $this = this;
					var opts = $this["options"]||{};
					$.closeModal(opts.modalName);
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

function view(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url="/ws/sjxxpripreview?fjid="+fjid
		$.showDialog($("#veriPurchase_Form #urlPrefix").val()+url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= "/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog($("#veriPurchase_Form #urlPrefix").val()+url,'文件预览',JPGMaterConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}
var JPGMaterConfig = {
	width		: "800px",
	offAtOnce	: true,  
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
function xz(fjid){
    jQuery('<form action="'+$("#veriPurchase_Form #urlPrefix").val()+'/ws/production/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '</form>')
    .appendTo('body').submit().remove();
}
function del(fjid,wjlj){
	$.confirm('您确定要删除所选择的记录吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url= $("#veriPurchase_Form #urlPrefix").val()+"/common/file/delFile";
			jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
				setTimeout(function(){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							$("#"+fjid).remove();
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

var kcxx_TableInit = function () {
    //初始化Table
    var qgid=$("#veriPurchase_Form #qgid").val();
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#veriPurchase_Form #kcxx_view").bootstrapTable({
			url: $("#veriPurchase_Form #urlPrefix").val()+'/purchase/purchase/pagedataQueryKcxx?qgid='+qgid,         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#veriPurchase_Form #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   //是否显示分页（*）
			paginationShowPageGo: false,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "mx.xh",				//排序字段
			sortOrder: "asc",                   //排序方式
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
			uniqueId: "qgmxid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: [{
				title: '序',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '2%',
				align: 'left',
				visible:true
			},{
                field: 'ckmc',
                title: '仓库',
                width: '5%',
                align: 'left',
                visible: true
            },{
				field: 'wlbm',
				title: '物料编码',
				width: '5%',
				align: 'left',
				visible: true
			},{
				field: 'wlmc',
				title: '物料名称',
				width: '15%',
				align: 'left',
				visible: true
			},{
				field: 'gg',
				title: '规格',
				width: '15%',
				align: 'left',
				visible: true
			},{
				field: 'sl',
				title: '明细数量',
				width: '5%',
				align: 'left',
				visible: true
			},{
				field: 'kcl',
				title: '库存量',
				width: '5%',
				align: 'left',
				visible: true
			},{
                field: 'klsl',
                title: '可领数量',
                width: '5%',
                align: 'left',
                visible: true
            },{
				field: 'bz',
				title: '备注',
				width: '25%',
				align: 'left',
				sortable: true,
				visible: true
			},{
                field: 'll',
                title: '领料',
                width: '10%',
                align: 'left',
                formatter:formSearch_llformat,
                visible: true
            }],
			onLoadSuccess:function(map){
                kcxxMap = map;
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
		})
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			pageSize: 1,   // 页面大小
			pageNumber:  1,  // 页码
			access_token:$("#ac_tk").val(),
			sortName: params.sort,      // 排序列名
			sortOrder: params.order, // 排位命令（desc，asc）
			sortLastName: "qgmx.qgmxid", // 防止同名排位用
			sortLastOrder: "asc" // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return map;
	};
	return oTableInit
}


function formSearch_llformat(value,row,index) {
	var idswl = $("#veriPurchase_Form #idswl").val().split(",");
	var klsl = parseFloat(row.klsl);
	if(row.klsl!=null && row.klsl!="" && klsl>0){
        for(var i=0;i<idswl.length;i++){
            if(row.ckhwid==idswl[i]){
                return "<div id='wl"+row.ckhwid+"'><span id='t"+row.ckhwid+"' class='btn btn-danger' title='移出领料车' onclick=\"delPickingCarT('" + row.ckhwid + "',event)\" >取消</span></div>";
            }
        }
        return "<div id='wl"+row.ckhwid+"'><span id='t"+row.ckhwid+"' class='btn btn-info' title='加入领料车' onclick=\"addPickingCarT('" + row.ckhwid + "',event)\" >领料</span></div>";
	}
	return "";
}

//添加至领料车
function addPickingCarT(ckhwid,event){
    $.ajax({
        async:false,
        type:'post',
        url:$('#veriPurchase_Form #urlPrefix').val()+"/storehouse/requisition/pagedataQueryWlxxByWlid",
        cache: false,
        data: {"ckhwid":ckhwid,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            //返回值
            if(data.ckhwxxDto!=null && data.ckhwxxDto!=''){
                //kcbj=1 有库存，kcbj=0 库存未0
                if(data.ckhwxxDto.kcbj!='1'){
                    $.confirm("该物料库存不足！");
                }else{
                    $.ajax({
                        type:'post',
                        url:$('#veriPurchase_Form #urlPrefix').val()+"/storehouse/stock/pagedataAddToPickingCar",
                        cache: false,
                        data: {"ckhwid":ckhwid,"access_token":$("#ac_tk").val()},
                        dataType:'json',
                        success:function(data){
                            //返回值
                            if(data.status=='success'){
                                $("#idswl").val(data.idswl);
                                $("#veriPurchase_Form #t"+ckhwid).remove();
                                var html="<span id='t"+ckhwid+"' class='btn btn-danger' title='移出领料车' onclick=\"delPickingCarT('" + ckhwid + "',event)\" >取消</span>";
                                $("#veriPurchase_Form #wl"+ckhwid).append(html);
                            }
                        }
                    });
                }
            }else{
                $.confirm("该物料不存在!");
            }
        }
    });
}

//从领料车中删除
function delPickingCarT(ckhwid,event){
    $.ajax({
        type:'post',
        url:$('#veriPurchase_Form #urlPrefix').val()+"/storehouse/stock/pagedataDelFromPickingCar",
        cache: false,
        data: {"ckhwid":ckhwid,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            //返回值
            if(data.status=='success'){
                $("#idswl").val(data.idswl);
                $("#veriPurchase_Form #t"+ckhwid).remove();
                var html="<span id='t"+ckhwid+"' class='btn btn-info' title='加入领料车' onclick=\"addPickingCarT('" + ckhwid + "',event)\" >领料</span>";
                $("#veriPurchase_Form #wl"+ckhwid).append(html);
            }
        }
    });
}


function allAdd(){
    if(kcxxMap.rows!=null && kcxxMap.rows.length > 0){
        var ids = "";
        for(var i=0;i<kcxxMap.rows.length;i++){
            if(parseFloat(kcxxMap.rows[i].klsl)>0){
                ids = ids + "," + kcxxMap.rows[i].ckhwid;
            }
        }
        ids =ids.substr(1);
        $.ajax({
            type:'post',
            url:$('#veriPurchase_Form #urlPrefix').val()+"/purchase/purchase/pagedataAddPickingCar",
            cache: false,
            data: {"ids":ids,"access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(data){
                //返回值
                if(data.status=='success'){
                    $("#idswl").val(data.idswl);
                     for(var i=0;i<kcxxMap.rows.length;i++){
                         var ckhwid = kcxxMap.rows[i].ckhwid;
                         $("#veriPurchase_Form #t"+ckhwid).remove();
                         var html="<span id='t"+ckhwid+"' class='btn btn-danger' title='移出领料车' onclick=\"delPickingCarT('" + ckhwid + "',event)\" >取消</span>";
                         $("#veriPurchase_Form #wl"+ckhwid).append(html);
                     }
                }
            }
        });
    }
}

function allDel(){
    if(kcxxMap.rows!=null && kcxxMap.rows.length > 0){
        var ids = "";
        for(var i=0;i<kcxxMap.rows.length;i++){
            if(parseFloat(kcxxMap.rows[i].klsl)>0){
                ids = ids + "," + kcxxMap.rows[i].ckhwid;
            }
        }
        ids =ids.substr(1);
        $.ajax({
            type:'post',
            url:$('#veriPurchase_Form #urlPrefix').val()+"/purchase/purchase/pagedataDelPickingCar",
            cache: false,
            data: {"ids":ids,"access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(data){
                //返回值
                if(data.status=='success'){
                    $("#idswl").val(data.idswl);
                     for(var i=0;i<kcxxMap.rows.length;i++){
                         var ckhwid = kcxxMap.rows[i].ckhwid;
                         $("#veriPurchase_Form #t"+ckhwid).remove();
                         var html="<span id='t"+ckhwid+"' class='btn btn-info' title='加入领料车' onclick=\"addPickingCarT('" + ckhwid + "',event)\" >领料</span>";
                         $("#veriPurchase_Form #wl"+ckhwid).append(html);
                     }
                }
            }
        });
    }

}


$(function(){
	//0.界初始化
	//1.初始化Table
	var oTable = new cgmx_TableInit();
	var oTable1 = new sbxx_TableInit();
    var oTable2 = new kcxx_TableInit();
	oTable.Init();
	oTable1.Init();
	oTable2.Init();
	 $("#qgmx").click(function(){
            setTimeout(function(){
                $("#veriPurchase_Form #cgmx_view").initResize();
            },1000)

     })
})