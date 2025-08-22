//筛选下滑上滑框
var partner_turnOff=true;
$("#ajaxForm #sl_searchMore").on("click",function(ev){
	var ev=ev||event; 
	if(partner_turnOff){
		$("#ajaxForm #searchMore").slideDown("low");
		partner_turnOff = false;
		this.innerHTML="基本筛选";
	}else{
		$("#ajaxForm #searchMore").slideUp("low");
		partner_turnOff=true;
		this.innerHTML="高级筛选";
	}
	ev.cancelBubble=true;
});

//伙伴全选
$("#ajaxForm .provinceDiv .checkAll").on('change',function(){
//	var sfid1 = $("#ajaxForm .provinceDiv .checkAll").attr("sfid");
//	var sfid2 = $("#ajaxForm .provinceDiv").attr("sfid");
	var sfid = this.id;
	var length = $("#ajaxForm .provinceDiv[sfid='"+sfid+"'] .hb").length;
	if (length > 0) {
	    for (var i = 0; i < length; i++) {
			var hbid = $("#ajaxForm .provinceDiv[sfid='"+sfid+"'] .hbitem ")[i].getAttribute("hbid");
			if ($("#ajaxForm .provinceDiv[sfid='"+sfid+"'] .hb ")[i].style.display!="none"){
				$("#ajaxForm .provinceDiv[sfid='"+sfid+"'] .hbitem[hbid='"+hbid+"'] ").prop("checked", this.checked);
			}
	    }
	}
})


function search(){
	$("#ajaxForm #searchMore").slideUp("low");
	partner_turnOff=true;
	$("#ajaxForm #sl_searchMore").html("高级筛选");
	var hbmc=$(".search_box input").val();
	var sf=$(".search_box select").val();
	var provinces = jQuery('#ajaxForm #province_id_tj').val();
	var sflist = provinces.split("','");
	var partners = jQuery('#ajaxForm #partner_id_tj').val();
	var partnerlist = partners.split("','");
	var ptgss = jQuery('#ajaxForm #ptgs_id_tj').val();
	var ptgslist = ptgss.split("','");
	var zts = jQuery('#ajaxForm #zt_id_tj').val();
	var ztlist = zts.split("','");
	var checkzts = jQuery('#ajaxForm #checkzt_id_tj').val();
	var checkztlist = checkzts.split("','");
	var xsbms = jQuery('#ajaxForm #xsbm_id_tj').val();
    var xsbmlist = xsbms.split("','");
	
	if(hbmc != null && hbmc != ""){
		$("#ajaxForm .hb").each(function(){
			$(this).hide();
		});
		$("#ajaxForm div[nm*='"+ hbmc + "']").each(function(){
			$(this).show();
		});
		$("#ajaxForm .provinceDiv").each(function(){
			$(this).show();
			if($(this).find("div[style*='display: block']").size() == 0){
				$(this).hide();
			}
		});
	}
	if(sf != null && sf != ""){
		$("#ajaxForm .provinceDiv").each(function(){
			$(this).hide();
			if($(this).attr("sfid") == sf){
				$(this).show();
			}
		});
	}
	if((hbmc == null || hbmc == "") && (sf == null || sf == "")){
		$("#ajaxForm .provinceDiv").each(function(){
			$(this).show();
		});
		$("#ajaxForm .hb").each(function(){
			$(this).show();
		});
	}
	if(sflist !=null && sflist !=""){
		$("#ajaxForm .provinceDiv").each(function(){
			$(this).hide();
			for(var i=0; i<sflist.length; i++){
				if($(this).attr("sfid") == sflist[i]){
					$(this).show();
				}
			}
		});
	}
	$("#ajaxForm .hb").each(function(){
        $(this).show();
    });
	if(partnerlist !=null && partnerlist !=""){
        $("#ajaxForm .hb").each(function(){
            let isShow = false
            for(var i=0; i<partnerlist.length; i++){
                if($(this).attr("hbflid")==partnerlist[i]){
                    isShow = true;
                }
            }
            if(!isShow){
                $(this).hide();
            }
        });
	}
	if(ptgslist !=null && ptgslist !=""){
        $("#ajaxForm .hb").each(function(){
            let isShow = false
            for(var i=0; i<ptgslist.length; i++){
                if($(this).attr("ptgsid")==ptgslist[i]){
                    isShow = true;
                }
            }
            if(!isShow){
                $(this).hide();
            }
        });
	}
	if(xsbmlist !=null && xsbmlist !=""){
        $("#ajaxForm .hb").each(function(){
            let isShow = false
            for(var i=0; i<xsbmlist.length; i++){
                if($(this).attr("xsbmid")==xsbmlist[i]){
                    isShow = true;
                }
            }
            if(!isShow){
                $(this).hide();
            }
        });
    }
	if(ztlist !=null && ztlist !=""){
        $("#ajaxForm .hb").each(function(){
            let isShow = false
			for(var i=0; i<ztlist.length; i++){
                if($(this).attr("zt")==ztlist[i]){
                    isShow = true;
                }
            }
            if(!isShow){
                $(this).hide();
            }
        });
	}
	if(checkztlist !=null && checkztlist !=""){
		$("#ajaxForm .hb").each(function(){
            let isShow = false
            for(var i=0; i<checkztlist.length; i++){
                if($(this).attr("checkzt")==checkztlist[i]){
                    isShow = true;
                }
            }
            if(!isShow){
                $(this).hide();
            }
        });
	}
}

function attrchecked(){
	for(var i=0;i<$(".yxhbqx").length;i++){
		$("#"+$(".yxhbqx")[i].value).prop("checked",true);
	}
}

$("#ajaxForm #yhqf").on('change',function(){
	var csid=$("#ajaxForm #yhqf").val();
	$.ajax({
		url : "/partner/pagedataDistinguishList",
		type : "post",
		data : {fcsid:csid,"access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data != null && data.length != 0){
	    		var csbHtml = "";
	    		csHtml += "<option value=''>--请选择--</option>";
            	$.each(data,function(i){   		
            		csHtml += "<option value='" + data[i].csid + "'>" + data[i].csmc + "</option>";
            	});
            	$("#ajaxForm #yhzqf").empty();
            	$("#ajaxForm #yhzqf").append(csHtml);
            	$("#ajaxForm #yhzqf").trigger("chosen:updated");
	    	}else{
	    		var csHtml = "";
	    		csHtml += "<option value=''>--请选择--</option>";
	    		$("#ajaxForm #yhzqf").empty();
            	$("#ajaxForm #yhzqf").append(csHtml);
            	$("#ajaxForm #yhzqf").trigger("chosen:updated");
	    	}
		}
	})
})

window.onresize=function(){
	if($(".modal-content").width()<1056){
		$("#sjhbsearch").removeClass("sjhbsearch_small");
	}else{
		$("#sjhbsearch").addClass("sjhbsearch_small");
	}
}


$(function(){
	//search();
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
	
    $("#ajaxForm [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
      });
  addTj('zt','0','ajaxForm');
  addTj('checkzt','1','ajaxForm');
  search()
})
