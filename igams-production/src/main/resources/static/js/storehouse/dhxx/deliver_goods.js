var t_map = [];
//显示字段
var columnsArray = [
	{
    	title: '序',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序',
		width: '2%',
        align: 'left',
        visible:true
    }, {
        field: 'hwid',
        title: '货物ID',
        titleTooltip:'货物ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'fhmxid',
        title: '发货明细ID',
        titleTooltip:'发货明细ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'wlid',
        title: '物料ID',
        titleTooltip:'物料ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'fhdh',
        title: '发货单号',
        titleTooltip:'发货单号',
        width: '13%',
        align: 'left',
        visible: true,
    }, {
        field: 'wlbm',
        title: '物料编码/质量类别',
        titleTooltip:'物料编码/质量类别',
        width: '10%',
        align: 'left',
        formatter:deliverGoodsForm_wlbmformat,
        visible: true
    }, {
		field: 'zllbmc',
		title: '质量类别',
		titleTooltip:'质量类别',
		width: '6%',
		align: 'left',
		visible: false
	}, {
        field: 'wlmc',
        title: '物料名称/规格',
        titleTooltip:'物料名称/规格',
        formatter:deliverGoodsForm_wlmcggformat,
        width: '10%',
        align: 'left',
        visible: true
    },{
        field: 'jldw',
        title: '单位/货号',
        titleTooltip:'单位/货号',
        formatter:deliverGoodsForm_dwhhformat,
        width: '8%',
        align: 'left',
        visible: true
    }, {
        field: 'kthsl',
        title: '可退货数量',
        titleTooltip:'可退货数量',
        width: '8%',
        align: 'left',
        visible: true,
    },{
        field: 'thsl',
        title: '退货数量',
        titleTooltip:'退货数量',
        width: '7%',
        align: 'left',
        formatter:deliverGoodsForm_thslformat,
        visible: true
    }, {
        field: 'ck',
        title: '仓库',
        titleTooltip:'仓库',
        width: '8%',
        align: 'left',
        formatter:deliverGoodsForm_ckformat,
        visible: true,
    },{
        field: 'kw',
        title: '库位',
        titleTooltip:'库位',
        width: '8%',
        align: 'left',
        formatter:deliverGoodsForm_kwformat,
        visible: true,
    },{
        field: 'scph',
        title: '生产批号',
        titleTooltip:'生产批号',
        width: '8%',
        align: 'left',
        visible: true,
    }, {
        field: 'scrq',
        title: '生产日期',
        titleTooltip:'生产日期',
        width: '8%',
        align: 'left',
        visible: true
    }, {
        field: 'yxq',
        title: '失效日期',
        titleTooltip:'生产日期',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'bz',
        title: '备注',
        titleTooltip:'备注',
        width: '8%',
        align: 'left',
        formatter:dhbzformat,
        visible: true
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '10%',
        align: 'left',
        formatter:czformat,
        visible: true
    }];

var arrivalGoodsEdit_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#deliverGoodsForm #tb_list').bootstrapTable({
            url: $('#deliverGoodsForm #urlPrefix').val()+'/storehouse/arrivalGoods/pagedataArrivalgoodsdetailList',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#deliverGoodsForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "fhdh",				//排序字段
            sortOrder: "desc",                   //排序方式
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
            uniqueId: "fhmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
            	t_map = map;
            	if(t_map.rows){
            		// 初始化dhmx_json
            		var json = [];
            		for (var i = 0; i < t_map.rows.length; i++) {
            			var sz = {"fhid":'',"fhmxid":''};
						sz.fhid = t_map.rows[i].fhid;
						sz.fhmxid = t_map.rows[i].fhmxid;
						json.push(sz);
    				}
            		$("#deliverGoodsForm #dhmx_json").val(JSON.stringify(json));
            		$("#deliverGoodsForm #dhmxJson").val(JSON.stringify(t_map.rows));
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
            sortLastName: "wlbm", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            //搜索框使用
            //search:params.search
        };
    	return map;
    };
    return oTableInit;
};

function deliverGoodsForm_dwhhformat(value,row,index){
	var jldw=row.jldw;
	var ychh=row.ychh;
	if(jldw==null)
		jldw="";
	if(ychh==null)
		ychh="";
	html="<span class='col-md-12 col-sm-12 ' style='padding:0px;' title='"+jldw+"'>"+jldw+"</span>"+
	"<span for='' class='col-md-12 col-sm-12 ' style='padding:0px;' title='"+ychh+"'>"+ychh+"</span>";
	return html;
}

function deliverGoodsForm_wlmcggformat(value,row,index){
	var wlmc=row.wlmc;
	var gg=row.gg;
	html="<span class='col-md-12 col-sm-12 ' style='padding:0px;' title='"+wlmc+"'>"+wlmc+"</span>"+
	"<span for='' class='col-md-12 col-sm-12 ' style='padding:0px;' title='"+gg+"'>"+gg+"</span>";
	return html;
}


/**
 * 仓库格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function deliverGoodsForm_ckformat(value,row,index){
	var cks = $("#deliverGoodsForm #cklist").val();
	var cklist = JSON.parse(cks);
	var ck=row.ck;
	var html="";
	html +="<div id='ckdiv_"+index+"' name='ckdiv' style='width:100%;display:inline-block'>";
	html += "<select id='ck_"+index+"' name='ck_"+index+"' class='form-control chosen-select' validate='{required:true}' onchange=\"checkCkKw('"+index+"',this,\'ck\')\">";
	html += "<option value=''>--请选择--</option>";
	var ckdm = ""
	for(var i = 0; i < cklist.length; i++) {
		html += "<option id='"+cklist[i].ckid+"' ckdm='"+cklist[i].ckdm+"' value='"+cklist[i].ckid+"'";
		if(ck!=null && ck!=""){
			if(ck==cklist[i].ckid){
				html += "selected"
				ckdm = cklist[i].ckdm
			}
		}		
		html += ">"+cklist[i].ckmc+"</option>";
	}
	html +="</select></div>";
	t_map.rows[index].ckdm = ckdm
	return html;
}

/**
 * 库位格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function deliverGoodsForm_kwformat(value,row,index){
	var kws = $("#deliverGoodsForm #kwlist").val();
	var kwlist = JSON.parse(kws);
	var kw=row.kw;
	// var ck=row.ck;
	var html="";
	html +="<div id='kwdiv_"+index+"' name='kwdiv' style='width:100%;display:inline-block'>";
	html += "<select id='kw_"+index+"' name='kw_"+index+"' class='form-control chosen-select' validate='{required:true}' onchange=\"checkCkKw('"+index+"',this,\'kw\')\">";
	html += "<option value=''>--请选择--</option>";
	for(var i = 0; i < kwlist.length; i++) {
		// if (ck!=null && ck!=""){
		// 	if (kwlist[i].fckid == ck) {
				html += "<option id='" + kwlist[i].ckid + "' value='" + kwlist[i].ckid + "'";
				if (kw != null && kw != "") {
					if (kw == kwlist[i].ckid) {
						html += "selected"
					}
				}
				html += ">" + kwlist[i].ckmc + "</option>";
		// 	}
		// }
	}
	html +="</select></div>";
	return html;
}


function checkCkKw(index, e, zdmc){
	if(zdmc=="ck"){
		let ckdm = $("#deliverGoodsForm #ck_"+index).find("option:selected").attr("ckdm");
		t_map.rows[index].ckdm = ckdm
		t_map.rows[index].ck = e.value;
		var ck = $("#deliverGoodsForm #ck_"+index).val();
		if(ck == null || ck==""){
			var zlbHtml = "";
			zlbHtml += "<option value=''>--请选择--</option>";
			$("#deliverGoodsForm #kw_"+index).empty();
	    	$("#deliverGoodsForm #kw_"+index).append(zlbHtml);
			$("#deliverGoodsForm #kw_"+index).trigger("chosen:updated");
			return;
		}
		var url="/warehouse/warehouse/pagedataQueryDtoList";
		url = $('#deliverGoodsForm #urlPrefix').val()+url;
		$.ajax({ 
		    type:'post',  
		    url:url, 
		    cache: false,
		    data: {"fckid":ck,"access_token":$("#ac_tk").val()},  
		    dataType:'json', 
		    success:function(data){
		    	if(data != null && data.length != 0){
		    		var zlbHtml = "";
		    		zlbHtml += "<option value=''>--请选择--</option>";
	            	$.each(data,function(i){   		
	            		zlbHtml += "<option value='" + data[i].ckid + "' id='"+data[i].ckid+"' ckmc='"+data[i].ckmc+"' >" + data[i].ckmc + "</option>";
	            	});
	            	$("#deliverGoodsForm #kw_"+index).empty();
	            	$("#deliverGoodsForm #kw_"+index).append(zlbHtml);
	            	$("#deliverGoodsForm #kw_"+index).trigger("chosen:updated");
		    	}else{
		    		var zlbHtml = "";
		    		zlbHtml += "<option value=''>--请选择--</option>";
		    		$("#deliverGoodsForm #kw_"+index).empty();
	            	$("#deliverGoodsForm #kw_"+index).append(zlbHtml);
	            	$("#deliverGoodsForm #kw_"+index).trigger("chosen:updated");
		    	}
		    }
		});
	}
	if(zdmc=="kw"){
		t_map.rows[index].kw = e.value;
	}
}

/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
	var html="";
	html += "<span style='margin-left:5px;height:24px !important;' class='btn btn-default' title='删除明细' onclick=\"delContractDetail('" + index + "',event)\" >删除</span></div>";
	return html;
}

/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delHwDetail(index,event){
	var preJson = JSON.parse($("#deliverGoodsForm #hwxx_json").val());	
	preJson.splice(index,1);
	t_map.rows.splice(index,1);
	$("#deliverGoodsForm #tb_list").bootstrapTable('load',t_map);
	$("#deliverGoodsForm #hwxx_json").val(JSON.stringify(preJson));
}
/**
 * 单据号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function qgdhformat(value,row,index){
	var html = "";
	if(row.qgdh==null){
		html = "";
	}else{
		html += "<a href='javascript:void(0);' onclick=\"queryByqgid('"+row.qgid+"')\">"+row.qgdh+"</a>";

	}
	return html;
}

/**
 * 数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
//取样量格式化
function deliverGoodsForm_thslformat(value,row,index){
	var thsl = "<input id='thsl"+index+"' type='number' autocomplete='off' name='thsl"+index+"' min='0' onblur=\"checkThslSum('"+index+"',this)\" max='"+row.kthsl+"' style='width: 100%;' value='"+(row.thsl==null?"0":row.thsl)+"'/>";
	return thsl;
}

function checkThslSum(index, e) {
	t_map.rows[index].thsl = e.value;
}
/**
 * 删除操作(从合同明细删除)
 * @param index
 * @param event
 * @returns
 */
function delContractDetail(index,event){
	var preJson = JSON.parse($("#deliverGoodsForm #thmx_json").val());
	let fhdh = $("#deliverGoodsForm #fhdh").val();
	let fhid = preJson[0].fhid;
	preJson.splice(index,1);
	t_map.rows.splice(index,1);
	$("#deliverGoodsForm #tb_list").bootstrapTable('load',t_map);
	$("#deliverGoodsForm #thmx_json").val(JSON.stringify(preJson));
	$("#deliverGoodsForm #thmxJson").val(JSON.stringify(t_map.rows));

	if (preJson.length <= 0){
		$("#deliverGoodsForm #fhdhs").tagsinput('remove', {"fhid":fhid,"fhdh":fhdh});
	}
}

/**
 * 备注格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function dhbzformat(value,row,index){
	if(row.dhbz == null){
		row.dhbz = "" ;
	}
	var html="<input id='dhbz_"+index+"' autocomplete='off' name='dhbz_"+index+"' value='"+row.dhbz+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{stringMaxLength:1024}' onblur=\"checkDhbzSum('"+index+"',this)\"></input>";
	return html;
}

function checkDhbzSum(index, e) {
	t_map.rows[index].dhbz = e.value;
}


/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */

function deliverGoodsForm_wlbmformat(value,row,index){
	var html = "";
	if($('#ac_tk').length > 0 && row.zllbmc != null && row.zllbmc != "null"){
		html += "<a href='javascript:void(0);' onclick=\"queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>"+
			"<span for='' class='col-md-12 col-sm-12 ' style='padding:0px;' title='"+row.zllbmc+"'>/"+row.zllbmc+"</span>";
	}else if ($('#ac_tk').length > 0 && (row.zllbmc == null || row.zllbmc == "null")){
		html += "<a href='javascript:void(0);' onclick=\"queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>"+
			"<span for='' class='col-md-12 col-sm-12 ' style='padding:0px;' title='"+row.zllbmc+"'>-</span>";
	}else {
		html += row.wlbm+
			"<span for='' class='col-md-12 col-sm-12 ' style='padding:0px;' title='"+row.zllbmc+"'>/"+row.zllbmc+"</span>";
	}

	return html;
}
function queryByWlbm(wlid){
	var url=$("#deliverGoodsForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'物料信息',viewWlConfig);	
}
var viewWlConfig = {
		width		: "800px",
		height		: "500px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("dhslNumber", function (value, element){
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


/**
 * 选择合同单号(及明细)
 * @returns
 */
function chooseFhgl(){
	var url = $('#deliverGoodsForm #urlPrefix').val();
	url = url + "/storehouse/storehouse/pagedataChooseListDeliverGoods?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择发货单号', choosefhglConfig);
}
var choosefhglConfig = {
		width : "1000px",
		modalName	: "chooseFhglModal",
		formName	: "chooseFhglForm",
		offAtOnce : true, // 当数据提交成功，立刻关闭窗口
		buttons : {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#chooseFhglForm").valid()){
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					// 获取未更改发货单和明细信息
					var preJson = [];
					if($("#deliverGoodsForm #thmx_json").val()){
						preJson = JSON.parse($("#deliverGoodsForm #thmx_json").val());
						if (preJson.length >0){
							$.alert("只能引用一条发货单！");
							return false;
						}
					}
					// 获取更改后的发货单和明细信息
					var fhmxJson = [];
					if($("#chooseFhglForm #fhmx_json").val()){
						fhmxJson = JSON.parse($("#chooseFhglForm #fhmx_json").val());

						// 判断删除的发货明细信息
						for (var j = preJson.length-1; j >= 0; j--) {
							var isDel = true;
							var isHave = false;
							for (var i = 0; i < fhmxJson.length; i++) {
								if(preJson[j].fhid == fhmxJson[i].fhid){
									isHave = true;
								}
								if(preJson[j].fhmxid == fhmxJson[i].fhmxid){
									isDel = false;
									break;
								}
													
							}
							if(isDel && isHave){
								// 根据明细ID直接更新页面列表(删除)
								for (var i = 0; i < t_map.rows.length; i++) {
									if(t_map.rows[i].fhmxid == preJson[j].fhmxid){
										preJson.splice(j,1);
										t_map.rows.splice(i,1);
										break;
									}
								}
							}
						}
						// 判断新增的合同明细信息
						var ids="";
						for (var i = 0; i < fhmxJson.length; i++) {
							var isAdd = true;
							for (var j = 0; j < preJson.length; j++) {
								if(preJson[j].fhmxid == fhmxJson[i].fhmxid){
									isAdd = false;
									break;
								}
							}
							if(isAdd){
								ids = ids + ","+ fhmxJson[i].fhmxid;
							}
						}
						if(ids.length > 0){
							ids = ids.substr(1);
							// 查询信息并更新到页面
							$.post($('#deliverGoodsForm #urlPrefix').val() + "/storehouse/storehouse/pagedataGetdeliverInfo",{"mxids":ids, "access_token":$("#ac_tk").val()},function(data){
								var fhmxDtos = data.fhmxList;
								if(fhmxDtos != null && fhmxDtos.length > 0){
									// 更新页面列表(新增)
									for (var i = 0; i < fhmxDtos.length; i++) {
										if(i == 0){
											if(!t_map.rows){
												t_map.rows= [];
											}
											$("#deliverGoodsForm #khjc").val(fhmxDtos[0].khjc);
											$("#deliverGoodsForm #kh").val(fhmxDtos[0].kh);
											$("#deliverGoodsForm #khdm").val(fhmxDtos[0].khdm);
											$("#deliverGoodsForm #suil").val(fhmxDtos[0].zsuil);
											$("#deliverGoodsForm #hl").val(fhmxDtos[0].hl);
											$("#deliverGoodsForm #biz").val(fhmxDtos[0].biz);
										}
										if (fhmxDtos[i].kthsl >0){
											t_map.rows.push(fhmxDtos[i]);
										}
										var sz = {"fhid":'',"fhmxid":'',"kh":''};
										sz.fhid = fhmxDtos[i].fhid;
										sz.fhmxid = fhmxDtos[i].fhmxid;
										sz.kh = fhmxDtos[i].kh;
										preJson.push(sz);
									}
								}
								if (t_map.rows.length <= 0){
									$.alert("可退回数量为0！");
									return false;
								}
								$("#deliverGoodsForm #tb_list").bootstrapTable('load',t_map);
								$("#deliverGoodsForm #thmx_json").val(JSON.stringify(preJson));
								// 发货单号
								var selDjh = $("#chooseFhglForm #yxdjh").tagsinput('items');
								$("#deliverGoodsForm #fhdhs").tagsinput({
					    			itemValue: "fhid",
					    			itemText: "fhdh",
					    		})
								for(var i = 0; i < selDjh.length; i++){
									var value = selDjh[i].value;
									var text = selDjh[i].text;
									$("#deliverGoodsForm #fhdhs").tagsinput('add', {"fhid":value,"fhdh":text});
								}
								$("#deliverGoodsForm #thmxJson").val(JSON.stringify(t_map.rows));
								$.closeModal(opts.modalName);
		    				},'json');
						}else{
							$("#deliverGoodsForm #tb_list").bootstrapTable('load',t_map);
							$("#deliverGoodsForm #thmx_json").val(JSON.stringify(preJson));
							// 请购单号
							var selDjh = $("#chooseFhglForm #yxdjh").tagsinput('items');
							$("#deliverGoodsForm #fhdhs").tagsinput({
				    			itemValue: "fhid",
				    			itemText: "fhdh",
				    		})
							for(var i = 0; i < selDjh.length; i++){
								var value = selDjh[i].value;
								var text = selDjh[i].text;
								$("#deliverGoodsForm #fhdhs").tagsinput('add', {"fhid":value,"fhdh":text});
							}
							$("#deliverGoodsForm #thmxJson").val(JSON.stringify(t_map.rows));
							preventResubmitForm(".modal-footer > button", true);
							$.closeModal(opts.modalName);
							preventResubmitForm(".modal-footer > button", false);
						}
					}else{
						$.alert("未获取到选中的发货信息！");
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
 * 监听单据号标签移除事件
 */
var tagsRemoved = $('#deliverGoodsForm #fhdhs').on('beforeItemRemove', function(event) {
	// $.confirm('您确定要删除发货单下的所有明细记录吗？',function(result){
	// 	if(result){
			// 获取未更改请购单和明细信息
			var preJson = [];
			if($("#deliverGoodsForm #thmx_json").val()){
				preJson = JSON.parse($("#deliverGoodsForm #thmx_json").val());
				// 删除明细并刷新列表
				var fhid = event.item.fhid;
				for (var i = t_map.rows.length-1; i >= 0 ; i--) {
					if(t_map.rows[i].fhid == fhid){
						t_map.rows.splice(i,1);
						preJson.splice(i,1);
					}
				}
				$("#deliverGoodsForm #thmx_json").val(JSON.stringify(preJson));
				$("#deliverGoodsForm #tb_list").bootstrapTable('load',t_map);
				$("#deliverGoodsForm #thmxJson").val(JSON.stringify(t_map.rows));
			}
	// 	}
	// })
});

/**
 * 刷新退货单号
 * @returns
 */
function refreshFhdh(){
	$.ajax({
		type:'post',
		url:$('#deliverGoodsForm #urlPrefix').val() + "/ship/ship/pagedataRefreshNumber",
		cache: false,
		data: {"access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(result){
			$("#deliverGoodsForm #fhdh").val(result.fhdh);
		}
	});
}

/**
 * 刷新退货单号
 * @returns
 */
function choseXslx(){
	let xslxdm = $("#deliverGoodsForm #xslx").find("option:selected").attr("csdm");
	$("#deliverGoodsForm #xslxdm").val(xslxdm);
}

/**
 * 选择销售部门列表
 * @returns
 */
function chooseXsbm(){
	var url=$('#deliverGoodsForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择申请部门',addXsbmConfig);	
}
var addXsbmConfig = {
	width		: "800px",
	modalName	:"addXsbmModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#deliverGoodsForm #xsbm').val(sel_row[0].jgid);
					$('#deliverGoodsForm #xsbmmc').val(sel_row[0].jgmc);
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
 * 选择经手人
 * @returns
 */
function chooseJsr() {
	var url = $('#deliverGoodsForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择申请人', chooseJsrConfig);
}
var chooseJsrConfig = { 
	width : "800px",
	height : "500px",
	modalName	: "chooseJsrModal",
	formName	: "deliverGoodsForm",
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
					$('#deliverGoodsForm #jsr').val(sel_row[0].yhid);
					$('#deliverGoodsForm #jsrmc').val(sel_row[0].zsxm);
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
	//添加日期控件
	laydate.render({
	   elem: '#deliverGoodsForm #djrq'
	  ,theme: '#2381E9'
	});
	//初始化列表
	var oTable=new arrivalGoodsEdit_TableInit();
	oTable.Init();
	//所有下拉框添加choose样式
	jQuery('#deliverGoodsForm .chosen-select').chosen({width: '100%'});
});