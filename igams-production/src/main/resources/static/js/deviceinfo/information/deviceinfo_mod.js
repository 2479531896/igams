
function chooseSqr() {
	var url = "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择负责人', chooseSqrConfig);
}
var chooseSqrConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseSqrModal",
	formName	: "deviceinfo_mod_Form",
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
					$('#deviceinfo_mod_Form #sqr').val(sel_row[0].yhid);
					$('#deviceinfo_mod_Form #sqrxm').val(sel_row[0].zsxm);
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


function chooseZrr() {
	var url = "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择负责人', chooseZrrConfig);
}
var chooseZrrConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseZrrModal",
	formName	: "deviceinfo_mod_Form",
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
					$('#deviceinfo_mod_Form #zrr').val(sel_row[0].yhid);
					$('#deviceinfo_mod_Form #zrrxm').val(sel_row[0].zsxm);
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

function btnBind(){
	$('#deviceinfo_mod_Form #zrrxm').typeahead({
		source : function(query, process) {
			$('#deviceinfo_mod_Form #zrrxm').val(null);
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
			$('#deviceinfo_mod_Form #zrr').attr('value', item.id);
			return item.name;
		}
	});
	
	$('#deviceinfo_mod_Form #sqrxm').typeahead({
		source : function(query, process) {
			$('#deviceinfo_mod_Form #sqrxm').val(null);
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
			$('#deviceinfo_mod_Form #sqr').attr('value', item.id);
			return item.name;
		}
	});
}
$(function(){
	btnBind();
	
	laydate.render({
	 	   elem: '#deviceinfo_mod_Form #dhrq'
	 	});
	laydate.render({
	 	   elem: '#deviceinfo_mod_Form #kprq'
	 	});
	laydate.render({
	 	   elem: '#deviceinfo_mod_Form #bxrq'
	 	});
	laydate.render({
	 	   elem: '#deviceinfo_mod_Form #glrq'
	 	});
	laydate.render({
	 	   elem: '#deviceinfo_mod_Form #ysrq'
	 	});
	jQuery('#deviceinfo_mod_Form .chosen-select').chosen({width: '100%'});
})