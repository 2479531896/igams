var t_map = [];
var kwlist= [];
var zshlsh=$("#arrivalGoodsEditForm #zshlsh").val();
var nowdate=$("#arrivalGoodsEditForm #nowdate").val();
var rklbbj = $("#arrivalGoodsEditForm #rklbbj").val();
var cskz3 = $("#arrivalGoodsEditForm #cskz3").val();
var gjxgbj = $("#arrivalGoodsEditForm #gjxgbj").val();
var dhlxdm = $("#arrivalGoodsEditForm #dhlxdm").val();
var rklbdm = $("#arrivalGoodsEditForm #rklbdm").val();
var xtszqz = $("#arrivalGoodsEditForm #xtszqz").val();
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
        field: 'htmxid',
        title: '合同明细ID',
        titleTooltip:'合同明细ID',
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
        field: 'htnbbh',
        title: '合同单号/请购单号',
        titleTooltip:'合同单号/请购单号',
        width: '13%',
        align: 'left',
        visible: rklbbj=='1'||cskz3=='1'|| dhlxdm=='TH'?false:true,
		formatter:qgdhformat,
	}, {
		field: 'zldh',
		title: '指令单号',
		titleTooltip:'指令单号',
		width: '13%',
		align: 'left',
		visible: cskz3=='1'&& dhlxdm!='TH'?true:false,
    }, {
        field: 'wlbm',
        title: '物料编码/质量类别',
        titleTooltip:'物料编码/质量类别',
		width: gjxgbj =='1'?'12%':'10%',
        align: 'left',
        formatter:t_wlbmformat,
        visible: true
    }, {
		field: 'lbcsmc',
		title: '质量类别',
		titleTooltip:'质量类别',
		width: '6%',
		align: 'left',
		visible: false
	}, {
        field: 'wlmc',
        title: '物料名称/规格',
        titleTooltip:'物料名称/规格',
        formatter:wlmcggformat,
        width: '12%',
        align: 'left',
        visible: true
    },{
        field: 'jldw',
        title: '单位/货号',
        titleTooltip:'单位/货号',
        formatter:dwhhformat,
        width: '5%',
        align: 'left',
        visible: true
    }, {
        field: 'dhsl',
        title: '到货数量*',
        titleTooltip:'到货数量',
        width: '7%',
        align: 'left',
        formatter:dhslformat,
        visible: true
    }, {
        field: 'zsh',
        title: '追溯号*',
        titleTooltip:'追溯号',
        width: '9%',
        align: 'left',
        formatter:zshformat,
        visible: true,
    }, {
		field: 'cpzch',
		title: '产品注册号',
		titleTooltip:'产品注册号',
		width: '9%',
		align: 'left',
		formatter:cpzchformat,
		visible: rklbbj=='1'||cskz3=='1'?false:true,
	}, {
        field: 'scph',
        title: '生产批号',
        titleTooltip:'生产批号',
        width: '9%',
        align: 'left',
        formatter:scphformat,
        visible: true,
    }, {
        field: 'cythsl',
        title: '退回数量',
        titleTooltip:'退回数量',
        width: '6%',
        align: 'left',
        formatter:cythslformat,
        visible: gjxgbj=='1'||dhlxdm=='TH'?false:true
    }, {
        field: 'scrq',
        title: '生产日期*',
        titleTooltip:'生产日期',
        width: '8%',
        align: 'left',
        formatter:scrqformat,
        visible: true
    }, {
        field: 'yxq',
        title: '失效日期*',
        titleTooltip:'生产日期',
        width: '8%',
        align: 'left',
        formatter:yxqformat,
        visible: true
    },{
		field: 'ckmc',
		title: '仓库',
		titleTooltip:'仓库',
		width: '6%',
		align: 'left',
		visible: cskz3=='CGHZ'?true:false
	},{
        field: 'cskw',
        title: '库位',
        titleTooltip:'库位',
        width: '6%',
        align: 'left',
        formatter:kwformat,
        visible: true
    },{
        field: 'dhbz',
        title: '备注',
        titleTooltip:'备注',
        width: '8%',
        align: 'left',
        formatter:dhbzformat,
        visible: true
    },{
        field: 'htbz',
        title: '合同备注',
        titleTooltip:'合同备注',
        width: '6%',
        align: 'left',
        visible: rklbbj=='1'||cskz3=='1'||dhlxdm=='TH'?false:true,
    },{
        field: 'sbbh',
        title: '关联设备',
        titleTooltip:'关联设备',
        width: '8%',
        align: 'left',
        visible: rklbdm=="3"?true:false,
		formatter: glsbFormatter
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: rklbdm=="3"?'15%':'10%',
        align: 'left',
        formatter:czformat,
        visible: dhlxdm=='TH'?false:true
    }];

var arrivalGoodsEdit_TableInit = function () {
	var ckid=$("#arrivalGoodsEditForm #ckid").val();
	$.ajax({ 
		async: false,
	    type:'post',  
	    url:$('#arrivalGoodsEditForm #urlPrefix').val()+"/storehouse/arrivalGoods/pagedataGetkwlist", 
	    cache: false,
	    data: {"ckid":ckid,"access_token":$("#ac_tk").val()},
	    dataType:'json', 
	    success:function(data){
	    	if(data.kwlist!=null && data.kwlist.length>0){
	    		kwlist=data.kwlist;
	    	}
	    }
	});
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#arrivalGoodsEditForm #tb_list').bootstrapTable({
            url: $('#arrivalGoodsEditForm #urlPrefix').val()+'/storehouse/arrivalGoods/pagedataArrivalgoodsdetailList?dhid='+$("#arrivalGoodsEditForm #dhid").val()+"&cskz3="+cskz3,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#arrivalGoodsEditForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "hwid",				//排序字段
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
            uniqueId: "hwid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
            	t_map = map;
            	if($("#arrivalGoodsEditForm #rklbbj").val() == "1"){
					if(t_map.rows){
						// 初始化dhmx_json
						var json = [];
						for (var i = 0; i < t_map.rows.length; i++) {
							t_map.rows[i].dataPermissionModel=null;
							t_map.rows[i].dataFilterModel=null;
							var sz = {"hwid":''};
							sz.hwid =  t_map.rows[i].hwid;
							json.push(sz);
						}
						$("#arrivalGoodsEditForm #hwxx_json").val(JSON.stringify(json));
					}
            	}else{
                	if(t_map.rows){
                		// 初始化dhmx_json
                		var json = [];
                		for (var i = 0; i < t_map.rows.length; i++) {
                			t_map.rows[i].dataPermissionModel=null;
							t_map.rows[i].dataFilterModel=null;
                			var sz = {"htid":'',"htmxid":'',"qgmxid":''};
    						sz.htid = t_map.rows[i].htid;
    						sz.htmxid = t_map.rows[i].htmxid;
    						sz.qgmxid = t_map.rows[i].qgmxid;
    						json.push(sz);
        				}
                		$("#arrivalGoodsEditForm #dhmx_json").val(JSON.stringify(json));
                		$("#arrivalGoodsEditForm #dhmxJson").val(JSON.stringify(t_map.rows));
                	}
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
            sortLastName: "hwid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            hwid: $("#arrivalGoodsEditForm #hwid").val(),
            //搜索框使用
            //search:params.search
        };
    	return map;
    };
    return oTableInit;
};
/**
 * 产品注册号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function cpzchformat(value,row,index){
	if(row.cpzch == null){
		row.cpzch = "" ;
	}
	var html="<input id='cpzch_"+index+"' autocomplete='off' name='cpzch_"+index+"' validate='{required:true}' value='"+row.cpzch+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{}' onblur=\"checkSum('"+index+"',this,'"+row.wlid+"',\'cpzch\')\"></input>";	return html;
}
function dwhhformat(value,row,index){
	var jldw=row.jldw;
	var ychh=row.ychh;
	if(jldw==null)
		jldw="";
	if(ychh==null)
		ychh="";
	let html="<span class='col-md-12 col-sm-12 ' style='padding:0px;' title='"+jldw+"'>"+jldw+"</span>"+
	"<span for='' class='col-md-12 col-sm-12 ' style='padding:0px;' title='"+ychh+"'>"+ychh+"</span>";
	return html;
}

function wlmcggformat(value,row,index){
	var wlmc=row.wlmc;
	var gg=row.gg;
	let html="<span class='col-md-12 col-sm-12 ' style='padding:0px;' title='"+wlmc+"'>"+wlmc+"</span>"+
	"<span for='' class='col-md-12 col-sm-12 ' style='padding:0px;' title='"+gg+"'>"+gg+"</span>";
	return html;
}

function kwformat(value,row,index){
	if (dhlxdm=='TH'){
		return row.kwmc;
	}
	if (cskz3=='CGHZ'&&(kwlist.length<1||kwlist==null)){
		t_map.rows[index].cskw=row.cskw;
		t_map.rows[index].cskwmc=row.kwmc;
		t_map.rows[index].kwbh=row.cskw;
		t_map.rows[index].kwbhmc=row.kwmc;
		return row.kwmc;
	}
	var html="<select name='kw' onchange=\"changekw('"+index+"')\" class='form-control chosen-select' id='kw_"+index+"' style='padding:0px;'>";
	if(kwlist!=null && kwlist.length>0){
		for(var i=0;i<kwlist.length;i++){
			if(row.cskw){
				if(row.cskw==kwlist[i].ckid){
					html+="<option selected='true' id='"+kwlist[i].ckid+"' value='"+kwlist[i].ckid+"' index='"+i+"'>"+kwlist[i].ckmc+"</option>";
				}else{
					html+="<option id='"+kwlist[i].ckid+"' value='"+kwlist[i].ckid+"' index='"+i+"'>"+kwlist[i].ckmc+"</option>";
				}
			}else{
				html+="<option id='"+kwlist[i].ckid+"' value='"+kwlist[i].ckid+"' index='"+i+"'>"+kwlist[i].ckmc+"</option>";
				if (t_map.rows.length > 0){
					t_map.rows[index].cskw=kwlist[i].ckid;
					t_map.rows[index].kwbh=kwlist[i].ckid;
				}
			}
		}
	}else{
		html+="<option>-请选择-</option>";
	}
	html+="</select>";
	return html;
}

function changekw(index){
	var kwid=$("#kw_"+index+"  option:selected").val();
	var cskwmc=$("#kw_"+index+"  option:selected").text();
	if (cskz3 == '1'  || gjxgbj == '1'){
		t_map.rows[index].cskw=kwid;
		t_map.rows[index].cskwmc=cskwmc;
		t_map.rows[index].kwbh=kwid;
		t_map.rows[index].kwbhmc=cskwmc;
	}else{
		var data=JSON.parse($("#arrivalGoodsEditForm #dhmx_json").val());
		if(data!=null && data.length>0){
			for(var i=0;i<data.length;i++){
				if(data[i].index=index){
					data[i].cskw=kwid;
					data[i].cskwmc=cskwmc;
					data[i].kwbh=kwid;
					data[i].kwbhmc=cskwmc;
					t_map.rows[index].cskw=kwid;
					t_map.rows[index].cskwmc=cskwmc;
					t_map.rows[index].kwbh=kwid;
					t_map.rows[index].kwbhmc=cskwmc;
				}
			}
		}
		$("#arrivalGoodsEditForm #dhmx_json").val(JSON.stringify(data));
		$("#arrivalGoodsEditForm #dhmxJson").val(JSON.stringify(t_map.rows));
	}

}
/**
 * 数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function dhslformat(value,row,index){
	if (dhlxdm=='TH'){
		return row.dhsl;
	}
	var ydhsl=0;
	var htsl=0;
	var dhsl=0;
	var kyysl=0;
	var khcsl=0;
	if(row.dhsl!=null){
		dhsl=row.dhsl;
	}
	if(row.ydhsl!=null){
		ydhsl=row.ydhsl;
	}
	if(row.htsl!=null){
		htsl=row.htsl;
	}
	if(row.kyysl!=null){
		kyysl=row.kyysl;
	}
	if(row.khcsl!=null){
		khcsl=row.khcsl;
	}
	var html="";
	if(rklbbj == "1" && cskz3 != '1'){
		if(row.dhsl==null){
			row.dhsl="";
		}
		if(row.slsl!=null){
			row.dhsl=row.slsl;
		}
		html="<input id='sl_"+index+"' name='sl_"+index+"' value='"+row.dhsl+"' validate='{dhslNumber:true}' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,'"+row.wlid+"',\'sl\')\"></input>";
	}else if(cskz3 == '1'){
		html ="<div id='dhsldiv_"+index+"' name='dhsldiv' isBeyond='false' style='background:darkcyan;'>";
		html += "<div><input id='dhsl_"+index+"' autocomplete='off' value='"+dhsl+"' name='dhsl_"+index+"' style='width:100%;border:1px solid #D5D5D5;' validate='{dhslNumber:true}' onblur=\"checkSum('"+index+"',this,'"+row.wlid+"',\'dhsl\')\"></input></div>";
		html += "<div><span id='htsl_"+index+"' style='font-size:13px;width:100%;margin-left:3px;'>/"+kyysl+"</span></div>";
		html += "</div>";
	}else if(cskz3 == 'CGHZ'){
		html ="<div id='dhsldiv_"+index+"'  name='dhsldiv' isBeyond='false' style='background:darkcyan;'>";
		html += "<div><input id='dhsl_"+index+"' autocomplete='off' value='"+dhsl+"' name='dhsl_"+index+"' style='width:100%;border:1px solid #D5D5D5;' validate='{hcslNumber:true}' onblur=\"checkSum('"+index+"',this,'"+row.wlid+"',\'hcsl\')\"></input></div>";
		html += "<div><span id='hwsl_"+index+"' title='"+khcsl+"' style='font-size:13px;width:100%;margin-left:3px;'>/"+khcsl+"</span></div>";
		html += "</div>";
	} else{
		html ="<div id='dhsldiv_"+index+"' name='dhsldiv' isBeyond='false' style='background:darkcyan;'>";
		if(dhsl!=null && dhsl!=""){
			html += "<div><input id='dhsl_"+index+"' autocomplete='off' value='"+dhsl+"' name='dhsl_"+index+"' style='width:100%;border:1px solid #D5D5D5;' validate='{dhslNumber:true}' onblur=\"checkSum('"+index+"',this,'"+row.wlid+"',\'dhsl\')\"></input></div>";
		}else{
			html += "<div><input id='dhsl_"+index+"' autocomplete='off' value='"+(parseFloat(htsl)-parseFloat(ydhsl)).toFixed(2)+"' name='dhsl_"+index+"' style='width:100%;border:1px solid #D5D5D5;' validate='{dhslNumber:true}' onblur=\"checkSum('"+index+"',this,'"+row.wlid+"',\'dhsl\')\"></input></div>";
			row.dhsl=(parseFloat(htsl)-parseFloat(ydhsl)).toFixed(2);
		}
		html += "<div><span id='htsl_"+index+"' style='font-size:13px;width:100%;margin-left:3px;'>/"+(parseFloat(htsl)-parseFloat(ydhsl)).toFixed(2)+"</span></div>";
		html += "</div>";
	}	
	return html;
}

/**
 * 退货数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function cythslformat(value,row,index){
	var cythsl="";
	if(row.cythsl!=null){
		cythsl=row.cythsl;
	}
	var html="";
	var html = "<input id='cythsl_"+index+"' autocomplete='off' value='"+cythsl+"' name='cythsl_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,'"+row.wlid+"',\'cythsl\')\"></input>";
	return html;
}

/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
	var gjxgbj = $("#arrivalGoodsEditForm #gjxgbj").val();
	var html="";
	if(gjxgbj=="0"){
		if(rklbbj!=null && rklbbj=="0"){
			html="<div id='cf"+index+"'><span style='margin-left:5px;height:24px !important;' class='btn btn-success' title='拆分合同明细' onclick=\"splitContractDetail('" + index + "',event)\" >拆分</span>";
			html += "<span style='margin-left:5px;height:24px !important;' class='btn btn-default' title='删除合同明细' onclick=\"delContractDetail('" + index + "',event)\" >删除</span></div>";
		}else if(cskz3=="1"){
			html="<div id='cf"+index+"'><span style='margin-left:5px;height:24px !important;' class='btn btn-success' title='拆分' onclick=\"splitContractDetail('" + index + "',event)\" >拆分</span>";
			html += "<span style='margin-left:5px;height:24px !important;' class='btn btn-default' title='删除' onclick=\"delContractDetail('" + index + "',event)\" >删除</span></div>";
		}else if(rklbdm=="3"){
			html="<div id='cf"+index+"'><span style='margin-left:5px;height:24px !important;' class='btn btn-success' title='拆分货物明细' onclick=\"splitHwDetail('" + index + "',event)\" >拆分</span>";
			html += "<span style='margin-left:5px;height:24px !important;' class='btn btn-default' title='删除物料明细' onclick=\"delHwDetail('" + index + "',event)\" >删除</span>";
			if ("3"==row.lbcskz1){
				html += "<span style='margin-left:5px;height:24px !important;' class='btn btn-default' title='关联设备' onclick=\"relevancy('" + index + "',event)\" >关联</span></div>";
			}
		}else{
			html="<div id='cf"+index+"'><span style='margin-left:5px;height:24px !important;' class='btn btn-success' title='拆分货物明细' onclick=\"splitHwDetail('" + index + "',event)\" >拆分</span>";
			html += "<span style='margin-left:5px;height:24px !important;' class='btn btn-default' title='删除物料明细' onclick=\"delHwDetail('" + index + "',event)\" >删除</span></div>";
		}
//		if(row.htmxid){
//			html += "<span style='margin-left:5px;height:24px !important;' class='btn btn-success' title='新增物料合同明细' onclick=\"addInfo('" + index + "',event)\" ><span class='glyphicon glyphicon glyphicon-plus' aria-hidden='true'></span></span>";
//		}		
	}else{
		html="<div id='cf"+index+"'><span style='margin-left:5px;height:24px !important;' class='btn btn-success' title='拆分货物明细' onclick=\"splitContractDetail('" + index + "',event)\" >拆分</span>";
		html += "<span style='margin-left:5px;height:24px !important;' class='btn btn-default' title='删除物料明细' onclick=\"delContractDetail('" + index + "',event)\" >删除</span></div>";
	}
	return html;
}

/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delHwDetail(index,event){
	var preJson = JSON.parse($("#arrivalGoodsEditForm #hwxx_json").val());	
	preJson.splice(index,1);
	t_map.rows.splice(index,1);
	$("#arrivalGoodsEditForm #tb_list").bootstrapTable('load',t_map);
	$("#arrivalGoodsEditForm #hwxx_json").val(JSON.stringify(preJson));
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
	var qghtml = "";
	if(row.qgdh==null){
		qghtml = "";
	}else{
		qghtml += "<a href='javascript:void(0);' onclick=\"queryByqgid('"+row.qgid+"')\">"+row.qgdh+"</a>";
	}
	html = qghtml+"<hr style='width: 80%;margin: 0 auto;border: 0;height: 0;border-top: 1px solid rgba(0, 0, 0, 0.1);border-bottom: 1px solid rgba(255, 255, 255, 0.3);'>"+row.htnbbh;

	return html;
}
/**
 * 入库类型改变事件
 * @returns
 */
function checkrklx(){
	let rklb = $('#arrivalGoodsEditForm #rklbs').val();
	$('#arrivalGoodsEditForm #rklb').val(rklb);
	let cskz2= $("#arrivalGoodsEditForm #rklbs").find("option:selected").attr("cskz2");
	if (cskz2 == "1"){
		$("#arrivalGoodsEditForm #cglx").removeAttr("validate");
		$("#arrivalGoodsEditForm #cglxdiv").attr("style","display:none;");
		return;
	}else{
		$("#arrivalGoodsEditForm #cglx").attr("validate","{required:true}");
		$("#arrivalGoodsEditForm #cglxdiv").attr("style","display:block;");
	}
	if(rklb == null || rklb==""){
		var zlbHtml = "";
		zlbHtml += "<option value=''>--请选择--</option>";
		$("#arrivalGoodsEditForm #cglx").empty();
		$("#arrivalGoodsEditForm #cglx").append(zlbHtml);
		$("#arrivalGoodsEditForm #cglx").trigger("chosen:updated");
		return;
	}
	$.ajax({
		type:'post',
		url:$('#arrivalGoodsEditForm #urlPrefix').val() + "/systemmain/data/ansyGetJcsjList",
		cache: false,
		data: {"fcsid":rklb,"access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			if(data != null && data.length != 0){
				var zlbHtml = "";
				var cglxls=$("#arrivalGoodsEditForm #cglxls").val()
				zlbHtml += "<option value=''>--请选择--</option>";
				$.each(data,function(i){
					zlbHtml += "<option id='"+data[i].csid+"' value='" + data[i].csid + "' cskz1='"+data[i].cskz1+"'";
					if (cglxls == data[i].csid){
						zlbHtml +=  "selected = 'true'";
					}
					zlbHtml += ">" + data[i].csdm+"--" +data[i].csmc+"</option>";

				});
				$("#arrivalGoodsEditForm #cglx").empty();
				$("#arrivalGoodsEditForm #cglx").append(zlbHtml);
				$("#arrivalGoodsEditForm #cglx").trigger("chosen:updated");
			}else{
				var zlbHtml = "";
				zlbHtml += "<option value=''>--请选择--</option>";
				$("#arrivalGoodsEditForm #cglx").empty();
				$("#arrivalGoodsEditForm #cglx").append(zlbHtml);
				$("#arrivalGoodsEditForm #cglx").trigger("chosen:updated");
			}
		}
	});
}

function queryByqgid(qgid){
	var url=$("#arrivalGoodsEditForm #urlPrefix").val()+"/purchase/purchase/viewPurchase?qgid="+qgid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'请购信息',viewQgConfig);	
}

var viewQgConfig={
	width		: "1500px",
	modalName	:"viewQgModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}

//拆分到货明细
function splitContractDetail(index,event){
	var html="";
	html="<div class='col-sm-12 col-md-12 col-xs-12' style='padding:0px;'>" +
			"<div class='input-group'>" +
			"<input id='ts"+index+"'  type='text' class='form-control success'  style='padding:0px;' autocomplete='off'>"+
			"<span class='input-group-btn' style='height:28px !important'>"+
			"<button type='button' onclick=\"confirmCf('" + index + "')\" class='btn btn-success glyphicon glyphicon-ok' style='height:28px !important'></button>"+
			"<button type='button' onclick=\"refuseCf('" + index + "')\" class='btn btn-danger glyphicon glyphicon-remove' style='height:28px !important'></button>"+
			"</span>"+
			"</div>" +
			"</div>";
	$("#arrivalGoodsEditForm #cf"+index).empty();
	$("#arrivalGoodsEditForm #cf"+index).append(html);
}

//确认拆分
function confirmCf(index){
	var value=$("#arrivalGoodsEditForm #ts"+index).val();
	if(value){
		if (cskz3 =='1' || gjxgbj == '1'){
			for(var i=1;i<=value;i++){
				var t_row={
					'cskw':t_map.rows[index].cskw
					,'cskwmc':t_map.rows[index].cskwmc
					,'cythsl':t_map.rows[index].cythsl
					,'dhbz':t_map.rows[index].dhbz
					,'dhsl':t_map.rows[index].dhsl
					,'gg':t_map.rows[index].gg
					,'jldw':t_map.rows[index].jldw
					,'kwbh':t_map.rows[index].kwbh
					,'kwbhmc':t_map.rows[index].kwbhmc
					,'lbcsmc':t_map.rows[index].lbcsmc
					,'scph':t_map.rows[index].scph
					,'scrq':t_map.rows[index].scrq
					,'sczlmxid':t_map.rows[index].sczlmxid
					,'wlbm':t_map.rows[index].wlbm
					,'wlmc':t_map.rows[index].wlmc
					,'ychh':t_map.rows[index].ychh
					,'yxq':t_map.rows[index].yxq
					,'zldh':t_map.rows[index].zldh
					,'zsh':t_map.rows[index].zsh
					,'wlid':t_map.rows[index].wlid
				};
				$("#arrivalGoodsEditForm #tb_list").bootstrapTable('insertRow', {
					index: parseInt(index)+i,
					row: t_row
				});
			}
		}else{
			var preJson = JSON.parse($("#arrivalGoodsEditForm #dhmx_json").val());
			for(var i=1;i<=value;i++){
				var t_row={'hwid':'','zsh':'','bzq':t_map.rows[index].bzq,'bzqflag':t_map.rows[index].bzqflag,'lbcsmc':t_map.rows[index].lbcsmc
					,'cskw':t_map.rows[index].cskw,'dhbz':t_map.rows[index].dhbz,'dhsl':'','gg':t_map.rows[index].gg
					,'gysid':t_map.rows[index].gysid,'gysmc':t_map.rows[index].gysmc,'htid':t_map.rows[index].htid,'htmxid':t_map.rows[index].htmxid
					,'htnbbh':t_map.rows[index].htnbbh,'htsl':t_map.rows[index].htsl,'jldw':t_map.rows[index].jldw,'qgmxid':t_map.rows[index].qgmxid
					,'scph':'','scrq':'','sqbm':t_map.rows[index].sqbm,'sqbmmc':t_map.rows[index].sqbmmc
					,'wlbm':t_map.rows[index].wlbm,'wlid':t_map.rows[index].wlid,'wlmc':t_map.rows[index].wlmc,'ydhsl':t_map.rows[index].ydhsl
					,'yxq':'','zsh':'','ychh':t_map.rows[index].ychh,'dhbz':'','kwbh':t_map.rows[index].kwbh,'qgdh':t_map.rows[index].qgdh
				};
				$("#arrivalGoodsEditForm #tb_list").bootstrapTable('insertRow', {
					index: parseInt(index)+i,
					row: t_row
				});
				preJson.splice(parseInt(index)+i,0,preJson[index]);
			}
			$("#arrivalGoodsEditForm #dhmx_json").val(JSON.stringify(preJson));
			$("#arrivalGoodsEditForm #dhmxJson").val(JSON.stringify(t_map.rows));
		}
	}else{
		$.confirm("请输入需要拆分的数量！");
	}
}

//确认拆分
function confirmHwCf(index){
	var value=$("#arrivalGoodsEditForm #ts"+index).val();
	if(value){
		var preJson = JSON.parse($("#arrivalGoodsEditForm #hwxx_json").val());
		for(var i=1;i<=value;i++){
			var t_row={'hwid':'','zsh':'','bzq':t_map.rows[index].bzq,'bzqflag':t_map.rows[index].bzqflag,'lbcsmc':t_map.rows[index].lbcsmc
				,'cskw':t_map.rows[index].cskw,'dhbz':t_map.rows[index].dhbz,'dhsl':'','gg':t_map.rows[index].gg
				,'gysid':t_map.rows[index].gysid,'gysmc':t_map.rows[index].gysmc,'htid':t_map.rows[index].htid,'htmxid':t_map.rows[index].htmxid
				,'htnbbh':t_map.rows[index].htnbbh,'htsl':t_map.rows[index].htsl,'jldw':t_map.rows[index].jldw,'qgmxid':t_map.rows[index].qgmxid
				,'scph':'','scrq':'','sqbm':t_map.rows[index].sqbm,'sqbmmc':t_map.rows[index].sqbmmc
				,'wlbm':t_map.rows[index].wlbm,'wlid':t_map.rows[index].wlid,'wlmc':t_map.rows[index].wlmc,'ydhsl':t_map.rows[index].ydhsl
				,'yxq':'','zsh':'','ychh':t_map.rows[index].ychh,'dhbz':'','kwbh':t_map.rows[index].kwbh,'qgdh':t_map.rows[index].qgdh
			};
			$("#arrivalGoodsEditForm #tb_list").bootstrapTable('insertRow', {
				index: parseInt(index)+i,
				row: t_row
			});
			preJson.splice(parseInt(index)+i,0,preJson[index]);
		}
		$("#arrivalGoodsEditForm #hwxx_json").val(JSON.stringify(preJson));
	}else{
		$.confirm("请输入需要拆分的数量！");
	}
}

//撤销拆分
function refuseCf(){
	$("#arrivalGoodsEditForm #tb_list").bootstrapTable('load',t_map);
}


function splitHwDetail(index,event){
	var html="";
	html="<div class='col-sm-12 col-md-12 col-xs-12' style='padding:0px;'>" +
		"<div class='input-group'>" +
		"<input id='ts"+index+"'  type='text' class='form-control success'  style='padding:0px;' autocomplete='off'>"+
		"<span class='input-group-btn' style='height:28px !important'>"+
		"<button type='button' onclick=\"confirmHwCf('" + index + "')\" class='btn btn-success glyphicon glyphicon-ok' style='height:28px !important'></button>"+
		"<button type='button' onclick=\"refuseCf('" + index + "')\" class='btn btn-danger glyphicon glyphicon-remove' style='height:28px !important'></button>"+
		"</span>"+
		"</div>" +
		"</div>";
	$("#arrivalGoodsEditForm #cf"+index).empty();
	$("#arrivalGoodsEditForm #cf"+index).append(html);
}

/**
 * 删除操作(从合同明细删除)
 * @param index
 * @param event
 * @returns
 */
function delContractDetail(index,event){
	if (cskz3 == '1' || gjxgbj == '1'){
		t_map.rows.splice(index,1);
		$("#arrivalGoodsEditForm #tb_list").bootstrapTable('load',t_map);
	}else{
		var preJson = JSON.parse($("#arrivalGoodsEditForm #dhmx_json").val());
		preJson.splice(index,1);
		t_map.rows.splice(index,1);
		$("#arrivalGoodsEditForm #tb_list").bootstrapTable('load',t_map);
		$("#arrivalGoodsEditForm #dhmx_json").val(JSON.stringify(preJson));
		$("#arrivalGoodsEditForm #dhmxJson").val(JSON.stringify(t_map.rows));
	}

}
/*
	关联设备
 */
function relevancy(index,e){
	var url=$("#arrivalGoodsEditForm #urlPrefix").val()+"/storehouse/arrivalGoods/pagedataViewGlsb?index="+index+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'关联设备',relevancyConfig);
}

var relevancyConfig = {
	width		: "1200px",
	modalName	:"relevancyModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#dh_glsb_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					t_map.rows[Number($("#dh_glsb_formSearch #index").val())].sbysid = sel_row[0].sbysid;
					t_map.rows[Number($("#dh_glsb_formSearch #index").val())].sbbh = sel_row[0].sbbh;
					$("#arrivalGoodsEditForm #tb_list").bootstrapTable('load',t_map);
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
 * 追溯号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function zshformat(value,row,index){
	if (dhlxdm=='TH'){
		return row.zsh;
	}
	if(row.zsh == null){
		var zsh = "00" + zshlsh;
		zsh = zsh.substring(zsh.length-3);
		row.zsh = nowdate+zsh;
		zshlsh=parseInt(zshlsh)+1;
	}
	var html = "<input id='zsh_"+index+"' autocomplete='off' value='"+row.zsh+"' name='zsh_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{zshReq:true,stringMaxLength:32}' onblur=\"checkSum('"+index+"',this,'"+row.wlid+"',\'zsh\')\"></input>";
	return html;
}

/**
 * 生产批号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function scphformat(value,row,index){
	if(row.scph == null){
		row.scph = "";
	}
	if (dhlxdm=='TH'){
		return row.scph;
	}
	var html = "<input id='scph_"+index+"' autocomplete='off' value='"+row.scph+"' name='scph_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{scphReq:true,stringMaxLength:32}' onblur=\"checkSum('"+index+"',this,'"+row.wlid+"',\'scph\')\"></input>";
	return html;
}

/**
 * 生产日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function scrqformat(value,row,index){
	if(row.scrq == null){
		row.scrq = "";
	}
	if (dhlxdm=='TH'){
		return row.scrq;
	}
	var html = "<input id='scrq_"+index+"' autocomplete='off' value='"+row.scrq+"' name='scrq_"+index+"'  style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' validate='{scrqReq:true}' onchange=\"checkSum('"+index+"',this,'"+row.wlid+"',\'scrq\')\"><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='复制当前日期并替换所有'  onclick=\"copyTime('"+index+"',\'scrq\')\"><span></input>";
	setTimeout(function() {
		laydate.render({
			elem: '#arrivalGoodsEditForm #scrq_'+index
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
	  				$("#yxq_"+index).val(finaldate);
	  				t_map.rows[index].yxq=finaldate;
	  			}else{
	  				var nowyear=now.getFullYear();
	  				var number=99;
	  				now.setFullYear(nowyear+number);
	  				var nowdate=now.getDate();
	  				now.setDate(nowdate-1);
	  				var finaldate=now.getFullYear()+"-"+(now.getMonth()+1)+"-"+now.getDate();
	  				$("#yxq_"+index).val(finaldate);
	  				t_map.rows[index].yxq=finaldate;
	  			}
	  			t_map.rows[index].scrq = value;
	  			$("#arrivalGoodsEditForm #dhmxJson").val(JSON.stringify(t_map.rows));
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

/**追溯号生产日期
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("zshReq", function (value, element){
	if(!value){
		$.error("请填写追溯号!");
		return false;
	}
    return this.optional(element) || value;
},"请填写追溯号。");

/**生产批号
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("scphReq", function (value, element){
	if(!value){
		$.error("请填写生产批号!");
		return false;
	}
    return this.optional(element) || value;
},"请填写生产批号。");

/**
 * 有效日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function yxqformat(value,row,index){
	if(row.yxq == null){
		row.yxq = "";
	}
	if (dhlxdm=='TH'){
		return row.yxq;
	}
	var html = "<input id='yxq_"+index+"' autocomplete='off' value='"+row.yxq+"' name='yxq_"+index+"' style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' validate='{yxqReq:true}' onchange=\"checkSum('"+index+"',this,'"+row.wlid+"',\'yxq\')\"><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='复制当前日期并替换所有'  onclick=\"copyTime('"+index+"',\'yxq\')\"><span></input>";
	setTimeout(function() {
		laydate.render({
			elem: '#arrivalGoodsEditForm #yxq_'+index
		  	,theme: '#2381E9'
	  		,done: function(value, date, endDate){
	  			t_map.rows[index].yxq = value;
	  			$("#arrivalGoodsEditForm #dhmxJson").val(JSON.stringify(t_map.rows));
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
 * 备注格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function dhbzformat(value,row,index){
	if (dhlxdm=='TH'){
		return row.dhbz;
	}
	if(row.dhbz == null){
		row.dhbz = "" ;
	}
	var html="<input id='dhbz_"+index+"' autocomplete='off' name='dhbz_"+index+"' value='"+row.dhbz+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{stringMaxLength:1024}' onblur=\"checkSum('"+index+"',this,'"+row.wlid+"',\'dhbz\')\"></input>";
	return html;
}


/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */

function t_wlbmformat(value,row,index){
	var html = "";
	if($('#ac_tk').length > 0 && row.lbcsmc != null && row.lbcsmc != "null"){
		html += "<a href='javascript:void(0);' onclick=\"queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>"+
			"<span for='' class='col-md-12 col-sm-12 ' style='padding:0px;' title='"+row.lbcsmc+"'>/"+row.lbcsmc+"</span>";
	}else if ($('#ac_tk').length > 0 && (row.lbcsmc == null || row.lbcsmc == "null")){
		html += "<a href='javascript:void(0);' onclick=\"queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>"+
			"<span for='' class='col-md-12 col-sm-12 ' style='padding:0px;' title='"+row.lbcsmc+"'>-</span>";
	}else {
		html += row.wlbm+
			"<span for='' class='col-md-12 col-sm-12 ' style='padding:0px;' title='"+row.lbcsmc+"'>/"+row.lbcsmc+"</span>";
	}

	return html;
}
function queryByWlbm(wlid){
	var url=$("#arrivalGoodsEditForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
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
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */

function glsbFormatter(value,row,index){
	var html = "";
	if(row.sbysid!=null && row.sbysid!=''){
		html = html + "<a href='javascript:void(0);' onclick=\"viewSb('"+row.sbysid+"')\">"+(row.sbbh?row.sbbh:'')+"</a>";
	}else {
		return row.sbbh?row.sbbh:'';
	}
	return html;
}
function viewSb(sbysid){
	var url=$("#arrivalGoodsEditForm #urlPrefix").val()+"/device/device/pagedataViewDeviceCheck?sbysid="+sbysid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'设备信息',viewSbConfig);
}
var viewSbConfig = {
		width		: "1400px",
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
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("hcslNumber", function (value, element){
	if(!value) {
		$.error("请填写数量!");
		return false;
	}else {
		if (Number(value)>=0){
			$.error("数量不能大于0!");
			return false;
		}
	}
	return this.optional(element) || /^(-)?\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");


/**
 * 复制当前日期并替换全部
 * @param wlid
 * @returns
 */
function copyTime(index,zdmc){
	var dqrq=$("#arrivalGoodsEditForm #"+zdmc+"_"+index).val();
	if(dqrq==null || dqrq==""){
		$.confirm("请先选择日期！");
	}else{
		for(var i=0;i<t_map.rows.length;i++){
			$("#arrivalGoodsEditForm #"+zdmc+"_"+i).val(dqrq);
		}
	}
	if(rklbbj!=null && rklbbj=="0"){
		var data=JSON.parse($("#arrivalGoodsEditForm #dhmx_json").val());
		if(data!=null && data.length>0){
			for(var j=0;j<data.length;j++){
				if(zdmc=="scrq"){
					var now = new Date(dqrq);
		  			if(t_map.rows[j].bzqflg=='0'){
		  				var nowmonth=now.getMonth();
		  				var number=parseInt(t_map.rows[j].bzq);
		  				now.setMonth(nowmonth+number);
		  				var nowdate=now.getDate();
		  				now.setDate(nowdate-1);
		  				var finaldate=now.getFullYear()+"-"+(now.getMonth()+1)+"-"+now.getDate();
		  				$("#yxq_"+j).val(finaldate);
		  				t_map.rows[j].yxq=finaldate;
		  			}else{
		  				var nowyear=now.getFullYear();
		  				var number=99;
		  				now.setFullYear(nowyear+number);
		  				var nowdate=now.getDate();
		  				now.setDate(nowdate-1);
		  				var finaldate=now.getFullYear()+"-"+(now.getMonth()+1)+"-"+now.getDate();
		  				$("#yxq_"+j).val(finaldate);
		  				t_map.rows[j].yxq=finaldate;
		  			}
					data[j].scrq=dqrq;
					t_map.rows[j].scrq=dqrq;
				}else if(zdmc=="yxq"){
					data[j].yxq=dqrq;
					t_map.rows[j].yxq=dqrq;
				}
			}
		}
		$("#arrivalGoodsEditForm #dhmx_json").val(JSON.stringify(data));
		$("#arrivalGoodsEditForm #dhmxJson").val(JSON.stringify(t_map.rows));
	}else{
		if(t_map.rows.length>0){
			for(var j=0;j<t_map.rows.length;j++){
				if(zdmc=="scrq"){
					var now = new Date(dqrq);
		  			if(t_map.rows[j].bzqflg=='0'){
		  				var nowmonth=now.getMonth();
		  				var number=parseInt(t_map.rows[j].bzq);
		  				now.setMonth(nowmonth+number);
		  				var nowdate=now.getDate();
		  				now.setDate(nowdate-1);
		  				var finaldate=now.getFullYear()+"-"+(now.getMonth()+1)+"-"+now.getDate();
		  				$("#yxq_"+j).val(finaldate);
		  				t_map.rows[j].yxq=finaldate;
		  			}else{
		  				var nowyear=now.getFullYear();
		  				var number=99;
		  				now.setFullYear(nowyear+number);
		  				var nowdate=now.getDate();
		  				now.setDate(nowdate-1);
		  				var finaldate=now.getFullYear()+"-"+(now.getMonth()+1)+"-"+now.getDate();
		  				$("#yxq_"+j).val(finaldate);
		  				t_map.rows[j].yxq=finaldate;
		  			}
					t_map.rows[j].scrq=dqrq;
				}else if(zdmc=="yxq"){
					t_map.rows[j].yxq=dqrq;
				}
			}
		}
	}

}

/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkSum(index, e, wlid,zdmc) {
	if(t_map.rows.length > index){
		if (zdmc == 'dhsl') {
			var dhsl = e.value
			t_map.rows[index].dhsl = dhsl;
			if(!dhsl){
				dhsl = 0;
			}
			// 判断背景颜色
			var htsl = $("#arrivalGoodsEditForm #htsl_"+index).text();
			htsl = htsl.substring(1,htsl.length);
			if(parseFloat(dhsl).toFixed(2) - parseFloat(htsl).toFixed(2) > 0){
				$("#arrivalGoodsEditForm #dhsldiv_"+index).attr("style","background:orange;");
				$("#arrivalGoodsEditForm #dhsldiv_"+index).attr("isBeyond","false");
			}else{
				$("#arrivalGoodsEditForm #dhsldiv_"+index).attr("style","background:darkcyan;");
				$("#arrivalGoodsEditForm #dhsldiv_"+index).attr("isBeyond","true");
			}
		} else if (zdmc == 'zsh') {
			t_map.rows[index].zsh = e.value;			
		} else if (zdmc == 'scph') {
			t_map.rows[index].scph = e.value;
		} else if (zdmc == 'scrq') {
			t_map.rows[index].scrq = e.value;
		} else if (zdmc == 'yxq') {
			t_map.rows[index].yxq = e.value;
		} else if(zdmc == 'dhbz'){
			t_map.rows[index].dhbz = e.value;
		} else if(zdmc == 'cythsl'){
			t_map.rows[index].cythsl = e.value;
		} else if(zdmc == 'sl'){
			t_map.rows[index].dhsl = e.value;
		}else if (zdmc == 'hcsl'){
			var hcsl = e.value
			t_map.rows[index].dhsl = hcsl;
			if(!hcsl){
				hcsl = 0;
			}
			// 判断背景颜色
			var hwsl = $("#arrivalGoodsEditForm #hwsl_"+index).text();
			hwsl = hwsl.substring(1,hwsl.length);
			if(Math.abs(parseFloat(hcsl).toFixed(2))- Math.abs(parseFloat(hwsl).toFixed(2)) > 0){
				$("#arrivalGoodsEditForm #dhsldiv_"+index).attr("style","background:orange;");
				$("#arrivalGoodsEditForm #dhsldiv_"+index).attr("isBeyond","false");
				$.error("红冲数量不能超过库存量！")
				t_map.rows[index].dhsl = '';
				$("#arrivalGoodsEditForm #dhsl_"+index).val('')
			}else{
				$("#arrivalGoodsEditForm #dhsldiv_"+index).attr("style","background:darkcyan;");
				$("#arrivalGoodsEditForm #dhsldiv_"+index).attr("isBeyond","true");
			}
		}else if(zdmc == 'cpzch'){
			t_map.rows[index].cpzch = e.value;
		}
	}
	$("#arrivalGoodsEditForm #dhmxJson").val(JSON.stringify(t_map.rows));
}


/**
 * 选择合同单号(及明细)
 * @returns
 */
function chooseHtxx(){
	if (cskz3=='CGHZ'){
		var url = $('#arrivalGoodsEditForm #urlPrefix').val();
		url = url + "/contract/contract/pagedataChooseListContract?cghzbj=1&access_token=" + $("#ac_tk").val();
		$.showDialog(url, '选择合同单号', chooseHtglConfig);
	}else {
		if (kwlist == null || kwlist.length <= 0) {
			$.confirm("请先选择仓库！");
		} else {
			var url = $('#arrivalGoodsEditForm #urlPrefix').val();
			if (rklbbj != null && rklbbj == "0" && (cskz3 == "0" || cskz3 == "WW" || cskz3 == "CGHZ")) {
				if (dhlxdm == 'OA') {
					url = url + "/contract/contract/pagedataChooseListContract?htlx=1&access_token=" + $("#ac_tk").val();
					$.showDialog(url, '选择合同单号', chooseHtglConfig);
				} else {
					if (cskz3 == 'WW') {
						url = url + "/contract/contract/pagedataChooseListContract?htlx=2&access_token=" + $("#ac_tk").val();
						$.showDialog(url, '选择合同单号', chooseHtglConfig);
					} else {
						url = url + "/contract/contract/pagedataChooseListContract?htlx=0&access_token=" + $("#ac_tk").val();
						$.showDialog(url, '选择合同单号', chooseHtglConfig);
					}
				}
			} else if (rklbbj != null && rklbbj == "1" && cskz3 == "0") {
				url = url + "/storehouse/arrivalGoods/pagedataChooseMaterial?access_token=" + $("#ac_tk").val() + "&zt=80";
				$.showDialog(url, '选择物料', chooseWlglConfig);
			} else if (cskz3 == "1") {
				if (t_map.rows && t_map.rows.length > 0) {
					$.alert("只允许引用一条生产指令单！");
					return false;
				}
				url = url + "/storehouse/arrivalGoods/pagedataChooseProduce?access_token=" + $("#ac_tk").val();
				$.showDialog(url, '选择生产指令', chooseProduceConfig);
			} else {
				$.alert("请先选择入库类别！");
				return false;
			}
		}
	}
}
var chooseProduceConfig = {
	width : "1600px",
	modalName	: "chooseProduceModal",
	formName	: "chooseProduceForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				if ($("#arrival_produce_formSearch #sczl_json").val()){
					var sczl_json = JSON.parse($("#arrival_produce_formSearch #sczl_json").val());
					if(sczl_json.length > 0){
						$("#arrivalGoodsEditForm #sczlid").val(sczl_json[0].sczlid);
						let ids = "";
						for (var i = 0; i < sczl_json.length; i++) {// 循环读取选中行数据
							ids= ids + ","+ sczl_json[i].sczlmxid;
						}
						ids=ids.substr(1);
						$.ajax({
							type:'post',
							url: $("#arrivalGoodsEditForm input[name='urlPrefix']").val() + "/storehouse/produce/pagedataGetProduceInfo",
							cache: false,
							data: {"ids":ids, "access_token":$("#ac_tk").val()},
							dataType:'json',
							success:function(result){
								if(result.sczlglDtos != null && result.sczlglDtos.length > 0){
									let dhmxJson = "";
									if ($("#arrivalGoodsEditForm #dhmxJson").val()){
										dhmxJson = JSON.parse($("#arrivalGoodsEditForm #dhmxJson").val());
									}
									for (var i = 0; i < result.sczlglDtos.length; i++) {
										if (result.sczlglDtos[i].kyysl >0){
											var sz = {"sczlid":'',"sczlmxid":'',"zldh":'',"scph":'',"wlid":'',"wlbm":'',"wlmc":'',"gg":'',"jldw":'',"dhsl":'',"lbcsmc":'',"ychh":''};
											sz.sczlmxid = result.sczlglDtos[i].sczlmxid;
											sz.sczlid = result.sczlglDtos[i].sczlid;
											sz.zldh = result.sczlglDtos[i].zldh;
											sz.wlbm = result.sczlglDtos[i].wlbm;
											sz.wlmc = result.sczlglDtos[i].wlmc;
											sz.gg = result.sczlglDtos[i].gg;
											sz.scph = result.sczlglDtos[i].xlh;
											sz.jldw = result.sczlglDtos[i].jldw;
											sz.dhsl = result.sczlglDtos[i].kyysl;
											sz.kyysl = result.sczlglDtos[i].kyysl;
											if (null != dhmxJson && dhmxJson.length >0){
												for (let j = 0; j < dhmxJson.length; j++) {
													if (dhmxJson[j].sczlmxid ==  result.sczlglDtos[i].sczlmxid){
														sz.kyysl = result.sczlglDtos[i].kyysl*1 + dhmxJson[j].dhsl*1;
														sz.dhsl = sz.kyysl;
													}
												}
											}
											sz.lbcsmc = result.sczlglDtos[i].lbcsmc;
											sz.ychh = result.sczlglDtos[i].ychh;
											sz.wlid = result.sczlglDtos[i].wlid;
											if(!t_map.rows){
												t_map.rows= [];
											}
											t_map.rows.push(sz);
										}
									}

									$("#arrivalGoodsEditForm #tb_list").bootstrapTable('load',t_map);
								}
							}
						});
					}else{
						$("#arrivalGoodsEditForm #tb_list").bootstrapTable('load',t_map);

						$.closeModal(opts.modalName);
					}
				}else{
					$.alert("至少选择一条数据！");
					return false;
				}
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
var chooseWlglConfig = {
		width : "1600px",
		modalName	: "chooseHwxxModal",
		formName	: "chooseHwxxForm",
		offAtOnce : true, // 当数据提交成功，立刻关闭窗口
		buttons : {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					var $this = this;
					var opts = $this["options"]||{};
					if(rklbbj!=null && rklbbj=="1"){
						var sel_row = $('#dh_finish_formSearch #tb_list').bootstrapTable('getSelections');// 获取选择行数据
						var ids="";
		    			for (var i = 0; i < sel_row.length; i++) {// 循环读取选中行数据
		        			ids= ids + ","+ sel_row[i].wlid;
		        		}
		    			if(ids.length > 0){
		    				ids=ids.substr(1);
			    			// 查询货物信息并更新到页面
							$.post($('#arrivalGoodsEditForm #urlPrefix').val() + "/storehouse/arrivalGoods/pagedataGetListBywlid",{ids:ids,"access_token":$("#ac_tk").val()},function(data){
								var hwxxJson = [];
								//获取hwxx_json
								if($("#arrivalGoodsEditForm #hwxx_json").val()){
									hwxxJson = JSON.parse($("#arrivalGoodsEditForm #hwxx_json").val());
								}
								var hwxxDtos = data.rows;
								if(hwxxDtos != null && hwxxDtos.length > 0){
									// 更新页面列表(新增)
									for (var i = 0; i < hwxxDtos.length; i++) {
										if(i == 0){
											if(!t_map.rows){
												t_map.rows= [];
											}
										}
										t_map.rows.push(hwxxDtos[i]);				
										var sz = {"hwid":''};
										sz.hwid = hwxxDtos[i].hwid;
										hwxxJson.push(sz);
									}
										$("#arrivalGoodsEditForm #tb_list").bootstrapTable('load',t_map);
										$("#arrivalGoodsEditForm #hwxx_json").val(JSON.stringify(hwxxJson));
										$.closeModal(opts.modalName);
								}
			    			},'json');
						}else{
							$("#arrivalGoodsEditForm #tb_list").bootstrapTable('load',t_map);
							$("#arrivalGoodsEditForm #hwxx_json").val(JSON.stringify(hwxxJson));
							$.closeModal(opts.modalName);
						}							
		    		}else{
						$.alert("请先选择入库类别！");
						return false;
					}	
		    			
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
var chooseHtglConfig = {
		width : "1000px",
		modalName	: "chooseHtglModal",
		formName	: "chooseHtglForm",
		offAtOnce : true, // 当数据提交成功，立刻关闭窗口
		buttons : {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#chooseHtxxForm").valid()){
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					// 获取未更改合同单和明细信息
					var preJson = [];
					if($("#arrivalGoodsEditForm #dhmx_json").val()){
						preJson = JSON.parse($("#arrivalGoodsEditForm #dhmx_json").val());
					}
					// 获取更改后的合同单和明细信息
					var htmxJson = [];
					if($("#chooseHtxxForm #htmx_json").val()){
						htmxJson = JSON.parse($("#chooseHtxxForm #htmx_json").val());
						// 判断删除的合同明细信息
						for (var j = preJson.length-1; j >= 0; j--) {
							var isDel = true;
							var isHave = false;
							for (var i = 0; i < htmxJson.length; i++) {
								if(preJson[j].htid == htmxJson[i].htid){
									isHave = true;
								}
								if(preJson[j].htmxid == htmxJson[i].htmxid){
									isDel = false;
									break;
								}
													
							}
							if(isDel && isHave){
								// 根据明细ID直接更新页面列表(删除)
								for (var i = 0; i < t_map.rows.length; i++) {
									if(t_map.rows[i].htmxid == preJson[j].htmxid&&cskz3!='CGHZ'){
										preJson.splice(j,1);
										t_map.rows.splice(i,1);
										break;
									}
								}
							}
						}
						// 判断新增的合同明细信息
						var ids="";
						if(cskz3=='CGHZ') {
							for (var i = 0; i < htmxJson.length; i++) {
								ids = ids + "," + htmxJson[i].htmxid;
							}
						}else {
							for (var i = 0; i < htmxJson.length; i++) {
								var isAdd = true;
								for (var j = 0; j < preJson.length; j++) {
									if (preJson[j].htmxid == htmxJson[i].htmxid) {
										isAdd = false;
										break;
									}
								}
								if (isAdd) {
									ids = ids + "," + htmxJson[i].htmxid;
								}
							}
						}
						if(ids.length > 0){
							ids = ids.substr(1);
							var dhid = $("#arrivalGoodsEditForm #dhid").val();
							var url = "/contract/contract/pagedataGetcontractinfo";
							var cghzbj ='';
							if(cskz3=='CGHZ'){
								url = "/contract/contract/pagedataGetHzInfo";
							}
							var hwids = "";
							if (t_map.rows){
								for (var i = 0; i < t_map.rows.length; i++) {
									hwids = hwids+","+t_map.rows[i].hwid;
								}
							}
							if (hwids.length>0){
								hwids = hwids.substr(1);
							}
							// 查询信息并更新到页面
							$.post($('#arrivalGoodsEditForm #urlPrefix').val() + url,{"ids":ids, "dhid":dhid,"hwids":hwids, "access_token":$("#ac_tk").val()},function(data){
								var hwxxDtos = data.hwxxDtos;
								var htzbt = '';
								var htids = [];
								if(hwxxDtos != null && hwxxDtos.length > 0){
									// 更新页面列表(新增)
									for (var i = 0; i < hwxxDtos.length; i++) {
										if(i == 0){
											if(!t_map.rows){
												t_map.rows= [];
											}
											$("#arrivalGoodsEditForm #gysid").val(hwxxDtos[0].gysid);
											$("#arrivalGoodsEditForm #gysmc").val(hwxxDtos[0].gysmc);
											$("#editPutInStorageForm #bm").val(hwxxDtos[0].sqbm);
											$("#editPutInStorageForm #sqbmmc").val(hwxxDtos[0].sqbmmc);
										}
										if (hwxxDtos[i].htzbt!=null&&htids.indexOf(hwxxDtos[i].htid)==-1){
											htids.push(hwxxDtos[i].htid);
											if (htzbt==''){
												htzbt = hwxxDtos[i].htnbbh+":"+hwxxDtos[i].htzbt;
											}else {
												htzbt = htzbt + '//' +hwxxDtos[i].htnbbh+":"+ hwxxDtos[i].htzbt;
											}
										}
										hwxxDtos[i].htzbt = hwxxDtos[i].htnbbh+":"+ hwxxDtos[i].htzbt;
										t_map.rows.push(hwxxDtos[i]);
										var sz = {"htid":'',"htmxid":'',"qgmxid":''};
										sz.htid = hwxxDtos[i].htid;
										sz.htmxid = hwxxDtos[i].htmxid;
										sz.qgmxid = hwxxDtos[i].qgmxid;
										preJson.push(sz);
									}
								}
								var yhtzbz = $("#arrivalGoodsEditForm #htzbz").val();

								if (yhtzbz==''){
									yhtzbz = htzbt;
								}else {
									yhtzbz = yhtzbz + '/' + htzbt;
								}
								$("#arrivalGoodsEditForm #htzbz").val(yhtzbz)
								$("#arrivalGoodsEditForm #tb_list").bootstrapTable('load',t_map);
								$("#arrivalGoodsEditForm #dhmx_json").val(JSON.stringify(preJson));
								// 合同单号
								var selDjh = $("#chooseHtxxForm #yxdjh").tagsinput('items');
								$("#arrivalGoodsEditForm #htnbbhs").tagsinput({
					    			itemValue: "htid",
					    			itemText: "htnbbh",
					    		})
								for(var i = 0; i < selDjh.length; i++){
									var value = selDjh[i].value;
									var text = selDjh[i].text;
									$("#arrivalGoodsEditForm #htnbbhs").tagsinput('add', {"htid":value,"htnbbh":text});
								}
								$("#arrivalGoodsEditForm #dhmxJson").val(JSON.stringify(t_map.rows));
								$.closeModal(opts.modalName);
		    				},'json');
						}else{
							$("#arrivalGoodsEditForm #tb_list").bootstrapTable('load',t_map);
							$("#arrivalGoodsEditForm #dhmx_json").val(JSON.stringify(preJson));
							// 请购单号
							var selDjh = $("#chooseHtxxForm #yxdjh").tagsinput('items');
							$("#arrivalGoodsEditForm #htnbbhs").tagsinput({
				    			itemValue: "htid",
				    			itemText: "htnbbh",
				    		})
							for(var i = 0; i < selDjh.length; i++){
								var value = selDjh[i].value;
								var text = selDjh[i].text;
								$("#arrivalGoodsEditForm #htnbbhs").tagsinput('add', {"htid":value,"htnbbh":text});
							}
							$("#arrivalGoodsEditForm #dhmxJson").val(JSON.stringify(t_map.rows));
							preventResubmitForm(".modal-footer > button", true);
							$.closeModal(opts.modalName);
							preventResubmitForm(".modal-footer > button", false);
						}
					}else{
						$.alert("未获取到选中的合同信息！");
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
var tagsRemoved = $('#arrivalGoodsEditForm #htnbbhs').on('beforeItemRemove', function(event) {
	$.confirm('您确定要删除合同下的所有明细记录吗？',function(result){
		if(result){
			// 获取未更改请购单和明细信息
			var preJson = [];
			if($("#arrivalGoodsEditForm #dhmx_json").val()){
				preJson = JSON.parse($("#arrivalGoodsEditForm #dhmx_json").val());
				// 删除明细并刷新列表
				var htid = event.item.htid;
				var htids = [];
				var yhtzbz = $("#arrivalGoodsEditForm #htzbz").val();
				for (var i = t_map.rows.length-1; i >= 0 ; i--) {
					if(t_map.rows[i].htid == htid){
						if (htids.indexOf(htid)==-1){
							htids.push(htid);
							if (yhtzbz.indexOf('//'+t_map.rows[i].htzbt)==-1){
								yhtzbz = yhtzbz.replace(t_map.rows[i].htzbt,'');
							}else {
								yhtzbz =  yhtzbz.replace('//'+t_map.rows[i].htzbt,'');
							}
						}
						t_map.rows.splice(i,1);
						preJson.splice(i,1);
					}
				}
				$("#arrivalGoodsEditForm #htzbz").val(yhtzbz)
				$("#arrivalGoodsEditForm #dhmx_json").val(JSON.stringify(preJson));
				$("#arrivalGoodsEditForm #tb_list").bootstrapTable('load',t_map);
				$("#arrivalGoodsEditForm #dhmxJson").val(JSON.stringify(t_map.rows));
			}
		}
	})
});
/**
 * 监听单据号标签移除事件
 */
var tagsRemoved = $('#arrivalGoodsEditForm #lldhs').on('beforeItemRemove', function(event) {
	$.confirm('您确定要删除领料单号下的所有明细记录吗？',function(result){
		if(result){
			var preJson = [];
			if($("#arrivalGoodsEditForm #hwxx_json").val()){
				preJson = JSON.parse($("#arrivalGoodsEditForm #hwxx_json").val());
				// 删除明细并刷新列表
				var llid = event.item.llid;
				for (var i = t_map.rows.length-1; i >= 0 ; i--) {
					if(t_map.rows[i].llid == llid){
						t_map.rows.splice(i,1);
						preJson.splice(i,1);
					}
				}
				$("#arrivalGoodsEditForm #hwxx_json").val(JSON.stringify(preJson));
				$("#arrivalGoodsEditForm #tb_list").bootstrapTable('load',t_map);
			}
		}
	})
});
/**
 * 监听标签点击事件
 */
var tagClick = $("#arrivalGoodsEditForm #ll").on('click','.label-info',function(e){
	event.stopPropagation();
	var url = xtszqz + "/storehouse/receiveMateriel/pagedataChooseLlmx?access_token=" + $("#ac_tk").val() + "&llid=" + e.currentTarget.dataset.logo;
	$.showDialog(url, '查看领料明细', viewLlmxConfig);
});
var viewLlmxConfig = {
	width : "1200px",
	modalName	: "chooseLlmxModal",
	formName	: "chooseLlmxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
/**
 * 选择供应商列表
 * @returns
 */
function chooseGys(){
	var url=$('#arrivalGoodsEditForm #urlPrefix').val() + "/warehouse/supplier/pagedataSupplier?access_token=" + $("#ac_tk").val()+"&scbj="+"0";
	$.showDialog(url,'选择供应商',addGysConfig);	
}
var addGysConfig = {
	width		: "1200px",
	modalName	:"addGysModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row=$('#choosesupplier_formSearch #tb_list').bootstrapTable('getSelections');
				if(sel_row.length == 1){
					var gysid = sel_row[0].gysid;
					var gysmc = sel_row[0].gysmc;
					var sl = sel_row[0].sl;
					$("#gysmc").val(gysmc);
					$("#gysid").val(gysid);
					$("#sl").val(sl);
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
 * 初始化合同单号
 * @returns
 */
function inithtnbbh(){
	var htids = $("#arrivalGoodsEditForm #htids").val();
	if(!htids || htids.length < 0){
		return;
	}
	$.ajax({ 
	    type:'post',  
	    url:$('#arrivalGoodsEditForm #urlPrefix').val()+"/contract/contract/pagedataGetContractList", 
	    cache: false,
	    data: {"ids":htids,"access_token":$("#ac_tk").val()},  
	    dataType:'json',
	    success:function(result){
	    	//返回值
	    	var htglDtos = result.htglDtos;
	    	if(htglDtos){
	    		$("#arrivalGoodsEditForm #htnbbhs").tagsinput({
	    			itemValue: "htid",
	    			itemText: "htnbbh",
	    		})
	    		for(var i = 0; i < htglDtos.length; i++){
		    		$("#arrivalGoodsEditForm #htnbbhs").tagsinput('add',{"htid": htglDtos[i].htid, "htnbbh": htglDtos[i].htnbbh});
		    	}
	    	}
	    }
	});
}

function btnBind(){
	var sel_ck=$("#arrivalGoodsEditForm #ckid");
	//仓库下拉框事件
	sel_ck.unbind("change").change(function(){
		ckEvent();
	});
}

//仓库下拉事件
function ckEvent(){
	var ckid=$("#arrivalGoodsEditForm #ckid").val();
	$.ajax({ 
	    type:'post',  
	    url:$('#arrivalGoodsEditForm #urlPrefix').val()+"/storehouse/arrivalGoods/pagedataGetkwlist", 
	    cache: false,
	    data: {"ckid":ckid,"access_token":$("#ac_tk").val()},
	    dataType:'json', 
	    success:function(data){
	    	if(data.kwlist!=null && data.kwlist.length>0){
	    		kwlist=data.kwlist;
	    		if(t_map.rows!=null && t_map.rows.length>0){
	    			for(var i=0;i<t_map.rows.length;i++){
						t_map.rows[i].cskw="";
						t_map.rows[i].kwbh="";
					}
				}
	    		$("#arrivalGoodsEditForm #tb_list").bootstrapTable('load',t_map);
	    	}else{
	    		$.confirm("该仓库下库位信息为空，请先进行维护！");
				$("#arrivalGoodsEditForm #ckid #ckid_0").prop("selected","selected");
				$("#arrivalGoodsEditForm #ckid #ckid_0").trigger("chosen:updated");
				kwlist = null;
				if(t_map.rows!=null && t_map.rows.length>0){
					for(var i=0;i<t_map.rows.length;i++){
						t_map.rows[i].cskw="";
						t_map.rows[i].kwbh="";
					}
				}
				$("#arrivalGoodsEditForm #tb_list").bootstrapTable('load',t_map);
	    	}
	    }
	});
}

function judgment(){
	if(kwlist==null || kwlist.length<=0) {
		$.confirm("请先选择仓库！");
	}
}

/**
 * 根据物料名称模糊查询
 */
$('#arrivalGoodsEditForm #addnr').typeahead({
	source : function(query, process) {
		return $.ajax({
			url : $('#arrivalGoodsEditForm #urlPrefix').val()+'/progress/progress/pagedataQueryWlxx',
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
		$('#arrivalGoodsEditForm #addwlid').attr('value', item.id);
		return item.name;
	}
});

/**
 * 回车键添加物料至请购车
 * @param event
 * @returns
 */
var addkeydown = $("#addnr").bind("keydown",function(event){
	var wlid=$('#arrivalGoodsEditForm #addwlid').val();
	if (event.keyCode == "13") {
		//判断新增输入框是否有内容
		if(!$('#arrivalGoodsEditForm #addnr').val()){
			return false;
		}
		setTimeout(function(){ if(wlid != null && wlid != ''){
			$.ajax({
				type:'post',
				url:$('#arrivalGoodsEditForm #urlPrefix').val()+"/progress/progress/pagedataQueryWlxx",
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
						$("#arrivalGoodsEditForm #tb_list").bootstrapTable('load',t_map);
						$('#arrivalGoodsEditForm #addwlid').val("");
						$('#arrivalGoodsEditForm #addnr').val("");

					}else{
						$.confirm("该物料不存在!");
					}
				}
			});
		}else{
			var addnr = $('#arrivalGoodsEditForm #addnr').val();
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
	var wlid=$('#arrivalGoodsEditForm #addwlid').val();
	//判断新增输入框是否有内容
	if(!$('#arrivalGoodsEditForm #addnr').val()){
		return false;
	}
	setTimeout(function(){ if(wlid != null && wlid != ''){
		$.ajax({
			type:'post',
			url:$('#arrivalGoodsEditForm #urlPrefix').val()+"/progress/progress/pagedataQueryWlxx",
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
					$("#arrivalGoodsEditForm #tb_list").bootstrapTable('load',t_map);
					$('#arrivalGoodsEditForm #addwlid').val("");
					$('#arrivalGoodsEditForm #addnr').val("");
				}else{
					$.confirm("该物料不存在!");
				}
			}
		});
	}else{
		var addnr = $('#arrivalGoodsEditForm #addnr').val();
		if(addnr != null && addnr != ''){
			$.confirm("请选择物料信息!");
		}
	}}, '200')
}


/**
 * 选择申请部门列表
 * @returns
 */
function chooseSqbm(){
	var url=$('#arrivalGoodsEditForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择申请部门',addSqbmConfig);	
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
					$('#arrivalGoodsEditForm #bm').val(sel_row[0].jgid);
					$('#arrivalGoodsEditForm #sqbmmc').val(sel_row[0].jgmc);
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
		var url=$('#arrivalGoodsEditForm #urlPrefix').val()+"/ws/sjxxpripreview?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= $('#arrivalGoodsEditForm #urlPrefix').val()+"/common/file/pdfPreview?fjid=" + fjid;
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
 * 点击文件下载
 * @param fjid
 * @returns
 */
function xz(fjid){
	jQuery('<form action="'+$('#arrivalGoodsEditForm #urlPrefix').val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
		'<input type="text" name="fjid" value="'+fjid+'"/>' +
		'<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
		'</form>')
		.appendTo('body').submit().remove();
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
			var url= $('#arrivalGoodsEditForm #urlPrefix').val()+"/common/file/delFile";
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
 * 附件上传回调
 * @param fjid
 * @returns
 */
function displayUpInfo(fjid){
	if(!$("#arrivalGoodsEditForm #fjids").val()){
		$("#arrivalGoodsEditForm #fjids").val(fjid);
	}else{
		$("#arrivalGoodsEditForm #fjids").val($("#arrivalGoodsEditForm #fjids").val()+","+fjid);
	}
}
/**
 * 选择领料信息
 * @returns
 */
function qtrk_chooseLldh (){
	var url = xtszqz + "/storehouse/receiveMateriel/pagedataChooseLlxx?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择领料信息', chooseLlxxConfig);
}
var chooseLlxxConfig = {
	width : "1000px",
	modalName	: "chooseLlxxModal",
	formName	: "chooseLlxxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                // 获取未更改请购单和明细信息
                var preJson = [];
                if($("#arrivalGoodsEditForm #hwxx_json").val()){
                    preJson = JSON.parse($("#arrivalGoodsEditForm #hwxx_json").val());
                }
                // 获取更改后的请购单和明细信息
                var llmxJson = [];
                if($("#chooseLlxxForm #llmx_json").val()){
                    llmxJson = JSON.parse($("#chooseLlxxForm #llmx_json").val());
                    // 判断新增的请购明细信息
                    var ids="";
                    for (var i = 0; i < llmxJson.length; i++) {
                        var isAdd = true;
                        for (var j = 0; j < preJson.length; j++) {
                            if(preJson[j].llmxid == llmxJson[i].llmxid){
                                isAdd = false;
                                break;
                            }
                        }
                        if(isAdd){
                            ids = ids + ","+ llmxJson[i].llmxid;
                        }
                    }
                    if(ids.length > 0){
                        ids = ids.substr(1);
                        // 查询信息并更新到页面
                        $.post(xtszqz + "/storehouse/receiveMateriel/pagedataGetHwllmx",{"llmxids":ids,"access_token":$("#ac_tk").val()},function(data){
                            var hwllmxDtos = data.hwllmxDtos;
                            //已存在的领料单号
                            var yczlldh = []
                            if(hwllmxDtos != null && hwllmxDtos.length > 0){
                                if(!t_map.rows){
                                    t_map.rows= [];
                                }
                                for (var i = 0; i < t_map.rows.length; i++) {
                                    if (t_map.rows[i].llid){
                                        yczlldh.push(t_map.rows[i].llid);
                                    }
                                }
                                // 更新页面列表(新增)
                                for (var i = 0; i < hwllmxDtos.length; i++) {
                                    if(hwllmxDtos[i].slsl > 0 && hwllmxDtos[i].slsl != null && hwllmxDtos[i].slsl != ''){
                                        var isRepeat = false;
                                        for(var repeatIndex = 0; repeatIndex < t_map.rows.length; repeatIndex++) {
                                            if(t_map.rows[repeatIndex].llmxid==hwllmxDtos[i].llmxid){
                                                isRepeat = true;
                                            }
                                        }
                                        if(!isRepeat){
                                            t_map.rows.push(hwllmxDtos[i]);
                                            var sz = {"llid":'',"llmxid":''};
                                            sz.llid = hwllmxDtos[i].llid;
                                            sz.llmxid = hwllmxDtos[i].llmxid;
                                            preJson.push(sz);
                                        }
                                    }
                                }
                            }
                            $("#arrivalGoodsEditForm #tb_list").bootstrapTable('load',t_map);
                            $("#arrivalGoodsEditForm #hwxx_json").val(JSON.stringify(preJson));
                            // 领料单号
                            var selLldh = $("#chooseLlxxForm #yxlldh").tagsinput('items');
                            for(let i = yczlldh.length-1; i >=0 ; i--){
                                for(let j = selLldh.length-1; j >=0 ; j--){
                                    if (yczlldh[i] == selLldh[j].value){
										selLldh.splice(j,1);
                                        j--;
                                    }
                                }
                            }
							$("#arrivalGoodsEditForm  #lldhs").tagsinput({
								itemValue: "llid",
								itemText: "lldh",
							})
                            for(var i = 0; i < selLldh.length; i++){
                                var value = selLldh[i].value;
                                var text = selLldh[i].text;
                                $("#arrivalGoodsEditForm #lldhs").tagsinput('add', {"llid":value,"lldh":text});
                            }
                            $.closeModal(opts.modalName);
                        },'json');
                    }else{
                        $("#arrivalGoodsEditForm #tb_list").bootstrapTable('load',t_map);
                        $("#arrivalGoodsEditForm #hwxx_json").val(JSON.stringify(preJson));
                        // 领料单号
                        var selLldh = $("#chooseLlxxForm #yxlldh").tagsinput('items');
						$("#arrivalGoodsEditForm  #lldhs").tagsinput({
							itemValue: "llid",
							itemText: "lldh",
						})
                        for(var i = 0; i < selLldh.length; i++){
                            var value = selLldh[i].value;
                            var text = selLldh[i].text;
                            $("#arrivalGoodsEditForm #lldhs").tagsinput('add', {"llid":value,"lldh":text});
                        }
                        $.closeModal(opts.modalName);
                    }
                }else{
                    $.alert("添加领料明细失败！");
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
	if (dhlxdm=='TH'){
		$('#arrivalGoodsEditForm #ckid').attr("disabled", true);
	}
	btnBind();
	checkrklx();
	//添加日期控件
	laydate.render({
	   elem: '#arrivalGoodsEditForm #dhrq'
	  ,theme: '#2381E9'
	});
	var oFileInput = new FileInput();
	var sign_params=[];
	sign_params.prefix=$('#arrivalGoodsEditForm #urlPrefix').val();
	oFileInput.Init("arrivalGoodsEditForm","displayUpInfo",2,1,"pro_file",null,sign_params);
	//初始化列表
	var oTable=new arrivalGoodsEdit_TableInit();
	oTable.Init();
	// 初始化页面
	inithtnbbh();
	//所有下拉框添加choose样式
	jQuery('#arrivalGoodsEditForm #ck .chosen-select').chosen({width: '100%'});
});