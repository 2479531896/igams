
var Amplification_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#amplification_formSearch #tb_list').bootstrapTable({
            url: '/amplification/amplification/pageGetListAmplification',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#amplification_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"lrsj",					// 排序字段
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
            uniqueId: "kzid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true
            }, {
                'field': '',
                'title': '序号',
                'align': 'center',
                'width': '6%',
                'formatter': function (value, row, index) {
                    var options = $('#amplification_formSearch #tb_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            }, {
                field: 'kzmc',
                title: '扩增名称',
                width: '15%',
                align: 'left',
                visible: true
            }, {
                field: 'lrsj',
                title: '创建日期',
                width: '15%',
                align: 'left',
                visible: true
            }, {
                field: 'jcdwmc',
                title: '检测单位',
                width: '10%',
                align: 'left',
                visible: true
            },  {
                field: 'sjph1',
                title: 'DNA建库试剂盒',
                width: '18%',
                align: 'left',
                visible: true
            }, {
                field: 'sjph2',
                title: '逆转录试剂盒',
                width: '18%',
                align: 'left',
                visible: true
            }, {
                field: 'lrrymc',
                title: '创建人员',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'xgsj',
                title: '修改日期',
                width: '15%',
                align: 'left',
                visible: true
            }, {
                field: 'xgrymc',
                title: '修改人员',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'kzsj',
                title: '扩增时间',
                width: '15%',
                align: 'left',
                visible: true
            }, {
                field: 'zt',
                title: '状态',
                width: '8%',
                align: 'left',
                formatter:ztformat,
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                AmplificationButtonOperate(row.kzid,'view',$("#amplification_formSearch #btn_view").attr("tourl"));
            },
        });
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
            sortLastName: "lrsj", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getAmplificationSearchData(map);
    };

    return oTableInit;
};

//状态格式化
function ztformat(value,row,index){
    if(row.zt=="00"){
        var zt="<span style='color:red;'>未通过</span>";
    }else{
        var zt="<span >通过</span>";
    }
    return zt;
}

function getAmplificationSearchData(map){
    var cxtj=$("#amplification_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#amplification_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["lrrymc"]=cxnr
    }else if(cxtj=="1"){
        map["xgrymc"]=cxnr
    }else if(cxtj=="2"){
        map["wkmc"]=cxnr
    }else if(cxtj=="3"){
        map["jcdwmc"]=cxnr
    }else if(cxtj=="4"){
        map["sjph1"]=cxnr
    }else if(cxtj=="5") {
        map["sjph2"] = cxnr
    }else if(cxtj=="6") {
        map["nbbh"] = cxnr
    }
    return map;
}

function searchAmplificationResult(isTurnBack){
    if(isTurnBack){
        $('#amplification_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#amplification_formSearch #tb_list').bootstrapTable('refresh');
    }
}

function AmplificationButtonOperate(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?kzid=" +id;
        $.showDialog(url,'扩增详细信息',viewAmplificationConfig);
    }else if(action =='add'){
        var url= tourl;
        $.showDialog(url,'新增扩增信息',addAmplificationConfig);
    }else if(action =='mod'){
        var url= tourl + "?kzid=" +id;
        $.showDialog(url,'修改扩增信息',modAmplificationConfig);
    }
}


var Amplification_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        // 查询
        var btn_query=$("#amplification_formSearch #btn_query");
        var btn_view = $("#amplification_formSearch #btn_view");
        var btn_add = $("#amplification_formSearch #btn_add");
        var btn_mod = $("#amplification_formSearch #btn_mod");
        var btn_del = $("#amplification_formSearch #btn_del");
 
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchAmplificationResult(true);
            });
        }
        

        /*---------------------------查看-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#amplification_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                AmplificationButtonOperate(sel_row[0].kzid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------新增-----------------------------------*/
        btn_add.unbind("click").click(function(){
            AmplificationButtonOperate(null,"add",btn_add.attr("tourl"));
        });
        /*---------------------------编辑-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#amplification_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                AmplificationButtonOperate(sel_row[0].kzid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------删除-----------------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#amplification_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].kzid;
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
                                    searchAmplificationResult();
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


var viewAmplificationConfig = {
    width		: "1600px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var addAmplificationConfig = {
    width		: "2000px",
    modalName	: "addAmplificationModal",
    formName	: "editAmplificationForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success2 : {
            label : "复 制",
            className : "btn-danger",
            callback : function() {
                var str="";
                for(var i=1;i<97;i++){
                    if(i==96){
                        str=str+$("#editAmplificationForm #nbbh-"+i).val()+","+$("#editAmplificationForm #tqm-"+i).val()+","+$("#editAmplificationForm #id-"+i).val();
                    }else{
                        str=str+$("#editAmplificationForm #nbbh-"+i).val()+","+$("#editAmplificationForm #tqm-"+i).val()+","+$("#editAmplificationForm #id-"+i).val()+";";
                    }
                }
                copyText(str);
                return false;
            }
        },
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editAmplificationForm").valid()){
                    return false;
                }

                var nbbhs=[];
                var tqms=[];
                var xhs=[];
                var syglids=[];
                var nbbh;
                var tqm;
                var syglid='';
                for(var i=1;i<97;i++){
                    nbbh=$("#nbbh-"+i).val();
                    tqm=$("#tqm-"+i).val();
                    if(!tqm){
                        tqm='';
                    }
                    syglid=$("#id-"+i).val();
                    if(!syglid){
                        syglid='';
                    }
                    if(nbbh!=null && nbbh!=''){
                        nbbhs.push(nbbh);
                        tqms.push(tqm);
                        syglids.push(syglid);
                        xhs.push($("#nbbh-"+i).attr("xh"));
                    }
                }
                if(nbbhs.length==0){
                    $.error("请填入数据！");
                    return false;
                }
                $("#editAmplificationForm #nbbhs").val(nbbhs);
                $("#editAmplificationForm #xhs").val(xhs);
                $("#editAmplificationForm #tqms").val(tqms);
                $("#editAmplificationForm #syglids").val(syglids);
                $("#editAmplificationForm #zt").val("80");
                $("#editAmplificationForm input[name='access_token']").val($("#ac_tk").val());

                var $this = this;
                var opts = $this["options"]||{};
                submitForm(opts["formName"]||"editAmplificationForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchAmplificationResult();
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
        success1 : {
            label : "保 存",
            className : "btn-success",
            callback : function() {
                if(!$("#editAmplificationForm").valid()){
                    return false;
                }

                var nbbhs=[];
                var tqms=[];
                var xhs=[];
                var syglids=[];
                var nbbh;
                var tqm;
                var syglid='';
                for(var i=1;i<97;i++){
                    nbbh=$("#nbbh-"+i).val();
                    tqm=$("#tqm-"+i).val();
                    if(!tqm){
                        tqm='';
                    }
                    syglid=$("#id-"+i).val();
                    if(!syglid){
                        syglid='';
                    }
                    if(nbbh!=null && nbbh!=''){
                        nbbhs.push(nbbh);
                        tqms.push(tqm);
                        syglids.push(syglid);
                        xhs.push($("#nbbh-"+i).attr("xh"));
                    }
                }
                if(nbbhs.length==0){
                    $.error("请填入数据！");
                    return false;
                }
                $("#editAmplificationForm #nbbhs").val(nbbhs);
                $("#editAmplificationForm #xhs").val(xhs);
                $("#editAmplificationForm #tqms").val(tqms);
                $("#editAmplificationForm #syglids").val(syglids);
                $("#editAmplificationForm #zt").val("00");
                $("#editAmplificationForm input[name='access_token']").val($("#ac_tk").val());

                var $this = this;
                var opts = $this["options"]||{};
                submitForm(opts["formName"]||"editAmplificationForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchAmplificationResult();
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

copyContentH5: function copyText(content) {
    var copyDom = document.createElement('div');
    copyDom.innerText=content;
    copyDom.style.position='absolute';
    copyDom.style.top='0px';
    copyDom.style.right='-9999px';
    document.body.appendChild(copyDom);
    //创建选中范围
    var range = document.createRange();
    range.selectNode(copyDom);
    //移除剪切板中内容
    window.getSelection().removeAllRanges();
    //添加新的内容到剪切板
    window.getSelection().addRange(range);
    //复制
    var successful = document.execCommand('copy');
    copyDom.parentNode.removeChild(copyDom);
    try{
        var msg = successful ? "successful" : "failed";
        console.log('Copy command was : ' + msg);
    } catch(err){
        console.log('Oops , unable to copy!');
    }
}

var modAmplificationConfig = {
    width		: "2000px",
    modalName	: "modAmplificationModal",
    formName	: "editAmplificationForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editAmplificationForm").valid()){
                    return false;
                }

                var nbbhs=[];
                var tqms=[];
                var xhs=[];
                var syglids=[];
                var nbbh;
                var tqm;
                var syglid='';
                for(var i=1;i<97;i++){
                    nbbh=$("#nbbh-"+i).val();
                    tqm=$("#tqm-"+i).val();
                    if(!tqm){
                        tqm='';
                    }
                    syglid=$("#id-"+i).val();
                    if(!syglid){
                        syglid='';
                    }
                    if(nbbh!=null && nbbh!=''){
                        nbbhs.push(nbbh);
                        tqms.push(tqm);
                        syglids.push(syglid);
                        xhs.push($("#nbbh-"+i).attr("xh"));
                    }
                }
                if(nbbhs.length==0){
                    $.error("请填入数据！");
                    return false;
                }
                $("#editAmplificationForm #nbbhs").val(nbbhs);
                $("#editAmplificationForm #xhs").val(xhs);
                $("#editAmplificationForm #tqms").val(tqms);
                $("#editAmplificationForm #syglids").val(syglids);
                $("#editAmplificationForm #zt").val("80");
                $("#editAmplificationForm input[name='access_token']").val($("#ac_tk").val());

                var $this = this;
                var opts = $this["options"]||{};
                submitForm(opts["formName"]||"editAmplificationForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchAmplificationResult();
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
        success1 : {
            label : "保 存",
            className : "btn-success",
            callback : function() {
                if(!$("#editAmplificationForm").valid()){
                    return false;
                }

                var nbbhs=[];
                var tqms=[];
                var xhs=[];
                var syglids=[];
                var nbbh;
                var tqm;
                var syglid='';
                for(var i=1;i<97;i++){
                    nbbh=$("#nbbh-"+i).val();
                    tqm=$("#tqm-"+i).val();
                    if(!tqm){
                        tqm='';
                    }
                    syglid=$("#id-"+i).val();
                    if(!syglid){
                        syglid='';
                    }
                    if(nbbh!=null && nbbh!=''){
                        nbbhs.push(nbbh);
                        tqms.push(tqm);
                        syglids.push(syglid);
                        xhs.push($("#nbbh-"+i).attr("xh"));
                    }
                }
                if(nbbhs.length==0){
                    $.error("请填入数据！");
                    return false;
                }
                $("#editAmplificationForm #nbbhs").val(nbbhs);
                $("#editAmplificationForm #xhs").val(xhs);
                $("#editAmplificationForm #tqms").val(tqms);
                $("#editAmplificationForm #syglids").val(syglids);
                $("#editAmplificationForm #zt").val("00");
                $("#editAmplificationForm input[name='access_token']").val($("#ac_tk").val());

                var $this = this;
                var opts = $this["options"]||{};
                submitForm(opts["formName"]||"editAmplificationForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchAmplificationResult();
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
    var oTable = new Amplification_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new Amplification_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#amplification_formSearch .chosen-select').chosen({width: '100%'});
});