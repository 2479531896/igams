
var un_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#unsamp_formSearch #tb_untreatedsampsrc').bootstrapTable({
            url: '/sample/stock/pageGetListUntreatedSampSrc',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#unsamp_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
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
            uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
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
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'lybh',
                title: '来源编号',
                width: '10%',
                align: 'left',
                sortable: true,
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
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'lyd',
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
                formatter:unsexFormatter,
            },{
                field: 'yz',
                title: '孕周',
                width: '6%',
                align: 'right',
                sortable: true,
                visible: true
            },{
                field: 'tz',
                title: '体重',
                width: '6%',
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
                width: '6%',
                align: 'left',
                formatter:unztFormatter,
                visible: true,
            },{
                field: 'lrsj',
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
            	var url= $("#unsamp_formSearch #btn_deal").attr("tourl") + "?lyid=" +row.lyid;
        		$.showDialog(url,'标本处理登记',addYbConfig);
            },
        });
        $("#unsamp_formSearch #tb_untreatedsampsrc").colResizable({
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

    	var cxtj = $("#unsamp_formSearch #cxtj").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#unsamp_formSearch #cxnr').val());
    	// '0':'来源编号','1':'外部编号','2':'孕周','3':'体重','4':'备注'
    	if (cxtj == "0") {
    		map["lybh"] = cxnr;
    	}else if (cxtj == "1") {
    		map["wbbh"] = cxnr;
    	}else if (cxtj == "2") {
    		map["yz"] = cxnr;
    	}else if (cxtj == "3") {
    		map["tz"] = cxnr;
    	}else if (cxtj == "4") {
    		map["bz"] = cxnr;
    	}
    	return map;
    };
    return oTableInit;
};

//性别格式化
function unsexFormatter(value, row, index) {
    if (row.xb == '1') {
        return '男';
    }
    else if (row.xb == '2') {
        return '女';
    }else{
    	return "";
    }
}

function unztFormatter(value, row, index) {
    if (row.zt == '80') {
        return '已处理';
    }else if(row.zt == '15'){
    	return '已废弃';
    }else{
    	return '未处理';
    }
}


var addYbConfig = {
	width		: "1200px",
	modalName	: "addSpModal",
	formName	: "Samp_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				
				if(!$("#Samp_ajaxForm").valid()){
					return false;
				}
				var cskz2=$("#sp_yblx option:selected").attr("cskz2");
				var checkhzlength=$('input[name="hzid"]:checked').length;
				var hzids="";
				if(cskz2=='1'){
					if(yxhzsl!=checkhzlength){
						$.error("标本类型为企参盘的标本数需等于选中盒子数",function() {
						});
						return false;
					}else{
						for(var i=0;i<checkhzlength;i++){
							hzids=hzids+","+$('input[name="hzid"]:checked').next()[i].id;
						}
						hzids=hzids.substr(1);
						$("#hzids").val(hzids);
					}
				}
				var trs =$("#sample_reg_table").find("tr");
				var size = trs.length;
				if(size ==0){
					$.error("请添加标本信息！");
					return false;
				}
				var myhzs = {};
				for(let i=0;i<trs.length;i++){
					var vhz = $(trs[i]).find("#h_hz")[0];
					if(myhzs[vhz.value]){
				    	$.error("第" + (i+1) + "行盒子有重复。");
				        return false;
				    }
					myhzs[vhz.value] = true;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				
				$("input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								unsearchResult();
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

var un_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_add = $("#unsamp_formSearch #btn_deal");
    	var btn_discard = $("#unsamp_formSearch #btn_discard");
    	//查询按钮
    	var btn_spsource_query = $("#btn_unspsource_query");
    	
    	btn_add.unbind("click").click(function(){
    		var sel_row = $('#unsamp_formSearch #tb_untreatedsampsrc').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			var url= btn_add.attr("tourl") + "?lyid=" +sel_row[0].lyid;
        		$.showDialog(url,'标本处理登记',addYbConfig);
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_discard.unbind("click").click(function(){
    		var sel_row = $('#unsamp_formSearch #tb_untreatedsampsrc').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			if(sel_row[i].zt == '80'){
    				$.error("存在已处理标本，不允许废弃！");
    				return;
    			}
    			ids = ids + ","+ sel_row[i].lyid;
    		}
    		ids = ids.substr(1);
    		$.confirm('您确定要废弃所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= btn_discard.attr("tourl");
    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								unsearchResult();
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
    	//绑定搜索发送功能
    	if(btn_spsource_query != null){
    		btn_spsource_query.unbind("click").click(function(){
    			unsearchResult();
    		});
    	}
    };

    return oInit;
};

/**
 * 检索
 */
function unsearchResult(){
	$('#unsamp_formSearch #tb_untreatedsampsrc').bootstrapTable('refresh');
}

$(function(){
    //1.初始化Table
    var oTable = new un_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new un_ButtonInit();
    oButtonInit.Init();
	//所有下拉框添加choose样式
	jQuery('#unsamp_formSearch .chosen-select').chosen({width: '100%'});
	
});
