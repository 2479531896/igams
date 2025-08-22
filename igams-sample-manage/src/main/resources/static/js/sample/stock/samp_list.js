
var samp_turnOff=true;
    	
var samp_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#samp_formSearch #tb_list').bootstrapTable({
            url: '/sample/stock/pageGetListSamp',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#samp_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "yb.lrsj",				//排序字段
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
            uniqueId: "ybid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'ybid',
                title: '标本ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'lyid',
                title: '来源ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'yblx',
                title: '标本类型',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter:yblxFormat
            }, {
                field: 'yblylx',
                title: '来源方',
                width: '6%',
                align: 'left',
                visible: true,
                formatter:lylxFormat
            }, {
                field: 'ybbh',
                title: '标本编号',
                width: '18%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'bxh',
                title: '冰箱号',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'chth',
                title: '抽屉号',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'hzh',
                title: '盒子号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'ysl',
                title: '原数量',
                width: '8%',
                align: 'right',
                visible: false
            },{
                field: 'sl',
                title: '数量',
                width: '8%',
                align: 'right',
                visible: true
            },{
                field: 'yds',
                title: '预定数',
                width: '8%',
                align: 'right',
                visible: true
            },{
                field: 'qswzdm',
                title: '起始位置',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'jswzdm',
                title: '结束位置',
                width: '6%',
                align: 'right',
                visible: true
            },{
                field: 'dw',
                title: '单位',
                width: '6%',
                align: 'right',
                sortable: true,
                visible: true
            },{
                field: 'nd',
                title: '浓度',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'lrsj',
                width: '8%',
                align: 'center',
                title: '录入时间',
                visible: false
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	sampDealById(row.ybid, 'view',$("#samp_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#samp_formSearch #tb_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true}        
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
            sortLastName: "yb.ybid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var cxtj = $("#samp_formSearch #cxtj").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#samp_formSearch #cxnr').val());
    	// '0':'来源编号','1':'标本编号','2':'冰箱号','3':'抽屉号','4':'盒子号','5':'备注'
    	if (cxtj == "0") {
    		map["lybh"] = cxnr;
    	}else if (cxtj == "1") {
    		map["ybbh"] = cxnr;
    	}else if (cxtj == "2") {
    		map["bxh"] = cxnr;
    	}else if (cxtj == "3") {
    		map["chth"] = cxnr;
    	}else if (cxtj == "4") {
    		map["hzh"] = cxnr;
    	}else if (cxtj == "5") {
    		map["bz"] = cxnr;
    	}
    	// 标本类型
    	var yblx = jQuery('#samp_formSearch #yblx_id_tj').val();
    	map["yblxs"] = yblx;
    	
    	// 来源方
    	var lyf = jQuery('#samp_formSearch #lyf_id_tj').val();
    	map["lyfs"] = lyf;
    	
    	return map;
    };
    return oTableInit;
};

//来源类型格式化
function lylxFormat(value,row,index) {
    if (row.yblylx == '0') {
        return '外部';
    }
    else{
        return '内部';
    }
}

function yblxFormat(value,row,index) {
	return row.yblxmc;
}

var addSampConfig = {
	width		: "1200px",
	modalName	: "addSampModal",
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
				$("#ajaxForm #sp_qswz").removeAttr("disabled");
				$("#ajaxForm #sp_jswz").removeAttr("disabled");
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				$("#ajaxForm #yblxdm").val($("#ajaxForm #yblx").find("option:selected").attr("csdm"));

				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchSampResult();
							}
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

var viewSampConfig = {
	width		: "800px",
	modalName	: "viewSampModal",
	formName	: "viewSamp_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var addApplyConfig = {
	width		: "800px",
	modalName	: "addSampApplyModal",
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
				
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				if($("#ajaxForm #sqs").val() > ($("#ajaxForm #ysl").val() - $("#ajaxForm #yyds").val())){
					$.error("申请数量不允许超过库存数",function() {
					});
					return false;
				}
				
				if($("#ajaxForm #yt").val() == ""){
					$.error("请填写申请用途",function() {
					});
					return false;
				}
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						if(opts.offAtOnce){
							$.closeModal(opts.modalName);
						}
						//提交审核
						var auditType = $("#samp_formSearch #auditType").val();
						showAuditFlowDialog(auditType,responseText["ywid"],function(){
							searchSampResult();
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

//按钮动作函数
function sampDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?ybid=" +id;
		$.showDialog(url,'查看标本',viewSampConfig);
	}else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'新增标本',addSampConfig);
	}else if(action =='mod'){
		var url=tourl + "?ybid=" +id;
		$.showDialog(url,'编辑标本',addSampConfig);
	}else if(action =='apply'){
		var url=tourl + "?ybid=" +id;
		$.showDialog(url,'标本申请',addApplyConfig);
	}
}


var samp_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_add = $("#samp_formSearch #btn_add");
    	var btn_mod = $("#samp_formSearch #btn_mod");
    	var btn_del = $("#samp_formSearch #btn_del");
    	var btn_view = $("#samp_formSearch #btn_view");
    	var btn_apply = $("#samp_formSearch #btn_apply");
    	
    	var btn_query = $("#samp_formSearch #btn_query");

    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchSampResult(true);
    		});
    	}
    	btn_add.unbind("click").click(function(){
    		sampDealById(null,"add",btn_add.attr("tourl"));
    	});
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#samp_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			sampDealById(sel_row[0].ybid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#samp_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			sampDealById(sel_row[0].ybid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#samp_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].ybid;
    		}
    		ids = ids.substr(1);
    		$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= btn_del.attr("tourl");
    				jQuery.post(url,{ybids:ids,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								searchSampResult();
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
    	btn_apply.unbind("click").click(function(){
    		var sel_row = $('#samp_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			sampDealById(sel_row[0].ybid,"apply",btn_apply.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
    	/**显示隐藏**/      
    	$("#samp_formSearch #sl_searchMore").on("click", function(ev){ 
    		var ev=ev||event; 
    		if(samp_turnOff){
    			$("#samp_formSearch #searchMore").slideDown("low");
    			samp_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#samp_formSearch #searchMore").slideUp("low");
    			samp_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});
    };

    return oInit;
};

function searchSampResult(isTurnBack){
	//关闭高级搜索条件
	$("#samp_formSearch #searchMore").slideUp("low");
	samp_turnOff=true;
	$("#samp_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#samp_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#samp_formSearch #tb_list').bootstrapTable('refresh');
	}
}

$(function(){

    //1.初始化Table
    var oTable = new samp_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new samp_ButtonInit();
    oButtonInit.Init();
	//所有下拉框添加choose样式
	jQuery('#samp_formSearch .chosen-select').chosen({width: '100%'});
	
	// 初始绑定显示更多的事件
	//var showMore = function() {
	$("#samp_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	
});
