$(function(){
	//添加日期控件
	laydate.render({
	   elem: '#submitContractForm #jhrq'
	  ,theme: '#2381E9'
	});	    
})
/**
 * 点击显示文件上传
 * @returns
 */
function editfile(){
	$("#fileDiv").show();
	$("#file_btn").hide();
}
/**
 * 点击隐藏文件上传
 * @returns
 */
function cancelfile(){
	$("#fileDiv").hide();
	$("#file_btn").show();
}

/**
 * 判断上传文件类型
 * @returns
 */
function onFile(){
	var file = document.getElementById('pro_file');
    file.onchange = function() {
    	var path = this.value;
        var fix = path.substr(path.lastIndexOf('.'));
        var lowFix = fix.toLowerCase();
        if(lowFix !== '.pdf'){
        	// $.alert("请传入pdf格式文件！")
        }  
    }
}
/**
 * 点击图片预览
 * @param fjid
 * @param wjm
 * @returns
 */
function view(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url=$("#submitContractForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= $("#submitContractForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',JPGMaterConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}
var JPGMaterConfig = {
	width		: "800px",
	offAtOnce	: true,  
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
/**
 * 点击文件下载
 * @param fjid
 * @returns
 */
function xz(fjid){
    jQuery('<form action='+$("#submitContractForm #urlPrefix").val()+'"/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
}
/**
 * 点击删除文件
 * @param fjid
 * @param wjlj
 * @returns
 */
function del(fjid,wjlj){
	$.confirm('您确定要删除所选择的记录吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url= $("#submitContractForm #urlPrefix").val()+"/common/file/delFile";
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

/**
 * 选择抄送人
 * @returns
 */
function chooseCsr(){
	var url = $("#submitContractForm #urlPrefix").val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择申请人', chooseCsrConfig);
}
var chooseCsrConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseCsrModal",
	formName	: "ajaxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				var data = $('#taskListFzrForm #xzrys').val();//获取选择行数据
				if(data != null){
					json = data;
					var jsonStr = eval('('+json+')');
					for (var i = 0; i < jsonStr.length; i++) {
						$("#submitContractForm  #csrs").tagsinput('add', jsonStr[i]);
					}
				}
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

/**
 * 初始化抄送人
 * @returns
 */
function initCC(){
	//初始化已选单据号
	$("#submitContractForm #csrs").tagsinput({
		itemValue: "yhid",
		itemText: "zsxm",
	})
	// 查询默认抄送人信息
	$.ajax({ 
	    type:'post',  
	    url: $("#submitContractForm #urlPrefix").val() + "/contract/contract/pagedataSelectCsrs",
	    cache: false,
	    data: {"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	var ddxxglDtos = data.ddxxglDtos;
	    	if(ddxxglDtos != null && ddxxglDtos.length > 0){
	    		for (var i = 0; i < ddxxglDtos.length; i++) {
	    			$("#submitContractForm #csrs").tagsinput('add', {"yhid":ddxxglDtos[i].yhid, "zsxm":ddxxglDtos[i].zsxm});
				}
	    	}
	    }
	});
}

/**
 * 附件上传回调
 * @param fjid
 * @returns
 */
function displayUpInfo(fjid){
	if(!$("#submitContractForm #fjids").val()){
		$("#submitContractForm #fjids").val(fjid);
	}else{
		$("#submitContractForm #fjids").val($("#submitContractForm #fjids").val()+","+fjid);
	}
}

$(function () { $('.collapse').collapse('hide')});

function getFlow(sprs,index){
	var t_collapse=$("#t_collapse"+index).attr("class");
	if(t_collapse.indexOf("collapsed")!='-1' && t_collapse.indexOf("getflow")=='-1'){
		$.ajax({ 
		    type:'post',  
		    url:$('#submitContractForm #urlPrefix').val() +"/purchase/purchase/pagedataGetDingtalkFlow",
		    cache: false,
		    data: {"ids":sprs,"access_token":$("#ac_tk").val()},  
		    dataType:'json', 
		    success:function(data){
		    	//返回值=
		    	if(data.yhlist!=null && data.yhlist.length>0){
		    		var html="";
		    		html+="<div class='col-sm-12 col-md-12'>"+
		    		"<div class='col-sm-3 col-md-3' style='padding:0px;margin-top:5px;'>"+
		    		"<div class='jdsty btn-info col-sm-9 col-md-9 jdlist' text='申请人' title='申请人'>申请人</div>"+
		    		"<div class='col-sm-2 col-md-2 glyphicon glyphicon-arrow-right jt' style='transform: translateY(50%);'></div>"+
		    		"</div>";
		    		for(var i=0;i<data.yhlist.length;i++){
		    			html+="<div class='col-sm-3 col-md-3' style='padding:0px;margin-top:5px;'>"+
		    			"<div class='jdsty btn-info col-sm-9 col-md-9 jdlist' text='"+data.yhlist[i].zsxm+"' title='"+data.yhlist[i].zsxm+"'>"+data.yhlist[i].zsxm+"</div>";
		    			if(i<data.yhlist.length-1){
		    				html+="<div class='col-sm-2 col-md-2 glyphicon glyphicon-arrow-right jt' style='transform: translateY(50%);'></div>";
		    			}
		    			html+="</div>";
		    		}
		    		html+="</div>";
		    		$("#collapsebody"+index).append(html);
		    		$("#t_collapse"+index).addClass("getflow");
		    	}
		    }
		});
	}
}

function changeFlow(spid,index){
	$(".logeok").attr("style","float:right;color:grey;cursor:pointer;");
	$("#ok"+index).attr("style","float:right;color:green;cursor:pointer;");
	$("#submitContractForm #ddspid").val(spid);
}
$(function(){
	var index=$("#submitContractForm #mrxz").val();
	$("#ok"+index).attr("style","float:right;color:green;cursor:pointer;");
	initCC();
	//0.初始化fileinput
	var oFileInput = new FileInput();
	var sign_params=[];
	sign_params.prefix=$("#submitContractForm #urlPrefix").val();
	oFileInput.Init("submitContractForm","displayUpInfo",2,1,"pro_file",null,sign_params);
	jQuery('#submitContractForm .chosen-select').chosen({width: '100%'});
})