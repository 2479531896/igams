/**
 * 绑定按钮事件
 */
function btnBind(){
	//添加日期控件
	laydate.render({
	   elem: '#ajaxForm #re_qwwcsj'
	  ,theme: '#2381E9'
	});
}

function init(){
}

var viewPreModConfig = {
    width        : "900px",
    height        : "800px",
    offAtOnce    : true,  //当数据提交成功，立刻关闭窗口
    buttons        : {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var vm = new Vue({
	el:'#resedit',
	data: {
	},
	methods:{
		view(fjid){
			var url= "/common/file/pdfPreview?fjid=" + fjid;
            $.showDialog(url,'文件预览',viewPreModConfig);
            
		},
		xz(wjlj,wjm,fjid){
		    jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
	                '<input type="text" name="wjlj" value="'+wjlj+'"/>' + 
	                '<input type="text" name="wjm" value="'+wjm+'"/>' + 
	                '<input type="text" name="fjid" value="'+fjid+'"/>' + 
	                '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
	            '</form>')
	        .appendTo('body').submit().remove();
		},
		del(fjid,wjlj){
			$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= "/common/file/delFile";
    				jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								$("#"+fjid).remove();
    							});
    						}else if(responseText["status"] == "fail"){
    							$.error(responseText["message"],function() {
    							});
    						} else{
    							$.alert(responseText["message"],function() {
    							});
    						}
    					},1);
    				},'json');
    				jQuery.ajaxSetup({async:true});
    			}
    		});
		}
	}
})

function displayUpInfo(fjid){
	if(!$("#ajaxForm #fjids").val()){
		$("#ajaxForm #fjids").val(fjid);
	}else{
		$("#ajaxForm #fjids").val($("#ajaxForm #fjids").val()+","+fjid);
	}
}

$(document).ready(function(){
	//0.初始化fileinput
	var oFileInput = new FileInput();
	oFileInput.Init("ajaxForm","displayUpInfo",2,1,"res_file");
	btnBind();
	init();
	//所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
});