var Xpxx_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#xpxx_formSearch #xpxx_list").bootstrapTable({
			url: '/chipinfo/chipinfo/pageGetListChipinfo',
            method: 'get',                      // 请求方式（*）
            toolbar: '#xpxx_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "xpxx.lrsj",				// 排序字段
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
            uniqueId: "xpid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'xpid',
                title: '芯片ID',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'xpm',
                title: '芯片名',
                width: '20%',
                align: 'left',
                visible: true
            },{
                field: 'cxkssj',
                title: '测序开始时间',
                width: '10%',
                align: 'left',
                visible:true,
                
            },{
                field: 'cxjssj',
                title: '测序结束时间',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'sfsdcx',
                title: '是否双端测序',
                width: '10%',
                align: 'left',
                formatter:sfformat,
                visible:false
            },{
                field: 'dc',
                title: '读长',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'cxmd',
                title: '测序目的',
                width: '10%',
                align: 'left',
                formatter:cxmdformat,
                visible:false
            },{
                field: 'fxkssj',
                title: '分析开始时间',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'fxjssj',
                title: '分析结束时间',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'cxymc',
                title: '测序仪',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'wksm',
                title: '文库数目',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'fxjd',
                title: '分析节点',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'zt',
                title: '状态',
                width: '8%',
                align: 'left',
                formatter:ztformat,
                visible:true
            },{
                field: 'bz',
                title: '备注',
                width: '12%',
                align: 'left',
                visible:true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	Xpxx_DealById(row.xpid,'view',$("#xpxx_formSearch #btn_view").attr("tourl"));
             },
		});
        $("#xpxx_formSearch #xpxx_list").colResizable({
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
            sortLastName: "xpxx.xpid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getXpxxSearchData(map);
	};
	return oTableInit;
}

function sfformat(value,row,index){
	if(row.sfsdcx =='1'){
		return '是';
	}else if(row.sfsdcx =='0'){
		return '否';
	}else {
		return '--';
	}
}

function cxmdformat(value,row,index){
    if(row.cxmd =='1' ){
        return '加分析';
    }else if(row.cxmd =='0'){
        return '仅测序';
    }else {
        return '--';
    }
}


function ztformat(value,row,index){
    if(row.zt =='50'){
        return '测序中';
    }else if(row.zt =='51'){
        return '测序完成';
    }else if(row.zt =='52'){
        return '数据分析中';
    }else if(row.zt =='53'){
        return '分析完成';
    }else if(row.zt =='54'){
        return '审核完成';
    }else {
        return '--';
    }
}

function getXpxxSearchData(map){
	var xpxx_select=$("#xpxx_formSearch #xpxx_select").val();
	var xpxx_input=$.trim(jQuery('#xpxx_formSearch #xpxx_input').val());
	if(xpxx_select=="0"){
		map["xpm"]=xpxx_input
	}else if(xpxx_select=="1"){
        map["cxymc"]=xpxx_input
    }
	return map;
}



function searchXpxxResult(isTurnBack){
	if(isTurnBack){
		$('#xpxx_formSearch #xpxx_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#xpxx_formSearch #xpxx_list').bootstrapTable('refresh');
	}
}
function Xpxx_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action=="view"){
		var url=tourl+"?xpid="+id;
		$.showDialog(url,'查看芯片信息',viewXpxxConfig);
	}else if(action=="add"){
        var url=tourl;
        $.showDialog(url,'新增芯片信息',editXpxxConfig);
    }else if(action=="mod"){
        var url=tourl+"?xpid="+id;
        $.showDialog(url,'修改芯片信息',editXpxxConfig);
    }
}

var viewXpxxConfig = {
		width		: "1100px",
		modalName	:"viewXpxxModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

var editXpxxConfig = {
    width		: "1000px",
    modalName	:"editXpxxModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editChipForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#editChipForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"editChipForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchXpxxResult();
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


var Xpxx_oButton=function(){
	var oButtonInit = new Object();
	var postdata = {};
	oButtonInit.Init=function(){
		var btn_query=$("#xpxx_formSearch #btn_query");
		var btn_view = $("#xpxx_formSearch #btn_view");
        var btn_add = $("#xpxx_formSearch #btn_add");
        var btn_mod = $("#xpxx_formSearch #btn_mod");
/*-----------------------模糊查询ok------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchXpxxResult(true); 
    		});
		}
/*-----------------------查看------------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#xpxx_formSearch #xpxx_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			Xpxx_DealById(sel_row[0].xpid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
        /*--------------------------------新增---------------------------*/
        btn_add.unbind("click").click(function(){
            Xpxx_DealById(null,"add",btn_add.attr("tourl"));
        });
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#xpxx_formSearch #xpxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                Xpxx_DealById(sel_row[0].xpid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
/*-------------------------------------------------------------*/
	}
	return oButtonInit;
}


$(function(){
	var oTable = new Xpxx_TableInit();
	    oTable.Init();
	
	var oButton = new Xpxx_oButton();
	    oButton.Init();
	    
    jQuery('#xpxx_formSearch .chosen-select').chosen({width: '100%'});
})