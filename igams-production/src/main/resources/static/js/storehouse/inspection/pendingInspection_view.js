function queryByWlbm(wlid){
	var url=$("#pendingInspectionViewForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'物料详细信息',viewWlConfig);	
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
function queryQgxx(qgid){
	var url=$("#pendingInspectionViewForm #urlPrefix").val()+"/purchase/purchase/viewPurchase?qgid="+qgid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'请购详细信息',qgxxViewConfig);	
}
var qgxxViewConfig = {
	width		: "1600px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
function queryDhxx(dhid){
	var url=$("#pendingInspectionViewForm #urlPrefix").val()+"/storehouse/arrivalGoods/pagedataViewArrivalGoods?dhid="+dhid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'到货详细信息',dhxxViewConfig);	
}
var dhxxViewConfig = {
	width		: "1600px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
function queryDhjyxx(dhjyid){
	var val = $("#pendingInspectionViewForm #lbcskz1").val();
	if(val=='3'){
		var url=$("#pendingInspectionViewForm #urlPrefix").val()+"/device/device/viewDeviceCheck?hwid="+dhjyid+"&access_token=" + $("#ac_tk").val();
		$.showDialog(url,'详细信息',deviceViewConfig);
	}else{
		var url=$("#pendingInspectionViewForm #urlPrefix").val()+"/inspectionGoods/inspectionGoods/viewInspectionGoods?dhjyid="+dhjyid+"&access_token=" + $("#ac_tk").val();
		$.showDialog(url,'到货检验详细信息',dhjyxxViewConfig);
	}
}

/**
 * 查看页面模态框
 */
var deviceViewConfig={
	width		: "1000px",
	modalName	:"viewDeviceCheckModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}
var dhjyxxViewConfig = {
	width		: "1600px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
function queryRkxx(rkid){
	var url=$("#pendingInspectionViewForm #urlPrefix").val()+"/warehouse/putInStorage/pagedataViewPutInStorage?rkid="+rkid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'货物入库详细信息',rkxxViewConfig);	
}
var rkxxViewConfig = {
	width		: "1600px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};