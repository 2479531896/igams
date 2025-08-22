var t_map=[];
var editPerformance_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#performanceEditForm #edit_list").bootstrapTable({
            url:   $('#performanceEditForm #urlPrefix').val()+'/performance/performance/pagedataGetPerformance',         //请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#performanceEditForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: false,                     // 是否启用排序
            sortName:"jxmx.jxmxid",					// 排序字段
            sortOrder: "asc",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            pageSize: 15,                       // 每页的记录行数（*）
            pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: false,                  // 是否显示所有的列
            showRefresh: false,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "jxmxid",                     // 每一行的唯一标识，一般为主键列
            showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns:  [
                {
                    field: 'jxmxid',
                    title: '绩效明细id',
                    width: '6%',
                    align: 'left',
                    visible: false
                 },{
                    field: 'xmmc',
                    title: '评估项目',
                    width: '12%',
                    align: 'left',
                    visible: true
                },{
                    field: 'nrmc',
                    title: '评估内容',
                    width: '12%',
                    align: 'left',
                    visible: true
                },{
                    field: 'bizmc',
                    title: '标准',
                    width: '20%',
                    align: 'left',
                    formatter:edit_bizformat,
                    visible: true
                },{
                    field: 'bz',
                    title: '备注',
                    width: '25%',
                    align: 'left',
                    formatter:edit_bzformat,
                    visible: true
                },{
                    field: 'df',
                    title: '得分',
                    width: '10%',
                    align: 'left',
                    formatter:edit_dfformat,
                    visible: true
                }],
            onLoadSuccess:function(map){
                t_map=map;
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            },
            onPostBody:function(){
                $('#performanceEditForm #edit_list').bootstrapTable('mergeCells',{
                    index:0,
                    field:'xmmc',
                    rowspan:3
                });
                $('#performanceEditForm #edit_list').bootstrapTable('mergeCells',{
                    index:4,
                    field:'xmmc',
                    rowspan:3
                });
                $('#performanceEditForm #edit_list').bootstrapTable('mergeCells',{
                    index:7,
                    field:'xmmc',
                    rowspan:2
                });
                $('#performanceEditForm #edit_list').bootstrapTable('mergeCells',{
                    index:9,
                    field:'xmmc',
                    rowspan:3
                });
            },
        });
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            access_token:$("#ac_tk").val(),
            gfjxid: $('#performanceEditForm #gfjxid').val()
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit;
}

function edit_bizformat(value,row,index){
    var html = "";
    if(row.bizmc!=null && row.bizmc!=""){
        html = html + "<span>"+row.bizmc + "</span>"
    }
    return html;
}

function edit_dfformat(value,row,index){
        var df = "";
        if(row.dfbj!=null && row.dfbj!=""){
            df = row.dfbj;
        }else{
            if(row.df!=null && row.df!=""){
                df = row.df;
            }
        }
        return "<input id='df_"+index+"' value='"+df+"' text='"+df+"' name='df_"+index+"' οninput=\"value=value.replace(/[^\-?\d.]/g,'')\" style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'df\')\" required='required' autocomplete='off'></input>";
    }


function edit_bzformat(value,row,index){
    if(row.bz == null){
        row.bz = "";
    }
    var html = "<textarea id='bz_"+index+"'row='3' value='"+row.bz+"' text='"+row.bz+"' name='bz_"+index+"'  style='height: auto;resize: vertical;' onblur=\"checkSum('"+index+"',this,\'bz\')\" class='form-control'>"+row.bz+"</textarea>";
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
    if (zdmc == 'bz') {
        t_map.rows[index].bz = e.value;
    }else if (zdmc == 'df') {
        if(!(/^-?\d+$/.test(e.value) || /^-?\d+\.\d+$/.test(e.value))){
            t_map.rows[index].dfbj = e.value;
            t_map.rows[index].df = "0";
        }else{
            t_map.rows[index].df = e.value;
            t_map.rows[index].dfbj = '';
        }
        var df = parseFloat("0");
        var mf = parseFloat("0");
        var dfl = parseFloat("0");
        if(t_map.rows != null && t_map.rows.length > 0){
            for(var i=0;i<t_map.rows.length;i++){
                if(t_map.rows[i].dfbj == null || t_map.rows[i].dfbj ==''){
                    mf = mf + parseFloat(t_map.rows[i].bizfs);
                    if(t_map.rows[i].df!=null && t_map.rows[i].df!=''){
                        df = df + parseFloat(t_map.rows[i].df);
                    }
                }
            }
        }
        if(mf!=parseFloat("0.00")){
            dfl = (df/mf*parseFloat("100")).toFixed(2);
        }else{
            $.alert("请设置内容的评分标准！");
            return false;
        }
        var mfSpan = document.getElementById('mf');
        mfSpan.innerText = mf+"";
        var dfSpan = document.getElementById('df');
        dfSpan.innerText = df+"";
        var dflSpan = document.getElementById('dfl');
        dflSpan.innerText = dfl+"";
        var pj = "";
        var pjmc = "";
        var bizList = [];
        bizList = JSON.parse($("#performanceEditForm #pjJson").val());
        if(bizList!=null && bizList.length>0){
            for(var i=0;i<bizList.length;i++){
                var minNum = parseFloat("0.00");
                var maxNum = parseFloat("0.00");
                dflNum = parseFloat(dfl);
                if(bizList[i].cskz1!=null && bizList[i].cskz1!='' && bizList[i].cskz2!=null && bizList[i].cskz2!=''){
                    minNum = parseFloat(bizList[i].cskz1);
                    maxNum = parseFloat(bizList[i].cskz2);
                    if(minNum<=dflNum && dflNum<maxNum){
                        pj = bizList[i].csid;
                        pjmc = bizList[i].csmc;
                        break;
                    }
                }else if (bizList[i].cskz1!=null && bizList[i].cskz1!=''){
                    minNum = parseFloat(bizList[i].cskz1);
                    if(minNum<=dflNum){
                        pj = bizList[i].csid;
                        pjmc = bizList[i].csmc;
                        break;
                    }
                }else if (bizList[i].cskz2!=null && bizList[i].cskz2!=''){
                    maxNum = parseFloat(bizList[i].cskz2);
                    if(dflNum<maxNum){
                        pj = bizList[i].csid;
                        pjmc = bizList[i].csmc;
                        break;
                    }
                }
            }
        }
        var pjSpan = document.getElementById('pjmc');
        pjSpan.innerText = pjmc;
        $("#performanceEditForm #pj").val(pj);
    }
}



/**
 * 选择供应商列表
 * @returns
 */
function chooseGys(){
    var khjssj = $('#performanceEditForm #khjssj').val();
    if(khjssj==null || khjssj==""){
         $.error("请选择考核结束时间!");
        return false;
    }
    var url=$('#performanceEditForm #urlPrefix').val() + "/warehouse/supplier/pagedataSupplier?jxflg="+khjssj+"&gysid="+  $('#performanceEditForm #xgqGysid').val() + "&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择供应商',addGysConfig);
}
var addGysConfig = {
    width		: "1200px",
    modalName	:"addGysModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row=$('#choosesupplier_formSearch #tb_list').bootstrapTable('getSelections');
                if(sel_row.length == 1){
                    $("#performanceEditForm #gfmc").val(sel_row[0].gysmc);
                    $("#performanceEditForm #gysid").val(sel_row[0].gysid);
                    $("#performanceEditForm #gfbh").val(sel_row[0].gfbh);
                    $("#performanceEditForm #dh").val(sel_row[0].dh);
                    $("#performanceEditForm #dz").val(sel_row[0].dq);
                    $("#performanceEditForm #lxr").val(sel_row[0].lxr);
                    $("#performanceEditForm #cz").val(sel_row[0].cz);
                }else{
                    $.error("请选中一行");
                    return false;
                }
            },
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function chooseShr(){
    var url=$('#performanceEditForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择审核人',addShrConfig);
}
var addShrConfig = {
    width		: "800px",
    modalName	:"addShrModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var xzrys = $('#taskListFzrForm #xzrys').val();
                if(xzrys!=null&&xzrys!=""){
                    var sel_row = JSON.parse(xzrys);
                    var ids="";
                    var mcs="";
                    for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                        ids = ids + ","+ sel_row[i].yhid;
                        mcs = mcs + ","+ sel_row[i].zsxm;
                    }
                    ids = ids.substr(1);
                    mcs = mcs.substr(1);
                    $('#performanceEditForm #sybmshr').val(ids);
                    $('#performanceEditForm #shrmc').val(mcs);
                }else{
                    $.error("请至少选中一行");
                    return false;
                }
            },
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


$(function(){
    //添加日期控件
    laydate.render({
        elem: '#performanceEditForm #khkssj'
        ,theme: '#2381E9'
    });
    laydate.render({
            elem: '#performanceEditForm #khjssj'
            ,theme: '#2381E9'
        });
    var oTable_t=new editPerformance_TableInit();
    oTable_t.Init();
})
