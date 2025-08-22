function init(){
	var jsids = "";
	for (var i = 0; i < $("#ajaxform #sel_list optgroup option").length; i++) {//循环读取已选角色
		jsids = jsids + ","+ $("#ajaxform #sel_list optgroup")[0].children[i].value;
	}
	jsids = jsids.substr(1);
	$("#ajaxform #jsids").val(jsids);
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

var vm = new Vue({
	el:'#document_edit',
	data: {
	},
	methods:{
		addOption(){
			var unselected = $("#ajaxform #sel_unlist");
			var selected = $("#ajaxform #sel_list");
			for (var i = $("#ajaxform #sel_unlist option").length-1; i >= 0; i--) {
				 if(unselected[0].children[i].selected){
					 selected.append("<option value='"+unselected[0].children[i].value+"'ondblclick='moveOption()'>"+unselected[0].children[i].text+"</option>");
				 }
			}
			var xtyhs=[];
			for (var i = 0; i < $("#ajaxform #sel_list option").length; i++) {//循环读取已选角色
				xtyhs.push({"value":$("#ajaxform #sel_list")[0].children[i].value,"text":$("#ajaxform #sel_list")[0].children[i].text})
			}
			var json = JSON.stringify(xtyhs);
			$("#ajaxform #jsids").val(json);
		},
		moveOption(){
			var unselected = $("#ajaxform #sel_unlist");
			var selected = $("#ajaxform #sel_list");
			for (var i = $("#ajaxform #sel_list option").length-1; i >= 0; i--) {
				 if(selected[0].children[i].selected){
					 selected[0].children[i].remove();
				 }
			}
			var xtyhs=[];
			for (var i = 0; i < $("#ajaxform #sel_list option").length; i++) {//循环读取已选角色
				xtyhs.push({"value":$("#ajaxform #sel_list")[0].children[i].value,"text":$("#ajaxform #sel_list")[0].children[i].text})
			}
			var json = JSON.stringify(xtyhs);
			$("#ajaxform #jsids").val(json);
		}
	}
})
//双击事件
function moveOption(){
	var unselected = $("#ajaxform #sel_unlist");
	var selected = $("#ajaxform #sel_list");
	for (var i = $("#ajaxform #sel_list option").length-1; i >= 0; i--) {
		 if(selected[0].children[i].selected){
			 selected[0].children[i].remove();
		 }
	}
	var xtyhs=[];
	for (var i = 0; i < $("#ajaxform #sel_list option").length; i++) {//循环读取已选角色
		xtyhs.push({"value":$("#ajaxform #sel_list")[0].children[i].value,"text":$("#ajaxform #sel_list")[0].children[i].text})
	}
	var json = JSON.stringify(xtyhs);
	$("#ajaxform #jsids").val(json);
}

function addOption(){
	var unselected = $("#ajaxform #sel_unlist");
	var selected = $("#ajaxform #sel_list");
	for (var i = $("#ajaxform #sel_unlist option").length-1; i >= 0; i--) {
		 if(unselected[0].children[i].selected){
			 selected.append("<option value='"+unselected[0].children[i].value+"'ondblclick='moveOption()'>"+unselected[0].children[i].text+"</option>");
		 }
	}
	var xtyhs=[];
	for (var i = 0; i < $("#ajaxform #sel_list option").length; i++) {//循环读取已选角色
		xtyhs.push({"value":$("#ajaxform #sel_list")[0].children[i].value,"text":$("#ajaxform #sel_list")[0].children[i].text})
	}
	var json = JSON.stringify(xtyhs);
	$("#ajaxform #jsids").val(json);
}

function getRolesMenu(){
	$('#rolesMenu').jstree( {'plugins':["wholerow"],
		'core' : {
			'data' : {
				'url' : '/systemrole/menu/commonRolesMenuTree',
				'data' : function (node) {
					var map = {};
					map['access_token'] = $("#ac_tk").val();
					map['entire'] = $("#ajaxform #entire").val();
					return map;
	            }
			}
		}
	}).on('loaded.jstree', function(e, data){
		var wH  = $(window).height();
		var dialogH = $(this).parents('.modal-dialog').height();
		var top = (wH - dialogH)/2;
		$(this).parents('.modal-dialog').css("top",top);
	}).on("select_node.jstree", function (e, data) {
		//选中事件，查询角色下人员
		selectXtyh(data.selected[0]);
	}).on("dblclick.jstree", function (event) {
		//双击事件，查询角色下人员，筛选保存
		
	});
}

/**
 * 根据角色ID获取用户
 * @param jsid
 * @returns
 */
function selectXtyh(jsid){
	$.ajax({ 
	    type:'post',  
	    url: "/web/main/pagedataYhmByjsid",
	    cache: false,
	    data: {"jsid":jsid,"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	$("#ajaxform #sel_unlist").empty();
	    	for (var i = 0; i < data.length; i++) {
	    		var option="<option value="+data[i].yhid+" ondblclick='addOption()'>"+data[i].yhm+'-'+data[i].zsxm+"</option>";
	    		$("#sel_unlist").append(option);
	    	}
	    }
	});
}
$(document).ready(function(){
	//所有下拉框添加choose样式
	getRolesMenu();

	$('#ajaxform #entire').typeahead({
		source : function(query, process) {
			return $.ajax({
				url : $("#ajaxform #urlprefix").val()+'/systemmain/task/pagedataCommonSelectUser',
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
			tree_name = item.name;
			tree_id = item.id;
			return item.name;
		}
	});
});

var tree_name;
var tree_id;

function changeList() {
	if (tree_name && tree_id){
		var selected = $("#ajaxform #sel_list");
		selected.append("<option value='"+tree_id+"'ondblclick='moveOption()'>"+tree_name+"</option>");
		var xtyhs=[];
		for (var i = 0; i < $("#ajaxform #sel_list option").length; i++) {//循环读取已选角色
			xtyhs.push({"value":$("#ajaxform #sel_list")[0].children[i].value,"text":$("#ajaxform #sel_list")[0].children[i].text})
		}
		var json = JSON.stringify(xtyhs);
		$("#ajaxform #jsids").val(json);
		tree_name = null;
		tree_id = null;
	}
}

function searchTree() {
	$.jstree.destroy();
	getRolesMenu();
}


$("#ajaxform input").keypress(function (event) {
	// 13代表当前的按键是 13
	if (event.keyCode == 13) {
		return false;
	}
})