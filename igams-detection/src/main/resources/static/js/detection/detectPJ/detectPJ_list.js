var DetectPJ_turnOff=true;

var DetectPJ_TableInit = function (FieldList) {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function (){
        $('#detectPJ_formSearch #detectPJ_list').bootstrapTable({
            url: '/detectionPJ/detectionPJ/pageGetListDetectionPJ',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#detectPJ_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "fzjcxx.zt",				//排序字段
            sortOrder: "asc,fzjcxx.syh desc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100, 1000],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "fzjcid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns:[{
                checkbox: true
            }, {
                'field': '',
                'title': '序号',
                'align': 'center',
                'width': '3%',
                'formatter': function (value, row, index) {
                    var options = $('#detectPJ_formSearch #detectPJ_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            },{
                field: 'fzjcid',
                title: '分子检测ID',
                titleTooltip:'分子检测ID',
                width: '8%',
                align: 'left',
                visible:false
            },{
                field: 'ybbh',
                title: '标本编号',
                titleTooltip:'标本编号',
                width: '15%',
                align: 'left',
                visible:true,
                sortable:true
            },{
                field: 'syh',
                title: '实验号',
                titleTooltip:'实验号',
                width: '12%',
                align: 'left',
                visible:true,
                sortable:true
            },{
                field: 'jcxmmc',
                title: '检测项目',
                titleTooltip:'检测项目',
                width: '13%',
                align: 'left',
                visible:true,
                sortable:true
            },{
                field: 'xm',
                title: '姓名',
                titleTooltip:'姓名',
                width: '6%',
                align: 'left',
                visible:true,
                sortable:true
            },{
                field: 'sjdwmc',
                title: '医院名称',
                titleTooltip:'医院名称',
                width: '8%',
                align: 'left',
                visible:true,
                sortable:true
            },{
                field: 'jcjgdm',
                title: '结果',
                titleTooltip:'结果',
                width: '5%',
                align: 'left',
                formatter:jgformat,
                visible:true,
                sortable:true
            },{
                field: 'nl',
                title: '年龄',
                titleTooltip:'年龄',
                width: '5%',
                align: 'left',
                visible:true,
                sortable:true
            },{
                field: 'xb',
                title: '性别',
                titleTooltip:'性别',
                width: '5%',
                align: 'left',
                formatter:xbformat,
                visible:true,
                sortable:true
            },{
                field: 'yblxmc',
                title: '标本类型',
                titleTooltip:'标本类型',
                width: '12%',
                align: 'left',
                visible:true,
                sortable:true
            },
                {
                    field: 'bbztmc',
                    title: '标本状态',
                    titleTooltip:'标本状态',
                    width: '10%',
                    align: 'left',
                    visible:true,
                    sortable:true
                },{
                field: 'jcdwmc',
                title: '检测单位',
                titleTooltip:'检测单位',
                width: '8%',
                align: 'left',
                visible:false,
                sortable:true
            },{
                field: 'jssj',
                title: '接收时间',
                titleTooltip:'接收时间',
                width: '12%',
                align: 'left',
                visible:true,
                sortable:true
            },{
                field: 'sysj',
                title: '实验时间',
                titleTooltip:'实验时间',
                width: '12%',
                align: 'left',
                visible:true,
                sortable:true
            },{
                field: 'bgrq',
                title: '报告日期',
                titleTooltip:'报告日期',
                width: '10%',
                align: 'left',
                visible:false,
                sortable:true
            },{
                field: 'xms',
                title: '报告完成数/项目数',
                titleTooltip:'报告完成数/项目数',
                width: '10%',
                align: 'left',
                visible:true,
                sortable:true,
                formatter:xmsformat,
            }
//                ,{
//                field: 'zt',
//                title: '状态',
//                titleTooltip:'状态',
//                width: '8%',
//                align: 'left',
//                visible:true,
//                formatter:ztformat,
//                sortable:true
//            },{
//                field: 'cz',
//                title: '操作',
//                titleTooltip:'操作',
//                width: '8%',
//                align: 'left',
//                visible:true,
//                formatter:czFormat
//            }
            ],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                DetectPJDealById(row.fzjcid,'view',$("#detectPJ_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#detectPJ_formSearch #detectPJ_list").colResizable({
                liveDrag:true,
                gripInnerHtml:"<div class='grip'></div>",
                draggingClass:"dragging",
                resizeMode:'fit',
                postbackSafe:true,
                partialRefresh:true,
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
            sortLastName: "fzjcxx.fzjcid", // 防止同名排位用
            sortLastOrder: "desc", // 防止同名排位用
            jclx:$("#detectPJ_formSearch #jclx").val() // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getDetectPJSearchData(map);
    };
    return oTableInit;
}

function getDetectPJSearchData(map){
    var cxtj=$("#detectPJ_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#detectPJ_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["ybbh"]=cxnr
    }else if(cxtj=='1'){
        map["xm"]=cxnr
    }else if(cxtj=='2'){
        map["syh"]=cxnr
    }else if(cxtj=='3'){
        map["sjdwmc"]=cxnr
    }else if(cxtj=='4'){
        map["nl"]=cxnr
    }

    // var zts = jQuery('#detectPJ_formSearch #zt_id_tj').val();
    // map["zts"] = zts;

    // 接收日期
    var jssjstart = jQuery('#detectPJ_formSearch #jssjstart').val();
    map["jssjstart"] = jssjstart;
    var jssjend = jQuery('#detectPJ_formSearch #jssjend').val();
    map["jssjend"] = jssjend;
    // 检测日期
    var sysjstart = jQuery('#detectPJ_formSearch #sysjstart').val();
    map["sysjstart"] = sysjstart;
    var sysjend = jQuery('#detectPJ_formSearch #sysjend').val();
    map["sysjend"] = sysjend;
    // 报告日期
    var bgrqstart = jQuery('#detectPJ_formSearch #bgrqstart').val();
    map["bgrqstart"] = bgrqstart;
    var bgrqend = jQuery('#detectPJ_formSearch #bgrqend').val();
    map["bgrqend"] = bgrqend;
    var yblxs = jQuery('#detectPJ_formSearch #yblx_id_tj').val();
    map["yblxs"] = yblxs;
    var zts = jQuery('#detectPJ_formSearch #zt_id_tj').val();
    map["zts"] = zts;
    var sfsy = jQuery('#detectPJ_formSearch #sfsy_id_tj').val();
    map["sfsy"] = sfsy;

    var sfjs = jQuery('#detectPJ_formSearch #sfjs_id_tj').val();
    map["sfjs"] = sfjs;
    return map;
}
//传递jclx
function DetectPJDealByIds(id1,id2,action,tourl) {
    if (!tourl) {
        return;
    }
    if (action == 'add') {
        var url = tourl + "?jclx=" + id2;
        $.showDialog(url, '新增', addPJDetection);
    } else if (action == 'mod') {
        var url = tourl + "?fzjcid=" + id1 + "&jclx=" + id2;
        $.showDialog(url, '修改', modPJDetection);
    }
}
// 按钮动作函数
function DetectPJDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action=="reportdownload"){
        var url=tourl + "?ids=" +id+"&jclx="+$("#detectPJ_formSearch #jclx").val();
        $.showDialog(url,'报告下载',DetectPJreportDownloadConfig);
    }
    else if(action=='detection'){
        var url=tourl + "?ids=" +id+"&jclx="+$("#detectPJ_formSearch #jclx").val();
        $.showDialog(url,'实验',modPJDetectionConfig);
    }else if(action=='resultmod'){
        var url=tourl + "?ids=" +id;
        $.showDialog(url,'结果修改',resultModDetectionPJConfig);
    }else  if(action =='view'){
        var url= tourl + "?fzjcid="+id;
        $.showDialog(url,'详情',viewPJDetectionConfig);}
    else  if(action =='add'){
            var url= tourl;
            $.showDialog(url,'新增',addPJDetection);
    }else if(action =='mod'){
        var url= tourl + "?fzjcid="+id;
        $.showDialog(url,'修改',modPJDetection);
    }else if(action =='reportgenerate'){
        var url= tourl + "?ids="+id+"&jclx="+$('#detectPJ_formSearch #jclx').val();
        $.showDialog(url,'报告生成',reportgeneratePJDetection);
    }else if(action =='del'){
        $.confirm('您确定要删除所选择的记录吗？',function(result){
            if(result){
                jQuery.ajaxSetup({async:false});
                var url=tourl;
                jQuery.post(url,{ids:id,"access_token":$("#ac_tk").val()},function(responseText){
                    setTimeout(function(){
                        if(responseText["status"] == 'success'){
                            $.success(responseText["message"],function() {
                                searchDetectPJResult();
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
    else if(action =='accept'){
        if(id!=null){
            var url= tourl + "?ybbh="+id+"&jclx="+$("#detectPJ_formSearch #jclx").val();
        }else{
            var url= tourl+"?jclx="+$("#detectPJ_formSearch #jclx").val();
        }
        $.showDialog(url,'标本接收',confirmPjConfig);
        $("#samplePjAcceptForm #ybbh").focus();
    }else if(action=="resultupload"){
        var url=tourl+"?jclx="+$("#detectPJ_formSearch #jclx").val();
        $.showDialog(url,'结果上传',uploadDetectionPJConfig);
    }

}

var uploadDetectionPJConfig = {
    width		: "900px",
    modalName	: "uploadDetectionPJModal",
    formName	: "uploadDetectionPJForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#uploadDetectionPJForm").valid()){
                    return false;
                }
                if(!$("#uploadDetectionPJForm #fjids").val()){
                    $.error("请上传文件！")
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#uploadDetectionPJForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"uploadDetectionPJForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.closeModal(opts.modalName);
                        $.showDialog("/detectionPJ/detectionPJ/pagedataFluUpView?fjid="+responseText["fjids"]+"&jclx="+responseText["jclx"],'上传进度',viewUpViewConfig);
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

var viewUpViewConfig = {
    width		: "1200px",
    modalName	:"viewUpViewModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "保 存",
            className : "btn-primary",
            callback : function() {
                if(t_map.length==0){
                    return false;
                }
                var data=$('#uploadDetectionPJViewForm #tb_list').bootstrapTable('getData');
                var jcjg_json=[];
                for (var i = 0; i < t_map.allDtos.length; i++) {
                    for(var j=0;j<data.length;j++){
                        if(t_map.allDtos[i].fzjcjgid==data[j].fzjcjgid){
                            t_map.allDtos[i].jcjg=$('#uploadDetectionPJViewForm input:radio[name="jcjg_'+data[j].fzjcjgid+'"]:checked').val();
                            break;
                        }
                    }
                    var sz={"fzjcjgid":'',"fzjcid":'',"fzxmid":'',"jgid":'',"ctz":'',"jcjg":'',"glid":''};
                    sz.fzjcjgid=t_map.allDtos[i].fzjcjgid;
                    sz.fzjcid=t_map.allDtos[i].fzjcid;
                    sz.fzxmid=t_map.allDtos[i].fzxmid;
                    sz.jgid=t_map.allDtos[i].jgid;
                    sz.ctz=t_map.allDtos[i].ctz;
                    sz.jcjg=t_map.allDtos[i].jcjg;
                    sz.glid=t_map.allDtos[i].glid;
                    jcjg_json.push(sz);
                }
                $("#uploadDetectionPJViewForm #jcjg_json").val(JSON.stringify(jcjg_json));
                if(t_map.fzzkjgDtos&&t_map.fzzkjgDtos.length>0){
                    var zkjg_json=[];
                    for (var i = 0; i < t_map.fzzkjgDtos.length; i++) {
                        var sz={"fzzkjgid":'',"zkmc":'',"glid":'',"ctz":'',"jgid":''};
                        sz.fzzkjgid=t_map.fzzkjgDtos[i].fzzkjgid;
                        sz.zkmc=t_map.fzzkjgDtos[i].zkmc;
                        sz.glid=t_map.fzzkjgDtos[i].glid;
                        sz.ctz=t_map.fzzkjgDtos[i].ctz;
                        sz.jgid=t_map.fzzkjgDtos[i].jgid;
                        zkjg_json.push(sz);
                    }
                    $("#uploadDetectionPJViewForm #zkjg_json").val(JSON.stringify(zkjg_json));
                }
                var $this = this;
                var opts = $this["options"] || {};
                $("#uploadDetectionPJViewForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"] || "uploadDetectionPJViewForm", function (responseText, statusText) {
                    if (responseText["status"] == 'success') {
                        $.success(responseText["message"], function () {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchDetectPJResult();
                        });
                    } else if (responseText["status"] == "fail") {
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"], function () {
                        });
                    } else {
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert("不好意思！出错了！");
                    }
                }, ".modal-footer > button");
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default",
        }
    }
};

var confirmPjConfig = {
    width: "800px",
    modalName: "confirmPjModal",
    offAtOnce: true,  //当数据提交成功，立刻关闭窗口
    onClose: function () {
        searchDetectPJResult();
    },
    buttons: {
        success : {
            label : "保存",
            className : "btn-primary",
            callback : function() {
                if (!$("#samplePjAcceptForm").valid()) {
                    return false;
                }
                var nbbh = $("#samplePjAcceptForm #nbbh").val();
                // $("#samplePjAcceptForm #px").val(nbbh.substring(1, nbbh.length - 1));
                var $this = this;
                var opts = $this["options"] || {};
                $("#samplePjAcceptForm input[name='access_token']").val($("#ac_tk").val());
                var szz = $('#samplePjAcceptForm input:radio[name="szz"]:checked').val();
                if (szz == $("#samplePjAcceptForm #ysszz").val()) {
                    $("#samplePjAcceptForm #grsz_flg").val(0);
                } else {
                    $("#samplePjAcceptForm #grsz_flg").val(1);
                }

                submitForm(opts["formName"] || "samplePjAcceptForm", function (responseText, statusText) {
                    if (responseText["status"] == 'success') {
                        preventResubmitForm(".modal-footer > button", false);
                        if (responseText["printUrl"]) {
                            let printUrl = responseText["printUrl"];
                            print_nbbh(printUrl);
                        }
                        $.success(responseText["message"], function () {
                            // Toast(responseText["message"],"#samplePjAcceptForm #toast");
                            $("#samplePjAcceptForm #ybbh").val("");
                            $("#samplePjAcceptForm #nbbh").val("");
                            $("#samplePjAcceptForm #syh").val("");
                            searchDetectPJResult();
                        });
                    } else if (responseText["status"] == "fail") {
                        preventResubmitForm(".modal-footer > button", false);
                        // Toast(responseText["message"],"#samplePjAcceptForm #toast");
                        $.error(responseText["message"], function () {
                        });
                    } else {
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert("不好意思！出错了！");
                        //修改弹窗为显示几秒消失
                        // Toast("不好意思！出错了！","#samplePjAcceptForm #toast");
                    }
                }, ".modal-footer > button");
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default",
            callback: function () {
                searchDetectPJResult();
            }
        }

    }
}

function print_nbbh(printUrl){
    var openWindow = window.open(printUrl+ "&access_token="+ $("#ac_tk").val());
    setTimeout(function(){
        openWindow.close();
    }, 300);
}

/*查看详情信息模态框*/
var viewPJDetectionConfig = {
    width		: "900px",
    height		: "1500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var addPJDetection = {
    width		: "1000px",
    height      : "1000px",
    modalName	: "addPJModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success7 : {
            label : "保存",
            className : "btn-primary",
            callback : function() {
                if(!$("#editHPVForm").valid()){
                    return false;
                }
                if($("#editHPVForm #yblx option:selected").text()=="--请选择--"){
                    $.alert("请选择样本类型！");
                    return false;
                }
                if($("#editHPVForm #ks option:selected").text()=="--请选择--"){
                    $.alert("请选择科室！");
                    return false;
                }
                if($("#editHPVForm #jcdw option:selected").text()=="--请选择--"){
                    $.alert("请选择检测单位！");
                    return false;
                }
                if($("#editHPVForm #sfjs").val()=="1"&&($("#editHPVForm #syh").val()==null||$("#editHPVForm #syh").val()==undefined||$("#editHPVForm #syh").val()=="")){
                    $.alert("是否接收为是，请选择实验号");
                    return false;
                }
                $("#editHPVForm #wybh").val($("#editHPVForm #ybbh").val());
                $("#editHPVForm #bbzbh").val($("#editHPVForm #ybbh").val());
                var $this = this;
                var opts = $this["options"]||{};
                $("#editHPVForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"editHPVForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchDetectPJResult();
                            }
                        });
                    }else if(responseText["status"] == "fail"){
                        // preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                            preventResubmitForm(".modal-footer > button", false);
                        });
                    } else{
                        preventResubmitForm(".modal-footer > button", false);
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
var reportgeneratePJDetection = {
    width		: "1000px",
    height      : "1000px",
    modalName	: "reportgeneratePJModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success7 : {
            label : "保存",
            className : "btn-primary",
            callback : function() {
                if(!$("#reportgeneratePJForm").valid()){
                    return false;
                }
                if($("#reportgeneratePJForm input:checkbox[name='fzxmids']:checked").length==0){
                    $.alert("请至少选择一个检测项目");
                    return false;
                }else if($("#reportgeneratePJForm input:checkbox[name='fzjczxmids']:checked").length==0){
                    $.alert("请至少选择一个检测子项目");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#reportgeneratePJForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"reportgeneratePJForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchDetectPJResult();
                            }
                        });
                    }else if(responseText["status"] == "fail"){
                        $.error(responseText["message"],function() {
                            preventResubmitForm(".modal-footer > button", false);
                        });
                    } else{
                        preventResubmitForm(".modal-footer > button", false);
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

var modPJDetection = {
    width		: "1000px",
    height      : "1000px",
    modalName	: "modPJModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success8 : {
            label : "保存",
            className : "btn-primary",
            callback : function() {
                if(!$("#editHPVForm").valid()){
                    return false;
                }
                if($("#editHPVForm #yblx option:selected").text()=="--请选择--"){
                    $.alert("请选择样本类型！");
                    return false;
                }
                if($("#editHPVForm #ks option:selected").text()=="--请选择--"){
                    $.alert("请选择科室！");
                    return false;
                }
                if($("#editHPVForm #jcdw option:selected").text()=="--请选择--"){
                    $.alert("请选择检测单位！");
                    return false;
                }
                if($("#editHPVForm #sfjs").val()=="1"&&($("#editHPVForm #syh").val()==null||$("#editHPVForm #syh").val()==undefined||$("#editHPVForm #syh").val()=="")){
                    $.alert("是否接收为是，请选择实验号");
                    return false;
                }
                $("#editHPVForm #wybh").val($("#editHPVForm #ybbh").val());
                $("#editHPVForm #bbzbh").val($("#editHPVForm #ybbh").val());
                var $this = this;
                var opts = $this["options"]||{};
                $("#editHPVForm input[name='access_token']").val($("#ac_tk").val());
                if(t_map.rows != null && t_map.rows.length > 0){
                   var json = [];
                   for(var i=0;i<t_map.rows.length;i++){
                        var sz = {"fzxmid":'',"bgrq":''};
                        sz.fzxmid = t_map.rows[i].fzxmid;
                        sz.bgrq = t_map.rows[i].bgrq;
                        json.push(sz);
                   }
                   $("#editHPVForm #jcxm_json").val(JSON.stringify(json));
                }


                submitForm(opts["formName"]||"editHPVForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchDetectPJResult();
                            }
                        });
                    }                    else if(responseText["status"] == "fail"){
                        // preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                            preventResubmitForm(".modal-footer > button", false);
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
var modPJDetectionConfig = {
    width		: "500px",
    modalName	: "modPJDetectionModal",
    formName	: "detectionPJ_Form",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success7 : {
            label : "保存",
            className : "btn-primary",
            callback : function() {
                if(!$("#detectionPJ_Form").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#detectionPJ_Form input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"detectionPJ_Form",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchDetectPJResult();
                            }
                        });
                    }
                    else if(responseText["status"] == "fail"){
                            // preventResubmitForm(".modal-footer > button", false);
                            $.error(responseText["message"],function() {
                                preventResubmitForm(".modal-footer > button", false);
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
var DetectPJreportDownloadConfig={
    width		: "700px",
    modalName	: "DetectPJreportDownloadConfig",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		:	{
        success 	: 	{
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#DownloadForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var bglxbj = $("#DownloadForm input[type='radio']:checked").val();
                var jclx = $("#DownloadForm #jclx").val();
                var map = {
                    access_token:$("#ac_tk").val(),
                    bglxbj:bglxbj,
                    jclx:jclx,
                };
                //判断有选中的采用选中导出，没有采用选择导出
                var sel_row = $('#detectPJ_formSearch #detectPJ_list').bootstrapTable('getSelections');//获取选择行数据
                var ids="";
                if(sel_row.length>=1){
                    for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                        ids = ids + ","+ sel_row[i].fzjcid;
                    }
                    ids = ids.substr(1);
                    map["ids"] = ids;
                }else{
                    getDetectPJSearchData(map);
                }
                var url= $("#DownloadForm #action").val();
                $.post(url,map,function(data){
                    if(data){
                        if(data.status == 'success'){
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            //创建objectNode
                            var cardiv =document.createElement("div");
                            cardiv.id="cardiv";
                            var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span id="totalCount" style="color:red;font-weight:600;">/' + data.count + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
                            cardiv.innerHTML=s_str;
                            //将上面创建的P元素加入到BODY的尾部
                            document.body.appendChild(cardiv);
                            setTimeout("checkReportZipStatus(2000,'"+ data.redisKey + "')",1000);
                            //绑定导出取消按钮事件
                            $("#exportCancel").click(function(){
                                //先移除导出提示，然后请求后台
                                if($("#cardiv").length>0) $("#cardiv").remove();
                                $.ajax({
                                    type : "POST",
                                    url : "/common/export/commCancelExport",
                                    data : {"wjid" : data.redisKey+"","access_token":$("#ac_tk").val()},
                                    dataType : "json",
                                    success:function(res){
                                        if(res != null && res.result==false){
                                            if(res.msg && res.msg!="")
                                                $.error(res.msg);
                                        }

                                    }
                                });
                            });
                        }else if(data.status == 'fail'){
                            $.error(data.error,function() {
                            });
                        }else{
                            preventResubmitForm(".modal-footer > button", false);
                            if(data.error){
                                $.alert(data.error,function() {
                                });
                            }
                        }
                    }
                },'json');
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}
//自动检查报告压缩进度
var checkReportZipStatus = function(intervalTime,redisKey){
    $.ajax({
        type : "POST",
        url : "/common/export/commCheckExport",
        data : {"wjid" : redisKey +"","access_token":$("#ac_tk").val()},
        dataType : "json",
        success:function(data){
            if(data.cancel){//取消导出则直接return
                return;
            }
            if(data.result == false){
                if(data.msg && data.msg!=""){
                    $.error(data.msg);
                    if($("#cardiv")) $("#cardiv").remove();
                }else{
                    if(intervalTime < 5000)
                        intervalTime = intervalTime + 1000;
                    if($("#exportCount")){
                        $("#exportCount").html(data.currentCount)
                        if(("/"+data.currentCount) == $("#totalCount").text()){
                            $("#totalCount").html($("#totalCount").text()+" 压缩中....")
                        }
                    }
                    setTimeout("checkReportZipStatus("+intervalTime+",'"+ redisKey +"')",intervalTime);
                }
            }else{
                if(data.msg && data.msg!=""){
                    $.error(data.msg);
                    if($("#cardiv")) $("#cardiv").remove();
                }
                else{
                    if($("#cardiv")) $("#cardiv").remove();
                    window.open("/common/export/commDownloadExport?wjid="+redisKey + "&access_token="+$("#ac_tk").val());
                }
            }
        }
    });
}

var resultModDetectionPJConfig= {
    width		: "1200px",
    modalName   :"resultModDetectionPJModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
//        success : {
//            label : "确认",
//            className : "btn-primary",
//            callback : function() {
//                if(!$("#resultModDetectionPJForm").valid()){
//                    return false;
//                }
//                var $this = this;
//                var opts = $this["options"]||{};
//                var json=[];
//                var ids=$("#resultModDetectionPJForm #ids").val();
//                var tab=$("#resultModDetectionPJForm .xmlist");
//                if(tab.length>0){
//                    for(var i=0;i<tab.length;i++){
//                        var sz = {"fzxmid":'',"jcjg":'',"fzjcid":'',"jgid":'',"ctz":'',"jcjgmc":''};
//                        sz.fzxmid =$("#resultModDetectionPJForm #tr_"+i).attr("fzxmid");
//                        sz.jgid = $("#resultModDetectionPJForm #tr_"+i).attr("pjjg");
//                        sz.fzjcid = $("#resultModDetectionPJForm #tr_"+i).attr("fzjcid");
//                        sz.ctz = $("#resultModDetectionPJForm #ctz_"+i).val();
//                        sz.jcjg= $("#resultModDetectionPJForm input[name=jg_"+i+"]:checked").val();
//                        sz.jcjgmc= $("#resultModDetectionPJForm input[name=jg_"+i+"]:checked").attr("csmc");
//                        json.push(sz);
//                    }
//                }
//
//                $("#resultModDetectionPJForm #ids").val(ids);
//                $("#resultModDetectionPJForm #jcjg_json").val(JSON.stringify(json));
//                $("#resultModDetectionPJForm input[name='access_token']").val($("#ac_tk").val());
//                submitForm(opts["formName"]||"resultModDetectionPJForm",function(responseText,statusText){
//                    if(responseText["status"] == 'success'){
//                        $.success(responseText["message"],function() {
//                            if(opts.offAtOnce){
//                                //提交审核
//                                if(responseText["auditType"]!=null){
//                                    showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
//                                        searchDetectPJResult();
//                                    });
//                                }else{
//                                    searchDetectPJResult();
//                                }
//                                $.closeModal(opts.modalName);
//                            }
//                        });
//                    }else if(responseText["status"] == "fail"){
//                        preventResubmitForm(".modal-footer > button", false);
//                        $.error(responseText["message"],function() {
//                        });
//                    } else{
//                        preventResubmitForm(".modal-footer > button", false);
//                        $.alert(responseText["message"],function() {
//                        });
//                    }
//                },".modal-footer > button");
//                return false;
//            }
//        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }

    }
};

//性别格式化
function xbformat(value,row,index){
    if(row.xb=='1'){
        return '男';

    }else  if(row.xb=='2'){
        return '女';
    }else{
        return '未知';
    }
}
/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
    if (row.zt == '10' ) {
        return "<span class='btn btn-warning' onclick=\"recallRequisitions('" + row.fzjcid +"','" + $("#detectPJ_formSearch #auditType").val()+ "',event)\" >撤回</span>";
    }else{
        return "";
    }
}

//撤回请购单提交
function recallRequisitions(fzjcid,auditType,event){
    var msg = '您确定要撤回该记录吗？';
    var purchase_params = [];
    $.confirm(msg,function(result){
        if(result){
            doAuditRecall(auditType,fzjcid,function(){
                searchDetectPJResult();
            },purchase_params);
        }
    });
}

/**
 * 结果格式化
 * @returns
 */
function jgformat(value,row,index) {
    var html="";
   if(row.jcjgmc=='阴性'){
       html="<a href='javascript:void(0);'  onclick=\"viewResultDetails('"+row.fzjcid+"')\"  style='color:green;'>"+"阴性"+"</a>";
   }else if(row.jcjgmc=='阳性'){
       html="<a href='javascript:void(0);'  onclick=\"viewResultDetails('"+row.fzjcid+"')\" style='color:red;'>"+"阳性"+"</a>";
   }else if(row.jcjgmc==null || row.jcjgmc==''){
       html="";
   }else{
       html="<a href='javascript:void(0);'  onclick=\"viewResultDetails('"+row.fzjcid+"')\" style='color:yellow;'>"+"可疑"+"</a>";
   }
   return html;
}

function viewResultDetails(fzjcid){
    var url="/detectionPJ/detectionPJ/pagedataDetectionPJResult?fzjcid="+fzjcid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'结果详情',viewResultConfig);
}
var viewResultConfig = {
    width		: "800px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
function xmsformat(value,row,index) {
    var xms = 0;
    var bgwcs = 0;
    if (row.bgwcs){
        bgwcs = row.bgwcs;
    }
    if (row.xms){
        xms = row.xms;
    }
    if (bgwcs>=xms){
        return "<span  style='color:green;'>"+bgwcs+"/"+xms+"</span>"
    }else if (bgwcs<xms&&bgwcs>0){
        return "<span  style='color:orange;'>"+bgwcs+"/"+xms+"</span>"
    }else {
        return "<span  style='color:red;'>"+bgwcs+"/"+xms+"</span>"
    }
}
/**
 * 审核列表的状态格式化函数
 * @returns
 */
function ztformat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fzjcid + "\",event,\"AUDIT_GENERAL_INSPECTION\")' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fzjcid + "\",event,\"AUDIT_GENERAL_INSPECTION\")' >审核未通过</a>";
    }else if(row.zt == '10'){
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fzjcid + "\",event,\"AUDIT_GENERAL_INSPECTION\")' >" + row.shxx_dqgwmc + "审核中</a>";
    }
    else{
        return '未提交';
    }
}


var detectPJ_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_query =$("#detectPJ_formSearch #btn_query");//模糊查询
        var btn_reportdownload = $("#detectPJ_formSearch #btn_reportdownload");//报告下载
        var btn_detection=$("#detectPJ_formSearch #btn_detection");//检测
        var btn_resultmod=$("#detectPJ_formSearch #btn_resultmod");//修改检测结果
        var btn_view =$("#detectPJ_formSearch #btn_view");//查看
        var btn_add =$("#detectPJ_formSearch #btn_add");//修改
        var btn_mod =$("#detectPJ_formSearch #btn_mod");//修改
        var btn_del = $("#detectPJ_formSearch #btn_del");//删除
	    var btn_accept =$("#detectPJ_formSearch #btn_accept");//样本接收
        var btn_reportgenerate=$("#detectPJ_formSearch #btn_reportgenerate");//重新生成新冠报告
        var btn_resultupload=$("#detectPJ_formSearch #btn_resultupload");//结果上传
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
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchDetectPJResult(true);
            });
        };
        //添加日期控件
        laydate.render({
            elem: '#jssjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#jssjend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#sysjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#sysjend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#bgrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#bgrqend'
            ,theme: '#2381E9'
        });
        //---------------------------------检验结果上传----------------------------------
        btn_resultupload.unbind("click").click(function(){
            DetectPJDealById(null,"resultupload",btn_resultupload.attr("tourl"));
        });
        /*---------------------------样本接收-----------------------------------*/
        btn_accept.unbind("click").click(function(){
            var sel_row = $('#detectPJ_formSearch #detectPJ_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if (sel_row[0].jcdxcsdm == "W"){//物检
                    // if (!sel_row[0].cjsj){
                    //     $.error("该用户还未录入！");
                    // }else if (!sel_row[0].ybbh){
                    //     $.error("标本编号不可为空！");
                    // }else{
                          DetectPJDealById(sel_row[0].ybbh,"accept",btn_accept.attr("tourl"));
                    // }
                }else {//人检
                    // if (!sel_row[0].cjsj){
                    //     $.error("该用户还未录入！");
                    // }else if (!sel_row[0].xm || !sel_row[0].xb || !sel_row[0].nl || !sel_row[0].zjh || !sel_row[0].zjlxmc  ){
                    //     $.error("个人信息不充足，是否缺少姓名、性别、年龄、证件号、证件类型等信息！");
                    // }else if (!sel_row[0].ybbh){
                    //     $.error("标本编号不可为空！");
                    // }
                    // else{
                          DetectPJDealById(sel_row[0].ybbh,"accept",btn_accept.attr("tourl"));
                    // }
                }
            }else{
                DetectPJDealById(null,"accept",btn_accept.attr("tourl"));
            }
        });
        //------------------------------------- 报告下载 ---------------------------------------
        btn_reportdownload.unbind("click").click(function(){
            //判断有选中的采用选中导出，没有采用选择导出
            var sel_row = $('#detectPJ_formSearch #detectPJ_list').bootstrapTable('getSelections');//获取选择行数据
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].fzjcid;
            }
            ids = ids.substr(1);
            DetectPJDealById(ids,"reportdownload",btn_reportdownload.attr("tourl"));
        });
        /* ------------------------------生成报告-----------------------------*/
        btn_reportgenerate.unbind("click").click(function(){
            var sel_row = $('#detectPJ_formSearch #detectPJ_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].fzjcid;
                }
                ids=ids.substr(1);
                DetectPJDealById(ids,"reportgenerate",btn_reportgenerate.attr("tourl"));
            }
        });
        /*--------------------------------修改检测状态-----------------------------*/
        btn_detection.unbind("click").click(function(){
            var sel_row = $('#detectPJ_formSearch #detectPJ_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids = "";
                for (var i = 0; i < sel_row.length; i++) {
                    if (sel_row[i].sfjs != '1' || sel_row[i].jssj == null) {
                        $.error("有标本未接收，不允许实验操作");
                        return;
                    }
                    ids = ids + "," + sel_row[i].fzjcid;
                }
                ids = ids.substr(1);
                var checkjcxm = true;
                $.ajax({
                    type: 'post',
                    url: "/detectionPJ/detectionPJ/pagedataCheckJcxm?jclx="+$("#detectPJ_formSearch #jclx").val(),
                    cache: false,
                    data: {"ids": ids, "access_token": $("#ac_tk").val()},
                    dataType: 'json',
                    success: function (data) {
                        //返回值
                        if (data == false) {
                            $.error("检测项目必须相同!");
                        } else {
                            DetectPJDealById(ids, "detection", btn_detection.attr("tourl"));
                        }
                    }
                });
            }
        });
        /*--------------------------------修改检测结果---------------------------*/
        btn_resultmod.unbind("click").click(function(){
            var sel_row=$('#detectPJ_formSearch #detectPJ_list').bootstrapTable('getSelections');
            if(sel_row.length>=1){
                // var ids="";
                var ids="";
                var str=sel_row[0].jcxmmc;
                var num=str.split(",").length;
                for(var i=0;i<sel_row.length;i++){
                    if(sel_row[i].sysj==null||sel_row[i].sysj==''){
                        $.error("标本编号为"+sel_row[i].ybbh+"的样本未实验！");
                        return;
                    }
                    if(sel_row[i].zt!='00' && sel_row[i].zt!='15') {
                        $.error("标本编号为"+sel_row[i].ybbh+"的数据，该状态不允许提交！");
                        return;
                    }
                    ids=ids+","+sel_row[i].fzjcid;
                }
                ids=ids.substring(1);
                DetectPJDealById(ids,"resultmod",btn_resultmod.attr("tourl"));

            }else{
                $.error("请至少选中一行");
            }
        });

        /*---------------------------查看详情信息表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#detectPJ_formSearch #detectPJ_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                DetectPJDealById(sel_row[0].fzjcid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------------------新增---------------------------*/
        btn_add.unbind("click").click(function(){
            DetectPJDealByIds(null,$('#detectPJ_formSearch #jclx').val(),"add",btn_add.attr("tourl"));
        });
        /*---------------------------修改信息表-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#detectPJ_formSearch #detectPJ_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                DetectPJDealByIds(sel_row[0].fzjcid,$('#detectPJ_formSearch #jclx').val(),"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        btn_del.unbind("click").click(function(){
            var sel_row = $('#detectPJ_formSearch #detectPJ_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].fzjcid;
            }
            ids = ids.substr(1);
            DetectPJDealById(ids,"del",btn_del.attr("tourl"));
        });



        /**显示隐藏**/
        $("#detectPJ_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(DetectPJ_turnOff){
                $("#detectPJ_formSearch #searchMore").slideDown("low");
                DetectPJ_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#detectPJ_formSearch #searchMore").slideUp("low");
                DetectPJ_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    };
    return oInit;
};


function searchDetectPJResult(isTurnBack){
    //关闭高级搜索条件
    $("#detectPJ_formSearch #searchMore").slideUp("low");
    DetectPJ_turnOff=true;
    $("#detectPJ_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#detectPJ_formSearch #detectPJ_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#detectPJ_formSearch #detectPJ_list').bootstrapTable('refresh');
    }
}

$(function(){
    // 1.初始化Table
    var oTable = new DetectPJ_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new detectPJ_ButtonInit();
    oButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#detectPJ_formSearch .chosen-select').chosen({width: '100%'});
    $("#detectPJ_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});
