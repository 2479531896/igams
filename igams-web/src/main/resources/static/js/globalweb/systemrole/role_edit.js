function initPage(){
	if($('#dwxdbj').prop("checked"))
		$('#dwxdbj').bootstrapSwitch('state', true);
	else
		$('#dwxdbj').bootstrapSwitch('state', false);
	
	var jsjgids = "";
	for (var i = 0; i < $("#role_ajaxForm #sel_list optgroup option").length; i++) {//循环读取已选角色
		jsjgids = jsjgids + ","+ $("#role_ajaxForm #sel_list optgroup")[0].children[i].value;
	}
	jsjgids = jsjgids.substr(1);
	$("#role_ajaxForm #jsjgids").val(jsjgids);
}
function checkDwxz(){
	var jsid=$("#jsid").val();
	if($('#dwxdbj').prop("checked")){
		if(jsid != null && jsid){
			//查询用户机构
			$.ajax({
				url:'/systemrole/configunit/pagedataYhidByJsid',
				type:'post',
				dataType:'JSON',
				async : false,
				data:{"access_token":$("#ac_tk").val(),"jsid":jsid},
				success:function(data){
					if(data.yhjsList!=null){
						for(var i = 0; i < data.yhjsList.length; i++){
							var yhHtml="<div class='col-md-12 col-sm-12'  id='"+data.yhjsList[i].yhid+"' style='margin-bottom:20px;'>";
								yhHtml+="<div class='col-md-6 col-sm-6' style='margin-bottom:10px;' >";
								yhHtml+="<label for='' class='col-md-6 col-sm-6 control-label'>";
								yhHtml+="<span>所属用户：</span>";
								yhHtml+="</label>";	
								yhHtml+="<div class='col-sm-6 col-md-6  padding_t7'>";
								yhHtml+="<input type='hidden' name='xtyhDtos["+$("#jsjgDiv").children().length+"].yhid'  value='"+data.yhjsList[i].yhid+"'>";
								yhHtml+="<span>"+data.yhjsList[i].zsxm+"</span>";
								yhHtml+="</div>";
								yhHtml+="</div>";
								yhHtml+="<div class='col-md-6 col-sm-6'>";
								yhHtml+="<label for='' class='col-md-4 col-sm-4 control-label'>";
								yhHtml+="<span>机构：</span>";
								yhHtml+="</label>";
								yhHtml+="<div class='col-sm-8 col-md-8' style='padding-right:0px;'>";
								yhHtml+="<input type='text'  id='jgmcs'  name='noname' data-provide='typeahead' data-items='5' style='border:0px;border-bottom:1px solid #cccccc;width:130px;height:25px;outline:none;'>";
								yhHtml+="</div>";
								yhHtml+="</div>";
								yhHtml+="<div class='col-md-12 col-sm-12'>";
								yhHtml+="<label for='' class='col-md-3 col-sm-3 control-label' >";
								yhHtml+="<span style='margin-right:7px;'>已选机构：</span>";
								yhHtml+="</label>";
								yhHtml+="<div class='col-md-9 col-sm-9' style='padding-right:0px;'>";
								yhHtml+="<input   type='text' id='jgids'  name='xtyhDtos["+$("#jsjgDiv").children().length+"].jgids' class='form-control'>";
								yhHtml+="</div>";
								yhHtml+="</div>";
								yhHtml+="</div>";
							$("#role_ajaxForm #jsjgDiv").append(yhHtml);
							JgxxSearch(data.yhjsList[i].yhid);
							//当前的用户和角色对应的机构
							if(data.yhjsList[i].dtos && data.yhjsList[i].dtos.length>0){
								for (var j = 0; j <data.yhjsList[i].dtos.length; j++) {
									$("#"+data.yhjsList[i].dtos[j].yhid+"  #jgids").tagsinput({
										itemValue: "value",
										itemText: "text",
									})
									$("#"+data.yhjsList[i].dtos[j].yhid+"  #jgids").tagsinput('add', { value: data.yhjsList[i].dtos[j].jgid, text:data.yhjsList[i].dtos[j].jgmc});
								}
							}
						}
					}
				}
			})
		}
		//显示角色机构选择
		$("#role_ajaxForm #xzjgDiv").show();
	}else{
		$("#role_ajaxForm #jsjgDiv").empty();
		//隐藏角色机构选择
		$("#role_ajaxForm #xzjgDiv").hide();
	}
	return true;
}

/**
 * 机构名称模糊查询
 * @returns
 */
function JgxxSearch(yhid){
	$('#role_ajaxForm #jgmcs').typeahead({
		source: function(query, process) {
			return $.ajax({
				url: '/systemrole/organ/pagedataJgxxByjgmc',
				type: 'post',
				data: {
					"jgmc": query,
					"access_token":$("#ac_tk").val()
				},
				dataType: 'json',
				success: function(result) {
					var resultList = result.jgxxList
						.map(function(item) {
							var aItem = {
								jgmc: item.jgmc,
								jgid: item.jgid,
							};
							return JSON.stringify(aItem);
						});
					return process(resultList);
				}
			});
		},
		matcher: function(obj) {
			var item = JSON.parse(obj);
			return ~item.jgmc.toLowerCase().indexOf(
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
				if (!item.jgmc.toLowerCase().indexOf(
						this.query.toLowerCase()))
					beginswith.push(JSON.stringify(item));
				else if (~item.jgmc.indexOf(this.query))
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
			return item.jgmc.replace(new RegExp('(' + query +
				')', 'ig'), function($1, match) {
				return '<strong>' + match + '</strong>'
			})
		},
		updater: function(obj) {
			var item = JSON.parse(obj);
			tagsinputJsxx(yhid,item.jgid,item.jgmc);
			return item.jgmc;
		}
	});
}
function tagsinputJsxx(yhid,jgid,jgmc){
	$("#"+yhid+"  #jgids").tagsinput({
		itemValue: "value",
		itemText: "text",
		})
	$("#"+yhid+"  #jgids").tagsinput('add', { value: jgid, text: jgmc});
}

//增加机构
function addOption(){
	var unselected = $("#role_ajaxForm #sel_unlist optgroup");
	var selected = $("#role_ajaxForm #sel_list optgroup");
	for (var i = $("#role_ajaxForm #sel_unlist optgroup option").length-1; i >= 0; i--) {
		 if(unselected[0].children[i].selected){
			 selected.append("<option value='"+unselected[0].children[i].value+"'ondblclick='moveOption()'>"+unselected[0].children[i].text+"</option>");
			 unselected[0].children[i].remove();
		 }
	}
	var jgids = "";
	for (var i = 0; i < $("#role_ajaxForm #sel_list optgroup option").length; i++) {//循环读取已选角色
		jgids = jgids + ","+ $("#role_ajaxForm #sel_list optgroup")[0].children[i].value;
	}
	jgids = jgids.substr(1);
	$("#role_ajaxForm #jsjgids").val(jgids);
}
//移除机构
function moveOption(){
	var unselected = $("#role_ajaxForm #sel_unlist optgroup");
	var selected = $("#role_ajaxForm #sel_list optgroup");
	for (var i = $("#role_ajaxForm #sel_list optgroup option").length-1; i >= 0; i--) {
		 if(selected[0].children[i].selected){
			 unselected.append("<option value='"+selected[0].children[i].value+"'ondblclick='addOption()'>"+selected[0].children[i].text+"</option>");
			 selected[0].children[i].remove();
		 }
	}
	var jgids = "";
	for (var i = 0; i < $("#role_ajaxForm #sel_list optgroup option").length; i++) {//循环读取已选角色
		jgids = jgids + ","+ $("#role_ajaxForm #sel_list optgroup")[0].children[i].value;
	}
	jgids = jgids.substr(1);
	$("#role_ajaxForm #jsjgids").val(jgids);
}

$(document).ready(function(){
	//$('#user_ajaxForm input[type="checkbox"]').bootstrapSwitch();
	//基本初始化
    $('#role_ajaxForm input[type="checkbox"]').bootstrapSwitch({
        handleWidth: '25px',
        labelWidth: '25px',
    });
	//初始化页面数据
	initPage();
	//所有下拉框添加choose样式
	jQuery('#role_ajaxForm .chosen-select').chosen({width: '100%'});
});