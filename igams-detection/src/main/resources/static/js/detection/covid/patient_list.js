
var Patient_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function (){
        $('#patientformSearch #patient_list').bootstrapTable({
            url: '/detection/detection/pageGetListPatients',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#patientformSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "hzxx.xm",				//排序字段
            sortOrder: "ASC",                   //排序方式
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
            uniqueId: "hzid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns:[{
                checkbox: true
            }, {
                field: 'hzid',
                title: '患者ID',
                titleTooltip:'患者ID',
                sortable: true,
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'xm',
                title: '患者姓名',
                titleTooltip:'患者姓名',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'xb',
                title: '性别',
                titleTooltip:'性别',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'nl',
                title: '年龄',
                titleTooltip:'年龄',
                sortable: true,
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'sj',
                title: '手机',
                titleTooltip:'手机',
                sortable: true,
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'zjlxmc',
                title: '证件类型',
                titleTooltip:'证件类型',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'zjh',
                title: '证件号',
                titleTooltip:'证件号',
                width: '15%',
                align: 'left',
                visible:true
            },{
                field: 'xjzd',
                title: '现居住地',
                titleTooltip:'现居住地',
                width: '45%',
                align: 'left',
                visible:true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                PatientDealById(row.hzid,'view',$("#patientformSearch #btn_view").attr("tourl"));
            },
        });
        $("#patientformSearch #patient_list").colResizable({
                liveDrag:true,
                gripInnerHtml:"<div class='grip'></div>",
                draggingClass:"dragging",
                resizeMode:'fit',
                postbackSafe:true,
                partialRefresh:true
            }
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
            sortLastName: "hzxx.hzid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getPatientSearchData(map);
    };
    return oTableInit;
}

function getPatientSearchData(map){
    var cxtj=$("#patientformSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#patientformSearch #cxnr').val());
    var xb=$.trim(jQuery('#patientformSearch #xb').val());

    if(cxtj=="0"){
        map["xm"]=cxnr
    }else if(cxtj=="1"){
        map["nl"]=cxnr
    }else if(cxtj=="2"){
        map["sj"]=cxnr
    }else if(cxtj=="3"){
        map["zjh"]=cxnr
    }

    if(xb=="0"){
        map["xb"]="1"
    }else if(xb=="1"){
        map["xb"]="2"
    }
    return map;
}

// 按钮动作函数
function PatientDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?hzid="+id;
        $.showDialog(url,'详情',viewPatientConfig);
    }else if(action =='mod'){
        var url= tourl + "?hzid="+id;
        $.showDialog(url,'修改',modPatientConfig);
    }else if(action =='del'){
        $.confirm('您确定要删除所选择的记录吗？',function(result){
            if(result){
                jQuery.ajaxSetup({async:false});
                var url=tourl;
                jQuery.post(url,{ids:id,"access_token":$("#ac_tk").val()},function(responseText){
                    setTimeout(function(){
                        if(responseText["status"] == 'success'){
                            $.success(responseText["message"],function() {
                                searchPatientResult();
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
}


var Patient_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_query =$("#patientformSearch #btn_query");//模糊查询
        var btn_view =$("#patientformSearch #btn_view");
        var btn_mod =$("#patientformSearch #btn_mod");
        var btn_del = $("#patientformSearch #btn_del");
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchPatientResult(true);
            });
        };
        /*---------------------------查看详情信息表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#patientformSearch #patient_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                PatientDealById(sel_row[0].hzid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------修改信息表-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#patientformSearch #patient_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                PatientDealById(sel_row[0].hzid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        btn_del.unbind("click").click(function(){
            var sel_row = $('#patientformSearch #patient_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].hzid;
            }
            ids = ids.substr(1);
            PatientDealById(ids,"del",btn_del.attr("tourl"));
        });
    };
    return oInit;
};

function searchPatientResult(isTurnBack){
    if(isTurnBack){
        $('#patientformSearch #patient_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#patientformSearch #patient_list').bootstrapTable('refresh');
    }
}


/*查看详情信息模态框*/
var viewPatientConfig = {
    width		: "600px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var modPatientConfig = {
    width		: "700px",
    modalName	: "modPatientModal",
    formName	: "modPatientForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success7 : {
            label : "保存",
            className : "btn-primary",
            callback : function() {
                if(!$("#modPatientForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#modPatientForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"modPatientForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchPatientResult();
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
    var oTable = new Patient_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new Patient_ButtonInit();
    oButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#patientformSearch .chosen-select').chosen({width: '100%'});
    $("#patientformSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});
