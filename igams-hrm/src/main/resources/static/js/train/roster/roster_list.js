var roster_turnOff=true;

var roster_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#roster_formSearch #roster_list").bootstrapTable({
            url: $("#roster_formSearch #urlPrefix").val()+'/roster/roster/pageGetListRoster',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#roster_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "yh.yhm",				//排序字段
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
            uniqueId: "yghmcid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '0.5%',
                align: 'center',

            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '1%',
                align: 'left',
                visible:true
            },{
                field: 'yghmcid',
                title: '员工花名册id',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: false,
            },{
                field: 'ygbm',
                title: '员工编码',
                width: '2%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xm',
                title: '姓名',
                width: '2%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'bm',
                title: '部门',
                width: '3%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'gwmc',
                title: '岗位名称',
                width: '4%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'mz',
                title: '民族',
                width: '2%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'sex',
                title: '性别',
                width: '2%',
                align: 'left',
                sortable: true,
                visible:true,
                formatter: get_xb,
            },{
                field: 'rzrq',
                title: '入职日期',
                width: '3%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'zzrq',
                title: '转正日期',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sfz',
                title: '身份证',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'zjyxq',
                title: '证件有效期',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'csrq',
                title: '出生日期',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'csyf',
                title: '出生月份',
                width: '2%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'xl',
                title: '学历',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'byyx',
                title: '毕业院校',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'zy',
                title: '专业',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'sjhm',
                title: '手机号码',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'cjgzsj',
                title: '参加工作时间',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'sl',
                title: '司龄',
                width: '2%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'hyzk',
                title: '婚育状况',
                width: '2%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'zzmm',
                title: '政治面貌',
                width: '2%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'zc',
                title: '职称',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'jg',
                title: '籍贯',
                width: '2%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'sfzdz',
                title: '身份证地址',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'hjxz',
                title: '户籍性质',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'jjlxr',
                title: '紧急联系人',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'lxrdh',
                title: '紧急联系人电话',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'schtqsr',
                title: '首次合同起始日',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'schtdqr',
                title: '首次合同到期日',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'yhkh',
                title: '银行卡号',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'khh',
                title: '开户行',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'sfqdbmxy',
                title: '保密协议签订',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'sflz',
                title: '是否离职',
                width: '2%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'lzrq',
                title: '离职日期',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'dqtx',
                title: '到期提醒',
                width: '2%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'xhtqsr',
                title: '现合同起始日',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'xhtdqr',
                title: '现合同到期日',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'bgdd',
                title: '办公地点',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'xjdz',
                title: '现居地址',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'ssgs',
                title: '所属公司',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:false
            },{
                field: 'zszg',
                title: '直属主管',
                width: '10%',
                align: 'left',
                sortable: true,
                visible:false
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                    rosterDealById(row.yghmcid,row.yhid,'view',$("#roster_formSearch #btn_view").attr("tourl"));
            }
        });
        $("#roster_formSearch #roster_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        })
    }
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "zzrq", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return rosterSearchData(map);
    }
    return oTableInit
}

function rosterSearchData(map) {
    var cxtj = $("#roster_formSearch #cxtj").val();
    var cxnr = $.trim(jQuery('#roster_formSearch #cxnr').val());
    if (cxtj == "0") {
        map["entire"] = cxnr
    } else if (cxtj == "1") {
        map["xm"] = cxnr
    }else if (cxtj == "2") {
        map["ygbm"] = cxnr
    }else if (cxtj == "3") {
        map["bm"] = cxnr
    }else if (cxtj == "4") {
        map["gwmc"] = cxnr
    }else if (cxtj == "5") {
        map["mz"] = cxnr
    }else if (cxtj == "6") {
        map["sfz"] = cxnr
    }else if (cxtj == "7") {
        map["csyf"] = cxnr
    }else if (cxtj == "8") {
        map["xl"] = cxnr
    }else if (cxtj == "9") {
        map["byyx"] = cxnr
    }else if (cxtj == "10") {
        map["zy"] = cxnr
    }else if (cxtj == "11") {
        map["sjhm"] = cxnr
    }else if (cxtj == "12") {
        map["hyzk"] = cxnr
    }else if (cxtj == "13") {
        map["zzmm"] = cxnr
    }else if (cxtj == "14") {
        map["zc"] = cxnr
    }else if (cxtj == "15") {
        map["jg"] = cxnr
    }else if (cxtj == "16") {
        map["bgdd"] = cxnr
    }else if (cxtj == "17") {
        map["yhkh"] = cxnr
    }else if (cxtj == "18") {
        map["khh"] = cxnr
    }
    // 创建开始日期
    var rzrqstart = jQuery('#roster_formSearch #rzrqstart').val();
    map["rzrqstart"] = rzrqstart;
    var rzrqend = jQuery('#roster_formSearch #rzrqend').val();
    map["rzrqend"] = rzrqend;

    var zzrqstart = jQuery('#roster_formSearch #zzrqstart').val();
    map["zzrqstart"] = zzrqstart;
    var zzrqend = jQuery('#roster_formSearch #zzrqend').val();
    map["zzrqend"] = zzrqend;

    var cjgzrqstart = jQuery('#roster_formSearch #cjgzrqstart').val();
    map["cjgzrqstart"] = cjgzrqstart;
    var cjgzrqend = jQuery('#roster_formSearch #cjgzrqend').val();
    map["cjgzrqend"] = cjgzrqend;

    var schtqsrqstart = jQuery('#roster_formSearch #schtqsrqstart').val();
    map["schtqsrqstart"] = schtqsrqstart;
    var schtqsrqend = jQuery('#roster_formSearch #schtqsrqend').val();
    map["schtqsrqend"] = schtqsrqend;

    var schtdqrqstart = jQuery('#roster_formSearch #schtdqrqstart').val();
    map["schtdqrqstart"] = schtdqrqstart;
    var schtdqrqend = jQuery('#roster_formSearch #schtdqrqend').val();
    map["schtdqrqend"] = schtdqrqend;

    var lzrqstart = jQuery('#roster_formSearch #lzrqstart').val();
    map["lzrqstart"] = lzrqstart;
    var lzrqend = jQuery('#roster_formSearch #lzrqend').val();
    map["lzrqend"] = lzrqend;

    var sflzs = jQuery('#roster_formSearch #sflz_id_tj').val();
    map["sflzs"] = sflzs.replace(/'/g, "");
    var sfqdbmxys = jQuery('#roster_formSearch #sfqdbmxy_id_tj').val();
    map["sfqdbmxys"] = sfqdbmxys.replace(/'/g, "");
    var bms = jQuery('#roster_formSearch #bm_id_tj').val();
    map["bms"] = bms.replace(/'/g, "");
    var fl = jQuery('#roster_formSearch #fl_id_tj').val();
    map["fl"] = fl;
    var ssgss = jQuery('#roster_formSearch #ssgs_id_tj').val();
    map["ssgss"] = ssgss.replace(/'/g, "");

    return map;
}
function rosterDealById(id,yhid,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#roster_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?yghmcid=" +id+"&yhid="+yhid;
        $.showDialog(url,'查看',viewrosterConfig);
    }else if (action=='mod'){
        var url=tourl+"?ids="+id;
        $.showDialog(url,'修改',modrosterConfig);
    }else if (action=='yghtuphold'){
        var url=tourl+"?yghmcid="+id;
        $.showDialog(url,'员工合同信息维护',yghtUpholdFormConfig);
    }else if (action=='yglzuphold'){
        var url=tourl+"?yghmcid="+id;
        $.showDialog(url,'员工离职信息维护',yglzUpholdFormConfig);
    }else if (action=='trainprint'){
        var url= tourl + "?yghmcid=" +id+"&yhid="+yhid;
        $.showDialog(url,'培训档案打印',trainprintConfig);
    }else if (action=='modtrainrecord'){
        var url= tourl + "?yhid="+yhid;
        $.showDialog(url,'培训档案修改',modTrainRecordConfig);
    }else if (action=='addHealth'){
        var url= tourl+id;
        $.showDialog(url,'新增健康档案',addHealthConfig);
    }else if (action=='modHealth'){
        var url= tourl+"?yghmcid="+id;
        $.showDialog(url,'修改健康档案',modHealthConfig);
    }else if (action=='printHealth'){
        var url= tourl + "?yghmcid=" +id;
        $.showDialog(url,'健康档案打印',printHealthConfig);
    }
}


var printHealthConfig = {
    width		: "1400px",
    modalName	: "chooseprintHealthModal",
    formName	: "chooseprintHealthForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                var sel_row = $('#chooseprintHealthForm #print_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length ==0){
                    $.error("请至少选中一行");
                    return;
                }
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].jkdaid;
                }
                ids = ids.substr(1);
                var url=$('#chooseprintHealthForm #urlPrefix').val() + "/roster/roster/pagedataModPrintHealth?ids="+ids+"&yghmcid="+ $("#chooseprintHealthForm #yghmcid").val()+"&access_token=" + $("#ac_tk").val();
                $.showDialog(url,'修改打印数据',modprintHealthConfig);
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var modprintHealthConfig = {
    width		: "600px",
    modalName	:"modprintHealthModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var url=$('#modPrintHealthForm #urlPrefix').val()+"/roster/roster/pagedataPrintHealthChoose?ids="
                +$("#modPrintHealthForm #ids").val().replace("[", "").replace("]", "").trim()
                +"&zsxm="+ $("#modPrintHealthForm #zsxm").val()
                +"&jlbh="+ $("#modPrintHealthForm #jlbh").val()
                +"&bm="+ $("#modPrintHealthForm #bm").val()
                +"&gwmc="+ $("#modPrintHealthForm #gwmc").val()
                +"&cskz1="+ $("#modPrintHealthForm #ssgs").val()
                +"&xb="+ $("#modPrintHealthForm #sex").val()
                +"&access_token="+$("#ac_tk").val();
                window.open(url);
            },
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


var modHealthConfig={
    width		: "1600px",
    modalName	:"updateHealthModal",
    formName	: "eidtHealthForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}

var addHealthConfig = {
    width		: "800px",
    modalName	: "addHealthFormModal",
    formName	: "addHealthForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                $("#addHealthForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"addHealthForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchrosterResult();
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    }else{
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


//员工合同信息维护
var yghtUpholdFormConfig = {
    width		: "1000px",
    modalName	: "yghtUpholdFormModal",
    formName	: "yghtUpholdForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(var i=0;i<t_map.rows.length;i++){
                        var fjids = $("#htfj_"+i).val().split(",");
                        var sz = {"yghmcid":'',"yghtid":'',"htqsr":'',"htdqr":'',"fjids":''};
                        sz.yghmcid = t_map.rows[i].yghmcid;
                        sz.yghtid = t_map.rows[i].yghtid;
                        sz.htqsr = t_map.rows[i].htqsr;
                        sz.htdqr = t_map.rows[i].htdqr;
                        sz.fjids = fjids;
                        json.push(sz);
                    }
                    $("#yghtUpholdForm #yghtmx_json").val(JSON.stringify(json));
                }
                $("#yghtUpholdForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"yghtUpholdForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchrosterResult();
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    }else{
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
};//培训档案打印
var trainprintConfig = {
    width		: "1400px",
    modalName	: "chooseTrainPrint_FormModal",
    formName	: "chooseTrainPrint_Form",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                var sel_row = $('#chooseTrainPrint_Form #pxxx_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length ==0){
                    $.error("请至少选中一行");
                    return;
                }
                var gzids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    gzids = gzids + ","+ sel_row[i].gzid;

                }
                gzids = gzids.substr(1);
                var url=$('#chooseTrainPrint_Form #urlPrefix').val() + "/roster/roster/pagedataModPrintTrainData?gzids="+gzids+"&yghmcid="+ $("#chooseTrainPrint_Form #yghmcid").val()+"&access_token=" + $("#ac_tk").val();
                $.showDialog(url,'修改打印数据',modPrintTrainDataConfig);
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var modPrintTrainDataConfig = {
    width		: "600px",
    modalName	:"modPrintTrainDataModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var url=$('#modPrintTrainDataForm #urlPrefix').val()+"/roster/roster/pagedataPrintTrainForChoose?gzids="
                +$("#modPrintTrainDataForm #gzids").val().replace("[", "").replace("]", "").trim()
                +"&yghmcid="+ $("#modPrintTrainDataForm #yghmcid").val()
                +"&rzrq="+ $("#modPrintTrainDataForm #rzrq").val()
                +"&zc="+ $("#modPrintTrainDataForm #zc").val()
                +"&cskz1="+ $("#modPrintTrainDataForm #ssgs").val()
                +"&access_token="+$("#ac_tk").val();
                window.open(url);
            },
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
//员工离职信息维护
var yglzUpholdFormConfig = {
    width		: "1000px",
    modalName	: "yglzUpholdFormModal",
    formName	: "yglzUpholdForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(var i=0;i<t_map.rows.length;i++){
                        var fjids = $("#lzfj_"+i).val().split(",");
                        var sz = {"yghmcid":'',"yglzid":'',"rzrq":'',"lzrq":'',"fjids":''};
                        sz.yghmcid = t_map.rows[i].yghmcid;
                        sz.yglzid = t_map.rows[i].yglzid;
                        sz.rzrq = t_map.rows[i].rzrq;
                        sz.lzrq = t_map.rows[i].lzrq;
                        sz.fjids = fjids;
                        json.push(sz);
                    }
                    $("#yglzUpholdForm #yglzmx_json").val(JSON.stringify(json));
                }
                $("#yglzUpholdForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"yglzUpholdForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchrosterResult();
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    }else{
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
function get_xb(value) {
    if (value)
    {
        if (value=="0")
            return "男"
        else if (value=="1")
                return "女"
    }
    return ""

}
//提供给导出用的回调函数
function rosSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="yghmc.rzrq";
    map["sortLastOrder"]="desc";
    map["sortName"]="yghmc.yghmcid";
    map["sortOrder"]="desc";
    return rosterSearchData(map);
}
function sflz_formatter(value) {
        if (value)
        {
            if (value=="1")
                return "是"
            else if (value=="0")
                return "否"
        }
    return ""
}

var modTrainRecordConfig={
    width		: "1300px",
    modalName	:"modTrainRecordModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}
/**
 * 查看页面模态框
 */
var viewrosterConfig={
    width		: "1300px",
    modalName	:"viewrosterModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}
var modrosterConfig = {
    width		: "800px",
    modalName	: "modrosterModal",
    formName	: "rosterEditForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};

                $("#rosterEditForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"rosterEditForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchrosterResult();
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
var roster_ButtonInit = function () {
    var oInit = new Object();
    oInit.Init = function () {
        var btn_query = $("#roster_formSearch #btn_query");
        var btn_view = $("#roster_formSearch #btn_view");
        var btn_mod = $("#roster_formSearch #btn_mod");
        var btn_searchexport = $("#roster_formSearch #btn_searchexport");
        var btn_selectexport = $("#roster_formSearch #btn_selectexport");
        var btn_process = $("#roster_formSearch #btn_process");
        var btn_yghtuphold = $("#roster_formSearch #btn_yghtuphold");
        var btn_yglzuphold = $("#roster_formSearch #btn_yglzuphold");
        var btn_trainprint = $("#roster_formSearch #btn_trainprint");
        var btn_modtrainrecord = $("#roster_formSearch #btn_modtrainrecord");
        var btn_addHealth = $("#roster_formSearch #btn_addHealth");
        var btn_modHealth = $("#roster_formSearch #btn_modHealth");
        var btn_printHealth = $("#roster_formSearch #btn_printHealth");
        /* ---------------------------模糊查询-----------------------------------*/
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchrosterResult(true);
            });
        }
//添加日期控件
        laydate.render({
            elem: '#rzrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#rzrqend'
            ,theme: '#2381E9'
        });
        laydate.render({
            elem: '#zzrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#zzrqend'
            ,theme: '#2381E9'
        });
        laydate.render({
            elem: '#cjgzrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#cjgzrqend'
            ,theme: '#2381E9'
        });
        laydate.render({
            elem: '#schtqsrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#schtqsrqend'
            ,theme: '#2381E9'
        });
        laydate.render({
            elem: '#schtdqrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#schtdqrqend'
            ,theme: '#2381E9'
        });
        laydate.render({
            elem: '#lzrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#lzrqend'
            ,theme: '#2381E9'
        });
        /* --------------------------- 新增健康档案 -----------------------------------*/
        btn_addHealth.unbind("click").click(function(){
        var sel_row = $('#roster_formSearch #roster_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                rosterDealById("?yghmcid="+sel_row[0].yghmcid+"&bm="+sel_row[0].bm+"&gwmc="+sel_row[0].gwmc,null,"addHealth", btn_addHealth.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------修改健康档案-----------------------------------*/
        btn_modHealth.unbind("click").click(function(){
            var sel_row = $('#roster_formSearch #roster_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                rosterDealById(sel_row[0].yghmcid,null,"modHealth",btn_modHealth.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------打印健康档案-----------------------------------*/
        btn_printHealth.unbind("click").click(function(){
            var sel_row = $('#roster_formSearch #roster_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length !=1){
                $.error("请选中一行");
                return;
            }
            rosterDealById(sel_row[0].yghmcid,null,"printHealth",btn_printHealth.attr("tourl"));
        });
        /* ---------------------------培训档案修改-----------------------------------*/
        btn_modtrainrecord.unbind("click").click(function(){
            var sel_row = $('#roster_formSearch #roster_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                rosterDealById(null,sel_row[0].yhid,"modtrainrecord",btn_modtrainrecord.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#roster_formSearch #roster_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                    rosterDealById(sel_row[0].yghmcid,sel_row[0].yhid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------编辑信息-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#roster_formSearch #roster_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
            }else{
                var ids="";
                for (var i=0;i<sel_row.length;i++){
                    ids=ids+","+sel_row[i].yghmcid;
                }
                ids=ids.substring(1);
                rosterDealById(ids,'',"mod",btn_mod.attr("tourl"));
            }
        });
        // 	  ---------------------------导出-----------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#roster_formSearch #roster_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].yghmcid;
                }
                ids = ids.substr(1);
                $.showDialog($('#roster_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=YGHMC_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });

        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#roster_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=YGHMC_SEARCH&expType=search&callbackJs=rosSearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
        /* --------------------------- 培训档案打印 -----------------------------------*/
        /*---------------------------编辑信息-----------------------------------*/
        btn_trainprint.unbind("click").click(function(){
            var sel_row = $('#roster_formSearch #roster_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length !=1){
                $.error("请选中一行");
                return;
            }
            rosterDealById(sel_row[0].yghmcid,sel_row[0].yhid,"trainprint",btn_trainprint.attr("tourl"));
        });
        /* ------------------------------处理-----------------------------*/
        btn_process.unbind("click").click(function(){
            var sel_row = $('#roster_formSearch #roster_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].yghmcid;
            }
            ids = ids.substr(1);
            $.confirm('您确定要处理所选择的记录吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url=  $('#roster_formSearch #urlPrefix').val() + btn_process.attr("tourl");
                    jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    searchrosterResult(true);
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
        /* --------------------------- 员工合同维护 -----------------------------------*/
        btn_yghtuphold.unbind("click").click(function(){
            var sel_row = $('#roster_formSearch #roster_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                rosterDealById(sel_row[0].yghmcid, '',"yghtuphold", btn_yghtuphold.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* --------------------------- 员工离职维护 -----------------------------------*/
        btn_yglzuphold.unbind("click").click(function(){
            var sel_row = $('#roster_formSearch #roster_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                rosterDealById(sel_row[0].yghmcid, '',"yglzuphold", btn_yglzuphold.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-----------------------高级筛选------------------------------------*/
        $("#roster_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(roster_turnOff){
                $("#roster_formSearch #searchMore").slideDown("low");
                roster_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#roster_formSearch #searchMore").slideUp("low");
                roster_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
    };
    return oInit;
};


function searchrosterResult(isTurnBack) {
    //关闭高级搜索条件
    $("#roster_formSearch #searchMore").slideUp("low");
    roster_turnOff=true;
    $("#roster_formSearch #sl_searchMore").html("高级筛选");
    if (isTurnBack) {
        $('#roster_formSearch #roster_list').bootstrapTable('refresh', {pageNumber: 1});
    } else {
        $('#roster_formSearch #roster_list').bootstrapTable('refresh');
    }
}
$(function(){
    //0.界面初始化
    // 1.初始化Table
    var oTable = new roster_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new roster_ButtonInit();
    oButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#roster_formSearch .chosen-select').chosen({width: '100%'});
    // 初始绑定显示更多的事件
    $("#roster_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});
