var Question_turnOff=true;
var question_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#question_formSearch #question_list').bootstrapTable({
            url: $("#question_formSearch #urlPrefix").val()+'/train/question/pageGetListQuestion',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#question_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"tkmc",					// 排序字段
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
            showColumns: true,                  // 是否显示所有的列
            showRefresh: true,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "tkid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '1%'
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
                field: 'tkid',
                title: '题库ID',
                width: '15%',
                align: 'left',
                visible: false
            }, {
                field: 'tkmc',
                title: '题库名称',
                width: '25%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'tklxmc',
                title: '题库类型',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'tkbq',
                title: '题库标签',
                width: '10%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'danxts',
                title: '单选题数',
                width: '3%',
                align: 'left',
                visible: true
            }, {
                field: 'duoxts',
                title: '多选题数',
                width: '3%',
                align: 'left',
                visible: true
            }, {
                field: 'pdts',
                title: '判断题数',
                width: '3%',
                align: 'left',
                visible: true
            }, {
                field: 'tkts',
                title: '填空题数',
                width: '3%',
                align: 'left',
                visible: true
            }, {
                field: 'jdts',
                title: '简答题数',
                width: '3%',
                align: 'left',
                visible: true
            }, {
                field: 'ssgsmc',
                title: '所属公司',
                width: '3%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                questionById(row.tkid,'view',$("#question_formSearch #btn_view").attr("tourl"));
            }
        });
        $("#question_formSearch #question_list").colResizable({
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
            sortLastName: "tkid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return questionSearchData(map);
    };
    return oTableInit;
};
function questionSearchData(map){
    var cxtj=$("#question_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#question_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["tkmc"]=cxnr;
    }else if(cxtj=="1"){
        map["tklxmc"]=cxnr;
    }else if(cxtj=="2"){
        map["tkbq"]=cxnr;
    }
    var ssgss = jQuery('#question_formSearch #ssgs_id_tj').val();
    map["ssgss"] = ssgss.replace(/'/g, "");
    return map;
}

function questionById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#question_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?tkid=" +id;
        $.showDialog(url,'题库详细信息',viewQuestionConfig);
    }else if(action =='add'){
        var url=tourl;
        $.showDialog(url,'新增',addQuestionConfig);
    }else if(action =='mod'){
        var url=tourl + "?tkid=" +id;
        $.showDialog(url,'修改',modQuestionConfig);
    }
}

var viewQuestionConfig = {
    width		: "1200px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var addQuestionConfig = {
    width		: "1525px",
    modalName	: "addQuestionModal",
    formName	: "editQuestionForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "保 存",
            className : "btn-primary",
            callback : function() {
                // if(!$("#editQuestionForm").valid()){
                //     $.alert("题库明称，题库类型，题目内容，类型 为必填项！");
                //     return false;
                // }
                var $this = this;
                var opts = $this["options"]||{};

                $("#editQuestionForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editQuestionForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                questionResult();
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

var modQuestionConfig = {
    width		: "1525px",
    modalName	: "modQuestionModal",
    formName	: "editQuestionForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "保 存",
            className : "btn-primary",
            callback : function() {
                // if(!$("#editQuestionForm").valid()){
                //     $.alert("题库明称，题库类型，题目内容，类型 为必填项！");
                //     return false;
                // }
                var $this = this;
                var opts = $this["options"]||{};

                $("#editQuestionForm input[name='access_token']").val($("#ac_tk").val());
                $.confirm('是否更新已有分数？',function(result){
                    if(result){
                        $("#editQuestionForm #sfgx").val("1")
                    }else {
                        $("#editQuestionForm #sfgx").val("0")
                    }
                    submitForm(opts["formName"]||"editQuestionForm",function(responseText,statusText){
                        if(responseText["status"] == 'success'){
                            $.success(responseText["message"],function() {
                                if(opts.offAtOnce){
                                    $.closeModal(opts.modalName);
                                    questionResult();
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
                });
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var question_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_view = $("#question_formSearch #btn_view");
        var btn_query = $("#question_formSearch #btn_query");
        var btn_add = $("#question_formSearch #btn_add");
        var btn_del = $("#question_formSearch #btn_del");
        var btn_mod = $("#question_formSearch #btn_mod");

        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                questionResult(true);
            });
        }
        /* --------------------------- 新增 -----------------------------------*/
        btn_add.unbind("click").click(function(){
            questionById(null, "add", btn_add.attr("tourl"));
        });
        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#question_formSearch #question_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                questionById(sel_row[0].tkid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------修改列表-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#question_formSearch #question_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                questionById(sel_row[0].tkid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-----------------------显示隐藏------------------------------------*/
        $("#question_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(Question_turnOff){
                $("#question_formSearch #searchMore").slideDown("low");
                Question_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#question_formSearch #searchMore").slideUp("low");
                Question_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
        /* ------------------------------删除合同信息-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#question_formSearch #question_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].tkid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $('#question_formSearch #urlPrefix').val()+btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        questionResult();
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

    };
    return oInit;
};



function questionResult(isTurnBack){
    if(isTurnBack){
        $('#question_formSearch #question_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#question_formSearch #question_list').bootstrapTable('refresh');
    }
}


$(function(){
    // 1.初始化Table
    var oTable = new question_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new question_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#question_formSearch .chosen-select').chosen({width: '100%'});

    $("#question_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});