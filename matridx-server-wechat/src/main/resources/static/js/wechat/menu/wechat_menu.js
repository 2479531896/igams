//监听微信公众号改变事件
$("#competencetreeDiv #wbcxid").bind("change",function(){
	var wbcxid=this.value;
	//1.初始化Table
    var oTable = new tag_TableInit(wbcxid);
    oTable.Init();
    selectTag(wbcxid);
})

/**
 * 查询公众号菜单个性类型(标签)
 * @param wbcxid
 */
function selectTag(wbcxid){
	$.ajax({
		url:'/menu/menu/selectTag',
		type:'post',
		dateType:'json',
		data:{"wbcxid":wbcxid,"access_token":$("#ac_tk").val()},
		success:function(data){
			if(data.wbcxDto){
				$("#competencetreeDiv #wbcxdm").val(data.wbcxDto.wbcxdm);
			}else{
				$("#competencetreeDiv #wbcxdm").val(null);
			}
			if(data.bqglList!=null){
				var bqHtml = "";
				bqHtml+="<option value=''>默认</option>";
            	$.each(data.bqglList,function(i){   		
            		bqHtml += "<option value='" + data.bqglList[i].bqid + "'>" + data.bqglList[i].bqm + "</option>";
            	});
            	$("#wxcdDiv #gxlx").empty();
            	$("#wxcdDiv #gxlx").append(bqHtml);
            	$("#wxcdDiv #gxlx").trigger("chosen:updated");
            	$('#wechatMenu').jstree("destroy");
        		getWeChatMenu();
			}
		}
	})
}
	
/**
 * 绑定按钮事件
 */
function btnBind(){
	//推送菜单
	var btn_wxcd = $("#competencetreeDiv #btn_wxcd");
	btn_wxcd.unbind("click").click(function(){
		var url= "/menu/menu/createMenu"
		$.confirm('您确定将当前菜单更新至微信吗？',function(result){
			if(result){
				jQuery.ajaxSetup({async:false});
				jQuery.post(url,{"wbcxid":$("#competencetreeDiv #wbcxid").val(),"wbcxdm":$("#competencetreeDiv #wbcxdm").val(),"gxlx":$("#competencetreeDiv #gxlx").val(),"access_token":$("#ac_tk").val()},function(responseText){
					setTimeout(function(){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
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
	
	//删除菜单
	var btn_del_wxcd = $("#competencetreeDiv #btn_del_wxcd");
	btn_del_wxcd.unbind("click").click(function(){
		var url= "/menu/menu/deleteMenu"
		$.confirm('您确定删除微信当前菜单吗？',function(result){
			if(result){
				jQuery.ajaxSetup({async:false});
				jQuery.post(url,{"wbcxid":$("#competencetreeDiv #wbcxid").val(),"wbcxdm":$("#competencetreeDiv #wbcxdm").val(),"access_token":$("#ac_tk").val()},function(responseText){
					setTimeout(function(){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
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
	
	//生成临时二维码(检验)
	var btn_genQRCode = $("#btn_genQRCode");
	btn_genQRCode.unbind("click").click(function(){
		var url= "/menu/menu/generateQRcodeView";
		$.showDialog(url,'生成临时二维码(检验)',generateQRcodeConfig);
	});
	
	//上传图片(生物)
	var btn_image = $("#btn_image");
	btn_image.unbind("click").click(function(){
		var url= "/menu/menu/uploadMaterial";
		$.showDialog(url,'上传素材(图片)',uploadMaterialConfig);
	});
	
	//群发(生物)
	var btn_sendall = $("#btn_sendall");
	btn_sendall.unbind("click").click(function(){
		var url= "/menu/menu/sendAllCustom";
		$.showDialog(url,'群发消息',sendAllCustomConfig);
	});
	
	//个性类型下拉框改变事件
	var sel_gxlx = $("#competencetreeDiv #gxlx");
	sel_gxlx.unbind("change").change(function(){
		$('#wechatMenu').jstree("destroy");
		getWeChatMenu();
	});
	
	//添加根菜单
	var btn_add = $("#competencetreeDiv #btn_add");
	btn_add.unbind("click").click(function(){
    	var gxlx = $("#competencetreeDiv #gxlx").val();
    	var url= "/menu/menu/addWechatMenu?gxlx=" + gxlx + "&wbcxid="+$("#competencetreeDiv #wbcxid").val();
		$.showDialog(url,'新增根菜单',addWechatMenuConfig);
	});
	
}

var generateQRcodeConfig = {
	width		: "800px",
	modalName	: "generateQRcodeModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var uploadMaterialConfig = {
	width		: "800px",
	modalName	: "uploadMaterialModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				
				if(!$("#ajaxForm").valid()){
					return false;
				}
				//判断是否上传一张图片
				var arr = null;
				if($("#ajaxForm #fjids").val()){
					arr = $("#ajaxForm #fjids").val().split(",");
				}
				if(arr == null || arr.length != 1){
					$.error("请选择一张图片！");
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
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				});
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var sendAllCustomConfig = {
	width		: "800px",
	modalName	: "sendAllCustomModal",
	formName	: "ajaxForm",
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
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				});
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

/* -------------------------------------    初始化菜单树        ------------------------------------- */
/**
 * 初始化树状菜单
 */
function getWeChatMenu(){
	$('#wechatMenu').jstree( {'plugins':["wholerow","contextmenu"],
		'core' : {
			'data' : {
				'url' : '/menu/menu/wechatMenuTree',
				'data' : function (node) {
					var map = {};
					map['access_token'] = $("#ac_tk").val();
					map['gxlx'] = $("#competencetreeDiv #gxlx").val();
					map['wbcxid'] = $("#competencetreeDiv #wbcxid").val();
					return map;
	            }
			}
		},
		'contextmenu' : {
		    'items' : customMenu     //每个节点都会调用这个函数
		}
	}).on('loaded.jstree', function(e, data){
		var wH  = $(window).height();
		$(this).parents('.modal-dialog').height(550);
		$(this).parents('.bootbox-body').height(550);
		var dialogH = $(this).parents('.modal-dialog').height();
		var top = (wH - dialogH)/2;
		$(this).parents('.modal-dialog').css("top",top);
	});
}
/**
 * 绑定树状右键事件
 */
function customMenu(node){
    var items = {
    		'add' : {
                'label' : '设置同级菜单',
				'icon': "glyphicon glyphicon-plus",
                'action' : function (node) {
                	var cdid = node.reference.prevObject[0].id;
                	var gxlx = $("#competencetreeDiv #gxlx").val();
                	var wbcxid = $("#competencetreeDiv #wbcxid").val();
                	var url= "/menu/menu/addWechatMenu?cdid=" +cdid+"&gxlx="+gxlx+"&wbcxid="+wbcxid;
            		$.showDialog(url,'设置同级菜单',addWechatMenuConfig);
                }
            },
            'addzcd' : {
                'label' : '设置子菜单',
				'icon': "glyphicon glyphicon-plus",
                'action' : function (node) { 
                	var cdid = node.reference.prevObject[0].id;
                	var gxlx = $("#competencetreeDiv #gxlx").val();
                	var wbcxid = $("#competencetreeDiv #wbcxid").val();
                	var url= "/menu/menu/addWechatMenu?fcdid=" +cdid+"&gxlx="+gxlx+"&wbcxid="+wbcxid;
            		$.showDialog(url,'设置子菜单',addWechatMenuConfig);
                }
            },
            'mod' : {
                'label' : '修改信息',
				'icon': "glyphicon glyphicon-pencil",
                'action' : function (node) { 
                	var cdid = node.reference.prevObject[0].id;
                	var url= "/menu/menu/modWechatMenu?cdid=" +cdid;
            		$.showDialog(url,'修改菜单信息',modWechatMenuConfig);
                }
            },
            'del' : {
                'label' : '删除菜单',
				'icon': "glyphicon glyphicon-remove",
                'action' : function (node) { 
                	var cdid = node.reference.prevObject[0].id;
                	var url= "/menu/menu/delWechatMenu"
                	$.confirm('您确定要删除所选择的菜单吗？',function(result){
    	    			if(result){
    	    				jQuery.ajaxSetup({async:false});
    	    				jQuery.post(url,{"cdid":cdid,"access_token":$("#ac_tk").val()},function(responseText){
    	    					setTimeout(function(){
    	    						if(responseText["status"] == 'success'){
    	    							$.success(responseText["message"],function() {
    	    								$('#wechatMenu').jstree(true).refresh();
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
            },
		    'cancle' : {
		        'label' : '取消',
		        'icon': "glyphicon glyphicon-ban-circle",
		        'action' : function () {
		        	$('#wechatMenu').jstree(true).refresh();
		        }
		    }
        }

	return items;    //注意要有返回值
}
//新增菜单页面
var addWechatMenuConfig = {
	width		: "800px",
	modalName	: "addWechatMenuModal",
	formName	: "ajaxForm",
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
								$('#wechatMenu').jstree(true).refresh();
								$.closeModal(opts.modalName);
							}
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				});
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
//修改菜单页面
var modWechatMenuConfig = {
	width		: "800px",
	modalName	: "modWechatMenuModal",
	formName	: "ajaxForm",
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
								$('#wechatMenu').jstree(true).refresh();
								$.closeModal(opts.modalName);
							}
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				});
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

/* ------------------------------------      初始化标签table      ------------------------------------ */
var tag_turnOff=true;
var tag_TableInit = function (wbcxid) {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
    	$('#tag_formSearch #tb_list').bootstrapTable('destroy'); 
        $('#tag_formSearch #tb_list').bootstrapTable({
            url: '/tag/tag/listTag?wbcxid='+wbcxid,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#tag_formSearch #toolbar', //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName:"bq.bqm",					//排序字段
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
            uniqueId: "bqm",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'bqid',
                title: '标签ID',
                width: '30%',
                align: 'left',
                visible: true
            }, {
                field: 'bqm',
                title: '标签名称',
                titleTooltip:'标签名称',
                width: '40%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	tagDealById(row.bqid, 'view',$("#tag_formSearch #btn_view").attr("tourl"));
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
            sortLastName: "bq.bqm", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var cxbt = $("#tag_formSearch #cxbt").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#tag_formSearch #cxnr').val());
    	// '0':'标签名称',
    	if (cxbt == "0") {
    		map["bqm"] = cxnr;
    	}
    	
    	return map;
    };
    return oTableInit;
};
var tag_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_add = $("#tag_formSearch #btn_add");
    	var btn_mod = $("#tag_formSearch #btn_mod");
    	var btn_del = $("#tag_formSearch #btn_del");
    	var btn_tag_query = $("#btn_tag_query");
    	var btn_get = $("#btn_get");
    	
    	//绑定搜索发送功能
    	if(btn_tag_query != null){
    		btn_tag_query.unbind("click").click(function(){
    			searchTagResult();
    		});
    	}
    	btn_add.unbind("click").click(function(){
    		var wbcxid=$("#competencetreeDiv #wbcxid").val();
    		if(wbcxid!=null &&wbcxid!=""){
    			tagDealById(null,"add",btn_add.attr("tourl"));
    		}else{
    			$.error("请先选择公众号！");
    		}
    		
    	});
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#tag_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			tagDealById(sel_row[0].bqid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#tag_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].bqid;
    		}
    		ids = ids.substr(1);
    		$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= btn_del.attr("tourl");
    				var wbcxid = $("#competencetreeDiv #wbcxid").val();
    				var wbcxdm = $("#competencetreeDiv #wbcxdm").val();
    				jQuery.post(url,{ids:ids,"wbcxid":wbcxid,"wbcxdm":wbcxdm,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								searchTagResult();
    								selectTag(wbcxid)
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
    	btn_get.unbind("click").click(function(){
    		var wbcxid = $("#competencetreeDiv #wbcxid").val();
    		if(wbcxid ==null || wbcxid ==""){
    			$.error("请先选择公众号！");
    			return;
    		}
    		$.confirm('您确定要拉取微信标签信息吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= btn_get.attr("tourl");
    				var wbcxdm = $("#competencetreeDiv #wbcxdm").val();
    				jQuery.post(url,{"wbcxid":wbcxid,"wbcxdm":wbcxdm,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								searchTagResult();
    								selectTag(wbcxid)
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
    };

    return oInit;
};
/**
 * 按钮动作函数
 */
function tagDealById(id,action,tourl){
	var wbcxid = $("#competencetreeDiv #wbcxid").val();
	var wbcxdm = $("#competencetreeDiv #wbcxdm").val();
	if(!tourl){
		return;
	}
	if(action =='mod'){
		var url=tourl + "?bqid=" +id+"&wbcxid="+wbcxid+"&wbcxdm="+wbcxdm;
		$.showDialog(url,'编辑标签',addTagConfig);
	}else if(action =='add'){
		var url= tourl+"?wbcxid="+wbcxid+"&wbcxdm="+wbcxdm;
		$.showDialog(url,'新增标签',addTagConfig);
	}
}
//新增/编辑标签
var addTagConfig = {
	width		: "600px",
	modalName	: "addTagModal",
	formName	: "ajaxForm",
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
				var wbcxid = $("#competencetreeDiv #wbcxid").val();
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchTagResult();
								selectTag(wbcxid)
							}
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				});
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
 * 查询列表
 */
function searchTagResult(){
	$('#tag_formSearch #tb_list').bootstrapTable('refresh');
}

$(document).ready(function(){
	btnBind();
	getWeChatMenu();
	//所有下拉框添加choose样式
	jQuery('#competencetreeDiv .chosen-select').chosen({width: '100%'});
	//1.初始化Table 
	var oTable = new tag_TableInit(null);
		oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new tag_ButtonInit();
    oButtonInit.Init();
});