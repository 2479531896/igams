
function exportSubject(type,obj){
	if(type=='1'){
		$("#ajaxForm #sfbc").val("Y");//保存个人设置
	}else{
		$("#ajaxForm #sfbc").val("N");
	}
	$(obj).attr("disabled","disabled");
	
	var expType = $("#ajaxForm #expType").val();
	if(expType=="select")
		$("#ajaxForm").attr("action",$('#ajaxForm #urlPrefix').val() + "/common/export/exportSelect?access_token="+$("#ac_tk").val());
	else
		$("#ajaxForm").attr("action",$('#ajaxForm #urlPrefix').val() + "/common/export/exportSearch");
	
	var map = {};
	var callbackJs = $("#ajaxForm #callbackJs").val();
	if(callbackJs){
		map = eval(callbackJs+"()")
	}
	var html = '';
	$.each(map,function(k,v){
		html += '<input type="hidden" name="'+k+'" value="'+v+'"/>';
	})
	if(html!=''){
		$("#sendData").empty().append(html);
	}
	if($("#ajaxForm").attr("action")!=null){
		var _choseListDiv = $("#ajaxForm #list2 div[id='choseListDiv']");
		if(_choseListDiv.length < 1 ){
			$.error("请选择导出字段。")
			$(obj).removeAttr("disabled");
			return;
		}
		submitForm("ajaxForm",function(resp,status){
			if(resp.result){
				//创建objectNode
				var cardiv =document.createElement("div");
				cardiv.id="cardiv";
				var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span style="color:red;font-weight:600;">/' + resp.totalCount + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
				cardiv.innerHTML=s_str;
				//将上面创建的P元素加入到BODY的尾部
				document.body.appendChild(cardiv);
				
				setTimeout("checkdownLoadStatus('"+ resp.wjid + "',1000)",1000);
				
				//绑定导出取消按钮事件
				$("#exportCancel").click(function(){
					//先移除导出提示，然后请求后台
					if($("#cardiv").length>0) $("#cardiv").remove();
					$.ajax({
						type : "POST",
						url : $('#ajaxForm #urlPrefix').val() + "/common/export/commCancelExport",
						data : {"wjid" : resp.wjid+"","access_token":$("#ac_tk").val()},
						dataType : "json",
						success:function(data){
							if(data != null && data.result==false){
								if(data.msg && data.msg!="")
									$.error(data.msg);
							}
						}
					});
				});
				
			}else{
				$.alert(resp.msg);
			}
			$(obj).removeAttr("disabled");
		});
	}
}

var checkdownLoadStatus = function(wjid,intervalTime){
	$.ajax({
		type : "POST",
		url : $('#ajaxForm #urlPrefix').val() + "/common/export/commCheckExport",
		data : {"wjid" : wjid+"","access_token":$("#ac_tk").val(),"concatCs":$('#ajaxForm #concatCs').val()},
		dataType : "json",
		success:function(data){
			if(data.cancel){//取消导出则直接return
				return;
			}
			if(data.result==false){
				if(data.msg && data.msg!=""){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}
				else{
					if(intervalTime < 5000)
						intervalTime = intervalTime + 1000;
					if($("#exportCount")){$("#exportCount").html(data.currentCount)}
					setTimeout("checkdownLoadStatus('"+wjid+"',"+ intervalTime +")",intervalTime)
				}
			}else{
				if(data.msg && data.msg!=""){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}
				else{
					if($("#cardiv")) $("#cardiv").remove();
					window.open($('#ajaxForm #urlPrefix').val() + "/common/export/commDownloadExport?wjid="+wjid + "&access_token="+$("#ac_tk").val());
				}
			}
		}
	});
}

//点击+图标添加字段
var seletLi = function(obj){
	var selectLi = $(obj).parent().parent();
	selectLi.find("i").remove();
	var selectCol = selectLi.clone();
	selectLi.remove();
	$("#list2").append(selectCol);
	/*selectCol.hover(function(){
		createDelEvent(this);
	},function(){
		createOutEvent(this);
	});*/
	
	sortData();
}
//点击-图标去除字段
var unseletLi = function(obj){
	var selectLi = $(obj).parent().parent();
	selectLi.find("i").remove();
	var selectCol = selectLi.clone();
	selectLi.remove();
	selectCol.find("#dczd").attr("name","dczd");
	var flmc = selectCol.find("#flmc").val();
	$("#"+flmc).parent().after(selectCol);
	//$("ul [id='"+flmc+"']").after(selectCol);//特殊字符处理
	/*selectCol.hover(function(){
		createAddEvent(this);
	},function(){
		createOutEvent(this);
	});*/
	sortData();
}

function createAddEvent(t){
	$(t).children("div").addClass("export-select");
	$(t).children("div").append('<i class="fa fa-plus-circle fa-lg text-success"></i>');
	$(t).children("div").find('i').mousedown(function(){
		seletLi($(this));
		return false;
	});
	return false;
}

function createDelEvent(t){
	$(t).children("div").addClass("export-select");
	$(t).children("div").append('<i class="fa fa-minus-circle fa-lg text-danger"></i>');
	$(t).children("div").find('i').mousedown(function(){
		unseletLi($(this));
		return false;
	});
}

function createOutEvent(t){
	$(t).children("div").removeClass("export-select");
	$(t).children("div").find('i').remove();
	sortData();
}

$("#list1, #list2").dragsort({
	dragSelector: "div",
    dragSelectorExclude:"input, textarea, .sorting-line-desc",
	dragBetween: true,
	dragEnd: saveOrder,
	placeHolderTemplate: "<li class='placeHolder'><div></div></li>",
	scrollSpeed: 5
});

function saveOrder() {
	var data = $("#list1 li").map(function(){
		return
	}).get();
	$("input[name=list1SortOrder]").val(data.join("|"));
};

var search_timer;
$(document).ready(function () {
	$('#search_input').keydown(function (e) {
		if(e.keyCode == 13){
			return false;
		}
    });
	$('#search_input').keyup(function (e) {
        clearTimeout(search_timer); // 清除计时器
        var t = this;
	    search_timer = setTimeout(function () { // 使用计时器，防止快速输入或打拼音的时候进行无用的过滤，提升性能
            filterItems(t);
        },500);
    });
});

function filterItems(t) {
    var $t = $(t);
    var v = $t.val();
    if(v == ''){
        $t.closest('.search-box').siblings('.dragsort').find('li').show(250);
        return false;
    }
    $t.closest('.search-box').siblings('.dragsort').find('li>div').each(function () {
        var t = $(this).text();
        if(t.indexOf(v) >= 0){
            $(this).parent('li').show(250);
        }else{
            $(this).parent('li').hide(250);
        }
    });
}
function sortData() {
	var $li = this;
	var _choseListDiv = $("#list2 div[id='choseListDiv']");
	for(var i=0;i<_choseListDiv.length;i++){
		$(_choseListDiv[i]).find("#dczd").attr("name","choseList["+i+"]");
	}
	$("#list1").children('li').not('.sorting-line').each(function(){	
		$(this).children("div").find('i').remove();
		//createAddEvent($li);
		$(this).off().hover(function(){
			createAddEvent(this);
		},function(){
			createOutEvent(this);
		});
		
	});
	
	$("#list2").children('li').each(function(){
		$(this).children("div").find('i').remove();
		//createDelEvent($li);
		$(this).off().hover(function(){
			createDelEvent(this);
		},function(){
			createOutEvent(this);
		});
		
	});
	if($li.length>0){
		$li.mouseover();
	}
	
};

jQuery(function(){
	//选择文件类型
	$("input[name='fileType']").unbind("change").change(function(){
		var fileType = $("input[name='fileType']:checked").val();
	})

	//鼠标悬停事件
	$("#list1 li").not(".sorting-line").unbind().hover(function(){
		createAddEvent(this);
	},function(){
		createOutEvent(this);
	});

	$("#list2 li").unbind().hover(function(){
		createDelEvent(this);
	},function(){
		createOutEvent(this);
	});

	$("#right-all").unbind("click").click(function(){
		var selAll = $("#list1 li").not(".sorting-line").clone()
		$("#list2").append(selAll);
		$("#list1 li").not(".sorting-line").remove();
		sortData();
	})

	$("#left-all").unbind("click").click(function(){
		var selAll = $("#list2").html();
		var _choseListDiv = $("#list2 div[id='choseListDiv']");
		for(var i=_choseListDiv.length-1;i>=0;i--){
			var selectCol = $(_choseListDiv[i]);
			var flmc = selectCol.find("#flmc").val();
			$("#"+flmc).parent().after($(_choseListDiv[i]).parent());
			selectCol.find("#dczd").attr("name","dczd");
			//$("ul [id='"+flmc+"']").after(selectCol);//特殊字符处理留着备用
		}
		$("#list2").empty();
		sortData();
	})

});

function changeDczd(cyid){
	var cardiv =document.createElement("div");
	cardiv.id="cardiv";
	var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;">' +
					'<div align="center" style="top:45%">' +
						'<img src="/loading.gif">' +
							'</div></div>';
	cardiv.innerHTML=s_str;
	document.body.appendChild(cardiv);
	$("#left-all").click();
	$.ajax({
		type : "POST",
		url : "/common/export/commGetCydcxx",
		data : {"cyid" : cyid,"access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.cydcxxs != null && data.cydcxxs.length != 0){
				$.each(data.cydcxxs,function(i){
					var dczd = data.cydcxxs[i].dczd;
					if(dczd != null && dczd != ''){
					createAddEvent($("input[value='"+dczd+"']").parent("div").parent("li"));
					$("input[value='"+dczd+"']").parent("div").parent("li").find('i').mousedown();
					}
				});
			}else{
				$.alert("此常用设置未设置导出字段！");
				
			}
		}
	});
	
	 $("#cardiv").remove();
}

