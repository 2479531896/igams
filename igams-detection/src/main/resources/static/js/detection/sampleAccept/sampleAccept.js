var t_map = [];
var Hzxx_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    var ybbh = $("#sampleAcceptForm #ybbh").val();
    oTableInit.Init = function (){
        $('#sampleAcceptForm #hzxx_list').bootstrapTable({
            url: '/detection/detection/pagedataListHzxx',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#sampleAcceptForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: false,                     //是否启用排序
            sortName: "fzjcid",				//排序字段
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
            isForceTable: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "fzjcid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns:[{
                field: 'fzjcid',
                title: '分子检测ID',
                titleTooltip:'分子检测ID',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'xm',
                title: '患者姓名',
                titleTooltip:'患者姓名',
                width: '5%',
                align: 'left',
                visible:true
            },{
                field: 'nl',
                title: '年龄',
                titleTooltip:'年龄',
                width: '4%',
                align: 'left',
                visible:true
            },{
                field: 'xb',
                title: '性别',
                titleTooltip:'性别',
                width: '4%',
                align: 'left',
                visible:true
            },{
                field: 'sj',
                title: '电话',
                titleTooltip:'电话',
                width: '6%',
                align: 'left',
                visible:true
            }, {
                field: 'yblx',
                title: '标本类型',
                titleTooltip:'标本类型',
                width: '6%',
                align: 'left',
                visible:true
            },{
                field: 'jcxmmc',
                title: '检测项目',
                titleTooltip:'检测项目',
                width: '6%',
                align: 'left',
                visible:true
            },{
                field: 'cjsj',
                title: '采集时间',
                titleTooltip:'采集时间',
                width: '6%',
                align: 'left',
                visible:true
            }, {
                field: 'cjryxm',
                title: '采集人员',
                titleTooltip:'采集人员',
                width: '5%',
                align: 'left',
                visible:true
            }],
            onLoadSuccess: function (map) {
                t_map = map;
            },
            onLoadError: function () {
            },
            onDblClickRow: function (row, $element) {
            },
        });
    };
    // 得到查询的参数
    oTableInit.queryParams = function(params){
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            access_token: $("#ac_tk").val(),
            ybbh: ybbh,
        };
        return map;
    };
    return oTableInit;
};


function changeUrl(){
    var bh = $("#sampleAcceptForm #ybbh").val();
    //当扫描到特殊标签时，取消查询编号是否存在的操作，直接存数据库
    if(bh=='-K1'||bh=='-K2'||bh=='-K3'||bh=='-PC'||bh=='-NC'||bh=='-QC') {
        $("#sampleAcceptForm").attr("action","/detection/detection/insertKzbInfo");
        $("#sampleAcceptForm #syh").attr("disabled","disabled");
    }else{
        $("#sampleAcceptForm").attr("action","/detection/detection/acceptSaveSample");
        $("#sampleAcceptForm #syh").removeAttr("disabled");
    }
}

/**
 * 内网打印机
 * @returns
 */
$("#sampleAcceptForm #local_ip").click(function () {
    $("#sampleAcceptForm #remoteDiv").hide();
    $("#sampleAcceptForm #glxx").attr("disabled", "disabled");
})

/**
 *
 * @returns
 */
$("#sampleAcceptForm #remote_ip").click(function () {
    $("#sampleAcceptForm #remoteDiv").show();
    $("#sampleAcceptForm #glxx").removeAttr("disabled");
})

function printerIpChecked() {
    if ($("#sampleAcceptForm #local_ip").attr("checked")) {
        $("#sampleAcceptForm #remoteDiv").hide();
        $("#sampleAcceptForm #glxx").attr("disabled", "disabled");
    } else if ($("#sampleAcceptForm #remote_ip").attr("checked")) {
        $("#sampleAcceptForm #remoteDiv").show();
        $("#sampleAcceptForm #glxx").removeAttr("disabled");
    }
}


function kzbcz() {
    $("#sampleAcceptForm #kzbmc").show();
    $("#sampleAcceptForm #kzbmc").removeAttr("disabled");
    $("#sampleAcceptForm #kzbmc_sel").hide();
    $("#sampleAcceptForm #kzbmc_sel").attr("disabled","disabled");
    for(var i=1;i<=12;i++){
        for(var j=1;j<=8;j++){
            $("#sampleAcceptForm #ybbh"+j+'-'+i).css("background-color","#eee");
            $("#sampleAcceptForm #ybbh"+j+'-'+i).val("");
            $("#sampleAcceptForm #ybbh"+j+'-'+i).css("border","1px solid #ccc");
        }
    }
    $("#sampleAcceptForm #hs").val("1");
    $("#sampleAcceptForm #ls").val("1");
    $("#sampleAcceptForm #kzbxh").val("0");
    $("#sampleAcceptForm #ybbh1-1").css("border","3px solid red");
    // smqsm()
}

function kzbcz_back() {
    $.ajax({
        url : "/detection/pagedataAllKzb",
        type : "post",
        data : {"access_token":$("#ac_tk").val()},
        dataType : "json",
        success:function(data){
            if(data != null && data.length != 0){
                var csHtml = "";
                csHtml += "<option value=''>--请选择--</option>";
                $.each(data,function(i){
                    csHtml += "<option value='" + data[i].kzbid + "'>" + data[i].kzbh + "</option>";
                });
                $("#sampleAcceptForm #kzbmc_sel").empty();
                $("#sampleAcceptForm #kzbmc_sel").append(csHtml);
                $("#sampleAcceptForm #kzbmc_sel").trigger("chosen:updated");
            }else{
                var csHtml = "";
                csHtml += "<option value=''>--请选择--</option>";
                $("#sampleAcceptForm #kzbmc_sel").empty();
                $("#sampleAcceptForm #kzbmc_sel").append(csHtml);
                $("#sampleAcceptForm #kzbmc_sel").trigger("chosen:updated");
            }
        }
    });


    $("#sampleAcceptForm #kzbmc_sel").show();
    $("#sampleAcceptForm #kzbmc_sel").removeAttr("disabled");
    $("#sampleAcceptForm #kzbmc").hide();
    $("#sampleAcceptForm #kzbmc").attr("disabled","disabled");
    // smqsm()
}

function selectKzb(hs,ls){
    for(var i=1;i<=12;i++){
        for(var j=1;j<=8;j++){
            if(i==ls&&j==hs){
                $("#sampleAcceptForm #ybbh"+j+'-'+i).css("border","3px solid red");
            }else{
                $("#sampleAcceptForm #ybbh"+j+'-'+i).css("border","1px solid #ccc");
            }
        }
    }
    $("#sampleAcceptForm #hs").val(hs);
    $("#sampleAcceptForm #ls").val(ls);
}

$("#sampleAcceptForm #kzbmc_sel").on('change',function(){
    var kzbid=$("#sampleAcceptForm #kzbmc_sel").val();
    $.ajax({
        url : "/detection/pagedataKzbmx",
        type : "post",
        data : {kzbid:kzbid,"access_token":$("#ac_tk").val()},
        dataType : "json",
        success:function(data){
            if(data != null && data.length != 0){
                for(var i=1;i<=12;i++){
                    for(var j=1;j<=8;j++){
                        $("#sampleAcceptForm #ybbh"+j+'-'+i).css("border","1px solid #ccc");
                        $("#sampleAcceptForm #ybbh"+j+'-'+i).val("");
                        $("#sampleAcceptForm #ybbh"+j+'-'+i).css("background-color","#eee");
                    }
                }
                for(var i=1;i<=12;i++){
                    for(var j=1;j<=8;j++){
                        for(var k=0;k<data.length;k++){
                            if(i.toString()==data[k].ls&&j.toString()==data[k].hs){
                                $("#sampleAcceptForm #ybbh"+j+'-'+i).css("background-color","#00FF00");
                                $("#sampleAcceptForm #ybbh"+j+'-'+i).val(data[k].syh);
                                break;
                            }
                        }
                    }
                }
                $("#sampleAcceptForm #kzbxh").val(data.length);
            }
            $("#sampleAcceptForm #hs").val("");
            $("#sampleAcceptForm #ls").val("");
        }
    });
});


function nbbhbt() {
    $("#sampleAcceptForm #nbbhspan").show();
    // smqsm()
}
function changeKzb() {
    var ls = $("#sampleAcceptForm #ls").val();
    var hs =$("#sampleAcceptForm #hs").val();
    if(ls!=null&&ls!=''&&hs!=null&&hs!=''){
        if(parseInt(hs)<1||parseInt(hs)>8){
            $.error("行数超出范围！");
            return;
        }
        if(parseInt(ls)<1||parseInt(ls)>12){
            $.error("列数超出范围！");
            return;
        }
        for(var i=1;i<=12;i++){
            for(var j=1;j<=8;j++){
                if(i==ls&&j==hs){
                    $("#sampleAcceptForm #ybbh"+j+'-'+i).css("border","3px solid red");
                }else{
                    $("#sampleAcceptForm #ybbh"+j+'-'+i).css("border","1px solid #ccc");
                }
            }
        }
    }
    // smqsm()
}


function nbbhfbt() {
    $("#sampleAcceptForm #nbbhspan").hide();
}

$("#keydown").bind("keydown", function (e) {
    let evt = window.event || e;
    if (evt.keyCode == 13) {
        smqsm()
    }
});

function syhyz() {
    let syh = $("#sampleAcceptForm #syh").val();
    if (syh > 99999){
        $.alert("实验号位数不能超过5位！");
        return
    }
    $("#sampleAcceptForm #syh").val(PrefixZero(syh, 5));
}
function PrefixZero(num, n) {
    return (Array(n).join(0) + num).slice(-n);
}
function smqsm() {
//回车执行查询
    let glxx = $("#sampleAcceptForm #glxx").val();
    let remote_ip = $("#sampleAcceptForm #remote_ip").prop('checked');
    if (!glxx && remote_ip == true){
        $.alert("请填写打印地址！");
        return
    }
    let bh = $("#sampleAcceptForm #ybbh").val();
    //当扫描到特殊标签时，取消查询编号是否存在的操作，直接存数据库
    if(bh=='-K1'||bh=='-K2'||bh=='-K3'||bh=='-PC'||bh=='-NC'||bh=='-QC'){
        let url =  "/detection/detection/insertKzbInfo";
        var cz= $('input[name="cz"]:checked').val();
        var hs= $("#sampleAcceptForm #hs").val();
        if(hs==null||hs==''){
            $.error("行数不得为空！");
            return;
        }
        var ls= $("#sampleAcceptForm #ls").val();
        if(ls==null||ls==''){
            $.error("列数不得为空！");
            return;
        }
        var kzbxh= $("#sampleAcceptForm #kzbxh").val();
        var kzbmc= $("#sampleAcceptForm #kzbmc").val();
        var kzbmc_sel= $("#sampleAcceptForm #kzbmc_sel").val();
        var jcdw= $("#sampleAcceptForm #jcdw").val();
        if(cz=='1'){
            if(kzbmc==null||kzbmc==''){
                $.error("扩增板名称不得为空！");
                return;
            }
        }else if(cz=='0'){
            if(kzbmc_sel==null||kzbmc_sel==''){
                $.error("扩增板名称未选择！");
                return;
            }                        }
        $.ajax({
            type: 'post',
            url: url,
            data: {"ybbh": bh,"access_token": $("#ac_tk").val(),"cz":cz,
                "hs":hs,"ls":ls,"kzbxh":kzbxh,"kzbmc":kzbmc,"kzbmc_sel":kzbmc_sel,"syh":bh,"jcdw":jcdw},
            dataType: 'json',
            success: function (data) {
                if (data.status == 'success') {
                    preventResubmitForm(".modal-footer > button", false);
                    // $.success(data.message, function () {
                    $("#sampleAcceptForm #ybbh").val("");
                    t_map.rows = [];
                    $("#sampleAcceptForm #hzxx_list").bootstrapTable('load',t_map);
                    $("#sampleAcceptForm #syh").val("");
                    $("#sampleAcceptForm #ybbh").focus();
                    var kzbid=data.kzbid;
                    if(data.cz=='0'){
                        var hs=data.hs;
                        var ls=data.ls;
                        var xh= $("#sampleAcceptForm #kzbxh").val();
                        var newxh=parseInt(xh)+1;
                        var j=parseInt(hs)+1;
                        var i=parseInt(ls)+1;
                        if(newxh==96){
                            var cz = document.getElementsByName('cz');
                            for(var i=0;i<cz.length;i++){
                                if(cz[i].value == "1")
                                {
                                    cz[i].checked=true; //选中
                                    //country[i].checked=false; //不选中
                                }
                            }
                            kzbcz();
                            return;
                        }
                        if(j>8){
                            if(data.sqlcz=='insert'){
                                $("#sampleAcceptForm #ybbh"+hs+'-'+ls).css("background-color","#00FF00");
                                $("#sampleAcceptForm #ybbh"+hs+'-'+ls).val(bh);
                                $("#sampleAcceptForm #ybbh"+hs+'-'+ls).css("border","1px solid #ccc");
                                $("#sampleAcceptForm #hs").val("1");
                                $("#sampleAcceptForm #ls").val(i);
                                $("#sampleAcceptForm #ybbh1-"+i).css("border","3px solid red");;
                                $("#sampleAcceptForm #kzbxh").val(newxh);
                            }else if(data.sqlcz=='update'){
                                $("#sampleAcceptForm #ybbh"+hs+'-'+ls).css("background-color","#00FF00");
                                $("#sampleAcceptForm #ybbh"+hs+'-'+ls).val(bh);
                                $("#sampleAcceptForm #kzbxh").val(xh);
                            }
                        }else{
                            if(data.sqlcz=='insert'){
                                $("#sampleAcceptForm #ybbh"+hs+'-'+ls).css("background-color","#00FF00");
                                $("#sampleAcceptForm #ybbh"+hs+'-'+ls).val(bh);
                                $("#sampleAcceptForm #ybbh"+hs+'-'+ls).css("border","1px solid #ccc");
                                $("#sampleAcceptForm #hs").val(j);
                                $("#sampleAcceptForm #ybbh"+j+'-'+ls).css("border","3px solid red");
                                $("#sampleAcceptForm #kzbxh").val(newxh);
                            }else if(data.sqlcz=='update'){
                                $("#sampleAcceptForm #ybbh"+hs+'-'+ls).css("background-color","#00FF00");
                                $("#sampleAcceptForm #ybbh"+hs+'-'+ls).val(bh);
                                $("#sampleAcceptForm #kzbxh").val(xh);
                            }
                        }
                    }else if(data.cz=='1'){
                        var cz = document.getElementsByName('cz');
                        for(var i=0;i<cz.length;i++){
                            if(cz[i].value == "0")
                            {
                                cz[i].checked=true; //选中
                                //country[i].checked=false; //不选中
                            }
                        }
                        $.ajax({
                            url : "/detection/pagedataAllKzb",
                            type : "post",
                            data : {"access_token":$("#ac_tk").val()},
                            dataType : "json",
                            success:function(data){
                                if(data != null && data.length != 0){
                                    var csHtml = "";
                                    csHtml += "<option value=''>--请选择--</option>";
                                    $.each(data,function(i){
                                        if(data[i].kzbid ==kzbid){
                                            csHtml += "<option value='" + data[i].kzbid + "' selected>" + data[i].kzbh + "</option>";
                                        }else{
                                            csHtml += "<option value='" + data[i].kzbid + "'>" + data[i].kzbh + "</option>";
                                        }
                                    });
                                    $("#sampleAcceptForm #kzbmc_sel").empty();
                                    $("#sampleAcceptForm #kzbmc_sel").append(csHtml);
                                    $("#sampleAcceptForm #kzbmc_sel").trigger("chosen:updated");
                                }else{
                                    var csHtml = "";
                                    csHtml += "<option value=''>--请选择--</option>";
                                    $("#sampleAcceptForm #kzbmc_sel").empty();
                                    $("#sampleAcceptForm #kzbmc_sel").append(csHtml);
                                    $("#sampleAcceptForm #kzbmc_sel").trigger("chosen:updated");
                                }
                            }
                        });
                        $("#sampleAcceptForm #kzbmc_sel").show();
                        $("#sampleAcceptForm #kzbmc_sel").removeAttr("disabled");
                        $("#sampleAcceptForm #kzbmc").hide();
                        $("#sampleAcceptForm #kzbmc").attr("disabled","disabled");
                        $.ajax({
                            url : "/detection/pagedataKzbmx",
                            type : "post",
                            data : {kzbid:data.kzbid,"access_token":$("#ac_tk").val()},
                            dataType : "json",
                            success:function(data){
                                if(data != null && data.length != 0){
                                    for(var i=1;i<=12;i++){
                                        for(var j=1;j<=8;j++){
                                            $("#sampleAcceptForm #ybbh"+j+'-'+i).css("border","1px solid #ccc");
                                            $("#sampleAcceptForm #ybbh"+j+'-'+i).val("");
                                            $("#sampleAcceptForm #ybbh"+j+'-'+i).css("background-color","#eee");
                                        }
                                    }
                                    for(var i=1;i<=12;i++){
                                        for(var j=1;j<=8;j++){
                                            for(var k=0;k<data.length;k++){
                                                if(i.toString()==data[k].ls&&j.toString()==data[k].hs){
                                                    $("#sampleAcceptForm #ybbh"+j+'-'+i).css("background-color","#00FF00");
                                                    $("#sampleAcceptForm #ybbh"+j+'-'+i).val(bh);
                                                    $("#sampleAcceptForm #ybbh"+j+'-'+i).css("border","1px solid #ccc");
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    $("#sampleAcceptForm #kzbxh").val(data.length);
                                }
                                $("#sampleAcceptForm #hs").val("2");
                                $("#sampleAcceptForm #ls").val("1");
                                $("#sampleAcceptForm #ybbh2-1").css("border","3px solid red");;
                            }
                        });
                    }
                    // });
                } else if (data.status == "fail") {
                    preventResubmitForm(".modal-footer > button", false);
                    $.error(data.message, function () {
                    });
                } else {
                    preventResubmitForm(".modal-footer > button", false);
                    $.alert("不好意思！出错了！");
                }
            }
        });
    }else{
        let temps = bh.split("?");
        let ybbh = "";
        if (temps.length > 1){
            ybbh = temps[1].split("=")[2];
        }else{
            ybbh = bh;
        }
        if (ybbh){
            ybbh = ybbh.toUpperCase();
            let sfjs;
            let checked =$("#sampleAcceptForm #sfjs0").prop('checked');
            if ( checked == true) {
                sfjs = "0";
            } else {
                sfjs = "1";
            }
            var url = "/detection/detection/pagedataSampleAcceptInfo";
            // url = $('#sampleAcceptForm #urlPrefix').val()+url;
            $.ajax({
                type: 'post',
                url: url,
                data: {"ybbh": ybbh, "sfjs": sfjs, "access_token": $("#ac_tk").val()},
                dataType: 'json',
                success: function (data) {
                    if(data.fzjcxxDtoInfo==null){
                        $.error("没有该标本编码!");
                        return;
                    }
                    var jcdw= $("#sampleAcceptForm #jcdw").val();
                    t_map.rows = data.fzjcxxDtos;
                    $("#sampleAcceptForm #hzxx_list").bootstrapTable('load',t_map);
                    if (data.fzjcxxDtoInfo != null) {
                        if (data.fzjcxxDtoInfo.ybbh) {
                            $("#sampleAcceptForm #ybbh").val(data.fzjcxxDtoInfo.ybbh);
                        }
                        if (data.fzjcxxDtoInfo.nbbh) {
                            $("#sampleAcceptForm #nbbh").val(data.fzjcxxDtoInfo.nbbh);
                        }
                        if (data.fzjcxxDtoInfo.sfjs) {
                            let sfjs = data.fzjcxxDtoInfo.sfjs;
                            if (null != sfjs && '' != sfjs) {
                                if (sfjs == '0') {
                                    document.getElementById("sfjs0").checked = "checked";
                                    document.getElementById("sfjs1").checked = "";
                                } else {
                                    document.getElementById("sfjs0").checked = "";
                                    document.getElementById("sfjs1").checked = "checked";
                                }
                            }
                        }
                        if (data.fzjcxxDtoInfo.syh) {
                            $("#sampleAcceptForm #syh").val(data.fzjcxxDtoInfo.syh);
                        }
                        let jcdxcsdm=data.fzjcxxDtoInfo.jcdxcsdm;
                        let bcchecked =$("#sampleAcceptForm #bc1").prop('checked');
                        let sychecked =$("#sampleAcceptForm #sfsy1").prop('checked');
                        let printcheck = $("#sampleAcceptForm #local_ip").prop('checked');
                        let szz = 1;
                        if (printcheck ==true){
                            szz =0;
                        }
                        let grsz_flg ;
                        if (szz == $("#sampleAcceptForm #ysszz").val()) {
                            grsz_flg = 0;
                        } else {
                            grsz_flg = 1;
                        }
                        let sfsy = 0;
                        if (sychecked){
                            sfsy = 1;
                        }
                        if ( bcchecked == true){
                            let bcurl =  "/detection/detection/acceptSaveSample";
                            let nbbh = $("#sampleAcceptForm #nbbh").val();
                            let syh = $("#sampleAcceptForm #syh").val();
                            var bbzt=getRadioValue("bbzt");
                            let glxx = $("#sampleAcceptForm #glxx").val();
                            var cz= $('input[name="cz"]:checked').val();
                            var hs= $("#sampleAcceptForm #hs").val();
                            var jclx= $("#sampleAcceptForm #jclx").val();
                            if(hs==null||hs==''){
                                $.error("行数不得为空！");
                                return;
                            }
                            var ls= $("#sampleAcceptForm #ls").val();
                            if(ls==null||ls==''){
                                $.error("列数不得为空！");
                                return;
                            }
                            var kzbxh= $("#sampleAcceptForm #kzbxh").val();
                            var kzbmc= $("#sampleAcceptForm #kzbmc").val();
                            var kzbmc_sel= $("#sampleAcceptForm #kzbmc_sel").val();
                            if(cz=='1'){
                                if(kzbmc==null||kzbmc==''){
                                    $.error("扩增板名称不得为空！");
                                    return;
                                }
                            }else if(cz=='0'){
                                if(kzbmc_sel==null||kzbmc_sel==''){
                                    $.error("扩增板名称未选择！");
                                    return;
                                }                        }
                            $.ajax({
                                type: 'post',
                                url: bcurl,
                                data: {"nbbh": nbbh,"ybbh": ybbh,"glxx": glxx,"szz": szz,"syh": syh,"grsz_flg": grsz_flg,"bbzt": bbzt,"sfjs": sfjs,"sfsy":sfsy, "access_token": $("#ac_tk").val(),"jcdxcsdm":jcdxcsdm,"cz":cz,
                                    "hs":hs,"ls":ls,"kzbxh":kzbxh,"kzbmc":kzbmc,"kzbmc_sel":kzbmc_sel,"jcdw":jcdw,"jclx":jclx},
                                dataType: 'json',
                                success: function (data) {
                                    if (data.status == 'success') {
                                        preventResubmitForm(".modal-footer > button", false);
                                        if (data.print && data.sz_flg) {
                                            let ResponseSign = data.ResponseSign;
                                            let type = data.type;
                                            let RequestLocalCode = data.RequestLocalCode;
                                            let nbbh = data.nbbh;
                                            let ybbh = data.ybbh;
                                            let syh = data.syh;
                                            let sz_flg = data.sz_flg;
                                            print_nbbh(sz_flg,ResponseSign,type,RequestLocalCode,nbbh,ybbh,syh);
                                        }

                                        // $.success(data.message, function () {
                                        $("#sampleAcceptForm #ybbh").val("");
                                        t_map.rows = [];
                                        $("#sampleAcceptForm #hzxx_list").bootstrapTable('load',t_map);
                                        $("#sampleAcceptForm #syh").val("");
                                        $("#sampleAcceptForm #ybbh").focus();
                                        var kzbid=data.kzbid;
                                        //当扩增板操作为修改时，需判断是否装满，满的话自动切换
                                        if(data.cz=='0'){
                                            var hs=data.hs;
                                            var ls=data.ls;
                                            var xh= $("#sampleAcceptForm #kzbxh").val();
                                            var newxh=parseInt(xh)+1;
                                            var j=parseInt(hs)+1;
                                            var i=parseInt(ls)+1;
                                            //判断扩增板是否已满，满的话自动切换成新增
                                            if(newxh==96){
                                                var cz = document.getElementsByName('cz');
                                                for(var i=0;i<cz.length;i++){
                                                    if(cz[i].value == "1")
                                                    {
                                                        cz[i].checked=true; //选中
                                                        //country[i].checked=false; //不选中
                                                    }
                                                }
                                                kzbcz();
                                                return;
                                            }
                                            //判断行数是否超过8，超过的移到第二列第一个
                                            if(j>8){
                                                //判断执行了新增还是修改操作，新增的话序号加一，修改的话序号不变
                                                if(data.sqlcz=='insert'){
                                                    $("#sampleAcceptForm #ybbh"+hs+'-'+ls).css("background-color","#00FF00");
                                                    $("#sampleAcceptForm #ybbh"+hs+'-'+ls).css("border","1px solid #ccc");
                                                    $("#sampleAcceptForm #ybbh"+hs+'-'+ls).val(data.syh);
                                                    $("#sampleAcceptForm #hs").val("1");
                                                    $("#sampleAcceptForm #ls").val(i);
                                                    $("#sampleAcceptForm #ybbh1-"+i).css("border","3px solid red");;
                                                    $("#sampleAcceptForm #kzbxh").val(newxh);
                                                }else if(data.sqlcz=='update'){
                                                    $("#sampleAcceptForm #ybbh"+hs+'-'+ls).css("background-color","#00FF00");
                                                    $("#sampleAcceptForm #ybbh"+hs+'-'+ls).val(data.syh);
                                                    $("#sampleAcceptForm #kzbxh").val(xh);
                                                }
                                            }else{
                                                //判断执行了新增还是修改操作，新增的话序号加一，修改的话序号不变
                                                if(data.sqlcz=='insert'){
                                                    $("#sampleAcceptForm #ybbh"+hs+'-'+ls).css("background-color","#00FF00");
                                                    $("#sampleAcceptForm #ybbh"+hs+'-'+ls).val(data.syh);
                                                    $("#sampleAcceptForm #ybbh"+hs+'-'+ls).css("border","1px solid #ccc");
                                                    $("#sampleAcceptForm #hs").val(j);
                                                    $("#sampleAcceptForm #ybbh"+j+'-'+ls).css("border","3px solid red");
                                                    $("#sampleAcceptForm #kzbxh").val(newxh);
                                                }else if(data.sqlcz=='update'){
                                                    $("#sampleAcceptForm #ybbh"+hs+'-'+ls).css("background-color","#00FF00");
                                                    $("#sampleAcceptForm #ybbh"+hs+'-'+ls).val(data.syh);
                                                    $("#sampleAcceptForm #kzbxh").val(xh);
                                                }
                                            }
                                        //当扩增板操作为新增时，成功后自动切换到修改状态并默认选中新增的扩增板，便于继续增加编号
                                        }else if(data.cz=='1'){
                                            var cz = document.getElementsByName('cz');
                                            for(var i=0;i<cz.length;i++){
                                                if(cz[i].value == "0")
                                                {
                                                    cz[i].checked=true; //选中
                                                    //country[i].checked=false; //不选中
                                                }
                                            }
                                            $.ajax({
                                                url : "/detection/pagedataAllKzb",
                                                type : "post",
                                                data : {"access_token":$("#ac_tk").val()},
                                                dataType : "json",
                                                success:function(data){
                                                    if(data != null && data.length != 0){
                                                        var csHtml = "";
                                                        csHtml += "<option value=''>--请选择--</option>";
                                                        $.each(data,function(i){
                                                            if(data[i].kzbid ==kzbid){
                                                                csHtml += "<option value='" + data[i].kzbid + "' selected>" + data[i].kzbh + "</option>";
                                                            }else{
                                                                csHtml += "<option value='" + data[i].kzbid + "'>" + data[i].kzbh + "</option>";
                                                            }
                                                        });
                                                        $("#sampleAcceptForm #kzbmc_sel").empty();
                                                        $("#sampleAcceptForm #kzbmc_sel").append(csHtml);
                                                        $("#sampleAcceptForm #kzbmc_sel").trigger("chosen:updated");
                                                    }else{
                                                        var csHtml = "";
                                                        csHtml += "<option value=''>--请选择--</option>";
                                                        $("#sampleAcceptForm #kzbmc_sel").empty();
                                                        $("#sampleAcceptForm #kzbmc_sel").append(csHtml);
                                                        $("#sampleAcceptForm #kzbmc_sel").trigger("chosen:updated");
                                                    }
                                                }
                                            });
                                            $("#sampleAcceptForm #kzbmc_sel").show();
                                            $("#sampleAcceptForm #kzbmc_sel").removeAttr("disabled");
                                            $("#sampleAcceptForm #kzbmc").hide();
                                            $("#sampleAcceptForm #kzbmc").attr("disabled","disabled");
                                            $.ajax({
                                                url : "/detection/pagedataKzbmx",
                                                type : "post",
                                                data : {kzbid:data.kzbid,"access_token":$("#ac_tk").val()},
                                                dataType : "json",
                                                success:function(data){
                                                    if(data != null && data.length != 0){
                                                        for(var i=1;i<=12;i++){
                                                            for(var j=1;j<=8;j++){
                                                                $("#sampleAcceptForm #ybbh"+j+'-'+i).css("border","1px solid #ccc");
                                                                $("#sampleAcceptForm #ybbh"+j+'-'+i).val("");
                                                                $("#sampleAcceptForm #ybbh"+j+'-'+i).css("background-color","#eee");
                                                            }
                                                        }
                                                        for(var i=1;i<=12;i++){
                                                            for(var j=1;j<=8;j++){
                                                                for(var k=0;k<data.length;k++){
                                                                    if(i.toString()==data[k].ls&&j.toString()==data[k].hs){
                                                                        $("#sampleAcceptForm #ybbh"+j+'-'+i).css("background-color","#00FF00");
                                                                        $("#sampleAcceptForm #ybbh"+j+'-'+i).val(data[k].syh);
                                                                        $("#sampleAcceptForm #ybbh"+j+'-'+i).css("border","1px solid #ccc");
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        $("#sampleAcceptForm #kzbxh").val(data.length);
                                                    }
                                                    $("#sampleAcceptForm #hs").val("2");
                                                    $("#sampleAcceptForm #ls").val("1");
                                                    $("#sampleAcceptForm #ybbh2-1").css("border","3px solid red");;
                                                }
                                            });
                                        }
                                        // });
                                    } else if (data.status == "fail") {
                                        preventResubmitForm(".modal-footer > button", false);
                                        $.error(data.message, function () {
                                        });
                                    } else {
                                        preventResubmitForm(".modal-footer > button", false);
                                        $.alert("不好意思！出错了！");
                                    }
                                }
                            });
                        }
                    } else {
                        $.confirm("没有该标本编码！");
                        $("#sampleAcceptForm #ybbh").val("");
                        $("#sampleAcceptForm #nbbh").val("");
                        t_map.rows = [];
                        $("#sampleAcceptForm #hzxx_list").bootstrapTable('load',t_map);
                        $("#sampleAcceptForm #syh").val("");
                    }
                }
            });
        }else{
            $.confirm("请输入标本编号！");
        }
    }
}
function getRadioValue(args){
    var element = document.getElementsByName(args);
// 通过获取到name属性=hero的所dom对象
    for(var i=0; i<element.length; i ++){
        // 判断该单选框是否处于选中状态
        if(element[i].checked){
            return element[i].value;
        }
    }
}
$(document).ready(function () {
    var oTable = new Hzxx_TableInit();
    oTable.Init();
    //所有下拉框添加choose样式
    jQuery('#sampleAcceptForm .chosen-select').chosen({width: '100%'});
    printerIpChecked();
    // let bbzt = $("#sampleAcceptForm #bbzt1").val();
    // if (!bbzt){
    //     $("#sampleAcceptForm #bbzt input:first").attr("checked",true);
    // }
    // smqsm()
    $("#sampleAcceptForm #kzbxh").val("0");
    $("#sampleAcceptForm #ls").val("1");
    $("#sampleAcceptForm #hs").val("1");
    $("#sampleAcceptForm #ybbh1-1").css("border","3px solid red");;
    var cz=$('input[name="cz"]:checked').val();
    if(cz=='1'){
        $("#sampleAcceptForm #kzbmc_sel").attr("disabled", "disabled");
        $("#sampleAcceptForm #kzbmc_sel").hide();
    }else{
        $("#sampleAcceptForm #kzbmc").attr("disabled", "disabled");
        $("#sampleAcceptForm #kzbmc").hide();
    }

});
