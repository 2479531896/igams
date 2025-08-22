var sampleStock_turnOff=true;
var sampleStock_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#sampleStock_formSearch #sampleStock_list').bootstrapTable({
            url: $("#sampleStock_formSearch #urlPrefix").val()+'/retention/retention/pageGetListSampleStock',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#sampleStock_formSearch #toolbar', // 工具按钮用哪个容器
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
                width: '4%'
            }, {
                'field': '',
                'title': '序号',
                'align': 'center',
                'width': '4%',
                'sortable': 'true',
                'formatter': function (value, row, index) {
                    var options = $('#sampleStock_formSearch #sampleStock_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            }, {
                field: 'lykcid',
                title: '留样库存id',
                width: '10%',
                align: 'left',
                visible: false,
                sortable: true
            }, {
                field: 'wlbm',
                title: '物料编码',
                width: '5%',
                align: 'left',
                visible: true,
                sortable: true,
                formatter:sampleStock_wlbmformat
            }, {
                field: 'wlmc',
                title: '物料名称',
                width: '25%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'ychh',
                title: '产品编号',
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
                field: 'gg',
                title: '规格',
                width: '8%',
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
                field: 'kcl',
                title: '库存量',
                width: '5%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'bctj',
                title: '贮存条件',
                width: '13%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'scrq',
                title: '生产日期',
                width: '7%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'yxq',
                title: '有效期',
                width: '7%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'scgcrq',
                title: '上次观察日期',
                width: '7%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'gcjl',
                title: '观察记录',
                width: '3%',
                align: 'left',
                visible: true,
                formatter:gcjlformat,
                sortable: true
            },{
                field: 'cz',
                title: '操作',
                width: '5%',
                align: 'left',
                visible: true,
                formatter:czformat,
                sortable: true
            },
            ],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                sampleStockById(row.lykcid,'view',$("#sampleStock_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#sampleStock_formSearch #sampleStock_list").colResizable({
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
        return getSampleStockSearchData(map);
    };
    return oTableInit;
};

function getSampleStockSearchData(map) {
    var cxtj = $("#sampleStock_formSearch #cxtj").val();
    var cxnr = $.trim(jQuery('#sampleStock_formSearch #cxnr').val());
    if (cxtj == "0") {
        map["wlbm"] = cxnr
    }else if (cxtj == "1"){
        map["wlmc"] = cxnr
    }else if (cxtj == "2"){
        map["ychh"] = cxnr
    }else if (cxtj == "3"){
        map["scph"] = cxnr
    }else if (cxtj == "4"){
        map["bctj"] = cxnr
    }else if (cxtj == "5"){
        map["entire"] = cxnr
    }
    // 生产开始日期
    var cssjstart = jQuery('#sampleStock_formSearch #cssjstart').val();
    map["cssjstart"] = cssjstart;
    // 生产结束日期
    var cssjend = jQuery('#sampleStock_formSearch #cssjend').val();
    map["cssjend"] = cssjend;
    // 有限期开始日期
    var yxqstart = jQuery('#sampleStock_formSearch #yxqstart').val();
    map["yxqstart"] = yxqstart;
    // 有效期结束日期
    var yxqsjend = jQuery('#sampleStock_formSearch #yxqsjend').val();
    map["yxqsjend"] = yxqsjend;
    // 上次观察日期开始日期
    var scgcrqstart = jQuery('#sampleStock_formSearch #scgcrqstart').val();
    map["scgcrqstart"] = scgcrqstart;
    // 上次观察日期结束日期
    var scgcrqend = jQuery('#sampleStock_formSearch #scgcrqend').val();
    map["scgcrqend"] = scgcrqend;
    // 库存
    var kczt = jQuery('#sampleStock_formSearch #kczt_id_tj').val();
    map["kczt"] = kczt;
    // 留样小结
    var sflyxj = jQuery('#sampleStock_formSearch #sflyxj_id_tj').val();
    map["sflyxj"] = sflyxj;
    return map;
}

/**
 * 列表刷新
 * @param isTurnBack
 */

function sampleStockResult(isTurnBack){
    if(isTurnBack){
        $('#sampleStock_formSearch #sampleStock_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#sampleStock_formSearch #sampleStock_list').bootstrapTable('refresh');
    }
}
/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function sampleStock_wlbmformat(value,row,index){
    var html = "";
    if(row.wlbm!=null && row.wlbm!=''){
        html += "<a href='javascript:void(0);' onclick=\"sampleStock_queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
    }else {
        html +="-";
    }
    return html;
}
function sampleStock_queryByWlbm(wlid){
    var url=$("#sampleStock_formSearch #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料信息',viewWlConfig);
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
 * 操作格式化
 * @returns
 */
function czformat(value,row,index) {
    if($("#sampleStock_formSearch #btn_sampling").length>0){
        if (row.kcl>0){
            return "<div><span class='btn btn-info' title='取样' onclick=\"yq('" + row.lykcid + "',event)\" >取样</span></div>";
        }
    }
    return "";
}

function gcjlformat(value,row,index) {
    var html="";
    if(row.gcjl=="是"){
        html="<span style='color:green;'>"+"是"+"</span>";
    }else{
        html="<span style='color:red;'>"+"否"+"</span>";
    }
    return html;
}
/**
 * 选择取样人列表
 * @returns
 */
function yq(lykcid,event){
    sampleStockById(lykcid,"sampling",$("#sampleStock_formSearch #btn_sampling").attr("tourl"));
}

function sampleStockById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#sampleStock_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?lykcid=" +id;
        $.showDialog(url,'详细信息',viewsampleStockConfig);
    }else if(action =='sampling'){
        var url= tourl + "?lykcid=" +id;
        $.showDialog(url,'取样',samplingsampleStockConfig);
    }
     else if(action=='inspectionrecords'){
        var url=tourl+"?ids="+id;
        $.showDialog(url,'观察要求',inspectionRecordsConfig);
    }
    else if(action=='samplesummary'){
        var url=tourl+"?lykcid="+id;
        $.showDialog(url,'留样小结',sampleSummaryConfig);
    }
    else if(action=='quality'){
        var url=tourl+"?lykcid="+id;
        $.showDialog(url,'质检设置',qualitySettingConfig);
    }
}


var sampleStock_ButtonInit = function(){
    var oInit = new Object();

    oInit.Init = function () {
        var btn_view = $("#sampleStock_formSearch #btn_view");
        var btn_query = $("#sampleStock_formSearch #btn_query");
        var btn_inspectionrecords = $("#sampleStock_formSearch #btn_inspectionrecords");
	    var btn_sampling = $("#sampleStock_formSearch #btn_sampling");
        var btn_samplesummary = $("#sampleStock_formSearch #btn_samplesummary");
        var btn_quality = $("#sampleStock_formSearch #btn_quality");
        //添加日期控件
        laydate.render({
            elem: '#cssjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#cssjend'
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
        //添加日期控件
        laydate.render({
            elem: '#scgcrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#scgcrqend'
            ,theme: '#2381E9'
        });

        /*-----------------------模糊查询------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchSampleStockResult(true);
            });
        }
        //---------------------------查看列表-----------------------------------
        btn_view.unbind("click").click(function(){
            var sel_row = $('#sampleStock_formSearch #sampleStock_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                sampleStockById(sel_row[0].lykcid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        //--------------------------质检设置-----------------------------------
        btn_quality.unbind("click").click(function(){
            var sel_row = $('#sampleStock_formSearch #sampleStock_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                sampleStockById(sel_row[0].lykcid,"quality",btn_quality.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        //---------------------------观察要求-----------------------------------
        btn_inspectionrecords.unbind("click").click(function(){
            var sel_row = $('#sampleStock_formSearch #sampleStock_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>0){
                var ids="";
                for(var i = 0; i < sel_row.length; i++){
                    ids= ids + ","+ sel_row[i].lykcid;
                }
                ids=ids.substr(1);
                sampleStockById(ids,"inspectionrecords",btn_inspectionrecords.attr("tourl"));
            }else{
                $.error("请至少选中一行");
            }
        });
	        //---------------------------取样-----------------------------------
        btn_sampling.unbind("click").click(function(){
            var sel_row = $('#sampleStock_formSearch #sampleStock_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if (sel_row[0].kcl>0){
                    sampleStockById(sel_row[0].lykcid,"sampling",btn_sampling.attr("tourl"));
                }else {
                    $.error("库存量不足");
                }
            }else{
                $.error("请选中一行");
            }
        });
        //---------------------------留样小结-----------------------------------
        btn_samplesummary.unbind("click").click(function(){
            var sel_row = $('#sampleStock_formSearch #sampleStock_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                sampleStockById(sel_row[0].lykcid,"samplesummary",btn_samplesummary.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /**显示隐藏**/
        $("#sampleStock_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(sampleStock_turnOff){
                $("#sampleStock_formSearch #searchMore").slideDown("low");
                sampleStock_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#sampleStock_formSearch #searchMore").slideUp("low");
                sampleStock_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};


function searchSampleStockResult(isTurnBack){
    //关闭高级搜索条件
    $("#sampleStock_formSearch #searchMore").slideUp("low");
    sampleStock_turnOff=true;
    $("#sampleStock_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#sampleStock_formSearch #sampleStock_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#sampleStock_formSearch #sampleStock_list').bootstrapTable('refresh');
    }
}



var sfbc=0;//是否继续保存
var viewsampleStockConfig = {
    width		: "1600px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var qualitySettingConfig = {
    width		: "1300px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var inspectionRecordsConfig={
    width		: "800px",
    modalName	: "inspectionRecordsModal",
    formName	: "inspectionRecordsForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#inspectionRecordsForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#inspectionRecordsForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"inspectionRecordsForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchSampleStockResult ();
                            }
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

var sampleSummaryConfig={
    width		: "800px",
    modalName	: "sampleSummaryModal",
    formName	: "sampleSummaryForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#sampleSummaryForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#sampleSummaryForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"sampleSummaryForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchSampleStockResult ();
                            }
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
var samplingsampleStockConfig={
    width		: "1200px",
    modalName	: "samplingModal",
    formName	: "samplingForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#samplingForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#samplingForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"samplingForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchSampleStockResult ();
                            }
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
var sampleStock_PageInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        addTj('kczt','1','sampleStock_formSearch');
        addTj('sflyxj','0','sampleStock_formSearch');
    }
    return oInit;
}
$(function(){
    var oInit = new sampleStock_PageInit();
    oInit.Init();
    //0.界面初始化
    // 1.初始化Table
    var oTable = new sampleStock_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new sampleStock_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#sampleStock_formSearch .chosen-select').chosen({width: '100%'})
    $("#sampleStock_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});