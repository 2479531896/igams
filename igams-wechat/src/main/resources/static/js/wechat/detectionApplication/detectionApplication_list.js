var detectionApplication_turnOff=true;
// 列表初始化
var detectionApplication_TableInit=function(){
    var oTableInit=new Object();
    // 初始化table
    oTableInit.Init=function(){
        $("#detectionApplication_formSearch #detectionApplication_list").bootstrapTable({
            url: '/application/application/pageGetListDetectionApplication',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#detectionApplication_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "jcsqgl.lrsj",					//排序字段
            sortOrder: "desc",               	//排序方式
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
            //height: 500,                     	//行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "sqglid",                	//每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                	//是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%',
            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'sqglid',
                title: '申请管理ID',
                titleTooltip:'申请管理ID',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'sqlxmc',
                title: '申请类型',
                titleTooltip:'申请类型',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'hb',
                title: '合作伙伴',
                titleTooltip:'合作伙伴',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'sqyy',
                title: '申请原因',
                titleTooltip:'申请原因',
                width: '15%',
                align: 'left',
                visible: true
            },{
                field: 'xqsm',
                title: '需求说明',
                titleTooltip:'需求说明',
                width: '15%',
                align: 'left',
                visible: true
            },{
                field: 'ybxx',
                title: '样本信息',
                titleTooltip:'样本信息',
                width: '15%',
                align: 'left',
                visible: true
            },{
                field: 'wkxx',
                title: '文库信息',
                titleTooltip:'文库信息',
                width: '15%',
                align: 'left',
                visible: true
            },{
                field: 'lx',
                title: '类型',
                titleTooltip:'类型',
                width: '5%',
                align: 'left',
                formatter:lxFormat,
                sortable: true,
                visible: true
            },{
                field: 'lrrymc',
                title: '申请人员',
                titleTooltip:'申请人员',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'lrsj',
                title: '申请时间',
                titleTooltip:'申请时间',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zt',
                title: '状态',
                titleTooltip:'状态',
                width: '8%',
                formatter:ztFormat,
                align: 'left',
                sortable: true,
                visible: true
            },{
             field: 'zt',
             title: '钉钉审批状态',
             titleTooltdip:'钉钉审批状态',
             width: '8%',
             formatter:ddztFormat,
             align: 'left',
             sortable: true,
             visible: true
         },{
                field: 'cz',
                title: '操作',
                titleTooltip:'操作',
                width: '5%',
                align: 'left',
                formatter:czformat,
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                // 双击事件
                detectionApplicationDealById(row.sqglid,'view',$("#detectionApplication_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#detectionApplication_formSearch #detectionApplication_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        })
    };
    // 得到查询的参数
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "jcsqgl.sqglid", // 防止同名排位用
            sortLastOrder: "desc", // 防止同名排位用
            tab_flag:  $("#detectionApplication_formSearch #tab_flag").val()
            // 搜索框使用
            // search:params.search
        };
        return detectionApplicationSearchData(map);
    };
    return oTableInit
};




function ztFormat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sqglid + "\",event,\"AUDIT_DETECTIONAPPLICATION\")' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sqglid + "\",event,\"AUDIT_DETECTIONAPPLICATION\")' >审核未通过</a>";
    }else if(row.zt == '10'){
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sqglid + "\",event,\"AUDIT_DETECTIONAPPLICATION\")' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}

function ddztFormat(value,row,index) {
    if (row.zt == '00') {
        return '暂无';
    }else{
    return "<a href='javascript:void(0);' onclick='showDdsp(\"" + row.sqglid + "\",event,\"AUDIT_DETECTIONAPPLICATION\")' >查看钉钉审批流程</a>";
    }
}
function showDdsp(sqglid,event,shlb){
    var url="/application/application/pagedataViewDd?sqglid="+sqglid;
    $.showDialog(url,'钉钉审批流程',ddsplcConfig);
}
function lxFormat(value,row,index) {
    if (row.lx == '1') {
        return "<span>自定义<span>";
    }else if (row.lx == '0') {
        return "<span>清单<span>";
    }
}


/**
 * 操作按钮格式化函数
 * @returns
 */
function czformat(value,row,index) {
    if (row.zt == '10' ) {
        return "<span class='btn btn-warning' onclick=\"recallRequisitions('" + row.sqglid +"','" + "AUDIT_DETECTIONAPPLICATION"+ "',event)\" >撤回</span>";
    }else{
        return "";
    }
}

function recallRequisitions(sqglid,auditType,event){
    var msg = '您确定要撤回该记录吗？';
    var purchase_params = [];
    $.confirm(msg,function(result){
        if(result){
            doAuditRecall(auditType,sqglid,function(){
                searchDetectionApplicationResult();
            },purchase_params);
        }
    });
}
function recallRequisitions_del(sqglid,auditType){
    var purchase_params = [];
    doAuditRecallnoback(auditType,sqglid,function(){
    },purchase_params);
}
// 条件搜索
function detectionApplicationSearchData(map){
    var cxtj=$("#detectionApplication_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#detectionApplication_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr
    }else if(cxtj=="1"){
        map["hb"]=cxnr
    }else if(cxtj=="2"){
        map["sqyy"]=cxnr
    }else if(cxtj=="3"){
        map["xqsm"]=cxnr
    }else if(cxtj=="4"){
        map["wkxx"]=cxnr
    }

    var sqlxs = jQuery('#detectionApplication_formSearch #sqlx_id_tj').val();
    map["sqlxs"] = sqlxs;
    var lxs=jQuery('#detectionApplication_formSearch #lx_id_tj').val();
    map["lxs"]=lxs;
    var zts=jQuery('#detectionApplication_formSearch #zt_id_tj').val();
    map["zts"]=zts;
    // 开始日期
    var lrsjstart = jQuery('#detectionApplication_formSearch #lrsjstart').val();
    map["lrsjstart"] = lrsjstart;
    // 结束日期
    var lrsjend = jQuery('#detectionApplication_formSearch #lrsjend').val();
    map["lrsjend"] = lrsjend;
    return map;
}

// 列表刷新
function searchDetectionApplicationResult(isTurnBack){
//关闭高级搜索条件
    $("#detectionApplication_formSearch #searchMore").slideUp("low");
    detectionApplication_turnOff=true;
    $("#detectionApplication_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#detectionApplication_formSearch #detectionApplication_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#detectionApplication_formSearch #detectionApplication_list').bootstrapTable('refresh');
    }
}


// 按钮动作函数
function detectionApplicationDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?sqglid="+id;
        $.showDialog(url,'详情',viewDetectionApplicationConfig);
    }else if(action =='add'){
        var url= tourl;
        $.showDialog(url,'新增',addDetectionApplicationConfig);
    }else if(action =='addopen'){
        var url= tourl;
        $.showDialog(url,'新增(外部)',addDetectionApplicationConfig);
    }else if(action =='mod'){
        var url= tourl + "?sqglid="+id;
        $.showDialog(url,'修改',modDetectionApplicationConfig);
    }else if(action =='submit'){
        var url= tourl + "?sqglid="+id;
        $.showDialog(url,'提交',addDetectionApplicationConfig);
    }else if(action =='upload'){
        var url= tourl + "?lrry="+id;
        $.showDialog(url,'文件上传',uploadDetectionApplicationConfig);
    }
}

// 按钮初始化
var detectionApplication_ButtonInit= function (){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        //初始化页面上面的按钮事件
        //添加日期控件
        laydate.render({
            elem: '#detectionApplication_formSearch #lrsjstart'
            ,type: 'date'
        });
        //添加日期控件
        laydate.render({
            elem: '#detectionApplication_formSearch #lrsjend'
            ,type: 'date'
        });
        // 查询
        var btn_query=$("#detectionApplication_formSearch #btn_query");
        // 查看
        var btn_view=$("#detectionApplication_formSearch #btn_view");
        // 新增
        var btn_add=$("#detectionApplication_formSearch #btn_add");
        // 新增外部
        var btn_addopen=$("#detectionApplication_formSearch #btn_addopen");
        // 修改
        var btn_mod=$("#detectionApplication_formSearch #btn_mod");
        // 删除
        var btn_del=$("#detectionApplication_formSearch #btn_del");
        // 提交
        var btn_submit=$("#detectionApplication_formSearch #btn_submit");
        // 上传
        var btn_upload=$("#detectionApplication_formSearch #btn_upload");

        /*--------------------模糊查询--------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchDetectionApplicationResult(true);
            });
        }
        /*--------------------查　　看--------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#detectionApplication_formSearch #detectionApplication_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                detectionApplicationDealById(sel_row[0].sqglid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------新   增--------------------*/
        btn_add.unbind("click").click(function(){
            detectionApplicationDealById(null,"add",btn_add.attr("tourl"));
        });
        /*--------------------新   增--------------------*/
        btn_addopen.unbind("click").click(function(){
            detectionApplicationDealById(null,"addopen",btn_addopen.attr("tourl"));
        });
        /*--------------------修   改--------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row=$('#detectionApplication_formSearch #detectionApplication_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                if(sel_row[0].zt=='10'||sel_row[0].zt=='80'){
                    $.error("请选择审核未通过或者未提交的数据！");
                    return false;
                }else{
                    detectionApplicationDealById(sel_row[0].sqglid,"mod",btn_mod.attr("tourl"));
                }

            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------上   传--------------------*/
        btn_upload.unbind("click").click(function(){
            var sel_row=$('#detectionApplication_formSearch #detectionApplication_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                if(sel_row[0].zt!='80'){
                    $.error("请选择审核通过的数据！");
                    return false;
                }else{
                    detectionApplicationDealById(sel_row[0].lrry,"upload",btn_upload.attr("tourl"));
                }

            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------提   交--------------------*/
        btn_submit.unbind("click").click(function(){
            var sel_row=$('#detectionApplication_formSearch #detectionApplication_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                if(sel_row[0].zt=='10'||sel_row[0].zt=='80'){
                    $.error("请选择审核未通过或者未提交的数据！");
                    return false;
                }else{
                    detectionApplicationDealById(sel_row[0].sqglid,"submit",btn_submit.attr("tourl"));
                }
            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------删除列表-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#detectionApplication_formSearch #detectionApplication_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                   var _flag= $("#detectionApplication_formSearch #tab_flag").val()
                    
                    if((_flag ==null||_flag=='')){
                        ids= ids + ","+ sel_row[i].sqglid;

                        if(sel_row[i].zt=='10'){
                            recallRequisitions_del(sel_row[i].sqglid,"AUDIT_DETECTIONAPPLICATION");
                        }
                    }else{
                        if(sel_row[i].zt=='80'||sel_row[i].zt=='10'){
                            $.error("审核通过/审核中的不允许删除操作！");
                            return false;
                        }else{
                            ids= ids + ","+ sel_row[i].sqglid;
                        }

                    }
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
                                        searchDetectionApplicationResult();
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
        /**显示隐藏**/
        $("#detectionApplication_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(detectionApplication_turnOff){
                $("#detectionApplication_formSearch #searchMore").slideDown("low");
                detectionApplication_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#detectionApplication_formSearch #searchMore").slideUp("low");
                detectionApplication_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    }
    return oInit;
}
var ddsplcConfig={
                     width		: "900px",
                     modalName	: "ddsplcModal",
                     formName	: "ajaxForm",
                     offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
                     buttons		: {
                         success : {
                             label : "确 定",
                             className : "btn-primary",
                             callback : function() {

                         },
                         cancel : {
                             label : "关 闭",
                             className : "btn-default"
                         }
                     }
                     }
                 };
var uploadDetectionApplicationConfig = {
    width		: "900px",
    modalName	: "uploadDetectionApplicationModal",
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
                                searchDetectionApplicationResult();
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


// 查看
var viewDetectionApplicationConfig = {
    width : "700px",
    modalName : "viewDetectionApplicationModal",
    offAtOnce : true,  //当数据提交成功，立刻关闭窗口
    buttons : {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var addDetectionApplicationConfig = {
    width : "800px",
    modalName : "addDetectionApplicationModal",
    formName : "addDetectionApplicationForm",
    offAtOnce : true,  // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确　定",
            className : "btn-primary",
            callback : function() {
                if(!$("#addDetectionApplicationForm").valid()){// 表单验证
                    $.alert("请填写必填信息！");
                    return false;
                }
                var xzbj = $('#addDetectionApplicationForm #xzbj').val();
                var lx = $('#addDetectionApplicationForm input:radio[name="lx"]:checked').val();
                if("0"==lx){
                    var rows = $("#addDetectionApplicationForm #tb_list").bootstrapTable('getData');
                    var json=[];
                    if(rows!=null&&rows.length){
                        for (var i=0; i < rows.length; i++){
                            var sz={"sjid":""};
                            sz.sjid=rows[i].sjid;
                            json.push(sz);
                        }
                    }else{
                        $.error("请选择样本！");
                        return false;
                    }
                    $("#addDetectionApplicationForm #sqmx_json").val(JSON.stringify(json));
                }else{
                    if(!$("#addDetectionApplicationForm #hb").val()){
                        $.error("请填写合作伙伴！");
                        return false;
                    }
                }

                var $this = this;
                var opts = $this["options"]||{};
                $("#addDetectionApplicationForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"addDetectionApplicationForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            //提交审核
                            if(xzbj!='1'){
                                if(responseText["auditType"]){
                                    showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                        searchDetectionApplicationResult();
                                    });
                                }else{
                                    searchDetectionApplicationResult();
                                }
                            }else{
                                window.open("/application/application/pagedataDetectionZipDownload?sqglid="+responseText["ywid"]+"&access_token="+$("#ac_tk").val()
                                                );
                                searchDetectionApplicationResult();
                            }
                            $.closeModal(opts.modalName);
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
}

var modDetectionApplicationConfig = {
    width : "800px",
    modalName : "modDetectionApplicationModal",
    formName : "addDetectionApplicationForm",
    offAtOnce : true,  // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确　定",
            className : "btn-primary",
            callback : function() {
                if(!$("#addDetectionApplicationForm").valid()){// 表单验证
                    $.alert("请填写必填信息！");
                    return false;
                }
                var lx = $('#addDetectionApplicationForm input:radio[name="lx"]:checked').val();
                if("0"==lx){
                    var rows = $("#addDetectionApplicationForm #tb_list").bootstrapTable('getData');
                    var json=[];
                    if(rows!=null&&rows.length){
                        for (var i=0; i < rows.length; i++){
                            var sz={"sjid":""};
                            sz.sjid=rows[i].sjid;
                            json.push(sz);
                        }
                    }else{
                        $.error("请选择样本！");
                        return false;
                    }
                    $("#addDetectionApplicationForm #sqmx_json").val(JSON.stringify(json));
                }else{
                    if(!$("#addDetectionApplicationForm #hb").val()){
                        $.error("请填写合作伙伴！");
                        return false;
                    }
                }

                var $this = this;
                var opts = $this["options"]||{};
                $("#addDetectionApplicationForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"addDetectionApplicationForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchDetectionApplicationResult();
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
}


// 页面初始化
$(function(){
    // 1.初始化Table
    var oTable = new detectionApplication_TableInit();
    oTable.Init();
    // 2.初始化Button的点击事件
    var oButtonInit = new detectionApplication_ButtonInit();
    oButtonInit.Init();
    jQuery('#detectionApplication_formSearch .chosen-select').chosen({width: '100%'});
});