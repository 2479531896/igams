var programs_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#programs_formSearch #programs_list').bootstrapTable({
            url: '/externalPrograms/externalPrograms/pagedataExternalProgramsList',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#programs_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一s下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"wbcx.lrsj",					// 排序字段
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
            uniqueId: "code",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            }, {
                field: 'wbcxid',
                title: 'ID',
                width: '10%',
                align: 'left',
                visible: false,
                sortable: true
            }, {
                field: 'wbcxmc',
                title: '名称',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'lx',
                title: '类型',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'wbcxdm',
                title: '代码',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'appid',
                title: 'appkey',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'secret',
                title: 'secret',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'token',
                title: 'token',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'gzpt',
                title: 'gzpt',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'agentid',
                title: 'agentid',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'jumpdingtalkurl',
                title: 'jumpdingtalkurl',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'aeskey',
                title: 'aeskey',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'cropid',
                title: 'cropid',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'sftb',
                title: '是否同步',
                width: '8%',
                align: 'left',
                visible: true,
                formatter:sftbformatter,
                sortable: true
            }, {
                field: 'px',
                title: '排序',
                width: '15%',
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
                programsDealById(row.wbcxid,'view',$("#programs_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#programs_formSearch #programs_list").colResizable({
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
            sortLastName: "wbcxid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getProgramsSearchData(map);
    };
    return oTableInit;
};
//是否收費格式化
function sftbformatter(value,row,index){
    if(row.sftb=='1'){
        return '是';
    }else {
        return '否';
    }
}
function getProgramsSearchData(map) {
    var cxtj = $("#programs_formSearch #cxtj").val();
    var cxnr = $.trim(jQuery('#programs_formSearch #cxnr').val());
    if (cxtj == "0") {
        map["entire"] = cxnr
    } else if (cxtj == '1'){
        map["wbcxdm"] = cxnr
    }else if (cxtj =='2'){
        map["wbcxmc"]=cxnr
    }else if (cxtj =='3'){
        map["lx"]=cxnr
    }
    return map;
}

function programsDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?wbcxid=" +id;
        $.showDialog(url,'详细信息',viewProgramsConfig);
    }else if(action =='add'){
        var url= tourl;
        $.showDialog(url,'增加信息',addProgramsConfig);
    }else if(action =='mod'){
        var url=tourl + "?wbcxid=" +id;
        $.showDialog(url,'修改信息',modProgramsConfig);
    }
}


function searchProgramsResult(isTurnBack){
    if(isTurnBack){
        $('#programs_formSearch #programs_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#programs_formSearch #programs_list').bootstrapTable('refresh');
    }
}

var programs_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_view = $("#programs_formSearch #btn_view");
        var btn_query = $("#programs_formSearch #btn_query");
        var btn_add = $("#programs_formSearch #btn_add");
        var btn_mod = $("#programs_formSearch #btn_mod");
        var btn_del = $("#programs_formSearch #btn_del");

        /*-----------------------模糊查询------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchProgramsResult(true);
            });
        }
        //---------------------------查看列表-----------------------------------
        btn_view.unbind("click").click(function(){
            var sel_row = $('#programs_formSearch #programs_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                programsDealById(sel_row[0].wbcxid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });

        /*---------------------------新增信息-----------------------------------*/
        btn_add.unbind("click").click(function(){
            programsDealById(null,"add",btn_add.attr("tourl"));
        });

        /*---------------------------编辑信息-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#programs_formSearch #programs_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                programsDealById(sel_row[0].wbcxid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------删除信息-----------------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#programs_formSearch #programs_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].wbcxid;
            }
            ids = ids.substr(1);
            $.confirm('您确定要删除所选择的记录吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url= btn_del.attr("tourl");
                    jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    searchProgramsResult();
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
        });

    };
    return oInit;
};





var viewProgramsConfig = {
    width		: "800px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};



var addProgramsConfig = {
    width		: "1000px",
    modalName	: "addWbaqyzModal",
    formName	: "ajaxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#ajaxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchProgramsResult ();
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

var modProgramsConfig = {
    width		: "800px",
    modalName	: "modWbaqyzModal",
    formName	: "ajaxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#ajaxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchProgramsResult();
                            }
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

$(function(){
    //0.界面初始化
    // 1.初始化Table
    var oTable = new programs_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new programs_ButtonInit();
    oButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#programs_formSearch .chosen-select').chosen({width: '100%'});

});