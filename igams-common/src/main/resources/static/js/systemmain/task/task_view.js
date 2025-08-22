//事件绑定
function btnBind(){
	if($("#taskViewForm #gzzt").val() == "00"){
		$("#taskViewForm #jd").attr("style","display:block;");
	}else{
		$("#taskViewForm #jd").attr("style","display:none;");
	}
	if($("#taskViewForm #dqjd").text()){
		$("#taskViewForm #dqjd").text($("#taskViewForm #dqjd").text()+"%");
	}
	if($("#taskViewForm #listsize").val()){
		for (let i = 0; i < $("#taskViewForm #listsize").val(); i++) {
			//添加日期控件
			laydate.render({
				elem: '#taskViewForm #jhksrq_'+i
				,theme: '#2381E9',
				trigger: 'click'
			});
			//添加日期控件
			laydate.render({
				elem: '#taskViewForm #jhjsrq_'+i
				,theme: '#2381E9',
				trigger: 'click'
			});
		}
	}

}

function initPage(){
}
var viewThisTaskConfig = {
	width		: "1000px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

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
var vm = new Vue({
	el:'#task_view',
	data: {
	},
	methods:{
		viewtask(ywdz,urlqz){
			if(urlqz!='null'&&urlqz!=''){
				var url= urlqz+ywdz+"&access_token=" + $("#ac_tk").val();
			}else{
				var url= $("#taskViewForm #urlprefix").val()+ywdz+"&access_token=" + $("#ac_tk").val();
			}
            $.showDialog(url,'查看任务',viewThisTaskConfig);
		},
		view(fjid){
			var url= $("#taskViewForm #urlprefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
            $.showDialog(url,'文件预览',viewPreModConfig);
            
		},
		 yl(fjid,wjm){
			var begin=wjm.lastIndexOf(".");
			var end=wjm.length;
			var type=wjm.substring(begin,end);
			if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"|| type.toLowerCase()==".png"){
				var url= $("#taskViewForm #urlprefix").val()+"/ws/sjxxpripreview?fjid="+fjid
				$.showDialog(url,'图片预览',JPGMaterConfig);
			}else if(type.toLowerCase()==".pdf"){
				var url= $("#taskViewForm #urlprefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
				$.showDialog(url,'文件预览',JPGMaterConfig);
			}else {
				$.alert("暂不支持其他文件的预览，敬请期待！");
			}
		},
		xz(wjlj,wjm,fjid){
		    jQuery('<form action="'+$("#taskViewForm #urlprefix").val()+ '/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
	                '<input type="text" name="wjlj" value="'+wjlj+'"/>' + 
	                '<input type="text" name="wjm" value="'+wjm+'"/>' + 
	                '<input type="text" name="fjid" value="'+fjid+'"/>' + 
	                '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
	            '</form>')
	        .appendTo('body').submit().remove();
		},
	}
})

function viewAuditHistory(){
	var gzid = $("#taskViewForm #gzid").val();
	var url="/systemmain/task/pagedataTaskHistory?gzid=" +gzid;
	$.showDialog(url,'查看任务历史进度',viewAuditHistoryConfig);
}

var viewAuditHistoryConfig = {
	width		: "900px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};


$(document).ready(function(){
    //所有下拉框添加choose样式
	jQuery('#taskViewForm .chosen-select').chosen({width: '100%'});
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
	showless();
	var oFileInput = new FileInput();
	oFileInput.Init("taskViewForm","messageUpInfo",2,1,"message_file");
});



//点击留言
function editrwly(){
	$("#rwlyDiv").show();
	$("#rwly_btn").hide();
}
//点击取消
function cancelrwly(){
	$("#rwlyDiv").hide();
	$("#rwly_btn").show();
}
//点击发表留言
function saverwly(){
	var rwid=$("#rwid").val();
	var lyxx=$("#lyxx").val();
	if(lyxx!=null && lyxx!=""){
		$.ajax({
			url:"/experiment/project/pagedataAddRwly",
			type:"post",
			data:{rwid:rwid,lyxx:lyxx,"access_token":$("#ac_tk").val(),fjids:$("#message_fjids").val()},
			success:function(map){
				$("#rwlyDiv").hide();
				$("#rwly_btn").show();
				$("#lyxx").val("");
				var tr="<tr style='color:"+map.color+";'>";
				tr+="<td><span style='font-size:14px;margin-right:10px;'>"+map.lysj+"</span></td";
				tr+="<td><span style='font-size:14px;margin-right:10px;'>"+map.lyry+"</span></td";
				tr+="<td><span style='font-size:14px;font-weight:bold;'>"+map.lyxx+"</span></td";
				tr+="</tr>"
				if(map.fjcfbDtos!=null){
					for (var j = 0; j <map.fjcfbDtos.length; j++) {
						tr+="<tr style='display:block;height:20px;padding-left:15px;margin-bottom: 10px'>";
						tr+="<td>";
						tr+="<div class='col-sm-12' style='padding:0px;height:20px;'>";
						tr+="<button style='outline:none;margin-bottom:5px;padding:0px;font-size:12px;' class='btn btn-link' type='button' title='下载' onclick=\"xz('"+map.fjcfbDtos[j].fjid+"');\">";
						tr+="<span style='font-size:12px;height:20px;'>"+map.fjcfbDtos[j].wjm+"</span>";
						tr+="<span class='glyphicon glyphicon glyphicon-save'></span>";
						tr+="</button>";
						tr+="<button title='预览' class='f_button' type='button'>";
						tr+="<span  class='glyphicon glyphicon-eye-open' onclick=\"view_fjly('"+map.fjcfbDtos[j].fjid+"','"+map.fjcfbDtos[j].wjm+"')\"></span>";
						tr+="</button>";
						tr+="</div>";
						tr+="</td>";
						tr+="</tr>";
					}
				}
				$("#rwlytable").prepend(tr);
				fileinputInit();
			}
		})
	}else{
		$.error("留言内容不能为空！");
	}
}
/**
 * 初始化文件选择样式（ajax调用）
 * @returns
 */
function fileinputInit(){
	$("#taskViewForm #message_fjids").val("");
	$("#taskViewForm #message_div").html("");
	var fileHtml="<input id='message_file' name='message_file' type='file'>";
	$("#taskViewForm #message_div").append(fileHtml);
	//初始化fileinput
	var oFileInput = new FileInput();
	oFileInput.Init("taskViewForm","messageUpInfo",2,1,"message_file");
}

function messageUpInfo(fjid){
	if(!$("#taskViewForm #message_fjids").val()){
		$("#taskViewForm #message_fjids").val(fjid);
	}else{
		$("#taskViewForm #message_fjids").val($("#taskViewForm #message_fjids").val()+","+fjid);
	}
}
//显示所有动态
function showmore(){
	var rwid=$("#rwid").val();
	$.ajax({
		url:"/experiment/project/pagedataGetRwlyDto",
		type:"post",
		data:{rwid:rwid,"access_token":$("#ac_tk").val()},
		dataType:"json",
		success:function(map){
			$("#showless").show();
			$("#showmore").hide();
			$("#fatherDiv").html("");
			$("#rwlytable").html("");
			if(map.rwlylist!=null){
				for (var i = 0; i <map.rwlylist.length; i++) {
					var table="<div class='col-sm-11 col-md-11' style='color:"+map.rwlylist[i].color+";display:block;margin-bottom: 10px'><table>";
					table+="<tr>";
					table+="<td><span style='font-size:14px;margin-right:10px;'>"+map.rwlylist[i].lysj+"</span><span style='font-size:14px;margin-right:10px;'>"+map.rwlylist[i].lyry+"</span><span style='font-size:14px;font-weight:bold;'>"+map.rwlylist[i].lyxx+"</span></td>";
					table+="</tr>";
					if(map.rwlylist[i].fj_List!=null){
						for (var j = 0; j <map.rwlylist[i].fj_List.length; j++) {
							table+="<tr style='display:block;height:20px;padding-left:15px;margin-bottom: 10px;'>";
							table+="<td>";
							table+="<div class='col-sm-12' style='padding:0px;height:20px;'>";
							table+="<button style='outline:none;margin-bottom:5px;padding:0px;font-size:12px;' class='btn btn-link' type='button' title='下载' onclick=\"xz('"+map.rwlylist[i].fj_List[j].fjid+"');\">";
							table+="<span style='font-size:12px;height:20px;'>"+map.rwlylist[i].fj_List[j].wjm+"</span>";
							table+="<span class='glyphicon glyphicon glyphicon-save'></span>";
							table+="</button>";
							table+="<button title='预览' class='f_button' type='button'>";
							table+="<span  class='glyphicon glyphicon-eye-open' onclick=\"view_fjly('"+map.rwlylist[i].fj_List[j].fjid+"','"+map.rwlylist[i].fj_List[j].wjm+"')\"></span>";
							table+="</button>";
							table+="</div>";
							table+="</td>";
							table+="</tr>";
						}
					}
					table+="</table></div>";
					$("#fatherDiv").append(table);
				}
			}
		}
	})
}
//显示最近动态
function showless(){
	var rwid=$("#rwid").val();
	$.ajax({
		url:"/experiment/project/pagedataGetRwlyDto",
		type:"post",
		data:{rwid:rwid,pageSize:10,"access_token":$("#ac_tk").val()},
		dataType:"json",
		success:function(map){
			$("#showless").hide();
			$("#showmore").show();
			$("#fatherDiv").html("");
			$("#rwlytable").html("");
			if(map.rwlylist){
				for (var i = 0; i <map.rwlylist.length; i++) {
					var table="<div class='col-sm-11 col-md-11' style='color:"+map.rwlylist[i].color+";display:block;margin-bottom: 10px'><table>";
					table+="<tr>";
					table+="<td><span style='font-size:14px;margin-right:10px;'>"+map.rwlylist[i].lysj+"</span><span style='font-size:14px;margin-right:10px;'>"+map.rwlylist[i].lyry+"</span><span style='font-size:14px;font-weight:bold;'>"+map.rwlylist[i].lyxx+"</span></td>";
					table+="</tr>";
					if(map.rwlylist[i].fj_List!=null){
						for (var j = 0; j <map.rwlylist[i].fj_List.length; j++) {
							table+="<tr style='display:block;height:20px;padding-left:15px;margin-bottom: 10px;'>";
							table+="<td>";
							table+="<div class='col-sm-12' style='padding:0px;height:20px;'>";
							table+="<button style='outline:none;margin-bottom:5px;padding:0px;font-size:12px;' class='btn btn-link' type='button' title='下载' onclick=\"xz('"+map.rwlylist[i].fj_List[j].fjid+"');\">";
							table+="<span style='font-size:12px;height:20px;'>"+map.rwlylist[i].fj_List[j].wjm+"</span>";
							table+="<span class='glyphicon glyphicon glyphicon-save'></span>";
							table+="</button>";
							table+="<button title='预览' class='f_button' type='button'>";
							table+="<span  class='glyphicon glyphicon-eye-open' onclick=\"view_fjly('"+map.rwlylist[i].fj_List[j].fjid+"','"+map.rwlylist[i].fj_List[j].wjm+"')\"></span>";
							table+="</button>";
							table+="</div>";
							table+="</td>";
							table+="</tr>";
						}
					}
					table+="</table></div>";
					$("#fatherDiv").append(table);
				}
			}
		}
	})
}
/**
 * 文件在线预览
 * @param fjid
 * @param wjm
 * @returns
 */
function view_fjly(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"|| type.toLowerCase()==".png"){
		var url="/ws/pripreview/?fjid="+fjid
		$.showDialog(url,'图片预览',viewPreViewConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= "/common/file/pdfPreview?fjid=" + fjid;
		$.showDialog(url,'文件预览',viewPreViewConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}
var viewPreViewConfig={
	width		: "800px",
	offAtOnce	: true,
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}
function view(fjid){
	var url= "/common/file/pdfPreview?fjid=" + fjid;
	$.showDialog(url,'文件预览',viewProTaskConfig);
}
var viewProTaskConfig = {
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
function xz(fjid){
	jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
		'<input type="text" name="fjid" value="'+fjid+'"/>' +
		'<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
		'</form>')
		.appendTo('body').submit().remove();
}
function del(fjid,wjlj){
	$.confirm('您确定要删除所选择的记录吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url= "/common/file/delFile";
			jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
				setTimeout(function(){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							$("#taskViewForm #"+fjid).remove();
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