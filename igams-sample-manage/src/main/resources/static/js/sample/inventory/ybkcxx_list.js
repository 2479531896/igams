var Ybkcxx_turnOff=true;

var Ybkcxx_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#ybkcxx_formSearch #ybkcxx_list").bootstrapTable({
            url: $("#ybkcxx_formSearch #urlPrefix").val()+'/sample/inventory/pageGetListYbkcxx',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#ybkcxx_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "bxh,ctwz,hh,cast(ybkcxx.wz as DECIMAL)",				//排序字段
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
            uniqueId: "ybkcid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '5%',
                align: 'left',
                visible:true
            },{
                field: 'nbbm',
                title: '内部编码',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter:nbbm_formatter
            },{
                field: 'ybbh',
                title: '样本编号',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'yblxmc',
                title: '样本类型',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jcdwmc',
                title: '存储单位',
                width: '15%',
                align: 'left',
                sortable: false,
                visible: true
            },{
                field: 'yblrlxmc',
                title: '样本录入类型',
                width: '15%',
                align: 'left',
                sortable: false,
                visible: true
            },{
                field: 'tj',
                title: '体积',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'bxh',
                title: '冰箱',
                width: '15%',
                align: 'left',
                sortable: true,
                visible:true,
                formatter:bxformater
            },{
                field: 'ctwz',
                title: '抽屉',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'hh',
                title: '盒子',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter:hzformater
            },{
                field: 'wz',
                title: '位置',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'lrrymc',
                title: '录入人员',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'lrsj',
                title: '录入时间',
                width: '10%',
                align: 'left',
                visible: false,
                sortable: true
            },{
                field: 'll',
                title: '领料',
                width: '10%',
                align: 'left',
                formatter:yb_llformat,
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                ybkcxxDealById(row.ybkcid,'view',$("#ybkcxx_formSearch #btn_view").attr("tourl"));
            }
        });
        $("#ybkcxx_formSearch #ybkcxx_list").colResizable({
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
            sortLastName: "lrsj", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return ybkcxxSearchData(map);
    }
    return oTableInit
}

//提供给导出用的回调函数
function YbkcxxSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="ybkcxx.lrsj";
    map["sortLastOrder"]="desc";
    map["sortName"]="sbglbx.sbh";
    map["sortOrder"]="desc";
    return ybkcxxSearchData(map);
}
/**
 * 样本领料车领料格式化
 * @returns
 */
function yb_llformat(value,row,index) {
    if (row.ydbj == '1'){
        return "<div id='yb"+row.ybkcid+"'><span id='t"+row.ybkcid+"' style='color: red' >已预定</span></div>";
    }else if (row.ydbj == '2'){
        return "<div id='yb"+row.ybkcid+"'><span id='t"+row.ybkcid+"' style='color: #7986cb' >处理中</span></div>";
    }else if (row.ydbj == '3'){
        return "<div id='yb"+row.ybkcid+"'><span id='t"+row.ybkcid+"' style='color: #ddd' >销毁</span></div>";
    }
    var idsyb = $("#ybkcxx_formSearch #idsyb").val().split(",");
    if($("#ybkcxx_formSearch #btn_pickingcar").length>0){
        for(var i=0;i<idsyb.length;i++){
            if(row.ybkcid==idsyb[i]){
                return "<div id='yb"+row.ybkcid+"'><span id='t"+row.ybkcid+"' class='btn btn-danger' title='移出领料车' onclick=\"yb_delPickingCar('" + row.ybkcid + "',event)\" >取消</span></div>";
            }
        }
        return "<div id='yb"+row.ybkcid+"'><span id='t"+row.ybkcid+"' class='btn btn-info' title='加入领料车' onclick=\"yb_addPickingCar('" + row.ybkcid + "',event)\" >领料</span></div>";
    }
    return "";
}
function bxformater(value,row,index) {
    return (row.bxwz==null?'':row.bxwz)+(row.bxh==null?'':"("+row.bxh+")");
}
function hzformater(value,row,index) {
    return (row.hzwz==null?'':row.hzwz)+(row.hzbh==null?(row.hh==null?'':"("+row.hh+")"):"("+row.hzbh+")");
}
function nbbm_formatter(value,row,index) {
    var html = "";
    if(row.nbbm==null){
        html = "";
    }else{
        html += "<a href='javascript:void(0);' onclick=\"openByt('" + row.sjid + "',event)\">"+row.nbbm+"</a>";
    }
    return html;
}
function openByt(sjid,event) {
    var url=$("#ybkcxx_formSearch #urlPrefix").val()+"/inspection/inspection/pagedataViewSjxx?sjid="+sjid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'送检详细信息',viewBytConfig);
}
/**
 * 病原体查看页面模态框
 */
var viewBytConfig={
    width		: "800px",
    modalName	:"viewBytModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}
//从领料车中删除
function yb_delPickingCar(ybkcid,event){
    $.ajax({
        type:'post',
        url:$('#ybkcxx_formSearch #urlPrefix').val()+"/sample/inventory/pagedataDelFromPickingCar",
        cache: false,
        data: {"ybkcid":ybkcid,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            //返回值
            if(data.status=='success'){
                $("#idsyb").val(data.idsyb);
                ybpickingCar_addOrDelNum("del");
                $("#ybkcxx_formSearch #t"+ybkcid).remove();
                var html="<span id='t"+ybkcid+"' class='btn btn-info' title='加入领料车' onclick=\"yb_addPickingCar('" + ybkcid + "',event)\" >领料</span>";
                $("#ybkcxx_formSearch #yb"+ybkcid).append(html);
            }
        }
    });
}
//添加至入库车
function yb_addPickingCar(ybkcid,event){
    $.ajax({
        async:false,
        type:'post',
        url:$('#ybkcxx_formSearch #urlPrefix').val()+"/sample/inventory/pagedataAddToPickingCar",
        cache: false,
        data: {"ybkcid":ybkcid,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
          //返回值
          if(data.status=='success'){
              $("#idsyb").val(data.idsyb);
              ybpickingCar_addOrDelNum("add");
              $("#ybkcxx_formSearch #t"+ybkcid).remove();
              var html="<span id='t"+ybkcid+"' class='btn btn-danger' title='移出入库车' onclick=\"yb_delPickingCar('" + ybkcid + "',event)\" >取消</span>";
              $("#ybkcxx_formSearch #yb"+ybkcid).append(html);
          }
        }
    });
}
/**
 * 样本领料车数字加减
 * @param sfbj
 * @returns
 */
function ybpickingCar_addOrDelNum(sfbj){
    if(sfbj=='add'){
        ybll_count=parseInt(ybll_count)+1;
    }else{
        ybll_count=parseInt(ybll_count)-1;
    }
    if((ybll_count==1 && sfbj=='add') || (ybll_count==0 && sfbj=='del')){
        ybll_btnOinit();
    }
    $("#ybll_num").text(ybll_count);
}
//初始化领料车上的数字
var ybll_count=0;
function ybll_btnOinit(){
    if(ybll_count>0){
        var strnum=ybll_count;
        if(ybll_count>99){
            strnum='99+';
        }
        var html="";
        html+="<span id='ybll_num' style='position:absolute;background-color:red;color:white;border-radius: 15px;border:0px;width:20px;height:20px;top:-10px;font-size:10px;line-height:150%;'>"+strnum+"</span>";
        $("#ybkcxx_formSearch #btn_pickingcar").append(html);
    }else{
        $("#ybkcxx_formSearch #ybll_num").remove();
    }
}
function ybkcxxSearchData(map) {
    var cxtj = $("#ybkcxx_formSearch #cxtj").val();
    var cxnr = $.trim(jQuery('#ybkcxx_formSearch #cxnr').val());
    if (cxtj == "0") {
        map["entire"] = cxnr
    } else if (cxtj == "1") {
        map["nbbm"] = cxnr
    }else if (cxtj == "2") {
        map["ybbh"] = cxnr
    } else if (cxtj == "3") {
        map["lrrymc"] = cxnr
    }else if (cxtj == "4") {
        map["gzzb"] = cxnr
    }else if (cxtj == "5") {
        map["yszb"] = cxnr
    }
    // 冰箱s
    var bxhs = jQuery('#ybkcxx_formSearch #bx_id_tj').val();
    map["bxhs"] = bxhs;
    // 抽屉s
    var cths = jQuery('#ybkcxx_formSearch #ct_id_tj').val();
    map["cths"] = cths;
    // 盒子s
    var hhs = jQuery('#ybkcxx_formSearch #hz_id_tj').val();
    map["hhs"] = hhs;
    // 存储单位
    var jcdws = jQuery('#ybkcxx_formSearch #jcdw_id_tj').val();
    map["jcdws"] = jcdws.replace(/'/g, "");
    // 样本类型
    var yblxs = jQuery('#ybkcxx_formSearch #yblx_id_tj').val();
    map["yblxs"] = yblxs.replace(/'/g, "");
    // 样本录入类型
    var yblrlxs = jQuery('#ybkcxx_formSearch #yblrlx_id_tj').val();
    map["yblrlxs"] = yblrlxs.replace(/'/g, "");
    // 发展开始日期
    var lrsjstart = jQuery('#ybkcxx_formSearch #lrsjstart').val();
    map["lrsjstart"] = lrsjstart;
    // 发展结束日期
    var lrsjend = jQuery('#ybkcxx_formSearch #lrsjend').val();
    map["lrsjend"] = lrsjend;
    var volumeMin = jQuery('#ybkcxx_formSearch #volumeMin').val();
    map["volumeMin"] = volumeMin;
    // 发展结束日期
    var volumeMax = jQuery('#ybkcxx_formSearch #volumeMax').val();
    map["volumeMax"] = volumeMax;
    //库存状态
    var ybkczts = jQuery('#ybkcxx_formSearch #ybkczt_id_tj').val();
    map["ybkczts"] = ybkczts.replace(/'/g, "");
    return map;
}
/**
 * 根据冰箱获取抽屉
 * @returns
 */
function getAllBx(){
    var jcdw = jQuery('#ybkcxx_formSearch #jcdw_id_tj').val().replace(/'/g, "");
    if(!isEmpty(jcdw)){
        $("#ybkcxx_formSearch #bx_id").removeClass("hidden");
    }else{
        $("#ybkcxx_formSearch #bx_id").addClass("hidden");
    }
    if(!isEmpty(jcdw)){
        var url = $("#ybkcxx_formSearch #urlPrefix").val()+"/sample/device/pagedataSbListByJcdw";
        $.ajax({
            type:'post',
            url:url,
            data:{"jcdws":jcdw,"access_token":$("#ac_tk").val()},
            dataType:'JSON',
            success:function(data){
                if(data.length>0){
                    var html="";
                    $.each(data,function(i){
                        html +="<li>";
                        html += "<a style='text-decoration:none;cursor:pointer;'  onclick=\"addTj('bx','" + data[i].sbid + "','ybkcxx_formSearch');getAllct('" + data[i].sbid + "');\" id=\"bx_id_" + data[i].sbid + "\"><span>" + (data[i].wz==null?'':data[i].wz)+(data[i].sbh==null?'':"("+data[i].sbh+")") + "</span></a>";
                        html +="</li>"
                    });
                    $("#ybkcxx_formSearch #ul_bx").html(html);
                }else{
                    $("#ybkcxx_formSearch #ul_bx").empty();
                }
                $("#ybkcxx_formSearch #bx_id_tj").val("");
            }
        });
    }else{
        $("#ybkcxx_formSearch [id^='bx_li_']").remove();
        $("#ybkcxx_formSearch #bx_id_tj").val("");
    }
}
/**
 * 根据冰箱获取抽屉
 * @returns
 */
function getAllct(){
    var bx=$("#ybkcxx_formSearch #bx_id_tj").val();
    if(!isEmpty(bx)){
        $("#ybkcxx_formSearch #ct_id").removeClass("hidden");
    }else{
        $("#ybkcxx_formSearch #ct_id").addClass("hidden");
    }
    if(!isEmpty(bx)){
        var url = $("#ybkcxx_formSearch #urlPrefix").val()+"/sample/device/pagedataSbListByFsbid";
        $.ajax({
            type:'post',
            url:url,
            data:{"fids":bx,"access_token":$("#ac_tk").val()},
            dataType:'JSON',
            success:function(data){
                if(data.length>0){
                    var html="";
                    $.each(data,function(i){
                        html +="<li>";
                        html += "<a style='text-decoration:none;cursor:pointer;'  onclick=\"addTj('ct','" + data[i].sbid + "','ybkcxx_formSearch');getAllhz('" + data[i].sbid + "');\" id=\"ct_id_" + data[i].sbid + "\"><span>" + data[i].wz + "</span></a>";
                        html +="</li>"
                    });
                    $("#ybkcxx_formSearch #ul_ct").html(html);
                }else{
                    $("#ybkcxx_formSearch #ul_ct").empty();
                }
                $("#ybkcxx_formSearch #ct_id_tj").val("");
            }
        });
    }else{
        $("#ybkcxx_formSearch [id^='ct_li_']").remove();
        $("#ybkcxx_formSearch #ct_id_tj").val("");
    }
}
/**
 * 根据抽屉获取盒子
 * @returns
 */
function getAllhz(ct) {
    setTimeout(function () {
        gethzList(ct);
    }, 10);
}
function gethzList(ct) {
    ct=$("#ybkcxx_formSearch #ct_id_tj").val();
    if(!isEmpty(ct)){
        $("#ybkcxx_formSearch #hz_id").removeClass("hidden");
    }else{
        $("#ybkcxx_formSearch #hz_id").addClass("hidden");
    }
    if(!isEmpty(ct)){
        var url = $("#ybkcxx_formSearch #urlPrefix").val()+"/sample/device/pagedataSbListByFsbid";
        $.ajax({
            type:'post',
            url:url,
            data:{"fids":ct,"access_token":$("#ac_tk").val()},
            dataType:'JSON',
            success:function(data){
                if(data.length>0){
                    var html="";
                    $.each(data,function(i){
                        html +="<li>";
                        html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('hz','" + data[i].sbid + "','ybkcxx_formSearch');\" id=\"hz_id_" + data[i].sbid + "\"><span>" + (data[i].wz==null?'':data[i].wz)+(data[i].sbh==null?'':"("+data[i].sbh+")") + "</span></a>";
                        html +="</li>"
                    });
                    $("#ybkcxx_formSearch #ul_hz").html(html);
                }else{
                    $("#ybkcxx_formSearch #ul_hz").empty();
                }
                $("#ybkcxx_formSearch #hz_id_tj").val("");
            }
        });
    }else{
        $("#ybkcxx_formSearch [id^='hz_li_']").remove();
        $("#ybkcxx_formSearch #hz_id_tj").val("");
    }
}


function ybkcxxDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#ybkcxx_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?ybkcid=" +id;
        $.showDialog(url,'详细信息',viewYbkcxxConfig);
    }else if(action == 'ybadd'){
        var url= tourl;
        $.showDialog(url,'样本录入',addYbkcxxConfig);
    }else if(action == 'mod'){
        var url= tourl + "?ybkcid=" +id;
        $.showDialog(url,'样本库存修改',modYbkcxxConfig);
    }else if(action=='pickingcar'){
        var url=tourl;
        $.showDialog(url,'领料车',ybpickingConfig);
    }else if (action=='stockview'){
        var url=tourl;
        $.showDialog(url,'库存查看',stockviewYbkcxxConfig);
    }else if (action=='modbox'){
        var url=tourl;
        $.showDialog(url,'修改盒子',modBoxYbkcxxConfig);
    }
}

/**
 * 查看页面模态框
 */
var viewYbkcxxConfig={
    width		: "1400px",
    modalName	:"viewYbkcxxModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}

/**
 * 库存查看页面模态框
 */
var stockviewYbkcxxConfig={
    width		: "800px",
    modalName	:"stockviewYbkcxxModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}
var addYbkcxxConfig = {
    width		: "1200px",
    modalName	: "addYbkcxxModal",
    formName	: "yblrForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#yblrForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                if(t_map.rows != null && t_map.rows.length > 0) {
                    for (let i = 0; i < t_map.rows.length; i++) {
                        //是否重复
                        for (let y = 0; y < t_map.rows.length; y++) {
                            if ( t_map.rows[i].wz == t_map.rows[y].wz&&i!=y) {
                                $.error("第"+ (i+1) + "行和第"+(y+1)+"行重复");
                                return false;
                            }
                        }
                    }
                }else {
                    $.error("明细不允许为空！");
                    return false;
                }
                if(t_map.rows != null && t_map.rows.length > 0) {
                    for (let i = 0; i < t_map.rows.length; i++) {
                        for (var y = 0; y < cfqwzList.length; y++) {
                            if (t_map.rows[i].wz == cfqwzList[i]){
                                $.error("第"+ (i+1) + "行和盒子里重复");
                                return false;
                            }
                        }
                    }
                }
                let json = [];
                if(t_map.rows != null && t_map.rows.length > 0) {
                    for (let i = 0; i < t_map.rows.length; i++) {
                        let sz = {"ybbh": '', "yblx": '', "tj": '', "wz": '', "sjid": '', "yblxmc": '', "yblrlx": ''};
                        sz.ybbh = t_map.rows[i].ybbh;
                        sz.yblx = t_map.rows[i].yblx;
                        sz.tj = t_map.rows[i].tj;
                        sz.wz = t_map.rows[i].wz;
                        sz.sjid = t_map.rows[i].sjid;
                        sz.yblxmc = t_map.rows[i].yblxmc;
                        sz.yblrlx = t_map.rows[i].yblrlx;
                        json.push(sz);
                    }
                }
                $("#yblrForm #ybmx_json").val(JSON.stringify(json));
                var $this = this;
                var opts = $this["options"]||{};
                $('#yblrForm #bxid').attr("disabled", false);
                $('#yblrForm #bxid').trigger("chosen:updated");
                $('#yblrForm #chtid').attr("disabled", false);
                $('#yblrForm #chtid').trigger("chosen:updated");
                $('#yblrForm #hzid').attr("disabled", false);
                $('#yblrForm #hzid').trigger("chosen:updated");
                $('#yblrForm #jcdw').attr("disabled", false);
                $('#yblrForm #jcdw').trigger("chosen:updated");
                $("#yblrForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"yblrForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchYbkcxxResult (true);
                            }
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                            if(opts.offAtOnce&&responseText["message"]=="该盒子已被其他人使用，请关闭后重新录入!"){
                                $.closeModal(opts.modalName);
                                searchYbkcxxResult (true);
                            }
                        });
                    } else{
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"],function() {
                            $('#yblrForm #bxid').attr("disabled", true);
                            $('#yblrForm #bxid').trigger("chosen:updated");
                            $('#yblrForm #chtid').attr("disabled", true);
                            $('#yblrForm #chtid').trigger("chosen:updated");
                            $('#yblrForm #hzid').attr("disabled", true);
                            $('#yblrForm #hzid').trigger("chosen:updated");
                            $('#yblrForm #jcdw').attr("disabled", true);
                            $('#yblrForm #jcdw').trigger("chosen:updated");
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
var modYbkcxxConfig = {
    width		: "1200px",
    modalName	: "addYbkcxxModal",
    formName	: "yblrForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#yblrForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                // debugger
                // if (cfqwzList.length>0&&cfqwzList != null){
                //     if (cfqwzList.length>80){
                //         $.error("盒子已满！");
                //         return false;
                //     }
                //     for (var y = 0; y < cfqwzList.length; y++) {
                //         if (t_map.rows[0].wz == cfqwzList[y]){
                //             $.error("该位置和盒子里重复");
                //             return false;
                //         }
                //     }
                // }
                var sfczdb = $("#yblrForm #hzid option:selected").attr("sfczdb");
                if ("1"==sfczdb){
                    $.confirm("这个盒子正在做调出处理，不允许修改!");
                    return false;
                }
                let json = [];
                if(t_map.rows != null && t_map.rows.length > 0) {
                    for (let i = 0; i < t_map.rows.length; i++) {
                        let sz = {"ybbh": '', "yblx": '', "tj": '', "wz": '', "sjid": '', "yblxmc": '', "yblrlx": '',"bz":''};
                        sz.ybbh = t_map.rows[i].ybbh;
                        sz.yblx = t_map.rows[i].yblx;
                        sz.tj = t_map.rows[i].tj;
                        sz.wz = t_map.rows[i].wz;
                        sz.sjid = t_map.rows[i].sjid;
                        sz.yblxmc = t_map.rows[i].yblxmc;
                        sz.yblrlx = t_map.rows[i].yblrlx;
                        sz.bz = t_map.rows[i].bz;
                        json.push(sz);
                    }
                }
                $("#yblrForm #ybmx_json").val(JSON.stringify(json));
                var $this = this;
                var opts = $this["options"]||{};

                $("#yblrForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"yblrForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchYbkcxxResult (true);
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
var modBoxYbkcxxConfig = {
    width		: "1200px",
    modalName	: "modBoxYbkcxxModal",
    formName	: "ybModBoxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#ybModBoxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                if ($("#ybModBoxForm #dchzid").val()==$("#ybModBoxForm #drhzid").val()){
                    $.alert("调出盒子和调入盒子不允许相同！");
                    return false;
                }
                var dcycfs = $("#ybModBoxForm #dchzid").find("option:selected").attr("ycfs");
                if (dcycfs=='0'){
                    $.alert("这个调出盒子是空的，请选择其他盒子！");
                    return false;
                }
                var drycfs = $("#ybModBoxForm #drhzid").find("option:selected").attr("ycfs");
                if (drycfs!='0'){
                    $.alert("调入盒子存在样本，请选择调入其他盒子");
                    return false;
                }
                $("#ybModBoxForm #dcycfs").val(dcycfs)
                $("#ybModBoxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"ybModBoxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchYbkcxxResult (true);
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
var ybpickingConfig = {
    width		: "1500px",
    modalName	: "ybpickingModal",
    formName	: "ybpickingCarForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "保 存",
            className : "btn-success",
            callback : function() {
                if(!$("#ybpickingCarForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                let json = [];
                var jcdwdm="";
                if(t_map.rows != null && t_map.rows.length > 0) {
                    var checkJcdw=t_map.rows[0].jcdwmc;
                    jcdwdm=t_map.rows[0].jcdwdm;
                    for (let i = 0; i < t_map.rows.length; i++) {
                        if(checkJcdw!=t_map.rows[i].jcdwmc){
                            $.error("明细需选择存储单位一致的数据！");
                            return false;
                        }
                        let sz = {"ybkcid": ''};
                        sz.ybkcid = t_map.rows[i].ybkcid;
                        json.push(sz);
                    }
                }else {
                    $.error("明细不允许为空！");
                    return false;
                }
                $("#ybpickingCarForm input[name='access_token']").val($("#ac_tk").val());
                $("#ybpickingCarForm #ybllmx_json").val(JSON.stringify(json));

                submitForm(opts["formName"]||"ybpickingCarForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        $.success(responseText["message"],function() {
                            //提交审核
                            if(responseText["auditType"]!=null){
                                var ybpickingCar_params=[];
                                ybpickingCar_params.prefix=responseText["urlPrefix"];
                                var extend={"mrsz": "01"};
                                if(jcdwdm&&"0Y"==jcdwdm){
                                    extend={"extend_2": "0Y"};
                                }
                                showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                    $.closeModal(opts.modalName);
                                    searchYbkcxxResult();
                                },null,ybpickingCar_params,extend);
                            }else{
                                searchYbkcxxResult();
                            }
                        });
                        $("#ybkcxx_formSearch #ybll_num").remove();
                        $("#ybkcxx_formSearch #idsyb").val("");
                        searchYbkcxxResult();
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


var Ybkcxx_ButtonInit = function () {
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        var btn_query = $("#ybkcxx_formSearch #btn_query");
        var bxBind = $("#ybkcxx_formSearch #bx_id ul li a");
        var jcdwBind = $("#ybkcxx_formSearch #jcdw_id ul li a");
        var btn_view = $("#ybkcxx_formSearch #btn_view");
        var btn_ybadd = $("#ybkcxx_formSearch #btn_ybadd");
        var btn_mod = $("#ybkcxx_formSearch #btn_mod");
        var btn_searchexport = $("#ybkcxx_formSearch #btn_searchexport");
        var btn_selectexport = $("#ybkcxx_formSearch #btn_selectexport");
        var btn_pickingcar = $("#ybkcxx_formSearch #btn_pickingcar");
        var btn_stockview =$("#ybkcxx_formSearch #btn_stockview")
        var btn_modbox =$("#ybkcxx_formSearch #btn_modbox")
        //添加日期控件
        laydate.render({
            elem: '#ybkcxx_formSearch #lrsjstart'
            , theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#ybkcxx_formSearch #lrsjend'
            , theme: '#2381E9'
        });



        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#ybkcxx_formSearch #ybkcxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                ybkcxxDealById(sel_row[0].ybkcid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------库存查看-----------------------------------*/
        btn_stockview.unbind("click").click(function(){
                ybkcxxDealById("","stockview",btn_stockview.attr("tourl"));
        });
        /* ---------------------------模糊查询-----------------------------------*/
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchYbkcxxResult(true);
            });
        }
        /*-----------------------高级筛选------------------------------------*/
        $("#ybkcxx_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(Ybkcxx_turnOff){
                $("#ybkcxx_formSearch #searchMore").slideDown("low");
                Ybkcxx_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#ybkcxx_formSearch #searchMore").slideUp("low");
                Ybkcxx_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
        /* ------------------------------样本录入-----------------------------*/
        btn_ybadd.unbind("click").click(function(){
            ybkcxxDealById(null,"ybadd",btn_ybadd.attr("tourl"));
        });
        /*-----------------------领料车--------------------------------------*/
        btn_pickingcar.unbind("click").click(function(){
            ybkcxxDealById(null,"pickingcar",btn_pickingcar.attr("tourl"));
        });
        /*-----------------------修改盒子--------------------------------------*/
        btn_modbox.unbind("click").click(function(){
            ybkcxxDealById(null,"modbox",btn_modbox.attr("tourl"));
        });
        /*-----------------------样本库存修改--------------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#ybkcxx_formSearch #ybkcxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                if (sel_row[0].ydbj=='0') {
                    ybkcxxDealById(sel_row[0].ybkcid, "mod", btn_mod.attr("tourl"));
                }else{
                    $.error("已预定样本不允许修改！")
                    return;
                }
            }else{
                $.error("请选中一行");
            }
        });
        //---------------------------导出--------------------------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#ybkcxx_formSearch #ybkcxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].ybkcid;
                }
                ids = ids.substr(1);
                $.showDialog($('#ybkcxx_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=YBKCXX_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });

        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#ybkcxx_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=YBKCXX_SEARCH&expType=search&callbackJs=YbkcxxSearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
        /*------------------------抽屉的显示----------------------------------*/
        if(jcdwBind!=null){
            jcdwBind.on("click",function(){
                setTimeout(function(){
                    getAllBx();
                },10);
            });
        }
        if(bxBind!=null){
            bxBind.on("click",function(){
                setTimeout(function(){
                    getAllct();
                },10);
            });
        }

    };
    return oInit;
};


function searchYbkcxxResult(isTurnBack) {
    //关闭高级搜索条件
    $("#ybkcxx_formSearch #searchMore").slideUp("low");
    Ybkcxx_turnOff=true;
    $("#ybkcxx_formSearch #sl_searchMore").html("高级筛选");
    if (isTurnBack) {
        $('#ybkcxx_formSearch #ybkcxx_list').bootstrapTable('refresh', {pageNumber: 1});
    } else {
        $('#ybkcxx_formSearch #ybkcxx_list').bootstrapTable('refresh');
    }
}
$(function(){
    //0.界面初始化
    // 1.初始化Table
    var oTable = new Ybkcxx_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new Ybkcxx_ButtonInit();
    oButtonInit.Init();

    if($("#ybllzsl").val()==null || $("#ybllzsl").val()==''){
        ybll_count=0;
    }else{
        ybll_count=$("#ybllzsl").val();
    }
    ybll_btnOinit();

    // 所有下拉框添加choose样式
    jQuery('#ybkcxx_formSearch .chosen-select').chosen({width: '100%'});
    $("#ybkcxx_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});
