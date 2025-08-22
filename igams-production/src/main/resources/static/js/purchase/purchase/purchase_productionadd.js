var t_map = [];

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
        field: 'wlid',
        title: '物料ID',
        width: '8%',
        align: 'left',
        visible: false
    },{
        field: 'xqjhmxid',
        title: '需求计划明细ID',
        width: '8%',
        align: 'left',
        visible: false
    },{
        field: 'bommxid',
        title: 'Bom明细ID',
        width: '8%',
        align: 'left',
        visible: false
    },{
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '15%',
        align: 'left',
        formatter:wlbmformat,
        visible: true,
        sortable: true
    }, {
        field: 'wlmc，gg',
        title: '生产商与规格',
        titleTooltip:'生产商与规格',
        width: '15%',
        align: 'left',
        formatter:scs_ggformat,
        visible: true
    },{
        field: 'kcl',
        title: '库存量',
        width: '8%',
        align: 'left',
        formatter:wlkclformat,
        visible: true
    }, {
        field: 'sl,jldw',
        title: '数量',
        titleTooltip:'数量',
        width: '10%',
        align: 'left',
        formatter:slformat,
        visible: true
    }, {
        field: 'wllbmc,wlzlbmc',
        title: '物料类别与子类别',
        titleTooltip:'物料类别与子类别',
        width: '20%',
        formatter:wllb_zlbformat,
        align: 'left',
        visible: true
    }, {
        field: 'wlzlbtcmc,ychh',
        title: '物料子类别统称与货号',
        titleTooltip:'物料子类别统称与货号',
        width: '15%',
        align: 'left',
        formatter:zlbtc_hhformat,
        visible: true
    },{
        field: 'cpzch',
        title: '产品注册号',
        width: '8%',
        align: 'left',
        formatter:cpzchformat,
        visible: true
    },{
        field: 'qwrq',
        title: '物料使用日期',
        width: '10%',
        align: 'left',
        formatter:qwrqformat,
        visible: true
    },{
        field: 'bz',
        title: '备注',
        width: '15%',
        align: 'left',
        formatter:bzformat,
        visible: true
    },{
        field: 'fj',
        title: '附件',
        width: '3%',
        align: 'left',
        formatter:fjformat,
        visible: true
    },{
        field: 'cz',
        title: '操作',
        width: '5%',
        align: 'left',
        formatter:czformat,
        visible: true
    }];
var produceAddForm_TableInit= function () {
var oTableInit=new Object();
oTableInit.Init=function(){
    $("#produceAddForm #tb_list").bootstrapTable({
        url: $("#produceAddForm #urlPrefix").val()+'/production/materiel/pagedataShoppinglist', //请求后台的URL（*）
        method: 'get',                      //请求方式（*）
        toolbar: '#produceAddForm #toolbar',                //工具按钮用哪个容器
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        paginationShowPageGo: false,         //增加跳转页码的显示
        sortable: true,                     //是否启用排序
        sortName: '',				//排序字段
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
        showColumns: false,                  //是否显示所有的列
        showRefresh: false,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: false,                //是否启用点击选中行
        //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: '',                     //每一行的唯一标识，一般为主键列
        showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                   //是否显示父子表
        paginationDetailHAlign:' hidden',
        columns: columnsArray,
        onLoadSuccess:function(map){
            t_map=map;
                $("#produceAddForm #tb_list").bootstrapTable('load',t_map);
        },
        onLoadError:function(){
            alert("数据加载失败！");
        },
        onDblClickRow: function (row, $element) {
        },
        rowStyle: function (row, index) {
            //这里有5个取值代表5中颜色['active', 'success', 'info', 'warning', 'danger'];
            var strclass = "";
            if (row.zt == "15") {
                strclass = 'danger';//还有一个active
            }
            else if (row.zt == "30") {
                strclass = 'warning';
            }
            else if (row.zt == "80") {
                strclass = 'success';
            }
            else {
                return {};
            }
            return { classes: strclass }
        },
    });
    $("#produceAddForm #tb_list").colResizable({
        liveDrag:true,
        gripInnerHtml:"<div class='grip'></div>",
        draggingClass:"dragging",
        resizeMode:'fit',
        postbackSafe:true,
        partialRefresh:true
    })
};
oTableInit.queryParams=function(params){
    var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pageSize: params.limit,   // 页面大小
        pageNumber: (params.offset / params.limit) + 1,  // 页码
        access_token:$("#ac_tk").val(),
        sortName: params.sort,      // 排序列名
        sortOrder: params.order, // 排位命令（desc，asc）
        sortLastName: '', // 防止同名排位用
        sortLastOrder: "asc" // 防止同名排位用
        // 搜索框使用
        // search:params.search
    };
    return map;
};
return oTableInit;
}
function wlbmformat(value,row,index){
    var html="";
    if(row.wlbm!=null && row.wlbm!=''){
        html="<span>"+row.wlbm +"<hr style='width: 80%;margin: 0 auto;border: 0;height: 0;border-top: 1px solid rgba(0, 0, 0, 0.1);border-bottom: 1px solid rgba(255, 255, 255, 0.3);'>"+'<span title='+row.wlmc+'>'
            + "<a href='javascript:void(0);' onclick=\"viewwlxx('"+row.bommxid+"','"+row.xqjhmxid+"')\">"+row.wlmc+"</a>";
    }
    return html;
}
function viewwlxx(bommxid,xqjhmxid) {
    var url=$("#produceAddForm #urlPrefix").val()+"/progress/progress/pagedataViewBomMx?bommxid="+bommxid+"&xqjhmxid="+xqjhmxid+"&access_token=" + $("#ac_tk").val();
        $.showDialog(url,'物料信息',viewWlConfig);
    }

 var viewWlConfig = {
        width		: "800px",
        height		: "500px",
        offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
        buttons		: {
            cancel : {
                label : "关 闭",
                className : "btn-default"
            }
        }
    };
function scs_ggformat(value,row,index){
    if(row.gg == null){
        row.gg = "暂无";
    }
    if (row.scs == null) {
        row.scs = "暂无";
    }

    var html = row.scs+"<hr style='width: 80%;margin: 0 auto;border: 0;height: 0;border-top: 1px solid rgba(0, 0, 0, 0.1);border-bottom: 1px solid rgba(255, 255, 255, 0.3);'>"+row.gg;

    return html;
}
function showWlkcInfo(wlid){
    var url=$("#produceAddForm #urlPrefix").val()+"/production/materiel/pagedataViewMaterKcInfo?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料库存信息',viewWlkcConfig);
}
function slformat(value,row,index){
    if(row.sl == null){
        row.sl = "";
    }
    var html = "<input id='sl_"+index+"' value='"+row.sl+"' name='sl_"+index+"' style='width:80%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'sl\')\" autocomplete='off'></input>"+row.jldw;
    return html;
}
function bzformat(value,row,index){
    if(row.bz == null){
        row.bz = "" ;
    }
    var html="<input id='bz_"+index+"' name='bz_"+index+"' value='"+row.bz+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'bz\')\" autocomplete='off'></input>";
    return html;
}

/**
 * 物料库存格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wlkclformat(value,row,index){
    var html = "";
    if(row.kcl){
        html += "<a href='javascript:void(0);' onclick=\"showWlkcInfo('"+row.wlid+"')\">"+row.kcl+"</a>";
    }else{
        return "";
    }
    return html;
}
function showWlkcInfo(wlid){
    var url=$("#produceAddForm #urlPrefix").val()+"/production/materiel/pagedataViewMaterKcInfo?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料库存信息',viewWlkcConfig);
}
var viewWlkcConfig = {
    width		: "1000px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * 附件格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fjformat(value,row,index){
    var html = "<div id='fj_"+index+"'>";
    if ($("#produceAddForm #qgmx_json").val()!=null && $("#produceAddForm #qgmx_json").val() != ''){
            var data=JSON.parse($("#produceAddForm #qgmx_json").val());
        if (data.length>0){
            if(data[index].fjids!=null && data[index].fjids!=''){
                html += "<input type='hidden' name='mxfj_"+index+"' value='"+data[index].fjids+"'/>";
            }else{
                html += "<input type='hidden' name='mxfj_"+index+"'/>";
            }
        }else {
            html += "<input type='hidden' name='mxfj_"+index+"'/>";
        }
        }
    if(row.fjbj == "0"){
        html += "<a href='javascript:void(0);' onclick='uploadShoppingFile(\"" + index + "\")' >是</a>";
    }else{
        html += "<a href='javascript:void(0);' onclick='uploadShoppingFile(\"" + index + "\")' >否</a>";
    }
    html += "</div>";
    return html;
}
/**
 * 选择申请部门
 * @returns
 */
function chooseBm() {
    var url = $('#produceAddForm #urlPrefix').val()+"/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择部门', chooseBmConfig);
}
var chooseBmConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseBmModal",
    formName	: "produceAddForm",
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
                    if($("#produceAddForm #qglbdm").val() == "MATERIAL" || $("#produceAddForm #qglbdm").val() == "OUTSOURCE"){
                        if (sel_row[0].jgdm){
                            $('#produceAddForm #sqbmdm').val(sel_row[0].jgdm);
                        }else{
                            $('#produceAddForm #sqbmdm').val("");
                            $.alert("U8不存在此部门！");
                            return false;
                        }
                    }
                    $('#produceAddForm #sqbm').val(sel_row[0].jgid);
                    $('#produceAddForm #sqbmmc').val(sel_row[0].jgmc);
                    if (!sel_row[0].kzcs1){
                        $('#produceAddForm #sqbm').val("");
                        $('#produceAddForm #sqbmmc').val("");
                        $('#produceAddForm #sqbmdm').val("");
                        $('#produceAddForm #jlbh').val("");
                        $.alert("所选部门存在异常！");
                        return false;
                    }
                    $.closeModal(opts.modalName);
                    //重新生成单据号
                    $.ajax({
                        type:'post',
                        url:$('#produceAddForm #urlPrefix').val()+"/purchase/purchase/pagedataGenerateDjh?cskz1="+$('#produceAddForm #cskz1').val(),
                        cache: false,
                        data: {"sqbm":sel_row[0].jgid,"access_token":$("#ac_tk").val()},
                        dataType:'json',
                        success:function(data){
                            //返回值
                            if(data.djh!=null && data.djh!=''){
                                $("#produceAddForm #djh").val(data.djh);
                            }
                            if(data.jlbh!=null && data.jlbh!=''){
                                $("#produceAddForm #jlbh").val(data.jlbh);
                            }
                        }
                    });
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
 * 选择申请人
 * @returns
 */
function chooseSqr() {
    var url = $('#produceAddForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择申请人', chooseQrrConfig);
}
var chooseQrrConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseSqrModal",
    formName	: "produceAddForm",
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
                    $('#produceAddForm #sqr').val(sel_row[0].yhid);
                    $('#produceAddForm #sqrmc').val(sel_row[0].zsxm);
                    $.closeModal(opts.modalName);
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#produceAddForm #urlPrefix').val()+"/common/habit/commonModUserHabit",
                        cache: false,
                        data: {"dxid":sel_row[0].yhid,"access_token":$("#ac_tk").val()},
                        dataType:'json',
                        success:function(data){}
                    });
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
 * 请购明细上传附件
 * @param index
 * @returns
 */
function uploadShoppingFile(index) {
    var url = $('#produceAddForm #urlPrefix').val()+"/production/purchase/pagedataGetUploadFile?access_token=" + $("#ac_tk").val()+"&requestName="+$("#produceAddForm #requestName").val();
    if(index){
        var qgid = $("#produceAddForm #qgid").val();
        var wlid = t_map.rows[index].wlid;
        var fjids = $("#produceAddForm #fj_"+ index +" input").val();
        var qgmxid = t_map.rows[index].qgmxid;
        url=$('#produceAddForm #urlPrefix').val()+"/production/purchase/pagedataGetUploadFile?access_token=" + $("#ac_tk").val()+"&qgmxid="+qgmxid+"&qgid="+qgid+"&wlid="+wlid+"&index="+index+"&requestName="+$("#produceAddForm #requestName").val();
        if(fjids){
            url=url + "&fjids="+fjids;
        }
    }
    $.showDialog(url, '上传附件', uploadFileConfig);
}
var uploadFileConfig = {
    width : "800px",
    height : "500px",
    modalName	: "uploadFileModal",
    formName	: "ajaxForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                if($("#ajaxForm #fjids").val()){
                    var wlid=$("#ajaxForm #wlid").val();
                    var index=$("#ajaxForm #index").val();
                    if(!$("#produceAddForm #fj_"+index+" input").val()){
                        $("#produceAddForm #fj_"+index+" input").val($("#ajaxForm #fjids").val());
                    }else{
                        $("#produceAddForm #fj_"+index+" input").val($("#produceAddForm #fj_"+index+" input").val() + "," + $("#ajaxForm #fjids").val());
                    }
                    $("#produceAddForm #fj_"+index+" a").text("是");
                }
                // 更新临时复件信息到json
                var data=JSON.parse($("#produceAddForm #qgmx_json").val());
                for(var i=0;i<data.length;i++){
                    if(data[i].index == index){
                        data[i].fjids = $("#produceAddForm #fj_"+index+" input").val();
                        t_map.rows[index].fjbj = "0";
                    }
                }
                $("#produceAddForm #qgmx_json").val(JSON.stringify(data));
                $.closeModal(opts.modalName);
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
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
    var html = "<span style='margin-left:5px;' class='btn btn-default' title='移出采购车' onclick=\"delShoppingCarMater('" + index + "','"+row.xqjhmxid+"',event)\" >删除</span>";
    return html;
}

/**
 * 删除操作(从采购车删除)
 * @param index
 * @param event
 * @returns
 */
function delShoppingCarMater(index,xqjhmxid,event){
    var t_data = JSON.parse($("#produceAddForm #qgmx_json").val());
    t_map.rows.splice(index,1);
    for(var i=0;i<t_data.length;i++){
        if(t_data[i].index == index){
            t_data.splice(i,1);
        }
    }
    $("#produceAddForm #tb_list").bootstrapTable('load',t_map);
    var json=[];
    for(var i=0;i<t_data.length;i++){
        var sz={"index":i,"wlid":'',"sl":'',"qwrq":'',"bz":'',"wlbm":'',"cpzch":'',"fjids":'',"wllbmc":'',"wlzlbmc":'',"wlzlbtc":'',"ychh":'',"gg":'',"scs":'',"jldw":'',"xqjhmxid":'',"kcl":'',"bommxid":''};
        sz.wlid=t_data[i].wlid;
        sz.sl=t_data[i].sl;
        sz.wlbm=t_data[i].wlbm;
        sz.qwrq=t_data[i].qwrq;
        sz.gg=t_data[i].gg;
        sz.bz=t_data[i].bz;
        sz.fjids=t_data[i].fjids;
        sz.wllbmc=t_data[i].wllbmc;
        sz.wlzlbmc=t_data[i].wlzlbmc;
        sz.wlzlbtc=t_data[i].wlzlbtc;
        sz.ychh=t_data[i].ychh;
        sz.scs=t_data[i].scs;
        sz.jldw=t_data[i].jldw;
        sz.cpzch=t_data[i].cpzch;
        sz.xqjhmxid=t_data[i].xqjhmxid;
        sz.kcl=t_data[i].kcl;
        sz.bommxid=t_data[i].bommxid;
        json.push(sz);
    }
    $("#produceAddForm #qgmx_json").val(JSON.stringify(json));
}

function wllb_zlbformat(value,row,index){
    if(row.wllbmc == null){
        row.wllbmc = "暂无";
    }
    if (row.wlzlbmc == null) {
        row.wlzlbmc = "暂无";
    }

    var html = row.wllbmc+"<hr style='width: 80%;margin: 0 auto;border: 0;height: 0;border-top: 1px solid rgba(0, 0, 0, 0.1);border-bottom: 1px solid rgba(255, 255, 255, 0.3);'>"+row.wlzlbmc;

    return html;
}
function zlbtc_hhformat(value,row,index){
    if(row.wlzlbtcmc == null){
        row.wlzlbtcmc = "暂无";
    }
    if (row.ychh == null) {
        row.ychh = "暂无";
    }

    var html = row.wlzlbtcmc+"<hr style='width: 80%;margin: 0 auto;border: 0;height: 0;border-top: 1px solid rgba(0, 0, 0, 0.1);border-bottom: 1px solid rgba(255, 255, 255, 0.3);'>"+row.ychh;

    return html;
}
function cpzchformat(value,row,index){
    if(row.cpzch == null){
        row.cpzch = "" ;
    }
    var html="<input id='cpzch_"+index+"' name='cpzch_"+index+"' value='"+row.cpzch+"' validate='{required:true}' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'cpzch\')\" autocomplete='off'></input>";
    return html;
}
function qwrqformat(value,row,index){
    if(row.qwrq==null){
        row.qwrq="";
    }
    var html="<input id='qwrq_"+index+"' qgmxid='"+row.qgmxid+"' name='qwrq_"+index+"' class='qwdhrq' validate='{required:true}'  value='"+row.qwrq+"'  style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'qwrq\')\"></input><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='复制当前日期并替换所有'  onclick=\"copyTime('"+index+"')\" autocomplete='off'><span>";
    setTimeout(function() {
        laydate.render({
            elem: '#produceAddForm #qwrq_'+index
            ,theme: '#2381E9'
            ,min: 1
            ,btns: ['clear', 'confirm']
            ,done: function(value, date, endDate){
                t_map.rows[index].qwrq=value;
                var data=JSON.parse($("#produceAddForm #qgmx_json").val())
                $("#produceAddForm #qgmx_json").val(JSON.stringify(data));
            }
        });
    }, 100);
    return html;
}
/**
 * 复制当前日期并替换全部
 * @param wlid
 * @returns
 */
function copyTime(index){
    var dqrq=$("#produceAddForm #qwrq_"+index).val();
    if(dqrq==null || dqrq==""){
        $.confirm("请先选择日期！");
    }else{
        for(var i=0;i<t_map.rows.length;i++){
            $("#produceAddForm #qwrq_"+i).val(dqrq);
        }
    }
    var data=JSON.parse($("#produceAddForm #qgmx_json").val());
    if(data!=null && data.length>0){
        for(var j=0;j<data.length;j++){
            data[j].qwrq=dqrq;
            t_map.rows[j].qwrq=dqrq;
        }
    }
    $("#produceAddForm #qgmx_json").val(JSON.stringify(data));
}
/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @param bj
 * @returns
 */
function checkSum(index,e,zdmc){
    var data=JSON.parse($("#produceAddForm #qgmx_json").val());
    for(var i=0;i<data.length;i++){
        if(data[i].index == index){
            if(zdmc=='sl'){
                data[i].sl=e.value;
                t_map.rows[i].sl=e.value;
            }else if(zdmc=='qwrq'){
                data[i].qwrq=e.value;
                t_map.rows[i].qwrq=e.value;
            }else if(zdmc=='bz'){
                data[i].bz=e.value;
                t_map.rows[i].bz=e.value;
            }else if(zdmc=='cpzch'){
                data[i].cpzch=e.value;
                t_map.rows[i].cpzch=e.value;
            }
        }
    }
    $("#produceAddForm #qgmx_json").val(JSON.stringify(data));
}
/**
 * 选择生产号(及明细)
 * @returns
 */
function chooseScxx(){
    var url = $('#produceAddForm #urlPrefix').val() + "/progress/progress/pagedataProgressMxList?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择生产单号', chooseScxxConfig);
}
var chooseScxxConfig = {
    width : "1000px",
    modalName	: "produceAddModal",
    formName	: "produceAddForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#progress_details").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                // 获取未更改请购单和明细信息
                var preJson = [];
                // 获取更改后的请购单和明细信息
                var bommx_json = [];
                if($("#produceAddForm #qgmx_json").val()){
                    preJson = JSON.parse($("#produceAddForm #qgmx_json").val());
                }
                if($("#progress_details #bommx_json").val()){
                    bommx_json = JSON.parse($("#progress_details #bommx_json").val());
                    // 判断新增的请购明细信息
                    var ids="";
                    var xqjhmxids="";
                    for (var i = 0; i < bommx_json.length; i++) {
                        var isAdd = true;
                        for (var j = 0; j < preJson.length; j++) {
                            if(preJson[j].xqjhmxid == bommx_json[i].xqjhmxid && preJson[j].bommxid == bommx_json[i].bommxid){
                                isAdd = false;
                                break;
                            }
                        }
                        if(isAdd){
                            ids = ids + ","+ bommx_json[i].bommxid;
                            xqjhmxids=xqjhmxids + ","+ bommx_json[i].xqjhmxid;
                        }
                    }
                    if(ids.length > 0){
                        ids = ids.substr(1);
                        xqjhmxids=xqjhmxids.substr(1);
                        // 查询信息并更新到页面
                        $.post($('#produceAddForm #urlPrefix').val() + "/progress/progress/pagedataListBommxInfoByIds",{"ids":ids,"xqjhmxids":xqjhmxids,"access_token":$("#ac_tk").val()},function(data){
                            var dtoList = data.dtoList;
                            if(dtoList != null && dtoList.length > 0){
                                // 更新页面列表(新增)
                                for (var i = 0; i < dtoList.length; i++) {
                                    var json=[];
                                        t_map.rows.push(dtoList[i]);
                                        var sz={"index":i,"wlid":'',"sl":'',"qwrq":'',"bz":'',"wlbm":'',"scs":'','zt':'',"gg":'',"fjids":'',"cpzch":'',"xqjhmxid":'',"wllbmc":'',"wlzlbmc":'',"wlzlbtcmc":'',"ychh":'',"jldw":'',"kcl":'',"bommxid":''};
                                        sz.wlid=dtoList[i].wlid;
                                        sz.wlbm=dtoList[i].wlbm;
                                        sz.wlmc=dtoList[i].wlmc;
                                        sz.sl=dtoList[i].sl;
                                        sz.scs=dtoList[i].scs;
                                        sz.gg=dtoList[i].gg;
                                        sz.xqjhmxid=dtoList[i].xqjhmxid;
                                        sz.wllbmc=dtoList[i].wllbmc;
                                        sz.wlzlbmc=dtoList[i].wlzlbmc;
                                        sz.wlzlbtcmc=dtoList[i].wlzlbtcmc;
                                        sz.ychh=dtoList[i].ychh;
                                        sz.jldw=dtoList[i].jldw;
                                        sz.kcl=dtoList[i].kcl;
                                        sz.bommxid=dtoList[i].bommxid;
                                        json.push(sz);
                                        preJson.push(sz);
                                }

                                $("#produceAddForm #qgmx_json").val(JSON.stringify(preJson));
                                $("#produceAddForm #tb_list").bootstrapTable('load',t_map);
                                var selDjh = $("#progress_details #xqdh").tagsinput('items');
                                $("#produceAddForm #xqdhs").tagsinput({
                                    itemValue: "xqjhmxid",
                                    itemText: "xqdh",
                                })
                                for(var i = 0; i < selDjh.length; i++){
                                    var value = selDjh[i].value;
                                    var text = selDjh[i].text;
                                    $("#produceAddForm #xqdhs").tagsinput('add', {"xqjhmxid":value,"xqdh":text});
                                }
                            }
                            $.closeModal(opts.modalName);
                        },'json');
                    }else{
                        $("#produceAddForm #tb_list").bootstrapTable('load',t_map);
                        $("#produceAddForm #qgmx_json").val(JSON.stringify(preJson));
                        // 请购单号
                        var selDjh = $("#progress_details #xqdh").tagsinput('items');
                        $("#produceAddForm #xqdhs").tagsinput({
                            itemValue: "xqjhmxid",
                            itemText: "xqdh",
                        })
                        for(var i = 0; i < selDjh.length; i++){
                            var value = selDjh[i].value;
                            var text = selDjh[i].text;
                            $("#produceAddForm #xqdhs").tagsinput('add', {"xqjhmxid":value,"xqdh":text});
                        }
                        $.closeModal(opts.modalName);
                    }
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
 * 点击显示文件上传
 * @returns
 */
function editfile(){
    $("#fileDiv").show();
    $("#file_btn").hide();
}
/**
 * 点击隐藏文件上传
 * @returns
 */
function cancelfile(){
    $("#fileDiv").hide();
    $("#file_btn").show();
}


/**
 * 项目编码下拉框事件
 * @returns
 */
function xmbmEvent(){
    var xmbm = $("#produceAddForm #xmbm").val();
    if(xmbm == null || xmbm == "")
        return;
    var xmbmdm=$("#produceAddForm #"+xmbm).attr("csdm");
    var xmmc=$("#produceAddForm #"+xmbm).attr("csmc");
    $("#shoppingCarForm #xmbmdm").val(xmbmdm);
    $("#shoppingCarForm #xmmc").val(xmmc);
}
function btnBind() {
    var sel_xmdl = $("#produceAddForm #xmdl");
    var sel_xmbm =	$("#produceAddForm #xmbm");
    //项目大类下拉框事件
    sel_xmdl.unbind("change").change(function(){
        xmdlEvent();
    });
    //项目编码
    sel_xmbm.unbind("change").change(function(){
        xmbmEvent();
    });
}
/**
 * 项目大类下拉框事件
 */
function xmdlEvent(){
    var xmdl = $("#produceAddForm #xmdl").val();
    var xmdldm = $("#"+xmdl).attr("csdm");
    $("#produceAddForm #xmdldm").val(xmdldm);
    if(xmdl == null || xmdl==""){
        var zlbHtml = "";
        zlbHtml += "<option value=''>--请选择--</option>";
        $("#produceAddForm #xmbm").empty();
        $("#produceAddForm #xmbm").append(zlbHtml);
        $("#produceAddForm #xmbm").trigger("chosen:updated");
        return;
    }
    var url="/systemmain/data/ansyGetJcsjList";
    if($("#produceAddForm #fwbj").val()!=null && $("#produceAddForm #fwbj").val()!=""){
        url="/ws/data/ansyGetJcsjList";
    }
    url = $('#produceAddForm #urlPrefix').val()+url
    $.ajax({
        type:'post',
        url:url,
        cache: false,
        data: {"fcsid":xmdl,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            if(data != null && data.length != 0){
                var zlbHtml = "";
                zlbHtml += "<option value=''>--请选择--</option>";
                $.each(data,function(i){
                    zlbHtml += "<option value='" + data[i].csid + "' id='"+data[i].csid+"' csdm='"+data[i].csdm+"' csmc='"+data[i].csmc+"'>" + data[i].csmc + "</option>";
                });
                $("#produceAddForm #xmbm").empty();
                $("#produceAddForm #xmbm").append(zlbHtml);
                $("#produceAddForm #xmbm").trigger("chosen:updated");
            }else{
                var zlbHtml = "";
                zlbHtml += "<option value=''>--请选择--</option>";
                $("#produceAddForm #xmbm").empty();
                $("#produceAddForm #xmbm").append(zlbHtml);
                $("#produceAddForm #xmbm").trigger("chosen:updated");
            }
        }
    });
}
/**
 * 请购附件上传回调
 * @param fjid
 * @returns
 */
function displayUpInfo(fjid){
    if(!$("#produceAddForm #fjids").val()){
        $("#produceAddForm #fjids").val(fjid);
    }else{
        $("#produceAddForm #fjids").val($("#produceAddForm #fjids").val()+","+fjid);
    }
}
$(document).ready(function(){
    //初始化列表
    var oTable=new produceAddForm_TableInit();
    oTable.Init();
    btnBind();
    var oFileInput = new FileInput();
    var sign_params=[];
    sign_params.prefix=$('#produceAddForm #urlPrefix').val();
    oFileInput.Init("produceAddForm","displayUpInfo",2,1,"pro_file",null,sign_params);
    //添加日期控件
    laydate.render({
        elem: '#produceAddForm #sqrq'
        ,theme: '#2381E9'
    });
    jQuery('#produceAddForm .chosen-select').chosen({width: '100%'});
})