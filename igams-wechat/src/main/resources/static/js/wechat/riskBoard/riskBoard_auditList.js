var Recheck_turnOff=true;
var Recheck_audit_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#riskBoard_audit_formSearch  #riskBoard_auditList").bootstrapTable({
            url: '/risk/board/pageGetListBoardAudit',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#riskBoard_audit_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一s下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"fxsjgl.lrsj",					// 排序字段
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
            uniqueId: "code",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            }, {
                field: 'fxsjid',
                title: 'ID',
                width: '10%',
                align: 'left',
                visible: false,
                sortable: true
            }, {
                field: 'ybbh',
                title: '标本编号',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'nbbm',
                title: '内部编码',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'yblxmc',
                title: '样本类型',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'hzxm',
                title: '患者姓名',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'fxlbmc',
                title: '风险类别',
                width: '12%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'bbclmc',
                title: '标本处理',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'tzrq',
                title: '通知日期',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'qrsj',
                title: '确认时间',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'bz',
                title: '备注',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'zt',
                title: '状态',
                width: '10%',
                align: 'left',
                formatter:ztFormat,
                visible: true,
                sortable: true
          
            },
            ],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	riskBoard_audit_DealById(row.fxsjid,"view",$("#riskBoard_audit_formSearch #btn_view").attr("tourl"));
             },
	});
		$("#riskBoard_audit_formSearch #riskBoard_auditList").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true        
	  });
	}
		oTableInit.queryParams=function(params){
			var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
                pageSize: params.limit,   // 页面大小
                pageNumber: (params.offset / params.limit) + 1,  // 页码
                access_token:$("#ac_tk").val(),
                sortName: params.sort,      // 排序列名
                sortOrder: params.order, // 排位命令（desc，asc）
                sortLastName: "fxsjid", // 防止同名排位用
                sortLastOrder: "desc", // 防止同名排位用
	            zt: "10"
	            // 搜索框使用
	            // search:params.search
	        };
			return getRiskBoardAuditSearchData(map);
		};
		return oTableInit;
}

/**
 * 状态格式化
 * @returns
 */
function ztFormat(value,row,index) {
    var shlxdm = $("#risk_board_formSearch #auditType").val();
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fxsjid + "\",event,\""+shlxdm+"\")' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fxsjid + "\",event,\""+shlxdm+"\")' >审核未通过</a>";
    }else{
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fxsjid + "\",event,\""+shlxdm+"\")' >" + row.shxx_gwmc + "审核中</a>";
    }
}

/**
 * 审核记录列表
 */
var recheck_Audited_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#riskBoard_audited_formSearch #riskBoard_auditedList").bootstrapTable({
			url: '/risk/board/pageGetListBoardAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#riskBoard_audited_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "shxx_shsj",				//排序字段
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
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "fxsjid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            isForceTable:true,
            columns: [{
                checkbox: true
            },{
                field: 'fxsjid',
                title: 'ID',
                width: '10%',
                align: 'left',
                visible: false,
                sortable: true
            }, {
                field: 'ybbh',
                title: '标本编号',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'nbbm',
                title: '内部编码',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'yblxmc',
                title: '样本类型',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'hzxm',
                title: '患者姓名',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'fxlbmc',
                title: '风险类别',
                width: '12%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'bbclmc',
                title: '标本处理',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'tzrq',
                title: '通知日期',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'qrsj',
                title: '确认时间',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'bz',
                title: '备注',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'sqrxm',
                title: '申请人员',
                titleTooltip:'申请人员',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                titleTooltip:'申请时间',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_lrryxm',
                title: '审核人',
                titleTooltip:'审核人',
                align: 'left',
                width: '6%',
                visible: true
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                titleTooltip:'审核时间',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                titleTooltip:'审核意见',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_sftgmc',
                title: '是否通过',
                titleTooltip:'是否通过',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_shxxid',
                align: 'center',
                title: '审核信息ID',
                visible: false
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	riskBoard_audit_DealById(row.fxsjid,"view",$("#riskBoard_audit_formSearch #btn_view").attr("tourl"));
             },
		});
		$("#riskBoard_audited_formSearch #riskBoard_auditedList").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true        
		});
	}
		oTableInit.queryParams=function(params){
			var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
                pageSize: params.limit,   // 页面大小
                pageNumber: (params.offset / params.limit) + 1,  // 页码
                access_token:$("#ac_tk").val(),
                sortName: params.sort,      // 排序列名
                sortOrder: params.order, // 排位命令（desc，asc）
                sortLastName: "fxsjid", // 防止同名排位用
                sortLastOrder: "desc" // 防止同名排位用
	            // 搜索框使用
	            // search:params.search
	        };
            var cxtj_audited=$("#riskBoard_audited_formSearch #cxtj_audited").val();
            var cxnr_audited=$.trim(jQuery('#riskBoard_audited_formSearch #cxnr_audited').val());
            if (cxtj_audited == "0") {
                map["entire"] = cxnr_audited
            } else if (cxtj_audited == '1'){
                map["fxlbmc"] = cxnr_audited
            }else if (cxtj_audited =='2'){
                map["ybbh"]=cxnr_audited
            }else if (cxtj_audited =='3'){
                map["nbbm"]=cxnr_audited
            }else if (cxtj_audited =='3'){
                map["hzxm"]=cxnr_audited
            }
            var fxlb=jQuery('#riskBoard_audited_formSearch #fxlb_id_tj').val()
            map["fxlbids"]=fxlb;
            var bbcl=jQuery('#riskBoard_audited_formSearch #bbcl_id_tj').val()
            map["bbclids"]=bbcl;
            var tzrqstart = jQuery('#riskBoard_audited_formSearch #tzrqstart').val();
            map["tzrqstart"] = tzrqstart;
            var tzrqend = jQuery('#riskBoard_audited_formSearch #tzrqend').val();
            map["tzrqend"] = tzrqend;
            var qrsjstart = jQuery('#riskBoard_audited_formSearch #qrsjstart').val();
            map["qrsjstart"] = qrsjstart;
            var qrsjend = jQuery('#riskBoard_audited_formSearch #qrsjend').val();
            map["qrsjend"] = qrsjend;
            map["dqshzt"] = 'ysh';
            return map;
		}
		return oTableInit;
}
function getRiskBoardAuditSearchData(map){
    var cxtj=$("#riskBoard_audit_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#riskBoard_audit_formSearch #cxnr').val());

    if (cxtj == "0") {
        map["entire"] = cxnr
    } else if (cxtj == '1'){
        map["fxlbmc"] = cxnr
    }else if (cxtj =='2'){
        map["ybbh"]=cxnr
    }else if (cxtj =='3'){
        map["nbbm"]=cxnr
    }else if (cxtj =='3'){
        map["hzxm"]=cxnr
    }
    var fxlb=jQuery('#riskBoard_audit_formSearch #fxlb_id_tj').val()
    map["fxlbids"]=fxlb;
    var bbcl=jQuery('#riskBoard_audit_formSearch #bbcl_id_tj').val()
    map["bbclids"]=bbcl;
    var tzrqstart = jQuery('#riskBoard_audit_formSearch #tzrqstart').val();
    map["tzrqstart"] = tzrqstart;
    var tzrqend = jQuery('#riskBoard_audit_formSearch #tzrqend').val();
    map["tzrqend"] = tzrqend;
    var qrsjstart = jQuery('#riskBoard_audit_formSearch #qrsjstart').val();
    map["qrsjstart"] = qrsjstart;
    var qrsjend = jQuery('#riskBoard_audit_formSearch #qrsjend').val();
    map["qrsjend"] = qrsjend;
    return map;
}


/**
 * 按钮点击函数
 * @param id
 * @param action
 * @param tourl
 * @returns
 */
function riskBoard_audit_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action=="view"){
		var url=tourl+"?fxsjid="+id;
		$.showDialog(url,'查看信息',viewRiskBoardConfig);
	}else if(action=="audit"){
		var url= tourl;
		showAuditDialogWithLoading({
			id: id,
			type: 'AUDIT_RISK_BOARD',
			url:$("#riskBoard_audit_formSearch #btn_audit").attr("tourl"),
			data:{'ywzd':'fxsjid'},
			title:"风险上机审核",
			preSubmitCheck:'submitRecheck',
			callback:function(){
				search_riskBoard_Audit();//回调
			},
			dialogParam:{width:1000}
		});
	}
}


function submitRecheck(){
    $("#riskBoardAjaxForm #ywid").remove();
	return true;
}



var viewRiskBoardConfig = {
    width		: "900px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * 初始化按钮
 */
var Recheck_audit_oButtton=function(){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#riskBoard_audit_formSearch #btn_query");//模糊查询
		var btn_view = $("#riskBoard_audit_formSearch #btn_view");//查看页面
		var btn_audit = $("#riskBoard_audit_formSearch #btn_audit");//审核
		var btn_queryAudited = $("#riskBoard_audited_formSearch #btn_queryAudited");//审核记录列表模糊查询
    	var btn_cancelAudit = $("#riskBoard_audited_formSearch #btn_cancelAudit");//取消审核

        laydate.render({
            elem: '#riskBoard_audit_formSearch #tzrqstart'
            ,type: 'date'
        });

        laydate.render({
            elem: '#riskBoard_audit_formSearch #tzrqend'
            ,type: 'date'
        });

        laydate.render({
            elem: '#riskBoard_audit_formSearch #qrsjstart'
            ,type: 'date'
        });

        laydate.render({
            elem: '#riskBoard_audit_formSearch #qrsjend'
            ,type: 'date'
        });

        //记录
        laydate.render({
            elem: '#riskBoard_audited_formSearch #tzrqstart'
            ,type: 'date'
        });

        laydate.render({
            elem: '#riskBoard_audited_formSearch #tzrqend'
            ,type: 'date'
        });

        laydate.render({
            elem: '#riskBoard_audited_formSearch #qrsjstart'
            ,type: 'date'
        });

        laydate.render({
            elem: '#riskBoard_audited_formSearch #qrsjend'
            ,type: 'date'
        });


        /*-----------------------模糊查询(审核列表)------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				search_riskBoard_Audit();
    		});
		}
		//-----------------------模糊查询(审核记录)------------------------------------
		if(btn_queryAudited!=null){
			btn_queryAudited.unbind("click").click(function(){
				search_riskBoard_Audited();
    		});
		}

		//---------------------------审核--------------------------------
		btn_audit.unbind("click").click(function(){
    		var sel_row = $('#riskBoard_audit_formSearch #riskBoard_auditList').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			riskBoard_audit_DealById(sel_row[0].fxsjid,"audit",btn_audit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		//---------------------------查看页面--------------------------------
		btn_view.unbind("click").click(function(){
			var sel_row=$('#riskBoard_audit_formSearch #riskBoard_auditList').bootstrapTable('getSelections');
			if(sel_row.length==1){
				riskBoard_audit_DealById(sel_row[0].fxsjid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
        /*--------------------------------------显示隐藏-----------------------------------------*/
        $("#riskBoard_audit_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(Recheck_turnOff){
                $("#riskBoard_audit_formSearch #searchMore").slideDown("low");
                Recheck_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#riskBoard_audit_formSearch #searchMore").slideUp("low");
                Recheck_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
        /*--------------------------------------显示隐藏-----------------------------------------*/
        $("#riskBoard_audited_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(Recheck_turnOff){
                $("#riskBoard_audited_formSearch #searchMore").slideDown("low");
                Recheck_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#riskBoard_audited_formSearch #searchMore").slideUp("low");
                Recheck_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
        /*---------------------------查看审核记录--------------------------------*/
        // 选项卡切换事件回调
        $('#riskBoard_formAudit #recheck_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#",'');
            if(!e.target.isLoaded){// 只调用一次
                if(_hash=='recheck_auditing'){
                    var oTable= new Recheck_audit_TableInit();
                    oTable.Init();

                }else{
                    var oTable= new recheck_Audited_TableInit();
                    oTable.Init();
                }
                e.target.isLoaded = true;
            }else{// 重新加载
                // $(_gridId + ' #riskBoard_auditedList').bootstrapTable('refresh');
            }
        }).first().trigger('shown.bs.tab');// 触发第一个选中事件
    	/*-------------------------------------取消审核-----------------------------------*/
    	if(btn_cancelAudit!=null){
    		btn_cancelAudit.unbind("click");
    		btn_cancelAudit.click(function(){
    			cancelAudit($('#riskBoard_audited_formSearch  #riskBoard_auditedList').bootstrapTable('getSelections'),function(){
    				search_riskBoard_Audited();
    			});
    		})
    	}
	}
	return oInit;
}

/**
 * 刷新列表
 * @returns
 */
function search_riskBoard_Audit(){
    $("#riskBoard_audit_formSearch #searchMore").slideUp("low");
    Recheck_turnOff=true;
    $("#riskBoard_audit_formSearch #sl_searchMore").html("高级筛选");
	$('#riskBoard_audit_formSearch #riskBoard_auditList').bootstrapTable('refresh');
}
/**
 * 刷新列表
 * @returns
 */
function search_riskBoard_Audited(){
    $("#riskBoard_audited_formSearch #searchMore").slideUp("low");
    Recheck_turnOff=true;
    $("#riskBoard_audited_formSearch #sl_searchMore").html("高级筛选");
    $('#riskBoard_audited_formSearch #riskBoard_auditedList').bootstrapTable('refresh');
}
$(function(){
	var oTable= new Recheck_audit_TableInit();
		oTable.Init();
	var oButtonInit = new Recheck_audit_oButtton();
		oButtonInit.Init();
	jQuery('#recheck .chosen-select').chosen({width: '100%'});
})

//复检预览
var viewPreViewConfig={
    width		: "800px",
    offAtOnce	: true,
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}