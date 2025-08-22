var Ndz_turnOff=true;

var Ndz_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function (){
        $('#ndz_formSearch #ndz_List').bootstrapTable({
            url: '/crf/ndzview/getSelectPage',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#ndz_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "jldjt",				//排序字段
            sortOrder: "DESC",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 50,                       //每页的记录行数（*）
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
            uniqueId: "hzid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns:[{
                checkbox: true
            }, {
                    field: 'hzid',
                    title: '患者ID',
                    width: '8%',
                    align: 'left',
                    visible:false
                },{
                    field: 'ndzjlid',
                    title: '记录ID',
                    width: '8%',
                    align: 'left',
                    visible:false
                },{
                    field: 'hzxm',
                    title: '患者姓名',
                    width: '8%',
                    align: 'left',
                    visible:true
                },{
                    field: 'zyh',
                    title: '住院号',
                    width: '8%',
                    align: 'left',
                    visible:true
                },{
                    field: 'nryjbh',
                    title: '纳入研究编号',
                    width: '8%',
                    align: 'left',
                    visible:true
                },{
                    field: 'rysj',
                    title: '入院时间',
                    width: '8%',
                    align: 'left',
                    visible:true
                },{
                    field: 'nrsj',
                    title: '纳入时间',
                    width: '12%',
                    align: 'left',
                    visible:true
                },{
                    field: 'jlrq',
                    title: '记录日期',
                    width: '12%',
                    align: 'left',
                    visible:true
                },{
                    field: 'jldjt',
                    title: '诊断脓毒症第几日',
                    width: '10%',
                    align: 'left',
                    visible:true
                },{
                    field: 'hrmax',
                    title: 'HRmax(次/分钟)',
                    width: '10%',
                    align: 'left',
                    visible:true
                },{
                    field: 'mapmax',
                    title: 'MAPmax(mmHg)',
                    width: '12%',
                    align: 'left',
                    visible:true
                },{
                    field: 'sapmax',
                    title: 'SAPmax(mmHg)',
                    width: '12%',
                    align: 'left',
                    visible:true
                },{
                    field: 'rrmax',
                    title: 'RRmax(次/分)',
                    width: '10%',
                    align: 'left',
                    visible:true
                },{
                    field: 'tmax',
                    title: 'T(摄氏度)',
                    width: '8%',
                    align: 'left',
                    visible:true
                },{
                    field: 'jxtq',
                    title: '机械通气',
                    width: '8%',
                    align: 'left',
                    visible:true
                },{
                    field: 'xghxy',
                    title: '血管活性药',
                    width: '8%',
                    align: 'left',
                    visible:true
                },{
                    field: 'crrt',
                    title: 'CRRT',
                    width: '8%',
                    align: 'left',
                    visible:true
                },{
                    field: 'gcs',
                    title: 'GCS(分)',
                    width: '8%',
                    align: 'left',
                    visible:true
                }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
               NdzDealById(row.hzid,'view',$("#ndz_formSearch #btn_view").attr("tourl"),row.jldjt,row.ndzjlid);
            },
        });
      $("#ndz_formSearch #ndz_List").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
            }
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
            sortLastName: "hzid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getNdzSearchData(map);
    };
    return oTableInit;
}

function getNdzSearchData(map){
    var cxtj=$("#ndz_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#ndz_formSearch #cxnr').val());
    var jldjt=$.trim(jQuery('#ndz_formSearch #jldjt').val());
    if(cxtj=="0"){
        map["hzxm"]=cxnr
    }else if(cxtj=="1"){
        map["zyh"]=cxnr
    }else if(cxtj=="2"){
        map["nryjbh"]=cxnr
    }

    if(jldjt=="0"){
        map["jldjt"]="1"
    }else if(jldjt=="1"){
        map["jldjt"]="3"
    }else if(jldjt=="2"){
        map["jldjt"]="5"
    }else if(jldjt=="3"){
        map["jldjt"]="7"
    }

    // 记录日期
    var jlrqstart = jQuery('#ndz_formSearch #jlrqstart').val();
    map["jlrqstart"] = jlrqstart;
    var jlrqend = jQuery('#ndz_formSearch #jlrqend').val();
    map["jlrqend"] = jlrqend;
    // 纳入时间
    var nrsjstart = jQuery('#ndz_formSearch #nrsjstart').val();
    map["nrsjstart"] = nrsjstart;
    var nrsjend = jQuery('#ndz_formSearch #nrsjend').val();
    map["nrsjend"] = nrsjend;
    return map;
}

// 按钮动作函数
function NdzDealById(str,action,tourl,jldjt,ndzjlid){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?hzid="+str+ "&jldjt="+jldjt+"&ndzjlid="+ndzjlid;
        $.showDialog(url,'详情',viewHzxxConfig);
    }else if(action =='upload'){
        var url=tourl+ "?ndzjlid="+str;
        $.showDialog(url,'上传',uploadHzxxConfig);
    }
}

var Ndz_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_view = $("#ndz_formSearch #btn_view");//详情
        var btn_query =$("#ndz_formSearch #btn_query");//模糊查询
        var btn_upload =$("#ndz_formSearch #btn_upload");//上传

        //添加日期控件
        laydate.render({
            elem: '#jlrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#jlrqend'
            ,theme: '#2381E9'
        });
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchNdzResult(true);
            });
        };
        //添加日期控件
        laydate.render({
            elem: '#nrsjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#nrsjend'
            ,theme: '#2381E9'
        });
        /*---------------------------查看详情信息表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#ndz_formSearch #ndz_List').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                NdzDealById(sel_row[0].hzid,"view",btn_view.attr("tourl"),sel_row[0].jldjt,sel_row[0].ndzjlid);
            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------上传-----------------------------*/
        btn_upload.unbind("click").click(function(){
            var sel_row = $('#ndz_formSearch #ndz_List').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                NdzDealById(sel_row[0].ndzjlid,"upload",btn_upload.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /**显示隐藏**/
        $("#ndz_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(Ndz_turnOff){
                $("#ndz_formSearch #searchMore").slideDown("low");
                Ndz_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#ndz_formSearch #searchMore").slideUp("low");
                Ndz_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    };
    return oInit;
};

var uploadHzxxConfig = {
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
                                searchNdzResult();
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
/*查看详情信息模态框*/
var viewHzxxConfig = {
    width		: "1100px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function searchNdzResult(isTurnBack){
    //关闭高级搜索条件
    $("#ndz_formSearch #searchMore").slideUp("low");
    Ndz_turnOff=true;
    $("#ndz_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#ndz_formSearch #ndz_List').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#ndz_formSearch #ndz_List').bootstrapTable('refresh');
    }
}

$(function(){
    //0.界面初始化
    // 1.初始化Table
    var oTable = new Ndz_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new Ndz_ButtonInit();
    oButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#ndz_formSearch .chosen-select').chosen({width: '100%'});
    $("#ndz_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});
