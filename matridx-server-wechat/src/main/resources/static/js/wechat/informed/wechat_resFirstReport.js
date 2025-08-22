//初始化
var isButtonActive =  true;
var loadMoreSjysFlag = true;//加载更多送检医生标记
var loadMoreSjdwFlag = true;//加载更多送检单位标记
var pageFlag = "hzxxInfo";
var pageNumber = 0;
var pageSize = 15;
var loadingFlag = false;
//年龄单位列表
var nldwList = [{label: '岁',value: '岁'}, {label: '个月',value: '个月'}, {label: '天',value: '天'}, {label: '无',value: '无'}];
//科室
var ksxxlist;
var xzksList;
var dqksid;
//关注病原
var pathogenylist;
//快递类型
var sdlist;
var qtkdlx;
//检测单位
var zmmddlist;
var xzjcdwList
//送检区分
var divisionlist;
//检测项目
var detectlist;
var xzdetectList;
var dqdetectid = "";
//科研项目
var kylist;
var xzkylist;
//检测子项目
var detectsublist;
var jcxm;
//可选择检测单位
var unitlist;
var xzunitList;
var localids=[];
var navigationIndex = 0;
var yblxList;
var xzYblxList;
var qtyblxid;
//定义计时器(二维码预览使用)
var interval = null;
/**
 * 年龄单位改变事件
 */
function chooseNldw(){
    weui.picker(
        nldwList, {
            defaultValue: [$("#nldw").val()],
            onChange: function (result) {
                console.log(result);
            },
            onConfirm: function (result) {
                $("#nldw").val(result[0].value)
            },
            title: '请选择年龄单位'
        });
}

/**
 * 送检医生改变事件
 */
function changeSjys(){
    pageFlag = "sjysInfo";
    $("#hzxxInfo").hide();
    $("#sjysInfo").show();
    searchSjysList();
}

/**
 * 送检医生查询事件
 */
function searchSjysList(){
    loadMoreSjysFlag=true;
    $("#loading").show()
    $("#noInfo").hide();
    $("#loadComplete").hide();
    $("#sjysList").html("")
    loadMoreSjys()
}

/**
 * 送检医生懒加载事件
 */
function loadMoreSjys(){
    if(loadMoreSjysFlag){
        $.ajax({
            url :"/wechat/sjysxx/getSjysxxListInfo",
            type : "POST",
            dataType : "json",
            data : {
                "wxid" : $("#wxid").val(),
                "sjys" : $("#sjysCxnr").val()?$("#sjysCxnr").val():"",
            },
            success : function(data) {
                loadMoreSjysFlag = false
                if(data.sjysList.length>0){
                    sjysHtmlConcat(data.sjysList);
                }else {
                    $("#loading").hide()
                    $("#noInfo").show();
                    $("#loadComplete").hide();
                }
            },
            error : function(data) {
                $.alert(data.message);
            }
        })
    }
}

/**
 * 送检医生页面拼接
 * @param list
 */
function sjysHtmlConcat(list){
    var html = '';
    var firstSjdw = '';
    for (var j = 0; j < list.length; j++) {
        if (!list[j].ysid) {
            if(j==0){
                firstSjdw = list[j].sjdw;
            }
            html += '<div class="weui-panel weui-panel_access" style="border-radius: 15px;">' +
                '        <div class="weui-panel__hd" style="padding: 5px 16px;" onclick="openOrClose(\''+list[j].sjdw+'\')">' +
                '			<div class="weui-cell" style="padding: 0">' +
                '           	<div class="weui-cell__bd" style="padding-left: 0">'+(list[j].sjdwjc?list[j].sjdwjc:(list[j].dwmc?list[j].dwmc:""))+'</div>' +
                '           	<span class="weui-cell__ft weui-icon-doubleUp iconUp iconUp'+list[j].sjdw+'"></span>' +
                '           	<span class="weui-cell__ft weui-icon-doubleDown iconDown iconDown'+list[j].sjdw+'""></span>' +
                '          </div>' +
                '		</div>' +
                '       <div class="weui-panel__bd">'+
                '          <div class="weui-media-box weui-media-box_text" style="padding: 0;">';
            for (var i = j+1; i < list.length; i++) {
                if (list[i].ysid){
                    var jcdwmc = "";
                    for (var h = 0; h < zmmddlist.length; h++) {
                        if (zmmddlist[h].csid == list[i].jcdw) {
                            jcdwmc = zmmddlist[h].csmc
                            break
                        }
                    }
                    html += '<div class="weui-cell weui-cell_swiped sjysListInfo '+list[j].sjdw+'" style="padding: 0;border-top: 1px solid rgb(222 222 222);">' +
                        '            <div class="weui-cell__bd" style="transform: translate3d(0px, 0px, 0px);padding-left: 0" onclick="chooseSjys(\''+list[i].ysid+'\',\''+list[i].sjys+'\',\''+(list[i].ysdh?list[i].ysdh:"")+'\',\''+list[i].ks+'\',\''+list[i].qtks+'\',\''+list[i].sjdw+'\',\''+list[i].dwmc+'\',\''+list[i].sjdwbj+'\',\''+list[i].dbm+'\',\''+list[i].jcdw+'\')">' +
                        '              <div class="weui-cell">' +
                        '                <div class="weui-cell__bd" style="font-size: 15px;">'+list[i].sjys+'</div>' +
                        '                <div class="weui-cell__bd" style="font-size: 15px;">'+list[i].qtks+'</div>' +
                        '              </div>' +
                        '            </div>' +
                        '            <div class="weui-cell__ft">' +
                        '              <a class="weui-swiped-btn weui-swiped-btn_default close-swipeout" style="color: black" onclick="sjysCopy(\''+list[i].ysid+'\',\''+list[i].sjys+'\',\''+(list[i].ysdh?list[i].ysdh:"")+'\',\''+list[i].ks+'\',\''+list[i].qtks+'\',\''+list[i].sjdw+'\',\''+list[i].dwmc+'\',\''+list[i].jcdw+'\',\''+list[i].dbm+'\')">复制</a>' +
                        '              <a class="weui-swiped-btn weui-swiped-btn_warn delete-swipeout" onclick="delSjys(\''+list[i].ysid+'\')">删除</a>' +
                        '            </div>' +
                        '          </div>';
                }else {
                    break;
                }
            }
            html += '		</div>' +
                '		</div>' +
                '	</div>'
        }
    }
    $("#loading").hide()
    $("#noInfo").hide();
    $("#loadComplete").show();
    $("#sjysList").append(html)
    $('.weui-cell_swiped').swipeout()
    if (!$("#sjysCxnr").val()){
        $(".sjysListInfo").hide();
        $(".iconUp").hide();
        $(".iconDown").show();
        openOrClose(firstSjdw)
    }else {
        $(".iconUp").show();
        $(".iconDown").hide();
    }
}

function openOrClose(dwid){
    if($("."+dwid).is(":hidden")){
        $("."+dwid).show()
        $(".iconDown"+dwid).hide()
        $(".iconUp"+dwid).show()
    }else {
        $("."+dwid).hide()
        $(".iconDown"+dwid).show()
        $(".iconUp"+dwid).hide()
    }

}
/**
 * 送检医生删除事件
 * @param ysid
 */
function delSjys(ysid){
    $.confirm({
        title: '警告',
        text: '<span style="color: red;">确认删除？</span>',
        onOK: function () {
            $.ajax({
                url:'/wechat/sjysxx/deleteSjysxx',
                type:'post',
                dataType:'json',
                data:{"ysid":ysid},
                success:function(data){
                    if(data.state=="true"){
                        $.alert({
                            title: '提示',
                            text: '删除成功！',
                            onOK: function () {
                                //点击确认
                                searchSjysList();
                            }
                        });
                    }else if(data.state=="false"){
                        $.alert("删除失败！");
                    }
                }
            })
        },
        onCancel: function () {
            $.closeModal();
        }
    })
}

/**
 * 送检医生新增方法
 */
function sjysAdd(){
    pageFlag = "sjysAdd";
    $("#hzxxInfo").hide();
    $("#sjysInfo").hide();
    $("#sjysAdd").show();
    for (var i = 0; i < ksxxlist.length; i++) {
        if(ksxxlist[i].dwid == $("#sjysadd_ks").val()){
            if (ksxxlist[i].kzcs == '1'){
                $("#qtksInput").show()
            } else {
                $("#qtksInput").hide()
                $("#qtksInput #qtks").val("")
            }
        }
    }
    refreshSjysDB();
}

/**
 * 送检医生复制
 * @param ysid
 * @param ysmc
 * @param ysdh
 * @param ksid
 * @param ksmc
 * @param sjdwid
 * @param sjdwmc
 * @param jcdw
 * @param dbm
 */
function sjysCopy(ysid,ysmc,ysdh,ksid,ksmc,sjdwid,sjdwmc,jcdw,dbm){
    pageFlag = "sjysAdd";
    $("#hzxxInfo").hide();
    $("#sjysInfo").hide();
    $("#sjysAdd").show();
    $("#sjysAdd #sjysadd_ysxm").val(ysmc)
    $("#sjysAdd #sjdwadd_sjdwid").val(sjdwid)
    $("#sjysAdd #sjdwadd_sjdwmc").val(sjdwmc)
    $("#sjysAdd #sjysadd_ks").val(ksid)
    $("#sjysAdd #sjysadd_qtks").val(ksmc)
    $("#sjysAdd #sjysadd_jcdwid").val(jcdw)
    $("#sjysAdd #sjysadd_ysdh").val(ysdh)
    $("#sjysAdd #sjysadd_hzhb").val(dbm)
    for (var i = 0; i < zmmddlist.length; i++) {
        if (jcdw == zmmddlist[i].csid){
            $("#sjysAdd #sjysadd_jcdwmc").val(zmmddlist[i].csmc)
            break;
        }
    }
    for (var i = 0; i < ksxxlist.length; i++) {
        if(ksxxlist[i].dwid == $("#sjysadd_ks").val()){
            $("#ajaxForm #ksmc").val(ksxxlist[i].dwmc)
            if (ksxxlist[i].kzcs == '1'){
                $("#sjysadd_qtksInput").show()
                $("#sjysAdd #sjysadd_ksmc").val(ksxxlist[i].dwmc)
                $("#sjysAdd #sjysadd_qtks").val(ksmc);
                break;
            } else {
                $("#qtksInput").hide()
                $("#ksmc").val(ksmc);
                $("#qtksInput #qtks").val("")
            }
        }
    }
    refreshSjysDB();
}

/**
 * 送检医生保存
 */
function sjysSave(){
    if(!$("#sjysadd_ysxm").val() ){
        $.toptip('请填写主治医师!', 'error');
    }else if(!$("#sjdwadd_sjdwid").val()){
        $.toptip('请选择医院名称!', 'error');
    }else if(!$("#sjysadd_ks").val()){
        $.toptip("请选择科室!", 'error');
    }else if(!$("#sjysadd_jcdwid").val()){
        $.toptip("请选择检测单位!", 'error');
    }else if(!$("#sjysadd_hzhb").val()){
        $.toptip("请填写负责人!", 'error');
    }else{
        $("#sjysSave").attr("disabled","true");
        $.ajax({
            url:'/wechat/sjysxx/saveSjysxx',
            type:'post',
            dataType:'json',
            data:{
                "wxid" : $("#wxid").val(),
                "sjdw" : $("#sjdwadd_sjdwid").val(),
                "dwmc" : $("#sjdwadd_sjdwmc").val(),
                "ks" : $("#sjysadd_ks").val(),
                "sjys" : $("#sjysadd_ysxm").val(),
                "ysdh": $("#sjysadd_ysdh").val(),
                "dbm": $("#sjysadd_hzhb").val(),
                "qtks": $("#sjysadd_qtks").val(),
                "jcdw": $("#sjysadd_jcdwid").val(),
            },
            success:function(data){
                $("#sjysSave").removeAttr("disabled");
                if(data.status=="success"){
                    $.toptip(data.message, 'success');
                    backSjysList();
                }else if(data.status=="fail"){
                    $.toptip(data.message, 'error');
                }
            },
            failure : function(data) {
                $("#sjysSave").removeAttr("disabled");
                $.alert(JSON.stringify(data));
            }
        })
    }
}

/**
 * 送检医生新增信息情况
 */
function clearSjyxaddInfo(){
    $("#sjysadd_ysxm").val("");
    $("#sjdwadd_sjdwmc").val("");
    $("#sjdwadd_sjdwid").val("");
    $("#sjysadd_ksmc").val("");
    $("#sjysadd_ks").val("");
    $("#sjysadd_qtks").val("");
    $("#sjysadd_jcdwmc").val("");
    $("#sjysadd_jcdwid").val("");
    $("#sjysadd_ysdh").val("");
    $("#sjysadd_hzhb").val("");
}

/**
 * 送检医生取消保存
 */
function backSjysList(){
    $("#hzxxInfo").hide();
    $("#sjysInfo").show();
    $("#sjysAdd").hide();
    $("#sjysCxnr").val("");
    searchSjysList()
    clearSjyxaddInfo()
}

/**
 * 送检医生选择事件
 * @param ysid 医生id
 * @param ysmc 医生名称
 * @param ysdh 医生电话
 * @param ksid 科室id
 * @param ksmc 科室名称
 * @param sjdwid 送检单位id
 * @param sjdwmc 送检单位名称
 * @param sjdwbj 送检单位标记（1为其他）
 * @param dbm 代表名，送检伙伴
 */
function chooseSjys(ysid,ysmc,ysdh,ksid,ksmc,sjdwid,sjdwmc,sjdwbj,dbm,jcdw){
    $("#sjysCxnr").val("")
    $("#sjys").val(ysid)
    $("#sjysmc").val(ysmc)
    $("#sjdwid").val(sjdwid)
    $("#sjdwmc").val(sjdwmc)
    $("#yymc").val(sjdwmc)
    for (var i = 0; i < ksxxlist.length; i++) {
        if(ksxxlist[i].dwid == ksid){
            $("#ajaxForm #ksmc").val(ksxxlist[i].dwmc)
            if (ksxxlist[i].kzcs == '1'){
                $("#qtksInput").show()
                $("#qtks").val(ksmc);
                break;
            } else {
                $("#qtksInput").hide()
                $("#qtksInput #qtks").val("")
            }
        }
    }
    if(sjdwbj=='1'){
        $("#qtdwInput").show();
        $("#sjdwbj").val(sjdwbj);
    }else {
        $("#qtdwInput").hide();
        $("#sjdwbj").val("");
    }
    $("#ksid").val(ksid)
    $("#ysdh").val(ysdh?ysdh:"")
    $("#db").val(dbm)
    $("#hzxxInfo").show();
    $("#sjysInfo").hide();
    $("#sjysAdd").hide();
    $("#sjdwInfo").hide();
    refreshUnit(jcdw);
}

/**
 * 送检单位选择事件
 */
function changeSjdw(){
    pageFlag = "sjdwInfo";
    $("#hzxxInfo").hide();
    $("#sjysInfo").hide();
    $("#sjysAdd").hide();
    $("#sjdwInfo").show();
    $("#sjdwCxnr").val("")
    searchSjdwList()
}

/**
 * 选择送检单位页面返回事件
 */
function sjdwBack(){
    pageFlag = "sjysAdd";
    $("#hzxxInfo").hide();
    $("#sjysInfo").hide();
    $("#sjysAdd").show();
    $("#sjdwInfo").hide();
}

/**
 * 送检单位查询事件
 */
function searchSjdwList(){
    loadMoreSjdwFlag=true;
    $("#sjdw_loading").show()
    $("#sjdw_noInfo").hide();
    $("#sjdw_loadComplete").hide();
    pageNumber = 0;
    $("#sjdwList").html("")
    loadMoreSjdw()
}

/**
 * 送检单位懒加载事件
 */
function loadMoreSjdw(){
    if(loadMoreSjdwFlag){
        pageNumber = pageNumber+1;
        $.ajax({
            url :"/wechat/hospital/pagedataListHospital",
            type : "POST",
            dataType : "json",
            data : {
                "searchParam" : $("#sjdwCxnr").val()?$("#sjdwCxnr").val():"",
                "sortName" : "yyxx.lrsj",
                "sortOrder" : "desc",
                "sortLastName" : "yyxx.lrsj",
                "sortLastOrder" : "asc",
                "pageNumber": pageNumber,
                "pageSize": pageSize,
                "dwFlag":"1"
            },
            success : function(data) {
                if(data.rows.length>0){
                    if (data.rows.length<pageSize){
                        loadMoreSjdwFlag = false
                    }
                    sjdwHtmlConcat(data.rows);
                }else {
                    loadMoreSjdwFlag = false
                    $("#sjdw_loading").hide()
                    $("#sjdw_noInfo").show();
                    $("#sjdw_loadComplete").hide();
                }
                loadingFlag = false;
            },
            error : function(data) {
                console.log(data)
                $.alert(data.message);
                loadingFlag = false;
            }
        })
    }
}

/**
 * 送检单位html拼接
 * @param list
 */
function sjdwHtmlConcat(list){
    var html = 	'';
    for (var i = 0; i < list.length; i++) {
        html += '  <a class="weui-cell" style="padding-bottom: 0;padding-top: 0;color: black" onclick="chooseSjdw(\''+list[i].dwid+'\',\''+list[i].dwmc+'\',\''+list[i].cskz1+'\')">' +
            '    <div class="weui-cell__bd">' +
            '      <p style="margin: 10px 0;">'+list[i].dwmc+'</p>' +
            '    </div>' +
            '  </a>'

    }
    $("#sjdw_loading").hide()
    $("#sjdw_noInfo").hide();
    $("#sjdw_loadComplete").show();
    $("#sjdwList").append(html)
    // $('.weui-cell_swiped').swipeout()
}

/**
 * 送检单位选择事件
 * @param dwid
 */
function chooseSjdw(dwid,dwmc,cskz1){
    sjdwBack();
    $("#sjdwadd_sjdwid").val(dwid);
    $("#sjdwadd_sjdwmc").val(dwmc);
}

/**
 * 判断是否获取到微信id
 * @returns {boolean}
 */
function getWxOrNot (){
    var wxid = $("#wxid").val();
    if (wxid==null || wxid ==''){
        $.alert("没有获取到您的微信信息，请重新至公众号打开！")
        return false;
    }
    return true;
}

/**
 * 获取基础数据
 */
function getBasicData(){
    $.ajax({
        url:'/wechat/getInspBasicData',
        type:'post',
        dataType:'json',
        data:{
            "dbm" : $("#dbm").val()
        },
        success:function(data){
            ksxxlist = data.ksxxlist;
            pathogenylist = data.pathogenylist;
            sdlist = data.sdlist;
            zmmddlist = data.zmmddlist;
            divisionlist = data.divisionlist;
            detectlist = data.detectlist;
            kylist = data.kylist;
            detectsublist = data.detectsublist;
            unitlist = data.unitlist;
            dealBasicData()
        },
        failure : function(data) {
            $.alert(JSON.stringify(data));
        }
    })
}

/**
 * 处理基础数据
 */
function dealBasicData(){
    //科室、初始化其他科室
    xzksList = [];
    for (var i = 0; i < ksxxlist.length; i++) {
        var lsks = {label:ksxxlist[i].dwmc,value:ksxxlist[i].dwid};
        xzksList.push(lsks)
    }
    for (var i = 0; i < ksxxlist.length; i++) {
        if(ksxxlist[i].dwid == $("#ksid").val()){
            $("#ajaxForm #ksmc").val(ksxxlist[i].dwmc)
            if (ksxxlist[i].kzcs == '1'){
                $("#qtksInput").show()
            } else {
                $("#qtksInput").hide()
                $("#qtksInput #qtks").val("")
            }
        }
    }
    //检测项目
    xzdetectList = [];
    var dqdetectcskz = "";
    var jcxmdms = $("#jcxmdms").val();
    if (jcxmdms!='' && jcxmdms != null && jcxmdms != undefined){
        var jcxmcsdms = jcxmdms.split(',');
        for (let jcxmcsdm of jcxmcsdms) {
            for (var i = 0; i < detectlist.length; i++) {
                if (detectlist[i].csdm==jcxmcsdm) {
                    var lsjcxm = {label: detectlist[i].csmc, value: detectlist[i].csid,jcxmdm:detectlist[i].csdm};
                    xzdetectList.push(lsjcxm)
                }
                if($("#jcxmids").val().indexOf(detectlist[i].csid)!=-1){
                    dqdetectid += ","+detectlist[i].csid
                    dqdetectcskz += ","+detectlist[i].cskz1
                }
            }
        }
        //初始化检测项目
        if (!$("#jcxmmc").val()) {
            $("#jcxmmc").val(xzdetectList[0].label);
            $("#jcxmids").val(xzdetectList[0].value);
            $("#jcxmdm").val(xzdetectList[0].jcxmdm);
        }
    }else {
        for (var i = 0; i < detectlist.length; i++) {
            var lsjcxm = {label:detectlist[i].csmc,value:detectlist[i].csid};
            xzdetectList.push(lsjcxm)
            if($("#jcxmids").val().indexOf(detectlist[i].csid)!=-1){
                dqdetectid += ","+detectlist[i].csid
                dqdetectcskz += ","+detectlist[i].cskz1
            }
        }
    }
    dqdetectid = dqdetectid.substring(1);
    dqdetectcskz = dqdetectcskz.substring(1);
    // 由于Res项目，将判断改为如下 if(dqdetectcskz.indexOf("F")!=-1 || dqdetectcskz.indexOf("Z")!=-1){
    if(dqdetectcskz.indexOf("Z")!=-1){
        $("#jczxmDiv").show()
    }else {
        $("#jczxmDiv").hide()
    }
    //检测单位
    xzunitList = [];
    if (unitlist.length > 0){
        for (var i = 0; i < unitlist.length; i++) {
            var lsuniy = {label:unitlist[i].csmc,value:unitlist[i].csid};
            xzunitList.push(lsuniy)
            if($("#jcdwid").val() && $("#jcdwid").val() == unitlist[i].csid){
                $("#jcdwmc").val(unitlist[i].csmc)
            }
        }
    }
    if (unitlist.length == 1) {
        $("#jcdwmc").val(unitlist[0].csmc);
        $("#jcdwid").val(unitlist[0].csid);
    }

    //检测单位
    xzjcdwList = [];
    if (zmmddlist.length > 0){
        for (var i = 0; i < zmmddlist.length; i++) {
            var lsjcdw = {label:zmmddlist[i].csmc,value:zmmddlist[i].csid};
            xzjcdwList.push(lsjcdw)
        }
    }

    //快递类型
    for (i = 0; i < sdlist.length; i++) {
        if (sdlist[i].cskz2 == "1") {
            qtkdlx = sdlist[i].csid;
            break
        }
    }
    initKdlx()

    //送检单位
    var dqzmmdd;
    if (zmmddlist.length > 0) {
        for (var j = 0; j < zmmddlist.length; j++) {
            if ($("#sjdw").val() == zmmddlist[j].dwid){
                dqzmmdd = zmmddlist[j]
            }
        }
    }
    if($("#sjdwbj").val()=="1"){
        $("#qtdwInput").show()
    }else {
        $("#qtdwInput").hide()
    }
    if ($("#ybbh").val()==null || $("#ybbh").val()=='' ){
        initSjqf()
    }else {
        var kyxmid = $("#ajaxForm #kyxm").val();
        var sjqf = $("#ajaxForm input:radio[name='sjqf']:checked").val();
        xzkylist = [];
        if(sjqf){
            for (var i = 0; i < kylist.length; i++) {
                if(kylist[i].fcsid == sjqf){
                    var lskyxm = {label:kylist[i].csmc,value:kylist[i].csid};
                    xzkylist.push(lskyxm)
                    if(kyxmid == kylist[i].csid){
                        $("#ajaxForm #kyxmmc").val(kylist[i].csmc)
                    }
                }
            }
        }
        if(xzkylist.length>0){
            $("#kyxmDiv").show();
        }else{
            $("#ajaxForm #kyxm").val("");
            $("#ajaxForm #kyxmmc").val("");
            $("#kyxmDiv").hide();
        }
    }
}

/**
 * 科室改变事件
 * @param flag
 */
function chooseKs(flag){
    weui.picker(
        xzksList, {
            onChange: function (result) {
                console.log(result);
            },
            onConfirm: function (result) {
                $("#"+(flag?"ksmc":"sjysadd_ksmc")).val(result[0].label);
                $("#"+(flag?"ksid":"sjysadd_ks")).val(result[0].value);
                for (var i = 0; i < ksxxlist.length; i++) {
                    if(ksxxlist[i].dwid == result[0].value){
                        if (ksxxlist[i].kzcs == '1'){
                            $("#"+(flag?"qtksInput":"sjysadd_qtksInput")).show();
                                $("#"+(flag?"qtks":"sjysadd_qtks")).val(result[0].label)
                        } else {
                            $("#"+(flag?"qtksInput":"sjysadd_qtksInput")).hide()
                            $("#"+(flag?"qtks":"sjysadd_qtks")).val("")
                        }
                    }
                }
            },
            title: '请选择科室'
        });
}

/**
 * 检测单位改变事件
 * @param flag
 */
function chooseJcdw(flag){
    setTimeout(function(){
        weui.picker(
            flag?xzunitList:xzjcdwList, {
                defaultValue: [(flag?($("#jcdwid").val()?$("#jcdwid").val():xzunitList[0].value):($("#sjysadd_jcdwid").val()?$("#sjysadd_jcdwid").val():xzjcdwList[0].value))],
                onChange: function (result) {
                    console.log(result);
                },
                onConfirm: function (result) {
                    $("#"+(flag?"jcdwmc":"sjysadd_jcdwmc")).val(result[0].label);
                    $("#"+(flag?"jcdwid":"sjysadd_jcdwid")).val(result[0].value);
                },
                title: '请选择检测单位'
            });
    }, 200);
}

/**
 * 检测项目改变事件
 */
function chooseJcxm(){
    weui.picker(
        xzdetectList, {
            defaultValue: [$("#jcxm").val()?$("#jcxm").val():xzdetectList[0].value],
            onChange: function (result) {
                console.log(result);
            },
            onConfirm: function (result) {
                $("#jcxmmc").val(result[0].label);
                $("#jcxmids").val(result[0].value);
                var dqdetectcskz = "";
                dqdetectid = "";
                for (var i = 0; i < detectlist.length; i++) {
                    if($("#jcxmids").val().indexOf(detectlist[i].csid)!=-1){
                        dqdetectid += ","+detectlist[i].csid;
                        dqdetectcskz += ","+detectlist[i].cskz1;
                    }
                }
                dqdetectid = dqdetectid.substring(1);
            },
            title: '请选择检测项目'
        });
}

/**
 * 初始化送检区分
 */
function initSjqf(){
    var db = $("#ajaxForm #db").val();
    if (db.length>=4){
        db = db.substring(db.length-2,db.length);
    }
    var length = $("#ajaxForm #sjqfGroup").length;
    if (length>0){
        for (var i = 0; i < length; i++) {
            var id = $("#ajaxForm #sjqfGroup input")[i].id;
            if ($("#ajaxForm #sjqfGroup")[i].innerText.replace(" ","")=="特检"){
                $("#ajaxForm #sjqfGroup").removeAttr("checked")
                $("#ajaxForm #sjqfGroup #"+id).prop("checked", true);
            }
            if ($("#ajaxForm #sjqfGroup")[i].innerText.replace(" ","")==db){
                $("#ajaxForm #sjqfGroup").removeAttr("checked")
                $("#ajaxForm #sjqfGroup #"+id).prop("checked", true);
                //根据送检区分初始化科研项目
                initKyxm();
                return
            }
        }
    }
    //根据送检区分初始化科研项目
    initKyxm();
}

/**
 * 初始化快递类型
 */
function initKdlx(){
    if($('input:radio[name="kdlx"]:checked').attr("data-cskz2") == "1"){
        $("#ajaxForm #kdh").attr("placeholder","如无请填无");
        $("#ajaxForm #kdhname").text("快递号")
        $("#ajaxForm #kdhspan").text(" ");
        $("#ajaxForm #kdh").removeAttr("validate");
        $("#ajaxForm #kdh").attr("validate","{stringMaxLength:32}");
    }else{
        $("#ajaxForm #kdhspan").text("*");
        $("#ajaxForm #kdh").removeAttr("validate");
        $("#ajaxForm #kdh").removeAttr("placeholder");
        if ($('input:radio[name="kdlx"]:checked').attr("data-csdm") == "QYY"){
            $("#ajaxForm #kdhname").text("取样员")
            $("#ajaxForm #kdh").attr("validate","{stringMaxLength:32}");
            $("#ajaxForm #kdh").attr("placeholder","请填写取样员姓名");
        }else {
            $("#ajaxForm #kdhname").text("快递号")
            $("#ajaxForm #kdh").attr("placeholder","请填写快递号");
            $("#ajaxForm #kdh").attr("validate","{required:true, stringMaxLength:32}");
        }
    }
}

/**
 * 页面初始化
 */
function initPage(){
    //初始化基础数据
    getBasicData()
}

//事件绑定
function btnBind() {
    //快递类型改变事件
    $("#ajaxForm input[type=radio][name=kdlx]").change(function(e) {
        initKdlx()
    });

    //暂存功能
    $('#ajaxForm #info_save').unbind("click").click(function(){
        if (!isButtonActive){
            return false;
        }
        buttonDisabled(true,false,"",true);
        if(!getWxOrNot()){
            return buttonDisabled(false,false,"",false);
        }
        if (!checkYbbh()){
            $("#ajaxForm .preBtn").attr("disabled", false);
            return buttonDisabled(false,false,"",false);
        }
        if(!$("#ajaxForm #hzxm").val()){
            return buttonDisabled(false,true,"请填写患者姓名!",false);
        }else {
            $.alert("未完善数据请至“录入清单”中补充");
            buttonDisabled(false,false,"",false);
        }
        $("#ajaxForm #db").val($("#ajaxForm #db").val().replace(/\s+/g,""));
        $("#ajaxForm #db").val(DBC2SBC($("#ajaxForm #db").val()));
        $("#ajaxForm #sjys").val($("#ajaxForm #sjys").val().replace(/\s+/g,""));
        $("#ajaxForm #ybbh").val($("#ajaxForm #ybbh").val().replace(/\s+/g,""));
        $("#ajaxForm #status").val("true");
        //性别
        var lsxb = ($("#ajaxForm input:radio[name='xb']:checked").val()=="" || $("#ajaxForm input:radio[name='xb']:checked").val()==null)?"":$("#ajaxForm input:radio[name='xb']:checked").val();
        if (!lsxb){
            $("#ajaxForm #status").val("false");
        }
        //年龄+年龄单位
        var lsnl = ($("#ajaxForm #nl").val()?$("#ajaxForm #nl").val():"");
        if (!lsnl){
            $("#ajaxForm #status").val("false");
        }
        var lsnldw = ($("#ajaxForm #nldw").val()?$("#ajaxForm #nldw").val():"岁");
        if (!lsnldw){
            $("#ajaxForm #status").val("false");
        }
        //联系电话
        var lslxdh = ($("#ajaxForm #dh").val()?$("#ajaxForm #dh").val():"");
        //医生电话
        var lsysdh = ($("#ajaxForm #ysdh").val()?$("#ajaxForm #ysdh").val():"");
        //送检单位
        var lssjdw = ($("#ajaxForm #sjdwid").val()?$("#ajaxForm #sjdwid").val():"");
        if (!lssjdw){
            $("#ajaxForm #status").val("false");
        }
        //送检单位名称
        var lssjdwmc = ($("#ajaxForm #sjdwmc").val()?$("#ajaxForm #sjdwmc").val():"");
        if (!lssjdwmc){
            $("#ajaxForm #status").val("false");
        }
        //科室
        var lsks = ($("#ajaxForm #ksid").val()?$("#ajaxForm #ksid").val():"");
        if (!lsks){
            $("#ajaxForm #status").val("false");
        }
        //其他科室
        var lsqtks = (!$("#ajaxForm #qtks").is(":hidden")?($("#qtksInput #qtks").val()?$("#qtksInput #qtks").val():""):"");
        if (!lsqtks){
            $("#ajaxForm #status").val("false");
        }
        //检测单位
        var lsjcdw = ($("#ajaxForm #jcdwid").val()?$("#ajaxForm #jcdwid").val():"");
        if (!lsjcdw){
            $("#ajaxForm #status").val("false");
        }
        //合作伙伴
        var lsdb = ($("#ajaxForm #db").val()?$("#ajaxForm #db").val():"");
        if (!lsdb){
            $("#ajaxForm #status").val("false");
        }
        //快递类型
        var lskdlx = $("#ajaxForm input:radio[name='kdlx']:checked").val()?$("#ajaxForm input:radio[name='kdlx']:checked").val():"";
        if (!lskdlx){
            $("#ajaxForm #status").val("false");
        }
        //快递号
        var lskdh = $("#ajaxForm #kdh").val()?$("#ajaxForm #kdh").val():"";
        if(lskdh && lskdh.length>32){
            return buttonDisabled(false,true,"请填写正确的快递号!",false);
        }
        //检测项目
        var lsjcxmids = $("#ajaxForm #jcxmids").val().replace("[","").replace("]","");
        if (!lsjcxmids){
            $("#ajaxForm #status").val("false");
        }
        var lssjqf = $("#ajaxForm input:radio[name='sjqf']:checked").val()?$("#ajaxForm input:radio[name='sjqf']:checked").val():"";
        if (!lssjqf){
            $("#ajaxForm #status").val("false");
        }
        //科研项目
        var lskyxm =  (!$("#ajaxForm #kyxmDiv").is(":hidden"))?($("#ajaxForm #kyxm").val()?$("#ajaxForm #kyxm").val():""):"";
        //科研项目
        if(!$("#ajaxForm #kyxm").val() && !$("#ajaxForm #kyxmDiv").is(":hidden")){
            $("#ajaxForm #status").val("false");

        }
        var bz= $("#ajaxForm #bz").val()?$("#ajaxForm #bz").val():"";
        var cyrq=$("#ajaxForm #cyrq").val()?$("#ajaxForm #cyrq").val():"";
        if (!cyrq){
            $("#ajaxForm #status").val("false");
        }
        var jcxmmc=$("#ajaxForm #jcxmmc").val()?$("#ajaxForm #jcxmmc").val():"";
        if (!jcxmmc){
            $("#ajaxForm #status").val("false");
        }
        var yblxmc=$("#ajaxForm #yblxmc").val()?$("#ajaxForm #yblxmc").val():"";
        if (!yblxmc){
            $("#ajaxForm #status").val("false");
        }
        var yblx=$("#ajaxForm #yblx").val()?$("#ajaxForm #yblx").val():"";
        if (!yblx){
            $("#ajaxForm #status").val("false");
        }
        var sjrq=$("#ajaxForm #sjrq").val()?$("#ajaxForm #sjrq").val():"";
        var ybtj=$("#ajaxForm #ybtj").val()?$("#ajaxForm #ybtj").val():"";
        if (!ybtj){
            $("#ajaxForm #status").val("false");
        }
        var json=[];
        var sz={"jcxmid":lsjcxmids,"jczxmid":"","xmglid": $("#ajaxForm #xmglids").val().replace("[","").replace("]","")};
        json.push(sz);
        dealSfwsFlag();
        $.ajax({
            type:'post',
            url:"/wechat/miniprogram/saveInspectFirst",
            cache: false,
            data: {
                "wxid":$("#wxid").val(),
                "sjid":($("#sjid").val()?$("#sjid").val():""),
                "ywlx":$("#ywlx").val(),
                "status":$("#status").val(),
                "fjids":$("#fjids").val()?$("#fjids").val():"",
                "ybbh":$("#ajaxForm #ybbh").val(),
                "hzxm":$("#ajaxForm #hzxm").val(),
                "xb":lsxb,
                "nl":lsnl,
                "nldw":lsnldw,
                "dh":lslxdh,
                "sjys":$("#sjysmc").val(),
                "ysdh":lsysdh,
                "sjdw":lssjdw,
                "yymc":lssjdwmc,
                "sjdwmc":lssjdwmc,
                "jcxmids":lsjcxmids,
                "jcxm":JSON.stringify(json),
                "ks":lsks,
                "qtks":lsqtks,
                "jcdw":lsjcdw,
                "kdlx":lskdlx,
                "kdh":lskdh,
                "db":lsdb,
                "yblx":yblx,
                "yblxmc":yblxmc,
                "ybtj":ybtj,
                "cyrq":cyrq,
                "sjrq":sjrq,
                "bz":bz,
                "jcxmmc":jcxmmc,
                "sjqf":lssjqf,
                "kyxm":lskyxm,
                "flag":$("#ajaxForm #flag").val(),
                "sfws":$("#ajaxForm #sfws").val(),//是否完善标记
                "lczz":$("#ajaxForm #lczz").val()?$("#ajaxForm #lczz").val():'--',
                "qqzd":$("#ajaxForm #qqzd").val()?$("#ajaxForm #qqzd").val():'--',
            },
            dataType:'json',
            success:function(result){
                buttonDisabled(false,false,"",true);
                if(result.status == "success"){
                    if(result.sjxxDto){
                        $("#ajaxForm #sjid").val(result.sjxxDto.sjid);
                    }
                    refreshFile();
                    refreshFj(result.fjcfbDtos);
                }else{
                    $.closeModal();
                    $.alert(result.message);
                }
            },
            error: function (data) {
                buttonDisabled(false,true,data,false);
            }

        })
        return true;
    });

    //初始化微信信息
    $.ajax({
        url: '/wechat/getJsApiInfo',
        type: 'post',
        data: {
            "url":location.href.split('#')[0],
            "wbcxdm":$("#wbcxdm").val()
        },
        dataType: 'json',
        success: function(result) {
            //注册信息
            wx.config({
                debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                appId: result.appid, // 必填，公众号的唯一标识
                timestamp: result.timestamp, // 必填，生成签名的时间戳
                nonceStr: result.noncestr, // 必填，生成签名的随机串
                signature: result.sign, // 必填，签名，见附录1
                jsApiList: ['checkJsApi','scanQRCode','startRecord','stopRecord','onVoiceRecordEnd','playVoice','pauseVoice','stopVoice','onVoicePlayEnd','uploadVoice','downloadVoice','translateVoice','chooseImage','previewImage','uploadImage','downloadImage']
                // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
            });
            wx.error(function(res) {
                console.log(res);
            });
            //config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作
            wx.ready(function() {
                wx.checkJsApi({
                    jsApiList: ['scanQRCode','startRecord','stopRecord','onVoiceRecordEnd','playVoice','pauseVoice','stopVoice','onVoicePlayEnd','uploadVoice','downloadVoice','translateVoice','chooseImage','previewImage','uploadImage','downloadImage'],
                    success: function(res) {
                        /**
                         * 样本编号扫码事件
                         */
                        $("#getQRCode").unbind("click").click(function(){
                            //扫码事件
                            wx.scanQRCode({
                                needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果
                                scanType: ["qrCode", "barCode"],
                                success: function(res) {
                                    // 当needResult 为 1 时，扫码返回的结果
                                    var result = res.resultStr;
                                    // http://service.matridx.com/wechat/getUserReport?ybbh=1112
                                    var s_res = result.split('ybbh=')

                                    $("#ajaxForm #ybbh").val(s_res[s_res.length-1]);

                                    if(s_res[s_res.length-1]!=null && s_res[s_res.length-1]!= "")
                                        getInspectionInfo();
                                },
                                fail: function(res) {
                                    alert(JSON.stringify(res));
                                }
                            });
                        });
                        /**
                         * 快递单号扫码时间
                         */
                        $("#getKddhQRCode").unbind("click").click(function(){
                            //扫码事件
                            wx.scanQRCode({
                                needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果
                                scanType: ["qrCode", "barCode"],
                                success: function(res) {
                                    // 当needResult 为 1 时，扫码返回的结果
                                    var result = res.resultStr;
                                    // https://ucmp.sf-express.com/service/weixin/activity/wx_b2sf_order?p1=SF1087793119325
                                    var s_res = result.split('p1=');
                                    $("#ajaxForm #kdh").val(s_res[s_res.length-1]);
                                },
                                fail: function(res) {
                                    alert(JSON.stringify(res));
                                }
                            });
                        });

                        $("#uploader").unbind("click").click(function(e){
                            try{
                                wx.chooseImage({
                                    count: 4, // 默认9
                                    sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
                                    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                                    success: function (res) {
                                        var localIds = res.localIds;
                                        upload(localIds, 0);
                                    }
                                });
                            } catch (ex) {
                                $("#chrom").show();
                                $("#phone").hide();
                            }
                        });
                    }
                });
            });
        }
    });
}

function upload(localIds, i){
    var ywlx = $("#ajaxForm #ywlx").val();
    wx.uploadImage({
        localId:localIds[i], //需要上传的图片的本地ID,由chooseImage接口获得
        isShowProgressTips:1,   //默认为1，显示进度提示
        success:function (res) {
            var serverId = res.serverId; //返回图片的服务器端ID
            $.ajax({
                url: '/wechat/file/saveTempFile',
                type: 'post',
                dataType: 'json',
                data : {"mediaid" : serverId,"localid" : localIds[i], "ywlx":ywlx},
                success: function(result) {
                    i = i + 1;
                    var localid = result.fjcfbDto.localid;
                    var fjid = result.fjcfbDto.fjid;
                    displayUpInfo(fjid);
                    var u = navigator.userAgent, app = navigator.appVersion;
                    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //安卓
                    var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
                    if (isAndroid) {
                        localids.push(localid);
                        var img =  '<div id="'+fjid+'" style="padding:0;width: 30%;height: 70px;margin: 0 5px;">'
                            +'<div style="padding:0;border:1px solid grey;">'
                            +'<img onclick="viewfile(event)" style="height: 50px;width: 100%;" src="'+localid+'"/>'
                            +'<img onclick=\'delfile(event,"'+fjid+'");\' data-wjlj="'+localid+'" style="height: 20px;width: 100%;" src="/images/delete.png"/>'
                            +'</div>'
                            +'</div>'
                        $("#uploader").before(img);
                    }else if (isIOS) {
                        wx.getLocalImgData({
                            localId: localid, // 图片的localID
                            success: function (res) {
                                var localData = res.localData; // localData是图片的base64数据，可以用img标签显示
                                localids.push(localData);
                                var img =  '<div id="'+fjid+'" style="padding:0;width: 30%;height: 70px;margin: 0 5px;">'
                                    +'<div style="padding:0;border:1px solid grey;">'
                                    +'<img onclick="viewfile(event)" style="height: 50px;width: 100%;" src="'+localid+'"/>'
                                    +'<img onclick=\'delfile(event,"'+fjid+'");\' data-wjlj="'+localid+'" style="height: 20px;width: 100%;" src="/images/delete.png"/>'
                                    +'</div>'
                                    +'</div>'
                                $("#uploader").before(img);
                            }
                        });
                    }else{
                        localids.push(localid);

                        var img =  '<div id="'+fjid+'" style="padding:0;width: 30%;height: 70px;margin: 0 5px;">'
                            +'<div style="padding:0;border:1px solid grey;">'
                            +'<img onclick="viewfile(event)" style="height: 50px;width: 100%;" src="'+localid+'"/>'
                            +'<img onclick=\'delfile(event,"'+fjid+'");\' data-wjlj="'+localid+'" style="height: 20px;width: 100%;" src="/images/delete.png"/>'
                            +'</div>'
                            +'</div>'
                        $("#uploader").before(img);
                    }
                    if(i < localIds.length){
                        upload(localIds, i);
                    }
                }
            });
        }
    });
}

function viewfile(e){
    wx.previewImage({
        current: e.currentTarget.src, // 当前显示图片的http链接
        urls: localids // 需要预览的图片http链接列表
    });
}

function delfile(e,fjid){
    for(var i = 0; i < localids.length; i++){
        if(e.currentTarget.dataset.wjlj == localids[i]){
            localids.splice(i,1);
            break;
        }
    }
    var str = $("#ajaxForm #fjids").val();
    var arr=str.split(",");
    for(var i=0;i<arr.length;i++){
        if(fjid == arr[i]){
            arr.splice(i,1);
            break;
        }
    }
    var str = arr.join(",");
    $("#ajaxForm #fjids").val(str);
    $("#ajaxForm #"+fjid).remove();
}

function getInspectionInfo(){
    $.ajax({
        url: '/wechat/getInspectionInfo',
        type: 'post',
        dataType: 'json',
        data : {"ybbh" : $("#ajaxForm #ybbh").val(),"wxid" : $("#wxid").val()},
        success: function(result) {
            if(result.sjxxDto && result.sjxxDto.zt != '80'){
                //患者姓名
                $("#ajaxForm #hzxm").val(result.sjxxDto.hzxm);
                //性别
                $("input:radio[value='"+result.sjxxDto.xb+"']").attr('checked','true');
                //年龄
                $("#ajaxForm #nl").val(result.sjxxDto.nl);
                //年龄单位
                $("#ajaxForm #nldw").val(result.sjxxDto.nldw);
                //电话
                $("#ajaxForm #dh").val(result.sjxxDto.dh);
                //送检单位
                $("#ajaxForm #sjdwid").val(result.sjxxDto.sjdw);
                $("#ajaxForm #sjdwmc").val(result.sjxxDto.sjdwmc);
                //科室
                $("#ajaxForm #ksid").val(result.sjxxDto.ks);
                for (var i = 0; i < ksxxlist.length; i++) {
                    if(ksxxlist[i].dwid == result.sjxxDto.ks){
                        $("#ajaxForm #ksmc").val(ksxxlist[i].dwmc)
                        if (ksxxlist[i].kzcs == '1'){
                            $("#qtksInput").show()
                            $("#qtksInput #qtks").val(result.sjxxDto.qtks)
                        } else {
                            $("#qtksInput").hide()
                            $("#qtksInput #qtks").val("")
                        }
                    }
                }
                $("#ajaxForm #sjys").val(result.sjxxDto.sjys);
                $("#ajaxForm #sjysmc").val(result.sjxxDto.sjys);
                $("#ajaxForm #ysdh").val(result.sjxxDto.ysdh);
                $("#ajaxForm #db").val(result.sjxxDto.db);
                $("#ajaxForm #kdh").val(result.sjxxDto.kdh);
                $("#ajaxForm #cwh").val(result.sjxxDto.cwh);
                $("#ajaxForm #zyh").val(result.sjxxDto.zyh);
                $("#ajaxForm #dbm").val(result.sjxxDto.db);
                //送检id
                $("#ajaxForm #sjid").val(result.sjxxDto.sjid);
                $("#ajaxForm #jcxmids").val(result.sjxxDto.jcxm);
                $("input:radio[value='"+result.sjxxDto.kdlx+"']").attr('checked','true');
                $("input:radio[value='"+result.sjxxDto.sjqf+"']").attr('checked','true');

                // 检测项目限制
                detectlist = result.detectlist;
                xzdetectList = [];
                var dqdetectcskz = "";
                for (var i = 0; i < detectlist.length; i++) {
                    var lsjcxm = {label:detectlist[i].csmc,value:detectlist[i].csid};
                    xzdetectList.push(lsjcxm)
                    if($("#jcxmids").val().indexOf(detectlist[i].csid)!=-1){
                        dqdetectid += ","+detectlist[i].csid
                        dqdetectcskz += ","+detectlist[i].cskz1
                        $("#jcxmmc").val(detectlist[i].csmc)
                    }
                }
                dqdetectid = dqdetectid.substring(1);
                dqdetectcskz = dqdetectcskz.substring(1);
                // 由于Res项目，将判断改为如下 if(dqdetectcskz.indexOf("F")!=-1 || dqdetectcskz.indexOf("Z")!=-1){
                if(dqdetectcskz.indexOf("Z")!=-1){
                    $("#jczxmDiv").show()
                    getSubDetectByDetect($("#jcxmids").val())
                }else {
                    $("#jczxmDiv").hide()
                }
                var sjgzbys = result.sjxxDto.sjgzbys;
                if(sjgzbys.length>0){
                    for (var i = 0; i < sjgzbys.length; i++) {
                        $("#ajaxForm [name='bys'][value='"+sjgzbys[i].by+"']").prop("checked",true);
                    }
                }
            }else if(result.sjxxDto && result.sjxxDto.zt == '80'){
                $("#ajaxForm #ybbh").val("");
                $.alert("条码重复不能使用！");
            }
        }
    });
}
/**
 * 按钮禁用事件
 * @param isError 是否提示错误信息
 * @param isDisabled	是否禁用按钮
 * @param errorInfo	错误信息内容
 * @param returnFlg	返回状态 true:成功 false:失败
 * @returns {*}
 */
function buttonDisabled(isDisabled,isError,errorInfo,returnFlg){
    if (isDisabled){
        isButtonActive = false;
        $("#ajaxForm #btn_complete").attr("disabled","true");
        $("#ajaxForm #info_pay").attr("disabled","true");
        $("#ajaxForm #info_save").attr("disabled","true");
    }else {
        isButtonActive = true;
        $("#ajaxForm #btn_complete").removeAttr("disabled");
        $("#ajaxForm #info_pay").removeAttr("disabled");
        $("#ajaxForm #info_save").removeAttr("disabled");
    }
    if (isError){
        $.toptip(errorInfo, 'error');
    }
    return returnFlg;
}
/**
 * 提取患者信息
 */
$("#ajaxForm #getuser").unbind("click").click(function(){
    if(!getWxOrNot()){
        return false;
    }
    var ybbh = $("#ajaxForm #ybbh").val();
    var hzxm = $("#ajaxForm #hzxm").val();
    if(!ybbh && !hzxm){
        $.toptip("请填写标本编号或患者姓名！", 'error');
        return false;
    }
    var wxid = $("#ajaxForm #wxid").val();
    $.ajax({
        type:'post',
        url:"/wechat/inspection/extractUserInfo",
        cache: false,
        data: {"wxid":wxid,"hzxm":hzxm,"ybbh":ybbh},
        dataType:'json',
        success:function(data){
            if(data.status == "success"){
                $("#ajaxForm #sjid").val(data.sjxxDto.sjid);
                $("#ajaxForm #ybbh").val(data.sjxxDto.ybbh);
                $("#ajaxForm #hzxm").val(data.sjxxDto.hzxm);
                $("input:radio[name='xb'][value='"+data.sjxxDto.xb+"']").attr('checked','true');
                $("#ajaxForm #nl").val(data.sjxxDto.nl);
                $("#ajaxForm #nldw").val(data.sjxxDto.nldw);
                $("#ajaxForm #dh").val(data.sjxxDto.dh);
                $("#ajaxForm #sjdw").val(data.sjxxDto.sjdw);
                $("#ajaxForm #yymc").val(data.sjxxDto.yymc);
                $("#ajaxForm #sjdwmc").val(data.sjxxDto.sjdwmc);
                if(data.sjxxDto.sjdwbj=='1'){
                    $("#ajaxForm #qtdwInput").show();
                }else{
                    $("#ajaxForm #qtdwInput").hide();
                }
                $("#ajaxForm #ksid").val(data.sjxxDto.ks);
                for (i = 0; i < xzksList.length; i++) {
                    if(xzksList[i].value == data.sjxxDto.ks){
                        $("#ajaxForm #ksmc").val(xzksList[i].label)
                        break;
                    }
                }
                for (var i = 0; i < ksxxlist.length; i++) {
                    if(ksxxlist[i].dwid == data.sjxxDto.ks){
                        $("#ajaxForm #ksmc").val(ksxxlist[i].dwmc)
                        if (ksxxlist[i].kzcs == '1'){
                            $("#qtksInput").show()
                            $("#qtksInput #qtks").val(data.sjxxDto.qtks)
                        } else {
                            $("#qtksInput").hide()
                            $("#qtksInput #qtks").val("")
                        }
                    }
                }
                $("#ajaxForm #sjys").val(data.sjxxDto.sjys);
                $("#ajaxForm #zyh").val(data.sjxxDto.zyh);
                $("#ajaxForm #cwh").val(data.sjxxDto.cwh);
                $("#ajaxForm #ysdh").val(data.sjxxDto.ysdh);
                $("#ajaxForm #db").val(data.sjxxDto.db);
                $("#ajaxForm #kdh").val(data.sjxxDto.kdh);
                $("input:radio[value='"+data.sjxxDto.kdlx+"']").attr('checked','true');
                initKdlx()
                // 检测单位
                $("#hzxxInfo #jcdwid").val(data.sjxxDto.jcdw)
                $("#hzxxInfo #jcdwmc").val(data.sjxxDto.jcdwmc)
                refreshUnit(data.sjxxDto.jcdw)
                $("input:radio[value='"+data.sjxxDto.sjqf+"']").attr('checked','true');
                //科研项目
                var kyxmid = data.sjxxDto.kyxm;
                var sjqf = data.sjxxDto.sjqf;
                xzkylist = [];
                if(sjqf){
                    for (var i = 0; i < kylist.length; i++) {
                        if(kylist[i].fcsid == sjqf){
                            var lskyxm = {label:kylist[i].csmc,value:kylist[i].csid};
                            xzkylist.push(lskyxm)
                            if(kyxmid == kylist[i].csid){
                                $("#ajaxForm #kyxmmc").val(kylist[i].csmc)
                            }
                        }
                    }
                }
                if(xzkylist.length>0){
                    $("#kyxmDiv").show();
                }else{
                    $("#ajaxForm #kyxm").val("");
                    $("#ajaxForm #kyxmmc").val("");
                    $("#kyxmDiv").hide();
                }
                //检测项目、初始化检测子项目
                var dqdetectcsid = data.sjxxDto.jcxm;
                if(dqdetectcsid){
                    var dqdetectcskz = ""
                    var dqdetectcsmc = data.sjxxDto.jcxmmc;
                    $("#jcxmids").val(dqdetectcsid)
                    $("#jcxmmc").val(dqdetectcsmc)
                    for (var i = 0; i < detectlist.length; i++) {
                        if(dqdetectcsid.indexOf(detectlist[i].csid)!=-1){
                            dqdetectcskz += ","+detectlist[i].cskz1
                        }
                    }
                    dqdetectcskz = dqdetectcskz.substring(1);
                    // 由于Res项目，将判断改为如下 if(dqdetectcskz.indexOf("F")!=-1 || dqdetectcskz.indexOf("Z")!=-1){
                    if(dqdetectcskz.indexOf("Z")!=-1){
                        $("#jczxmDiv").show()
                        $("#jczxmmc").val(data.sjxxDto.jczxmmc);
                        $("#jczxmid").val(data.sjxxDto.jczxm);
                    }else {
                        $("#jczxmDiv").hide()
                    }
                }

                if(data.sjxxDto.xmglids){
                    $("#ajaxForm #xmglids").val(data.sjxxDto.xmglids);
                }

                if(data.sjxxDto.sjgzbys){
                    $.each(data.sjxxDto.sjgzbys,function(n,value) {
                        $("#ajaxForm [name='bys'][value='"+value.by+"']").prop("checked",true);
                    });
                }
                if(data.fjcfbDtos){
                    refreshFj(data.fjcfbDtos);
                }
            }else{
                if(data.message){
                    $.toptip(data.message, 'error');
                    $("#ajaxForm #ybbh").val("");
                }else{
                    $.toptip("未获取到数据!", 'error');
                }
            }
        }
    });
});

/**
 * 中文数字转阿拉伯数字
 * @param digit
 */
function zhDigitToArabic(digit) {
    var result = 0,quotFlag;
    if(!isNaN(digit)){
        result = digit;
        return result;
    }
    const zh = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九'];
    const unit = ['千', '百', '十'];
    const quot = ['万', '亿', '兆', '京', '垓', '秭', '穰', '沟', '涧', '正', '载', '极', '恒河沙', '阿僧祗', '那由他', '不可思议', '无量', '大数'];

    for (var i = digit.length - 1; i >= 0; i--) {
        if (zh.indexOf(digit[i]) > -1) { // 数字
            if (quotFlag) {
                result += quotFlag * getNumber(digit[i]);
            } else {
                result += getNumber(digit[i]);
            }
        } else if (unit.indexOf(digit[i]) > -1) { // 十分位
            if (quotFlag) {
                result += quotFlag * getUnit(digit[i]) * getNumber(digit[i - 1]);
            } else {
                if(i == 0){
                    result += getUnit(digit[i]);
                }else{
                    result += getUnit(digit[i]) * getNumber(digit[i - 1]);
                }
            }
            --i;
        } else if (quot.indexOf(digit[i]) > -1) { // 万分位
            if (unit.indexOf(digit[i - 1]) > -1) {
                if (getNumber(digit[i - 1])) {
                    result += getQuot(digit[i]) * getNumber(digit[i - 1]);
                } else {
                    result += getQuot(digit[i]) * getUnit(digit[i - 1]) * getNumber(digit[i - 2]);
                    quotFlag = getQuot(digit[i]);
                    --i;
                }
            } else {
                result += getQuot(digit[i]) * getNumber(digit[i - 1]);
                quotFlag = getQuot(digit[i]);
            }
            --i;
        }
    }
    return result;
    // 返回中文大写数字对应的阿拉伯数字
    function getNumber(num) {
        for (var i = 0; i < zh.length; i++) {
            if (zh[i] == num) {
                return i;
            }
        }
    }
    // 取单位
    function getUnit(num) {
        for (var i = unit.length; i > 0; i--) {
            if (num == unit[i - 1]) {
                return Math.pow(10, 4 - i);
            }
        }
    }
    // 取分段
    function getQuot(q) {
        for (var i = 0; i < quot.length; i++) {
            if (q == quot[i]) {
                return Math.pow(10, (i + 1) * 4);
            }
        }
    }
}

var chnNumChar = {
    零: 0,
    一: 1,
    二: 2,
    三: 3,
    四: 4,
    五: 5,
    六: 6,
    七: 7,
    八: 8,
    九: 9
};

/**
 * 手机号转换
 * @param digit
 * @returns {Number}
 */
function phoneNumber(digit) {
    var rtn = 0;
    if(!isNaN(digit)){
        rtn = digit;
        return rtn;
    }
    var str = digit.split('');
    for (var i = 0; i < str.length; i++) {
        var num = chnNumChar[str[i]];
        rtn = rtn * 10 + num;
    }
    return rtn;
}

/**
 * 全角数字或字母转换为半角
 * @param str
 * @param flag
 * @returns {String}
 */
function DBC2SBC(str, flag) {
    var i;
    var result = '';
    for (i = 0; i < str.length; i++) {
        str1 = str.charCodeAt(i);
        if (str1 < 65296) {
            result += String.fromCharCode(str.charCodeAt(i));
            continue;
        }
        if (str1 < 125 && !flag)
            result += String.fromCharCode(str.charCodeAt(i));

        else result += String.fromCharCode(str.charCodeAt(i) - 65248);
    }
    return result;
}

window.onload = function() {
    var list = new Array();
    for (var i = 50; i < document.all.length; i++) {
        if (document.all[i].type == "text" && document.all[i].style.display != "none")
            list.push(i);
    }

    for (var i = 0; i < list.length - 1; i++) {
        document.all[list[i]].setAttribute("nextFocusIndex", list[i + 1]);
        document.all[list[i]].onkeydown = JumpToNext;
    }
    for (var i = list.length - 1; i < document.all.length; i++) {
        if (document.all[i].type == "button") {
            document.all[list[list.length - 1]].setAttribute("nextFocusIndex", i);
            document.all[list[list.length - 1]].onkeydown = JumpToNext;
            break;
        }
    }
    document.all[list[0]].focus();
}

function JumpToNext() {
    if (event.keyCode == 13) {
        var nextFocusIndex = this.getAttribute("nextFocusIndex");
        document.all[nextFocusIndex].focus();
    }
}

function del(fjid,wjlj){
    var url= "/wechat/inspection/delFile";
    jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
        setTimeout(function(){
            if(responseText["status"] == 'success'){
                $.toptip(responseText["message"], 'success');
                $("#"+fjid).remove();
            }else if(responseText["status"] == "fail"){
                $.toptip(responseText["message"], 'error');
            } else{
                $.toptip(responseText["message"], 'error');
            }
        },1);
    },'json');
}

var predb = null;
function refreshDB(){
    var jcdw = $("#ajaxForm #jcdwid").val();
    refreshUnit(jcdw)
}

function refreshSjysDB(){
    // 查询检测单位
    $.ajax({
        url: '/wechat/selectDetectionUnit',
        type: 'post',
        data: {
            "hbmc": $("#sjysadd_hzhb").val()
        },
        dataType: 'json',
        success: function(result) {
            unitlist = result.unitList;
            if(unitlist != null && unitlist.length > 0){
                xzjcdwList = [];
                for (var i = 0; i < unitlist.length; i++) {
                    var lsuniy = {label:unitlist[i].csmc,value:unitlist[i].csid};
                    xzjcdwList.push(lsuniy)
                    if($("#ajaxForm #jcdwid").val()){
                        if(lsuniy.value==$("#ajaxForm #jcdwid").val()){
                            $("#jcdwmc").val(lsuniy.label);
                            $("#jcdwid").val(lsuniy.value);
                        }
                    }
                }
                if(!$("#ajaxForm #jcdwid").val()){
                    $("#jcdwmc").val("");
                    $("#jcdwid").val("");
                    if (unitlist.length == 1) {
                        $("#jcdwmc").val(unitlist[0].csmc);
                        $("#jcdwid").val(unitlist[0].csid);
                    }
                }
            }
        }
    });
}
/**
 * 根据合作伙伴刷新检测单位
 * @returns {boolean}
 */
function refreshUnit(jcdw){
    //根据伙伴匹配送检区分
    initSjhb()
    //刷新检测单位
    var db = $("#ajaxForm #db").val();
    if(db == predb){
        if (!jcdw){
            return false;
        }else {
            let isCompare = false;
            if (xzunitList!=null && xzunitList.length>0){
                for (var i = 0; i < xzunitList.length; i++) {
                    if(xzunitList[i].value==jcdw){
                        $("#jcdwmc").val(xzunitList[i].label);
                        $("#jcdwid").val(xzunitList[i].value);
                        isCompare = true;
                        break;
                    }
                }
            }
            if (isCompare){
                return  false;
            }else {
                $("#jcdwmc").val(xzunitList[0].label);
                $("#jcdwid").val(xzunitList[0].value);
            }
        }
    }
    predb = db;
    // 查询检测单位
    $.ajax({
        url: '/wechat/selectDetectionUnit',
        type: 'post',
        data: {
            "hbmc": db
        },
        dataType: 'json',
        success: function(result) {
            let isCompare = false;
            unitlist = result.unitList;
            if(unitlist != null && unitlist.length > 0){
                xzunitList = [];
                for (var i = 0; i < unitlist.length; i++) {
                    var lsuniy = {label:unitlist[i].csmc,value:unitlist[i].csid};
                    xzunitList.push(lsuniy)
                    if(jcdw){
                        if(lsuniy.value==jcdw){
                            $("#jcdwmc").val(lsuniy.label);
                            $("#jcdwid").val(lsuniy.value);
                            isCompare = true;
                        }
                    }
                }
                if(!jcdw || !isCompare){
                    $("#jcdwmc").val("");
                    $("#jcdwid").val("");
                    if (unitlist.length == 1) {
                        $("#jcdwmc").val(unitlist[0].csmc);
                        $("#jcdwid").val(unitlist[0].csid);
                    }
                }
            }
        }
    });

}

function initSjhb(){
    var sjqf = $("#ajaxForm input:radio[name='sjqf']:checked").val();
    var sjqfmc = $("#ajaxForm input:radio[name='sjqf']:checked").attr("sjqfmc");
    var sjhbmc = $("#ajaxForm #db").val();
    var sjhbmcPrefix = sjhbmc;//送检伙伴名称前缀
    var sjhbmcSuffix = "";//送检伙伴名称后缀
    if (sjhbmc == null || sjhbmc == "" || sjqfmc == null || sjqfmc == "") {
        return false;
    }
    //若送检区分是“科研”或“入院”
    if (sjqfmc.indexOf("科研") > -1 || sjqfmc.indexOf("入院") > -1){
        sjhbmcSuffix = sjqfmc;
    }
    //若送检伙伴名称的最后两位是“科研”
    if ((sjhbmc.indexOf("科研") > -1 && sjhbmc.substring(sjhbmc.length-2,sjhbmc.length) == "科研") || (sjhbmc.indexOf("入院") > -1 && sjhbmc.substring(sjhbmc.length-2,sjhbmc.length) == "入院") ) {
        sjhbmcPrefix = sjhbmc.substring(0,sjhbmc.length-2);
    }
    sjhbmc = sjhbmcPrefix + sjhbmcSuffix;
    $("#ajaxForm #db").val(sjhbmc);
}
//科研项目数据初始化
function initKyxm(){
    $("#ajaxForm #kyxm").val("");
    $("#ajaxForm #kyxmmc").val("");
    var sjqf = $("#ajaxForm input:radio[name='sjqf']:checked").val();
    xzkylist = [];
    if(sjqf){
        for (var i = 0; i < kylist.length; i++) {
            if(kylist[i].fcsid == sjqf){
                var lskyxm = {label:kylist[i].csmc,value:kylist[i].csid};
                xzkylist.push(lskyxm)
            }
        }
    }
    if(xzkylist.length>0){
        $("#kyxmDiv").show();
    }else{
        $("#ajaxForm #kyxm").val("");
        $("#ajaxForm #kyxmmc").val("");
        $("#kyxmDiv").hide();
    }
}
/**
 * 科研项目改变事件
 */
function chooseKyxm(){
    weui.picker(
        xzkylist, {
            defaultValue: [$("#kyxm").val()?$("#kyxm").val():xzkylist[0].value],
            onChange: function (result) {
                console.log(result);
            },
            onConfirm: function (result) {
                $("#kyxmmc").val(result[0].label);
                $("#kyxm").val(result[0].value);
            },
            title: '请选择立项编号'
        });
}
//初始化fileinput
var FileInput = function () {
    var oFile = new Object();
    //初始化fileinput控件（第一次初始化）
    //参数说明 singleFlg 好像是标明 是否单文件上传限制，有点忘记了
    //impflg 用于后台判断是否为导入。1为导入，则开启线程进行导入  2：代表附件保存
    oFile.Init = function(ctrlName,callback,impflg,singleFlg,fileName) {
        var control = $('#' + ctrlName + ' #' + fileName);
        var filecnt = 0;

        //初始化上传控件的样式
        control.fileinput({
            language: 'zh', //设置语言
            uploadUrl: "/wechat/file/saveImportFile?access_token="+$("#ac_tk").val(), //上传的地址
            showUpload: false, //是否显示上传按钮
            showPreview: true, //展前预览
            showCaption: true,//是否显示标题
            showRemove: true,
            showCancel: false,
            showClose: false,
            showUploadedThumbs: true,
            encodeUrl: false,
            purifyHtml: false,
            browseClass: "btn btn-primary", //按钮样式
            dropZoneEnabled: false,//是否显示拖拽区域
            //minImageWidth: 50, //图片的最小宽度
            //minImageHeight: 50,//图片的最小高度
            //maxImageWidth: 1000,//图片的最大宽度
            //maxImageHeight: 1000,//图片的最大高度
            maxFileSize: 0,//单位为kb，如果为0表示不限制文件大小
            //minFileCount: 0,
            maxFileCount: 5, //表示允许同时上传的最大文件个数
            enctype: 'multipart/form-data',
            validateInitialCount:true,
            previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
            msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
            layoutTemplates :{
                //actionDelete:'', //去除上传预览的缩略图中的删除图标
                actionUpload:'',//去除上传预览缩略图中的上传图片；
                //actionZoom:''   //去除上传预览缩略图中的查看详情预览的缩略图标。
            },

            uploadExtraData:function (previewId, index) {
                //向后台传递id作为额外参数，是后台可以根据id修改对应的图片地址。
                var obj = {};
                obj.access_token = $("#ac_tk").val();
                obj.ywlx = $("#"+ ctrlName + " #ywlx").val();
                if(impflg && impflg == 1){
                    obj.impflg = 1;
                }
                obj.fileName = fileName;
                return obj;
            },
            initialPreviewAsData:false,
            initialPreview: [        //这里配置需要初始展示的图片连接数组，字符串类型的，通常是通过后台获取后然后组装成数组直接赋给initialPreview就行了
                //"http://localhost:8086/common/file/getFileInfo",
            ],
            initialPreviewConfig: [ //配置预览中的一些参数
            ]
        })
            //删除初始化存在文件时执行
            .on('filepredelete', function(event, key, jqXHR, data) {

            })
            //删除打开页面后新上传文件时执行
            .on('filesuccessremove', function(event, key, jqXHR, data) {
                if(fileName == 'wechat_file'){
                    var frames = $("#"+ ctrlName +" .file-preview-frame.krajee-default.kv-preview-thumb.file-preview-success");
                    var index = 0;
                    for(var i=0;i<frames.length;i++){
                        if(key == frames[i].id){
                            index = i;
                            break;
                        }
                    }
                    var str = $("#"+ ctrlName + " #fjids").val();
                    var arr=str.split(",");
                    arr.splice(index,1);
                    var str=arr.join(",");
                    $("#"+ ctrlName + " #fjids").val(str);
                }
            })

            // 实现图片被选中后自动提交
            .on('filebatchselected', function(event, files) {
                // 选中事件
                $(event.target).fileinput('upload');
                filecnt = files.length;

            })
            // 异步上传错误结果处理
            .on('fileerror', function(event, data, msg) {  //一个文件上传失败
                // 清除当前的预览图 ，并隐藏 【移除】 按钮
                $(event.target)
                    .fileinput('clear')
                    .fileinput('unlock');
                if(singleFlg && singleFlg==1){
                    $(event.target)
                        .parent()
                        .siblings('.fileinput-remove')
                        .hide()
                }
                // 打开失败的信息弹窗
                $.toptip("请求失败，请稍后重试!", 'error');
            })
            // 异步上传成功结果处理
            .on('fileuploaded', function(event, data) {
                // 判断 code 是否为  0	0 代表成功
                status = data.response.status
                if (status === "success") {

                    if(callback){
                        eval(callback+"('"+ data.response.fjcfbDto.fjid+"')");
                    }
                } else {
                    // 失败回调
                    // 清除当前的预览图 ，并隐藏 【移除】 按钮
                    $(event.target)
                        .fileinput('clear')
                        .fileinput('unlock');
                    if(singleFlg && singleFlg==1){
                        $(event.target)
                            .parent()
                            .siblings('.fileinput-remove')
                            .hide()
                    }
                    // 打开失败的信息弹窗
                    $.toptip("请求失败，请稍后重试!", 'error');
                }
            })
    }
    return oFile;
};

function refreshFj(fjcfbDtos){
    $("#ajaxForm #fjcfbDto").html("");
    var fileHtml = "";
    if(fjcfbDtos != null && fjcfbDtos.length > 0){
        for (var i = 0; i < fjcfbDtos.length; i++) {
            fileHtml += '<div id="'+fjcfbDtos[i].fjid+'" style="font-size: small;">' +
                '	<button title="删除" class="weui-icon-delete" style="color: red;margin-top: -15px" type="button" onclick="del(\''+fjcfbDtos[i].fjid+'\',\''+fjcfbDtos[i].wjlj+'\');"></button>' +
                '	<span style="white-space: nowrap;display: inline-block;overflow: hidden;text-overflow: ellipsis;max-width: 150px">附件'+(i+1)+':'+fjcfbDtos[i].wjm+'</span>' +
                '</div>'
        }
    }
    $("#ajaxForm #fjcfbDto").append(fileHtml);
}

function refreshFile(){
    $("#ajaxForm #fjids").val("");
    $("#ajaxForm .upfile").remove();

    $("#ajaxForm #fileDiv").html("");
    var fileHtml="<input id='wechat_file' name='wechat_file' type='file'>";
    $("#ajaxForm #fileDiv").append(fileHtml);
    //初始化fileinput
    var oFileInput = new FileInput();
    oFileInput.Init("ajaxForm","displayUpInfo",2,1,"wechat_file");
}

function displayUpInfo(fjid){
    if(!$("#ajaxForm #fjids").val()){
        $("#ajaxForm #fjids").val(fjid);
    }else{
        $("#ajaxForm #fjids").val($("#ajaxForm #fjids").val()+","+fjid);
    }
}

//上传图片点击开始
$("#ajaxForm #uploader").on("touchstart",function(){
    $("#uploader img").eq(0).attr("src","/images/addon.png");
})
//上传图片点击结束
$("#ajaxForm #uploader").on("touchend",function(){
    $("#uploader img").eq(0).attr("src","/images/add.png");
})

/**
 * 判断是否为微信内置浏览器
 */
function isFromWeiXin() {
    var ua = window.navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i) == 'micromessenger'){
        var system = {
            win: false,
            mac: false
        };
        //检测平台
        var p = navigator.platform;
        system.win = p.indexOf("Win") == 0;
        system.mac = p.indexOf("Mac") == 0;
        // 非微信PC端上打开内置浏览器不做操作
        if (system.win || system.mac) {
            $("#chrom").show();
            $("#phone").hide();
        }
    }else{
        $("#chrom").show();
        $("#phone").hide();
    }

}

$(document).ready(function(){
    $("#ajaxForm #hzxxInfo").show()
    $("#ajaxForm #sjysInfo").hide()
    $("#ajaxForm #sjysAdd").hide()
    $("#ajaxForm #sjdwInfo").hide()
    if(!getWxOrNot()){
        return false;
    }
    isFromWeiXin();
    //0.初始化fileinput
    var oFileInput = new FileInput();
    oFileInput.Init("ajaxForm","displayUpInfo",2,1,"wechat_file");
    //绑定事件
    btnBind();
    //初始化页面数据
    initPage();
    //监听送检区分改变事件
    $('input:radio[name="sjqf"]').change( function(){
        refreshDB();
        initKyxm();
    })
});

$(document.body).infinite().on("infinite", function() {
    if (loadingFlag){
        return;
    }
    loadingFlag = true
    if (pageFlag == "sjdwInfo"){
        loadMoreSjdw()
    }
});
//设置上一页为空
function pushHistory() {
    var state = {
        title: "title",
        url: "#"
    };
    window.history.pushState(state, "title", "#");
}
function goBack(){
    if($("#ajaxForm #hzxxInfo").is(":hidden")){
        pushHistory();
        if($("#ajaxForm #sjysInfo").is(":hidden")){
            //若 送检医生选择页面 是隐藏的
            if($("#ajaxForm #sjysAdd").is(":hidden")){
                //若 送检医生新增页面 是隐藏的
                if($("#ajaxForm #sjdwInfo").is(":hidden")){
                    //若 送检单位选择页面 是隐藏的
                    $("#ajaxForm #hzxxInfo").show()
                    $("#ajaxForm #sjysInfo").hide()
                    $("#ajaxForm #sjysAdd").hide()
                    $("#ajaxForm #sjdwInfo").hide()
                }else {
                    //若 送检单位选择页面 是显示的
                    $("#ajaxForm #hzxxInfo").hide()
                    $("#ajaxForm #sjysInfo").hide()
                    $("#ajaxForm #sjysAdd").show()
                    $("#ajaxForm #sjdwInfo").hide()
                }
            }else {
                //若 送检医生新增页面 是显示的
                $("#ajaxForm #hzxxInfo").hide()
                $("#ajaxForm #sjysInfo").show()
                $("#ajaxForm #sjysAdd").hide()
                $("#ajaxForm #sjdwInfo").hide()
            }
        }else {
            //若 送检医生选择页面 是显示的
            $("#ajaxForm #hzxxInfo").show()
            $("#ajaxForm #sjysInfo").hide()
            $("#ajaxForm #sjysAdd").hide()
            $("#ajaxForm #sjdwInfo").hide()
        }
    }
    else {
        window.history.go(navigationIndex)
    }
}
/**
 * 保存事件
 * @returns {boolean}
 */
function complete(){
    $("#ajaxForm #btn_complete").attr("disabled","true");
    if(!getWxOrNot()){
        return false;
    }
    buttonDisabled(true,false,"",true);
    if(!getWxOrNot()){
        return buttonDisabled(false,false,"",false);
    }
    $("#ajaxForm #db").val($("#ajaxForm #db").val().replace(/\s+/g,""));
    $("#ajaxForm #db").val(DBC2SBC($("#ajaxForm #db").val()));
    $("#ajaxForm #sjys").val($("#ajaxForm #sjys").val().replace(/\s+/g,""));
    $("#ajaxForm #ybbh").val($("#ajaxForm #ybbh").val().replace(/\s+/g,""));
    //附件
    if(!$("#ajaxForm #fjids").val() && $("#ajaxForm #fjcfbDto").children().length == 0){
        return buttonDisabled(false,true,"请上传附件!",false);
    }
    if (!checkYbbh()){
        $("#ajaxForm .preBtn").attr("disabled", false);
        return buttonDisabled(false,false,"",false);
    }
    var lsybbh = $("#ajaxForm #ybbh").val();
    //患者姓名
    if(!$("#ajaxForm #hzxm").val()){
        return buttonDisabled(false,true,"请填写患者姓名!",false);
    }
    var lshzxm = $("#ajaxForm #hzxm").val();
    //性别
    if($("#ajaxForm input:radio[name='xb']:checked").val()=="" || $("#ajaxForm input:radio[name='xb']:checked").val()==null){
        return buttonDisabled(false,true,"请选择性别!",false);
    }
    var lsxb = $("#ajaxForm input:radio[name='xb']:checked").val();
    //年龄+年龄单位
    if(!$("#ajaxForm #nl").val()){
        return buttonDisabled(false,true,"请填写年龄!",false);
    }
    if(!$("#ajaxForm #nldw").val()){
        return buttonDisabled(false,true,"请选择年龄单位!",false);
    }
    var lsnl = $("#ajaxForm #nl").val()
    var lsnldw = $("#ajaxForm #nldw").val();
    //联系电话
    var lslxdh = $("#ajaxForm #dh").val();
    //送检医生
    var lssjys = $("#ajaxForm #sjys").val();
    if(!$("#ajaxForm #sjys").val()){
        return buttonDisabled(false,true,"请选择主治医师!",false);
    }
    //医生电话
    var lsysdh = $("#ajaxForm #ysdh").val();
    //送检单位
    if(!$("#ajaxForm #sjdwid").val()){
        return buttonDisabled(false,true,"请选择医院名称!",false);
    }
    //送检单位
    if(!$("#ajaxForm #sjdwmc").val()){
        return buttonDisabled(false,true,"请选择医院名称!",false);
    }
    var lssjdw = $("#ajaxForm #sjdwid").val();
    var lssjdwmc = $("#ajaxForm #sjdwmc").val();
    var lsjcxmids = $("#ajaxForm #jcxmids").val().replace("[","").replace("]","");
    //科室
    if(!$("#ajaxForm #ksid").val()){
        return buttonDisabled(false,true,"请选择科室!",false);
    }
    var lsks = $("#ajaxForm #ksid").val();
    //其他科室
    var lsqtks ="";
    for (let i = 0; i < ksxxlist.length; i++) {
        if(ksxxlist[i].dwid == $("#ajaxForm #ksid").val()){
            if (ksxxlist[i].kzcs == "1") {
                if(!$("#ajaxForm #qtks").val()){
                    return buttonDisabled(false,true,"请填写报告显示科室!",false);
                }
            }
            lsqtks = $("#ajaxForm #qtks").val();
        }
    }
    //检测单位
    if(!$("#ajaxForm #jcdwid").val()){
        return buttonDisabled(false,true,"请选择检测单位!",false);
    }
    var lsjcdw = $("#ajaxForm #jcdwid").val();

    //合作伙伴
    if(!$("#ajaxForm #db").val()){
        return buttonDisabled(false,true,"请填写负责人!",false);
    }
    //检测项目
    if(!$("#ajaxForm #jcxmmc").val()){
        return buttonDisabled(false,true,"请填写检测项目!",false);
    }
    var jcxmmc=$("#ajaxForm #jcxmmc").val();
    var lsdb = $("#ajaxForm #db").val();
    //快递类型
    if(!$("#ajaxForm input:radio[name='kdlx']:checked").val()){
        return buttonDisabled(false,true,"请选择快递类型!",false);
    }
    var lskdlx = $("#ajaxForm input:radio[name='kdlx']:checked").val();
    //快递号
    var lskdh = "无"
    if (lskdlx != qtkdlx){
        if(!$("#ajaxForm #kdh").val()){
            return buttonDisabled(false,true,"请填写快递号!",false);
        }
        lskdh = $("#ajaxForm #kdh").val();
    }

	if($("#ajaxForm #kdh").val().length>32){
        return buttonDisabled(false,true,"请填写正确的快递号!",false);
    }
    //检测项目
    if(!$("#ajaxForm #jcxmids").val()){
        return buttonDisabled(false,true,"请选择检测项目!",false);
    }
    //送检区分
    var lssjqf = $("#ajaxForm input:radio[name='sjqf']:checked").val();
    if(!$("#ajaxForm input:radio[name='sjqf']:checked").val()){
        return buttonDisabled(false,true,"请选择送检区分!",false);
    }
    var lskyxm = $("#ajaxForm #kyxm").val()?$("#ajaxForm #kyxm").val():"";
    //科研项目
    if(!$("#ajaxForm #kyxm").val() && !$("#ajaxForm #kyxmDiv").is(":hidden")){
        return buttonDisabled(false,true,"请选择立项编号!",false);

    }
    if (!$("#yblx").val()) {
        $.toptip('请选择标本类型!', 'error');
        $("#ajaxForm #btn_complete").removeAttr("disabled");
        return false;
    }
    var yblx=$("#yblx").val();
    if(!$("#ajaxForm #qt").is(":hidden")){
        if (!$("#yblxmc").val()) {
            $.toptip('请填写其它标本类型!', 'error');
            $("#ajaxForm #btn_complete").removeAttr("disabled");
            return false;
        }
    }
    var yblxmc=$("#yblxmc").val();
    if (!$("#ybtj").val()) {
        $.toptip('请填写标本体积!', 'error');
        $("#ajaxForm #btn_complete").removeAttr("disabled");
        return false;
    }
    var ybtj=$("#ybtj").val();
    if (!$("#cyrq").val()) {
        $.toptip('请选择采样日期!', 'error');
        $("#ajaxForm #btn_complete").removeAttr("disabled");
        return false;
    }
    var cyrq=$("#cyrq").val();
    var sjrq=$("#sjrq").val();
    var bz=$("#bz").val();
    var json=[];
    var sz={"jcxmid":lsjcxmids,"jczxmid":"","xmglid": $("#ajaxForm #xmglids").val().replace("[","").replace("]","")};
    json.push(sz);

    dealSfwsFlag();
    $.ajax({
        type:'post',
        url:"/wechat/miniprogram/saveInspectFirst",
        cache: false,
        data: {
            "wxid":$("#wxid").val(),
            "sjid":($("#sjid").val()?$("#sjid").val():""),
            "ywlx":$("#ywlx").val(),
            "status":"true",
            "fjids":$("#fjids").val(),
            "ybbh":lsybbh,
            "hzxm":lshzxm,
            "xb":lsxb,
            "nl":lsnl,
            "nldw":lsnldw,
            "dh":lslxdh,
            "sjys":$("#sjysmc").val(),
            "ysdh":lsysdh,
            "sjdw":lssjdw,
            "yymc":lssjdwmc,
            "sjdwmc":lssjdwmc,
            "jcxmids":lsjcxmids,
            "jcxm":JSON.stringify(json),
            "ks":lsks,
            "qtks":lsqtks,
            "jcdw":lsjcdw,
            "kdlx":lskdlx,
            "kdh":lskdh,
            "db":lsdb,
            "sjqf":lssjqf,
            "yblx":yblx,
            "yblxmc":yblxmc,
            "ybtj":ybtj,
            "cyrq":cyrq,
            "sjrq":sjrq,
            "bz":bz,
            "jcxmmc":jcxmmc,
            "kyxm":lskyxm,
            "flag":$("#flag").val(),
            "sfws":$("#ajaxForm #sfws").val(),//是否完善标记
            "lczz":$("#ajaxForm #lczz").val()?$("#ajaxForm #lczz").val():'--',
            "qqzd":$("#ajaxForm #qqzd").val()?$("#ajaxForm #qqzd").val():'--',
        },
        dataType:'json',
        success:function(data){
            if (data.status=="success"){
                $.modal({
                    text: data.message,
                    buttons: [
                        { text: "进入清单", onClick: function(){
                                window.location.replace('/wechat/inspPerfectReport?wxid=' + $("#wxid").val() + '&jcxmdms='+ $("#jcxmdms").val()+'&flag='+ $("#flag").val());
                            }},
                        { text: "再录一单", onClick: function(){
                                window.location.replace('/wechat/resFirstReport?wxid=' + $("#wxid").val() + '&jcxmdms='+ $("#jcxmdms").val()+'&flag='+ $("#flag").val());
                            }},
                    ]
                });
            }else {
                buttonDisabled(false,true,data.message,false);
            }
        },
        error: function (data) {
            buttonDisabled(false,true,data,false);
        }

    })
    return true;
    $("#ajaxForm #newflg").val("1");
}

/**
 * 暂存事件
 */
function temporarySave(){
    if(!getWxOrNot()){
        return false;
    }
    $.alert("未完善数据请至“录入清单”中补充");
    $.ajax({
        url: '/wechat/miniprogram/backInspectOne',
        type: 'post',
        dataType: 'json',
        data : $('#ajaxForm').serialize(),
        success: function(result) {
            if(result.status == "success"){
                $.alert(result.message);
            }else{
                $.alert(result.message);
            }
        }
    });
}

/**
 * 转账、扫码付款
 * @param event
 * @returns
 */
function payModal(flag){
    //打开付款前，先对信息进行保存
    $("#ajaxForm #info_pay").attr("disabled","true");
    $("#ajaxForm #btn_complete").attr("disabled","true");
    if(!getWxOrNot()){
        return false;
    }
    if (!checkYbbh()){
        $("#ajaxForm .preBtn").attr("disabled", false);
        return buttonDisabled(false,false,"",false);
    }
    if(!$("#ajaxForm #hzxm").val()){
        return buttonDisabled(false,true,"请填写患者姓名!",false);
    }
    $("#ajaxForm #db").val($("#ajaxForm #db").val().replace(/\s+/g,""));
    $("#ajaxForm #db").val(DBC2SBC($("#ajaxForm #db").val()));
    $("#ajaxForm #sjys").val($("#ajaxForm #sjys").val().replace(/\s+/g,""));
    $("#ajaxForm #ybbh").val($("#ajaxForm #ybbh").val().replace(/\s+/g,""));
    $("#ajaxForm #status").val("true");
    //性别
    var lsxb = ($("#ajaxForm input:radio[name='xb']:checked").val()=="" || $("#ajaxForm input:radio[name='xb']:checked").val()==null)?"":$("#ajaxForm input:radio[name='xb']:checked").val();
    if (!lsxb){
        $("#ajaxForm #status").val("false");
    }
    //年龄+年龄单位
    var lsnl = ($("#ajaxForm #nl").val()?$("#ajaxForm #nl").val():"");
    if (!lsnl){
        $("#ajaxForm #status").val("false");
    }
    var lsnldw = ($("#ajaxForm #nldw").val()?$("#ajaxForm #nldw").val():"岁");
    if (!lsnldw){
        $("#ajaxForm #status").val("false");
    }
    //联系电话
    var lslxdh = ($("#ajaxForm #dh").val()?$("#ajaxForm #dh").val():"");
    //医生电话
    var lsysdh = ($("#ajaxForm #ysdh").val()?$("#ajaxForm #ysdh").val():"");
    //送检单位
    var lssjdw = ($("#ajaxForm #sjdwid").val()?$("#ajaxForm #sjdwid").val():"");
    if (!lssjdw){
        $("#ajaxForm #status").val("false");
    }
    //送检单位名称
    var lssjdwmc = ($("#ajaxForm #sjdwmc").val()?$("#ajaxForm #sjdwmc").val():"");
    if (!lssjdwmc){
        $("#ajaxForm #status").val("false");
    }
    //科室
    var lsks = ($("#ajaxForm #ksid").val()?$("#ajaxForm #ksid").val():"");
    if (!lsks){
        $("#ajaxForm #status").val("false");
    }
    //其他科室
    var lsqtks = (!$("#ajaxForm #qtks").is(":hidden")?($("#qtksInput #qtks").val()?$("#qtksInput #qtks").val():""):"");
    if (!lsqtks){
        $("#ajaxForm #status").val("false");
    }
    //检测单位
    var lsjcdw = ($("#ajaxForm #jcdwid").val()?$("#ajaxForm #jcdwid").val():"");
    if (!lsjcdw){
        $("#ajaxForm #status").val("false");
    }
    //合作伙伴
    var lsdb = ($("#ajaxForm #db").val()?$("#ajaxForm #db").val():"");
    if (!lsdb){
        $("#ajaxForm #status").val("false");
    }
    //快递类型
    var lskdlx = $("#ajaxForm input:radio[name='kdlx']:checked").val()?$("#ajaxForm input:radio[name='kdlx']:checked").val():"";
    if (!lskdlx){
        $("#ajaxForm #status").val("false");
    }
    //快递号
    var lskdh = $("#ajaxForm #kdh").val()?$("#ajaxForm #kdh").val():"";
    if(lskdh && lskdh.length>32){
        return buttonDisabled(false,true,"请填写正确的快递号!",false);
    }
    //检测项目
    var lsjcxmids = $("#ajaxForm #jcxmids").val().replace("[","").replace("]","");
    if (!lsjcxmids){
        $("#ajaxForm #status").val("false");
    }
    var lssjqf = $("#ajaxForm input:radio[name='sjqf']:checked").val()?$("#ajaxForm input:radio[name='sjqf']:checked").val():"";
    if (!lssjqf){
        $("#ajaxForm #status").val("false");
    }
    //科研项目
    var lskyxm =  (!$("#ajaxForm #kyxmDiv").is(":hidden"))?($("#ajaxForm #kyxm").val()?$("#ajaxForm #kyxm").val():""):"";
    //科研项目
    if(!$("#ajaxForm #kyxm").val() && !$("#ajaxForm #kyxmDiv").is(":hidden")){
        $("#ajaxForm #status").val("false");

    }
    var bz= $("#ajaxForm #bz").val()?$("#ajaxForm #bz").val():"";
    var cyrq=$("#ajaxForm #cyrq").val()?$("#ajaxForm #cyrq").val():"";
    if (!cyrq){
        $("#ajaxForm #status").val("false");
    }
    var jcxmmc=$("#ajaxForm #jcxmmc").val()?$("#ajaxForm #jcxmmc").val():"";
    if (!jcxmmc){
        $("#ajaxForm #status").val("false");
    }
    var yblxmc=$("#ajaxForm #yblxmc").val()?$("#ajaxForm #yblxmc").val():"";
    if (!yblxmc){
        $("#ajaxForm #status").val("false");
    }
    var yblx=$("#ajaxForm #yblx").val()?$("#ajaxForm #yblx").val():"";
    if (!yblx){
        $("#ajaxForm #status").val("false");
    }
    var sjrq=$("#ajaxForm #sjrq").val()?$("#ajaxForm #sjrq").val():"";
    var ybtj=$("#ajaxForm #ybtj").val()?$("#ajaxForm #ybtj").val():"";
    if (!ybtj){
        $("#ajaxForm #status").val("false");
    }
    var json=[];
    var sz={"jcxmid":lsjcxmids,"jczxmid":"","xmglid": $("#ajaxForm #xmglids").val().replace("[","").replace("]","")};
    json.push(sz);
    dealSfwsFlag();
    //执行上一步中的保存操作
    $.ajax({
        url: '/wechat/miniprogram/saveInspectFirst',
        type: 'post',
        dataType: 'json',
        data: {
            "wxid":$("#wxid").val(),
            "sjid":($("#sjid").val()?$("#sjid").val():""),
            "ywlx":$("#ywlx").val(),
            "status":$("#ajaxForm #status").val(),
            "fjids":$("#fjids").val(),
            "ybbh":$("#ajaxForm #ybbh").val(),
            "hzxm":$("#ajaxForm #hzxm").val(),
            "xb":lsxb,
            "nl":lsnl,
            "nldw":lsnldw,
            "dh":lslxdh,
            "sjys":$("#sjysmc").val(),
            "ysdh":lsysdh,
            "sjdw":lssjdw,
            "yymc":lssjdwmc,
            "sjdwmc":lssjdwmc,
            "jcxmids":lsjcxmids,
            "jcxm":JSON.stringify(json),
            "ks":lsks,
            "qtks":lsqtks,
            "jcdw":lsjcdw,
            "kdlx":lskdlx,
            "kdh":lskdh,
            "db":lsdb,
            "sjqf":lssjqf,
            "yblx":yblx,
            "yblxmc":yblxmc,
            "ybtj":ybtj,
            "cyrq":cyrq,
            "sjrq":sjrq,
            "bz":bz,
            "jcxmmc":jcxmmc,
            "kyxm":lskyxm,
            "flag":$("#flag").val(),
            "sfws":$("#ajaxForm #sfws").val(),//是否完善标记
            "lczz":$("#ajaxForm #lczz").val()?$("#ajaxForm #lczz").val():'--',
            "qqzd":$("#ajaxForm #qqzd").val()?$("#ajaxForm #qqzd").val():'--',
        },
        success: function(result) {
            if(result.status == "success"){
                var sjxxDto=result.sjxxDto
                var sjid=sjxxDto.sjid
                if (!$("#sjid").val()){
                    $("#sjid").val(sjid)
                }
                var ybbh=sjxxDto.ybbh
                var hzxm=sjxxDto.hzxm
                if(result.status == "success"){
                    $.ajax({
                        url: '/wechat/inspection/getDetectionPayInfo',
                        type: 'post',
                        dataType: 'json',
                        data: {"sjid": sjid},
                        success: function (data) {
                            if (data != null && data.length > 0) {
                                var htmltext = '<div class="weui-media-box_appmsg" style="height: 35px">' +
                                    '<div style="flex: 2;text-align: left">标本编号</div>' +
                                    '<div style="flex: 3;text-align: left"><input disabled id="fk_ybbh" name="fk_ybbh" class="weui-input" style="color: black;font-weight: bold;" value="' + ybbh + '"></div>' +
                                    '<input type="hidden" id="fk_sjid" name="fk_sjid" value="\'' + sjid + '\'">' +
                                    '<div style="flex: 1"><button type="button" class="weui-icon-scan" style="color: #00AFEC" onClick="getQRCode()" className="weui-btn weui-btn_mini weui-btn_primary weui-wa-hotarea" style="font-size: smaller;padding: 0 5px;">扫码</button></div>' +
                                    '</div>' +
                                    '<div class="weui-media-box_appmsg" style="height: 35px">' +
                                    '<div style="flex: 2;text-align: left">患者姓名</div>' +
                                    '<div style="flex: 4;text-align: left"><input disabled id="fk_hzxm" name="fk_hzxm" class="weui-input" style="color: black;font-weight: bold;" value="' + hzxm + '"></div>' +
                                    '</div>'
                                for (let i = 0; i < data.length; i++) {
                                    htmltext += '<div class="weui-media-box_appmsg" style="border-top: 2px solid grey;height: 35px"><div style="font-weight: bolder;text-align: left">' + (data[i].jczxmmc ? data[i].jczxmmc : data[i].jcxmmc) + '</div></div>'
                                    htmltext += '<div class="weui-media-box_appmsg" style="' + (i == data.length - 1 ? 'border-bottom: 2px solid grey;' : '') + 'height: 35px"><div style="text-align: left"><input id="fk_fkje_' + data[i].jcxmid + (data[i].jczxmid ? "_" + data[i].jczxmid : "") + '" name="fk_fkje" class="weui-input" type="number" style="color: black;font-weight: bold;" placeholder="请输入金额"></div></div>'
                                }
                                $.modal({
                                    title: "核对用户信息",
                                    text: htmltext,
                                    autoClose: false,
                                    buttons: [
                                        {
                                            text: (flag == "scan" ? "扫码付款" : (flag == "transfer" ? "支付宝付款" : "确定")),
                                            onClick: function () {
                                                pay(flag, sjid, ybbh, hzxm)
                                            }
                                        },
                                        {
                                            text: "取消",
                                            className: "default",
                                            onClick: function () {
                                                $("#ajaxForm #btn_complete").removeAttr("disabled");
                                                $("#ajaxForm #info_pay").removeAttr("disabled");
                                                $.closeModal()
                                            }
                                        },
                                    ]
                                });
                            }
                        }
                    });
                }
            }else{
                $("#ajaxForm #btn_complete").removeAttr("disabled");
                $("#ajaxForm #info_pay").removeAttr("disabled");
                $.alert(result.message);
            }
        }
    });
}

/**
 * 支付框中的扫码事件
 */
function getQRCode(){
    $.ajax({
        url: '/wechat/getJsApiInfo',
        type: 'post',
        data: {
            "url":location.href.split('#')[0],
            "wbcxdm":$("#wbcxdm").val()
        },
        dataType: 'json',
        success: function(result) {
            //注册信息
            wx.config({
                debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                appId: result.appid, // 必填，公众号的唯一标识
                timestamp: result.timestamp, // 必填，生成签名的时间戳
                nonceStr: result.noncestr, // 必填，生成签名的随机串
                signature: result.sign, // 必填，签名，见附录1
                jsApiList: ['checkJsApi','scanQRCode']
                // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
            });
            wx.error(function(res) {
                console.log(res);
            });
            //config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作
            wx.ready(function() {
                wx.checkJsApi({
                    jsApiList: ['scanQRCode'],
                    success: function(res) {
                        //扫码
                        wx.scanQRCode({
                            needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果
                            scanType: ["qrCode", "barCode"],
                            success: function(res) {
                                // 当needResult 为 1 时，扫码返回的结果
                                var result = res.resultStr;
                                // result = "http://service.matridx.com/wechat/getUserReport?ybbh=20220216003"
                                var s_res = result.split('ybbh=')
                                var scanYbbh = s_res[s_res.length-1];
                                $.ajax({
                                    url: '/wechat/pay/getInspectionInfo',
                                    type: 'post',
                                    data: {"ybbh": scanYbbh},
                                    dataType: 'json',
                                    success: function(data) {
                                        if(data.status == "success"){
                                            $("#fk_ybbh").val(data.sjxxDto.ybbh);
                                            $("#fk_sjid").val(data.sjxxDto.sjid);
                                            $("#fk_hzxm").val(data.sjxxDto.hzxm);
                                            $("#fk_fkje").val("");
                                        }else{
                                            $.toptip('未查询到标本'+ybbh, 'error');
                                        }
                                    }
                                });
                            },
                            fail: function(res) {
                                alert(JSON.stringify(res));
                            }
                        });
                    }
                });
            });
        }
    });
}

/**
 * 调用外部支付接口
 * @param flag
 * @param sjid
 * @param ybbh
 * @returns {boolean}
 */
function pay(flag,sjid,ybbh,hzxm) {
    if(!$("#fk_ybbh").val()){
        $.toptip('请填写标本编号!', 'error');
        return false;
    }
    var fk_fkjeList = document.getElementsByName("fk_fkje");
    var zfjcxm = [];
    var zje = 0;
    if(fk_fkjeList.length>0){
        for (let i = 0; i < fk_fkjeList.length; i++) {
            let fkje = fk_fkjeList[i].value;//付款金额
            if (fkje && (!/^\d+(\.\d{1,2})?$/.test(fkje) || fkje*1 <= 0)){
                $.toptip('请填写付款金额, 允许保留两位小数!', 'error');
                paying = false;
                return false;
            }
            var fk_id_list = fk_fkjeList[i].id.split("_");
            let jcxm = fk_id_list[2];//检测项目
            let json= {};
            json.jcxmid = jcxm//组装
            json.je = fkje?fkje:0//组装
            if (json.je == 0){
                break;
            }
            if (fk_id_list.length > 3){
                json.jczxmid = fk_id_list[3];//组装
            }
            zfjcxm.push(json);
            zje += new Number(fkje?fkje:0);
        }
        if (zje == 0){
            $.toptip('请填写付款金额!', 'error');
            paying = false;
            return false;
        }
        zje = ""+zje;
        zfjcxm = JSON.stringify(zfjcxm);
        $("#zfjcxm").val(zfjcxm);
    }else {
        $.toptip('请确认该样本是否已选择检测项目并填写金额!', 'error');
        paying = false;
        return false;
    }
    if(flag == "scan"){
        $.ajax({
            url: '/wechat/pay/createOrderQRCode',
            type: 'post',
            dataType: 'json',
            data : {
                "ywid" : sjid,
                "ybbh" : ybbh,
                "fkje": zje,
                "wxid": $("#wxid").val(),
                "zfjcxm":zfjcxm,
                "ywlx": "SJ",
                "wbcxdm": $("#wbcxdm").val()
            },
            success: function(data) {
                if(data.status == 'success'){
                    // 二维码路径 data.qrCode
                    $("#preview_ajaxForm #path").val(data.filePath);
                    //去掉患者姓名和样本编号中的空格
                    for (;hzxm.indexOf(" ")>-1;) {
                        hzxm = hzxm.replace(" ","");
                    }
                    for (;ybbh.indexOf(" ")>-1;) {
                        ybbh = ybbh.replace(" ","");
                    }
                    var url="/wechat/pay/picturePreview?path="+ data.filePath +"&fkje="+zje +"&ywid="+sjid +"&ybbh="+ ybbh+"&hzxm="+ hzxm+"&ywlx=SJ";
                    $.closeModal()
                    $("#hzxxInfo").load(url);
                }else{
                    $.toptip('生成订单失败！'+data.message, 'error');
                }
            },
            fail:function (data) {
                $.toptip('网络错误！'+data.message, 'error');
            }
        })
    }else if(flag == "transfer"){
        // 支付宝支付 判断是否为微信环境
        var ua = navigator.userAgent.toLowerCase();
        if (ua.indexOf('micromessenger') != -1) {
            // 打开引导页
            window.location.href="/wechat/pay/alipayGuide?ywid="+ sjid +"&ybbh="+ ybbh +"&fkje="+ zje +"&wxid="+  $("#wxid").val() +"&ywlx=SJ"+"&wbcxdm="+$("#wbcxdm").val();
        }else{
            // 创建支付宝native订单
            $.ajax({
                url: '/wechat/pay/alipayNative',
                type: 'post',
                dataType: 'json',
                data : {"ywid" : sjid, "ybbh" : ybbh,"fkje": zje,"wxid": $("#wxid").val(),"ywlx": "SJ","wbcxdm": $("#wbcxdm").val() },
                success: function(data) {
                    if(data.status == 'success'){
                        $.closeModal()
                        // 唤起支付宝路径 data.qrCode
                        window.location.href = data.qrCode;
                    }else{
                        $.toptip('生成订单失败！'+data.message, 'error');
                    }
                }
            });
        }
    }else {
        // 微信统一下单
        $.ajax({
            url: '/wechat/pay/wechatPayOrder',
            type: 'post',
            dataType: 'json',
            data : {"sjid" : sjid, "ybbh" : ybbh,"fkje": zje,"wxid": $("#wxid").val(),"ywlx": "SJ","wxzflx":"public"},
            success: function(data) {
                if(data.status == 'success'){
                    // 唤起微信支付路径
                    var appId = data.payData.appId; // 公众号名称,由商户传入
                    var timeStamp = data.payData.timeStamp; // 时间戳,自1970年以来的秒数
                    var nonceStr = data.payData.nonceStr; // 随机串
                    var packages = data.payData.package; //微信订单
                    var signType = data.payData.signType; // 微信签名方式
                    var paySign = data.payData.paySign // 微信签名
                    //准备发起微信支付
                    if (typeof WeixinJSBridge == "undefined") {
                        if (document.addEventListener) {
                            document.addEventListener('WeixinJSBridgeReady',onBridgeReady, false);
                        } else if (document.attachEvent) {
                            document.attachEvent('WeixinJSBridgeReady',onBridgeReady);
                            document.attachEvent('onWeixinJSBridgeReady',onBridgeReady);
                        }
                    } else {
                        onBridgeReady(appId,timeStamp,nonceStr,packages,signType,paySign);
                    }
                }else{
                    $.toptip('生成订单失败！'+data.message, 'error');
                }
            }
        });
    }
}

/**
 * 微信支付
 * @param data
 */
function onBridgeReady(appId,timeStamp,nonceStr,packages,signType,paySign,ddh) {
    WeixinJSBridge.invoke('getBrandWCPayRequest', {
        "appId" : appId, // 公众号名称,由商户传入
        "timeStamp" : timeStamp, // 时间戳,自1970年以来的秒数
        "nonceStr" : nonceStr, // 随机串
        "package" : packages, //微信订单
        "signType" : signType, // 微信签名方式
        "paySign" : paySign // 微信签名
    }, function(res) {
        if (res.err_msg == "get_brand_wcpay_request:ok") {
            alert("支付成功,支付成功后跳转到页面："+res.err_msg);
            // 支付成功后跳转的页面
            $("#preview_ajaxForm").attr('action',"/wechat/pay/wxPayComplete");
            $("#preview_ajaxForm").attr('method',"get");
            $("#preview_ajaxForm").submit();
        } else if (res.err_msg == "get_brand_wcpay_request:cancel") {
            alert("支付取消,支付取消后跳转到页面："+res.err_msg);
        } else if (res.err_msg == "get_brand_wcpay_request:fail") {
            WeixinJSBridge.call('closeWindow');
            alert("支付失败,支付失败后跳转到页面："+res.err_msg);
            // 支付失败后跳转的页面
            $("#preview_ajaxForm").attr('action',"/wechat/pay/wxPayFaild");
            $("#preview_ajaxForm").attr('method',"get");
            $("#preview_ajaxForm").submit();
        } // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok,但并不保证它绝对可靠。
    });
}

/**
 * 采样日期改变事件
 */
function changeCyrq(){
    weui.datePicker({
        defaultValue: ($("#cyrq").val()?$("#cyrq").val().split("-"):[new Date().getFullYear(), new Date().getMonth() + 1, new Date().getDate()]),
        onConfirm: function(result){
            var cyrq = result[0]+"-"+(result[1]>9?result[1]:"0"+result[1])+"-"+(result[2]>9?result[2]:"0"+result[2]);
            $("#cyrq").val(cyrq)
        },
        id: 'datePickerCyrq'
    });
}

var nowDate = new Date();

/**
 * 送检日期改变事件
 */
function changeSjrq(){
    weui.datePicker({
        defaultValue: ($("#sjrq").val()?$("#sjrq").val().split("-"):[new Date().getFullYear(), new Date().getMonth() + 1, new Date().getDate()]),
        onConfirm: function(result){
            var sjrq = result[0]+"-"+(result[1]>9?result[1]:"0"+result[1])+"-"+(result[2]>9?result[2]:"0"+result[2]);
            $("#sjrq").val(sjrq)
        },
        id: 'datePickerSjrq'
    });
}

/**
 * 样本类型改变事件
 */
function changeYblx(){
    weui.picker(
        xzYblxList, {
            defaultValue: [$("#yblx").val()?$("#yblx").val():xzYblxList[0].value],
            onConfirm: function (result) {
                $("#xzyblxmc").val(result[0].label)
                $("#yblx").val(result[0].value)
                if(result[0].value == qtyblxid){
                    $("#yblxmc").val("")
                }else {
                    $("#yblxmc").val(result[0].label)
                }
                initQtYblx();
                initQqjc();
            },
            id: 'yblxchange',
            title: '请选择标本类型'
        });
}
$(document).ready(function(){
    if(!getWxOrNot()){
        return false;
    }
    //初始化样本类型
    initYblx();
});
/**
 * 初始化样本类型
 */
function initYblx(){
    //初始化所有样本类型、样本类型选择list
    var yblxInputList =$("#samplelist input");
    var jcxmdms = $("#jcxmdms").val().split(",")
    xzYblxList = [];
    yblxList = [];
    var dqyblxmc;
    for (var i = 0; i < yblxInputList.length; i++) {
        var yblxcsid = yblxInputList[i].id;
        var yblxcsmc = yblxInputList[i].getAttribute("csmc");
        var yblxcsdm = yblxInputList[i].getAttribute("csdm");
        var yblxcskz2 = yblxInputList[i].getAttribute("extend_2");
        for (var j=0;j<jcxmdms.length;j++){
            if (yblxcskz2.indexOf(jcxmdms[j])!=-1){
                var xzlsyblx = {label:yblxcsmc,value:yblxcsid};
                xzYblxList.push(xzlsyblx)
            }
        }
        var lsyblx = {csid:yblxcsid,csmc:yblxcsmc,csdm:yblxcsdm,cskz2:yblxcskz2};
        yblxList.push(lsyblx)
        if(yblxInputList[i].getAttribute("extend_1")){
            qtyblxid = yblxInputList[i].id;
        }
        if(yblxcsid == $("#yblx").val()){
            dqyblxmc = yblxcsmc;
            $("#xzyblxmc").val(dqyblxmc)
            if(yblxcsid != qtyblxid){
                $("#yblxmc").val(dqyblxmc)
            }
        }
    }
    initQtYblx();
    initQqjc();
}

/**
 * 初始化其它样本类型
 */
function initQtYblx(){
    if(qtyblxid == $("#yblx").val()){
        $("#qt").show();
    }else {
        $("#qt").hide();
    }
}


/**
 * 限制前期检测
 */
function initQqjc(){
    var yblxid=$("#yblx").val();
    //限制前期检测
    var dqyblx;
    var dqcsmc;
    var dqcsdm;
    for (var i = 0; i < yblxList.length; i++) {
        if (yblxid == yblxList[i].csid) {
            dqyblx = yblxList[i]
            dqcsmc=dqyblx.csmc;
            dqcsdm=dqyblx.csdm;
            break;
        }
    }

    var length = $("#ajaxForm .qqjc").length;
    //若标本类型没有选择则全部显示；若选择了，则根据cskz2显示
    if (typeof(dqcsmc) != "undefined"){
        if(length>0){
            for(var i=0;i<length;i++){
                var id=$("#ajaxForm .qqjc")[i].id;
                var cskz2= $("#ajaxForm #"+id).attr("cskz2");
                var cskz2s = cskz2.split(",");
                if (cskz2s.includes(dqcsdm)){
                    $("#ajaxForm #"+id).show();
                    $("#ajaxForm input[name='sjqqjcs["+i+"].yjxm']").val(id.split("-")[1]);
                }else {
                    $("#ajaxForm #"+id.split("-")[1]).val("");
                    $("#ajaxForm #"+id).hide();
                }
            }
        }
    }
}
function checkYbbh(){
    let ybbh = $("#ajaxForm #ybbh").val();
    if(!ybbh){
        $.toptip("请填写样本编号!", 'error');
        return false;
    }
    //若样本编号中包含除英文、数字以及-以外的字符，则提示错误
    if(!/^[a-zA-Z0-9-]+$/.test(ybbh)){
        $.toptip("样本编号只能包含英文、数字以及-!", 'error');
        return false;
    }
    return true;
}
$(function(){
    navigationIndex += -1
    pushHistory()
    window.addEventListener("popstate", function(e) {
        goBack()
    }, false);
});

/**
 * 处理是否完善
 */
function dealSfwsFlag(){
    $("#ajaxForm #sfws").val(checkIsPerfect()?'3':'0');
}

/**
 * 校验所有出报告必填项是否均已填写
 * @returns {boolean}
 */
function checkIsPerfect() {
    //校验样本编号，录单必填项
    if (!$("#ajaxForm #ybbh").val()) {
        return false;
    }
    //校验患者姓名，录单必填项
    if (!$("#ajaxForm #hzxm").val()) {
        return false;
    }
    //校验性别，录单必填项
    if ($("#ajaxForm input:radio[name='xb']:checked").val() == "" || $("#ajaxForm input:radio[name='xb']:checked").val() == null) {
        return false;
    }
    //校验年龄，录单必填项
    if (!$("#ajaxForm #nl").val()) {
        return false;
    }
    //校验年龄单位，录单必填项
    if (!$("#ajaxForm #nldw").val()) {
        return false;
    }
    //校验检测单位，录单必填项
    if (!$("#ajaxForm #jcdwid").val()) {
        return false;
    }
    //校验合作伙伴，录单必填项
    if (!$("#ajaxForm #db").val()) {
        return false;
    }
    //校验送检区分，完善必填项
    if (!$("#ajaxForm input:radio[name='sjqf']:checked").val()) {
        return false;
    }
    //校验科研项目，若送检区分为科研项目，则必填，完善必填项
    if (!$("#ajaxForm #kyxm").val() && !$("#ajaxForm #kyxmDiv").is(":hidden")) {
        return false;
    }
    //校验检测项目，录单必填项
    if (!$("#ajaxForm #jcxmids").val()) {
        return false;
    }
    //校验检测子项目，录单必填项
    if (!$("#ajaxForm #jczxmDiv").is(":hidden") && $("#ajaxForm #jczxmDiv").length>0) {
        if (!$("#ajaxForm #jczxmids").val()) {
            return false;
        }
    }
    //校验送检医生，完善必填项
    if (!$("#ajaxForm #sjys").val()) {
        return false;
    }
    if (!$("#ajaxForm #sjdwid").val()) {
        return false;
    }
    //校验报告显示单位，完善必填项
    if (!$("#ajaxForm #sjdwmc").val()) {
        return false;
    }
    //校验科室，完善必填项
    if (!$("#ajaxForm #ksid").val()) {
        return false;
    }
    //校验报告显示科室，完善必填项
    for (let i = 0; i < ksxxlist.length; i++) {
        if (ksxxlist[i].dwid == $("#ajaxForm #ksid").val()) {
            if (ksxxlist[i].kzcs == "1") {
                if (!$("#ajaxForm #qtks").val()) {
                    return false;
                }
            }
        }
    }
    //校验快递类型，完善必填项
    if (!$("#ajaxForm input:radio[name='kdlx']:checked").val()) {
        return false;
    }
    //校验快递号,若快递类型不为无时，则必填，完善必填项
    if ($("#ajaxForm input:radio[name='kdlx']:checked").val() != qtkdlx) {
        if (!$("#ajaxForm #kdh").val()) {
            return false;
        }else if($("#ajaxForm #kdh").val().length>32){
             return false;
         }
    }
    //校验标本类型，录单必填项
    if (!$("#ajaxForm #yblx").val()) {
        return false;
    }
    //校验其他标本类型，录单必填项
    if(!$("#ajaxForm #qt").is(":hidden")){
        if (!$("#yblxmc").val()) {
            return false;
        }
    }
    //校验采样日期，录单必填项
    if (!$("#ajaxForm #cyrq").val()) {
        return false;
    }
    //校验样本体积，录单必填项
    if (!$("#ajaxForm #ybtj").val()) {
        return false;
    }
    return true;
}


