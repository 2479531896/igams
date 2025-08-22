function chanceType(csdm,csid){	
	var qglx=$("#qglbtype_ajaxForm #qglx").val();
	var wlflag=$("#qglbtype_ajaxForm #wlflag").val();
	var ljbj=$("#qglbtype_ajaxForm #ljbj").val();
	var url=$("#qglbtype_ajaxForm #urlPrefix").val()+$("#qglbtype_ajaxForm #url").val()+"?qglb="+csid+"&qglx="+qglx+"&ljbj="+ljbj;
	if("1"==wlflag){
		var modalName_wl = this.chancePurchaseTypeMaterConfig.modalName;
		$.closeModal(modalName_wl);
		var cofig=this.shoppingMaterConfig;
		if(cofig==undefined){
			cofig=this.shoppingMaterConfig;
		}
		$.showDialog(url,'新增请购信息',cofig);
	}else{
		var modalName = this.chancePurchaseTypeConfig.modalName;
		$.closeModal(modalName);
		var cofig=this.addPurchaseConfig;
		if(cofig==undefined){
			cofig=this.addPurchaseAllConfig;
		}
		$.showDialog(url,'新增请购信息',cofig);
	}	
}