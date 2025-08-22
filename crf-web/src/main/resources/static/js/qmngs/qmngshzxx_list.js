var hzxx_turnOff=true;
var hzxx_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
     console.log('标记22');
        $('#qmngshzxxformSearch #hzxxqmngs_list').bootstrapTable({
            url: '/qmngs/hzxx/listQmngsHzxx',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#qmngshzxxformSearch #toolbar', //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName:"hz.rysj",					//排序字段
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
            uniqueId: "qmngshzid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'qmngshzid',
                title: '患者ID',
                width: '1%',
                align: 'left',
                visible: false
            }, {
                field: 'xmsx',
                title: '患者姓名缩写',
                width: '7%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'hzsjh',
                title: '患者实验随机号',
                width: '7%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'ylzxbh',
                title: '医疗中心',
                align: 'left',
                 width: '7%',
                sortable: true,
                visible: true
            }, {
                field: 'hzrzsj',
                title: '患者入组时间',
                align: 'center',
                 width: '5%',
                sortable: true,
                visible: true
            }, {
                field: 'txrq',
                title: '填写日期',
                width: '5%',
                align: 'center',
                sortable: true,
                visible: true
            },
            {
                field: 'txhzxm',
                title: '填写者名称',
                width: '5%',
                align: 'center',
                sortable: true,
                visible: true
             },
             {
                 field: 'lxdh',
                 title: '联系电话',
                 width: '5%',
                 align: 'center',
                 sortable: true,
                 visible: true
              },
             {
				field: 'nl',
				title: '年龄',
				width: '5%',
				align: 'center',
				sortable: true,
				visible: true
			},{
                field: 'xb',
                title: '性别',
                width: '6%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'sjfzjg',
                title: '随机分组结果',
                width: '4%',
                align: 'left',
                visible: true
            },{
                field: 'grxgzd',
                title: '感染相关诊断',
                 width: '4%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'kjzlzlc',
                title: '抗菌药物治疗总疗程',
                align: 'left',
                 width: '7%',
                sortable: true,
                visible: true
            },{
                field: 'sfkjjjtzl',
                title: '抗菌药物降阶梯治疗',
                width: '7%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'zgcysj',
                title: '出院时间',
                width: '9%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'cyzt',
                title: '出院状态',
                width: '6%',
                align: 'left',
                visible: true,
                sortable: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	qmngsqmngshzxxDealById(row.qmngshzid, 'view',$("#qmngshzxxformSearch #btn_view").attr("tourl"));
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
            sortLastName: "hz.qmngshzid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        return getqmngsHzxxSearchData(map);
    };
    return oTableInit;
}

function getqmngsHzxxSearchData(map){
	var cxbt = $("#qmngshzxxformSearch #cxbt").val();
	// 查询条件
	var cxnr = $.trim(jQuery('#qmngshzxxformSearch #cxnr').val());
	// '0':'来源编号','1':'外部编号','2':'孕周','3':'体重','4':'备注'
	if (cxbt == "0") {
		map["xmsx"] = cxnr;
	}else if (cxbt == "1") {
		map["hzsjh"] = cxnr;
	}else if (cxbt == "2") {
		map["ylzxbh"] = cxnr;
	}else if (cxbt == "3") {
		map["ylzxmc"] = cxnr;
	}else if (cxbt == "4") {
		map["txhzxm"] = cxnr;
	}else if (cxbt == "5") {
		map["lxdh"] = cxnr;
	}else if (cxbt == "6") {
     	map["nl"] = cxnr;
    }else if (cxbt == "7") {
     	map["kjzlzlc"] = cxnr;
    }
	// 删除标记
	var hzrzsjstart = jQuery('#qmngshzxxformSearch #hzrzsjstart').val();
	map["hzrzsjstart"] = hzrzsjstart;
	var hzrzsjend = jQuery('#qmngshzxxformSearch #hzrzsjend').val();
	map["hzrzsjend"] = hzrzsjend;
	var txrqstart = jQuery('#qmngshzxxformSearch #txrqstart').val();
    map["txrqstart"] = txrqstart;
    var txrqend = jQuery('#qmngshzxxformSearch #txrqend').val();
    map["txrqend"] = txrqend;
    var zgcysjstart = jQuery('#qmngshzxxformSearch #zgcysjstart').val();
    map["zgcysjstart"] = zgcysjstart;
    var zgcysjend = jQuery('#qmngshzxxformSearch #zgcysjend').val();
    map["zgcysjend"] = zgcysjend;
	var sfkjjjtzls = jQuery('#qmngshzxxformSearch #sfkjjjtzl_id_tj').val();
	map["sfkjjjtzls"] = sfkjjjtzls.replace(/'/g, "");
	var cyzt = jQuery('#qmngshzxxformSearch #zgcyzt_id_tj').val();
	map["cyzts"] = cyzt.replace(/'/g, "");
	var sjfzjgs = jQuery('#qmngshzxxformSearch #sjfzjg_id_tj').val();
	map["sjfzjgs"] = sjfzjgs.replace(/'/g, "");
	var xb = jQuery('#qmngshzxxformSearch #xb_id_tj').val();
	map["xbs"] = xb.replace(/'/g, "");
	var grxgzds = jQuery('#qmngshzxxformSearch #grxgzd_id_tj').val();
	map["grxgzds"] = grxgzds.replace(/'/g, "");
	return map;
}
laydate.render({
	   elem: '#hzrzsjstart'
	  ,theme: '#2381E9'
	});
	laydate.render({
	   elem: '#hzrzsjend'
	  ,theme: '#2381E9'
	});
	laydate.render({
	   elem: '#txrqstart'
	  ,theme: '#2381E9'
	});
	laydate.render({
	   elem: '#txrqend'
	  ,theme: '#2381E9'
	});
    laydate.render({
    elem: '#zgcysjstart'
    ,theme: '#2381E9'
    });
    laydate.render({
    elem: '#zgcysjend'
    ,theme: '#2381E9'
    });



var addqmngsHzxxConfig = {
	width		: "1200px",
	modalName	: "addHzxxqmngsModal",
	formName	: "hzxxqmngs_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success7 : {
			label : "保存",
			className : "btn-primary",
			callback : function() {
			console.log('2121');
				/*if(!$("#hzxxqmngs_ajaxForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}*/
				console.log('2121');
				var $this = this;
				var opts = $this["options"]||{};
				$("#djrbj").val("0");//第几日标记,0全部
				$("#hzxxqmngs_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchqmngshzxxResult();
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

var editqmngsHzxxConfig = {
	width		: "1200px",
	modalName	: "editHzxxqmngsModal",
	formName	: "hzxxqmngs_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success7 : {
			label : "保存",
			className : "btn-primary",
			callback : function() {
				/*if(!$("#hzxxqmngs_ajaxForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}*/
				var $this = this;
				var opts = $this["options"]||{};
				$("#djrbj").val("0");//第几日标记,0全部
				$("#hzxxqmngs_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchqmngshzxxResult();
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
var viewqmngsHzxxIndexConfig = {
	width		: "1200px",
	modalName	: "viewHzxxqmngsModal",
	formName	: "viewhzxxqmngs_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

//按钮动作函数
function qmngsqmngshzxxDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?qmngshzid=" +id;
		$.showDialog(url,'查看',viewqmngsHzxxIndexConfig);
	}else if(action =='add'){
		var url=tourl;
		$.showDialog(url,'新增',addqmngsHzxxConfig);
	}else if(action =='mod'){
		var url= tourl + "?qmngshzid=" +id;
		$.showDialog(url,'编辑',editqmngsHzxxConfig);
	}
}


var hzxxqmngs_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
     console.log('标记221');
        //初始化页面上面的按钮事件
    	var btn_add = $("#qmngshzxxformSearch #btn_add");
    	var btn_mod = $("#qmngshzxxformSearch #btn_mod");
    	var btn_del = $("#qmngshzxxformSearch #btn_del");
    	var btn_view = $("#qmngshzxxformSearch #btn_view");
		var btn_selectexport = $("#qmngshzxxformSearch #btn_selectexport");
    	var btn_qmngshzxx_query = $("#qmngshzxxformSearch #btn_qmngshzxx_query");

    	//绑定搜索发送功能
    	if(btn_qmngshzxx_query != null){
    		console.log('标记11');
    	
    		btn_qmngshzxx_query.unbind("click").click(function(){
    			searchqmngshzxxResult(true);
    		});
    	}
    	btn_add.unbind("click").click(function(){
    		qmngsqmngshzxxDealById(null,"add",btn_add.attr("tourl"));
    	});

    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#qmngshzxxformSearch #hzxxqmngs_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			qmngsqmngshzxxDealById(sel_row[0].qmngshzid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#qmngshzxxformSearch #hzxxqmngs_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			qmngsqmngshzxxDealById(sel_row[0].qmngshzid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
        btn_del.unbind("click").click(function(){
    		var sel_row = $('#qmngshzxxformSearch #hzxxqmngs_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0 || sel_row.length>1){
    			$.error("请选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].qmngshzid;
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
    								searchqmngshzxxResult();
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
		//---------------------------------选中导出---------------------------------------
		btn_selectexport.unbind("click").click(function(){
			var sel_row = $('#qmngshzxxformSearch #hzxxqmngs_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].qmngshzid;
				}
				ids = ids.substr(1);
				$.showDialog(exportPrepareUrl+"?ywid=QMNGSNDZ_SELECT&expType=select&ids="+ids
					,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
			}else{
				$.error("请选择数据");
			}
		});
    	/**显示隐藏**/      
    	$("#qmngshzxxformSearch #sl_searchMore").on("click", function(ev){
    		console.log("121");
    		var ev=ev||event;
    		if(hzxx_turnOff){
    			$("#qmngshzxxformSearch #searchMore").slideDown("low");
    			hzxx_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#qmngshzxxformSearch #searchMore").slideUp("low");
    			hzxx_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
    };

    return oInit;
};



function searchqmngshzxxResult(isTurnBack){
console.log('标记');
	if(isTurnBack){
		$('#qmngshzxxformSearch #hzxxqmngs_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#qmngshzxxformSearch #hzxxqmngs_list').bootstrapTable('refresh');
		}
}

function ndzqmngsSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="ndz.hzrzsj desc";
	map["sortLastOrder"]="desc";
	map["sortName"]="ndz.hzrzsj";
	map["sortOrder"]="desc";
	return getqmngsHzxxSearchData(map);
}
var qmngshzxx_PageInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
	    var sfsd = $("#qmngshzxxformSearch a[id^='sfsd_id_']");
		$.each(sfsd, function(i, n){
			var id = $(this).attr("id");
			var code = id.substring(id.lastIndexOf('_')+1,id.length);
			if(code == '0'){
				addTj('sfsd',code,'hzxxformSearch');
			}
		});
    }
    return oInit;
}

$(function(){
	//0.界面初始化
    var oInit = new qmngshzxx_PageInit();
    oInit.Init();
    //1.初始化Table
    var oTable = new hzxx_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new hzxxqmngs_ButtonInit();
    oButtonInit.Init();
    
	//所有下拉框添加choose样式
	jQuery('#qmngshzxxformSearch .chosen-select').chosen({width: '100%'});
});
