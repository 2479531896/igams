
var modDocumentAudit_turnOff=true;

var modDocumentAuditing_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#modDocument_auditing #tb_list').bootstrapTable({
            url: $('#modDocument_formAudit #urlPrefix').val() + '/production/document/pageGetListModDocumentAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#modDocument_auditing #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "shgc.sqsj",				//排序字段
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
            uniqueId: "wjid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'no',
                title: '序号',
                titleTooltip:'序号',
                width: '10%',
                align: 'left',
                visible: true,
                formatter:xhFormat
            }, {
                field: 'wjid',
                title: '文件ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'shxx_shid',
                title: '审核ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'wjbh',
                title: '文件编号',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'wjmc',
                title: '文件名称',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'wjflmc',
                title: '文件分类',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'wjlbmc',
                title: '文件类别',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'bbh',
                title: '版本',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'sxrq',
                title: '生效日期',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'sqrxm',
                title: '申请人员',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '16%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zt',
                title: '状态',
                width: '10%',
                align: 'left',
                formatter:wjsqztFormat,
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	modDocumentAuditDealById(row.wjid, 'view',$("#modDocument_auditing #btn_view").attr("tourl"));
            },
        });
        $("#modDocument_auditing #tb_list").colResizable(
        		{
                    liveDrag:true, 
                    gripInnerHtml:"<div class='grip'></div>", 
                    draggingClass:"dragging", 
                    resizeMode:'fit', 
                    postbackSafe:true,
                    partialRefresh:true
        		}
            );
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
        	pageSize: params.limit,   //页面大小
        	pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "wj.wjid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var cxtj = $("#modDocument_auditing #cxtj").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#modDocument_auditing #cxnr').val());
    	// '0':'文件名称','1':'生产商'
    	if (cxtj == "0") {
    		map["wjmc"] = cxnr;
    	}else if (cxtj == "1") {
    		map["scs"] = cxnr;
    	}
    	map["dqshzt"] = 'dsh';
    	
    	return map;
    };
    return oTableInit;
};

//序号格式化
function xhFormat(value, row, index) {
    //获取每页显示的数量
    var pageSize=$('#modDocument_auditing #tb_list').bootstrapTable('getOptions').pageSize;
    //获取当前是第几页
    var pageNumber=$('#modDocument_auditing #tb_list').bootstrapTable('getOptions').pageNumber;
    //返回序号，注意index是从0开始的，所以要加上1
    return pageSize * (pageNumber - 1) + index + 1;
}
/**
 * 文件申请列表的状态格式化函数
 * @returns
 */
function wjsqztFormat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return '审核通过';
    }
    else{
        return row.shxx_gwmc + '审核中';
    }
}

var modDocumentAudited_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#modDocument_audited #tb_list').bootstrapTable({
            url: $('#modDocument_formAudit #urlPrefix').val() + '/production/document/pageGetListModDocumentAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#modDocument_audited #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "shxx.shsj",				//排序字段
            sortOrder: "desc",                  //排序方式
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
            //height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "shxx_shxxid",            //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                  //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'wjid',
                title: '文件ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'shxx_shid',
                title: '审核ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'wjflmc',
                title: '文件分类',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'wjlbmc',
                title: '文件类别',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'wjmc',
                title: '文件名称',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'wjbh',
                title: '文件编号',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'sqrxm',
                title: '申请人员',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_lrryxm',
                title: '审核人',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_sftgmc',
                title: '是否通过',
                width: '4%',
                align: 'left',
                visible: true
            },{
                field: 'lrsj',
                align: 'center',
                title: '录入时间',
                visible: false
            },{
                field: 'shxx_shxxid',
                align: 'center',
                title: '审核信息ID',
                visible: false
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	modDocumentAuditDealById(row.wjid, 'view',$("#modDocument_auditing #btn_view").attr("tourl"));
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
        	pageSize: params.limit,   //页面大小
        	pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "wj.wjid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var cxtj_audited = $("#modDocument_audited #cxtj_audited").val();
    	// 查询条件
    	var cxnr_audited = $.trim(jQuery('#modDocument_audited #cxnr_audited').val());
    	// '0':'文件名称','1':'申请人姓名'
    	if (cxtj_audited == "0") {
    		map["wjmc"] = cxnr_audited;
    	}else if (cxtj_audited == "1") {
    		map["sqrxm"] = cxnr_audited;
    	}
    	// 申请开始日期
    	var sqsjstart = jQuery('#modDocument_audited #sqsjstart').val();
    	map["sqsjstart"] = sqsjstart;
    	
    	// 申请结束日期
    	var sqsjend = jQuery('#modDocument_audited #sqsjend').val();
    	map["sqsjend"] = sqsjend;
    	map["dqshzt"] = 'ysh';
    	return map;
    };
    return oTableInit;
};

//按钮动作函数
function modDocumentAuditDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $('#modDocument_formAudit #urlPrefix').val() + tourl;
	if(action =='view'){
		var url= tourl + "?wjid=" +id;
		$.showDialog(url,'查看申请',viewModDocumentAuditConfig);
	}else if(action =='audit'){
		var url= tourl;
		showAuditDialogWithLoading({
			id: id,
			type: 'QUALITY_FILE_MOD',
			url: tourl,
			data:{'ywzd':'wjid'},
			title:$("#modDocument_auditing #btn_audit").attr("gnm"),
			preSubmitCheck:'preSubmitModDocument',
			callback:function(){
				searchModDocumentAudit();//回调
			},
			prefix:$('#modDocument_formAudit #urlPrefix').val(),
			dialogParam:{width:1000}
		});
	}else if(action =='batchaudit'){
		var url= tourl + "?ywids=" +id+"&shlb=QUALITY_FILE_MOD&ywzd=wjid&business_url="+tourl;
		$.showDialog(url,'批量审核',batchAuditModDocumentConfig);
	}
};

//上传后自动提交服务器检查进度
var checkDocumentModeAuditCheckStatus = function(intervalTime,loadYmCode){
	$.ajax({
		type : "POST",
		url : $('#modDocument_formAudit #urlPrefix').val() + "/systemcheck/auditProcess/commCheckAudit",
		data : {"loadYmCode":loadYmCode,"access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.cancel){//取消则直接return
				return;
			}
			if(data.result==false){
				if(data.status == "-2"){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}
				else{
					if(intervalTime < 5000)
						intervalTime = intervalTime + 1000;
					if($("#exportCount")){
						$("#exportCount").html(data.currentCount)
					}
					setTimeout("checkDocumentModeAuditCheckStatus("+intervalTime+","+ loadYmCode +")",intervalTime)
				}
			}else{
				if(data.status == "2"){
					if($("#cardiv")) $("#cardiv").remove();
					$.success(data.msg,function(){
						$.closeModal("batchAuditModal");
						searchModDocumentAudit();
					});
				}else if(data.status == "0"){
					if($("#cardiv")) $("#cardiv").remove();
					$.alert(data.msg,function(){
						$.closeModal("batchAuditModal");
						searchModDocumentAudit();
					});
				}else{
					if($("#cardiv")) $("#cardiv").remove();
					$.error(data.msg,function(){
						$.closeModal("batchAuditModal");
						searchModDocumentAudit();
					});
				}
			}
		}
	});
}

var batchAuditModDocumentConfig = {
	width		: "800px",
	modalName	: "batchAuditModal",
	formName	: "batchAuditAjaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#batchAuditAjaxForm").valid()){
					return false;
				}
				if($("#batchAuditAjaxForm #lastStep").val() == 'true'){
					$.alert("最后一步不支持批量审核！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#batchAuditAjaxForm input[name='access_token']").val($("#ac_tk").val());
				var loadYmCode = $("#batchAuditAjaxForm #batchaudit_loadYmCode").val();
				var sel_row = $('#modDocument_auditing #tb_list').bootstrapTable('getSelections');//获取选择行数据
				//创建objectNode
				var cardiv =document.createElement("div");
				cardiv.id="cardiv";
				var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span style="color:red;font-weight:600;">/' + sel_row.length + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
				cardiv.innerHTML=s_str;
				//将上面创建的P元素加入到BODY的尾部
				document.body.appendChild(cardiv);
				
				submitForm(opts["formName"]||"batchAuditAjaxForm",function(responseText,statusText){
					//1.启动进度条检测
					setTimeout("checkDocumentModeAuditCheckStatus(2000,'"+ loadYmCode + "')",1000);
					
					//绑定导出取消按钮事件
					$("#exportCancel").click(function(){
						//先移除导出提示，然后请求后台
						if($("#cardiv").length>0) $("#cardiv").remove();
						$.ajax({
							type : "POST",
							url : $('#modDocument_formAudit #urlPrefix').val() + "/systemcheck/auditProcess/cancelAuditProcess",
							data : {"loadYmCode" : loadYmCode+"","access_token":$("#ac_tk").val()},
							dataType : "json",
							success:function(data){
								if(data != null && data.result==false){
									if(data.msg && data.msg!="")
										$.error(data.msg);
								}
							}
						});
					});
				});
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function preSubmitModDocument(){
	if(!$("#auditAjaxForm").valid()){
		return false;
	}
	if($("#auditAjaxForm #lastStep").val() == "true"){
		if(!$("#auditAjaxForm #jsids").val() && !$("#auditAjaxForm #t_jsids").val() && $("#auditAjaxForm #sftg").val() == "1"){
			$.error("请选择权限信息！");
			return false;
		}
	}
    let json = [];
    if(t_map.rows != null && t_map.rows.length > 0) {
        for (let i = 0; i < t_map.rows.length; i++) {
            var sz = {"index":'',"zwjid":''};
            sz.index=t_map.rows.length;
            sz.zwjid = t_map.rows[i].zwjid;
            json.push(sz);
        }
    }
    var wjlbcskz2 = $("#ajaxForm #wjlb").find("option:selected").attr("cskz2");
    $("#ajaxForm #wjlbcskz2").val(wjlbcskz2);
    $("#ajaxForm #glwj_json").val(JSON.stringify(json));
    $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
	return true;
};

var viewModDocumentAuditConfig = {
	width		: "800px",
	modalName	: "viewmoddocumentModal",
	formName	: "viewmoddocument_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var vm = new Vue({
	el:'#modDocumentAudit',
	data: {
	},
	methods: {
		handButton(data){
			var btn_audit = $("#modDocument_auditing #btn_audit");
	    	var btn_view = $("#modDocument_auditing #btn_view");
	    	var btn_batchaudit = $("#modDocument_auditing #btn_batchaudit");
			
			if(data == "view"){
				var sel_row = $('#modDocument_auditing #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					modDocumentAuditDealById(sel_row[0].wjid,"view",btn_view.attr("tourl"));
	    		}else{
	    			$.error("请选中一行");
	    		}
			}else if(data == "audit"){
				var sel_row = $('#modDocument_auditing #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					modDocumentAuditDealById(sel_row[0].wjid,"audit",btn_audit.attr("tourl"));
	    		}else{
	    			$.error("请选中一行");
	    		}
			}else if(data == "batchaudit"){
				var sel_row = $('#modDocument_auditing #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length > 0){
					var wjids = "";
					var gwmc = sel_row[0].shxx_gwmc;
	        		for (var i = 0; i < sel_row.length; i++) {
	        			if(gwmc != sel_row[i].shxx_gwmc){
	        				$.error("请选择相同阶段审核");
	        				return;
	        			}
	        			wjids = wjids + ","+ sel_row[i].wjid;
	        		}
        			wjids = wjids.substr(1);
        			modDocumentAuditDealById(wjids,"batchaudit",btn_batchaudit.attr("tourl"));
	    		}else{
	    			$.error("请至少选中一行");
	    		}
			}
		},
		modAuditingEnter(){
			searchModDocumentAudit(true);
		},
		modAuditedEnter(){
			searchModDocumentAudited(true);
		},
		modAuditingBtnQuery(){
			searchModDocumentAudit(true);
		},
		modAuditedBtnQuery(){
			searchModDocumentAudited(true);
		},
		modAuditedBtnCancelAudit(){
			if($("#modDocument_audited #btn_cancelAudit") != null){
				var document_params=[];
				document_params.prefix=$('#modDocument_formAudit #urlPrefix').val();
				cancelAudit($('#modDocument_audited #tb_list').bootstrapTable('getSelections'),function(){
					searchModDocumentAudited();
				},null,document_params);
			}
		}
	},
});

var modDocumentAudit_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
    	//添加日期控件
    	laydate.render({
    	   elem: '#modDocument_audited #sqsjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#modDocument_audited #sqsjend'
    	  ,theme: '#2381E9'
    	});
        //初始化页面上面的按钮事件 
    	
    	//选项卡切换事件回调
    	$('#modDocument_formAudit #audit_docmod a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
    		var _hash = e.target.hash.replace("#",'');
    		var _gridId = "#modDocument_"+ _hash;
    		if(!e.target.isLoaded){//只调用一次
    			if(_hash=='auditeddocmod'){
	    			var oTable = new modDocumentAudited_TableInit();
	    		    oTable.Init();
    			}else{
    				var oTable = new modDocumentAuditing_TableInit();
	    		    oTable.Init();
    			}
    			e.target.isLoaded = true;
    		}
    	}).first().trigger('shown.bs.tab');//触发第一个选中事件
    };

    return oInit;
};

function searchModDocumentAudit(isTurnBack){
	if(isTurnBack){
		$('#modDocument_auditing #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#modDocument_auditing #tb_list').bootstrapTable('refresh');
	}
}

function searchModDocumentAudited(isTurnBack){
	if(isTurnBack){
		$('#modDocument_audited #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#modDocument_audited #tb_list').bootstrapTable('refresh');
	}
	
}


$(function(){

    //2.初始化Button的点击事件
    var oButtonInit = new modDocumentAudit_ButtonInit();
    oButtonInit.Init();
	//所有下拉框添加choose样式
	jQuery('#modDocument_auditing .chosen-select').chosen({width: '100%'});
	jQuery('#modDocument_audited .chosen-select').chosen({width: '100%'});
	
	// 初始绑定显示更多的事件
	$("#modDocument_auditing [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	
});
