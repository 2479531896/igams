var perRedPacket_turnOff=true;

var perRedPacket_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#perRedPacket_formSearch #perRedPacket_list").bootstrapTable({
            url: $("#perRedPacket_formSearch #urlPrefix").val()+'/train/test/pageGetListRedPacket',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#perRedPacket_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "xtyh.yhm",				//排序字段
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
            uniqueId: "grksid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '0.2%',
                align: 'center',

            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '1%',
                align: 'left',
                visible:true
            },{
                field: 'grksid',
                title: '个人考试id',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: false,
            },{
                field: 'yhm',
                title: '工号',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: true,
            },{
                field: 'xm',
                title: '姓名',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: true,
            },{
                field: 'je',
                title: '金额',
                width: '2%',
                align: 'left',
                sortable: true,
                visible: true,
            },{
                field: 'pxbt',
                title: '培训名称',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true,
            },{
                field: 'hbmc',
                title: '红包名称',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true,
            },{
                field: 'sfff',
                title: '是否发放',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: true,
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                perRedPacketDealById(row.grksid,'view',$("#perRedPacket_formSearch #btn_view").attr("tourl"));
            }
        });
        $("#perRedPacket_formSearch #perRedPacket_list").colResizable({
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
            sortLastName: "grksgl.ksjssj", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return perRedPacketSearchData(map);
    }
    return oTableInit
}

function perRedPacketSearchData(map) {
    var cxtj = $("#perRedPacket_formSearch #cxtj").val();
    var cxnr = $.trim(jQuery('#perRedPacket_formSearch #cxnr').val());
    if (cxtj == "0") {
        map["entire"] = cxnr
    } else if (cxtj == "1") {
        map["hbmc"] = cxnr
    }else if (cxtj == "2") {
        map["pxbt"] = cxnr
    }else if (cxtj == "3") {
        map["xm"] = cxnr
    }else if (cxtj == "4") {
        map["yhm"] = cxnr
    }else if (cxtj == "5") {
        map["je"] = cxnr
    }
    var sfffs = jQuery('#perRedPacket_formSearch #sfff_id_tj').val();
    map["sfffs"] = sfffs.replace(/'/g, "");

    return map;
}
function perRedPacketDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#perRedPacket_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?grksid=" +id;
        $.showDialog(url,'查看',viewPerRedPacketConfig);
    }
}

/**
 * 查看页面模态框
 */
var viewPerRedPacketConfig={
    width		: "1000px",
    modalName	:"viewPerRedPacketModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}
//提供给导出用的回调函数
function perRedPacketData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="xtyh.yhm";
    map["sortLastOrder"]="asc";
    map["sortName"]="grksgl.ksjssj";
    map["sortOrder"]="asc";
    return perRedPacketSearchData(map);
}
var perRedPacket_ButtonInit = function () {
    var oInit = new Object();
    oInit.Init = function () {
        var btn_query = $("#perRedPacket_formSearch #btn_query");
        var btn_view = $("#perRedPacket_formSearch #btn_view");
        var btn_release = $("#perRedPacket_formSearch #btn_release");
        var btn_searchexport = $("#perRedPacket_formSearch #btn_searchexport");
        var btn_selectexport = $("#perRedPacket_formSearch #btn_selectexport");
        /* ---------------------------模糊查询-----------------------------------*/
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchPerRedPacketResult(true);
            });
        }
        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#perRedPacket_formSearch #perRedPacket_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                perRedPacketDealById(sel_row[0].grksid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });        /* ---------------------------发送红包-----------------------------------*/
        btn_release.unbind("click").click(function(){
            var sel_row = $('#perRedPacket_formSearch #perRedPacket_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
            }else{
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].grksid;
                }
                ids=ids.substr(1);
                $.confirm('是否发放红包？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $('#perRedPacket_formSearch #urlPrefix').val()+btn_release.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchPerRedPacketResult();
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
        });
        // 	  ---------------------------导出-----------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#perRedPacket_formSearch #perRedPacket_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].grksid;
                }
                ids = ids.substr(1);
                $.showDialog($('#perRedPacket_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=GRHBGL_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });

        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#perRedPacket_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=GRHBGL_SEARCH&expType=search&callbackJs=perRedPacketData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });

        /*-----------------------高级筛选------------------------------------*/
        $("#perRedPacket_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(perRedPacket_turnOff){
                $("#perRedPacket_formSearch #searchMore").slideDown("low");
                perRedPacket_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#perRedPacket_formSearch #searchMore").slideUp("low");
                perRedPacket_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
    };
    return oInit;
};


function searchPerRedPacketResult(isTurnBack) {
    //关闭高级搜索条件
    $("#perRedPacket_formSearch #searchMore").slideUp("low");
    perRedPacket_turnOff=true;
    $("#perRedPacket_formSearch #sl_searchMore").html("高级筛选");
    if (isTurnBack) {
        $('#perRedPacket_formSearch #perRedPacket_list').bootstrapTable('refresh', {pageNumber: 1});
    } else {
        $('#perRedPacket_formSearch #perRedPacket_list').bootstrapTable('refresh');
    }
}
$(function(){
    // 1.初始化Table
    var oTable = new perRedPacket_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new perRedPacket_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#perRedPacket_formSearch .chosen-select').chosen({width: '100%'});

});
