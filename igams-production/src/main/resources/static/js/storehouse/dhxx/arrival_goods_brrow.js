var t_map = [];
var kwlist= [];
//显示字段
var columnsArray = [
	{
    	title: '序',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序',
		width: '1%',
        align: 'left',
        visible:true,
    }, {
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '5%',
        align: 'left',
        formatter:brrow_wlbmformat,
        visible: true
    }, {
        field: 'wlmc',
        title: '物料名称/规格',
        titleTooltip:'物料名称/规格',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'jldw',
        title: '单位/货号',
        titleTooltip:'单位/货号',
        width: '3%',
        align: 'left',
        visible: true
    }, {
        field: 'dhsl',
        title: '到货数量*',
        titleTooltip:'到货数量',
        width: '3%',
        align: 'left',
        visible: true
    }, {
        field: 'zsh',
        title: '追溯号*',
        titleTooltip:'追溯号',
        width: '5%',
        align: 'left',
        visible: true,
    }, {
        field: 'scph',
        title: '生产批号',
        titleTooltip:'生产批号',
        width: '5%',
        align: 'left',
        visible: true,
    },{
        field: 'ckmc',
        title: '库位',
        titleTooltip:'库位',
        width: '5%',
        align: 'left',
        visible: true
    }, {
        field: 'dhid',
        title: '到货ID',
        titleTooltip:'到货ID',
        width: '8%',
        align: 'left',
        visible: false,
    }, {
        field: 'dhdh',
        title: '到货单号',
        titleTooltip:'到货单号',
        width: '5%',
        align: 'left',
        visible: false,
    }, {
        field: 'hwid',
        title: '货物ID',
        titleTooltip:'货物ID',
        width: '8%',
        align: 'left',
        visible: false,
    }, {
        field: 'jysl',
        title: '借用数量',
        titleTooltip:'借用数量',
        width: '3%',
        align: 'left',
//        formatter:jyslformat,
        visible: true,
    }, {
        field: 'jyry',
        title: '借用人员',
        titleTooltip:'借用人员',
        formatter:jyryformat,
        width: '4%',
        align: 'left',
        visible: true,
    }, {
        field: 'jysj',
        title: '借用时间',
        titleTooltip:'借用时间',
        width: '4%',
        align: 'left',
        formatter:jysjformat,
        visible: true
    }];

var arrivalGoodsBorrow_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#arrivalGoodsBorrowForm #jy_list').bootstrapTable({
            url: $('#arrivalGoodsBorrowForm #urlPrefix').val()+'/storehouse/arrivalGoods/pagedataGetArrivalGoodsJyList?dhid='+$("#arrivalGoodsBorrowForm #dhid").val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#arrivalGoodsBorrowForm #toolbar',                //工具按钮用哪个容器
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
            	if(t_map.rows){
            		// 初始化dhmx_json
            		var json = [];
            		for (var i = 0; i < t_map.rows.length; i++) {
            			var sz = {"hwid":''};
						sz.hwid = t_map.rows[i].hwid;
						json.push(sz);
    				}
//            		$("#arrivalGoodsBorrowForm #dhmx_json").val(JSON.stringify(json));
            		$("#arrivalGoodsBorrowForm #dhmxJson").val(JSON.stringify(t_map.rows));
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
            hwid: $("#arrivalGoodsBorrowForm #hwid").val(),
            //搜索框使用
            //search:params.search
        };
    	return map;
    };
    return oTableInit;
};

/**
 * 借用数量格式化
 */
function jyslformat(value,row,index){
	if(row.jysl == null){
		row.jysl = "";
	}
	var html = "<input id='jysl_"+index+"' autocomplete='off' value='"+row.jysl+"' name='jysl_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{required:true,max:10000}' onblur=\"checkSum('"+index+"',this,\'jysl\')\"></input>";
	return html;
}

/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function brrow_wlbmformat(value,row,index){
	var html = ""; 
	if($('#ac_tk').length > 0){
		html += "<a href='javascript:void(0);' onclick=\"brrow_queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
	}else{
		html += row.wlbm;
	}
	return html;
}
function brrow_queryByWlbm(wlid){
	var url=$("#arrivalGoodsBorrowForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'物料信息',viewBrrowWlConfig);	
}
var viewBrrowWlConfig = {
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
 * 借用格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function jyryformat(value,row,index){
	var html = "<input id='jyry_"+index+"' value='"+row.jyry+"' name='jyry_"+index+"' type='hidden'></input><button type='button' class='glyphicon glyphicon-chevron-up' style='width:30%;top:0;font-weight:100;float:right;height:24px;border-top-right-radius:5px;border-bottom-right-radius:5px;border:0.2px solid black;' onclick=\"choseJyr('"+index+"',this,\'jyry\')\"     onblur=\"checkSum('"+index+"',this,\'jyry\')\"></button>" +
	"<input id='jyrymc_"+index+"'" 
	if(row.jyrymc!=null && row.jyrymc!=""){
		html=html+"value='"+row.jyrymc+"'";
	}
	html=html+" name='jyrymc_"+index+"' style='width:70%;border-top-left-radius:5px;border-bottom-left-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'jyrymc\')\"></input>";
					
	return html;
}
var current;
function choseJyr(index,e,zdmc){
	current = index; 
	var url = $('#arrivalGoodsBorrowForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage";
	$.showDialog(url,'选择借用人',choseJyrConfig);	
} 

var choseJyrConfig = {
		width		: "800px",
		modalName	:"addJyrModal",
		offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
					if(sel_row.length==1){
						$('#jyry_'+current).val(sel_row[0].yhid);
						$('#jyrymc_'+current).val(sel_row[0].zsxm);
						t_map.rows[current].jyry = sel_row[0].yhid;
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
	if(t_map.rows.length > index){
		if (zdmc == 'jysj') {
			t_map.rows[index].jysj = e.value;			
		} else if (zdmc == 'jyry') {
			t_map.rows[index].jyry = e.value;
		} 
	}
}


/**
 * 生产日期格式化
 */
function jysjformat(value,row,index){
	if(row.jysj==null){
		row.jysj="";
	}
	var html="<input id='jysj"+index+"' hwid='"+row.hwid+"' autocomplete='off' name='jysj_"+index+"' class='jysj'  value='"+row.jysj+"' style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'jysj\')\"></input><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='复制当前日期并替换所有'  onclick=\"BorrowCopyTime('"+index+"')\"><span>";
	setTimeout(function() {
		laydate.render({
			elem: '#arrivalGoodsBorrowForm #jysj'+index
		  	,theme: '#2381E9'
		  	,btns: ['clear', 'confirm']
	  		,done: function(value, date, endDate){
	  			$('#arrivalGoodsBorrowForm #jysj'+index).val(value);
	  			t_map.rows[index].jysj = value;
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
function BorrowCopyTime(index,zdmc){
	var dqrq=$("#arrivalGoodsBorrowForm #"+zdmc+""+index).val();
	if(dqrq==null || dqrq==""){
		$.confirm("请先选择日期！");
	}else{
		for(var i=0;i<t_map.rows.length;i++){
			$("#arrivalGoodsBorrowForm #"+zdmc+""+i).val(dqrq);
		}
	}
}

$(document).ready(function(){
	//初始化列表
	var oTable=new arrivalGoodsBorrow_TableInit();
	oTable.Init();
	//所有下拉框添加choose样式
	jQuery('#arrivalGoodsBorrowForm .chosen-select').chosen({width: '100%'});
});