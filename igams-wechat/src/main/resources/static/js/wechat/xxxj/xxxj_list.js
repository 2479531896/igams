var Xxxj_turnOff=true;

var Sale_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#xxxjlist_formSearch #tb_list').bootstrapTable({
            url: '/xxxj/xxxj/pageGetListXxxj',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#salelist_formSearch #toolbar', //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName:"xxxj.lrsj",					//排序字段
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
            uniqueId: "xjid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            }, {
                field: 'xjid',
                title: '小结ID',
                width: '24%',
                align: 'left',
                visible: false
            }, {
                field: 'xjlxmc',
                title: '类型',
                titleTooltip:'类型',
                width: '24%',
                align: 'left',
                visible: true
            }, {
                 field: 'xjmc',
                 title: '小结名称',
                 titleTooltip:'小结名称',
                 width: '24%',
                 align: 'left',
                 visible: true
             }, {
                field: 'xjnr',
                title: '小结内容',
                titleTooltip:'小结内容',
                width: '24%',
                align: 'left',
                visible: true
            }, {
                field: 'lrrymc',
                title: '录入人员',
                titleTooltip:'录入人员',
                width: '24%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	XxxjDealById(row.xjid, 'view',$("#xxxjlist_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#xxxjlist_formSearch #tb_list").colResizable({
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
            sortLastName: "xxxj.xjid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var cxbt = $("#xxxjlist_formSearch #cxtj").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#xxxjlist_formSearch #cxnr').val());
    	// '0':'审核名称','1':'描述',
    	if (cxbt == "0") {
    		map["xjmc"] = cxnr;
    	}else if (cxbt == "1") {
            map["lrrymc"] = cxnr;
        }
        var cxxjlxs = jQuery('#xxxjlist_formSearch #xjlx_id_tj').val();
        map["xjlxs"] = cxxjlxs;

        var lrsjstart = jQuery('#xxxjlist_formSearch #lrsjstart').val();
        	map["lrsjstart"] = lrsjstart;
        var lrsjend = jQuery('#xxxjlist_formSearch #lrsjend').val();
        	map["lrsjend"] = lrsjend;
    	return map;
    };
    return oTableInit;
};

$("#xxxjlist_formSearch #sl_searchMore").on("click", function(ev){
	var ev=ev||event;
	if(Xxxj_turnOff){
		$("#xxxjlist_formSearch #searchMore").slideDown("low");
		Xxxj_turnOff=false;
		this.innerHTML="基本筛选";
	}else{
		$("#xxxjlist_formSearch #searchMore").slideUp("low");
		Xxxj_turnOff=true;
		this.innerHTML="高级筛选";
	}
	ev.cancelBubble=true;
});

//按钮动作函数
function XxxjDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
        var url= tourl + "?xjid=" +id;
        $.showDialog(url,'查看信息小结',viewXxxjConfig);
    }else if(action =='add'){
        var url= tourl;
        $.showDialog(url,'新增信息小结',addXxxjConfig);
    }else if(action =='mod'){
        var url=tourl + "?xjid=" +id;
        $.showDialog(url,'编辑信息小结',addXxxjConfig);
    }
}

var addXxxjConfig = {
	width		: "1000px",
	modalName	: "addXxxjModal",
	formName	: "addUpdateXxxjForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
                console.log(3321)
				if(!$("#addUpdateXxxjForm").valid()){
					return false;
				}
				if($("#addUpdateXxxjForm #xjmc").val()==null || $("#addUpdateXxxjForm #xjmc").val()==""){
				    $.alert("小结名称不能为空！");
                		return false;
				}
				if($("#addUpdateXxxjForm #xjlx").val()==null || $("#addUpdateXxxjForm #xjlx").val()==""){
                    $.alert("请选择小结类型！");
                        return false;
                }
				if($("#addUpdateXxxjForm #xjnr").val()==null || $("#addUpdateXxxjForm #xjnr").val()==""){
                    $.alert("小结内容不能为空！");
                    return false;
                }
				var $this = this;
				var opts = $this["options"]||{};

				$("#addUpdateXxxjForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"addUpdateXxxjForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchXxxjResult();
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

var viewXxxjConfig = {
	width		: "600px",
	modalName	: "viewXxxjModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
	buttons		: {

		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var Xxxj_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_add = $("#xxxjlist_formSearch #btn_add");
        var btn_mod = $("#xxxjlist_formSearch #btn_mod");
        var btn_del = $("#xxxjlist_formSearch #btn_del");
        var btn_view = $("#xxxjlist_formSearch #btn_view");
        var btn_query = $("#xxxjlist_formSearch #btn_query");

    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchXxxjResult(true);
    		});
    	}
        btn_add.unbind("click").click(function(){
            XxxjDealById(null,"add",btn_add.attr("tourl"));
        });
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#xxxjlist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                XxxjDealById(sel_row[0].xjid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        btn_view.unbind("click").click(function(){
            var sel_row = $('#xxxjlist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                XxxjDealById(sel_row[0].xjid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        btn_del.unbind("click").click(function(){
            var sel_row = $('#xxxjlist_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].xjid;
            }
            ids = ids.substr(1);
            $.confirm('您确定要删除所选择的记录吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url= btn_del.attr("tourl");
                    jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    searchXxxjResult();
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

function searchXxxjResult(isTurnBack){
	if(isTurnBack){
		$('#xxxjlist_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#xxxjlist_formSearch #tb_list').bootstrapTable('refresh');
	}
}
laydate.render({
   elem: '#lrsjstart'
  ,theme: '#2381E9'
});
laydate.render({
   elem: '#lrsjend'
  ,theme: '#2381E9'
});
$(function(){

    //1.初始化Table
    var oTable = new Sale_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new Xxxj_ButtonInit();
    oButtonInit.Init();
    $("#xxxjlist_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	//所有下拉框添加choose样式
	jQuery('#xxxjlist_formSearch .chosen-select').chosen({width: '100%'});
});
