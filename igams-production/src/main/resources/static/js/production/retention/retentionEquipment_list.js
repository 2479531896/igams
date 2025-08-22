var retention_turnOff=true;
var retention_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#retention_formSearch #retention_list').bootstrapTable({
            url: $("#retention_formSearch #urlPrefix").val()+'/retention/retention/pageGetListRetention',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#retention_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一s下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"jlsj",					// 排序字段
            sortOrder: "ASC",                   // 排序方式
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
            uniqueId: "sbjlid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '0.5%'
            }, {
                'field': '',
                'title': '序号',
                'align': 'center',
                'width': '2%',
                'sortable': 'true',
                'formatter': function (value, row, index) {
                    var options = $('#retention_formSearch #retention_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            }, {
                field: 'sbjlid',
                title: '设备记录id',
                width: '20%',
                align: 'left',
                visible: false,
                sortable: true
            }, {
                field: 'sbid',
                title: '设备id',
                width: '10%',
                align: 'left',
                visible: false,
                sortable: true
            }, {
                field: 'bhlbdm',
                title: '设备编号',
                width: '4%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'bhlbmc',
                title: '设备名称',
                width: '4%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'jlrymc',
                title: '记录人',
                width: '4%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'jlsj',
                title: '记录时间',
                width: '6%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'wd',
                title: '温度',
                width: '4%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'zdwd',
                title: '最低温度',
                width: '4%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'zgwd',
                title: '最高温度',
                width: '4%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'sd',
                title: '湿度',
                width: '4%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'zt',
                title: '状态',
                width: '4%',
                align: 'left',
                visible: true,
                formatter:ztformat
            }, {
                field: 'bz',
                title: '备注',
                width: '25%',
                align: 'left',
                visible: true,
                sortable: true
            },
            ],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                retentionById(row.sbjlid,'view',$("#retention_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#retention_formSearch #retention_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true}
        );
    };
    // 得到查询的参数
    oTableInit.queryParams = function(params){
        // 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
        // 例如 toolbar 中的参数
        // 如果 queryParamsType = ‘limit’ ,返回参数必须包含
        // limit, offset, search, sort, order
        // 否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
        // 返回false将会终止请求
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "wd", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getretentionSearchData(map);
    };
    return oTableInit;
};

function getretentionSearchData(map) {
    var cxtj = $("#retention_formSearch #cxtj").val();
    var cxnr = $.trim(jQuery('#retention_formSearch #cxnr').val());
    if (cxtj == "0") {
        map["jlrymc"] = cxnr
    }
    // 记录开始日期
    var jlsjstart = jQuery('#retention_formSearch #jlsjstart').val();
    map["jlsjstart"] = jlsjstart;
    // 记录结束日期
    var jlsjend = jQuery('#retention_formSearch #jlsjend').val();
    map["jlsjend"] = jlsjend;
    //设备编号
    var bhlbs=jQuery('#retention_formSearch #bhlb_id_tj').val();
    map["bhlbs"] = bhlbs.replace(/'/g, "");
    //设备名称
    var bhlbmcs=jQuery('#retention_formSearch #bhlbmcs_id_tj').val();
    map["bhlbmcs"] = bhlbmcs.replace(/'/g, "");
    //状态
    var wdzt = jQuery('#retention_formSearch #wdzt_id_tj').val();
    map["wdzt"] = wdzt;
    return map;
}

/**
 * 状态格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function ztformat(value,row,index){
    var wd = parseFloat(row.wd);
    var zdwd = parseFloat(row.zdwd);
    var zgwd = parseFloat(row.zgwd);
    var html="";
    if (wd&&zdwd&&zgwd){
        if (wd>=zdwd&&wd<=zgwd){
            html="<span style='color:green;'>正常</span>";
        }else if (wd<zdwd){
            html="<span style='color:#f0ad4e'>低于</span>";
        }else if (wd>zgwd){
            html="<span style='color:red;'>高于</span>";
        }
    }
    return html;
}

function retentionResult(isTurnBack){
    if(isTurnBack){
        $('#retention_formSearch #retention_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#retention_formSearch #retention_list').bootstrapTable('refresh');
    }
}



function retentionById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#retention_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?sbjlid=" +id;
        $.showDialog(url,'详细信息',viewretentionConfig);
    }else if(action =='record'){
        var url= tourl;
        $.showDialog(url,'记录',recordConfig);
    }else if(action =='mod'){
        var url= tourl+"?sbjlid="+id;
        $.showDialog(url,'修改',recordConfig);
    }
}


var retention_ButtonInit = function(){
    var oInit = new Object();

    oInit.Init = function () {
        var btn_view = $("#retention_formSearch #btn_view");
        var btn_query = $("#retention_formSearch #btn_query");
        var btn_record = $("#retention_formSearch #btn_record");
        var btn_mod = $("#retention_formSearch #btn_mod");
        var btn_del = $("#retention_formSearch #btn_del");
        //添加日期控件
        laydate.render({
            elem: '#jlsjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#jlsjend'
            ,theme: '#2381E9'
        });

        /*-----------------------模糊查询------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchretentionResult(true);
            });
        }
        //---------------------------查看列表-----------------------------------
        btn_view.unbind("click").click(function(){
            var sel_row = $('#retention_formSearch #retention_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                retentionById(sel_row[0].sbjlid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        //---------------------------记录-----------------------------------
        btn_record.unbind("click").click(function(){
            retentionById(null, "record", btn_record.attr("tourl"));
        });
        //---------------------------修改-----------------------------------
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#retention_formSearch #retention_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                retentionById(sel_row[0].sbjlid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-----------------------删除------------------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#retention_formSearch #retention_list').bootstrapTable('getSelections');//获取选择行数据
            if (sel_row.length==0) {
                $.error("请至少选中一行");
                return;
            }else{
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].sbjlid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchretentionResult();
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
        /**显示隐藏**/
        $("#retention_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(retention_turnOff){
                $("#retention_formSearch #searchMore").slideDown("low");
                retention_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#retention_formSearch #searchMore").slideUp("low");
                retention_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};


function searchretentionResult(isTurnBack){
    //关闭高级搜索条件
    $("#retention_formSearch #searchMore").slideUp("low");
    retention_turnOff=true;
    $("#retention_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#retention_formSearch #retention_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#retention_formSearch #retention_list').bootstrapTable('refresh');
    }
}



var sfbc=0;//是否继续保存
var viewretentionConfig = {
    width		: "800px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var recordConfig={
    width		: "1000px",
    modalName	: "recordModal",
    formName	: "recordForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#recordForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#recordForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"recordForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchretentionResult ();
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
$(function(){

    //0.界面初始化
    // 1.初始化Table
    var oTable = new retention_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new retention_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#retention_formSearch .chosen-select').chosen({width: '100%'});


    $("#retention_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});