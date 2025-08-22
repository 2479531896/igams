var flg = $("#train_formSearch #flg").val()=="1"?false:true;
var Train_turnOff=true;
var train_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#train_formSearch #train_list').bootstrapTable({
            url: $("#train_formSearch #urlPrefix").val()+'/train/test/pageGetListTrain?flg='+$("#train_formSearch #flg").val(),         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#train_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"pxgl.lrsj desc,pxgl.gqsj",					// 排序字段
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
            uniqueId: "grksid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            },{
                field: 'pxid',
                title: '培训ID',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'pxbt',
                title: '培训标题',
                width: '22%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'bmmc',
                title: '部门',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'pxlb',
                title: '类别',
                width: '6%',
                align: 'left',
                sortable:true,
                visible: true
            }, {
                field: 'pxzlb',
                title: '子类别',
                width: '6%',
                align: 'left',
                sortable:true,
                visible: true
            },{
                field: 'spbj',
                title: '视频',
                width: '3%',
                align: 'left',
                visible: true
            }, {
                field: 'sfcs',
                title: '测试',
                width: '3%',
                align: 'left',
                visible: true
            },{
                field: 'csts',
                title: '题数',
                width: '3%',
                align: 'left',
                visible: flg
            },{
                field: 'danxts',
                title: '单选题数',
                width: '4%',
                align: 'left',
                visible: false
            },{
                field: 'duoxts',
                title: '多选题数',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'jdts',
                title: '简答题数',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'tkts',
                title: '填空题数',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'pdts',
                title: '判断题数',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'cssc',
                title: '时长(分)',
                width: '5%',
                align: 'left',
                visible: flg
            },{
                field: 'cszf',
                title: '总分',
                width: '3%',
                align: 'left',
                sortable:true,
                visible: flg
            },{
                field: 'tgfs',
                title: '通过分数',
                width: '5%',
                align: 'left',
                sortable:true,
                visible: true
            },{
                field: 'htbj',
                title: '允许回退',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'tzfsbj',
                title: '跳转方式',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'ckdabj',
                title: '查看答案',
                width: '5%',
                align: 'left',
                visible: flg
            },{
                field: 'dcckbj',
                title: '当场重考',
                width: '5%',
                align: 'left',
                visible: flg
            },{
                field: 'qrry',
                title: '确认人员',
                width: '5%',
                align: 'left',
                visible: flg
            },{
                field: 'spxz',
                title: '视频允许下载',
                width: '7%',
                align: 'left',
                formatter:spxzFormat,
                visible: flg
            },{
                field: 'gqsj',
                title: '过期时间',
                width: '6%',
                align: 'left',
                sortable:true,
                visible: true
            },{
                field: 'ssgsmc',
                title: '所属公司',
                width: '6%',
                align: 'left',
                sortable:true,
                visible: true
            },{
                field: 'dqtxmc',
                title: '到期提醒',
                width: '5%',
                align: 'left',
                sortable:true,
                visible: false
            },{
                field: 'sffshb',
                title: '红包',
                width: '3%',
                align: 'left',
                sortable:true,
                visible: flg
            },{
                field: 'lrsj',
                title: '录入时间',
                width: '6%',
                align: 'left',
                sortable:true,
                visible: true
            },{
                field: 'zt',
                title: '状态',
                width: '5%',
                align: 'left',
                formatter:ztFormat,
                visible: flg
            },{
                field: 'cz',
                title: '操作',
                titleTooltip:'操作',
                width: '4%',
                align: 'left',
                formatter:czFormat,
                visible: flg
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                trainById(row.pxid,'view',$("#train_formSearch #btn_view").attr("tourl")+"?gzid="+row.gzid+"&spwcbj="+row.spwcbj);
            }
        });
        $("#train_formSearch #train_list").colResizable({
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
            sortLastName: "pxgl.pxid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return trainSearchData(map);
    };
    return oTableInit;
};
//视频是否允许下载格式化
function spxzFormat(value,row,index){
    if(row.spxz=='1'){
        return '允许';
    }else if(row.spxz=='0'){
        return '不允许';
    }
}

/**
 * 状态格式化函数
 * @returns
 */
function ztFormat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.pxid + "\",event,\"AUDIT_TRAIN\",{prefix:\"" + $('#train_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.pxid + "\",event,\"AUDIT_TRAIN\",{prefix:\"" + $('#train_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else{
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.pxid + "\",event,\"AUDIT_TRAIN\",{prefix:\"" + $('#train_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
    if (row.zt == '10' && row.scbj =='0' ) {
        return "<span class='btn btn-warning' onclick=\"recallTrain('" + row.pxid + "',event)\" >撤回</span>";
    }else if(row.zt == '10' && row.scbj ==null ){
        return "<span class='btn btn-warning' onclick=\"recallTrain('" + row.pxid + "',event)\" >撤回</span>";
    }else{
        return "";
    }
}

//撤回项目提交
function recallTrain(pxid,event){
    var auditType = $("#train_formSearch #auditType").val();
    var msg = '您确定要撤回该培训吗？';
    var purchase_params = [];
    purchase_params.prefix = $("#train_formSearch #urlPrefix").val();
    $.confirm(msg,function(result){
        if(result){
            doAuditRecall(auditType,pxid,function(){
                trainResult();
            },purchase_params);
        }
    });
}

function trainSearchData(map){
    var cxtj=$("#train_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#train_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr;
    }else if(cxtj=="1"){
        map["qrry"]=cxnr;
    }else if(cxtj=="2"){
        map["pxbt"]=cxnr;
    }else if(cxtj=="3"){
        map["bmmc"]=cxnr;
    }
    var lrsjstart = jQuery('#train_formSearch #lrsjstart').val();
    map["lrsjstart"] = lrsjstart;
    var lrsjend = jQuery('#train_formSearch #lrsjend').val();
    map["lrsjend"] = lrsjend;
    var pxlbs = jQuery('#train_formSearch #pxlb_id_tj').val();
    map["pxlbs"] = pxlbs;
    var pxlbs = jQuery('#train_formSearch #pxlb_id_tj').val();
    map["pxlbs"] = pxlbs;
    var zts = jQuery('#train_formSearch #zt_id_tj').val();
    map["zts"] = zts;
    var spbjs = jQuery('#train_formSearch #spbj_id_tj').val();
    map["spbjs"] = spbjs;
    var sfcss = jQuery('#train_formSearch #sfcs_id_tj').val();
    map["sfcss"] = sfcss;
    var htbjs = jQuery('#train_formSearch #htbj_id_tj').val();
    map["htbjs"] = htbjs;
    var tzfsbjs = jQuery('#train_formSearch #tzfsbj_id_tj').val();
    map["tzfsbjs"] = tzfsbjs;
    var ckdabjs = jQuery('#train_formSearch #ckdabj_id_tj').val();
    map["ckdabjs"] = ckdabjs;
    var dcckbjs = jQuery('#train_formSearch #dcckbj_id_tj').val();
    map["dcckbjs"] = dcckbjs;
    var ssgss = jQuery('#train_formSearch #ssgs_id_tj').val();
    map["ssgss"] = ssgss.replace(/'/g, "");
    var dqtxs = jQuery('#train_formSearch #dqtx_id_tj').val();
    map["dqtxs"] = dqtxs.replace(/'/g, "");
    var sffshbs = jQuery('#train_formSearch #sffshb_id_tj').val();
    map["sffshbs"] = sffshbs.replace(/'/g, "");
    return map;
}
//提供给导出用的回调函数
function TrainSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="pxgl.lrsj";
    map["sortLastOrder"]="desc";
    map["sortName"]="pxgl.pxid";
    map["sortOrder"]="desc";
    return trainSearchData(map);
}
function trainById(id,action,tourl,data){
    if(!tourl){
        return;
    }
    tourl = $("#train_formSearch #urlPrefix").val()+tourl;
    if(action =='distribute'){
        var url= tourl + "?ids=" +id;
        $.showDialog(url,'分发',distributeTrainConfig);
    }else if(action =='view'){
        var url= tourl + "&pxid=" +id+"&flg="+$("#train_formSearch #flg").val();
        $.showDialog(url,'查看',viewTrainConfig);
    }else if(action =='add'){
        var url=tourl;
        $.showDialog(url,'新增培训信息',addTrainConfig);
    }else if(action =='mod'){
        var url=tourl+ "?pxid=" +id;
        $.showDialog(url,'修改培训信息',modTrainConfig);
    }else if(action =='submit'){
        var url=tourl+ "?pxid=" +id;
        $.showDialog(url,'提交培训信息',submitTrainConfig);
    }else if(action =='copy'){
        var url=tourl+ "?pxid=" +id;
        $.showDialog(url,'复制培训信息',addTrainConfig);
    }else if(action =='departmentSet'){
        var url=tourl+ "?ids=" +id;
        $.showDialog(url,'部门设置',departmentSetTrainConfig);
    }else if(action =='departDistribute'){
        var url= tourl + "?bm=" +id+"&ids="+data;
        $.showDialog(url,'部门分发',distributeTrainConfig);
    }else if(action =='generatesignincode'){
        var url= tourl + "?pxid=" +id+"&sfxzqd="+data;
        $.showDialog(url,'生成签到码',generatesignincodeTrainConfig);
    }else if(action =='modresult'){
        var url= tourl + "?pxid=" +id;
        $.showDialog(url,'结果修改',modresultConfig);
    }else if(action =='generatetestpapers'){
        var url=tourl;
        $.showDialog(url,'生成试卷',generatetestpapersTrainConfig);
    }
}
var generatetestpapersTrainConfig = {
    width		: "1400px",
    modalName	: "generatetestpapersModal",
    formName	: "generatetestpapersForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#generatetestpapersForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                let json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(let i=0;i<t_map.rows.length;i++){
                        let sz = {"tkid":'',"tmlx":'',"sl":'',"fz":''};
                        sz.tkid = t_map.rows[i].tkid;
                        if (!sz.tkid){
                            $.alert("请填写第"+(i+1)+"行题库！");
                            return false;
                        }
                        sz.tmlx = t_map.rows[i].tmlx;
                        if (!sz.tmlx){
                            $.alert("请填写第"+(i+1)+"行题目类型！");
                            return false;
                        }
                        sz.sl = t_map.rows[i].sl;
                        if (!sz.sl){
                            $.alert("请填写第"+(i+1)+"行数量！");
                            return false;
                        }
                        sz.fz = t_map.rows[i].fz;
                        if (!sz.fz){
                            $.alert("请填写第"+(i+1)+"行分值！");
                            return false;
                        }
                        sz.zf = Number(t_map.rows[i].sl)*Number(t_map.rows[i].fz);
                        json.push(sz);
                    }
                }
                const encodedParams = encodeURIComponent(JSON.stringify(json));
                var url=$('#generatetestpapersForm #urlPrefix').val()+"/train/test/pagedataViewTestPapersTrain?tmmx_json="+encodedParams+"&access_token="+$("#ac_tk").val();
                window.open(url);
                return false;

            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var modresultConfig = {
    width		: "1000px",
    modalName	: "modresultTrainModal",
    formName	: "modresultTrainForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#modresultTrainForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#modresultTrainForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"modresultTrainForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                trainResult();
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
var departmentSetTrainConfig = {
    width		: "800px",
    modalName : "departmentSetTrainModal",
    buttons : {
        success : {
            label : "确定",
            className : "btn-primary",
            callback : function	() {
                //调用指定函数方法
//					var formAction = $("#menutreeDiv #formAction").val();
//					if(formAction){
//						eval(formAction+"()");
//					}
//					jQuery.closeModal("saveUnitModal");
//					return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var addTrainConfig = {
    width		: "1525px",
    modalName	: "addTrainModal",
    formName	: "editTrainForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editTrainForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                var val1 = $("#editTrainForm #fjids").val();
                var flag=$("#editTrainForm #flag").val();
                if (flag=="add") {
                    if (!val1) {
                        $.alert("请上传图片！");
                        return false;
                    }
                }
                var val2 = $("#editTrainForm #fjid").val();
                if (flag=="mod"){
                    if (!val2&&!val1) {
                        $.alert("请上传图片！");
                        return false;
                    }
                }
               if ($("#editTrainForm input:radio[name='sfcs']:checked").val()=='1'){
                   var  kscs = $("#editTrainForm #kscs").val();
                   if (kscs==null||kscs==''){
                       $.alert("请填写考试次数！");
                       return false;
                   }
               }
                var val3 = $("#editTrainForm #hbvalue").val();
                var val4 = $("#editTrainForm #hbszid").val();
                    if (val3=='1'&&(val4==""||val4==null)) {
                        $.alert("请选择红包！");
                        return false;
                    }
                var json = $("#editTrainForm #tmmx_json").val();
                if (json && json!="") {
                    var arr = JSON.parse(json);
                    for (var i = 0; i < arr.length; i++) {
                        if(arr[i].tkmc==null||arr[i].tkmc==''){
                            $.alert("第"+(i+1)+"行未填写题库名称！");
                            return false;
                        }
                        if(arr[i].tmlx==null||arr[i].tmlx==''){
                            $.alert("第"+(i+1)+"行未选择题目类型！");
                            return false;
                        }
                        if(arr[i].sl==null||arr[i].sl==''){
                            $.alert("第"+(i+1)+"行未填写数量！");
                            return false;
                        }
                        if(arr[i].fz==null||arr[i].fz==''){
                            $.alert("第"+(i+1)+"行未填写分值！");
                            return false;
                        }
                    }
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#editTrainForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            //提交审核
                            if(responseText["auditType"]!=null){
                                var params=[];
                                params.prefix=responseText["urlPrefix"];
                                showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                    $.closeModal(opts.modalName);
                                    trainResult();
                                },null,params);
                            }else{
                                trainResult();
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

var modTrainConfig = {
    width		: "1525px",
    modalName	: "modTrainModal",
    formName	: "editTrainForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editTrainForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                var val1 = $("#editTrainForm #fjids").val();
                var flag=$("#editTrainForm #flag").val();
                if (flag=="add") {
                    if (!val1) {
                        $.alert("请上传图片！");
                        return false;
                    }
                }
                var val2 = $("#editTrainForm #fjid").val();
                if (flag=="mod"){
                    if (!val2&&!val1) {
                        $.alert("请上传图片！");
                        return false;
                    }
                }
                if ($("#editTrainForm input:radio[name='sfcs']:checked").val()=='1'){
                    var  kscs = $("#editTrainForm #kscs").val();
                    if (kscs==null||kscs==''){
                        $.alert("请填写考试次数！");
                        return false;
                    }
                }
                var json = $("#editTrainForm #tmmx_json").val();
                if (json && json!="") {
                    var arr = JSON.parse(json);
                    for (var i = 0; i < arr.length; i++) {
                        if(arr[i].tkmc==null||arr[i].tkmc==''){
                            $.alert("第"+(i+1)+"行未填写题库名称！");
                            return false;
                        }
                        if(arr[i].tmlx==null||arr[i].tmlx==''){
                            $.alert("第"+(i+1)+"行未选择题目类型！");
                            return false;
                        }
                        if(arr[i].sl==null||arr[i].sl==''){
                            $.alert("第"+(i+1)+"行未填写数量！");
                            return false;
                        }
                        if(arr[i].fz==null||arr[i].fz==''){
                            $.alert("第"+(i+1)+"行未填写分值！");
                            return false;
                        }
                    }
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#editTrainForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                trainResult();
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

var submitTrainConfig = {
    width		: "1525px",
    modalName	: "submitTrainModal",
    formName	: "editTrainForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editTrainForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                var val1 = $("#editTrainForm #fjids").val();
                var flag=$("#editTrainForm #flag").val();
                if (flag=="add") {
                    if (!val1) {
                        $.alert("请上传图片！");
                        return false;
                    }
                }
                var val2 = $("#editTrainForm #fjid").val();
                if (flag=="mod"){
                    if (!val2&&!val1) {
                        $.alert("请上传图片！");
                        return false;
                    }
                }
                if ($("#editTrainForm input:radio[name='sfcs']:checked").val()=='1'){
                    var  kscs = $("#editTrainForm #kscs").val();
                    if (kscs==null||kscs==''){
                        $.alert("请填写考试次数！");
                        return false;
                    }
                }
                var json = $("#editTrainForm #tmmx_json").val();
                if (json && json!="") {
                    var arr = JSON.parse(json);
                    for (var i = 0; i < arr.length; i++) {
                        if(arr[i].tkmc==null||arr[i].tkmc==''){
                            $.alert("第"+(i+1)+"行未填写题库名称！");
                            return false;
                        }
                        if(arr[i].tmlx==null||arr[i].tmlx==''){
                            $.alert("第"+(i+1)+"行未选择题目类型！");
                            return false;
                        }
                        if(arr[i].sl==null||arr[i].sl==''){
                            $.alert("第"+(i+1)+"行未填写数量！");
                            return false;
                        }
                        if(arr[i].fz==null||arr[i].fz==''){
                            $.alert("第"+(i+1)+"行未填写分值！");
                            return false;
                        }
                    }
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#editTrainForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            //提交审核
                            if(responseText["auditType"]!=null){
                                var params=[];
                                params.prefix=responseText["urlPrefix"];
                                showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                    $.closeModal(opts.modalName);
                                    trainResult();
                                },null,params);
                            }else{
                                trainResult();
                            }
                        });
                    }else if(responseText["status"] == "fail"){
                        $.error(responseText["message"],function() {
                            preventResubmitForm(".modal-footer > button", false);
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                            preventResubmitForm(".modal-footer > button", false);
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


var viewTrainConfig = {
    width		: "1400px",
    modalName	: "viewTrainModal",
    formName	: "viewTrainForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
            cancel : {
            label : "关 闭",
            className : "btn-default"
            }
        }
    };


var generatesignincodeTrainConfig = {
    width		: "600px",
    modalName	: "generatesignincodeTrainModal",
    formName	: "generatesignincode_Form",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#generatesignincode_Form").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#generatesignincode_Form input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"generatesignincode_Form",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                trainResult();
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
var distributeTrainConfig = {
    width		: "600px",
    modalName	: "distributeTrainModal",
    formName	: "train_ajaxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#train_ajaxForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#train_ajaxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                trainResult();
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


var train_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    //添加日期控件
    laydate.render({
        elem: '#train_formSearch #lrsjstart'
        ,theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
        elem: '#train_formSearch #lrsjend'
        ,theme: '#2381E9'
    });
    oInit.Init = function () {
        var btn_distribute = $("#train_formSearch #btn_distribute");
        var btn_query = $("#train_formSearch #btn_query");
        var btn_view = $("#train_formSearch #btn_view");
        var btn_del=$("#train_formSearch #btn_del");
        var btn_add = $("#train_formSearch #btn_add");
        var btn_mod = $("#train_formSearch #btn_mod");
        var btn_copy = $("#train_formSearch #btn_copy");
        var btn_departmentSet = $("#train_formSearch #btn_departmentSet");
        var btn_departDistribute = $("#train_formSearch #btn_departDistribute");
        var btn_submit = $("#train_formSearch #btn_submit");
		var btn_clear=$("#train_formSearch #btn_clear");
	    var btn_searchexport = $("#train_formSearch #btn_searchexport");
        var btn_selectexport = $("#train_formSearch #btn_selectexport");
        var btn_remind = $("#train_formSearch #btn_remind");//提醒
        var btn_generatesignincode = $("#train_formSearch #btn_generatesignincode");//签到码
        var btn_modresult = $("#train_formSearch #btn_modresult");//培训结果修改
        var btn_generatetestpapers = $("#train_formSearch #btn_generatetestpapers");//生成试卷
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                trainResult(true);
            });
        }
        /* --------------------------- 新增 -----------------------------------*/
        btn_add.unbind("click").click(function(){
            trainById(null, "add", btn_add.attr("tourl"));
        });
        /* --------------------------- 修改--------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#train_formSearch #train_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                if (sel_row[0].sffshb=='是'&&sel_row[0].zt=='80'){
                    $.error("发放红包的培训审核通过后不允许修改");
                }
                else
                    trainById(sel_row[0].pxid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* --------------------------- 培训结果修改--------------------------------*/
        btn_modresult.unbind("click").click(function(){
            var sel_row = $('#train_formSearch #train_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                trainById(sel_row[0].pxid,"modresult",btn_modresult.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* --------------------------- 签到码--------------------------------*/
        btn_generatesignincode.unbind("click").click(function(){
            var sel_row = $('#train_formSearch #train_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                trainById(sel_row[0].pxid,"generatesignincode",btn_generatesignincode.attr("tourl"),sel_row[0].sfxzqd);
            }else{
                $.error("请选中一行");
            }
        });
        /* --------------------------- 提交--------------------------------*/
        btn_submit.unbind("click").click(function(){
            var sel_row = $('#train_formSearch #train_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                if(sel_row[0].zt=='00'||sel_row[0].zt=='15'){
                    trainById(sel_row[0].pxid,"submit",btn_submit.attr("tourl"));
                }else{
                    $.error("当前培训已经提交审核，请勿重复提交！");
                }
            }else{
                $.error("请选中一行");
            }
        });
        /* --------------------------- 复制--------------------------------*/
        btn_copy.unbind("click").click(function(){
            var sel_row = $('#train_formSearch #train_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                trainById(sel_row[0].pxid,"copy",btn_copy.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------查看-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#train_formSearch #train_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                trainById(sel_row[0].pxid,"view",btn_view.attr("tourl")+"?gzid="+sel_row[0].gzid+"&spwcbj="+sel_row[0].spwcbj+"&tgbj="+sel_row[0].tgbj);
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------分发-----------------------------------*/
        btn_distribute.unbind("click").click(function(){
            var sel_row = $('#train_formSearch #train_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    if(sel_row[i].sfgk=='1'){
                        $.error("公开的培训资料不需要分发！");
                        return;
                    }
                    if(sel_row[i].spbj=='否'&&sel_row[i].sfcs=='否'){
                        $.error("没有视频且没有测试的培训不允许分发操作！");
                        return;
                    }
                    if(sel_row[i].zt!='80'){
                        $.error("未审核通过的培训不允许进行分发！");
                        return;
                    }
                    ids = ids + ","+ sel_row[i].pxid;
                }
                ids = ids.substr(1);
                trainById(ids,"distribute",btn_distribute.attr("tourl"));
            }else{
                $.error("请选择数据");
            }
        });
        /* ---------------------------提醒-----------------------------------*/
        btn_remind.unbind("click").click(function(){
            var sel_row = $('#train_formSearch #train_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].pxid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要发送提醒吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $('#train_formSearch #urlPrefix').val()+btn_remind.attr("tourl");
                        jQuery.post(url,{pxids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        trainResult();
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
        /* ---------------------------部门分发-----------------------------------*/
        btn_departDistribute.unbind("click").click(function(){
            var sel_row = $('#train_formSearch #train_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].sfgk=='1'){
                    $.error("公开的培训资料不需要分发！");
                    return;
                }
                if(sel_row[0].spbj=='否'&&sel_row[0].sfcs=='否'){
                    $.error("没有视频且没有测试的培训不允许分发操作！");
                    return;
                }
                if(sel_row[0].zt!='80'){
                    $.error("未审核通过的培训不允许进行分发！");
                    return;
                }
                trainById(sel_row[0].bm,"departDistribute",btn_departDistribute.attr("tourl"),sel_row[0].pxid);
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------部门设置-----------------------------------*/
        btn_departmentSet.unbind("click").click(function(){
            var sel_row = $('#train_formSearch #train_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>0){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].pxid;
                }
                ids = ids.substr(1);
                trainById(ids,"departmentSet",btn_departmentSet.attr("tourl"));
            }else{
                $.error("请选择至少一条数据！");
            }
        });
        // 	  ---------------------------导出-----------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#train_formSearch #train_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].pxid;
                }
                ids = ids.substr(1);
                $.showDialog($('#train_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=TRAIN_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });

        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#train_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=TRAIN_SEARCH&expType=search&callbackJs=TrainSearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
        /* ---------------------------删除-----------------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#train_formSearch #train_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].pxid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $('#train_formSearch #urlPrefix').val()+btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        trainResult();
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
        /* --------------------------- 生成试卷 -----------------------------------*/
        btn_generatetestpapers.unbind("click").click(function(){
            trainById(null, "generatetestpapers", btn_generatetestpapers.attr("tourl"));
        });
       /* ---------------------------清除缓存-----------------------------------*/
        btn_clear.unbind("click").click(function(){
                $.confirm('您确定要清除缓存信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $('#train_formSearch #urlPrefix').val()+btn_clear.attr("tourl");
                        jQuery.post(url,{"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        trainResult();
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
        /*-----------------------显示隐藏------------------------------------*/
        $("#train_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(Train_turnOff){
                $("#train_formSearch #searchMore").slideDown("low");
                Train_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#train_formSearch #searchMore").slideUp("low");
                Train_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
    };
    return oInit;
};



function trainResult(isTurnBack){
    //关闭高级搜索条件
    $("#train_formSearch #searchMore").slideUp("low");
    Train_turnOff=true;
    $("#train_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#train_formSearch #train_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#train_formSearch #train_list').bootstrapTable('refresh');
    }
}


$(function(){


    // 1.初始化Table
    var oTable = new train_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new train_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#train_formSearch .chosen-select').chosen({width: '100%'});

    $("#train_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});