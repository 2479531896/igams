
var train_turnOff=true;

var rosterViewpxxx_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#modTrainRecordRosterForm #pxxx_list').bootstrapTable({
            url: $("#modTrainRecordRosterForm #urlPrefix").val()+'/train/test/pagedataPxxx',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#modTrainRecordRosterForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "gzgl.qwwcsj",				//排序字段
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
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [
                {
                    checkbox: true,
                    width: '3%'
                }
                ,{
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index+1;
                    },
                    width: '5%',
                    align: 'left',
                    visible:true
                }, {
                    field: 'qwwcsj',
                    title: '培训时间',
                    width: '10%',
                    align: 'left',
                    visible: true,
                    sortable: true
                }, {
                    field: 'jlbh',
                    title: '记录编号',
                    width: '12%',
                    align: 'left',
                    visible: true,
                    sortable: true
                }, {
                    field: 'ks',
                    title: '课时/小时',
                    width: '10%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'pxfsmc',
                    title: '培训方式',
                    width: '8%',
                    align: 'left',
                    visible: true,
                }, {
                    field: 'ywmc',
                    title: '培训标题',
                    width: '26%',
                    align: 'left',
                    formatter: ywmcformat,
                    visible: true,
                }, {
                    field: 'tgbj',
                    title: '考核结果',
                    width: '8%',
                    align: 'left',
                    formatter: function (value, row, index) {
                        if(row.tgbj&&row.tgbj=="1"){
                            return "<span style='color:green;'>合格</span>";
                        }else {
                            return "<span style='color:red;'>不合格</span>";
                        }
                    },
                    visible: true,
                }, {
                    field: 'ssgsmc',
                    title: '所属公司',
                    width: '16%',
                    align: 'left',
                    visible: true,
                    sortable: true
                }, {
                    field: 'bz',
                    title: '备注',
                    width: '12%',
                    align: 'left',
                    visible: true,
                    sortable: true
                },{
                    field: 'cz',
                    title: '操作',
                    width: '6%',
                    align: 'left',
                    formatter:czformat,
                    visible: true
                }],
            onLoadSuccess: function (map) {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
        });
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
            pageSize: 1,   //页面大小
            pageNumber: 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            fzr:$("#modTrainRecordRosterForm #yhid").val(),
            //搜索框使用
            //search:params.search
        };
        var cxnr=$.trim(jQuery('#modTrainRecordRosterForm #cxnr').val());
        if(cxnr){
            map["ywmc"]=cxnr
        }
        var tgbj = jQuery('#modTrainRecordRosterForm #tgbj_id_tj').val();
        map["tgbj"] = tgbj;
        // 期望完成开始日期
        var qwwcsjstart = jQuery('#modTrainRecordRosterForm #qwwcsjstart').val();
        map["qwwcsjstart"] = qwwcsjstart;

        // 期望完成结束日期
        var qwwcsjend = jQuery('#modTrainRecordRosterForm #qwwcsjend').val();
        map["qwwcsjend"] = qwwcsjend;
        var pxlbs = jQuery('#modTrainRecordRosterForm #pxlb_id_tj').val();
        map["pxlbs"] = pxlbs.replace(/'/g, "");
        var ssgss = jQuery('#modTrainRecordRosterForm #ssgs_id_tj').val();
        map["ssgss"] = ssgss.replace(/'/g, "");
        var pxfss = jQuery('#modTrainRecordRosterForm #pxfs_id_tj').val();
        map["pxfss"] = pxfss.replace(/'/g, "");
        return map;
    };
    return oTableInit;
};

function ywmcformat(value,row,index){
    if(row.ywid){
        return "<a href='javascript:void(0);' onclick=\"queryByYwid('"+row.ywid+"')\">"+row.ywmc+"</a>";
    }else{
        return row.ywmc
    }
}

function queryByYwid(ywid){
    var urlPrefix = $("#modTrainRecordRosterForm #urlPrefix").val();
    var url=urlPrefix+"/train/test/viewTrain?pxid="+ywid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'培训信息',viewTrainConfig);
}
var viewTrainConfig = {
    width		: "1400px",
    modalName	:"viewTrainModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

/**
 * 操作按钮格式化函数
 * @returns
 */
function czformat(value,row,index) {
    if(row.ywid){
        return ""
    }else{
        return "<span class='btn btn-success' onclick=\"updateTrainRecord('" + row.gzid + "',event)\" >修改</span>";
    }

}

function updateTrainRecord(gzid){
    var url=$("#modTrainRecordRosterForm #urlPrefix").val()+ "/roster/roster/pagedataModTrainRecordDetail?gzid=" +gzid;
    $.showDialog(url,'修改',modTrainRecordDetailConfig);
}

var modTrainRecordDetailConfig = {
    width		: "1000px",
    modalName	: "modTrainRecordDetailModal",
    formName	: "modTrainRecordDetailForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#modTrainRecordDetailForm").valid()){
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#modTrainRecordDetailForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"modTrainRecordDetailForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            preventResubmitForm(".modal-footer > button", false);
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                refresh();
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

function delTrainRecords(){
    var sel_row = $('#modTrainRecordRosterForm #pxxx_list').bootstrapTable('getSelections');//获取选择行数据
    if(sel_row.length ==0){
        $.error("请至少选中一行");
        return;
    }
    var ids="";
    for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        ids = ids + ","+ sel_row[i].gzid;
    }
    ids = ids.substr(1);
    $.confirm('您确定要删除所选择的记录吗？',function(result){
        if(result){
            jQuery.ajaxSetup({async:false});
            var url=$("#modTrainRecordRosterForm #urlPrefix").val()+"/systemmain/task/pagedataDelTask";
            jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                setTimeout(function(){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            refresh();
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

function refresh(){
    $('#modTrainRecordRosterForm #pxxx_list').bootstrapTable('refresh');
}

$(document).ready(function(){
  
    // 初始化页面
    var oTableTrain =rosterViewpxxx_TableInit();
    oTableTrain.Init();
    $("#modTrainRecordRosterForm #sl_searchMore").on("click", function(ev){
        var ev=ev||event;
        if(train_turnOff){
            $("#modTrainRecordRosterForm #searchMore").slideDown("low");
            train_turnOff=false;
            this.innerHTML="基本筛选";
        }else{
            $("#modTrainRecordRosterForm #searchMore").slideUp("low");
            train_turnOff=true;
            this.innerHTML="高级筛选";
        }
        ev.cancelBubble=true;
    });
    $("#modTrainRecordRosterForm #btn_query").unbind("click").click(function(){
        $('#modTrainRecordRosterForm #pxxx_list').bootstrapTable('refresh');
    });
});


