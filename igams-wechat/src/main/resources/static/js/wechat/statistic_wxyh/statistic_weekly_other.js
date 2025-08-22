function chooseSf(sfid){
	var yhid=$("#weeklyStatis_wxyh_other #yhid").val();
	var t_sign=$("#weeklyStatis_wxyh_other #t_sign").val();
		jQuery('<form action="/common/view/displayView" method="POST">' +  // action请求路径及推送方法
	            '<input type="text" name="view_url" value="/ws/statistics/weekWechatStatisProvincePage?sf='+sfid+'&yhid='+yhid+'&sign='+t_sign+'"/>' + 
	        '</form>')
	    .appendTo('body').submit().remove();
	return true;
}

function back(){
	var yhid=$("#weeklyStatis_wxyh_other #yhid").val();
	var jsrqstart=$("#weeklyStatis_wxyh_other #jsrqstart").val();
	var jsrqend=$("#weeklyStatis_wxyh_other #jsrqend").val();
	var t_sign=$("#weeklyStatis_wxyh_other #t_sign").val();
	jQuery('<form action="/common/view/displayView" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="view_url" value="/ws/statictis/weeklyByYhid?&yhid='+yhid+'&jsrqstart='+jsrqstart+'&jsrqend='+jsrqend+'&sign='+t_sign+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
return true;
}

$(function(){
	
});

