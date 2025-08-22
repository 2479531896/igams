var user_turnOff=true;
var user_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#userformSearch #tb_list').bootstrapTable({
            url: '/systemrole/user/pageGetListUser',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#userformSearch #toolbar', //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName:"yh.yhm",					//排序字段
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
            uniqueId: "yhid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
				width: '2%',
            }, {
                field: 'yhid',
                title: '用户ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'ddid',
                title: '钉钉ID',
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'wechatid',
                title: '微信ID',
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'yhm',
                title: '用户名',
				width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'zsxm',
                title: '真实姓名',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
				field: 'ssgs',
				title: '所属公司',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
                field: 'sfsd',
                title: '是否锁定',
                width: '4%',
                align: 'left',
                formatter:sdFormatter,
                visible: true
            }, {
                field: 'dqjsmc',
                title: '当前角色',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'signflg',
                title: '上传签名',
                align: 'left',
				width: '4%',
                formatter:signFormatter,
                visible: true
            },{
                  field: 'gwmc',
                  title: '岗位名称',
                width: '12%',
                align: 'left',
                  sortable: true,
                  visible: true
              }, {
                field: 'dlsj',
                title: '登录时间',
                align: 'left',
				width: '10%',
				sortable: true,
                visible: true
            },{
                field: 'cwcs',
                title: '错误次数',
                width: '4%',
                align: 'left',
                visible: true,
                sortable: true,
			},{
				field: 'grouping',
				title: 'Group',
				width: '6%',
				align: 'left',
				visible: false,
				sortable: true,
			},{
				field: 'mrfz',
				title: '默认分组',
				width: '6%',
				align: 'left',
				visible: false,
				sortable: true,
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            },
            onDblClickRow: function (row, $element) {
            	userDealById(row.yhid, 'view',$("#userformSearch #btn_view").attr("tourl"));
            },
        });
        $("#userformSearch #tb_list").colResizable({
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
            sortLastName: "yh.yhid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var cxbt = $("#userformSearch #cxbt").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#userformSearch #cxnr').val());
    	// '0':'来源编号','1':'外部编号','2':'孕周','3':'体重','4':'备注'
    	if (cxbt == "0") {
    		map["yhm"] = cxnr;
    	}else if (cxbt == "1") {
    		map["zsxm"] = cxnr;
    	}else if (cxbt == "2") {
    		map["dqjsmc"] = cxnr;
    	}else if (cxbt == "3") {
			map["jgmc"] = cxnr;
		}else if (cxbt == "4") {
			map["grouping"] = cxnr;
		}else if (cxbt == "5") {
			map["ddid"] = cxnr;
		}
    	// 删除标记
    	var sfsds = jQuery('#userformSearch #sfsd_id_tj').val();
    	map["sfsds"] = sfsds;
		// 所属公司
		var ssgss = jQuery('#userformSearch #ssgs_id_tj').val();
		map["ssgss"] = ssgss.replace(/'/g, "");
    	return map;
    };
    return oTableInit;
};

//锁定
function sdFormatter(value, row, index) {
    if (row.sfsd == '1') {
        return '已锁定';
    }
    else{
    	return "正常";
    }
}
//签名
function signFormatter(value, row, index) {
    if (row.signflg == '1') {
        return '已上传';
    }
    else{
    	return "未上传";
    }
}

var updateCheckConfig = {
	width		: "600px",
	modalName	: "updateCheckModal",
	formName	: "updateCheckForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success7 : {
			label : "保存",
			className : "btn-primary",
			callback : function() {
				if(!$("#updateCheckForm").valid()){
					return false;
				}
				var sDate=$("#updateCheckForm #startdate").val();
				var eDate=$("#updateCheckForm #enddate").val();
				var sArr = sDate.split("-");
				var eArr = eDate.split("-");
				var sRDate = new Date(sArr[0], sArr[1], sArr[2]);
				var eRDate = new Date(eArr[0], eArr[1], eArr[2]);
				var result = (eRDate-sRDate)/(24*60*60*1000);
				if(result>31){
					$.error('起止时间之间跨度不得超过一个月');
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#updateCheckForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"updateCheckForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchUserResult();
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

//关联用户信息列表模态框
var relWechatUserConfig = {
		width		: "800px",
		modalName	: "relUserModal",
		height		: "500px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					var sel_row = $('#RelWxyhformSearch #relwxyh_list').bootstrapTable('getSelections');//获取选择行数据
		    		if(sel_row.length!=1){
		    			$.error("请选中一行");
		    			if(!$("#ajaxForm").valid()){
							return false;
						}
		    		}
					$.confirm('您确定要更新所选择的信息吗？',function(result){
		    			if(result){
		    				jQuery.ajaxSetup({async:false});
		    				var wxid=sel_row[0].wxid;
		    				var yhid=sel_row[0].yhid;
		    				var xtyhid=$("#Xtyhid").val();
		    				var url= "/systemrole/user/pagedataUpdate";
		    				jQuery.post(url,{wxid:wxid,yhid:yhid,xtyhid:xtyhid,"access_token":$("#ac_tk").val()},function(responseText){
		    					setTimeout(function(){
		    						if(responseText["status"] == 'success'){
		    							$.success(responseText["message"],function() {
		    								$.closeModal("relUserModal");
		    								searchUserResult();
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
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

var setMessageConfig = {
    width		: "1200px",
    modalName	: "setMessageModal",
    formName	: "setMessageForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};

                $("#setMessageForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"setMessageForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchUserResult();
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
var addUserConfig = {
	width		: "900px",
	modalName	: "addUserModal",
	formName	: "user_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#user_ajaxForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#user_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchUserResult();
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

var addExternalUserConfig = {
	width		: "900px",
	modalName	: "addExternalUserModal",
	formName	: "externaluser_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
	    success : {
            label : "下一步",
            className : "btn-primary",
            callback : function() {
                if(!$("#externaluser_ajaxForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var permissions=[];
                var xtqxlist="";
                $.each($("#externaluser_ajaxForm .zdpbCheck"),function(i){
                    if($("#externaluser_ajaxForm .zdpbCheck")[i].checked==true){
                        xtqxlist=xtqxlist+","+$("#externaluser_ajaxForm .zdpbCheck")[i].value;
                        if($("#"+$("#externaluser_ajaxForm .zdpbCheck")[i].id).attr("csdm")=="deterpret"){
                            $.each($("#externaluser_ajaxForm .deterpret"),function(i){
                                if($("#externaluser_ajaxForm .deterpret")[i].checked==true){
                                    if($("#externaluser_ajaxForm .deterpret")[i].value=="displaytag"){
                                        $("#externaluser_ajaxForm #display_tag").val("1");
                                    }else{
                                        permissions.push($("#externaluser_ajaxForm .deterpret")[i].value);
                                    }
                                }
                            });
                            if(permissions.length==0){
                                $.alert("8000系统请至少选择一项权限！");
                                return false;
                            }
                        }
                    }
                });
                $('#externaluser_ajaxForm #jcdwmc').val($('#externaluser_ajaxForm #'+$('#externaluser_ajaxForm #jcdw').val()).text());
                $("#externaluser_ajaxForm #permissions").val(permissions);
                $("#externaluser_ajaxForm #xtqxlist").val(xtqxlist.substring(1));
                $("#externaluser_ajaxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"externaluser_ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            if($("#externaluser_ajaxForm #signflg").val()=="1"){
                                $.closeModal(opts.modalName);
                            }
                            preventResubmitForm(".modal-footer > button", false);
                            $("#externaluser_ajaxForm .page1").attr("style","display:none;margin-top:20px;")
                            $("#externaluser_ajaxForm .page2").attr("style","display:block;margin-top:20px;")
                            $("#externaluser_ajaxForm #signflg").val("1");//用于判断是否为为第二步确定操作
                        }
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

var checkinrecordUserConfig = {
	width		: "600px",
	modalName	: "checkinUserModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						if(opts.offAtOnce){
							$.closeModal(opts.modalName);
							searchUserResult();
						}
						window.open("/systemrole/user/downloadCheckinRecord?path="+responseText["path"] + "&access_token="+$("#ac_tk").val());
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

var partnerInstallUserConfig = {
		width		: "1200px",
		modalName	: "partnerInstallUserModal",
		formName	: "ajaxForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#ajaxForm").valid()){
						$.alert("请填写完整信息！");
						return false;
					}
					var num=0;
					$("#ajaxForm .hb input").each(function(){
						if(this.checked){
							num++;
						}
					})
					// if(num==0&&$("#ajaxForm #sjid option:selected").text()=="--请选择--"&&$("#ajaxForm #yhqf option:selected").text()=="--请选择--"&&$("#ajaxForm #yhzqf option:selected").text()=="--请选择--"){
					// 	$.alert("保存内容不能全部为空！");
					// 	return false;
					// }
					var $this = this;
					var opts = $this["options"]||{};
					var ids="";
					var size=$(".hb input[name='hbid']:checked").length;
					for(var i=0;i<size;i++){
						ids=ids+","+$(".hb input[name='hbid']:checked")[i].value;
					}
		    		ids = ids.substr(1);
		    		$("#ajaxForm #ids").val(ids);
					$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
					
					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									searchUserResult();
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

var viewUserConfig = {
	width		: "600px",
	modalName	: "viewUserModal",
	formName	: "viewUser_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var uploadSignConfig = {
	width		: "900px",
	modalName	: "uploadSignModal",
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
								searchUserResult();
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

//按钮动作函数
function userDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?yhid=" +id;
		$.showDialog(url,'查看用户',viewUserConfig);
	}else if(action =='add'){
		var url=tourl;
		$.showDialog(url,'新增用户',addUserConfig);
	}else if(action =='externaladd'){
        var url=tourl;
        $.showDialog(url,'新增外部用户',addExternalUserConfig);
    }else if(action =='mod'){
		var url= tourl + "?yhid=" +id;
		$.showDialog(url,'编辑用户',addUserConfig);
	}else if(action =='relwechat'){
		var url= tourl+"?yhid=" +id;
		$.showDialog(url,'关联微信用户列表',relWechatUserConfig);
	}else if(action =='checkinrecord'){
		var url= tourl;
		$.showDialog(url,'下载签到记录',checkinrecordUserConfig);
	}else if(action =='partnerinstall'){
		var url= tourl + "?yhid=" +id;
		$.showDialog(url,'设置合作伙伴',partnerInstallUserConfig);
	}else if(action =='uploadsign'){
		 var url=tourl+ "?yhm=" +id;
		$.showDialog(url,'上传签名',uploadSignConfig);
	}else if(action =='updatecheck'){
		var url=tourl;
		$.showDialog(url,'更新考勤信息',updateCheckConfig);
	}else if(action =='setmessage'){
        var url= tourl + "?yhid=" +id;
        $.showDialog(url,'消息订阅',setMessageConfig);
    }
}
//提供给导出用的回调函数
function XtyhSearchData(){
	var map = {};
	var cxbt = $("#userformSearch #cxbt").val();
	// 查询条件
	var cxnr = $.trim(jQuery('#userformSearch #cxnr').val());
	// '0':'来源编号','1':'外部编号','2':'孕周','3':'体重','4':'备注'
	if (cxbt == "0") {
		map["yhm"] = cxnr;
	}else if (cxbt == "1") {
		map["zsxm"] = cxnr;
	}else if (cxbt == "2") {
		map["dqjsmc"] = cxnr;
	}else if (cxbt == "3") {
		map["jgmc"] = cxnr;
	}else if (cxbt == "4") {
		map["grouping"] = cxnr;
	}else if (cxbt == "5") {
		map["ddid"] = cxnr;
	}
	// 删除标记
	var sfsds = jQuery('#userformSearch #sfsd_id_tj').val();
	map["sfsds"] = sfsds;
	// 所属公司
	var ssgss = jQuery('#userformSearch #ssgs_id_tj').val();
	map["ssgss"] = ssgss.replace(/'/g, "");
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="yh.xgsj";
	map["sortLastOrder"]="desc";
	map["sortName"]="yh.lrsj";
	map["sortOrder"]="desc";
	return map;
}

var user_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_add = $("#userformSearch #btn_add");
    	var btn_mod = $("#userformSearch #btn_mod");
    	var btn_del = $("#userformSearch #btn_del");
    	var btn_view = $("#userformSearch #btn_view");
    	var btn_initpass = $("#userformSearch #btn_initpass");
    	var bth_relwechat=$("#userformSearch #btn_relwechat");
    	var btn_initdd = $("#userformSearch #btn_initdd");
    	var btn_checkinrecord = $("#userformSearch #btn_checkinrecord");
    	var btn_partnerinstall = $("#userformSearch #btn_partnerinstall")
    	var btn_user_query = $("#btn_user_query");
    	var btn_uploadsign=$("#userformSearch #btn_uploadsign");
		var btn_searchexport = $("#userformSearch #btn_searchexport");
		var btn_selectexport = $("#userformSearch #btn_selectexport");
		var btn_updatecheck=$("#userformSearch #btn_updatecheck");
		var btn_externaladd=$("#userformSearch #btn_externaladd");
		var btn_setmessage=$("#userformSearch #btn_setmessage");

    	//绑定搜索发送功能
    	if(btn_user_query != null){
    		btn_user_query.unbind("click").click(function(){
    			searchUserResult(true);
    		});
    	}
		// 	  ---------------------------导出-----------------------------------
		btn_selectexport.unbind("click").click(function(){
			var sel_row = $('#userformSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].yhid;
				}
				ids = ids.substr(1);
				$.showDialog(exportPrepareUrl+"?ywid=USER_SELECT&expType=select&ids="+ids
					,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
			}else{
				$.error("请选择数据");
			}
		});

		btn_searchexport.unbind("click").click(function(){
			$.showDialog(exportPrepareUrl+"?ywid=USER_SEARCH&expType=search&callbackJs=XtyhSearchData"
				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
		});
    	btn_add.unbind("click").click(function(){
    		userDealById(null,"add",btn_add.attr("tourl"));
    	});

    	btn_externaladd.unbind("click").click(function(){
            userDealById(null,"externaladd",btn_externaladd.attr("tourl"));
        });
    	
    	btn_checkinrecord.unbind("click").click(function(){
    		userDealById(null,"checkinrecord",btn_checkinrecord.attr("tourl"));
        });

		btn_updatecheck.unbind("click").click(function(){
			userDealById(null,"updatecheck",btn_updatecheck.attr("tourl"));
		});
    	
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#userformSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			userDealById(sel_row[0].yhid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_setmessage.unbind("click").click(function(){
            var sel_row = $('#userformSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                userDealById(sel_row[0].yhid,"setmessage",btn_setmessage.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });

    	btn_partnerinstall.unbind("click").click(function(){
    		var sel_row = $('#userformSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			userDealById(sel_row[0].yhid,"partnerinstall",btn_partnerinstall.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#userformSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			userDealById(sel_row[0].yhid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});

    	/* ------------------------------关联微信用户列表-----------------------------*/
    	bth_relwechat.unbind("click").click(function(){
    		var sel_row = $('#userformSearch #tb_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			userDealById(sel_row[0].yhid,"relwechat",bth_relwechat.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
        btn_initpass.unbind("click").click(function(){
            var sel_row = $('#userformSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].yhid;
                ids = ids.substr(1);
                $.confirm('您确定要初始化所选用户的密码吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_initpass.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchUserResult();
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
        });
            
        btn_initdd.unbind("click").click(function(){
            $.confirm('您确定要更新钉钉ID信息吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url= btn_initdd.attr("tourl");
                    jQuery.post(url,{"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    searchUserResult();
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
	
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#userformSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		var yhms="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].yhid;
    			yhms = yhms + "," + sel_row[i].yhm;
    		}
    		yhms = yhms.substr(1);
    		ids = ids.substr(1);
    		$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= btn_del.attr("tourl");
    				jQuery.post(url,{ids:ids,yhms:yhms,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								searchUserResult();
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
    	/* ------------------------------上传签名-----------------------------*/
    	btn_uploadsign.unbind("click").click(function(){
            var sel_row = $('#userformSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            userDealById(sel_row[0].yhm,"uploadsign",btn_uploadsign.attr("tourl"));
    	});
    	
    	/**显示隐藏**/      
    	$("#userformSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(user_turnOff){
    			$("#userformSearch #searchMore").slideDown("low");
    			user_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#userformSearch #searchMore").slideUp("low");
    			user_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
    };

    return oInit;
};

function searchUserResult(isTurnBack){
	if(isTurnBack){
		$('#userformSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#userformSearch #tb_list').bootstrapTable('refresh');
		}
}

var user_PageInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
	    var sfsd = $("#userformSearch a[id^='sfsd_id_']");
		$.each(sfsd, function(i, n){
			var id = $(this).attr("id");
			var code = id.substring(id.lastIndexOf('_')+1,id.length);
			if(code == '0'){
				addTj('sfsd',code,'userformSearch');
			}
		});
    }
    return oInit;
}

$(function(){
	//0.界面初始化
    var oInit = new user_PageInit();
    oInit.Init();
    //1.初始化Table
    var oTable = new user_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new user_ButtonInit();
    oButtonInit.Init();
    
	//所有下拉框添加choose样式
	jQuery('#userformSearch .chosen-select').chosen({width: '100%'});
});
