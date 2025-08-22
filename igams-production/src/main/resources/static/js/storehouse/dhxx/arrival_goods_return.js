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
		width: '2%',
        align: 'left',
        visible:true,
    }, {
        field: 'wlid',
        title: '物料ID',
        titleTooltip:'物料ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '6%',
        align: 'left',
        formatter:returnback_wlbmformat,
        visible: true
    }, {
        field: 'wlmc',
        title: '物料名称/规格',
        titleTooltip:'物料名称/规格',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'jldw',
        title: '单位/货号',
        titleTooltip:'单位/货号',
        width: '6%',
        align: 'left',
        visible: true
    }, {
        field: 'dhsl',
        title: '到货数量*',
        titleTooltip:'到货数量',
        width: '8%',
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
        width: '8%',
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
        width: '5%',
        align: 'left',
        visible: true,
    }, {
        field: 'jyrymc',
        title: '借用人员',
        titleTooltip:'借用人员',
        width: '5%',
        align: 'left',
        visible: true,
    }, {
        field: 'jysj',
        title: '借用时间',
        titleTooltip:'借用时间',
        width: '6%',
        align: 'left',
        visible: true
    }, {
        field: 'ghsl',
        title: '归还数量',
        titleTooltip:'归还数量',
        formatter:ghslformat,
        width: '8%',
        align: 'left',
        visible: true,
    }, {
        field: 'ghry',
        title: '归还人员',
        titleTooltip:'归还人员',
        formatter:ghryformat,
        width: '8%',
        align: 'left',
        visible: true,
    }, {
        field: 'ghsj',
        title: '归还时间',
        titleTooltip:'归还时间',
        width: '6%',
        align: 'left',
        formatter:ghsjformat,
        visible: true
    }];

var arrivalGoodsReturn_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#arrivalGoodsReturnbackForm #gh_list').bootstrapTable({
            url: $('#arrivalGoodsReturnbackForm #urlPrefix').val()+'/storehouse/arrivalGoods/pagedataGetArrivalGoodsGhList?dhid='+$("#arrivalGoodsReturnbackForm #dhid").val(),         //请求后台的URL（*）
//            url: $('#arrivalGoodsReturnbackForm #urlPrefix').val()+'/storehouse/arrivalGoods/pagedataArrivalgoodsdetailList?dhid='+$("#arrivalGoodsReturnbackForm #dhid").val(),
            method: 'get',                      //请求方式（*）
            toolbar: '#arrivalGoodsReturnbackForm #toolbar',                //工具按钮用哪个容器
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
//            		$("#arrivalGoodsReturnbackForm #dhmx_json").val(JSON.stringify(json));
            		$("#arrivalGoodsReturnbackForm #dhmxJson").val(JSON.stringify(t_map.rows));
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
            hwid: $("#arrivalGoodsReturnbackForm #hwid").val(),
            //搜索框使用
            //search:params.search
        };
    	return map;
    };
    return oTableInit;
};


/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function returnback_wlbmformat(value,row,index){
	var html = ""; 
	if($('#ac_tk').length > 0){
		html += "<a href='javascript:void(0);' onclick=\"returnback_queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
	}else{
		html += row.wlbm;
	}
	return html;
}
function returnback_queryByWlbm(wlid){
	var url=$("#arrivalGoodsReturnbackForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'物料信息',viewReturnbackWlConfig);	
}
var viewReturnbackWlConfig = {
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
 * 归还人员格式化
 */
function ghryformat(value,row,index){
//	var html = "<input id='jyry_"+index+"' value='"+row.jyry+"' name='jyry_"+index+"' style='width:70%;border-top-left-radius:5px;border-bottom-left-radius:5px;border:1px solid #D5D5D5;' validate='{djNumber:true}' onblur=\"checkSum('"+index+"',this,\'jyry\')\"></input><button type='button' class='glyphicon glyphicon-chevron-up' style='width:30%;top:0;font-weight:100;float:right;height:24px;border-top-right-radius:5px;border-bottom-right-radius:5px;border:0.2px solid black;' onclick=\"choseJyr('"+index+"',this,\'jyry\')\"></button>" +
//			"<input id='jyrymc_"+index+"' value='"+row.jyrymc+"' name='jyrymc_"+index+"' style='width:70%;border-top-left-radius:5px;border-bottom-left-radius:5px;border:1px solid #D5D5D5;' validate='{djNumber:true}' onblur=\"checkSum('"+index+"',this,\'jyrymc\')\"></input>";
	var html = "<input id='ghry_"+index+"' value='"+row.ghry+"' name='ghry_"+index+"' type='hidden'  onblur=\"checkSum('"+index+"',this,\'ghry\')\"></input><button type='button' class='glyphicon glyphicon-chevron-up' style='width:30%;top:0;font-weight:100;float:right;height:24px;border-top-right-radius:5px;border-bottom-right-radius:5px;border:0.2px solid black;' onclick=\"choseJyr('"+index+"',this,\'ghry\')\"></button>" +
	"<input id='ghrymc_"+index+"'" 
	if(row.ghrymc!=null && row.ghrymc!=""){
		html=html+"value='"+row.ghrymc+"'";
	}
	html=html+" name='ghrymc_"+index+"' style='width:70%;border-top-left-radius:5px;border-bottom-left-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'ghrymc\')\"></input>";
	return html;
}
var current;
function choseJyr(index,e,zdmc){
	current = index; 
	var url = $('#arrivalGoodsReturnbackForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage";
	$.showDialog(url,'选择归还人',choseJyrConfig);	
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
						$('#ghry_'+current).val(sel_row[0].yhid);
						$('#ghrymc_'+current).val(sel_row[0].zsxm);
						t_map.rows[current].ghry = sel_row[0].yhid;
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
 * 归还数量格式化
 */
function ghslformat(value,row,index){
	if(row.ghsl == null){
		row.ghsl = "";
	}
	var html = "<input id='ghsl_"+index+"' autocomplete='off' value='"+row.ghsl+"' name='ghsl_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{required:true,max:10000}' onblur=\"checkSum('"+index+"',this,\'ghsl\')\"></input>";
	return html;
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
		if (zdmc == 'jysj') {
			t_map.rows[index].jysj = e.value;			
		} else if (zdmc == 'ghry') {
			t_map.rows[index].ghry = e.value;
		} else if (zdmc == 'ghsl') {
			t_map.rows[index].ghsl = e.value;
		} 
	}
}


/**
 * 归还日期格式化
 */
function ghsjformat(value,row,index){
	if(row.ghsj==null){
		row.ghsj="";
	}
	var html="<input id='ghsj"+index+"' hwid='"+row.hwid+"' autocomplete='off' name='ghsj_"+index+"' class='ghsj'  value='"+row.ghsj+"' style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'ghsj\')\"><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='复制当前日期并替换所有'  onclick=\"copyTime('"+index+"',\'ghsj\')\"><span></input>";
	setTimeout(function() {
		laydate.render({
			elem: '#arrivalGoodsReturnbackForm #ghsj'+index
		  	,theme: '#2381E9'
		  	,btns: ['clear', 'confirm']
	  		,done: function(value, date, endDate){
	  			$('#arrivalGoodsReturnbackForm #ghsj'+index).val(value);
	  			t_map.rows[index].ghsj = value;
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
function copyTime(index,zdmc){
	var dqrq=$("#arrivalGoodsReturnbackForm #"+zdmc+""+index).val();
	if(dqrq==null || dqrq==""){
		$.confirm("请先选择日期！");
	}else{
		for(var i=0;i<t_map.rows.length;i++){
			$("#arrivalGoodsReturnbackForm #"+zdmc+""+i).val(dqrq);
		}
	}
}

$(document).ready(function(){
	laydate.render({
	   elem: '#arrivalGoodsReturnbackForm #dhrq'
	  ,theme: '#2381E9'
	});
	//初始化列表
	var oTable=new arrivalGoodsReturn_TableInit();
	oTable.Init();
	//所有下拉框添加choose样式
	jQuery('#arrivalGoodsReturnbackForm .chosen-select').chosen({width: '100%'});
});