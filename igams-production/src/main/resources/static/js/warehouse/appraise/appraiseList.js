var appraise_turnOff=true;
var appraise_TableInit = function () {
	 var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#appraiseList_formSearch #appraise_list').bootstrapTable({
			url: $("#appraiseList_formSearch #urlPrefix").val()+'/appraise/appraise/pageGetListAppraise',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#appraiseList_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"gfpjb.lrsj,gfpjb.pjsj",					// 排序字段
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
			uniqueId: "gfpjid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true,
				width: '4%'
			},{
            	title: '序号',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '4%',
                align: 'center',
                visible:true
            },{
				field: 'gfmc',
				titleTooltip:'供应商名称',
				title: '供应商名称',
				width: '15%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'gfbh',
				titleTooltip:'供方编号',
				title: '供方编号',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
                field: 'gfgllbmc',
                titleTooltip:'供方管理类别名称',
                title: '供方管理类别名称',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
               field: 'sqrmc',
               titleTooltip:'申请人',
               title: '申请人',
               width: '10%',
               align: 'left',
               sortable: true,
               visible: true
            },{
              field: 'pjsj',
              titleTooltip:'评价时间',
              title: '申请时间',
              width: '10%',
              align: 'left',
              sortable: true,
              visible: true
          },{
				field: 'dz',
				titleTooltip:'地址',
				title: '地址',
				width: '15%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'lxr',
				titleTooltip:'联系人',
				title: '联系人',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: false
			}, {
				field: 'cz',
				titleTooltip:'传真',
				title: '传真',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'dh',
				titleTooltip:'电话',
				title: '电话',
				width: '8%',
				align: 'left',
				visible: false
			}, {
                field: 'zzwjqk',
                titleTooltip:'资质文件情况',
                title: '资质文件情况',
                width: '20%',
                align: 'left',
                visible: true
            }, {
				field: 'zlqk',
				titleTooltip:'质量情况',
				title: '质量情况',
				width: '20%',
				align: 'left',
				visible: true
			}, {
                field: 'jgqk',
                titleTooltip:'价格情况',
                title: '价格情况',
                width: '20%',
                align: 'left',
                visible: true
            }, {
				field: 'ghzqqk',
				titleTooltip:'价格情况',
				title: '供货周期情况',
				width: '20%',
				align: 'left',
				visible: true
			}, {
                field: 'xcshqk',
                titleTooltip:'现场审核情况',
                title: '现场审核情况',
                width: '20%',
                align: 'left',
                visible: true
            },{
                field: 'jl',
                titleTooltip:'评价结论',
                title: '评价结论',
                width: '20%',
                align: 'left',
                visible: true
          },   {
                field: 'zt',
                titleTooltip:'状态',
                title: '状态',
                width: '10%',
                align: 'left',
                visible: true,
                formatter:appraise_ztformat
           },{
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '10%',
				align: 'center',
				formatter:appraise_czFormat,
				visible: true
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				appraiseById(row.gfpjid,'view',$("#appraiseList_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#appraiseList_formSearch #appraise_list").colResizable({
			liveDrag:true,
			gripInnerHtml:"<div class='grip'></div>",
			draggingClass:"dragging",
			resizeMode:'fit',
			postbackSafe:false,
			partialRefresh:true
		})
	};
	// 得到查询的参数
	oTableInit.queryParams = function(params){
	// 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
	// 例如 toolbar 中的参数
	// 如果 queryParamsType = ‘limit’ ,返回参数必须包含
	// limit, offset, search, sort, order
	// 否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
	// 返回false将会终止请求
    var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
    	pageSize: params.limit,   // 页面大小
    	pageNumber: (params.offset / params.limit) + 1,  // 页码
        access_token:$("#ac_tk").val(),
        sortName: params.sort,      // 排序列名
        sortOrder: params.order, // 排位命令（desc，asc）
        sortLastName: "gfpjb.lrsj", // 防止同名排位用
        sortLastOrder: "desc" // 防止同名排位用
        // 搜索框使用
        // search:params.search
    };
    return appraiseSearchData(map);
	};
	return oTableInit;
};

//操作格式化
function appraise_czFormat(value,row,index){
    var auditType = "";
	if (row.zt == '10') {
	    auditType = $("#appraiseList_formSearch #auditType").val();
		return "<span class='btn btn-warning' onclick=\"recallAppraise('" + row.gfpjid +"','" + auditType+ "',event)\" >撤回</span>";
	}else{
	    return "";
	}
}

//状态格式化
function appraise_ztformat(value,row,index){
	   if (row.zt == '00') {
	        return '未提交';
	    }else if (row.zt == '80'){
			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.gfpjid + "\",event,\"AUDIT_SUPPLIER_EVALUATION\",{prefix:\"" + $('#appraiseList_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
	    }else if (row.zt == '15') {

			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.gfpjid + "\",event,\"AUDIT_SUPPLIER_EVALUATION\",{prefix:\"" + $('#appraiseList_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
	    }else{

			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.gfpjid + "\",event,\"AUDIT_SUPPLIER_EVALUATION\",{prefix:\"" + $('#appraiseList_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
	    }
}



function appraiseSearchData(map){
	var cxtj=$("#appraiseList_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#appraiseList_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["gfmc"]=cxnr;
	}else if(cxtj=="1"){
		map["lxr"]=cxnr;
	}else if(cxtj=="2"){
		map["cz"]=cxnr;
	}else if(cxtj=="3"){
		map["dh"]=cxnr;
	}else if(cxtj=="4"){
		map["gfbh"]=cxnr;
	}else if(cxtj=="5"){
        map["entire"]=cxnr;
    }else if(cxtj=="6"){
        map["sqrmc"]=cxnr;
    }else if(cxtj=="7"){
        map["wlbm"]=cxnr;
    }else if(cxtj=="8"){
        map["wlmc"]=cxnr;
    }else if(cxtj=="9"){
        map["xmh"]=cxnr;
    }

	//开始日期
	var pjsjstart = jQuery('#appraiseList_formSearch #pjsjstart').val();
	map["pjsjstart"] = pjsjstart;

	// 结束日期
	var pjsjend = jQuery('#appraiseList_formSearch #pjsjend').val();
	map["pjsjend"] = pjsjend;

	// 供方管理类别
	var gfgllbs = jQuery('#appraiseList_formSearch #gfgllb_id_tj').val();
	map["gfgllbs"] = gfgllbs;

	return map;
}


//提供给导出用的回调函数
function AppraiseSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="gfpjb.lrsj";
	map["sortLastOrder"]="desc";
	map["sortName"]="gfpjb.gysmc";
	map["sortOrder"]="desc";
	return appraiseSearchData(map);
}

function appraiseById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#appraiseList_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?gfpjid=" +id;
		$.showDialog(url,'供方评价详细信息',viewAppraiseConfig);
	}else if(action =='add'){
		$.showDialog(tourl,'新增供方评价',addAppraiseConfig);
	}else if(action =='submit'){
        var url= tourl+"?gfpjid="+id;
        $.showDialog(url,'提交供方评价信息',addAppraiseConfig);
	}else if(action=="mod"){
        var url=tourl+"?gfpjid="+id;
        $.showDialog(url,'供方验证信息修改',addAppraiseConfig);
	}
}


var addAppraiseConfig = {
		width		: "1600px",
		modalName	:"addAppraiseModel",
		formName	: "appraiseEditForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
		successTwo : {
        				label : "保存",
        				className : "btn-success",
        				callback : function() {
        					if(!$("#appraiseEditForm").valid()){
        						$.alert("请填写完整信息");
        						return false;
        					}
        					var $this = this;
        					var opts = $this["options"]||{};
                            var json = [];
                            if(t_map.rows != null && t_map.rows.length > 0){
                                for(var i=0;i<t_map.rows.length;i++){
                                    if(t_map.rows[i].wlid==null || t_map.rows[i].wlid==''){
                                        if(t_map.rows[i].fwmc==null || t_map.rows[i].fwmc==''){
                                             $.alert("服务名称不能为空！");
                                             return false;
                                        }
                                    }
                                    var sz = {"pjmxid":'',"yzmxid":'',"wlid":'',"jszb":'',"zlyq":'',"xh":'',"fwmc":'',"xmh":''};
                                    sz.pjmxid = t_map.rows[i].pjmxid;
                                    sz.yzmxid = t_map.rows[i].yzmxid;
                                    sz.wlid = t_map.rows[i].wlid;
                                    sz.jszb = t_map.rows[i].jszb;
                                    sz.zlyq = t_map.rows[i].zlyq;
                                    sz.xh = i+1+"";
                                    sz.fwmc = t_map.rows[i].fwmc;
                                    sz.xmh = t_map.rows[i].xmh;
                                    json.push(sz);
                                }
                                $("#appraiseEditForm #gfpjmxJson").val(JSON.stringify(json));
                            }else{
                                $.alert("请添加供方物料或服务！");
                                return false;
                            }
        					$("#appraiseEditForm input[name='access_token']").val($("#ac_tk").val());
        					submitForm(opts["formName"]||"appraiseEditForm",function(responseText,statusText){
        						if(responseText["status"] == 'success'){
        						    $.success(responseText["message"],function() {
                                        if(opts.offAtOnce){
                                            $.closeModal(opts.modalName);
                                            appraiseResult();
                                        }
                                    });
        						}else if(responseText["status"] == "fail"){
        							$.error(responseText["message"],function() {
        							    preventResubmitForm(".modal-footer > button", false);
        							});
        						} else{
        							$.alert(responseText["message"],function() {
                                       preventResubmitForm(".modal-footer > button", false);
        							});
        						}
        					},".modal-footer > button");
        					return false;
        				}
        			},
			success : {
				label : "提交",
				className : "btn-primary",
				callback : function() {
					if(!$("#appraiseEditForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
                    var json = [];
                    if(t_map.rows != null && t_map.rows.length > 0){
                        for(var i=0;i<t_map.rows.length;i++){
                            if(t_map.rows[i].wlid==null || t_map.rows[i].wlid==''){
                                if(t_map.rows[i].fwmc==null || t_map.rows[i].fwmc==''){
                                     $.alert("服务名称不能为空！");
                                     return false;
                                }
                            }
                            var sz = {"pjmxid":'',"yzmxid":'',"wlid":'',"jszb":'',"zlyq":'',"xh":'',"fwmc":'',"xmh":''};
                            sz.pjmxid = t_map.rows[i].pjmxid;
                            sz.yzmxid = t_map.rows[i].yzmxid;
                            sz.wlid = t_map.rows[i].wlid;
                            sz.jszb = t_map.rows[i].jszb;
                            sz.zlyq = t_map.rows[i].zlyq;
                            sz.xh = i+1+"";
                            sz.fwmc = t_map.rows[i].fwmc;
                            sz.xmh = t_map.rows[i].xmh;
                            json.push(sz);
                        }
                        $("#appraiseEditForm #gfpjmxJson").val(JSON.stringify(json));
                    }else{
                        $.alert("请添加供方物料或服务！");
                        return false;
                    }
					$("#appraiseEditForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"appraiseEditForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							var auditType = $("#appraiseEditForm #auditType").val();
							var evaluation_params=[];
							evaluation_params.prefix=$('#appraiseEditForm #urlPrefix').val();
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							//提交审核
							showAuditFlowDialog(auditType, responseText["ywid"],function(){
								appraiseResult();
							},null,evaluation_params);
						}else if(responseText["status"] == "fail"){
							$.error(responseText["message"],function() {
							    preventResubmitForm(".modal-footer > button", false);
							});
						} else{
							$.alert(responseText["message"],function() {
							    preventResubmitForm(".modal-footer > button", false);
							});
						}
					},".modal-footer > button");
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};


var viewAppraiseConfig = {
	width		: "1600px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

//撤回项目提交
function recallAppraise(gfpjid,auditType,event){
	var msg = '您确定要撤回该条到货信息吗？';
	var appraise_params = [];
	appraise_params.prefix = $("#appraiseList_formSearch #urlPrefix").val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,gfpjid,function(){
				appraiseResult(true);
			},appraise_params);
		}
	});
}

var appraise_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_query = $("#appraiseList_formSearch #btn_query");
		var btn_view = $("#appraiseList_formSearch #btn_view");
		var btn_del = $("#appraiseList_formSearch #btn_del");
		var btn_add = $("#appraiseList_formSearch #btn_add");
		var btn_mod = $("#appraiseList_formSearch #btn_mod");
		var btn_submit = $("#appraiseList_formSearch #btn_submit");
    	var btn_print = $("#appraiseList_formSearch #btn_print");
		//添加日期控件
    	laydate.render({
    	   elem: '#pjsjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#pjsjend'
    	  ,theme: '#2381E9'
    	});

		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			appraiseResult(true);
    		});
    	}


		/* --------------------------- 新增供方信息 -----------------------------------*/
    	btn_add.unbind("click").click(function(){
    		appraiseById(null, "add", btn_add.attr("tourl"));
    	});


    	/* --------------------------- 修改供方信息 -----------------------------------*/
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#appraiseList_formSearch #appraise_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length == 1){
    		    if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
    		        appraiseById(sel_row[0].gfpjid, "mod", btn_mod.attr("tourl"));
    		    }else{
    		        $.error("该状态不允许修改！");
    		    }
    		}else{
    			$.error("请选中一行");
    		}
    	});

    	/* ---------------------------提交-----------------------------------*/
    	btn_submit.unbind("click").click(function(){
    		var sel_row = $('#appraiseList_formSearch #appraise_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			if(sel_row[0].zt !='00' && sel_row[0].zt !='15' && sel_row[0].zt !=null){
    				$.error("您好！该条记录已提交或者已通过，不允许重复提交！");
    				return false;
    			}
    			appraiseById(sel_row[0].gfpjid,"submit",btn_submit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});

       /* ---------------------------查看列表-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#appraiseList_formSearch #appraise_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			appraiseById(sel_row[0].gfpjid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
/* ------------------------------删除-----------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#appraiseList_formSearch #appraise_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else {
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].gfpjid;
        		}
    			ids=ids.substr(1);
    			var gfyzids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    gfyzids= gfyzids + ","+ sel_row[i].gfyzid;
                }
                gfyzids=gfyzids.substr(1);
    			$.confirm('您确定要删除所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= $("#appraiseList_formSearch #urlPrefix").val()+btn_del.attr("tourl");
        				jQuery.post(url,{ids:ids,gfyzids:gfyzids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								appraiseResult();
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

    	/* --------------------------- 打印请检单 -----------------------------------*/
    	btn_print.unbind("click").click(function(){
    		var sel_row = $('#appraiseList_formSearch #appraise_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				var url=$('#appraiseList_formSearch #urlPrefix').val()+btn_print.attr("tourl")+"?gfpjid="+sel_row[0].gfpjid+"&access_token="+$("#ac_tk").val();
				window.open(url);
			}else{
				$.error("请选中一行");
			}
    	});


    	/* --------------------------- 结束 -----------------------------------*/
       	/**显示隐藏**/
    	$("#appraiseList_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event;
    		if(appraise_turnOff){
    			$("#appraiseList_formSearch #searchMore").slideDown("low");
    			appraise_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#appraiseList_formSearch #searchMore").slideUp("low");
    			appraise_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});

    };
    	return oInit;
};


function appraiseResult(isTurnBack){
	//关闭高级搜索条件
	$("#appraiseList_formSearch #searchMore").slideUp("low");
	appraise_turnOff=true;
	$("#appraiseList_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#appraiseList_formSearch #appraise_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#appraiseList_formSearch #appraise_list').bootstrapTable('refresh');
	}
}


$(function(){


	// 1.初始化Table
	var oTable = new appraise_TableInit();
	oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new appraise_ButtonInit();
    oButtonInit.Init();

	// 所有下拉框添加choose样式
	jQuery('#appraiseList_formSearch .chosen-select').chosen({width: '100%'});

	$("#appraiseList_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});

});