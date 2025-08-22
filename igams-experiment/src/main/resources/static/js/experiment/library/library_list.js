var Library_turnOff=true;

var Library_TableInit = function () {
	var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#library_formSearch #tb_list').bootstrapTable({
			url: '/experiment/library/pageGetListLibrary',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#library_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"lrsj",					// 排序字段
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
			uniqueId: "wkid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true
			}, {
				'field': '',
			    'title': '序号',
			    'align': 'center',
			    'width': '6%',
			    'formatter': function (value, row, index) {
			    　　var options = $('#library_formSearch #tb_list').bootstrapTable('getOptions'); 
			    　　return options.pageSize * (options.pageNumber - 1) + index + 1; 
			    }
			}, {
				field: 'wkmc',
				title: '文库名称',
				width: '15%',
				align: 'left',
				visible: true
			}, {
				field: 'lrsj',
				title: '创建日期',
				width: '12%',
				align: 'left',
				visible: true
			}, {
				field: 'jcdwmc',
				title: '检测单位',
				width: '10%',
				align: 'left',
				visible: true
			},  {
				field: 'sjph1',
				title: 'DNA建库试剂盒',
				width: '18%',
				align: 'left',
				visible: true
			}, {
				field: 'sjph2',
				title: '逆转录试剂盒',
				width: '18%',
				align: 'left',
				visible: true
			}, {
				field: 'sjph3',
				title: '文库定量试剂盒',
				width: '15%',
				align: 'left',
				visible: true
			}, {
				field: 'lrrymc',
				title: '创建人员',
				width: '8%',
				align: 'left',
				visible: true
			}, {
				field: 'xgsj',
				title: '修改日期',
				width: '12%',
				align: 'left',
				visible: true
			}, {
				field: 'xgrymc',
				title: '修改人员',
				width: '8%',
				align: 'left',
				visible: true
			}, {
				field: 'sjscsj',
				title: '上传时间',
				width: '12%',
				align: 'left',
				visible: true
			}, {
				field: 'zt',
				title: '状态',
				width: '6%',
				align: 'left',
				formatter:ztformat,
				visible: true
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				LibraryById(row.wkid,'view',$("#library_formSearch #btn_view").attr("tourl"));
			},
		});
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
        sortLastName: "lrsj", // 防止同名排位用
        sortLastOrder: "desc" // 防止同名排位用
        // 搜索框使用
        // search:params.search
    };
    return getLibrarySearchData(map);
	};

	return oTableInit;
};

//状态格式化
function ztformat(value,row,index){
	if(row.zt=="未通过"){
		var zt="<span style='color:red;'>"+row.zt+"</span>";
	}else{
		var zt=row.zt;
	}
	return zt;
}

function getLibrarySearchData(map){
	var cxtj=$("#library_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#library_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["lrrymc"]=cxnr
	}else if(cxtj=="1"){
		map["xgrymc"]=cxnr
	}else if(cxtj=="2"){
		map["wkmc"]=cxnr
	}else if(cxtj=="3"){
		map["jcdwmc"]=cxnr
	}else if(cxtj=="4"){
		map["sjph1"]=cxnr
	}else if(cxtj=="5"){
		map["sjph2"]=cxnr
	}else if(cxtj=="6"){
		map["sjph3"]=cxnr
	}else if(cxtj=="7"){
		map["nbbh"]=cxnr
	}
	// 创建时间开始日期
	var cjsjstart = jQuery('#library_formSearch #cjsjstart').val();
	map["cjsjstart"] = cjsjstart;
	
	// 创建时间结束日期
	var cjsjend = jQuery('#library_formSearch #cjsjend').val();
	map["cjsjend"] = cjsjend;
	 return map;
}

function LibraryResult(isTurnBack){
	if(isTurnBack){
		$('#library_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#library_formSearch #tb_list').bootstrapTable('refresh');
	}
}

function LibraryById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?wkid=" +id;
		$.showDialog(url,'文库详细信息',viewLibraryConfig);
	}else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'新增文库信息',addLibraryConfig);
	}else if(action =='mod'){
		var url=tourl + "?wkid=" +id;
		$.showDialog(url,'编辑文库明细',modLibraryConfig);
	}else if(action =='addpotency'){
		var url=tourl + "?wkid=" +id;
		$.showDialog(url,'录入浓度',addPotencyLibraryConfig);
	}else if(action =='imports_conc'){
		var url=tourl+"?wkid="+id;
		$.showDialog(url,'导入',importPotencyLibraryConfig);	
	}else if(action =='pcrdockingfile'){
		var url=tourl+"?wkid="+id;
		$.showDialog(url,'生成PCR对接文档',pcrdockingfileLibraryConfig);	
	}else if(action =='uploadcomputerwatch'){
		var url=tourl+"?wkid="+id;
		$.showDialog(url,'上传上机表',uploadComputerWatchConfig);	
	}else if(action =='merge'){
		var url=tourl+"?ids="+id;
		$.showDialog(url,'合并文库列表',mergeLibraryConfig);
	}else if(action =='pooling_export'){
        var url=tourl+"?wkid="+id;
        $.showDialog(url,'pooling导出',exportPoolingConfig);
    }else if(action =='pooling_upload'){
         var url=tourl+"?wkid="+id;
         $.showDialog(url,'pooling回传',uploadPoolingConfig);
     }
}


var Library_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#library_formSearch #btn_view");
		var btn_add = $("#library_formSearch #btn_add");
		var btn_mod = $("#library_formSearch #btn_mod");
		var btn_send = $("#library_formSearch #btn_send");
		var btn_del = $("#library_formSearch #btn_del");
		var btn_query = $("#library_formSearch #btn_query");
		var btn_selectexport = $("#library_formSearch #btn_selectexport");//选中导出
		var btn_selectexportold = $("#library_formSearch #btn_selectexportold");//选中导出
		var btn_addpotency = $("#library_formSearch #btn_addpotency");
		var btn_imports_conc = $("#library_formSearch #btn_importconc");//浓度导入
		var btn_pooling_export = $("#library_formSearch #btn_poolingexport");//
		var btn_pcrdockingfile = $("#library_formSearch #btn_pcrdockingfile");
		var btn_uploadcomputerwatch = $("#library_formSearch #btn_uploadcomputerwatch");//上传上机表
		var btn_updateCxy = $("#library_formSearch #btn_updatecxy");//更新测序仪信息
		var btn_merge = $("#library_formSearch #btn_merge");//合并文库列表
		var btn_cancelmerge = $("#library_formSearch #btn_cancelmerge");//取消合并文库列表
		var btn_export = $("#library_formSearch #btn_export");
		var btn_pooling_upload = $("#library_formSearch #btn_poolingupload");
		var btn_downloadChipQcDcore = $("#library_formSearch #btn_downloadChipQcDcore");
		/*---------------------------------------生成PCR对接文档-----------------------------------*/
		btn_pcrdockingfile.unbind("click").click(function(){
    		var sel_row = $('#library_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			LibraryById(sel_row[0].wkid,"pcrdockingfile",btn_pcrdockingfile.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}  		
    	});
    	/*--------------------------------------显示隐藏-----------------------------------------*/ 
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			LibraryResult(true);
    		});
    	}
	
    	//添加日期控件(创建时间开始时间)
    	laydate.render({
    	   elem: '#library_formSearch #cjsjstart'
		   ,type: 'date'
    	});
    	//添加日期控件(创建时间结束时间)
    	laydate.render({
    	   elem: '#library_formSearch #cjsjend'
		   ,type: 'date'
    	});
		
        /*---------------------------查看文库列表-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#library_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			LibraryById(sel_row[0].wkid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------编辑文库信息-----------------------------------*/
    	btn_send.unbind("click").click(function(){
    		var sel_row = $('#library_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    		$.confirm('您确定要发送所选择的文库记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= btn_send.attr("tourl");
    				jQuery.post(url,{"wkid":sel_row[0].wkid,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								searchHzxxResult();
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
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------新增文库信息-----------------------------------*/
    	btn_add.unbind("click").click(function(){
    		LibraryById(null,"add",btn_add.attr("tourl"));
    	});
    	/*---------------------------编辑文库信息-----------------------------------*/
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#library_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			LibraryById(sel_row[0].wkid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------录入浓度信息-----------------------------------*/
    	btn_addpotency.unbind("click").click(function(){
    		var sel_row = $('#library_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			LibraryById(sel_row[0].wkid,"addpotency",btn_addpotency.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------删除文库信息-----------------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#library_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].wkid;
    		}
    		ids = ids.substr(1);
    		$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= btn_del.attr("tourl");
    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								LibraryResult();
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
    	});
    	/*---------------------------------------选中导出-----------------------------------*/
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#library_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].wkid;
        		}
        		ids = ids.substr(1);
        		$.showDialog(exportPrepareUrl+"?ywid=LIBRARY_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});
    	
    	/*---------------------------------------选中导出（旧）-----------------------------------*/
    	btn_selectexportold.unbind("click").click(function(){
    		var sel_row = $('#library_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].wkid;
        		}
        		ids = ids.substr(1);
        		$.showDialog(exportPrepareUrl+"?ywid=LIBRARY_SELECTOLD&expType=select&ids="+ids
        				,btn_selectexportold.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});
    	/*---------------------------------------导入-----------------------------------*/
    	btn_imports_conc.unbind("click").click(function(){
    		var sel_row = $('#library_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			LibraryById(sel_row[0].wkid,"imports_conc",btn_imports_conc.attr("tourl"));
    		}else{
    			$.error("请选择一条数据");
    		}
    	});
    	/*---------------------------------------pooling导出-----------------------------------*/
    	btn_pooling_export.unbind("click").click(function(){
    		var sel_row = $('#library_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    		    LibraryById(sel_row[0].wkid,"pooling_export",btn_pooling_export.attr("tourl"))
    		}else{
    			$.error("请选择一条数据");
    		}
    	});
    	btn_downloadChipQcDcore.unbind("click").click(function(){
    		var sel_row = $('#library_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].wkid;
				}
				ids = ids.substr(1);
				let ajaxurl = btn_downloadChipQcDcore.attr("tourl");
				window.open(ajaxurl + "?ids=" + ids + "&access_token=" + $("#ac_tk").val());
			}else{
				$.error("请选择数据");
			}
    	});

    	/*---------------------------------------pooling回传-----------------------------------*/
        btn_pooling_upload.unbind("click").click(function(){
            var sel_row = $('#library_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                LibraryById(sel_row[0].wkid,"pooling_upload",btn_pooling_upload.attr("tourl"))
            }else{
                $.error("请选择一条数据");
            }
        });
    	
    	/*---------------------------上传上机表-----------------------------------*/
    	btn_uploadcomputerwatch.unbind("click").click(function(){
    		var sel_row = $('#library_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			LibraryById(sel_row[0].wkid,"uploadcomputerwatch",btn_uploadcomputerwatch.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});

		/*---------------------------更新测序仪信息-----------------------------------*/
		btn_updateCxy.unbind("click").click(function(){
			$.confirm('您确定要更新测序仪信息吗？',function(result){
				if(result){
					jQuery.ajaxSetup({async:false});
					var url= btn_updateCxy.attr("tourl");
					jQuery.post(url,{"access_token":$("#ac_tk").val()},function(responseText){
						setTimeout(function(){
							if(responseText["status"] == 'success'){
								$.success(responseText["message"],function() {
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
		});
    	
    	/*--------------------------------------显示隐藏-----------------------------------------*/     
    	$("#library_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(Library_turnOff){
    			$("#library_formSearch #searchMore").slideDown("low");
    			Library_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#library_formSearch #searchMore").slideUp("low");
    			Library_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});
    	/*------------------------------------合并文库列表------------------------------------------*/
		btn_merge.unbind("click").click(function(){
			var sel_row = $('#library_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length <2){
				$.error("请至少选中两行");
				return;
			}
			var ids="";
			var jcdwmc=sel_row[0].jcdwmc;
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
				ids = ids + ","+ sel_row[i].wkid;
				if (jcdwmc != sel_row[i].jcdwmc){
					$.error("请确认数据检测单位是否一致");
					return;
				}
			}
			ids = ids.substr(1);
			LibraryById(ids,"merge",btn_merge.attr("tourl"));
		});
		/*------------------------------------取消合并文库列表------------------------------------------*/
		btn_cancelmerge.unbind("click").click(function(){
			var sel_row = $('#library_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==1){
				$.confirm('您确定要取消合并所选择的记录吗？',function(result){
					if(result){
						jQuery.ajaxSetup({async:false});
						var url= btn_cancelmerge.attr("tourl");
						jQuery.post(url,{wkid:sel_row[0].wkid,"access_token":$("#ac_tk").val()},function(responseText){
							setTimeout(function(){
								if(responseText["status"] == 'success'){
									$.success(responseText["message"],function() {
										LibraryResult();
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
			}else{
				$.error("请选中一行");
			}
		});
		/* --------------------------- 导出 -----------------------------------*/
		btn_export.unbind("click").click(function(){
			var sel_row = $('#library_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==0){
				$.error("请选中一行");
				return;
			}
			var url=btn_export.attr("tourl")+"?wkid="+sel_row[0].wkid+"&access_token="+$("#ac_tk").val();
			window.open(url);
		});
		/*------------------------------------------------------------------------------------*/
    };
    return oInit;
};

var sfbc=0;//是否继续保存 

var pcrdockingfileLibraryConfig = {
		width		: "500px",
		modalName	: "pcrdockingfileLibraryModal",
		formName	: "dockingPCR_Form",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#dockingPCR_Form").valid()){
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					
					$("#dockingPCR_Form input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"dockingPCR_Form",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							jQuery('<form action="/experiment/file/pagedataPredownPcrFile" method="POST">' +  // action请求路径及推送方法
					                '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
					                '<input type="text" name="wjm" value="'+responseText["wjm"]+'"/>' + 
					                '<input type="text" name="wjlj" value="'+responseText["wjlj"]+'"/>' + 
					                '<input type="text" name="newWjlj" value="'+responseText["newWjlj"]+'"/>' + 
					            '</form>')
					        .appendTo('body').submit().remove();
							
							$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
						});
							
						}else if(responseText["status"] == "fail"){
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

var addPotencyLibraryConfig = {
		width		: "2000px",
		modalName	: "addLibraryModal",
		formName	: "ajaxForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#ajaxForm").valid()){
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					var xhs=[];
					var quantitys=[];
					for(var i=1;i<25;i++){
						for(var j=1;j<9;j++){
							xh=$("#quantity"+i+"-"+j).attr("xh");
							quantity=$("#quantity"+i+"-"+j).val();
							mxxx=$("#lie"+i+"-"+j).text();
							if(mxxx != null && mxxx!=''){
								xhs.push(xh);
								quantitys.push(quantity);
							}
						}
					}
					
					$("#ajaxForm #xhs").val(xhs);
					$("#ajaxForm #quantitys").val(quantitys);
					$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
					
					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							sfbc=0;
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								LibraryResult();
							});
						}else if(responseText["status"] == "caution"){
							$.confirm(responseText["message"],function(result) {
							});	
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
var viewLibraryConfig = {
		width		: "1600px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

var addLibraryConfig = {
	width		: "2000px",
	modalName	: "addLibraryModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success2 : {
			label : "复 制",
			className : "btn-danger",
			callback : function() {
				var str="";
				for(var i=1;i<25;i++){
					for(var j=1;j<9;j++){
						if($("#ajaxForm #nbbh"+i+"-"+j).val()){
							if(j==8){
								str=str+$("#ajaxForm #nbbh"+i+"-"+j).val()+"+"+$("#ajaxForm #tqm"+i+"-"+j).val()+"+"+$("#ajaxForm #id"+i+"-"+j).val();
							}else{
								str=str+$("#ajaxForm #nbbh"+i+"-"+j).val()+"+"+$("#ajaxForm #tqm"+i+"-"+j).val()+"+"+$("#ajaxForm #id"+i+"-"+j).val()+",";
							}
						}else{
							if(j!=8){
								str=str+",";
							}
						}
					}
					if(i!=24){
						str=str+";";
					}
				}

				copyText(str);
				return false;
			}
		},
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					return false;
				}
				$("input[name='nbbh']").attr("style","border-color:#ccc;height:30px;padding:6px 3px;");
				var jtxxs=[];
				var nbbhs=[];
				var tqms=[];
				var xhs=[];
				var syglids=[];
				var tqmxids=[];
				var jtxx;
				var nbbh;
				var tqm;
				var json=[];
				var syglid='';
				var tqmxid='';
				var tip='';
				for(i=1;i<25;i++){
					for(j=1;j<9;j++){
						jtxx=$("#lie"+i+"-"+j).val();
						nbbh=$("#nbbh"+i+"-"+j).val();
						tqm=$("#tqm"+i+"-"+j).val();
						if(!tqm){
							tqm='';
						}
						syglid=$("#id"+i+"-"+j).val();
						if(!syglid){
							syglid='';
						}
						tqmxid=$("#tqmx"+i+"-"+j).val();
						if(!tqmxid){
							tqmxid='';
						}
						if(jtxx!=null && jtxx!='' && nbbh!=null && nbbh!=''){
							if(syglid!=null && syglid!=''){
								var sz={"syglid":'',"jtxx":''};
								sz.syglid=syglid;
								sz.jtxx=jtxx;
								json.push(sz);
							}else{
								tip=tip+','+nbbh;
							}
							jtxxs.push(jtxx);
							nbbhs.push(nbbh);
							tqms.push(tqm);
							syglids.push(syglid);
							tqmxids.push(tqmxid);
							xhs.push($("#nbbh"+i+"-"+j).attr("xh"));
							var jtcxcs=0;//用于判断接头信息是否重复
							var nbbhcxcs=0;//用于判断内部编号是否重复
							for(var l=0;l<jtxxs.length;l++){
								if(jtxxs[l]==jtxx && jtxx!=""){
									jtcxcs++;
									if(jtcxcs>=2){
										$.confirm("接头信息不允许重复!"+"(接头信息:"+jtxx+")");
										return false;
									}
								}
							}
							for(var l=0;l<nbbhs.length;l++){
								if(nbbhs[l]==nbbh && nbbh!=""){
									nbbhcxcs++;
									if(nbbhcxcs>=2){
										$.confirm("内部编号存在重复!"+"(编号信息:"+nbbh+")");
									}
								}
							}
						}else if((jtxx==null || jtxx=='') && (nbbh!=null && nbbh!='')){
							$.confirm("对应接头信息不能为空!");
							return false;
						}else if((jtxx!=null && jtxx!='') && (nbbh==null || nbbh=='')){
							$.confirm("对应内部编码不能为空!");
							return false;
						}
					}
				}
				if(jtxxs == '' && nbbhs==''){
					$.confirm("内容不能为空!");
					return false;
				}
				if (!$("#ajaxForm #yqlx").val()){
					$.confirm("测序仪不能为空!");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#ajaxForm #jtxxs").val(jtxxs);
				$("#ajaxForm #nbbhs").val(nbbhs);
				$("#ajaxForm #xhs").val(xhs);
				$("#ajaxForm #tqms").val(tqms);
				$("#ajaxForm #syglids").val(syglids);
				$("#ajaxForm #tqmxids").val(tqmxids);
				$("#ajaxForm #zt_flag").val("1");
				$("#ajaxForm #sfbc").val(sfbc);
				$("#ajaxForm #sygl_json").val(JSON.stringify(json));
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

				if(tip) {
					tip = tip.substring(1);
					$.error('内部编码' + tip + '未匹配实验管理数据!');
					return false;
				}
				submitForm(opts["formName"] || "ajaxForm", function (responseText, statusText) {
					if (responseText["status"] == 'success') {
						sfbc = 0;
						$.success(responseText["message"], function () {
							if (opts.offAtOnce) {
								$.closeModal(opts.modalName);
								LibraryResult();
							}
						});
					} else if (responseText["status"] == "caution") {
						preventResubmitForm(".modal-footer > button", false);
						sfbc = 0;
						$.confirm(responseText["message"], function (result) {
							if (result) {
								sfbc = 1;
								$("#btn_success").click();
								$("#ajaxForm #quantitys").val(quantitys);
							}
						});
						var notexitnbbh = responseText["notexitnbbhs"];
						var allnbbhlist = $("input[name='nbbh']");
						for (var i = 0; i < notexitnbbh.length; i++) {
							for (var j = 0; j < allnbbhlist.length; j++) {
								if (allnbbhlist[j].value == notexitnbbh[i]) {
									$("#" + allnbbhlist[j].id).attr("style", "border-color: #a94442;height:30px;");
								}
							}
						}

					} else if (responseText["status"] == "fail") {
						preventResubmitForm(".modal-footer > button", false);
						sfbc = 0;
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
		successtwo : {
			label : "保 存",
			className : "btn-success",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					return false;
				}
				$("input[name='nbbh']").attr("style","border-color:#ccc;height:30px;");
				var jtxxs=[];
				var nbbhs=[];
				var xhs=[];
				var syglids=[];
				var tqmxids=[];
				var jtxx;
				var nbbh;
				var tqms=[];
				var tqm;
				var json=[];
				var syglid='';
				var tqmxid='';
				var tip='';
				for(i=1;i<25;i++){
					for(j=1;j<9;j++){
						jtxx=$("#lie"+i+"-"+j).val();
						nbbh=$("#nbbh"+i+"-"+j).val();
						tqm=$("#tqm"+i+"-"+j).val();
						if(!tqm){
							tqm='';
						}
						syglid=$("#id"+i+"-"+j).val();
						if(!syglid){
							syglid='';
						}
						tqmxid=$("#tqmx"+i+"-"+j).val();
						if(!tqmxid){
							tqmxid='';
						}
						if(nbbh!=null && nbbh!=''){
							if(syglid!=null && syglid!=''){
								var sz={"syglid":'',"jtxx":''};
								sz.syglid=syglid;
								sz.jtxx=jtxx;
								json.push(sz);
							}else{
								syglid=$("#id"+i+"-"+j).val();
								if (syglid){
									var sz={"syglid":'',"jtxx":''};
									sz.syglid=syglid;
									sz.jtxx=jtxx;
									json.push(sz);
								}else{
									tip=tip+','+nbbh;
								}
							}
							jtxxs.push(jtxx);
							nbbhs.push(nbbh);
							tqms.push(tqm);
							syglids.push(syglid);
							tqmxids.push(tqmxid);
							xhs.push($("#nbbh"+i+"-"+j).attr("xh"));
							var jtcxcs=0;//用于判断接头信息是否重复
							var nbbhcxcs=0;//用于判断内部编号是否重复
							for(var l=0;l<jtxxs.length;l++){
								if(jtxxs[l]==jtxx && jtxx!=""){
									jtcxcs++;
									if(jtcxcs>=2){
										$.confirm("接头信息不允许重复!"+"(接头信息:"+jtxx+")");
										return false;
									}
								}
							}
							for(var l=0;l<nbbhs.length;l++){
								if(nbbhs[l]==nbbh && nbbh!=""){
									nbbhcxcs++;
									if(nbbhcxcs>=2){
										$.confirm("内部编号存在重复!"+"(编号信息:"+nbbh+")");
									}
								}
							}
						}else if(nbbh!=null && nbbh!=''){
							jtxxs.push(jtxx);
							nbbhs.push(nbbh);
							tqms.push(tqm);
							syglids.push(syglid);
							if(!syglid){
								tip=tip+','+nbbh;
							}
							xhs.push($("#nbbh"+i+"-"+j).attr("xh"));
						}else if((jtxx!=null && jtxx!='') && (nbbh==null || nbbh=='')){
							$.confirm("对应内部编码不能为空!");
							return false;
						}
					}
				}
				if(jtxxs == '' && nbbhs==''){
					$.confirm("内容不能为空!");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#ajaxForm #jtxxs").val(jtxxs);
				$("#ajaxForm #nbbhs").val(nbbhs);
				$("#ajaxForm #xhs").val(xhs);
				$("#ajaxForm #tqms").val(tqms);
				$("#ajaxForm #syglids").val(syglids);
				$("#ajaxForm #tqmxids").val(tqmxids);
				$("#ajaxForm #zt_flag").val("0");
				$("#ajaxForm #sfbc").val(sfbc);
				$("#ajaxForm #sygl_json").val(JSON.stringify(json));
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"] || "ajaxForm", function (responseText, statusText) {
					if (responseText["status"] == 'success') {
						sfbc = 0;
						$.success(responseText["message"], function () {
							if (opts.offAtOnce) {
								$.closeModal(opts.modalName);
								LibraryResult();
							}
						});
					} else if (responseText["status"] == "caution") {
						preventResubmitForm(".modal-footer > button", false);
						sfbc = 0;
						$.confirm(responseText["message"], function (result) {
							if (result) {
								sfbc = 1;
								$("#btn_success").click();
								$("#ajaxForm #quantitys").val(quantitys);
							}
						});
						var notexitnbbh = responseText["notexitnbbhs"];
						var allnbbhlist = $("input[name='nbbh']");
						for (var i = 0; i < notexitnbbh.length; i++) {
							for (var j = 0; j < allnbbhlist.length; j++) {
								if (allnbbhlist[j].value == notexitnbbh[i]) {
									$("#" + allnbbhlist[j].id).attr("style", "border-color: #a94442;height:30px;");
								}
							}
						}

					} else if (responseText["status"] == "fail") {
						preventResubmitForm(".modal-footer > button", false);
						sfbc = 0;
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

copyContentH5: function copyText(content) {
	var copyDom = document.createElement('div');
	copyDom.innerText=content;
	copyDom.style.position='absolute';
	copyDom.style.top='0px';
	copyDom.style.right='-9999px';
	document.body.appendChild(copyDom);
	//创建选中范围
	var range = document.createRange();
	range.selectNode(copyDom);
	//移除剪切板中内容
	window.getSelection().removeAllRanges();
	//添加新的内容到剪切板
	window.getSelection().addRange(range);
	//复制
	var successful = document.execCommand('copy');
	copyDom.parentNode.removeChild(copyDom);
	try{
		var msg = successful ? "successful" : "failed";
		console.log('Copy command was : ' + msg);
	} catch(err){
		console.log('Oops , unable to copy!');
	}
}

var modLibraryConfig = {
	width		: "2000px",
	modalName	: "modLibraryModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success2 : {
			label : "复 制",
			className : "btn-danger",
			callback : function() {
				var json = [];
				for(var i=1;i<25;i++){
					for(var j=1;j<9;j++){
						if($("#ajaxForm #nbbh"+i+"-"+j).val()){
							var sz = {"nbbh":'',"tqm":'',"id":'',"tqmxid":'',"html":'',"lie":'',"num":''};
							sz.nbbh = $("#ajaxForm #nbbh"+i+"-"+j).val();
							sz.tqm = $("#ajaxForm #tqm"+i+"-"+j).val();
							sz.id = $("#ajaxForm #id"+i+"-"+j).val();
							sz.tqmxid = $("#ajaxForm #tqmx"+i+"-"+j).val();
							sz.html=$("#ajaxForm #tqmx"+i+"-"+j).html();
							sz.lie=i;
							sz.num=j;
							json.push(sz)
						}
					}
				}

				copyText(JSON.stringify(json));
				return false;
			}
		},
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					return false;
				}
				var jtxxs=[];
				var nbbhs=[];
				var xhs=[];
				var quantitys=[];
				var tqms=[];
				var jtxx;
				var nbbh;
				var syglids=[];
				var tqmxids=[];
				var tqm;
				var quantity;
				var syglid='';
				var tqmxid='';
				var json=[];
				var tip='';
				for(var i=1;i<25;i++){
					for(var j=1;j<9;j++){
						jtxx=$("#lie"+i+"-"+j).text();
						nbbh=$("#nbbh"+i+"-"+j).val();
						quantity=$("#quantity"+i+"-"+j).val();
						tqm=$("#tqm"+i+"-"+j).val();
						if(!tqm){
							tqm='';
						}
						syglid=$("#id"+i+"-"+j).val();
						if(!syglid){
							syglid='';
						}
						tqmxid=$("#tqmx"+i+"-"+j).val();
						if(!tqmxid){
							tqmxid='';
						}
						if(jtxx!=null && jtxx!='' && nbbh!=null && nbbh!=''){
							if(syglid!=null && syglid!=''){
								var sz={"syglid":'',"jtxx":''};
								sz.syglid=syglid;
								sz.jtxx=jtxx;
								json.push(sz);
							}else{
								tip=tip+','+nbbh;
							}
							jtxxs.push(jtxx);
							nbbhs.push(nbbh);
							quantitys.push(quantity);
							tqms.push(tqm);
							syglids.push(syglid);
							tqmxids.push(tqmxid);
							xhs.push($("#nbbh"+i+"-"+j).attr("xh"));
							var jtcxcs=0;//用于判断接头信息是否重复
							var nbbhcxcs=0;//用于判断内部编号是否重复
							for(var l=0;l<jtxxs.length;l++){
								if(jtxxs[l]==jtxx && jtxx!=""){
									jtcxcs++;
									if(jtcxcs>=2){
										$.confirm("接头信息不允许重复!"+"(接头信息:"+jtxx+")");
										return false;
									}
								}
							}
							for(var l=0;l<nbbhs.length;l++){
								if(nbbhs[l]==nbbh && nbbh!=""){
									nbbhcxcs++;
									if(nbbhcxcs>=2){
										$.confirm("内部编号存在重复!"+"(编号信息:"+nbbh+")");
									}
								}
							}
						}else if((jtxx==null || jtxx=='') && (nbbh!=null && nbbh!='')){
							$.confirm("对应接头信息不能为空!");
							return false;
						}else if((jtxx!=null && jtxx!='') && (nbbh==null || nbbh=='')){
							$.confirm("对应内部编码不能为空!");
							return false;
						}
					}
				}
				if(jtxxs == '' && nbbhs==''){
					$.confirm("内容不能为空!");
					return false;
				}
				if (!$("#ajaxForm #yqlx").val()){
					$.confirm("测序仪不能为空!");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#ajaxForm #jtxxs").val(jtxxs);
				$("#ajaxForm #nbbhs").val(nbbhs);
				$("#ajaxForm #xhs").val(xhs);
				$("#ajaxForm #quantitys").val(quantitys);
				$("#ajaxForm #tqms").val(tqms);
				$("#ajaxForm #syglids").val(syglids);
				$("#ajaxForm #tqmxids").val(tqmxids);
				$("#ajaxForm #sygl_json").val(JSON.stringify(json));
				$("#ajaxForm #zt_flag").val("1");
				$("#ajaxForm #sfbc").val(sfbc);
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				if(tip) {
					tip = tip.substring(1);
					$.error('内部编码' + tip + '未匹配实验管理数据!');
					return false;
				}
				submitForm(opts["formName"] || "ajaxForm", function (responseText, statusText) {
					if (responseText["status"] == 'success') {
						sfbc = 0;
						$.success(responseText["message"], function () {
							if (opts.offAtOnce) {
								$.closeModal(opts.modalName);
								LibraryResult();
							}
						});
					} else if (responseText["status"] == "caution") {
						preventResubmitForm(".modal-footer > button", false);
						sfbc = 0;
						$.confirm(responseText["message"], function (result) {
							if (result) {
								sfbc = 1;
								$("#btn_success").click();
								$("#ajaxForm #quantitys").val(quantitys);
							}
						});
						var notexitnbbh = responseText["notexitnbbhs"];
						var allnbbhlist = $("input[name='nbbh']");
						for (var i = 0; i < notexitnbbh.length; i++) {
							for (var j = 0; j < allnbbhlist.length; j++) {
								if (allnbbhlist[j].value == notexitnbbh[i]) {
									$("#" + allnbbhlist[j].id).attr("style", "border-color: #a94442;height:30px;");
								}
							}
						}

					} else if (responseText["status"] == "fail") {
						preventResubmitForm(".modal-footer > button", false);
						sfbc = 0;
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
		successtwo : {
			label : "保 存",
			className : "btn-success",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					return false;
				}
				$("input[name='nbbh']").attr("style","border-color:#ccc;height:30px;");
				var jtxxs=[];
				var nbbhs=[];
				var xhs=[];
				var quantitys=[];
				var tqms=[];
				var jtxx;
				var nbbh;
				var syglids=[];
				var tqmxids=[];
				var tqm;
				var quantity;
				var syglid='';
				var tqmxid='';
				var json=[];
				var tip='';
				for(var i=1;i<25;i++){
					for(var j=1;j<9;j++){
						jtxx=$("#lie"+i+"-"+j).text();
						nbbh=$("#nbbh"+i+"-"+j).val();
						quantity=$("#quantity"+i+"-"+j).val();
						tqm=$("#tqm"+i+"-"+j).val();
						if(!tqm){
							tqm='';
						}
						syglid=$("#id"+i+"-"+j).val();
						if(!syglid){
							syglid='';
						}
						tqmxid=$("#tqmx"+i+"-"+j).val();
						if(!tqmxid){
							tqmxid='';
						}
						if(nbbh!=null && nbbh!=''){
							if(syglid!=null && syglid!=''){
								var sz={"syglid":'',"jtxx":''};
								sz.syglid=syglid;
								sz.jtxx=jtxx;
								json.push(sz);
							}
							jtxxs.push(jtxx);
							nbbhs.push(nbbh);
							quantitys.push(quantity);
							tqms.push(tqm);
							syglids.push(syglid);
							tqmxids.push(tqmxid);
							xhs.push($("#nbbh"+i+"-"+j).attr("xh"));
							var jtcxcs=0;//用于判断接头信息是否重复
							var nbbhcxcs=0;//用于判断内部编号是否重复
							for(var l=0;l<jtxxs.length;l++){
								if(jtxxs[l]==jtxx && jtxx!=""){
									jtcxcs++;
									if(jtcxcs>=2){
										$.confirm("接头信息不允许重复!"+"(接头信息:"+jtxx+")");
										return false;
									}
								}
							}
							for(var l=0;l<nbbhs.length;l++){
								if(nbbhs[l]==nbbh && nbbh!=""){
									nbbhcxcs++;
									if(nbbhcxcs>=2){
										$.confirm("内部编号存在重复!"+"(编号信息:"+nbbh+")");
									}
								}
							}
						}else if((jtxx!=null && jtxx!='') && (nbbh==null || nbbh=='')){
							$.confirm("对应内部编码不能为空!");
							return false;
						}
					}
				}
				if(jtxxs == '' && nbbhs==''){
					$.confirm("内容不能为空!");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				//修改保存时不更新状态
				$("#ajaxForm #jtxxs").val(jtxxs);
				$("#ajaxForm #nbbhs").val(nbbhs);
				$("#ajaxForm #xhs").val(xhs);
				$("#ajaxForm #quantitys").val(quantitys);
				$("#ajaxForm #tqms").val(tqms);
				$("#ajaxForm #syglids").val(syglids);
				$("#ajaxForm #tqmxids").val(tqmxids);
				$("#ajaxForm #sygl_json").val(JSON.stringify(json));
				$("#ajaxForm #sfbc").val(sfbc);
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						sfbc=0;
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							LibraryResult();
						});
					}else if(responseText["status"] == "caution"){
						preventResubmitForm(".modal-footer > button", false);
						sfbc=0;
						$.confirm(responseText["message"],function(result) {
							if(result){
								sfbc=1;
								$("#btn_successtwo").click();
								$("#ajaxForm #quantitys").val(quantitys);
							}
						});
						var notexitnbbh=responseText["notexitnbbhs"];
						var allnbbhlist=$("input[name='nbbh']");
						for(var i=0;i<notexitnbbh.length;i++){
							for(var j=0;j<allnbbhlist.length;j++){
								if(allnbbhlist[j].value==notexitnbbh[i]){
									$("#"+allnbbhlist[j].id).attr("style","border-color: #a94442;height:30px;");
								}
							}
						}
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						sfbc=0;
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
						sfbc=0;
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

var mergeLibraryConfig = {
	width		: "2000px",
	modalName	: "mergeLibraryModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success2 : {
			label : "复 制",
			className : "btn-danger",
			callback : function() {
				var str="";
				for(var i=1;i<25;i++){
					for(var j=1;j<9;j++){
						if($("#ajaxForm #nbbh"+i+"-"+j).val()){
							if(j==8){
								str=str+$("#ajaxForm #nbbh"+i+"-"+j).val();
							}else{
								str=str+$("#ajaxForm #nbbh"+i+"-"+j).val()+",";
							}
						}else{
							if(j!=8){
								str=str+",";
							}
						}
					}
					if(i!=24){
						str=str+";";
					}
				}

				copyText(str);
			}
		},
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if ( $("#ajaxForm #isOverFlow").val() ){
					$.error( "合并信息内容过多，不可继续！！",function() { });
				}
				if(!$("#ajaxForm").valid()){
					return false;
				}
				$("input[name='nbbh']").attr("style","border-color:#ccc;height:30px;padding:6px 3px;");
				var jtxxs=[];
				var nbbhs=[];
				var tqms=[];
				var xhs=[];
				var syglids=[];
				var quantitys=[];
				var jtxx;
				var nbbh;
				var tqm;
				var json=[];
				var syglid='';
				for(i=1;i<25;i++){
					for(j=1;j<9;j++){
						jtxx=$("#lie"+i+"-"+j).text();
						nbbh=$("#nbbh"+i+"-"+j).val();
						tqm=$("#tqm"+i+"-"+j).val();
						quantity=$("#quantity"+i+"-"+j).val();
						if(!tqm){
							tqm='';
						}
						syglid=$("#id"+i+"-"+j).val();
						if(!syglid){
							syglid='';
						}
						if(jtxx!=null && jtxx!='' && nbbh!=null && nbbh!=''){
							if(syglid!=null && syglid!=''){
								var sz={"syglid":'',"jtxx":''};
								sz.syglid=syglid;
								sz.jtxx=jtxx;
								json.push(sz);
							}
							jtxxs.push(jtxx);
							nbbhs.push(nbbh);
							tqms.push(tqm);
							syglids.push(syglid);
							quantitys.push(quantity);
							xhs.push($("#nbbh"+i+"-"+j).attr("xh"));
							var jtcxcs=0;//用于判断接头信息是否重复
							var nbbhcxcs=0;//用于判断内部编号是否重复
							for(var l=0;l<jtxxs.length;l++){
								if(jtxxs[l]==jtxx && jtxx!=""){
									jtcxcs++;
									if(jtcxcs>=2){
										$.confirm("接头信息不允许重复!"+"(接头信息:"+jtxx+")");
										return false;
									}
								}
							}
							for(var l=0;l<nbbhs.length;l++){
								if(nbbhs[l]==nbbh && nbbh!=""){
									nbbhcxcs++;
									if(nbbhcxcs>=2){
										$.confirm("内部编号存在重复!"+"(编号信息:"+nbbh+")");
									}
								}
							}
						}else if((jtxx==null || jtxx=='') && (nbbh!=null && nbbh!='')){
							$.confirm("对应接头信息不能为空!");
							return false;
						}else if((jtxx!=null && jtxx!='') && (nbbh==null || nbbh=='')){
							$.confirm("对应内部编码不能为空!");
							return false;
						}
					}
				}
				if(jtxxs == '' && nbbhs==''){
					$.confirm("内容不能为空!");
					return false;
				}

				var $this = this;
				var opts = $this["options"]||{};
				$("#ajaxForm #jtxxs").val(jtxxs);
				$("#ajaxForm #nbbhs").val(nbbhs);
				$("#ajaxForm #xhs").val(xhs);
				$("#ajaxForm #tqms").val(tqms);
				$("#ajaxForm #quantitys").val(quantitys);
				$("#ajaxForm #syglids").val(syglids);
				$("#ajaxForm #zt_flag").val("1");
				$("#ajaxForm #sfbc").val(sfbc);
				$("#ajaxForm #sygl_json").val(JSON.stringify(json));
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						sfbc=0;
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							LibraryResult();
						});
					}else if(responseText["status"] == "caution"){
						preventResubmitForm(".modal-footer > button", false);
						sfbc=0;
						$.confirm(responseText["message"],function(result) {
							if(result){
								sfbc=1;
								$("#btn_success").click();
								$("#ajaxForm #quantitys").val(quantitys);
							}
						});
						var notexitnbbh=responseText["notexitnbbhs"];
						var allnbbhlist=$("input[name='nbbh']");
						for(var i=0;i<notexitnbbh.length;i++){
							for(var j=0;j<allnbbhlist.length;j++){
								if(allnbbhlist[j].value==notexitnbbh[i]){
									$("#"+allnbbhlist[j].id).attr("style","border-color: #a94442;height:30px;");
								}
							}
						}

					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						sfbc=0;
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
		successtwo : {
			label : "保 存",
			className : "btn-success",
			callback : function() {
				if ( $("#ajaxForm #isOverFlow").val() ){
					$.error( "合并信息内容过多，不可继续！！",function() { });
				}
				if(!$("#ajaxForm").valid()){
					return false;
				}
				$("input[name='nbbh']").attr("style","border-color:#ccc;height:30px;");
				var jtxxs=[];
				var nbbhs=[];
				var xhs=[];
				var quantitys=[];
				var syglids=[];
				var jtxx;
				var nbbh;
				var tqms=[];
				var tqm;
				var json=[];
				var syglid='';
				for(i=1;i<25;i++){
					for(j=1;j<9;j++){
						jtxx=$("#lie"+i+"-"+j).text();
						nbbh=$("#nbbh"+i+"-"+j).val();
						quantity=$("#quantity"+i+"-"+j).val();
						tqm=$("#tqm"+i+"-"+j).val();
						if(!tqm){
							tqm='';
						}
						syglid=$("#id"+i+"-"+j).val();
						if(!syglid){
							syglid='';
						}
						if(jtxx!=null && jtxx!='' && nbbh!=null && nbbh!=''){
							if(syglid!=null && syglid!=''){
								var sz={"syglid":'',"jtxx":''};
								sz.syglid=syglid;
								sz.jtxx=jtxx;
								json.push(sz);
							}
							jtxxs.push(jtxx);
							nbbhs.push(nbbh);
							tqms.push(tqm);
							syglids.push(syglid);
							quantitys.push(quantity);
							xhs.push($("#nbbh"+i+"-"+j).attr("xh"));
							var jtcxcs=0;//用于判断接头信息是否重复
							var nbbhcxcs=0;//用于判断内部编号是否重复
							for(var l=0;l<jtxxs.length;l++){
								if(jtxxs[l]==jtxx && jtxx!=""){
									jtcxcs++;
									if(jtcxcs>=2){
										$.confirm("接头信息不允许重复!"+"(接头信息:"+jtxx+")");
										return false;
									}
								}
							}
							for(var l=0;l<nbbhs.length;l++){
								if(nbbhs[l]==nbbh && nbbh!=""){
									nbbhcxcs++;
									if(nbbhcxcs>=2){
										$.confirm("内部编号存在重复!"+"(编号信息:"+nbbh+")");
									}
								}
							}
						}else if(nbbh!=null && nbbh!=''){
							if(syglid!=null && syglid!=''){
								var sz={"syglid":'',"jtxx":''};
								sz.syglid=syglid;
								sz.jtxx=jtxx;
								json.push(sz);
							}
							tqms.push(tqm);
							syglids.push(syglid);
							jtxxs.push(jtxx);
							nbbhs.push(nbbh);
							quantitys.push(quantity);
							xhs.push($("#nbbh"+i+"-"+j).attr("xh"));
						}else if((jtxx!=null && jtxx!='') && (nbbh==null || nbbh=='')){
							$.confirm("对应内部编码不能为空!");
							return false;
						}
					}
				}
				if(jtxxs == '' && nbbhs==''){
					$.confirm("内容不能为空!");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				//修改保存时不更新状态
				$("#ajaxForm #jtxxs").val(jtxxs);
				$("#ajaxForm #nbbhs").val(nbbhs);
				$("#ajaxForm #xhs").val(xhs);
				$("#ajaxForm #quantitys").val(quantitys);
				$("#ajaxForm #tqms").val(tqms);
				$("#ajaxForm #syglids").val(syglids);
				$("#ajaxForm #zt_flag").val("0");
				$("#ajaxForm #sfbc").val(sfbc);
				$("#ajaxForm #sygl_json").val(JSON.stringify(json));
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						sfbc=0;
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							LibraryResult();
						});
					}else if(responseText["status"] == "caution"){
						preventResubmitForm(".modal-footer > button", false);
						sfbc=0;
						$.confirm(responseText["message"],function(result) {
							if(result){
								sfbc=1;
								$("#btn_successtwo").click();
								$("#ajaxForm #quantitys").val(quantitys);
							}
						});
						var notexitnbbh=responseText["notexitnbbhs"];
						var allnbbhlist=$("input[name='nbbh']");
						for(var i=0;i<notexitnbbh.length;i++){
							for(var j=0;j<allnbbhlist.length;j++){
								if(allnbbhlist[j].value==notexitnbbh[i]){
									$("#"+allnbbhlist[j].id).attr("style","border-color: #a94442;height:30px;");
								}
							}
						}
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						sfbc=0;
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
						sfbc=0;
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

var importPotencyLibraryConfig={
		width		: "1000px",
		modalName	: "exportLibraryModal",
		formName	: "pooling_formSearch",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};



var uploadComputerWatchConfig = {
		width		: "2000px",
		modalName	: "addLibraryModal",
		formName	: "ajaxForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "上 传",
				className : "btn-primary",
				callback : function() {
					if(!$("#ajaxForm").valid()){
						return false;
					}
					var cxyid=$("#ajaxForm #cxyid").val()
					var xpxx=$("#ajaxForm #xpxx").val();
					if(xpxx==null || xpxx==''){
						$.confirm("请选择芯片信息！");
						return false;
					}
					if(cxyid==null || cxyid==''){
						$.confirm("请选择测序仪信息！");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					var wksj_json={'seq':'','data':''};
					var data=[];
					var xpmc=$("#ajaxForm #xpmc").text();
					wksj_json.seq=$("#ajaxForm #xpxx").val();
					var mxlength=$("#ajaxForm .wksjmc").length;
					if(mxlength<=0){
						$.confirm("文库上机明细不能为空！");
						return false;
					}else{
						for(var i=0;i<mxlength;i++){
							var mxdata={"uid":'',"index":'',"index2":'',"project_type":'',"sample":'',"comment":'',"con.lib":'',"con.dna":'',"dnand":'',"dlnd":'',"volume":'',"con.sum":'',"original_con_unit":'',"goal_reads":'',"dilution_factor":'',"xmdm":'',"syglid":'',"spike":''}
							mxdata.uid=$("#ajaxForm #wkbm-"+(i+1)).val();//文库编号
							mxdata.index=$("#ajaxForm #jt1-"+(i+1)).val();//接头1
							mxdata.index2=$("#ajaxForm #jt2-"+(i+1)).val();//接头2
							mxdata.sample=$("#ajaxForm #ybbh-"+(i+1)).val();//标本编号
							mxdata.project_type=$("#ajaxForm #project_type-"+(i+1)).val();//项目类型
							mxdata.comment=$("#ajaxForm #yy-"+(i+1)).val();//原因
							mxdata.dnand=$("#ajaxForm #dnand-"+(i+1)).val();//cdna浓度
							mxdata.dlnd=$("#ajaxForm #dlnd-"+(i+1)).val();//定量浓度
							mxdata["con.lib"]=$("#ajaxForm #wknd-"+(i+1)).val();//文库浓度
							mxdata["con.dna"]=$("#ajaxForm #hsnd-"+(i+1)).val();//con.dna其实就是用的提取的hsnd
							mxdata["volume"]=$("#ajaxForm #tj-"+(i+1)).val();//体积
                            mxdata["con.sum"]=$("#ajaxForm #hbnd-"+(i+1)).val();//con.sum其实就是用的提取的合并浓度
                            mxdata["original_con_unit"]=$("#ajaxForm #nddw-"+(i+1)).val();//original_con_unit合并浓度单位
							mxdata.goal_reads=$("#ajaxForm #yjxjsjl-"+(i+1)).val();//预计下机数据量
							mxdata.dilution_factor=$("#ajaxForm #xsbs-"+(i+1)).val();//稀释倍数
							mxdata.xmdm=$("#ajaxForm #xmdm-"+(i+1)).val();;//项目代码，暂定为空
							mxdata.syglid=$("#ajaxForm #syglid-"+(i+1)).val();
							mxdata.spike=$("#ajaxForm #spike-"+(i+1)).val();
							var nbbm=$("#ajaxForm #nbbm-"+(i+1)).val();
							var xmdm=$("#ajaxForm #xmdm-"+(i+1)).val();
							if(nbbm&&xmdm){
								xmdm=xmdm.substring(0,xmdm.length-1);
								nbbm=nbbm.substring(nbbm.length-1);
								mxdata.xmdm=xmdm+nbbm;
							}else{
								mxdata.xmdm=$("#ajaxForm #xmdm-"+(i+1)).val();
							}
							if(mxdata.uid!=null && mxdata.uid!=''){
								var reg=/((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/;
								if(!reg.test($("#ajaxForm #xsbs-"+(i+1)).val())){
									$.confirm("第"+(i+1)+"行稀释浓度只能输入正数和小数点后两位！");
									return false;
								}
								if(mxdata.sample==null || mxdata.sample==''){
									$.confirm("文库上机明细第"+(i+1)+"行标本信息关联失败，请确认原因！");
									return false;
								}
								if(($("#ajaxForm #wknd-"+(i+1)).val()==null || $("#ajaxForm #wknd-"+(i+1)).val()=='')&&($("#ajaxForm #dlnd-"+(i+1)).val()==null || $("#ajaxForm #dlnd-"+(i+1)).val()=='')){
                                    $.confirm("文库上机明细第"+(i+1)+"行文库浓度,定量浓度不允许为空！");
                                    return false;
                                }
								if($("#ajaxForm #dlnd-"+(i+1)).val()==null || $("#ajaxForm #dlnd-"+(i+1)).val()==''){
								    if($("#ajaxForm #wknd-"+(i+1)).val()==null || $("#ajaxForm #wknd-"+(i+1)).val()==''){
								        $.confirm("文库上机明细第"+(i+1)+"行定量浓度不允许为空！");
									    return false;
								    }else{
                                        if($("#ajaxForm #wknd-"+(i+1)).val()!="-1"){
                                            mxdata.dlnd = (parseFloat($("#ajaxForm #wknd-"+(i+1)).val()) /20).toExponential(3)
                                        }
								    }

								}
								if($("#ajaxForm #wknd-"+(i+1)).val()==null || $("#ajaxForm #wknd-"+(i+1)).val()==''){
                                    if($("#ajaxForm #dlnd-"+(i+1)).val()==null || $("#ajaxForm #dlnd-"+(i+1)).val()==''){
                                        $.confirm("文库上机明细第"+(i+1)+"行文库浓度不允许为空！");
                                        return false;
                                    }else{
                                        if($("#ajaxForm #wknd-"+(i+1)).val()!="-1"){
                                            mxdata["con.lib"] = (parseFloat($("#ajaxForm #dlnd-"+(i+1)).val()) *20).toExponential(3)
                                        }
                                    }

                                }
//								if($("#ajaxForm #wknd-"+(i+1)).val()==null || $("#ajaxForm #wknd-"+(i+1)).val()==''){
//									$.confirm("文库上机明细第"+(i+1)+"行文库浓度不允许为空！");
//									return false;
//								}
								if($("#ajaxForm #wknd-"+(i+1)).val()=="-1" && $("#ajaxForm #dlnd-"+(i+1)).val()=="-1"){
                                    if($("#ajaxForm #tj-"+(i+1)).val()==null || $("#ajaxForm #tj-"+(i+1)).val()==''){
                                        $.confirm("文库上机明细第"+(i+1)+"行体积不允许为空！");
                                        return false;
                                    }
                                    if($("#ajaxForm #hbnd-"+(i+1)).val()==null || $("#ajaxForm #hbnd-"+(i+1)).val()==''){
                                        $.confirm("文库上机明细第"+(i+1)+"行合并浓度不允许为空！");
                                        return false;
                                    }
                                }
								data.push(mxdata);
							}
						}
						wksj_json.data=data;
						$("#ajaxForm #wksj_Json").val(JSON.stringify(wksj_json));
					}
					
					$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
					
					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								LibraryResult();
							});
						}else if(responseText["status"] == "caution"){
							preventResubmitForm(".modal-footer > button", false);
							$.confirm(responseText["message"],function(result) {
								
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
			successtwo : {
				label : "保 存",
				className : "btn-success",
				callback : function() {
					if(!$("#ajaxForm").valid()){
						return false;
					}
					console.log(3321)
					var $this = this;
					var opts = $this["options"]||{};
					var cxyid=$("#ajaxForm #cxyid").val()
					if(cxyid==null || cxyid==''){
						$.confirm("请选择测序仪信息！");
						return false;
					}
					var wksj_json={'seq':'','data':''};
					var data=[];
					var xpmc=$("#ajaxForm #xpmc").text();
					wksj_json.seq=$("#ajaxForm #xpxx").val();
					var mxlength=$("#ajaxForm .wksjmc").length;
					if(mxlength<=0){
						$.confirm("文库上机明细不能为空！");
						return false;
					}else{
						for(var i=0;i<mxlength;i++){
							if($("#ajaxForm #wkbm-"+(i+1)).val()&&!$("#ajaxForm #syglid-"+(i+1)).val()){
								// $.confirm("第"+(i+1)+"行文库编号异常，请重新确认！");
								// return false;
							}
							for(var j=0;j<mxlength;j++){
								if($("#ajaxForm #wkbm-"+(i+1)).val()&&$("#ajaxForm #syglid-"+(i+1)).val()==$("#ajaxForm #syglid-"+(j+1)).val()&&i!=j) {
									// $.confirm("第" + (i + 1) + "行与第" + (j + 1) + "行文库编号相同，请重新确认！");
									// return false;
								}
							}
							var mxdata={"uid":'',"index":'',"index2":'',"project_type":'',"sample":'',"comment":'',"con.lib":'',"con.dna":'',"dnand":'',"dlnd":'',"volume":'',"con.sum":'',"original_con_unit":'',"goal_reads":'',"dilution_factor":'',"xmdm":'',"syglid":'',"spike":''}
							mxdata.uid=$("#ajaxForm #wkbm-"+(i+1)).val();//文库编号
							mxdata.index=$("#ajaxForm #jt1-"+(i+1)).val();//接头1
							mxdata.index2=$("#ajaxForm #jt2-"+(i+1)).val();//接头2
							mxdata.sample=$("#ajaxForm #ybbh-"+(i+1)).val();//标本编号
							mxdata.project_type=$("#ajaxForm #project_type-"+(i+1)).val();//项目类型
							mxdata.comment=$("#ajaxForm #yy-"+(i+1)).val();//原因
							mxdata.dnand=$("#ajaxForm #dnand-"+(i+1)).val();//cdna浓度
							mxdata.dlnd=$("#ajaxForm #dlnd-"+(i+1)).val();//定量浓度
							mxdata["con.lib"]=$("#ajaxForm #wknd-"+(i+1)).val();//文库浓度
							mxdata["con.dna"]=$("#ajaxForm #hsnd-"+(i+1)).val();//con.dna其实就是用的提取的hsnd
							mxdata["volume"]=$("#ajaxForm #tj-"+(i+1)).val();//体积
                            mxdata["con.sum"]=$("#ajaxForm #hbnd-"+(i+1)).val();//con.sum其实就是用的提取的合并浓度
                            mxdata["original_con_unit"]=$("#ajaxForm #nddw-"+(i+1)).val();//original_con_unit合并浓度单位
							mxdata.goal_reads=$("#ajaxForm #yjxjsjl-"+(i+1)).val();//预计下机数据量
							mxdata.dilution_factor=$("#ajaxForm #xsbs-"+(i+1)).val();//稀释倍数
							mxdata.xmdm=$("#ajaxForm #xmdm-"+(i+1)).val();;//项目代码，暂定为空
							mxdata.syglid=$("#ajaxForm #syglid-"+(i+1)).val();
							mxdata.spike=$("#ajaxForm #spike-"+(i+1)).val();
							var nbbm=$("#ajaxForm #nbbm-"+(i+1)).val();
							var xmdm=$("#ajaxForm #xmdm-"+(i+1)).val();
							if(nbbm&&xmdm){
								xmdm=xmdm.substring(0,xmdm.length-1);
								nbbm=nbbm.substring(nbbm.length-1);
								mxdata.xmdm=xmdm+nbbm;
							}else{
								mxdata.xmdm=$("#ajaxForm #xmdm-"+(i+1)).val();
							}
							if(mxdata.uid!=null && mxdata.uid!=''){
								var reg=/((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/;
								if(!reg.test($("#ajaxForm #xsbs-"+(i+1)).val())){
									$.confirm("第"+(i+1)+"行稀释浓度只能输入正数和小数点后两位！");
									return false;
								}
								if(mxdata.sample==null || mxdata.sample==''){
									$.confirm("文库上机明细第"+(i+1)+"行标本信息关联失败，请确认原因！");
									return false;
								}
								data.push(mxdata);
							}
						}
						wksj_json.data=data;
						$("#ajaxForm #wksj_Json").val(JSON.stringify(wksj_json));
					}
					$("#ajaxForm #bcbj").val("1");
					$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								LibraryResult();
							});
						}else if(responseText["status"] == "caution"){
							preventResubmitForm(".modal-footer > button", false);
							$.confirm(responseText["message"],function(result) {

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
			successthree : {
				label : "pooling导出",
				className : "btn-info",
				callback : function() {
					if(!$("#ajaxForm").valid()){
                        return false;
                    }
                    var $this = this;
                    var opts = $this["options"]||{};
                    var cxyid=$("#ajaxForm #cxyid").val()
                    if(cxyid==null || cxyid==''){
                        $.confirm("请选择测序仪信息！");
                        return false;
                    }
                    var wksj_json={'seq':'','data':''};
                    var data=[];
                    var xpmc=$("#ajaxForm #xpmc").text();
                    wksj_json.seq=$("#ajaxForm #xpxx").val();
                    var mxlength=$("#ajaxForm .wksjmc").length;
                    if(mxlength<=0){
                        $.confirm("文库上机明细不能为空！");
                        return false;
                    }else{
                        for(var i=0;i<mxlength;i++){
                            if($("#ajaxForm #wkbm-"+(i+1)).val()&&!$("#ajaxForm #syglid-"+(i+1)).val()){
                                // $.confirm("第"+(i+1)+"行文库编号异常，请重新确认！");
                                // return false;
                            }
                            for(var j=0;j<mxlength;j++){
                                if($("#ajaxForm #wkbm-"+(i+1)).val()&&$("#ajaxForm #syglid-"+(i+1)).val()==$("#ajaxForm #syglid-"+(j+1)).val()&&i!=j) {
                                    // $.confirm("第" + (i + 1) + "行与第" + (j + 1) + "行文库编号相同，请重新确认！");
                                    // return false;
                                }
                            }
                            var mxdata={"uid":'',"index":'',"index2":'',"project_type":'',"sample":'',"comment":'',"con.lib":'',"con.dna":'',"dnand":'',"dlnd":'',"volume":'',"con.sum":'',"original_con_unit":'',"goal_reads":'',"dilution_factor":'',"xmdm":'',"syglid":'',"spike":''}
                            mxdata.uid=$("#ajaxForm #wkbm-"+(i+1)).val();//文库编号
                            mxdata.index=$("#ajaxForm #jt1-"+(i+1)).val();//接头1
                            mxdata.index2=$("#ajaxForm #jt2-"+(i+1)).val();//接头2
                            mxdata.sample=$("#ajaxForm #ybbh-"+(i+1)).val();//标本编号
                            mxdata.project_type=$("#ajaxForm #project_type-"+(i+1)).val();//项目类型
                            mxdata.comment=$("#ajaxForm #yy-"+(i+1)).val();//原因
                            mxdata.dnand=$("#ajaxForm #dnand-"+(i+1)).val();//cdna浓度
							mxdata.dlnd=$("#ajaxForm #dlnd-"+(i+1)).val();//定量浓度
                            mxdata["con.lib"]=$("#ajaxForm #wknd-"+(i+1)).val();//文库浓度
                            mxdata["con.dna"]=$("#ajaxForm #hsnd-"+(i+1)).val();//con.dna其实就是用的提取的hsnd
                            mxdata["volume"]=$("#ajaxForm #tj-"+(i+1)).val();//体积
                            mxdata["con.sum"]=$("#ajaxForm #hbnd-"+(i+1)).val();//con.sum其实就是用的提取的合并浓度
                            mxdata["original_con_unit"]=$("#ajaxForm #nddw-"+(i+1)).val();//original_con_unit合并浓度单位
                            mxdata.goal_reads=$("#ajaxForm #yjxjsjl-"+(i+1)).val();//预计下机数据量
                            mxdata.dilution_factor=$("#ajaxForm #xsbs-"+(i+1)).val();//稀释倍数
                            mxdata.xmdm=$("#ajaxForm #xmdm-"+(i+1)).val();;//项目代码，暂定为空
                            mxdata.syglid=$("#ajaxForm #syglid-"+(i+1)).val();
                            mxdata.spike=$("#ajaxForm #spike-"+(i+1)).val();
                            var nbbm=$("#ajaxForm #nbbm-"+(i+1)).val();
                            var xmdm=$("#ajaxForm #xmdm-"+(i+1)).val();
                            if(nbbm&&xmdm){
                                xmdm=xmdm.substring(0,xmdm.length-1);
                                nbbm=nbbm.substring(nbbm.length-1);
                                mxdata.xmdm=xmdm+nbbm;
                            }else{
                                mxdata.xmdm=$("#ajaxForm #xmdm-"+(i+1)).val();
                            }
                            if(mxdata.uid!=null && mxdata.uid!=''){
                                var reg=/((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/;
                                if(!reg.test($("#ajaxForm #xsbs-"+(i+1)).val())){
                                    $.confirm("第"+(i+1)+"行稀释浓度只能输入正数和小数点后两位！");
                                    return false;
                                }
                                if(mxdata.sample==null || mxdata.sample==''){
                                    $.confirm("文库上机明细第"+(i+1)+"行标本信息关联失败，请确认原因！");
                                    return false;
                                }
                                data.push(mxdata);
                            }
                        }
                        wksj_json.data=data;
                        $("#ajaxForm #wksj_Json").val(JSON.stringify(wksj_json));
                    }
                    $("#ajaxForm #bcbj").val("1");
                    $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

                    submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                        if(responseText["status"] == 'success'){
                        let wkid = $("#ajaxForm #wkid").val()
                            $.success(responseText["message"],function() {
                                var url="/experiment/library/poolingexport?wkid="+wkid;
                                $.showDialog(url,'pooling导出',exportPoolingConfig);
                                if(opts.offAtOnce){
                                    $.closeModal(opts.modalName);
                                }
                            });
                        }else if(responseText["status"] == "caution"){
                            preventResubmitForm(".modal-footer > button", false);
                            $.confirm(responseText["message"],function(result) {

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

$(function(){
	//0.界面初始化
	// 1.初始化Table
	var oTable = new Library_TableInit();
	oTable.Init();
	
    //2.初始化Button的点击事件
    var oButtonInit = new Library_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#library_formSearch .chosen-select').chosen({width: '100%'});	
});

/**
 * 回传界面
 */
var uploadPoolingConfig={
    width		: "1000px",
    modalName	: "uploadPoolingModal",
    formName	: "poolingUploadForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                $("#poolingUploadForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"poolingUploadForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                        });
                    }else if(responseText["status"] == "caution"){
                        $.confirm(responseText["message"],function(result) {
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
                },".modal-footer > button");
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

/**
 * 导出界面
 */
var exportPoolingConfig={
    width		: "1000px",
    modalName	: "exportPoolingModal",
    formName	: "pooling_formSearch",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#pooling_formSearch").valid()){
                    $.alert("请填写完整信息!");
                    return false;
                }
                $("#pooling_formSearch #ac_tk").val($("#ac_tk").val());
                var dealpoolingUrl = $("#pooling_formSearch").attr("action")? $("#pooling_formSearch").attr("action"):"/experiment/library/pagedataDealPooling";
                poolingDownload($("#pooling_formSearch #wkid").val(),dealpoolingUrl)
                var $this = this;
                var opts = $this["options"]||{};
                $.closeModal(opts.modalName);
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function poolingDownload(wkid,url){
	var timezone = new Date().getTime();
    var map = {
        access_token:$("#ac_tk").val(),
        wkid:wkid,
		timezone:timezone,
        mbid:$("#pooling_formSearch #mbid").val(),
        sjzl:$("#pooling_formSearch #sjzl").val(),
        tsum:$("#pooling_formSearch #tsum").val(),
        qsum:$("#pooling_formSearch #qsum").val(),
        fjids:$("#pooling_formSearch #fjids").val(),
		TheMinimum:$("#pooling_formSearch #TheMinimum").val(),
        tpool_ReagentName:$("#pooling_formSearch #settingForm #tpool_ReagentName").val(),
        tpool_PoolVolume:$("#pooling_formSearch #settingForm #tpool_PoolVolume").val(),
        tpool_Concentration:$("#pooling_formSearch #settingForm #tpool_Concentration").val(),
        tpool_LibraryReferenceVolume:$("#pooling_formSearch #settingForm #tpool_LibraryReferenceVolume").val(),
        tpool_DilutedLibraryReferenceVolume:$("#pooling_formSearch #settingForm #tpool_DilutedLibraryReferenceVolume").val(),
        tpool_DilutionFactor:$("#pooling_formSearch #settingForm #tpool_DilutionFactor").val(),
        tpool_MReadsMultiple:$("#pooling_formSearch #settingForm #tpool_MReadsMultiple").val(),
        tpool_MixDilutedLibraryReferenceVolume:$("#pooling_formSearch #settingForm #tpool_MixDilutedLibraryReferenceVolume").val(),
        tpool_MixDilutedLibraryReferenceConcentration:$("#pooling_formSearch #settingForm #tpool_MixDilutedLibraryReferenceConcentration").val(),
        ReagentName:$("#pooling_formSearch #settingForm #ReagentName").val(),
        PoolVolume:$("#pooling_formSearch #settingForm #PoolVolume").val(),
        Concentration:$("#pooling_formSearch #settingForm #Concentration").val(),
        LibraryReferenceVolume:$("#pooling_formSearch #settingForm #LibraryReferenceVolume").val(),
        DilutedLibraryReferenceVolume:$("#pooling_formSearch #settingForm #DilutedLibraryReferenceVolume").val(),
        DilutionFactor:$("#pooling_formSearch #settingForm #DilutionFactor").val(),
        MReadsMultiple:$("#pooling_formSearch #settingForm #MReadsMultiple").val(),
        MixDilutedLibraryReferenceVolume:$("#pooling_formSearch #settingForm #MixDilutedLibraryReferenceVolume").val(),
        MixDilutedLibraryReferenceConcentration:$("#pooling_formSearch #settingForm #MixDilutedLibraryReferenceConcentration").val(),
    };
    //创建objectNode
    var cardiv =document.createElement("div");
    cardiv.id="cardiv";
    var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="poolingMsg" style="color:red;font-weight:600;">处理中</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="poolingCancel">取消</button></p></div></div>';
    cardiv.innerHTML=s_str;
    //将上面创建的P元素加入到BODY的尾部
    document.body.appendChild(cardiv);
    setTimeout("checkPoolingStatus(2000,'" + wkid + "','"+ timezone +"')", 1000);
	isCanel = false;
    $.post(url, map, function (data) {
        if (data) {
            if (data.status == 'success') {
                //创建objectNode
                var cardiv =document.createElement("div");
                cardiv.id="cardiv";
                var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="poolingMsg" style="color:red;font-weight:600;">处理中</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="poolingCancel">取消</button></p></div></div>';
                cardiv.innerHTML=s_str;
                //绑定导出取消按钮事件
                $("#poolingCancel").click(function(){
                    isCancel = true;
                    //先移除导出提示，然后请求后台
                    if($("#cardiv").length>0) $("#cardiv").remove();
					isCanel = true;
                    $.ajax({
                        type:'post',
                        url: '/experiment/library/pagedataPoolCancel',
                        cache: false,
                        data: map,
                        dataType:'json',
                        success: function (res) {
                            if (res != null && res.result == false) {
                                if (res.msg && res.msg != "")
									console.log(res.msg);
                            }
                        },
                        error:function(res){
                            if($("#cardiv").length>0) $("#cardiv").remove();
							console.log(res.message);
                        }
                    });
                });
            }
            else{
                $.error(data.message);
            }
        }
    }, 'json');
    return false;
}
var isCanel = false;
//pooling下载 自动检查进度
function checkPoolingStatus(intervalTime, redisKey,timezone) {
	if (isCanel){
		return;
	}
    $.ajax({
        type: "POST",
        url: "/experiment/library/pagedataCheckPoolingStatus",
        data: {"wkid": redisKey, "timezone": timezone, "access_token": $("#ac_tk").val()},
        dataType: "json",
        success: function (data) {
            if (data.status) {
                if(data.status == 'continue'){
                    if (data.message && data.message != "") {
                        if ($("#poolingMsg")) {
                             $("#poolingMsg").html(data.message)
                         }
                    }
                    if (intervalTime < 5000){
                        intervalTime = intervalTime + 1000;
                    }
                    setTimeout("checkPoolingStatus(" + intervalTime + ",'" + redisKey + "','" + timezone + "')", intervalTime);
                }else if(data.status == 'finish' && !isCanel){
                    if ($("#cardiv")) $("#cardiv").remove();
                    window.open("/experiment/library/pagedataPoolingFileDownload?wkid=" + redisKey + "&timezone=" + timezone + "&access_token=" + $("#ac_tk").val());
                } else{
                    if (data.message && data.message != "") {
                        $.error(data.message);
                        if ($("#cardiv")) $("#cardiv").remove();
                    }
                    return;
                }
            } else{
                 if (data.message && data.message != "") {
                     $.error(data.message);
                     if ($("#cardiv")) $("#cardiv").remove();
                 }
                 return;
             }
        }
    });
}
