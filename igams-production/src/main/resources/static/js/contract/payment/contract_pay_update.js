
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
		width: '8%',
		align: 'left',
		visible: true
	}, {
		field: 'htid',
		title: '付款/发票详情',
		titleTooltip:'付款/发票详情',
		formatter:viewformat,
		width: '5%',
		align: 'left',
		visible: true
	}, {
		field: 'zje',
		title: '合同金额',
		titleTooltip:'合同金额',
		width: '8%',
		align: 'left',
		formatter:payMod_zjeFormat,
		visible: true
	}, {
		field: 'yfje',
		title: '已付金额',
		titleTooltip:'已付金额',
		width: '8%',
		align: 'left',
		visible: true
	}, {
		field: 'wfje',
		title: '未付金额',
		titleTooltip:'未付金额',
		width: '8%',
		align: 'left',
		visible: true,
	}, {
		field: 'fkzje',
		title: '付款中金额',
		titleTooltip:'付款中金额',
		width: '8%',
		align: 'left',
		visible: true,
	}, {
		field: 'fkje',
		title: '付款金额',
		titleTooltip:'付款金额',
		width: '9%',
		align: 'left',
		formatter:fkjeformat,
		visible: true
	}];
var contractPayEdit_TableInit = function () {
	var oTableInit = new Object();
	//初始化Table
	oTableInit.Init = function () {
		$('#payMod_ajaxForm #tb_list').bootstrapTable({
			url: $('#payMod_ajaxForm #urlPrefix').val()+'/contract/payment/pagedataGetContractPayDetails',         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#payMod_ajaxForm #toolbar',                //工具按钮用哪个容器
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
			uniqueId: "htfkmxid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: columnsArray,
			onLoadSuccess: function (map) {
				if(map.rows){
					// 初始化json
					var json = [];
					for (var i = 0; i < map.rows.length; i++) {
						var sz = {"index":'',"htfkmxid":'',"htfkid":'',"fkje":'',"zje":'',"xzje":''};
						sz.index = i;
						sz.htfkmxid = map.rows[i].htfkmxid;
						sz.htfkid = map.rows[i].htfkid;
						sz.fkje = map.rows[i].fkje;
						sz.zje = map.rows[i].zje;
						sz.xzje = map.rows[i].xzje;
						json.push(sz);
					}
					$("#payMod_ajaxForm #fkmxJson").val(JSON.stringify(json));
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
			sortLastName: "htfkmxid", //防止同名排位用
			sortLastOrder: "asc", //防止同名排位用
			htfkid: $("#payMod_ajaxForm #htfkid").val(),
			//搜索框使用
			//search:params.search
		};
		return map;
	};
	return oTableInit;
};


/**
 * 查看详情格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function viewformat(value,row,index){
	var html="";
	if(row.htid!=null && row.htid!=''){
		html="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"queryByHtid('"+row.htid+"')\">"+"查看详情"+"</a>";
	}
	return html;
}

function queryByHtid(htid){
	var url=$("#payMod_ajaxForm #urlPrefix").val()+"/contract/contract/pagedataViewContract?htid="+htid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'合同信息',viewHtConfig);
}

var viewHtConfig={
	width		: "1500px",
	modalName	:"viewHtModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function payMod_zjeFormat(value,row,index){
    var zje = row.zje;
    if(row.xzje!=null && row.xzje!=''){
        zje=row.xzje;
    }
    return zje;
}
/**
 * 付款金额格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fkjeformat(value,row,index){
	if(row.fkje == null){
		row.fkje = "";
	}
	var html = "<input id='fkje_"+index+"' value='"+row.fkje+"' name='fkje_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;'  onblur=\"checkSum('"+index+"',this,\'fkje\')\"/>";
	return html;
}

/**
 * 验证付款金额格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("fkNumber", function (value, element){
	if(value && !/^\d+(\.\d{1,2})?$/.test(value)){
		$.error("请填写正确付款金额格式，可保留两位小数!");
	}
	return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");



// //付款金额事件，计算付款百分比
// function fkjeblurMod(){
// 	var fkje = $("#payMod_ajaxForm #fkje").val();
// 	var htzje = $("#payMod_ajaxForm #htzje").val();
// 	if(fkje==""){
// 		var obj = document.getElementById("fkbfb");
// 		obj.value=0+"%";
// 	}else if(htzje==""){
// 		var obj = document.getElementById("fkbfb");
// 		obj.value=100+"%";
// 	}else if(htzje=="0"){
// 		var obj = document.getElementById("fkbfb");
// 		obj.value=0+"%";
// 	}else{
// 		fkjeNum = parseFloat(fkje);
// 		htzjeNum = parseFloat(htzje);
// 		result = parseFloat(Math.round(fkjeNum/htzjeNum*10000)/100);
// 		bfbbj = parseFloat("100");
// 		if(result >bfbbj){
// 			var bfb="100%";
// 		}else{
// 			var bfb =result+'%';
// 		}
// 		var obj = document.getElementById("fkbfb");
// 		obj.value=bfb;
// 	}
//
// }

/**
 * 选择供应商列表
 * @returns
 */
function chooseGys(){
	var url=$('#payMod_ajaxForm #urlPrefix').val() + "/warehouse/supplier/pagedataSupplier?access_token=" + $("#ac_tk").val()+"&scbj="+"0";
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
					var khh = sel_row[0].khh;
					var zh = sel_row[0].zh;
					$("#payMod_ajaxForm #zfdxmc").val(gysmc);
					$("#payMod_ajaxForm #zfdx").val(gysid);
					$("#payMod_ajaxForm #zffkhh").val(khh);
					$("#payMod_ajaxForm #zffyhzh").val(zh);
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
	var url=$('#payMod_ajaxForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
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
					$('#payMod_ajaxForm #sqr').val(sel_row[0].yhid);
					$('#payMod_ajaxForm #sqrmc').val(sel_row[0].zsxm);
					//保存操作习惯
					$.ajax({
						type:'post',
						url:$('#payMod_ajaxForm #urlPrefix').val() + "/common/habit/commonModUserHabit",
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
 * 选择申请部门列表
 * @returns
 */
function chooseSqbm(){
	var url=$('#payMod_ajaxForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
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
					$('#payMod_ajaxForm #sqbm').val(sel_row[0].jgid);
					$('#payMod_ajaxForm #sqbmmc').val(sel_row[0].jgmc);
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
 * 选择用款部门列表
 * @returns
 */
function chooseYkbm(){
	var url=$('#payMod_ajaxForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择用款部门',addYkbmConfig);
}
var addYkbmConfig = {
	width		: "800px",
	modalName	:"addYkbmModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#payMod_ajaxForm #ykbm').val(sel_row[0].jgid);
					$('#payMod_ajaxForm #ykbmmc').val(sel_row[0].jgmc);
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
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkSum(index, e, zdmc) {
	var data=JSON.parse($("#payMod_ajaxForm #fkmxJson").val());
	for(var i=0;i<data.length;i++){
		if(data[i].index == index){
			if(zdmc=='fkje'){
				data[i].fkje=e.value;
			}
		}
	}
	$("#payMod_ajaxForm #fkmxJson").val(JSON.stringify(data));
}


$(function() {
	//初始化列表
	var oTable=new contractPayEdit_TableInit();
	oTable.Init();
	//添加日期控件
	laydate.render({
		elem: '#payMod_ajaxForm #fkrq'
		,theme: '#2381E9'
	});
	laydate.render({
		elem: '#payMod_ajaxForm #zwzfrq'
		,theme: '#2381E9'
	});
	// 所有下拉框添加choose样式
	jQuery('#payMod_ajaxForm .chosen-select').chosen({width: '100%'});
});