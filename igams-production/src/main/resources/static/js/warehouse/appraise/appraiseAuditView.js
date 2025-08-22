
function closeNav() {
	$("#appraiseAuditViewForm #t_div").animate({left:'-380px'},"slow");
	$("#appraiseAuditViewForm .tr").attr("style","background-color:white;");
}

function viewmore(pjmxid){
	$.ajax({
	    type:'post',
	    url:$("#appraiseAuditViewForm #urlPrefix").val()+"/ws/appraise/viewMoreAppraise",
	    cache: false,
	    data: {"pjmxid":pjmxid},
	    dataType:'json',
	    success:function(data){
	    	var wlmc=data.gfpjmxDto.wlmc;
	    	var wlbm=data.gfpjmxDto.wlbm;
	    	var gg=data.gfpjmxDto.gg;
	    	var jszb=data.gfpjmxDto.jszb;
	    	var zlyq=data.gfpjmxDto.zlyq;
	    	var fwmc=data.gfpjmxDto.fwmc;
	    	var xmh=data.gfpjmxDto.xmh;
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
	    	$("#appraiseAuditViewForm #wlmc").text(wlmc);
	    	$("#appraiseAuditViewForm #wlbm").text(wlbm);
	    	$("#appraiseAuditViewForm #gg").text(gg);
	    	$("#appraiseAuditViewForm #zlyq").text(zlyq);
	    	$("#appraiseAuditViewForm #jszb").text(jszb);
	    	$("#appraiseAuditViewForm #xmh").text(xmh);
	    	$("#appraiseAuditViewForm #t_div").animate({left:'0px'},"slow");
	    	$("#appraiseAuditViewForm .tr").attr("style","background-color:white;");
	    	$("#appraiseAuditViewForm #"+yzmxid).attr("style","background-color:#B3F7F7;");
		}
	});
}