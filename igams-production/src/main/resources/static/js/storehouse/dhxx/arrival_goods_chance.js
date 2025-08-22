function chanceType(cskz1,csdm,csid,cskz3){
	var modalName = this.chanceTypeConfig.modalName;
	var dhlxdm = $("#dh_rklbtype_ajaxForm #dhlxdm").val();
	var url=$("#dh_rklbtype_ajaxForm #urlPrefix").val()+$("#dh_rklbtype_ajaxForm #url").val()+"?cskz1="+cskz1+"&rklb="+csid+"&rklbdm="+csdm+"&dhlxdm="+dhlxdm+"&cskz3="+cskz3;
	$.closeModal(modalName);
	var cofig=this.addArrivalGoodsConfig;
	$.showDialog(url,'新增到货信息',cofig);
}