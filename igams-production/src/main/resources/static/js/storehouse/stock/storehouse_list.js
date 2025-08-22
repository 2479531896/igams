var Storehouse_turnOff=true;
var storehouse_TableInit = function () {
var oTableInit = new Object();	    
	// 初始化Table
	oTableInit.Init = function () {
		$('#storehouse_formSearch #storehouse_list').bootstrapTable({
			url: $("#storehouse_formSearch #urlPrefix").val()+'/storehouse/storehouse/pageGetListStorehouse',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#storehouse_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"wlbm",					// 排序字段
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
			uniqueId: "hwid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮                 								
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true,
				width: '1%'
			},{
            	title: '序号',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '2%',
                align: 'left',
                visible:true
            },{
				field: 'wlbm',
				title: '物料编码',
				width: '4%',
				align: 'left',
				formatter:kcwlbmformat,
				sortable: true,
				visible: true
			}, {				
				field: 'wlmc',
				title: '物料名称',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
                field: 'scph',
                title: '生产批号',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zsh',
                title: '追溯号',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
				field: 'ckmc',
				title: '仓库',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'ychh',
				title: '货号',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{				
				field: 'scs',
				title: '生产商',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{				
				field: 'gg',
				title: '规格',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'jldw',
				title: '单位',
				width: '2%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'kcl',
				title: '库存量',
				width: '4%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'lb',
				title: '物料类别',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: false
			},{				
				field: 'zlb',
				title: '物料子类别',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'kczt',
				title: '库存',
				width: '4%',
				align: 'left',
				sortable: true,
				formatter:kcztFormat,
				visible: true
			},{
		        field: 'll',
		        title: '领料',
		        width: '4%',
		        align: 'left',
		        formatter:stock_formSearch_llformat,
		        visible: true
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				stockById(row.hwid,'view',$("#storehouse_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#storehouse_formSearch #storehouse_list").colResizable({
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
            sortLastName: "hwxx.hwid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
			jsid: $("#storehouse_formSearch #jsid").val()
            // 搜索框使用
            // search:params.search
        };
		return stockSearchData(map);
	}
	return oTableInit
}


function kcwlbmformat(value,row,index){
    var html = "";
    if(row.wlbm==null){
        html += "<span></span>"
    }else{
        html += "<a href='javascript:void(0);' onclick=\"queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
    }
    return html;
}
function queryByWlbm(wlid){
    var url=$("#storehouse_formSearch #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料信息',viewWlConfig);
}

var viewWlConfig = {
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

function kcztFormat(value,row,index){
	if(row.kcl == null||row.kcl == ''){
		row.kcl = 0;
	}
	if(row.kcl>0){
		var html="<span style='color:green;'>"+"库存充足"+"</span>";
	}else{
		var html="<span style='color:red;'>"+"库存不足"+"</span>";
	}
	return html;
}

/**
 * 领料车操作格式化
 * @returns
 */
function stock_formSearch_llformat(value,row,index) {
    if (row.kcl>0){
        return "<div><span class='btn btn-info' title='领料' onclick=\"yq('" + row.hwid + "',event)\" >领料</span></div>";
    }
    return "";
}


function yq(hwid,event){
    var url = $("#storehouse_formSearch #urlPrefix").val() + "/storehouse/storehouse/pagedataLl?hwid=" + hwid;
    $.showDialog(url,'领料',storehouseLlConfig);
}
var storehouseLlConfig={
    width		: "1200px",
    modalName	: "storehouseLlModal",
    formName	: "storehouseLlForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#storehouseLlForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#storehouseLlForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"storehouseLlForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                stockResult ();
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
};
// 根据查询条件查询
function stockSearchData(map){
	var cxtj=$("#storehouse_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#storehouse_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr;
	}else if(cxtj=="1"){
		map["wlmc"]=cxnr;
	}else if(cxtj=="2"){
		map["wlbm"]=cxnr;
	}else if(cxtj=="3"){
        map["zsh"]=cxnr;
    }else if(cxtj=="4"){
        map["scph"]=cxnr;
    }
	// 高级筛选
	var kczt = jQuery('#storehouse_formSearch #kczt_id_tj').val();
	map["kczt"] = kczt;
	// 仓库
	var cks = jQuery('#storehouse_formSearch #ck_id_tj').val();
	map["cks"] = cks.replace(/'/g, "");;
	return map;
}


//根据仓库货物ID查询仓库信息
function stockById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#storehouse_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?hwid=" +id;
		$.showDialog(url,'库存详细信息',viewstockConfig);
	}
}

var viewstockConfig = {
	width		: "1600px",
	modalName	: "viewstockModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	} 
};

var storehouse_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#storehouse_formSearch #btn_view");
		var btn_query = $("#storehouse_formSearch #btn_query");
		/*--------------------------------模糊查询---------------------------*/    	
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			stockResult(true);
    		});
    	}
  
       /* ---------------------------查看仓库货物-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#storehouse_formSearch #storehouse_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			stockById(sel_row[0].hwid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/*-----------------------显示隐藏------------------------------------*/
    	$("#storehouse_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(Storehouse_turnOff){
    			$("#storehouse_formSearch #searchMore").slideDown("low");
    			Storehouse_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#storehouse_formSearch #searchMore").slideUp("low");
    			Storehouse_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
    	
    };
    	return oInit;
};

    
function stockResult(isTurnBack){
	//关闭高级搜索条件
	$("#storehouse_formSearch #searchMore").slideUp("low");
	Storehouse_turnOff=true;
	$("#storehouse_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#storehouse_formSearch #storehouse_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#storehouse_formSearch #storehouse_list').bootstrapTable('refresh');
	}
}

var storehouse_PageInit = function(){
	var oInit = new Object();
	var postdata = {};
	oInit.Init = function () {
		var kczt = $("#storehouse_formSearch a[id^='kczt_id_']");
		$.each(kczt, function(i, n){
			var id = $(this).attr("id");
			var code = id.substring(id.lastIndexOf('_')+1,id.length);
			if(code === '1'){
				addTj('kczt',code,'storehouse_formSearch');
			}
		});
	}
	return oInit;
}
$(function(){
	var oInit = new storehouse_PageInit();
	oInit.Init();
	// 1.初始化Table
	var oTable = new storehouse_TableInit();
	oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new storehouse_ButtonInit();
    oButtonInit.Init();

	// 所有下拉框添加choose样式
	jQuery('#storehouse_formSearch .chosen-select').chosen({width: '100%'});
	
	$("#storehouse_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	
});