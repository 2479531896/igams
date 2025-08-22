function viewmore(hwllid){
	$.ajax({
		type:'post',
		url:$("#purchaseAuditViewForm #urlPrefix").val()+"/ws/production/viewMoreHwllxx",
		cache: false,
		data: {"hwllid":hwllid,"urlPrefix":$("#purchaseAuditViewForm #urlPrefix").val()},
		dataType:'json',
		success:function(data){
			// 显示质量类别，物料类别，物料子类别，单位，生产商，货号
			var lbmc=data.hwllxxDto.lbmc;
			var wllbmc=data.hwllxxDto.wllbmc;
			var wlzlbmc=data.hwllxxDto.wlzlbmc;
			var jldw=data.hwllxxDto.jldw;
			var scs=data.hwllxxDto.scs;
			var ychh=data.hwllxxDto.ychh;
			if(lbmc==null){
				lbmc="";
			}
			if(wllbmc==null){
				wllbmc="";
			}
			if(wlzlbmc==null){
				wlzlbmc="";
			}
			if(jldw==null){
				jldw="";
			}
			if(scs==null){
				scs="";
			}
			if(ychh==null){
				ychh="";
			}
			//返回值
			$("#purchaseAuditViewForm #lbmc").text(lbmc);
			$("#purchaseAuditViewForm #wllbmc").text(wllbmc);
			$("#purchaseAuditViewForm #wlzlbmc").text(wlzlbmc);
			$("#purchaseAuditViewForm #jldw").text(jldw);
			$("#purchaseAuditViewForm #scs").text(scs);
			$("#purchaseAuditViewForm #ychh").text(ychh);
			$("#purchaseAuditViewForm #t_div").animate({left:'0px'},"slow");
			$("#purchaseAuditViewForm .tr").attr("style","background-color:white;");
			$("#purchaseAuditViewForm #"+hwllid).attr("style","background-color:#B3F7F7;");
		}
	});
}
function closeNav() {
	$("#purchaseAuditViewForm #t_div").animate({left:'-200px'},"slow");
	$("#purchaseAuditViewForm .tr").attr("style","background-color:white;");
}