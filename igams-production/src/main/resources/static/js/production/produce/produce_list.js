var plsumitshlx = "";
var produce_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#produce_formSearch #produce_tb_list').bootstrapTable({
            url: $('#produce_formSearch #urlPrefix').val() + '/progress/produce/pageGetListProduce',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#produce_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
//            paginationDetailHAlign: " hidden", //不显示底部文字
            sortable: true,                     //是否启用排序
            sortName: "sczlmx.lrsj",				//排序字段
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
            uniqueId: "sczlmx.sczlmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
				width: '2%',
            }, {
                field: 'sczlmxid',
                title: '生产指令明细id',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'zldh',
                title: '指令单号',
                titleTooltip:'指令单号',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'jlbh',
                title: '记录编号',
                titleTooltip:'记录编号',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: false
            },
            {
                field: 'xqdh',
                title: '需求单号',
                titleTooltip:'需求单号',
                formatter:xqdhFormat,
                width: '8%',
                align: 'left',
				sortable: true,
                visible: true

            },
            {
                field: 'zlrq',
                title: '指令日期',
                sortable: true,
                titleTooltip:'指令日期',
                width: '6%',
                align: 'left',
                visible: true,
            }, {
                field: 'wlbm',
                title: '物料编码',
                titleTooltip:'物料编码',
                width: '6%',
                align: 'left',
				sortable: true,
                visible: true
            }, {
                field: 'wlmc',
                title: '物料名称',
                titleTooltip:'物料名称',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'xlh',
                title: '序列号',
                titleTooltip:'序列号',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'gg',
                title: '规格',
                titleTooltip:'规格',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'cpbh',
                title: '产品编号',
                titleTooltip:'产品编号',
                sortable: true,
                width: '6%',
                align: 'left',
                visible: true

            }, {
				field: 'jhcl',
				title: '计划产量',
				titleTooltip:'计划产量',
				width: '6%',
				align: 'left',
				visible: false
            }, {
                field: 'yjwcsj',
                title: '预计完成时间',
                titleTooltip:'预计完成时间',
                width: '7%',
                align: 'left',
                visible: true
            }, {
                field: 'sjwcsj',
                title: '实际完成时间',
                titleTooltip:'实际完成时间',
                width: '8%',
                align: 'left',
                visible: true
                }, {
                field: 'scsl',
                title: '生产数量',
                titleTooltip:'生产数量',
                width: '5%',
                align: 'left',
                visible: true
            }, {
                field: 'cplxmc',
                title: '产品类型',
                titleTooltip:'产品类型',
                width: '5%',
                align: 'left',
                visible: true
            }, {
               field: 'dhsl',
               title: '入库量',
               titleTooltip:'入库量',
               width: '4%',
               align: 'left',
               visible: true
                }, {
               field: 'kcl',
               title: '库存量',
               titleTooltip:'库存量',
               width: '4%',
               align: 'left',
               visible: true
                }, {
                field: 'zjgs',
                title: '质检工时',
                titleTooltip:'质检工时',
                width: '5%',
                align: 'left',
                visible: true
               }, {
                field: 'scgs',
                title: '生产工时',
                titleTooltip:'生产工时',
                width: '5%',
                align: 'left',
                visible: true
                }, {
                field: 'sfcs',
                title: '是否超时',
                titleTooltip:'是否超时',
                width: '5%',
                align: 'left',
                visible: true
                }, {
                field: 'ztmc',
                title: '状态',
                titleTooltip:'状态',
                width: '5%',
                align: 'left',
                visible: true
                },{
                field: 'zt',
                title: '生产进度',
                width: '6%',
                align: 'left',
                formatter:ztFormat,
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
            	produceDealById(row.sczlmxid, 'view',$("#produce_formSearch #btn_view").attr("tourl"));
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
            sortLastName: "sczlmx.sczlmxid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
        };

        return getProduceSearchData(map);
    };
    return oTableInit;
};

function searchProduceResult(isTurnBack){
    //关闭高级搜索条件
    $("#produce_formSearch #searchMore").slideUp("low");
    produce_turnOff=true;
    $("#produce_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#produce_formSearch #produce_tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#produce_formSearch #produce_tb_list').bootstrapTable('refresh');
	}
}


function getProduceSearchData(map){
	var cxtj = $("#produce_formSearch #cxtj").val();
	// 查询条件
	var cxnr = $.trim(jQuery('#produce_formSearch #cxnr').val());
	// '0':'物料编号','1':'物料名称','2':'生产商','3':'货号'
	if (cxtj == "0") {
		map["entire"] = cxnr;
	}else if (cxtj == "1") {
		map["zldh"] = cxnr;
	}else if (cxtj == "2") {
		map["cpbh"] = cxnr;
	}else if (cxtj == "3") {
		map["xlh"] = cxnr;
	}else if (cxtj == "4") {
		map["wlbm"] = cxnr;
    }else if (cxtj == "5") {
    	map["wlmc"] = cxnr;
	}else if (cxtj == "6") {
    	map["jlbh"] = cxnr;
	}
	// 需求开始日期
	var zlrqstart = jQuery('#produce_formSearch #zlrqstart').val();
	map["zlrqstart"] = zlrqstart;
	// 需求结束日期
	var zlrqend = jQuery('#produce_formSearch #zlrqend').val();
	map["zlrqend"] = zlrqend;

    // 高级筛选
    // 分组
    var cplxs = jQuery('#produce_formSearch #cplx_id_tj').val();
    map["cplxs"] = cplxs;
    var zts = jQuery('#produce_formSearch #zt_id_tj').val();
    map["zts"] = zts;
    return map;
}

//提供给导出用的回调函数
function ProduceSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="sczlmx.lrsj";
    map["sortLastOrder"]="desc";
    map["sortName"]="sczlmx.xgsj";
    map["sortOrder"]="desc";
    return getProduceSearchData(map);
}



/**
 * 物料列表的状态格式化函数
 * @returns
 */
function ztFormat(value,row,index) {
    // var param = {prefix:$('#produce_formSearch #urlPrefix').val()};
    // let cplx = row.cplxdm;
    // let type = "";
    // if (cplx == "YQ"){
    //     type = $("#produce_formSearch #YQ_auditType").val();
    // }else{
    //     type = $("#produce_formSearch #SJ_auditType").val();
    // }
    var type = "AUDIT_YQ_PRODUCE";
    if(row.cplxdm=="SJ"){
        type = "AUDIT_SJ_PRODUCE";
    }
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sczlmxid + "\",event,\""+type+"\",{prefix:\"" + $('#produce_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sczlmxid + "\",event,\""+type+"\",{prefix:\"" + $('#produce_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else{
    	 return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sczlmxid + "\",event,\""+type+"\",{prefix:\"" + $('#produce_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}

function xqdhFormat(value,row,index) {
    if (row.xqdh){
        return "<a href='javascript:void(0);' onclick='queryByXqdh(\"" + row.cpxqid + "\")' >"+row.xqdh+"</a>";
    }else{
        row.xqdh = "-";
        return "<a href='javascript:void(0);'>"+row.xqdh+"</a>";
    }
}

function queryByXqdh(cpxqid){
    var url=$("#produce_formSearch #urlPrefix").val()+"/progress/progress/viewProgress?cpxqid="+cpxqid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'需求计划',viewProduceXqdhConfig);
}

var viewProduceXqdhConfig = {
    width		: "1000px",
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
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
    if (row.zt == '10' && row.scbj =='0' ) {
        return "<span class='btn btn-warning' onclick=\"recallMaterProduce('" + row.sczlmxid + "',event,'" + row.shlx + "')\" >撤回</span>";
    }else if(row.zt == '10' && row.scbj ==null ){
        return "<span class='btn btn-warning' onclick=\"recallMaterProduce('" + row.sczlmxid + "',event,'" + row.shlx + "')\" >撤回</span>";
    }else{
        return "";
    }
}

//撤回项目提交
function recallMaterProduce(sczlmxid,event,shlx){
    // let type = "";
    // if (cplx == "YQ"){
    //     type = $("#produce_formSearch #YQ_auditType").val();
    // }else{
    //     type = $("#produce_formSearch #SJ_auditType").val();
    // }
    var type = shlx;
	var msg = '您确定要撤回该条数据吗？';
	var mater_params=[];
	mater_params.prefix=$('#produce_formSearch #urlPrefix').val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(type,sczlmxid,function(){
				searchProduceResult();
			},mater_params);
		}
	});
}



var viewProduceConfig = {
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

var addProduceConfig = {
    width		: "300px",
    height		: "50px",
    modalName	:"addProduceConfigModal",
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

produceModConfig = {
    width		: "1600px",
    modalName	:"produceConfig",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "提  交",
            className : "btn-primary",
            callback : function() {
                if(!$("#produce_ajaxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(var i=0;i<t_map.rows.length;i++){
                        if(t_map.rows[i].xlh==null || t_map.rows[i].xlh==''){
                            $.alert("序列号不能为空！");
                            return false;
                        }
                        var sz = {"xqjhmxid":'',"cpxqid":'',"xlh":'',"wlid":'',"scsl":'',"yjwcsj":''};
                        sz.xqjhmxid = $("#produce_ajaxForm #xqjhmxid").val();
                        sz.cpxqid = $("#produce_ajaxForm #cpxqid").val();
                        sz.xlh = t_map.rows[i].xlh;
                        sz.wlid = t_map.rows[i].wlid;
                        sz.scsl = t_map.rows[i].scsl;
                        sz.yjwcsj = t_map.rows[i].yjwcsj;
                        json.push(sz);
                    }
                    $("#produce_ajaxForm #sczlmx_json").val(JSON.stringify(json));
                }else{
                    $.alert("指令明细明细信息不能为空！");
                    return false;
                }
                if ($("#produce_ajaxForm #bysl").val()){
                    let bysl = $("#produce_ajaxForm #jhcl").val()*1 - $("#produce_ajaxForm #bysl").val()*1;
                    $("#produce_ajaxForm #bysl").val(bysl)
                }
                $("#produce_ajaxForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"produce_ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        // let cplx = $("#produce_ajaxForm #cplxmc").val();
                        // var auditType = "";
                        // if (cplx == "YQ"){
                        //     auditType = $("#produce_formSearch #YQ_auditType").val();
                        // }else{
                        //     auditType = $("#produce_formSearch #SJ_auditType").val();
                        // }
                        var auditType = $("#produce_ajaxForm #shlx").val();
                        var progress_params=[];
                        progress_params.prefix=$('#produce_formSearch #urlPrefix').val();
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                            $.closeModal(opts.modalName);
                        }
                        showAuditFlowDialog(auditType,  responseText["ywids"],function(){
                            searchProduceResult();
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
                if(!$("#produce_ajaxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(var i=0;i<t_map.rows.length;i++){
                        if(t_map.rows[i].xlh==null || t_map.rows[i].xlh==''){
                            $.alert("序列号不能为空！");
                            return false;
                        }

                        var sz = {"xqjhmxid":'',"cpxqid":'',"xlh":'',"wlid":'',"scsl":'',"yjwcsj":''};
                        sz.xqjhmxid = $("#produce_ajaxForm #xqjhmxid").val();
                        sz.cpxqid = $("#produce_ajaxForm #cpxqid").val();
                        sz.xlh = t_map.rows[i].xlh;
                        sz.wlid = t_map.rows[i].wlid;
                        sz.scsl = t_map.rows[i].scsl;
                        sz.yjwcsj = t_map.rows[i].yjwcsj;
                        json.push(sz);
                    }
                    $("#produce_ajaxForm #sczlmx_json").val(JSON.stringify(json));
                }else{
                    $.alert("指令明细明细信息不能为空！");
                    return false;
                }
                $("#produce_ajaxForm input[name='access_token']").val($("#ac_tk").val());
                if ($("#produce_ajaxForm #bysl").val()){
                    let bysl = $("#produce_ajaxForm #jhcl").val()*1 - $("#produce_ajaxForm #bysl").val()*1;
                    $("#produce_ajaxForm #bysl").val(bysl)
                }
                submitForm(opts["formName"] || "produce_ajaxForm", function (responseText, statusText) {
                    if (responseText["status"] == 'success') {
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal("produceProgressModal");
                                $.closeModal(opts.modalName);
                            }
                            searchProduceResult();
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

produceSubmitConfig = {
    width		: "1200px",
    modalName	:"produceSubmitModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "提  交",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                $('#produce_audit_ajaxForm #ids').val($('#produce_audit_ajaxForm #ywids').val());
                $("#produce_audit_ajaxForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"produce_audit_ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        var auditType = plsumitshlx
                        var progress_params=[];
                        progress_params.prefix=$('#produce_formSearch #urlPrefix').val();
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        showAuditFlowDialog(auditType,responseText["ywids"],function(){
                            searchProduceResult();
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
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
//按钮动作函数
function produceDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $('#produce_formSearch #urlPrefix').val() + tourl; 
	if(action =='view'){
		var url= tourl + "?sczlmxid=" +id;
		$.showDialog(url,'查看',viewProduceConfig);
	}else if (action =='add'){
        $.showDialog(tourl,'新增',addProduceConfig);
    } else if (action =='mod'){
        var url= tourl + "?sczlmxid=" +id;
        $.showDialog(url,'修改',produceModConfig);
    }else if (action =='bulksubmit'){
        var url= tourl + "?ids=" +id+"&formAction=/progress/produce/pagedataProduceSubmitSave";
        $.showDialog(url,'批量提交',produceSubmitConfig);
    }
}


var produce_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_view = $("#produce_formSearch #btn_view");
    	var btn_query = $("#produce_formSearch #btn_query");
        var btn_add = $("#produce_formSearch #btn_add");
        var btn_mod=$("#produce_formSearch #btn_mod");// 修改
        var btn_del = $("#produce_formSearch #btn_del");// 删除
        var btn_discard = $("#produce_formSearch #btn_discard");//领料废弃
        var btn_bulksubmit=$("#produce_formSearch #btn_bulksubmit");// 批量提交
        var btn_searchexport = $("#produce_formSearch #btn_searchexport");
        var btn_selectexport = $("#produce_formSearch #btn_selectexport");
        var btn_print = $("#produce_formSearch #btn_print");

    	//添加日期控件
    	laydate.render({
    	   elem: '#zlrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#zlrqend'
    	  ,theme: '#2381E9'
    	});

    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchProduceResult(true);
    		});
    	}
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#produce_formSearch #produce_tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			produceDealById(sel_row[0].sczlmxid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
        /*-------------------新增------------------*/
        btn_add.unbind("click").click(function(){
            produceDealById(null,"add",btn_add.attr("tourl"));
        });

        /* --------------------------- 修改 -----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#produce_formSearch #produce_tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
                    produceDealById(sel_row[0].sczlmxid, "mod", btn_mod.attr("tourl"));
                }else{
                    $.alert("该记录在审核中或已审核，不允许修改!");
                }
            }else{
                $.error("请选中一行");
            }
        });

        /* --------------------------- 批量提交 -----------------------------------*/
        btn_bulksubmit.unbind("click").click(function(){
            var sel_row = $('#produce_formSearch #produce_tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                if (sel_row[i].cplxdm != "YQ"){
                    $.error("请选择产品类别是仪器的数据！");
                    return;
                }
                if (sel_row[0].shlx != sel_row[i].shlx){
                    $.error("请选择审核类型一致的数据！");
                    return;
                }
                if(sel_row[i].zt!="00" && sel_row[i].zt!="15"){
                    $.alert("存在记录在审核中或已审核，不允许提交!");
                    return;
                }
                ids = ids + ","+ sel_row[i].sczlmxid;
            }
            ids = ids.substr(1);
            //批量审核类型
            plsumitshlx = sel_row[0].shlx;
            produceDealById(ids, "bulksubmit", btn_bulksubmit.attr("tourl"));
        });
        /* ------------------------------删除领料信息-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#produce_formSearch #produce_tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].sczlmxid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $("#produce_formSearch #urlPrefix").val()+btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchProduceResult();
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
            var sel_row = $('#produce_formSearch #produce_tb_list').bootstrapTable('getSelections');//获取选择行数据
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
                    ids= ids + ","+ sel_row[i].sczlmxid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要废弃所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url=$('#produce_formSearch #urlPrefix').val() + btn_discard.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchProduceResult();
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

        // 	  ---------------------------导出-----------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#produce_formSearch #produce_tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].sczlmxid;
                }
                ids = ids.substr(1);
                $.showDialog($('#produce_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=PRODUCTIONORDER_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });

        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#produce_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=PRODUCTIONORDER_SEARCH&expType=search&callbackJs=ProduceSearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
        /* ---------------------------打印-----------------------------------*/
        btn_print.unbind("click").click(function(){
            var sel_row = $('#produce_formSearch #produce_tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].sczlmxid;
            }
            ids = ids.substr(1);
            var url=$('#produce_formSearch #urlPrefix').val()+btn_print.attr("tourl")+"?ids="+ids+"&access_token="+$("#ac_tk").val();
            window.open(url);
        });
        /*-----------------------显示隐藏------------------------------------*/
        $("#produce_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(produce_turnOff){
                $("#produce_formSearch #searchMore").slideDown("low");
                produce_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#produce_formSearch #searchMore").slideUp("low");
                produce_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    };

    return oInit;
};
var produce_turnOff=true;

$(function(){
    //1.初始化Table
    var oTable = new produce_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new produce_ButtonInit();
    oButtonInit.Init();
	//所有下拉框添加choose样式
	jQuery('#produce_formSearch .chosen-select').chosen({width: '100%'});
	
});
