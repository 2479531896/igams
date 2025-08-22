function chanceType(csdm,csid){	
	if('QT'==csdm){
		var url=$("#chooseInstore_ajaxForm #urlPrefix").val()+'/purchase/purchase/pagedataInstoreOtherPurchase?rklb='+csid;
		var modalName = this.chanceInstoreTypeConfig.modalName;
		$.closeModal(modalName);
		var cofig=this.instoreOtherConfig;
		$.showDialog(url,'其他入库',cofig);
	}else{
		var url=$("#chooseInstore_ajaxForm #urlPrefix").val()+'/purchase/purchase/pagedataInstorePurchase?ids='+$("#chooseInstore_ajaxForm #ids").val()+'&rklb='+csid;
		var modalName = this.chanceInstoreTypeConfig.modalName;
		$.closeModal(modalName);
		var cofig=this.instoreConfig;
		$.showDialog(url,'请购入库',cofig);
	}
}