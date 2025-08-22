var pay_turnOff=true;
var pay_TableInit = function () { 
	 var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#payForm #tb_list').bootstrapTable({
			url: $("#payForm #urlPrefix").val()+'/contract/contract/pagedataListPayment',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#payForm #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"fkrq",					// 排序字段
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
			uniqueId: "htfkid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮                 								
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				'field': '',
			    'title': '序号',
			    'align': 'center',
			    'width': '3%',
			    'formatter': function (value, row, index) {
			    　　var options = $('#payForm #tb_list').bootstrapTable('getOptions'); 
			    　　return options.pageSize * (options.pageNumber - 1) + index + 1; 
			    }
			}, {
				field: 'htnbbh',
				title: '合同编号',
				width: '8%',
				align: 'left',
				visible: true
			}, {
				field: 'fkdh',
				title: '付款单号',
				width: '8%',
				align: 'left',
				visible: true
			}, {
				field: 'fkrq',
				title: '付款日期',
				width: '8%',
				align: 'left',
				visible: true
			},{				
				field: 'fkje',
				title: '付款金额',
				width: '8%',
				align: 'left',
				visible: true
			},{				
				field: 'fkbfb',
				title: '付款百分比',
				width: '8%',
				align: 'left',
				visible: true
			}, {				
				field: 'lrrymc',
				title: '操作人',
				width: '8%',
				align: 'left',
				visible: true
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
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
        sortLastName: "htfkid", // 防止同名排位用
        sortLastOrder: "desc", // 防止同名排位用
        htid:$("#payForm #htid").val()
        // 搜索框使用
        // search:params.search
    };
    return contractSearchData(map);
	};
	return oTableInit;
};

/**
 * 选择供应商列表
 * @returns
 */
function chooseGysSec(){
	var url=$('#payForm #urlPrefix').val() + "/warehouse/supplier/pagedataSupplier?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择供应商',addGysPayFormConfig);
}
var addGysPayFormConfig = {
	width		: "1200px",
	modalName	:"addGysPayFormModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row=$('#choosesupplier_formSearch #tb_list').bootstrapTable('getSelections');
				if(sel_row.length == 1){
					var gysid = sel_row[0].gysid;
					var gysmc = sel_row[0].gysmc;
					var khh = sel_row[0].khh;
					var zh = sel_row[0].zh;
					$("#pay_ajaxForm #zfdxmc").val(gysmc);
					$("#pay_ajaxForm #zfdx").val(gysid);
					$("#pay_ajaxForm #zffkhh").val(khh);
					$("#pay_ajaxForm #zffyhzh").val(zh);
				}else{
					$.error("请选中一行");
					return false;
				}
			},
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

/**
 * 点击显示
 * @returns
 */
function editdiv(){
	
	var date=new Date();
	//年
    var year=date.getFullYear();
    //月
    var month=date.getMonth()+1;
    //日
    var day=date.getDate();
    var rq=year+"-"+month+"-"+day;

	 var zje = $("#payForm #zje").html();
	 $("#pay_ajaxForm #htzje").val(zje);
	 $("#pay_ajaxForm #htfkid").val("");
	 $("#pay_ajaxForm #fkzje").val("");
	 $("#pay_ajaxForm #fkrq").val(rq);
	 $("#pay_ajaxForm #fkbfb").val("");
	 $("#pay_ajaxForm #fkjexgq").val("");	 
	 var formAction = $("#payForm #formAction").val();
	 $("#pay_ajaxForm #formAction").val(formAction);
	 var czr = $("#payForm #czr").val();
	 $("#pay_ajaxForm #czr").val(czr);
	 $("#addpay").show();
	 $("#payview").hide();
}

/**
 * 选择申请部门列表
 * @returns
 */
function chooseSqbm(){
	var url=$('#pay_ajaxForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择申请部门',addSqbmConfig);
}
var addSqbmConfig = {
	width		: "800px",
	modalName	:"addSqbmModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#pay_ajaxForm #sqbm').val(sel_row[0].jgid);
					$('#pay_ajaxForm #sqbmmc').val(sel_row[0].jgmc);
				}else{
					$.error("请选中一行");
					return false;
				}
			},
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

/**
 * 选择用款部门列表
 * @returns
 */
function chooseYkbm(){
	var url=$('#pay_ajaxForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择用款部门',addYkbmConfig);
}
var addYkbmConfig = {
	width		: "800px",
	modalName	:"addYkbmModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#pay_ajaxForm #ykbm').val(sel_row[0].jgid);
					$('#pay_ajaxForm #ykbmmc').val(sel_row[0].jgmc);
				}else{
					$.error("请选中一行");
					return false;
				}
			},
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

$(function(){
	//添加日期控件
	laydate.render({
	   elem: '#pay_ajaxForm #fkrq'
	  ,theme: '#2381E9'
	});
	laydate.render({
		elem: '#pay_ajaxForm #zwzfrq'
		,theme: '#2381E9'
		,min: 0
		,btns: ['clear', 'confirm']
	});
})

//付款金额事件，计算付款百分比
function fkjeblur(){
	var fkje = $("#pay_ajaxForm #fkzje").val();
	var htzje = $("#pay_ajaxForm #htzje").val();
	if(fkje==""){
		var obj = document.getElementById("fkbfb");
		obj.value=0+"%";
	}else if(htzje==""){
		 var obj = document.getElementById("fkbfb");
		 obj.value=100+"%";
	}else if(htzje=="0"){
		 var obj = document.getElementById("fkbfb");
		 obj.value=0+"%";
	}else{		
		fkjeNum = parseFloat(fkje);
		htzjeNum = parseFloat(htzje);
		result = parseFloat(Math.round(fkjeNum/htzjeNum*10000)/100);
		bfbbj = parseFloat("100");
		if(result >bfbbj){
			var bfb="100%";
		}else{
			var bfb =result+'%';
		}		
		 var obj = document.getElementById("fkbfb");
		 obj.value=bfb;
	}
	
}

function returnview(){
	$("#addpay").hide();
	$("#payview").show();
}
function insertpay(){
	//为空判断
	var fkje = $("#pay_ajaxForm #fkzje").val();
	var fkrq = $("#pay_ajaxForm #fkrq").val();
	var sqr = $("#pay_ajaxForm #sqr").val();
	var sqrmc = $("#pay_ajaxForm #sqrmc").val();
	var sqbm = $("#pay_ajaxForm #sqbm").val();
	var sqbmmc = $("#pay_ajaxForm #sqbmmc").val();
	var fkrq = $("#pay_ajaxForm #fkrq").val();
	var fkf = $("#pay_ajaxForm #fkf").val();
	var fkfs = $("#pay_ajaxForm #fkfs").val();
	var zwzfrq = $("#pay_ajaxForm #zwzfrq").val();
	var zfdx = $("#pay_ajaxForm #zfdx").val();
	var zffkhh = $("#pay_ajaxForm #zffkhh").val();
	var zffyhzh =$("#pay_ajaxForm #zffyhzh").val();
	var fksy=$("#pay_ajaxForm #fksy").val();
	var ykbm=$("#pay_ajaxForm #ykbm").val();
	var djcdfs=$("#pay_ajaxForm #djcdfs").val();
	if(fkje=="" || fkrq=="" || fkje==null || fkrq==null){
		$.alert("请填写完整信息！")
		return false;
	}
	if(sqr=="" || sqrmc=="" || sqr==null || sqrmc==null){
		$.alert("请填写申请人信息！")
		return false;
	}
	if(sqbm=="" || sqbmmc=="" || sqbm==null || sqbmmc==null){
		$.alert("请填写申请部门信息！")
		return false;
	}
	if(ykbm=="" || ykbm==null){
		$.alert("请填写用款部门信息！")
		return false;
	}
	if(fkf=="" || fkf ==null){
		$.alert("请填写付款方！")
		return false;
	}
	if(fkfs=="" || fkfs ==null){
		$.alert("请填写付款方式！")
		return false;
	}
	if(zwzfrq=="" || zwzfrq ==null){
		$.alert("请填写最晚支付日期！")
		return false;
	}
	if(zfdx=="" || zfdx ==null){
		$.alert("请填写支付对象！")
		return false;
	}
	if(zffkhh=="" || zffkhh ==null){
		$.alert("请填写支付方开户行！")
		return false;
	}
	if(zffyhzh=="" || zffyhzh ==null){
		$.alert("请填写支付方银行账户！")
		return false;
	}
	if(fksy=="" || fksy ==null ){
		$.alert("请填写付款事由！")
		return false;
	}
	if(fkje.length>9){
		$.alert("请控制字符长度不超过9位！")
		return false;
	}
	if(djcdfs=="" || djcdfs ==null){
		$.alert("请填写单据传达方式！")
		return false;
	}
	//验证付款是否超出合同总金额
	var yfje = $("#payForm #yfje").html();
	if(yfje.replace(/(^s*)|(s*$)/g, "").length ==0){		
		yfjeNum = 0.0;
	}else{
		yfjeNum = parseFloat(yfje);
	}
	var fkjixgq = $("#pay_ajaxForm #fkjexgq").val();
	if(fkjixgq.replace(/(^s*)|(s*$)/g, "").length ==0){
		fkjixgqNum = 0.0;
	}else{
		fkjixgqNum = parseFloat(fkjixgq);
	}
	var zje = $("#pay_ajaxForm #htzje").val();	
	fkjeNum = parseFloat(fkje);
	zjeNum = parseFloat(zje);
	jszje = yfjeNum - fkjixgqNum + fkjeNum;
	//判断数值类型
	var re = /^\d+(\.\d{1,2})?$/;
		if (fkje != "") {
			if (!re.test(fkje)) {
				$.alert("请输入正确的格式(整数或小数,小数保留两位)");
	            return false;
			}
	    }
	var access =$("#ac_tk").val();
	var fkmxJson=[];
	var sz={'htid':'','fkje':'','fkbfb':''}
	sz.htid=$("#payForm #htid").val();
	sz.fkje=$("#pay_ajaxForm #fkzje").val();
	sz.fkbfb=$("#pay_ajaxForm #fkbfb").val();
	fkmxJson.push(sz);
	$.ajax({
		type : 'post',
		url : $("#pay_ajaxForm #urlPrefix").val()+"/contract/contract/"+$("#pay_ajaxForm #formAction").val(),
		cache : false,
		data : {
			"fkdh":$("#pay_ajaxForm #fkdh").val(),
			"sqr":$("#pay_ajaxForm #sqr").val(),
			"sqbm":$("#pay_ajaxForm #sqbm").val(),
			"fkf":$("#pay_ajaxForm #fkf").val(),
			"fkfs":$("#pay_ajaxForm #fkfs").val(),
			"fkzje" : $("#pay_ajaxForm #fkzje").val(),
			"fkrq" : $("#pay_ajaxForm #fkrq").val(),
			"zwzfrq" : $("#pay_ajaxForm #zwzfrq").val(),
			"zfdx" : $("#pay_ajaxForm #zfdx").val(),
			"zffkhh" : $("#pay_ajaxForm #zffkhh").val(),
			"zffyhzh" : $("#pay_ajaxForm #zffyhzh").val(),
			"fygs" : $("#pay_ajaxForm #fygs").val(),
			"fkbfb" : $("#pay_ajaxForm #fkbfb").val(),
			"fksy" : $("#pay_ajaxForm #fksy").val(),
			"czr" : $("#pay_ajaxForm #czr").val(),
			"htid" : $("#payForm #htid").val(),
			"bz" : $("#pay_ajaxForm #bz").val(),
			"ykbm": $("#pay_ajaxForm #ykbm").val(),
			"djcdfs":$("#pay_ajaxForm #djcdfs").val(),
			"fkmxJson":JSON.stringify(fkmxJson),
			"access_token" : $("#ac_tk").val()
		},
		dataType : 'json',
		success:function(map){
			var ywid = map.ywid;
	    	//返回值
	    	if(map.status=="success"){
		    	 $("#payForm #yfje").html(map.yfje);
		    	 $("#payForm #htfkbfb").html(map.fkbfb);
	    		$.success(map.message,function(map){
	    			payResult();
					$("#addpay").hide();
					$("#payview").show();
				});
	    	}else if(map.status=="fail"){
	    		$.error(map.message);
	    	}	    	
	    },
	    fail:function(map){
			$.error(map.message);
		}
	});
}


function modviewpay(id){
	$("#payview").hide();
	$("#addpay").show();
	var access =$("#ac_tk").val();
	$.ajax({
		type : 'post',
		url : $("#payForm #urlPrefix").val()+"/contract/contract/modViewPayContract",
		data : {
			"htfkid" : id,
			"access_token" : $("#ac_tk").val()
		},
		dataType : 'json',
		success:function(map){
	    	 $("#pay_ajaxForm #htzje").val(map.htzje);
	    	 $("#pay_ajaxForm #htfkid").val(map.htfkid);
	    	 $("#pay_ajaxForm #formAction").val(map.formAction);
	    	 $("#pay_ajaxForm #fkzje").val(map.fkje);
	    	 $("#pay_ajaxForm #fkrq").val(map.fkrq);
	    	 $("#pay_ajaxForm #fkbfb").val(map.fkbfb);
	    	 $("#pay_ajaxForm #czr").val(map.czr);
	    	 $("#pay_ajaxForm #fkjexgq").val(map.fkje);
	    }
	});
}

function delpay(id,fkje){
	var access =$("#ac_tk").val();
	$.ajax({
		type : 'post',
		url : $("#payForm #urlPrefix").val()+"/contract/contract/delSavePayContract",
		data : {
			"htfkid" : id,
			"htid" : $("#payForm #htid").val(),
			"fkje" : fkje,
			"access_token" : $("#ac_tk").val()
		},
		dataType : 'json',
		success:function(map){
			//返回值
	    	if(map.status=="success"){
		    	 $("#payForm #yfje").html(map.yfje);
		    	 $("#payForm #htfkbfb").html(map.fkbfb);
		    	 $.success(map.message);
	    		 payResult();
	    	}else if(map.status=="fail"){
	    		$.error(map.message);
	    	}	    	
	    }
	});
}



/**
 * 操作按钮格式化函数
 * @returns
 */
function contractSearchData(map){
	return map;
}

//按钮动作函数
function payDealById(id,htid,action,tourl){
	if(!tourl){
		return;
	}
}

var pay_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
  	
    };

    return oInit;
};

function payResult(isTurnBack){
	if(isTurnBack){
		$('#payForm #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#payForm #tb_list').bootstrapTable('refresh');
	}
}


$(function(){
	//0.界面初始化
	// 1.初始化Table
	var oTable = new pay_TableInit();
	oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new pay_ButtonInit();
    oButtonInit.Init(); 
	// 所有下拉框添加choose样式
	jQuery('#payForm .chosen-select').chosen({width: '100%'});
	jQuery('#pay_ajaxForm .chosen-select').chosen({width: '100%'});
});

