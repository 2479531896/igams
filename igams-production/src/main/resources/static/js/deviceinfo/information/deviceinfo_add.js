/**
 * 选择申请人
 * @returns
 */
function chooseSqr() {
	var url = "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择负责人', chooseFzrConfig);
}

var chooseFzrConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseFzrModal",
	formName	: "deviceinfoForm",
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
					$('#deviceinfoForm #sqr').val(sel_row[0].yhid);
					$('#deviceinfoForm #zsxm').val(sel_row[0].zsxm);
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

/**
 * 选择采购信息
 * @returns
 */
function purchaseList(){
	var url = "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择采购信息', purchaseConfig);
}


var purchaseConfig = {
		width : "800px",
		height : "500px",
		modalName	: "purchaseModal",
		formName	: "purchaseForm",
		offAtOnce : true, // 当数据提交成功，立刻关闭窗口
		buttons : {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					var $this = this;
					var opts = $this["options"]||{};
					$.closeModal(opts.modalName);
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
/**
 * 预输入功能初始化
 * @returns
 */
function btnBind(){
	$('#deviceinfoForm #zsxm').typeahead({
		source : function(query, process) {
			$('#deviceinfoForm #sqr').val(null);
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
			$('#deviceinfoForm #sqr').attr('value', item.id);
			return item.name;
		}
	});
}

$(function(){
	laydate.render({
	 	   elem: '#deviceinfoForm #dhrq'
	 	});
	laydate.render({
	 	   elem: '#deviceinfoForm #kprq'
	 	});
	laydate.render({
	 	   elem: '#deviceinfoForm #bxrq'
	 	});
	laydate.render({
	 	   elem: '#deviceinfoForm #glrq'
	 	});
	jQuery('#deviceinfoForm .chosen-select').chosen({width: '100%'});
	btnBind();
})