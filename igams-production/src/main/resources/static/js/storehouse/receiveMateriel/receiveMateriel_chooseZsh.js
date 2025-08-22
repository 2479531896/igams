var th_map = [];
var zt=$("#chooseZsh_formSearch #zt").val();

// 显示字段
var columnsArray = [{
    	title: '序',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序',
		width: '1%',
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
        field: 'llmxid',
        title: '领料明细id',
        titleTooltip:'领料明细id',
        width: '4%',
        align: 'left',
        visible: false
    },{
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '3%',
        align: 'left',
        visible: true
    }, {
        field: 'wlmc',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'gg',
        title: '规格',
        titleTooltip:'规格',
        width: '8%',
        align: 'left',
        visible: true
    }, {
        field: 'jldw',
        title: '单位',
        titleTooltip:'单位',
        width: '2%',
        align: 'left',
        visible: true
    },{
        field: 'dhdh',
        title: '到货单号',
        titleTooltip:'到货单号',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'rkdh',
        title: '入库单号',
        titleTooltip:'入库单号',
        width: '8%',
        align: 'left',
        visible: false
    },{
        field: 'zsh',
        title: '追溯号',
        titleTooltip:'追溯号',
        width: '4%',
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
        field: 'qgmxbz',
        title: '请购备注',
        titleTooltip:'请购备注',
        width: '20%',
        align: 'left',
        visible: true
    }, {
        field: 'kcl',
        title: '库存数',
        titleTooltip:'库存数',
        width: '3%',
        align: 'left',
        visible: true
    }, {
        field: 'klsl',
        title: '可请领数',
        titleTooltip:'可请领数',
        width: '3%',
        align: 'left',
        formatter:chooseZsh_kqlsformat,
        visible: true
    },{
        field: 'qlsl',
        title: '请领数量',
        titleTooltip:'请领数量',
        width: '3%',
        align: 'left',
        formatter:chooseZsh_qlslformat,
        visible: true
    }];
var chooseZsh_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#chooseZsh_formSearch #chooseZsh_list').bootstrapTable({
            url: $('#chooseZsh_formSearch #urlPrefix').val()+"/storehouse/receiveMateriel/pagedataQueryHwxx?copyflg="+$('#editPickingForm #copyflg').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#chooseZsh_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "dhdh",					//排序字段
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
            wlid: $("#chooseZsh_formSearch #wlid").val(),
            llid: $("#chooseZsh_formSearch #llid").val(),
            ckid: $("#chooseZsh_formSearch #ckid").val(),
            //搜索框使用
            //search:params.search
        };
    	return getChooseZshSearchData(map);
    };
    return oTableInit;
};


//条件搜索
function getChooseZshSearchData(map){
	var cxtj=$("#chooseZsh_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#chooseZsh_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr
	}else if(cxtj=="1"){
		map["zsh"]=cxnr
	}else if(cxtj=="2"){
		map["scph"]=cxnr
	}else if(cxtj=="3"){
		map["dhdh"]=cxnr
	}else if(cxtj=="4"){
		map["rkdh"]=cxnr
	}
	return map;
}
/**
 * 请领数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function chooseZsh_qlslformat(value,row,index){
	var qlsl = "0";
	row.qqls=row.qlsl;
	var hwllmx_json = $("#editPickingForm #hwllmx_json").val();
	if (hwllmx_json && hwllmx_json!=""){
		var jsarr=JSON.parse(hwllmx_json);
		for (var j = 0; j < jsarr.length; j++) {
			if(row.hwid==jsarr[j].hwid){
				row.qlsl=jsarr[j].qlsl;
			}
		}
		if(row.qlsl!="" && row.qlsl!=null){
			qlsl =row.qlsl;
		}
	}else{
		var llid=$("#chooseZsh_formSearch #llid").val();
		if(llid!=null && llid!=""){
			if(row.qlsl){
				qlsl =row.qlsl;
			}
		}
	}
    var klsl = row.klsl;
    if (zt=='10'&&qlsl){
        klsl = Number(klsl)+Number(row.qlsl)
    }
	var html= "<input id='qlsl_"+index+"' autocomplete='off' value='"+qlsl+"' name='qlsl_"+index+"' min='0' max='"+klsl+"'  onblur=\"checkSum('"+index+"',this,\'qlsl\')\" ></input>";
	return html;
}/**
 * 可请领数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function chooseZsh_kqlsformat(value,row,index){
    var klsl = row.klsl;
	if (zt=='10'&&row.qlsl){
        klsl = Number(klsl)+Number(row.qlsl)
    }
	return klsl;
}

function checkSum(index, e, qlsl) {
	th_map.rows[index].qlsl = e.value;
}

jQuery.validator.addMethod("qlslNumber", function (value, element){
	if(!/^\d+(\.\d{1,2})?$/.test(value)){
		$.error("请填写正确数量格式，可保留两位小数!");
	}
    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");


var chooseZsh_oButtton=function(){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#chooseZsh_formSearch #btn_query");//模糊查询（待审核）

    	/*-----------------------模糊查询(审核列表)------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				chooseZsh_Result(true);
    		});
		}
	}
	return oInit;
}


function chooseZsh_Result(isTurnBack){
	if(isTurnBack){
		$('#chooseZsh_formSearch #chooseZsh_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#chooseZsh_formSearch #chooseZsh_list').bootstrapTable('refresh');
	}
}


$(document).ready(function(){
	//初始化列表
	var oTable=new chooseZsh_TableInit();
	oTable.Init();

	var oButtonInit = new chooseZsh_oButtton();
	oButtonInit.Init();
	// 初始化页面
	//init();
	//所有下拉框添加choose样式
	jQuery('#chooseZsh_formSearch .chosen-select').chosen({width: '100%'});
});