var Purchase_turnOff=true;

var UnconfirmPurchaseAdministratio_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#unconfirmPurchaseAdministration_formSearch #purchase_list").bootstrapTable({
			url: $("#unconfirmPurchaseAdministration_formSearch #urlPrefix").val()+'/administration/purchase/pageGetListUnconfirmPurchaseAdministration',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#unconfirmPurchaseAdministration_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "qgmx.hwmc",				//排序字段
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
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "qgmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
				width:'4%'
            },{
            	title: '序号',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'hwmc',
                title: '货物名称',
                width: '10%',
                align: 'left',
                sortable: true,   
                visible: true
            },{
				field: 'hwgg',
				title: '货物规格',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'sl',
				title: '货物数量',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'hwjldw',
				title: '货物单位',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			},{
                field: 'qgdh',
                title: '请购单号',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqbmmc',
                title: '申请部门',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
				field: 'qgsqrmc',
				title: '申请人',
				width: '10%',
				align: 'left',
				sortable: true,
				visible:true
			},{
                field: 'sqrq',
                title: '申请日期',
                width: '10%',
                align: 'left',
                sortable: true,   
                visible: true
            },{
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '5%',
				align: 'left',
				formatter:confirm_czFormat,
				visible: true
			}],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	UnconfirmPurchaseAdministrationDealById(row.qgmxid,'view',$("#unconfirmPurchaseAdministration_formSearch #btn_view").attr("tourl"));
             },
		});
		$("#unconfirmPurchaseAdministration_formSearch #purchase_list").colResizable({
			liveDrag:true, 
			gripInnerHtml:"<div class='grip'></div>", 
			draggingClass:"dragging", 
			resizeMode:'fit', 
			postbackSafe:true,
			partialRefresh:true
		})
	}
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "qggl.sqrq", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return UnconfirmPurchaseAdministrationSearchData(map);
	}
	return oTableInit
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function confirm_czFormat(value,row,index) {
	var idqgmxids = $("#unconfirmPurchaseAdministration_formSearch #idqgmxids").val().split(",");
	if($("#unconfirmPurchaseAdministration_formSearch #btn_confirm").length>0){
		for(var i=0;i<idqgmxids.length;i++){
			if(row.qgmxid==idqgmxids[i]){
				return "<div id='qgmxid"+row.qgmxid+"'><span id='t"+row.qgmxid+"' class='btn btn-danger' title='移出确认车' onclick=\"delConfirmCar('" + row.qgmxid + "',event)\" >取消</span></div>";
			}
		}
		return "<div id='qgmxid"+row.qgmxid+"'><span id='t"+row.qgmxid+"' class='btn btn-info' title='加入确认车' onclick=\"addConfirmCar('" + row.qgmxid + "',event)\" >确认</span></div>";
	}
	return "";
}

//添加至确认车
function addConfirmCar(qgmxid,event){
	var sfyhcgqx=$("#unconfirmPurchaseAdministration_formSearch #btn_confirm");
	if(sfyhcgqx.length==0){
		$.confirm("很抱歉,您没有此权限!");
	}else{
		$.ajax({
			type:'post',
			url:$('#unconfirmPurchaseAdministration_formSearch #urlPrefix').val()+"/administration/purchase/pagedataSaveConfirmCar",
			cache: false,
			data: {"qgmxid":qgmxid,"access_token":$("#ac_tk").val()},
			dataType:'json',
			success:function(data){
				//返回值
				if(data.status=='success'){
					$("#idqgmxids").val(data.idqgmxids);
					ConfirmCar_addOrDelNum("add");
					$("#unconfirmPurchaseAdministration_formSearch #t"+qgmxid).remove();
					var html="<span id='t"+qgmxid+"' class='btn btn-danger' title='移出确认车' onclick=\"delConfirmCar('" + qgmxid + "',event)\" >取消</span>";
					$("#unconfirmPurchaseAdministration_formSearch #qgmxid"+qgmxid).append(html);
				}else{
					$.confirm(data.message);
				}
			}
		});
	}
}


//从确认车中删除
function delConfirmCar(qgmxid,event){
	var sfyhcgqx=$("#unconfirmPurchaseAdministration_formSearch #btn_confirm");
	if(sfyhcgqx.length==0){
		$.confirm("很抱歉,您没有此权限!");
	}else{
		$.ajax({
			type:'post',
			url:$('#unconfirmPurchaseAdministration_formSearch #urlPrefix').val()+"/administration/purchase/pagedataDelConfirmCar",
			cache: false,
			data: {"qgmxid":qgmxid,"access_token":$("#ac_tk").val()},
			dataType:'json',
			success:function(data){
				//返回值
				if(data.status=='success'){
					$("#idqgmxids").val(data.idqgmxids);
					ConfirmCar_addOrDelNum("del");
					$("#unconfirmPurchaseAdministration_formSearch #t"+qgmxid).remove();
					var html="<span id='t"+qgmxid+"' class='btn btn-info' title='加入确认车' onclick=\"addConfirmCar('" + qgmxid + "',event)\" >确认</span>";
					$("#unconfirmPurchaseAdministration_formSearch #qgmxid"+qgmxid).append(html);
				}
			}
		});
	}
}

var qr_count=0;
function btnOinit(){
	if(qr_count>0){
		var strnum=qr_count;
		if(qr_count>99){
			strnum='99+';
		}
		var html="";
		html+="<span id='qr_num' style='position:absolute;background-color:red;color:white;border-radius: 15px;border:0px;width:20px;height:20px;top:-10px;font-size:10px;line-height:150%;'>"+strnum+"</span>";
		$("#unconfirmPurchaseAdministration_formSearch #btn_confirm").append(html);
	}else{
		$("#unconfirmPurchaseAdministration_formSearch #qr_num").remove();
	}
}
/**
 * 入库车数字加减
 * @param sfbj
 * @returns
 */
function ConfirmCar_addOrDelNum(sfbj){
	if(sfbj=='add'){
		qr_count=parseInt(qr_count)+1;
	}else{
		qr_count=parseInt(qr_count)-1;
	}
	if((qr_count==1 && sfbj=='add') || (qr_count==0 && sfbj=='del')){
		btnOinit();
	}
	$("#qr_num").text(qr_count);
}

function UnconfirmPurchaseAdministrationSearchData(map){
	var cxtj=$("#unconfirmPurchaseAdministration_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#unconfirmPurchaseAdministration_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr;
	}else if(cxtj=="1"){
		map["djh"]=cxnr
	}else if(cxtj=="2"){
		map["hwmc"]=cxnr
	}else if(cxtj=="3"){
		map["sqbmmc"]=cxnr
	}
	// 申请开始日期
	var sqrqstart = jQuery('#unconfirmPurchaseAdministration_formSearch #sqrqstart').val();
	map["sqrqstart"] = sqrqstart;
	// 申请结束日期
	var sqrqend = jQuery('#unconfirmPurchaseAdministration_formSearch #sqrqend').val();
	map["sqrqend"] = sqrqend;
	return map;
}

var UnconfirmPurchaseAdministratio_ButtonInit= function (){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#unconfirmPurchaseAdministration_formSearch #btn_query");
		var btn_confirm = $("#unconfirmPurchaseAdministration_formSearch #btn_confirm");//确认
		var btn_view = $("#unconfirmPurchaseAdministration_formSearch #btn_view");//确认
        //添加日期控件
    	laydate.render({
    	   elem: '#unconfirmPurchaseAdministration_formSearch #sqrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#unconfirmPurchaseAdministration_formSearch #sqrqend'
    	  ,theme: '#2381E9'
    	});
		/*--------------------------------模糊查询---------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchUnconfirmPurchaseAdministratioResult(true);
    		});
		}
		/* ------------------------------查看请购信息-----------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row=$('#unconfirmPurchaseAdministration_formSearch #purchase_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				UnconfirmPurchaseAdministrationDealById(sel_row[0].qgmxid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*-----------------------确认--------------------------------------*/
		btn_confirm.unbind("click").click(function(){
			if (qr_count>0)
				UnconfirmPurchaseAdministrationDealById(null,"confirm",btn_confirm.attr("tourl"));
		});
		/*-----------------------显示隐藏------------------------------------*/
    	$("#unconfirmPurchaseAdministration_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(Purchase_turnOff){
    			$("#unconfirmPurchaseAdministration_formSearch #searchMore").slideDown("low");
    			Purchase_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#unconfirmPurchaseAdministration_formSearch #searchMore").slideUp("low");
    			Purchase_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});
	}
	return oInit;
}

function UnconfirmPurchaseAdministrationDealById(id,action,tourl){
	var url= tourl;
	if(!tourl){
		return;
	}
	tourl = $("#unconfirmPurchaseAdministration_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl+"?qgmxid="+id;
		$.showDialog(url,'查看请购信息',viewUnconfirmPurchaseAdministratioConfig);
	}else if(action =='confirm'){
		var url= tourl;
		$.showDialog(url,'请购确认',confirmPurchaseXZConfig);
	}
}

/**
 * 查看页面模态框
 */
var viewUnconfirmPurchaseAdministratioConfig={
	width		: "1600px",
	modalName	:"viewUnconfirmPurchaseAdministratioModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}

/**
 * 确认页面模态框
 */
var confirmPurchaseXZConfig={
	width		: "1200px",
	modalName	:"confirmPurchaseXZModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#xzComfirmCarForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				qr_count=0;
				btnOinit();
				var $this = this;
				var opts = $this["options"]||{};
				$("#xzComfirmCarForm #qgqrmx_json").val(JSON.stringify($("#xzComfirmCarForm #qrmx_list").bootstrapTable("getData")));
				$("#xzComfirmCarForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"xzComfirmCarForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								//新增提交审
								var auditType = $("#xzComfirmCarForm #auditType").val();
								var dzfsid=$("#xzComfirmCarForm #dzfs").val();
								var dzfsdm=$("#xzComfirmCarForm #"+dzfsid).attr("csdm");
								var ywid = responseText["ywid"];
								if(dzfsdm=='PTP'){
									auditType=$("#xzComfirmCarForm #auditTypePTP").val();
								}
								var xzconfirm_params=[];
								xzconfirm_params.prefix=$('#xzComfirmCarForm #urlPrefix').val();
								showAuditFlowDialog(auditType,ywid,function(){
									$.closeModal(opts.modalName);
									searchUnconfirmPurchaseAdministratioResult();
								},null,xzconfirm_params);
								$.closeModal(opts.modalName);
							}
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
}

/**
 * 列表刷新
 * @param isTurnBack
 * @returns
 */
function searchUnconfirmPurchaseAdministratioResult(isTurnBack){
	//关闭高级搜索条件
	$("#unconfirmPurchaseAdministration_formSearch #searchMore").slideUp("low");
	Purchase_turnOff=true;
	$("#unconfirmPurchaseAdministration_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#unconfirmPurchaseAdministration_formSearch #purchase_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#unconfirmPurchaseAdministration_formSearch #purchase_list').bootstrapTable('refresh');
	}
}

$(function(){
	 // 1.初始化Table
	var oTable = new UnconfirmPurchaseAdministratio_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new UnconfirmPurchaseAdministratio_ButtonInit();
    oButtonInit.Init();
	if($("#qrcsl").val()==null || $("#qrcsl").val()==''){
		qr_count=0;
	}else{
		qr_count=$("#qrcsl").val();
	}
	btnOinit();
    jQuery('#unconfirmPurchaseAdministration_formSearch .chosen-select').chosen({width: '100%'});
})