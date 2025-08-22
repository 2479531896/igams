var contract_mx_turnOff=true;
var contract_mx_TableInit = function () {
	var oTableInit = new Object();

	// 初始化Table
	oTableInit.Init = function () {
		$('#receipt_ajaxForm #htmx_list').bootstrapTable({
			url: $("#receiptForm #urlPrefix").val()+'/contract/contract/pagedataGetHtmxList?htid='+$("#receiptForm #htid").val(),         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#receipt_ajaxForm #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			paginationDetailHAlign:' hidden',//隐藏分页
			sortable: true,                     // 是否启用排序
			sortName:"xh",					// 排序字段
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
			uniqueId: "htmxid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true
			},{
				title: '序号',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '10%',
				align: 'left',
				visible:true
			}, {
				field: 'wlbm',
				title: '物料编码/货物名称',
				titleTooltip:'物料编码/货物名称',
				width: '40%',
				align: 'left',
				formatter:wlbmformat,
				visible: true
			}, {
				field: 'gg',
				title: '规格/标准',
				titleTooltip: '规格/标准',
				formatter: ggformat,
				width: '20%',
				align: 'left',
				visible: true
			}, {
				field: 'sl',
				title: '数量',
				titleTooltip:'数量',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'hjje',
				title: '合计金额',
				titleTooltip:'合计金额',
				width: '15%',
				align: 'left',
				visible: true
			},{
				field: 'fphms',
				title: '发票号码',
				titleTooltip:'发票号码',
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
		$("#contract_formSearch #contract_list").colResizable({
			liveDrag:true,
			gripInnerHtml:"<div class='grip'></div>",
			draggingClass:"dragging",
			resizeMode:'fit',
			postbackSafe:true,
			partialRefresh:true
		})
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
			sortLastName: "htid", // 防止同名排位用
			sortLastOrder: "desc" // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return map;
	};
	return oTableInit;
};



$("#keydown").bind("keydown",function(e){
	let evt = window.event || e;
	if (evt.keyCode == 13) {
		smqsm()
	}
});

/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wlbmformat(value,row,index){
	var hwmc=row.wlmc;
	var qglbdm=row.qglbdm;
	var title=row.wlmc;
	if(qglbdm!='MATERIAL' && qglbdm!=null && qglbdm!=''){
		hwmc=row.hwmc;
		title=row.hwmc;
	}
	if(row.qgbz !=null && row.qgbz!=''){
		title=row.qgbz;
	}
	var html="";
	if(row.wlbm!=null && row.wlbm!=''){
		html="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>"+"<span style='width:70%;line-height:24px;' title='"+title+"'>/"+hwmc+"</span>";
	}else{
		html="/";
	}
	if(row.qglbdm!='MATERIAL' && row.qglbdm!=null && row.qglbdm!=''){
		row.wlbm="/";
	}
	return html;
}


//规格/货物标准格式化
function ggformat(value,row,index){
	var gg=row.gg;
	var title=row.gg;
	if(row.qglbdm!='MATERIAL' && row.qglbdm!=null && row.qglbdm!=''){
		gg=row.hwbz;
		title=row.hwbz;
	}
	var html="<span style='width:70%;line-height:24px;' title='"+title+"'>"+gg+"</span>";
	return html;
}

function smqsm() {
//回车执行查询
	// debugger
	let str = document.getElementById("fphm").value;
	let strList = str.split(",");
	if (strList.length > 7){
		document.getElementById("fphm").value=strList[3];
		document.getElementById("fpdm").value=strList[2];
		document.getElementById("bhsje").value=strList[4];
		document.getElementById("kprq").value=strList[5];
		var date = new Date();
		var now = date .getFullYear()+"-"+(date .getMonth()+1)+"-"+date .getDate();
		document.getElementById("fpjsrq").value=now;
		let gyssl = document.getElementById("sl").value;
		let fpje =Math.floor(strList[4]*(1+gyssl/100)*100)/100;
		document.getElementById("fpje").value=fpje;
		let select = document.getElementById("fpzl");
		if (strList[1] == "01"){
			for (let i = 0; i < select.options.length; i++){
				//专用发票
				if (select.options[i].value == "01"){
					$("#receipt_ajaxForm #fpzl").val(select.options[i].value);
					$("#receipt_ajaxForm #"+select.options[i].id).prop("selected","selected");
					$("#receipt_ajaxForm #"+select.options[i].id).trigger("chosen:updated");
					break;
				}
			}
		}
		if (strList[1] == "10") {
			for (let i = 0; i < select.options.length; i++) {
				//普通发票
				if (select.options[i].value == "10") {
					$("#receipt_ajaxForm #fpzl").val(select.options[i].value);
					$("#receipt_ajaxForm #" + select.options[i].id).prop("selected", "selected");
					$("#receipt_ajaxForm #" + select.options[i].id).trigger("chosen:updated");
					break;
				}
			}
		}
		if (strList[1] == "04"){
			for (let i = 0; i < select.options.length; i++){
				//专用发票
				if (select.options[i].value == "04"){
					$("#receipt_ajaxForm #fpzl").val(select.options[i].value);
					$("#receipt_ajaxForm #"+select.options[i].id).prop("selected","selected");
					$("#receipt_ajaxForm #"+select.options[i].id).trigger("chosen:updated");
					break;
				}
			}
		}
		var num = $("#receipt_ajaxForm #kprq").val();
		var reg = /(\d{4})/;
		var numlist = num.split(reg);
		if (numlist.length < 4){
			$.error("开票日期有误！");
			return false;
		}
		var year = numlist[1];
		var time = numlist[3];
		var reg2 =  /(\d{2})/;
		var timelist = time.split(reg2);
		if (timelist.length < 4){
			$.error("开票日期有误！");
			return false;
		}
		var month = timelist[1];
		var day = timelist[3];
		document.getElementById("kprq").value = year+"-"+month+"-"+day;
	}
}
function jsFpje() {
	let gyssl = document.getElementById("sl").value;
	let bhsje = document.getElementById("bhsje").value;
	let fpje = Math.floor(bhsje*(1+gyssl/100)*100)/100;
	document.getElementById("fpje").value=fpje;
}
function jsbhsje() {
	let gyssl = document.getElementById("sl").value;
	let fpje = document.getElementById("fpje").value;
	let bhsje = Math.floor(fpje/(1+gyssl/100)*100)/100;
	document.getElementById("bhsje").value=bhsje;
}
/**
 * 点击显示文件上传
 * @returns
 */
function ContractFpeditfile(){
	$("#contractFileDiv").show();
	$("#contractFile_btn").hide();
}
/**
 * 点击隐藏文件上传
 * @returns
 */
function ContractFpcancelfile(){
	$("#contractFileDiv").hide();
	$("#contractFile_btn").show();
}
//操作按钮样式
function dealFormatter(value, row, index) {
	var id = row.htfpid;
	var fkje = row.fkje;
    var result = "<div class='col-sm-12 col-md-12 '><div class='row'><div class='btn-group'>";
    result += "<button class='btn btn-default col-md-6 col-sm-6' type='button' title='合同发票编辑'><span class='glyphicon glyphicon-remove' onclick=\"modviewreceipt('" + id + "')\" >编辑</span></button>";
    result += "<button class='btn btn-default col-md-6 col-sm-6' type='button' title='合同发票删除'><span class='glyphicon glyphicon-pencil' onclick=\"delreceipt('" + id + "','" + fkje + "')\">删除</span></button>";
    result += "</div></div></div>";
    return result;
}

/**
 * 点击显示
 * @returns
 */
function editdiv(){
	 // $("#receipt_ajaxForm #fphm").focus();
	 var zje = $("#receiptForm #zje").html();
	 $("#receipt_ajaxForm #htzje").val(zje);
	 $("#receipt_ajaxForm #htfpid").val(""); 
	 $("#receipt_ajaxForm #fphm").val("");
	 // $("#receipt_ajaxForm #sl2").val("");
	 $("#receipt_ajaxForm #fpje").val("");
	 $("#receipt_ajaxForm #fpzl").val("");
	 $("#receipt_ajaxForm #fpjsrq").val("");
	 $("#receipt_ajaxForm #zjcwrq").val("");
	 $("#receipt_ajaxForm #fpzlmc").val("");
	 $("#receipt_ajaxForm #bhsje").val("");
	 $("#receipt_ajaxForm #kprq").val("");
	 $("#receipt_ajaxForm #fpdm").val("");
	 $("#receipt_ajaxForm #qxz").prop("selected","selected");
	 $("#receipt_ajaxForm #qxz").trigger("chosen:updated");
	 var formAction = $("#receiptForm #formAction").val();
	 $("#receipt_ajaxForm #formAction").val(formAction);
	 var czr = $("#receiptForm #czr").val();
	 $("#receipt_ajaxForm #czr").val(czr);
	 $("#addreceipt").show();
	 $("#receiptview").hide();
}

$(function(){
	//添加日期控件
	laydate.render({
	   elem: '#receipt_ajaxForm #fkrq'
	  ,theme: '#2381E9'
	});	    
})

function receiptResult(){
	$("#receiptbody").load($("#receiptForm #urlPrefix").val()+"/contract/contract/receiptContract?htid="+$("#receiptForm #htid").val());
}

function ContractReturnview(){
	$("#addreceipt").hide();
	$("#receiptview").show();
	$('#fjnr').remove("");
}
function ContractInsertreceipt(){
	if(!$("#receipt_ajaxForm").valid()){
		$.alert("请填写完整信息！")
		return false;
	}
	if($("#receipt_ajaxForm #fpzl").val()==null || $("#receipt_ajaxForm #fpzl").val()==''){
		$.alert("请选择发票种类！");
		return false;
	}
	var sel_row = $('#receipt_ajaxForm #htmx_list').bootstrapTable('getSelections');//获取选择行数据
	var ids="";
	if(sel_row.length>0){
		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
			ids= ids + ","+ sel_row[i].htmxid;
		}
		ids=ids.substr(1);
	}
	var obj = document.getElementById("fpzl");
	var indexs = obj.selectedIndex;
	var id = obj.options[indexs].id;
	var access =$("#ac_tk").val();
	$.ajax({
		type : 'post',
		url : $("#receiptForm #urlPrefix").val()+"/contract/contract/"+$("#receipt_ajaxForm #formAction").val(),
		cache : false,
		data : {
			"htfpid": $("#receipt_ajaxForm #htfpid").val(),
			"fphm" : $("#receipt_ajaxForm #fphm").val(),
			"sl" : $("#receipt_ajaxForm #sl").val(),
			"fpje" : $("#receipt_ajaxForm #fpje").val(),
			"fjids" : $("#receipt_ajaxForm #fjids").val(),
			"fpjsrq" : $("#receipt_ajaxForm #fpjsrq").val(),
			"zjcwrq" : $("#receipt_ajaxForm #zjcwrq").val(),
			"fpzl" : id,
			"bhsje" : $("#receipt_ajaxForm #bhsje").val(),
			"kprq" : $("#receipt_ajaxForm #kprq").val(),
			"fpdm" : $("#receipt_ajaxForm #fpdm").val(),
			"htid" : $("#receiptForm #htid").val(),
			"access_token" : $("#ac_tk").val(),
			"ids": ids
		},
		dataType : 'json',
		success:function(map){
	    	//返回值
	    	if(map.status=="success"){
	    		$("#addreceipt").hide();
	    		$("#receiptview").show();
	    		$.success(map.message);
	    		 receiptResult();
	    	}else if(map.status=="fail"){
	    		$.error(map.message);
	    	}	    	
	    }
	});
}
/**
 * 点击图片预览
 * @param fjid
 * @param wjm
 * @returns
 */
function view(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url=$('#receiptForm #urlPrefix').val()+"/ws/sjxxpripreview?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= $('#receiptForm #urlPrefix').val()+"/common/file/pdfPreview?fjid=" + fjid;
		$.showDialog(url,'文件预览',JPGMaterConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}
var JPGMaterConfig = {
	width		: "800px",
	offAtOnce	: true,
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
/**
 * 点击文件下载
 * @param fjid
 * @returns
 */
function xz(fjid){
	jQuery('<form action="'+$('#receiptForm #urlPrefix').val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
		'<input type="text" name="fjid" value="'+fjid+'"/>' +
		'<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
		'</form>')
		.appendTo('body').submit().remove();
}
/**
 * 点击删除文件
 * @param fjid
 * @param wjlj
 * @returns
 */
function del(fjid,wjlj){
	$.confirm('您确定要删除所选择的记录吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url= $('#receiptForm #urlPrefix').val()+"/common/file/delFile";
			jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
				setTimeout(function(){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							$("#"+fjid).remove();
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

function modviewreceipt(id){
	$("#receiptview").hide();
	$("#addreceipt").show();
	var access =$("#ac_tk").val();
	$.ajax({
		type : 'post',
		url : $("#receiptForm #urlPrefix").val()+"/contract/contract/modViewReceiptContract",
		data : {
			"htfpid" : id,
			"access_token" : $("#ac_tk").val()
		},
		dataType : 'json',
		success:function(map){
			 $("#receipt_ajaxForm #htfpid").val(map.htfpqkDto.htfpid);
	    	 $("#receipt_ajaxForm #fphm").val(map.htfpqkDto.fphm);
	    	 $("#receipt_ajaxForm #sl").val(map.htfpqkDto.sl);
	    	 $("#receipt_ajaxForm #formAction").val(map.formAction);
	    	 $("#receipt_ajaxForm #fpje").val(map.htfpqkDto.fpje);
	    	 $("#receipt_ajaxForm #fpzl").val(map.htfpqkDto.fpzlmc);
	    	 $("#receipt_ajaxForm #fpjsrq").val(map.htfpqkDto.fpjsrq);
	    	 $("#receipt_ajaxForm #zjcwrq").val(map.htfpqkDto.zjcwrq);
	    	 $("#receipt_ajaxForm #fpzlmc").val(map.htfpqkDto.fpzlmc);
	    	 $("#receipt_ajaxForm #bhsje").val(map.htfpqkDto.bhsje);
	    	 $("#receipt_ajaxForm #kprq").val(map.htfpqkDto.kprq);
	    	 $("#receipt_ajaxForm #fpdm").val(map.htfpqkDto.fpdm);
	    	 $("#receipt_ajaxForm #czr").val(map.htfpqkDto.czr);
			 $("#receipt_ajaxForm #fjids").val(map.htfpqkDto.fjids);
			 $("#receipt_ajaxForm #ywlx").val(map.htfpqkDto.ywlx);
	    	 $("#receipt_ajaxForm #"+map.htfpqkDto.fpzl).prop("selected","selected");
	    	 $("#receipt_ajaxForm #"+map.htfpqkDto.fpzl).trigger("chosen:updated");
	    	 if ( map.fjcfbDtos.length>0 && map.fjcfbDtos!=null){
				 Load(map);
			 }
	    }
	});
}
function Load(map){
	let html = "";
	let fjcfbDtos = map.fjcfbDtos;
	$.each( fjcfbDtos,function (index,value) {
		html+= "<div id='"+value.fjid+"' >\n" ;
		html+=	"<label class='col-sm-1 control-label' text=''>附件：</label>\n" ;
		html+=	"<div class='col-sm-11 padding_t7'>\n" ;
		html+=	"<label text=''>"+(index+1)+".</label>\n" ;
		html+=	"<button style='outline:none;margin-bottom:5px;padding:0px;' class='btn btn-link' type='button' title='下载' onclick='xz(\""+value.fjid+"\")'>\n" ;
		html+=	"<span text=''>"+value.wjm+"</span>\n" ;
		html+=	"<span class='glyphicon glyphicon glyphicon-save'></span>\n" ;
		html+=	"</button>\n" ;
		html+=	"<button title='删除' class='f_button' type='button'>\n" ;
		html+=	"<span class='glyphicon glyphicon-remove' onclick='del(\""+value.fjid+"\",\""+value.wjlj+"\")'></span>\n" ;
		html+=	"</button>\n" ;
		html+=	"<button title='预览' class='f_button' type='button'>\n" ;
		html+=	"<span class='glyphicon glyphicon-eye-open' onclick='view(\""+value.fjid+"\",\""+value.wjm+"\")'></span>\n" ;
		html+=	"</button>\n" ;
		html+=	"</div>\n" ;
		html+=	"</div>\n";
	})
	$("#fjnr").append(html);

}

function delreceipt(id,fkje){
			var access =$("#ac_tk").val();
			$.ajax({
				type : 'post',
				url : $("#receiptForm #urlPrefix").val()+"/contract/contract/delSaveReceiptContract",
				data : {
					"htfpid" : id,
					"htid" : $("#receiptForm #htid").val(),
					"access_token" : $("#ac_tk").val()
				},
				dataType : 'json',
				success:function(map){
					//返回值
			    	if(map.status=="success"){
				    	 $("#receiptForm #yfje").html(map.yfje);
				    	 $("#receiptForm #htfkbfb").html(map.fkbfb);
				    	 $.success(map.message);
			    		 receiptResult();
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
function receiptDealById(id,htid,action,tourl){
	if(!tourl){
		return;
	}
}

var receipt_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
  	
    };

    return oInit;
};

/**
 * 请购附件上传回调
 * @param fjid
 * @returns
 */
function fpdisplayUpInfo(fjid){
	if(!$("#receipt_ajaxForm #fjids").val()){
		$("#receipt_ajaxForm #fjids").val(fjid);
	}else{
		$("#receipt_ajaxForm #fjids").val($("#receipt_ajaxForm #fjids").val()+","+fjid);
	}
}


$(function(){
	//初始化列表
	var oTable=new contract_mx_TableInit();
	oTable.Init();

	//添加日期控件
	laydate.render({
	   elem: '#receipt_ajaxForm #fpjsrq'
	  ,theme: '#2381E9'
	});	 
	laydate.render({
	   elem: '#receipt_ajaxForm #zjcwrq'
	  ,theme: '#2381E9'
	});	
	laydate.render({
	   elem: '#receipt_ajaxForm #kprq'
	  ,theme: '#2381E9'
	});
	//0.界面初始化

	var oFileInput = new FileInput();
	var sign_params=[];
	sign_params.prefix=$('#receipt_ajaxForm #urlPrefix').val();
	oFileInput.Init("receipt_ajaxForm","fpdisplayUpInfo",2,1,"pro_file",null,sign_params);
    //2.初始化Button的点击事件
    var oButtonInit = new receipt_ButtonInit();
    oButtonInit.Init(); 
	// 所有下拉框添加choose样式
	jQuery('#receipt_ajaxForm .chosen-select').chosen({width: '100%'});
	
});