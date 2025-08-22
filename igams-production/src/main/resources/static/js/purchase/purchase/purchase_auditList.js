var purchase_audit_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#purchase_audit_formSearch #purchase_audit_list").bootstrapTable({
			url: $('#purchase_formAudit #urlPrefix').val() +'/purchase/purchase/pageGetListAuditPurchase',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#purchase_audit_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "qggl.lrsj",				//排序字段
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
            uniqueId: "qgid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            },{
	            field: 'djh',
	            title: '请购单号',
	            width: '12%',
	            align: 'left',
	            sortable: true,
	            visible: true
	        },{
                field: 'qglx',
                title: '请购类型',
                width: '4%',
                align: 'left',
                formatter:qglxAllFormat,
                sortable: true,   
                visible: false
            },{
	            field: 'qglbmc',
	            title: '类别',
	            width: '6%',
	            align: 'left',
	            sortable: true,
	            formatter:qglbformat,
	            visible: true
	        },{
	            field: 'sqrmc',
	            title: '申请人',
	            width: '5%',
	            align: 'left',
	            sortable: true,
	            visible:true
	        },{
	            field: 'sqbmmc',
	            title: '申请部门',
	            width: '10%',
	            align: 'left',
	            sortable: true,
	            visible: true
	        },{
	            field: 'jlbh',
	            title: '记录编号',
	            width: '6%',
	            align: 'left',
	            sortable: true,
	            visible: false
	        },{
	            field: 'sqrq',
	            title: '申请日期',
	            width: '8%',
	            align: 'left',
	            sortable: true,
	            visible: true
	        },{
	            field: 'xmbmmc',
	            title: '项目编码',
	            width: '8%',
	            align: 'left',
	            sortable: true,
	            visible: true
	        },{
	            field: 'xmdlmc',
	            title: '项目大类',
	            width: '7%',
	            align: 'left',
	            sortable: true,
	            visible: true
	        },{
	            field: 'sqly',
	            title: '申请理由',
	            width: '10%',
	            align: 'left',
	            sortable: true,
	            visible: true
	        },{
	            field: 'zffsmc',
	            title: '支付方式',
	            width: '10%',
	            align: 'left',
	            sortable: false,
	            visible: false
	        },{
	            field: 'zffmc',
	            title: '支付方',
	            width: '10%',
	            align: 'left',
	            sortable: false,
	            visible: false
	        },{
	            field: 'bz',
	            title: '备注',
	            width: '10%',
	            align: 'left',
	            sortable: true,
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
				width: '15%',
				align: 'left',
				sortable: true,
				visible: true
			},{
	            field: 'lrrymc',
	            title: '录入人员',
	            width: '10%',
	            align: 'left',
	            sortable: true,
	            visible: false
	        },{
	            field: 'lrsj',
	            title: '录入时间',
	            width: '10%',
	            align: 'left',
	            visible: false,
	            sortable: true   
	        }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！"); 
            },
            onDblClickRow: function (row, $element) {
            	qggl_audit_DealById(row.qgid,"view",$("#purchase_audit_formSearch #btn_view").attr("tourl"));
             },
		});
		$("#purchase_audit_formSearch #purchase_audit_list").colResizable({
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
            sortLastName: "qggl.qgid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            zt: "10"
            // 搜索框使用
            // search:params.search
        };
		var cxtj=$("#purchase_audit_formSearch #cxtj").val();
		var cxnr=$.trim(jQuery('#purchase_audit_formSearch #cxnr').val());
		if(cxtj=="0"){
			map["djh"]=cxnr
		}else if (cxtj=='1'){
			map["sqbmmc"]=cxnr
		}else if (cxtj=='2'){
			map["sqrmc"]=cxnr
		}
		map["dqshzt"] = 'dsh';
		return map;
	}
	return oTableInit;
}

function qglbformat(value,row,index){
	if(row.qglbmc==null || row.qglbmc==''){
		return "物料";
	}
	return row.qglbmc;
}

function qglxAllFormat(value,row,index){
    if(row.qglx=='1'){
        return "OA请购";
    }else{
        return "普通请购";
    }
}

var sbyzAudited_TableInit=function(){
	var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
    	$("#purchase_audit_audited #tb_list").bootstrapTable({
			url: $('#purchase_formAudit #urlPrefix').val()+'/purchase/purchase/pageGetListAuditPurchase',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#purchase_audit_audited #toolbar',                //工具按钮用哪个容器
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
            isForceTable:true,
            columns: [{
                checkbox: true
            },{
	            field: 'djh',
	            title: '请购单号',
	            width: '10%',
	            align: 'left',
	            visible: true
	        },{
                field: 'qglx',
                title: '请购类型',
                width: '4%',
                align: 'left',
                formatter:qglxAllFormat,
                sortable: true,   
                visible: false
            },{
	            field: 'sqrmc',
	            title: '申请人',
	            width: '5%',
	            align: 'left',
	            visible:true
	        },{
	            field: 'sqbmmc',
	            title: '申请部门',
	            width: '10%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'jlbh',
	            title: '记录编号',
	            width: '6%',
	            align: 'left',
	            visible: false
	        },{
	            field: 'sqrq',
	            title: '申请日期',
	            width: '8%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'xmbmmc',
	            title: '项目编码',
	            width: '8%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'xmdlmc',
	            title: '项目大类',
	            width: '7%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'sqly',
	            title: '申请理由',
	            width: '10%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'zffsmc',
	            title: '支付方式',
	            width: '7%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'fkfmc',
	            title: '支付方',
	            width: '5%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'bz',
	            title: '备注',
	            width: '10%',
	            align: 'left',
	            visible: true
	        },{
                field: 'shxx_shid',
                title: '审核ID',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_lrryxm',
                title: '审核人',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_sftgmc',
                title: '是否通过',
                width: '10%',
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
            	qggl_audit_DealById(row.qgid,"view",$("#purchase_audit_formSearch #btn_view").attr("tourl"));
            },
        });
    	$("#purchase_audit_audited #tb_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true        
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
            sortLastName: "qggl.qgid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        var cxtj=$("#purchase_audit_audited #cxtj").val();
		var cxnr=$.trim(jQuery('#purchase_audit_audited #cxnr').val());
		if(cxtj=="0"){
			map["djh"]=cxnr
		}else if (cxtj=='1'){
			map["sqbmmc"]=cxnr
		}else if (cxtj=='2'){
			map["sqrmc"]=cxnr
		}
    	map["dqshzt"] = 'ysh';
    	return map;
    };
    return oTableInit;
}

var auditType="AUDIT_REQUISITIONS";//默认
function qggl_audit_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $('#purchase_formAudit #urlPrefix').val() + tourl; 
	if(action=="view"){
		var url=tourl+"?qgid="+id;
		$.showDialog(url,'查看信息',viewVerification_audit_Config);
	}else if(action=="audit"){
		showAuditDialogWithLoading({
			id: id,
			type:auditType ,
			url:tourl,
			data:{'ywzd':'qgid'},
			title:"物料请购审核",
			preSubmitCheck:'preSubmitPurchase',
			prefix:$('#purchase_formAudit #urlPrefix').val(),
			callback:function(){
				searchQggl_audit_Result(true);//回调
			},
			dialogParam:{width:1500}
		});
	}else if(action=="download"){
		var url=tourl+"?qgid="+id;
		$.showDialog(url,'附件下载',downloadPurchaseConfig);
	}
}

function preSubmitPurchase(){
	let shlc = $("#purchaseAuditViewForm #shlc").val();
	if(!shlc){
		if($("#shoppingCarForm #qglbdm").val() == "MATERIAL"){
			let jgdm= $("#shoppingCarForm #sqbmdm").val();
			if(!jgdm){
				$.alert("所选部门存在异常！");
				return false;
			}
		}
	}
	return true;
}

var purchase_audit_oButtton=function(){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#purchase_audit_formSearch #btn_query");//模糊查询
		var btn_queryAudited = $("#purchase_audit_audited #btn_query");//审核记录列表模糊查询
    	var btn_cancelAudit = $("#purchase_audit_audited #btn_cancelAudit");//取消审核
    	var btn_view = $("#purchase_audit_formSearch #btn_view");//查看页面
    	var btn_audit = $("#purchase_audit_formSearch #btn_audit");//审核
    	var btn_download= $("#purchase_audit_formSearch #btn_download");
    	
    	/*-----------------------模糊查询(审核列表)------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchQggl_audit_Result(true);
    		});
		}
		
		/*-----------------------模糊查询(审核记录)------------------------------------*/
		if(btn_queryAudited!=null){
			btn_queryAudited.unbind("click").click(function(){
				searchQgglAudited(true);
    		});
		}
		/*--------------------------------下载请购附件---------------------------*/
		btn_download.unbind("click").click(function(){
			var sel_row=$('#purchase_audit_formSearch #purchase_audit_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			qggl_audit_DealById(sel_row[0].qgid,"download",btn_download.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		
		/*---------------------------查看页面--------------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row=$('#purchase_audit_formSearch #purchase_audit_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				qggl_audit_DealById(sel_row[0].qgid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*---------------------------审核--------------------------------*/
    	btn_audit.unbind("click").click(function(){
    		var sel_row = $('#purchase_audit_formSearch #purchase_audit_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			if(sel_row[0].shlx!=null && sel_row[0].shlx!=''){
    				auditType=sel_row[0].shlx;
    			}
    			qggl_audit_DealById(sel_row[0].qgid,"audit",btn_audit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------查看审核记录--------------------------------*/
    	//选项卡切换事件回调
    	$('#purchase_formAudit #purchase_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
    		var _hash = e.target.hash.replace("#",'');
    		if(!e.target.isLoaded){//只调用一次
    			if(_hash=='purchase_auditing'){
    				var oTable= new purchase_audit_TableInit();
    				oTable.Init();

    			}else{
    				var oTable= new sbyzAudited_TableInit();
    				oTable.Init();
    			}
    			e.target.isLoaded = true;
    		}else{//重新加载
    			//$(_gridId + ' #tb_list').bootstrapTable('refresh');
    		}
    	}).first().trigger('shown.bs.tab');//触发第一个选中事件
    	
    	/*-------------------------------------取消审核-----------------------------------*/
    	if(btn_cancelAudit!=null){
    		btn_cancelAudit.unbind("click");
    		btn_cancelAudit.click(function(){
    			var purchase_params=[];
    			purchase_params.prefix=$('#purchase_formAudit #urlPrefix').val();
    			cancelAudit($('#purchase_audit_audited #tb_list').bootstrapTable('getSelections'),function(){
    				searchQgglAudited();
    			},null,purchase_params);
    		})
    	}
	}
	return oInit;
}

var downloadPurchaseConfig = {
		width		: "1500px",
		modalName	: "downloadPurchaseModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#downloadpurchase_formSearch").valid()){
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					var wlzlbtcs=$("#downloadpurchase_formSearch #wlzlbtc_id_tj").val();
					var ywlx=$("#downloadpurchase_formSearch #ywlx").val();
					var qgid=$("#downloadpurchase_formSearch #qgid").val();
					if(wlzlbtcs==null || wlzlbtcs==""){
						$.confirm("请选择筛选条件!");
						return false;
					}
					var map = {
			    			access_token:$("#ac_tk").val(),
			    			wlzlbtcs:wlzlbtcs,
			    			ywlx:ywlx,
			    			qgid:qgid,
			    		}; 
					var url= $("#downloadpurchase_formSearch #action").val();
					$.post(url,map,function(data){
						if(data){
							if(data.status == 'success'){
								if(data.count==0 || !data.count){
									$.confirm("未查询到附件!");
									return false;
								}
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								//创建objectNode
								var cardiv =document.createElement("div");
								cardiv.id="cardiv";
								var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span id="totalCount" style="color:red;font-weight:600;">/' + data.count + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
								cardiv.innerHTML=s_str;
								//将上面创建的P元素加入到BODY的尾部
								document.body.appendChild(cardiv);
								setTimeout("checkReportZipStatus(2000,'"+ data.redisKey + "')",1000);
								//绑定导出取消按钮事件
								$("#exportCancel").click(function(){
									//先移除导出提示，然后请求后台
									if($("#cardiv").length>0) $("#cardiv").remove();
									$.ajax({
										type : "POST",
										url : $('#purchase_formAudit #urlPrefix').val()+"/common/export/commCancelExport",
										data : {"wjid" : data.redisKey+"","access_token":$("#ac_tk").val()},
										dataType : "json",
										success:function(res){
											if(res != null && res.result==false){
												if(res.msg && res.msg!="")
													$.error(res.msg);
											}

										}
									});
								});
							}else if(data.status == 'fail'){
								$.error(data.error,function() {
								});
							}else{
								if(data.error){
									$.alert(data.error,function() {
									});
								}
							}
						}
					},'json');
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

var viewVerification_audit_Config = {
		width		: "1080px",
		modalName	:"viewVerificationConfig",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

//自动检查报告压缩进度
var checkReportZipStatus = function(intervalTime,redisKey){
	$.ajax({
		type : "POST",
		url : $('#purchase_formAudit #urlPrefix').val()+"/common/export/commCheckExport",
		data : {"wjid" : redisKey +"","access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.cancel){//取消导出则直接return
				return;
			}
			if(data.result == false){
				if(data.msg && data.msg!=""){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}else{
					if(intervalTime < 5000)
						intervalTime = intervalTime + 1000;
					if($("#exportCount")){
						$("#exportCount").html(data.currentCount)
						if(("/"+data.currentCount) == $("#totalCount").text()){
							$("#totalCount").html($("#totalCount").text()+" 压缩中....")
						}
					}
					setTimeout("checkReportZipStatus("+intervalTime+",'"+ redisKey +"')",intervalTime);
				}
			}else{
				if(data.msg && data.msg!=""){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}
				else{
					if($("#cardiv")) $("#cardiv").remove();
					window.open($('#purchase_formAudit #urlPrefix').val()+"/common/export/commDownloadExport?wjid="+redisKey + "&access_token="+$("#ac_tk").val());
				}
			}
		}
	});
}

function searchQggl_audit_Result(isTurnBack){
	if(isTurnBack){
		$('#purchase_audit_formSearch #purchase_audit_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#purchase_audit_formSearch #purchase_audit_list').bootstrapTable('refresh');
	}
}

function searchQgglAudited(isTurnBack){
	if(isTurnBack){
		$('#purchase_audit_audited #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#purchase_audit_audited #tb_list').bootstrapTable('refresh');
	}
}

$(function(){
	var oTable= new purchase_audit_TableInit();
	oTable.Init();
	
	var oButtonInit = new purchase_audit_oButtton();
	oButtonInit.Init();
	
	// 所有下拉框添加choose样式
	jQuery('#purchase_audit_formSearch .chosen-select').chosen({width: '100%'});
	jQuery('#purchase_audit_audited .chosen-select').chosen({width: '100%'});
})