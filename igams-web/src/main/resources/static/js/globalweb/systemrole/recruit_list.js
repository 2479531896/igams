var Recruit_turnOff=true;
var Recruit_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#recruitFormSearch #rszp_list').bootstrapTable({
            url: '/recruit/recruit/pageGetListRecruitments',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#recruitFormSearch #toolbar', //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName:"fqsj",					//排序字段
            sortOrder: "desc",                   //排序方式
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
            uniqueId: "zpid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            }, {
                field: 'zpid',
                title: '招聘ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'xqgw',
                title: '需求岗位',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'gwbased',
                title: '岗位base地',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'fzqyjyy',
                title: '负责区域及医院',
                width: '10%',
                align: 'left',
                visible:true
            }, {
                field: 'xqrs',
                title: '需求人数',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'yjnx',
                title: '预计年薪(元)',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'gwxyrs',
                title: '岗位现有人数',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'gwyqzz',
                title: '岗位职责要求',
                width: '20%',
                align: 'left',
                visible: true
            }, {
                field: 'xwdgrq',
                title: '希望到岗日期',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'fqr',
                title: '发起人',
                width: '5%',
                align: 'left',
                visible: true
            }, {
                field: 'fqrbmmc',
                title: '发起人部门',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'fqsj',
                title: '发起时间',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'lyrs',
                title: '已录用人数',
                width: '6%',
                align: 'left',
                formatter:lyrsformat,
                sortable: true,
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                recruitDealById(row.zpid, 'view',$("#recruitFormSearch #btn_view").attr("tourl"));
            },
        });
        $("#recruitFormSearch #rszp_list").colResizable({
                liveDrag:true,
                gripInnerHtml:"<div class='grip'></div>",
                draggingClass:"dragging",
                resizeMode:'fit',
                postbackSafe:true,
                partialRefresh:true
            }
        );
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
            pageSize: params.limit,   //页面大小
            pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "fqr", //防止同名排位用
            sortLastOrder: "desc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        return getRecruitSearchData(map);
    };
    return oTableInit;
}

function getRecruitSearchData(map){
    var cxbt = $("#recruitFormSearch #cxbt").val();
    // 查询条件
    var cxnr = $.trim(jQuery('#recruitFormSearch #cxnr').val());
    if (cxbt == "0") {
        map["fqr"] = cxnr;
    }else if(cxbt == "1"){
        map["fqrbm"] = cxnr;
    }else if(cxbt == "2"){
        map["xqgw"] = cxnr;
    }

    var xwdgrqstart = jQuery('#recruitFormSearch #xwdgrqstart').val();
    map["xwdgrqstart"] = xwdgrqstart;
    var xwdgrqend = jQuery('#recruitFormSearch #xwdgrqend').val();
    map["xwdgrqend"] = xwdgrqend;
    var fqsjstart = jQuery('#recruitFormSearch #fqsjstart').val();
    map["fqsjstart"] = fqsjstart;
    var fqsjend = jQuery('#recruitFormSearch #fqsjend').val();
    map["fqsjend"] = fqsjend;

    return map;
}

//按钮动作函数
function recruitDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?zpid=" +id;
        $.showDialog(url,'查看',viewRecruitConfig);
    }else if(action =='add'){
        var url=tourl;
        $.showDialog(url,'新增',addRecruitConfig);
    }else if(action =='mod'){
        var url= tourl + "?zpid=" +id;
        $.showDialog(url,'修改',addRecruitConfig);
    }
}

/**
 * 请假次数格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function lyrsformat(value,row,index){
    var html = "";
    if(row.lyrs!=null){
        html += "<a href='javascript:void(0);' onclick=\"queryBylyrs('"+row.zpid+ "')\" >"+row.lyrs+"</a>";
    }
    return html;
}
function queryBylyrs(zpid){
    var url="/recruit/recruit/viewEmploy?zpid="+zpid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'录用详情',viewLyConfig);
}
var viewLyConfig = {
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

var viewRecruitConfig = {
    width		: "600px",
    modalName	: "viewRecruitModal",
    formName	: "viewRecruitForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var addRecruitConfig = {
    width		: "600px",
    modalName	: "addRecruitModal",
    formName	: "recruit_ajaxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#recruit_ajaxForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#recruit_ajaxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchRecruitResult();
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


var recruit_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_add = $("#recruitFormSearch #btn_add");
        var btn_mod = $("#recruitFormSearch #btn_mod");
        var btn_del = $("#recruitFormSearch #btn_del");
        var btn_view = $("#recruitFormSearch #btn_view");
        var btn_query = $("#recruitFormSearch #btn_query");
        var btn_selectexport = $("#recruitFormSearch #btn_selectexport");//选中导出
        var btn_searchexport = $("#recruitFormSearch #btn_searchexport");//搜索导出

        //添加日期控件
        laydate.render({
            elem: '#xwdgrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#xwdgrqend'
            ,theme: '#2381E9'
        });
        laydate.render({
            elem: '#fqsjstart'
            , theme: '#2381E9'
            , type: 'datetime'
            , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
        });
        laydate.render({
            elem: '#fqsjend'
            , theme: '#2381E9'
            , type: 'datetime'
            , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
        });
        //绑定搜索发送功能
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchRecruitResult(true);
            });
        }
        btn_add.unbind("click").click(function(){
            recruitDealById(null,"add",btn_add.attr("tourl"));
        });

        btn_mod.unbind("click").click(function(){
            var sel_row = $('#recruitFormSearch #rszp_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                recruitDealById(sel_row[0].zpid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        btn_view.unbind("click").click(function(){
            var sel_row = $('#recruitFormSearch #rszp_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                recruitDealById(sel_row[0].zpid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        //---------------------------------选中导出---------------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#recruitFormSearch #rszp_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].zpid;
                }
                ids = ids.substr(1);
                $.showDialog(exportPrepareUrl+"?ywid=RECRUIT_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });
        //搜索导出
        btn_searchexport.unbind("click").click(function(){
            $.showDialog(exportPrepareUrl+"?ywid=RECRUIT_SEARCH&expType=search&callbackJs=SearchRecruitData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
        btn_del.unbind("click").click(function(){
            var sel_row = $('#recruitFormSearch #rszp_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].zpid;
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
                                    searchRecruitResult();
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
        /**显示隐藏**/
        $("#recruitFormSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(Recruit_turnOff){
                $("#recruitFormSearch #searchMore").slideDown("low");
                Recruit_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#recruitFormSearch #searchMore").slideUp("low");
                Recruit_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    };

    return oInit;
};

function SearchRecruitData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="zp.fqsj";
    map["sortLastOrder"]="desc";
    map["sortName"]="zp.fqr";
    map["sortOrder"]="desc";
    return getRecruitSearchData(map);
}


function searchRecruitResult(isTurnBack){
    //关闭高级搜索条件
    $("#recruitFormSearch #searchMore").slideUp("low");
    Recruit_turnOff=true;
    $("#recruitFormSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#recruitFormSearch #rszp_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#recruitFormSearch #rszp_list').bootstrapTable('refresh');
    }
}

$(function(){

    //1.初始化Table
    var oTable = new Recruit_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new recruit_ButtonInit();
    oButtonInit.Init();

    //所有下拉框添加choose样式
    jQuery('#recruitFormSearch .chosen-select').chosen({width: '100%'});
});


