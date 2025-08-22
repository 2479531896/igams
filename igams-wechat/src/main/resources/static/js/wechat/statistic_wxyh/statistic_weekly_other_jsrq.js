function chooseSf(sfid){
	var yhid=$("#weeklyStatis_wxyh_other_jsrq #yhid").val();
	var t_sign=$("#weeklyStatis_wxyh_other_jsrq #t_sign").val();
		jQuery('<form action="/common/view/displayView" method="POST">' +  // action请求路径及推送方法
	            '<input type="text" name="view_url" value="/ws/statistics/weekWechatStatisProvincePageByJsrq?sf='+sfid+'&yhid='+yhid+'&sign='+t_sign+'"/>' +
	        '</form>')
	    .appendTo('body').submit().remove();
	return true;
}

function back(){
	var yhid=$("#weeklyStatis_wxyh_other_jsrq #yhid").val();
	var jsrqstart=$("#weeklyStatis_wxyh_other_jsrq #jsrqstart").val();
	var jsrqend=$("#weeklyStatis_wxyh_other_jsrq #jsrqend").val();
	var t_sign=$("#weeklyStatis_wxyh_other_jsrq #t_sign").val();
	jQuery('<form action="/common/view/displayView" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="view_url" value="/ws/statictis/weeklyByYhidAndJsrq?&yhid='+yhid+'&jsrqstart='+jsrqstart+'&jsrqend='+jsrqend+'&sign='+t_sign+'"/>' +
        '</form>')
    .appendTo('body').submit().remove();
return true;
}

$(function(){
	
});

