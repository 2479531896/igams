var UserGroup_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#usergroup_formSearch #usergroup_list").bootstrapTable({
            url: '/systemrole/user/pageGetListUserGroup',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#usergroup_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "yhz.lrsj",				// 排序字段
            sortOrder: "asc",                   // 0排序方式
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
            uniqueId: "yhzid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            },{
                field: 'yhzid',
                title: '用户组ID',
                width: '16%',
                align: 'left',
                visible:false
            },{
                field: 'yhzmc',
                title: '用户组名称',
                width: '14%',
                align: 'left',
                visible: true
            },{
                field: 'zsx',
                title: '组属性',
                width: '14%',
                align: 'left',
                formatter: zsxFormatter,
                visible: true
            },{
                title: '操作',
                align: 'center',
                width: '25%',
                valign: 'middle',
                formatter: 'yhzDealFormatter',
                visible: true
            },{
                field: 'lrry',
                title: '录入人员',
                align: 'center',
                width: '25%',
                valign: 'middle',
                visible: false
            }
            ],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                UserGroup_DealById(row.yhzid,'view',$("#usergroup_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#usergroup_formSearch #usergroup_list").colResizable({
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
            sortLastName: "yhz.yhzid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getUserGroupSearchData(map);
    };
    return oTableInit;
}

function getUserGroupSearchData(map){
    var usergroup_select=$("#usergroup_formSearch #usergroup_select").val();
    var usergroup_input=$.trim(jQuery('#usergroup_formSearch #usergroup_input').val());
    if(usergroup_select=="0"){
        map["yhzmc"]=usergroup_input
    }
    return map;
}

function searchUserGroupResult(isTurnBack){
    if(isTurnBack){
        $('#usergroup_formSearch #usergroup_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#usergroup_formSearch #usergroup_list').bootstrapTable('refresh');
    }
}

//列表操作按钮
//操作按钮样式
function yhzDealFormatter(value, row, index) {
    var id = row.yhzid;
    var lrry = row.lrry;
    var result = "<div class='col-sm-12 col-md-12 '><div class='row'><div class='btn-group'>";
    result += "<button class='btn btn-default col-md-6 col-sm-6' type='button' onclick=\"Configuser('" + id + "','" + lrry +"')\" title='添加组成员'><span class='glyphicon glyphicon-plus'>添加组成员</span></button>";
    result += "<button class='btn btn-default col-md-6 col-sm-6' type='button' onclick=\"UserGroup_DealById('" + id + "','creater','/systemrole/user/pagedataSetCreater')\" title='更改创建者'><span class='glyphicon glyphicon-menu-hamburger'>更改创建者</span></button>";
    result += "</div></div></div>";
    return result;
}

function Configuser(yhzid,lrry){
    $.ajax({
        type: 'post',
        url: "/systemrole/user/pagedataCheckLrry", //这里调用的检测列表的
        data: {"yhzid": yhzid, "lrry":lrry , "access_token": $("#ac_tk").val()},
        dataType: 'json',
        success: function (data) {
            if(data.status == true){
                UserGroup_DealById(yhzid, 'user','/systemrole/user/pagedataConfiguser');
            }else if(data.status == false){
                $.error(data.message);
            }
        }
    });
}

function zsxFormatter(value, row, index){
    if (value == '0'){
        return '共有'
    }else if (value == '1'){
        return '私有'
    }else {
        return '--'
    }
}

function UserGroup_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action=="add"){
        var url=tourl;
        $.showDialog(url,'新增用户组',addUserGroupConfig);
    }else if(action=="mod"){
        var url=tourl+"?yhzid="+id
        $.showDialog(url,'修改用户组',modUserGroupConfig);
    }else if(action=="view"){
        var url=tourl+"?yhzid="+id
        $.showDialog(url,'查看用户组信息',viewUserGroupConfig);
    }else if(action=="view"){
        var url=tourl+"?yhzid="+id
        $.showDialog(url,'查看用户组信息',viewUserGroupConfig);
    }else if(action =='user'){
        var url= tourl + "?yhzid=" +id;
        $.showDialog(url,'添加组成员',saveTableConfig);
    }
    else if (action == 'creater'){
        var url= tourl + "?yhzid=" +id;
        $.showDialog(url,'修改创建者',modGroupCreaterConfig);
    }
}

var modGroupCreaterConfig = {
    width		: "800px",
    modalName : "modGroupCreaterModal",
    buttons : {
        success : {
            label : "确定",
            className : "btn-primary",
            callback : function	() {

                if(!$("#ajaxForm").valid()){// 表单验证
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
                            }
                            searchUserGroupResult();
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

var saveTableConfig = {
    width		: "800px",
    modalName : "saveTreeModal",
    buttons : {
        success : {
            label : "确定",
            className : "btn-primary",
            callback : function	() {
                //调用指定函数方法\
                var formAction = $("#menutreeDiv #formAction").val();
                if(formAction){
                    eval(formAction+"()");
                }
                jQuery.closeModal("saveTreeModal");
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var addUserGroupConfig = {
    width		: "800px",
    modalName	:"addUserGroupModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                $("#ajaxForm #yhzmc").val($("#ajaxForm #yhzmc").val().trim());
                if(!$("#ajaxForm").valid()){// 表单验证
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
                            }
                            searchUserGroupResult();
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

var modUserGroupConfig = {
    width		: "800px",
    modalName	:"modUserGroupModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                $("#ajaxForm #yhzid").val($("#ajaxForm #yhzid").val());
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
                            }
                            searchUserGroupResult();
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


var viewUserGroupConfig = {
    width		: "800px",
    modalName	:"viewUserGroupModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


var UserGroup_oButton=function(){
    var oButtonInit = new Object();
    var postdata = {};
    oButtonInit.Init=function(){
        var btn_query=$("#usergroup_formSearch #btn_query");
        var btn_add = $("#usergroup_formSearch #btn_add");
        var btn_mod = $("#usergroup_formSearch #btn_mod");
        var btn_view = $("#usergroup_formSearch #btn_view");
        var btn_del = $("#usergroup_formSearch #btn_del");
        /*-----------------------模糊查询ok------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchUserGroupResult(true);
            });
        }
        /*-----------------------新增------------------------------------*/
        btn_add.unbind("click").click(function(){
            UserGroup_DealById(null,"add",btn_add.attr("tourl"));
        });
        /*-----------------------修改------------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#usergroup_formSearch #usergroup_list').bootstrapTable('getSelections');// 获取选择行数据
            if(sel_row.length==1){
                UserGroup_DealById(sel_row[0].yhzid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-----------------------查看------------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#usergroup_formSearch #usergroup_list').bootstrapTable('getSelections');// 获取选择行数据
            if(sel_row.length==1){
                UserGroup_DealById(sel_row[0].yhzid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-----------------------删除------------------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#usergroup_formSearch #usergroup_list').bootstrapTable('getSelections');// 获取选择行数据
            if (sel_row.length==0) {
                $.error("请至少选中一行");
                return;
            }else{
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {// 循环读取选中行数据
                    ids= ids + ","+ sel_row[i].yhzid;
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
                                        searchUserGroupResult();
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
        /*-------------------------------------------------------------*/
    }
    return oButtonInit;
}


$(function(){
    var oTable = new UserGroup_TableInit();
    oTable.Init();

    var oButton = new UserGroup_oButton();
    oButton.Init();

    jQuery('#usergroup_formSearch .chosen-select').chosen({width: '100%'});
})