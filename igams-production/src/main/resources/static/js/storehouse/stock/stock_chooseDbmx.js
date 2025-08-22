var th_map = [];
// 显示字段
var columnsArray = [{
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
        title: '货物id',
        titleTooltip:'货物id',
        width: '4%',
        align: 'left',
        visible: false
    }, {
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'wlmc',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '8%',
        align: 'left',
        visible: true
    }, {
        field: 'scph',
        title: '生产批号',
        titleTooltip:'生产批号',
        width: '6%',
        align: 'left',
        visible: true
    }, {
        field: 'dbsl',
        title: '调拨数量',
        titleTooltip:'调拨数量',
        formatter:chooseDbmx_dbslformat,
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'klsl',
        title: '库存量',
        titleTooltip:'库存量',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'kwmc',
        title: '调出库位',
        titleTooltip:'调出库位',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'drkw',
        title: '调入库位',
        titleTooltip:'调入库位',
        formatter:chooseDbmx_dckwformat,
        width: '6%',
        align: 'left',
        visible: true
    }];
var chooseDbmx_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#chooseDbmx_formSearch #chooseDbmx_list').bootstrapTable({
            url: $('#chooseDbmx_formSearch #urlPrefix').val()+"/storehouse/stock/pagedataGetHwxxList",         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#chooseDbmx_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "scph",					//排序字段
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
            uniqueId: "hwid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
        		th_map = map;  	
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
            wlid: $("#chooseDbmx_formSearch #wlid").val(),
            ckid: $("#allocationForm #zcck").val(),
            //搜索框使用
            //search:params.search
        };
    	return map;
    };
    return oTableInit;
};

/**
 * 调出库位
 * @param value
 * @param row
 * @param index
 * @returns
 */
function chooseDbmx_dckwformat(value,row,index){
    let kwlists = $("#chooseDbmx_formSearch #kwlist").val();
    let kwlist = JSON.parse(kwlists);
    let drkw= row.drkw;
    let html="";
    let dbmx_json =t_map.rows[t_index].dbmx_json;
    if (dbmx_json && dbmx_json.length > 0){
        var jsarr=JSON.parse(dbmx_json);
        for (var j = 0; j < jsarr.length; j++) {
            if(row.hwid==jsarr[j].hwid){
                row.drkw=jsarr[j].drkw;
                drkw = jsarr[j].drkw;
            }
        }
    }
    html +="<div id='drkwdiv_"+index+"' name='drkwdiv' style='width:85%;display:inline-block'>";
    html += "<select id='drkw_"+index+"' name='drkw_"+index+"' class='form-control chosen-select' ";
    if (row.dbsl != "0"){
        html += "validate='{required:true}'";
    }
    html +=   "onblur=\"checkdrkw('"+index+"',this)\">";
    html += "<option value=''>--请选择--</option>";
    for(var i = 0; i < kwlist.length; i++) {
        html += "<option id='"+kwlist[i].ckid+"' value='"+kwlist[i].ckid+"'";
        if(drkw!=null && drkw!=""){
            if(drkw==kwlist[i].ckid){
                html += "selected"
            }
        }
        html += ">"+kwlist[i].ckdm+"--"+kwlist[i].ckmc+"</option>";
    }
    html +="</select></div>";

    return html;
}

function checkdrkw(index, e){
    th_map.rows[index].drkw = e.value;
}
/**
 * 调拨数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function chooseDbmx_dbslformat(value,row,index){
	var dbsl = "0";
	var dbmx_json =t_map.rows[t_index].dbmx_json;
	if (dbmx_json && dbmx_json.length > 0){
		var jsarr=JSON.parse(dbmx_json);
		for (var j = 0; j < jsarr.length; j++) {
			if(row.hwid==jsarr[j].hwid){
			    dbsl = jsarr[j].dbsl;
			}
		}
	}
    row.dbsl= dbsl;
	var html= "<input id='dbsl_"+index+"' autocomplete='off' value='"+dbsl+"' name='dbsl_"+index+"' min='0' max='"+row.klsl+"'  onblur=\"checkSum('"+index+"',this,\'dbsl\')\" ></input>";
	return html;	
}

function checkSum(index, e, dbsl) {
	th_map.rows[index].dbsl = e.value;
	if (e.value != "0"){
        $("#chooseDbmx_formSearch #drkw_"+index).attr("validate","{required:true}");
    }else{
        $("#chooseDbmx_formSearch #drkw_"+index).removeAttr("validate");
    }
}

$(document).ready(function(){
	//初始化列表
	var oTable=new chooseDbmx_TableInit();
	oTable.Init();
	// 初始化页面
	//init();	
	//所有下拉框添加choose样式
	jQuery('#chooseDbmx_formSearch .chosen-select').chosen({width: '100%'});
});