/**
 * 刷新质量协议编号
 * @returns
 */
function refreshZlxybh(){
    var htlx = $("#editQualityForm #htlx").val();
    var yyxybh = $("#editQualityForm #yyxybh").val();
    if(htlx){
        $.ajax({
            type:'post',
            url:$('#editQualityForm #urlPrefix').val() + "/agreement/agreement/pagedataGetAgreementContractCode",
            cache: false,
            data: {"htlx":htlx,"yyxybh":yyxybh,"access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(result){
                if (result.status=='success'){
                    $("#editQualityForm #zlxybh").val(result.message);
                }else {
                    $.alert(result.message);
                }
            }
        });
    }else{
        $.alert("请先选择合同类型！");
    }
}

/**
 * 选择供应商列表
 * @returns
 */
function chooseGys(){
    var url=$('#editQualityForm #urlPrefix').val() + "/warehouse/supplier/pagedataSupplier?access_token=" + $("#ac_tk").val();
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
                    $("#editQualityForm #gysmc").val(sel_row[0].gysmc);
                    $("#editQualityForm #gysid").val(sel_row[0].gysid);
                    $("#editQualityForm #gfbh").val(sel_row[0].gfbh);
                    $("#editQualityForm #gfgllbmc").val(sel_row[0].gfgllbmc);
                    $('#editQualityForm #view_list').bootstrapTable('refresh',{pageNumber:1});
                    $.ajax({
                        async:false,
                        url: $('#editQualityForm #urlPrefix').val()+'/agreement/agreement/pagedataGetDocuments',
                        type: 'post',
                        dataType: 'json',
                        data : {"gysid":$("#editQualityForm #gysid").val(),"zlxyid":$("#editQualityForm #zlxyid").val(), "access_token":$("#ac_tk").val()},
                        success: function(data) {
                            $("#editQualityForm #htDiv").empty();
                            if(data.fjcfbDtos){
                                var html="";
                                for(var i=0;i<data.fjcfbDtos.length;i++){
                                    html+="<div id="+data.fjcfbDtos[i].fjid+">";
                                    html+="<div class='col-sm-12'>";
                                    html+="<label for=''>"+(i+1)+".</label>";
                                    html+="<button style='outline:none;margin-bottom:5px;padding:0px;' class='btn btn-link' type='button' title='下载' onclick=\"xz('"+data.fjcfbDtos[i].fjid+"');\">";
                                    html+="<span>"+data.fjcfbDtos[i].wjm+"</span>";
                                    html+="<span class='glyphicon glyphicon glyphicon-save'></span>";
                                    html+="</button>";
                                    html+="</div>";
                                    html+="</div>";
                                }
                                $("#editQualityForm #htDiv").append(html);
                            }
                        }
                    });
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
function chooseSjxmh(index) {
    var sjxmh=t_map.rows[index].sjxmh;
        var url=$('#editQualityForm #urlPrefix').val() + "/agreement/agreement/pagedataChooseAgreement?access_token=" + $("#ac_tk").val()+"&sjxmh="+sjxmh+"&index="+index;
        $.showDialog(url,'选择涉及项目号',chooseSjxmhConfig);
}

var chooseSjxmhConfig = {
    width		: "1200px",
    modalName	:"chooseSjxmhModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var index=$('#quality_choose #index').val();
                var sjxmh_t="";
                var sjxmhmc="";
                $("input[name='sjxmh']:checked").each(function(){
                    var csid=$(this).attr("value");
                    var csmc=$(this).attr("csmc");
                    sjxmh_t+=","+csid;
                    sjxmhmc+=","+csmc;
                });
                sjxmh_t=sjxmh_t.substr(1)
                sjxmhmc=sjxmhmc.substr(1)
                $("#sjxmh_" + index).val(sjxmh_t);
                $("#sjxmhmc_" + index).val(sjxmhmc);
                t_map.rows[index].sjxmh = sjxmh_t;
            },
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
function xz(fjid){
    jQuery('<form action="' + $("#editQualityForm #urlPrefix").val() + '/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}

/**
 * 点击删除文件
 * @param fjid
 * @param wjlj
 * @returns
 */
function del(fjid,wjlj){
    $.confirm('您确定要删除所选择的记录吗？',function(result){
        if(result){
            jQuery.ajaxSetup({async:false});
            var url= $("#editQualityForm #urlPrefix").val()+"/common/file/delFile";
            jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
                setTimeout(function(){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            $("#"+fjid).remove();
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



/**
 * 选择负责人列表
 * @returns
 */
function chooseFzr(){
    var url=$('#editQualityForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择负责人',addFzrConfig);
}
var addFzrConfig = {
    width		: "800px",
    modalName	:"addFzrModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#editQualityForm #fzr').val(sel_row[0].yhid);
                    $('#editQualityForm #fzrmc').val(sel_row[0].zsxm);
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#editQualityForm #urlPrefix').val() + "/common/habit/commonModUserHabit",
                        cache: false,
                        data: {"dxid":sel_row[0].yhid,"access_token":$("#ac_tk").val()},
                        dataType:'json',
                        success:function(data){}
                    });
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

//打开文件上传界面
function editfile(divName,btnName){
    $("#editQualityForm"+"  #"+btnName).hide();
    $("#editQualityForm"+"  #"+divName).show();
}
//关闭文件上传界面
function cancelfile(divName,btnName){
    $("#editQualityForm"+"  #"+btnName).show();
    $("#editQualityForm"+"  #"+divName).hide();
}
/**
 * 附件上传回调
 * @param fjid
 * @returns
 */
function displayUpInfo(fjid){
    if(!$("#editQualityForm #fjids").val()){
        $("#editQualityForm #fjids").val(fjid);
    }else{
        $("#editQualityForm #fjids").val($("#editQualityForm #fjids").val()+","+fjid);
    }
}

var viewQuality_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#editQualityForm #view_list").bootstrapTable({
            url:   $('#editQualityForm #urlPrefix').val()+'/agreement/agreement/pagedataGetDetails',         //请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#editQualityForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"zlxymx.lrsj",					// 排序字段
            sortOrder: "desc",                   // 排序方式
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
            uniqueId: "zlxymxid",                     // 每一行的唯一标识，一般为主键列
            showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns:  [
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
                    field: 'wlbm',
                    title: '物料编码',
                    titleTooltip:'物料编码',
                    width: '8%',
                    align: 'left',
                    visible: true
                },{
                    field: 'wlmc',
                    title: '物料名称',
                    titleTooltip:'物料名称',
                    width: '10%',
                    align: 'left',
                    formatter:view_wlmcformat,
                    visible: true
                },{
                    field: 'scs',
                    title: '生产厂家',
                    titleTooltip:'生产厂家',
                    width: '10%',
                    align: 'left',
                    visible: true
                },{
                    field: 'sjxmhmc',
                    title: '涉及项目号',
                    titleTooltip:'涉及项目号',
                    width: '8%',
                    align: 'left',
                    visible: true
                },{
                    field: 'jszb',
                    title: '技术指标',
                    titleTooltip:'技术指标',
                    width: '8%',
                    align: 'left',
                    visible: true
                },{
                    field: 'zlyq',
                    title: '质量要求',
                    titleTooltip:'质量要求',
                    width: '8%',
                    align: 'left',
                    visible: true
                },{
                    field: 'ysbz',
                    title: '验收标准',
                    titleTooltip:'验收标准',
                    width: '8%',
                    align: 'left',
                    visible: true
                },{
                    field: 'zlxybh',
                    title: '质量协议编号',
                    titleTooltip:'质量协议编号',
                    width: '8%',
                    align: 'left',
                    visible: true
                },{
                    field: 'cjsj',
                    title: '创建时间',
                    titleTooltip:'创建时间',
                    width: '7%',
                    align: 'left',
                    visible: true
                },{
                    field: 'kssj',
                    title: '开始时间',
                    titleTooltip:'开始时间',
                    width: '7%',
                    align: 'left',
                    visible: true
                },{
                    field: 'dqsj',
                    title: '到期时间',
                    titleTooltip:'到期时间',
                    width: '7%',
                    align: 'left',
                    visible: true
                },{
                    field: 'cz',
                    title: '操作',
                    width: '5%',
                    align: 'left',
                    formatter:view_czformat,
                    visible: true
                },{
                    field: 'fz',
                    title: '复制',
                    width: '5%',
                    align: 'left',
                    formatter:view_fzformat,
                    visible: true
                }],
            onLoadSuccess:function(map){

            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            },
        });
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "zlxymxid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            gysid: $('#editQualityForm #gysid').val(),
            zlxyid: $('#editQualityForm #zlxyid').val()
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit;
}

/**
 * 物料名称格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function view_wlmcformat(value,row,index){
   if(row.wlmc){
       return row.wlmc;
   }else{
       return row.fwmc;
   }
}

/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function view_czformat(value,row,index){
    var html="";
    if("1"==row.zt){
        html = "<span style='margin-left:5px;' class='btn btn-danger' title='停用' onclick=\"stopUsing('" + row.zlxymxid + "')\" >停用</span>";
    }else{
        html = "<span style='margin-left:5px;' class='btn btn-success' title='启用' onclick=\"startUsing('" + row.zlxymxid + "')\" >启用</span>";
    }

    return html;
}
/**
 * 复制功能
 * @param index
 * @param e
 * @param zdmc
 */
function view_fzformat(value,row,index){
    return "<span style='margin-left:5px;' class='btn btn-success' title='复制' onclick=\"copyHistory('" + row.zlxymxid + "')\" >复制</span>";
}
function copyHistory(zlxymxid) {
    $.ajax({
        async:false,
        url: $('#editQualityForm #urlPrefix').val()+'/agreement/agreement/pagedataGetAgreementDetails',
        type: 'post',
        dataType: 'json',
        data : {"zlxymxid":zlxymxid, "access_token":$("#ac_tk").val()},
        success: function(data) {
            var json=JSON.parse($("#editQualityForm #zlxymx_json").val());
            var sz = {"wlid":'',"wlbm":'',"wlmc":'',"scs":'',"fwmc":'',"sjxmh":'',"sjxmhmc":'',"jszb":'',"zlyq":'',"ysbz":'',"xh":json.length};
            sz.wlid = data.zlxymxDto.wlid;
            sz.wlmc = data.zlxymxDto.wlmc;
            sz.wlbm = data.zlxymxDto.wlbm;
            sz.scs = data.zlxymxDto.scs;
            sz.sjxmh = data.zlxymxDto.sjxmh;
            sz.sjxmhmc = data.zlxymxDto.sjxmhmc;
            sz.jszb = data.zlxymxDto.jszb;
            sz.zlyq = data.zlxymxDto.zlyq;
            sz.ysbz = data.zlxymxDto.ysbz;
            t_map.rows.push(sz);
            json.push(sz);
            $("#editQualityForm #edit_list").bootstrapTable('load',t_map);
            $("#editQualityForm #zlxymx_json").val(JSON.stringify(json));
        }
    });
}
//弹出DIV
function showDiv(index,e,zdmc){
    $("#otherdiv").attr("style","display:block;left:65%;top:40%;");
    $("#otherdiv").attr("index",index);
    $("#otherdiv").attr("zdmc",zdmc);
    var value=$("#editQualityForm #"+zdmc+"_"+index).val();
    $("#nrdiv").text(value);
    $("#nrdiv").val(value);
    if(zdmc=='jszb'){
        $("#nrdiv").attr("placeholder"," ");
    }else if(zdmc=='zlyq'){
        $("#nrdiv").attr("placeholder","  ");
    }else if(zdmc=='ysbz'){
        $("#nrdiv").attr("placeholder","  ");
    }
    $("#otherdiv #nrdiv").focus();
}
//关闭弹出框
function canceldiv(){
    $("#otherdiv").attr("style","display:none;");
}
/**
 * 停用操作
 * @param index
 * @param event
 * @returns
 */
function stopUsing(zlxymxid){
    $.ajax({
        async:false,
        url: $('#editQualityForm #urlPrefix').val()+'/agreement/agreement/pagedataOperateDetail',
        type: 'post',
        dataType: 'json',
        data : {"zlxymxid":zlxymxid, "zt":"0", "access_token":$("#ac_tk").val()},
        success: function(data) {
            if(data.status == 'fail'){
                $.error("异常出错！");
            }
            $('#editQualityForm #view_list').bootstrapTable('refresh');
        }
    });
}

/**
 * 启用操作
 * @param index
 * @param event
 * @returns
 */
function startUsing(zlxymxid){
    $.ajax({
        async:false,
        url: $('#editQualityForm #urlPrefix').val()+'/agreement/agreement/pagedataOperateDetail',
        type: 'post',
        dataType: 'json',
        data : {"zlxymxid":zlxymxid, "zt":"1", "access_token":$("#ac_tk").val()},
        success: function(data) {
            if(data.status == 'fail'){
                $.error("异常出错！");
            }
            $('#editQualityForm #view_list').bootstrapTable('refresh');
        }
    });
}

var t_map=[];
var editQuality_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#editQualityForm #edit_list").bootstrapTable({
            url:   $('#editQualityForm #urlPrefix').val()+'/agreement/agreement/pagedataAgreementMx',         //请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#editQualityForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"zlxymx.lrsj",					// 排序字段
            sortOrder: "desc",                   // 排序方式
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
            uniqueId: "zlxymxid",                     // 每一行的唯一标识，一般为主键列
            showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns:  [
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
                    field: 'wlbm',
                    title: '物料编码',
                    width: '10%',
                    align: 'left',
                    visible: true
                },{
                    field: 'wlmc',
                    title: '物料名称',
                    width: '15%',
                    align: 'left',
                    formatter:edit_wlmcformat,
                    visible: true
                },{
                    field: 'scs',
                    title: '生产厂家',
                    width: '10%',
                    align: 'left',
                    visible: true
                },{
                    field: 'sjxmhmc',
                    title: '涉及项目号',
                    width: '10%',
                    align: 'left',
                    formatter:edit_sjxmhformat,
                    visible: true
                },{
                    field: 'jszb',
                    title: '技术指标',
                    width: '15%',
                    align: 'left',
                    formatter:edit_jszbformat,
                    visible: true
                },{
                    field: 'zlyq',
                    title: '质量要求',
                    width: '15%',
                    align: 'left',
                    formatter:edit_zlyqformat,
                    visible: true
                },{
                    field: 'ysbz',
                    title: '验收标准',
                    width: '15%',
                    align: 'left',
                    formatter:edit_ysbzformat,
                    visible: true
                },{
                    field: 'cz',
                    title: '操作',
                    width: '5%',
                    align: 'left',
                    formatter:edit_czformat,
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
        });
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            access_token:$("#ac_tk").val(),
            zlxyid: $('#editQualityForm #zlxyid').val()
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit;
}


/**
 * 物料名称格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function edit_wlmcformat(value,row,index){
    var html="";
    if(!row.wlmc){
        if(row.fwmc == null){
            row.fwmc = "";
        }
        html = "<input id='fwmc_"+index+"' value='"+row.fwmc+"' text='"+row.fwmc+"' name='fwmc_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'fwmc\')\" required='required' autocomplete='off'></input>";
    }else{
        html=row.wlmc;
    }
    return html;
}

/**
 * 涉及项目号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function edit_sjxmhformat(value,row,index){
    if(row.sjxmh == null){
        row.sjxmh = "";
    }
    if (!row.wlid){
        var html = "<input type='hidden' id='sjxmh_"+index+"' value='"+row.sjxmh+"' text='"+row.sjxmhmc+"' name='sjxmh_"+index+"' style='width:60%;border-radius:5px;border:1px solid #D5D5D5;' autocomplete='off'></input>" +
            "<input id='sjxmhmc_"+index+"' value='"+row.sjxmhmc+"' text='"+row.sjxmhmc+"' name='sjxmhmc_"+index+"' style='width:60%;border-radius:5px;border:1px solid #D5D5D5;'  autocomplete='off'></input>" +
            "<button type='button' onclick='chooseSjxmh(\"" + index + "\")' class='btn btn-primary'  style='height:25px !important;width:30%;margin-bottom:2px;font-size:10px'>新增</button>"+

            "<span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:5%;cursor:pointer;margin-left:3px;' title='复制涉及项目号并替换所有'  onclick=\"copySjxmh('"+index+"')\" autocomplete='off'><span>";
    }else {
        var html = "<input type='hidden' id='sjxmh_"+index+"' value='"+row.sjxmh+"' text='"+row.sjxmhmc+"' name='sjxmh_"+index+"' style='width:60%;border-radius:5px;border:1px solid #D5D5D5;' autocomplete='off'></input>" +
         "<input id='sjxmhmc_"+index+"' value='"+row.sjxmhmc+"' text='"+row.sjxmhmc+"' name='sjxmhmc_"+index+"' style='width:90%;border-radius:5px;border:1px solid #D5D5D5;'  autocomplete='off'></input>" +
            "<span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:5%;cursor:pointer;margin-left:3px;' title='复制涉及项目号并替换所有'  onclick=\"copySjxmh('"+index+"')\" autocomplete='off'><span>";
    }

    return html;
}
/**
 * 复制当前涉及项目号并替换全部
 * @param wlid
 * @returns
 */
function copySjxmh(index){
    var sjxmh=$("#editQualityForm #sjxmh_"+index).val();
    var sjxmhmc=$("#editQualityForm #sjxmhmc_"+index).val();
    if(sjxmh==null || sjxmh==""){
        $.confirm("请输入涉及项目号！");
    }else{
        for(var i=0;i<t_map.rows.length;i++){
            $("#editQualityForm #sjxmh_"+i).val(sjxmh);
            $("#editQualityForm #sjxmhmc_"+i).val(sjxmhmc);
        }
    }
    var data=JSON.parse($("#editQualityForm #zlxymx_json").val());
    if(data!=null && data.length>0){
        for(var j=0;j<data.length;j++){
            data[j].sjxmh=sjxmh;
            t_map.rows[j].sjxmh=sjxmh;
            t_map.rows[j].sjxmhmc=sjxmhmc;
        }
    }
    $("#editQualityForm #zlxymx_json").val(JSON.stringify(data));
}
/**
 * 复制当前jszb并替换全部
 * @param wlid
 * @returns
 */
function copyJszb(index){
    var jszb=$("#editQualityForm #jszb_"+index).val();
    if(jszb==null || jszb==""){
        $.confirm("请输入技术指标！");
    }else{
        for(var i=0;i<t_map.rows.length;i++){
            $("#editQualityForm #jszb_"+i).val(jszb);
        }
    }
    var data=JSON.parse($("#editQualityForm #zlxymx_json").val());
    if(data!=null && data.length>0){
        for(var j=0;j<data.length;j++){
            data[j].jszb=jszb;
            t_map.rows[j].jszb=jszb;
        }
    }
    $("#editQualityForm #zlxymx_json").val(JSON.stringify(data));
}
/**
 * 复制当前涉及项目号并替换全部
 * @param wlid
 * @returns
 */
function copyYsbz(index){
    var ysbz=$("#editQualityForm #ysbz_"+index).val();
    if(ysbz==null || ysbz==""){
        $.confirm("请输入验收标准！");
    }else{
        for(var i=0;i<t_map.rows.length;i++){
            $("#editQualityForm #ysbz_"+i).val(ysbz);
        }
    }
    var data=JSON.parse($("#editQualityForm #zlxymx_json").val());
    if(data!=null && data.length>0){
        for(var j=0;j<data.length;j++){
            data[j].ysbz=ysbz;
            t_map.rows[j].ysbz=ysbz;
        }
    }
    $("#editQualityForm #zlxymx_json").val(JSON.stringify(data));
}
/**
 * 复制当前涉及项目号并替换全部
 * @param wlid
 * @returns
 */
function copyZlyq(index){
    var zlyq=$("#editQualityForm #zlyq_"+index).val();
    if(zlyq==null || zlyq==""){
        $.confirm("请输入质量要求！");
    }else{
        for(var i=0;i<t_map.rows.length;i++){
            $("#editQualityForm #zlyq_"+i).val(zlyq);
        }
    }
    var data=JSON.parse($("#editQualityForm #zlxymx_json").val());
    if(data!=null && data.length>0){
        for(var j=0;j<data.length;j++){
            data[j].zlyq=zlyq;
            t_map.rows[j].zlyq=zlyq;
        }
    }
    $("#editQualityForm #zlxymx_json").val(JSON.stringify(data));
}
/**
 * 技术指标格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function edit_jszbformat(value,row,index){
    if(row.jszb == null){
        row.jszb = "";
    }
    var html = "<input id='jszb_"+index+"' value='"+row.jszb+"' text='"+row.jszb+"' name='jszb_"+index+"' style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'jszb\')\" autocomplete='off' onFocus=\"showDiv('"+index+"',this,\'jszb\')\"></input><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='复制技术指标并替换所有'  onclick=\"copyJszb('"+index+"')\" autocomplete='off'><span>";
    return html;
}
/**
 * 技术指标格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function edit_zlyqformat(value,row,index){
    if(row.zlyq == null){
        row.zlyq = "";
    }
    var html = "<input id='zlyq_"+index+"' value='"+row.zlyq+"' text='"+row.zlyq+"' name='zlyq_"+index+"' style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'zlyq\')\" autocomplete='off' onFocus=\"showDiv('"+index+"',this,\'zlyq\')\"></input><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='复制质量要求并替换所有'  onclick=\"copyZlyq('"+index+"')\" autocomplete='off'><span>";
    return html;
}
/**
 * 初始化明细json字段
 * @returns
 */
function  setZlxym_json(){
    var json=[];
    var data=$('#editQualityForm #edit_list').bootstrapTable('getData');//获取选择行数据
    for(var i=0;i<data.length;i++){
            var sz = {"wlid":'',"wlbm":'',"wlmc":'',"scs":'',"fwmc":'',"sjxmh":'',"sjxmhmc":'',"jszb":'',"zlyq":'',"ysbz":'',"xh":data.length};
            sz.wlid = data[i].wlid;
            sz.wlmc = data[i].wlmc;
            sz.wlbm = data[i].wlbm;
            sz.scs = data[i].scs;
            sz.fwmc = data[i].fwmc;
            sz.sjxmh = data[i].sjxmh;
            sz.sjxmhmc = data[i].sjxmhmc;
            sz.jszb = data[i].jszb;
            sz.zlyq = data[i].zlyq;
            sz.ysbz = data[i].ysbz;
            sz.xh = i;
            json.push(sz);
    }
    $("#editQualityForm #zlxymx_json").val(JSON.stringify(json));
}
//弹出框点击确定
function confirmdiv_t(){
    var nr=$("#otherdiv textarea").val();
    var zdmc=$("#otherdiv").attr("zdmc");
    var index=$("#otherdiv").attr("index");
    $("#nrdiv").text("");
    $("#nrdiv").val("");
    $("#editQualityForm #"+zdmc+"_"+index).val(nr);
    checkDeviceSum(index,nr,zdmc);
    $("#otherdiv").attr("style","display:none;");
}
/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @param bj
 * @returns
 */
function checkDeviceSum(index,e,zdmc){
    var data=JSON.parse($("#editQualityForm #zlxymx_json").val());
    for(var i=0;i<data.length;i++){
        if(data[i].xh == index){
            if(zdmc=='jszb'){
                data[i].jszb=e;
                t_map.rows[i].jszb=e;
            }else if(zdmc=='ysbz'){
                data[i].ysbz=e;
                t_map.rows[i].ysbz=e;
            }else if(zdmc=='zlyq'){
                data[i].zlyq=e;
                t_map.rows[i].zlyq=e;
            }
        }
    }
    $("#editQualityForm #zlxymx_json").val(JSON.stringify(data));
}
/**
 * 验收标准格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function edit_ysbzformat(value,row,index){
    if(row.ysbz == null){
        row.ysbz = "";
    }
    var html = "<input id='ysbz_"+index+"' value='"+row.ysbz+"' text='"+row.ysbz+"' name='ysbz_"+index+"' style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'ysbz\')\" autocomplete='off' onFocus=\"showDiv('"+index+"',this,\'ysbz\')\"></input><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='复制验收标准并替换所有'  onclick=\"copyYsbz('"+index+"')\" autocomplete='off'><span>";
    return html;
}

/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function edit_czformat(value,row,index){
    var html="";
    html = "<span style='margin-left:5px;' class='btn btn-default' title='删除' onclick=\"deleteDetail('" + index + "')\" >删除</span>";
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
    if (zdmc == 'fwmc') {
        t_map.rows[index].fwmc = e.value;
    }else if (zdmc == 'jszb') {
        t_map.rows[index].jszb = e.value;
    }else if (zdmc == 'ysbz') {
        t_map.rows[index].ysbz = e.value;
    }
}

/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function deleteDetail(index){
    t_map.rows.splice(index,1);
    $("#editQualityForm #edit_list").bootstrapTable('load',t_map);
}

function addWl() {
    var url = $("#editQualityForm #urlPrefix").val()+"/agreement/agreement/pagedataListMaterial?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择物料', chooseWlConfig);
}
var chooseWlConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseWlModal",
    formName	: "chooseMaterialForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chooseMaterialForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var sel = $("#chooseMaterialForm #yxwl").tagsinput('items');
                var json=JSON.parse($("#editQualityForm #zlxymx_json").val());
                var ids="";
                for(var i = 0; i < sel.length; i++){
                    var value = sel[i].value;
                    ids+=","+value;
                }
                //如果有选中数据
                if(ids){
                    ids=ids.substring(1);
                    //先将原表数据中未选中的数据删除
                    for (var i = t_map.rows.length-1; i >=0; i--) {
                        if(t_map.rows[i].wlid){
                            if(ids.indexOf(t_map.rows[i].wlid)==-1){
                                t_map.rows.splice(i,1);
                            }
                        }
                    }
                    $.ajax({
                        async:false,
                        url: $('#editQualityForm #urlPrefix').val()+'/agreement/agreement/pagedataGetMaterialDetails',
                        type: 'post',
                        dataType: 'json',
                        data : {"ids":ids, "access_token":$("#ac_tk").val()},
                        success: function(data) {
                            if(data.wlglDtos.length>0){
                                for (var i = 0; i < data.wlglDtos.length; i++) {
                                    var isFind=false;
                                    for (var j = 0; j < t_map.rows.length; j++) {
                                        if(data.wlglDtos[i].wlid==t_map.rows[j].wlid){
                                            isFind=true;
                                            break;
                                        }
                                    }
                                    if(!isFind){
                                        var sz = {"wlid":'',"wlbm":'',"wlmc":'',"scs":'',"fwmc":'',"sjxmh":'',"sjxmhmc":'',"jszb":'',"zlyq":'',"ysbz":'',"xh":json.length};
                                        sz.wlid = data.wlglDtos[i].wlid;
                                        sz.wlmc = data.wlglDtos[i].wlmc;
                                        sz.wlbm = data.wlglDtos[i].wlbm;
                                        sz.scs = data.wlglDtos[i].scs;
                                        sz.sjxmh = data.wlglDtos[i].sscplb;
                                        sz.sjxmhmc = data.wlglDtos[i].sjxmhmc;
                                        t_map.rows.push(sz);
                                        json.push(sz);
                                    }
                                }
                            }
                        }
                    });
                }else{//没有选中数据则将表中的物料清除
                    for (var i = t_map.rows.length-1; i >=0; i--) {
                        if(t_map.rows[i].wlid){
                            t_map.rows.splice(i,1);
                        }
                    }
                }
                $("#editQualityForm #edit_list").bootstrapTable('load',t_map);
                $("#editQualityForm #zlxymx_json").val(JSON.stringify(json));
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
 * 新增操作
 * @param index
 * @param event
 * @returns
 */
function addFw(){
    var data=JSON.parse($("#editQualityForm #zlxymx_json").val());
    var sz = {"wlid":'',"wlbm":'',"wlmc":'',"scs":'',"fwmc":'',"sjxmh":'',"sjxmhmc":'',"jszb":'',"zlyq":'',"ysbz":'',"xh":data.length};
    t_map.rows.push(sz);
    data.push(sz);
    $("#editQualityForm #edit_list").bootstrapTable('load',t_map);
    $("#editQualityForm #zlxymx_json").val(JSON.stringify(data));
}

$(function(){
    //添加日期控件
    laydate.render({
        elem: '#editQualityForm #cjsj'
        ,theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
        elem: '#editQualityForm #kssj'
        ,theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
        elem: '#editQualityForm #dqsj'
        ,theme: '#2381E9'
    });
    // 初始化明细json字段
    setTimeout(function() {
        setZlxym_json();
    }, 1000);
    var oTable=new viewQuality_TableInit();
    oTable.Init();
    var oTable_t=new editQuality_TableInit();
    oTable_t.Init();
    //初始化fileinput
    var oFileInput = new FileInput();
    var sign_params=[];
    sign_params.prefix=$("#editQualityForm #urlPrefix").val();
    oFileInput.Init("editQualityForm","displayUpInfo",2,1,"sign_file",null,sign_params);
    var formAction=$('#editQualityForm #formAction').val();
    if('modSaveAgreement'==formAction){
        $.ajax({
            async:false,
            url: $('#editQualityForm #urlPrefix').val()+'/agreement/agreement/pagedataGetDocuments',
            type: 'post',
            dataType: 'json',
            data : {"gysid":$("#editQualityForm #gysid").val(),"zlxyid":$("#editQualityForm #zlxyid").val(), "access_token":$("#ac_tk").val()},
            success: function(data) {
                $("#editQualityForm #htDiv").empty();
                if(data.fjcfbDtos){
                    var html="";
                    for(var i=0;i<data.fjcfbDtos.length;i++){
                        html+="<div id="+data.fjcfbDtos[i].fjid+">";
                        html+="<div class='col-sm-12'>";
                        html+="<label for=''>"+(i+1)+".</label>";
                        html+="<button style='outline:none;margin-bottom:5px;padding:0px;' class='btn btn-link' type='button' title='下载' onclick=\"xz('"+data.fjcfbDtos[i].fjid+"');\">";
                        html+="<span>"+data.fjcfbDtos[i].wjm+"</span>";
                        html+="<span class='glyphicon glyphicon glyphicon-save'></span>";
                        html+="</button>";
                        html+="</div>";
                        html+="</div>";
                    }
                    $("#editQualityForm #htDiv").append(html);
                }
            }
        });
    }
    jQuery('#editQualityForm .chosen-select').chosen({width: '100%'});
})
