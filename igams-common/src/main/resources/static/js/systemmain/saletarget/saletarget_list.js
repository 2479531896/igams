var xszb_turnOff=true;
var Saletarget_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#saletarget_formSearch #saletarget_list").bootstrapTable({
			url: '/saletarget/saletarget/pageGetListSale',
            method: 'get',                      // 请求方式（*）
            toolbar: '#saletarget_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "xszb.lrsj",				// 排序字段
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
            uniqueId: "zbid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            },{
                field: 'zbid',
                title: '指标编号',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'yhid',
                title: '用户ID',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'zsxm',
                title: '姓名',
                width: '16%',
                align: 'left',
                visible: true,
				sortable: true
            },{
                field: 'zblxcsmc',
                title: '指标类型',
                width: '20%',
                align: 'left',
                visible:true,
				sortable: true
                
            },{
                field: 'zbflmc',
                title: '指标分类',
                width: '20%',
                align: 'left',
                visible:true,
                sortable: true
            },{
                field: 'zbzflmc',
                title: '指标子分类',
                width: '20%',
                align: 'left',
                visible:true,
				sortable: true
            },{
                field: 'kszq',
                title: '开始周期',
                width: '20%',
                align: 'left',
                visible:true,
				sortable: true
            },{
                field: 'jszq',
                title: '结束周期',
                width: '20%',
                align: 'left',
                visible:true,
				sortable: true
            },{
                field: 'sz',
                title: '数值',
                width: '20%',
                align: 'left',
                visible:true,
				sortable: true
            },{
                 field: 'sfqr',
                 title: '是否确认',
                 width: '20%',
                 align: 'left',
                 visible:true,
                 formatter:sfqrformat,
                 sortable: true
             }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	Saletarget_DealById(row.zbid,'view',$("#saletarget_formSearch #btn_view").attr("tourl"));
             },
		});
        $("#saletarget_formSearch #saletarget_list").colResizable({
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
            sortLastName: "xszb.xgsj", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getSaletargetSearchData(map);
	};
	return oTableInit;
}

//状态格式化
function sfqrformat(value,row,index){
    if(row.sfqr!="1"){
        var zt="<span style='color:red;'>未确认</span>";
    }else{
        var zt="<span style='color:green;'>已确认</span>";
    }
    return zt;
}


function getSaletargetSearchData(map){
	var Saletarget_select=$("#saletarget_formSearch #saletarget_select").val();
	var Saletarget_input=$.trim(jQuery('#saletarget_formSearch #saletarget_input').val());
	if(Saletarget_select=="0"){
		map["zsxm"]=Saletarget_input
	}else if(Saletarget_select=="1"){
		map["zblx"]=Saletarget_input
	}else if(Saletarget_select=="2"){
        map["zbflmc"]=Saletarget_input
    }else if(Saletarget_select=="3"){
        map["zbzflmc"]=Saletarget_input
    }else if(Saletarget_select=="4"){
        map["kszq"]=Saletarget_input
    }else if(Saletarget_select=="5"){
        map["jszq"]=Saletarget_input
    }

    //指标类型  基础数据
    var zblxs = jQuery('#saletarget_formSearch #zblx_id_tj').val();
    map["zblxs"] = zblxs;
    //指标分类 基础数据
    var zbfls = jQuery('#saletarget_formSearch #zbfl_id_tj').val();
    map["zbfls"] = zbfls;
    //指标子分类
    var zbzfls = jQuery('#saletarget_formSearch #zbzfl_id_tj').val();
    map["zbzfls"] = zbzfls;
	return map;
}



function searchSaletargetResult(isTurnBack){
    //关闭高级搜索条件
    $("#saletarget_formSearch #searchMore").slideUp("low");
    xszb_turnOff=true;
    $("#saletarget_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#saletarget_formSearch #saletarget_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#saletarget_formSearch #saletarget_list').bootstrapTable('refresh');
	}
}
function Saletarget_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = tourl;
	if(action=="view"){
		var url=tourl+"?zbid="+id
		$.showDialog(url,'查看销售指标',viewSaletargetConfig);
	}else if(action=="add"){
        var url=tourl;
        $.showDialog(url,'新增销售指标',addSaletargetConfig);
    }else if(action=="mod"){
        var url=tourl+"?zbid="+id
        $.showDialog(url,'修改销售指标',modSaletargetConfig);
    }
}

var viewSaletargetConfig = {
		width		: "800px",
		modalName	:"viewSaletargetModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
var addSaletargetConfig = {
    width		: "800px",
    modalName	:"addSaletargetModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                $("#ajaxForm #zbid").val($("#ajaxForm #zbid").val().trim());
                if(!$("#ajaxForm").valid()){// 表单验证
                    $.alert("请填写完整信息或规范填写");
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
                            searchSaletargetResult();
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {});
                    } else{
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"],function() {});
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

var modSaletargetConfig = {
    width		: "800px",
    modalName	:"modSaletargetModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                $("#ajaxForm #zbid").val($("#ajaxForm #zbid").val());
                if(!$("#ajaxForm").valid()){
                    $.alert("请填写完整信息或规范填写");
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
                            searchSaletargetResult();
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {});
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

var Saletarget_oButton=function(){
	var oButtonInit = new Object();
	var postdata = {};
	oButtonInit.Init=function(){
		var btn_query=$("#saletarget_formSearch #btn_query");
		var btn_view = $("#saletarget_formSearch #btn_view");
        var btn_del = $("#saletarget_formSearch #btn_del");
        var btn_add = $("#saletarget_formSearch #btn_add");
        var btn_mod = $("#saletarget_formSearch #btn_mod");

/*-----------------------模糊查询ok------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchSaletargetResult(true); 
    		});
		}
/*-----------------------查看------------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#saletarget_formSearch #saletarget_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			Saletarget_DealById(sel_row[0].zbid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
 /*-----------------------新增------------------------------------*/
        btn_add.unbind("click").click(function(){
            Saletarget_DealById(null,"add",btn_add.attr("tourl"));
        });
 /*-----------------------修改------------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#saletarget_formSearch #saletarget_list').bootstrapTable('getSelections');// 获取选择行数据
            if(sel_row.length==1){
                Saletarget_DealById(sel_row[0].zbid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
/*-----------------------删除------------------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#saletarget_formSearch #saletarget_list').bootstrapTable('getSelections');// 获取选择行数据
            if (sel_row.length==0) {
                $.error("请至少选中一行");
                return;
            }else{
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {// 循环读取选中行数据
                    ids= ids + ","+ sel_row[i].zbid;
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
                                        searchSaletargetResult();
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
/*-------------------------------------------------------------*/
        /**显示隐藏**/
        $("#saletarget_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(xszb_turnOff){
                $("#saletarget_formSearch #searchMore").slideDown("low");
                xszb_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#saletarget_formSearch #searchMore").slideUp("low");
                xszb_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
	}
	return oButtonInit;
}


$(function(){
	var oTable = new Saletarget_TableInit();
	    oTable.Init();
	
	var oButton = new Saletarget_oButton();
	    oButton.Init();
	    
    jQuery('#saletarget_formSearch .chosen-select').chosen({width: '100%'});
})