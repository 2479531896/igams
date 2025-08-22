var t_map = [];
var ResFirst_turnOff=true;
var SalesRes_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#salesRes_formSearch #salesRes_list").bootstrapTable({
            url: '/experimentS/experiments/pageGetListSalesResFirst',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#salesRes_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "sjsygl.lrsj",				// 排序字段
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
            showColumns: true,                  // 是否显示所有的列
            showRefresh: true,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "sjid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%',
            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '2%',
                align: 'left',
                visible:true
            },{
                field: 'sjid',
                title: '送检ID',
                titleTooltip:'送检ID',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'ybbh',
                title: '标本编号',
                titleTooltip:'标本编号',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'nbbm',
                title: '内部编码',
                titleTooltip:'内部编码',
                width: '7%',
                align: 'left',
                visible:true,
            },{
                field: 'hzxm',
                title: '患者姓名',
                titleTooltip:'患者姓名',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'jcxmmc',
                title: '检测项目',
                width: '6%',
                align: 'left',
                titleTooltip:'检测项目',
                visible:false
            },{
                field: 'yblxmc',
                title: '样本类型',
                titleTooltip:'样本类型',
                width: '6%',
                align: 'left',
                visible:true,
            },{
                field: 'jcdwmc',
                title: '检测单位',
                titleTooltip:'检测单位',
                width: '7%',
                align: 'left',
                visible:false,
            },{
                field: 'jsrq',
                title: '接收日期',
                titleTooltip:'接收日期',
                width: '12%',
                align: 'left',
                visible:false,
            },{
                field: 'qyrq',
                title: '取样日期',
                width: '12%',
                align: 'left',
                titleTooltip:'取样日期',
                visible:false
            },{
                field: 'jcsj',
                title: '检测时间',
                width: '12%',
                align: 'left',
                titleTooltip:'检测时间',
                visible:false
            },{
                field: 'syrq',
                title: '提取日期',
                width: '12%',
                align: 'left',
                titleTooltip:'提取日期',
                visible:false
            },{
                field: 'qtsyrq',
                title: '扩增日期',
                width: '12%',
                align: 'left',
                titleTooltip:'扩展日期',
                visible:false
            },{
                field: 'bgrq',
                title: '报告日期',
                width: '12%',
                align: 'left',
                titleTooltip:'报告日期',
                visible:false
            },{
                field: 'qf',
                title: '区分',
                width: '5%',
                align: 'left',
                titleTooltip:'区分',
                visible:true
            },{
                field: 'flg_qf',
                title: '区分标记',
                width: '6%',
                align: 'left',
                titleTooltip:'区分标记',
                visible:false
            },{
                field: 'jd',
                title: '进度',
                titleTooltip:'进度',
                width: '70%',
                align: 'left',
                formatter:ztFormat,
                visible:true,
            }],
            onLoadSuccess:function(map){
                t_map = map;
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                ResFirst_DealById(row.sjid,'view',$("#salesRes_formSearch #btn_view").attr("tourl"),row.qf,row.flg_qf);
            },
        });
        $("#salesRes_formSearch #salesRes_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true}
        );
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "sjxx.sjid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            jcxmdm: $("#salesRes_formSearch #jcxmdm").val(),
            jcxm: $("#salesRes_formSearch #jcxm").val(),
            limitColumns: "{'sjxxDto':'syglid,sjid,ybbh,nbbm,hzxm,yblxmc,jcxmmc,jcxmkzcs,flg_qf,qf,jsrq,qyrq,syrq,qtsyrq,jcsj,bgrq'}" //筛选出这部分字段用于列表显示
            // 搜索框使用
            // search:params.search
        };
        return getResFirstSearchData(map);
    };
    return oTableInit;
}

/**
 * 状态格式化
 * @returns
 */
function ztFormat(value,row,index) {
    //注意：避免sjxxDto继续添加字段，而使用现有的字段qtsyrq存储扩增时间
    var html="";
    var estimate_rq = "";
    var szz_toQy = parseInt($("#salesRes_formSearch #szz_toQy").val());
    var szz_toTq = parseInt($("#salesRes_formSearch #szz_toTq").val());
    var szz_toJc = parseInt($("#salesRes_formSearch #szz_toJc").val());
    var szz_toKz = parseInt($("#salesRes_formSearch #szz_toKz").val());
    var szz_toBg = parseInt($("#salesRes_formSearch #szz_toBg").val());
    var step_num=0;

    var jrsq = (row.jsrq==null||row.jsrq==undefined||row.jsrq=='')?"":"end";
    if (jrsq==""){
        estimate_rq = "" ;
        step_num=1;
    }

    var qyrq = (row.qyrq==null||row.qyrq==undefined||row.qyrq=='')?"":"end";
    if (jrsq !="" && qyrq=="" ){
        step_num=2;
        var timestamp = new Date(row.jsrq).getTime() + szz_toQy*60*1000;
        var date = new Date(timestamp ); //根据时间戳生成的时间对象
        var year = date.getFullYear();
        var month = (date.getMonth()+1)>=10?(date.getMonth()+1):"0"+(date.getMonth()+1);
        var datestr = (date.getDate())>=10?(date.getDate()):"0"+(date.getDate());
        var hour = (date.getHours())>=10?(date.getHours()):"0"+(date.getHours());
        var minutes = (date.getMinutes())>=10?(date.getMinutes()):"0"+(date.getMinutes());
        var secondes = (date.getSeconds())>=10?(date.getSeconds()):"0"+(date.getSeconds())
        var dateStr = year + "-" + month + "-"  + datestr + " " + hour + ":" + minutes + ":" + secondes;
        estimate_rq = dateStr.substring(2,16);
    }

    var syrq = (row.syrq==null||row.syrq==undefined||row.syrq=='')?"":"end";
    if (qyrq !="" && syrq==""){
        step_num=3;
        var timestamp = new Date(row.qyrq).getTime() + szz_toTq*60*1000;
        var date = new Date(timestamp ); //根据时间戳生成的时间对象
        var year = date.getFullYear();
        var month = (date.getMonth()+1)>=10?(date.getMonth()+1):"0"+(date.getMonth()+1);
        var datestr = (date.getDate())>=10?(date.getDate()):"0"+(date.getDate());
        var hour = (date.getHours())>=10?(date.getHours()):"0"+(date.getHours());
        var minutes = (date.getMinutes())>=10?(date.getMinutes()):"0"+(date.getMinutes());
        var secondes = (date.getSeconds())>=10?(date.getSeconds()):"0"+(date.getSeconds())
        var dateStr = year + "-" + month + "-"  + datestr + " " + hour + ":" + minutes + ":" + secondes;
        estimate_rq = dateStr.substring(2,16);
    }

    var kzsj = (row.qtsyrq==null||row.qtsyrq==undefined||row.qtsyrq=='')?"":"end";
    if (syrq !="" && kzsj==""){
        step_num=4;
        var timestamp = new Date(row.syrq).getTime() + szz_toKz*60*1000;
        var date = new Date(timestamp ); //根据时间戳生成的时间对象
        var year = date.getFullYear();
        var month = (date.getMonth()+1)>=10?(date.getMonth()+1):"0"+(date.getMonth()+1);
        var datestr = (date.getDate())>=10?(date.getDate()):"0"+(date.getDate());
        var hour = (date.getHours())>=10?(date.getHours()):"0"+(date.getHours());
        var minutes = (date.getMinutes())>=10?(date.getMinutes()):"0"+(date.getMinutes());
        var secondes = (date.getSeconds())>=10?(date.getSeconds()):"0"+(date.getSeconds())
        var dateStr = year + "-" + month + "-"  + datestr + " " + hour + ":" + minutes + ":" + secondes;
        estimate_rq = dateStr.substring(2,16);
    }

    var jcsj = (row.jcsj==null||row.jcsj==undefined||row.jcsj=='')?"":"end";
    if (kzsj !="" && jcsj==""){
        step_num=5;
        var timestamp = new Date(row.qtsyrq).getTime() + szz_toJc*60*1000;
        var date = new Date(timestamp ); //根据时间戳生成的时间对象
        var year = date.getFullYear();
        var month = (date.getMonth()+1)>=10?(date.getMonth()+1):"0"+(date.getMonth()+1);
        var datestr = (date.getDate())>=10?(date.getDate()):"0"+(date.getDate());
        var hour = (date.getHours())>=10?(date.getHours()):"0"+(date.getHours());
        var minutes = (date.getMinutes())>=10?(date.getMinutes()):"0"+(date.getMinutes());
        var secondes = (date.getSeconds())>=10?(date.getSeconds()):"0"+(date.getSeconds())
        var dateStr = year + "-" + month + "-"  + datestr + " " + hour + ":" + minutes + ":" + secondes;
        estimate_rq = dateStr.substring(2,16);
    }

    var bgrq = (row.bgrq==null||row.bgrq==undefined||row.bgrq=='')?"":"end";
    if (jcsj !="" && bgrq==""){
        step_num=6;
        var timestamp = new Date(row.jcsj).getTime() + szz_toBg*60*1000;
        var date = new Date(timestamp ); //根据时间戳生成的时间对象
        var year = date.getFullYear();
        var month = (date.getMonth()+1)>=10?(date.getMonth()+1):"0"+(date.getMonth()+1);
        var datestr = (date.getDate())>=10?(date.getDate()):"0"+(date.getDate());
        var hour = (date.getHours())>=10?(date.getHours()):"0"+(date.getHours());
        var minutes = (date.getMinutes())>=10?(date.getMinutes()):"0"+(date.getMinutes());
        var secondes = (date.getSeconds())>=10?(date.getSeconds()):"0"+(date.getSeconds())
        var dateStr = year + "-" + month + "-"  + datestr + " " + hour + ":" + minutes + ":" + secondes;
        estimate_rq = dateStr.substring(2,16);
    }

    var jrsq_t = (row.jsrq==null||row.jsrq==undefined||row.jsrq=='')?"":row.jsrq.substring(2,16);
    var qyrq_t = (row.qyrq==null||row.qyrq==undefined||row.qyrq=='')?"":row.qyrq.substring(2,16);
    var syrq_t = (row.syrq==null||row.syrq==undefined||row.syrq=='')?"":row.syrq.substring(2,16);
    var kzsj_t = (row.qtsyrq==null||row.qtsyrq==undefined||row.qtsyrq=='')?"":row.qtsyrq.substring(2,16);
    var jcsj_t = (row.jcsj==null||row.jcsj==undefined||row.jcsj=='')?"":row.jcsj.substring(2,16);
    var bgrq_t = (row.bgrq==null||row.bgrq==undefined||row.bgrq=='')?"":row.bgrq.substring(2,16);
    switch (step_num){
        //case 0:  jrsq_t = row.jsrq?row.jsrq.substring(2,16):""; break;
        case 1:  jrsq_t = jrsq_t==""?estimate_rq:jrsq_t; break;
        case 2:  qyrq_t = qyrq_t==""?estimate_rq:qyrq_t; break;
        case 3:  syrq_t = syrq_t==""?estimate_rq:syrq_t; break;
        case 4:  kzsj_t = kzsj_t==""?estimate_rq:kzsj_t; break;
        case 5:  jcsj_t = jcsj_t==""?estimate_rq:jcsj_t; break;
        case 6:  bgrq_t = bgrq_t==""?estimate_rq:bgrq_t; break;
    }

    html="<div style='padding-right:30px;padding-left:30px;display: flex; justify-content: center; align-items: center'>" +
        "    <ul class='audit_steps padding_10 '>" +
        "       <li style='width: 150px;"+(jrsq==""?"":"color:#6EAFED;")+"' class='"+jrsq+"' id='"+row.jsrq+"'><span>接收&nbsp;"+jrsq_t+"</span></li>" +
        "       <li style='width: 150px;"+(qyrq==""?"":"color:#6EAFED;")+"' class='"+qyrq+"' id='"+row.qyrq+"'><span>取样&nbsp;"+qyrq_t+"</span></li>" +

        "       <li style='width: 150px;"+(syrq==""?"":"color:#6EAFED;")+"' class='"+syrq+"' id='"+row.syrq+"'><span>提取&nbsp;"+syrq_t+"</span></li>" +
        "       <li style='width: 150px;"+(kzsj==""?"":"color:#6EAFED;")+"' class='"+kzsj+"' id='"+row.qtsyrq+"'><span>扩增&nbsp;"+kzsj_t+"</span></li>" +
        "       <li style='width: 150px;"+(jcsj==""?"":"color:#6EAFED;")+"' class='"+jcsj+"' id='"+row.jcsj+"'><span>检测&nbsp;"+jcsj_t+"</span></li>" +


        "       <li style='"+(bgrq==""?"":"color:#6EAFED;")+"'' class='"+bgrq+"' id='"+row.bgrq+"'><span>报告&nbsp;"+bgrq_t+"</span></li>" +
        "     </ul>" +
        "     <div className='clearfix'></div>" +
        "</div>";
    return html;
}

function getResFirstSearchData(map){
    var salesRes_select=$("#salesRes_formSearch #salesRes_select").val();
    var salesRes_input=$.trim(jQuery('#salesRes_formSearch #salesRes_input').val());
    if(salesRes_select=="0"){
        map["hzxm"]=salesRes_input
    }else if(salesRes_select=="1"){
        map["ybbh"]=salesRes_input
    }else if(salesRes_select=="2"){
        map["nbbm"]=salesRes_input
    }
    //标本类型
    var yblxs = jQuery('#salesRes_formSearch #yblx_id_tj').val();
    map["yblxs"] = yblxs;
    //检测单位
    var jcdws=$("#salesRes_formSearch #jcdw_id_tj").val();
    map["jcdws"]=jcdws
    // if(jQuery('#salesRes_formSearch #syrqflg').is(":checked")){
    //     map["syrqflg"] = "1";
    // }
    return map;
}
function searchSalesResFirstResult(isTurnBack){
    $("#salesRes_formSearch #searchMore").slideUp("low");
    ResFirst_turnOff=true;
    $("#salesRes_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#salesRes_formSearch #salesRes_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#salesRes_formSearch #salesRes_list').bootstrapTable('refresh');
    }
}
function ResFirst_DealById(id,action,tourl,qf,flg_qf){
    if(!tourl){
        return;
    }
    if(action=="view"){
        var url=tourl+"?sjid="+id+"&qf="+qf+"&flg_qf="+flg_qf+"&jcxmdm="+$("#jcxmdm").val();
        $.showDialog(url,'查看详细信息',viewExperimentConfig);
    }
}
var viewExperimentConfig = {
    width		: "900px",
    modalName	:"viewExperimentModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var SalesRes_oButton=function(){
    var oButtonInit = new Object();
    var postdata = {};
    oButtonInit.Init=function(){
        var btn_query=$("#salesRes_formSearch #btn_query");
        var btn_view = $("#salesRes_formSearch #btn_view");

        /*-----------------------模糊查询ok------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchSalesResFirstResult(true);
            });
        }
        /*-----------------------查看------------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#salesRes_formSearch #salesRes_list').bootstrapTable('getSelections');// 获取选择行数据
            if(sel_row.length==1){
                ResFirst_DealById(sel_row[0].sjid,"view",btn_view.attr("tourl"),sel_row[0].qf,sel_row[0].flg_qf);
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------------------------------------------*/
        /**显示隐藏**/
        $("#salesRes_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(ResFirst_turnOff){
                $("#salesRes_formSearch #searchMore").slideDown("low");
                ResFirst_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#salesRes_formSearch #searchMore").slideUp("low");
                ResFirst_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
    }
    return oButtonInit;
}


$(function(){
    var oTable = new SalesRes_TableInit();
    oTable.Init();

    var oButton = new SalesRes_oButton();
    oButton.Init();

    $("#salesRes_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

    jQuery('#salesRes_formSearch .chosen-select').chosen({width: '100%'});
})