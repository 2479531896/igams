/**
 * 选择订单号
 * @returns
 */
function fhChooseDdh(){
	var url = $('#shipAjaxForm #urlPrefix').val()+"/ship/ship/pagedataDdhPage?access_token=" + $("#ac_tk").val()+ "&fhid=" + $("#shipAjaxForm #fhid").val();
	$.showDialog(url, '选择订单号', fhChooseDdhConfig);
}
var khjcmc  ="";
var khdm  ="";
var xslx = "";
var xslxmc = "";
var xsbm = "";
var xsbmmc = "";
var shdz = "";
var fhChooseDdhConfig = {
	width : "1300px",
	modalName	: "chooseDdhModal",
	formName	: "chooseDdhForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#chooseDdhForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				// 获取未更改请购单和明细信息
				var preJson = [];
				if($("#shipAjaxForm #xsmx_json").val()){
					preJson = JSON.parse($("#shipAjaxForm #xsmx_json").val());
				}
				if (preJson.length >0){
					$.alert("只能选择一条数据！");
					return ;
				}
				// 获取更改后的请购单和明细信息
				var xsmxJson = [];
				if($("#chooseDdhForm #xsmx_json").val()){
					xsmxJson = JSON.parse($("#chooseDdhForm #xsmx_json").val());
					khjcmc = xsmxJson[0].khjcmc
					khjc = xsmxJson[0].khjc
					khdm = xsmxJson[0].khdm
					xslx =xsmxJson[0].xslx;
					xslxmc =xsmxJson[0].xslxmc;
					xsbm = xsmxJson[0].xsbm;
					xsbmmc = xsmxJson[0].xsbmmc;
					shdz=  xsmxJson[0].shdz;
					// 判断新增的请购明细信息
					var ids="";
					for (var i = 0; i < xsmxJson.length; i++) {
						// var isAdd = true;
						// for (var j = 0; j < preJson.length; j++) {
						// 	if(preJson[j].xsmxid == xsmxJson[i].xsmxid){
						// 		isAdd = false;
						// 		break;
						// 	}
						// 	// if(preJson[j].khjcmc != xsmxJson[i].khjcmc){
						// 	// 	$.alert("客户简称不同！");
						// 	// 	return;
						// 	// }
						// }

						ids = ids + ","+ xsmxJson[i].xsmxid;

					}
					if(ids.length > 0){
						ids = ids.substr(1);
						// 查询信息并更新到页面
						$.post($('#shipAjaxForm #urlPrefix').val() + "/ship/ship/pagedataGetXsmxByIds",{"xsmxids":ids,"access_token":$("#ac_tk").val(),"fhid":$('#shipAjaxForm #fhid').val()},function(data){
							var xsmxDtos = data.xsmxDtos;
							if(xsmxDtos != null && xsmxDtos.length > 0){
								// // 更新页面列表(新增)
								// $('#shipAjaxForm #hl').val(soDetailsDtos[0].iExchRate);
								// $('#shipAjaxForm #suil').val(xsmxDtos[0].suil);
								// $('#shipAjaxForm #bz').val(xsmxDtos[0].bz);
								// $('#shipAjaxForm #biz').val(soDetailsDtos[0].cexch_name);
								for (var i = 0; i < xsmxDtos.length; i++) {
									if(i == 0){
										if(!t_map.rows){
											t_map.rows= [];
										}
									}
									let set = true
									if(preJson != null && preJson.length > 0){
										for (var j = 0; j < preJson.length; j++) {
											if (xsmxDtos[i].xsmxid == preJson[j].xsmxid){
												set = false;
											}
										}
									}
									if (set && xsmxDtos[i].kysl > 0){
										t_map.rows.push(xsmxDtos[i]);
										var sz = {"xsid":'',"xsmxid":'',"wlid":'',"wlbm":'',"kysl":''};
										sz.xsid = xsmxDtos[i].xsid;
										sz.xsmxid = xsmxDtos[i].xsmxid;
										sz.wlid = xsmxDtos[i].wlid;
										sz.wlbm = xsmxDtos[i].wlbm;
										sz.kysl = xsmxDtos[i].kysl;
										preJson.push(sz);
									}
								}
							}
							let temp = [];
							let pos =0;
							do {
								temp[pos++] = t_map.rows[t_map.rows.length-1];
								t_map.rows = delByTargetByCInvCode(t_map.rows,t_map.rows[t_map.rows.length-1])
							} while (t_map.rows.length != 0);
							t_map.rows = temp;
							$("#shipAjaxForm #shipments_tb_list").bootstrapTable('load',t_map);
							$("#shipAjaxForm #xsmx_json").val(JSON.stringify(preJson));
							$("#shipAjaxForm #khjcmc").val(khjcmc);
							$("#shipAjaxForm #khdm").val(khdm);
							$("#shipAjaxForm #khjc").val(khjc);
							$("#shipAjaxForm #xslx #"+xslx).prop("selected","selected");
							$("#shipAjaxForm #xslx #"+xslx).trigger("chosen:updated");
							$("#shipAjaxForm #xsbmmc").val(xsbmmc);
							$("#shipAjaxForm #xsbm").val(xsbm);
							$("#shipAjaxForm #shdz").val(shdz);
							// khjc = khjcmc;
							var selDjh = $("#chooseDdhForm #ddhs").tagsinput('items');
							$("#shipAjaxForm #ddh").tagsinput({
								itemValue: "xsid",
								itemText: "oaxsdh",
							})
							for(var i = 0; i < selDjh.length; i++){
								var value = selDjh[i].value;
								var text = selDjh[i].text;
								$("#shipAjaxForm #ddh").tagsinput('add', {"xsid":value,"oaxsdh":text});
							}
							$.closeModal(opts.modalName);
						},'json');
					}else{
						$("#shipAjaxForm #shipments_tb_list").bootstrapTable('load',t_map);
						$("#shipAjaxForm #xsmx_json").val(JSON.stringify(preJson));
						// 请购单号
						var selDjh = $("#chooseDdhForm #ddhs").tagsinput('items');
						$("#shipAjaxForm #ddh").tagsinput({
							itemValue: "xsid",
							itemText: "oaxsdh",
						})
						for(var i = 0; i < selDjh.length; i++){
							var value = selDjh[i].value;
							var text = selDjh[i].text;
							$("#shipAjaxForm #ddh").tagsinput('add', {"xsid":value,"oaxsdh":text});
						}
						$.closeModal(opts.modalName);
					}
				}else{
					$.alert("添加销售单失败！");
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
 * 根据具体元素删除
 */
function delByTargetByCInvCode(arr,target){
	let temp = [];
	let pos =0;
	for (let i = 0; i < arr.length; i++) {
		if (arr[i].xsmxid != target.xsmxid){
			temp[pos++] = arr[i]
		}
	}
	return temp;
}
/**
 * 选择业务员
 * @returns
 */
function chooseYwy() {
	var url = $('#shipAjaxForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择业务员', chooseQrrConfig);
}
var chooseQrrConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseSqrModal",
	formName	: "allocationForm",
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
					$('#shipAjaxForm #jsr').val(sel_row[0].yhid);
					$('#shipAjaxForm #jsrmc').val(sel_row[0].zsxm);
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
 * 刷新发货单号
 * @returns
 */
function refreshFhdh(){
	$.ajax({ 
	    type:'post',  
	    url:$('#shipAjaxForm #urlPrefix').val() + "/ship/ship/pagedataRefreshNumber", 
	    cache: false,
	    data: {"access_token":$("#ac_tk").val()},
	    dataType:'json',
	    success:function(result){
	    	$("#shipAjaxForm #fhdh").val(result.fhdh);
	    }
	});
}
// /**
//  * 选择客户
//  * @returns
//  */
// function chooseKh() {
// 	var url = $('#shipAjaxForm #urlPrefix').val()+"/ship/ship/pagedataListKh?access_token=" + $("#ac_tk").val();
// 	$.showDialog(url, '选择客户', chooseKhConfig);
// }
// var chooseKhConfig = {
// 	width : "1200px",
// 	height : "500px",
// 	modalName	: "chooseKhModal",
// 	formName	: "allocationForm",
// 	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
// 	buttons : {
// 		success : {
// 			label : "确 定",
// 			className : "btn-primary",
// 			callback : function() {
// 				if(!$("#taskListKhForm").valid()){
// 					return false;
// 				}
// 				var $this = this;
// 				var opts = $this["options"]||{};
// 				var sel_row = $('#taskListKhForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
// 				if(sel_row.length==1){
// 					$('#shipAjaxForm #khid').val(sel_row[0].khid);
// 					$('#shipAjaxForm #khjc').val(sel_row[0].khjc);
// 					$.closeModal(opts.modalName);
// 				}else{
// 					$.error("请选中一行");
// 					return;
// 				}
// 				return false;
// 			}
// 		},
// 		cancel : {
// 			label : "关 闭",
// 			className : "btn-default"
// 		}
// 	}
// };
/**
 * 选择销售部门
 * @returns
 */
function chooseXsbm(){
	var url=$('#shipAjaxForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择销售部门',addSqbmConfig);
}
var addSqbmConfig = {
	width		: "800px",
	modalName	:"addSqbmModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#shipAjaxForm #xsbm').val(sel_row[0].jgid);
					$('#shipAjaxForm #xsbmmc').val(sel_row[0].jgmc);
				}else{
					$.error("请选中一行");
					return false;
				}
			},
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};


var t_map = [];
var t_index="";
// 显示字段
var columnsArray = [
	{
		checkbox: true,
		width: '2%'
	},{
		title: '序号',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序号',
		width: '3%',
		align: 'left',
		visible:true
	},{
		field: 'xsmxid',
		title: '销售明细id',
		width: '6%',
		align: 'left',
		visible: false
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
		field: 'sl',
		title: '销售数量',
		width: '6%',
		align: 'left',
		visible: true,
	},{
		field: 'kysl',
		title: '可发货数量',
		width: '6%',
		align: 'left',
		visible: true,
		// formatter: shipAjaxForm_fhslformat
	}, {
		field: 'cz',
		title: '操作',
		width: '6%',
		align: 'left',
		formatter:shipAjaxForm_czformat,
		visible: true
	}];
var shipments_TableInit = function () {
	var oTableInit = new Object();
	//初始化Table
	oTableInit.Init = function () {
		$('#shipAjaxForm #shipments_tb_list').bootstrapTable({
			url: $('#shipAjaxForm #urlPrefix').val()+$('#shipAjaxForm #url').val(),         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#shipAjaxForm #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   //是否显示分页（*）
			sortable: true,                     //是否启用排序
			sortName: "xsmx.lrsj",					//排序字段
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
			uniqueId: "xsmx.xsmxid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: columnsArray,
			onLoadSuccess: function (map) {
				t_map = map;
				if ($('#shipAjaxForm #xsid').val() && $('#shipAjaxForm #xsdd').val()){
					if(t_map.rows != null && t_map.rows.length > 0){
						let fhmxDtos = JSON.parse($("#shipAjaxForm #fhmxDtos").val());
						if (fhmxDtos && fhmxDtos.length >0){
							var xsmx_json = [];
							for(var i=0;i<t_map.rows.length;i++){
								var json = [];
								let kfhsl = 0;
								for(var j=0;j<fhmxDtos.length;j++){
									if (t_map.rows[i].xsmxid == fhmxDtos[j].xsmxid){
										var sz = {"hwid":'',"cksl":'',"wlid":'',"ckid":'',"ckqxlx":''};
										sz.hwid=fhmxDtos[j].hwid;
										sz.cksl=fhmxDtos[j].fhsl;
										sz.ckqxlx = fhmxDtos[j].ckqxlx;
										sz.wlid = fhmxDtos[j].wlid;
										sz.ckid = fhmxDtos[j].ck;
										json.push(sz);
										kfhsl += fhmxDtos[j].fhsl*1;
									}
								}
								t_map.rows[i].kysl = (t_map.rows[i].kysl*1 + kfhsl*1).toFixed(2);
								t_map.rows[i].wlmx_json = JSON.stringify(json);
								json = [];
								var xs = {"xsid":'',"xsmxid":''};
								xs.xsid =t_map.rows[i].xsid;
								xs.xsmxid =t_map.rows[i].xsmxid;
								xsmx_json.push(xs)
							}
							$('#shipAjaxForm #xsmx_json').val(JSON.stringify(xsmx_json));
						}
					}
					$("#shipAjaxForm #shipments_tb_list").bootstrapTable('load',t_map);
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
			sortLastName: "xsmx.xsmxid", // 防止同名排位用
			sortLastOrder: "asc", // 防止同名排位用
			xsmxids: $('#shipAjaxForm #xsmxids').val()
		};
		return map;
	};
	return oTableInit;
};

function shipAjaxForm_fhslformat(value,row,index){
	var html="";
	html ="<div id='fhsldiv_"+index+"' name='fhsldiv' isBeyond='false' style='background:darkcyan;'>";
	html += "<input id='fhsl_"+index+"' type='number' autocomplete='off' value='"+(row.fhsl==null?"0":row.fhsl)+"' name='fhsl_"+index+"'  min='0'  max='"+row.sl+"' style='width:55%;border:1px solid #D5D5D5;'";
	html += "onblur=\"checkSum('"+index+"',this,\'fhsl\')\" ";
	html += "></input>";
	html += "<span id='sl_"+index+"' style='font-size:13px;width:45%;margin-left:3px;'>/"+row.sl+"</span>";
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
function checkSum(index, e, zdmc) {
	//当前物料可请领数
	if (zdmc == "fhsl"){
		t_map.rows[index].fhsl = e.value;
		if (t_map.rows[index].fhsl){
			if (t_map.rows[index].fhsl > t_map.rows[index].sl){
				$.alert("发货数量不能大于可用数量！");
				t_map.rows[index].fhsl = "0";
				$("#shipAjaxForm #fhsl_"+index).val("0");
			}
		}
	}
}
function choose_kfhslformat(value,row,index){
	var kfhsl = row.kfhsl;
	if (!kfhsl || kfhsl == 'undefined'){
		kfhsl = 0;
		t_map.rows[index].kfhsl = 0;
	}
	var html= "<input id='kfhsl_"+index+"' style='width: 70px' autocomplete='off'  value='"+kfhsl+"' name='kfhsl_"+index+"' min='0' max='"+row.klsl+"'  validate='{required:true,digits:true}' onblur=\"checkklslSum('"+index+"',this)\" ></input>";
	return html;
}

function checkklslSum(index, e) {
	t_map.rows[index].kfhsl = e.value;
	if (e.value*1 > t_map.rows[index].kcl*1){
		$.error("发货数量不能大于可用数量！");
		t_map.rows[index].kfhsl = 0;
		$("#shipAjaxForm #kfhsl_"+index).val(0)
		return;
	}
    let kfhzs = t_map.rows[index].kfhzs;
    let fhzs = 0;
	for (var i = 0; i < t_map.rows.length; i++) {
		if (t_map.rows[i].wlbm ==  t_map.rows[index].wlbm){
			fhzs += t_map.rows[i].kfhsl*1;
		}
	}
	if (kfhzs*1 < fhzs*1){
		$.error("发货数量不能大于发货总数！");
		t_map.rows[index].kfhsl = 0;
		$("#shipAjaxForm #kfhsl_"+index).val(0)
		return;
	}
}


function choose_bzformat(value,row,index){
	var bz = row.bz;
	if (bz == 'null' || !bz){
		bz = '';
	}
	var html= "<input id='bz_"+index+"' autocomplete='off' value='"+bz+"' name='bz_"+index+"' onblur=\"checkbzSum('"+index+"',this)\" ></input>";
	return html;
}

function checkbzSum(index, e) {
	t_map.rows[index].bz = e.value;
}

/**
 * 选择客户列表
 * @returns
 */
function chooseKh(){
	var url=$('#shipAjaxForm #urlPrefix').val() + "/systemmain/client/pagedataListClient?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择客户',addKhConfig);
}
var addKhConfig = {
	width		: "800px",
	modalName	:"addKhModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#client_list_ajaxForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#shipAjaxForm #khjc').val(sel_row[0].khid);
					$('#shipAjaxForm #khjcmc').val(sel_row[0].khmc);
				}else{
					$.error("请选中一行");
					return false;
				}
			},
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
/**
 * 监听订单号标签移除事件
 */
var tagsRemoved = $('#shipAjaxForm #ddh').on('beforeItemRemove', function(event) {
	// 获取未更改请购单和明细信息
	var preJson = [];
	if($("#shipAjaxForm #xsmx_json").val()){
		preJson = JSON.parse($("#shipAjaxForm #xsmx_json").val());
		// 删除明细并刷新列表
		var xsid = event.item.xsid;
		let cInvCode = "";
		let pops = 0;
		for (let i = 0; i < preJson.length; i++) {
			if(preJson[i].xsid == xsid){
				preJson.splice(i,1);
				i--;
				for (let j = 0; j < preJson.length; j++) {
					if(preJson[j].xsid == xsid){
						pops += 1;
					}
				}
				if (pops == 0){
					for (var j = t_map.rows.length-1; j >= 0 ; j--) {
						if(t_map.rows[j].xsid == xsid){
							t_map.rows.splice(j,1);
						}
					}
				}
				pops = 0;
			}
		}

		$("#shipAjaxForm #xsmx_json").val(JSON.stringify(preJson));
		$("#shipAjaxForm #shipments_tb_list").bootstrapTable('load',t_map);
	}
});

/**
 * 监听单据号标签点击事件
 */
var khjc = "";
var tagClick = $("#shipAjaxForm").on('click','.label-info',function(e){
	event.stopPropagation();
	var url = $('#shipAjaxForm #urlPrefix').val() + "/ship/ship/pagedataChooseListXsmxInfo?access_token=" + $("#ac_tk").val() + "&xsid=" + e.currentTarget.dataset.logo+ "&fhid=" + $("#shipAjaxForm #fhid").val();
	$.showDialog(url, '选择明细信息', chooseEditXsmxConfig);
});
var chooseEditXsmxConfig = {
	width : "1000px",
	modalName	: "chooseEditXsmxModal",
	formName	: "chooseEditXsmxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#chooseXsmxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				if($("#chooseXsmxForm input[name='checkXsmx']:checked").length == 0){
					$.alert("未选中请购明细信息！");
					return false;
				}
				// 获取未更改请购单和明细信息
				var preJson = [];
				if($("#shipAjaxForm #xsmx_json").val()){
					preJson = JSON.parse($("#shipAjaxForm #xsmx_json").val());
				}
				// 获取更改请购单和明细信息
				var xsmxJson = [];
				$("#chooseXsmxForm input[name='checkXsmx']").each(function(){
					if($(this).is(":checked")){
						var sz={"xsmxid":''};
						sz.xsmxid = this.dataset.xsmxid;
						xsmxJson.push(sz);
					}
				})


				// 获取新增请购明细
				var ids="";
				for (var i = 0; i < xsmxJson.length; i++) {
					ids = ids + ","+ xsmxJson[i].xsmxid;
				}
				if(ids.length > 0){
					ids = ids.substr(1);
					// 查询信息并更新到页面
					$.post($('#shipAjaxForm #urlPrefix').val() + "/ship/ship/pagedataGetXsmxByIds",{"xsmxids":ids,"access_token":$("#ac_tk").val(),"fhid":$('#shipAjaxForm #fhid').val()},function(data){
						var xsmxDtos = data.xsmxDtos;
						if(xsmxDtos != null && xsmxDtos.length > 0){
							// // 更新页面列表(新增)
							// $('#shipAjaxForm #hl').val(soDetailsDtos[0].iExchRate);
							// $('#shipAjaxForm #suil').val(xsmxDtos[0].suil);
							// $('#shipAjaxForm #bz').val(xsmxDtos[0].bz);
							// $('#shipAjaxForm #biz').val(soDetailsDtos[0].cexch_name);
							for (var i = 0; i < xsmxDtos.length; i++) {
								if(i == 0){
									if(!t_map.rows){
										t_map.rows= [];
									}
								}
								let set = true
								if(preJson != null && preJson.length > 0){
									for (var j = 0; j < preJson.length; j++) {
										if (xsmxDtos[i].xsmxid == preJson[j].xsmxid){
											set = false;
										}
									}
								}
								if (set && xsmxDtos[i].kysl > 0){
									t_map.rows.push(xsmxDtos[i]);
									var sz = {"xsid":'',"xsmxid":'',"wlid":'',"wlbm":'',"kysl":''};
									sz.xsid = xsmxDtos[i].xsid;
									sz.xsmxid = xsmxDtos[i].xsmxid;
									sz.wlid = xsmxDtos[i].wlid;
									sz.wlbm = xsmxDtos[i].wlbm;
									sz.kysl = xsmxDtos[i].kysl;
									preJson.push(sz);
								}
							}
						}
						let temp = [];
						let pos =0;
						do {
							temp[pos++] = t_map.rows[t_map.rows.length-1];
							t_map.rows = delByTargetByCInvCode(t_map.rows,t_map.rows[t_map.rows.length-1])
						} while (t_map.rows.length != 0);
						t_map.rows = temp;
						$("#shipAjaxForm #shipments_tb_list").bootstrapTable('load',t_map);
						$("#shipAjaxForm #xsmx_json").val(JSON.stringify(preJson));
						$.closeModal(opts.modalName);
					},'json');
				}else{
					$("#shipAjaxForm #shipments_tb_list").bootstrapTable('load',t_map);
					$("#shipAjaxForm #xsmx_json").val(JSON.stringify(preJson));
					$.closeModal(opts.modalName);
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
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delWlXsInfoInShip(index,xsmxid,event){
	var preJson = JSON.parse($("#shipAjaxForm #xsmx_json").val());
	let rows = t_map.rows.length;
	let pops = 0;
	let temps = JSON.stringify(preJson);
	var temp = JSON.parse(temps);
	for (let i = 0; i < preJson.length; i++) {
		if (preJson[i].xsmxid == xsmxid){
			let id = preJson[i].xsid;
			preJson.splice(i,1);
			i--;
			for (let j = 0; j < preJson.length; j++) {
				if (preJson[j].xsid == id){
					pops += 1;
				}
			}
			if (pops <= 0){
				for (let k = 0; k < temp.length; k++) {
					if (temp[k].xsid == id){
						$("#shipAjaxForm #ddh").tagsinput('remove', temp[k]);
						break
					}
				}
			}
			pops = 0;
			temps = JSON.stringify(preJson);
			temp = JSON.parse(temps);
		}
	}
	if ( rows == t_map.rows.length){
		t_map.rows.splice(index,1);
	}
	$("#shipAjaxForm #shipments_tb_list").bootstrapTable('load',t_map);
	$("#shipAjaxForm #xsmx_json").val(JSON.stringify(preJson));
}




/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function shipAjaxForm_czformat(value,row,index){
	if ( t_map.length >0 && t_map.rows.length >0 && t_map.rows){
		for (let i = 0; i < t_map.rows.length; i++) {
			if (!t_map.rows[i].wlmx_json){
				t_map.rows[i].wlmx_json = [];
			}
		}
	}
	var html="";
	html = html+"<span style='margin-left:5px;height:24px !important;' class='btn btn-info' title='选择明细' onclick=\"chooseXsmx('" + index + "','" + row.wlbm + "',event)\" >详细</span>";
	html = html+"<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除物料信息' onclick=\"delWlXsInfoInShip('" + index + "','" + row.xsmxid + "',event)\" >删除</span>";
	return html;
}

/**
 * 选择调拨明细
 * @returns
 */
function chooseXsmx(index,wlbm,event){
	t_index=index;
	var url=$('#shipAjaxForm #urlPrefix').val() + "/ship/ship/pagedataChooseXsmx?access_token=" + $("#ac_tk").val()+"&wlbm="+wlbm+"&fhid="+$('#shipAjaxForm #fhid').val();
	$.showDialog(url,'选择物料明细',chooseXsmxConfig);
}
var chooseXsmxConfig = {
	width		: "1600px",
	modalName	:"chooseXsmxModal",
	formName	: "chooseXsmx_formSearch",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#chooseXsmx_formSearch").valid()){
					$.alert("请确认信息是否正确！");
					return false;
				}
				if(th_map.rows != null && th_map.rows.length > 0){
					var json = [];
					let ckslzs = 0;
					let zs = t_map.rows[t_index].kysl;
					for(var i=0;i<th_map.rows.length;i++){
						var sz = {"hwid":'',"cksl":'',"wlid":'',"ckid":'',"ckqxlx":''};
						sz.hwid=th_map.rows[i].hwid;
						sz.cksl=th_map.rows[i].cksl;
						sz.ckqxlx = th_map.rows[i].ckqxlx;
						sz.wlid = th_map.rows[i].wlid;
						sz.ckid = th_map.rows[i].ckid;
						json.push(sz);
						ckslzs += Number(th_map.rows[i].cksl);
					}
					if (ckslzs > zs){
						$.alert("发货数不能大于可发数量！");
						return false;
					}
					t_map.rows[t_index].wlmx_json = JSON.stringify(json);
				}
			},
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};


$(document).ready(function(){
	//添加日期控件
	laydate.render({
		elem: '#shipAjaxForm #djrq'
		,theme: '#2381E9'
	});
	//初始化列表
	var oTable=new shipments_TableInit();
	oTable.Init();
	if ($('#shipAjaxForm #xsid').val() && $('#shipAjaxForm #xsdd').val()){
		$("#shipAjaxForm #ddh").tagsinput({
			itemValue: "xsid",
			itemText: "oaxsdh",
		});
		$("#shipAjaxForm #ddh").tagsinput('add', {"xsid":$('#shipAjaxForm #xsid').val(),"oaxsdh":$('#shipAjaxForm #xsdd').val()});
	}
	jQuery('#shipAjaxForm .chosen-select').chosen({width: '100%'});
});