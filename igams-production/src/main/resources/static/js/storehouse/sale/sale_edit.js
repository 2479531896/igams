var t_map = [];
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
	}, {
		field: 'wlid',
		title: '物料ID',
		titleTooltip:'物料ID',
		width: '4%',
		align: 'left',
		visible: false
	}, {
		field: 'wlbm',
		title: '物料编码',
		titleTooltip:'物料编码',
		formatter: sale_editForm_format,
		width: '8%',
		align: 'left',
		visible: true
	}, {
		field: 'wlmc',
		title: '物料名称',
		titleTooltip:'物料名称',
		width: '10%',
		align: 'left',
		visible: true
	}, {
		field: 'jldw',
		title: '单位',
		titleTooltip:'单位',
		width: '10%',
		align: 'left',
		visible: true
	}, {
		field: 'gg',
		title: '规格',
		titleTooltip:'规格',
		width: '8%',
		align: 'left',
		visible: true
	}
	// ,{
	// 	field: 'kcl',
	// 	title: '可售数量',
	// 	titleTooltip:'可售数量',
	// 	width: '6%',
	// 	align: 'left',
	// 	visible: true
	// }
	, {
		field: 'sl',
		title: '数量',
		titleTooltip:'数量',
		width: '8%',
		align: 'left',
		formatter:slformat,
		visible: true
	}, {
		field: 'hsdj',
		title: '含税单价',
		titleTooltip:'含税单价',
		width: '8%',
		align: 'left',
		formatter:hsdjformat,
		visible: true
	}, {
		field: 'wsdj',
		title: '无税单价',
		titleTooltip:'无税单价',
		width: '8%',
		align: 'left',
		formatter:wsdjformat,
		visible: true
	},{
		field: 'jsze',
		title: '价税总额',
		titleTooltip:'价税总额',
		width: '8%',
		align: 'left',
		formatter:jszeformat,
		visible: true
    },  {
        field: 'wszje',
        title: '无税总金额',
        titleTooltip:'无税总金额',
        width: '8%',
        align: 'left',
        formatter:wszjeformat,
        visible: true
    }, {
		field: 'dky',
		title: '到款月',
		titleTooltip:'到款月',
		width: '8%',
		align: 'left',
		formatter:dkyformat,
		visible: true
	}, {
		field: 'dkje',
		title: '到款金额',
		titleTooltip:'到款金额',
		width: '8%',
		align: 'left',
		formatter:dkjeformat,
		visible: true
	},{
		field: 'cplx',
		title: '产品类型',
		titleTooltip:'产品类型',
		formatter:cplxformat,
		width: '8%',
		align: 'left',
		visible: true
	},{
		field: 'yfhrq',
		title: '预发货日期',
		titleTooltip:'预发货日期',
		width: '10%',
		align: 'left',
		formatter:yfhrqformat,
		visible: true
	},{
		field: 'cz',
		title: '操作',
		titleTooltip:'操作',
		width: '6%',
		align: 'left',
		formatter:czformat,
		visible: true
	}];
var sale_edit_TableInit = function () {
	var oTableInit = new Object();
	//初始化Table
	oTableInit.Init = function () {
		$('#sale_edit_Form #sale_edit_list').bootstrapTable({
			url: $('#sale_edit_Form #urlPrefix').val()+$('#sale_edit_Form #url').val(),         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#sale_edit_Form #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   //是否显示分页（*）
			sortable: true,                     //是否启用排序
			sortName: "wlmc",					//排序字段
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
			uniqueId: "ckhwid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: columnsArray,
			onLoadSuccess: function (map) {
				t_map = map;
				if(t_map.rows){
					var json = [];
					for (var i = 0; i < t_map.rows.length; i++) {
						var sz = {"wlid":'',"sl":'',"hsdj":'',"wsdj":'',"wszje":'',"jsze":'',"yfhrq":'',"wlbm":'',"kcl":'',"ckhwid":'',"yds":'',"cplx":'',"jyxxid":'',"dky":'',"dkje":''};
						sz.wlid = t_map.rows[i].wlid;
						sz.sl = t_map.rows[i].sl;
						sz.hsdj = t_map.rows[i].hsdj;
						sz.wsdj = t_map.rows[i].wsdj;
						sz.wszje = t_map.rows[i].wszje;
						sz.jsze = t_map.rows[i].jsze;
						sz.yfhrq = t_map.rows[i].yfhrq;
						sz.wlbm = t_map.rows[i].wlbm;
						sz.kcl = t_map.rows[i].kcl;
						sz.ckhwid = t_map.rows[i].ckhwid;
						sz.yds = t_map.rows[i].yds;
						sz.cplx = t_map.rows[i].cplx;
						sz.jyxxid = t_map.rows[i].jyxxid;
						sz.dky = t_map.rows[i].dky;
						sz.dkje = t_map.rows[i].dkje;
						json.push(sz);
					}
					$("#sale_edit_Form #xsmx_json").val(JSON.stringify(json));
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
			sortLastName: "wlid", //防止同名排位用
			sortLastOrder: "asc", //防止同名排位用
			xsid: $("#sale_edit_Form #xsid").val(),
			//搜索框使用
			//search:params.search
		};
		return map;
	};
	return oTableInit;
};

/**
 *子件编码
 * @param value
 * @param row
 * @param index
 * @returns
 */
function sale_editForm_format(value,row,index){
	var html = "<a href='javascript:void(0);' onclick=\"queryXsByWlid('" + row.wlid + "',event)\">"+row.wlbm+"</a>"
	return html;
}
function queryXsByWlid(wlid){
	var url=$("#sale_edit_Form #urlPrefix").val()+"/production/materiel/viewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'物料信息',XSWlConfig);
}
var XSWlConfig = {
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
 * 产品类型格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function cplxformat(value,row,index){
	var html="";
	html +=  "<select id='cplx_"+index+"' name='cplx_"+index+"' class='form-control'  onchange=\"checkSum('"+index+"',this,\'cplx\')\">";
	html += "<option value=''>请选择--</option>";
	var val = JSON.parse($("#sale_edit_Form #cplxlist").val());
	if(val){
		for(var i=0;i<val.length;i++){
			if(row.cplx){
				if(row.cplx==val[i].csid){
					html += "<option value='"+val[i].csid+"' selected >"+val[i].csmc+"</option>";
				}else{
					html += "<option value='"+val[i].csid+"' >"+val[i].csmc+"</option>";
				}
			}else{
				html += "<option value='"+val[i].csid+"' >"+val[i].csmc+"</option>";
			}
		}
	}
	html += "</select>";
	return html;
}

/**
 * 数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function slformat(value,row,index){
	if(row.sl == null){
		row.sl = "";
	}
	var html ="";
	html += "<input id='sl_"+index+"' value='"+row.sl+"' name='sl_"+index+"' style='width:100%;border:1px solid #D5D5D5;' oninput=\"value=value.replace(/[^\\d.]/g,'')\" onblur=\"checkSum('"+index+"',this,\'sl\')\"></input>";
	return html;
}

/**
 * 到款月
 */
function dkyformat(value,row,index) {
	if(row.dky == null){
		row.dky = "";
	}
	var html = "<input id='dky_"+index+"' value='"+row.dky+"' name='dky_"+index+"' onblur=\"checkSum('"+index+"',this,\'dky\')\" style='width:85%;border-radius:5px;border:1px solid #D5D5D5;'><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='复制当前日期并替换所有'   onclick=\"copyTimeDky('"+index+"')\"><span></input>";
	setTimeout(function() {
		laydate.render({
			elem: '#sale_edit_Form #dky_'+index
			,theme: '#2381E9'
			,btns: ['clear', 'confirm']
			,done: function(value, date, endDate){
				t_map.rows[index].dky = value;
				var preJson = JSON.parse($("#sale_edit_Form #xsmx_json").val());
				preJson[index].dky=value;
				$("#sale_edit_Form #xsmx_json").val(JSON.stringify(preJson));
			}
		});
	}, 100);
	return html;
}



/**
 * 无税单价格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wsdjformat(value,row,index){
	if(row.wsdj == null){
		row.wsdj = "";
	}
	var html =""
	html += "<input id='wsdj_"+index+"' value='"+row.wsdj+"' name='wsdj_"+index+"' style='width:100%;border:1px solid #D5D5D5;' class='form-control' oninput=\"value=value.replace(/[^\\d.]/g,'')\" onblur=\"checkSum('"+index+"',this,\'wsdj\')\"></input>";
	return html;
}
/**
 * 含税单价格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function hsdjformat(value,row,index){
	if(row.hsdj == null){
		row.hsdj = "";
	}
	var html =""
	html += "<input id='hsdj_"+index+"' value='"+row.hsdj+"' name='hsdj_"+index+"' style='width:100%;border:1px solid #D5D5D5;'class='form-control' oninput=\"value=value.replace(/[^\\d.]/g,'')\"  onblur=\"checkSum('"+index+"',this,\'hsdj\')\"></input>";
	return html;
}
/**
 * 无税总金额格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wszjeformat(value,row,index){
	if(row.wszje == null){
		row.wszje = "";
	}
	var html =""
	html += "<input id='wszje_"+index+"' value='"+row.wszje+"' name='wszje_"+index+"' style='width:100%;border:1px solid #D5D5D5;' class='form-control'oninput=\"value=value.replace(/[^\\d.]/g,'')\" onblur=\"checkSum('"+index+"',this,\'wszje\')\"></input>";
	return html;
}
/**
 * 价税总额格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function jszeformat(value,row,index){
	if(row.jsze == null){
		row.jsze = "";
	}
	var html =""
	html += "<input id='jsze_"+index+"' value='"+row.jsze+"' name='jsze_"+index+"' validate='{jszeNumber:true}' style='width:100%;border:1px solid #D5D5D5;' class='form-control' oninput=\"value=value.replace(/[^\\d.]/g,'')\" onblur=\"checkSum('"+index+"',this,\'jsze\')\"></input>";
	return html;
}
/**
 * 到款金额格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function dkjeformat(value,row,index){
	if(row.dkje == null){
		row.dkje = "";
	}
	var html =""
	html += "<input id='dkje_"+index+"' value='"+row.dkje+"' name='dkje_"+index+"' validate='{dkjeNumber:true}' style='width:100%;border:1px solid #D5D5D5;' class='form-control' oninput=\"value=value.replace(/[^\\d.]/g,'')\" onblur=\"checkSum('"+index+"',this,\'dkje\')\"></input>";
	return html;
}
/**
 * 验证价税总额格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("jszeNumber", function (value, element){
	if(value && !/^\d+(\.\d{1,2})?$/.test(value)){
		$.error("请填写正确价税总额格式，可保留两位小数!");
	}
	return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");
/**
 * 到款金额格式(正整数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("dkjeNumber", function (value, element){
	if(value && !/^\d+(\.\d{1,2})?$/.test(value)){
		$.error("请填写正确到款金额格式，可保留两位小数!");
	}
	return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");
/**
 * 预发货日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function yfhrqformat(value,row,index){
	if(row.yfhrq == null){
		row.yfhrq = "";
	}
	var html = "<input id='yfhrq_"+index+"' value='"+row.yfhrq+"' name='yfhrq_"+index+"' style='width:85%;border-radius:5px;border:1px solid #D5D5D5;'><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='复制当前日期并替换所有'  onclick=\"copyTime('"+index+"')\"><span></input>";
	setTimeout(function() {
		laydate.render({
			elem: '#sale_edit_Form #yfhrq_'+index
			,theme: '#2381E9'
			,min: 1
			,btns: ['clear', 'confirm']
			,done: function(value, date, endDate){
				t_map.rows[index].yfhrq = value;
				var preJson = JSON.parse($("#sale_edit_Form #xsmx_json").val());
				preJson[index].yfhrq=value;

				$("#sale_edit_Form #xsmx_json").val(JSON.stringify(preJson));
			}
		});
	}, 100);
	return html;
}
/**
 * 复制当前日期并替换全部
 * @param wlid
 * @returns
 */
function copyTime(index){
	var rq=$("#sale_edit_Form #yfhrq_"+index).val();
	if(rq==null || rq==""){
		$.confirm("请先选择日期！");
	}else{
		for(var i=0;i<t_map.rows.length;i++){
			$("#sale_edit_Form #yfhrq_"+i).val(rq);
		}
	}
	var data=JSON.parse($("#sale_edit_Form #xsmx_json").val());
	if(data!=null && data.length>0){
		for(var j=0;j<data.length;j++){
			data[j].yfhrq=rq;
			t_map.rows[j].yfhrq=rq;
		}
	}
	$("#sale_edit_Form #xsmx_json").val(JSON.stringify(data));
}
/**
 * 复制当前日期并替换全部
 * @param wlid
 * @returns
 */
function copyTimeDky(index){
	var rq=$("#sale_edit_Form #dky_"+index).val();
	if(rq==null || rq==""){
		$.confirm("请先选择日期！");
	}else{
		for(var i=0;i<t_map.rows.length;i++){
			$("#sale_edit_Form #dky_"+i).val(rq);
		}
	}
	var data=JSON.parse($("#sale_edit_Form #xsmx_json").val());
	if(data!=null && data.length>0){
		for(var j=0;j<data.length;j++){
			data[j].dky=rq;
			t_map.rows[j].dky=rq;
		}
	}
	$("#sale_edit_Form #xsmx_json").val(JSON.stringify(data));
}

/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
	var html = "<span style='margin-left:5px;height:24px !important;' class='btn btn-default' title='删除明细' onclick=\"delSaleDetail('" + index + "',event)\" >删除</span>";
	return html;
}

/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delSaleDetail(index,event){
	var preJson = JSON.parse($("#sale_edit_Form #xsmx_json").val());
	preJson.splice(index,1);
	t_map.rows.splice(index,1);
	$("#sale_edit_Form #sale_edit_list").bootstrapTable('load',t_map);
	$("#sale_edit_Form #xsmx_json").val(JSON.stringify(preJson));
	var zje = 0;
	var dkje=0;
	if(t_map.rows!=null&&t_map.rows!=''){
		var zje = 0;
		var dkje=0;
		for (var i = 0; i < t_map.rows.length; i++) {
			zje = zje + Number(t_map.rows[i].jsze);
			dkje = dkje + Number(t_map.rows[i].dkje);
		}
		$("#sale_edit_Form #xszje").val(zje.toFixed(5).toString());
		$("#sale_edit_Form #dkje").val(dkje.toFixed(2).toString());
		$("#sale_edit_Form #ysk").val(($("#sale_edit_Form #xszje").val()-$("#sale_edit_Form #dkje").val()).toFixed(2).toString())
	}else {
		$("#sale_edit_Form #xszje").val(zje.toFixed(5).toString());
		$("#sale_edit_Form #dkje").val(dkje.toFixed(2).toString());
		$("#sale_edit_Form #ysk").val(($("#sale_edit_Form #xszje").val()-$("#sale_edit_Form #dkje").val()).toFixed(2).toString())
	}
}

/**
 * 刷新归还单号
 */

function refreshOaxsdh(){
	$.ajax({
		type:'post',
		url: $('#sale_edit_Form #urlPrefix').val()+'/storehouse/sale/pagedataRefreshDh',
		cache: false,
		data: {"access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			if(data != null && data.length != 0){
				if (data.oaxsdh){
					$("#sale_edit_Form #oaxsdh").val(data.oaxsdh);
				}
			}
		}
	});
}



/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkSum(index, e, zdmc) {
	var preJson = JSON.parse($("#sale_edit_Form #xsmx_json").val());
	var shuil = $("#sale_edit_Form #suil").val();
	if (zdmc == "sl"){
		$("#sale_edit_Form #sl_"+index).val(e.value);
		t_map.rows[index].sl = e.value;
		preJson[index].sl=e.value;
		if(t_map.rows[index].wsdj!=null&&t_map.rows[index].wsdj!=''){
			var wsdj = (t_map.rows[index].wsdj*1).toFixed(5);
			var sl = (e.value*1).toFixed(2);
			$("#sale_edit_Form #wszje_"+index).val((wsdj*sl).toFixed(5).toString());
			t_map.rows[index].wszje = (wsdj*sl).toFixed(5).toString();
			preJson[index].wszje=(wsdj*sl).toFixed(5).toString();
		}
		if(t_map.rows[index].hsdj!=null&&t_map.rows[index].hsdj!=''){
			var hsdj = (t_map.rows[index].hsdj*1).toFixed(5);
			var sl = (e.value*1).toFixed(2);
			$("#sale_edit_Form #jsze_"+index).val((hsdj*sl).toFixed(2).toString());
			t_map.rows[index].jsze = (hsdj*sl).toFixed(2).toString();
			preJson[index].jsze=(hsdj*sl).toFixed(2).toString();
		}
		if(t_map.rows!=null&&t_map.rows!=''){
			var zje = 0;
			for (var i = 0; i < t_map.rows.length; i++) {
				zje = zje + Number(t_map.rows[i].jsze);
			}
			$("#sale_edit_Form #xszje").val(zje.toFixed(5).toString());
			$("#sale_edit_Form #ysk").val(($("#sale_edit_Form #xszje").val()-$("#sale_edit_Form #dkje").val()).toFixed(2).toString())
		}
	}else if(zdmc == "hsdj"){
		$("#sale_edit_Form #hsdj_"+index).val(e.value);
		t_map.rows[index].hsdj = e.value;
		preJson[index].hsdj=e.value;
		$("#sale_edit_Form #bj_"+index).val(e.value);
		t_map.rows[index].bj = e.value;
		preJson[index].bj=e.value;
		if(shuil!=null&&shuil!='') {
			var suil = (1 + ((shuil * 1) / 100)).toFixed(5);
			var hsdj = (e.value * 1).toFixed(5);
			$("#sale_edit_Form #wsdj_"+index).val( (hsdj / suil).toFixed(5).toString());
			t_map.rows[index].wsdj = (hsdj / suil).toFixed(5).toString();
			preJson[index].wsdj = (hsdj / suil).toFixed(5).toString();
		}else{
			$("#sale_edit_Form #wsdj_"+index).val( e.value);
			preJson[index].wsdj=e.value;
		}
		if(t_map.rows[index].sl!=null&&t_map.rows[index].sl!=''){
			$("#sale_edit_Form #jsze_"+index).val( (preJson[index].hsdj*preJson[index].sl).toFixed(2).toString());
			t_map.rows[index].jsze = (t_map.rows[index].hsdj*t_map.rows[index].sl).toFixed(2).toString();
			preJson[index].jsze=(t_map.rows[index].hsdj*t_map.rows[index].sl).toFixed(2).toString();
			$("#sale_edit_Form #wszje_"+index).val((preJson[index].wsdj*preJson[index].sl).toFixed(5).toString());
			t_map.rows[index].wszje = (t_map.rows[index].wsdj*t_map.rows[index].sl).toFixed(5).toString();
			preJson[index].wszje=(t_map.rows[index].wsdj*t_map.rows[index].sl).toFixed(5).toString();
		}
		if(t_map.rows!=null&&t_map.rows!=''){

			var zje = 0;
			for (var i = 0; i < t_map.rows.length; i++) {
				zje = zje + Number(t_map.rows[i].jsze);
			}
			$("#sale_edit_Form #xszje").val(zje.toFixed(5).toString());
			$("#sale_edit_Form #ysk").val(($("#sale_edit_Form #xszje").val()-$("#sale_edit_Form #dkje").val()).toFixed(2).toString())
		}
	}else if(zdmc == "wsdj"){
		$("#sale_edit_Form #wsdj_"+index).val(e.value);
		t_map.rows[index].wsdj = e.value;
		preJson[index].wsdj=e.value;
		if(shuil!=null&&shuil!='') {
			var suil = (1 + ((shuil * 1) / 100)).toFixed(5);
			var wsdj = (e.value * 1).toFixed(5);
			$("#sale_edit_Form #hsdj_"+index).val((wsdj * suil).toFixed(5).toString());
			t_map.rows[index].hsdj = (wsdj * suil).toFixed(5).toString();
			preJson[index].hsdj = (wsdj * suil).toFixed(5).toString();
		}else{
			$("#sale_edit_Form #hsdj_"+index).val(e.value);
			t_map.rows[index].hsdj = e.value;
			preJson[index].hsdj=e.value;
		}
		if(t_map.rows[index].sl!=null&&t_map.rows[index].sl!=''){
			$("#sale_edit_Form #jsze_"+index).val((preJson[index].hsdj*preJson[index].sl).toFixed(2).toString());
			t_map.rows[index].jsze = (t_map.rows[index].hsdj*t_map.rows[index].sl).toFixed(2).toString();
			preJson[index].jsze=(t_map.rows[index].hsdj*t_map.rows[index].sl).toFixed(2).toString();
			$("#sale_edit_Form #wszje_"+index).val((preJson[index].wsdj*preJson[index].sl).toFixed(5).toString());
			t_map.rows[index].wszje = (t_map.rows[index].wsdj*t_map.rows[index].sl).toFixed(5).toString();
			preJson[index].wszje=(t_map.rows[index].wsdj*t_map.rows[index].sl).toFixed(5).toString();
		}
	}else if(zdmc == "wszje"){
		$("#sale_edit_Form #wszje_"+index).val(e.value);
		t_map.rows[index].wszje = e.value;
		preJson[index].wszje=e.value;
		if(t_map.rows[index].sl!=null&&t_map.rows[index].sl!=''){
			var wszje = (e.value*1).toFixed(5);
			var sl = (t_map.rows[index].sl*1).toFixed(2);
			$("#sale_edit_Form #wsdj_"+index).val((wszje/sl).toFixed(5).toString());
			t_map.rows[index].wsdj = (wszje/sl).toFixed(5).toString();
			preJson[index].wsdj=(wszje/sl).toFixed(5).toString();
			if(shuil!=null&&shuil!='') {
				var suil = (1 + ((shuil * 1) / 100)).toFixed(5);
				var wsdj = t_map.rows[index].wsdj;
				$("#sale_edit_Form #hsdj_"+index).val((wsdj * suil).toFixed(5).toString());
				t_map.rows[index].hsdj = (wsdj * suil).toFixed(5).toString();
				preJson[index].hsdj = (wsdj * suil).toFixed(5).toString();
				$("#sale_edit_Form #jsze_"+index).val((preJson[index].hsdj*sl).toFixed(2).toString());
				t_map.rows[index].jsze = (t_map.rows[index].hsdj*sl).toFixed(2).toString();
				preJson[index].jsze=(t_map.rows[index].hsdj*sl).toFixed(2).toString();
			}else{
				$("#sale_edit_Form #hsdj_"+index).val(preJson[index].wsdj);
				t_map.rows[index].hsdj = t_map.rows[index].wsdj;
				preJson[index].hsdj = t_map.rows[index].wsdj;
				$("#sale_edit_Form #jsze_"+index).val(preJson[index].wszje);
				t_map.rows[index].jsze = t_map.rows[index].wszje;
				preJson[index].jsze=t_map.rows[index].wszje;
			}
		}
	}else if(zdmc == "dkje"){
		$("#sale_edit_Form #dkje_"+index).val(e.value);
		t_map.rows[index].dkje = e.value;
		preJson[index].dkje=e.value;
		if(t_map.rows!=null&&t_map.rows!=''){

			var dkje=0;
			for (var i = 0; i < t_map.rows.length; i++) {
				dkje = dkje + Number(t_map.rows[i].dkje);
			}
			$("#sale_edit_Form #dkje").val(dkje.toFixed(2).toString());
			$("#sale_edit_Form #ysk").val(($("#sale_edit_Form #xszje").val()-$("#sale_edit_Form #dkje").val()).toFixed(2).toString())
		}
	}else if(zdmc == "jsze"){
		$("#sale_edit_Form #jsze_"+index).val(e.value);
		t_map.rows[index].jsze = e.value;
		preJson[index].jsze=e.value;
		if(t_map.rows[index].sl!=null&&t_map.rows[index].sl!=''){
			var jsze = (e.value*1).toFixed(5);
			var sl = (t_map.rows[index].sl*1).toFixed(2);
			$("#sale_edit_Form #hsdj_"+index).val((jsze/sl).toFixed(5).toString());
			t_map.rows[index].hsdj = (jsze/sl).toFixed(5).toString();
			preJson[index].hsdj=(jsze/sl).toFixed(5).toString();
			if(shuil!=null&&shuil!='') {
				var suil = (1 + ((shuil * 1) / 100)).toFixed(5);
				var hsdj = t_map.rows[index].hsdj;
				$("#sale_edit_Form #wsdj_"+index).val((hsdj / suil).toFixed(5).toString());
				t_map.rows[index].wsdj = (hsdj / suil).toFixed(5).toString();
				preJson[index].wsdj = (hsdj / suil).toFixed(5).toString();
				$("#sale_edit_Form #wszje_"+index).val((preJson[index].wsdj*sl).toFixed(5).toString());
				t_map.rows[index].wszje = (t_map.rows[index].wsdj*sl).toFixed(5).toString();
				preJson[index].wszje=(t_map.rows[index].wsdj*sl).toFixed(5).toString();
			}else{
				$("#sale_edit_Form #wsdj_"+index).val(preJson[index].hsdj);
				t_map.rows[index].wsdj = t_map.rows[index].hsdj;
				preJson[index].wsdj = t_map.rows[index].hsdj;
				$("#sale_edit_Form #wszje_"+index).val(preJson[index].jsze);
				t_map.rows[index].wszje = t_map.rows[index].jsze;
				preJson[index].wszje=t_map.rows[index].jsze;
			}
			if(t_map.rows!=null&&t_map.rows!=''){
				var zje = 0;
				for (var i = 0; i < t_map.rows.length; i++) {
					zje = zje + Number(t_map.rows[i].jsze);
				}
				$("#sale_edit_Form #xszje").val(zje.toFixed(5).toString());
				$("#sale_edit_Form #ysk").val(($("#sale_edit_Form #xszje").val()-$("#sale_edit_Form #dkje").val()).toFixed(2).toString())
			}
		}
	}else if (zdmc == 'cplx') {
		$("#sale_edit_Form #cplx_"+index).val(e.value);
		t_map.rows[index].cplx = e.value;
		preJson[index].cplx=e.value;
	}else if (zdmc == 'dky') {
		$("#sale_edit_Form #dky_"+index).val(e.value);
		t_map.rows[index].dky = e.value;
		preJson[index].dky=e.value;
	}
	$("#sale_edit_Form #xsmx_json").val(JSON.stringify(preJson));
}


function changeDj(){
	var preJson = JSON.parse($("#sale_edit_Form #xsmx_json").val());
	var shuil = $("#sale_edit_Form #suil").val();
	if(shuil){
		for(var index=0;index<t_map.rows.length;index++){
			t_map.rows[index].suil = shuil;
			preJson[index].suil=shuil;
			if(t_map.rows[index].hsdj!=null&&t_map.rows[index].hsdj!=''){
				var suil = (1+((shuil*1)/100)).toFixed(5);
				var hsdj = (t_map.rows[index].hsdj*1).toFixed(5);
				t_map.rows[index].wsdj = (hsdj/suil).toFixed(5).toString();
				preJson[index].wsdj=(hsdj/suil).toFixed(5).toString();
				if(t_map.rows[index].sl!=null&&t_map.rows[index].sl!=''){
					t_map.rows[index].jsze = (t_map.rows[index].hsdj*t_map.rows[index].sl).toFixed(2).toString();
					preJson[index].jsze=(t_map.rows[index].hsdj*t_map.rows[index].sl).toFixed(2).toString();
					t_map.rows[index].wszje = (t_map.rows[index].wsdj*t_map.rows[index].sl).toFixed(5).toString();
					preJson[index].wszje=(t_map.rows[index].wsdj*t_map.rows[index].sl).toFixed(5).toString();
				}
			}
		}
		$("#sale_edit_Form #sale_edit_list").bootstrapTable('load',t_map);
		$("#sale_edit_Form #xsmx_json").val(JSON.stringify(preJson));
	}
}




/**
 * 初始化页面
 * @returns
 */
function init(){
	//添加日期控件
	laydate.render({
		elem: '#sale_edit_Form #ddrq'
		,theme: '#2381E9'
	});
}




/**
 * 选择申请部门列表
 * @returns
 */
function chooseXsbm(){
	var url=$('#sale_edit_Form #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择部门',addXsbmConfig);
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
					$('#sale_edit_Form #xsbm').val(sel_row[0].jgid);
					$('#sale_edit_Form #xsbmmc').val(sel_row[0].jgmc);
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
 * 选择营销合同列表
 * @returns
 */
function chooseHtbh(){
	var url=$('#sale_edit_Form #urlPrefix').val() + "/marketingContract/marketingContract/pagedataChooseMarketingContract?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择营销合同',addHtbhConfig);
}
var addHtbhConfig = {
	width		: "1000px",
	modalName	:"addHtbhModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#chooseMarketingContractForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#sale_edit_Form #htid').val(sel_row[0].htid);
					$('#sale_edit_Form #htdh').val(sel_row[0].htbh);
					$('#sale_edit_Form #ywymc').val(sel_row[0].ssywymc);
					$('#sale_edit_Form #ywy').val(sel_row[0].ssywy);
					$('#sale_edit_Form #khjcmc').val(sel_row[0].khjc);
					$('#sale_edit_Form #khjc').val(sel_row[0].khid);
					$('#sale_edit_Form #khzd').val(sel_row[0].zd);
					$('#sale_edit_Form #zk').val(sel_row[0].htzk);
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
 * 选择负责人列表
 * @returns
 */
var lx="";
function chooseYwy(flag){
	var url=$('#sale_edit_Form #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择负责人',addYwyConfig);
	lx = flag;
}
var addYwyConfig = {
	width		: "800px",
	modalName	:"addYwyModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					if (lx=='ywy'){
						$('#sale_edit_Form #ywy').val(sel_row[0].yhid);
						$('#sale_edit_Form #ywymc').val(sel_row[0].zsxm);
						$('#sale_edit_Form #fzrmc').val(sel_row[0].zsxm);
						$('#sale_edit_Form #fzr').val(sel_row[0].yhid);
					}else if (lx=='fzr'){
						$('#sale_edit_Form #fzrmc').val(sel_row[0].zsxm);
						$('#sale_edit_Form #fzr').val(sel_row[0].yhid);
					}
					//保存操作习惯
					$.ajax({
						type:'post',
						url:$('#sale_edit_Form #urlPrefix').val() + "/common/habit/commonModUserHabit",
						cache: false,
						data: {"dxid":sel_row[0].yhid,"access_token":$("#ac_tk").val()},
						dataType:'json',
						success:function(data){}
					});
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
 * 选择客户列表
 * @returns
 */
function chooseKh(){
	var url=$('#sale_edit_Form #urlPrefix').val() + "/systemmain/client/pagedataListClient?access_token=" + $("#ac_tk").val();
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
					$('#sale_edit_Form #khjc').val(sel_row[0].khid);
					$('#sale_edit_Form #khjcmc').val(sel_row[0].khmc);
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

function cofirmAddWl(){
	var wlid=$('#sale_edit_Form #addwlid').val();
	//判断新增输入框是否有内容
	if(!$('#sale_edit_Form #addnr').val()){
		return false;
	}
	setTimeout(function(){ if(wlid != null && wlid != ''){
		$.ajax({
			type:'post',
			url:$('#sale_edit_Form #urlPrefix').val()+"/production/materiel/pagedataAddMaterOnShopping",
			cache: false,
			data: {"wlid":wlid,"access_token":$("#ac_tk").val()},
			dataType:'json',
			success:function(data){
				//返回值
				if(data.qgcglDto!=null && data.qgcglDto!=''){
					if(data.qgcglDto.wlzt!='80'){
						$.confirm("该物料未审核！");
					}else{
						if(data.qgcglDto.kcl!=null&&data.qgcglDto.kcl!=''){
							var date = new Date().Format("yyyy-MM-dd");
							if(t_map.rows==null || t_map.rows ==""){
								t_map.rows=[];
							}
							data.qgcglDto.yfhrq=date;
							if($("#sale_edit_Form #xsmx_json").val()!=null&&$("#sale_edit_Form #xsmx_json").val()!=''){
								var json=JSON.parse($("#sale_edit_Form #xsmx_json").val());
								var sz = {"wlid":'',"sl":'',"hsdj":'',"wsdj":'',"wszje":'',"jsze":'',"yfhrq":'',"wlbm":'',"kcl":'',"ckhwid":'',"yds":'',"cplx":'',"dky":'',"dkje":''};
								sz.wlid=data.qgcglDto.wlid;
								sz.wlbm=data.qgcglDto.wlbm;
								sz.kcl=data.qgcglDto.kcl;
								sz.ckhwid=data.qgcglDto.ckhwid;
								sz.yds=data.qgcglDto.yds;
								sz.yfhrq=date;
								var flag=true;
								for(var i=0;i<json.length;i++){
									if(data.qgcglDto.wlid==json[i].wlid){
										flag=false;
										break;
									}
								}
								if(flag){
									json.push(sz);
									t_map.rows.push(data.qgcglDto);
								}else{
									$.error("该物料已存在于明细中，请勿重复添加！");
								}
								$("#sale_edit_Form #xsmx_json").val(JSON.stringify(json));
							}else{
								var json = [];
								var sz = {"wlid":'',"sl":'',"hsdj":'',"wsdj":'',"wszje":'',"jsze":'',"yfhrq":'',"wlbm":'',"kcl":'',"ckhwid":'',"yds":'',"cplx":'',"dky":'',"dkje":''};
								sz.wlid=data.qgcglDto.wlid;
								sz.wlbm=data.qgcglDto.wlbm;
								sz.kcl=data.qgcglDto.kcl;
								sz.ckhwid=data.qgcglDto.ckhwid;
								sz.yds=data.qgcglDto.yds;
								sz.yfhrq=date;
								json.push(sz);
								t_map.rows.push(data.qgcglDto);
								$("#sale_edit_Form #xsmx_json").val(JSON.stringify(json));
							}
							$("#sale_edit_Form #sale_edit_list").bootstrapTable('load',t_map);
							$('#sale_edit_Form #addwlid').val("");
							$('#sale_edit_Form #addnr').val("");
						}
					}
				}else{
					$.confirm("该物料不存在!");
				}
			}
		});
	}else{
		var addnr = $('#sale_edit_Form #addnr').val();
		if(addnr != null && addnr != ''){
			$.confirm("请选择物料信息!");
		}
	}}, '200')
}

/**
 * 根据物料名称模糊查询
 */
$('#sale_edit_Form #addnr').typeahead({
	source : function(query, process) {
		return $.ajax({
			url : $('#sale_edit_Form #urlPrefix').val()+'/production/purchase/pagedataSelectWl',
			type : 'post',
			data : {
				"entire" : query,
				"access_token" : $("#ac_tk").val()
			},
			dataType : 'json',
			success : function(result) {
				var resultList = result.f_wlglDtos
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
		$('#sale_edit_Form #addwlid').attr('value', item.id);
		return item.name;
	}
});

Date.prototype.Format = function (fmt) {
	var o = {
		"M+": this.getMonth() + 1, //月份
		"d+": this.getDate(), //日
		"H+": this.getHours(), //小时
		"m+": this.getMinutes(), //分
		"s+": this.getSeconds(), //秒
		"q+": Math.floor((this.getMonth() + 3) / 3), //季度
		"S": this.getMilliseconds() //毫秒
	};
	if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
};

/**
 * 验证格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
function checkzk() {
	var value=$("#sale_edit_Form #zk").val();
	var result=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
	if(!result.test(value)){
		$.error("请填写正确格式，可保留两位小数!");
		$("#sale_edit_Form #zk").val("")
	}
}

$(document).ready(function(){
	//初始化列表
	var oTable=new sale_edit_TableInit();
	oTable.Init();
	if ($("#sale_edit_Form #dkje").val()==null||$("#sale_edit_Form #dkje").val()==''){
		$("#sale_edit_Form #dkje").val("0.00")
	}
	// 初始化页面
	init();
	//所有下拉框添加choose样式
	jQuery('#sale_edit_Form .chosen-select').chosen({width: '100%'});
});