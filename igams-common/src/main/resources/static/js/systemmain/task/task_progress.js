//事件绑定
function btnBind(){
	//是否完成
	jQuery("#ajaxForm input:radio").change(function(){
		var _this = $(this);
		var _value = _this.val()=="00";
		var t_value = _this.val()=="80";
		$("#ajaxForm #rwrq").attr("style","display:none;");
        $("#ajaxForm #jzDiv").attr("style","display:none;");
		//未完成
		if(_value){
			$("#ajaxForm #jd").attr("style","display:block;");
			$("#ajaxForm #rwrq").attr("style","display:block;");
		}else{
			//进度不显示
			$("#ajaxForm #jd").attr("style","display:none;");
		}
		//完成
		if(t_value){
			$("#ajaxForm #tj").attr("style","display:block;");
			$("#ajaxForm #zsxm").attr("validate","{required:true}");
            $("#ajaxForm #jzDiv").attr("style","display:block;");
		}else{
			//确认人不显示
			$("#ajaxForm #tj").attr("style","display:none;");
			$("#ajaxForm #zsxm").removeAttr("validate");
		}
	});
	if($("#ajaxForm #gzzt").val() == "00"){
		$("#ajaxForm #jd").attr("style","display:block;");
	}else{
		$("#ajaxForm #jd").attr("style","display:none;");
	}
	if($("#ajaxForm #gzzt").val() == "80"){
		$("#ajaxForm #tj").attr("style","display:block;");
		$("#ajaxForm #zsxm").attr("validate","{required:true}");
	}else{
		$("#ajaxForm #tj").attr("style","display:none;");
		$("#ajaxForm #zsxm").removeAttr("validate");
		
	}
	
	$('#ajaxForm #zsxm').typeahead({
		source : function(query, process) {
			$('#ajaxForm #qrry').val(null);
			return $.ajax({
				url : $("#task_progress #urlprefix").val()+'/systemmain/task/pagedataCommonSelectUser',
				type : 'post',
				data : {
					"zsxm" : query,
					"access_token" : $("#ac_tk").val()
				},
				dataType : 'json',
				success : function(result) {
					var resultList = result.f_gzglDtos
							.map(function(item) {
								var aItem = {
									id : item.yhid,
									dd : item.ddid,
									name : item.yhm+'-'+item.zsxm
								};
								return JSON.stringify(aItem);
							});
					return process(resultList);
				}
			});
		},
		matcher : function(obj) {
			var item = JSON.parse(obj);
			return ~item.name.toLowerCase().indexOf(
					this.query.toLowerCase())
		},
		sorter : function(items) {
			var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
			while (aItem = items.shift()) {
				var item = JSON.parse(aItem);
				if (!item.name.toLowerCase().indexOf(
						this.query.toLowerCase()))
					beginswith.push(JSON.stringify(item));
				else if (~item.name.indexOf(this.query))
					caseSensitive.push(JSON.stringify(item));
				else
					caseInsensitive.push(JSON.stringify(item));
			}
			return beginswith.concat(caseSensitive,
					caseInsensitive)
		},
		highlighter : function(obj) {
			var item = JSON.parse(obj);
			var query = this.query.replace(
					/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
			return item.name.replace(new RegExp('(' + query
					+ ')', 'ig'), function($1, match) {
				return '<strong>' + match + '</strong>'
			})
		},
		updater : function(obj) {
			var item = JSON.parse(obj);
			$('#ajaxForm #qrry').attr('value', item.id);
			$('#ajaxForm #ddid').attr('value', item.dd);
			return item.name;
		}
    });
}
//点击文件上传
function editfile(){
	$("#fileDiv").show();
	$("#file_btn").hide();
}
//点击隐藏文件上传
function cancelfile(){
	$("#fileDiv").hide();
	$("#file_btn").show();
}

function chooseFzr() {
	var url = $("#task_progress #urlprefix").val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择负责人', chooseFzrConfig);
}
var chooseFzrConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseFzrModal",
	formName	: "ajaxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#taskListFzrForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#ajaxForm #qrry').val(sel_row[0].yhid);
					$('#ajaxForm #zsxm').val(sel_row[0].yhm+'-'+sel_row[0].zsxm);
					$('#ajaxForm #ddid').val(sel_row[0].ddid);
					$.closeModal(opts.modalName);
					//保存操作习惯
					$.ajax({ 
					    type:'post',  
					    url:$("#task_progress #urlprefix").val() + "/common/habit/commonModUserHabit",
					    cache: false,
					    data: {"dxid":sel_row[0].yhid,"access_token":$("#ac_tk").val()},  
					    dataType:'json', 
					    success:function(data){}
					});
	    		}else{
	    			$.error("请选中一行");
	    			return;
	    		}
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function initPage(){
	//添加日期控件
	laydate.render({
		elem: '#ajaxForm #jhksrq'
		,theme: '#2381E9',
		trigger: 'click'
	});
	//添加日期控件
	laydate.render({
		elem: '#ajaxForm #jhjsrq'
		,theme: '#2381E9',
		trigger: 'click'
	});
	//添加日期控件
    laydate.render({
        elem: '#ajaxForm #jzrq'
        ,theme: '#2381E9',
		trigger: 'click'
    });
	if($("#extend_work_div").attr("url")){
		var tourl =$("#task_progress #urlprefix").val()+$("#extend_work_div").attr("url");
		if(tourl != ""){
			var data = {};
			data["rwid"] = $("#ywid").val();

			//div加载页面
			$("#extend_work_div").load(tourl,data,applyloadCallback);
		}
	}
}

function applyloadCallback(response,status,xhr){
    $("#xmjdid").change(function () {
        var xmjdid = $("#xmjdid").find("option:selected").val();
        if(xmjdid){
            $.ajax({
                type:'post',
                url:$("#task_progress #urlprefix").val() + "/experiment/project/pagedataGetRwrq",
                cache: false,
                data: {"xmjdid":xmjdid,"rwid":$("#rwid").val(),"access_token":$("#ac_tk").val()},
                dataType:'json',
                success:function(data){
                    if(data.rwrqDto){
                        $("#jhksrq").val(data.rwrqDto.jhksrq);
                        $("#jhjsrq").val(data.rwrqDto.jhjsrq);
                        if(data.rwrqDto.jzrq){
							$("#jzrq").val(data.rwrqDto.jzrq);
						}else{
							$("#jzrq").val($("#newJzrq").val());
						}
                    }
                }
            });
        }
    });
}

var viewTaskConfig = {
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
	el:'#task_progress',
	data: {
	},
	methods:{
		viewtask(ywdz){
			var url= $("#task_progress #urlprefix").val()+ywdz+"&access_token=" + $("#ac_tk").val();
            $.showDialog(url,'查看任务',viewTaskConfig);
		},
		view(fjid){
			var url=$("#task_progress #urlprefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
            $.showDialog(url,'文件预览',viewPreModConfig);
            
		},
		xz(wjlj,wjm,fjid){
		    jQuery('<form action="'+$("#task_progress #urlprefix").val()+ '/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
	                '<input type="text" name="wjlj" value="'+wjlj+'"/>' + 
	                '<input type="text" name="wjm" value="'+wjm+'"/>' + 
	                '<input type="text" name="fjid" value="'+fjid+'"/>' + 
	                '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
	            '</form>')
	        .appendTo('body').submit().remove();
		},
		yl(fjid,wjm){
			var begin=wjm.lastIndexOf(".");
			var end=wjm.length;
			var type=wjm.substring(begin,end);
			if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"|| type.toLowerCase()==".png"){
				var url= $("#ajaxForm #urlprefix").val()+"/ws/sjxxpripreview?fjid="+fjid
				$.showDialog(url,'图片预览',JPGMaterConfig);
			}else if(type.toLowerCase()==".pdf"){
				var url= $("#ajaxForm #urlprefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
				$.showDialog(url,'文件预览',JPGMaterConfig);
			}else {
				$.alert("暂不支持其他文件的预览，敬请期待！");
			}
		},
		del(fjid,wjlj){
			$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= $("#task_progress #urlprefix").val()+"/common/file/delFile";
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
	oFileInput.Init("ajaxForm","displayUpInfo",2,1,"task_file");
    //所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
});