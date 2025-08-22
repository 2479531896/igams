var Library_turnOff=true;

var Lib_TableInit = function () {
	var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#lib_formSearch #tb_list').bootstrapTable({
			url: '/lashome/library/listlibrary',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#lib_formSearch #toolbar', // 工具按钮用哪个容器
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
			    　　var options = $('#lib_formSearch #tb_list').bootstrapTable('getOptions'); 
			    　　return options.pageSize * (options.pageNumber - 1) + index + 1; 
			    }
			}, {
				field: 'wkmc',
				title: '文库名称',
				width: '15%',
				align: 'left',
				visible: true
			}, {
				field: 'xsyph',
				title: '稀释液批号',
				width: '18%',
				align: 'left',
				visible: true
			}, {
				field: 'kzyph',
				title: '扩增液批号',
				width: '18%',
				align: 'left',
				visible: true
			}, {
				field: 'bzyph1',
				title: '标准液批号1',
				width: '15%',
				align: 'left',
				visible: true
			}, {
				field: 'bzyph2',
				title: '标准液批号2',
				width: '15%',
				align: 'left',
				visible: true
			},{
				field: 'bzyph3',
				title: '标准液批号3',
				width: '15%',
				align: 'left',
				visible: true
			},{
				field: 'bzyph4',
				title: '标准液批号4',
				width: '15%',
				align: 'left',
				visible: true
			},{
				field: 'bzyph5',
				title: '标准液批号5',
				width: '15%',
				align: 'left',
				visible: true
			},{
				field: 'bxy',
				title: '变性液',
				width: '15%',
				align: 'left',
				visible: true
			},{
				field: 'phhhy',
				title: '平衡混合液',
				width: '15%',
				align: 'left',
				visible: true
			},{
				field: 'zjhcytj',
				title: '杂交缓冲液体积',
				width: '15%',
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



function getLibrarySearchData(map){
	var cxtj=$("#lib_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#lib_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["wkmc"]=cxnr
	}
	
	 return map;
}

function LibraryResult(isTurnBack){
	if(isTurnBack){
		$('#lib_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#lib_formSearch #tb_list').bootstrapTable('refresh');
	}
}

function LibraryById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?wkid=" +id;
		$.showDialog(url,'文库详细信息',viewLibraryConfig);
	}
}


var Lib_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
    	var btn_query = $("#lib_formSearch #btn_query"); //查询
		var btn_import = $("#lib_formSearch #btn_import"); //导入数据
		var btn_export = $("#lib_formSearch #btn_export"); //导出数据
		
    	/*--------------------------------------显示隐藏-----------------------------------------*/ 
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			LibraryResult(true);
    		});
    	}
	
    	//添加日期控件(创建时间开始时间)
    	laydate.render({
    	   elem: '#lib_formSearch #cjsjstart'
		   ,type: 'date'
    	});
    	//添加日期控件(创建时间结束时间)
    	laydate.render({
    	   elem: '#lib_formSearch #cjsjend'
		   ,type: 'date'
    	});
    	/*--------------------------------------导出文件-----------------------------------------*/ 
    	btn_export.unbind("click").click(function(){
    		var sel_row = $('#lib_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length == 1){
    			var wkid=sel_row[0].wkid;
    			$.ajax({
    				url: btn_export.attr("tourl"), 
    				type: "post",
    				dataType:'json',
    				data:{wkid:wkid,access_token:$("#ac_tk").val()},
    				success: function(data){
    					if(data.status=="success"){
    						jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
    					            '<input type="text" name="fjid" value="'+data.fjcfbDto.fjid+'"/>' + 
    					            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
    					        '</form>')
    					    .appendTo('body').submit().remove();
    						
    					}else{
    						$.error("生成文库文件失败！");
    		    			return;
    					}
    				}
    			})
    		}else {
    			$.error("请选中一行");
    			return;
    			
    		}
    	});
    	/* ---------------------------文库文件导入-----------------------------------*/
    	btn_import.unbind("click").click(function(){
    		var sel_row = $('#lib_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			libraryById(sel_row[0].wkid,"import",btn_import.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
    	/*--------------------------------------显示隐藏-----------------------------------------*/     
    	$("#lib_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(Library_turnOff){
    			$("#lib_formSearch #searchMore").slideDown("low");
    			Library_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#lib_formSearch #searchMore").slideUp("low");
    			Library_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});
    	/*------------------------------------------------------------------------------------*/
    	
    };
    return oInit;
};

function libraryById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='import'){
		var url=tourl + "?wkid=" +id;
		$.showDialog(url,'文库信息导入',importLibConfig);
	}
}


var importLibConfig = {
		width		: "600px",
		modalName	: "importLibModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "上传",
				className : "btn-primary",
				callback : function() {
					var $this = this;
					var opts = $this["options"]||{};
					
					$("#importLibForm input[name='access_token']").val($("#ac_tk").val());

					submitForm(opts["formName"]||"importLibForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									LibraryResult();
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
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

$(function(){
	//0.界面初始化
	// 1.初始化Table
	var oTable = new Lib_TableInit();
	oTable.Init();
	
    //2.初始化Button的点击事件
    var oButtonInit = new Lib_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#lib_formSearch .chosen-select').chosen({width: '100%'});	
});