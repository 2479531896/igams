/**
 * 刷新需求单号
 * @returns
 */
function refreshZldh(){
	$.ajax({ 
	    type:'post',  
	    url:$('#produce_ajaxForm #urlPrefix').val() + "/progress/progress/pagedataRefreshZlNumber",
	    cache: false,
	    data: {"access_token":$("#ac_tk").val()},
	    dataType:'json',
	    success:function(result){
	    	$("#produce_ajaxForm #zldh").val(result.zldh);
	    }
	});
}


var yyjhcl = 0;
var jhcls = 0;
var t_map = [];
var t_index="";
// 显示字段
var columnsArray = [
	{
	// 	checkbox: true,
	// 	width: '2%'
	// },{
		title: '序号',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序号',
		width: '3%',
		align: 'left',
		visible:true
	}, {
		field: 'wlbm',
		title: '物料编码',
		width: '6%',
		align: 'left',
		visible: true
	},  {
		field: 'wlmc',
		title: '物料名称',
		width: '4%',
		align: 'left',
		visible: true
	},  {
		field: 'scs',
		title: '生产商',
		width: '4%',
		align: 'left',
		visible: true
	}, {
		field: 'gg',
		title: '规格',
		width: '4%',
		align: 'left',
		visible: true
	},{
		field: 'jldw',
		title: '单位',
		width: '4%',
		align: 'left',
		visible: true
	},{
		field: 'scsl',
		title: '生产数量',
		width: '4%',
		align: 'left',
		visible: true
	},{
		field: 'wlid',
		title: '物料id',
		width: '8%',
		align: 'left',
		visible: false
	},{
		field: 'yjwcsj',
		title: '预计完成时间',
		width: '6%',
		align: 'left',
		formatter: yjwcsjformat,
		visible: true,
	},{
		field: 'xlh',
		title: '序列号',
		width: '6%',
		align: 'left',
		formatter: produceForm_xlhformat,
		visible: true,
	}, {
		field: 'cz',
		title: '操作',
		width: '6%',
		align: 'left',
		formatter:produceAjaxForm_czformat,
		visible: true
	}];
var progress_TableInit = function () {
	var oTableInit = new Object();
	//初始化Table
	oTableInit.Init = function () {
		$('#produce_ajaxForm #produce_tb_list').bootstrapTable({
			url: $('#produce_ajaxForm #urlPrefix').val()+$('#produce_ajaxForm #url').val(),         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#produce_ajaxForm #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   //是否显示分页（*）
			sortable: true,                     //是否启用排序
			sortName: "xqjhmx.lrsj",					//排序字段
			sortOrder: "asc",                   //排序方式
			queryParams: oTableInit.queryParams,//传递参数（*）
			sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
			pageNumber:1,                       //初始化加载第一页，默认第一页
			pageSize: 15,                       //每页的记录行数（*）
			pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
			paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
			search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			isForceTable: true,
			showColumns: false,                  //是否显示所有的列
			showRefresh: false,                  //是否显示刷新按钮
			minimumCountColumns: 2,             //最少允许的列数
			clickToSelect: false,                //是否启用点击选中行
			//height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "xqjhmx.xqjhmxid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: columnsArray,
			onLoadSuccess: function (map) {
				t_map = map;
				if ($("#produce_ajaxForm #sczlmxid").val()){
					yyjhcl = $('#produce_ajaxForm #jhcl').val()*1 - map.rows[0].scsl*1
				}
			},
			onDblClickRow: function (row, $element) {
				return;
			},
		});
	};

	//得到查询的参数
	oTableInit.queryParams = function(params){
		//请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
		//例如 toolbar 中的参数 
		//如果 queryParamsType = ‘limit’ ,返回参数必须包含 
		//limit, offset, search, sort, order 
		//否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder. 
		//返回false将会终止请求
		var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			pageSize: 15,   //页面大小
			pageNumber: 1,  //页码
			access_token:$("#ac_tk").val(),
			sortName: params.sort,      // 排序列名
			sortOrder: params.order, // 排位命令（desc，asc）
			sortLastName: "xqjhmx.xqjhmxid", // 防止同名排位用
			sortLastOrder: "asc", // 防止同名排位用
			sczlmxid: $("#produce_ajaxForm #sczlmxid").val(), // 防止同名排位用
		};
		return map;
	};
	return oTableInit;
};
//撤销拆分
function No(){
	$("#produce_ajaxForm #produce_tb_list").bootstrapTable('load',t_map);
}

//确认拆分
function Yes(index){
	var value=$("#produce_ajaxForm #ts"+index).val();
	if(value){
		if (value*1 > 0){
			let sl = t_map.rows[index].scsl;
			let scsl = Number(sl)/Number(value);
			if (Number(sl)%Number(value) != 0 && value*1 > 1){
				$.confirm("请输入生产数量的整除数！");
				$("#produce_ajaxForm #ts"+index).val("");
				return;
			} else if (Number(sl)%Number(value) == 0 && value*1 > 1){
				for(var i=1;i<=value*1-1;i++){
					var sz = {"xlh":t_map.rows[index].xlh,"wlid":t_map.rows[index].wlid,"scsl":scsl,"wlbm":t_map.rows[index].wlbm,"wlmc":t_map.rows[index].wlmc,"scs":t_map.rows[index].scs,"gg":t_map.rows[index].gg,"jldw":t_map.rows[index].jldw};
					$("#produce_ajaxForm #produce_tb_list").bootstrapTable('insertRow', {
						index: parseInt(index)+i,
						row: sz
					});
				}
			}else{
				scsl = parseInt(scsl);
				let zsl = 0;
				for (let i = 0; i < t_map.rows.length; i++) {
					zsl += t_map.rows[i].scsl
				}
				let sysl = jhcls*1-(zsl*1 - sl*1);
				if (sysl < scsl ){
					$.confirm("产生数量不能大于计划产量！");
					$("#produce_ajaxForm #ts"+index).val("");
					return;
				}
			}

			t_map.rows[index].scsl = scsl;

		}else{
			$.confirm("请输入正数！");
			$("#produce_ajaxForm #ts"+index).val("");
			return;
		}
		$("#produce_ajaxForm #produce_tb_list").bootstrapTable('load',t_map);
	}else{
		$.confirm("请输入需要拆分的数量！");
	}
}
function splitWl(index,event){
	if (t_map.rows[index].scsl*1 == 1){
		$.confirm("生产数量为1 不允许拆分！");
		return;
	}
	var html="";
	html="<div class='col-sm-12 col-md-12 col-xs-12' style='padding:0px;'>" +
		"<div class='input-group'>" +
		"<input id='ts"+index+"'  type='text' class='form-control success'  style='padding:0px;' autocomplete='off'>"+
		"<span class='input-group-btn' style='height:28px !important'>"+
		"<button type='button' onclick=\"Yes('" + index + "')\" class='btn btn-success glyphicon glyphicon-ok' style='height:28px !important'></button>"+
		"<button type='button' onclick=\"No('" + index + "')\" class='btn btn-danger glyphicon glyphicon-remove' style='height:28px !important'></button>"+
		"</span>"+
		"</div>" +
		"</div>";
	$("#produce_ajaxForm #cf"+index).empty();
	$("#produce_ajaxForm #cf"+index).append(html);
}
/**
 * 预计完成时间格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function yjwcsjformat(value,row,index){
	if(row.yjwcsj == null){
		row.yjwcsj = "";
	}
	var html = "<input id='yjwcsj_"+index+"' value='"+row.yjwcsj+"' name='yjwcsj_"+index+"' style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' validate='{required:true}' onchange=\"checkSum('"+index+"',this,\'yjwcsj\')\"><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='预计完成时间'><span></input>";
	setTimeout(function() {
		laydate.render({
			elem: '#produce_ajaxForm #yjwcsj_'+index
			,theme: '#2381E9'
			,min: 1
			,btns: ['clear', 'confirm']
			,done: function(value, date, endDate){
				t_map.rows[index].yjwcsj = value;
			}
		});
	}, 100);
	return html;
}
/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function produceAjaxForm_czformat(value,row,index){
	var html="";
	if ($('#produce_ajaxForm #czbs').val()){
		return html;
	}
	let cplxmc = $('#produce_ajaxForm #cplxmc').val();
	if (cplxmc == "YQ"){
		html +="<div id='cf"+index+"'><span style='margin-left:5px;height:24px !important;' class='btn btn-success' title='拆分明细' onclick=\"splitWl('" + index + "',event)\" >拆分</span>";
	}
	html = html+"<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除物料信息' onclick=\"delWlInfo('" + index +  "',event)\" >删除</span>";
	return html;
}

/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delWlInfo(index,event){
	t_map.rows.splice(index,1);
	$("#produce_ajaxForm #produce_tb_list").bootstrapTable('load',t_map);
}
function checkProduceValue(index, e, zdmc){
	if(zdmc=="xlh"){
		t_map.rows[index].xlh = e.value;
	}
	$("#produce_ajaxForm #produce_tb_list").bootstrapTable('load',t_map);
}
/**
 * 要求格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function produceForm_xlhformat(value,row,index){
	if(row.xlh == null){
		row.xlh = "";
	}
	var html = "<input id='xlh_"+index+"' autocomplete='off' value='"+row.xlh+"' name='xlh_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{required:true,stringMaxLength:32}' onchange=\"checkProduceValue('"+index+"',this,\'xlh\')\"></input>";
	return html;
}

/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkSum(index, e, zdmc) {
	if(t_map.rows.length > index){
		if (zdmc == 'yjwcsj') {
			t_map.rows[index].yjwcsj = e.value;
		}
	}
}

$(document).ready(function(){
	//添加日期控件
	laydate.render({
		elem: '#produce_ajaxForm #zlrq'
		,theme: '#2381E9'
	});
	//初始化列表
	var oTable=new progress_TableInit();
	oTable.Init();
	//0.初始化fileinput
	var oFileInput = new FileInput();
	var picking_params=[];
	picking_params.prefix=$("#produce_ajaxForm #urlPrefix").val();
	oFileInput.Init("produce_ajaxForm","produceUpInfo",2,1,"pro_file",null,picking_params);
	//所有下拉框添加choose样式
	jQuery('#produce_ajaxForm .chosen-select').chosen({width: '100%'});


});
/**
 * 计划产量改变
 * @returns
 */
function jhclChoose(){
	let jhcl= $('#produce_ajaxForm #jhcl').val();
	if (jhcl*1 <=0){
		$('#produce_ajaxForm #jhcl').val("")
		$.alert("计划产量不能小于1！")
		return false;
	}

	let zsl = 0;
	for (let i = 0; i < t_map.rows.length; i++) {
		zsl += t_map.rows[i].scsl
	}
	if ($("#produce_ajaxForm #sczlmxid").val()){
		if (jhcl*1 <= yyjhcl){
			$('#produce_ajaxForm #jhcl').val("")
			$.alert("计划产量不能小于"+(yyjhcl*1+1)+"!")
			return false;
		}
		jhcl = jhcl*1 - yyjhcl*1;
		// if ($("#produce_ajaxForm #xqjhmxid").val()){
		// 	let kysl = $("#produce_ajaxForm #kysl").val()*1 - yyjhcl*1;
		// 	if(kysl*1 < jhcl*1){
		// 		$('#produce_ajaxForm #jhcl').val($("#produce_ajaxForm #kysl").val())
		// 		$.alert("计划产量不能大于需求生产数量"+$("#produce_ajaxForm #kysl").val()+"!")
		// 		return false;
		// 	}
		// }
	}
	if (t_map.rows.length>0){
		if (jhcl*1 > zsl*1){
			t_map.rows[0].scsl = jhcl*1- zsl*1 +t_map.rows[0].scsl*1
		}else{
			let zs = 0;
			for (let i = 0; i < t_map.rows.length; i++) {
				zs +=  t_map.rows[i].scsl*1;
				if (zs*1 > jhcl){
					if(i+1< t_map.rows.length){
						t_map.rows.splice(i+1,t_map.rows.length*1);
					}
					t_map.rows[i].scsl = jhcl*1 - zs*1 + t_map.rows[i].scsl*1;
					break;
				}else if (zs*1 == jhcl){
					if(i+1< t_map.rows.length){
						t_map.rows.splice(i+1,t_map.rows.length*1);
						break;
					}
				}

			}
		}
	}
    $("#produce_ajaxForm #produce_tb_list").bootstrapTable('load',t_map);
	jhcls = jhcl;

}
/**
 * 点击显示文件上传
 * @returns
 */
function editfile(){
	$("#fileDiv").show();
	$("#file_btn").hide();
}

/**
 * 点击隐藏文件上传
 * @returns
 */
function cancelfile(){
	$("#fileDiv").hide();
	$("#file_btn").show();
}

/**
 * 点击图片预览
 * @param fjid
 * @param wjm
 * @returns
 */
function view(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url=$("#produce_ajaxForm #urlPrefix").val()+"/ws/sjxxpripreview/?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= $("#produce_ajaxForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
		$.showDialog(url,'文件预览',JPGMaterConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}
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
/**
 * 附件上传回调
 * @param fjid
 * @returns
 */
function produceUpInfo(fjid){
	if(!$("#produce_ajaxForm #fjids").val()){
		$("#produce_ajaxForm #fjids").val(fjid);
	}else{
		$("#produce_ajaxForm #fjids").val($("#produce_ajaxForm #fjids").val()+","+fjid);
	}
}

/**
 * 根据物料名称模糊查询
 */
$('#produce_ajaxForm #addnr').typeahead({
	source : function(query, process) {
		return $.ajax({
			url : $('#produce_ajaxForm #urlPrefix').val()+'/progress/progress/pagedataQueryWlxx',
			type : 'post',
			data : {
				"entire" : query,
				"access_token" : $("#ac_tk").val()
			},
			dataType : 'json',
			success : function(result) {
				var resultList = result.dtoList
					.map(function(item) {
						var aItem = {
							id : item.wlid,
							name : item.wlbm+"-"+item.wlmc+"-"+item.scs+"-"+item.gg
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
		$('#produce_ajaxForm #addwlid').attr('value', item.id);
		return item.name;
	}
});

/**
 * 回车键添加物料至请购车
 * @param event
 * @returns
 */
var addkeydown = $("#addnr").bind("keydown",function(event){
	var wlid=$('#produce_ajaxForm #addwlid').val();
	if (event.keyCode == "13") {
		//判断新增输入框是否有内容
		if(!$('#produce_ajaxForm #addnr').val()){
			return false;
		}
		if(t_map.rows.length>0){
			$.alert("只能选择一个物料！");
			$('#produce_ajaxForm #addnr').val("");
			return false;
		}
		setTimeout(function(){ if(wlid != null && wlid != ''){
			$.ajax({
				type:'post',
				url:$('#produce_ajaxForm #urlPrefix').val()+"/progress/progress/pagedataQueryWlxx",
				cache: false,
				data: {"wlid":wlid,"access_token":$("#ac_tk").val()},
				dataType:'json',
				success:function(data){
					//返回值
					if(data.dtoList!=null && data.dtoList.length>0){
						data.dtoList[0].scsl = jhcls;
						if(t_map.rows==null || t_map.rows ==""){
							t_map.rows=[];
						}
						t_map.rows.push(data.dtoList[0]);
						$("#produce_ajaxForm #produce_tb_list").bootstrapTable('load',t_map);
						$('#produce_ajaxForm #addwlid').val("");
						$('#produce_ajaxForm #addnr').val("");

					}else{
						$.confirm("该物料不存在!");
					}
				}
			});
		}else{
			var addnr = $('#produce_ajaxForm #addnr').val();
			if(addnr != null && addnr != ''){
				$.confirm("请选择物料信息!");
			}
		}}, '200')
	}
})


/**
 * 点击确定添加物料
 * @param
 * @param
 * @returns
 */
function punchCofirmaddwl(){
	var wlid=$('#produce_ajaxForm #addwlid').val();
	//判断新增输入框是否有内容
	if(!$('#produce_ajaxForm #addnr').val()){
		return false;
	}
	setTimeout(function(){ if(wlid != null && wlid != ''){
		$.ajax({
			type:'post',
			url:$('#produce_ajaxForm #urlPrefix').val()+"/progress/progress/pagedataQueryWlxx",
			cache: false,
			data: {"wlid":wlid,"access_token":$("#ac_tk").val()},
			dataType:'json',
			success:function(data){
				//返回值
				if(data.dtoList!=null && data.dtoList.length>0){
					if(t_map.rows==null || t_map.rows ==""){
						t_map.rows=[];
					}
					data.dtoList[0].scsl = jhcls;
					t_map.rows.push(data.dtoList[0]);
					$("#produce_ajaxForm #produce_tb_list").bootstrapTable('load',t_map);
					$('#produce_ajaxForm #addwlid').val("");
					$('#produce_ajaxForm #addnr').val("");
				}else{
					$.confirm("该物料不存在!");
				}
			}
		});
	}else{
		var addnr = $('#produce_ajaxForm #addnr').val();
		if(addnr != null && addnr != ''){
			$.confirm("请选择物料信息!");
		}
	}}, '200')
}

function clickAddWl(){
	let jhcl= $('#produce_ajaxForm #jhcl').val();
	if (!jhcl){
		$.alert("请先填写计划产量！")
		return false;
	}
	if(t_map.rows.length>0){
		$.alert("只能选择一个物料！")
		return false;
	}
}

/**
 * 点击删除文件
 * @param fjid
 * @param wjlj
 * @returns
 */
function del(fjid,wjlj){
	$.confirm('您确定要删除所选择的记录吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url= $("#produce_ajaxForm #urlPrefix").val()+"/common/file/delFile";
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

/**
 * 点击文件下载
 * @param fjid
 * @returns
 */
function xz(fjid){
	jQuery('<form action="'+$('#produce_ajaxForm #urlPrefix').val() + '/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
		'<input type="text" name="fjid" value="'+fjid+'"/>' +
		'<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
		'</form>')
		.appendTo('body').submit().remove();
}