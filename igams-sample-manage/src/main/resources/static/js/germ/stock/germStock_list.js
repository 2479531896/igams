
var germ_turnOff=true;

var germ_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#germ_formSearch #tb_list').bootstrapTable({
            url: '/germ/inventory/pageGetListGerm',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#germ_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "jzkcxx.lrsj",				//排序字段
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
            uniqueId: "jzkcid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                width: '2%',
                checkbox: true,
            }, {
                title: '序',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '2%',
                align: 'left',
                visible:true
            },{
                field: 'jzkcid',
                title: '菌种库ID',
                width: '8%',
                align: 'center',
                visible: false,
                sortable: true
            }, {
                field: 'bh',
                title: '编号',
                width: '6%',
                align: 'center',
                sortable: true,
                visible: true
            }, {
                field: 'mc',
                title: '名称',
                width: '10%',
                align: 'center',
                visible: true,
                sortable: true
            }, {
                field: 'ph',
                title: '批号',
                width: '8%',
                align: 'center',
                visible: true,
                sortable: true
            },{
                field: 'rksl',
                title: '入库数量',
                width: '4%',
                align: 'center',
                visible: false,
                sortable: true
            },{
                field: 'kcl',
                title: '库存量',
                width: '4%',
                align: 'center',
                visible: true,
                sortable: true
            },{
                field: 'yds',
                title: '预定数',
                width: '4%',
                align: 'center',
                visible: true,
                sortable: true
            },{
                field: 'kbs',
                title: '拷贝数',
                width: '4%',
                align: 'center',
                visible: true,
                sortable: true
            },{
                field: 'flmc',
                title: '分类',
                width: '5%',
                align: 'center',
                visible: true,
                sortable: true
            },{
                field: 'lxmc',
                title: '类型',
                width: '5%',
                align: 'center',
                sortable: true,
                visible: true
            },{
                field: 'lrrymc',
                width: '6%',
                align: 'center',
                title: '录入人员',
                visible: false,
                sortable: true
            },{
                field: 'lrsj',
                width: '8%',
                align: 'center',
                title: '录入时间',
                visible: false,
                sortable: true
            },{
                field: 'ly',
                title: '来源',
                width: '15%',
                align: 'center',
                sortable: true,
                visible: true
            },{
                field: 'gg',
                title: '规格',
                width: '15%',
                align: 'center',
                visible: true,
                sortable: true
            }, {
                field: 'wz',
                title: '位置',
                width: '15%',
                align: 'center',
                visible: true,
                sortable: true
            },{
                field: 'bz',
                title: '备注',
                width: '15%',
                align: 'center',
                visible: true,
                sortable: true
            },{
                field: 'cz',
                title: '操作',
                width: '4%',
                align: 'center',
                visible: true,
                sortable: true,
                formatter:stock_formSearch_llformat,
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                germDealById(row.jzkcid, 'view',$("#germ_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#germ_formSearch #tb_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true}
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
            sortLastName: "jzkcxx.jzkcid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        var cxtj = $("#germ_formSearch #cxtj").val();
        // 查询条件
        var cxnr = $.trim(jQuery('#germ_formSearch #cxnr').val());
        // '0':'来源编号','1':'标本编号','2':'冰箱号','3':'抽屉号','4':'盒子号','5':'备注'
        if (cxtj == "0") {
            map["entire"] = cxnr;
        }else if (cxtj == "1") {
            map["mc"] = cxnr;
        }else if (cxtj == "2") {
            map["wz"] = cxnr;
        }else if (cxtj == "3") {
            map["ly"] = cxnr;
        }else if (cxtj == "4") {
            map["ph"] = cxnr;
        }else if (cxtj == "5") {
            map["gg"] = cxnr;
        }else if (cxtj == "6") {
            map["bh"] = cxnr;
        }else if (cxtj == "7") {
            map["kbs"] = cxnr;
        }
        // 菌种类型
        var jzlxs = jQuery('#germ_formSearch #jzlx_id_tj').val();
        map["jzlxs"] = jzlxs.replace(/'/g, "");

        // 菌种分类
        var jzfls = jQuery('#germ_formSearch #jzfl_id_tj').val();
        map["jzfls"] = jzfls.replace(/'/g, "");
        // 开始时间
        var lrsjstart = jQuery('#germ_formSearch #lrsjstart').val();
        map["lrsjstart"] = lrsjstart;

        // 结束时间
        var lrsjend = jQuery('#germ_formSearch #lrsjend').val();
        map["lrsjend"] = lrsjend;
        return map;
    };
    return oTableInit;
};

var addGermPickingConfig = {
    width		: "1200px",
    modalName	: "addGermPickingModal",
    formName	: "editGermPickingForm",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editGermPickingForm").valid()){
                    return false;
                }
                var json=[];
                var data=$('#editGermPickingForm #tb_list').bootstrapTable('getData');//获取选择行数据
                if(data.length<1){
                    $.error("请新增明细！");
                    return false;
                }
                for(var i=0;i<data.length;i++){
                    var sz={"jzkcid":'',"klsl":'',"qlsl":'',"yds":''};
                    sz.jzkcid=data[i].jzkcid;
                    var qlsl = parseFloat(data[i].qlsl);
                    var klsl = parseFloat(data[i].klsl);
                    if(qlsl.toFixed(2)-klsl.toFixed(2)>0){
                        $.error("第"+(i+1)+"行 请领数量不能大于可请领数！");
                        return false;
                    }
                    sz.klsl=data[i].klsl;
                    sz.qlsl=data[i].qlsl;
                    sz.yds=data[i].yds;
                    json.push(sz);
                }
                $("#editGermPickingForm #llmx_json").val(JSON.stringify(json));

                var $this = this;
                var opts = $this["options"]||{};
                $("#editGermPickingForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"editGermPickingForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            $.closeModal(opts.modalName);
                            $("#germ_formSearch #ids").val("1,2");
                            ll_count=0;
                            $("#ll_num").text(ll_count);
                            //提交审核
                            if(responseText["auditType"]!=null){
                                showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                    searchgermResult();
                                });
                            }else{
                                searchgermResult();
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


var modgermConfig = {
    width		: "1200px",
    modalName	: "modgermModal",
    formName	: "germEdit_Form",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#germEdit_Form").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#germEdit_Form input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"germEdit_Form",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchgermResult();
                            }

                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", 1);
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
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
var addgermConfig = {
    width		: "1200px",
    modalName	: "addgermModal",
    formName	: "germEdit_Form",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#germEdit_Form").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#germEdit_Form input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"germEdit_Form",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            searchgermResult();
                             $("#germEdit_Form #bh").val("");
                            $("#germEdit_Form #mc").val("");
                            $("#germEdit_Form #ph").val("");
                            $("#germEdit_Form #rksl").val("");
                            $("#germEdit_Form #kcl").val("");
                            $("#germEdit_Form #bz").val("");
                            $("#germEdit_Form #yds").val("");
                            $("#germEdit_Form #kbs").val("");

                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", 1);
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
                }); return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var viewgermConfig = {
    width		: "1000px",
    modalName	: "viewgermModal",
    formName	: "germStockView_Form",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var ll_count=0;
function ll_btnOinit(){
    if(ll_count>0){
        var strnum=ll_count;
        if(ll_count>99){
            strnum='99+';
        }
        var html="";
        html+="<span id='ll_num' style='position:absolute;background-color:red;color:white;border-radius: 15px;border:0px;width:20px;height:20px;top:-10px;font-size:10px;line-height:150%;'>"+strnum+"</span>";
        $("#germ_formSearch #btn_pickingcar").append(html);
    }else{
        $("#germ_formSearch #ll_num").remove();
    }
}
/**
 * 领料车操作格式化
 * @returns
 */
function stock_formSearch_llformat(value,row,index) {
    var idswl = $("#germ_formSearch #ids").val().split(",");
    if($("#germ_formSearch #btn_pickingcar").length>0){
        for(var i=0;i<idswl.length;i++){
            if(row.jzkcid==idswl[i]){
                return "<div id='wl"+row.jzkcid+"'><span id='t"+row.jzkcid+"' class='btn btn-danger' title='移出领料车' onclick=\"stock_formSearch_delPickingCar('" + row.jzkcid + "',event)\" >取消</span></div>";
            }
        }
        return "<div id='wl"+row.jzkcid+"'><span id='t"+row.jzkcid+"' class='btn btn-info' title='加入领料车' onclick=\"addPickingCar('" + row.jzkcid + "',event)\" >领料</span></div>";
    }
    return "";
}
/**
 * 领料车数字加减
 * @param sfbj
 * @returns
 */
function pickingCar_addOrDelNum(sfbj){
    if(sfbj=='add'){
        ll_count=parseInt(ll_count)+1;
    }else{
        ll_count=parseInt(ll_count)-1;
    }
    if((ll_count==1 && sfbj=='add') || (ll_count==0 && sfbj=='del')){
        ll_btnOinit();
    }
    $("#ll_num").text(ll_count);
}
//从领料车中删除
function stock_formSearch_delPickingCar(jzkcid,event){
    var sfyhcgqx=$("#germ_formSearch #btn_pickingcar");
    if(sfyhcgqx.length==0){
        $.confirm("很抱歉,您没有此权限!");
    }else{
        $.ajax({
            type:'post',
            url:"/germ/inventory/pagedataDelFromPickingCarr",
            cache: false,
            data: {"jzkcid":jzkcid,"access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(data){
                //返回值
                if(data.status=='success'){
                    $("#ids").val(data.ids);
                    pickingCar_addOrDelNum("del");
                    $("#germ_formSearch #t"+jzkcid).remove();
                    var html="<span id='t"+jzkcid+"' class='btn btn-info' title='加入领料车' onclick=\"addPickingCar('" + jzkcid + "',event)\" >领料</span>";
                    $("#germ_formSearch #wl"+jzkcid).append(html);
                }
            }
        });
    }
}
//添加至领料车
function addPickingCar(jzkcid,event){
    var sfyhcgqx=$("#germ_formSearch #btn_pickingcar");
    if(sfyhcgqx.length==0){
        $.confirm("很抱歉,您没有此权限!");
    }else{
        $.ajax({
            async:false,
            type:'post',
            url:"/germ/inventory/pagedataViewGerm",
            cache: false,
            data: {"jzkcid":jzkcid,"access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(data){
                //返回值
                if(data.jzkcxxDto!=null && data.jzkcxxDto!=''){
                    //kcbj=1 有库存，kcbj=0 库存未0
                    if(data.jzkcxxDto.kcl-data.jzkcxxDto.yds<1){
                        $.confirm("该物料库存不足！");
                    }else{
                        $.ajax({
                            type:'post',
                            url:"/germ/inventory/pagedataAddToPickingCar",
                            cache: false,
                            data: {"jzkcid":jzkcid,"access_token":$("#ac_tk").val()},
                            dataType:'json',
                            success:function(data){
                                //返回值
                                if(data.status=='success'){
                                    $("#ids").val(data.ids);
                                    pickingCar_addOrDelNum("add");
                                    $("#germ_formSearch #t"+jzkcid).remove();
                                    var html="<span id='t"+jzkcid+"' class='btn btn-danger' title='移出领料车' onclick=\"stock_formSearch_delPickingCar('" + jzkcid + "',event)\" >取消</span>";
                                    $("#germ_formSearch #wl"+jzkcid).append(html);
                                }
                            }
                        });
                    }
                }else{
                    $.confirm("该物料不存在!");
                }
            }
        });
    }
}
//按钮动作函数
function germDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?jzkcid=" +id;
        $.showDialog(url,'查看',viewgermConfig);
    }else if(action =='add'){
        var url= tourl;
        $.showDialog(url,'新增',addgermConfig);
    }else if(action =='mod'){
        var url=tourl + "?jzkcid=" +id;
        $.showDialog(url,'编辑',modgermConfig);
    }else if(action =='pickingcar'){
        var url=tourl;
        $.showDialog(url,'领料新增',addGermPickingConfig);
    }
}


var germ_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_add = $("#germ_formSearch #btn_add");
        var btn_mod = $("#germ_formSearch #btn_mod");
        var btn_del = $("#germ_formSearch #btn_del");
        var btn_view = $("#germ_formSearch #btn_view");

        var btn_query = $("#germ_formSearch #btn_query");
        var btn_pickingcar = $("#germ_formSearch #btn_pickingcar");
        //添加日期控件
        laydate.render({
            elem: '#lrsjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#lrsjend'
            ,theme: '#2381E9'
        });
        //绑定搜索发送功能
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchgermResult(true);
            });
        }
        btn_add.unbind("click").click(function(){
            germDealById(null,"add",btn_add.attr("tourl"));
        });
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#germ_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
               germDealById(sel_row[0].jzkcid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        btn_view.unbind("click").click(function(){
            var sel_row = $('#germ_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                germDealById(sel_row[0].jzkcid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        btn_pickingcar.unbind("click").click(function(){
            germDealById(null,"pickingcar",btn_pickingcar.attr("tourl"));
        });
        btn_del.unbind("click").click(function(){
            var sel_row = $('#germ_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                if (sel_row[i].rksl!=sel_row[i].kcl) {
                    $.error("存在领料单不允许删除");
                    return;
                }
                else {
                    ids = ids + "," + sel_row[i].jzkcid;
                }
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
                                    searchgermResult();
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
        $("#germ_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(germ_turnOff){
                $("#germ_formSearch #searchMore").slideDown("low");
                germ_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#germ_formSearch #searchMore").slideUp("low");
                germ_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
    };

    return oInit;
};

function searchgermResult(isTurnBack){
    //关闭高级搜索条件
    $("#germ_formSearch #searchMore").slideUp("low");
    germ_turnOff=true;
    $("#germ_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#germ_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#germ_formSearch #tb_list').bootstrapTable('refresh');
    }
}

$(function(){

    //1.初始化Table
    var oTable = new germ_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new germ_ButtonInit();
    oButtonInit.Init();
    if($("#llcsl").val()==null || $("#llcsl").val()==''){
        ll_count=0;
    }else{
        ll_count=$("#llcsl").val();
    }
    ll_btnOinit();
    //所有下拉框添加choose样式
    jQuery('#germ_formSearch .chosen-select').chosen({width: '100%'});

});
