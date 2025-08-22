var quality_turnOff=true;
var quality_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#quality_formSearch #quality_list').bootstrapTable({
            url: $("#quality_formSearch #urlPrefix").val()+'/agreement/agreement/pageGetListAgreement',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#quality_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"zlxy.lrsj",					// 排序字段
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
            uniqueId: "zlxyid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮                 								
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width:'1%'
            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '2%',
                align: 'left',
                visible:true
            }, {
                field: 'zlxyid',
                title: '质量协议id',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'gfbh',
                title: '供方编号',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'gysmc',
                title: '供应商',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter: gysxxformat
            },{
                field: 'gfgllbmc',
                title: '供方管理类别',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'zlxybh',
                title: '质量协议编号',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'wbxybh',
                title: '外部协议编号',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'xjht',
                title: '新旧合同',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter: xjhtformat
            },{
                field: 'cjsj',
                title: '创建时间',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'kssj',
                title: '质量协议开始时间',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'dqsj',
                title: '质量协议到期时间',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'sfgq',
                title: '是否过期',
                width: '4%',
                align: 'left',
                formatter:sfgqormat,
                sortable: true,
                visible: true
            },{
                field: 'szbj',
                title: '双章合同',
                titleTooltip:'双章合同',
                width: '5%',
                align: 'left',
                visible:true,
                formatter:szbjformat,
                sortable:true
            },{
                field: 'bz',
                title: '备注',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zt',
                title: '状态',
                titleTooltip:'状态',
                width: '5%',
                align: 'left',
                visible:true,
                formatter:ztformat,
                sortable:true
            },{
                field: 'cz',
                title: '操作',
                titleTooltip:'操作',
                width: '3%',
                align: 'left',
                formatter:czFormat,
                visible: true
            }
            ],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                qualityById(row.zlxyid,'view',$("#quality_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#quality_formSearch #quality_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        })
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
            sortLastName: "zlxy.xgsj", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return qualitySearchData(map);
    };
    return oTableInit;
};



function qualitySearchData(map){
    var cxtj=$("#quality_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#quality_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr;
    }else if(cxtj=="1"){
        map["zlxybh"]=cxnr;
    }else if(cxtj=="2"){
        map["gysmc"]=cxnr;
    }else if (cxtj == "3") {
        map["gfbh"] = cxnr;
    }else if (cxtj == "4") {
        map["wlbm"] = cxnr;
    }else if (cxtj == "5") {
        map["wlmc"] = cxnr;
    }else if (cxtj == "6") {
        map["sccj"] = cxnr;
    }else if (cxtj == "7") {
        map["sjxmh"] = cxnr;
    }else if (cxtj == "8") {
        map["jszb"] = cxnr;
    }else if (cxtj == "9") {
        map["zlyq"] = cxnr;
    }else if (cxtj == "10") {
        map["ysbz"] = cxnr;
    }else if (cxtj == "11") {
        map["bctj"] = cxnr;
    }

    var cjsjstart = jQuery('#quality_formSearch #cjsjstart').val();
    map["cjsjstart"] = cjsjstart;

    var cjsjend = jQuery('#quality_formSearch #cjsjend').val();
    map["cjsjend"] = cjsjend;

    var kssjstart = jQuery('#quality_formSearch #kssjstart').val();
    map["kssjstart"] = kssjstart;

    var kssjend = jQuery('#quality_formSearch #kssjend').val();
    map["kssjend"] = kssjend;

    var dqsjstart = jQuery('#quality_formSearch #dqsjstart').val();
    map["dqsjstart"] = dqsjstart;

    var dqsjend = jQuery('#quality_formSearch #dqsjend').val();
    map["dqsjend"] = dqsjend;

    var gfgllbs=jQuery('#quality_formSearch #gfgllb_id_tj').val();
    map["gfgllbs"] = gfgllbs.replace(/'/g, "");

    var htxjs=jQuery('#quality_formSearch #htxj_id_tj').val();
    map["htxjs"] = htxjs.replace(/'/g, "");
    var sjxmhs=jQuery('#quality_formSearch #sjxmh_id_tj').val();
    map["sjxmhs"] = sjxmhs.replace(/'/g, "");
    var szbjs=jQuery('#quality_formSearch #szbj_id_tj').val();
    map["szbjs"] = szbjs.replace(/'/g, "");
    return map;
}


//提供给导出用的回调函数
function QualitySearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="zlxy.lrsj";
    map["sortLastOrder"]="desc";
    map["sortName"]="zlxy.xgsj";
    map["sortOrder"]="desc";
    return qualitySearchData(map);
}
function gysxxformat(value,row,index){
    var html = "<a href='javascript:void(0);' onclick='queryByGysid_t(\""+row.gysid+"\")'>"+row.gysmc+"</a>"
    return html;
}
function queryByGysid_t(gys){
    $.showDialog($("#quality_formSearch #urlPrefix").val()+"/warehouse/supplier/pagedataViewSupplier?gysid="+gys+"&access_token="+$("#ac_tk").val(),'供应商详细信息',viewSupplierConfig);
}
var viewSupplierConfig = {
    width		: "1200px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
function xjhtformat(value,row,index) {
    if (row.htxj=='0'){
        return "旧";
    }else if (row.htxj=='1'){
        return "新";
    }else
        return "";

}
//双章标记格式化
function szbjformat(value,row,index){
    if(row.szbj==1){
        var szbj="<span style='color:green;'>"+"是"+"</span>";
    }else if(row.szbj==0){
        var szbj="<span style='color:red;'>"+"否"+"</span>";
    }
    return szbj;
}
/**
 * 审核列表的状态格式化函数
 * @returns
 */
function ztformat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80'){
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.zlxyid + "\",event,\"AUDIT_QUALITYAGREEMENT\",{prefix:\"" + $('#quality_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.zlxyid + "\",event,\"AUDIT_QUALITYAGREEMENT\",{prefix:\"" + $('#quality_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else{
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.zlxyid + "\",event,\"AUDIT_QUALITYAGREEMENT\",{prefix:\"" + $('#quality_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}
function sfgqormat(value,row,index) {
    if (row.sfgq){
        var html="<span style='color:red;'>"+"已过期"+"</span>";
        return html;
    }
    else return "";
}
/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
    if (row.zt == '10' && row.scbj =='0' ) {
        return "<span class='btn btn-warning' onclick=\"recallTrain('" + row.zlxyid + "',event)\" >撤回</span>";
    }else if(row.zt == '10' && row.scbj ==null ){
        return "<span class='btn btn-warning' onclick=\"recallTrain('" + row.zlxyid + "',event)\" >撤回</span>";
    }else{
        return "";
    }
}
//撤回项目提交
function recallTrain(zlxyid,event){
    var auditType = $("#quality_formSearch #auditType").val();
    var msg = '您确定要撤回该条提交吗？';
    var quality_params = [];
    quality_params.prefix = $("#quality_formSearch #urlPrefix").val();
    $.confirm(msg,function(result){
        if(result){
            doAuditRecall(auditType,zlxyid,function(){
                qualityResult();
            },quality_params);
        }
    });
}
function qualityById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#quality_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?zlxyid=" +id;
        $.showDialog(url,'质量协议信息',viewQualityConfig);
    }else if(action =='add'){
        var url= tourl ;
        $.showDialog(url,'新增质量协议',addQualityConfig);
    }else if(action =='mod'){
        var url= tourl + "?zlxyid=" +id;;
        $.showDialog(url,'修改质量协议',modQualityConfig);
    }else if(action =='submit'){
        var url= tourl + "?zlxyid=" +id;;
        $.showDialog(url,'质量协议提交',modQualityConfig);
    }else if(action =='advancedmod'){
        var url= tourl + "?zlxyid=" +id;;
        $.showDialog(url,'高级修改',modQualityConfig);
    }else if(action =='contract'){
        var url= tourl + "?zlxyid=" +id;
        $.showDialog(url,'生成合同',generateContractConfig);
    }else if(action =='formal'){
        var url= tourl + "?zlxyid=" +id;
        $.showDialog(url,'双章合同',uploadContractConfig);
    }else if(action =='supplement'){
        var url= tourl + "?zlxyid=" +id;
        $.showDialog(url,'补充合同',addQualityConfig);
    }
}
function preSubmitRecheck(){
    return true;
}
var uploadContractConfig = {
    width		: "600px",
    modalName	: "agreementUploadContractModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "上传",
            className : "btn-primary",
            callback : function() {
                if(!$("#agreementUploadContractForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var fjids = $("#agreementUploadContractForm #fjids").val();

                if (fjids.length<=0){
                    $.error("请上传双章合同！");
                    return false;
                }
                $("#agreementUploadContractForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"agreementUploadContractForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                qualityResult();
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
var generateContractConfig = {
    width		: "800px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var viewQualityConfig = {
    width		: "1400px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var addQualityConfig = {
    width		: "1525px",
    modalName	: "addQualityModal",
    formName	: "editQualityForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "提 交",
            className : "btn-primary",
            callback : function() {
                if(!$("#editQualityForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                var json=[];
                var rows = $("#editQualityForm #edit_list").bootstrapTable('getData');
                if(rows!=null&&rows.length){
                    for (var i=0; i < rows.length; i++){
                        var sz = {"wlid":'',"wlbm":'',"wlmc":'',"scs":'',"fwmc":'',"sjxmh":'',"jszb":'',"zlyq":'',"ysbz":'',xh:''};
                        sz.wlid = rows[i].wlid;
                        sz.wlmc = rows[i].wlmc;
                        sz.wlbm = rows[i].wlbm;
                        sz.scs = rows[i].scs;
                        sz.fwmc = rows[i].fwmc;
                        sz.sjxmh = rows[i].sjxmh;
                        sz.jszb = rows[i].jszb;
                        sz.zlyq = rows[i].zlyq;
                        sz.ysbz = rows[i].ysbz;
                        sz.xh = i+1;
                        json.push(sz);
                    }
                }else{
                    $.error("明细不允许为空！");
                    return false;
                }

                $("#editQualityForm #zlxymx_json").val(JSON.stringify(json));


                var $this = this;
                var opts = $this["options"]||{};

                $("#editQualityForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editQualityForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
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
                                    qualityResult();
                                },null,params);
                            }else{
                                qualityResult();
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
        successtwo : {
            label : "保 存",
            className : "btn-success",
            callback : function() {
                if(!$("#editQualityForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                var json=[];
                var rows = $("#editQualityForm #edit_list").bootstrapTable('getData');
                if(rows!=null&&rows.length){
                    for (var i=0; i < rows.length; i++){
                        var sz = {"wlid":'',"wlbm":'',"wlmc":'',"scs":'',"fwmc":'',"sjxmh":'',"jszb":'',"zlyq":'',"ysbz":'',xh:''};
                        sz.wlid = rows[i].wlid;
                        sz.wlmc = rows[i].wlmc;
                        sz.wlbm = rows[i].wlbm;
                        sz.scs = rows[i].scs;
                        sz.fwmc = rows[i].fwmc;
                        sz.sjxmh = rows[i].sjxmh;
                        sz.jszb = rows[i].jszb;
                        sz.zlyq = rows[i].zlyq;
                        sz.ysbz = rows[i].ysbz;
                        sz.xh = i+1;
                        json.push(sz);
                    }
                }else{
                    $.error("明细不允许为空！");
                    return false;
                }

                $("#editQualityForm #zlxymx_json").val(JSON.stringify(json));


                var $this = this;
                var opts = $this["options"]||{};

                $("#editQualityForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editQualityForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                                qualityResult();
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

var modQualityConfig = {
    width		: "1525px",
    modalName	: "modQualityModal",
    formName	: "editQualityForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "提 交",
            className : "btn-primary",
            callback : function() {
                if(!$("#editQualityForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                var json=[];
                var rows = $("#editQualityForm #edit_list").bootstrapTable('getData');
                if(rows!=null&&rows.length){
                    for (var i=0; i < rows.length; i++){
                        var sz = {"wlid":'',"wlbm":'',"wlmc":'',"scs":'',"fwmc":'',"sjxmh":'',"jszb":'',"zlyq":'',"ysbz":'',xh:''};
                        sz.wlid = rows[i].wlid;
                        sz.wlmc = rows[i].wlmc;
                        sz.wlbm = rows[i].wlbm;
                        sz.scs = rows[i].scs;
                        sz.fwmc = rows[i].fwmc;
                        sz.sjxmh = rows[i].sjxmh;
                        sz.jszb = rows[i].jszb;
                        sz.zlyq = rows[i].zlyq;
                        sz.ysbz = rows[i].ysbz;
                        sz.xh = i+1;
                        json.push(sz);
                    }
                }else{
                    $.error("明细不允许为空！");
                    return false;
                }

                $("#editQualityForm #zlxymx_json").val(JSON.stringify(json));


                var $this = this;
                var opts = $this["options"]||{};

                $("#editQualityForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editQualityForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
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
                                    qualityResult();
                                },null,params);
                            }else{
                                qualityResult();
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
        successtwo : {
            label : "确 定",
            className : "btn-success",
            callback : function() {
                if(!$("#editQualityForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                var json=[];
                var rows = $("#editQualityForm #edit_list").bootstrapTable('getData');
                if(rows!=null&&rows.length){
                    for (var i=0; i < rows.length; i++){
                        var sz = {"wlid":'',"wlbm":'',"wlmc":'',"scs":'',"fwmc":'',"sjxmh":'',"jszb":'',"zlyq":'',"ysbz":'',xh:''};
                        sz.wlid = rows[i].wlid;
                        sz.wlmc = rows[i].wlmc;
                        sz.wlbm = rows[i].wlbm;
                        sz.scs = rows[i].scs;
                        sz.fwmc = rows[i].fwmc;
                        sz.sjxmh = rows[i].sjxmh;
                        sz.jszb = rows[i].jszb;
                        sz.zlyq = rows[i].zlyq;
                        sz.ysbz = rows[i].ysbz;
                        sz.xh = i+1;
                        json.push(sz);
                    }
                }else{
                    $.error("明细不允许为空！");
                    return false;
                }

                $("#editQualityForm #zlxymx_json").val(JSON.stringify(json));


                var $this = this;
                var opts = $this["options"]||{};

                $("#editQualityForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editQualityForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                                qualityResult();
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

var quality_ButtonInit = function(){
    var oInit = new Object();

    oInit.Init = function () {
        var btn_view = $("#quality_formSearch #btn_view");
        var btn_query = $("#quality_formSearch #btn_query");
        var btn_add = $("#quality_formSearch #btn_add");
        var btn_mod = $("#quality_formSearch #btn_mod");
        var btn_discard = $("#quality_formSearch #btn_discard");
        var btn_del = $("#quality_formSearch #btn_del");
        var btn_submit = $("#quality_formSearch #btn_submit");
        var btn_advancedmod=$("#quality_formSearch #btn_advancedmod");
        var btn_searchexport = $("#quality_formSearch #btn_searchexport");
        var btn_selectexport = $("#quality_formSearch #btn_selectexport");
        var btn_contract=$("#quality_formSearch #btn_contract");
        var btn_formal=$("#quality_formSearch #btn_formal");
        var btn_supplement=$("#quality_formSearch #btn_supplement");
        //添加日期控件
        laydate.render({
            elem: '#cjsjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#cjsjend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#kssjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#kssjend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#dqsjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#dqsjend'
            ,theme: '#2381E9'
        });

        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                qualityResult(true);
            });
        }

        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#quality_formSearch #quality_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                qualityById(sel_row[0].zlxyid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------新增-----------------------------------*/
        btn_add.unbind("click").click(function(){
            qualityById(null,"add",btn_add.attr("tourl"));
        });
        /* ---------------------------修改-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#quality_formSearch #quality_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if (sel_row[0].zt=='10'||sel_row[0].zt=='80'){
                    $.error("审核中和审核通过的数据不可以修改");
                }
                else {
                    qualityById(sel_row[0].zlxyid,"mod",btn_mod.attr("tourl"));
                }
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------补充合同-----------------------------------*/
        btn_supplement.unbind("click").click(function(){
            var sel_row = $('#quality_formSearch #quality_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if (sel_row[0].zt=='80'){
                    qualityById(sel_row[0].zlxyid,"supplement",btn_supplement.attr("tourl"));
                }else {
                    $.error("未审核通过,不能补充合同！");
                }
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------提交-----------------------------------*/
        btn_submit.unbind("click").click(function(){
            var sel_row = $('#quality_formSearch #quality_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if (sel_row[0].zt=='10'||sel_row[0].zt=='80'){
                    $.error("该状态不可以提交");
                }else {
                    qualityById(sel_row[0].zlxyid,"submit",btn_submit.attr("tourl"));
                }
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------删　　除--------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#quality_formSearch #quality_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].zlxyid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $('#quality_formSearch #urlPrefix').val()+btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        qualityResult(true);
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
        /*--------------------删　　除--------------------*/
        btn_discard.unbind("click").click(function(){
            var sel_row = $('#quality_formSearch #quality_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    if(sel_row[i].zt!='00'&&sel_row[i].zt!='15'){
                        $.error("请选择未提交或者审核未通过的数据！");
                        return;
                    }else{
                        ids= ids + ","+ sel_row[i].zlxyid;
                    }
                }
                ids=ids.substr(1);
                $.confirm('您确定要废弃所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $('#quality_formSearch #urlPrefix').val()+btn_discard.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        qualityResult(true);
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
        /*--------------------------------高级修改请购信息 ---------------------------*/
        btn_advancedmod.unbind("click").click(function(){
            var sel_row=$('#quality_formSearch #quality_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                qualityById(sel_row[0].zlxyid,"advancedmod",btn_advancedmod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        // 	  ---------------------------导出-----------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#quality_formSearch #quality_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].zlxyid;
                }
                ids = ids.substr(1);
                $.showDialog($('#quality_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=QUALITY_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });
        /*-----------------------------------生成合同----------------------------------*/
        btn_contract.unbind().click(function(){
            var sel_row = $('#quality_formSearch #quality_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                qualityById(sel_row[0].zlxyid, "contract", btn_contract.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        })
        /* ---------------------------双章合同上传-----------------------------------*/
        btn_formal.unbind("click").click(function(){
            var sel_row = $('#quality_formSearch #quality_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                qualityById(sel_row[0].zlxyid,"formal",btn_formal.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });

        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#quality_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=QUALITY_SEARCH&expType=search&callbackJs=QualitySearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
        /**显示隐藏**/
        $("#quality_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(quality_turnOff){
                $("#quality_formSearch #searchMore").slideDown("low");
                quality_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#quality_formSearch #searchMore").slideUp("low");
                quality_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};


function qualityResult(isTurnBack){
    //关闭高级搜索条件
    $("#quality_formSearch #searchMore").slideUp("low");
    quality_turnOff=true;
    $("#quality_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#quality_formSearch #quality_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#quality_formSearch #quality_list').bootstrapTable('refresh');
    }
}


$(function(){
    // 1.初始化Table
    var oTable = new quality_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new quality_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#quality_formSearch .chosen-select').chosen({width: '100%'});

    $("#quality_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});