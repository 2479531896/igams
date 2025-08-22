//事件绑定
function btnBind(){
	//是否通过选择
	jQuery("#ajaxForm #sftg").change(function(){
		$('#ajaxForm #qrry').val(null);
		$('#ajaxForm #zsxm').val(null);
		var _this = $(this);
		if(_this.val() == ""){
			$("#ajaxForm #qr").attr("style","display:none;");
		}else{
			$("#ajaxForm #qr").attr("style","display:block;");
			if(_this.val() == "1"){
				$("#ajaxForm #qr label").text("下一步确认人员");
				$('#ajaxForm #qrry').val($('#ajaxForm #gr_szz').val());
				if ( $('#ajaxForm #gr_yhm').val()!=null && $('#ajaxForm #gr_yhm').val()!=undefined && $('#ajaxForm #gr_yhm').val()!=''
					&& $('#ajaxForm #gr_zsxm').val()!=null && $('#ajaxForm #gr_zsxm').val()!=undefined && $('#ajaxForm #gr_zsxm').val()!=''){
					$('#ajaxForm #zsxm').val(   $('#ajaxForm #gr_yhm').val()+'-'+$('#ajaxForm #gr_zsxm').val()  );
				}
				$('#ajaxForm #ddid').val($('#ajaxForm #gr_ddid').val());
				$('#ajaxForm #jzrqDiv').hide();
			}else{
				$("#ajaxForm #qr label").text("退回人员");
				$('#ajaxForm #jzrqDiv').show();
			}
		}
	}).trigger("change");
	
	if($("#ajaxForm #gzzt").val() == "00"){
		$("#ajaxForm #jd").attr("style","display:block;");
	}else{
		$("#ajaxForm #jd").attr("style","display:none;");
	}
	
	$('#ajaxForm #zsxm').typeahead({
		source : function(query, process) {
			$('#ajaxForm #qrry').val(null);
			return $.ajax({
				url : $("#taskListConfirmForm #urlprefix").val()+'/systemmain/task/pagedataCommonSelectUser',
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
									name : item.yhm+'-'+item.zsxm,
									yhm : item.yhm
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
			$('#ajaxForm #qrryyhm').attr('value', item.yhm);
			$('#ajaxForm #ddid').attr('value', item.dd);
			$('#ajaxForm #jzrqDiv').show();
			return item.name;
		}
    });
}

function checkNull() {
	var val = $('#ajaxForm #zsxm').val();
	console.log(val);
	if(!val){
		$('#ajaxForm #jzrqDiv').hide();
	}
}

function chooseFzr() {
	var url = $("#taskListConfirmForm #urlprefix").val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
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
					$('#ajaxForm #qrryyhm').val(sel_row[0].yhm);
					$('#ajaxForm #zsxm').val(sel_row[0].yhm+'-'+sel_row[0].zsxm);
					$('#ajaxForm #ddid').val(sel_row[0].ddid);
					$('#ajaxForm #jzrqDiv').show();
					$.closeModal(opts.modalName);
	    		}else{
	    			$.error("请选中一行");
	    			return false;
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
		var tourl =$("#taskListConfirmForm #urlprefix").val()+ $("#extend_work_div").attr("url");
		if(tourl != ""){
			var data = {};
			data["rwid"] = $("#ywid").val();

			//div加载页面
			$("#extend_work_div").load(tourl,data,applyloadCallback);
		}
	}
	var rwid= $('#ajaxForm #rwid').val();
	if(!rwid){
		$('#ajaxForm #jdDiv').remove();
	}
}

function applyloadCallback(response,status,xhr){
	$("#xmjdid").change(function () {
		var xmjdid = $("#xmjdid").find("option:selected").val();
		if(xmjdid){
			$.ajax({
				type:'post',
				url:$("#taskconfirm #urlprefix").val() + "/experiment/project/pagedataGetRwrq",
				cache: false,
				data: {"xmjdid":xmjdid,"rwid":$("#rwid").val(),"access_token":$("#ac_tk").val()},
				dataType:'json',
				success:function(data){
					if(data.rwrqDto){
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
var viewRwqrlsConfig = {
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
var viewThisConfirmTaskConfig = {
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
	el:'#taskconfirm',
	data: {
	},
	methods:{
		view(fjid){
			var url= $("#taskListConfirmForm #urlprefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
            $.showDialog(url,'文件预览',viewPreModConfig);
            
		},
		xz(wjlj,wjm,fjid){
		    jQuery('<form action="'+$("#taskListConfirmForm #urlprefix").val()+ '/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
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
		qrzt(gzid){
			var url=$("#taskListConfirmForm #urlprefix").val()+"/systemmain/task/pagedataTaskHistory?gzid=" +gzid;
			$.showDialog(url,'查看任务历史进度',viewRwqrlsConfig);
		},
		viewtask(ywdz,urlqz){
			if(urlqz!='null'&&urlqz!=''){
				var url= urlqz+ywdz+"&access_token=" + $("#ac_tk").val();
			}else{
				var url= $("#taskListConfirmForm #urlprefix").val()+ywdz+"&access_token=" + $("#ac_tk").val();
			}
            $.showDialog(url,'查看任务',viewThisConfirmTaskConfig);
		},
	}
})
//点击编辑按钮
$("#edit").click('on',function(){
	$("#edit_div").show();
	$("#bz_div").hide();
	$("#edit").hide();
	$("#bz_text").val($("#bz_div").html())
})
//点击取消按钮
$("#cancel").click('on',function(){
	$("#edit_div").hide();
	$("#bz_div").show();
	$("#edit").show();
	$("#bz_text").val("")
})
//保存按钮
$("#save").click('on',function(){
	if($("#bz_text").val()!="" && $("#bz_text").val()!=null){
		$.ajax({ 
			url:$("#taskListConfirmForm #urlprefix").val()+"/systemmain/task/pagedataUpdateBz", 
		    type:'post',  
		    data: {"gzid":$("#gzid").val(),"access_token":$("#ac_tk").val(),"bz":$("#bz_text").val()}, 
		    dataType:'json', 
		    success:function(data){
		    	if(data.state=="faile"){
		    		$.error("保存失败!");
		    	}else if(data.state=="success"){
		    		$.confirm("保存成功!",function(result){
		    		$("#edit_div").hide();
		    		$("#bz_div").show();
		    		$("#edit").show();
		    		$("#bz_text").val("")
		    		$("#bz_div").html(data.bz);
		    		})
		    	}
		    }
		});
	}else{
		$.alert("不能为空！")
	}
	
})
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

function displayUpInfo(fjid){
	if(!$("#ajaxForm #fjids").val()){
		$("#ajaxForm #fjids").val(fjid);
	}else{
		$("#ajaxForm #fjids").val($("#ajaxForm #fjids").val()+","+fjid);
	}
}


jQuery(function(){
	//0.初始化fileinput
	var oFileInput = new FileInput();
	oFileInput.Init("ajaxForm","displayUpInfo",2,1,"confirm_file");
    //所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
});