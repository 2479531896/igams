var ChooseContract_turnOff=true;

var columnsArray = [
	{
	    checkbox: true,
		width: '5%'
	},{
		title: '序',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序',
		width: '5%',
	    align: 'left',
	    visible:true
	},{
	    field: 'htnbbh',
	    title: '合同内部编号',
	    width: '25%',
	    align: 'left',
	    sortable: true,   
	    visible: true
	},{
	    field: 'fzrmc',
	    title: '负责人',
	    width: '10%',
	    align: 'left',
	    visible:true
	},{
	    field: 'gysmc',
	    title: '供应商',
	    width: '25%',
	    align: 'left',
	    sortable: true,   
	    visible: true
	}, {				
		field: 'cjrq',
		title: '创建日期',
		width: '15%',
		align: 'left',
		visible: true
	},{
		field: 'cz',
		title: '操作',
		titleTooltip:'操作',
		width: '15%',
		align: 'left',
		formatter:czFormat,
		visible: true
	}]
var ChooseContract_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
        var htlx = $("#chooseHtxxForm #htlx").val();
        var cghzbj = $("#chooseHtxxForm #cghzbj").val();
        $("#chooseHtxxForm #contract_list").bootstrapTable({
			url: $("#chooseHtxxForm input[name='urlPrefix']").val() + '/contract/contract/pagedataAllContract?htlx='+htlx+'&cghzbj='+cghzbj,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#chooseHtxxForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "htgl.lrsj",				//排序字段
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
            uniqueId: "htid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onClickRow: function (row, $element) {
            	if($("#chooseHtxxForm #cz_"+row.htid).text()){
            		if($("#chooseHtxxForm #cz_"+row.htid).text() != "-"){
            			$("#chooseHtxxForm #cz_"+row.htid).removeAttr("class");
                		$("#chooseHtxxForm #cz_"+row.htid).text("");
            		}
            	}else{
            		$("#chooseHtxxForm #cz_"+row.htid).attr("class","btn btn-success");
                	$("#chooseHtxxForm #cz_"+row.htid).text("调整明细");
                	// 判断是否更新请购信息
                	var htmxJson = [];
                	var refresh = true;
    				if($("#chooseHtxxForm #htmx_json").val()){
    					htmxJson = JSON.parse($("#chooseHtxxForm #htmx_json").val());
    					if(htmxJson.length > 0){
        					for (var i = htmxJson.length-1; i >= 0; i--) {
        						if(htmxJson[i].htid == row.htid){
        							refresh = false;
        							break;
        						}
        					}
        				}
    				}
    				if(refresh){
    					var dhid = $("#arrivalGoodsEditForm #dhid").val();
						var url = "/contract/contract/pagedataGetcontractinfo";
						if (cghzbj=="1"){
							url = "/contract/contract/pagedataGetHzInfo";
						}
    					// 查询请购明细信息
    					$.ajax({
    					    type:'post',  
    					    url: $("#chooseHtxxForm input[name='urlPrefix']").val() + url,
    					    cache: false,
    					    data: {"htid":row.htid, "dhid":dhid, "access_token":$("#ac_tk").val()},  
    					    dataType:'json',
    					    success:function(result){
								var flag = true;
    					    	if(result.hwxxDtos != null && result.hwxxDtos.length > 0){
    					    		for (var i = 0; i < result.hwxxDtos.length; i++) {
										if (result.hwxxDtos[i].hzt=='0'&&cghzbj!="1"){
											flag = false;
										}else {
											var sl = 0;
											if(result.hwxxDtos[i].sl){
												sl = result.hwxxDtos[i].sl;
											}
											var sz = {"htid":'',"htmxid":''};
											sz.htid = result.hwxxDtos[i].htid;
											sz.htmxid = result.hwxxDtos[i].htmxid;
											htmxJson.push(sz);
										}
									}
    					    		$("#chooseHtxxForm #htmx_json").val(JSON.stringify(htmxJson));
									// 判断是否更新订单号
									if(flag){
										$("#chooseHtxxForm  #yxdjh").tagsinput('add',{"value":row.htid,"text":row.htnbbh});
									}else{
										$("#chooseHtxxForm #cz_"+row.htid).removeAttr("class");
										$("#chooseHtxxForm #cz_"+row.htid).text("-");
										htmxJson = [];
										$("#chooseHtxxForm #htmx_json").val(JSON.stringify(htmxJson));
										$.alert("合同被关闭不允许被引用！");
									}
    					    	}
    					    }
    					});
    				}
            	}
            },
            onDblClickRow: function (row, $element) {
            	return;
             },
		});
	}
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "htgl.htid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return chooseContractSearchData(map);
	}
	return oTableInit
}
function chooseContractSearchData(map){
	var cxtj=$("#chooseHtxxForm #cxtj").val();
	var cxnr=$.trim(jQuery('#chooseHtxxForm #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr;
	}else if(cxtj=="1"){
		map["htnbbh"]=cxnr;
	}else if(cxtj=="2"){
		map["htwbbh"]=cxnr;
	}else if (cxtj == "3") {
    	map["gys"] = cxnr;
	}else if (cxtj == "4") {
    	map["fzr"] = cxnr;
	}else if (cxtj == "5") {
    	map["sqbm"] = cxnr;
	}else if (cxtj == "6") {
    	map["wlbm"] = cxnr;
	}else if (cxtj == "7") {
    	map["wlmc"] = cxnr;
	}else if (cxtj == "8") {
    	map["gg"] = cxnr;
	}
	// 双章标记
	var szbj = jQuery('#chooseHtxxForm #szbj_id_tj').val();
	map["szbj"] = szbj;
	
	// 发票方式
	var fpfs = jQuery('#chooseHtxxForm #fpfs_id_tj').val();
	map["fpfss"] = fpfs;
	
	// 付款方式
	var fkfs = jQuery('#chooseHtxxForm #fkfs_id_tj').val();
	map["fkfss"] = fkfs;
	
	// 创建开始日期
	var shsjstart = jQuery('#chooseHtxxForm #shsjstart').val();
	map["shsjstart"] = shsjstart;
	
	// 创建结束日期
	var shsjend = jQuery('#chooseHtxxForm #shsjend').val();
	map["shsjend"] = shsjend;
	
	// 总金额最小值
	var zjemin = jQuery('#chooseHtxxForm #zjemin').val();
	map["zjemin"] = zjemin;
	
	// 总金额最大值
	var zjemax = jQuery('#chooseHtxxForm #zjemax').val();
	map["zjemax"] = zjemax;
	
	// 删除标记
	var scbjs = jQuery('#chooseHtxxForm #scbj_id_tj').val();
	map["scbjs"] = scbjs;
	
	// 完成标记
	var wcbj = jQuery('#chooseHtxxForm #wcbj_id_tj').val();
	map["wcbj"] = wcbj;
	
	return map;
}
function searchChooseContractResult(isTurnBack){
	//关闭高级搜索条件
	$("#chooseHtxxForm #searchMore").slideUp("low");
	ChooseContract_turnOff=true;
	$("#chooseHtxxForm #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#chooseHtxxForm #contract_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#chooseHtxxForm #contract_list').bootstrapTable('refresh');
	}
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
	return "<span id='cz_"+row.htid+"' onclick=\"reviseDetail('" + row.htid + "',event)\"/>";
}
/**
 * 调整明细点击事件
 * @param htid
 * @param event
 * @returns
 */
function reviseDetail(htid, event){
	if($("#cz_"+htid).text() == "-"){
		return false;
	}
	var cghzbj = $("#chooseHtxxForm #cghzbj").val();
	event.stopPropagation();
	var url = $("#chooseHtxxForm input[name='urlPrefix']").val() + "/contract/contract/pagedataChooseListContractInfo?cghzbj="+cghzbj+"&+access_token=" + $("#ac_tk").val() + "&htid=" + htid;
	$.showDialog(url, '选择合同明细', chooseHtmxConfig);
}
var chooseHtmxConfig = {
	width : "1000px",
	modalName	: "chooseHtmxModal",
	formName	: "chooseHtmxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#chooseHtmxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				// 获取选中明细并保存
				if($("#chooseHtmxForm input[name='checkHtmx']:checked").length == 0){
					$.alert("未选中合同明细信息！");
					return false;
				}
				var htmxJson = [];
				if($("#chooseHtxxForm #htmx_json").val()){
					htmxJson = JSON.parse($("#chooseHtxxForm #htmx_json").val());
				}
				var htid = $("#chooseHtmxForm input[name='htid']").val();
				if(htmxJson.length > 0){
					for (var i = htmxJson.length-1; i >= 0; i--) {
						if(htmxJson[i].htid == htid){
							htmxJson.splice(i,1);
						}
					}
				}
				$("#chooseHtmxForm input[name='checkHtmx']").each(function(){
					if($(this).is(":checked")){
						var sz={"htid":'',"htmxid":''};
						sz.htid = htid;
						sz.htmxid = this.dataset.htmxid;
						htmxJson.push(sz);
					}
				})
				$("#chooseHtxxForm #htmx_json").val(JSON.stringify(htmxJson));
				preventResubmitForm(".modal-footer > button", true);
				$.closeModal(opts.modalName);
				preventResubmitForm(".modal-footer > button", false);
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

/**
 * 初始化页面
 */
var ChooseContract_ButtonInit= function (){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		//添加日期控件
    	laydate.render({
    	   elem: '#chooseHtxxForm #shsjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#chooseHtxxForm #shsjend'
    	  ,theme: '#2381E9'
    	});
    	//初始化已选单据号
    	initTagsinput();
    	
		var btn_query=$("#chooseHtxxForm #btn_query");
		/*--------------------------------模糊查询---------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchChooseContractResult(true); 
    		});
		}
		/*-----------------------显示隐藏------------------------------------*/
    	$("#chooseHtxxForm #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(ChooseContract_turnOff){
    			$("#chooseHtxxForm #searchMore").slideDown("low");
    			ChooseContract_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#chooseHtxxForm #searchMore").slideUp("low");
    			ChooseContract_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
	}
	return oInit;
}

/**
 * 初始化已选订单号
 * @returns
 */
function initTagsinput(){
	$("#chooseHtxxForm  #yxdjh").tagsinput({
		itemValue: "value",
		itemText: "text",
	})
	// 初始化已选明细json
	$("#chooseHtxxForm #djhs").val($("#arrivalGoodsEditForm #htmx_json").val());
}

/**
 * 监听标签点击事件
 */
var tagClick = $("#chooseHtxxForm").on('click','.label-info',function(e){
	event.stopPropagation();
	var url = $("#chooseHtxxForm input[name='urlPrefix']").val() + "/contract/contract/pagedataChooseListContractInfo?access_token=" + $("#ac_tk").val() + "&htid=" + e.currentTarget.dataset.logo;
	$.showDialog(url, '选择合同明细', chooseHtmxConfig);
});
/**
 * 监听标签移除事件
 */
var tagsRemoved = $('#chooseHtxxForm #yxdjh').on('beforeItemRemove', function(event) {
	var htmxJson = [];
	if($("#chooseHtxxForm #htmx_json").val()){
		htmxJson = JSON.parse($("#chooseHtxxForm #htmx_json").val());
	}
	var htid = event.item.value;
	if(htmxJson.length > 0){
		for (var i = htmxJson.length-1; i >= 0; i--) {
			if(htmxJson[i].htid == htid){
				htmxJson.splice(i,1);
			}
		}
	}
	$("#chooseHtxxForm #htmx_json").val(JSON.stringify(htmxJson));
});

$(function(){
	// 1.初始化Table
	var oTable = new ChooseContract_TableInit();
	oTable.Init();
	//2.初始化Button的点击事件
	var oButtonInit = new ChooseContract_ButtonInit();
	oButtonInit.Init();
	jQuery('#chooseHtxxForm .chosen-select').chosen({width: '100%'});
})