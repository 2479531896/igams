var isPartnerFirstDisplay = true;
$("#xz input").blur(function(){
	var yzbz=$(this).val();
	var reg=/((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/;
	if(yzbz!=""&&!reg.test( yzbz)){
		$(this).attr("placeholder","只能输入正数和小数点后2位");
		$(this).val("");
	}
})
$("#hblx").on('change',function(){
	var hblx=$("#hblx").val();
	if(hblx==1){
		$("#wxid").prop("required", false);
		$("#checkwxid").hide();
		$("#mywxid .chosen-select").trigger("chosen:updated");
	}else{
		$("#wxid").prop("required", true);
		$("#checkwxid").show();
		$("#mywxid .chosen-select").trigger("chosen:updated");
	}
})

//通过省份级联城市
$("#sf").on('change',function(){
	var csid=$("#sf").val();
	$.ajax({
		url : "/partner/pagedataJscjcity",
		type : "post",
		data : {fcsid:csid,"access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data != null && data.length != 0){
	    		var csbHtml = "";
	    		csHtml += "<option value=''>--请选择--</option>";
            	$.each(data,function(i){   		
            		csHtml += "<option value='" + data[i].csid + "'>" + data[i].csmc + "</option>";
            	});
            	$("#ajaxForm #cs").empty();
            	$("#ajaxForm #cs").append(csHtml);
            	$("#ajaxForm #cs").trigger("chosen:updated");
	    	}else{
	    		var csHtml = "";
	    		csHtml += "<option value=''>--请选择--</option>";
	    		$("#ajaxForm #cs").empty();
            	$("#ajaxForm #cs").append(csHtml);
            	$("#ajaxForm #cs").trigger("chosen:updated");
	    	}
		}
	});
})

//通过省份级联城市
$("#fl").on('change',function(){
	var csid=$("#fl").val();
	$.ajax({
		url : "/partner/pagedataJscjsubclassify",
		type : "post",
		data : {fcsid:csid,"access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data != null && data.length != 0){
	    		var zflHtml = "";
	    		zflHtml += "<option value=''>--请选择--</option>";
            	$.each(data,function(i){   		
            		zflHtml += "<option value='" + data[i].csid + "'>" + data[i].csmc + "</option>";
            	});
            	$("#ajaxForm #zfl").empty();
            	$("#ajaxForm #zfl").append(zflHtml);
            	$("#ajaxForm #zfl").trigger("chosen:updated");
	    	}else{
	    		var zflHtml = "";
	    		zflHtml += "<option value=''>--请选择--</option>";
	    		$("#ajaxForm #zfl").empty();
            	$("#ajaxForm #zfl").append(zflHtml);
            	$("#ajaxForm #zfl").trigger("chosen:updated");
	    	}
		}
	});
})

function chooseKh() {
	var url = "/business/customer/pagedataListModel?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择公司', chooseKhConfig);
}

var chooseKhConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseKhModal",
	formName	: "ajaxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				var selkh = $("#customerListForm #yxkh").tagsinput('items');
				$("#ajaxForm  #khids").tagsinput({
					itemValue: "value",
					itemText: "text",
				});
				$("#ajaxForm  #khids").tagsinput('removeAll');
				for(var i = 0; i < selkh.length; i++){
					var value = selkh[i].value;
					var text = selkh[i].text;
					$("#ajaxForm  #khids").tagsinput('add',{"value":value,"text":text});
				}
				$.closeModal(opts.modalName);
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function chooseWxyh() {
	var url = "/wechat/user/pagedataListUser?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择微信用户', chooseWxyhConfig);
}

var chooseWxyhConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseWxyhModal",
	formName	: "ajaxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#userListWxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var selWx = $("#userListWxForm #yxyh").tagsinput('items');
				$("#ajaxForm  #cskz1").tagsinput({
					itemValue: "value",
					itemText: "text",
				})
				for(var i = 0; i < selWx.length; i++){
					var value = selWx[i].value;
					var text = selWx[i].text;
					$("#ajaxForm  #cskz1").tagsinput('add',{"value":value,"text":text});
				}
				$.closeModal(opts.modalName);
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
function checkemail(e){
	if (!e || !e.value){
		return false;
	}
	var email=e.value.split(",");
	var emailRegExp=/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
	if(email.length>1||email[0]!=""){
		for (let i = 0; i < email.length; i++) {
			var trim = $.trim(email[i]);
			if(!emailRegExp.test(trim))	{
				$.alert(trim+"不是正确的邮箱地址！");
				$('#ajaxForm #'+e.id).tagsinput('remove',email[i]);
			}
		}
	}

}
function initWxyh(){
	var wxids = $("#ajaxForm #wxids").val();
	if(!wxids || wxids.length < 0){
		return;
	}
	$.ajax({ 
	    type:'post',  
	    url:"/partner/partner/pagedataWechatUser",
	    cache: false,
	    data: {"ids":wxids,"access_token":$("#ac_tk").val()},  
	    dataType:'json',
	    success:function(result){
	    	//返回值
	    	var wxyhDtos = result.wxyhDtos;
	    	for(var i = 0; i < wxyhDtos.length; i++){
	        	$("#ajaxForm  #cskz1").tagsinput({
	    			itemValue: "value",
	    			itemText: "text",
	    		})
	    		$("#ajaxForm  #cskz1").tagsinput('add',{"value": wxyhDtos[i].wxid,"text": wxyhDtos[i].wxm});
	    	}
	    }
	});
}

function changeKh(){
	var html="";
	var selkh = $("#ajaxForm #khids").tagsinput('items');
	for(var i = 0; i < selkh.length; i++){
		html += "<option value='"+selkh[i].value+"'>"+selkh[i].text+"</option>";
	}
	$("#ajaxForm  #zykh").empty();
	if(!html){
		html += "<option value=''>--请选择--</option>";
	}
	$("#ajaxForm  #zykh").append(html);
	$("#ajaxForm  #zykh").trigger("chosen:updated");
}

function initSwkh(){
	var html="";
	$("#ajaxForm  #khids").tagsinput({
		itemValue: "value",
		itemText: "text",
	})
	if (!$("#ajaxForm #customerList").val()){
		html += "<option value=''>--请选择--</option>";
		$("#ajaxForm  #zykh").empty();
		$("#ajaxForm  #zykh").append(html);
		$("#ajaxForm  #zykh").trigger("chosen:updated");
		return;
	}

	var customerList = JSON.parse($("#ajaxForm #customerList").val());
	if(!customerList || customerList.length < 0){
		html += "<option value=''>--请选择--</option>";
		$("#ajaxForm  #zykh").empty();
		$("#ajaxForm  #zykh").append(html);
		$("#ajaxForm  #zykh").trigger("chosen:updated");
		return;
	}
	for(var i = 0; i < customerList.length; i++){
		if(customerList[i].gsmc){
			$("#ajaxForm  #khids").tagsinput('add',{"value": customerList[i].khid,"text": customerList[i].gsmc});
			if(customerList[i].zykh&&"1"==customerList[i].zykh){
				html += "<option value='"+customerList[i].khid+"' selected>"+customerList[i].gsmc+"</option>";
			}else{
				html += "<option value='"+customerList[i].khid+"'>"+customerList[i].gsmc+"</option>";
			}
		}
	}
	$("#ajaxForm  #zykh").empty();
	if(!html){
		html += "<option value=''>--请选择--</option>";
	}
	$("#ajaxForm  #zykh").append(html);
	$("#ajaxForm  #zykh").trigger("chosen:updated");
}
/**
 * 选择用户
 * @returns
 */
function chooseYh() {
	var url = "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择用户', chooseYhConfig);
}
var chooseYhConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseYhModal",
	formName	: "ajaxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#taskListFzrForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#ajaxForm #yhid').val(sel_row[0].yhid);
					$('#ajaxForm #zsxm').val(sel_row[0].yhm+'-'+sel_row[0].zsxm);
					$.closeModal(opts.modalName);
					//保存操作习惯
					$.ajax({
						type:'post',
						url:"/common/habit/commonModUserHabit",
						cache: false,
						data: {"dxid":sel_row[0].yhid,"access_token":$("#ac_tk").val()},
						dataType:'json',
						success:function(data){}
					});
				}else{
					$.error("请选中一行");
					return;
				}
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
//===============以上是原本文件的代码，下面是新增的代码=========================================

var jcdw_unselect_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#ajaxForm #jcdwInfoTab #tb_partner_unlist').bootstrapTable({
//            url: $("#audit_formSearch #urlPrefix").val()+'/systemmain/configProcess/listUnSelectProcess',//请求后台的URL（*）
        	url: '/partner/partner/pagedataListUnSelectJcdw',
        	method: 'get',                      //请求方式（*）
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: false,                    //是否启用排序
            sortName:"jc.cspx",						//排序字段
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: false,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            height: 400,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "csid",                    //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'csid',
                title: '检测ID',
                width: '20%',
                align: 'left',
                visible: false
            }, {
                field: 'csmc',
                title: '检测单位',
                width: '50%',
                align: 'left',
                visible: true
            }, {
                field: 'cspx',
                title: '序号',
                width: '30%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            	//$("#ajaxForm #jcdwInfoTab .export-left .fixed-table-header").attr("style","display:none");
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	removeToSelected(row);
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
            sortLastName: "jc.csid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        map["hbid"] = $("#ajaxForm #hbid").val();
    	return map;
    };
    return oTableInit;
};

var jcdw_selected_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#ajaxForm #jcdwInfoTab #tb_partner_list').bootstrapTable({
//            url: $("#audit_formSearch #urlPrefix").val()+'/systemmain/configProcess/listSelectedProcess',         //请求后台的URL（*）
        	url: '/partner/partner/pagedataListSelectedJcdw',
        	method: 'get',                      //请求方式（*）
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: false,                     //是否启用排序
            sortName:"hbdwqx.xh",					//排序字段
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 400,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "jcdw",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'hbid',
                title: '伙伴ID',
                width: '20%',
                align: 'left',
                visible: false
            }, {
                field: 'jcdw',
                title: 'csid',
                width: '20%',
                align: 'left',
                visible: false
            }, {
                field: 'jcdwmc',
                title: '检测单位',
                width: '50%',
                align: 'left',
                visible: true
            }, {
                field: 'xh',
                title: '序号',
                width: '10%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            	 //$("#ajaxForm #jcdwInfoTab .export-right .fixed-table-header").attr("style","display:none");
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	removeToOptional(row);
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
            sortLastName: "hbdwqx.jcdw", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        map["hbid"] = $("#ajaxForm #hbid").val();
    	return map;
    };
    return oTableInit;
};

function removeToSelected(row){
	$('#ajaxForm #tb_partner_list').bootstrapTable('insertRow', {
        index: row,
        row: {
        	jcdwmc: row.csmc,
            xh: row.cspx,
            jcdw: row.csid
        }
    });
	var ids = [];
	ids.push(row.csid);
	$('#ajaxForm #tb_partner_unlist').bootstrapTable('remove', {
        field: 'csid',
        values: ids
    });
}

function addOption(){
	var sel_row = $('#ajaxForm #tb_partner_unlist').bootstrapTable('getSelections');//获取选择行数据
	var ids = $.map($('#ajaxForm #tb_partner_unlist').bootstrapTable('getSelections'), function (row) {
        return row.csid;
    });
	if(sel_row.length<1){
		$.error("请选中一行左侧数据");
	}else{
		for (var i = 0; i < sel_row.length; i++) {
			$('#ajaxForm #tb_partner_list').bootstrapTable('insertRow', {
		        index: sel_row[i],
		        row: {
		        	jcdwmc: sel_row[i].csmc,
		            xh: sel_row[i].cspx,
//		        	xh: sel_row[i].index,
		            jcdw: sel_row[i].csid
		        }
		    });
		}
		$('#ajaxForm #tb_partner_unlist').bootstrapTable('remove', {
            field: 'csid',
            values: ids
        });
	}
}

function removeToOptional(row){
	$('#ajaxForm #tb_partner_unlist').bootstrapTable('insertRow', {
        index: row,
        row: {
            csmc: row.jcdwmc,
            cspx: row.xh,
            csid: row.jcdw
        }
    });
	
	var ids = [];
	ids.push(row.jcdw);
	$('#ajaxForm #tb_partner_list').bootstrapTable('remove', {
        field: 'jcdw',
        values: ids
    });
}

function moveOption(){
	var sel_row = $('#ajaxForm #tb_partner_list').bootstrapTable('getSelections');//获取选择行数据
	var ids = $.map($('#ajaxForm #tb_partner_list').bootstrapTable('getSelections'), function (row) {
        return row.jcdw;
    });
	if(sel_row.length<1){
		$.error("请选中一行右侧数据");
	}else{
		for (var i = 0; i < sel_row.length; i++) {
			$('#ajaxForm #tb_partner_unlist').bootstrapTable('insertRow', {
		        index: sel_row[i],
		        row: {
		            csmc: sel_row[i].jcdwmc,
		            cspx: sel_row[i].xh,
//		            xh: sel_row[i].index,
		            csid: sel_row[i].jcdw
		        }
		    });
		}
		$('#ajaxForm #tb_partner_list').bootstrapTable('remove', {
            field: 'jcdw',
            values: ids
        });
	}
}

function upOption(){
	var sel_row = $('#ajaxForm #tb_partner_list').bootstrapTable('getSelections');//获取选择行数据
	if(sel_row.length==1){
		var index = $("#ajaxForm #tb_partner_list").find("[data-uniqueid="+sel_row[0].jcdw+"]").attr("data-index");
		if(index > 0){
			$("#ajaxForm #tb_partner_list").find("[data-uniqueid="+sel_row[0].jcdw+"]").attr("data-index",index-1);
			$("#ajaxForm #tb_partner_list").find("[data-uniqueid="+sel_row[0].jcdw+"]").prev().attr("data-index",index);
		}
		$("#ajaxForm #tb_partner_list").find("[data-uniqueid="+sel_row[0].jcdw+"]").fadeOut().fadeIn();
		$("#ajaxForm #tb_partner_list").find("[data-uniqueid="+sel_row[0].jcdw+"]").prev().before($("#ajaxForm #tb_partner_list").find("[data-uniqueid="+sel_row[0].jcdw+"]"));
	}else{
		$.error("请选中一行");
	}
}

function downOption(){
	var sel_row = $('#ajaxForm #tb_partner_list').bootstrapTable('getSelections');//获取选择行数据
	if(sel_row.length==1){
		var index = $("#ajaxForm #tb_partner_list").find("[data-uniqueid="+sel_row[0].jcdw+"]").attr("data-index");
		if(index < $('#ajaxForm #tb_partner_list').bootstrapTable('getData').length){
			$("#ajaxForm #tb_partner_list").find("[data-uniqueid="+sel_row[0].jcdw+"]").attr("data-index", index+1);
			$("#ajaxForm #tb_partner_list").find("[data-uniqueid="+sel_row[0].jcdw+"]").attr("data-index", index);
		}
		$("#ajaxForm #tb_partner_list").find("[data-uniqueid="+sel_row[0].jcdw+"]").fadeOut().fadeIn();
		$("#ajaxForm #tb_partner_list").find("[data-uniqueid="+sel_row[0].jcdw+"]").next().after($("#ajaxForm #tb_partner_list").find("[data-uniqueid="+sel_row[0].jcdw+"]"));
	}else{
		$.error("请选中一行");
	}
}

$(function(){
	initWxyh();
	initSwkh();
	//1.初始化Table1
    var oTable = new jcdw_unselect_TableInit();
    oTable.Init();
    //2.初始化Table2
    var oTable2 = new jcdw_selected_TableInit();
    oTable2.Init();
	// //2.初始化Table
	// var oTable3 = new Bgmb_TableInit();
	// oTable3.Init();
    //初始化收费标准表
	var oTable=new Sfbz_TableInit();
	oTable.Init();
	setSfbz_json();
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
    
    $('a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
    	//e.relatedTarget // 前一个激活的标签页
    	//e.target // 激活的标签页
    	if(e.target.hash=="#jcdwInfoTab" && isPartnerFirstDisplay){
    		isPartnerFirstDisplay=false
    		$('#ajaxForm #jcdwInfoTab #tb_partner_unlist').bootstrapTable('refresh',{pageNumber:1});
    		$('#ajaxForm #jcdwInfoTab #tb_partner_list').bootstrapTable('refresh',{pageNumber:1});
    	}
	})
	var xmlist=$("#ajaxForm #jcxmlist").val();
	var json_t=[];
	var json=[];
	if(xmlist!="" && xmlist!=null){
		json_t=JSON.parse(xmlist);
		for (var i = 0; i < json_t.length; i++) {
			var sz = {"xh":'',"hbid":'',"jcxmid":'',"jczxmid":'',"bgmbid":'',"bgmbid2":''};
			sz.xh =  json_t[i].xh;
			sz.hbid =  json_t[i].hbid;
			sz.jczxmid =  json_t[i].jczxmid;
			sz.bgmbid =  json_t[i].bgmbid;
			sz.bgmbid2 =  json_t[i].bgmbid2;
			sz.jcxmid = json_t[i].jcxmid;
			json.push(sz);
		}
		$("#ajaxForm #bgmb_json").val(JSON.stringify(json));
	}
});

// ====================整改收费标准模块=====================
var t_map=[];
//初始化收费标准json字段
function setSfbz_json(){
	var json=[];
	var data=$('#ajaxForm #tb_sfbz_list').bootstrapTable('getData');//获取选择行数据
	for(var i=0;i<data.length;i++){
		var sz={"index":data.length,"hbid":'',"xm":'',"zxm":'',"sfbz":'',"tqje":'',"ksrq":'',"jsrq":''};
		sz.hbid=data[i].hbid;
		sz.xm=data[i].xm;
		sz.zxm=data[i].zxm;
		sz.sfbz=data[i].sfbz;
		sz.tqje=data[i].tqje;
		sz.ksrq=data[i].ksrq;
		sz.jsrq=data[i].jsrq;
		json.push(sz);
	}
	$("#ajaxForm #sfbz_json").val(JSON.stringify(json));
}

//新增明细
function addnewSfbz(){
	var data=JSON.parse($("#ajaxForm #sfbz_json").val());
	var a={"index":data.length,"hbid":'',"xm":'',"zxm":'',"sfbz":'',"tqje":'',"ksrq":'',"jsrq":''};
	t_map.rows.push(a);
	data.push(a);
	$("#ajaxForm #sfbz_json").val(JSON.stringify(data));
	$("#ajaxForm #tb_sfbz_list").bootstrapTable('load',t_map);
}
var Sfbz_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#ajaxForm #tb_sfbz_list").bootstrapTable({
			url: '/partner/partner/pagedataGetSfzbs?hbid='+$("#ajaxForm #hbid").val(),         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#ajaxForm #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   //是否显示分页（*）
			paginationShowPageGo: false,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "sfbz.hbid",				//排序字段
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
			showColumns: false,                  //是否显示所有的列
			showRefresh: false,                  //是否显示刷新按钮
			minimumCountColumns: 2,             //最少允许的列数
			clickToSelect: true,                //是否启用点击选中行
			//height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "hbid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			paginationDetailHAlign:' hidden',
			columns: [{
				field: 'hbid',
				title: '伙伴id',
				width: '10%',
				align: 'left',
				visible:false
			},{
				field: 'xm',
				title: '项目',
				width: '26%',
				align: 'left',
				visible: true,
				formatter:xmformat,
			},{
				field: 'zxm',
				title: '子项目',
				width: '16%',
				align: 'left',
				visible: true,
				formatter:zxmformat,
			},{
				field: 'sfbz',
				title: '收费标准',
				width: '8%',
				align: 'left',
				visible: true,
				formatter:sfbzformat,
			},{
				field: 'tqje',
				title: '提取金额',
				width: '8%',
				align: 'left',
				visible: true,
				formatter:tqjeformat,
			},{
				field: 'ksrq',
				title: '开始日期',
				width: '10%',
				align: 'left',
				visible: true,
				formatter:ksrqformat,
			},{
				field: 'jsrq',
				title: '结束日期',
				width: '10%',
				align: 'left',
				visible: true,
				formatter:jsrqformat,
			},{
				field: 'cz',
				title: '操作',
				width: '5%',
				align: 'left',
				formatter:czformat,
				visible: true
			},{
				field: 'htbh',
				title: '合同编号',
				width: '8%',
				align: 'left',
				visible: true
			}],
			onLoadSuccess:function(map){
				t_map=map;
				// if( map.rows == null || map.rows == undefined || map.rows.length <= 0 ){
				// 	addnewSfbz()
				// }
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
			},
		});
		$("#ajaxForm #tb_sfbz_list").colResizable({
			liveDrag:true,
			gripInnerHtml:"<div class='grip'></div>",
			draggingClass:"dragging",
			resizeMode:'fit',
			postbackSafe:true,
			partialRefresh:true}
		);
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			pageSize: params.limit,   // 页面大小
			pageNumber: (params.offset / params.limit) + 1,  // 页码
			access_token:$("#ac_tk").val(),
			sortName: params.sort,      // 排序列名
			sortOrder: params.order, // 排位命令（desc，asc）
			sortLastName: "sfbz.hbid", // 防止同名排位用
			sortLastOrder: "asc" // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return map;
	};
	return oTableInit;
}
/**
 * 收费标准---项目格式化 (该方法是选择基础数据项目)
 * @param value
 * @param row
 * @param index
 * @returns
 */
function xmformat(value,row,index){
	var xmList = JSON.parse($("#ajaxForm #xmList").val());
	var html="";
	if (xmList && xmList.length >0) {
		html +="<div id='xmdiv_"+index+"' name='xmdiv' style='width:100%;display:inline-block'>";
		html +=  "<select id='xm_"+index+"' name='xm_"+index+"' class='form-control'  onchange=\"checkXm('"+index+"')\">";
		html += "<option value=''>请选择--</option>";
		for(var i=0;i<xmList.length;i++){
			if(row.xm==xmList[i].csid){
				html += "<option value='"+xmList[i].csid+"' selected >"+xmList[i].csmc+"</option>";
			}else{
				html += "<option value='"+xmList[i].csid+"' >"+xmList[i].csmc+"</option>";
			}
		}
		html += "</select></div>";
	}else{
		html += "<span  >"+value+"</span>";
	}
	return html;
}
function checkXm(index){
	//修改项目值
	t_map.rows[index].xm = $("#ajaxForm #xm_" + index).find("option:selected").val();
	//子项目根据项目去限制选择范围
	var xmnew = $("#ajaxForm #xm_" + index).find("option:selected").val();
	var zxmList = JSON.parse($("#ajaxForm #zxmList").val());
	var html="";
	if (zxmList && zxmList.length >0) {
		html += "<option value=''>请选择--</option>";
		if( xmnew != null && xmnew != undefined && xmnew != ''){
			for(var i=0;i<zxmList.length;i++){
				if( xmnew == zxmList[i].fcsid ){
					html += "<option value='"+zxmList[i].csid+"' >"+zxmList[i].csmc+"</option>";
				}
			}
			$("#ajaxForm #zxm_"+index).empty();
			$("#ajaxForm #zxm_"+index).append(html);
			$("#ajaxForm #zxm_"+index).trigger("chosen:updated");
		}else{//项目id不存在，zxm无选择项
			$("#ajaxForm #zxm_"+index).empty();
			$("#ajaxForm #zxm_"+index).append(html);
			$("#ajaxForm #zxm_"+index).trigger("chosen:updated");
		}
		html += "</select></div>";
	}
}


/**
 * 收费标准---子项目格式化 (该方法是选择基础数据项目)
 * @param value
 * @param row
 * @param index
 * @returns
 */
function zxmformat(value,row,index){
	var zxmList = JSON.parse($("#ajaxForm #zxmList").val());
	var html="";
	if (zxmList && zxmList.length >0) {
		html +="<div id='zxmdiv_"+index+"' name='zxmdiv' style='width:100%;display:inline-block'>";
		html +=  "<select id='zxm_"+index+"' name='zxm_"+index+"' class='form-control'  onchange=\"checkZxm('"+index+"')\">";
		html += "<option value=''>请选择--</option>";
		if(row.xm != null && row.xm != undefined && row.xm != ''){
			for(var i=0;i<zxmList.length;i++){
				if(row.zxm==zxmList[i].csid){
					html += "<option value='"+zxmList[i].csid+"' selected >"+zxmList[i].csmc+"</option>";
				}else if(row.xm==zxmList[i].fcsid){
					html += "<option value='"+zxmList[i].csid+"' >"+zxmList[i].csmc+"</option>";
				}
			}
			$("#ajaxForm #zxm_"+index).empty();
			$("#ajaxForm #zxm_"+index).append(html);
		}else{//项目id不存在，zxm无选择项
			$("#ajaxForm #zxm_"+index).empty();
			$("#ajaxForm #zxm_"+index).append(html);
		}
		html += "</select></div>";
	}else{
		html += "<span  >"+value+"</span>";
	}
	$("#ajaxForm #zxm_"+index).trigger("chosen:updated");
	return html;
}
function checkZxm(index){
	t_map.rows[index].zxm = $("#ajaxForm #zxm_" + index).find("option:selected").val();
}
function sfbzformat(value,row,index){
	var html="";
	if(row.sfbz == null ||row.sfbz == undefined){
		row.sfbz="";
	}
	var html="<input id='sfbz_"+index+"' type='number' placeholder='填写数字' name='sfbz_"+index+"' class='sfbz' value='"+row.sfbz+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"tochangeZd('"+index+"',this,\'sfbz\')\" >";
	return html;
}
function tqjeformat(value,row,index){
	var html="";
	if(row.tqje == null ||row.tqje == undefined){
		row.tqje="";
	}
	var html="<input id='tqje_"+index+"' type='number' placeholder='填写数字' name='tqje_"+index+"' class='tqje' value='"+row.tqje+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"tochangeZd('"+index+"',this,\'tqje\')\" >";
	return html;
}
function tochangeZd(index,e,zdmc){
	if (zdmc=='sfbz'){
		t_map.rows[index].sfbz=e.value;
	}else if(zdmc=='tqje'){
		t_map.rows[index].tqje=e.value;
	}else if(zdmc=='ksrq'){
		t_map.rows[index].ksrq=e.value;
	}else if(zdmc=='jsrq'){
		t_map.rows[index].jsrq=e.value;
	}
}

function ksrqformat(value,row,index){
	if(row.ksrq==null || row.ksrq == undefined){
		row.ksrq="";
	}
	var html="<input id='ksrq_"+index+"' name='ksrq_"+index+"' placeholder='yyyy-mm-dd' class='ksrq' value='"+row.ksrq+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"tochangeZd('"+index+"',this,\'ksrq\')\" >";
	setTimeout(function() {
		laydate.render({
			elem: '#ajaxForm #ksrq_'+index
			,theme: '#2381E9'
			// , type: 'datetime'
			// , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
			,min: 0
			,btns: ['clear', 'confirm']
			,done: function(value, date, endDate){
				t_map.rows[index].ksrq=value;
			}
		});
	}, 100);
	return html;
}
function jsrqformat(value,row,index){
	if(row.jsrq==null || row.jsrq == undefined){
		row.jsrq="";
	}
	var html="<input id='jsrq_"+index+"' name='jsrq_"+index+"' placeholder='yyyy-mm-dd' class='jsrq' value='"+row.jsrq+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"tochangeZd('"+index+"',this,\'jsrq\')\" >";
	setTimeout(function() {
		laydate.render({
			elem: '#ajaxForm #jsrq_'+index
			,theme: '#2381E9'
			,min: 0
			,btns: ['clear', 'confirm']
			,done: function(value, date, endDate){
				t_map.rows[index].jsrq=value;
			}
		});
	}, 100);
	return html;
}

/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
	var html = "<span id='sfbz_delbtn' style='margin-left:5px;height: auto !important;' class='btn btn-default btn-sm' onclick=\"delSfbz('" + index + "',event)\" >删除</span>";
	return html;
}
/**
 * 删除操作(删除收费标准)
 * @param index
 * @param event
 * @returns
 */
function delSfbz(index,event){
	var t_data = JSON.parse($("#ajaxForm #sfbz_json").val());
	t_map.rows.splice(index,1);
	for(var i=0;i<t_data.length;i++){
		if(t_data[i].index == index){
			t_data.splice(i,1);
		}
	}
	$("#ajaxForm #tb_sfbz_list").bootstrapTable('load',t_map);
	var json=[];
	for(var i=0;i<t_data.length;i++){
		var sz={"index":i,"hbid":'',"xm":'',"zxm":'',"sfbz":'',"tqje":'',"ksrq":'',"jsrq":''};
		sz.hbid=t_data[i].hbid;
		sz.xm=t_data[i].xm;
		sz.zxm=t_data[i].zxm;
		sz.sfbz=t_data[i].sfbz;
		sz.tqje=t_data[i].tqje;
		sz.ksrq=t_data[i].ksrq;
		sz.jsrq=t_data[i].jsrq;
		json.push(sz);
	}
	$("#ajaxForm #sfbz_json").val(JSON.stringify(json));
}
$(function(){

    if(rows!=null){
        var list=rows
        for(var i=0;i<list.length;i++){
            var dto=list[i]
            if(dto.bgmbid==null){

                $("#bgmbid_"+dto.xh).on('chosen:showing_dropdown',function(e){
                       var html ="<option value=''>--请选择--</option>";
                       console.log(e)
                       for(var x=0;x<mblist.length;x++){
                            html+="<option  id='"+mblist[x].csid+"' value='"+mblist[x].csid+"' text='"+mblist[x].csmc+"'  csdm='"+mblist[x].csmc+"'>"+mblist[x].csmc+"</option>"
                       }
                       $("#"+e.target.id).empty();
                       $("#"+e.target.id).append(html);
                       $("#"+e.target.id).trigger("chosen:updated");
                       $("#"+e.target.id).unbind('chosen:showing_dropdown')
                })
            }
            if(dto.bgmbid2==null){

                $("#bgmbid2_"+dto.xh).on('chosen:showing_dropdown',function(e){
                       var html ="<option value=''>--请选择--</option>";
                       console.log(e)
                       for(var x=0;x<mblist.length;x++){
                            html+="<option  id='"+mblist[x].csid+"' value='"+mblist[x].csid+"' text='"+mblist[x].csmc+"'  csdm='"+mblist[x].csmc+"'>"+mblist[x].csmc+"</option>"
                       }
                       $("#"+e.target.id).empty();
                       $("#"+e.target.id).append(html);
                       $("#"+e.target.id).trigger("chosen:updated");
                       $("#"+e.target.id).unbind('chosen:showing_dropdown')
                })
            }
        }
    }

})