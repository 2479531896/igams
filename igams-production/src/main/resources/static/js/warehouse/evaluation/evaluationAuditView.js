
function closeNav() {
	$("#evaluationAuditViewForm #t_div").animate({left:'-380px'},"slow");
	$("#evaluationAuditViewForm .tr").attr("style","background-color:white;");
}

function viewmore(yzmxid){
	$.ajax({
	    type:'post',
	    url:$("#evaluationAuditViewForm #urlPrefix").val()+"/ws/evaluation/viewMoreEvaluation",
	    cache: false,
	    data: {"yzmxid":yzmxid},
	    dataType:'json',
	    success:function(data){
	    	var wlmc=data.gfyzmxDto.wlmc;
	    	var wlbm=data.gfyzmxDto.wlbm;
	    	var gg=data.gfyzmxDto.gg;
	    	var jszb=data.gfyzmxDto.jszb;
	    	var zlyq=data.gfyzmxDto.zlyq;
	    	var fwmc=data.gfyzmxDto.fwmc;
	    	var xmh=data.gfyzmxDto.xmh;
	    	if(wlbm==null){
                wlbm="";
            }
	    	if(wlmc==null){
                wlmc=fwmc;
            }
            if(gg==null){
                gg="";
            }
	    	if(jszb==null){
                jszb="";
            }
            if(zlyq==null){
                zlyq="";
            }
            if(xmh==null){
                xmh="";
            }
	    	//返回值
	    	$("#evaluationAuditViewForm #wlmc").text(wlmc);
	    	$("#evaluationAuditViewForm #wlbm").text(wlbm);
	    	$("#evaluationAuditViewForm #gg").text(gg);
	    	$("#evaluationAuditViewForm #zlyq").text(zlyq);
	    	$("#evaluationAuditViewForm #jszb").text(jszb);
	    	$("#evaluationAuditViewForm #xmh").text(xmh);
	    	$("#evaluationAuditViewForm #t_div").animate({left:'0px'},"slow");
	    	$("#evaluationAuditViewForm .tr").attr("style","background-color:white;");
	    	$("#evaluationAuditViewForm #"+yzmxid).attr("style","background-color:#B3F7F7;");
		}
	});
}