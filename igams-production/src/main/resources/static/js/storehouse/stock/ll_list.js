var ll_turnOff=true;
var ll_TableInit = function () {
var oTableInit = new Object();	    
	// 初始化Table
	oTableInit.Init = function () {
		$('#ll_formSearch #ll_list').bootstrapTable({
			url: $("#ll_formSearch #urlPrefix").val()+'/storehouse/storehouse/pageGetListLl',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#ll_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"llrq",					// 排序字段
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
			showColumns: true,                  // 是否显示所有的列
			showRefresh: true,                  // 是否显示刷新按钮
			minimumCountColumns: 2,             // 最少允许的列数
			clickToSelect: true,                // 是否启用点击选中行
			// height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "llid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮                 								
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true,
				width: '1%'
			},{
            	title: '序号',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '2%',
                align: 'left',
                visible:true
            },{
                field: 'llrq',
                title: '领料日期',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zsxm',
                title: '领料人',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'qlsl',
                title: '领取数量',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wlbm',
                title: '物料编码',
                width: '4%',
                align: 'left',
                formatter:llwlbmformat,
                sortable: true,
                visible: true
            },{
				field: 'wlmc',
				title: '物料名称',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
                field: 'scph',
                title: '生产批号',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zsh',
                title: '追溯号',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
				field: 'ckmc',
				title: '仓库',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'ychh',
				title: '货号',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			},{				
				field: 'scs',
				title: '生产商',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			},{				
				field: 'gg',
				title: '规格',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'jldw',
				title: '单位',
				width: '2%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'lb',
				title: '物料类别',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: false
			},{				
				field: 'zlb',
				title: '物料子类别',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: false
			},{
                field: 'bz',
                title: '备注',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            }],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				llById(row.llid,'view',$("#ll_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#ll_formSearch #ll_list").colResizable({
			liveDrag:true, 
			gripInnerHtml:"<div class='grip'></div>", 
			draggingClass:"dragging", 
			resizeMode:'fit', 
			postbackSafe:true,
			partialRefresh:true
		})
	}
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "llxx.llid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
			jsid: $("#ll_formSearch #jsid").val()
            // 搜索框使用
            // search:params.search
        };
		return llSearchData(map);
	}
	return oTableInit
}

function llwlbmformat(value,row,index){
    var html = "";
    if(row.wlbm==null){
        html += "<span></span>"
    }else{
        html += "<a href='javascript:void(0);' onclick=\"queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
    }
    return html;
}
function queryByWlbm(wlid){
    var url=$("#ll_formSearch #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
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

// 根据查询条件查询
function llSearchData(map){
	var cxtj=$("#ll_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#ll_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr;
	}else if(cxtj=="1"){
		map["wlmc"]=cxnr;
	}else if(cxtj=="2"){
		map["wlbm"]=cxnr;
	}else if(cxtj=="3"){
        map["zsh"]=cxnr;
    }else if(cxtj=="4"){
        map["scph"]=cxnr;
    }else if(cxtj=="5"){
        map["zsxm"]=cxnr;
    }else if(cxtj=="6"){
        map["bz"]=cxnr;
    }
    // 创建开始日期
    var llrqstart = jQuery('#ll_formSearch #llrqstart').val();
    map["llrqstart"] = llrqstart;

    // 创建结束日期
    var llrqend = jQuery('#ll_formSearch #llrqend').val();
    map["llrqend"] = llrqend;
	// 仓库
	var cks = jQuery('#ll_formSearch #ck_id_tj').val();
	map["cks"] = cks.replace(/'/g, "");;
	return map;
}


//根据仓库货物ID查询仓库信息
function llById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#ll_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?llid=" +id;
		$.showDialog(url,'领料详细信息',viewllConfig);
	}
}

var viewllConfig = {
	width		: "1600px",
	modalName	: "viewllModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	} 
};

var ll_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#ll_formSearch #btn_view");
		var btn_query = $("#ll_formSearch #btn_query");
		var btn_del = $("#ll_formSearch #btn_del");
		/*--------------------------------模糊查询---------------------------*/    	
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			stockResult(true);
    		});
    	}
  
       /* ---------------------------查看-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#ll_formSearch #ll_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			llById(sel_row[0].llid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});

    	btn_del.unbind("click").click(function(){
            var sel_row = $('#ll_formSearch #ll_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].llid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $('#ll_formSearch #urlPrefix').val()+btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        stockResult();
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
		/*-----------------------显示隐藏------------------------------------*/
    	$("#ll_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(ll_turnOff){
    			$("#ll_formSearch #searchMore").slideDown("low");
    			ll_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#ll_formSearch #searchMore").slideUp("low");
    			ll_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
    	
    };
    	return oInit;
};

    
function stockResult(isTurnBack){
	//关闭高级搜索条件
	$("#ll_formSearch #searchMore").slideUp("low");
	ll_turnOff=true;
	$("#ll_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#ll_formSearch #ll_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#ll_formSearch #ll_list').bootstrapTable('refresh');
	}
}


$(function(){
	// 1.初始化Table
	var oTable = new ll_TableInit();
	oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new ll_ButtonInit();
    oButtonInit.Init();

	// 所有下拉框添加choose样式
	jQuery('#ll_formSearch .chosen-select').chosen({width: '100%'});
	
	$("#ll_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	laydate.render({
       elem: '#ll_formSearch #llrqstart'
      ,theme: '#2381E9'
        ,trigger: 'click'
    });
    laydate.render({
       elem: '#ll_formSearch #llrqend'
      ,theme: '#2381E9'
        ,trigger: 'click'
    });
});