///
var mater_turnOff=true;
    	
var mater_TableInit = function () {
    var oTableInit = new Object();
    $('#tab_020101').on('scroll',function(){
    	if($('#mater_formSearch #tb_list').offset().top - 122 < 0){
    		if(!$('#mater_formSearch .js-affix').hasClass("affix"))
    		{
    			$('#mater_formSearch .js-affix').removeClass("affix-top").addClass("affix");
    		}
    		$('#mater_formSearch .js-affix').css({
		    	'top': '100px',
		        "z-index":1000,
		        "width":'100%'
		      });
    	}else{
    		if(!$('#mater_formSearch .js-affix').hasClass("affix-top"))
    		{
    			$('#mater_formSearch .js-affix').removeClass("affix").addClass("affix-top");
    		}
    		$('#mater_formSearch .js-affix').css({
		    	'top': '0px',
		        "z-index":1,
		        "width":'100%'
		      });
    	}
    })
    //初始化Table
    oTableInit.Init = function () {
        $('#mater_formSearch #tb_list').bootstrapTable({
            url: $('#mater_formSearch #urlPrefix').val() + '/production/materiel/pageGetListMater',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#mater_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
//            paginationDetailHAlign: " hidden", //不显示底部文字
            sortable: true,                     //是否启用排序
            sortName: "wl.lrsj",				//排序字段
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
            uniqueId: "wlid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'wlid',
                title: '物料ID',
                width: '8%',
                align: 'left',
                visible: false
            }, 
            {
                field: 'wllbmc',
                title: '物料类别',
                titleTooltip:'物料类别',
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'wlzlbmc',
                title: '物料子类别',
                titleTooltip:'物料子类别',
                width: '10%',
                align: 'left',
                visible: false
            },{
				field: 'wlzlbtcmc',
				title: '物料子类别统称',
				titleTooltip:'物料子类别统称',
				width: '11%',
				align: 'left',
				visible: false
		     },{
                field: 'wlbm',
                title: '物料编码',
                titleTooltip:'物料编码',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true,
            }, {
                field: 'wlmc',
                title: '物料名称',
                titleTooltip:'物料名称',
                width: '18%',
                align: 'left',
                visible: true
            }, {
            	field: 'cpzch',
				title: '产品注册号',
				titleTooltip:'产品注册号',
				width: '18%',
				align: 'left',
				visible: true
            }, {
                field: 'lbmc',
                title: '类别',
                titleTooltip:'类别',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'scs',
                title: '生产商',
                titleTooltip:'生产商',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'ychh',
                title: '货号',
                titleTooltip:'货号',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'gg',
                title: '规格',
                titleTooltip:'规格',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'jldw',
                title: '单位',
                titleTooltip:'单位',
                width: '4%',
                align: 'right',
                visible: true
            },{
				field: 'rf',
				title: '人份',
				titleTooltip:'人份',
				width: '5%',
				align: 'left',
				visible: true,
				formatter:rfFormat
				},{
                field: 'bzq',
                title: '保质期',
                titleTooltip:'保质期',
                width: '6%',
                align: 'right',
                visible: true,
                formatter:bzqFormat
            },{
                field: 'bzqflg',
                title: '保质期标记',
                titleTooltip:'保质期标记',
                width: '6%',
                align: 'right',
                visible: false,
            },{
                field: 'sfwxpmc',
                title: '危险品',
                titleTooltip:'危险品',
                width: '8%',
                align: 'right',
                visible: true,
                // formatter:wxpFormat
            },{
                field: 'bctj',
                title: '保存条件',
                titleTooltip:'保存条件',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'aqkc',
                title: '安全库存量',
                titleTooltip:'安全库存量',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'zt',
                title: '状态',
                width: '10%',
                align: 'left',
                formatter:wlztFormat,
                visible: true
            },{
                field: 'scbj',
                title: '使用状态',
                width: '8%',
                align: 'left',
                formatter:wlsyztFormat,
                visible: true
            },{
                field: 'lrryxm',
                title: '申请人',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'jwlbm',
                title: '旧物料编码',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '10%',
                align: 'left',
                visible: true
            }, {
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '7%',
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
            	materDealById(row.wlid, 'view',$("#mater_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#mater_formSearch #tb_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true,
            pid:"mater_formSearch"
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
            sortLastName: "wl.wlid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        return getMaterSearchData(map);
    };
    return oTableInit;
};

function getMaterSearchData(map){
	var cxtj = $("#mater_formSearch #cxtj").val();
	// 查询条件
	var cxnr = $.trim(jQuery('#mater_formSearch #cxnr').val());
	// '0':'物料编号','1':'物料名称','2':'生产商','3':'货号'
	if (cxtj == "0") {
		map["wlbm"] = cxnr;
	}else if (cxtj == "1") {
		map["wlmc"] = cxnr;
	}else if (cxtj == "2") {
		map["scs"] = cxnr;
	}else if (cxtj == "3") {
		map["ychh"] = cxnr;
	}else if (cxtj == "4") {
		map["lrryxm"] = cxnr;
    }else if (cxtj == "5") {
    	map["jwlbm"] = cxnr;
	}else if (cxtj == "6") {
    	map["entire"] = cxnr;
	}else if (cxtj == "7") {
        map["gg"] = cxnr;
    }
	// 物料类别
	var wllb = jQuery('#mater_formSearch #wllb_id_tj').val();
	map["wllbs"] = wllb;
	// 物料子类别
	var wlzlb = jQuery('#mater_formSearch #wlzlb_id_tj').val();
	map["wlzlbs"] = wlzlb;
	// 物料子类别
	var wlzlbtc = jQuery('#mater_formSearch #wlzlbtc_id_tj').val();
	map["wlzlbtcs"] = wlzlbtc;
	
	// 是否危险品
	var sfwxps = jQuery('#mater_formSearch #sfwxp_id_tj').val();
	map["sfwxps"] = sfwxps;
	
	// 是否启用
	var scbjs = jQuery('#mater_formSearch #scbj_id_tj').val();
	map["scbjs"] = scbjs;
	
	// 审核状态
	var zts = jQuery('#mater_formSearch #zt_id_tj').val();
	map["zts"] = zts;
	
	// 通过开始日期
	var shsjstart = jQuery('#mater_formSearch #shsjstart').val();
	map["shsjstart"] = shsjstart;
	
	// 通过结束日期
	var shsjend = jQuery('#mater_formSearch #shsjend').val();
	map["shsjend"] = shsjend;
	
	//库存类别
	var lb = jQuery('#mater_formSearch #lb_id_tj').val();
	map["lbs"] = lb;
	return map;
}
//提供给导出用的回调函数
function materSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="wl.wlid";
	map["sortLastOrder"]="asc";
	map["sortName"]="wl.wlbm";
	map["sortOrder"]="asc";
	return getMaterSearchData(map);
}

// //危险品格式化
// function wxpFormat(value,row,index) {
//     if (row.sfwxp == '0') {
//         return '否';
//     }
//     else{
//         return '是';
//     }
// }

//保质期格式化
function bzqFormat(value,row,index) {
    if (row.bzqflg == '1') {
    	return row.bzq;
    }else {
    	return  row.bzq+'个月';
    }
}

//人份
function rfFormat(value,row,index) {
	if (row.rf) {
		return row.rf+'份';
	}else {
		return  "";
	}
}

//使用状态（删除标记）格式化
function wlsyztFormat(value,row,index) {
    if (row.scbj == '2') {
        return '停用';
    }else if (row.scbj == '1') {
        return '删除';
    }
    else{
        return '启用';
    }
}
/**
 * 物料列表的状态格式化函数
 * @returns
 */
function wlztFormat(value,row,index) {
    var param = {prefix:$('#mater_formSearch #urlPrefix').val()};
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.wlid + "\",event,\"AUDIT_METRIEL\",{prefix:\"" + $('#mater_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.wlid + "\",event,\"AUDIT_METRIEL\",{prefix:\"" + $('#mater_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else if (row.zt == '20') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.lsid + "\",event,\"AUDIT_METRIELMOD\",{prefix:\"" + $('#mater_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }else{
    	 return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.wlid + "\",event,\"AUDIT_METRIEL\",{prefix:\"" + $('#mater_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
	var qg_ids=$("#qg_ids").val().split(",");
	if (row.zt == '10' && row.scbj =='0' ) {
		return "<span class='btn btn-warning' onclick=\"recallMater('"+row.zt+"','" + row.wlid + "',event)\" >撤回</span>";
	}else if(row.zt == '20' && row.scbj =='0' ){
		return "<span class='btn btn-success' onclick=\"recallMater('"+row.zt+"','" + row.lsid + "',event)\" >撤回</span>";
	}else if(row.zt == '80'){
		if($("#mater_formSearch #btn_shopping").length>0){
			for(var i=0;i<qg_ids.length;i++){
				if(row.wlid==qg_ids[i]){
					return "<div id='qg"+row.wlid+"'><span id='t"+row.wlid+"' class='btn btn-danger' title='移出采购车' onclick=\"delShoppingMater('" + row.wlid + "',event)\" >取消</span></div>";
				}
			}
			return "<div id='qg"+row.wlid+"'><span id='t"+row.wlid+"' class='btn btn-info' title='加入采购车' onclick=\"addShoppingMater('" + row.wlid + "','"+ row.scbj + "',event)\" >采购</span></div>";
		}
		return "";
	}else{
		return "";
	}
}

//撤回项目提交
function recallMater(zt,wlid,event){
	var auditType = $("#mater_formSearch #auditType").val();
	if (zt == "20"){
		auditType =  $("#mater_formSearch #auditModType").val();
	}
	var msg = '您确定要撤回该文件吗？';
	var mater_params=[];
	mater_params.prefix=$('#mater_formSearch #urlPrefix').val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,wlid,function(){
				searchMaterResult();
			},mater_params);
		}
	});
}

//添加至采购车
function addShoppingMater(wlid,scbj,event){
	var sfyhcgqx=$("#mater_formSearch #btn_shopping");
	// var sfyhcgqx_gm=$("#mater_formSearch #btn_shopping-gm");
	// if(sfyhcgqx.length==0&&sfyhcgqx_gm==0){
	if(sfyhcgqx.length==0){
		$.confirm("很抱歉,您没有此权限!");
	}else if(scbj=='2'){
		$.confirm("该物料被停用，不允许采购!");
	}else{
		$.ajax({ 
		    type:'post',  
		    url:$('#mater_formSearch #urlPrefix').val()+"/production/materiel/pagedataAddToShoppingCar",
		    cache: false,
		    data: {"wlid":wlid,"access_token":$("#ac_tk").val()},  
		    dataType:'json', 
		    success:function(data){
		    	//返回值
		    	if(data.status=='success'){
		    		$("#qg_ids").val(data.qg_ids);
		    		addOrDelNum("add");
		    		$("#mater_formSearch #t"+wlid).remove();
		    		var html="<span id='t"+wlid+"' class='btn btn-danger' title='移出采购车' onclick=\"delShoppingMater('" + wlid + "',event)\" >取消</span>";
		    		$("#mater_formSearch #qg"+wlid).append(html);
		    	}
		    }
		});
	}
}

//从采购车中删除
function delShoppingMater(wlid,event){
	var sfyhcgqx=$("#mater_formSearch #btn_shopping");
	// var sfyhcgqx_gm=$("#mater_formSearch #btn_shopping-gm");
	// if(sfyhcgqx.length==0&&sfyhcgqx_gm.length==0){
	if(sfyhcgqx.length==0){
		$.confirm("很抱歉,您没有此权限!");
	}else{
		$.ajax({ 
		    type:'post',  
		    url:$('#mater_formSearch #urlPrefix').val()+"/production/materiel/pagedataDelToShoppingCar",
		    cache: false,
		    data: {"wlid":wlid,"access_token":$("#ac_tk").val()},  
		    dataType:'json', 
		    success:function(data){
		    	//返回值
		    	if(data.status=='success'){
		    		$("#qg_ids").val(data.qg_ids);
		    		addOrDelNum("del");
		    		$("#mater_formSearch #t"+wlid).remove();
		    		var html="<span id='t"+wlid+"' class='btn btn-info' title='加入采购车' onclick=\"addShoppingMater('" + wlid + "',event)\" >采购</span>";
		    		$("#mater_formSearch #qg"+wlid).append(html);
		    	}
		    }
		});
	}
}

/**
 * 采购车数字加减
 * @param sfbj
 * @returns
 */
function addOrDelNum(sfbj){
	if(sfbj=='add'){
		count=parseInt(count)+1;
	}else{
		count=parseInt(count)-1;
	}
	if((count==1 && sfbj=='add') || (count==0 && sfbj=='del')){
		btnOinit();
	}
	$("#cg_num").text(count);
}

var addMaterConfig = {
	width		: "800px",
	modalName	: "addMaterModal",
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

				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						if(opts.offAtOnce){
							$.closeModal(opts.modalName);
						}
						var mater_params=[];
						mater_params.prefix=$('#mater_formSearch #urlPrefix').val();
						//提交审核
						var auditType = $("#mater_formSearch #auditType").val();
						showAuditFlowDialog(auditType,responseText["ywid"],function(){
							searchMaterResult();
						},null,mater_params);
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

var modMaterTempConfig = {
	width		: "800px",
	modalName	: "modMaterTempModal",
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

				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						if(opts.offAtOnce){
							$.closeModal(opts.modalName);
						}
						var mater_params=[];
						mater_params.prefix=$('#mater_formSearch #urlPrefix').val();
						//提交审核
						var auditModType = $("#mater_formSearch #auditModType").val();
						showAuditFlowDialog(auditModType,responseText["ywid"],function(){
							searchMaterResult();
						},null,mater_params);
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

var submitMaterConfig = {
	width		: "800px",
	modalName	: "submitMaterModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "提交",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						if(opts.offAtOnce){
							$.closeModal(opts.modalName);
						}
						var mater_params=[];
						mater_params.prefix=$('#mater_formSearch #urlPrefix').val();
						//提交审核
						var auditType = $("#mater_formSearch #auditType").val();
						showAuditFlowDialog(auditType,responseText["ywid"],function(){
							searchMaterResult();
						},null,mater_params);
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

var modMaterConfig = {
	width		: "800px",
	modalName	: "addMaterModal",
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

				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							searchMaterResult();
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

var shoppingMaterConfig = {
		width		: "1500px",
		modalName	: "addMaterModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "提 交",
				className : "btn-primary",
				callback : function() {
					//去除页面校验
					// $("#shoppingCarForm #sqly").attr("validate","{required:true,stringMaxLength:512}");
					// $("#shoppingCarForm #xmbm").attr("validate","{required:true}");
					// $("#shoppingCarForm #xmdldm").attr("validate","{required:true}");
					// $("#shoppingCarForm #xmdl").attr("validate","{required:true}");
					// if(!$("#shoppingCarForm").valid()){
					// 	$.alert("所选信息有误！");
					// 	return false;
					// }
					if($("#shoppingCarForm #qglbdm").val() == "MATERIAL"){
						let jgdm= $("#shoppingCarForm #sqbmdm").val()
						if(!jgdm){
							$.alert("所选部门存在异常！");
							return false;
						}
					}
					var nowDate=new Date();//当前系统时间
					var $this = this;
					var opts = $this["options"]||{};
					var json=[];
					var data=$('#shoppingCarForm #tb_list').bootstrapTable('getData');//获取选择行数据
					var xmbmdm=$("#shoppingCarForm #xmbm option:selected").attr("csdm");
					var xmdldm=$("#shoppingCarForm #xmdl option:selected").attr("csdm");
					var xmmc=$("#shoppingCarForm #xmbm option:selected").attr("csmc");
					$("#shoppingCarForm #xmbmdm").val(xmbmdm);
					$("#shoppingCarForm #xmdldm").val(xmdldm);
					$("#shoppingCarForm #xmmc").val(xmmc);
					for(var i=0;i<data.length;i++){
						var sz={"index":'',"wlid":'',"cskz1":'',"sl":'',"qwrq":'',"bz":'',"wlbm":'',"fjids":'',"wlzlbtc":'',"hwmc":'',"hwbz":'',"hwjldw":'',"jg":'',"cpzch":''};
						sz.index=i;
						sz.wlid=data[i].wlid;
						sz.cskz1=data[i].cskz1;
						sz.cskz2=data[i].cskz2;
						if($("#sl_"+i).val()==null || $("#sl_"+i).val()==''){
							$.confirm("第"+(i+1)+"行货物数量不能为空!");
							return false;
						}else{
							sz.sl=$("#sl_"+i).val();
						}
						if($("#shoppingCarForm #qglbdm").val()=='MATERIAL'){
							if($("#qwrq_"+i).val()==null || $("#qwrq_"+i).val()==''){
								$.confirm("第"+(i+1)+"行使用日期不能为空!");
								return false;
							}else{
								sz.qwrq=$("#qwrq_"+i).val();
							}
						}
						if($("#shoppingCarForm #qglbdm").val()=='DEVICE'){
							if($("#qwrq_"+i).val()==null || $("#qwrq_"+i).val()==''){
								$.confirm("第"+(i+1)+"行期望日期不能为空!");
								return false;
							}else{
								sz.qwrq=$("#qwrq_"+i).val();
							}
						}
						if($("#shoppingCarForm #qglbdm").val()=='ADMINISTRATION'){
							if($("#hwmc_"+i).val()==null || $("#hwmc_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物名称不能为空!");
								return false;
							}else{
								sz.hwmc=$("#hwmc_"+i).val();
							}
							if($("#qwrq_"+i).val()==null || $("#qwrq_"+i).val()==''){
								$.confirm("第"+(i+1)+"行期望日期不能为空!");
								return false;
							}else{
								sz.qwrq=$("#qwrq_"+i).val();
							}
							if($("#hwbz_"+i).val()==null || $("#hwbz_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物规格型号不能为空!");
								return false;
							}else{
								sz.hwbz=$("#hwbz_"+i).val();
							}
							if($("#hwjldw_"+i).val()==null || $("#hwjldw_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物计量单位不能为空!");
								return false;
							}else{
								sz.hwjldw=$("#hwjldw_"+i).val();
							}
							if(nowDate<new Date($("#qwrq_"+i).val())){
								sz.qwrq=$("#qwrq_"+i).val();
							}
						}else if($("#shoppingCarForm #qglbdm").val()=='SERVICE'){
							sz.hwmc=$("#hwmc_"+i).val();
							sz.hwbz=$("#hwbz_"+i).val();
							if($("#gys_"+i).val()==null || $("#gys_"+i).val()==''){
								$.confirm("第"+(i+1)+"行货物供应商不能为空!");
								return false;
							}else{
								sz.gys=$("#gys_"+i).val();
							}
						}
						sz.jg=$("#jg_"+i).val();
						sz.hwyt=$("#hwyt_"+i).val();	
						sz.yq=$("#yq_"+i).val();
						sz.wbyq=$("#wbyq_"+i).val();
						sz.pzyq=$("#pzyq_"+i).val();
						sz.sjjl=$("#sjjl_"+i).val();
						sz.fjbj=data[i].fjbj;
						sz.wlbm=data[i].wlbm;
						sz.cskz1=data[i].cskz1;
						sz.cskz2=data[i].cskz2;
						sz.cpzch=data[i].cpzch;
						sz.bz=$("#bz_"+i).val();
						sz.fjids=$("#fj_"+i+" input").val();
						sz.wlzlbtc=data[i].wlzlbtc;
						json.push(sz);
					}
					$("#shoppingCarForm #qgmx_json").val(JSON.stringify(json));
					
					$("#shoppingCarForm input[name='access_token']").val($("#ac_tk").val());

					submitForm(opts["formName"]||"shoppingCarForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							var auditType = $("#shoppingCarForm #auditType").val();
							var purchase_params=[];
							purchase_params.prefix=$('#shoppingCarForm #urlPrefix').val();
							$.success(responseText["message"],function() {
								//提交审核
								showAuditFlowDialog(auditType,responseText["ywid"],function(){
									$("#mater_formSearch #cg_num").remove();
									$("#qg_ids").val("");
									$.closeModal(opts.modalName);
									searchMaterResult();
								},null,purchase_params);
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
			successtwo : {
				label : "保 存",
				className : "btn-success",
				callback : function() {
					//去除页面校验
					$("#shoppingCarForm #sqly").removeAttr("validate");
					$("#shoppingCarForm #xmbm").removeAttr("validate");
					$("#shoppingCarForm #xmdldm").removeAttr("validate");
					$("#shoppingCarForm #xmdl").removeAttr("validate");
					if(!$("#shoppingCarForm").valid()){
						$.alert("所选信息有误！");
						return false;
					}
					if($("#shoppingCarForm #qglbdm").val() == "MATERIAL"){
						let jgdm= $("#shoppingCarForm #sqbmdm").val()
						if(!jgdm){
							$.alert("所选部门存在异常！");
							return false;
						}
					}
					var $this = this;
					var opts = $this["options"]||{};
					var json=[];
					var data=$('#shoppingCarForm #tb_list').bootstrapTable('getData');//获取选择行数据
					var xmbmdm=$("#shoppingCarForm #xmbm option:selected").attr("csdm");
					var xmdldm=$("#shoppingCarForm #xmdl option:selected").attr("csdm");
					var xmmc=$("#shoppingCarForm #xmbm option:selected").attr("csmc");
					$("#shoppingCarForm #xmbmdm").val(xmbmdm);
					$("#shoppingCarForm #xmdldm").val(xmdldm);
					$("#shoppingCarForm #xmmc").val(xmmc);
					$("#shoppingCarForm #bcbj").val("1");//保存标记，用于跳过后台验证
					for(var i=0;i<data.length;i++){
						var sz={"index":'',"wlid":'',"sl":'',"qwrq":'',"bz":'',"wlbm":'',"fjids":'',"hwmc":'',"hwbz":'',"hwjldw":'',"jg":'',"hwyt":'',"yq":'',"cpzch":''};
						sz.index=i;
						sz.wlid=data[i].wlid;
						sz.sl=$("#sl_"+i).val();
						sz.wlbm=data[i].wlbm;11
						sz.qwrq=$("#qwrq_"+i).val();
						sz.jg=$("#jg_"+i).val();
						sz.fjbj=data[i].fjbj;
						sz.hwyt=$("#hwyt_"+i).val();	
						sz.yq=$("#yq_"+i).val();
						sz.wbyq=$("#wbyq_"+i).val();
						sz.pzyq=$("#pzyq_"+i).val();
						sz.sjjl=$("#sjjl_"+i).val();
						sz.wlbm=data[i].wlbm;
						sz.cpzch=data[i].cpzch;
						sz.bz=$("#bz_"+i).val();
						sz.fjids=$("#fj_"+i+" input").val();
						json.push(sz);
					}
					$("#shoppingCarForm #qgmx_json").val(JSON.stringify(json));
					
					$("#shoppingCarForm input[name='access_token']").val($("#ac_tk").val());

					submitForm(opts["formName"]||"shoppingCarForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								$("#mater_formSearch #cg_num").remove();
								$("#qg_ids").val("");
								searchMaterResult();
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

//库存维护弹窗
var stockupholdMaterConfig = {
		width		: "300px",
		modalName	: "stockupholdMaterModal",
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

					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchMaterResult();
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
//部门设置弹窗
var depsettingMaterConfig = {
	width		: "600px",
	modalName	: "depsettingMaterModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#depsettingForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#depsettingForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"depsettingForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							searchMaterResult();
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
//库存维护弹窗
var revisehqMaterConfig = {
		width		: "800px",
		modalName	: "revisehqMaterModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#reviseForm").valid()){
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					
					$("#reviseForm input[name='access_token']").val($("#ac_tk").val());

					submitForm(opts["formName"]||"reviseForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchMaterResult();
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
var viewMaterConfig = {
	width		: "800px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

//按钮动作函数
function materDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $('#mater_formSearch #urlPrefix').val() + tourl; 
	if(action =='view'){
		var url= tourl + "?wlid=" +id;
		$.showDialog(url,'查看物料',viewMaterConfig);
	}else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'新增物料',addMaterConfig);
	}else if(action =='mod'){
		var url=tourl + "?wlid=" +id;
		$.showDialog(url,'编辑物料',modMaterConfig);
	}else if(action =='modsubmit'){
		var url=tourl + "?wlid=" +id;
		$.showDialog(url,'物料修订提交',modMaterTempConfig);
	}else if(action =='submit'){
		var url=tourl + "?wlid=" +id;
		$.showDialog(url,'提交物料',submitMaterConfig);
		/*$.confirm('您确定要提交所选择的记录吗？',function(result){
			if(result){
				//提交审核
				var auditType = $("#mater_formSearch #auditType").val();
				showAuditFlowDialog(auditType,id,function(){
					searchMaterResult();
				});
			}
		});*/
	}else if(action =='stockuphold'){
		var url=tourl + "?wlid=" +id;
		$.showDialog(url,'库存维护',stockupholdMaterConfig);
	}else if(action =='depsetting'){
		var url=tourl + "?wlid=" +id;
		$.showDialog(url,'部门设置',depsettingMaterConfig);
	}else if(action=='revisehq'){
		var url=tourl + "?wlid=" +id;
		$.showDialog(url,'修订货期',revisehqMaterConfig);
	}else if(action=='shopping'){
		var url=tourl+"?wlflag=1";
		$.showDialog(url,'采购车',chancePurchaseTypeMaterConfig);
	}
}

var chancePurchaseTypeMaterConfig={
		width		: "600px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		modalName	: "chancePurchaseTypeModal",
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		} 	
};
function plcz(url,msg){
    $.confirm('您确定要'+msg+'选择的记录吗？',function(result){
        if(result){
            jQuery.ajaxSetup({async:false});
            jQuery.post(url,{access_token:$("#ac_tk").val()},function(responseText){
                setTimeout(function(){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            $('#mater_formSearch #tb_list').bootstrapTable('refresh');
                        });
                    }else if(responseText["status"] == "fail"){
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
                    preventResubmitForm(".modal-footer > button", false);
                },1);

            },'json');
            jQuery.ajaxSetup({async:true});
        }
    });
}
var mater_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_add = $("#mater_formSearch #btn_add");
    	var btn_mod = $("#mater_formSearch #btn_mod");
    	var btn_del = $("#mater_formSearch #btn_del");
    	var btn_view = $("#mater_formSearch #btn_view");
    	var btn_submit = $("#mater_formSearch #btn_submit");
    	var btn_query = $("#mater_formSearch #btn_query");
    	var btn_searchexport = $("#mater_formSearch #btn_searchexport");
    	var btn_selectexport = $("#mater_formSearch #btn_selectexport");
    	var btn_modsubmit = $("#mater_formSearch #btn_modsubmit");
    	var btn_stockuphold = $("#mater_formSearch #btn_stockuphold");
    	var btn_revisehq=$("#mater_formSearch #btn_revisehq");
    	var wllbBind = $("#mater_formSearch #wllb_id ul li a");
    	var btn_shopping= $("#mater_formSearch #btn_shopping");
    	// var btn_shopping_gm= $("#mater_formSearch #btn_shopping-gm");
		var btn_discard= $("#mater_formSearch #btn_discard");
		var btn_depsetting= $("#mater_formSearch #btn_depsetting");
		var btn_synchronous= $("#mater_formSearch #btn_synchronous");

    	//添加日期控件
    	laydate.render({
    	   elem: '#shsjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#shsjend'
    	  ,theme: '#2381E9'
    	});

    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchMaterResult(true);
    		});
    	}
    	if(btn_synchronous != null){
            btn_synchronous.unbind("click").click(function(){
                var sel_row = $('#mater_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length != 1){
                    $.alert('请选定一条记录!');
                    return false;
                }
                var url = $("#mater_formSearch #urlPrefix").val()+$(this).attr("tourl") + "?wlid="+sel_row[0].wlid;
                plcz(url,'同步');
            });
        }
    	btn_add.unbind("click").click(function(){
    		materDealById(null,"add",btn_add.attr("tourl"));
    	});
    	btn_shopping.unbind("click").click(function(){
    		materDealById(null,"shopping",btn_shopping.attr("tourl"));
    	});
    	// btn_shopping_gm.unbind("click").click(function(){
    	// 	materDealById(null,"shopping",btn_shopping_gm.attr("tourl"));
    	// });
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#mater_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			materDealById(sel_row[0].wlid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		//部门设置
		btn_depsetting.unbind("click").click(function(){
    		var sel_row = $('#mater_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			materDealById(sel_row[0].wlid,"depsetting",btn_depsetting.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_stockuphold.unbind("click").click(function(){
    		var sel_row = $('#mater_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			materDealById(sel_row[0].wlid,"mod",btn_stockuphold.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#mater_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			materDealById(sel_row[0].wlid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#mater_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].wlid;
    		}
    		ids = ids.substr(1);
    		$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url=$('#mater_formSearch #urlPrefix').val()+ btn_del.attr("tourl");
    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								searchMaterResult();
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
    	btn_submit.unbind("click").click(function(){
    		var sel_row = $('#mater_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			if(sel_row[0].zt !='00' && sel_row[0].zt !='15'){
    				$.error("您好！该条记录已提交或者已通过，不允许重复提交！");
    				return false;
    			}
    			materDealById(sel_row[0].wlid,"submit",btn_submit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_modsubmit.unbind("click").click(function(){
    		var sel_row = $('#mater_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			materDealById(sel_row[0].wlid,"modsubmit",btn_modsubmit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#mater_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].wlid;
        		}
        		ids = ids.substr(1);
        		$.showDialog($('#mater_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=MATERIEL_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});
    	btn_revisehq.unbind("click").click(function(){
    		var sel_row = $('#mater_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			materDealById(sel_row[0].wlid,"revisehq",btn_revisehq.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	})
    	btn_searchexport.unbind("click").click(function(){
    		$.showDialog($('#mater_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=MATERIEL_SEARCH&expType=search&callbackJs=materSearchData"
    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});
		btn_discard.unbind("click").click(function(){
			var sel_row = $('#mater_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==0){
				$.error("请至少选中一行");
				return;
			}
			var ids="";
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
				if( sel_row[i].zt=='00'||sel_row[i].zt=='15'){
					ids = ids + ","+ sel_row[i].wlid;
				}else{
					$.error("审核中和审核通过的数据不允许废弃！");
					return;
				}
			}
			ids = ids.substr(1);
			$.confirm('您确定要废弃所选择的记录吗？',function(result){
				if(result){
					jQuery.ajaxSetup({async:false});
					var url=$('#mater_formSearch #urlPrefix').val()+ btn_discard.attr("tourl");
					jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
						setTimeout(function(){
							if(responseText["status"] == 'success'){
								$.success(responseText["message"],function() {
									searchMaterResult();
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
    	
    	/**显示隐藏**/      
    	$("#mater_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(mater_turnOff){
    			$("#mater_formSearch #searchMore").slideDown("low");
    			mater_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#mater_formSearch #searchMore").slideUp("low");
    			mater_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});
    	
    	//绑定物料类别的单击事件
    	if(wllbBind!=null){
    		wllbBind.on("click", function(){
    			setTimeout(function(){
    				getWlzlbs();
    			}, 10);
    		});
    	}
    };

    return oInit;
};

var mater_PageInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
    	var scbj = $("#mater_formSearch a[id^='scbj_id_']");
    	$.each(scbj, function(i, n){
    		var id = $(this).attr("id");
    		var code = id.substring(id.lastIndexOf('_')+1,id.length);
    		if(code === '0'){
    			addTj('scbj',code,'mater_formSearch');
    		}
    	});
    }
    return oInit;
}

//物料子类别的取得
function getWlzlbs() {
	// 物料类别
	var wllb = jQuery('#mater_formSearch #wllb_id_tj').val();
	if (!isEmpty(wllb)) {
		wllb = "'" + wllb + "'";
		jQuery("#mater_formSearch #wlzlb_id").removeClass("hidden");
	}else{
		jQuery("#mater_formSearch #wlzlb_id").addClass("hidden");
	}
	// 项目类别不为空
	if (!isEmpty(wllb)) {
		var url = $('#mater_formSearch #urlPrefix').val()+"/systemmain/data/ansyGetJcsjListInStop";
		$.ajax({
			type : "POST",
			url : url,
			data : {"fcsids":wllb,"access_token":$("#ac_tk").val()},
			dataType : "json",
			success : function(data){
				if(data.length > 0) {
					var html = "";
					$.each(data,function(i){
						html += "<li>";
						if(data[i].scbj=="2"){
							html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('wlzlb','" + data[i].csid + "','mater_formSearch');\" id=\"wlzlb_id_" + data[i].csid + "\"><span style='color:red'>" + data[i].csmc + "(停用)" +"</span></a>";
						}else
							html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('wlzlb','" + data[i].csid + "','mater_formSearch');\" id=\"wlzlb_id_" + data[i].csid + "\"><span>" + data[i].csmc + "</span></a>";
						html += "</li>";
					});
					jQuery("#mater_formSearch #ul_wlzlb").html(html);
					jQuery("#mater_formSearch #ul_wlzlb").find("[name='more']").each(function(){
						$(this).on("click", s_showMoreFn);
					});
				} else {
					jQuery("#mater_formSearch #ul_wlzlb").empty();
					
				}
				jQuery("#mater_formSearch [id^='wlzlb_li_']").remove();
				$("#mater_formSearch #wlzlb_id_tj").val("");
			}
		});
	} else {
		jQuery("#mater_formSearch #div_wlzlb").empty();
		jQuery("#mater_formSearch [id^='wlzlb_li_']").remove();
		$("#mater_formSearch #wlzlb_id_tj").val("");
	}
}

function searchMaterResult(isTurnBack){
	//关闭高级搜索条件
	$("#mater_formSearch #searchMore").slideUp("low");
	mater_turnOff=true;
	$("#mater_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#mater_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#mater_formSearch #tb_list').bootstrapTable('refresh');
	}
}

var count=0;
function btnOinit(){
	if(count>0){
		var strnum=count;
		if(count>99){
			strnum='99+';
		}
		var html="";
		html+="<span id='cg_num' style='position:absolute;background-color:red;color:white;border-radius: 15px;border:0px;width:20px;height:20px;top:-10px;font-size:10px;line-height:150%;'>"+strnum+"</span>";
		$("#mater_formSearch #btn_shopping").append(html);
		// $("#mater_formSearch #btn_shopping-gm").append(html);
	}else{
		$("#mater_formSearch #cg_num").remove();
	}
}

$(function(){
	if($("#ycgzlsl").val()==null || $("#ycgzlsl").val()==''){
		count=0;
	}else{
		count=$("#ycgzlsl").val();
	}
	btnOinit();
	//0.界面初始化
	//1.初始化Table
    var oInit = new mater_PageInit();
    oInit.Init();
	
    //1.初始化Table
    var oTable = new mater_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new mater_ButtonInit();
    oButtonInit.Init();
	//所有下拉框添加choose样式
	jQuery('#mater_formSearch .chosen-select').chosen({width: '100%'});
	
	// 初始绑定显示更多的事件
	//var showMore = function() {
	$("#mater_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
});
