//事件绑定
function btnBind() {
	$('#ajaxForm #zsxm').typeahead({
		source : function(query, process) {
			$('#ajaxForm #fzr').val(null);
			return $.ajax({
				url : $("#ajaxForm #urlprefix").val()+'/systemmain/task/commonSelectUser',
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
									name : item.zsxm
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
			$('#ajaxForm #fzr').attr('value', item.id);
			$('#ajaxForm #ddid').attr('value', item.dd);
			return item.name;
		}
	});
	// for (var i = 0; i < $("#ajaxForm #gzglDtos").val().length; i++) {
	// 	var id = '#ajaxForm #qwwcsj_'+i
	// 	// 添加日期控件
	// 	laydate.render({
	// 		elem : id,
	// 		theme : '#2381E9',
	// 		trigger: 'click'
	// 	});
	// };
}

function chooseFzr() {
	var url = $("#ajaxForm #urlprefix").val()+"/systemmain/task/pagedataListFzr?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择负责人', chooseTreeFzrConfig);
}
var chooseTreeFzrConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseFzrModal",
	formName	: "ajaxform",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var json=$("#ajaxform #jsids").val();
				if (json){
					$("#ajaxForm  #fzr").tagsinput({
						itemValue: "value",
						itemText: "text",
					})
					var jsonStr=eval('('+json+')');
					for (var i = 0; i < jsonStr.length; i++) {
						$("#ajaxForm  #fzr").tagsinput('add',jsonStr[i]);
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

function initPage() {
}

$(document).ready(function() {
	//所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({
		width : '100%'
	});
	//绑定事件 
	btnBind();
	//初始化页面数据			
	initPage();
});

$("#ajaxForm input").keypress(function (event) {
	// 13代表当前的按键是 13
	if (event.keyCode == 13) {
		return false;
	}
})