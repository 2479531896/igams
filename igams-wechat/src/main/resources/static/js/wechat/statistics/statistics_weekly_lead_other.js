function toJcdwPage(){
	var load_flg=$("#load_flg").val();
	if(load_flg=="0"){
		jQuery('<form action="/common/view/displayView" method="POST">' +  // action请求路径及推送方法
	            '<input type="text" name="view_url" value="/ws/statistics/weekLeadStatisJcdwPage?load_flg='+load_flg+'"/>' + 
	        '</form>')
	    .appendTo('body').submit().remove();
	}else if(load_flg=="1"){
		$("#weeklyStatis").load("/ws/statistics/weekLeadStatisJcdwPage?load_flg="+load_flg);
	}
	return true;
}

function chooseSf(sfid){
	var load_flg=$("#load_flg").val();
	if(load_flg=="0"){
		jQuery('<form action="/common/view/displayView" method="POST">' +  // action请求路径及推送方法
	            '<input type="text" name="view_url" value="/ws/statistics/weekLeadStatisProvincePage?load_flg='+load_flg+'&sf='+sfid+'"/>' + 
	        '</form>')
	    .appendTo('body').submit().remove();
	}else if(load_flg=="1"){
		$("#weeklyStatis").load("/ws/statistics/weekLeadStatisProvincePage?load_flg="+load_flg+"&sf="+sfid);
	}
	return true;
}

function weekiy_back(){
	$("#weeklyStatis").load("/wechat/statistics/pageListLocal_weekly");
}

$(function(){
	$("#phonehome").scrollTop(0);
});

