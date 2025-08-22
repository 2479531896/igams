var t_map = [];
var wz = 1;
var wzList = [];
var scwzList = [];
var cfqwzList = [];
var xgqwz = '';
// 显示字段
var columnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '3%',
        align: 'left',
        visible:true
    },{
        field: 'nbbm',
        title: '内部编码',
        titleTooltip:'内部编码',
        formatter: nbbm_formatter,
        width: '12%',
        align: 'left',
        visible: true
    },{
        field: 'ybbh',
        title: '样本编号',
        titleTooltip:'样本编号',
        width: '12%',
        align: 'left',
        visible: true
    },{
        field: 'yblxmc',
        title: '样本类型',
        titleTooltip:'样本类型',
        width: '10%',
        align: 'left',
        visible: true
    },{
        field: 'yblrlxmc',
        title: '样本录入类型',
        titleTooltip:'样本录入类型',
        width: '10%',
        align: 'left',
        formatter: yblrlxformat,
        visible: true
    }, {
        field: 'tj',
        title: '体积',
        titleTooltip:'体积',
        width: '8%',
        align: 'left',
        formatter:tjformat,
        visible: true
    }, {
        field: 'wz',
        title: '位置',
        titleTooltip:'位置',
        width: '11%',
        align: 'left',
        formatter:wzformat,
        visible: true
    },{
        field: 'cz',
        title: '操作/备注',
        titleTooltip:'操作/备注',
        width: '6%',
        align: 'left',
        formatter:czformat,
        visible: true
    }];
var ybkcxxEdit_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        var ybkcid = $('#yblrForm #ybkcid').val();
        $('#yblrForm #ybmx_list').bootstrapTable({
            url: $('#yblrForm #urlPrefix').val()+'/sample/inventory/pagedataYbkcxxlist?ybkcid='+ ybkcid,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#yblrForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "bxh,cth,hh,wz",				        //排序字段
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
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "nbbm",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
                t_map = map;
                if(t_map.rows!=null){
                    // 初始化ybmx_json
                    var json = [];
                    for (var i = 0; i < t_map.rows.length; i++) {
                        var sz = {"index":'',"nbbm":'',"ybbh":'',"yblx":'',"tj":'',"wz":'',"cz":'',"sjid":'',"yblxmc":'',"yblrlx":''};
                        sz.index=t_map.rows.length;
                        sz.nbbm = t_map.rows[i].nbbm;
                        sz.ybbh = t_map.rows[i].ybbh;
                        sz.yblx = t_map.rows[i].yblx;
                        sz.tj = t_map.rows[i].tj;
                        sz.wz = t_map.rows[i].wz;
                        sz.cz = t_map.rows[i].cz;
                        sz.sjid = t_map.rows[i].sjid;
                        sz.yblxmc = t_map.rows[i].yblxmc;
                        sz.yblrlx = t_map.rows[i].yblrlx;
                        json.push(sz);
                    }
                    $("#yblrForm #ybmx_json").val(JSON.stringify(json));
                }
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                return;
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
            pageSize: 1,   //页面大小
            pageNumber: 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "bxh", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};
document.onkeydown=function(e){
    if(e.keyCode==123)
        return false;
};
//扫描枪扫描后触发
function getYbbhAndYbbmAndWz(){
    var hzid = document.getElementById("hzid").value;
    if (hzid==null||hzid==''){
        $.confirm("请选择盒子!");
        return false;
    }
    var xgsj = $("#yblrForm #"+hzid).attr("xgsj");
    if (xgsj=='null'||xgsj == null||xgsj==undefined){
        xgsj = '';
    }
    $("#yblrForm #hzxgsj").val(xgsj);

    var sfczdb = $("#yblrForm  #"+hzid).attr("sfczdb");
    if("1"==sfczdb){
        $.confirm("这个盒子正在做调出处理，不允许录入样本!");
        return false;
    }
    // 获取扫描枪扫描后的sjid
    var lrnbbm =$('#yblrForm #lrnbbm').val();
    //判断新增输入框是否有内容
    if(!$('#yblrForm #lrnbbm').val()){
        return false;
    }
    if (wzList.length>=81) {
        $.confirm("该盒子已放满，请保存!");
        return false;
    }else {
        $.ajax({
            type:'post',
            url:$('#yblrForm #urlPrefix').val()+"/sample/inventory/pagedataYbkcxx",
            cache: false,
            data: {"nbbm":lrnbbm,"yblrlx":$('#yblrForm #yblrlx').val(),"access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(data){
                //返回值
                if(data!=null && data!=''){
                    $('#yblrForm #bxid').attr("disabled", true);
                    $('#yblrForm #bxid').trigger("chosen:updated");
                    $('#yblrForm #chtid').attr("disabled", true);
                    $('#yblrForm #chtid').trigger("chosen:updated");
                    $('#yblrForm #hzid').attr("disabled", true);
                    $('#yblrForm #hzid').trigger("chosen:updated");
                    $('#yblrForm #jcdw').attr("disabled", true);
                    $('#yblrForm #jcdw').trigger("chosen:updated");
                    if(t_map.rows ==null){
                        t_map.rows=[];
                    }
                    t_map.rows.push(data);
                    var size =  t_map.rows.length-1;
                    if (scwzList!=null && scwzList!=''){
                    scwzList = Array.from(new Set(scwzList));
                    for (var i = 0; i < wzList.length; i++) {
                        for (var j = 0; j < scwzList.length; j++) {
                            if (wzList[i] == scwzList[j]){
                                scwzList.splice(j,1);
                            }
                        }
                    }}
                    if (scwzList!=null && scwzList!=''){
                        t_map.rows[size].wz = scwzList[0];
                        wzList.push(scwzList[0]);
                        wzList.sort(function compare(value1,value2){
                            //升序
                            return value1 - value2
                        });
                        scwzList.splice(0,1)
                    }else {
                        if (wzList == null || wzList== ''){
                            t_map.rows[size].wz = wz;
                            wzList.push(wz)
                            wz++;
                        }else {
                            for (var i = 0; i < wzList.length; i++) {
                                if (wz==wzList[i]){
                                    wz = wz+1;
                                }
                            }
                            wzList.push(wz);
                            t_map.rows[size].wz = wz;
                            wz++;
                        }
                    }
                    $("#yblrForm #ybmx_list").bootstrapTable('load',t_map);
                    document.getElementById("lrnbbm").value="";
                } else{
                    $.confirm("样本信息不存在!");
                    $("#yblrForm #lrnbbm").empty();
                }
            }
        });
    }
}


/**
 * 位置格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wzformat(value,row,index){
    if(row.wz == null){
        row.wz = "";
    }
    var html = "<input id='wz_"+index+"' value='"+row.wz+"' name='wz_"+index+"' onkeyup=\"if(!/^[0-9]+$/.test(value)) value=value.replace(/\\D/g,'');if(value>81)value=81;if(value<1)value=null\"  style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' placeholder='请输入1~81的正整数'  validate='{required:true}' onfocus='xgwz(value)' onblur= \"checkSum('"+index+"',this,\'wz\')\"/>";
    return html;
}
function xgwz(wz) {
    xgqwz = wz;
}
//内部编码
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
 * 体积格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function tjformat(value,row,index){
    if(row.tj == null){
        row.tj = "";
    }
    var html = "<input id='tj_"+index+"' value='"+row.tj+"' name='tj_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{required:true}' onblur=\"checkSum('"+index+"',this,\'tj\')\"/>";
    return html;
}
/**
 * 样本录入类型格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function yblrlxformat(value,row,index){
    var yblrlxlist = $("#yblrForm #yblrlxlist").val();
    var yblrlist = JSON.parse(yblrlxlist);
    let yblr = "";
    if (row.yblrlx){
        yblr = row.yblrlx;
    }
    var html="";
    html += "<select id='yblrlx_"+index+"' name='yblrlx_"+index+"' class='form-control chosen-select' validate='{required:true}' onchange=\"checkSum('"+index+"',this,\'yblrlx\')\">";
    html += "<option value=''>--请选择--</option>";
    for(var i = 0; i < yblrlist.length; i++) {
        html += "<option id='"+yblrlist[i].csid+"' value='"+yblrlist[i].csid+"'";
        if(yblr!=null && yblr!=""){
            if(yblr==yblrlist[i].csid){
                html += "selected"
            }
        }
        html += ">"+yblrlist[i].csmc+"</option>";
    }
    html +="</select></div>";
    return html;
}

/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
    var formAction = $("#yblrForm #formAction").val();
    if (formAction == 'ybaddSaveYbkcxx'){
        var html = "<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除' onclick=\"delYbkcxxDetail('" + index + "','"+row.wz+"',event)\" >删除</span>";
        return html;
    }else {
        if (row.bz ==null){
            row.bz = '';
        }
        var html = "<input id='bz_"+index+"' value='"+row.bz+"' name='bz_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' title='"+row.bz+"' validate='{stringMaxLength:512}' onblur=\"checkSum('"+index+"',this,\'bz\')\"/>";
        return html;
    }
}
/**
 * 删除操作(从样本明细删除)
 * @param index
 * @param event
 * @returns
 */
function delYbkcxxDetail(index,wz,event){
    scwzList.push(wz);
    scwzList.sort(function compare(value1,value2){
        //升序
        return value1 - value2
    });
    t_map.rows.splice(index,1);
    wzList.splice(wzList.indexOf(wz),1);
    $("#yblrForm #ybmx_list").bootstrapTable('load',t_map);
}
/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkSum(index, e, zdmc) {
    if(t_map.rows.length > index){
        if (zdmc == 'wz') {
            var ewz = e.value;
            //赋值
            t_map.rows[index].wz = ewz;
            //校验
            if (xgqwz!=ewz){
                for (var i = 0; i < wzList.length; i++) {
                    if (wzList[i] == xgqwz){
                        wzList.splice(i,1)
                    }
                }
                scwzList.push(xgqwz);
                scwzList.sort(function compare(value1,value2){
                    //升序
                    return value1 - value2
                });
                for (var i = 0; i < cfqwzList.length; i++) {
                    if (cfqwzList[i]==ewz){
                        $.confirm("该位置已被占用！");
                        return false;
                    }
                }
                for (var i = 0; i < t_map.rows.length; i++) {
                    if (t_map.rows[i].wz == ewz&&index!=i){
                        $.confirm("该位置已被占用！");
                        return false;
                    }
                }
            }
            var flag = true;
            for (var i = 0; i < wzList.length; i++) {
                if (wzList[i]==ewz){
                    //如果wzList里有
                    flag = false;
                }
            }
            //如果wzList里有不放入，无则放入
            if (flag){
                wzList.push(ewz);
                wzList.sort(function compare(value1,value2){
                    //升序
                    return value1 - value2
                });
            }
        } else if (zdmc == 'tj') {
            t_map.rows[index].tj = e.value;
        } else if (zdmc == 'yblrlx') {
            t_map.rows[index].yblrlx = e.value;
        }else if (zdmc == 'bz') {
            t_map.rows[index].bz = e.value;
        }
    }
}
/*获取冰箱list*/
function getBxList() {
    var jcdw = document.getElementById("jcdw").value;
    $.ajax({
        type: "post",
        url: $("#urlPrefix").val()+"/sample/device/pagedataSbListByJcdw",
        data: {"jcdws":jcdw,"access_token":$("#ac_tk").val()},
        dataType: "json",
        success: function (data) {
            if(data != null && data.length != 0){
                var zHtml = "";
                $.each(data,function(i){
                    zHtml += "<option value='" + data[i].sbid + "' id='"+data[i].sbid+"'>" + (data[i].wz==null?'':data[i].wz)+(data[i].sbh==null?'':'('+data[i].sbh+')') + "</option>";
                });
                $("#yblrForm #bxid").empty();
                $("#yblrForm #bxid").append(zHtml);
                $("#yblrForm #bxid").trigger("chosen:updated");
                var bxid = document.getElementById("bxid").value;
                $.ajax({
                    type: "post",
                    url: $("#urlPrefix").val()+"/sample/device/pagedataSbListByFsbid",
                    data: {"fids":bxid,"access_token":$("#ac_tk").val()},
                    dataType: "json",
                    success: function (data) {
                        if(data != null && data.length != 0){
                            var zHtml = "";
                            $.each(data,function(i){
                                zHtml += "<option value='" + data[i].sbid + "' id='"+data[i].sbid+"'>" + data[i].wz + "</option>";
                            });
                            $("#yblrForm #chtid").empty();
                            $("#yblrForm #chtid").append(zHtml);
                            $("#yblrForm #chtid").trigger("chosen:updated");
                            var chtid = document.getElementById("chtid").value;
                            /*获取盒子list*/
                            $.ajax({
                                type: "post",
                                url: $("#urlPrefix").val()+"/sample/device/pagedataSbListByFsbid",
                                data: {"fids":chtid,"access_token":$("#ac_tk").val()},
                                dataType: "json",
                                success: function (data) {
                                    if(data != null && data.length != 0){
                                        var zHtml = "";
                                        $.each(data,function(i){
                                            zHtml += "<option sbh= '"+ data[i].sbh+"' xgsj= '"+ data[i].xgsj+"' sfczdb= '"+ data[i].sfczdb+"' value='" + data[i].sbid + "' id='"+data[i].sbid+"'>" + (data[i].wz==null?'':data[i].wz)+(data[i].sbh==null?'':"("+data[i].sbh+")") + "</option>";
                                        });
                                        $("#yblrForm #hzid").empty();
                                        $("#yblrForm #hzid").append(zHtml);
                                        $("#yblrForm #hzid").trigger("chosen:updated");
                                        getwzList();
                                    }else{
                                        var zHtml = "";
                                        zHtml += "<option value=''>--该抽屉下没有盒子--</option>";
                                        $("#yblrForm #hzid").empty();
                                        $("#yblrForm #hzid").append(zHtml);
                                        $("#yblrForm #hzid").trigger("chosen:updated");
                                        $("#yblrForm #hh").val('')
                                    }
                                }
                            })
                        }else{
                            var zHtml = "";
                            zHtml += "<option value=''>--该冰箱下没有抽屉--</option>";
                            $("#yblrForm #chtid").empty();
                            $("#yblrForm #chtid").append(zHtml);
                            $("#yblrForm #chtid").trigger("chosen:updated");
                            var sHtml = "";
                            sHtml += "<option value=''>--该抽屉下没有盒子--</option>";
                            $("#yblrForm #hzid").empty();
                            $("#yblrForm #hzid").append(sHtml);
                            $("#yblrForm #hzid").trigger("chosen:updated");
                            $("#yblrForm #hh").val('')
                        }
                    }
                })
            }else{
                var bxHtml = "";
                bxHtml += "<option value=''>--该存储单位下没有冰箱--</option>";
                $("#yblrForm #bxid").empty();
                $("#yblrForm #bxid").append(bxHtml);
                $("#yblrForm #bxid").trigger("chosen:updated");
                zHtml += "<option value=''>--该冰箱下没有抽屉--</option>";
                $("#yblrForm #chtid").empty();
                $("#yblrForm #chtid").append(zHtml);
                $("#yblrForm #chtid").trigger("chosen:updated");
                var sHtml = "";
                sHtml += "<option value=''>--该抽屉下没有盒子--</option>";
                $("#yblrForm #hzid").empty();
                $("#yblrForm #hzid").append(sHtml);
                $("#yblrForm #hzid").trigger("chosen:updated");
                $("#yblrForm #hh").val('')
            }
        }
    })
}
/*获取抽屉list*/
function getCtList() {
    var bxid = document.getElementById("bxid").value;
    $.ajax({
        type: "post",
        url: $("#urlPrefix").val()+"/sample/device/pagedataSbListByFsbid",
        data: {"fids":bxid,"access_token":$("#ac_tk").val()},
        dataType: "json",
        success: function (data) {
            if(data != null && data.length != 0){
                var zHtml = "";
                $.each(data,function(i){
                    zHtml += "<option value='" + data[i].sbid + "' id='"+data[i].sbid+"'>" + data[i].wz + "</option>";
                });
                $("#yblrForm #chtid").empty();
                $("#yblrForm #chtid").append(zHtml);
                $("#yblrForm #chtid").trigger("chosen:updated");
                var chtid = document.getElementById("chtid").value;
                /*获取盒子list*/
                $.ajax({
                    type: "post",
                    url: $("#urlPrefix").val()+"/sample/device/pagedataSbListByFsbid",
                    data: {"fids":chtid,"access_token":$("#ac_tk").val()},
                    dataType: "json",
                    success: function (data) {
                        if(data != null && data.length != 0){
                            var zHtml = "";
                            $.each(data,function(i){
                                zHtml += "<option sbh= '"+ data[i].sbh+"' xgsj= '"+ data[i].xgsj+"' sfczdb= '"+ data[i].sfczdb+"' value='" + data[i].sbid + "' id='"+data[i].sbid+"'>" + (data[i].wz==null?'':data[i].wz)+(data[i].sbh==null?'':'('+data[i].sbh+')') + "</option>";
                            });
                            $("#yblrForm #hzid").empty();
                            $("#yblrForm #hzid").append(zHtml);
                            $("#yblrForm #hzid").trigger("chosen:updated");
                            getwzList();
                        }else{
                            var zHtml = "";
                            zHtml += "<option value=''>--该抽屉下没有盒子--</option>";
                            $("#yblrForm #hzid").empty();
                            $("#yblrForm #hzid").append(zHtml);
                            $("#yblrForm #hzid").trigger("chosen:updated");
                            $("#yblrForm #hh").val('')
                        }
                    }
                })
            }else{
                var zHtml = "";
                zHtml += "<option value=''>--该冰箱下没有抽屉--</option>";
                $("#yblrForm #chtid").empty();
                $("#yblrForm #chtid").append(zHtml);
                $("#yblrForm #chtid").trigger("chosen:updated");
                var sHtml = "";
                sHtml += "<option value=''>--该抽屉下没有盒子--</option>";
                $("#yblrForm #hzid").empty();
                $("#yblrForm #hzid").append(sHtml);
                $("#yblrForm #hzid").trigger("chosen:updated");
                $("#yblrForm #hh").val('')
            }
        }
    })
}
/*获取盒子list*/
function getHzList(){
    var chtid = document.getElementById("chtid").value;
    /*获取盒子list*/
    $.ajax({
        type: "post",
        url: $("#urlPrefix").val()+"/sample/device/pagedataSbListByFsbid",
        data: {"fids":chtid,"access_token":$("#ac_tk").val()},
        dataType: "json",
        success: function (data) {
            if(data != null && data.length != 0){
                var zHtml = "";
                $.each(data,function(i){
                    zHtml += "<option sbh= '"+ data[i].sbh+"' xgsj= '"+ data[i].xgsj+"'  sfczdb= '"+ data[i].sfczdb+"' value='" + data[i].sbid + "' id='"+data[i].sbid+"'>" + (data[i].wz==null?'':data[i].wz)+(data[i].sbh==null?'':'('+data[i].sbh+')') + "</option>";
                });
                $("#yblrForm #hzid").empty();
                $("#yblrForm #hzid").append(zHtml);
                $("#yblrForm #hzid").trigger("chosen:updated");
                getwzList();
            }else{
                var zHtml = "";
                zHtml += "<option value=''>--该抽屉下没有盒子--</option>";
                $("#yblrForm #hzid").empty();
                $("#yblrForm #hzid").append(zHtml);
                $("#yblrForm #hzid").trigger("chosen:updated");
                $("#yblrForm #hh").val('')
            }
        }
    })
}
/*获取wzlist*/
function getwzList(){
    var hzid = document.getElementById("hzid").value;
    var sbh = $("#yblrForm #hzid option:selected").attr("sbh");
    if(sbh!='null'&&sbh!=''&&sbh!=null){
        $("#yblrForm #hh").val(sbh)
        $("#yblrForm #hh").attr("readonly","readonly")
    }else{
        $("#yblrForm #hh").val('')
        $("#yblrForm #hh").removeAttr("readonly");
    }
    //获取wzlist
    $.ajax({
        type: "post",
        url: $("#urlPrefix").val()+"/sample/inventory/pagedataWzlist",
        data: {hzid:hzid,"access_token":$("#ac_tk").val()},
        dataType: "json",
        success: function (data) {
            if (data!=""){
                wzList = data;
                wzList.sort(function compare(value1,value2){
                    //升序
                    return value1 - value2
                });
                cfqwzList = [];
                for (var i = 0; i < data.length; i++) {
                    cfqwzList.push(data[i]);
                }
                cfqwzList.sort(function compare(value1,value2){
                    //升序
                    return value1 - value2
                });
            }else {
                wzList = [];
                cfqwzList = [];
            }

        }
    })
}

$(function(){
    //所有下拉框添加choose样式
    jQuery('#yblrForm .chosen-select').chosen({width: '100%'});
    //初始化列表
    var oTable=new ybkcxxEdit_TableInit();
    oTable.Init();
    var wzListStr = $("#yblrForm #wzList").val();
    if (wzListStr!="") {
        wzList = $("#yblrForm #wzList").val().split(",");
        wzList.sort(function compare(value1,value2){
            //升序
            return value1 - value2
        });
        cfqwzList = $("#yblrForm #wzList").val().split(",");
        cfqwzList.sort(function compare(value1,value2){
            //升序
            return value1 - value2
        });
    }
    var hh = $("#yblrForm #hh").val();
    if(hh){
        $("#yblrForm #hh").attr("readonly","readonly")
    }
});

