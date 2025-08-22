var progress_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#progress_formSearch #progress_tb_list').bootstrapTable({
            url: $('#progress_formSearch #urlPrefix').val() + '/progress/progress/pageGetListProgress',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#progress_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
//            paginationDetailHAlign: " hidden", //不显示底部文字
            sortable: true,                     //是否启用排序
            sortName: "lrsj",				//排序字段
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
            uniqueId: "cpxqid",                     //每一行的唯一标识，一般为主键列
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
                title: '需求单号',
                titleTooltip:'需求单号',
                width: '8%',
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
                field: 'yjyt',
                title: '预计用途',
                titleTooltip:'预计用途',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'bz',
                title: '备注',
                titleTooltip:'备注',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'zt',
                title: '状态',
                width: '6%',
                align: 'left',
                formatter:ztFormat,
                visible: true
            },{
                field: 'rkzt',
                title: '入库状态',
                width: '6%',
                align: 'left',
                formatter:rkztFormat,
                visible: true
            }, {
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '6%',
				align: 'left',
				formatter:czFormat,
				visible: true
			}],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	progressDealById(row.cpxqid, 'view',$("#progress_formSearch #btn_view").attr("tourl"));
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
            sortLastName: "cpxqid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
        };

        return getProgressSearchData(map);
    };
    return oTableInit;
};

function searchProgressResult(isTurnBack){
	if(isTurnBack){
		$('#progress_formSearch #progress_tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#progress_formSearch #progress_tb_list').bootstrapTable('refresh');
	}
}


function getProgressSearchData(map){
	var cxtj = $("#progress_formSearch #cxtj").val();
	// 查询条件
	var cxnr = $.trim(jQuery('#progress_formSearch #cxnr').val());
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
	var xqrqstart = jQuery('#progress_formSearch #xqrqstart').val();
	map["xqrqstart"] = xqrqstart;
	// 需求结束日期
	var xqrqend = jQuery('#progress_formSearch #xqrqend').val();
	map["xqrqend"] = xqrqend;

	return map;
}




/**
 * 入库状态初始化
 */
function rkztFormat(value,row,index) {
    var html ="";
    if(row.rkzt=="01"){
         html="<span style='color:red;'>"+"未入库"+"</span>";
    }else if(row.rkzt=="02"){
         html="<span style='color:#f0ad4e;'>"+"部分入库"+"</span>";
    }else if (row.rkzt=="03"){
         html="<span style='color:green;'>"+"全部入库"+"</span>";
    }else {
         html="<span style='color:red;'>"+"未入库"+"</span>";
    }
    return html;
}
/**
 * 物料列表的状态格式化函数
 * @returns
 */
function ztFormat(value,row,index) {
    var param = {prefix:$('#progress_formSearch #urlPrefix').val()};
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.cpxqid + "\",event,\"AUDIT_FG_PLAN\",{prefix:\"" + $('#progress_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.cpxqid + "\",event,\"AUDIT_FG_PLAN\",{prefix:\"" + $('#progress_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else{
    	 return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.cpxqid + "\",event,\"AUDIT_FG_PLAN\",{prefix:\"" + $('#progress_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
    if (row.zt == '10' && row.scbj =='0' ) {
        return "<span class='btn btn-warning' onclick=\"recallMaterProgress('" + row.cpxqid + "',event)\" >撤回</span>";
    }else if(row.zt == '10' && row.scbj ==null ){
        return "<span class='btn btn-warning' onclick=\"recallMaterProgress('" + row.cpxqid + "',event)\" >撤回</span>";
    }else{
        return "";
    }
}

//撤回项目提交
function recallMaterProgress(cpxqid,event){
	var auditType = $("#progress_formSearch #auditType").val();
	var msg = '您确定要撤回该需求单吗？';
	var mater_params=[];
	mater_params.prefix=$('#progress_formSearch #urlPrefix').val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,cpxqid,function(){
				searchProgressResult();
			},mater_params);
		}
	});
}



var viewProgressConfig = {
	width		: "1200px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var addProgressConfig = {
    width		: "1600px",
    modalName	:"addProgressConfig",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "提  交",
            className : "btn-primary",
            callback : function() {
                if($("#progress_edit_ajaxForm #zt").val()!=null && $("#progress_edit_ajaxForm #zt").val()!=""
                && $("#progress_edit_ajaxForm #zt").val()=="80"){
                    $.alert("审核通过的单子不允许重复提交！");
                    return false;
                }
                if(!$("#progress_edit_ajaxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(var i=0;i<t_map.rows.length;i++){
                        if(t_map.rows[i].sl==null || t_map.rows[i].sl==''){
                            $.alert("数量不能为空！");
                            return false;
                        }
                        if(t_map.rows[i].sl==0){
                            $.alert("需求数量不能为0！");
                            return false;
                        }


                        var sz = {"sczlmxid":'',"wlbm":'',"wlmc":'',"sl":'',"wlid":'',"scsl":'',"yq":''};
                        sz.sczlmxid = t_map.rows[i].sczlmxid;
                        sz.wlbm = t_map.rows[i].wlbm;
                        sz.wlmc = t_map.rows[i].wlmc;
                        sz.sl = t_map.rows[i].sl;
                        sz.wlid = t_map.rows[i].wlid;
                        sz.scsl = 0;
                        if(t_map.rows[i].yq==null || t_map.rows[i].yq==''){
                            sz.yq = "/";
                        }else{
                            sz.yq = t_map.rows[i].yq;
                        }
                        json.push(sz);
                    }
                    $("#progress_edit_ajaxForm #xqjhmx_json").val(JSON.stringify(json));
                }else{
                    $.alert("需求明细信息不能为空！");
                    return false;
                }
                $("#progress_edit_ajaxForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"progress_edit_ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        var auditType = $("#progress_formSearch #auditType").val();
                        var progress_params=[];
                        progress_params.prefix=$('#progress_formSearch #urlPrefix').val();
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        //提交审核
                        showAuditFlowDialog(auditType, responseText["ywid"],function(){
                            searchProgressResult();
                        },null,progress_params);
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
                },".modal-footer > button");
                return false;
            }
        },
        successtwo : {
            label : "保 存",
            className : "btn-success",
            callback : function() {
                if(!$("#progress_edit_ajaxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(var i=0;i<t_map.rows.length;i++){
                        if(t_map.rows[i].sl==null || t_map.rows[i].sl==''){
                            $.alert("数量不能为空！");
                            return false;
                        }
                        if(t_map.rows[i].sl==0){
                            $.alert("需求数量不能为0！");
                            return false;
                        }


                        var sz = {"sczlmxid":'',"wlbm":'',"wlmc":'',"sl":'',"wlid":'',"scsl":'',"yq":''};
                        sz.sczlmxid = t_map.rows[i].sczlmxid;
                        sz.wlbm = t_map.rows[i].wlbm;
                        sz.wlmc = t_map.rows[i].wlmc;
                        sz.sl = t_map.rows[i].sl;
                        sz.wlid = t_map.rows[i].wlid;
                        sz.scsl = 0;
                        if(t_map.rows[i].yq==null || t_map.rows[i].yq==''){
                            sz.yq = "/";
                        }else{
                            sz.yq = t_map.rows[i].yq;
                        }
                        json.push(sz);
                    }
                    $("#progress_edit_ajaxForm #xqjhmx_json").val(JSON.stringify(json));
                }else{
                    $.alert("需求明细信息不能为空！");
                    return false;
                }
                $("#progress_edit_ajaxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"] || "progress_edit_ajaxForm", function (responseText, statusText) {
                    if (responseText["status"] == 'success') {
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchProgressResult();
                        });
                    } else if (responseText["status"] == "fail") {
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"], function () {
                        });
                    } else {
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"], function () {
                        });
                    }
                }, ".modal-footer > button");
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
//按钮动作函数
function progressDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $('#progress_formSearch #urlPrefix').val() + tourl; 
	if(action =='view'){
		var url= tourl + "?cpxqid=" +id;
		$.showDialog(url,'查看',viewProgressConfig);
	}else if (action =='add'){
        $.showDialog(tourl,'新增',addProgressConfig);
    } else if (action =='mod'){
        var url= tourl + "?cpxqid=" +id;
        $.showDialog(url,'修改',addProgressConfig);
    }else if (action =='rkmaintenance'){
        var url= tourl + "?ids=" +id;
        $.showDialog(url,'入库状态维护',rkmaintenanceConfig);
    }else if (action =='review'){
        var url= tourl + "?cpxqid=" +id;
        $.showDialog(url,'生产评审',reviewConfig);
    }else if (action =='setmateriel'){
        $.showDialog(tourl,'个人物料设置',setmaterielConfig);
    }
}

var setmaterielConfig = {
	width		: "1400px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var reviewConfig={
    width		: "1000px",
    modalName	: "reviewModal",
    formName	: "reviewProgressForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#reviewProgressForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#reviewProgressForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"reviewProgressForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchProgressResult();
                            }
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"],function() {
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
var rkmaintenanceConfig={
    width		: "600px",
    modalName	: "rkmaintenanceModal",
    formName	: "rkmaintenanceForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#rkmaintenanceForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#rkmaintenanceForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"rkmaintenanceForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchProgressResult();
                            }
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"],function() {
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

var progress_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_view = $("#progress_formSearch #btn_view");
    	var btn_query = $("#progress_formSearch #btn_query");
        var btn_add = $("#progress_formSearch #btn_add");
        var btn_mod=$("#progress_formSearch #btn_mod");// 修改
        var btn_del = $("#progress_formSearch #btn_del");// 删除
        var btn_discard = $("#progress_formSearch #btn_discard");//领料废弃
        var btn_rkmaintenance = $("#progress_formSearch #btn_rkmaintenance");//入库状态维护
        var btn_review = $("#progress_formSearch #btn_review");//生产评审
        var btn_setmateriel = $("#progress_formSearch #btn_setmateriel");//生产评审

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

    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchProgressResult(true);
    		});
    	}
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#progress_formSearch #progress_tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			progressDealById(sel_row[0].cpxqid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});

        btn_setmateriel.unbind("click").click(function(){
            progressDealById(null,"setmateriel",btn_setmateriel.attr("tourl"));
        });

        /*-------------------新增------------------*/
        btn_add.unbind("click").click(function(){
            progressDealById(null,"add",btn_add.attr("tourl"));
        });

        /* --------------------------- 修改 -----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#progress_formSearch #progress_tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                progressDealById(sel_row[0].cpxqid, "mod", btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* --------------------------- 生产评审 -----------------------------------*/
        btn_review.unbind("click").click(function(){
            var sel_row = $('#progress_formSearch #progress_tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                progressDealById(sel_row[0].cpxqid, "review", btn_review.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------删除领料信息-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#progress_formSearch #progress_tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].cpxqid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $("#progress_formSearch #urlPrefix").val()+btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchProgressResult();
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

        /* ------------------------------废弃领料信息-----------------------------*/
        btn_discard.unbind("click").click(function(){
            var sel_row = $('#progress_formSearch #progress_tb_list').bootstrapTable('getSelections');//获取选择行数据
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                if(sel_row[i].zt !='00' && sel_row[i].zt !='15'){
                    $.error("该记录在审核中或已审核，不允许废弃!");
                    return;
                }
            }
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else{
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].cpxqid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要废弃所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url=$('#progress_formSearch #urlPrefix').val() + btn_discard.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchProgressResult();
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
        /* ------------------------------入库状态维护-----------------------------*/
        btn_rkmaintenance.unbind("click").click(function(){
            var sel_row = $('#progress_formSearch #progress_tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else{
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].cpxqid;
                }
                ids=ids.substr(1);
                progressDealById(ids, "rkmaintenance", btn_rkmaintenance.attr("tourl"));
            }
        });
    };

    return oInit;
};


$(function(){
    //1.初始化Table
    var oTable = new progress_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new progress_ButtonInit();
    oButtonInit.Init();
	//所有下拉框添加choose样式
	jQuery('#progress_formSearch .chosen-select').chosen({width: '100%'});
	
});
