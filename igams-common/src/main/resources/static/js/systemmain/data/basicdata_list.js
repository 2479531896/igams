var pre_row = null;
var data_turnOff=true;
//得到查询的参数
data_queryParams = function(params){
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

	var cxbt = $("#dataFormSearch #cxbt").val();
	// 查询条件
	var cxnr = $.trim(jQuery('#dataFormSearch #cxnr').val());
	// '0':'参数代码','1':'参数名称','2':'父参数代码','3':'父参数名称'
	if (cxbt == "0") {
		map["csdm"] = cxnr;
	}else if (cxbt == "1") {
		map["csmc"] = cxnr;
	}else if (cxbt == "2") {
		map["fcsdm"] = cxnr;
	}else if (cxbt == "3") {
		map["fcsmc"] = cxnr;
	}
	if(pre_row!=null){
		map["jclb"]= pre_row.attr("attr_type");

	}
	// 删除标记
	var scbjs = jQuery('#dataFormSearch #scbj_id_tj').val();
	map["scbjs"] = scbjs.replace(/'/g, "");
	return map;
};

var data_baseEvents = {
    url: $("#basicDataSearch #urlPrefix").val()+'/systemmain/data/pageGetListBasicData',         //请求后台的URL（*）
    method: 'get',                      //请求方式（*）
    toolbar: '#dataFormSearch #toolbar', //工具按钮用哪个容器
    striped: true,                      //是否显示行间隔色
    cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
    pagination: true,                   //是否显示分页（*）
    paginationShowPageGo: true,         //增加跳转页码的显示
    sortable: true,                     //是否启用排序
    sortName:"jc.csdm",					//排序字段
    sortOrder: "asc",                   //排序方式
    queryParams: data_queryParams,//传递参数（*）
    sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
    pageNumber:1,                       //初始化加载第一页，默认第一页
    pageSize: 10,                       //每页的记录行数（*）
    pageList: [10, 30, 50, 100],        //可供选择的每页的行数（*）
    paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
    paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
    search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
    strictSearch: true,
    showColumns: true,                  //是否显示所有的列
    showRefresh: true,                  //是否显示刷新按钮
    minimumCountColumns: 2,             //最少允许的列数
    clickToSelect: true,                //是否启用点击选中行
    //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
    uniqueId: "csid",                     //每一行的唯一标识，一般为主键列
    showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
    cardView: false,                    //是否显示详细视图
    detailView: false,                   //是否显示父子表
    columns: [{
        checkbox: true
    }],
    onLoadSuccess: function () {
    },
    onLoadError: function () {
    },
    onDblClickRow: function (row, $element) {
    	dataDealById(row.csid);
    },
};

var data_TableInit = function () {

	var firstrow = $("#dataFormSearch #typedata tbody").find("tr").eq(0);
	
	pre_row = firstrow;
	firstrow.addClass("success");
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
    	loadBasicData(firstrow);
    };

    return oTableInit;
};

var data_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
		var btnAdd=jQuery("#dataFormSearch #btn_add");
		var btnMod=jQuery("#dataFormSearch #btn_mod");
		var btnDel=jQuery("#dataFormSearch #btn_del");
		var btnView=jQuery("#dataFormSearch #btn_view");
		var btnSearch = jQuery("#dataFormSearch #search_go");
		var btnEnable = jQuery("#dataFormSearch #btn_enable");
		var btnDisable = jQuery("#dataFormSearch #btn_disable");
		var btnData = jQuery("#dataFormSearch #btn_data_basic");
		var btnPush = jQuery("#dataFormSearch #btn_push");
		
		//绑定查询按钮事件
		if(btnSearch != null){
			btnSearch.unbind("click").click(function(){
				searchDataResult(true);
				return false;
			});
		}
		
		//绑定增加按钮事件
		if(btnAdd != null){
			btnAdd.unbind("click").click(function(){
				var url = $("#basicDataSearch #urlPrefix").val()+$(this).attr("tourl");
				$.showDialog(
					url,
					btnAdd.attr("gnm"),
					$.extend({},addBasicConfig,{"width":"600px",
						data:{'jclb':pre_row.attr("attr_type"),
						'fcslb':pre_row.attr("attr_p")}})
				);
			});
		}
		
		//绑定修改按钮事件
		if(btnMod != null){
			btnMod.unbind("click").click(function() {
				var sel_row = $('#dataFormSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length != 1){
					$.alert('请选定一条记录!');
					return false;
				}
				var url = $("#basicDataSearch #urlPrefix").val()+$(this).attr("tourl");
				$.showDialog(
					url,
					btnMod.attr("gnm"),
					$.extend({},modBasicConfig,{"width":"600px",
						data:{'csid':sel_row[0].csid,'jclb':pre_row.attr("attr_type")}})
				);
			});
		}
		
		//绑定启用按钮事件
		if(btnEnable != null){
			btnEnable.unbind("click").click(function(){
				var url = $("#basicDataSearch #urlPrefix").val()+$(this).attr("tourl") + "?scbj=0&jclb="+pre_row.attr("attr_type");
				plcz(url,'启用');
			});
		}
		
		//绑定停用按钮事件
		if(btnDisable != null){
			btnDisable.unbind("click").click(function(){
				var url = $("#basicDataSearch #urlPrefix").val()+$(this).attr("tourl") + "?scbj=2&jclb="+pre_row.attr("attr_type");
				plcz(url,'停用');
			});
		}
		
		//绑定删除按钮事件
		if(btnDel != null){
			btnDel.unbind("click").click(function(){
				var url = $("#basicDataSearch #urlPrefix").val()+$(this).attr("tourl") + "?scbj=1&jclb="+pre_row.attr("attr_type");
				plcz(url,'删除');
			});
		}
		//绑定推送按钮事件
		if(btnPush != null){
			btnPush.unbind("click").click(function(){
				var url = $("#basicDataSearch #urlPrefix").val()+$(this).attr("tourl") + "?jclb="+pre_row.attr("attr_type");
				if (pre_row.attr("attr_type")){
					plts(url,'推送');
				}else {
					$.alert('请选择您要推送的类别或者数据！');
				}
			});
		}
		
		//绑定查看按钮事件
		if(btnView != null){
			btnView.unbind("click").click(function () {
				var sel_row = $('#dataFormSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length != 1){
					$.alert('请选定一条记录!');
					return false;
				}
				dataDealById(sel_row[0].csid);
			});
		}
		/**显示隐藏**/
		$("#dataFormSearch #sl_searchMore").on("click", function(ev){
			var ev=ev||event;
			if(data_turnOff){
				$("#dataFormSearch #searchMore").slideDown("low");
				data_turnOff=false;
				this.innerHTML="基本筛选";
			}else{
				$("#dataFormSearch #searchMore").slideUp("low");
				data_turnOff=true;
				this.innerHTML="高级筛选";
			}
			ev.cancelBubble=true;
		});
		function contractResult(isTurnBack){
			//关闭高级搜索条件
			$("#dataFormSearch #searchMore").slideUp("low");
			data_turnOff=true;
			$("#dataFormSearch #sl_searchMore").html("高级筛选");
			if(isTurnBack){
				$('#dataFormSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
			}else{
				$('#dataFormSearch #tb_list').bootstrapTable('refresh');
			}
		}
		
		
		//绑定表格双击事件
		dataDealById = function(id){
			var url = $("#basicDataSearch #urlPrefix").val()+btnView.attr("tourl");
			if(url != null && url != ''){
				$.showDialog(url,btnView.attr("gnm"),$.extend({},viewConfig,{"width":"600px",
						data:{'csid':id+"",
						'cskz1':pre_row.attr("attr_ex1")+pre_row.attr("attr_ex1_sel"),
						'cskz2':pre_row.attr("attr_ex2")+pre_row.attr("attr_ex2_sel"),
						'cskz3':pre_row.attr("attr_ex3")+pre_row.attr("attr_ex3_sel"),
						'cskz4':pre_row.attr("attr_ex4")+pre_row.attr("attr_ex4_sel"),
                        'cskz5':pre_row.attr("attr_ex5")+pre_row.attr("attr_ex5_sel")}}));
			}
		};
		
		tableBind();
    };
    return oInit;
};

var addBasicConfig = {
    width: "900px",
    modalName: "addModal",
    gridName	: "tabGrid",
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
				var $this = this;
                if ($("#ajaxForm").valid()) {
                	$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
                	
                    submitForm("ajaxForm", function (responseText, statusText) {
                        // responseText 可能是 xmlDoc, jsonObj, html, text, 等等...
                        // statusText 	描述状态的字符串（可能值："No Transport"、"timeout"、"notmodified"---304 "、"parsererror"、"success"、"error"
                    	if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								preventResubmitForm(".modal-footer > button", false);
								var jclb = $("#dataFormSearch #jclb option:selected").val();
                            	var jclbmc = $("#dataFormSearch #jclb option:selected").text();
                            	//清除元素数据
								setTimeout(function () { 
									//$($this["document"]).clearElements();
	                            	$($this["document"]).each(function() {
										if($(this).is('input,select,textarea')){
											$(this).clearFields();
											$(this).trigger("chosen:updated");
										}else{
											//筛选要清除数据的元素
											var elements = $(this).find('input,select,textarea');
											//筛选自定义清除数据的元素
											var elementFilters = $(elements).filter('[data-clear="true"]');
											if(elementFilters.length > 0){
												$(elementFilters).clearFields();
												$(elementFilters).trigger("chosen:updated");
											}else{ 
												$(elements).clearFields();
												$(elements).trigger("chosen:updated");
											}

										}
									});
								}, 500);
								//保留基础数据类别
                            	$("#dataFormSearch #jclb").val(jclb);
								$("#dataFormSearch #jclb").text(jclbmc);
								jQuery('#dataFormSearch #jclb').trigger("chosen:updated");
								$('#dataFormSearch #tb_list').bootstrapTable('refresh');
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
							$.alert(responseText["message"],function() {
							});
						}
                    }, ".modal-footer > button");
                } else {
                    
                }
                return false;
            }
        },
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
};


var modBasicConfig = {
    width: "900px",
    modalName: "addModal",
    gridName	: "tabGrid",
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
				var $this = this;
                if ($("#ajaxForm").valid()) {
                	$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
                	
                    submitForm("ajaxForm", function (responseText, statusText) {
                        // responseText 可能是 xmlDoc, jsonObj, html, text, 等等...
                        // statusText 	描述状态的字符串（可能值："No Transport"、"timeout"、"notmodified"---304 "、"parsererror"、"success"、"error"
                    	if(responseText["status"] == 'success'){
                    		preventResubmitForm(".modal-footer > button", false);
							$.success(responseText["message"],function() {
								$.closeModal("addModal");
								$('#dataFormSearch #tb_list').bootstrapTable('refresh');
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
							$.alert(responseText["message"],function() {
							});
						}
                    }, ".modal-footer > button");
                } else {
                    
                }
                return false;
            }
        },
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
};

function loadBasicData(nowrow){
	$("#dataFormSearch #tb_list").bootstrapTable('destroy');
	$("#dataFormSearch #tb_list").bootstrapTable($.extend({},
			getDataTableEvents(nowrow.attr("attr_p")==undefined? false:true,nowrow.attr("attr_ex1")==undefined? false:true,nowrow.attr("attr_ex2")==undefined? false:true,
				nowrow.attr("attr_ex3")==undefined? false:true,nowrow.attr("attr_ex4")==undefined? false:true,nowrow.attr("attr_ex5")==undefined? false:true,nowrow.attr("attr_ex1_sel")==undefined? false:true,
				nowrow.attr("attr_ex2_sel")==undefined? false:true,nowrow.attr("attr_ex3_sel")==undefined? false:true,nowrow.attr("attr_ex4_sel")==undefined? false:true,nowrow.attr("attr_ex5_sel")==undefined? false:true)
		));
	$("#dataFormSearch #tb_list").colResizable({
        liveDrag:true,
        gripInnerHtml:"<div class='grip'></div>",
        draggingClass:"dragging",
        resizeMode:'fit',
        postbackSafe:true,
        partialRefresh:true
    })
}
//查询基础数据
function searchDataResult(isTurnBack) {
	if(isTurnBack){
		$('#dataFormSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#dataFormSearch #tb_list').bootstrapTable('refresh');
	}
}

function searchOther(){
	var cslx = jQuery('#dataFormSearch #cslx').val();
	var firstrow = null;
	var rowcount = 0;
	if(cslx != undefined && cslx!=null && cslx!="")
	{
		jQuery("#dataFormSearch #typedata tr").each(function() {
			
			var txt = $(this).children("td").text().trim();
			if(txt.indexOf(cslx)== -1){
				$(this).hide();
				if(pre_row!=null && $(this).val() ==  pre_row.val()){
					pre_row.removeClass("success");
					pre_row = null;
				}
			}else{
				rowcount ++;
				$(this).show();
				if(firstrow == null){
					firstrow = $(this);
				}
			}
		});
	}else{
		jQuery("#dataFormSearch #typedata tr").each(function() {
			rowcount ++;
			$(this).show();
			if(firstrow == null)
				firstrow = $(this);
		});
	}
	if(rowcount < 13){
		$("#dataFormSearch #typedata")[0].style.height = (37*rowcount+21) + "px";
		$("#dataFormSearch #basictable")[0].style.height = (37*rowcount) + "px";
	}else{
		$("#dataFormSearch #typedata")[0].style.height = "501px";
		$("#dataFormSearch #basictable")[0].style.height = "480px";
	}
	if(cslx != undefined && cslx!=null && cslx!="" && firstrow != null){
		pre_row = firstrow;
		firstrow.addClass("success");
		
		loadBasicData(firstrow);
	}else{
		searchDataResult(true)
	}
}

/**
 * 批量操作
 * @param url
 * @param msg
 * @param tabGridId 操作的tabGrid的Id，可选参数，默认通过getChecked()获取
 */
function plcz(url,msg,tabGridId){
	var sel_row = $('#dataFormSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
	
	if (sel_row.length == 0){
		$.alert('请选择您要'+msg+'的记录！');
	} else {
		var arrays = new Array();// 声明一个数组
		$(sel_row).each(function () {// 通过获得别选中的来进行遍历
			arrays.push(this.csid);	// cid为获得到的整条数据中的一列
        });
		var ids = arrays.join(',');
		
		$.confirm('您确定要'+msg+'选择的记录吗？',function(result){
			if(result){
				jQuery.ajaxSetup({async:false});
				jQuery.post(url,{ids:ids.toString(),access_token:$("#ac_tk").val()},function(responseText){
					setTimeout(function(){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								$('#dataFormSearch #tb_list').bootstrapTable('refresh');
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
}

/**
 * 批量推送
 * @param url
 * @param msg
 * @param tabGridId 操作的tabGrid的Id，可选参数，默认通过getChecked()获取
 */
function plts(url,msg,tabGridId){
	var sel_row = $('#dataFormSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据

	if (sel_row.length == 0){
		$.confirm('您确定要'+msg+'当前类别下的所有数据吗？',function(result){
			if(result){
				jQuery.ajaxSetup({async:false});
				jQuery.post(url,{access_token:$("#ac_tk").val()},function(responseText){
					setTimeout(function(){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								$('#dataFormSearch #tb_list').bootstrapTable('refresh');
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
	} else {
		var arrays = new Array();// 声明一个数组
		$(sel_row).each(function () {// 通过获得别选中的来进行遍历
			arrays.push(this.csid);	// cid为获得到的整条数据中的一列
        });
		var ids = arrays.join(',');

		$.confirm('您确定要'+msg+'选择的记录吗？',function(result){
			if(result){
				jQuery.ajaxSetup({async:false});
				jQuery.post(url,{ids:ids.toString(),access_token:$("#ac_tk").val()},function(responseText){
					setTimeout(function(){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								$('#dataFormSearch #tb_list').bootstrapTable('refresh');
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
}

//点击表格改变颜色
function tableBind(){
	$("#dataFormSearch table tbody tr").unbind("click");
    $("#dataFormSearch table tbody tr").click(function(){
		if(pre_row!=null)
			pre_row.removeClass("success");
		$(this).addClass("success");
		pre_row = $(this);
    	loadBasicData($(this));
	});
}
var data_PageInit = function(){
	var oInit = new Object();
	var postdata = {};

	oInit.Init = function () {
		var scbj = $("#dataFormSearch a[id^='scbj_id_']");
		$.each(scbj, function(i, n){
			var id = $(this).attr("id");
			var code = id.substring(id.lastIndexOf('_')+1,id.length);
			if(code == '0'){
				addTj('scbj',code,'dataFormSearch');
			}
		});
	}
	return oInit;
}
//获取表格显示形式
function getDataTableEvents(parent_flg,extend1_flg,extend2_flg,extend3_flg,extend4_flg,extend5_flg,extend1_sel_flg,extend2_sel_flg,extend3_sel_flg,extend4_sel_flg,extend5_sel_flg){
	return $.extend({},data_baseEvents,{
		columns:[
		             {checkbox: true},
		        	 {title:'参数代码',field:'csdm',width:'8%', sortable:true, sortName: 'csdm',align:'left'},
				     {title:'参数名称',field:'csmc',width:'20%', align:'left'},
				     {title:'参数排序',field:'cspx',width:'8%', sortable:true, sortName: 'cspx',align:'right'},
				     {title:'是否默认',field:'sfmr',width:'8%', sortable:true, sortName: 'sfmr',align:'right'},
		        	 {title:'父参数代码',field:'fcsdm',width:'8%', index: 'fcsdm',align:'left',visible:parent_flg},
		        	 {title:'父参数名称',field:'fcsmc',width:'8%', index: 'fcsmc',align:'left',visible:parent_flg},
		        	 {title:'扩展参数1',field:'cskz1',width:'8%', index: 'cskz1',sortable:true, sortName: 'cskz1',align:'left',visible:extend1_flg},
		        	 {title:'扩展参数1',field:'kz1mc',width:'8%', index: 'cskz1',align:'left',visible:extend1_sel_flg},
		        	 {title:'扩展参数2',field:'cskz2',width:'8%', index: 'cskz2',align:'left',visible:extend2_flg},
		        	 {title:'扩展参数2',field:'kz2mc',width:'8%', index: 'cskz2',align:'left',visible:extend2_sel_flg},
		        	 {title:'扩展参数3',field:'cskz3',width:'8%', index: 'cskz3',align:'left',visible:extend3_flg},
		        	 {title:'扩展参数3',field:'kz3mc',width:'8%', index: 'cskz3',align:'left',visible:extend3_sel_flg},
		        	 {title:'扩展参数4',field:'cskz4',width:'8%', index: 'cskz4',align:'left',visible:extend4_flg},
		        	 {title:'扩展参数4',field:'kz4mc',width:'8%', index: 'cskz4',align:'left',visible:extend4_sel_flg},
		        	 {title:'扩展参数5',field:'cskz5',width:'8%', index: 'cskz5',align:'left',visible:extend5_flg},
		        	 {title:'扩展参数5',field:'kz5mc',width:'8%', index: 'cskz5',align:'left',visible:extend5_sel_flg},
			      	 {title:'状态',field:'scbjmc',width:'8%', align:'center',width:'8%'},
			      	 {title:'备注',field:'bz', align:'left'}
				]
	    	});
}

//初始化
jQuery(function(){
	//0.界面初始化
	var oInit = new data_PageInit();
	oInit.Init();
	//1.初始化Table
    var oTable = new data_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new data_ButtonInit();
    oButtonInit.Init();
    
    $("#dataFormSearch #tb_list").colResizable({
        liveDrag:true,
        gripInnerHtml:"<div class='grip'></div>",
        draggingClass:"dragging",
        resizeMode:'fit',
        postbackSafe:true,
        partialRefresh:true
    })
    
    jQuery('#dataFormSearch .chosen-select').chosen({width: '100%'});
});