
function closeNav() {
	$("#purchaseAuditViewForm #t_div").animate({left:'-200px'},"slow");
	$("#purchaseAuditViewForm .tr").attr("style","background-color:white;");
}

function viewmore(qgmxid){
	$.ajax({ 
	    type:'post',  
	    url:"/ws/external/getMorePurchase", 
	    cache: false,
	    data: {"qgmxid":qgmxid,"url":$("#purchaseAuditViewForm #url").val(),"urlPrefix":$("#purchaseAuditViewForm #urlPrefix").val()},  
	    dataType:'json', 
	    success:function(data){
	    	var wlmc=data.QgmxDto.wlmc;
	    	if($("#purchaseAuditViewForm #qglbdm").val()!='MATERIAL' && $("#purchaseAuditViewForm #qglbdm").val()!=null && $("#purchaseAuditViewForm #qglbdm").val()!=''){
	    		wlmc=data.QgmxDto.hwmc
	    	}
	    	var wllbmc=data.QgmxDto.wllbmc;
	    	var wlzlbmc=data.QgmxDto.wlzlbmc;
	    	var wlzlbtcmc=data.QgmxDto.wlzlbtcmc;
	    	var gg=data.QgmxDto.gg;
	    	if($("#purchaseAuditViewForm #qglbdm").val()!='MATERIAL' && $("#purchaseAuditViewForm #qglbdm").val()!=null && $("#purchaseAuditViewForm #qglbdm").val()!=''){
	    		gg=data.QgmxDto.hwbz;
	    	}
	    	var jldw=data.QgmxDto.jldw;
	    	if($("#purchaseAuditViewForm #qglbdm").val()!='MATERIAL' && $("#purchaseAuditViewForm #qglbdm").val()!=null && $("#purchaseAuditViewForm #qglbdm").val()!=''){
	    		jldw=data.QgmxDto.hwjldw
	    	}
	    	var scs=data.QgmxDto.scs;
	    	if($("#purchaseAuditViewForm #qglbdm").val()!='MATERIAL' && $("#purchaseAuditViewForm #qglbdm").val()!=null && $("#purchaseAuditViewForm #qglbdm").val()!=''){
	    		scs=data.QgmxDto.gys
	    	}
	    	var wlbm=data.QgmxDto.wlbm;
	    	var jg=data.QgmxDto.jg;
	    	var sl=data.QgmxDto.sl;
	    	var fjcfbDtos=data.mx_fjcfbDtos;
	    	var fwqx=data.QgmxDto.hwbz;
	    	var hwyt=data.QgmxDto.hwyt;
	    	var fwyq=data.QgmxDto.yq;
	    	if(fwyq==null){
	    		fwyq="";
	    	}
	    	if(hwyt==null){
	    		hwyt="";
	    	}
	    	if(fwqx==null){
	    		fwqx="";
	    	}
	    	if(jg==null || jg==''){
	    		jg=0;
	    	}
	    	if(sl==null || sl==''){
	    		sl=0;
	    	}
	    	if(wlmc==null){
	    		wlmc="";
	    	}
	    	if(wlbm==null){
	    		wlbm=""
	    	}
	    	if(wllbmc==null){
	    		wllbmc="";
	    	}
	    	if(wlzlbmc==null){
	    		wlzlbmc="";
	    	}
	    	if(wlzlbtcmc==null){
	    		wlzlbtcmc="";
	    	}
	    	if(gg==null){
	    		gg="";
	    	}
	    	if(scs==null){
	    		scs="";
	    	}
	    	if(jldw==null){
	    		jldw="";
	    	}
	    	//返回值
	    	$("#purchaseAuditViewForm #wlmc").text(wlmc);
	    	$("#purchaseAuditViewForm #wlbm").text(wlbm);
	    	$("#purchaseAuditViewForm #wllbmc").text(wllbmc);
	    	$("#purchaseAuditViewForm #wlzlbmc").text(wlzlbmc);
	    	$("#purchaseAuditViewForm #wlzlbtcmc").text(wlzlbtcmc);
	    	$("#purchaseAuditViewForm #gg").text(gg);
	    	$("#purchaseAuditViewForm #jldw").text(jldw);
	    	$("#purchaseAuditViewForm #scs").text(scs);
	    	$("#purchaseAuditViewForm #sl").text(sl);
	    	$("#purchaseAuditViewForm #dj").text(jg);
	    	$("#purchaseAuditViewForm #zj").text(((parseInt(jg*100)*parseInt(sl*100))/10000).toFixed(2));
	    	$("#purchaseAuditViewForm #fwqx").text(fwqx);
	    	$("#purchaseAuditViewForm #fwyq").text(fwyq);
	    	$("#purchaseAuditViewForm #hwyt").text(hwyt);
	    	$("#purchaseAuditViewForm #t_div").animate({left:'0px'},"slow");
	    	$("#purchaseAuditViewForm .tr").attr("style","background-color:white;");
	    	$("#purchaseAuditViewForm #"+qgmxid).attr("style","background-color:#B3F7F7;");
	    	//附件
	    	var html="";
	    	if(fjcfbDtos != null && fjcfbDtos.length>0){
	    		for(var i=0;i<fjcfbDtos.length;i++){
	    			html+="<div class='form-group'>";
	    			if(i==0){
	    				html+="<label for='' class='col-md-4 col-sm-4 col-xs-4 control-label' style='padding-left:0px;padding-right:0px;'>附件</label>";
	    			}else if(i>0){
	    				html+="<label for='' class='col-md-4 col-sm-4 col-xs-4 control-label' style='padding-left:0px;padding-right:0px;'>　　</label>";
	    			}
	    			html+="<div class='col-md-8 col-sm-8 padding_t7 h30'>";
	    			if(fjcfbDtos.length>1){
	    				html+="<label for='' text='"+(i+1)+"."+"'></label>";
	    			}
	    			html+="<span class='col-md-12 col-sm-12' style='overflow:hidden;text-overflow:ellipsis; white-space: nowrap;padding-left:0px;'>"+fjcfbDtos[i].wjm+"</span>";
	    			html+="</a>&nbsp;&nbsp; <a title='预览' onclick=\"yl('"+fjcfbDtos[i].fjid+"','"+fjcfbDtos[i].wjm+"')\"><span class='glyphicon glyphicon-eye-open'></span></a>";
	    			html+="</div>";
	    		}
	    	}
	    	$("#purchaseAuditViewForm #mx_fjcfbDtos").append(html);
	    }
	});
}

function yl(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	var url= $("#purchaseAuditViewForm #url").val();
	var urlPrefix= $("#purchaseAuditViewForm #urlPrefix").val();
	var View_url="/ws/external/viewFilePage?url="+url+"%26fjid="+ fjid+"%26urlPrefix=" + urlPrefix + "%26fileType=" + type;
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png" || type.toLowerCase()==".pdf"){
		window.location.href="/common/view/displayView?view_url="+View_url;
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}

$(function(){
})