var progressWait_turnOff=true;
var progressWait_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#progressWait_formSearch #progress_wait_tb_list').bootstrapTable({
            url: $('#progressWait_formSearch #urlPrefix').val() + '/progress/progress/pageGetListWaitProgress',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#progressWait_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
//            paginationDetailHAlign: " hidden", //不显示底部文字
            sortable: true,                     //是否启用排序
            sortName: "lx",				//排序字段
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
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "xqjhmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
				width: '2%',
            }, {
                field: 'cpxqid',
                title: '成本需求ID',
                width: '8%',
                align: 'left',
                visible: false
            }, 
            {
                field: 'xqdh',
                title: '单号',
                titleTooltip:'单号',
                width: '10%',
                align: 'left',
				sortable: true,
                visible: true
            }, {
                field: 'sqrmc',
                title: '申请人',
                titleTooltip:'申请人',
                width: '6%',
                align: 'left',
				visible: true
            },
            {
                field: 'sqbmmc',
                title: '申请部门',
                titleTooltip:'申请部门',
                width: '6%',
                align: 'left',
                visible: true,
            }, {
                field: 'xqrq',
                title: '需求日期',
                titleTooltip:'需求日期',
                width: '6%',
                align: 'left',
				sortable: true,
                visible: true
            }, {
                field: 'wlbm',
                title: '物料编码',
                titleTooltip:'物料编码',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'wlmc',
                title: '物料名称',
                titleTooltip:'物料名称',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'scs',
                title: '生产商',
                titleTooltip:'生产商',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'gg',
                title: '规格',
                titleTooltip:'规格',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'jldw',
                title: '单位',
                titleTooltip:'单位',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'sl',
                title: '需求数量',
                titleTooltip:'需求数量',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'scsl',
                title: '生产数量',
                titleTooltip:'生产数量',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'kcl',
                title: '库存量',
                titleTooltip:'库存量',
                width: '6%',
                align: 'left',
                formatter:clickKcl,
                visible: true
             },{
                field: 'yq',
                title: '批次和批量要求',
                titleTooltip:'批次和批量要求',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'sczt',
                title: '是否生产',
                titleTooltip:'是否生产',
                width: '6%',
                align: 'left',
                formatter:scztFormat,
                visible: true
                }, {
                field: 'bz',
                title: '备注',
                titleTooltip:'备注',
                width: '12%',
                align: 'left',
                visible: true
			}],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {

            },
        });
    };
    //得到查询的参数
    oTableInit.queryParams = function(params){
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   //页面大小
        	pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "lrsj", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            czbs: $("#progressWait_formSearch #czbs").val() //防止同名排位用
        };

        return getProgressWaitSearchData(map);
    };
    return oTableInit;
};

function searchWaitProgressResult(isTurnBack){
    //关闭高级搜索条件
    $("#progressWait_formSearch #searchMore").slideUp("low");
    progressWait_turnOff=true;
    $("#progressWait_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#progressWait_formSearch #progress_wait_tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#progressWait_formSearch #progress_wait_tb_list').bootstrapTable('refresh');
	}
}
/**
 * 生产状态
 */
function scztFormat(value,row,index) {
    if (row.sczt == '1') {
        return '需要生产';
    }else if (row.sczt == '0')
        return "无需生产";
    else return "";

}
function clickKcl(value,row, index) {
    if (!row.wlid){
        return "";
    }else {
        if (row.kcl){
            var url=$('#progressWait_formSearch #urlPrefix').val() +"/progress/progress/pagedataHwxx?wlid="+row.wlid;
            return "<a href='javascript:void(0);' onclick='viewHwxx(\"" + url + "\")' >"+row.kcl+"</a>";
        }
        return;
    }
}
function viewHwxx(url) {

    $.showDialog(url,'货物信息',viewHwxxConfig);
}
/*查看送检信息模态框*/
var viewHwxxConfig = {
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
function getProgressWaitSearchData(map){
	var cxtj = $("#progressWait_formSearch #cxtj").val();
	// 查询条件
	var cxnr = $.trim(jQuery('#progressWait_formSearch #cxnr').val());
	// '0':'物料编号','1':'物料名称','2':'生产商','3':'货号'
	if (cxtj == "0") {
		map["entire"] = cxnr;
	}else if (cxtj == "1") {
		map["xqdh"] = cxnr;
	}else if (cxtj == "2") {
		map["sqbmmc"] = cxnr;
	}else if (cxtj == "3") {
		map["sqrmc"] = cxnr;
	}else if (cxtj == "4") {
		map["wlbm"] = cxnr;
    }else if (cxtj == "5") {
    	map["wlmc"] = cxnr;
	}
	// 需求开始日期
	var xqrqstart = jQuery('#progressWait_formSearch #xqrqstart').val();
	map["xqrqstart"] = xqrqstart;
	// 需求结束日期
	var xqrqend = jQuery('#progressWait_formSearch #xqrqend').val();
	map["xqrqend"] = xqrqend;
    // 生产状态多
    var sczts = jQuery('#progressWait_formSearch #sczt_id_tj').val();
    map["sczts"] = sczts.replace(/'/g, "");

	return map;
}


var produceProgressConfig = {
	width		: "300px",
	height		: "50px",
    modalName	:"produceProgressModal",
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

//按钮动作函数
function progressDealById(id,sfsc,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $('#progressWait_formSearch #urlPrefix').val() + tourl; 
	if(action =='produce'){
        $.showDialog(tourl+"?xqjhmxid="+id+"&sfsc="+sfsc,'生产',produceProgressConfig);
	}
}


var progressWait_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_query = $("#progressWait_formSearch #btn_query");
        var btn_produce = $("#progressWait_formSearch #btn_produce");
        var btn_mod=$("#progressWait_formSearch #btn_mod");// 修改
        //添加日期控件
    	laydate.render({
    	   elem: '#xqrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#xqrqend'
    	  ,theme: '#2381E9'
    	});
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchWaitProgressResult(true);
            });
        }
    	//绑定搜索发送功能
        btn_produce.unbind("click").click(function(){
    		var sel_row = $('#progressWait_formSearch #progress_wait_tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			progressDealById(sel_row[0].xqjhmxid,sel_row[0].sczt,"produce",btn_produce.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
        /**显示隐藏**/
        $("#progressWait_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(progressWait_turnOff){
                $("#progressWait_formSearch #searchMore").slideDown("low");
                progressWait_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#progressWait_formSearch #searchMore").slideUp("low");
                progressWait_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
        /* ------------------------------删除领料信息-----------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#progressWait_formSearch #progress_wait_tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                var lxs="";
                var sczts="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    if (sel_row[i].xqjhmxid){
                        ids= ids + ","+ sel_row[i].xqjhmxid;
                        lxs=lxs + ","+ sel_row[i].lx;
                        sczts=sczts + ","+ sel_row[i].sczt;
                    }
                }
                ids=ids.substr(1);
                lxs=lxs.substr(1);
                sczts=sczts.substr(1);
                $.confirm('您确定要修改所选择的信息的生产状态吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $("#progressWait_formSearch #urlPrefix").val()+btn_mod.attr("tourl");
                        jQuery.post(url,{ids:ids,lxs:lxs,sczts:sczts,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchWaitProgressResult();
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
        });
    };

    return oInit;
};


$(function(){
    addTj('sczt',"1",'progressWait_formSearch');
    //1.初始化Table
    var oTable = new progressWait_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new progressWait_ButtonInit();
    oButtonInit.Init();
	//所有下拉框添加choose样式
	jQuery('#progressWait_formSearch .chosen-select').chosen({width: '100%'});
	
});
