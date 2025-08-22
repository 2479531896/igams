var sampleAllot_turnOff = true;
//点击事件初始化
var SampleAllot_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_query = $("#sampleAllotList_formSearch #btn_query");//查询
        var btn_view = $("#sampleAllotList_formSearch #btn_view");//查看
        var btn_mod = $("#sampleAllotList_formSearch #btn_mod");//修改
        var btn_allocation = $("#sampleAllotList_formSearch #btn_allocation");//调拨
        var btn_del = $("#sampleAllotList_formSearch #btn_del");//删除
        //高级筛选父参数
        var dcccdwBind  = $("#sampleAllotList_formSearch #dcccdw_id ul li a");
        var dcbxBind    = $("#sampleAllotList_formSearch #dcbx_id ul li a");
        var drccdwBind  = $("#sampleAllotList_formSearch #drccdw_id ul li a");
        var drbxBind    = $("#sampleAllotList_formSearch #drbx_id ul li a");
        //添加日期控件
        laydate.render({
           elem: "#sampleAllotList_formSearch #dbrqstart"
          ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
           elem: "#sampleAllotList_formSearch #dbrqend"
          ,theme: '#2381E9'
        });
        /*----------查询----------*/
        btn_query.unbind("click").click(function(){
            searchSampleAllotResult(true);
        });
        /*----------查看----------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $("#sampleAllotList_formSearch #sampleAllotList").bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                SampleAllotDealById(sel_row[0].ybdbid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*----------修改----------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $("#sampleAllotList_formSearch #sampleAllotList").bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
                    SampleAllotDealById(sel_row[0].ybdbid,"mod",btn_mod.attr("tourl"));
                }else{
                    $.error("审核中，不允许修改!");
                }
            }else{
                $.error("请选中一行");
            }
        });
        /*----------删除----------*/
        btn_del.unbind("click").click(function(){
        	var sel_row = $("#sampleAllotList_formSearch #sampleAllotList").bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].ybdbid;
            }
            ids = ids.substr(1);
            $.confirm('您确定要删除所选择的记录吗？',function(result){
                if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= ($("#sampleAllotList_formSearch #urlPrefix").val()?$("#sampleAllotList_formSearch #urlPrefix").val():($("#urlPrefix").val()?$("#urlPrefix").val():"")) + btn_del.attr("tourl");
    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    searchSampleAllotResult();
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
            })
        })
        /*----------调拨----------*/
		btn_allocation.unbind("click").click(function(){
			SampleAllotDealById(null,"allocation",btn_allocation.attr("tourl"));
		});

        if (dcccdwBind != null){
            dcccdwBind.on("click", function(){
                setTimeout(function(){
                    getConditionsByFather("dcccdw","dcbx","/sampleAllot/pagedataGetBxList");
                }, 10);
            });
        }
        if (dcbxBind != null){
            dcbxBind.on("click", function(){
                setTimeout(function(){
                    getConditionsByFather("dcbx","dcct","/sampleAllot/pagedataGetSbList");
                }, 10);
            });
        }
        if (drccdwBind != null){
            drccdwBind.on("click", function(){
                setTimeout(function(){
                    getConditionsByFather("drccdw","drbx","/sampleAllot/pagedataGetBxList");
                }, 10);
            });
        }
        if (drbxBind != null){
            drbxBind.on("click", function(){
                setTimeout(function(){
                    getConditionsByFather("drbx","drct","/sampleAllot/pagedataGetSbList");
                }, 10);
            });
        }
        /*----------显示隐藏----------*/
        $("#sampleAllotList_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(sampleAllot_turnOff){
                $("#sampleAllotList_formSearch #searchMore").slideDown("low");
                sampleAllot_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#sampleAllotList_formSearch #searchMore").slideUp("low");
                sampleAllot_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    };
    return oInit;
};
//表格初始化
var SampleAllot_TableInit = function(){
    var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$("#sampleAllotList_formSearch #sampleAllotList").bootstrapTable({
			url: ($("#sampleAllotList_formSearch #urlPrefix").val()?$("#sampleAllotList_formSearch #urlPrefix").val():($("#urlPrefix").val()?$("#urlPrefix").val():"")) + '/sampleAllot/pageGetListSampleAllot',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: "#sampleAllotList_formSearch #toolbar", // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"dbrq,dbrmc,dcccdw,dcbx,dcct,drccdw,drbx,drct",					// 排序字段
			sortOrder: "DESC",                   // 排序方式
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
			uniqueId: "ybdbid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true,
				width: '5%'
			}, {
				'field': '',
			    'title': '序号',
			    'align': 'center',
			    'width': '5%',
			    'formatter': function (value, row, index) {
			    　　var options = $("#sampleAllotList_formSearch #sampleAllotList").bootstrapTable('getOptions');
			    　　return options.pageSize * (options.pageNumber - 1) + index + 1;
			    }
			}, {
				field: 'dbrmc',
				title: '调拨人',
				width: '10%',
				align: 'left',
                sortable: true,
				visible: true
			}, {
				field: 'dbrq',
				title: '调拨日期',
				width: '10%',
				align: 'left',
                sortable: true,
				visible: true
			}, {
				field: 'dcccdwmc',
				title: '调出储存单位',
				width: '15%',
				align: 'left',
                sortable: true,
				visible: true
			}, {
				field: 'dcbxmc',
				title: '调出冰箱',
				width: '10%',
				align: 'left',
                sortable: true,
                formatter:dcbxFormat,
				visible: true
			}, {
				field: 'dcctmc',
				title: '调出抽屉',
				width: '10%',
				align: 'left',
                sortable: true,
				visible: true
			}, {
				field: 'drccdwmc',
				title: '调入储存单位',
				width: '15%',
				align: 'left',
                sortable: true,
				visible: true
			}, {
				field: 'drbxmc',
				title: '调入冰箱',
				width: '10%',
				align: 'left',
                sortable: true,
                formatter:drbxFormat,
				visible: true
			}, {
				field: 'drctmc',
				title: '调入抽屉',
				width: '10%',
				align: 'left',
                sortable: true,
				visible: true
			},{
				field: 'bz',
				title: '备注',
				width: '10%',
				align: 'left',
                sortable: true,
				visible: true
			},{
				field: 'zt',
				title: '状态',
				width: '10%',
				align: 'left',
                sortable: true,
                formatter:ztFormat,
				visible: true
			},{
                 title: '操作',
                 align: 'center',
                 width: '15%',
                 valign: 'middle',
                 formatter:czFormat,
                 visible: true
             }],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				SampleAllotDealById(row.ybdbid,'view',$("#sampleAllotList_formSearch #btn_view").attr("tourl"));
			},
		});
	};
	// 得到查询的参数
    oTableInit.queryParams = function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "ybdb.ybdbid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getSampleAllotSearchData(map);
    }
	return oTableInit;
}
//调拨弹框初始化
var editSampleAllotConfig = {
	width		: "1200px",
	modalName   : "sampleAllotModal",
	formName    : "sampleAllotForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
    	successtwo : {
    	    label       : "提交",
            className   : "btn-primary",
            callback : function() {
                if (!$("#sampleAllotForm").valid()) {
                    $.alert("请填写完整信息");
                    return false;
                }
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    //判断List中是否有dchz重复的
                    for(var i=0;i<t_map.rows.length;i++){
                        if(json != null && json.length > 0){
                            for(var j=0;j<json.length;j++){
                                if(json[j].dchz == t_map.rows[i].dchz){
                                    $.alert("调出盒子（"+t_map.rows[i].dchzmc+"）重复");
                                    return false;
                                }
                            }
                        }
                        var sz = {"xh":"","dbmxid":"","dchz":"","drhz":""};
                        sz.xh = t_map.rows[i].xh;
                        sz.dbmxid = t_map.rows[i].dbmxid;
                        if (!t_map.rows[i].dchz){
                            $.alert("请添加调出盒子！");
                            return false;
                        }
                        sz.dchz = t_map.rows[i].dchz;
                        sz.drhz = t_map.rows[i].drhz;
                        json.push(sz);
                    }
                    $("#sampleAllotForm #hzInfo_json").val(JSON.stringify(json));
                }else{
                    $.alert("请添加调出盒子！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"] || {};
                $("#sampleAllotForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"] || "sampleAllotForm", function (responseText, statusText) {
                    if (responseText["status"] == 'success') {
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            //提交审核
                            if(responseText["auditType"]!=null){
                                var params=[];
                                params.prefix=responseText["urlPrefix"];
                                showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                    $.closeModal(opts.modalName);
                                    searchSampleAllotResult();
                                },null,params);
                            }else{
                                searchSampleAllotResult();
                            }
                            searchSampleAllotResult();
                        });
                    } else if (responseText["status"] == "fail") {
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"], function () {
                        });
                    } else {
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"], function () {
                        });
                    }
                }, ".modal-footer > button");
                return false;
            }
	    },
    	success : {
    	    label       : "保 存",
            className   : "btn-success",
            callback : function() {
                if (!$("#sampleAllotForm").valid()) {
                    $.alert("请填写完整信息");
                    return false;
                }
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    //判断List中是否有dchz重复的
                    for(var i=0;i<t_map.rows.length;i++){
                        if(json != null && json.length > 0){
                            for(var j=0;j<json.length;j++){
                                if(json[j].dchz == t_map.rows[i].dchz){
                                    $.alert("调出盒子（"+t_map.rows[i].dchzmc+"）重复");
                                    return false;
                                }
                            }
                        }
                        var sz = {"xh":"","dbmxid":"","dchz":"","drhz":""};
                        sz.xh = t_map.rows[i].xh;
                        sz.dbmxid = t_map.rows[i].dbmxid;
                        if (!t_map.rows[i].dchz){
                            $.alert("请添加调出盒子！");
                            return false;
                        }
                        sz.dchz = t_map.rows[i].dchz;
                        sz.drhz = t_map.rows[i].drhz;
                        json.push(sz);
                    }
                    $("#sampleAllotForm #hzInfo_json").val(JSON.stringify(json));
                }else{
                    $.alert("请添加调出盒子！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"] || {};
                $("#sampleAllotForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"] || "sampleAllotForm", function (responseText, statusText) {
                    if (responseText["status"] == 'success') {
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchSampleAllotResult();
                        });
                    } else if (responseText["status"] == "fail") {
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"], function () {
                        });
                    } else {
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"], function () {
                        });
                    }
                }, ".modal-footer > button");
                return false;
            }
	    },
		cancel      : {
			label       : "关 闭",
			className   : "btn-default"
		}
	}
};
//查看弹框初始化
var viewSampleAllotConfig = {
	width		: "1200px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

$(function(){
	//0.界面初始化
	//var oInit = new sampleAllot_PageInit();
	//oInit.Init();

	// 1.初始化Table
	var oTable = new SampleAllot_TableInit();
	oTable.Init();

	//2.初始化Button的点击事件
	var oSampleAllot_ButtonInit = new SampleAllot_ButtonInit();
	oSampleAllot_ButtonInit.Init();

	// 所有下拉框添加choose样式
	jQuery("#sampleAllotList_formSearch .chosen-select").chosen({width: '100%'});

	$("#sampleAllotList_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});

});

//根据高级筛选中父参数，获取子参数的高级筛选条件
function getConditionsByFather(fatherName,childName,url) {
	// 获取选中的父参数
	var ids = jQuery("#sampleAllotList_formSearch #" + fatherName+ "_id_tj").val();
	if (!isEmpty(ids)) {
		ids = "'" + ids + "'";
		jQuery("#sampleAllotList_formSearch #" + childName + "_id").removeClass("hidden");
	}else{
		jQuery("#sampleAllotList_formSearch #" + childName + "_id").addClass("hidden");
		if(childName == 'dcbx'){
		    jQuery("#sampleAllotList_formSearch #dcct" + "_id").addClass("hidden");
		}
		if(childName == 'drbx'){
		    jQuery("#sampleAllotList_formSearch #drct" + "_id").addClass("hidden");
		}
	}
	// 若选中的父参数不为空
	if (!isEmpty(ids)) {
		var url = ($("#sampleAllotList_formSearch #urlPrefix").val()?$("#sampleAllotList_formSearch #urlPrefix").val():($("#urlPrefix").val()?$("#urlPrefix").val():"")) + url;
		$.ajax({
			type : "POST",
			url : url,
			data : {"fcsids":ids,"access_token":$("#ac_tk").val()},
			dataType : "json",
			success : function(data){
				if(data.list.length > 0) {
					var html = "";
					$.each(data.list,function(i){
						html += "<li>";
					    html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('"+childName+"','" + data.list[i].csid + "','sampleAllotList_formSearch');\" id=\""+childName+"_id_" + data.list[i].csid + "\"><span>" + data.list[i].wz + (data.list[i].sbh?"(" + data.list[i].sbh + ")":"") + "</span></a>";
						html += "</li>";
					});
					jQuery("#sampleAllotList_formSearch #ul_"+childName).html(html);
					jQuery("#sampleAllotList_formSearch #ul_"+childName).find("[name='more']").each(function(){
						$(this).on("click", s_showMoreFn);
					});
				} else {
					jQuery("#sampleAllotList_formSearch #ul_"+childName).empty();

				}
				jQuery("#sampleAllotList_formSearch [id^='" + childName + "_li_']").remove();
				$("#sampleAllotList_formSearch #" + childName + "_id_tj").val("");
				
                var dcbxBind = $("#sampleAllotList_formSearch #dcbx_id ul li a");
                if (dcbxBind != null){
                    dcbxBind.on("click", function(){
                        setTimeout(function(){
                            getConditionsByFather("dcbx","dcct","/sampleAllot/pagedataGetSbList");
                        }, 10);
                    });
                }
                var drbxBind = $("#sampleAllotList_formSearch #drbx_id ul li a");
                if (drbxBind != null){
                    drbxBind.on("click", function(){
                        setTimeout(function(){
                            getConditionsByFather("drbx","drct","/sampleAllot/pagedataGetSbList");
                        }, 10);
                    });
                }
			}
		});
	} else {
		jQuery("#sampleAllotList_formSearch #div_"+childName).empty();
		jQuery("#sampleAllotList_formSearch [id^='" + childName + "_li_']").remove();
		$("#sampleAllotList_formSearch #" + childName + "_id_tj").val("");
	}
}

//查询事件
function searchSampleAllotResult(isTurnBack){
    //关闭高级搜索条件
    $("#sampleAllotList_formSearch #searchMore").slideUp("low");
    sampleAllot_turnOff = true;
     $("#sampleAllotList_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $("#sampleAllotList_formSearch #sampleAllotList").bootstrapTable('refresh',{pageNumber:1});
    }else{
        $("#sampleAllotList_formSearch #sampleAllotList").bootstrapTable('refresh');
    }
}

//查询条件
function getSampleAllotSearchData(map){
	var cxtj=$("#sampleAllotList_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery("#sampleAllotList_formSearch #cxnr").val());
	if(cxtj=="0"){
		map["dbrmc"]=cxnr
	}

	//调出储存单位  基础数据
	var dcccdw = jQuery("#sampleAllotList_formSearch #dcccdw_id_tj").val();
	map["dcccdws"] = dcccdw;
	//调出冰箱
	var dcbx = jQuery("#sampleAllotList_formSearch #dcbx_id_tj").val();
	map["dcbxs"] = dcbx;
	//调出抽屉
	var dcct = jQuery("#sampleAllotList_formSearch #dcct_id_tj").val();
	map["dccts"] = dcct;
	//调入储存单位  基础数据
	var drccdw = jQuery("#sampleAllotList_formSearch #drccdw_id_tj").val();
	map["drccdws"] = drccdw;
	//调入冰箱
	var drbx = jQuery("#sampleAllotList_formSearch #drbx_id_tj").val();
	map["drbxs"] = drbx;
	//调入抽屉
	var drct = jQuery("#sampleAllotList_formSearch #drct_id_tj").val();
	map["drcts"] = drct;
    //调拨日期开始
	var dbrqstart = jQuery("#sampleAllotList_formSearch #dbrqstart").val();
	map["dbrqstart"] = dbrqstart;
    //调拨日期结束
	var dbrqend = jQuery("#sampleAllotList_formSearch #dbrqend").val();
	map["dbrqend"] = dbrqend;

	return map;
}

//页面弹框事件
function SampleAllotDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = ($("#sampleAllotList_formSearch #urlPrefix").val()?$("#sampleAllotList_formSearch #urlPrefix").val():($("#urlPrefix").val()?$("#urlPrefix").val():"")) + tourl;
	if(action =='view'){
		var url = tourl + "?ybdbid=" +id;
		$.showDialog(url,'样本调拨查看',viewSampleAllotConfig);
	}else if(action =='mod'){
		var url = tourl + "?ybdbid=" +id;
		$.showDialog(url,'样本调拨修改',editSampleAllotConfig);
	}else if(action =='allocation'){
		var url = tourl
		$.showDialog(url,'样本调拨',editSampleAllotConfig);
	}else if(action =='submit'){
		var url = tourl
		$.showDialog(url,'样本调拨提交',editSampleAllotConfig);
	}
}

//操作按钮样式
function czFormat(value, row, index) {
    if (row.zt == '10' ) {
            return "<span class='btn btn-warning' onclick=\"recall('" + row.ybdbid +"',event)\" >撤回</span>";
        }else{
            return "";
        }
}
//调出冰箱名称
function dcbxFormat(value, row, index) {
    return (row.dcbxmc?row.dcbxmc:"-") + (row.dcbxsbh?"(" + row.dcbxsbh + ")":"");
}

//调入冰箱名称
function drbxFormat(value, row, index) {
    return (row.drbxmc?row.drbxmc:"-") + (row.drbxsbh?"(" + row.drbxsbh + ")":"");
}
//状态
function ztFormat(value, row, index) {
    if (row.zt == '00') {
            return '未提交';
        }else if (row.zt == '80') {
            return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.ybdbid + "\",event,\"AUTID_SAMPLE_ALLOT\",{prefix:\"" + $('#sampleAllotList_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
        }else if (row.zt == '15') {
            return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.ybdbid + "\",event,\"AUTID_SAMPLE_ALLOT\",{prefix:\"" + $('#sampleAllotList_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
        }else{
            return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.ybdbid + "\",event,\"AUTID_SAMPLE_ALLOT\",{prefix:\"" + $('#sampleAllotList_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
        }
}

//撤回提交
function recall(ybdbid,event){
	var auditType = $("#sampleAllotList_formSearch #auditType").val();
	var msg = '您确定要撤回吗？';
	var params = [];
	params.prefix = ($("#sampleAllotList_formSearch #urlPrefix").val()?$("#sampleAllotList_formSearch #urlPrefix").val():($("#urlPrefix").val()?$("#urlPrefix").val():""));
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,ybdbid,function(){
				searchSampleAllotResult();
			},params);
		}
	});
}