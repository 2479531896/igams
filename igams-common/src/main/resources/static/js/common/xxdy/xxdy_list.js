var user_turnOff=true;
var Xxdy_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#xxdy_formSearch #xxdy_list").bootstrapTable({
			url: $("#xxdy_formSearch #urlPrefix").val()+'/xxdy/xxdy/pageGetListXxdy',
            method: 'get',                      // 请求方式（*）
            toolbar: '#xxdy_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "xxdy.dylx desc,xxdy.yxx desc,xxdy.dydm desc,xxdy.kzcs1 desc,xxdy.kzcs2 desc,xxdy.kzcs3 desc,xxdy.kzcs4 desc,xxdy.kzcs5 desc,xxdy.kzcs6",				// 排序字段
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
            uniqueId: "dyid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'dyid',
                title: '对应ID',
                width: '2%',
                align: 'left',
                visible:false
            },{
                field: 'dylx',
                title: '对应类型',
                width: '20%',
                align: 'left',
                visible: false
            },{
                field: 'dylxmc',
                title: '对应类型',
                width: '14%',
                align: 'left',
				sortable: true,
                visible: true
            },{
				field: 'px',
				title: '排序',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{
                field: 'yxx',
                title: '原信息',
                width: '26%',
                align: 'left',
				sortable: true,
                visible:true,
			},{
				field: 'zxx',
				title: '子信息',
				width: '20%',
				align: 'left',
				sortable: true,
				visible:true,

            },{
                field: 'dyxx',
                title: '对应信息ID',
                width: '20%',
                align: 'left',
                visible:false,
            },{
                field: 'dyxxmc',
                title: '对应信息',
                width: '20%',
                align: 'left',
				sortable: true,
                visible:true,
			},{
				field: 'dydm',
				title: '对应代码',
				width: '16%',
				align: 'left',
				sortable: true,
				visible:true,
			},{
				field: 'kzcs1',
				title: '扩展参数1',
				width: '10%',
				align: 'left',
				sortable: true,
				visible:true,
			},{
				field: 'kzcs2',
				title: '扩展参数2',
				width: '10%',
				align: 'left',
				sortable: true,
				visible:true,
			},{
				field: 'kzcs3',
				title: '扩展参数3',
				width: '10%',
				align: 'left',
				sortable: true,
				visible:true,
            },{
				field: 'kzcs4',
				title: '扩展参数4',
				width: '10%',
				align: 'left',
				sortable: true,
				visible:true,
			},{
				field: 'kzcs5',
				title: '扩展参数5',
				width: '10%',
				align: 'left',
				sortable: true,
				visible:true,
			},{
				field: 'kzcs6',
				title: '扩展参数6',
				width: '10%',
				align: 'left',
				sortable: true,
				visible:true,
			},{
				field: 'kzcs7',
				title: '扩展参数7',
				width: '10%',
				align: 'left',
				sortable: true,
				visible:true,
			},{
				field: 'kzcs8',
				title: '扩展参数8',
				width: '10%',
				align: 'left',
				sortable: true,
				visible:true,
			},{
				field: 'kzcs9',
				title: '扩展参数9',
				width: '10%',
				align: 'left',
				sortable: true,
				visible:false,
			}],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	Xxdy_DealById(row.dyid,'view',$("#xxdy_formSearch #btn_view").attr("tourl"));
             },
		});
        $("#xxdy_formSearch #xxdy_list").colResizable({
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
            sortLastName: "xxdy.dyid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getXxdySearchData(map);
	};
	return oTableInit;
}

function getXxdySearchData(map){
	var xxdy_select=$("#xxdy_formSearch #xxdy_select").val();
	var xxdy_input=$.trim(jQuery('#xxdy_formSearch #xxdy_input').val());
	var xxdy_select1=$("#xxdy_formSearch #xxdy_select1").val();
    var xxdy_input1=$.trim(jQuery('#xxdy_formSearch #xxdy_input1').val());
    var xxdy_select2=$("#xxdy_formSearch #xxdy_select2").val();
    var xxdy_input2=$.trim(jQuery('#xxdy_formSearch #xxdy_input2').val());
	if(xxdy_select=="0"){
		map["dylx"]=xxdy_input
	}else if(xxdy_select=="1"){
		map["yxx"]=xxdy_input
	}else if(xxdy_select=="2"){
		map["dyxx"]=xxdy_input
	}else if(xxdy_select=="3"){
		map["dydm"]=xxdy_input
	}else if(xxdy_select=="4"){
		map["kzcs1"]=xxdy_input
	}else if(xxdy_select=="5"){
		map["kzcs2"]=xxdy_input
	}else if(xxdy_select=="6"){
		map["kzcs3"]=xxdy_input
	}else if(xxdy_select=="7"){
		map["kzcs4"]=xxdy_input
	}else if(xxdy_select=="8"){
		map["kzcs5"]=xxdy_input
	}else if(xxdy_select=="9"){
		map["kzcs6"]=xxdy_input
	}else if(xxdy_select=="10"){
		map["kzcs7"]=xxdy_input
	}else if(xxdy_select=="11"){
		map["kzcs8"]=xxdy_input
	}

	if(xxdy_select1=="0"){
        map["dylx"]=xxdy_input1
    }else if(xxdy_select1=="1"){
        map["yxx"]=xxdy_input1
    }else if(xxdy_select1=="2"){
        map["dyxx"]=xxdy_input1
    }else if(xxdy_select1=="3"){
        map["dydm"]=xxdy_input1
    }else if(xxdy_select1=="4"){
        map["kzcs1"]=xxdy_input1
    }else if(xxdy_select1=="5"){
        map["kzcs2"]=xxdy_input1
    }else if(xxdy_select1=="6"){
        map["kzcs3"]=xxdy_input1
    }else if(xxdy_select1=="7"){
        map["kzcs4"]=xxdy_input1
    }else if(xxdy_select1=="8"){
        map["kzcs5"]=xxdy_input1
    }else if(xxdy_select1=="9"){
        map["kzcs6"]=xxdy_input1
    }else if(xxdy_select1=="10"){
		map["kzcs7"]=xxdy_input1
	}else if(xxdy_select1=="11"){
		map["kzcs8"]=xxdy_input1
	}

    if(xxdy_select2=="0"){
        map["dylx"]=xxdy_input2
    }else if(xxdy_select2=="1"){
        map["yxx"]=xxdy_input2
    }else if(xxdy_select2=="2"){
        map["dyxx"]=xxdy_input2
    }else if(xxdy_select2=="3"){
        map["dydm"]=xxdy_input2
    }else if(xxdy_select2=="4"){
        map["kzcs1"]=xxdy_input2
    }else if(xxdy_select2=="5"){
        map["kzcs2"]=xxdy_input2
    }else if(xxdy_select2=="6"){
        map["kzcs3"]=xxdy_input2
    }else if(xxdy_select2=="7"){
        map["kzcs4"]=xxdy_input2
    }else if(xxdy_select2=="8"){
        map["kzcs5"]=xxdy_input2
    }else if(xxdy_select2=="9"){
        map["kzcs6"]=xxdy_input2
    }else if(xxdy_select2=="10"){
		map["kzcs7"]=xxdy_input2
	}else if(xxdy_select2=="11"){
		map["kzcs8"]=xxdy_input2
	}
    var dylxs = jQuery('#xxdy_formSearch #dylx_id_tj').val();
    map["dylxs"] = dylxs;
    var dyxxs = jQuery('#xxdy_formSearch #dyxx_id_tj').val();
    map["dyxxs"] = dyxxs;
	return map;
}

function searchXxdyResult(isTurnBack){
	if(isTurnBack){
		$('#xxdy_formSearch #xxdy_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#xxdy_formSearch #xxdy_list').bootstrapTable('refresh');
	}
}

function Xxdy_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl=$("#xxdy_formSearch #urlPrefix").val()+tourl;
	if(action=="view"){
		var url=tourl+"?dyid="+id
		$.showDialog(url,'查看信息',viewXxdyConfig);
	}else if(action=="add"){
		var url=tourl
		$.showDialog(url,'新增信息',addXxdyConfig);
	}else if(action=="mod"){
		var url=tourl+"?dyid="+id
		$.showDialog(url,'修改信息',modXxdyConfig);
	}
}

var addXxdyConfig = {
	width		: "500px",
	modalName	:"addXxdyModal",
	offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var cskz = $("#ajaxForm #dylx").find("option:selected").attr("csdm");
				var zxx = $("#ajaxForm #zid").find("option:selected").attr("csmc");
				if (zxx){
					$("#ajaxForm #zxx").val(zxx);
				}
				$("#ajaxForm #cskz").val(cskz);
				if ("XM" == cskz||"XMCSS" == cskz){
					$("#ajaxForm #yxx").val($("#ajaxForm #yxxid").find("option:selected").attr("csmc"));
				}
				if(!$("#ajaxForm").valid()){// 表单验证
					$.alert("请填写完整信息");
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
							searchXxdyResult();
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

var modXxdyConfig = {
	width		: "500px",
	modalName	:"modXxdyModal",
	offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var cskz = $("#ajaxForm #dylx").find("option:selected").attr("csdm");
				var zxx = $("#ajaxForm #zid").find("option:selected").attr("csmc");
				if (zxx){
					$("#ajaxForm #zxx").val(zxx);
				}
				$("#ajaxForm #cskz").val(cskz);
				if ("XM" == cskz||"XMCSS" == cskz){
					$("#ajaxForm #yxx").val($("#ajaxForm #yxxid").find("option:selected").attr("csmc"));
				}
				$("#ajaxForm #dyid").val($("#ajaxForm #dyid").val());
				if(!$("#ajaxForm").valid()){
					$.alert("请填写完整信息");
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
							searchXxdyResult();
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

var viewXxdyConfig = {
	width		: "500px",
	modalName	:"viewXxdyModal",
	offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var Xxdy_oButton=function(){
	var oButtonInit = new Object();
	var postdata = {};
	oButtonInit.Init=function(){
		var btn_query=$("#xxdy_formSearch #btn_query");
		var btn_add = $("#xxdy_formSearch #btn_add");
		var btn_mod = $("#xxdy_formSearch #btn_mod");
		var btn_view = $("#xxdy_formSearch #btn_view");
		var btn_del = $("#xxdy_formSearch #btn_del");
		var btn_searchexport = $("#xxdy_formSearch #btn_searchexport");
		var btn_selectexport = $("#xxdy_formSearch #btn_selectexport");
		var dylxBind = $("#xxdy_formSearch #dylx_id ul li a");
		/*-----------------------模糊查询ok------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchXxdyResult(true);
    		});
		}
		/*-----------------------新增------------------------------------*/
		btn_add.unbind("click").click(function(){
				Xxdy_DealById(null,"add",btn_add.attr("tourl"));
    	});
		/*-----------------------修改------------------------------------*/
		btn_mod.unbind("click").click(function(){
    		var sel_row = $('#xxdy_formSearch #xxdy_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			Xxdy_DealById(sel_row[0].dyid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/*-----------------------查看------------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#xxdy_formSearch #xxdy_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			Xxdy_DealById(sel_row[0].dyid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*-----------------------删除------------------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#xxdy_formSearch #xxdy_list').bootstrapTable('getSelections');// 获取选择行数据
    		if (sel_row.length==0) {
				$.error("请至少选中一行");
				return;
			}else{
				var ids="";
    			for (var i = 0; i < sel_row.length; i++) {// 循环读取选中行数据
        			ids= ids + ","+ sel_row[i].dyid;
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定要删除所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= btn_del.attr("tourl");
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								searchXxdyResult();
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
		//---------------------------导出--------------------------------------------------
		btn_selectexport.unbind("click").click(function(){
			var sel_row = $('#xxdy_formSearch #xxdy_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].dyid;
				}
				ids = ids.substr(1);
				$.showDialog(exportPrepareUrl+"?ywid=XXDY_SELECT&expType=select&ids="+ids
					,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
			}else{
				$.error("请选择数据");
			}
		});

		btn_searchexport.unbind("click").click(function(){
			$.showDialog(exportPrepareUrl+"?ywid=XXDY_SEARCH&expType=search&callbackJs=xxdyExportData"
				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
		});

		if(dylxBind!=null){
            dylxBind.on("click", function(){
                setTimeout(function(){
                    getdyxxs();
                }, 10);
            });
        }
	}
	return oButtonInit;
}

function getdyxxs(){
    // 分类
    	var fl = jQuery('#xxdy_formSearch #dylx_id_tj').val();
    	if (!isEmpty(fl)) {
    		fl = "'" + fl + "'";
    		jQuery("#xxdy_formSearch #dyxx_id").removeClass("hidden");
    	}else{
    		jQuery("#xxdy_formSearch #dyxx_id").addClass("hidden");
    	}

        console.log(fl)


    	// 项目类别不为空
    	if (!isEmpty(fl)) {
            var arr=fl.split(",");
            var paramsArr=new Array();
             for(var i=0;i<arr.length;i++){
             var id="#dylx_id_"+arr[i].replaceAll("'","")
             console.log(id)
                var cskz1=$(id).attr("cskz1");
                var cskz2=$(id).attr("cskz2");
                var dylxcsdm=$(id).attr("dylxcsdm");
                var dto={dylxcsdm:dylxcsdm,cskz1:cskz1,cskz2:cskz2}
                paramsArr.push(dto)
             }
    		var url = "/xxdy/xxdy/pagedataDylxdeaList?access_token="+$("#ac_tk").val();
    		var requestBody={paramsArr}
    		$.ajax({
    			type : "POST",
    			url : url,
    			data : JSON.stringify(paramsArr),
    			dataType : "json",
    			contentType:"application/json;charset=UTF-8",
    			success : function(data){
    				if(data.list!=null&&data.list.length > 0) {
    					var html = "";
    					$.each(data.list,function(i){
    					    var obj=data.list[i]
    					    $.each(obj.resultList,function(j){
                                    html += "<li>";
                                    html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('dyxx','" + obj.resultList[j].csid + "','xxdy_formSearch');\" id=\"dyxx_id_" + obj.resultList[j].csid + "\"><span>" + obj.resultList[j].csmc + "</span></a>";
                                    html += "</li>";
                                jQuery("#xxdy_formSearch #ul_dyxx").html(html);
                                jQuery("#xxdy_formSearch #ul_dyxx").find("[name='more']").each(function(){
                                    $(this).on("click", s_showMoreFn);
                            });
                            });
    					});

    				} else {
    					jQuery("#xxdy_formSearch #ul_dyxx").empty();

    				}
    				jQuery("#xxdy_formSearch [id^='dyxx_li_']").remove();
    				$("#xxdy_formSearch #dyxx_id_tj").val("");
    			}
    		});
    	} else {
    		jQuery("#xxdy_formSearch #div_dyxx").empty();
    		jQuery("#xxdy_formSearch [id^='dyxx_li_']").remove();
    		$("#xxdy_formSearch #dyxx_id_tj").val("");
    	}
}
//提供给导出用的回调函数
function xxdyExportData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="xxdy.dyid";
	map["sortLastOrder"]="desc";
	map["sortName"]="xxdy.lrsj";
	map["sortOrder"]="desc";
	return getXxdySearchData(map);
}


$(function(){
	var oTable = new Xxdy_TableInit();
	oTable.Init();
	var oButton = new Xxdy_oButton();
	oButton.Init();
	$("#xxdy_formSearch [name='more']").each(function(){
        		$(this).on("click", s_showMoreFn);
        	});
    jQuery('#xxdy_formSearch .chosen-select').chosen({width: '100%'});
})

$("#xxdy_formSearch #sl_searchMore").on("click", function(ev){
    var ev=ev||event;
    if(user_turnOff){
        $("#xxdy_formSearch #searchMore").slideDown("low");
        user_turnOff=false;
        this.innerHTML="基本筛选";
    }else{
        $("#xxdy_formSearch #searchMore").slideUp("low");
        user_turnOff=true;
        this.innerHTML="高级筛选";
    }
    ev.cancelBubble=true;
});
function changoption(event){
    var val1=$("#xxdy_select1").val()
    var val2=$("#xxdy_select2").val()
    var val=$("#xxdy_select").val()
    $("#xxdy_select option").each(function(){
        $(this).attr("disabled",false)
    })
     $("#xxdy_select1 option").each(function(){
         $(this).attr("disabled",false)
     })
     $("#xxdy_select2 option").each(function(){
         $(this).attr("disabled",false)
     })
    if(val!='x'){
        $("#xxdy_select1_"+val).attr("disabled",true)
        $("#xxdy_select2_"+val).attr("disabled",true)
    }
    if(val1!='x'){
        $("#xxdy_select_"+val1).attr("disabled",true)
        $("#xxdy_select2_"+val1).attr("disabled",true)
    }
    if(val2!='x'){
        $("#xxdy_select_"+val2).attr("disabled",true)
        $("#xxdy_select1_"+val2).attr("disabled",true)
    }
    $("#xxdy_select_"+val).attr("selected","selected");
    $("#xxdy_select1_"+val1).attr("selected","selected");
    $("#xxdy_select2_"+val2).attr("selected","selected");

    $("#xxdy_formSearch #xxdy_select").trigger("chosen:updated");
    $("#xxdy_formSearch #xxdy_select1").trigger("chosen:updated");
    $("#xxdy_formSearch #xxdy_select2").trigger("chosen:updated");
}