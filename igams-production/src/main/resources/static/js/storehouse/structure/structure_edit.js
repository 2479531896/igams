var t_map = [];
var t_index="";
var gylxList = [];
var cclxList = [];
var ckxxDtos = [];

/**
 * 子件行号
 * @param value
 * @param row
 * @param index
 * @returns
 */
function structureEditForm_zjhhformat(value,row,index){
	if(row.zjhh == null){
		row.zjhh = "10";
	}
	if (index != 0){
		row.zjhh =t_map.rows[index-1].zjhh*1 + 10
	}

	var html = "<input id='zjhh_"+index+"' autocomplete='off' value='"+row.zjhh+"' name='zjhh_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{required:true,digits:true}' min='0000' max='9999' onchange=\"checkTableValue('"+index+"',this,\'zjhh\')\"></input>";
	return html;
}

/**
 * 工序行号
 * @param value
 * @param row
 * @param index
 * @returns
 */
function structureEditForm_gxhhformat(value,row,index){
	if(row.gxhh == null){
		row.gxhh = "0000";
	}
	// if (index != 0){
	// 	row.gxhh = t_map.rows[index-1].gxhh*1 + 10
	// }
	// row.gxhh = "0000"+row.gxhh
	// row.gxhh = row.gxhh.substring(row.gxhh.length-4,row.gxhh.length)
	var html = "<input id='gxhh_"+index+"' autocomplete='off' value='"+row.gxhh+"' name='gxhh_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{required:true,digits:true}' min='0000' max='9999' onchange=\"checkTableValue('"+index+"',this,\'gxhh\')\"></input>";
	return html;
}

/**
 *子件编码
 * @param value
 * @param row
 * @param index
 * @returns
 */
function structureEditForm_format(value,row,index){
	// if(row.wlbm == null){
	// 	row.gxhh = "-";
	// }
	// if(row.gg == null){
	// 	row.gg = "-";
	// }
	// if(row.wlmc == null){
	// 	row.wlmc = "-";
	// }
	//
	// if(row.jldw == null){
	// 	row.jldw = "-";
	// }
	// var html = "<div><span>"+row.wlmc+"</span><br><span>"+row.wlbm+"</span><br><span>"+row.gg+"</span><br><span>"+row.jldw+"</span></div>";
	if(row.wlbm == null){
		return "";
	}
	var html = "<a href='javascript:void(0);' onclick=\"queryByWlid('" + row.wlid + "',event)\">"+row.wlbm+"</a>"
	return html;
}
function queryByWlid(wlid){
	var url=$("#structureEditForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'物料信息',WlConfig);
}
var WlConfig = {
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
 *基本用量
 * @param value
 * @param row
 * @param index
 * @returns
 */
function structureEditForm_jbylformat(value,row,index){
	let jbyl = 0;
	if (row.jbyl == null){
		jbyl = 1
		t_map.rows[index].jbyl = jbyl;
	}else {
		jbyl = row.jbyl
	}
	var html="";
	html ="<div id='jbyldiv_"+index+"' name='jbyldiv' style='width: 100%'>";
	html += "<input id='jbyl_"+index+"' autocomplete='off' value='"+jbyl+"' validate = '{ylNumber:true}' name='jbyl_"+index+"' style='border: 1px solid #ccc;border-radius: 4px;width: 100%' ";
	html += "onblur=\"checkTableValue('"+index+"',this,\'jbyl\')\" ";
	html += "></input>";
	html += "</div>";
	return html;
}

/**
 *基础用量
 * @param value
 * @param row
 * @param index
 * @returns
 */
function structureEditForm_jcslformat(value,row,index){
	let jcsl = 0;
	if (row.jcsl == null){
		jcsl = 1
		t_map.rows[index].jcsl = jcsl;
	}else {
		jcsl = row.jcsl
	}
	var html="";
	html ="<div id='jcsldiv_"+index+"' name='jcsldiv' style='width: 100%'>";
	html += "<input id='jcsl_"+index+"' autocomplete='off' value='"+jcsl+"' validate = '{ylNumber:true}' name='jcsl_"+index+"' style='border: 1px solid #ccc;border-radius: 4px;width: 100%' ";
	html += "onblur=\"checkTableValue('"+index+"',this,\'jcsl\')\" ";
	html += "></input>";
	html += "</div>";
	return html;
}


/**
 *子件损耗率
 * @param value
 * @param row
 * @param index
 * @returns
 */
function structureEditForm_zjshlformat(value,row,index){
	let zjshl = 0;
	if (row.zjshl == null){
		zjshl = 0
		t_map.rows[index].zjshl = zjshl;
	}else {
		zjshl = row.zjshl
	}
	var html="";
	html ="<div id='zjshldiv_"+index+"' name='zjshldiv' style='width: 100%'>";
	html += "<input id='zjshl_"+index+"' autocomplete='off' value='"+zjshl+"' validate = '{ylNumber:true}' name='zjshl_"+index+"' style='border: 1px solid #ccc;border-radius: 4px;width: 80%' ";
	html += "onblur=\"checkTableValue('"+index+"',this,\'zjshl\')\" ";
	html += "></input><span>%</span>";
	html += "</div>";
	return html;
}


/**
 * 固定用量
 * @param value
 * @param row
 * @param index
 * @returns
 */
function structureEditForm_gdylformat(value,row,index){
	var gdyl=row.gdyl;
	if (!gdyl){
		gdyl = "0";
		t_map.rows[index].gdyl = gdyl;
	}
	var html="";
	html +="<div id='gdyldiv_"+index+"' name='gdyldiv' style='width:100%;display:inline-block'>";
	html += "<select id='gdyl_"+index+"' name='gdyl_"+index+"' class=' chosen-select' validate='{required:true}' onchange=\"checkTableValue('"+index+"',this,\'gdyl\')\">";
	html += "<option value=''>--请选择--</option>";
	html += "<option value='0'";
	if(gdyl!=null && gdyl!=""){
		if(gdyl== "0"){
			html += "selected"
		}
	}
	html += ">否</option>";
	html += "<option value='1'";
	if(gdyl!=null && gdyl!=""){
		if(gdyl== "1"){
			html += "selected"
		}
	}
	html += ">是</option>";
	html +="</select></div>";
	return html;
}


/**
 *使用数量
 * @param value
 * @param row
 * @param index
 * @returns
 */
function structureEditForm_syslformat(value,row,index){
	let sysl = 0;
	if (row.sysl == null){
		sysl = 1
		t_map.rows[index].sysl = sysl;
	}else {
		sysl = row.sysl
	}
	var html="";
	html ="<div id='sysldiv_"+index+"' name='sysldiv' style='width: 100%'>";
	html += "<input id='sysl_"+index+"' autocomplete='off' value='"+sysl+"' validate = '{ylNumber:true}' name='sysl_"+index+"' style='border: 1px solid #ccc;border-radius: 4px;width: 100%' ";
	html += "onblur=\"checkTableValue('"+index+"',this,\'sysl\')\" ";
	html += "></input>";
	html += "</div>";
	return html;
}


/**
 * 生产日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function structureEditForm_scrqformat(value,row,index){
	if(row.scrq == null){
		row.scrq = "";
	}
	var html = "<input id='scrq_"+index+"' autocomplete='off' value='"+row.scrq+"' name='scrq_"+index+"'  style='border-radius:5px;border:1px solid #D5D5D5;' validate='{scrqReq:true}' onchange=\"checkTableValue('"+index+"',this,\'scrq\')\"/>";
	setTimeout(function() {
		laydate.render({
			elem: '#structureEditForm #scrq_'+index
			,theme: '#2381E9'
			,max: 0
			,done: function(value, date, endDate){
				var now = new Date(value);
				if(t_map.rows[index].bzqflg=='0'){
					var nowmonth=now.getMonth();
					var nowdate=now.getDate();
					var number=parseInt(t_map.rows[index].bzq);
					now.setMonth(nowmonth+number);
					now.setDate(nowdate-1);
					var finaldate=now.getFullYear()+"-"+(now.getMonth()+1)+"-"+now.getDate();
					$("#sxrq_"+index).val(finaldate);
					t_map.rows[index].sxrq=finaldate;
				}else{
					var nowyear=now.getFullYear();
					var number=99;
					now.setFullYear(nowyear+number);
					var nowdate=now.getDate();
					now.setDate(nowdate-1);
					var finaldate=now.getFullYear()+"-"+(now.getMonth()+1)+"-"+now.getDate();
					$("#sxrq_"+index).val(finaldate);
					t_map.rows[index].sxrq=finaldate;
				}
				t_map.rows[index].scrq = value;
			}
		});
	}, 200);
	return html;
}

/**
 * 验证生产日期
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("scrqReq", function (value, element){
	if(!value){
		$.error("请填写生产日期!");
		return false;
	}
	return this.optional(element) || value;
},"请填写生产日期。");

/**
 * 有效日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function structureEditForm_sxrqformat(value,row,index){
	if(row.yxq == null){
		row.yxq = "";
	}
	var html = "<input id='sxrq_"+index+"' autocomplete='off' value='"+row.yxq+"' name='sxrq_"+index+"' style='border-radius:5px;border:1px solid #D5D5D5;' validate='{yxqReq:true}' onchange=\"checkTableValue('"+index+"',this,\'sxrq\')\"></input>";
	setTimeout(function() {
		laydate.render({
			elem: '#structureEditForm #sxrq_'+index
			,theme: '#2381E9'
			,done: function(value, date, endDate){
				t_map.rows[index].sxrq = value;
			}
		});
	}, 100);
	return html;
}
/**
 * 验证生产日期
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("yxqReq", function (value, element){
	if(!value){
		$.error("请填写有效期!");
		return false;
	}
	return this.optional(element) || value;
},"请填写y有效期。");


/**
 * 产出品
 * @param value
 * @param row
 * @param index
 * @returns
 */
function structureEditForm_ccpformat(value,row,index){
	var ccp=row.ccp;
	if (!ccp){
		ccp = "0";
		t_map.rows[index].ccp = ccp;
	}
	var html="";
	html +="<div id='ccpdiv_"+index+"' name='ccpdiv' style='width:100%;display:inline-block'>";
	html += "<select id='ccp_"+index+"' name='ccp_"+index+"' class='chosen-select' validate='{required:true}' onchange=\"checkTableValue('"+index+"',this,\'ccp\')\">";
	html += "<option value=''>--请选择--</option>";
	html += "<option value='0'";
	if(ccp!=null && ccp!=""){
		if(ccp== "0"){
			html += "selected"
		}
	}
	html += ">否</option>";
	html += "<option value='1'";
	if(ccp!=null && ccp!=""){
		if(ccp== "1"){
			html += "selected"
		}
	}
	html += ">是</option>";
	html +="</select></div>";
	return html;
}

/**
 * 成本相关
 * @param value
 * @param row
 * @param index
 * @returns
 */
function structureEditForm_cbxgformat(value,row,index){
	var cbxg=row.cbxg;
	if (!cbxg){
		cbxg = "1";
		t_map.rows[index].cbxg = cbxg;

	}
	var html="";
	html +="<div id='cbxgdiv_"+index+"' name='cbxgdiv' style='width:100%;display:inline-block'>";
	html += "<select id='cbxg_"+index+"' name='cbxg_"+index+"' class='chosen-select' validate='{required:true}' onchange=\"checkTableValue('"+index+"',this,\'cbxg\')\">";
	html += "<option value=''>--请选择--</option>";
	html += "<option value='0'";
	if(cbxg!=null && cbxg!=""){
		if(cbxg== "0"){
			html += "selected"
		}
	}
	html += ">否</option>";
	html += "<option value='1'";
	if(cbxg!=null && cbxg!=""){
		if(cbxg== "1"){
			html += "selected"
		}
	}
	html += ">是</option>";
	html +="</select></div>";
	return html;
}

/**
 * 备注格式化
 */
function structureEditForm_bzformat(value,row,index){
	if (!row.bz){
		row.bz="";
	}
	var html = "<input id='bz_"+index+"' autocomplete='off' value='"+row.bz+"' name='bz_"+index+"' validate='{stringMaxLength:1024}' onchange=\"checkTableValue('"+index+"',this,\'bz\')\"/>";
	return html;
}



/**
 * 供应类型
 * @param value
 * @param row
 * @param index
 * @returns
 */
function structureEditForm_gylxformat(value,row,index){
	var gylx=row.gylx;
	var html="";
	html +="<div id='gylxdiv_"+index+"' name='gylxdiv'>";
	html += "<select id='gylx_"+index+"' name='gylx_"+index+"' class=' chosen-select' validate='{required:true}' onchange=\"checkTableValue('"+index+"',this,\'gylx\')\">";
	html += "<option value=''>--请选择--</option>";
	if (null != gylxList && gylxList.length > 0){
		for(var i = 0; i < gylxList.length; i++) {
			html += "<option id='"+gylxList[i].csid+"' value='"+gylxList[i].csid+"' gylxdm='"+gylxList[i].csdm+"'";
			if(gylx!=null && gylx!=""){
				if(gylx==gylxList[i].csid){
					html += "selected"
				}
			}
			html += ">"+gylxList[i].csmc+"</option>";
		}
	}
	html +="</select></div>";
	return html;
}

/**
 * 产出类型
 * @param value
 * @param row
 * @param index
 * @returns
 */
function structureEditForm_cclxformat(value,row,index){
	var cclx=row.cclx;
	var html="";
	html +="<div id='cclxdiv_"+index+"' name='cclxdiv'>";
	html += "<select id='cclx_"+index+"' name='cclx_"+index+"' class=' chosen-select' onchange=\"checkTableValue('"+index+"',this,\'cclx\')\">";
	html += "<option value=''>--请选择--</option>";
	if (null != cclxList && cclxList.length > 0){
		for(var i = 0; i < cclxList.length; i++) {
			html += "<option id='"+cclxList[i].csid+"' value='"+cclxList[i].csid+"' cclxdm='"+cclxList[i].csdm+"'";
			if(cclx!=null && cclx!=""){
				if(cclx==cclxList[i].csid){
					html += "selected"
				}
			}
			html += ">"+cclxList[i].csdm+"--"+cclxList[i].csmc+"</option>";
		}
	}
	html +="</select></div>";
	return html;
}

/**
 * 仓库
 * @param value
 * @param row
 * @param index
 * @returns
 */
function structureEditForm_ckidformat(value,row,index){
	var ckid=row.ckid;
	var html="";
	html +="<div id='ckiddiv_"+index+"' name='ckiddiv'>";
	html += "<select id='ckid_"+index+"' name='ckid_"+index+"' class=' chosen-select'  onchange=\"checkTableValue('"+index+"',this,\'ckid\')\">";
	html += "<option value=''>--请选择--</option>";
	if (null != ckxxDtos && ckxxDtos.length > 0){
		for(var i = 0; i < ckxxDtos.length; i++) {
			html += "<option id='"+ckxxDtos[i].ckid+"' value='"+ckxxDtos[i].ckid+"'  ckdm='"+ckxxDtos[i].ckdm+"'";
			if(ckid!=null && ckid!=""){
				if(ckid==ckxxDtos[i].ckid){
					html += "selected"
				}
			}
			html += ">"+ckxxDtos[i].ckmc+"</option>";
		}
	}
	html +="</select></div>";
	return html;
}

/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function structureEditForm_czformat(value,row,index){
	var html="<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除' onclick=\"delWlDetail('" + index + "',event)\" >删除</span>";
	return html;
}

/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delWlDetail(index,event){
	t_map.rows.splice(index,1);
	$("#structureEditForm #structure_edit_list").bootstrapTable('load',t_map);

}
/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("ylNumber", function (value, element){
	if(!value){
		$("#structureEditForm #"+element.id).val("")
		return false;
	}else{
		if(!/^\d+(\.\d{1,2})?$/.test(value)){
			$.error("请填写正确数量格式，可保留两位小数!");
			$("#structureEditForm #"+element.id).val("")
		}
	}
	return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");

function checkTableValue(index, e, zdmc){
	if(zdmc=="zjhh"){
		t_map.rows[index].zjhh = e.value;
	}
	if(zdmc=="gxhh"){
		if (null != t_map.rows && t_map.rows.length>0){
			for (let i = 0; i < t_map.rows.length; i++) {
				if (i != index){
					if (t_map.rows[i].gxhh == e.value){
						$.error("工序行号不允许重复！");
						t_map.rows[index].gxhh = "";
						$("#structureEditForm #gxhh_"+index).val("");
						return
					}
				}
			}
		}
		t_map.rows[index].gxhh = e.value;
	}
	if(zdmc=="jbyl"){
		t_map.rows[index].jbyl = e.value;
	}
	if(zdmc=="jcsl"){
		t_map.rows[index].jcsl = e.value;
	}
	if(zdmc=="zjshl"){
		t_map.rows[index].zjshl = e.value;
	}
	if(zdmc=="gdyl"){
		t_map.rows[index].gdyl = e.value;
	}
	if(zdmc=="gylx"){
		t_map.rows[index].gylx = e.value;
	}
	if(zdmc=="sysl"){
		t_map.rows[index].sysl = e.value;
	}
	if(zdmc=="scrq"){
		t_map.rows[index].scrq = e.value;
	}
	if(zdmc=="sxrq"){
		t_map.rows[index].sxrq = e.value;
	}
	if(zdmc=="cclx"){
		t_map.rows[index].cclx = e.value;
	}
	if(zdmc=="ckid"){
		t_map.rows[index].ckid = e.value;
		t_map.rows[index].ckdm = $("#structureEditForm #dtoList").val();
	}
	if(zdmc=="ccp"){
		t_map.rows[index].ccp = e.value;
	}
	if(zdmc=="cbxg"){
		t_map.rows[index].cbxg = e.value;
	}
	if(zdmc=="bz"){
		t_map.rows[index].bz = e.value;
	}
}


// 显示字段
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
    }
	// , {
    //     field: 'zjhh',
    //     title: '子件行号',
    //     titleTooltip:'子件行号',
    //     width: '4%',
	// 	formatter: structureEditForm_zjhhformat,
    //     align: 'left',
    //     visible: true
    // }, {
    //     field: 'gxhh',
    //     title: '工序行号',
    //     titleTooltip:'工序行号',
    //     width: '4%',
    //     align: 'left',
	// 	formatter: structureEditForm_gxhhformat,
    //     visible: true
    // }
	, {
        field: 'wlbm',
        title: '子件编码',
        titleTooltip:'子件编码',
		formatter: structureEditForm_format,
        width: '4%',
        align: 'left',
        visible: true
    }, {
		field: 'wlmc',
		title: '子件名称',
		titleTooltip:'子件名称',
		width: '4%',
		align: 'left',
		visible: true
	},{
        field: 'gg',
        title: '子件规格',
        titleTooltip:'子件规格',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'jldw',
        title: '单位',
        titleTooltip:'单位',
        width: '3%',
        align: 'left',
        visible: true
    }, {
        field: 'jbyl',
        title: '基本用量',
        titleTooltip:'基本用量',
		formatter: structureEditForm_jbylformat,
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'jcsl',
        title: '基础用量',
        titleTooltip:'基础用量',
		formatter: structureEditForm_jcslformat,
        width: '4%',
        align: 'left',
        visible: true
	}
	// , {
	// 	field: 'zjshl',
	// 	title: '子件损耗率',
	// 	titleTooltip:'子件损耗率',
	// 	formatter: structureEditForm_zjshlformat,
	// 	width: '4%',
	// 	align: 'left',
	// 	visible: true
	// }
	, {
		field: 'gdyl',
		title: '固定用量',
		titleTooltip:'固定用量',
		formatter: structureEditForm_gdylformat,
		width: '5%',
		align: 'left',
		visible: true
	}, {
		field: 'gylx',
		title: '供应类型',
		titleTooltip:'供应类型',
		formatter: structureEditForm_gylxformat,
		width: '5%',
		align: 'left',
		visible: true
	}
	// , {
	// 	field: 'sysl',
	// 	title: '使用数量',
	// 	titleTooltip:'使用数量',
	// 	formatter: structureEditForm_syslformat,
	// 	width: '3%',
	// 	align: 'left',
	// 	visible: true
	// }
	,{
		field: 'scrq',
		title: '生产日期',
		titleTooltip:'生产日期',
		formatter: structureEditForm_scrqformat,
		width: '6%',
		align: 'left',
		visible: true
	}, {
		field: 'sxrq',
		title: '失效日期',
		titleTooltip:'失效日期',
		formatter: structureEditForm_sxrqformat,
		width: '6%',
		align: 'left',
		visible: true
	}, {
		field: 'ckid',
		title: '仓库',
		titleTooltip:'仓库',
		formatter: structureEditForm_ckidformat,
		width: '6%',
		align: 'left',
		visible: true
	}, {
		field: 'ccp',
		title: '产出品',
		titleTooltip:'产出品',
		formatter: structureEditForm_ccpformat,
		width: '5%',
		align: 'left',
		visible: true
	}
	// , {
	// 	field: 'cclx',
	// 	title: '产出类型',
	// 	titleTooltip:'产出类型',
	// 	formatter: structureEditForm_cclxformat,
	// 	width: '6%',
	// 	align: 'left',
	// 	visible: true
	// }
	, {
		field: 'cbxg',
		title: '成本相关',
		titleTooltip:'成本相关',
		formatter: structureEditForm_cbxgformat,
		width: '6%',
		align: 'left',
		visible: true
	}, {
		field: 'bz',
		title: '备注',
		titleTooltip:'备注',
		formatter: structureEditForm_bzformat,
		width: '6%',
		align: 'left',
		visible: true
	},{
		field: 'cz',
		title: '操作',
		titleTooltip:'操作',
		width: '4%',
		align: 'left',
		formatter:structureEditForm_czformat,
		visible: true
    }];
var structure_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#structureEditForm #structure_edit_list').bootstrapTable({
            url: $('#structureEditForm #urlPrefix').val()+$('#structureEditForm #url').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#structureEditForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "zjhh",					//排序字段
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
            uniqueId: "cpjgmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
            	t_map = map;
            	if(t_map.length>0){
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
            sortLastName: "cpjgmxid", //防止同名排位用
            sortLastOrder: "asc",//防止同名排位用
			cpjgid:$("#structureEditForm #cpjgid").val()
            //搜索框使用
            //search:params.search
        };
    	return map;
    };
    return oTableInit;
};




/**
 * 选择订单号
 * @returns
 */
function chooseWl(){
	var url = $('#structureEditForm #urlPrefix').val()+"/storehouse/materiel/pagedataListMater?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择物料', wlChooseConfig);
}

var wlChooseConfig = {
	width : "1300px",
	modalName	: "chooseDdhModal",
	formName	: "chooseDdhForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#mater_choose_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length !=1 ){
					$.error("有且只能选中一行");
					return false;
				}
				if (!sel_row[0].wlid){
					$.error("未获取到物料！");
					return false;
				}
				$.ajax({
					type : "POST",
					url : $('#structureEditForm #urlPrefix').val()+'/storehouse/structure/pagedataWhetherWl',
					data : {"mjwlid" :sel_row[0].wlid+"","access_token":$("#ac_tk").val(),"nowwlid":$('#structureEditForm #nowwlid').val()},
					dataType : "json",
					success:function(res){
						if(res != null && res.status == false){
							$.error("已存在该母料产品结构！");
							return false;
						}else{
							$('#structureEditForm #mjbm').val(sel_row[0].wlbm +"-"+sel_row[0].wlmc);
							$('#structureEditForm #mjwlid').val(sel_row[0].wlid);
						}
					}
				});
				return true;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};


/**
 * 根据物料名称模糊查询
 */
$('#structureEditForm #addnr').typeahead({
	source : function(query, process) {
		return $.ajax({
			url : $('#structureEditForm #urlPrefix').val()+'/progress/progress/pagedataQueryWlxxById',
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
		$('#structureEditForm #addwlid').attr('value', item.id);
		return item.name;
	}
});

/**
 * 回车键添加物料至请购车
 * @param event
 * @returns
 */
var addkeydown = $("#addnr").bind("keydown",function(event){
	var wlid=$('#structureEditForm #addwlid').val();
	if (event.keyCode == "13") {
		//判断新增输入框是否有内容
		if(!$('#structureEditForm #addnr').val()){
			return false;
		}
		setTimeout(function(){ if(wlid != null && wlid != ''){
			$.ajax({
				type:'post',
				url:$('#structureEditForm #urlPrefix').val()+"/progress/progress/pagedataQueryWlxxById",
				cache: false,
				data: {"wlid":wlid,"access_token":$("#ac_tk").val()},
				dataType:'json',
				success:function(data){
					//返回值
					if(data.dtoList!=null && data.dtoList.length>0){

						if(t_map.rows==null || t_map.rows ==""){
							t_map.rows=[];
						}
						t_map.rows.push(data.dtoList[0]);
						$("#structureEditForm #structure_edit_list").bootstrapTable('load',t_map);
						$('#structureEditForm #addwlid').val("");
						$('#structureEditForm #addnr').val("");

					}else{
						$.confirm("该物料不存在!");
					}
				}
			});
		}else{
			var addnr = $('#structureEditForm #addnr').val();
			if(addnr != null && addnr != ''){
				$.confirm("请选择物料信息!");
			}
		}}, '200')
	}
});


/**
 * 点击确定添加物料
 * @param
 * @param
 * @returns
 */
function punchCofirmaddwl(){
	var wlid=$('#structureEditForm #addwlid').val();
	//判断新增输入框是否有内容
	if(!$('#structureEditForm #addnr').val()){
		return false;
	}
	setTimeout(function(){ if(wlid != null && wlid != ''){
		$.ajax({
			type:'post',
			url:$('#structureEditForm #urlPrefix').val()+"/progress/progress/pagedataQueryWlxxById",
			cache: false,
			data: {"wlid":wlid,"access_token":$("#ac_tk").val()},
			dataType:'json',
			success:function(data){
				//返回值
				if(data.dtoList!=null && data.dtoList.length>0){
					if(t_map.rows==null || t_map.rows ==""){
						t_map.rows=[];
					}
					t_map.rows.push(data.dtoList[0]);
					$("#structureEditForm #structure_edit_list").bootstrapTable('load',t_map);
					$('#structureEditForm #addwlid').val("");
					$('#structureEditForm #addnr').val("");
				}else{
					$.confirm("该物料不存在!");
				}
			}
		});
	}else{
		var addnr = $('#structureEditForm #addnr').val();
		if(addnr != null && addnr != ''){
			$.confirm("请选择物料信息!");
		}
	}}, '200')
}


/**
 * 初始化页面
 * @returns
 */
function init(){
  	//添加日期控件
	laydate.render({
	   elem: '#bbrq'
	  ,theme: '#2381E9'
	});
}
$(document).ready(function(){
	cclxList = JSON.parse($("#structureEditForm #dtoList").val());
	ckxxDtos = JSON.parse($("#structureEditForm #ckxxDtos").val());
	gylxList = JSON.parse($("#structureEditForm #jcsjDtoList").val());
	//初始化列表
	var oTable=new structure_TableInit();
	oTable.Init();
	// 初始化页面
	init();
	jQuery('#structureEditForm .chosen-select').chosen({width: '100%'});
});