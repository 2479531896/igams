///
var mater_turnOff=true;
var mater_choose_TableInit = function () {
    var oTableInit = new Object();
    $('#tab_020101').on('scroll',function(){
    	if($('#mater_choose_formSearch #tb_list').offset().top - 122 < 0){
    		if(!$('#mater_choose_formSearch .js-affix').hasClass("affix"))
    		{
    			$('#mater_choose_formSearch .js-affix').removeClass("affix-top").addClass("affix");
    		}
    		$('#mater_choose_formSearch .js-affix').css({
		    	'top': '100px',
		        "z-index":1000,
		        "width":'100%'
		      });
    	}else{
    		if(!$('#mater_choose_formSearch .js-affix').hasClass("affix-top"))
    		{
    			$('#mater_choose_formSearch .js-affix').removeClass("affix").addClass("affix-top");
    		}
    		$('#mater_choose_formSearch .js-affix').css({
		    	'top': '0px',
		        "z-index":1,
		        "width":'100%'
		      });
    	}
    })
    //初始化Table
    oTableInit.Init = function () {
        $('#mater_choose_formSearch #tb_list').bootstrapTable({
            url: $('#mater_choose_formSearch #urlPrefix').val() + '/production/materiel/pageGetListMater',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#mater_choose_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
//            paginationDetailHAlign: " hidden", //不显示底部文字
            sortable: true,                     //是否启用排序
            sortName: "wl.lrsj",				//排序字段
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
            uniqueId: "wlid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
				width: '5%',
            }, 
            {
                field: 'wlbm',
                title: '物料编码',
                titleTooltip:'物料编码',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true,
            }, {
                field: 'wlmc',
                title: '物料名称',
                titleTooltip:'物料名称',
                width: '18%',
                align: 'left',
                visible: true
            }, {
                field: 'lbmc',
                title: '类别',
                titleTooltip:'类别',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'scs',
                title: '生产商',
                titleTooltip:'生产商',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'gg',
                title: '规格',
                titleTooltip:'规格',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'jldw',
                title: '单位',
                titleTooltip:'单位',
                width: '4%',
                align: 'left',
                visible: true
			}],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
				materDealById(row.wlid, 'view',"/production/materiel/pagedataViewMater");
            },
        });
        $("#mater_choose_formSearch #tb_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true,
            pid:"mater_choose_formSearch"
            }		
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
            sortLastName: "wl.wlid", //防止同名排位用
			scbjs: "0", //防止同名排位用
            zts: "80", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        return getMaterSearchData(map);
    };
    return oTableInit;
};

function materDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $('#mater_choose_formSearch #urlPrefix').val() + tourl;
	if(action =='view'){
		var url= tourl + "?wlid=" +id;
		$.showDialog(url,'查看物料',viewMaterConfig);
	}
}

var viewMaterConfig = {
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

function getMaterSearchData(map){
	var cxtj = $("#mater_choose_formSearch #cxtj").val();
	// 查询条件
	var cxnr = $.trim(jQuery('#mater_choose_formSearch #cxnr').val());
	// '0':'物料编号','1':'物料名称','2':'生产商','3':'货号'
	if (cxtj == "0") {
		map["wlbm"] = cxnr;
	}else if (cxtj == "1") {
		map["wlmc"] = cxnr;
	}else if (cxtj == "2") {
		map["scs"] = cxnr;
	}else if (cxtj == "3") {
		map["ychh"] = cxnr;
	}else if (cxtj == "4") {
		map["lrryxm"] = cxnr;
    }else if (cxtj == "5") {
    	map["jwlbm"] = cxnr;
	}else if (cxtj == "6") {
    	map["entire"] = cxnr;
	}else if (cxtj == "7") {
        map["gg"] = cxnr;
    }
	// 物料类别
	var wllb = jQuery('#mater_choose_formSearch #wllb_id_tj').val();
	map["wllbs"] = wllb;
	// 物料子类别
	var wlzlb = jQuery('#mater_choose_formSearch #wlzlb_id_tj').val();
	map["wlzlbs"] = wlzlb;
	// 物料子类别
	var wlzlbtc = jQuery('#mater_choose_formSearch #wlzlbtc_id_tj').val();
	map["wlzlbtcs"] = wlzlbtc;
	
	// 是否危险品
	var sfwxps = jQuery('#mater_choose_formSearch #sfwxp_id_tj').val();
	map["sfwxps"] = sfwxps;
	// 通过开始日期
	var shsjstart = jQuery('#mater_choose_formSearch #shsjstart').val();
	map["shsjstart"] = shsjstart;
	
	// 通过结束日期
	var shsjend = jQuery('#mater_choose_formSearch #shsjend').val();
	map["shsjend"] = shsjend;
	
	//库存类别
	var lb = jQuery('#mater_choose_formSearch #lb_id_tj').val();
	map["lbs"] = lb;
	return map;
}
//提供给导出用的回调函数
function materSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="wl.wlid";
	map["sortLastOrder"]="asc";
	map["sortName"]="wl.wlbm";
	map["sortOrder"]="asc";
	return getMaterSearchData(map);
}








var mater_choose_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件

    	var btn_query = $("#mater_choose_formSearch #btn_query");
		var wllbBind = $("#mater_choose_formSearch #wllb_id ul li a");
    	//添加日期控件
    	laydate.render({
    	   elem: '#shsjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#shsjend'
    	  ,theme: '#2381E9'
    	});

    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchMaterResult(true);
    		});
    	}
    	
    	/**显示隐藏**/      
    	$("#mater_choose_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(mater_turnOff){
    			$("#mater_choose_formSearch #searchMore").slideDown("low");
    			mater_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#mater_choose_formSearch #searchMore").slideUp("low");
    			mater_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});
    	
    	//绑定物料类别的单击事件
    	if(wllbBind!=null){
    		wllbBind.on("click", function(){
    			setTimeout(function(){
    				getWlzlbs();
    			}, 10);
    		});
    	}
    };

    return oInit;
};

var mater_PageInit = function(){
    var oInit = new Object();
    return oInit;
}

//物料子类别的取得
function getWlzlbs() {
	// 物料类别
	var wllb = jQuery('#mater_choose_formSearch #wllb_id_tj').val();
	if (!isEmpty(wllb)) {
		wllb = "'" + wllb + "'";
		jQuery("#mater_choose_formSearch #wlzlb_id").removeClass("hidden");
	}else{
		jQuery("#mater_choose_formSearch #wlzlb_id").addClass("hidden");
	}
	// 项目类别不为空
	if (!isEmpty(wllb)) {
		var url = $('#mater_choose_formSearch #urlPrefix').val()+"/systemmain/data/ansyGetJcsjListInStop";
		$.ajax({
			type : "POST",
			url : url,
			data : {"fcsids":wllb,"access_token":$("#ac_tk").val()},
			dataType : "json",
			success : function(data){
				if(data.length > 0) {
					var html = "";
					$.each(data,function(i){
						html += "<li>";
						if(data[i].scbj=="2"){
							html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('wlzlb','" + data[i].csid + "','mater_choose_formSearch');\" id=\"wlzlb_id_" + data[i].csid + "\"><span style='color:red'>" + data[i].csmc + "(停用)" +"</span></a>";
						}else
							html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('wlzlb','" + data[i].csid + "','mater_choose_formSearch');\" id=\"wlzlb_id_" + data[i].csid + "\"><span>" + data[i].csmc + "</span></a>";
						html += "</li>";
					});
					jQuery("#mater_choose_formSearch #ul_wlzlb").html(html);
					jQuery("#mater_choose_formSearch #ul_wlzlb").find("[name='more']").each(function(){
						$(this).on("click", s_showMoreFn);
					});
				} else {
					jQuery("#mater_choose_formSearch #ul_wlzlb").empty();
					
				}
				jQuery("#mater_choose_formSearch [id^='wlzlb_li_']").remove();
				$("#mater_choose_formSearch #wlzlb_id_tj").val("");
			}
		});
	} else {
		jQuery("#mater_choose_formSearch #div_wlzlb").empty();
		jQuery("#mater_choose_formSearch [id^='wlzlb_li_']").remove();
		$("#mater_choose_formSearch #wlzlb_id_tj").val("");
	}
}

function searchMaterResult(isTurnBack){
	//关闭高级搜索条件
	$("#mater_choose_formSearch #searchMore").slideUp("low");
	mater_turnOff=true;
	$("#mater_choose_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#mater_choose_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#mater_choose_formSearch #tb_list').bootstrapTable('refresh');
	}
}



$(function(){


	
    //1.初始化Table
    var oTable = new mater_choose_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new mater_choose_ButtonInit();
    oButtonInit.Init();
	//所有下拉框添加choose样式
	jQuery('#mater_choose_formSearch .chosen-select').chosen({width: '100%'});
	
	// 初始绑定显示更多的事件
	//var showMore = function() {
	$("#mater_choose_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
});
