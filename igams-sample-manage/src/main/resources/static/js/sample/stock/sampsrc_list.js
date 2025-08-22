var task_turnOff=true;
var ss_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#ss_formSearch #tb_samplesources').bootstrapTable({
            url: '/sample/stock/pageGetListSampSrc',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#ss_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "ly.lrsj",				//排序字段
            sortOrder: "desc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "lyid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'lyid',
                title: '来源ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'yblxmc',
                title: '标本类型',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'rklx',
                title: '入库类型',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter:rklxFormatter,
            }, {
                field: 'lybh',
                title: '来源编号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'ybbh',
                title: '标本编号',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'wbbh',
                title: '外部编号',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'jyjg',
                title: '检验结果',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'lydmc',
                title: '来源地',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'cysj',
                title: '采样时间',
                width: '8%',
                align: 'center',
                sortable: true,
                visible: true
            },{
                field: 'xb',
                title: '性别',
                width: '6%',
                align: 'left',
                visible: true,
                sortable: true,
                formatter:sexFormatter,
            },{
                field: 'yz',
                title: '孕周',
                width: '5%',
                align: 'right',
                sortable: true,
                visible: true
            },{
                field: 'tz',
                title: '体重',
                width: '5%',
                align: 'right',
                sortable: true,
                visible: true
            },{
                field: 'bz',
                align: 'left',
                title: '备注',
                visible: true
            },{
                field: 'zt',
                title: '状态',
                width: '5%',
                align: 'left',
                visible: true,
                formatter:ztFormatter,
            },{
                field: 'lrsj',
                align: 'center',
                title: '录入时间',
                visible: false
            },{
                title: '操作',
                align: 'center',
                width: '6%',
                valign: 'middle',
                formatter:dealFormatter,
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	sampSrcDealById(row.lyid, 'view',$("#ss_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#ss_formSearch #tb_samplesources").colResizable({
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
            sortLastName: "ly.lyid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var ybcxtj = $("#ss_formSearch #ybcxtj").val();
    	// 查询条件
    	var cxtj = $.trim(jQuery('#ss_formSearch #cxtj').val());
    	// '0':'来源编号','1':'外部编号','2':'孕周','3':'体重','4':'备注'
    	if (ybcxtj == "0") {
    		map["lybh"] = cxtj;
    	}else if (ybcxtj == "1") {
    		map["wbbh"] = cxtj;
    	}else if (ybcxtj == "2") {
    		map["yz"] = cxtj;
    	}else if (ybcxtj == "3") {
    		map["tz"] = cxtj;
    	}else if (ybcxtj == "4") {
    		map["bz"] = cxtj;
    	}else if (ybcxtj == "5") {
    		map["jyjg"] = cxtj;
    	}
    	
		// 采样开始时间
    	var cykssj = jQuery('#ss_formSearch #cykssj').val();
    		map["cykssj"] = cykssj;
		// 采样开始时间
    	var cyjssj = jQuery('#ss_formSearch #cyjssj').val();
    		map["cyjssj"] = cyjssj;	
    	//来源地
    	var lyds = jQuery("#ss_formSearch #lyd_id_tj").val();
    		map["lyds"] = lyds;
    	return map;	
    };
    return oTableInit;
};

//性别格式化
function sexFormatter(value, row, index) {
    if (row.xb == '1') {
        return '男';
    }
    else if (row.xb == '2') {
        return '女';
    }else{
    	return "";
    }
}

//添加日期控件
laydate.render({
   elem: '#cykssj'
  ,theme: '#2381E9'
});
laydate.render({
   elem: '#cyjssj'
  ,theme: '#2381E9'
});

function ztFormatter(value, row, index) {
	if (row.zt == '80') {
        return '已处理';
    }else if(row.zt == '15'){
    	return '已废弃';
    }else{
    	return '未处理';
    }
}

function rklxFormatter(value, row, index) {
	if (row.rklx == '00') {
        return '正式';
    }else{
    	return '临时';
    }
}

//操作按钮样式
function dealFormatter(value, row, index) {
	var id = row.lyid;
    var result = "";
    result += "<a href='javascript:void(0);' class='btn btn-xs green' onclick=\"sampSrcDealById('" + id + "', 'view',$('#ss_formSearch #btn_view').attr('tourl'))\" title='查看'><span class='glyphicon glyphicon-search'></span></a>";
    result += "<a href='javascript:void(0);' class='btn btn-xs blue' onclick=\"sampSrcDealById('" + id + "','edit',$('#ss_formSearch #btn_mod').attr('tourl'))\" title='编辑'><span class='glyphicon glyphicon-pencil'></span></a>";
    result += "<a href='javascript:void(0);' class='btn btn-xs red' onclick=\"sampSrcDealById('" + id + "','del',$('#ss_formSearch #btn_del').attr('tourl'))\" title='删除'><span class='glyphicon glyphicon-remove'></span></a>";

    return result;
}

var addSpConfig = {
	width		: "1200px",
	modalName	: "addSpModal",
	formName	: "SampSrc_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				
				if(!$("#ss_formAction").valid()){
					return false;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#"+$("#ss_formAction").val()+"SampSrc_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				if($("#"+$("#ss_formAction").val()+"SampSrc_ajaxForm #ss_yblx").find("option:selected").attr("cskz1")){
					$("#"+$("#ss_formAction").val()+"SampSrc_ajaxForm #ss_yblxdm").val($("#"+$("#ss_formAction").val()+"SampSrc_ajaxForm #ss_yblx").find("option:selected").attr("cskz1"));
				}else{
					$("#"+$("#ss_formAction").val()+"SampSrc_ajaxForm #ss_yblxdm").val($("#"+$("#ss_formAction").val()+"SampSrc_ajaxForm #ss_yblx").find("option:selected").attr("csdm"));
				}
				
				submitForm($("#ss_formAction").val() + opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchSrcResult();
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

var viewSpConfig = {
	width		: "900px",
	modalName	: "viewSpModal",
	formName	: "viewSp_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

//按钮动作函数
function sampSrcDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url=tourl + "?lyid=" +id;
		$.showDialog(url,'查看标本来源',viewSpConfig);
	}else if(action =='add'){
		var url=tourl;
		$.showDialog(url,'新增标本来源',addSpConfig);
	}else if(action =='edit'){
		var url=tourl +"?lyid=" +id;
		$.showDialog(url,'编辑标本来源',addSpConfig);
	}else if(action =='del'){
		$.confirm('您确定要删除所选择的记录吗？',function(result){
			if(result){
				jQuery.ajaxSetup({async:false});
				var url=tourl;
				jQuery.post(url,{ids:id,"access_token":$("#ac_tk").val()},function(responseText){
					setTimeout(function(){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								searchSrcResult();
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
}


var ss_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_add = $("#ss_formSearch #btn_add");
    	var btn_mod = $("#ss_formSearch #btn_mod");
    	var btn_del = $("#ss_formSearch #btn_del");
    	var btn_view = $("#ss_formSearch #btn_view");
    	var btn_spsource_query = $("#ss_formSearch #btn_spsource_query");
    	
    	//绑定搜索发送功能
    	if(btn_spsource_query != null){
    		btn_spsource_query.unbind("click").click(function(){
    			searchSrcResult(true);
    		});
    	}
    	btn_add.unbind("click").click(function(){
    		sampSrcDealById(null,"add",btn_add.attr("tourl"));
    	});
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#ss_formSearch #tb_samplesources').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			sampSrcDealById(sel_row[0].lyid,"edit",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#ss_formSearch #tb_samplesources').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			sampSrcDealById(sel_row[0].lyid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#ss_formSearch #tb_samplesources').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].lyid;
    		}
    		ids = ids.substr(1);
    		sampSrcDealById(ids,"del",btn_del.attr("tourl"));
    	});
    };

    return oInit;
};

function searchMore(){
var ev=ev||event; 
if(task_turnOff){
	$("#ss_formSearch #searchMore").slideDown("low");
	task_turnOff=false;
	this.innerHTML="基本筛选";
}else{
	$("#ss_formSearch #searchMore").slideUp("low");
	task_turnOff=true;
	this.innerHTML="高级筛选";
}
ev.cancelBubble=true;
}
function searchSrcResult(isTurnBack){
	if(isTurnBack){
		$('#ss_formSearch #tb_samplesources').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#ss_formSearch #tb_samplesources').bootstrapTable('refresh');
	}
	//关闭高级搜索条件
	$("#ss_formSearch #searchMore").slideUp("low");
	task_turnOff=true;
	$("#ss_formSearch #sl_searchMore").html("高级筛选");
}

$(function(){

    //1.初始化Table
    var oTable = new ss_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new ss_ButtonInit();
    oButtonInit.Init();
	//所有下拉框添加choose样式
	jQuery('#ss_formSearch .chosen-select').chosen({width: '100%'});
	// 初始绑定显示更多的事件
	$("#task_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
});
