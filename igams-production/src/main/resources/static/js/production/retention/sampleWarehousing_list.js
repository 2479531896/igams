var sampleWarehousing_turnOff=true;
var sampleWarehousing_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#sampleWarehousing_formSearch #sampleWarehousing_list').bootstrapTable({
            url: $("#sampleWarehousing_formSearch #urlPrefix").val()+'/retention/retention/pageGetListSampleWarehousing',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#sampleWarehousing_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一s下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"wlgl.wlbm",					// 排序字段
            sortOrder: "ASC",                   // 排序方式
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
            uniqueId: "lykcid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '3%'
            }, {
                'field': '',
                'title': '序号',
                'align': 'center',
                'width': '4%',
                'sortable': 'true',
                'formatter': function (value, row, index) {
                    var options = $('#sampleWarehousing_formSearch #sampleWarehousing_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            }, {
                field: 'lyrkid',
                title: '留样入库id',
                width: '10%',
                align: 'left',
                visible: false,
                sortable: true
            }, {
                field: 'wlbm',
                title: '物料编码',
                width: '7%',
                align: 'left',
                visible: true,
                sortable: true,
                formatter:sampleWarehousing_wlbmformat
            }, {
                field: 'wlmc',
                title: '物料名称',
                width: '20%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'ychh',
                title: '产品编号',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'gg',
                title: '规格',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'jldw',
                title: '单位',
                width: '4%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'scph',
                title: '批号',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'sl',
                title: '数量',
                width: '5%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'lldh',
                title: '领料单号',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true,
                formatter: lldhformat,
            },{
                field: 'lrsj',
                title: '留样日期',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'lyry',
                title: '留样人',
                width: '5%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'jlbh',
                title: '记录编号',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'bctj',
                title: '贮存条件',
                width: '20%',
                align: 'left',
                visible: false,
                sortable: true
            },{
                field: 'scrq',
                title: '生产日期',
                width: '8%',
                align: 'left',
                visible: false,
                sortable: true
            },{
                field: 'yxq',
                title: '有效期',
                width: '8%',
                align: 'left',
                visible: false,
                sortable: true
            },
            ],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                sampleWarehousingById(row.lyrkid,'view',$("#sampleWarehousing_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#sampleWarehousing_formSearch #sampleWarehousing_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true}
        );
    };
    // 得到查询的参数
    oTableInit.queryParams = function(params){
        // 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
        // 例如 toolbar 中的参数
        // 如果 queryParamsType = ‘limit’ ,返回参数必须包含
        // limit, offset, search, sort, order
        // 否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
        // 返回false将会终止请求
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "yxq", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getsampleWarehousingSearchData(map);
    };
    return oTableInit;
};

function getsampleWarehousingSearchData(map) {
    var cxtj = $("#sampleWarehousing_formSearch #cxtj").val();
    var cxnr = $.trim(jQuery('#sampleWarehousing_formSearch #cxnr').val());
    if (cxtj == "0") {
        map["wlbm"] = cxnr
    }else if (cxtj == "1"){
        map["wlmc"] = cxnr
    }else if (cxtj == "2"){
        map["ychh"] = cxnr
    }else if (cxtj == "3"){
        map["scph"] = cxnr
    }else if (cxtj == "4"){
        map["lldh"] = cxnr
    }
    else if (cxtj == "5"){
        map["lyry"] = cxnr
    }
    else if (cxtj == "6"){
        map["jlbh"] = cxnr
    }
    else if (cxtj == "7"){
        map["entire"] = cxnr
    }
    // 留样开始日期
    var lysjstart = jQuery('#sampleWarehousing_formSearch #lysjstart').val();
    map["lysjstart"] = lysjstart;
    // 留样结束日期
    var lysjend = jQuery('#sampleWarehousing_formSearch #lysjend').val();
    map["lysjend"] = lysjend;
    return map;
}

/**
 * 列表刷新
 * @param isTurnBack
 */

function sampleWarehousingResult(isTurnBack){
    if(isTurnBack){
        $('#sampleWarehousing_formSearch #sampleWarehousing_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#sampleWarehousing_formSearch #sampleWarehousing_list').bootstrapTable('refresh');
    }
}

/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function sampleWarehousing_wlbmformat(value,row,index){
    var html = "";
    if(row.wlbm!=null && row.wlbm!=''){
        html += "<a href='javascript:void(0);' onclick=\"sampleWarehousing_queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
    }else {
        html +="-";
    }
    return html;
}
function sampleWarehousing_queryByWlbm(wlid){
    var url=$("#sampleWarehousing_formSearch #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料信息',viewWlConfig);
}

/**
 * 领料单号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function lldhformat(value,row,index){
    var html = "";
    if(row.lldh!=null && row.lldh!=''){
        html += "<a href='javascript:void(0);' onclick=\"viewllxx('"+row.llid+"')\">"+row.lldh+"</a>";
    }else {
        html +="-";
    }
    return html;
}
function viewllxx(llid) {
    var url=$("#sampleWarehousing_formSearch #urlPrefix").val()+"/storehouse/receiveMateriel/pagedataReceiveMateriel?llid="+llid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'领料详细信息',viewllxxConfig);
}
var viewWlConfig = {
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
/**
 * 领料查看
 */
var viewllxxConfig={
    width		: "1600px",
    modalName	:"viewllxxModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}

function sampleWarehousingById(id,action,tourl,data){
    if(!tourl){
        return;
    }
    tourl = $("#sampleWarehousing_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?lyrkid=" +id;
        $.showDialog(url,'详细信息',viewsampleWarehousingConfig);
    }else if(action =='mod'){
        var url= tourl + "?lyrkid=" +id+"&lrsj="+data;
        $.showDialog(url,'修改',modsampleWarehousingConfig);
    }
}

var modsampleWarehousingConfig = {
		width		: "700px",
		modalName	: "modsampleModal",
		formName	: "modsampleForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确定",
				className : "btn-primary",
				callback : function() {
					if(!$("#modsampleForm").valid()){
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};

					$("#modsampleForm input[name='access_token']").val($("#ac_tk").val());

					submitForm(opts["formName"]||"submitContractForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
						     $.success(responseText["message"],function() {
                                if(opts.offAtOnce){
                                    $.closeModal(opts.modalName);
                                    searchsampleWarehousingResult ();
                                }
                            });
						}else if(responseText["status"] == "fail"){
							$.error(responseText["message"],function() {
								preventResubmitForm(".modal-footer > button", false);
							});
						} else{
							$.alert(responseText["message"],function() {
								preventResubmitForm(".modal-footer > button", false);
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
var sampleWarehousing_ButtonInit = function(){
    var oInit = new Object();

    oInit.Init = function () {
        var btn_view = $("#sampleWarehousing_formSearch #btn_view");
        var btn_query = $("#sampleWarehousing_formSearch #btn_query");
        var btn_mod = $("#sampleWarehousing_formSearch #btn_mod");
        //添加日期控件
        laydate.render({
            elem: '#lysjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#lysjend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#yxqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#yxqsjend'
            ,theme: '#2381E9'
        });

        /*-----------------------模糊查询------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchsampleWarehousingResult(true);
            });
        }
        //---------------------------查看列表-----------------------------------
        btn_view.unbind("click").click(function(){
            var sel_row = $('#sampleWarehousing_formSearch #sampleWarehousing_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                sampleWarehousingById(sel_row[0].lyrkid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#sampleWarehousing_formSearch #sampleWarehousing_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                sampleWarehousingById(sel_row[0].lyrkid,"mod",btn_mod.attr("tourl"),sel_row[0].lrsj);
            }else{
                $.error("请选中一行");
            }
        });

        /**显示隐藏**/
        $("#sampleWarehousing_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(sampleWarehousing_turnOff){
                $("#sampleWarehousing_formSearch #searchMore").slideDown("low");
                sampleWarehousing_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#sampleWarehousing_formSearch #searchMore").slideUp("low");
                sampleWarehousing_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};


function searchsampleWarehousingResult(isTurnBack){
    //关闭高级搜索条件
    $("#sampleWarehousing_formSearch #searchMore").slideUp("low");
    sampleWarehousing_turnOff=true;
    $("#sampleWarehousing_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#sampleWarehousing_formSearch #sampleWarehousing_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#sampleWarehousing_formSearch #sampleWarehousing_list').bootstrapTable('refresh');
    }
}



var sfbc=0;//是否继续保存
var viewsampleWarehousingConfig = {
    width		: "800px",
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
    // 1.初始化Table
    var oTable = new sampleWarehousing_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new sampleWarehousing_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#sampleWarehousing_formSearch .chosen-select').chosen({width: '100%'});


    $("#sampleWarehousing_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});