function hf(fkid,fkrymc,fkry){
	$("#ajaxForm #fkxx").focus();
	$("#ajaxForm #fkid").val(fkid);
	$("#ajaxForm #fkry").val(fkry);
	$("#ajaxForm #fkrymc").val(fkrymc);
}

$("#ajaxForm #fkxx").focus(function(){
	$("#ajaxForm #pl").attr("style","display:none;");
	$("#ajaxForm #fs").attr("style","display:block;padding-right:0px;padding-left:5px;position:relative;");
})

$("#ajaxForm #sr").blur(function(){
	$("#ajaxForm #pl").attr("style","display:block;padding-right:0px;padding-left:5px;position:relative;color:black;");
	$("#ajaxForm #fs").attr("style","display:none;");
})

function fs(){
	var ycid=$("#ycid").val();
	var fkxx=$("#fkxx").val();
	var fkid=$("#fkid").val();
	var fkry=$("#fkry").val();
	var lrry=$("#yhid").val();
	var qrr=$("#qrr").val();
	if(fkxx==null || fkxx==''){
		$.confirm("请输入反馈信息!");
	}else{
		$.ajax({ 
		    type:'post',  
		    url:$("#ajaxForm #urlPrefix").val()+"/ws/exception/addSaveExceptionFk",
		    cache: false,
		    data: {"lrry":lrry,"ycid":ycid,"ffkid":fkid,"qrr":fkry,"fkxx":fkxx,"qrr":qrr},  
		    dataType:'json', 
		    success:function(data){
		    	//返回值
		    	if(data.status==true){
		    		var html="";
		    		html+="<div><div class='col-md-12 col-sm-12 col-xs-12' >"+
		    		"<span style='color:#77C9FF'>"+data.sjycfkDto.fkrymc+"</span>"+
		    		"<span style='color:#ddd'>"+data.sjycfkDto.hfsj+"</span>"+
		    		"</div>"+
		    		"<div class='col-sm-12 col-md-12 col-xs-12 text-left'>"+
		    		"<span>　　"+data.sjycfkDto.fkxx+"</span>";
		    		if(data.sjycfkDto.ffkxx!=null && data.sjycfkDto.ffkxx!=""){
		    			html+="<span style='color:#77C9FF'>@"+data.sjycfkDto.qrrmc+"</span>"+
		    			"<span>"+data.sjycfkDto.ffkxx+"</span>";
		    		}
		    		html+="</div>"+
		    		"<div class='col-sm-12 col-md-12 col-xs-12 text-left'>"+
		    		"<span style='color:#ddd'>"+computingTime(data.sjycfkDto.hfsj)+"</span>"+
		    		"<span style='cursor:pointer;color:#ddd;' onclick=\"hf(\'"+data.sjycfkDto.fkid+"\',\'"+data.sjycfkDto.fkrymc+"\',\'"+data.sjycfkDto.fkry+"')\">回复</span>"+
		    		"</div></div>";
		    		$("#all").append(html);
		    		$("#fkxx").val("");
		    	}
		    }
		});
	}
}

//转发按钮，选择转发人员或部门
function repeat(ycid){
	var url=$("#ajaxForm #urlPrefix").val()+"/ws/exception/choseRepeatObject?ycid="+ycid;
	$.showDialog(url, '选择通知对象', chooseTzdxConfig);
}

var chooseTzdxConfig = {
		width : "800px",
		height : "200px",
		modalName	: "chooseTzdxModal",
		formName	: "repeatObjectForm",
		offAtOnce : true, // 当数据提交成功，立刻关闭窗口
		buttons : {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#repeatObjectForm").valid()){
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					
					$("#repeatObjectForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"repeatObjectForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
							$.alert(responseText["message"],function() {
							});
						}
					},".modal-footer > button");
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};


//计算时间
function computingTime(date1) {
    var time = '';
    var date2 = new Date();    //当前系统时间
    var date3 = date2.getTime() - new Date(date1).getTime();   //时间差的毫秒数

    var hours = Math.floor(date3 / (3600 * 1000)); //相差小时
    if (hours > 0) {
      time = hours + '小时前'
      if (hours > 24) {//如果小时大于24，计算出天和小时
        var day = parseInt(hours / 24);
        hours %= 24;//算出有多分钟
        time = day + '天' + hours + '小时前'
      }
    } else {
      //计算相差分钟数
      var leave2 = date3 % (3600 * 1000);      //计算小时数后剩余的毫秒数
      var minutes = Math.floor(leave2 / (60 * 1000));
      if (minutes > 0) {
        time = minutes + '分钟前';
      } else {
        time = '刚刚';
      }
    }
    return time;
  }