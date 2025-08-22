var t_map = [];
var kthslbj = true;
//显示字段
var columnsArray = [
    {
        title: '序',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序',
        width: '3%',
        align: 'left',
        visible:true
    }, {
        field: 'fhdh',
        title: '发货单号',
        titleTooltip:'发货单号',
        width: '15%',
        align: 'left',
        visible: true
    }, {
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '10%',
        align: 'left',
        visible: true
    }, {
        field: 'wlmc',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '8%',
        align: 'left',
        visible: true
    }, {
        field: 'gg',
        title: '规格',
        titleTooltip:'规格',
        width: '5%',
        align: 'left',
        visible: true
    }, {
        field: 'jldw',
        title: '单位',
        titleTooltip:'单位',
        width: '6%',
        align: 'left',
        visible: true
    }, {
        title: '可退货数量',
        titleTooltip:'可退货数量',
        formatter:kthslformat,
        width: '8%',
        align: 'left',
        visible: true
    }, {
        field: 'thsl',
        title: '退货数量',
        titleTooltip:'退货数量',
        formatter:thslformat,
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'ck',
        title: '仓库',
        titleTooltip:'仓库',
        formatter:ckformat,
        width: '10%',
        align: 'left',
        visible: true
    }, {
        field: 'kw',
        title: '库位',
        titleTooltip:'库位',
        width: '10%',
        align: 'left',
        formatter:kwformat,
        visible: true
    }, {
        field: 'scph',
        title: '生产批号',
        titleTooltip:'生产批号',
        width: '9%',
        align: 'left',
        visible: true
    }, {
        field: 'scrq',
        title: '生产日期*',
        titleTooltip:'生产日期',
        width: '8%',
        align: 'left',
        visible: true
    }, {
        field: 'yxq',
        title: '失效日期*',
        titleTooltip:'生产日期',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'bz',
        title: '备注',
        titleTooltip:'备注',
        width: '6%',
        align: 'left',
        formatter:bzformat,
        visible: true
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '10%',
        align: 'left',
        formatter:czformat,
        visible: true
    }];

var editReturnManagementForm_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#editReturnManagementForm #tb_list').bootstrapTable({
            url: $('#editReturnManagementForm #urlPrefix').val()+'/storehouse/returnManagement/pagedataGetReturnManagementDetailList?thid='+$('#editReturnManagementForm #thid').val(),         //请求后台的URL（*）
            method: 'get',                       //请求方式（*）
            toolbar: '#editReturnManagementForm #toolbar',                //工具按钮用哪个容器
            striped: true,                       //是否显示行间隔色
            cache: false,                        //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                      //是否启用排序
            sortName: "fhdh",				     //排序字段
            sortOrder: "desc",                   //排序方式
            queryParams: oTableInit.queryParams, //传递参数（*）
            sidePagination: "server",            //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                        //初始化加载第一页，默认第一页
            pageSize: 15,                        //每页的记录行数（*）
            pageList: [15, 30, 50, 100],         //可供选择的每页的行数（*）
            paginationPreText: '‹',				 //指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			 //指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,              //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                       //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "fhdh",                    //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                     //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
                t_map = map;
                if(t_map.rows!=null){
                    // 初始化thmx_json
                    var json = [];
                    for (var i = 0; i < t_map.rows.length; i++) {
                        var sz = {"index":'',"fhid":'',"hwid":'',"thsl":'',"bz":'',"xsdd":'',"fhmxglid":'',"bj":'',"suil":'',"wsdj":'',"hsdj":'',"xsmxid":'',"fhmxid":'',"thmxid":'',"thid":'',"ck":'',"kw":''};
                        sz.index=t_map.rows.length;
                        sz.fhid = t_map.rows[i].fhid;
                        sz.hwid = t_map.rows[i].hwid;
                        sz.thsl = t_map.rows[i].thsl;
                        sz.bz = t_map.rows[i].bz;
                        sz.xsdd = t_map.rows[i].xsdd;
                        sz.fhmxglid = t_map.rows[i].fhmxglid;
                        sz.bj = t_map.rows[i].bj;
                        sz.suil = t_map.rows[i].suil;
                        sz.wsdj = t_map.rows[i].wsdj;
                        sz.hsdj = t_map.rows[i].hsdj;
                        sz.xsmxid = t_map.rows[i].xsmxid;
                        sz.fhmxid = t_map.rows[i].fhmxid;
                        sz.thmxid = t_map.rows[i].thmxid;
                        sz.thid = t_map.rows[i].thid;
                        sz.ck = t_map.rows[i].ck;
                        sz.kw = t_map.rows[i].kw;
                        t_map.rows[i].kthsl = (Number(t_map.rows[i].kthsl)+ Number(t_map.rows[i].thsl)).toFixed(2);
                        json.push(sz);
                    }
                    $("#editReturnManagementForm #thmx_json").val(JSON.stringify(json));
                    $("#editReturnManagementForm #xgqthmx_json").val(JSON.stringify(json));
                }
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                return;
            }
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
            sortLastName: "scrq", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};
/**
 * 仓库格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function ckformat(value,row,index){
    var cks = $("#editReturnManagementForm #cklist").val();
    var cklist = JSON.parse(cks);
    var ck=row.ck;
    var html="";
    html +="<div id='ckdiv_"+index+"' name='ckdiv' style='width:100%;display:inline-block'>";
    html += "<select id='ck_"+index+"' name='ck_"+index+"' class='form-control chosen-select' validate='{required:true}' onchange=\"checkCkKw('"+index+"',this,\'ck\')\">";
    html += "<option value=''>--请选择--</option>";
    for(var i = 0; i < cklist.length; i++) {
        html += "<option id='"+cklist[i].ckid+"' value='"+cklist[i].ckid+"'";
        if(ck!=null && ck!=""){
            if(ck==cklist[i].ckid){
                html += "selected"
            }
        }
        html += ">"+cklist[i].ckmc+"</option>";
    }
    html +="</select></div>";
    return html;
}
/**
 * 库位格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function kwformat(value,row,index){
    var kws = $("#editReturnManagementForm #kwlist").val();
    var ykwlist = JSON.parse(kws);
    var kwlist =[];
    var kw=row.kw;
    var ck=row.ck;
    for (var i = 0; i < ykwlist.length; i++) {
        if (ykwlist[i].fckid == ck){
            kwlist.push(ykwlist[i]);
        }
    }
    var html="";
    html +="<div id='kwdiv_"+index+"' name='kwdiv' style='width:100%;display:inline-block'>";
    html += "<select id='kw_"+index+"' name='kw_"+index+"' class='form-control chosen-select' validate='{required:true}' onchange=\"checkCkKw('"+index+"',this,\'kw\')\">";
    html += "<option value=''>--请选择--</option>";
    for(var i = 0; i < kwlist.length; i++) {
        html += "<option id='" + kwlist[i].ckid + "' value='" + kwlist[i].ckid + "'";
        if (kw != null && kw != "") {
            if (kw == kwlist[i].ckid) {
                html += "selected"
            }
        }
        html += ">" + kwlist[i].ckmc + "</option>";
    }
    html +="</select></div>";
    return html;
}
function checkCkKw(index, e, zdmc){
    if(zdmc=="ck"){
        t_map.rows[index].ck = e.value;
        var ck = $("#editReturnManagementForm #ck_"+index).val();
        if(ck == null || ck==""){
            var zlbHtml = "";
            zlbHtml += "<option value=''>--请选择--</option>";
            $("#editReturnManagementForm #kw_"+index).empty();
            $("#editReturnManagementForm #kw_"+index).append(zlbHtml);
            $("#editReturnManagementForm #kw_"+index).trigger("chosen:updated");
            return;
        }
        var url="/warehouse/warehouse/pagedataQueryDtoList";
        url = $('#editReturnManagementForm #urlPrefix').val()+url;
        $.ajax({
            type:'post',
            url:url,
            cache: false,
            data: {"fckid":ck,"access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(data){
                if(data != null && data.length != 0){
                    var zlbHtml = "";
                    zlbHtml += "<option value=''>--请选择--</option>";
                    $.each(data,function(i){
                        zlbHtml += "<option value='" + data[i].ckid + "' id='"+data[i].ckid+"' ckmc='"+data[i].ckmc+"' >" + data[i].ckmc + "</option>";
                    });
                    $("#editReturnManagementForm #kw_"+index).empty();
                    $("#editReturnManagementForm #kw_"+index).append(zlbHtml);
                    $("#editReturnManagementForm #kw_"+index).trigger("chosen:updated");
                }else{
                    var zlbHtml = "";
                    zlbHtml += "<option value=''>--请选择--</option>";
                    $("#editReturnManagementForm #kw_"+index).empty();
                    $("#editReturnManagementForm #kw_"+index).append(zlbHtml);
                    $("#editReturnManagementForm #kw_"+index).trigger("chosen:updated");
                }
            }
        });
    }
    if(zdmc=="kw"){
        t_map.rows[index].kw = e.value;
    }
}
function bzformat(value,row,index){
    if(row.bz == null){
        row.bz = "" ;
    }
    var html="<input id='bz_"+index+"' name='bz_"+index+"' value='"+row.bz+"' validate='{stringMaxLength:1024}' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'bz\')\"></input>";
    return html;
}
/**
 * 退货数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function thslformat(value,row,index){
    if(row.thsl == null){
        row.thsl = "";
    }
    var html = "<input id='thsl_"+index+"' value='"+row.thsl+"' name='thsl_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{slNumber:true}' onblur= \"checkSum('"+index+"',this,\'thsl\')\"/>";
    return html;
}
/**
 * 可退货数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function kthslformat(value,row,index){
   var flag = $("#editReturnManagementForm #flag").val();
   if (flag=="mod"&&kthslbj){
       return (Number(row.thsl)+ Number(row.kthsl)).toFixed(2);
   }else {
       return row.kthsl;
   }
}
/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("slNumber", function (value, element){
    if(!value){
        $("#editReturnManagementForm #"+element.id).val("")
        return false;
    }else{
        if (value==0){
            $.error("退货数量不能为0!");
            $("#editReturnManagementForm #"+element.id).val("")
        }
        if(!/^\d+(\.\d{1,2})?$/.test(value)){
            $.error("请填写正确数量格式，可保留两位小数!");
            $("#editReturnManagementForm #"+element.id).val("")
        }
    }
    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delFhmx(index,event){
    var preJson = JSON.parse($("#editReturnManagementForm #thmx_json").val());
    let fhdh = $("#editReturnManagementForm #fhdhs").val();
    let fhid = preJson[0].fhid;
    preJson.splice(index,1);
    t_map.rows.splice(index,1);
    $("#editReturnManagementForm #thmx_json").val(JSON.stringify(preJson));
    $("#editReturnManagementForm #thmxJson").val(JSON.stringify(t_map.rows));
    if (preJson.length <= 0){
        $("#editReturnManagementForm #fhdhs").tagsinput('remove', {"fhid":fhid,"fhdh":fhdh});
    }
    $("#editReturnManagementForm #tb_list").bootstrapTable('load',t_map);
}
/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
    var html="";
    html += "<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除明细' onclick=\"delFhmx('" + index + "',event)\" >删除</span></div>";
    return html;
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
        if (zdmc == 'thsl') {
             if (Number(e.value)>Number(t_map.rows[index].kthsl)){
                 $.alert("退货数量不能大于可退货数量！");
                 e.value = '';
                 return false;
             }
            t_map.rows[index].thsl = e.value;
        } else if (zdmc == 'ckid') {
            t_map.rows[index].ckid = e.value;
        }else if (zdmc == 'bz') {
            t_map.rows[index].bz = e.value;
        }
    }
    $("#editReturnManagementForm #thmx_json").val(JSON.stringify(t_map.rows));
}
//添加日期控件
laydate.render({
    elem: '#djrq'
    ,theme: '#2381E9'
});
/*
* 选择发货单号
* */
function chooseFhgl_T(){
    var url = $('#editReturnManagementForm #urlPrefix').val();
    url = url + "/storehouse/storehouse/pagedataChooseListDeliverGoods?zt=80&access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择发货单号', choosefhglConfig);
}
var choosefhglConfig = {
    width : "1000px",
    modalName	: "chooseFhglModal",
    formName	: "chooseFhglForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chooseFhglForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                // 获取未更改发货单和明细信息
                var preJson = [];
                // 获取更改后的发货单和明细信息
                var fhmxJson = [];
                if($("#chooseFhglForm #fhmx_json").val()){
                    fhmxJson = JSON.parse($("#chooseFhglForm #fhmx_json").val());
                    if($("#editReturnManagementForm #thmx_json").val()){
                        preJson = JSON.parse($("#editReturnManagementForm #thmx_json").val());
                        if (preJson.length >0){
                            var fhid = preJson[0].fhid;
                            for (var i = 0; i < fhmxJson.length; i++) {
                                if (fhid!=fhmxJson[i].fhid){
                                    $.alert("只能引用一条发货单！");
                                    return false;
                                }
                            }
                        }
                    }
                    var ids="";
                    for (var i = 0; i < fhmxJson.length; i++) {
                        var  flag = true;
                        for (var j = 0; j < preJson.length; j++) {
                            if (preJson[j].fhmxid==fhmxJson[i].fhmxid){
                                flag =false;
                            }
                        }
                        if (flag){
                            ids = ids + "," + fhmxJson[i].fhmxid;
                        }
                    }
                    if(ids.length > 0){
                        kthslbj =false;
                        ids = ids.substr(1);
                        // 查询信息并更新到页面
                        $.post($('#editReturnManagementForm #urlPrefix').val() + "/storehouse/storehouse/pagedataGetdeliverInfo",{"mxids":ids, "access_token":$("#ac_tk").val()},function(data){
                            var fhmxDtos = data.fhmxList;
                            if(fhmxDtos != null && fhmxDtos.length > 0){
                                // 更新页面列表(新增)
                                for (var i = 0; i < fhmxDtos.length; i++) {
                                    if(i == 0){
                                        if(!t_map.rows){
                                            t_map.rows= [];
                                        }
                                        $("#editReturnManagementForm #khjc").val(fhmxDtos[0].khjc);
                                        $("#editReturnManagementForm #kh").val(fhmxDtos[0].kh);
                                        $("#editReturnManagementForm #khdm").val(fhmxDtos[0].khdm);
                                        $("#editReturnManagementForm #jsr").val(fhmxDtos[0].jsr);
                                        $("#editReturnManagementForm #ddh").val(fhmxDtos[0].ddh);
                                        $("#editReturnManagementForm #shdz").val(fhmxDtos[0].shdz);
                                    }
                                    if (fhmxDtos[i].kthsl >0){
                                        t_map.rows.push(fhmxDtos[i]);
                                    }
                                    var sz = {"fhid":'',"fhmxid":'',"kh":''};
                                    sz.fhid = fhmxDtos[i].fhid;
                                    sz.fhmxid = fhmxDtos[i].fhmxid;
                                    sz.kh = fhmxDtos[i].kh;
                                    preJson.push(sz);
                                }
                            }
                            if (t_map.rows.length <= 0){
                                $.alert("可退回数量为0！");
                                return false;
                            }
                            $("#editReturnManagementForm #thmx_json").val(JSON.stringify(preJson));

                            // 发货单号
                            var selDjh = $("#chooseFhglForm #yxdjh").tagsinput('items');
                            $("#editReturnManagementForm #fhdhs").tagsinput({
                                itemValue: "fhid",
                                itemText: "fhdh",
                            })
                            for(var i = 0; i < selDjh.length; i++){
                                var value = selDjh[i].value;
                                var text = selDjh[i].text;
                                $("#editReturnManagementForm #fhdhs").tagsinput('add', {"fhid":value,"fhdh":text});
                            }
                            $("#editReturnManagementForm #thmxJson").val(JSON.stringify(t_map.rows));
                            $("#editReturnManagementForm #tb_list").bootstrapTable('load',t_map);
                            $.closeModal(opts.modalName);
                        },'json');
                    }else{
                        $("#editReturnManagementForm #tb_list").bootstrapTable('load',t_map);
                        $("#editReturnManagementForm #thmx_json").val(JSON.stringify(preJson));
                        // 请购单号
                        var selDjh = $("#chooseFhglForm #yxdjh").tagsinput('items');
                        $("#editReturnManagementForm #fhdhs").tagsinput({
                            itemValue: "fhid",
                            itemText: "fhdh",
                        })
                        for(var i = 0; i < selDjh.length; i++){
                            var value = selDjh[i].value;
                            var text = selDjh[i].text;
                            $("#editReturnManagementForm #fhdhs").tagsinput('add', {"fhid":value,"fhdh":text});
                        }
                        $("#editReturnManagementForm #thmxJson").val(JSON.stringify(t_map.rows));
                        preventResubmitForm(".modal-footer > button", true);
                        $.closeModal(opts.modalName);
                        preventResubmitForm(".modal-footer > button", false);
                    }
                }else{
                    $.alert("未获取到选中的发货信息！");
                }
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * 监听单据号标签移除事件
 */
var tagsRemoved = $('#editReturnManagementForm #fhdhs').on('beforeItemRemove', function(event) {
    // 获取未更改请购单和明细信息
    var preJson = [];
    if($("#editReturnManagementForm #thmx_json").val()){
        preJson = JSON.parse($("#editReturnManagementForm #thmx_json").val());
        // 删除明细并刷新列表
        var fhid = event.item.fhid;
        for (var i = t_map.rows.length-1; i >= 0 ; i--) {
            if(t_map.rows[i].fhid == fhid){
                t_map.rows.splice(i,1);
                preJson.splice(i,1);
            }
        }
        $("#editReturnManagementForm #thmx_json").val(JSON.stringify(preJson));
        $("#editReturnManagementForm #tb_list").bootstrapTable('load',t_map);
        $("#editReturnManagementForm #thmxJson").val(JSON.stringify(t_map.rows));
    }
});
/**
 * 选择销售部门
 * @returns
 */
function chooseXsBm() {
    var url = $('#editReturnManagementForm #urlPrefix').val()+"/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择销售部门', chooseXsBmConfig);
}
var chooseXsBmConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseXsBmModal",
    formName	: "editReturnManagementForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#listDepartmentForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#editReturnManagementForm #xsbm').val(sel_row[0].jgid);
                    $('#editReturnManagementForm #xsbmmc').val(sel_row[0].jgmc);
                    $('#editReturnManagementForm #xsbmdm').val(sel_row[0].jgdm);
                    $.closeModal(opts.modalName);
                }else{
                    $.error("请选中一行");
                    return;
                }
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * 选择业务员
 * @returns
 */
function chooseYwy() {
    var url = "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择业务员', chooseYwyConfig);
}

var chooseYwyConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseYwyModal",
    formName	: "editReturnManagementForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#taskListFzrForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#editReturnManagementForm #ywy').val(sel_row[0].yhid);
                    $('#editReturnManagementForm #ywymc').val(sel_row[0].zsxm);
                    $.closeModal(opts.modalName);
                }else{
                    $.error("请选中一行");
                    return;
                }
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

$(function(){
    // 所有下拉框添加choose样式
    jQuery('#editReturnManagementForm .chosen-select').chosen({width: '100%'});

    //初始化列表
    var oTable=new editReturnManagementForm_TableInit();
    oTable.Init();
});