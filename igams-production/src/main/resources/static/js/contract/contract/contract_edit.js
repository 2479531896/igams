var t_map = [];
var htlx = $('#editContractForm #htlx').val();
var yhtbchtFlag = $('#editContractForm #yhtbchtFlag').val()=='1'?true:false;
// 显示字段
var columnsArray = [
	{
    	title: '序号',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序号',
		width: '3%',
        align: 'left',
        visible:true
    }, {
        field: 'htmxid',
        title: '合同明细ID',
        titleTooltip:'合同明细ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'mxid',
        title: '明细ID',
        titleTooltip:'明细ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'qgid',
        title: '请购ID',
        titleTooltip:'请购ID',
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
        field: 'djh',
        title: '请购单号/说明',
        titleTooltip:'请购单号/说明',
        width: '8%',
        align: 'left',
        formatter:djhformat,
        visible: htlx=='3'?false:true
    }, {
        field: 'qglbmc',
        title: '类别',
        titleTooltip:'请购类别',
        width: '3%',
        align: 'left',
        visible: htlx=='3'?false:true
    }, {
        field: 'wlbm',
        title: '物料信息',
        titleTooltip:'物料信息',
        width: '12%',
        align: 'left',
        formatter:wlbmformat,
        visible: true
    }
    ,{
        field: 'gg',
        title: '规格/生产商',
        titleTooltip:'规格/生产商',
        formatter:ggscsformat,
        width: '6%',
        align: 'left',
        visible: htlx=='3'?true:false
    }
    , {
        field: 'sl',
        title: '数量',
        titleTooltip:'数量',
        width: '4%',
        align: 'left',
        formatter:yhtbchtFlag?bcslformat:slformat,
        visible: htlx=='3'?false:true
    }, {
        field: 'presl',
        title: '原数量',
        titleTooltip:'原数量',
        width: '6%',
        align: 'left',
        visible: false
    }, {
        field: 'glid',
        title: '关联ID',
        titleTooltip:'关联ID',
        width: '6%',
        align: 'left',
        visible: false
    }, {
        field: 'jldw',
        title: '单位',
        titleTooltip:'单位',
        formatter:jldwformat,
        width: '3%',
        align: 'left',
        visible: true
    }, {
        field: 'hsdj',
        title: '含税单价',
        titleTooltip:'含税单价',
        width: '5%',
        align: 'left',
        formatter:hsdjformat,
        visible: true
    }, {
        field: 'dhzq',
        title: '到货周期',
        titleTooltip:'到货周期',
        width: '6%',
        align: 'left',
        formatter:dhzqformat,
        visible:  htlx=='3'?true:false
    },{
        field: 'hjje',
        title: '合计金额',
        titleTooltip:'合计金额',
        width: '5%',
        align: 'left',
        formatter:hjjeformat,
        visible: htlx=='3'?false:true
    },{
        field: 'jhdhrq',
        title: '计划到货日期',
        titleTooltip:'计划到货日期',
        width: '6%',
        align: 'left',
        formatter:jhdhrqformat,
        visible: htlx=='3'?false:true
    },{
        field: 'ccdg',
        title: '初次订购',
        titleTooltip:'初次订购',
        width: '6%',
        align: 'left',
        formatter:sfccdgformat,
        visible: true
    },{
        field: 'wlfl',
        title: '物料分类',
        titleTooltip:'物料分类',
        width: '8%',
        align: 'left',
        formatter:wlflformat,
        visible: true
    },{
        field: 'cpzch',
        title: '产品注册号',
        titleTooltip:'产品注册号',
        width: '8%',
        align: 'left',
        formatter:cpzchformat,
        visible: true
    },{
        field: 'bxsj',
        title: '保修时间',
        titleTooltip:'保修时间',
        width: '5%',
        align: 'left',
        formatter:bxsjformat,
        visible: true
    },{
        field: 'sfgdzc',
        title: '是否固定资产',
        titleTooltip:'是否固定资产',
        width: '6%',
        align: 'left',
        formatter:sfgdzcformat,
        visible: true
    },{
        field: 'bz',
        title: '备注',
        titleTooltip:'备注',
        width: '8%',
        align: 'left',
        formatter:bzformat,
        visible: true
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '8%',
        align: 'left',
        formatter:czformat,
        visible: true
    }];
var contractEdit_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#editContractForm #tb_list').bootstrapTable({
            url: $('#editContractForm #urlPrefix').val()+'/contract/contract/pagedataGetContractDetailList',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#editContractForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "xh",				//排序字段
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
            uniqueId: "htmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
            	t_map = map;
            	if(t_map.rows){
            		// 初始化htmx_json
            		var json = [];
            		for (var i = 0; i < t_map.rows.length; i++) {
            			var sz = {"qgid":'',"qgmxid":''};
						sz.qgid = t_map.rows[i].qgid;
						sz.qgmxid = t_map.rows[i].qgmxid;
						json.push(sz);
    				}
            		$("#editContractForm #htmx_json").val(JSON.stringify(json));
            	}
            },
            onLoadError: function () {
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
            htid: $("#editContractForm #htid").val(),
            //搜索框使用
            //search:params.search
        };
    	return map;
    };
    return oTableInit;
};

//单位格式化
function jldwformat(value,row,index){
    if (htlx=='3'&&row.fwbj=='1'){
        return "";
    }
    var jldw='';
    var title='';
    if(row.jldw!=null&&row.jldw!=''){
        jldw=row.jldw;
        title=row.jldw;
    }else if(row.hwjldw!=null&&row.hwjldw!=''){
        jldw=row.hwjldw;
        title=row.hwjldw;
    }
    var html="<span style='width:70%;line-height:24px;' title='"+title+"'>"+jldw+"</span>";
    return html;
}

//规格/生产商格式化
function ggscsformat(value,row,index){
    if (htlx=='3'&&row.fwbj=='1'){
        return "";
    }
    var gg=row.gg;
    var title=row.gg;
    if(gg==null || gg ==''){
        gg=row.hwbz;
        title=row.hwbz;
    }
    var html="<span style='width:70%;line-height:24px;' title='"+title+"'>"+gg+"</span>";
    html+="<br/>";
    html+="<span style='width:70%;line-height:24px;' title='"+title+"'>"+row.scs+"</span>";
    return html;
}
//规格/货物标准格式化
function ggformat(value,row,index){
    var gg=row.gg;
    var title=row.gg;
    if(row.qglbdm!='MATERIAL' && row.qglbdm!='OUTSOURCE' && row.qglbdm!=null && row.qglbdm!='' ){
        gg=row.hwbz;
        title=row.hwbz;
    }
    var html="<span style='width:70%;line-height:24px;' title='"+title+"'>"+gg+"</span>";
    return html;
}
/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wlbmformat(value,row,index){
    var hwmc=row.wlmc;
    var qglbdm=row.qglbdm;
    var title=row.wlmc;
    if(row.qglbdm!='DEVICE' && row.qglbdm!='MATERIAL' && row.qglbdm!='OUTSOURCE' && qglbdm!=null && qglbdm!=''){
        hwmc=row.hwmc;
        title=row.hwmc;
    }
    if(row.qgbz !=null && row.qgbz!=''){
        title=row.qgbz;
    }
	var html="";
	if(row.wlbm!=null && row.wlbm!=''){
		html="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a><button type='button' class='glyphicon glyphicon-chevron-up' style='width:30%;font-weight:100;float:right;width:28px;height:24px;border-radius:5px;border:0.5px solid black;' onclick=\"showImg(\'echarts_qggl\','"+row.wlid+"',\'wlbm\')\"></button>"+"<span style='width:70%;line-height:24px;' title='"+title+"'>/"+hwmc+"</span>";
	}else{
        html="<button type='button' class='glyphicon glyphicon-chevron-up' style='width:30%;font-weight:100;float:right;width:28px;height:24px;border-radius:5px;border:0.5px solid black;' onclick=\"showImg(\'echarts_qggl\','"+row.wlid+"',\'wlbm\')\"></button>"+"<span style='width:70%;line-height:24px;' title='"+title+"'>/"+hwmc+"</span>";
	}
	if(row.qglbdm!='DEVICE' && row.qglbdm!='MATERIAL' && row.qglbdm!='OUTSOURCE' && row.qglbdm!=null && row.qglbdm!=''){
		row.wlbm="/";
	}
    if (htlx != '3') {
        var gg = row.gg;
        var ggtitle = row.gg;
        if (gg == null || gg == '') {
            gg = row.hwbz;
            ggtitle = row.hwbz;
        }
        html += "<br/>";
        html += "<span style='width:70%;line-height:24px;' title='" + ggtitle + "'>" + gg + "</span>";
    }
    if (htlx == '3'){
        if (row.fwbj&&row.fwbj=='1'){
            var fwhtml = "<input id='hwmc_"+index+"' title='"+row.hwmc+"' value='"+row.hwmc+"' name='hwmc_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{required:true}' onblur=\"checkSum('"+index+"',this,\'hwmc\')\"/>";
            return fwhtml;
        }
    }
	return html;
}
function queryByWlbm(wlid){
	var url=$("#editContractForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
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
    var sl = 0;
    var sysl = 0;
    var ydsl = 0;
    var presl = 0;
    if(row.sl){
        sl = row.sl;
    }
    if(row.presl){
        presl = row.presl;
    }
    if(row.sysl){
        sysl = row.sysl;
    }
    if(row.ydsl){
        ydsl = row.ydsl;
    }
    var xssl = (parseInt(sysl*100)-parseInt(ydsl*100)+parseInt(presl*100))/100;
    if (!row.sl){
        row.sl = xssl;
    }
    var html =""
    if(row.qgmxid){
        if((parseInt(row.sl*100) - parseInt(xssl*100))/100  > 0){
            html ="<div id='sldiv_"+index+"' name='sldiv' isBeyond='false' style='background:orange;'>"
        }else if (row.sl==0){
            html ="<div id='sldiv_"+index+"' name='sldiv' isBeyond='true' style='background:darkcyan;'>"
        }else if ((parseInt(row.sl*100) - parseInt(xssl*100))/100  <0){
            html ="<div id='sldiv_"+index+"' name='sldiv' isBeyond='true' style='background:yellow;'>"
        }else if ((parseInt(row.sl*100) - parseInt(xssl*100))/100  ==0){
            html ="<div id='sldiv_"+index+"' name='sldiv' isBeyond='true' style='background:darkcyan;'>"
        }
    }else{
        if((parseInt(row.sl*100)/100).toFixed(2) > 999999){
            html ="<div id='sldiv_"+index+"' name='sldiv' isBeyond='false' style='background:orange;'>"
        }else if ((parseInt(row.sl*100)/100).toFixed(2) > 999999){
            html ="<div id='sldiv_"+index+"' name='sldiv' isBeyond='true' style='background:yellow;'>"
        }
        else
            html ="<div id='sldiv_"+index+"' name='sldiv' isBeyond='true' style='background:darkcyan;'>"
    }
    html += "<input id='sl_"+index+"' value='"+row.sl+"' name='sl_"+index+"' style='width:100%;border:1px solid #D5D5D5;' validate='{slNumber:true}' onblur=\"checkSum('"+index+"',this,\'sl\')\"></input>";
    if(row.qgmxid){
        html += "<p id='xssl_"+index+"' style='font-size:14px;width:100%;margin-left:3px;'>/"+xssl+"</p>";
    }else{
        html += "<p id='xssl_"+index+"' style='font-size:14px;width:100%;margin-left:3px;'>/999999</p>";
    }
    html += "</div>"
    return html;
}



/**
 * 补充合同的数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function bcslformat(value,row,index){
    if(row.sl == null){
        row.sl = "";
    }
    var html = "<input id='bcsl_"+index+"' value='"+row.sl+"' name='bcsl_"+index+"' style='width:70%;border-top-left-radius:5px;border-bottom-left-radius:5px;border:1px solid #D5D5D5;' validate='{djNumber:true}' onblur=\"checkSum('"+index+"',this,\'bcsl\')\"></input><button type='button' class='glyphicon glyphicon-chevron-up' style='width:30%;top:0;font-weight:100;float:right;height:24px;border-top-right-radius:5px;border-bottom-right-radius:5px;border:0.2px solid black;' onclick=\"showImg(\'echarts_qggl\','"+row.wlid+"',\'hsdj\',800,400)\"></button>";
    return html;
}/**
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
    var html = "<input id='hsdj_"+index+"' value='"+row.hsdj+"' name='hsdj_"+index+"' style='width:70%;border-top-left-radius:5px;border-bottom-left-radius:5px;border:1px solid #D5D5D5;' validate='{djNumber:true}' onblur=\"checkSum('"+index+"',this,\'hsdj\')\"></input><button type='button' class='glyphicon glyphicon-chevron-up' style='width:30%;top:0;font-weight:100;float:right;height:24px;border-top-right-radius:5px;border-bottom-right-radius:5px;border:0.2px solid black;' onclick=\"showImg(\'echarts_qggl\','"+row.wlid+"',\'hsdj\',800,400)\"></button>";
    return html;
}
/**
 * 含税单价格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function dhzqformat(value,row,index){
    if(row.dhzq == null){
        row.dhzq = "";
    }
    var html = "<input id='dhzq_"+index+"' value='"+row.dhzq+"' name='dhzq_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{required:true}' onblur=\"checkSum('"+index+"',this,\'dhzq\')\"/>";
    return html;
}
/**
 * 单据号/说明
 * @param value
 * @param row
 * @param index
 * @returns
 */
function djhformat(value,row,index){
    if(row.djh == null){
        row.djh = "";
    }
    if(row.qgbz == null){
        row.qgbz = "";
    }
    var html = "<span title='"+row.djh+row.qgbz+"'>"+row.djh+"</span></br><span title='"+row.djh+row.qgbz+"'>"+row.qgbz+"</span>";
    return html;
}

/**
 * 合计金额格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function hjjeformat(value,row,index){
    if(row.hjje == null){
        row.hjje = "";
    }
    var html = "<input id='hjje_"+index+"' value='"+row.hjje+"' name='hjje_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{hjNumber:true}' onblur=\"checkSum('"+index+"',this,\'hjje\')\"></input>";
    return html;
}

/**
 * 计划到货日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function jhdhrqformat(value,row,index){
    if(row.jhdhrq == null){
        row.jhdhrq = "";
    }
    var html = "<input id='jhdhrq_"+index+"' value='"+row.jhdhrq+"' name='jhdhrq_"+index+"' style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' validate='{jhdhrqReq:true}' onchange=\"checkSum('"+index+"',this,\'jhdhrq\')\"><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='复制当前日期并替换所有'  onclick=\"copyTime('"+index+"')\"><span></input>";
    setTimeout(function() {
        laydate.render({
            elem: '#editContractForm #jhdhrq_'+index
            ,theme: '#2381E9'
            ,min: 1
            ,btns: ['clear', 'confirm']
            ,done: function(value, date, endDate){
                t_map.rows[index].jhdhrq = value;
            }
        });
    }, 100);
    return html;
}
/**
 * 复制并替换全部
 * @param wlid
 * @returns
 */
function copyCcdg(index){
    var ccdg=$("#editContractForm #ccdg_"+index).val();
    if(ccdg==null || ccdg==""){
        $.confirm("请先选择初次订购！");
    }else{
        for(var i=0;i<t_map.rows.length;i++){
            $("#editContractForm #ccdg_"+i).val(ccdg);
        }
    }
    var wlfls = $("#editContractForm #wlfls").val();
    var ywlfllist = JSON.parse(wlfls);
    var wlfllist = [];
    for (var i = 0; i < ywlfllist.length; i++) {
        if (ywlfllist[i].cskz1 == ccdg){
            wlfllist.push(ywlfllist[i]);
        }
    }
    var data= [];
    if (htlx =='3'){
        data = JSON.parse($("#editContractForm #kjht_json").val());
    }else {
        data = JSON.parse($("#editContractForm #htmx_json").val());
    }
    if(data!=null && data.length>0){
        for(var j=0;j<data.length;j++){
            data[j].ccdg=ccdg;
            t_map.rows[j].ccdg=ccdg;
            var zlbHtml = "";
            zlbHtml += "<option value=''>--请选择--</option>";
            $.each(wlfllist,function(i){
                zlbHtml += "<option value='" + wlfllist[i].csid + "' id='"+wlfllist[i].csid+"' wlflmc='"+wlfllist[i].csmc+"' >" + wlfllist[i].csmc + "</option>";
            });
            $("#editContractForm #wlfl_"+j).empty();
            $("#editContractForm #wlfl_"+j).append(zlbHtml);
            $("#editContractForm #wlfl_"+j).trigger("chosen:updated");
        }
    }
    if (htlx =='3'){
        $("#editContractForm #kjht_json").val(JSON.stringify(data));
    }else {
        $("#editContractForm #htmx_json").val(JSON.stringify(data));
    }

}
/**
 * 复制当前日期并替换全部
 * @param wlid
 * @returns
 */
function copyTime(index){
    var dqrq=$("#editContractForm #jhdhrq_"+index).val();
    if(dqrq==null || dqrq==""){
        $.confirm("请先选择日期！");
    }else{
        for(var i=0;i<t_map.rows.length;i++){
            $("#editContractForm #jhdhrq_"+i).val(dqrq);
        }
    }
    var data= [];
    if (htlx =='3'){
        data = JSON.parse($("#editContractForm #kjht_json").val());
    }else {
        data = JSON.parse($("#editContractForm #htmx_json").val());
    }
    if(data!=null && data.length>0){
        for(var j=0;j<data.length;j++){
            data[j].jhdhrq=dqrq;
            t_map.rows[j].jhdhrq=dqrq;
        }
    }
    if (htlx =='3'){
        $("#editContractForm #kjht_json").val(JSON.stringify(data));
    }else {
        $("#editContractForm #htmx_json").val(JSON.stringify(data));
    }
}
/**
 * 复制当前物料分类并替换全部
 * @param wlid
 * @returns
 */
function copyWlfl(index){
    var wlfl =$("#editContractForm #wlfl_"+index).val();
    if(wlfl==null || wlfl==""){
        $.confirm("请先选择物料分类！");
    }else{
        for(var i=0;i<t_map.rows.length;i++){
            $("#editContractForm #wlfl_"+i).val(wlfl);
        }
    }
    var data=[];
    if (htlx =='3'){
        data = JSON.parse($("#editContractForm #kjht_json").val());
    }else {
        data = JSON.parse($("#editContractForm #htmx_json").val());
    }
    if(data!=null && data.length>0){
        for(var j=0;j<data.length;j++){
            data[j].wlfl=wlfl;
            t_map.rows[j].wlfl=wlfl;
        }
    }
    if (htlx =='3'){
        $("#editContractForm #kjht_json").val(JSON.stringify(data));
    }else {
        $("#editContractForm #htmx_json").val(JSON.stringify(data));
    }
}

/**
 * 是否固定资产格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function sfgdzcformat(value,row,index){
    if (row.lbcskz1 == '3'){
        var html="";
        html +=  "<select id='sfgdzc_"+index+"' name='sfgdzc_"+index+"' class='form-control chosen-select'  onchange=\"checkSum('"+index+"',this,\'sfgdzc\')\">";
        if(row.sfgdzc=='1'){
            html += "<option value=''>请选择--</option>";
            html += "<option value='1' selected>是</option>";
            html += "<option value='0'>否</option>";
        }else if(row.sfgdzc=='0'){
            html += "<option value=''>请选择--</option>";
            html += "<option value='1' >是</option>";
            html += "<option value='0' selected>否</option>";
        }else{
            html += "<option value=''>请选择--</option>";
            html += "<option value='1' >是</option>";
            html += "<option value='0' >否</option>";
        }
        html += "</select>";
        return html;
    }
}
/**
 * 是否初次订购
 * @returns
 */
function sfccdgformat(value,row,index) {
    var html="";
    html +=  "<select style='width: 85%;display:inline-block' id='ccdg_"+index+"' name='ccdg_"+index+"' validate='{required:true}' class='form-control chosen-select'  onchange=\"checkCcdgWlfl('"+index+"',this,\'ccdg\')\">";
    if(row.ccdg=='1'){
        html += "<option value=''>请选择--</option>";
        html += "<option value='1' selected>是</option>";
        html += "<option value='0'>否</option>";
    }else if(row.ccdg=='0'){
        html += "<option value=''>请选择--</option>";
        html += "<option value='1' >是</option>";
        html += "<option value='0' selected>否</option>";
    }else{
        html += "<option value=''>请选择--</option>";
        html += "<option value='1' >是</option>";
        html += "<option value='0' >否</option>";
    }
    html += "</select><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='复制并替换所有'  onclick=\"copyCcdg('"+index+"')\"></span>";
    return html;
}
/**
 * 物料分类
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wlflformat(value,row,index){
    var ccdg = row.ccdg;
    var wlfl = row.wlfl;
    var wlfls = $("#editContractForm #wlfls").val();
    var ywlfllist = JSON.parse(wlfls);
    var wlfllist = [];
    for (var i = 0; i < ywlfllist.length; i++) {
        if (ywlfllist[i].cskz1 == ccdg){
            wlfllist.push(ywlfllist[i]);
        }
    }
    var html="<div id='wlfldiv_"+index+"' name='wlfldiv' style='width:85%;display:inline-block'>";
    html+="<select  id='wlfl_"+index+"' name='wlfl_"+index+"' onchange=\"checkCcdgWlfl('"+index+"',this,\'wlfl\')\"  class='form-control chosen-select' style='padding:0px;display:inline-block'>";
    if(wlfllist!=null && wlfllist.length>0){
        html += "<option value=''>--请选择--</option>";
        for(var i = 0; i < wlfllist.length; i++) {
            html += "<option id='"+wlfllist[i].csid+"' value='"+wlfllist[i].csid+"'";
            if(wlfl!=null && wlfl!=""){
                if(wlfl==wlfllist[i].csid){
                    html += "selected"
                }
            }
            html += ">"+wlfllist[i].csmc+"</option>";
        }
    }else{
        html+="<option>-请选择-</option>";
    }
    html+="</select><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='复制并替换所有'  onclick=\"copyWlfl('"+index+"')\"></span></div>";
    return html;
}
function checkCcdgWlfl(index, e, zdmc){
    if(zdmc=="ccdg"){
        t_map.rows[index].ccdg = e.value;
        var ccdg = $("#editContractForm #ccdg_"+index).val();
        if(ccdg == null || ccdg==""){
            var zlbHtml = "";
            zlbHtml += "<option value=''>--请选择--</option>";
            $("#editContractForm #wlfl_"+index).empty();
            $("#editContractForm #wlfl_"+index).append(zlbHtml);
            $("#editContractForm #wlfl_"+index).trigger("chosen:updated");
            return;
        }else {
            var wlfls = $("#editContractForm #wlfls").val();
            var ywlfllist = JSON.parse(wlfls);
            var wlfllist = [];
            for (var i = 0; i < ywlfllist.length; i++) {
                if (ywlfllist[i].cskz1 == ccdg){
                    wlfllist.push(ywlfllist[i]);
                }
            }
            var zlbHtml = "";
            zlbHtml += "<option value=''>--请选择--</option>";
            $.each(wlfllist,function(i){
                zlbHtml += "<option value='" + wlfllist[i].csid + "' id='"+wlfllist[i].csid+"' wlflmc='"+wlfllist[i].csmc+"' >" + wlfllist[i].csmc + "</option>";
            });
            $("#editContractForm #wlfl_"+index).empty();
            $("#editContractForm #wlfl_"+index).append(zlbHtml);
            $("#editContractForm #wlfl_"+index).trigger("chosen:updated");
        }
    }
    if(zdmc=="wlfl"){
        t_map.rows[index].wlfl = e.value;
    }
}
/**
 * 备注格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function bzformat(value,row,index){
	if(row.bz == null){
		row.bz = "" ;
	}
	var html="<input id='bz_"+index+"' name='bz_"+index+"' value='"+row.bz+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'bz\')\"></input>";
	return html;
}
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
	var html="<input id='cpzch_"+index+"' name='cpzch_"+index+"' validate='{required:true}' value='"+row.cpzch+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'cpzch\')\"></input>";
	return html;
}
/**
 * 保修时间格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function bxsjformat(value,row,index){
    if(row.bxsj == null){
        row.bxsj = "" ;
    }
    if (row.lbcskz1 == '3'){
        var html="<input id='bxsj_"+index+"' name='bxsj_"+index+"' value='"+row.bxsj+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'bxsj\')\"></input>";
        return html;
    }
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
		if (zdmc == 'sl') {
			var sl = e.value
			t_map.rows[index].sl = sl;
			if(!sl){
				sl = 0;
			}
			var hsdj = t_map.rows[index].hsdj;
			var hjje = t_map.rows[index].hjje;
			if(hsdj){ // 根据含税单价计算其它金额
				hjje = calculateHjje(hsdj,sl);
				t_map.rows[index].hjje = hjje;
				$("#editContractForm #hjje_"+index).val(hjje);
			}else if(hjje){ // 根据合计金额计算其它金额
				hsdj = calculateHsdj(hjje,sl);
				t_map.rows[index].hsdj = hsdj;
				$("#editContractForm #hsdj_"+index).val(hsdj);
			}
            // 判断背景颜色
            var xssl = $("#editContractForm #xssl_"+index).text();
            xssl = xssl.substring(1,xssl.length);
            if(parseFloat(sl).toFixed(2) - parseFloat(xssl).toFixed(2) > 0){
                $("#editContractForm #sldiv_"+index).attr("style","background:orange;");
                $("#editContractForm #sldiv_"+index).attr("isBeyond","false");
            }else if (parseFloat(sl).toFixed(2) - parseFloat(xssl).toFixed(2) ==0){
                $("#editContractForm #sldiv_"+index).attr("style","background:darkcyan;");
                $("#editContractForm #sldiv_"+index).attr("isBeyond","true");
            }else {
                $("#editContractForm #sldiv_"+index).attr("style","background:yellow;");
                $("#editContractForm #sldiv_"+index).attr("isBeyond","true");
            }
			
		} else if (zdmc == 'hsdj') {
			var hsdj = e.value;
			t_map.rows[index].hsdj = hsdj;
			if(!hsdj){
				hsdj = 0;
			}
			var sl = t_map.rows[index].sl;
			var hjje = t_map.rows[index].hjje;
			// 根据数量计算总价
			if(sl){
				hjje = calculateHjje(hsdj,sl);
				t_map.rows[index].hjje = hjje;
				$("#editContractForm #hjje_"+index).val(hjje);
			}
		} else if (zdmc == 'hjje') {
			var hjje = e.value;
			t_map.rows[index].hjje = hjje;
			if(!hjje){
				hjje = 0;
			}
			var sl = t_map.rows[index].sl;
			var hsdj = t_map.rows[index].hsdj;
			// 根据数量计算其它金额
			if(sl){
				hsdj = calculateHsdj(hjje, sl);
				t_map.rows[index].hsdj = hsdj;
				$("#editContractForm #hsdj_"+index).val(hsdj);
			}
		} else if (zdmc == 'jhdhrq') {
			t_map.rows[index].jhdhrq = e.value;
		} else if (zdmc == 'bz') {
			t_map.rows[index].bz = e.value;
		}else if (zdmc == 'sfgdzc') {
            	t_map.rows[index].sfgdzc = e.value;
		}else if (zdmc == 'bxsj') {
            	t_map.rows[index].bxsj = e.value;
		}else if (zdmc == 'cpzch') {
            	t_map.rows[index].cpzch = e.value;
		}else if (zdmc == 'dhzq') {
            if(e.value && !/^\d+(\.\d{0})?$/.test(e.value)){
                $.error("请填写正确到货周期!");
                e.value = '';
                return;
            }
            t_map.rows[index].dhzq = e.value;

		}else if (zdmc == 'hwmc') {
            t_map.rows[index].hwmc = e.value;
		}else if (zdmc == 'bcsl') {
            var sl = e.value
            t_map.rows[index].sl = sl;
            if(!sl){
                sl = 0;
            }
            var hsdj = t_map.rows[index].hsdj;
            var hjje = t_map.rows[index].hjje;
            if(hsdj){ // 根据含税单价计算其它金额
                hjje = calculateHjje(hsdj,sl);
                t_map.rows[index].hjje = hjje;
                $("#editContractForm #hjje_"+index).val(hjje);
            }else if(hjje){ // 根据合计金额计算其它金额
                hsdj = calculateHsdj(hjje,sl);
                t_map.rows[index].hsdj = hsdj;
                $("#editContractForm #hsdj_"+index).val(hsdj);
            }
        }
	}
}	
/**
 * 无税单价= 含税单价 / (1+ 税率/100)
 * @param hsdj
 * @param suil
 * @returns
 */
function calculateWsdj(hsdj, suil){
	hsdj = parseFloat(hsdj).toFixed(5);
	suil = parseFloat(suil).toFixed(5);
	var temp = (1+suil/100).toFixed(5);
	var wsdj = (hsdj/temp).toFixed(5);
	return wsdj.toString();
}
/**
 * 合计金额 = 数量 * 含税单价
 * @param sl
 * @param hsdj
 * @returns
 */
function calculateHjje(hsdj, sl){
	var hjje = ((parseInt((sl*100).toFixed(0))*parseInt((hsdj*100000).toFixed(0)))/10000000).toFixed(2);
	return hjje.toString();
}
/**
 * 含税单价 = 合计金额 / 数量
 * @param hjje
 * @param sl
 * @returns
 */
function calculateHsdj(hjje, sl){
	var hsdj = ((parseInt((hjje*100000).toFixed(0))/parseInt((sl*100).toFixed(0)))/1000).toFixed(6);
	return hsdj.toString();
}
/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("slNumber", function (value, element){
    // var bcbj=$("#editContractForm #bcbj").val();
	// if(!value && bcbj!='2'){
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
 * 验证单价格式(五位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("djNumber", function (value, element){
	if(value && !/^\d+(\.\d{1,6})?$/.test(value)){
		$.error("请填写正确含税单价格式，可保留六位小数!");
	}
    return this.optional(element) || /^\d+(\.\d{1,6})?$/.test(value);
},"请填写正确格式，可保留六位小数。");

/**
 * 验证合计金额格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("hjNumber", function (value, element){
	if(value && !/^\d+(\.\d{1,2})?$/.test(value)){
		$.error("请填写正确合计金额格式，可保留两位小数!");
	}
    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");

/**
 * 验证计划到货日期
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("jhdhrqReq", function (value, element){
    // var bcbj=$("#editContractForm #bcbj").val();
	// if(!value && bcbj!='2'){
	if(!value){
		$.error("请填写计划到货日期!");
		return false;
	}
    return this.optional(element) || value;
},"请填写计划到货日期。");



/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
	var html = "<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除合同明细' onclick=\"delContractDetail('" + index + "',event)\" >删除</span>";
	if(row.qgmxid){
		html += "<span style='margin-left:5px;height:24px !important;' class='btn btn-success' title='新增物料合同明细' onclick=\"addInfo('" + index + "',event)\" ><span class='glyphicon glyphicon glyphicon-plus' aria-hidden='true'></span></span>";
	}
    if (htlx =='3'){
        var  kjhthtml = "";
        kjhthtml += "<span style='margin-left:5px;' class='btn btn-danger' title='删除' onclick=\"delKjht('" + index + "')\" >删除</span>";
        if("0"==row.scbj){
            kjhthtml += "<span style='margin-left:5px;' class='btn btn-danger' title='停用' onclick=\"stopUsingNow('" + index + "')\" >停用</span>";
        }else if ("3"==row.scbj){
            kjhthtml += "<span style='margin-left:5px;' class='btn btn-success' title='启用' onclick=\"startUsingNow('" + index + "')\" >启用</span>";
        }
        return kjhthtml;
    }
    if (yhtbchtFlag){
        var  yhthtml ="";
        yhthtml += "<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除合同明细' onclick=\"delContractDetail('" + index + "',event)\" >删除</span>";
        yhthtml+="<br>"
        // 1 启用  0 停用
        if("0"==row.sfty){
            yhthtml += "<span style='margin-left:5px;margin-top:5px;' class='btn btn-success' title='启用' onclick=\"startUsingOriginal('" + index + "')\" >启用</span>";
        }else {
            yhthtml += "<span style='margin-left:5px;margin-top:5px;' class='btn btn-danger' title='停用' onclick=\"stopUsingOriginal('" + index + "')\" >停用</span>";
        }
        return yhthtml;
    }
    return html;
}
/**
 * 框架合同的停用操作
 * @index index
 * @event event
 * @returns
 */
function stopUsingNow(index,e){
    var preJson = JSON.parse($("#editContractForm #kjht_json").val());
    preJson[index].scbj ='3';
    t_map.rows[index].scbj ='3';
    $("#editContractForm #kjht_list").bootstrapTable('load',t_map);
    $("#editContractForm #kjht_json").val(JSON.stringify(preJson));
}
/**
 * 框架合同的停用操作
 * @index index
 * @event event
 * @returns
 */
function startUsingNow(index,e){
    var preJson = JSON.parse($("#editContractForm #kjht_json").val());
    preJson[index].scbj ='0';
    t_map.rows[index].scbj ='0';
    $("#editContractForm #kjht_list").bootstrapTable('load',t_map);
    $("#editContractForm #kjht_json").val(JSON.stringify(preJson));
}
/**
 * 补充合同的停用操作
 * @index index
 * @event event
 * @returns
 */
function stopUsingOriginal(index,e){
    var preJson = JSON.parse($("#editContractForm #htmx_json").val());
    preJson[index].sfty ='0';
    t_map.rows[index].sfty ='0';
    $("#editContractForm #tb_list").bootstrapTable('load',t_map);
    $("#editContractForm #htmx_json").val(JSON.stringify(preJson));
}
/**
 * 补充合同的停用操作
 * @index index
 * @event event
 * @returns
 */
function startUsingOriginal(index,e){
    var preJson = JSON.parse($("#editContractForm #htmx_json").val());
    preJson[index].sfty ='1';
    t_map.rows[index].sfty ='1';
    $("#editContractForm #tb_list").bootstrapTable('load',t_map);
    $("#editContractForm #htmx_json").val(JSON.stringify(preJson));
}
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delKjht(index){
    var preJson = JSON.parse($("#editContractForm #kjht_json").val());
    preJson.splice(index,1);
    t_map.rows.splice(index,1);
    $("#editContractForm #kjht_list").bootstrapTable('load',t_map);
    $("#editContractForm #kjht_json").val(JSON.stringify(preJson));
}
/**
 * 删除操作(从合同明细删除)
 * @param index
 * @param event
 * @returns
 */
function delContractDetail(index,event){
	var preJson = JSON.parse($("#editContractForm #htmx_json").val());
	preJson.splice(index,1);
	t_map.rows.splice(index,1);
	$("#editContractForm #tb_list").bootstrapTable('load',t_map);
	$("#editContractForm #htmx_json").val(JSON.stringify(preJson));
}
/**
 * 新增物料合同明细(不关联请购)
 * @returns
 */
function addInfo(index, event){
	var addModel = new Object();
	var row = $("#editContractForm #tb_list").bootstrapTable('getData')[index];
	addModel.gg = row.gg;
	addModel.jldw = row.jldw;
	addModel.wlbm = row.wlbm;
	addModel.wlid = row.wlid;
	addModel.wlmc = row.wlmc;
	addModel.hsdj = row.hsdj;
	addModel.jhdhrq = row.jhdhrq;
	addModel.qgsl = 999999;
	addModel.glid = row.glid;
	t_map.rows.push(addModel);
	var preJson = JSON.parse($("#editContractForm #htmx_json").val());
	var sz = {"qgid":'',"qgmxid":''};
	preJson.push(sz);
	$("#editContractForm #htmx_json").val(JSON.stringify(preJson));
	$("#editContractForm #tb_list").bootstrapTable('load',t_map);
}

/**
 * 初始化页面
 * @returns
 */
function init(){
	//添加日期控件
	laydate.render({
	   elem: '#editContractForm #cjrq'
	   ,type: 'datetime'
        ,trigger: 'click'
	   ,ready: function(date){
			if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
				var myDate = new Date(); //实例一个时间对象；
				this.dateTime.hours=myDate.getHours();
				this.dateTime.minutes=myDate.getMinutes();
	        	this.dateTime.seconds=myDate.getSeconds();
			}
		}
	});
	//添加日期控件
	laydate.render({
	   elem: '#editContractForm #jhrq'
	  ,theme: '#2381E9'
        ,trigger: 'click'
	});
    //添加日期控件
    laydate.render({
        elem: '#editContractForm .yfrqsz'
        ,theme: '#2381E9'
        ,trigger: 'click'
    });
    //添加日期控件
    laydate.render({
        elem: '#editContractForm #ksrq'
        ,theme: '#2381E9'
        ,trigger: 'click'
    });
    //添加日期控件
    laydate.render({
        elem: '#editContractForm #dqrq'
        ,theme: '#2381E9'
        ,trigger: 'click'
    });
	// 初始化请购单号
	initDjh();
	//币种下拉框改变事件
	var sel_biz = $("#editContractForm #biz");
	sel_biz.unbind("change").change(function(){
		var csid = $("#editContractForm #biz").val();
		var cskz1 = $("#editContractForm #"+csid).attr("cskz1");
		$("#editContractForm #bizmc").val($("#editContractForm").attr("csmc"));
		if(cskz1){
			$("#editContractForm #hl").val(cskz1);
		}
	});
	//付款方式下拉框改变事件
    var sel_fkfs=$("#editContractForm #fkfs");
    sel_fkfs.unbind("change").change(function(){
        var fkfs=$("#editContractForm #fkfs").val();
        var csdm=$("#editContractForm #"+fkfs).attr("csdm")
        if(csdm=="002"){//判断是否为预付部分
            $("#editContractForm #yfjediv").attr("style","display:block;")
            $("#editContractForm #yfrqdiv").attr("style","display:block;")
        }else{
            $("#editContractForm #yfjediv").attr("style","display:none;")
            $("#editContractForm #yfrqdiv").attr("style","display:none;")
        }
    });
	// 编码权限控制
//	if($("#editContractForm #flag").val() == "add" || $("#editContractForm #flag").val() == "mod"){
//		$("#editContractForm #htnbbhDiv").remove();
//	}
}

/**
 * 初始化请购单号
 * @returns
 */
function initDjh(){
	var qgids = $("#editContractForm #qgids").val();
	if(!qgids || qgids.length < 0){
		return;
	}
	$.ajax({ 
	    type:'post',  
	    url:$('#editContractForm #urlPrefix').val()+"/purchase/purchase/pagedataGetPurchaseList",
	    cache: false,
	    data: {"ids":qgids,"access_token":$("#ac_tk").val()},  
	    dataType:'json',
	    success:function(result){
	    	//返回值
	    	var qgglDtos = result.qgglDtos;
	    	if(qgglDtos){
	    		$("#editContractForm #djhs").tagsinput({
	    			itemValue: "qgid",
	    			itemText: "djh",
	    		})
	    		for(var i = 0; i < qgglDtos.length; i++){
		    		$("#editContractForm #djhs").tagsinput('add',{"qgid": qgglDtos[i].qgid, "djh": qgglDtos[i].djh});
		    	}
	    	}
	    }
	});
}
/**
 * 监听单据号标签移除事件
 */
var tagsRemoved = $('#editContractForm #djhs').on('beforeItemRemove', function(event) {
	$.confirm('您确定要删除请购单下的所有明细记录吗？',function(result){
		if(result){
			// 获取未更改请购单和明细信息
			var preJson = [];
			if($("#editContractForm #htmx_json").val()){
				preJson = JSON.parse($("#editContractForm #htmx_json").val());
				// 删除明细并刷新列表
				var qgid = event.item.qgid;
				for (var i = t_map.rows.length-1; i >= 0 ; i--) {
					if(t_map.rows[i].qgid == qgid){
						t_map.rows.splice(i,1);
						preJson.splice(i,1);
					}
				}
				$("#editContractForm #htmx_json").val(JSON.stringify(preJson));
				$("#editContractForm #tb_list").bootstrapTable('load',t_map);
			}
		}
	})
});
/**
 * 监听单据号标签点击事件
 */
var tagClick = $("#editContractForm").on('click','.label-info',function(e){
	event.stopPropagation();
	var url = $('#editContractForm #urlPrefix').val() + "/purchase/purchase/pagedataChooseListPurchaseInfo?access_token=" + $("#ac_tk").val() + "&qgid=" + e.currentTarget.dataset.logo;
	$.showDialog(url, '选择请购明细', chooseEditQgmxConfig);
});
var chooseEditQgmxConfig = {
	width : "1000px",
	modalName	: "chooseEditQgmxModal",
	formName	: "chooseEditQgmxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#chooseQgmxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				if($("#chooseQgmxForm input[name='checkQgmx']:checked").length == 0){
					$.alert("未选中请购明细信息！");
					return false;
				}
				// 获取未更改请购单和明细信息
				var preJson = [];
				if($("#editContractForm #htmx_json").val()){
					preJson = JSON.parse($("#editContractForm #htmx_json").val());
				}
				// 获取更改请购单和明细信息
				var qgmxJson = [];
				$("#chooseQgmxForm input[name='checkQgmx']").each(function(){
					if($(this).is(":checked")){
						var sz={"qgid":'',"qgmxid":''};
						sz.qgid = qgid;
						sz.qgmxid = this.dataset.qgmxid;
						qgmxJson.push(sz);
					}
				})
				// 获取删除请购明细
				var qgid = $("#chooseQgmxForm input[name='qgid']").val();
				for (var j = preJson.length-1; j >=0; j--) {
					if(qgid == preJson[j].qgid){
						var isDel = true;
						for (var i = 0; i < qgmxJson.length; i++) {
							if(preJson[j].qgmxid == qgmxJson[i].qgmxid){
								isDel = false;
								break;
							}
						}
						if(isDel){
							// 根据明细ID直接更新页面列表(删除)
							for (var i = 0; i < t_map.rows.length; i++) {
								if(t_map.rows[i].qgmxid == preJson[j].qgmxid){
									preJson.splice(j,1);
									t_map.rows.splice(i,1);
									break;
								}
							}
						}
					}
				}
				// 获取新增请购明细
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
					$.post($('#editContractForm #urlPrefix').val() + "/purchase/purchase/pagedataGetPurchaseInfo",{ids:ids,"access_token":$("#ac_tk").val()},function(data){
						var htmxDtos = data.htmxDtos;
						if(htmxDtos != null && htmxDtos.length > 0){
							// 更新页面列表(新增)
							for (var i = 0; i < htmxDtos.length; i++) {
								if(t_map.rows){
									t_map.rows.push(htmxDtos[i]);
								}else{
									t_map.rows= [];
									t_map.rows.push(htmxDtos[i]);
								}
								var sz = {"qgid":'',"qgmxid":''};
								sz.qgid = htmxDtos[i].qgid;
								sz.qgmxid = htmxDtos[i].qgmxid;
								preJson.push(sz);
							}
							$("#editContractForm #tb_list").bootstrapTable('load',t_map);
							$("#editContractForm #htmx_json").val(JSON.stringify(preJson));
							$.closeModal(opts.modalName);
						}
    				},'json');
				}else{
					$("#editContractForm #tb_list").bootstrapTable('load',t_map);
					$("#editContractForm #htmx_json").val(JSON.stringify(preJson));
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
 * 选择供应商列表
 * @returns
 */
function chooseGys(){
	var url=$('#editContractForm #urlPrefix').val() + "/warehouse/supplier/pagedataSupplier?access_token=" + $("#ac_tk").val()+"&scbj="+"0";
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
					var gysdm = sel_row[0].gysdm;
					var sfyfj = sel_row[0].sfyfj;
                    			if (sfyfj=='0'){
                        			$.error("供应商信息没有相关附件！请先上传相关附件再录入！<br>供应商资质附件格式：<br>例:文件名(2022.01.01-2023.01.01)");
                        			return false;
                    			}
					$("#editContractForm #gysmc").val(gysmc);
					$("#editContractForm #gys").val(gysid);
					$("#editContractForm #sl").val(sl);
					$("#editContractForm #gysdm").val(gysdm);
                    if (htlx =='3'){
                        $('#editContractForm #viewkjht_list').bootstrapTable('refresh',{pageNumber:1});
                        getHisFj();
                    }else {
                        $.ajax({
                            async:false,
                            url: $('#editContractForm #urlPrefix').val()+'/contract/contract/pagedataContractByGysWithWgq',
                            type: 'post',
                            dataType: 'json',
                            data : {"gys":$("#editContractForm #gys").val(),"htlx":'3',"sfdq":'0',"kjlx":'0', "access_token":$("#ac_tk").val()},
                            success: function(data) {
                                if (data&&data.htglDto){
                                    $("#editContractForm #htywlx").find("option[value='"+data.htglDto.ywlx+"']").prop("selected",true);
                                    $("#editContractForm #htywlx").trigger("chosen:updated");
                                    $('#editContractForm #sl').val(data.htglDto.sl)
                                    $('#editContractForm #hl').val(data.htglDto.hl)
                                    $("#editContractForm #biz").find("option[value='"+data.htglDto.biz+"']").prop("selected",true);
                                    $("#editContractForm #biz").trigger("chosen:updated");
                                    $("#editContractForm #fpfs").find("option[value='"+data.htglDto.fpfs+"']").prop("selected",true);
                                    $("#editContractForm #fpfs").trigger("chosen:updated");
                                    $("#editContractForm #fkfs").find("option[value='"+data.htglDto.fkfs+"']").prop("selected",true);
                                    $("#editContractForm #fkfs").trigger("chosen:updated");
                                    $("#editContractForm #fkf").find("option[value='"+data.htglDto.fkf+"']").prop("selected",true);
                                    $("#editContractForm #fkf").trigger("chosen:updated");
                                    $("#editContractForm #htfxcd").find("option[value='"+data.htglDto.htfxcd+"']").prop("selected",true);
                                    $("#editContractForm #htfxcd").trigger("chosen:updated");
                                }
                            }
                        });
                    }
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
function getHisFj(){
    $.ajax({
        async:false,
        url: $('#editContractForm #urlPrefix').val()+'/contract/contract/pagedataGetDocuments',
        type: 'post',
        dataType: 'json',
        data : {"gys":$("#editContractForm #gys").val(),"htid":$("#editContractForm #htid").val(),"htlx":'3', "access_token":$("#ac_tk").val()},
        success: function(data) {
            $("#editContractForm #htDiv").empty();
            if(data.fjcfbDtos){
                var html="";
                for(var i=0;i<data.fjcfbDtos.length;i++){
                    html+="<div id="+data.fjcfbDtos[i].fjid+">";
                    html+="<div class='col-sm-12'>";
                    html+="<label for=''>"+(i+1)+".</label>";
                    html+="<button style='outline:none;margin-bottom:5px;padding:0px;' class='btn btn-link' type='button' title='下载' onclick=\"xz('"+data.fjcfbDtos[i].fjid+"');\">";
                    html+="<span>"+data.fjcfbDtos[i].wjm+"</span>";
                    html+="<span class='glyphicon glyphicon glyphicon-save'></span>";
                    html+="</button>";
                    html+="&nbsp;&nbsp; <a title='预览' onclick=\"yl('"+data.fjcfbDtos[i].fjid+"','"+data.fjcfbDtos[i].wjm+"')\"><span class='glyphicon glyphicon-eye-open'></span></a>";
                    html+="</div>";
                    html+="</div>";
                }
                $("#editContractForm #htDiv").append(html);
            }
        }
    });
}
function yl(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
        var url=$("#editContractForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
        $.showDialog(url,'图片预览',viewPreViewConfig);
    }else if(type.toLowerCase()==".pdf"){
        var url= $("#editContractForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',viewPreViewConfig);
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}
var viewPreViewConfig = {
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
/**
 * 选择申请部门列表
 * @returns
 */
function chooseSqbm(){
	var url=$('#editContractForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
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
					$('#editContractForm #sqbm').val(sel_row[0].jgid);
					$('#editContractForm #sqbmmc').val(sel_row[0].jgmc);
                    $('#editContractForm #jqdm').val(sel_row[0].jgdm);
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
function chooseFzr(){
	var url=$('#editContractForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择负责人',addFzrConfig);	
}
var addFzrConfig = {
	width		: "800px",
	modalName	:"addFzrModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#editContractForm #fzr').val(sel_row[0].yhid);
					$('#editContractForm #fzrmc').val(sel_row[0].zsxm);
					//保存操作习惯
					$.ajax({
					    type:'post',  
					    url:$('#editContractForm #urlPrefix').val() + "/common/habit/commonModUserHabit",
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
 * 刷新内部编号
 * @returns
 */
function refreshNbbh(){
	var htnbbh = $("#editContractForm #htnbbh").val();
	var htwbbh = $("#editContractForm #htwbbh").val();
    var ywlx = $("#editContractForm #htywlx").val();
    var htlx = $("#editContractForm #htlx").val();
    var yyhtnbbh = $("#editContractForm #yyhtnbbh").val();
    var fzr = $("#editContractForm #fzr").val();
	if(ywlx&&fzr){
		$.ajax({ 
		    type:'post',
		    url:$('#editContractForm #urlPrefix').val() + "/contract/contract/pagedataGetContractCode",
		    cache: false,
		    data: {"htnbbh":htnbbh,"ywlx":ywlx,"htlx":htlx,"yyhtnbbh":yyhtnbbh,"fzr":fzr,"access_token":$("#ac_tk").val()},
		    dataType:'json',
		    success:function(result){
                if (result.status=='success'){
                    $("#editContractForm #htnbbh").val(result.message);
                    if(htnbbh == htwbbh){
                        $("#editContractForm #htwbbh").val(result.message);
                    }
                }else {
                    $.alert(result.message);
                }
		    }
		});
	}else{
		$.alert("请选择合同类型和负责人！");
	}
}

/**
 * 选择请购单据号(及明细)
 * @returns
 */
function chooseQgxx(){
    var flag = checkGys();
    if (!flag){
        return;
    }
    var htlx=$('#editContractForm #htlx').val();
    var qglbdm=$('#editContractForm #qglbdm').val();
    var htaddflag=$('#editContractForm #htaddflag').val();
    if (htlx==1)
        var qglx="1";
    else qglx='0';
    var url = $('#editContractForm #urlPrefix').val() + "/purchase/purchase/pagedataChooseListPurchase?access_token=" + $("#ac_tk").val()+"&qglx="+qglx+"&qglbdm="+qglbdm+"&htaddflag="+htaddflag;
    $.showDialog(url, '选择请购单号', chooseQgxxConfig);
}
var chooseQgxxConfig = {
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
				if($("#editContractForm #htmx_json").val()){
					preJson = JSON.parse($("#editContractForm #htmx_json").val());
				}
				// 获取更改后的请购单和明细信息
				var qgmxJson = [];
				if($("#chooseQgxxForm #qgmx_json").val()){
					qgmxJson = JSON.parse($("#chooseQgxxForm #qgmx_json").val());
					// 判断删除的请购明细信息
//					for (var j = preJson.length-1; j >= 0; j--) {
//						var isDel = true;
//						var isHave = false;
//						for (var i = 0; i < qgmxJson.length; i++) {
//							if(preJson[j].qgid == qgmxJson[i].qgid){
//								isHave = true;
//							}
//							if(preJson[j].qgmxid == qgmxJson[i].qgmxid){
//								isDel = false;
//								break;
//							}
//												
//						}
//						if(isDel && isHave){
//							// 根据明细ID直接更新页面列表(删除)
//							for (var i = 0; i < t_map.rows.length; i++) {
//								if(t_map.rows[i].qgmxid == preJson[j].qgmxid){
//									preJson.splice(j,1);
//									t_map.rows.splice(i,1);
//									break;
//								}
//							}
//						}
//					}
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
						var htid = $("#editContractForm #htid").val();
						// 查询信息并更新到页面
						$.post($('#editContractForm #urlPrefix').val() + "/purchase/purchase/pagedataGetPurchaseInfo",{"ids":ids,"kjhtbj":'1',"gys":$("#editContractForm #gys").val(), "htid":htid, "access_token":$("#ac_tk").val()},function(data){
							var htmxDtos = data.htmxDtos;
							if(htmxDtos != null && htmxDtos.length > 0){
								// 更新页面列表(新增)
								for (var i = 0; i < htmxDtos.length; i++) {
									if(i == 0){
										if(!t_map.rows){
											t_map.rows= [];
										}
										if(!$("#editContractForm #sqbm").val()){
											$("#editContractForm #sqbm").val(htmxDtos[i].sqbm);
											$("#editContractForm #sqbmmc").val(htmxDtos[i].sqbmmc);
										}
										if(!$("#editContractForm #fzr").val()){
											$("#editContractForm #fzr").val(htmxDtos[i].fzr);
											$("#editContractForm #fzrmc").val(htmxDtos[i].fzrmc);
										}
									}
									t_map.rows.push(htmxDtos[i]);
									var sz = {"qgid":'',"qgmxid":''};
									sz.qgid = htmxDtos[i].qgid;
									sz.qgmxid = htmxDtos[i].qgmxid;
									preJson.push(sz);
								}
							}
							$("#editContractForm #tb_list").bootstrapTable('load',t_map);
							$("#editContractForm #htmx_json").val(JSON.stringify(preJson));
							// 请购单号
							var selDjh = $("#chooseQgxxForm #yxdjh").tagsinput('items');
							$("#editContractForm #djhs").tagsinput({
				    			itemValue: "qgid",
				    			itemText: "djh",
				    		})
							for(var i = 0; i < selDjh.length; i++){
								var value = selDjh[i].value;
								var text = selDjh[i].text;
								$("#editContractForm #djhs").tagsinput('add', {"qgid":value,"djh":text});
							}
							$.closeModal(opts.modalName);
	    				},'json');
					}else{
						$("#editContractForm #tb_list").bootstrapTable('load',t_map);
						$("#editContractForm #htmx_json").val(JSON.stringify(preJson));
						// 请购单号
						var selDjh = $("#chooseQgxxForm #yxdjh").tagsinput('items');
						$("#editContractForm #djhs").tagsinput({
			    			itemValue: "qgid",
			    			itemText: "djh",
			    		})
						for(var i = 0; i < selDjh.length; i++){
							var value = selDjh[i].value;
							var text = selDjh[i].text;
							$("#editContractForm #djhs").tagsinput('add', {"qgid":value,"djh":text});
						}
						$.closeModal(opts.modalName);
					}
				}else{
					$.alert("未获取到选中的请购信息！");
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
 * 统计查询
 * @returns
 */
function selectStatistic(domId, item, zdmc){
	if(zdmc == "hsdj"){
		$.ajax({
			type : 'post',
			url : $('#editContractForm #urlPrefix').val() + "/contract/statistic/pagedataStatisticTaxPrice",
			cache : false,
			data : {
				"wlid" : item,
				"access_token" : $("#ac_tk").val()
			},
			dataType : 'json',
			success : function(data) {
				buildStatistic(domId, data,800,400);
			}
		});
	}else if(zdmc == "wlbm"){
		$.ajax({
			type : 'post',
			url : $('#editContractForm #urlPrefix').val() + "/contract/statistic/pagedataStatisticCycle",
			cache : false,
			data : {
				"wlid" : item,
				"access_token" : $("#ac_tk").val()
			},
			dataType : 'json',
			success : function(data) {
				buildStatistic(domId, data);
			}
		});
	}
}

//请购物料列表生成合同页面初始化
function creatContractInit(){
	var qgmxids=$("#editContractForm #qgmxids").val();
	if(qgmxids!=null && qgmxids!=''){
		$.ajax({ 
		    type:'post',  
		    url:$("#editContractForm #urlPrefix").val()+"/purchase/purchase/pagedataGetPurchaseInfo",
		    cache: false,
		    data: {"ids":qgmxids,"access_token":$("#ac_tk").val()},  
		    dataType:'json', 
		    success:function(data){
		    	//返回值
		    	var htmxDtos=data.htmxDtos;
		    	if(htmxDtos!=null && htmxDtos.length>0){
		    		t_map.rows=[];
		    		t_map.rows=htmxDtos;
		    		// 请购单号
		    		var qgxxlist=[];
					$("#editContractForm #djhs").tagsinput({
		    			itemValue: "qgid",
		    			itemText: "djh",
		    		})
		    		var preJson=[];
					for(var i = 0; i < htmxDtos.length; i++){
						if(i == 0){
							if(!$("#editContractForm #sqbm").val()){
								$("#editContractForm #sqbm").val(htmxDtos[i].sqbm);
								$("#editContractForm #sqbmmc").val(htmxDtos[i].sqbmmc);
							}
							if(!$("#editContractForm #fzr").val()){
								$("#editContractForm #fzr").val(htmxDtos[i].fzr);
								$("#editContractForm #fzrmc").val(htmxDtos[i].fzrmc);
							}
						}
						var value = htmxDtos[i].qgid;
						var text = htmxDtos[i].djh;
						var qgxx={"qgid":value,"djh":text};
						var sz = {"qgid":'',"qgmxid":''};
						sz.qgid = htmxDtos[i].qgid;
						sz.qgmxid = htmxDtos[i].qgmxid;
						preJson.push(sz);
						if(qgxxlist!=null && qgxxlist.length>0){
							var sfcz=false;//请购单是否已存在
							for(var j=0;j<qgxxlist.length;j++){
								if(qgxxlist[j].qgid==value){
									sfcz==true;
								}
							}
							if(!sfcz){
								qgxxlist.push(qgxx);
								$("#editContractForm #djhs").tagsinput('add', {"qgid":value,"djh":text});
							}
						}else{
							qgxxlist.push(qgxx);
							$("#editContractForm #djhs").tagsinput('add', {"qgid":value,"djh":text});
						}
					}
					$("#editContractForm #htmx_json").val(JSON.stringify(preJson));
		    		$("#editContractForm #tb_list").bootstrapTable('load',t_map);
		    	}
		    }
		});
	}
}


//打开文件上传界面
function editfile(divName,btnName){
    $("#editContractForm"+"  #"+btnName).hide();
    $("#editContractForm"+"  #"+divName).show();
}
//关闭文件上传界面
function cancelfile(divName,btnName){
    $("#editContractForm"+"  #"+btnName).show();
    $("#editContractForm"+"  #"+divName).hide();
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
		var url=$("#editContractForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= $("#editContractForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
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
    jQuery('<form action='+$("#editContractForm #urlPrefix").val()+'"/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
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
			var url= $("#editContractForm #urlPrefix").val()+"/common/file/delFile";
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

function displayUpInfo(fjid){
    if(!$("#editContractForm #fjids").val()){
        $("#editContractForm #fjids").val(fjid);
    }else{
        $("#editContractForm #fjids").val($("#editContractForm #fjids").val()+","+fjid);
    }
}
var viewFrameworkContract_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#editContractForm #viewkjht_list").bootstrapTable({
            url:   $('#editContractForm #urlPrefix').val()+'/contract/contract/pagedataGetHisContract',         //请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#editContractForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"htmx.lrsj",					// 排序字段
            sortOrder: "desc",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            pageSize: 15,                       // 每页的记录行数（*）
            pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: false,                  // 是否显示所有的列
            showRefresh: false,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "htmxid",                     // 每一行的唯一标识，一般为主键列
            showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns:  [
                {
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index+1;
                    },
                    titleTooltip:'序号',
                    width: '3%',
                    align: 'left',
                    visible:true
                },{
                    field: 'wlbm',
                    title: '物料编码',
                    titleTooltip:'物料编码',
                    width: '5%',
                    align: 'left',
                    visible: true,
                    formatter:hisWlbmFormatter
                },{
                    field: 'wlmc',
                    title: '物料名称',
                    titleTooltip:'物料名称',
                    width: '10%',
                    align: 'left',
                    formatter:viewFramework_wlmcformat,
                    visible: true
                },{
                    field: 'scs',
                    title: '生产厂家',
                    titleTooltip:'生产厂家',
                    width: '10%',
                    align: 'left',
                    visible: true
                },{
                    field: 'htnbbh',
                    title: '框架合同编号',
                    titleTooltip:'框架合同编号',
                    width: '10%',
                    align: 'left',
                    visible: true
                },{
                    field: 'cjrq',
                    title: '创建日期',
                    titleTooltip:'创建日期',
                    width: '8%',
                    align: 'left',
                    visible: true
                },{
                    field: 'ksrq',
                    title: '开始日期',
                    titleTooltip:'开始日期',
                    width: '5%',
                    align: 'left',
                    visible: true
                },{
                    field: 'dqrq',
                    title: '到期日期',
                    titleTooltip:'到期日期',
                    width: '5%',
                    align: 'left',
                    visible: true
                },{
                    field: 'sfgq',
                    title: '是否过期',
                    titleTooltip:'是否过期',
                    width: '7%',
                    align: 'left',
                    visible: true
                },{
                    field: 'cz',
                    title: '操作',
                    width: '5%',
                    align: 'left',
                    formatter:viewFramework_czformat,
                    visible: true
                },{
                    field: 'fz',
                    title: '复制',
                    width: '5%',
                    align: 'left',
                    formatter:viewFramework_fzformat,
                    visible: true
                }],
            onLoadSuccess:function(map){
            },
            onLoadError:function(){
            },
            onDblClickRow: function (row, $element) {
            },
        });
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "htmxid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            gysid: $('#editContractForm #gys').val(),
            htid: $('#editContractForm #htid').val(),
            htlx: '3',
            sfgq: $("#editContractForm #sfgq").val()
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit;
}
function hisWlbmFormatter(value,row,index) {
    var html = "";
    if(row.wlbm==null){
        html = "";
    }else{
        html += "<a href='javascript:void(0);' onclick=\"queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";

    }
    return html;
}
/**
 * 复制功能
 * @param index
 * @param e
 * @param zdmc
 */
function viewFramework_fzformat(value,row,index){
    return "<span style='margin-left:5px;' class='btn btn-success' title='复制' onclick=\"copyHistory('" + row.htmxid + "')\" >复制</span>";
}
function copyHistory(htmxid) {
    $.ajax({
        async:false,
        url: $('#editContractForm #urlPrefix').val()+'/contract/contract/pagedataGetHisMx',
        type: 'post',
        dataType: 'json',
        data : {"htmxid":htmxid, "access_token":$("#ac_tk").val()},
        success: function(data) {
            var json=JSON.parse($("#editContractForm #kjht_json").val());
            data.htmxDto.htmxid ='';
            data.htmxDto.htid ='';
            data.htmxDto.scbj ='0';
            t_map.rows.push(data.htmxDto);
            json.push(data.htmxDto);
            $("#editContractForm #kjht_list").bootstrapTable('load',t_map);
            $("#editContractForm #kjht_json").val(JSON.stringify(json));
        }
    });
}
/**
 * 物料名称格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function viewFramework_wlmcformat(value,row,index){
    if(row.wlmc){
        return row.wlmc;
    }else{
        return row.hwmc;
    }
}
/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function viewFramework_czformat(value,row,index){
    var html="";
    if("0"==row.scbj){
        html = "<span style='margin-left:5px;' class='btn btn-danger' title='停用' onclick=\"stopUsingHis('" + row.htmxid + "')\" >停用</span>";
    }else if ("3"==row.scbj){
        html = "<span style='margin-left:5px;' class='btn btn-success' title='启用' onclick=\"startUsingHis('" + row.htmxid + "')\" >启用</span>";
    }
    return html;
}
/**
 * 停用操作
 * @index index
 * @event event
 * @returns
 */
function stopUsingHis(htmxid){
    $.ajax({
        async:false,
        url: $('#editContractForm #urlPrefix').val()+'/contract/contract/pagedataUpdateScbj',
        type: 'post',
        dataType: 'json',
        data : {"htmxid":htmxid, "scbj":"3", "access_token":$("#ac_tk").val()},
        success: function(data) {
            if(data.status == 'fail'){
                $.error("异常出错！");
            }
            $('#editContractForm #viewkjht_list').bootstrapTable('refresh');
        }
    });
}
/**
 * 启用操作
 * @param index
 * @param event
 * @returns
 */
function startUsingHis(htmxid){
    $.ajax({
        async:false,
        url: $('#editContractForm #urlPrefix').val()+'/contract/contract/pagedataUpdateScbj',
        type: 'post',
        dataType: 'json',
        data : {"htmxid":htmxid, "scbj":"0", "access_token":$("#ac_tk").val()},
        success: function(data) {
            if(data.status == 'fail'){
                $.error("异常出错！");
            }
            $('#editContractForm #viewkjht_list').bootstrapTable('refresh');
        }
    });
}
function addWl_ht() {
    var url = $("#editContractForm #urlPrefix").val()+"/agreement/agreement/pagedataListMaterial?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择物料', chooseWlConfig);
}
var chooseWlConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseWlModal",
    formName	: "chooseMaterialForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chooseMaterialForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var sel = $("#chooseMaterialForm #yxwl").tagsinput('items');
                var json=JSON.parse($("#editContractForm #kjht_json").val());
                var ids="";
                for(var i = 0; i < sel.length; i++){
                    var value = sel[i].value;
                    ids+=","+value;
                }
                //如果有选中数据
                if(ids){
                    ids=ids.substring(1);
                    $.ajax({
                        async:false,
                        url: $('#editContractForm #urlPrefix').val()+'/agreement/agreement/pagedataGetMaterialDetails',
                        type: 'post',
                        dataType: 'json',
                        data : {"ids":ids, "access_token":$("#ac_tk").val()},
                        success: function(data) {
                            if(data.wlglDtos.length>0){
                                for (var i = 0; i < data.wlglDtos.length; i++) {
                                    var isFind=false;
                                    for (var j = 0; j < t_map.rows.length; j++) {
                                        if(data.wlglDtos[i].wlid==t_map.rows[j].wlid){
                                            isFind=true;
                                            break;
                                        }
                                    }
                                    if(!isFind){
                                        data.wlglDtos[i].scbj ='0';
                                        data.wlglDtos[i].bz ='';
                                        t_map.rows.push(data.wlglDtos[i]);
                                        json.push(data.wlglDtos[i]);
                                    }
                                }
                            }
                        }
                    });
                }else{//没有选中数据则将表中的物料清除
                    for (var i = t_map.rows.length-1; i >=0; i--) {
                        if(t_map.rows[i].wlid){
                            json.splice(t_map.rows[i].wlid,1);
                            t_map.rows.splice(i,1);
                        }
                    }
                }
                $("#editContractForm #kjht_list").bootstrapTable('load',t_map);
                $("#editContractForm #kjht_json").val(JSON.stringify(json));
                $.closeModal(opts.modalName);
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
 * 新增操作
 * @param index
 * @param event
 * @returns
 */
function addFw_ht(){
    var data=JSON.parse($("#editContractForm #kjht_json").val());
    var sz = {"hwmc":'',"scs":'',"jldw":'',"gg":'',"fwbj":'1',"scbj":'0',"xh":data.length};
    t_map.rows.push(sz);
    data.push(sz);
    $("#editContractForm #kjht_list").bootstrapTable('load',t_map);
    $("#editContractForm #kjht_json").val(JSON.stringify(data));
}
var editFrameworkContract_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#editContractForm #kjht_list").bootstrapTable({
            url:   $('#editContractForm #urlPrefix').val()+'/contract/contract/pagedataFrameworkContract',         //请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#editContractForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"htmx.lrsj",					// 排序字段
            sortOrder: "desc",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            pageSize: 15,                       // 每页的记录行数（*）
            pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: false,                  // 是否显示所有的列
            showRefresh: false,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "htmxid",                     // 每一行的唯一标识，一般为主键列
            showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns:  columnsArray,
            onLoadSuccess:function(map){
                t_map=map;
                $("#editContractForm #kjht_json").val(JSON.stringify(t_map.rows));
            },
            onLoadError:function(){
            },
            onDblClickRow: function (row, $element) {
            },
        });
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            access_token:$("#ac_tk").val(),
            htid: $('#editContractForm #htid').val()
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit;
}
$("#editContractForm #sfgq").bind('change',function(){
    $('#editContractForm #viewkjht_list').bootstrapTable('refresh',{pageNumber:1});
})
$("#editContractForm #sfkj").bind('change',function(){
    checkGys()
    var  gys = $("#editContractForm #gys").val();
    var sfkj = $("#editContractForm #sfkj option:selected").val()
    if ("1"==sfkj&&gys){
        $.ajax({
            async:false,
            url: $('#editContractForm #urlPrefix').val()+'/contract/contract/pagedataContractByGysWithWgq',
            type: 'post',
            dataType: 'json',
            data : {"gys":gys,"htlx":'3',"sfdq":'0',"kjlx":'0', "access_token":$("#ac_tk").val()},
            success: function(data) {
                if (data&&data.htglDto){
                    $('#editContractForm #kjhtid').val(data.htglDto.htid)
                    $('#editContractForm #kjhtbh').val(data.htglDto.htnbbh)
                }else {
                    $('#editContractForm #kjhtid').val("")
                    $('#editContractForm #kjhtbh').val("")
                    $.confirm("该供应商没有框架合同！")
                }
            }
        });
    }
    if ("0"==sfkj){
        $('#editContractForm #kjhtid').val("")
        $('#editContractForm #kjhtbh').val("")
    }
})
function checkGys(){
    if (htlx!='3'){
       var gys = $('#editContractForm #gys').val();
       if (gys==null||gys==''){
           $.confirm("请优先选择供应商！");
           return false;
       }
    }
    return true;
}
//原合同
var viewOriginalContract_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#editContractForm #viewyht_list").bootstrapTable({
            url:   $('#editContractForm #urlPrefix').val()+'/contract/contract/pagedataGetOriginalContractDetails',         //请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#editContractForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"htmx.lrsj",					// 排序字段
            sortOrder: "desc",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            pageSize: 15,                       // 每页的记录行数（*）
            pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: false,                  // 是否显示所有的列
            showRefresh: false,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "htmxid",                     // 每一行的唯一标识，一般为主键列
            showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns:  [
                {
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index+1;
                    },
                    titleTooltip:'序号',
                    width: '3%',
                    align: 'left',
                    visible:true
                }, {
                    field: 'htmxid',
                    title: '合同明细ID',
                    titleTooltip:'合同明细ID',
                    width: '8%',
                    align: 'left',
                    visible: false
                }, {
                    field: 'mxid',
                    title: '明细ID',
                    titleTooltip:'明细ID',
                    width: '8%',
                    align: 'left',
                    visible: false
                }, {
                    field: 'qgid',
                    title: '请购ID',
                    titleTooltip:'请购ID',
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
                    field: 'djh',
                    title: '请购单号/说明',
                    titleTooltip:'请购单号/说明',
                    width: '8%',
                    align: 'left',
                    formatter:djhformat,
                    visible: true
                }, {
                    field: 'qglbmc',
                    title: '类别',
                    titleTooltip:'请购类别',
                    width: '3%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'wlbm',
                    title: '物料信息',
                    titleTooltip:'物料信息',
                    width: '12%',
                    align: 'left',
                    formatter:wlbmformat,
                    visible: true
                }, {
                    field: 'sl',
                    title: '数量',
                    titleTooltip:'数量',
                    width: '4%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'jldw',
                    title: '单位',
                    titleTooltip:'单位',
                    formatter:jldwformat,
                    width: '3%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'hsdj',
                    title: '含税单价',
                    titleTooltip:'含税单价',
                    width: '5%',
                    align: 'left',
                    visible: true
                },{
                    field: 'hjje',
                    title: '合计金额',
                    titleTooltip:'合计金额',
                    width: '5%',
                    align: 'left',
                    visible: true
                },{
                    field: 'jhdhrq',
                    title: '计划到货日期',
                    titleTooltip:'计划到货日期',
                    width: '6%',
                    align: 'left',
                    visible: true
                },{
                    field: 'ccdg',
                    title: '初次订购',
                    titleTooltip:'初次订购',
                    width: '6%',
                    align: 'left',
                    formatter:viewSfccdgformat,
                    visible: true
                },{
                    field: 'wlfl',
                    title: '物料分类',
                    titleTooltip:'物料分类',
                    width: '8%',
                    align: 'left',
                    formatter:viewWlflformat,
                    visible: true
                },{
                    field: 'cpzch',
                    title: '产品注册号',
                    titleTooltip:'产品注册号',
                    width: '8%',
                    align: 'left',
                    visible: true
                },{
                    field: 'bxsj',
                    title: '保修时间',
                    titleTooltip:'保修时间',
                    width: '5%',
                    align: 'left',
                    visible: true
                },{
                    field: 'sfgdzc',
                    title: '是否固定资产',
                    titleTooltip:'是否固定资产',
                    width: '6%',
                    align: 'left',
                    formatter:viewSfgdzcformat,
                    visible: true
                },{
                    field: 'bz',
                    title: '备注',
                    titleTooltip:'备注',
                    width: '8%',
                    align: 'left',
                    visible: true
                },{
                    field: 'fz',
                    title: '复制',
                    width: '5%',
                    align: 'left',
                    formatter:viewOriginal_fzformat,
                    visible: true
                }],
            onLoadSuccess:function(map){
            },
            onLoadError:function(){
            },
            onDblClickRow: function (row, $element) {
            },
        });
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            access_token:$("#ac_tk").val(),
            htid: $('#editContractForm #bchtid').val(),
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit;
}
/**
 * 备注格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function viewBzformat(value,row,index){
    if(row.bz == null){
        row.bz = "" ;
    }
    return row.bz;
}
/**
 * 是否固定资产格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function viewSfgdzcformat(value,row,index){
    if(row.sfgdzc=='1'){
        return '是';
    }else if(row.sfgdzc=='0'){
        return '否';
    }else{
        return '';
    }
}
/**
 * 是否初次订购
 * @returns
 */
function viewSfccdgformat(value,row,index) {
    if(row.ccdg=='1'){
        return '是';
    }else if(row.ccdg=='0'){
        return '否';
    }else{
        return '';
    }
}
/**
 * 物料分类
 * @param value
 * @param row
 * @param index
 * @returns
 */
function viewWlflformat(value,row,index){
    var wlfl = row.wlfl;
    if (wlfl == null){
        return "";
    }
    var wlfls = $("#editContractForm #wlfls").val();
    var wlfllist = JSON.parse(wlfls);
    if(wlfllist!=null && wlfllist.length>0){
      for(var i = 0; i < wlfllist.length; i++) {
          if(wlfl==wlfllist[i].csid){
              return wlfllist[i].csmc;
          }
      }
    }
    return html;
}
/**
 * 复制功能
 * @param index
 * @param e
 * @param zdmc
 */
function viewOriginal_fzformat(value,row,index){
    return "<span style='margin-left:5px;' class='btn btn-success' title='复制' onclick=\"copyOriginal('"+row.htmxid+"')\" >复制</span>";
}
function copyOriginal(htmxid) {
    for (var i = 0; i < t_map.rows.length; i++) {
        if(t_map.rows[i].bchtmxid == htmxid){
            $.confirm("该合同明细已被引用！");
            return false;
        }
    }
    $.ajax({
        async:false,
        url: $('#editContractForm #urlPrefix').val()+'/contract/contract/pagedataGetOriginalContractDetails',
        type: 'post',
        dataType: 'json',
        data : {"htmxid":htmxid, "access_token":$("#ac_tk").val()},
        success: function(data) {
            if (data.rows){
                var json=JSON.parse($("#editContractForm #htmx_json").val());
                data.rows[0].bchtmxid = data.rows[0].htmxid
                data.rows[0].htmxid ='';
                data.rows[0].htid ='';
                data.rows[0].bz ='';
                if (data.rows[0].hzt){
                    data.rows[0].sfty = data.rows[0].hzt;
                }else{
                    data.rows[0].sfty = '1';
                }
                data.rows[0].scbj ='0';
                t_map.rows.push(data.rows[0]);
                t_map.rows.sort(function(a, b) {
                    return Number(a.xh) - Number(b.xh);
                });
                $("#editContractForm #tb_list").bootstrapTable('load',t_map);
                $("#editContractForm #htmx_json").val(JSON.stringify(t_map.rows));
            }
        }
    });
}
$(document).ready(function(){
    var htlx = $('#editContractForm #htlx').val();
    if (htlx!='3'){
        //初始化列表
        var oTable=new contractEdit_TableInit();
        oTable.Init();
    }
    if (htlx=='3'){
        //初始化列表
        var oTableFramework=new viewFrameworkContract_TableInit();
        oTableFramework.Init();
        var editOtableFramework=new editFrameworkContract_TableInit();
        editOtableFramework.Init();
    }
    if ($('#editContractForm #yhtbchtFlag').val()=='1'){
        var viewOriginalContract = new viewOriginalContract_TableInit();
        viewOriginalContract.Init();
    }
	creatContractInit();
	// 初始化页面
    //0.初始化fileinput
    var sign_params=[];
    sign_params.prefix=$('#editContractForm #urlPrefix').val();
    var oFileInput = new FileInput();
    oFileInput.Init("editContractForm","displayUpInfo",2,1,"sign_file",null,sign_params);
    var fjid=$("#editContractForm #fjid").val();
    if(fjid!=null&&fjid!=""){
        $("#editContractForm #ycfj").show();
        $("#editContractForm #fjsc").hide();
    }else{
        $("#editContractForm #ycfj").hide();
        $("#editContractForm #fjsc").show();
    }
	init();
	//所有下拉框添加choose样式
	jQuery('#editContractForm .chosen-select').chosen({width: '100%'});
    var htid=$("#editContractForm #htid").val();
    if (htid){
        var fkfs=$("#editContractForm #fkfs").val();
        var csdm=$("#editContractForm #"+fkfs).attr("csdm")
        if(csdm=="002"){//判断是否为预付部分
            $("#editContractForm #yfjediv").attr("style","display:block;")
            $("#editContractForm #yfrqdiv").attr("style","display:block;")
        }else{
            $("#editContractForm #yfjediv").attr("style","display:none;")
            $("#editContractForm #yfrqdiv").attr("style","display:none;")
        }
        var yfJson=$("#editContractForm #yfJson").val();
        if(yfJson){
            var parse = JSON.parse(yfJson);
            for(var i=0;i<parse.length;i++){
                $("#editContractForm #yfje"+i).val(parse[i].yfje);
                $("#editContractForm #yfrq"+i).val(parse[i].yfrq);
            }
        }
        if (htlx=='3'){
            getHisFj()
        }
    }
    if ("3"==htlx&&$("#editContractForm #bchtid").val()){
        $("#editContractForm #kjlx").attr("disabled", "disabled");
        $("#editContractForm #kjlx").trigger("chosen:updated");
    }
});