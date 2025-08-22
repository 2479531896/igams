

function yl(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	var url= $("#ajaxForm #url").val();
	var urlPrefix= $("#ajaxForm #urlPrefix").val();
	var View_url="/ws/external/viewFilePage?url="+url+"%26fjid="+ fjid+"%26urlPrefix=" + urlPrefix + "%26fileType=" + type;
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png" || type.toLowerCase()==".pdf"){
		window.location.href="/common/view/displayView?view_url="+View_url;
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}
/*function xz(fjid){
	jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
}*/
/**
 * 初始化页面
 * @returns
 */
function init(){
	//添加日期控件
	laydate.render({
	   elem: '#ajaxForm #cjrq'
	  ,theme: '#2381E9'
	});
	// 初始化请购单号
	initDjh();
	//币种下拉框改变事件
	var sel_biz = $("#ajaxForm #biz");
	sel_biz.unbind("change").change(function(){
		var csid = $("#ajaxForm #biz").val();
		var cskz1 = $("#ajaxForm #"+csid).attr("cskz1");
		if(cskz1){
			$("#ajaxForm #sl").val(cskz1);
		}
	});
}
///**
// * 初始化请购单号
// * @returns
// */
//function initDjh(){
//	var qgids = $("#ajaxForm #qgids").val();
//	if(!qgids || qgids.length < 0){
//		return;
//	}
//	$.ajax({ 
//	    type:'post',  
//	    url:"/purchase/purchase/getPurchaseList", 
//	    cache: false,
//	    data: {"ids":qgids,"access_token":$("#ac_tk").val()},  
//	    dataType:'json',
//	    success:function(result){
//	    	//返回值
//	    	var qgglDtos = result.qgglDtos;
//	    	if(qgglDtos){
//	    		$("#ajaxForm #djhs").tagsinput({
//	    			itemValue: "qgid",
//	    			itemText: "djh",
//	    		})
//	    		for(var i = 0; i < qgglDtos.length; i++){
//		    		$("#ajaxForm #djhs").tagsinput('add',{"qgid": qgglDtos[i].qgid, "djh": qgglDtos[i].djh});
//		    	}
//	    	}
//	    }
//	});
//}


$(document).ready(function(){
});


