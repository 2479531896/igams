function chanceType(csdm,csid){
    var modalName = this.chanceSupplierTypeConfig.modalName;
    var url=$("#server_ajaxForm #urlPrefix").val()+$("#server_ajaxForm #url").val()+"?server="+csid+"&gysid="+$("#server_ajaxForm #gysid").val();
    $.closeModal(modalName);
    var cofig=this.copySupplierConfig;
    $.showDialog(url,'复制供应商信息',cofig);
}