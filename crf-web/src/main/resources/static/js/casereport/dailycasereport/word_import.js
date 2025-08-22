
var impReport_TableInit = function (fjid) {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#wordImportForm #tb_list').bootstrapTable({
            url: $('#wordImportForm #urlPrefix').val() + '/crf/hzxx/importReportPage',		 //请求后台的URL（*）
            method: 'get',					  //请求方式（*）
            toolbar: '#wordImportForm #toolbar', //工具按钮用哪个容器
            striped: true,					  //是否显示行间隔色
            cache: false,					   //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,				   //是否显示分页（*）
            sortable: true,					 //是否启用排序
            sortName:"lrsj",					//排序字段
            sortOrder: "desc",				   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",		   //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,					   //初始化加载第一页，默认第一页
            pageSize: 999,					  //每页的记录行数（*）
            pageList: [999, 1999],				//可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,					  //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: false,				  //是否显示所有的列
            showRefresh: false,				  //是否显示刷新按钮
            minimumCountColumns: 2,			 //最少允许的列数
            clickToSelect: false,				//是否启用点击选中行
            //height: 500,						//行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "fjid",					 //每一行的唯一标识，一般为主键列
            showToggle:false,					//是否显示详细视图和列表视图的切换按钮
            cardView: false,					//是否显示详细视图
            detailView: false,				   //是否显示父子表
            columns: [{
                field: 'hzid',
                title: '患者ID',
                width: '5%',
                align: 'left',
                sortable: false,
                visible: false
            },{
                field: 'hzxm',
                title: '患者姓名',
                width: '7%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zyh',
                title: '住院号',
                width: '7%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jldjt',
                title: '记录第几天',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'nrsj',
                title: '纳入时间',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'nl',
                title: '年龄',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xb',
                title: '性别',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jzks',
                title: '就诊科室',
                width: '7%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'brlb',
                title: '病人类别',
                width: '7%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jwhbz',
                title: '既往合并症',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'rysj',
                title: '入院时间',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'grbw',
                title: '感染部位',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'kjywbls',
                title: '抗菌药物暴露史',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'kjywjjtzl',
                title: '抗菌药物降阶梯治疗',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'kjzlc',
                title: '抗菌药物治疗总疗程',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xghxytysj',
                title: '血管活性药物停用时间',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'crrttysj',
                title: 'CRRT停用时间',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'hxjtysj',
                title: '呼吸机停用时间',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'cicusj',
                title: '出ICU时间',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'cicuzt',
                title: '出ICU状态',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'cysj',
                title: '出院时间',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'cyzt',
                title: '出院状态',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'hrmax',
                title: 'HRmax',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'mapmax',
                title: 'MAPmax',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sapmax',
                title: 'SAPmax',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'rrmax',
                title: 'RRmax',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'tmax',
                title: 'Tmax',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jxtq',
                title: '机械通气',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'crrt',
                title: 'CRRT',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'gcs',
                title: 'GCS',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xghxy',
                title: '血管活性药',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'rs',
                title: '乳酸',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'k',
                title: 'K+',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'na',
                title: 'Na+',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'ca',
                title: 'Ca2+',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'ph',
                title: 'pH',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'paco2',
                title: 'PaCO2',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'pao2',
                title: 'PaO2',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'abe',
                title: 'Abe',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'hco3',
                title: 'HCO3-',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'fio2',
                title: 'FiO2',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'crp',
                title: 'CRP',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'pct',
                title: 'PCT',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'il6',
                title: 'IL-6',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'il1b',
                title: 'IL-1B',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'il10',
                title: 'IL-10',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'nsd',
                title: '尿素氮',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'bdb',
                title: '白蛋白',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jg',
                title: '肌酐',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zdhs',
                title: '总胆红素',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wbc',
                title: 'WBC',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'plt',
                title: 'PLT',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'lcount',
                title: 'Lcount',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sjqsfsykjyw',
                title: '使用抗菌药物',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'kjywzl',
                title: '抗菌药种类',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'mcfdna',
                title: 'McfDNA',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'mcfdnajg',
                title: 'McfDNA结果',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xpy',
                title: '血培养',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xpyjg',
                title: '血培养结果',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'ttp',
                title: '痰涂片',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'ttpjg',
                title: '痰涂片结果',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'tpy',
                title: '痰培养',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'tpyjg',
                title: '痰培养结果',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'fstp',
                title: '腹水涂片',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'fstpjg',
                title: '腹水涂片结果',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'fspy',
                title: '腹水培养',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'fspyjg',
                title: '腹水培养结果',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'qtf',
                title: '其它1',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'qtfjg',
                title: '其它1结果',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'qtt',
                title: '其它2',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'qttjg',
                title: '其它2结果',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            },
        });
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
            access_token: $("#ac_tk").val(),
            fjid: fjid,
            ywlx: $("#wordImportForm #ywlx").val(),
        };

        return map;
    };
    return oTableInit;
};



//信息显示配置
var showConfig = function(errormsg,successmsg){
    var formname = "#wordImportForm";
    $(formname + " #successdiv").addClass("hidden");
    $(formname + " #errordiv").addClass("hidden");
    $(formname + " #btn-cancel").addClass("hidden");
    $(formname + " #btn-continue").addClass("hidden");
    $(formname + " #btn-complete").addClass("hidden");


    if(errormsg){
        $(formname + " #errormsg").html(errormsg);
        $(formname + " #errordiv").removeClass("hidden");
        $(formname + " #btn-complete").removeClass("hidden");
        return;
    }else{
        if(successmsg){
            $(formname + " #successmsg").html(successmsg);
            $(formname + " #successdiv").removeClass("hidden");
            $(formname + " #btn-continue").removeClass("hidden");
            $(formname + " #btn-cancel").removeClass("hidden");
        }
    }
}
//上传后自动提交服务器检查进度
var checkImpCheckStatus = function(intervalTime,fjid){
    var formname = "#wordImportForm";
    $.ajax({
        type : "POST",
        url : $('#wordImportForm #urlPrefix').val() + "/crf/file/checkImpInfo",
        data : {"fjid":fjid,"access_token":$("#ac_tk").val()},
        dataType : "json",
        success:function(data){
            if(data.errormsg && data.errormsg!="") {
                $(formname + " #errormsg").html(data.errormsg);
                showConfig(data.errormsg);
            }else{
                $(formname + " #successmsg").html(data.successmsg);
                showConfig(data.errormsg,data.successmsg);
                var oTable = new impReport_TableInit(fjid);
                oTable.Init();
            }
        }
    });
};


var reportImp_ButtonInit = function(){
    var oInits = new Object();
    var formname ="#wordImportForm";
    oInits.Init = function () {
        //取消按钮
        $(formname + " #btn-cancel").unbind("click").click(function(){
            $(formname + " #imp_file")
                .fileinput('clear')
                .fileinput('unlock');
            $(formname + " #imp_file")
                .parent()
                .siblings('.fileinput-remove')
                .hide();
            $('#wordImportForm #update_table').html('<table id="tb_list" class="table table-hover table-bordered"></table>');
            $(formname + " #errordiv").addClass("hidden");
            $(formname + " #successdiv").addClass("hidden");
            $(formname + " #btn-cancel").addClass("hidden");
            $(formname + " #btn-continue").addClass("hidden");
            $(formname + " #btn-complete").addClass("hidden");
        });
        //继续
        $(formname + " #btn-continue").unbind("click").click(function(){
            var ssyy=$.trim(jQuery('#wordImportForm #ssyy').val());
            var yz=$.trim(jQuery('#wordImportForm #yz').val());
            if(ssyy=="请选择"||yz=="请选择"){
                $.alert("所属医院和亚组都需要选择");
                return;
            }
            $(formname + " #errordiv").addClass("hidden");
            $(formname + " #successdiv").addClass("hidden");
            $(formname + " #btn-cancel").addClass("hidden");
            $(formname + " #btn-continue").addClass("hidden");
            $(formname + " #btn-complete").addClass("hidden");
            var s_fjid = $("#wordImportForm #fjid").val();
            $.ajax({
                type : "POST",
                url : $("#wordImportForm #urlPrefix").val()+"/crf/file/saveImportRecord",
                data : {"fjid":s_fjid,"access_token":$("#ac_tk").val(),ywlx: $("#wordImportForm #ywlx").val(),ssyy:ssyy,yz:yz},
                dataType : "json",
                success:function(data){
                    if(data.status == "fail")
                    {
                        $.error("出现重复数据或者服务器异常," + data.msg);
                        $(formname + " #btn-cancel").removeClass("hidden");
                    }else{
                        $.success(data.msg);
                        $(formname + " #btn-complete").removeClass("hidden");
                    }
                }
            });
        });

        //完成
        $(formname + " #btn-complete").unbind("click").click(function(){
            $(formname + " #imp_file")
                .fileinput('clear')
                .fileinput('unlock');
            $(formname + " #imp_file")
                .parent()
                .siblings('.fileinput-remove')
                .hide();
            $('#wordImportForm #update_table').html('<table id="tb_list" class="table table-hover table-bordered"></table>');
            $(formname + " #errordiv").addClass("hidden");
            $(formname + " #successdiv").addClass("hidden");
            $(formname + " #btn-cancel").addClass("hidden");
            $(formname + " #btn-continue").addClass("hidden");
            $(formname + " #btn-complete").addClass("hidden");

        });


        //下载模板按钮
        $(formname + " #template_dr").unbind("click").click(function(){
            $("#updateWordForm #access_token").val($("#ac_tk").val());
            $("#updateWordForm").submit();
        });
    }
    return oInits;
}

function displayUpInfo(fjid){
    $("#wordImportForm #fjid").val(fjid);
    //1.初始化Table
    setTimeout("checkImpCheckStatus(2000,'"+ fjid + "')",1000);
}

$(function () {

    //0.初始化fileinput,这是公用方法，调用comdefine.js里的方法
    var oFileInput = new FileInput();
    //ctrlName,callback,impflg,singleFlg,fileName,importType
    //参数说明 singleFlg 好像是标明 是否单文件上传限制，有点忘记了
    //impflg 用于后台判断是否为导入。1为导入，则开启线程进行导入  2：代表附件保存 3固定表头

    var document_params=[];
    document_params.prefix=$('#wordImportForm #urlPrefix').val();
    oFileInput.Init("wordImportForm","displayUpInfo",1,1,"imp_file","",document_params);
    //2.初始化Button的点击事件
    var oimpButtonInit = new reportImp_ButtonInit();
    oimpButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#wordImportForm .chosen-select').chosen({width: "200px"});
});