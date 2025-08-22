var sffqlld = $("#inspectionViewForm #sffqlld").text();
var flag = $("#inspectionViewForm #flag").val();
if(sffqlld=="1"){
	$("#inspectionViewForm #sffqlld").text("是");
}else{
	$("#inspectionViewForm #sffqlld").text("否");
}

var dhjyhwList_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#inspectionViewForm #dhjyhwList').bootstrapTable({
            url: $('#inspectionViewForm #urlPrefix').val()+'/inspectionGoods/pendingInspection/pagedataGetGoodsFromDhjyid',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#inspectionViewForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "wlbm",				//排序字段
            sortOrder: "desc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "wlbm",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
            	
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	return;
            },
        });
    };
    // 得到查询的参数
	oTableInit.queryParams = function(params){
		var map = {
				access_token:$("#ac_tk").val(),
				dhjyid:$("#inspectionViewForm #dhjyid").val()
		};
		return map;
	};
    return oTableInit;
};
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
    	field: 'htnbbh',
    	title: '合同编号',
    	titleTooltip:'合同编号',
    	width:'12%',
    	align:'left',
    	visible:true
    },{
    	field: 'wlbm',
    	title: '物料编码',
    	titleTooltip:'物料编码',
    	width:'8%',
    	align:'left',
    	formatter:wlbmformat,
    	visible:true
    },{
    	field: 'wlmc',
    	title: '物料名称',
    	titleTooltip:'物料名称',
    	width:'10%',
		formatter:wlmcformat,
    	align:'left',
    	visible:true
    },{
    	field: 'gg',
    	title: '物料规格',
    	titleTooltip:'物料规格',
    	width:'7%',
    	align:'left',
    	visible:true
    },{
    	field: 'jldw',
    	title: '计量单位',
    	titleTooltip:'计量单位',
    	width:'5%',
    	align:'left',
    	visible:true
    },{
    	field: 'ychh',
    	title: '货号',
    	titleTooltip:'货号',
    	width:'7%',
    	align:'left',
    	visible:true
    },{
    	field: 'sl',
    	title: '数量',
    	titleTooltip:'数量',
    	width:'5%',
    	align:'left',
    	visible:true
    },{
    	field: 'bgdh',
    	title: '报告单号',
    	titleTooltip:'报告单号',
    	width:'12%',
    	align:'left',
    	visible:true
    },{
    	field: 'qyl',
    	title: '取样量',
    	titleTooltip:'取样量',
    	width:'5%',
    	align:'left',
    	visible:true
    },{
    	field: 'jysl',
    	title: '借用数量',
    	titleTooltip:'借用数量',
    	width:'5%',
    	align:'left',
    	visible:true
    },{
    	field: 'qyrq',
    	title: '取样日期',
    	titleTooltip:'取样日期',
    	width:'10%',
    	align:'left',
    	visible:true
    },{
    	field: 'zjthsl',
    	title: '退回数量',
    	titleTooltip:'退回数量',
    	width:'5%',
    	align:'left',
    	visible:true
    },{
        field: 'fj',
        title: '附件',
        titleTooltip:'附件',
        width: '3%',
        align: 'left',
        formatter:fjformat,
        visible: true
    },{
    	field: 'hwid',
    	title: '货物id',
    	titleTooltip:'货物id',
    	width:'7%',
    	align:'left',
    	visible:false
    }
    
];
/**
 * 附件格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fjformat(value,row,index){
	var html = "<div id='fj_"+index+"'>"; 
	html += "<input type='hidden' id='hwfj_"+index+"' name='hwfj_"+index+"'/>";
	if(row.fjbj == "0"){
		html += "<a href='javascript:void(0);' onclick='uploadShoppingFile(\"" + index + "\",\""+row.hwid+"\")' >是</a>";
	}else{
		html += "<a href='javascript:void(0);' onclick='uploadShoppingFile(\"" + index + "\",\""+row.hwid+"\")' >否</a>";
	}
	
	
	html += "</div>";
	return html;
}
/**
 * 货物质量检验上传附件
 * @param index
 * @returns
 */
var currentUploadIndex = "";
function uploadShoppingFile(index,hwid) {
	if(index){
		var dhjyid = $("#inspectionViewForm #dhjyid").val();
		var fjids = $("#inspectionViewForm #fj_"+ index +" input").val();
		var url=$('#inspectionViewForm #urlPrefix').val()+"/inspectionGoods/pendingInspection/pagedataGetUploadFilePage?access_token=" + $("#ac_tk").val()+"&ywid="+dhjyid+"&zywid="+hwid+"&ckbj=1"+"&flag="+flag;
		if(fjids){
			url=url + "&fjids="+fjids;
		}
		currentUploadIndex = index;
	}
	$.showDialog(url, '附件内容', uploadFileConfig);
}
var uploadFileConfig = { 
	width : "800px",
	height : "500px",
	modalName	: "uploadFileModal",
	formName	: "ajaxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
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
function wlbmformat(value,row,index){
	var html = ""; 	
		html += "<a href='javascript:void(0);' onclick=\"queryByNewWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
	return html;
}
function queryByNewWlbm(wlid){
	var url=$("#inspectionViewForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
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
 * 物料名称格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wlmcformat(value,row,index){
	var html = "";
	html += "<a href='javascript:void(0);' onclick=\"queryByHwid('"+row.hwid+"')\">"+row.wlmc+"</a>";
	return html;
}
function queryByHwid(hwid){
	var url=$("#inspectionViewForm #urlPrefix").val()+"/inspectionGoods/inspectionGoods/pagedataGetGoodsView?hwid="+hwid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'货物信息',viewHwConfig);
}
var viewHwConfig={
	width		: "1000px",
	modalName	:"viewHwModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
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
        var url=$("#inspectionViewForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
        $.showDialog(url,'图片预览',JPGMaterConfig);
    }else if(type.toLowerCase()==".pdf"){
        var url= $("#inspectionViewForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',JPGMaterConfig);
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}
var JPGMaterConfig = {
    width: "800px",
    offAtOnce	: true,
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

//下载模板
function xz(fjid){
    jQuery('<form action="'+$("#inspectionViewForm #urlPrefix").val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}
$(function () {
	var tableInit = new dhjyhwList_TableInit();
	tableInit.Init();
});