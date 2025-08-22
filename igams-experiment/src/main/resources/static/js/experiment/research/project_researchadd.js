//事件绑定
function btnBind(){
		//根据输入信息模糊查询
		$('#ajaxForm #xmcy').typeahead({
			source: function(query, process) {
				return $.ajax({
					url: '/experiment/research/pagedataSelectXmcy',
					type: 'post',
					data: {
						"zsxm": query,
						"access_token":$("#ac_tk").val()
					},
					dataType: 'json',
					success: function(result) {
						var resultList = result.xmglDtos
							.map(function(item) {
								var aItem = {
									yhid: item.yhid,
									zsxm: item.zsxm,
								};
								return JSON.stringify(aItem);
							});
						return process(resultList);
					}
				});
			},
			matcher: function(obj) {
				var item = JSON.parse(obj);
				return ~item.zsxm.toLowerCase().indexOf(
					this.query.toLowerCase())
			},
			sorter: function(items) {
				var beginswith = [],
					caseSensitive = [],
					caseInsensitive = [],
					item;
				var aItem;
				while (aItem = items.shift()) {
					var item = JSON.parse(aItem);
					if (!item.zsxm.toLowerCase().indexOf(
							this.query.toLowerCase()))
						beginswith.push(JSON.stringify(item));
					else if (~item.zsxm.indexOf(this.query))
						caseSensitive.push(JSON.stringify(item));
					else
						caseInsensitive.push(JSON.stringify(item));
				}
				return beginswith.concat(caseSensitive,
					caseInsensitive)
			},
			highlighter: function(obj) {
				var item = JSON.parse(obj);
				var query = this.query.replace(
					/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
				return item.zsxm.replace(new RegExp('(' + query +
					')', 'ig'), function($1, match) {
					return '<strong>' + match + '</strong>'
				})
			},
			updater: function(obj) {
				var item = JSON.parse(obj);
				$('#ajaxForm #xmcy').val(item.zsxm);
				$('#ajaxForm #yhid').val(item.yhid);
				var xmcymc=$("#ajaxForm #xmcy").val();
		    	var yhid=$("#ajaxForm #yhid").val();
		    	$("#ajaxForm  #yxcy").tagsinput({
					itemValue: "value",
					itemText: "text",
					})
				$("#ajaxForm  #yxcy").tagsinput('add',{"value":yhid,"text":xmcymc});
		    	getxmcyxx();
		    	$('#ajaxForm #xmcy').val("");
			}
		});
		
}


//获取默认项目成员
$(function () {
		$("#ajaxForm  #yxcy").val("");
		var mrxmcyid=$("#ajaxForm #mryhid").val();
		var mrxmcymc=$("#ajaxForm #mrzsxm").val();
		$("#ajaxForm  #yxcy").tagsinput({
			itemValue: "value",
			itemText: "text",
			})
			if(mrxmcyid!=null){
				$("#ajaxForm  #yxcy").tagsinput('add',{"value":mrxmcyid,"text":mrxmcymc});
			}
		if($("#addormod").val()=="addSaveProjectResearch"){
		getxmcyxx();
		}
});


function initPage(){
	laydate.render({
	 	   elem: '#ksrq'
	 	  ,theme: '#2381E9',
		trigger: 'click'
	 	});
 	//添加日期控件
 	laydate.render({
 	   elem: '#jsrq'
 	  ,theme: '#2381E9',
		trigger: 'click'
 	});
 	//添加日期控件
 	laydate.render({
 	   elem: '#jhksrq'
 	  ,theme: '#2381E9',
		trigger: 'click'
 	});
 	//添加日期控件
 	laydate.render({
 	   elem: '#jhjsrq'
 	  ,theme: '#2381E9',
		trigger: 'click'
 	});
}
//点击编辑任务描述
function editrwms(){
	$("#rwmsDiv").show();
	$("#rwms_btn").hide();
}
//点击取消任务描述
function cancelrwms(){
	$("#rwmsDiv").hide();
	$("#rwms_btn").show();
}
//点击编辑阶段日期
function editJdrq(){
	$("#jdrqDiv").show();
	$("#jdrq_btn").hide();
}
//点击取消编辑阶段日期
function cancelJdrq(){
	$("#jdrqDiv").hide();
	$("#jdrq_btn").show();
}

//获取负责人id与名称
function getxmcyxx(){
	var mrxmcyid=$("#ajaxForm #mryhid").val();
	var mrxmcymc=$("#ajaxForm #mrzsxm").val();
	var xmcylist=$("#yxcy").tagsinput('items');
	var fzrsz=[];
	for(var i=0;i<xmcylist.length;i++){
		fzrsz.push(xmcylist[i].text);
	}
	if(xmcylist == null || xmcylist==""){
		var fzrHtml = "";
		fzrHtml += "<option value=''>--请选择--</option>";
		$("#ajaxForm #fzr").empty();
    	$("#ajaxForm #fzr").append(fzrHtml);
		$("#ajaxForm #fzr").trigger("chosen:updated");
		return;
	}
	if(xmcylist != null && xmcylist.length != 0){
		var fzrHtml = "";
		var xzfzr= $("#fzr option:selected").text();
		var xzfzrval=$("#fzr option:selected").val();
		if($.inArray(xzfzr,fzrsz)!=-1){
			fzrHtml += "<option value='" + xzfzrval + "'>" + xzfzr + "</option>";
			for(var i=0;i<xmcylist.length;i++){
				if(xmcylist[i].text!=xzfzr){
		    		fzrHtml += "<option value='" + xmcylist[i].value + "'>" + xmcylist[i].text + "</option>";
				}
			}
	    	$("#ajaxForm #fzr").empty();
	    	$("#ajaxForm #fzr").append(fzrHtml);
	    	$("#ajaxForm #fzr").trigger("chosen:updated");
		}else{
			fzrHtml += "<option value=''>--请选择--</option>";
	    	$.each(xmcylist,function(i){   		
	    		fzrHtml += "<option value='" + xmcylist[i].value + "'>" + xmcylist[i].text + "</option>";
	    	});
	    	$("#ajaxForm #fzr").empty();
	    	$("#ajaxForm #fzr").append(fzrHtml);
	    	$("#ajaxForm #fzr").trigger("chosen:updated");
		}
	}else{
		var fzrHtml = "";
		fzrHtml += "<option value=''>--请选择--</option>";
		$("#ajaxForm #fzr").empty();
    	$("#ajaxForm #fzr").append(fzrHtml);
    	$("#ajaxForm #fzr").trigger("chosen:updated");
	}
}

$(function () {
	if($("#addormod").val()=="modSaveProjectResearch"){
		var xmcyxxlist=$("#listmap").val();
		var jsonStr=eval('('+xmcyxxlist+')');
		for (var i = 0; i < jsonStr.length; i++) {
			$("#ajaxForm #yxcy").tagsinput('add',jsonStr[i]);
		}
		getxmcyxx();
	}
});

//修改子任务
function modSubTask(rwid,xmjdid,xmid,ev){
		var url="/experiment/project/pagedataModProjectSubTask?rwid="+rwid+"&xmjdid="+xmjdid+"&xmid="+xmid;
		$.showDialog(url,'修改任务',modProjectTaskConfig);
}

//编辑项目任务
var modProjectTaskConfig = {
	width		: "1200px",
	modalName	: "modProjectTaskModal",
	formName	: "modSubTaskForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var ksrq=new Date($("#ajaxForm #ksrq").val()).getTime();
				var jsrq=new Date($("#ajaxForm #jsrq").val()).getTime();
				if(ksrq>jsrq){
					$.error("开始日期不能大于结束日期");
					return false;
				}
				if(!$("#ajaxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"modSubTaskForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								reload();
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

//修改阶段日期
function updateJdDate(){
	var xmid=$("#xmid").val();
	var jdid=$(".chosen").attr("id");
}

$(".ztab").click(function(){
	var ahref=$(this).find("a").attr("href");
	$(".ztabnr").attr("style","display:none;");
	$(ahref).attr("style","display:block;");
})
$(document).ready(function(){
	
	//子任务修改界面设置第一个tab为active in
	$("#tab ul li:eq(0)").addClass("active");
	var tabbodyid=$("#tab ul li:eq(0) a").attr("href");
	$(tabbodyid).addClass("active in");
	
//	$("#ajaxForm  #yxcy").val($("#ajaxForm #mryhid").val());
    //所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
	//初始化富文本编辑器
	/*var editor = new Simditor({
		textarea: $("#rwms")
	});*/
});