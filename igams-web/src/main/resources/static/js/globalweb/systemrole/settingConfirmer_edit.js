//事件绑定
function btnBind() {
	$('#ajaxForm #glxx').typeahead({
		source : function(query, process) {
			$('#ajaxForm #szz').val(null);
			return $.ajax({
				url : '/systemmain/task/commonSelectUser',
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
			$('#ajaxForm #szz').attr('value', item.id);
			$('#ajaxForm #ddid').attr('value', item.dd);
			return item.name;
		}
	});
}

function chooseFzr() {
	var url = "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择人员', chooseFzrConfig);
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
					$('#ajaxForm #szz').val(sel_row[0].yhid);
					$('#ajaxForm #glxx').val(sel_row[0].zsxm);
					$('#ajaxForm #ddid').val(sel_row[0].ddid);
					$.closeModal(opts.modalName);
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