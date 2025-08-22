function chanceType(cskz1,csdm,csid){
	var modalName = this.chancePuInTypeConfig.modalName;
	var url=$("#rklbtype_ajaxForm #urlPrefix").val()+$("#rklbtype_ajaxForm #url").val()+"?cskz1="+cskz1+"&rklb="+csid;
	$.closeModal(modalName);
	var cofig=this.addPutInStorageConfig;
	$.showDialog(url,'新增入库信息',cofig);
}