var jnsjHzxx_turnOff=true;
var jnsjHzxx_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#jnsjHzxx_formSearch #jnsjHzxx_list').bootstrapTable({
            url: '/crf/cdiff/listJnsjHzxx',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#jnsjHzxx_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"rysj",					// 排序字段
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
            uniqueId: "jnsjhzid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'jnsjbgid',
                title: '报告ID',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'jnsjhzid',
                title: '患者ID',
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'xm',
                title: '姓名',
                width: '8%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'nl',
                title: '年龄',
                width: '5%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'rysj',
                title: '入院时间',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'cysj',
                title: '出院时间',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'cyzd',
                title: '出院诊断',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'blrzdwmc',
                title: '病例入组单位',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'ksmc',
                title: '科室',
                width: '8%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'blrzbh',
                title: '病例入组编号',
                width: '8%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'zyh',
                title: '住院号',
                width: '8%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'tbsj',
                title: '填表时间',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'jzlxmc',
                title: '就诊类型',
                width: '10%',
                align: 'left',
                formatter:jzlxformat,
                sortable:true,
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                jnsjHzxxById(row.jnsjbgid,'view',$("#jnsjHzxx_formSearch #btn_view").attr("tourl"));
            }
        });
        $("#jnsjHzxx_formSearch #jnsjHzxx_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        })
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
            sortLastName: "jnsjhzid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return jnsjHzxxSearchData(map);
    };
    return oTableInit;
};
function jnsjHzxxSearchData(map){
    var cxtj=$("#jnsjHzxx_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#jnsjHzxx_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["xm"]=cxnr;
    }else if(cxtj=="1"){
        map["nl"]=cxnr;
    }else if(cxtj=="2"){
        map["blrzbh"]=cxnr;
    }else if(cxtj=="3"){
        map["zyh"]=cxnr;
    }else if(cxtj=="4"){
        map["cyzd"]=cxnr;
    }


    var rysjstart = jQuery('#jnsjHzxx_formSearch #rysjstart').val();
    map["rysjstart"] = rysjstart;
    var rysjend = jQuery('#jnsjHzxx_formSearch #rysjend').val();
    map["rysjend"] = rysjend;

    var cysjstart = jQuery('#jnsjHzxx_formSearch #cysjstart').val();
    map["cysjstart"] = cysjstart;
    var cysjend = jQuery('#jnsjHzxx_formSearch #cysjend').val();
    map["cysjend"] = cysjend;

    var tbsjstart = jQuery('#jnsjHzxx_formSearch #tbsjstart').val();
    map["tbsjstart"] = tbsjstart;
    var tbsjend = jQuery('#jnsjHzxx_formSearch #tbsjend').val();
    map["tbsjend"] = tbsjend;

    var jzlxs = jQuery('#jnsjHzxx_formSearch #jzlx_id_tj').val();
    map["jzlxs"] = jzlxs;
    var blrzdws = jQuery('#jnsjHzxx_formSearch #blrzdw_id_tj').val();
    map["blrzdws"] = blrzdws;
    var kss = jQuery('#jnsjHzxx_formSearch #ks_id_tj').val();
    map["kss"] = kss;
    var xbs=jQuery('#jnsjHzxx_formSearch #xb_id_tj').val()
    map["xbs"]=xbs;
    return map;
}

function jzlxformat(value,row,index){
    if(row.jzlx==0){
        var html="<span>"+"住ICU"+"</span>";
    }else if(row.jzlx==1){
        var html="<span>"+"门、急诊"+"</span>";
    }else if(row.jzlx==2){
        var html="<span>"+"住普通病房"+"</span>";
    }
    return html;
}

function jnsjHzxxById(id,action,tourl){
    if(!tourl){
        return;
    }

    if(action =='view'){
        var url= tourl + "?jnsjbgid=" +id;
        $.showDialog(url,'查看详情',viewjnsjHzxxConfig);
    }else  if(action =='mod'){
        var url= tourl + "?jnsjbgid=" +id;
        $.showDialog(url,'修改艰难梭菌报告信息',modjnsjHzxxConfig);
    }else if(action=="add"){
        var url=tourl;
        $.showDialog(url,'新增艰难梭菌报告信息',addJnsjConfig);
    }else if (action=="extend"){
        var url= tourl + "?jnsjbgid=" +id;
        $.showDialog(url,'扩展修改艰难梭菌报告信息',extendjnsjHzxxConfig);
    }
}

var extendjnsjHzxxConfig = {
    width		: "1000px",
    modalName	:"extendmodjnsjHzxxModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#hzxxExtendModJnsj_ajaxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#hzxxExtendModJnsj_ajaxForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"hzxxExtendModJnsj_ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchJnsjResult();
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    } else{
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

var addJnsjConfig = {
    width		: "1200px",
    modalName	:"addJnsjModal",
    offAtOnce	: false,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
//					$("#ajaxForm #txid").val($("#ajaxForm #txid").val().trim());
                //console.log($("#ajaxForm").html())
                // if(!$("#ajaxForm").valid()){// 表单验证
                //
                //     $.alert("请填写完整信息");
                //     return false;
                // }

                var $this = this;

                var sonData=this.setDataModel();
                var getCheck=this.getCheck();
                sonData["access_token"]=$("#ac_tk").val()
                if(!getCheck){
                    return false;
                }
                var opts = $this["options"]||{};
                // console.log(sonData)
                // console.log(obj)
                // console.log( $("#ajaxForm").html())
                //
                // alert(opts["ajaxForm"])
                // $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
                $.ajax({
                    url: "/crf/cdiff/insertJnsj",
                    type: "post",
                    dataType: 'json',
                    data: {"formeData":JSON.stringify(sonData),"access_token":$("#ac_tk").val()},
                    success: function(data) {

                        if(data.status== 'success'){
                            $.closeModal(opts.modalName);
                            searchJnsjResult();
                        }else if(data.status == "fail"){
                            preventResubmitForm(".modal-footer > button", false);
                            $.error(data.message,function() {
                            });
                        } else{
                            preventResubmitForm(".modal-footer > button", false);
                            $.alert(data.message,function() {
                            });
                        }

                    },
                    error: function (data) {

                        $.alert(data.message+'添加异常错误!');
                    }
                })
                // submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                //     if(responseText["status"] == 'success'){
                //         $.success(responseText["message"],function() {
                //             if(opts.offAtOnce){
                //                 $.closeModal(opts.modalName);
                //             }
                //             searchJnsjResult();
                //         });
                //     }else if(responseText["status"] == "fail"){
                //         preventResubmitForm(".modal-footer > button", false);
                //         $.error(responseText["message"],function() {
                //         });
                //     } else{
                //         preventResubmitForm(".modal-footer > button", false);
                //         $.alert(responseText["message"],function() {
                //         });
                //     }
                // },".modal-footer > button");
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var viewjnsjHzxxConfig = {
    width		: "1000px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var modjnsjHzxxConfig = {
    width		: "1200px",
    modalName	:"jnsjHzxxModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
//					$("#ajaxForm #txid").val($("#ajaxForm #txid").val().trim());
                //console.log($("#ajaxForm").html())
                // if(!$("#ajaxForm").valid()){// 表单验证
                //
                //     $.alert("请填写完整信息");
                //     return false;
                // }

                var $this = this;

                var sonData=this.setDataModel()
                sonData["access_token"]=$("#ac_tk").val()
                sonData["jnsjbgid"]=$("#jnsjbgid").val()
                var opts = $this["options"]||{};
                // console.log(sonData)
                // console.log(obj)
                // console.log( $("#ajaxForm").html())
                //
                // alert(opts["ajaxForm"])
                // $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
                $.ajax({
                    url: "/crf/cdiff/updateJnsj",
                    type: "post",
                    dataType: 'json',
                    data: {"formeData":JSON.stringify(sonData),"access_token":$("#ac_tk").val()},
                    success: function(data) {
                        if(data.status== 'success'){
                            $.closeModal(opts.modalName);
                            searchJnsjResult();
                        }else if(data.status == "fail"){
                            preventResubmitForm(".modal-footer > button", false);
                            $.error(data.message,function() {
                            });
                        } else{
                            preventResubmitForm(".modal-footer > button", false);
                            $.alert(data.message,function() {
                            });
                        }

                    },
                    error: function (data) {
                        $.alert(data.message+'修改异常错误!');
                    }
                })
                // submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                //     if(responseText["status"] == 'success'){
                //         $.success(responseText["message"],function() {
                //             if(opts.offAtOnce){
                //                 $.closeModal(opts.modalName);
                //             }
                //             searchJnsjResult();
                //         });
                //     }else if(responseText["status"] == "fail"){
                //         preventResubmitForm(".modal-footer > button", false);
                //         $.error(responseText["message"],function() {
                //         });
                //     } else{
                //         preventResubmitForm(".modal-footer > button", false);
                //         $.alert(responseText["message"],function() {
                //         });
                //     }
                // },".modal-footer > button");
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
function searchJnsjResult(isTurnBack){
    if(isTurnBack){
        $('#jnsjHzxx_formSearch #jnsjHzxx_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#jnsjHzxx_formSearch #jnsjHzxx_list').bootstrapTable('refresh');
    }
}
//提供给导出用的回调函数
function jnsjSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="payinfo.zfid";
    map["sortLastOrder"]="asc";
    map["sortName"]="payinfo.lrsj";
    map["sortOrder"]="asc";
    map["yhid"]=$("#yhid").val();
    return jnsjHzxxSearchData(map);
}
var jnsjHzxx_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_view = $("#jnsjHzxx_formSearch #btn_view");//查看
        var btn_query = $("#jnsjHzxx_formSearch #btn_query");//查询
        var btn_add = $("#jnsjHzxx_formSearch #btn_add");
        var btn_del = $("#jnsjHzxx_formSearch #btn_del");//阐述
        var btn_mod = $("#jnsjHzxx_formSearch #btn_mod");
        var btn_deal = $("#jnsjHzxx_formSearch #btn_deal");
        var btn_selectexport = $("#jnsjHzxx_formSearch #btn_selectexport");//选中导出
        var btn_searchexport = $("#jnsjHzxx_formSearch #btn_searchexport");//搜索导出
        var btn_extend = $("#jnsjHzxx_formSearch #btn_extend");//扩展修改
        //添加日期控件
        laydate.render({
            elem: '#jnsjHzxx_formSearch #rysjstart'
            ,type: 'date'
        });
        //添加日期控件
        laydate.render({
            elem: '#jnsjHzxx_formSearch #rysjend'
            ,type: 'date'
        });
        //添加日期控件
        laydate.render({
            elem: '#jnsjHzxx_formSearch #cysjstart'
            ,type: 'date'
        });
        //添加日期控件
        laydate.render({
            elem: '#jnsjHzxx_formSearch #cysjend'
            ,type: 'date'
        });
        //添加日期控件
        laydate.render({
            elem: '#jnsjHzxx_formSearch #tbsjstart'
            ,type: 'date'
        });
        //添加日期控件
        laydate.render({
            elem: '#jnsjHzxx_formSearch #tbsjend'
            ,type: 'date'
        });
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                jnsjHzxxResult(true);
            });
        }
        /* ---------------------------扩展修改-----------------------------------*/
        btn_extend.unbind("click").click(function(){
            var sel_row = $('#jnsjHzxx_formSearch #jnsjHzxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                jnsjHzxxById(sel_row[0].jnsjbgid,"extend",btn_extend.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-----------------------选中导出------------------------------------*/
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#jnsjHzxx_formSearch #jnsjHzxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].jnsjbgid;
                }
                ids = ids.substr(1);
                $.showDialog(exportPrepareUrl+"?ywid=JNSJ_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));//gnm功能名
            }else{
                $.error("请选择数据");
            }
        });
        /*-----------------------搜索导出------------------------------------*/
        btn_searchexport.unbind("click").click(function(){
            $.showDialog(exportPrepareUrl+"?ywid=JNSJ_SEARCH&expType=search&callbackJs=jnsjSearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
        /* --------------------------- 新增 -----------------------------------*/
        btn_add.unbind("click").click(function(){
            jnsjHzxxById(null, "add", btn_add.attr("tourl"));
        });
        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#jnsjHzxx_formSearch #jnsjHzxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                jnsjHzxxById(sel_row[0].jnsjbgid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------修改列表-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#jnsjHzxx_formSearch #jnsjHzxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                    jnsjHzxxById(sel_row[0].jnsjbgid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------删除-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#jnsjHzxx_formSearch #jnsjHzxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].jnsjbgid;
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
                                        jnsjHzxxResult();
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
        /* ------------------------------删除-----------------------------*/
        btn_deal.unbind("click").click(function(){
            var sel_row = $('#jnsjHzxx_formSearch #jnsjHzxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].tssqid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要处理所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_deal.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        jnsjHzxxResult();
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
        $("#jnsjHzxx_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(jnsjHzxx_turnOff){
                $("#jnsjHzxx_formSearch #searchMore").slideDown("low");
                jnsjHzxx_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#jnsjHzxx_formSearch #searchMore").slideUp("low");
                jnsjHzxx_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};



function jnsjHzxxResult(isTurnBack){
    //关闭高级搜索条件
    $("#jnsjHzxx_formSearch #searchMore").slideUp("low");
    jnsjHzxx_turnOff=true;
    $("#jnsjHzxx_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#jnsjHzxx_formSearch #jnsjHzxx_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#jnsjHzxx_formSearch #jnsjHzxx_list').bootstrapTable('refresh');
    }
}


$(function(){
    // 1.初始化Table
    var oTable = new jnsjHzxx_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new jnsjHzxx_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#jnsjHzxx_formSearch .chosen-select').chosen({width: '100%'});

    $("#jnsjHzxx_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});