
//确定按钮，提交
function checkyhm(){
	var yhm = document.getElementById("yhm").value;
		$.ajax({ 
		    type:'post',  
		    url:"/client/getyhm", 
		    data: {
				yhm : yhm
			},
		    dataType:'json', 
		    success:function(map){
		    	//返回值
		    	if(map.status=="fail"){
		    		$.error(map.message);
		    	}
		    }
		});
		
	}
	
function sendyzm(){
	var yhm = document.getElementById("yhm").value;
		$.ajax({ 
		    type:'post',  
		    url:"/client/sendyzm", 
		    data: {
				yhm : yhm
			},
		    dataType:'json', 
		    success:function(map){
		    	//返回值
		    	if(map.status=="fail"){
		    		$.error(map.message);
		    	}
		    }
		});		
	}
function yanzhengyzm(){
	if(!$("#ajaxform").valid()){
		return false;
	}else{
		$.ajax({ 
		    type:'post',  
		    url:"/client/verification", 
		    data: $("#ajaxform").serialize(), 
		    dataType:'json', 
		    success:function(map){		    	
		    	//返回值
		    	if(map.status=="success"){
		    		var yhid=map.xtyhid;
		    		jQuery('<form action="/client/toFindPwd" method="POST">' +  // action请求路径及推送方法
			                '<input type="text" name="yhid" value="'+yhid+'"/>' +  
			            '</form>')
			        .appendTo('body').submit().remove();
		    	}else if(map.status=="fail"){
		    		$.error(map.message);
		    	}
		    }
		});
	}
}
function keysub(evt){
	  var evt=evt?evt:(window.event?window.event:null);//兼容IE和FF
	  if (evt.keyCode==13){
		  checkpwd();
	}
}