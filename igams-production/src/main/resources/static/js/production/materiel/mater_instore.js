var t_map = [];
var t_index="";
// 显示字段
var columnsArray = [
	{
    	title: '序',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序',
		width: '1%',
        align: 'left',
        visible:true
    }, {
        field: 'qgmxid',
        title: '请购明细id',
        titleTooltip:'请购明细id',
        width: '1%',
        align: 'left',
        visible: false
    }, {
        field: 'hwmc',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '5%',
        align: 'left',
        visible: true
    }, {
        field: 'hwbz',
        title: '规格',
        titleTooltip:'规格',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'hwjldw',
        title: '单位',
        titleTooltip:'单位',
        width: '3%',
        align: 'left',
        visible: true
    }, {
        field: 'sl',
        title: '数量',
        titleTooltip:'数量',
        width: '4%',
        align: 'left',
        formatter:inStoreForm_qlslformat,
        visible: true
	}, {
		field: 'ck',
		title: '仓库',
		titleTooltip:'仓库',
		width: '6%',
		align: 'left',
		formatter:inStoreForm_ckformat,
		visible: true
	}, {
		field: 'kw',
		title: '库位',
		titleTooltip:'库位',
		width: '6%',
		align: 'left',
		formatter:inStoreForm_kwformat,
		visible: true
    }, {
		field: 'hwbz',
		title: '备注',
		titleTooltip:'备注',
		width: '5%',
		align: 'left',
		formatter:inStoreForm_bzformat,
		visible: true
    }];
var pickingEdit_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#inStoreForm #hw_list').bootstrapTable({
            url: $('#inStoreForm #urlPrefix').val()+$('#inStoreForm #url').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#inStoreForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "hwmc",					//排序字段
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
            uniqueId: "qgmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
            	 
            	t_map = map;
				if(t_map.rows!=null){
					// 初始化llmx_json
					var json = [];
					for (var i = 0; i < t_map.rows.length; i++) {
						var sz = {"qgid":'',"qgmxid":''};
						sz.qgid = t_map.rows[i].qgid;
						sz.qgmxid = t_map.rows[i].qgmxid;
						json.push(sz);
					}
					$("#inStoreForm #qgmx_json").val(JSON.stringify(json));
				}
			},
            onLoadError: function () {
            	alert("数据加载失败！");
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
        	pageSize: 1,   //页面大小
        	pageNumber: 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "qgmxid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            qgids: $("#inStoreForm #ids").val(),
			xzrkid:$("#inStoreForm #xzrkid").val(),
            //搜索框使用
            //search:params.search
        };
    	return map;
    };
    return oTableInit;
};

/**
 * 监听单据号标签移除事件
 */
var tagsRemoved = $('#inStoreForm #djhs').on('beforeItemRemove', function(event) {
	// 获取未更改请购单和明细信息
	var preJson = [];
	if($("#inStoreForm #qgmx_json").val()){
		preJson = JSON.parse($("#inStoreForm #qgmx_json").val());
		// 删除明细并刷新列表
		var qgid = event.item.qgid;

		for (let i = 0; i < preJson.length; i++) {
			if(preJson[i].qgid == qgid){
				preJson.splice(i,1);
				i--;
			}
		}
		for (var j = t_map.rows.length-1; j >= 0 ; j--) {
			if(t_map.rows[j].qgid == qgid){
				t_map.rows.splice(j,1);
			}
		}

		$("#inStoreForm #qgmx_json").val(JSON.stringify(preJson));
		$("#inStoreForm #hw_list").bootstrapTable('load',t_map);
	}
});

/**
 * 选择请购单据号(及明细)
 * @returns
 */
function llglchooseQgInfo(){
	var url = $('#inStoreForm #urlPrefix').val() + "/purchase/purchase/pagedataChooseListPurchase?access_token=" + $("#ac_tk").val()+"&lllb=2";
		$.showDialog(url, '选择请购单号', llglchooseQgxxConfig);
}
var llglchooseQgxxConfig = {
	width : "1000px",
	modalName	: "chooseQgxxModal",
	formName	: "chooseQgxxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#chooseQgxxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				// 获取未更改请购单和明细信息
				var preJson = [];
				if($("#inStoreForm #qgmx_json").val()){
					preJson = JSON.parse($("#inStoreForm #qgmx_json").val());
				}
				// 获取更改后的请购单和明细信息
				var qgmxJson = [];
				if($("#chooseQgxxForm #qgmx_json").val()){
					qgmxJson = JSON.parse($("#chooseQgxxForm #qgmx_json").val());
					// 判断新增的请购明细信息
					var ids="";
					for (var i = 0; i < qgmxJson.length; i++) {
						var isAdd = true;
						for (var j = 0; j < preJson.length; j++) {
							if(preJson[j].qgmxid == qgmxJson[i].qgmxid){
								isAdd = false;
								break;
							}
						}
						if(isAdd){
							ids = ids + ","+ qgmxJson[i].qgmxid;
						}
					}
					if(ids.length > 0){
						ids = ids.substr(1);
						// 查询信息并更新到页面
						$.post($('#inStoreForm #urlPrefix').val() + "/purchase/purchase/getRKmxList",{"ids":ids,"access_token":$("#ac_tk").val()},function(data){
							if(!t_map.rows){
								t_map.rows= [];
							}
							// 请购单号
							var selDjh = $("#chooseQgxxForm #yxdjh").tagsinput('items');
							if (t_map.rows.length > 0 && t_map.rows != null){
								for(let i = t_map.rows.length-1; i >=0 ; i--){
									for(let j = selDjh.length-1; j >=0 ; j--){
										if (t_map.rows[i].qgid == selDjh[j].value){
											selDjh.splice(j,1);
											j = 0;
										}
									}
								}
							}
							var qgmxList = data.rows;
							if(qgmxList != null && qgmxList.length > 0){
								// 更新页面列表(新增)
								for (var i = 0; i < qgmxList.length; i++) {
									t_map.rows.push(qgmxList[i]);
									var sz = {"qgid":'',"qgmxid":''};
									sz.qgid = qgmxList[i].qgid;
									sz.qgmxid = qgmxList[i].qgmxid;
									preJson.push(sz);

								}
							}
							$("#inStoreForm #qgmx_json").val(JSON.stringify(preJson));
							$("#inStoreForm #hw_list").bootstrapTable('load',t_map);

							$("#inStoreForm #djhs").tagsinput({
								itemValue: "qgid",
								itemText: "djh",
							})
							for(var i = 0; i < selDjh.length; i++){
								var value = selDjh[i].value;
								var text = selDjh[i].text;
								$("#inStoreForm #djhs").tagsinput('add', {"qgid":value,"djh":text});
							}
							$.closeModal(opts.modalName);
						},'json');
					}else{
						$("#inStoreForm #qgmx_json").val(JSON.stringify(preJson));
						// 请购单号
						var selDjh = $("#chooseQgxxForm #yxdjh").tagsinput('items');
						$("#inStoreForm #djhs").tagsinput({
							itemValue: "qgid",
							itemText: "djh",
						})
						for(var i = 0; i < selDjh.length; i++){
							var value = selDjh[i].value;
							var text = selDjh[i].text;
							$("#inStoreForm #djhs").tagsinput('add', {"qgid":value,"djh":text});
						}
						$.closeModal(opts.modalName);
					}
				}else{
					$.alert("添加入库失败！");
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
 * 刷新入库单号
 */

function sxrkdh(){
	$.ajax({
		type:'post',
		url: $('#inStoreForm #urlPrefix').val()+'/purchase/purchase/sxrkdh',
		cache: false,
		data: {"access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			if(data != null && data.length != 0){
				if (data.rkdh){
					$("#inStoreForm #rkdh").val(data.rkdh);
				}
			}
		}
	});
}
/**
 * 仓库格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function inStoreForm_ckformat(value,row,index){
	 
	var ckxxlist = $("#inStoreForm #cklist").val();
	var cklist = JSON.parse(ckxxlist);
	var html="";
	let ckxx = "";
	if (t_map.rows){
		ckxx = t_map.rows[index].ck;
	}
	html +="<div id='ckxxdiv_"+index+"' name='ckxxdiv' style='width:85%;display:inline-block'>";
	html += "<select id='ckxx_"+index+"' name='ckxx_"+index+"' class='form-control chosen-select' validate='{required:true}' onchange=\"checkInfo('"+index+"',this,\'ck\')\">";
	html += "<option value=''>--请选择--</option>";
	for(var i = 0; i < cklist.length; i++) {
		html += "<option id='"+cklist[i].ckid+"' value='"+cklist[i].ckid+"'";
		if(ckxx!=null && ckxx!=""){
			if(ckxx==cklist[i].ckid){
				html += "selected"
			}
		}
		html += ">"+cklist[i].ckmc+"</option>";
	}
	html +="</select></div>";
// <div style='width:10%;float: right;margin-top: 3px;'><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;cursor:pointer;' title='复制当前日期并替换所有'  onclick=\"copyckxx('"+index+"')\" autocomplete='off'><span></div>
	return html;
}

/**
 * 库位格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function inStoreForm_kwformat(value,row,index){
	 
	var kwxxlist = $("#inStoreForm #kwlist").val();
	var kwlist = JSON.parse(kwxxlist);
	var html="";
	let kw = "";
	if (t_map.rows){
		kw = t_map.rows[index].kw;
	}
	html +="<div id='kwdiv_"+index+"' name='kwdiv' style='width:85%;display:inline-block'>";
	html += "<select id='kw_"+index+"' name='kw_"+index+"' class='form-control chosen-select' validate='{required:true}' onchange=\"checkInfo('"+index+"',this,\'kw\')\">";
	html += "<option value=''>--请选择--</option>";
	for(var i = 0; i < kwlist.length; i++) {
		html += "<option id='"+kwlist[i].ckid+"' value='"+kwlist[i].ckid+"'";
		if(kw!=null && kw!=""){
			if(kw==kwlist[i].ckid){
				html += "selected"
			}
		}
		html += ">"+kwlist[i].ckdm+"--"+kwlist[i].ckmc+"</option>";
	}
	html +="</select></div>";
// <div style='width:10%;float: right;margin-top: 3px;'><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;cursor:pointer;' title='复制当前日期并替换所有'  onclick=\"copyckxx('"+index+"')\" autocomplete='off'><span></div>
	return html;
}


function checkInfo(index, e, zdmc){
	if(zdmc=="ck"){
		t_map.rows[index].ck = e.value;
		var ckxx = $("#inStoreForm #ckxx_"+index).val();
		if(ckxx == null || ckxx==""){
			var zlbHtml = "";
			zlbHtml += "<option value=''>--请选择--</option>";
			$("#inStoreForm #kw_"+index).empty();
			$("#inStoreForm #kw_"+index).append(zlbHtml);
			$("#inStoreForm #kw_"+index).trigger("chosen:updated");
			return;
		}
		var url="storehouse/arrivalGoods/pagedataGetkwlist";
		url = $('#inStoreForm #urlPrefix').val()+url;
		$.ajax({
			type:'post',
			url:url,
			cache: false,
			data: {"ckid":ckxx,"access_token":$("#ac_tk").val()},
			dataType:'json',
			success:function(data){
				if(data != null && data.length != 0){
					 
					var zlbHtml = "";
					zlbHtml += "<option value=''>--请选择--</option>";
					$.each(data.kwlist,function(i){
						zlbHtml += "<option value='" + data.kwlist[i].ckid + "' id='"+data.kwlist[i].ckid+"' csdm='"+data.kwlist[i].ckdm+"' csmc='"+data.kwlist[i].ckmc+"'>" + data.kwlist[i].ckmc + "</option>";
					});
					$("#inStoreForm #kw_"+index).empty();
					$("#inStoreForm #kw_"+index).append(zlbHtml);
					$("#inStoreForm #kw_"+index).trigger("chosen:updated");
				}else{
					var zlbHtml = "";
					zlbHtml += "<option value=''>--请选择--</option>";
					$("#inStoreForm #kw_"+index).empty();
					$("#inStoreForm #kw_"+index).append(zlbHtml);
					$("#inStoreForm #kw_"+index).trigger("chosen:updated");
				}
			}
		});
	}
	if(zdmc=="kw"){
		t_map.rows[index].kw = e.value;
	}
	if(zdmc=="bz"){
		t_map.rows[index].bz = e.value;
	}
}

/**
 * 请领数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function inStoreForm_qlslformat(value,row,index){
	var sl=row.sl;
	if(!row.sl){
		sl=0.00;
	}
	sl = parseFloat(sl)
	sl = sl.toFixed(2)
	var html="";
	html ="<div id='sldiv_"+index+"' name='sldiv'>";
	if(sl!=null && sl!=""){
		html += "<input id='sl_"+index+"' value='"+sl+"' style='width: 100%' name='sl_"+index+"' validate='{slNumber:true}' ";
		html += "onblur=\"checkslSum('"+index+"',this,\'sl\')\" ";
		html += "></input>";
	}
	html += "</div>";
	return html;
}

/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkslSum(index, e, zdmc) {
	t_map.rows[index].sl = e.value;
}

/**
 * 备注格式化
 */
function inStoreForm_bzformat(value,row,index){
	var html="";
	if (!row.bz){
		row.bz="";
	}
	html ="<div id='bzdiv_"+index+"' name='bzdiv'>";
	html += "<input id='bz"+index+"' value='"+row.bz+"' name='bz_"+index+"' onblur=\"checkInfo('"+index+"',this,\'bz\')\" style='width: 98%'  validate='{stringMaxLength:64}'></input>";
	html += "</div>";
	return html;
}
/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("slNumber", function (value, element){
	if(!value){
		$.error("请填写数量!");
		return false;
	}else{
		if(!/^\d+(\.\d{1,2})?$/.test(value)){
			$.error("请填写正确数量格式，可保留两位小数!");
		}
	}
    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");




// /**
//  * 根据具体元素删除
//  */
// function delByTarget(arr,target){
// 	let temp = [];
// 	let pos =0;
// 	for (let i = 0; i < arr.length; i++) {
// 		if (arr[i].hwid != target.hwid){
// 			temp[pos++] = arr[i]
// 		}
// 	}
// 	return temp;
// }


/**
 * 初始化页面
 * @returns
 */
function init(){
  	//添加日期控件
	laydate.render({
	   elem: '#rkrq'
	  ,theme: '#2381E9'
	});
}

/**
 * 选择申请人
 * @returns
 */
function chooseSqr() {
	var url = $('#inStoreForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择申请人', chooseQrrConfig);
}
var chooseQrrConfig = { 
	width : "800px",
	height : "500px",
	modalName	: "chooseSqrModal",
	formName	: "inStoreForm",
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
					$('#inStoreForm #rkry').val(sel_row[0].yhid);
					$('#inStoreForm #rkrymc').val(sel_row[0].zsxm);
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


$(document).ready(function(){
	//初始化列表
	var oTable=new pickingEdit_TableInit();
	oTable.Init();
	// 初始化页面
	init();
	jQuery('#inStoreForm .chosen-select').chosen({width: '100%'});
});