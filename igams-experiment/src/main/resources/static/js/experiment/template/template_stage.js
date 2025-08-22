$(".dragsort").dragsort({
	//可以通过修改dragSelector参数实现拖拽其他标签
	dragSelector: "div", 
	//启用多组列表之间拖动选定的列表, 默认值是false
	dragBetween: true, 
	//拖拽完成后的回调
	dragEnd: saveOrder, 
	dragProId: "jdid",
	//拖动列表的HTML部分。默认值是”<li></li>”.
	placeHolderTemplate: "<li class='placeHolder'><div></div></li>",
	//设置为0以禁用滚动。默认值是5
	scrollSpeed: 5
});

function saveOrder(listIndex) {
	var prejdid = $(this).attr("jdid");
	var newjdid = listIndex;
	var rwmbid = $(this).attr("id");
	//获取需要重新排序的阶段
	var prelist =  $("ul[jdid='"+prejdid+"'] li");
	if(prelist.length > 0){
		var ids="";
		for(var i = 0; i < prelist.length; i++){
			ids = ids + ","+ prelist[i].id;
		}
		ids = ids.substr(1);
		taskSort(ids, null, null);
	}
	//更新转移的jdid
	if(prejdid != newjdid){
		$(this).attr("jdid",listIndex);
		var newlist =  $("ul[jdid='"+newjdid+"'] li");
		var ids="";
		for(var i = 0; i < newlist.length; i++){
			ids = ids + ","+ newlist[i].id;
		}
		ids = ids.substr(1);
		taskSort(ids, newjdid, rwmbid);
	}
};

function taskSort(ids, jdid, rwmbid){
	$.ajax({
	    type:'post',  
	    url:"/experiment/template/pagedataTaskSort",
	    cache: false,
	    data: {ids:ids,"jdid":jdid,"rwmbid":rwmbid,"access_token":$("#ac_tk").val()}, 
	    dataType:'json', 
	    success:function(data){
	    	var mbid=$("#mbid").val();
	    	if(data.status=="success"){
	    		$("#disdiv").load("/experiment/template/pagedataTemplateListStage?mbid="+mbid);
	    	}else if(map.status=="fail"){
	    		$.error(data.message);
	    		$("#disdiv").load("/experiment/template/pagedataTemplateListStage?mbid="+mbid);
	    	}
	    }
	});
}

function addStage(){
	$("#btn_addStage").attr("style","display:none;");
	$("#newStage").attr("style","display:block;height:120px;padding-top:25px;");
}

function cancleStage(){
	$("#newStage").attr("style","display:none;");
	$("#btn_addStage").attr("style","display:block;height:120px;text-align:center;");
}

function cancleTask(){
	$("#newTask").attr("style","display:none;");
	$("#btn_addTask").attr("style","display:block;height:70px;text-align:center;");
}

function addTaskAlert(jdid){
	var mbid=$("#mbid").val()
	var url="/experiment/template/pagedataAddTemplateTask?jdid="+jdid+"&mbid="+mbid ;
	$.showDialog(url,'新增任务',addTemplateTaskConfig);
}
//修改任务模板
function modtemplate(rwmbid){
	var url="/experiment/template/pagedataModtasktemplate?rwmbid="+rwmbid ;
	$.showDialog(url,'修改任务',addTemplateTaskConfig);
}
function modjdmb(jdid){
	var url="/experiment/template/pagedataModstage?jdid="+jdid ;
	$.showDialog(url,'修改阶段模板',modstageConfig);
}
function moreoperation(rwid){
	event.stopPropagation();
	
}

function delrwmb(rwmbid,event){
	event.stopPropagation();
	$.confirm("您确定要删除当前任务么？",function(result){
		if(result){
			var mbid=$("#mbid").val();
			$.ajax({ 
			    type:'post',  
			    url:"/experiment/template/pagedataDeltasktemplate",
			    data: {rwmbid:rwmbid,"access_token":$("#ac_tk").val()}, 
			    dataType:'json', 
			    success:function(map){
			    	//返回值
			    	if(map.status=="success"){
			    		$.confirm("删除成功",function(){
			    			var mbid=$("#mbid").val();
			    			$("#disdiv").load("/experiment/template/pagedataTemplateListStage?mbid="+mbid);
				    	});
			    	}else if(map.status=="fail"){
			    		$.error(map.message);
			    	}
			    }
			});
		}
	})
}
//新增阶段名称
function saveStage(){
	var mbid=$("#mbid").val();
	$("#token").val($("#ac_tk").val());
	$.ajax({ 
	    type:'post',  
	    url:"/experiment/template/pagedataAddSaveStage",
	    cache: false,
	    data: $("#ajaxForm").serialize(),  
	    dataType:'json', 
	    success:function(map){
	    	//返回值
	    	if(map.status=="success"){
	    		$.confirm("保存成功！",function(){
	    			$("#disdiv").load("/experiment/template/pagedataTemplateListStage?mbid="+mbid);
		    	});
	    	}else if(map.status=="fail"){
	    		$.error(map.message);
	    	}
	    }
	});
}
//删除阶段
function deljd(jdid){
	var mbid=$("#mbid").val();
	$.confirm("您确定要删除当前阶段么？",function(result){
		if(result){
			$.ajax({ 
			    type:'post',  
			    url:"/experiment/template/pagedataDeljd",
			    cache: false,
			    data: {jdid:jdid,"access_token":$("#ac_tk").val()}, 
			    dataType:'json', 
			    success:function(map){
			    	//返回值
			    	if(map.status=="success"){
			    		$.confirm("删除成功",function(){
			    			$("#disdiv").load("/experiment/template/pagedataTemplateListStage?mbid="+mbid);
				    	});
			    	}else if(map.status=="fail"){
			    		$.error(map.message);
			    	}
			    }
			});
		}
	})
}
//双击修改阶段名称
function xgjdmc(jdid){
	$("#"+jdid).removeAttr("readonly");
	$("#"+jdid).removeClass("style_1"); //移除
	$("#"+jdid).addClass("style_2"); // 追加样式
}
function checkstyle(jdid){
	var mbid=$("#mbid").val();
	$("#disdiv").load("/experiment/template/pagedataTemplateListStage?mbid="+mbid);
}
//修改阶段名称提交
function hyjdmc(jdid){
	  var evt=evt?evt:(window.event?window.event:null);//兼容IE和FF
	  if (evt.keyCode==13){
		  var jdmc=$("#"+jdid).val();
		  var mbid=$("#mbid").val();
		  $.confirm("您确定要修改名称么？",function(result){
				if(result){
					if(jdmc!="" ){
						$.ajax({ 
						    type:'post',  
						    url:"/experiment/template/updatejdm", 
						    cache: false,
						    data: {jdid:jdid,jdmc:jdmc,"access_token":$("#ac_tk").val()}, 
						    dataType:'json', 
						    success:function(map){
						    	//返回值
						    	if(map.status=="success"){
						    		$.confirm("修改成功",function(){
						    			$("#disdiv").load("/experiment/template/pagedataTemplateListStage?mbid="+mbid);
							    	});
						    	}else if(map.status=="fail"){
						    		$.error(map.message);
						    	}
						    }
						});
					}else{
						$.error("内容不能为空");
						$("#disdiv").load("/experiment/template/pagedataTemplateListStage?mbid="+mbid);
					}
				}else{
					$("#disdiv").load("/experiment/template/pagedataTemplateListStage?mbid="+mbid);
				}
				
			})
		}
}
var addTemplateTaskConfig = {
	width		: "800px",
	modalName	: "addTemplateTaskModal",
	formName	: "taskForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				
				if(!$("#taskForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#taskForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"taskForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								var mbid=$("#mbid").val();
								$("#disdiv").load("/experiment/template/pagedataTemplateListStage?mbid="+mbid);
								//刷新页面
							}
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				});
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var modstageConfig = {
		width		: "800px",
		modalName	: "modstageModal",
		formName	: "stageForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					
					if(!$("#stageForm").valid()){
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#stageForm input[name='access_token']").val($("#ac_tk").val());
					
					submitForm(opts["formName"]||"stageForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									var mbid=$("#mbid").val();
									$("#disdiv").load("/experiment/template/pagedataTemplateListStage?mbid="+mbid);
									//刷新页面
								}
							});
						}else if(responseText["status"] == "fail"){
							$.error(responseText["message"],function() {
							});
						} else{
							$.alert(responseText["message"],function() {
							});
						}
					});
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

function stageSort(ids){
	$.ajax({ 
	    type:'post',  
	    url:"/experiment/template/pagedataStageSort",
	    cache: false,
	    data: {ids:ids,"access_token":$("#ac_tk").val()}, 
	    dataType:'json', 
	    success:function(data){
	    	var mbid=$("#mbid").val();
	    	if(data.status=="success"){
    			$("#disdiv").load("/experiment/template/pagedataTemplateListStage?mbid="+mbid);
	    	}else if(map.status=="fail"){
	    		$.error(data.message);
	    		$("#disdiv").load("/experiment/template/pagedataTemplateListStage?mbid="+mbid);
	    	}
	    }
	});
}
//任务点击更多,显示菜单
function showoperation(rwmbid, event) {
	event.stopPropagation();
	if($("#"+rwmbid+"menu").css("display")=="none"){
		$("#"+rwmbid+"menu").show();
	}else{
		$("#"+rwmbid+"menu").hide();
	}
	
} 
$(document).ready(function(){
	var size = $("#size").val();
	var width = size * 312 + 400;
	$("#stage").attr("style","width:"+width+"px;height:calc(100% - 15px);padding-bottom: 0px;")
	//拖动排序
	$(".sortable").sortable({
		cursor: "move",
		opacity: 0.6,                       //拖动时，透明度为0.6
		revert: true,                       //释放时，增加动画
		update : function(event, ui){       //更新排序之后
			var ids="";
    		for (var i = 0; i < $(this).sortable("toArray").length; i++) {//循环读取选中行数据
    			ids = ids + ","+ $(this).sortable("toArray")[i];
    		}
    		ids = ids.substr(1);
    		stageSort(ids);
		}
	});
	$(".sortable").disableSelection();
});

