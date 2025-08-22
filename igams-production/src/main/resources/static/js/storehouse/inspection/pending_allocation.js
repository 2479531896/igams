var t_map = [];
var t_index="";
var kwlist = [];
// 显示字段
var columnsArray = [
	{
		checkbox: true,
		width: '1%'
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
		field: 'wlid',
		title: '物料ID',
		width: '8%',
		align: 'left',
		visible: false
	},{				
		field: 'wlbm',
		title: '物料编码',
		width: '8%',
		align: 'left',
		visible: true
	}, {				
		field: 'wlmc',
		title: '物料名称',
		width: '10%',
		align: 'left',
		visible: true
	},  {
		field: 'ckqxmc',
		title: '仓库分类',
		width: '10%',
		align: 'left',
		visible: true
	}, {
		field: 'gg',
		title: '规格',
		width: '4%',
		align: 'left',
		visible: true
	}, {				
		field: 'jldw',
		title: '单位',
		width: '5%',
		align: 'left',
		visible: true
	}, {				
		field: 'dclsl',
		title: '待处理数量',
		width: '5%',
		align: 'left',
		visible: true
	},{				
		field: 'clsl',
		title: '处理数量',
		width: '5%',
		align: 'left',
		formatter:clslformat,
		visible: true
	},{
		field: 'cskwmc',
		title: '调出库位',
		titleTooltip:'调出库位',
		width: '8%',
		align: 'left',
		visible: true
	},{
		field: 'drkw',
		title: '调入库位',
		titleTooltip:'调入库位',
		formatter:pendingAllocation_kwformat,
		width: '8%',
		align: 'left',
		visible: true
    }];
var pendingEdit_TableInit = function () {
    var oTableInit = new Object();
    var zrck =  $('#pendingAllocationForm #zrck').val();
    if (zrck){
		$.ajax({
			async: false,
			type:'post',
			url:$('#editPutInStorageForm #urlPrefix').val()+"/storehouse/arrivalGoods/pagedataGetkwlist",
			cache: false,
			data: {"ckid":zrck,"access_token":$("#ac_tk").val()},
			dataType:'json',
			success:function(data){
				if(data.kwlist!=null && data.kwlist.length>0){
					kwlist=data.kwlist;
				}
			}
		});
	}
    //初始化Table
    oTableInit.Init = function () {
        $('#pendingAllocationForm #pendingTb_list').bootstrapTable({
            url: $('#pendingAllocationForm #urlPrefix').val()+$('#pendingAllocationForm #url').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#pendingAllocationForm #toolbar',                //工具按钮用哪个容器
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
            uniqueId: "wlid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
            	t_map = map;
            },
            onDblClickRow: function (row, $element) {
            	return;
            },
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function(params){
		var hwxxids =  $('#pendingAllocationForm #hwxxids').val();
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
			ids: hwxxids,
        };
    	return map;
    };
    return oTableInit;
};

/**
 * 库位格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function pendingAllocation_kwformat(value,row,index){
	var html="";
	html="<select name='kw' onchange=\"pendingAllocation_changekw('"+index+"')\" class='form-control chosen-select' validate='{required:true}' id='kw_"+index+"' style='padding:0px;'>";
	if(kwlist!=null && kwlist.length>0){
		html+="<option selected='true'>--请选择--</option>";
		for(var i=0;i<kwlist.length;i++){
			if(row.drkw){
				if(row.drkw==kwlist[i].ckid){
					html+="<option selected='true' id='"+kwlist[i].ckid+"' value='"+kwlist[i].ckid+"' index='"+i+"'>"+kwlist[i].ckmc+"</option>";
				}else{
					html+="<option id='"+kwlist[i].ckid+"' value='"+kwlist[i].ckid+"' index='"+i+"'>"+kwlist[i].ckmc+"</option>";
				}
			}else{
				html+="<option id='"+kwlist[i].ckid+"' value='"+kwlist[i].ckid+"' index='"+i+"'>"+kwlist[i].ckmc+"</option>";
				t_map.rows[index].cskw=kwlist[i].ckid;
			}
		}
	}else{
		html+="";
	}
	html+="</select>";
	return html;
}

function pendingAllocation_changekw(index){
	var kwid=$("#kw_"+index+"  option:selected").val();
	var kwmc=$("#kw_"+index+"  option:selected").text();
	t_map.rows[index].drkw=kwid;
	t_map.rows[index].kwmc=kwmc;
}
/**
 * 调拨数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function clslformat(value,row,index){
	var clsl = "0";
	row.clsl= clsl;
	var html= "<input id='clsl_"+index+"' validate='{required:true}' autocomplete='off' value='"+clsl+"' name='clsl_"+index+"' min='0' max='"+row.dclsl+"'  onblur=\"checkSum('"+index+"',this,\'clsl\')\" ></input>";
	return html;
}

function checkSum(index, e, clsl) {
	t_map.rows[index].clsl = e.value;
	if (e.value > t_map.rows[index].dclsl){
		$.alert("处理数量不能大于待处理数量！");
		$("#pendingAllocationForm #clsl_"+index).val("0");
		t_map.rows[index].clsl = 0;
	}
}


/**
 * 初始化页面
 * @returns
 */
function init(){
  	//添加日期控件
	laydate.render({
	   elem: '#dbrq'
	  ,theme: '#2381E9'
	});
}

$('#pendingAllocationForm').submit(function() {
	$('#pendingAllocationForm #cklb').attr("disabled",false);
	$('#pendingAllocationForm #rklb').attr("disabled",false);
	$('#pendingAllocationForm #zcck').attr("disabled",false);
	return false;
});
/**
 * 转出仓库改变事件
 * @returns
 */
function checkzcck(){
	let zrck = $('#pendingAllocationForm #zrck').val();
	if(zrck){
		let zcck = $('#pendingAllocationForm #zcck').val();
		if (zrck == zcck){
			$('#pendingAllocationForm #zrck option:first').prop("selected", 'selected');
			$("#pendingAllocationForm #zrck").trigger("chosen:updated");
			$.alert("转入仓库与转出仓库不能相同！");
			return
		}
	}
}
/**
 * 转入仓库改变事件
 * @returns
 */
function checkzrck(){
	let zcck = $('#pendingAllocationForm #zcck').val();
	if(zcck){
		let zrck = $('#pendingAllocationForm #zrck').val();
		if (zrck == zcck){
			$('#pendingAllocationForm #zrck option:first').prop("selected", 'selected');
			$("#pendingAllocationForm #zrck").trigger("chosen:updated");
			$.alert("转入仓库与转出仓库不能相同！");
			return
		}
	}
}
// /**
//  * 删除操作
//  * @param index
//  * @param event
//  * @returns
//  */
// function delWlInfo(index,event){
// 	t_map.rows.splice(index,1);
// 	$("#pendingAllocationForm #pendingTb_list").bootstrapTable('load',t_map);
// }

// /**
//  * 操作格式化
//  * @param value
//  * @param row
//  * @param index
//  * @returns
//  */
// function pendingAllocationForm_czformat(value,row,index){
// 	var html="";
// 	html = html+"<span style='margin-left:5px;height:24px !important;' class='btn btn-info' title='选择调拨明细' onclick=\"chooseDbmx('" + index + "',event)\" >详细</span>";
// 	html = html+"<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除物料信息' onclick=\"delWlInfo('" + index + "',event)\" >删除</span>";
// 	return html;
// }


/**
 * 选择申请人
 * @returns
 */
function chooseJsr() {
	var url = $('#pendingAllocationForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择申请人', chooseQrrConfig);
}
var chooseQrrConfig = { 
	width : "800px",
	height : "500px",
	modalName	: "chooseSqrModal",
	formName	: "pendingAllocationForm",
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
				var sel_row = $('#taskListFzrForm #pendingTb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#pendingAllocationForm #jsr').val(sel_row[0].yhid);
					$('#pendingAllocationForm #jsrmc').val(sel_row[0].zsxm);
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

function pendingAllocation_btnBind(){
	var sel_ck=$("#pendingAllocationForm #zrck");
	//仓库下拉框事件
	sel_ck.unbind("change").change(function(){
		pendingAllocation_ckEvent();
	});
}

//仓库下拉事件
function pendingAllocation_ckEvent(){
	var ckid=$("#pendingAllocationForm #zrck").val();
	if (ckid){
		$.ajax({
			type:'post',
			url:$('#pendingAllocationForm #urlPrefix').val()+"/storehouse/arrivalGoods/pagedataGetkwlist",
			cache: false,
			data: {"ckid":ckid,"access_token":$("#ac_tk").val()},
			dataType:'json',
			success:function(data){
				if(data.kwlist!=null && data.kwlist.length>0){
					kwlist=data.kwlist;
					$("#pendingAllocationForm #pendingTb_list").bootstrapTable('load',t_map);
				}else{
					$.confirm("该仓库下库位信息为空，请先进行维护！");
				}
			}
		});
	}
}


$(document).ready(function(){
	//初始化列表
	pendingAllocation_btnBind();
	pendingAllocation_ckEvent()
	var oTable=new pendingEdit_TableInit();
	oTable.Init();
	// 初始化页面
	init();	

	jQuery('#pendingAllocationForm .chosen-select').chosen({width: '100%'});
});