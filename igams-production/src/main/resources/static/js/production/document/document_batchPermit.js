/**
 * 绑定按钮事件
 */
function btnBind(){
	//添加日期控件
	laydate.render({
	   elem: '#ajaxForm #wj_qwwcsj'
	  ,theme: '#2381E9'
	});
}

function init(){
	var jsids = "";
	for (var i = 0; i < $("#ajaxForm #sel_list optgroup option").length; i++) {//循环读取已选角色
		jsids = jsids + ","+ $("#ajaxForm #sel_list optgroup")[0].children[i].value;
	}
	jsids = jsids.substr(1);
	$("#ajaxForm #jsids").val(jsids);
}

var vm = new Vue({
	el:'#document_batchPermit',
	data: {
	},
	methods:{
		addOption(){
			var unselected = $("#ajaxForm #sel_unlist optgroup");
			var t_unselected = $("#ajaxForm #t_sel_unlist optgroup");
			var selected = $("#ajaxForm #sel_list optgroup");
			for (var i = $("#ajaxForm #sel_unlist optgroup option").length-1; i >= 0; i--) {
				 if(unselected[0].children[i].selected){
					 selected.append("<option value='"+unselected[0].children[i].value+"'ondblclick='moveOption()'>"+unselected[0].children[i].text+"</option>");
					 unselected[0].children[i].remove();
					 t_unselected[0].children[i].remove();
				 }
			}
			var jsids = "";
			for (var i = 0; i < $("#ajaxForm #sel_list optgroup option").length; i++) {//循环读取已选角色
				jsids = jsids + ","+ $("#ajaxForm #sel_list optgroup")[0].children[i].value;
			}
			jsids = jsids.substr(1);
			$("#ajaxForm #jsids").val(jsids);
		},
		moveOption(){
			var unselected = $("#ajaxForm #sel_unlist optgroup");
			var t_unselected = $("#ajaxForm #t_sel_unlist optgroup");
			var selected = $("#ajaxForm #sel_list optgroup");
			for (var i = $("#ajaxForm #sel_list optgroup option").length-1; i >= 0; i--) {
				 if(selected[0].children[i].selected){
					 unselected.append("<option value='"+selected[0].children[i].value+"'ondblclick='addOption()'>"+selected[0].children[i].text+"</option>");
					 t_unselected.append("<option value='"+selected[0].children[i].value+"'ondblclick='t_addOption()'>"+selected[0].children[i].text+"</option>");
					 selected[0].children[i].remove();
				 }
			}
			var jsids = "";
			for (var i = 0; i < $("#ajaxForm #sel_list optgroup option").length; i++) {//循环读取已选角色
				jsids = jsids + ","+ $("#ajaxForm #sel_list optgroup")[0].children[i].value;
			}
			jsids = jsids.substr(1);
			$("#ajaxForm #jsids").val(jsids);
		},
		t_addOption(){
			var t_unselected = $("#ajaxForm #t_sel_unlist optgroup");
			var t_selected = $("#ajaxForm #t_sel_list optgroup");
			var unselected = $("#ajaxForm #sel_unlist optgroup");
			for (var i = $("#ajaxForm #t_sel_unlist optgroup option").length-1; i >= 0; i--) {
				 if(t_unselected[0].children[i].selected){
					 t_selected.append("<option value='"+t_unselected[0].children[i].value+"'ondblclick='t_moveOption()'>"+t_unselected[0].children[i].text+"</option>");
					 t_unselected[0].children[i].remove();
					 unselected[0].children[i].remove();
				 }
			}
			var jsids = "";
			for (var i = 0; i < $("#ajaxForm #t_sel_list optgroup option").length; i++) {//循环读取已选角色
				jsids = jsids + ","+ $("#ajaxForm #t_sel_list optgroup")[0].children[i].value;
			}
			jsids = jsids.substr(1);
			$("#ajaxForm #t_jsids").val(jsids);
		},
		t_moveOption(){
			var t_unselected = $("#ajaxForm #t_sel_unlist optgroup");
			var t_selected = $("#ajaxForm #t_sel_list optgroup");
			var unselected = $("#ajaxForm #sel_unlist optgroup");
			for (var i = $("#ajaxForm #t_sel_list optgroup option").length-1; i >= 0; i--) {
				 if(t_selected[0].children[i].selected){
					 t_unselected.append("<option value='"+t_selected[0].children[i].value+"'ondblclick='t_addOption()'>"+t_selected[0].children[i].text+"</option>"); 
					 unselected.append("<option value='"+t_selected[0].children[i].value+"'ondblclick='addOption()'>"+t_selected[0].children[i].text+"</option>");
					 t_selected[0].children[i].remove();
				 }
			}
			var jsids = "";
			for (var i = 0; i < $("#ajaxForm #t_sel_list optgroup option").length; i++) {//循环读取已选角色
				jsids = jsids + ","+ $("#ajaxForm #t_sel_list optgroup")[0].children[i].value;
			}
			jsids = jsids.substr(1);
			$("#ajaxForm #t_jsids").val(jsids);
		},
		d_addOption(){
			var d_unselected = $("#ajaxForm #d_sel_unlist optgroup");
			var d_selected = $("#ajaxForm #d_sel_list optgroup");
			for (var i = $("#ajaxForm #d_sel_unlist optgroup option").length-1; i >= 0; i--) {
				 if(d_unselected[0].children[i].selected){
					 d_selected.append("<option value='"+d_unselected[0].children[i].value+"'ondblclick='d_moveOption()'>"+d_unselected[0].children[i].text+"</option>");
					 d_unselected[0].children[i].remove();
				 }
			}
			var jsids = "";
			for (var i = 0; i < $("#ajaxForm #d_sel_list optgroup option").length; i++) {//循环读取已选角色
				jsids = jsids + ","+ $("#ajaxForm #d_sel_list optgroup")[0].children[i].value;
			}
			jsids = jsids.substr(1);
			$("#ajaxForm #d_jsids").val(jsids);
		},
		d_moveOption(){
			var d_unselected = $("#ajaxForm #d_sel_unlist optgroup");
			var d_selected = $("#ajaxForm #d_sel_list optgroup");
			for (var i = $("#ajaxForm #d_sel_list optgroup option").length-1; i >= 0; i--) {
				 if(d_selected[0].children[i].selected){
					 d_unselected.append("<option value='"+d_selected[0].children[i].value+"'ondblclick='d_addOption()'>"+d_selected[0].children[i].text+"</option>");
					 d_selected[0].children[i].remove();
				 }
			}
			var jsids = "";
			for (var i = 0; i < $("#ajaxForm #d_sel_list optgroup option").length; i++) {//循环读取已选角色
				jsids = jsids + ","+ $("#ajaxForm #d_sel_list optgroup")[0].children[i].value;
			}
			jsids = jsids.substr(1);
			$("#ajaxForm #d_jsids").val(jsids);
		}
	}
})
//双击事件
function d_moveOption(){
	var d_unselected = $("#ajaxForm #d_sel_unlist optgroup");
	var d_selected = $("#ajaxForm #d_sel_list optgroup");
	for (var i = $("#ajaxForm #d_sel_list optgroup option").length-1; i >= 0; i--) {
		 if(d_selected[0].children[i].selected){
			 d_unselected.append("<option value='"+d_selected[0].children[i].value+"'ondblclick='d_addOption()'>"+d_selected[0].children[i].text+"</option>");
			 d_selected[0].children[i].remove();
		 }
	}
	var jsids = "";
	for (var i = 0; i < $("#ajaxForm #d_sel_list optgroup option").length; i++) {//循环读取已选角色
		jsids = jsids + ","+ $("#ajaxForm #d_sel_list optgroup")[0].children[i].value;
	}
	jsids = jsids.substr(1);
	$("#ajaxForm #d_jsids").val(jsids);
}

function d_addOption(){
	var d_unselected = $("#ajaxForm #d_sel_unlist optgroup");
	var d_selected = $("#ajaxForm #d_sel_list optgroup");
	for (var i = $("#ajaxForm #d_sel_unlist optgroup option").length-1; i >= 0; i--) {
		 if(d_unselected[0].children[i].selected){
			 d_selected.append("<option value='"+d_unselected[0].children[i].value+"'ondblclick='d_moveOption()'>"+d_unselected[0].children[i].text+"</option>");
			 d_unselected[0].children[i].remove();
		 }
	}
	var jsids = "";
	for (var i = 0; i < $("#ajaxForm #d_sel_list optgroup option").length; i++) {//循环读取已选角色
		jsids = jsids + ","+ $("#ajaxForm #d_sel_list optgroup")[0].children[i].value;
	}
	jsids = jsids.substr(1);
	$("#ajaxForm #d_jsids").val(jsids);
}
//双击事件
function t_moveOption(){
	var t_unselected = $("#ajaxForm #t_sel_unlist optgroup");
	var t_selected = $("#ajaxForm #t_sel_list optgroup");
	var unselected = $("#ajaxForm #sel_unlist optgroup");
	for (var i = $("#ajaxForm #t_sel_list optgroup option").length-1; i >= 0; i--) {
		 if(t_selected[0].children[i].selected){
			 t_unselected.append("<option value='"+t_selected[0].children[i].value+"'ondblclick='t_addOption()'>"+t_selected[0].children[i].text+"</option>"); 
			 unselected.append("<option value='"+t_selected[0].children[i].value+"'ondblclick='addOption()'>"+t_selected[0].children[i].text+"</option>");
			 t_selected[0].children[i].remove();
		 }
	}
	var jsids = "";
	for (var i = 0; i < $("#ajaxForm #t_sel_list optgroup option").length; i++) {//循环读取已选角色
		jsids = jsids + ","+ $("#ajaxForm #t_sel_list optgroup")[0].children[i].value;
	}
	jsids = jsids.substr(1);
	$("#ajaxForm #t_jsids").val(jsids);
}

function t_addOption(){
	var t_unselected = $("#ajaxForm #t_sel_unlist optgroup");
	var t_selected = $("#ajaxForm #t_sel_list optgroup");
	var unselected = $("#ajaxForm #sel_unlist optgroup");
	for (var i = $("#ajaxForm #t_sel_unlist optgroup option").length-1; i >= 0; i--) {
		 if(t_unselected[0].children[i].selected){
			 t_selected.append("<option value='"+t_unselected[0].children[i].value+"'ondblclick='t_moveOption()'>"+t_unselected[0].children[i].text+"</option>");
			 t_unselected[0].children[i].remove();
			 unselected[0].children[i].remove();
		 }
	}
	var jsids = "";
	for (var i = 0; i < $("#ajaxForm #t_sel_list optgroup option").length; i++) {//循环读取已选角色
		jsids = jsids + ","+ $("#ajaxForm #t_sel_list optgroup")[0].children[i].value;
	}
	jsids = jsids.substr(1);
	$("#ajaxForm #t_jsids").val(jsids);
}

//双击事件
function moveOption(){
	var unselected = $("#ajaxForm #sel_unlist optgroup");
	var selected = $("#ajaxForm #sel_list optgroup");
	var t_unselected = $("#ajaxForm #t_sel_unlist optgroup");
	for (var i = $("#ajaxForm #sel_list optgroup option").length-1; i >= 0; i--) {
		 if(selected[0].children[i].selected){
			 unselected.append("<option value='"+selected[0].children[i].value+"'ondblclick='addOption()'>"+selected[0].children[i].text+"</option>"); 
			 t_unselected.append("<option value='"+selected[0].children[i].value+"'ondblclick='t_addOption()'>"+selected[0].children[i].text+"</option>");
			 selected[0].children[i].remove();
		 }
	}
	var jsids = "";
	for (var i = 0; i < $("#ajaxForm #sel_list optgroup option").length; i++) {//循环读取已选角色
		jsids = jsids + ","+ $("#ajaxForm #sel_list optgroup")[0].children[i].value;
	}
	jsids = jsids.substr(1);
	$("#ajaxForm #jsids").val(jsids);
}

function addOption(){
	var unselected = $("#ajaxForm #sel_unlist optgroup");
	var t_unselected = $("#ajaxForm #t_sel_unlist optgroup");
	var selected = $("#ajaxForm #sel_list optgroup");
	for (var i = $("#ajaxForm #sel_unlist optgroup option").length-1; i >= 0; i--) {
		 if(unselected[0].children[i].selected){
			 selected.append("<option value='"+unselected[0].children[i].value+"'ondblclick='moveOption()'>"+unselected[0].children[i].text+"</option>");
			 unselected[0].children[i].remove();
			 t_unselected[0].children[i].remove();
		 }
	}
	var jsids = "";
	for (var i = 0; i < $("#ajaxForm #sel_list optgroup option").length; i++) {//循环读取已选角色
		jsids = jsids + ","+ $("#ajaxForm #sel_list optgroup")[0].children[i].value;
	}
	jsids = jsids.substr(1);
	$("#ajaxForm #jsids").val(jsids);
}

$(document).ready(function(){
	btnBind();
	init();
	moveOption();
	addOption();
});