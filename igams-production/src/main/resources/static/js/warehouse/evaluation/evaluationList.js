var evaluation_turnOff=true;
var evaluation_TableInit = function () {
	 var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#evaluationList_formSearch #evaluation_list').bootstrapTable({
			url: $("#evaluationList_formSearch #urlPrefix").val()+'/evaluation/evaluation/pageGetListEvaluation',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#evaluationList_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"gfyz.lrsj,gfyz.yzshsj",					// 排序字段
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
			uniqueId: "gfyzid",                     // 每一行的唯一标识，一般为主键列
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
              field: 'yzsqsj',
              titleTooltip:'申请时间',
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
				field: 'fzrq',
				titleTooltip:'发展日期',
				title: '发展日期',
				width: '8%',
				align: 'left',
				visible: false
			},{
				field: 'yzshsj',
				titleTooltip:'验证审核时间',
				title: '验证审核时间',
				width: '8%',
				align: 'center',
				sortable: true,
				visible: true
			}, {
                field: 'cbpj',
                titleTooltip:'初步评价',
                title: '初步评价',
                width: '20%',
                align: 'left',
                visible: true
            }, {
				field: 'yzshpj',
				titleTooltip:'验证审核评价',
				title: '验证审核评价',
				width: '20%',
				align: 'left',
				visible: true
			}, {
                field: 'jl',
                titleTooltip:'结论',
                title: '结论',
                width: '20%',
                align: 'left',
                visible: true
            }, {
				field: 'sqzt',
				title: '申请状态',
				width: '10%',
				align: 'left',
				formatter:evaluation_sqztformat,
				visible: true
			}, {
                field: 'yzzt',
                title: '验证状态',
                width: '10%',
                align: 'left',
                formatter:evaluation_yzztformat,
                visible: true
            },{
                field: 'sfwc',
                title: '是否完成',
                width: '5%',
                align: 'left',
                formatter:evaluation_sfwcformat,
                visible: true
          }, {
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '10%',
				align: 'center',
				formatter:evaluation_czFormat,
				visible: true
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				evaluationById(row.gfyzid,'view',$("#evaluationList_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#evaluationList_formSearch #evaluation_list").colResizable({
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
        sortLastName: "gfyz.lrsj", // 防止同名排位用
        sortLastOrder: "desc" // 防止同名排位用
        // 搜索框使用
        // search:params.search
    };
    return evaluationSearchData(map);
	};
	return oTableInit;
};

//操作格式化
function evaluation_czFormat(value,row,index){
    var auditType = "";
	if (row.sqzt == '10') {
	    auditType = $("#evaluationList_formSearch #applicationAudit").val();
		return "<span class='btn btn-warning' onclick=\"recallEvaluation('" + row.gfyzid +"','" + auditType+ "',event)\" >撤回</span>";
	}else if(row.yzzt == '10'){
		auditType = $("#evaluationList_formSearch #auditType").val();
        return "<span class='btn btn-warning' onclick=\"recallEvaluation('" + row.gfyzid +"','" + auditType+ "',event)\" >撤回</span>";
	}else{
	    return "";
	}
}

//状态格式化
function evaluation_sqztformat(value,row,index){
	   if (row.sqzt == '00') {
	        return '未提交';
	    }else if (row.sqzt == '80'){
			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.gfyzid + "\",event,\"AUDIT_VERIFICATION_APPLICATION\",{prefix:\"" + $('#evaluationList_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
	    }else if (row.sqzt == '15') {

			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.gfyzid + "\",event,\"AUDIT_VERIFICATION_APPLICATION\",{prefix:\"" + $('#evaluationList_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
	    }else{

			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.gfyzid + "\",event,\"AUDIT_VERIFICATION_APPLICATION\",{prefix:\"" + $('#evaluationList_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
	    }
}
function evaluation_sfwcformat(value,row,index){
    if(row.sfwc==1){
    		var html="<span style='color:green;'>"+"是"+"</span>";
    	}else{
    		var html="<span style='color:red;'>"+"否"+"</span>";
    	}
    	return html;
}

function evaluation_yzztformat(value,row,index){
	   if (row.yzzt == '00') {
	        return '未提交';
	    }else if (row.yzzt == '80'){
			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.gfyzid + "\",event,\"AUDIT_VERIFICATION_AUDIT\",{prefix:\"" + $('#evaluationList_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
	    }else if (row.yzzt == '15') {

			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.gfyzid + "\",event,\"AUDIT_VERIFICATION_AUDIT\",{prefix:\"" + $('#evaluationList_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
	    }else{

			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.gfyzid + "\",event,\"AUDIT_VERIFICATION_AUDIT\",{prefix:\"" + $('#evaluationList_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
	    }
}


function evaluationSearchData(map){
	var cxtj=$("#evaluationList_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#evaluationList_formSearch #cxnr').val());
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

	// 验证申请开始日期
	var yzsqsjstart = jQuery('#evaluationList_formSearch #yzsqsjstart').val();
	map["yzsqsjstart"] = yzsqsjstart;

	// 验证申请结束日期
	var yzsqsjend = jQuery('#evaluationList_formSearch #yzsqsjend').val();
	map["yzsqsjend"] = yzsqsjend;

	// 验证审核开始日期
	var yzshsjstart = jQuery('#evaluationList_formSearch #yzshsjstart').val();
	map["yzshsjstart"] = yzshsjstart;

	// 验证审核结束日期
	var yzshsjend = jQuery('#evaluationList_formSearch #yzshsjend').val();
	map["yzshsjend"] = yzshsjend;

	// 供方管理类别
	var gfgllbs = jQuery('#evaluationList_formSearch #gfgllb_id_tj').val();
	map["gfgllbs"] = gfgllbs;

	return map;
}


//提供给导出用的回调函数
function EvaluationSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="gfyz.lrsj";
	map["sortLastOrder"]="desc";
	map["sortName"]="gfyz.gysmc";
	map["sortOrder"]="desc";
	return evaluationSearchData(map);
}

function evaluationById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#evaluationList_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?gfyzid=" +id;
		$.showDialog(url,'供方验证详细信息',viewEvaluationConfig);
	}else if(action =='add'){
		$.showDialog(tourl,'新增供方验证',addEvaluationConfig);
	}else if(action =='submit'){
        var url= tourl+"?gfyzid="+id;
        $.showDialog(url,'提交供方验证信息',addEvaluationConfig);
	}else if(action=="mod"){
        var url=tourl+"?gfyzid="+id;
        $.showDialog(url,'供方验证信息修改',addEvaluationConfig);
	}else if(action=="verification"){
         var url=tourl+"?gfyzid="+id;
         $.showDialog(url,'供方验证审核结果提交',addEvaluationConfig);
    }
}


var addEvaluationConfig = {
		width		: "1600px",
		modalName	:"addEvaluationModel",
		formName	: "evaluationEditForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
		successTwo : {
        				label : "保存",
        				className : "btn-success",
        				callback : function() {
        					if(!$("#evaluationEditForm").valid()){
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
                                    var sz = {"yzmxid":'',"wlid":'',"jszb":'',"zlyq":'',"xh":'',"fwmc":'',"xmh":''};
                                    sz.yzmxid = t_map.rows[i].yzmxid;
                                    sz.wlid = t_map.rows[i].wlid;
                                    sz.jszb = t_map.rows[i].jszb;
                                    sz.zlyq = t_map.rows[i].zlyq;
                                    sz.xh = i+1+"";
                                    sz.fwmc = t_map.rows[i].fwmc;
                                    sz.xmh = t_map.rows[i].xmh;
                                    json.push(sz);
                                }
                                $("#evaluationEditForm #gfyzmxJson").val(JSON.stringify(json));
                            }else{
                                $.alert("请添加供方物料或服务！");
                                return false;
                            }
        					$("#evaluationEditForm input[name='access_token']").val($("#ac_tk").val());
        					submitForm(opts["formName"]||"evaluationEditForm",function(responseText,statusText){
        						if(responseText["status"] == 'success'){
        						    $.success(responseText["message"],function() {
                                        if(opts.offAtOnce){
                                            $.closeModal(opts.modalName);
                                            evaluationResult();
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
					if(!$("#evaluationEditForm").valid()){
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
                            var sz = {"yzmxid":'',"wlid":'',"jszb":'',"zlyq":'',"xh":'',"fwmc":'',"xmh":''};
                            sz.yzmxid = t_map.rows[i].yzmxid;
                            sz.wlid = t_map.rows[i].wlid;
                            sz.jszb = t_map.rows[i].jszb;
                            sz.zlyq = t_map.rows[i].zlyq;
                            sz.xh = i+1+"";
                            sz.fwmc = t_map.rows[i].fwmc;
                            sz.xmh = t_map.rows[i].xmh;
                            json.push(sz);
                        }
                        $("#evaluationEditForm #gfyzmxJson").val(JSON.stringify(json));
                    }else{
                        $.alert("请添加供方物料或服务！");
                        return false;
                    }
					$("#evaluationEditForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"evaluationEditForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){

							var auditType = $("#evaluationEditForm #auditType").val();
							var evaluation_params=[];
							evaluation_params.prefix=$('#evaluationEditForm #urlPrefix').val();
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							//提交审核
							showAuditFlowDialog(auditType, responseText["ywid"],function(){
								evaluationResult();
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


var viewEvaluationConfig = {
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
function recallEvaluation(gfyzid,auditType,event){
	var msg = '您确定要撤回该条到货信息吗？';
	var evaluation_params = [];
	evaluation_params.prefix = $("#evaluationList_formSearch #urlPrefix").val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,gfyzid,function(){
				evaluationResult(true);
			},evaluation_params);
		}
	});
}

var evaluation_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_query = $("#evaluationList_formSearch #btn_query");
		var btn_view = $("#evaluationList_formSearch #btn_view");
		var btn_del = $("#evaluationList_formSearch #btn_del");
		var btn_add = $("#evaluationList_formSearch #btn_add");
		var btn_mod = $("#evaluationList_formSearch #btn_mod");
		var btn_submit = $("#evaluationList_formSearch #btn_submit");
    	var btn_print = $("#evaluationList_formSearch #btn_print");
    	var btn_verification = $("#evaluationList_formSearch #btn_verification");
		//添加日期控件
    	laydate.render({
    	   elem: '#yzshsjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#yzshsjend'
    	  ,theme: '#2381E9'
    	});

    	laydate.render({
    	   elem: '#yzsqsjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#yzsqsjend'
    	  ,theme: '#2381E9'
    	});
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			evaluationResult(true);
    		});
    	}


		/* --------------------------- 新增供方信息 -----------------------------------*/
    	btn_add.unbind("click").click(function(){
    		evaluationById(null, "add", btn_add.attr("tourl"));
    	});


    	/* --------------------------- 修改供方信息 -----------------------------------*/
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#evaluationList_formSearch #evaluation_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length == 1){
    		    if(sel_row[0].sqzt=="00" || sel_row[0].sqzt=="15"){
    		        evaluationById(sel_row[0].gfyzid, "mod", btn_mod.attr("tourl"));
    		    }else{
    		        $.error("该状态不允许修改！");
    		    }
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/* --------------------------- 供方验证审核结果 -----------------------------------*/
        btn_verification.unbind("click").click(function(){
            var sel_row = $('#evaluationList_formSearch #evaluation_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                if(sel_row[0].sqzt == "80"){
                    evaluationById(sel_row[0].gfyzid, "verification", btn_verification.attr("tourl"));
                }else{
                    $.error("验证审核申请通过才允许发起供方验证结果审核!");
                }
            }else{
                $.error("请选中一行");
            }
        });
    	/* ---------------------------提交-----------------------------------*/
    	btn_submit.unbind("click").click(function(){
    		var sel_row = $('#evaluationList_formSearch #evaluation_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			if(sel_row[0].sqzt !='00' && sel_row[0].sqzt !='15' && sel_row[0].sqzt !=null){
    				$.error("您好！该条记录已提交或者已通过，不允许重复提交！");
    				return false;
    			}
    			evaluationById(sel_row[0].gfyzid,"submit",btn_submit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});

       /* ---------------------------查看列表-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#evaluationList_formSearch #evaluation_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			evaluationById(sel_row[0].gfyzid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
/* ------------------------------删除-----------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#evaluationList_formSearch #evaluation_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else {
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].gfyzid;
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定要删除所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= $("#evaluationList_formSearch #urlPrefix").val()+btn_del.attr("tourl");
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								evaluationResult();
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
    		var sel_row = $('#evaluationList_formSearch #evaluation_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				var url=$('#evaluationList_formSearch #urlPrefix').val()+btn_print.attr("tourl")+"?gfyzid="+sel_row[0].gfyzid+"&access_token="+$("#ac_tk").val();
				window.open(url);
			}else{
				$.error("请选中一行");
			}
    	});


    	/* --------------------------- 结束 -----------------------------------*/
       	/**显示隐藏**/
    	$("#evaluationList_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event;
    		if(evaluation_turnOff){
    			$("#evaluationList_formSearch #searchMore").slideDown("low");
    			evaluation_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#evaluationList_formSearch #searchMore").slideUp("low");
    			evaluation_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});

    };
    	return oInit;
};


function evaluationResult(isTurnBack){
	//关闭高级搜索条件
	$("#evaluationList_formSearch #searchMore").slideUp("low");
	evaluation_turnOff=true;
	$("#evaluationList_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#evaluationList_formSearch #evaluation_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#evaluationList_formSearch #evaluation_list').bootstrapTable('refresh');
	}
}


$(function(){


	// 1.初始化Table
	var oTable = new evaluation_TableInit();
	oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new evaluation_ButtonInit();
    oButtonInit.Init();

	// 所有下拉框添加choose样式
	jQuery('#evaluationList_formSearch .chosen-select').chosen({width: '100%'});

	$("#evaluationList_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});

});