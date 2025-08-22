
/**
 * 选择部门列表
 * @returns
 */
function chooseBm(){
    var url=$('#editTrainForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择部门',addSqbmConfig);
}
var addSqbmConfig = {
    width		: "800px",
    modalName	:"addSqbmModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#editTrainForm #bm').val(sel_row[0].jgid);
                    $('#editTrainForm #bmmc').val(sel_row[0].jgmc);
                }else{
                    $.error("请选中一行");
                    return false;
                }
            },
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * 选择年度培训
 * @returns
 */
function chooseNdjh(){
    var url=$('#editTrainForm #urlPrefix').val() + "/annualPlan/annualPlan/pagedataChooseAnnualPlan?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择年度培训',addNdjhConfig);
}
var addNdjhConfig = {
    width		: "1200px",
    modalName	:"addNdjhModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#chooseAnnualPlanForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#editTrainForm #pxmc').val(sel_row[0].pxmc);
                    $('#editTrainForm #pxbt').val(sel_row[0].pxmc);
                    $('#editTrainForm #ndpxid').val(sel_row[0].ndpxid);
                    $('#editTrainForm #ks').val(sel_row[0].ks);
                    $('#editTrainForm #pxfs').val(sel_row[0].pxfs);
                    $('#editTrainForm #pxfsmc').text(sel_row[0].pxfsmc);
                }else{
                    $.error("请选中一行");
                    return false;
                }
            },
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


$("#editTrainForm #spbj_no").click(function () {
    $("#editTrainForm #spsc").hide();
    $("#editTrainForm #ycsp").hide();
    $("#editTrainForm #hspxz").hide();
});

$("#editTrainForm #spbj_yes").click(function () {
    $("#editTrainForm #spsc").show();
    $("#editTrainForm #hspxz").show();
    $("#editTrainForm #ycsp").show();
});

$("#editTrainForm #sfcs_yes").click(function () {
    $("#editTrainForm #csDiv").show();
    $("#editTrainForm #danxts").attr("validate","{required:true,number:true}");
    $("#editTrainForm #duoxts").attr("validate","{required:true,number:true}");
    $("#editTrainForm #jdts").attr("validate","{required:true,number:true}");
    $("#editTrainForm #cszf").attr("validate","{required:true,number:true}");
    $("#editTrainForm #tgfs").attr("validate","{required:true,number:true}");
});
$("#editTrainForm #sfcs_no").click(function () {
    $("#editTrainForm #csDiv").hide();
    $("#editTrainForm #danxts").removeAttr("validate");
    $("#editTrainForm #duoxts").removeAttr("validate");
    $("#editTrainForm #jdts").removeAttr("validate");
    $("#editTrainForm #cszf").removeAttr("validate");
    $("#editTrainForm #tgfs").removeAttr("validate");
    $("#editTrainForm #tgfs").removeAttr("validate");
    $("#editTrainForm #tmmx_json").val(null);
});

$("#editTrainForm #sfgk_yes").click(function () {
    $('input:radio[name="sfcs"][value="0"]').prop('checked', true);
    $("#editTrainForm #csDiv").hide();
    $("#editTrainForm #danxts").removeAttr("validate");
    $("#editTrainForm #duoxts").removeAttr("validate");
    $("#editTrainForm #jdts").removeAttr("validate");
    $("#editTrainForm #cszf").removeAttr("validate");
    $("#editTrainForm #tgfs").removeAttr("validate");
    $("#editTrainForm #tgfs").removeAttr("validate");
    $("#editTrainForm #tmmx_json").val(null);
});
$("#editTrainForm #sffshb_yes").click(function () {
    $("#editTrainForm #hbvalue").val("1");
    $("#editTrainForm #hbsz").show();
});
$("#editTrainForm #sffshb_no").click(function () {
    $("#editTrainForm #hbvalue").val("0");
    $("#editTrainForm #hbsz").hide();

});
function init(){

}

//打开文件上传界面
function editfile(divName,btnName){
    $("#editTrainForm"+"  #"+btnName).hide();
    $("#editTrainForm"+"  #"+divName).show();
}
//关闭文件上传界面
function cancelfile(divName,btnName){
    $("#editTrainForm"+"  #"+btnName).show();
    $("#editTrainForm"+"  #"+divName).hide();
}

function displayUpInfo(fjid){
    if(!$("#editTrainForm #fjids").val()){
        $("#editTrainForm #fjids").val(fjid);
    }else{
        $("#editTrainForm #fjids").val($("#editTrainForm #fjids").val()+","+fjid);
    }
}
function displayUpInfo_sp(fjid){
    if(!$("#editTrainForm #spfjid").val()){
        $("#editTrainForm #spfjid").val(fjid);
    }else{
        $("#editTrainForm #spfjid").val($("#editTrainForm #spfjid").val()+","+fjid);
    }
}

//下载模板
function xz(fjid){
    jQuery('<form action="' + $("#editTrainForm #urlPrefix").val() + '/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}
//删除模板
function del(fjid,wjlj,div,div_t){
    $.confirm('您确定要删除所选择的记录吗？',function(result){
        if(result){
            jQuery.ajaxSetup({async:false});
            var url=  $("#editTrainForm #urlPrefix").val()+"/common/file/delFile";
            jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
                setTimeout(function(){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            $("#editTrainForm"+"  #"+fjid).remove();
                            $("#editTrainForm #tpfj").val(null);
                        });
                        if(div&&div_t){
                            $("#editTrainForm"+"  #"+div).remove();
                            $("#editTrainForm"+"  #"+div_t).show();
                        }
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

//初始化fileinput
var TrainFileInput = function () {
    var oInspectFileInput = new Object();
    //初始化fileinput控件（第一次初始化）
    //参数说明 singleFlg 好像是标明 是否单文件上传限制，有点忘记了
    //impflg 用于后台判断是否为导入。1为导入，则开启线程进行导入  2：代表附件保存
    oInspectFileInput.Init = function(ctrlName,callback,impflg,singleFlg,fileName, ywlx) {
        var control = $('#' + ctrlName + ' #' + fileName);
        var filecnt = 0;

        var access_token = null;
        if($("#ac_tk").val()){
            access_token = $("#ac_tk").val();
        }
        if($("#ajaxForm input[name='access_token']").val()){
            access_token = $("#ajaxForm input[name='access_token']").val();
        }
        //初始化上传控件的样式
        control.fileinput({
            language: 'zh', //设置语言
            uploadUrl:  $("#editTrainForm #urlPrefix").val()+"/common/file/saveImportFile?access_token="+access_token, //上传的地址
            showUpload: false, //是否显示上传按钮
            showPreview: true, //展前预览
            showCaption: false,//是否显示标题
            browseClass: "btn btn-primary", //按钮样式
            dropZoneEnabled: true,//是否显示拖拽区域
            //minImageWidth: 50, //图片的最小宽度
            //minImageHeight: 50,//图片的最小高度
            //maxImageWidth: 1000,//图片的最大宽度
            //maxImageHeight: 1000,//图片的最大高度
            maxFileSize: 0,//单位为kb，如果为0表示不限制文件大小
            //minFileCount: 0,
            maxFileCount: 10, //表示允许同时上传的最大文件个数
            enctype: 'multipart/form-data',
            validateInitialCount:true,
            previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
            msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
            layoutTemplates :{
                // actionDelete:'', //去除上传预览的缩略图中的删除图标
                actionUpload:'',//去除上传预览缩略图中的上传图片；
                //actionZoom:''   //去除上传预览缩略图中的查看详情预览的缩略图标。
            },
            otherActionButtons:'<button type="button" class="kv-file-down btn btn-xs btn-default" {dataKey} title="下载附件"><i class="fa fa-cloud-download"></i></button>',
            uploadExtraData:function (previewId, index) {
                //向后台传递id作为额外参数，是后台可以根据id修改对应的图片地址。
                var obj = {};
                if($("#ac_tk").val()){
                    obj.access_token = $("#ac_tk").val();
                }
                if($("#ajaxForm input[name='access_token']").val()){
                    obj.access_token = $("#ajaxForm input[name='access_token']").val();
                }
                obj.ywlx = ywlx;
                if(impflg && impflg == 1){
                    obj.impflg = 1;
                }
                obj.fileName = fileName;
                return obj;
            },
            initialPreviewAsData:true,
            initialPreview: [        //这里配置需要初始展示的图片连接数组，字符串类型的，通常是通过后台获取后然后组装成数组直接赋给initialPreview就行了
                //"http://localhost:8086/common/file/getFileInfo",
            ],
            initialPreviewConfig: [ //配置预览中的一些参数
            ]
        })
        //删除初始化存在文件时执行
            .on('filepredelete', function(event, key, jqXHR, data) {

            })
            //删除打开页面后新上传文件时执行
            .on('filesuccessremove', function(event, key, jqXHR, data) {
                if(fileName == 'det_file'){
                    var frames = $("#"+ ctrlName +" .file-preview-frame.krajee-default.kv-preview-thumb.file-preview-success");
                    var index = 0;
                    for(var i=0;i<frames.length;i++){
                        if(key == frames[i].id){
                            index = i;
                            break;
                        }
                    }
                    var str = $("#"+ ctrlName + " #fjids").val();
                    var arr=str.split(",");
                    arr.splice(index,1);
                    var str=arr.join(",");
                    $("#"+ ctrlName + " #fjids").val(str);
                }else if(fileName == 'word_file'){
                    $("#"+ ctrlName + " #w_fjids").val("");
                }else if(fileName == 'det_file_q'){
                    var frames = $("#"+ ctrlName +" .file-preview-frame.krajee-default.kv-preview-thumb.file-preview-success");
                    var index = 0;
                    for(var i=0;i<frames.length;i++){
                        if(key == frames[i].id){
                            index = i;
                            break;
                        }
                    }
                    var str = $("#"+ ctrlName + " #fjids_q").val();
                    var arr=str.split(",");
                    arr.splice(index,1);
                    var str=arr.join(",");
                    $("#"+ ctrlName + " #fjids_q").val(str);
                }else if(fileName == 'word_file_q'){
                    $("#"+ ctrlName + " #w_fjids_q").val("");
                }
            })

            // 实现图片被选中后自动提交
            .on('filebatchselected', function(event, files) {
                // 选中事件
                $(event.target).fileinput('upload');
                filecnt = files.length;

            })
            // 异步上传错误结果处理
            .on('fileerror', function(event, data, msg) {  //一个文件上传失败
                // 清除当前的预览图 ，并隐藏 【移除】 按钮
                $(event.target)
                    .fileinput('clear')
                    .fileinput('unlock');
                if(singleFlg && singleFlg==1){
                    $(event.target)
                        .parent()
                        .siblings('.fileinput-remove')
                        .hide()
                }
                // 打开失败的信息弹窗
                $.error("请求失败，请稍后重试",function() {});
            })
            // 异步上传成功结果处理
            .on('fileuploaded', function(event, data) {
                // 判断 code 是否为  0	0 代表成功
                status = data.response.status
                if (status === "success") {

                    if(callback){
                        eval(callback+"('"+ data.response.fjcfbDto.fjid+"')");
                    }
                } else {
                    // 失败回调
                    // 清除当前的预览图 ，并隐藏 【移除】 按钮
                    $(event.target)
                        .fileinput('clear')
                        .fileinput('unlock');
                    if(singleFlg && singleFlg==1){
                        $(event.target)
                            .parent()
                            .siblings('.fileinput-remove')
                            .hide()
                    }
                    // 打开失败的信息弹窗
                    $.error("请求失败，请稍后重试",function() {});
                }
            })
    }
    return oInspectFileInput;
};

$("#pxlb").on('change',function(){
    var csid=$("#pxlb").val();
    $.ajax({
        url : $("#editTrainForm #urlPrefix").val()+"/train/pagedataSubcategory",
        type : "post",
        data : {fcsid:csid,"access_token":$("#ac_tk").val()},
        dataType : "json",
        success:function(data){
            if(data != null && data.length != 0){
                var csbHtml = "";
                csHtml += "<option value=''>--请选择--</option>";
                $.each(data,function(i){
                    csHtml += "<option value='" + data[i].csid + "'>" + data[i].csmc + "</option>";
                });
                $("#editTrainForm #pxzlb").empty();
                $("#editTrainForm #pxzlb").append(csHtml);
                $("#editTrainForm #pxzlb").trigger("chosen:updated");
            }else{
                var csHtml = "";
                csHtml += "<option value=''>--请选择--</option>";
                $("#editTrainForm #pxzlb").empty();
                $("#editTrainForm #pxzlb").append(csHtml);
                $("#editTrainForm #pxzlb").trigger("chosen:updated");
            }
        }
    });
})

var t_map=[];
//行政请购类型显示字段
var columnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '5%',
        align: 'left',
        visible:true
    },{
        field: 'tkmc',
        title: '题库',
        width: '33%',
        align: 'left',
        formatter:tkmcformat,
        visible: true
    },{
        field: 'tmlxmc',
        title: '题目类型',
        width: '15%',
        align: 'left',
        formatter:tmlxformat,
        visible: true
    }, {
        field: 'sl',
        title: '数量',
        width: '20%',
        align: 'left',
        formatter:slformat,
        visible: true
    },{
        field: 'fz',
        title: '每题分值',
        width: '15%',
        align: 'left',
        formatter:fzformat,
        visible: true
    },{
        field: 'cz',
        title: '操作',
        width: '10%',
        align: 'left',
        formatter:czformat,
        visible: true
    }]

var QuestionBank_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#editTrainForm #tb_list").bootstrapTable({
            url:   $('#editTrainForm #urlPrefix').val()+$('#editTrainForm #url').val()+$("#ac_tk").val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#editTrainForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: 'xh',				//排序字段
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: null,                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            paginationDetailHAlign:' hidden',
            columns: columnsArray,
            onLoadSuccess:function(map){
                t_map=map;
                if(map.rows.length<=0){
                    var a={"tkmc":'',"tmlx":'',"sl":'',"zsl":'',"fz":''};
                    t_map.rows.push(a);
                    $("#editTrainForm #tb_list").bootstrapTable('load',t_map);
                }
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            },
        });
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: 1,   // 页面大小
            pageNumber:  1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "pxid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getQuestionSearchData(map);
    };
    return oTableInit;
}
/**
 * 组装列表查询条件(未使用)
 * @param map
 * @returns
 */
function getQuestionSearchData(map){
    return map;
}
function searchQuestionResult(isTurnBack){
    if(isTurnBack){
        $('#editTrainForm #tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#editTrainForm #tb_list').bootstrapTable('refresh');
    }
}
var sum=1;
//新增明细
function addnew(){
    var data=JSON.parse($("#editTrainForm #tmmx_json").val());
    var a={"index":data.length,"tkmc":'',"tmlx":'',"sl":'',"zsl":'',"fz":''};
    t_map.rows.push(a);
    data.push(a);
    $("#editTrainForm #tmmx_json").val(JSON.stringify(data));
    $("#editTrainForm #tb_list").bootstrapTable('load',t_map);
    sum ++;
}

/**
 * 所属公司改变
 */
var ssgs=$("#editTrainForm #ssgs").val();
function changessgs() {
    wj_map.rows = [];
    $("#editTrainForm #glwj_list").bootstrapTable('load',wj_map);
    $("#editTrainForm #glwjids").val(JSON.stringify([]));
    var html="";
    html += "<option value=''>--请选择--</option>";
    ssgs= $("#editTrainForm #ssgs").val();
    $.ajax({
        url :  $('#editTrainForm #urlPrefix').val()+'/train/question/pagedataTk',
        type : 'post',
        data : {
            "ssgs" :ssgs,
            "access_token" : $("#ac_tk").val()
        },
        dataType : 'json',
        success : function(data) {
            if (data != null) {
                for (var i = 0; i < data.tklist.length; i++) {
                    html += "<option id='" + data.tklist[i].tkid + "' value='" + data.tklist[i].tkid + "'";
                    html += ">" + data.tklist[i].tkmc + "</option>";
                }
            }
            for (var i=0;i<sum;i++)
            {
                $("#tkmc_"+i).html(html);
            }


        }
    });
}
/**
 * 题库名称格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function tkmcformat(value,row,index){
    var xmdls = $("#editTrainForm #tklist").val();
    var tklist = JSON.parse(xmdls);
    var tkmc=row.tkmc;
    var html="";
    html += "<select id='tkmc_"+index+"' name='tkmc_"+index+"' class='form-control chosen-select'  onchange=\"checkSum('"+index+"',this,\'tkmc\')\">";
    html += "<option value=''>--请选择--</option>";
    for(var i = 0; i < tklist.length; i++) {
        if (ssgs!=null) {
            if (tklist[i].ssgs==ssgs) {
                html += "<option id='" + tklist[i].tkid + "' value='" + tklist[i].tkid + "'";
                if (tkmc != null && tkmc != "") {
                    if (tkmc == tklist[i].tkid) {
                        html += "selected"
                    }
                }
                html += ">" + tklist[i].tkmc + "</option>";
            }
        }

    }
    html +="</select>";
    return html;
}

/**
 * 题目类型格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function tmlxformat(value,row,index){
    var html="";
    html +=  "<select id='tmlx_"+index+"' name='tmlx_"+index+"' class='form-control chosen-select'  onchange=\"checkSum('"+index+"',this,\'tmlx\')\">";
    //父节点数据
    html += "<option value=''>--请选择--</option>";
    if(row.tmlx=='SELECT'){
        html += "<option value='SELECT' selected>单选题</option>";
        html += "<option value='MULTIPLE'>多选题</option>";
        html += "<option value='EXPLAIN'>简答题</option>";
        html += "<option value='GAP'>填空题</option>";
        html += "<option value='JUDGE'>判断题</option>";
    }else if(row.tmlx=='MULTIPLE'){
        html += "<option value='SELECT'>单选题</option>";
        html += "<option value='MULTIPLE' selected>多选题</option>";
        html += "<option value='EXPLAIN'>简答题</option>";
        html += "<option value='GAP'>填空题</option>";
        html += "<option value='JUDGE'>判断题</option>";
    }else if(row.tmlx=='EXPLAIN'){
        html += "<option value='SELECT'>单选题</option>";
        html += "<option value='MULTIPLE' >多选题</option>";
        html += "<option value='EXPLAIN' selected>简答题</option>";
        html += "<option value='GAP'>填空题</option>";
        html += "<option value='JUDGE'>判断题</option>";
    }else if(row.tmlx=='GAP'){
        html += "<option value='SELECT'>单选题</option>";
        html += "<option value='MULTIPLE' >多选题</option>";
        html += "<option value='EXPLAIN'>简答题</option>";
        html += "<option value='GAP'  selected>填空题</option>";
        html += "<option value='JUDGE'>判断题</option>";
    }else if(row.tmlx=='JUDGE'){
        html += "<option value='SELECT'>单选题</option>";
        html += "<option value='MULTIPLE' >多选题</option>";
        html += "<option value='EXPLAIN'>简答题</option>";
        html += "<option value='GAP'>填空题</option>";
        html += "<option value='JUDGE'  selected>判断题</option>";
    }else{
        html += "<option value='SELECT'>单选题</option>";
        html += "<option value='MULTIPLE'>多选题</option>";
        html += "<option value='EXPLAIN'>简答题</option>";
        html += "<option value='GAP'>填空题</option>";
        html += "<option value='JUDGE'>判断题</option>";
    }
    html += "</select>";
    return html;
}
/**
 * 数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function slformat(value,row,index){
    if(row.sl == null){
        row.sl = "";
    }
    var html = "<input id='sl_"+index+"' value='"+row.sl+"' text='"+row.sl+"' name='sl_"+index+"' style='width:60%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'sl\')\" autocomplete='off'/>&nbsp;&nbsp;/&nbsp;&nbsp;<input id='zsl_"+index+"'text='"+row.zsl+"'  value='"+row.zsl+"'disabled style='color: red;width: 30%;border: 0px;'></input>";
    return html;
}

/**
 * 分值格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fzformat(value,row,index){
    if(row.fz == null){
        row.fz = "";
    }
    var html = "<input id='fz_"+index+"'  value='"+row.fz+"' text='"+row.fz+"' name='fz_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'fz\')\" autocomplete='off'></input>";
    return html;
}

/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
    var html = "<span style='margin-left:5px;' class='btn btn-default' title='移出' onclick=\"delQuestionBank('" + index + "',event)\" >删除</span>";
    return html;
}
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delQuestionBank(index,event,id){
    var t_data = JSON.parse($("#editTrainForm #tmmx_json").val());
    t_map.rows.splice(index,1);
    for(var i=0;i<t_data.length;i++){
        if(t_data[i].index == index){
            t_data.splice(i,1);
        }
    }
    $("#editTrainForm #tb_list").bootstrapTable('load',t_map);
    var json=[];
    var zf=0;
    if(t_data.length==0){
        $("#editTrainForm #cszf").text(0);
    }
    for(var i=0;i<t_data.length;i++){
        var sz={"index":i,"tkmc":'',"tmlx":'',"sl":'',"zsl":'',"fz":''};
        sz.tkmc=t_data[i].tkmc;
        sz.tmlx=t_data[i].tmlx;
        sz.sl=t_data[i].sl;
        sz.zsl=t_data[i].zsl;
        sz.fz=t_data[i].fz;
        json.push(sz);
        var sl= parseInt(t_data[i].sl);
        var fz= parseInt(t_data[i].fz);
        zf=parseInt(zf)+(sl*fz);
        zf=zf.toString();
        if(zf!='NaN'){
            $("#editTrainForm #cszf").text(zf);
        }
    }
    sum=sum-1;
    $("#editTrainForm #tmmx_json").val(JSON.stringify(json));
}
/**
 * 初始化明细json字段
 * @returns
 */
function settmmx_json(){
    var sfcs=$("#editTrainForm #sfcs_t").val();
    if(sfcs=="否"){
        return "";
    }
    var json=[];
    var data=$('#editTrainForm #tb_list').bootstrapTable('getData');//获取选择行数据
    for(var i=0;i<data.length;i++){
        var sz={"index":i,"tkmc":'',"tmlx":'',"sl":'',"zsl":'',"fz":''};
        sz.tkmc=data[i].tkmc;
        sz.tmlx=data[i].tmlx;
        sz.sl=data[i].sl;
        sz.zsl=data[i].zsl;
        sz.fz=data[i].fz;
        json.push(sz);
    }
    $("#editTrainForm #tmmx_json").val(JSON.stringify(json));
}
function btnBind(){
    setTimeout(function() {
        settmmx_json();
    }, 1000);
}
/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @param bj
 * @returns
 */
function checkSum(index,e,zdmc){
    if (ssgs==null||ssgs=='') {
        $.error("请先选择所属公司");
        for (var i=0;i<sum;i++)
        {
            $("#tkmc_"+i).html("");
        }
        return;
    }
    var tkmc = $("#editTrainForm #tkmc_"+index).val();
    var tmlx = $("#editTrainForm #tmlx_"+index).val();
    $.ajax({
        url :  $('#editTrainForm #urlPrefix').val()+'/train/question/pagedataCountInfo',
        type : 'post',
        data : {
            "tkid" :tkmc,
            "tmlx": tmlx,
            "access_token" : $("#ac_tk").val()
        },
        dataType : 'json',
        success : function(result) {
            $("#editTrainForm #zsl_"+index).val(result.count);
            var s = result.count.toString();
            var data=JSON.parse($("#editTrainForm #tmmx_json").val());
            var zf=0;
            for(var i=0;i<data.length;i++){
                if(data[i].index == index){
                    if(zdmc=='tkmc'){
                        data[i].tkmc=e.value;
                        t_map.rows[i].tkmc=e.value;
                        data[i].zsl=s;
                        t_map.rows[i].zsl=s;
                    }else if(zdmc=='tmlx'){
                        data[i].tmlx=e.value;
                        t_map.rows[i].tmlx=e.value;
                        data[i].zsl=s;
                        t_map.rows[i].zsl=s;
                    }else if(zdmc=='sl'){
                        data[i].sl=e.value;
                        t_map.rows[i].sl=e.value;
                    }else if(zdmc=='fz'){
                        data[i].fz=e.value;
                        t_map.rows[i].fz=e.value;
                    }
                }
                var sl= parseInt(data[i].sl);
                var fz= parseInt(data[i].fz);
                zf=parseInt(zf)+(sl*fz);
                zf=zf.toString();
                if(zf!='NaN'){
                    $("#editTrainForm #cszf").text(zf);
                }
            }
            $("#editTrainForm #tmmx_json").val(JSON.stringify(data));
        }
    });
}

//添加日期控件
laydate.render({
    elem: '#gqsj'
    , theme: '#2381E9'
    , type: 'date'
});
function view(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
        var url=$("#editTrainForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
        $.showDialog(url,'图片预览',JPGMaterConfig);
    }else if(type.toLowerCase()==".pdf"){
        var url= $("#editTrainForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',JPGMaterConfig);
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}

var JPGMaterConfig = {
    width		: "800px",
    offAtOnce	: true,
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var wj_map=[];
var editGlwj_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#editTrainForm #glwj_list").bootstrapTable({
            url:   $('#editTrainForm #urlPrefix').val()+'/train/train/pagedataGetGlWj',         //请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#editTrainForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"wj.lrsj",					// 排序字段
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
            showColumns: false,                  // 是否显示所有的列
            showRefresh: false,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "wjid",                     // 每一行的唯一标识，一般为主键列
            showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns:  [
                {
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index+1;
                    },
                    titleTooltip:'序号',
                    width: '3%',
                    align: 'left',
                    visible:true
                }, {
                    field: 'wjbh',
                    title: '文件编号',
                    titleTooltip:'文件编号',
                    width: '15%',
                    align: 'left',
                    visible: true,
                    sortable: true
                }, {
                    field: 'wjmc',
                    title: '文件名称',
                    titleTooltip:'文件名称',
                    width: '30%',
                    align: 'left',
                    visible: true,
                    formatter:viewwjxxformat
                }, {
                    field: 'bbh',
                    title: '版本',
                    titleTooltip:'版本',
                    width: '10%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'sxrq',
                    title: '生效日期',
                    titleTooltip:'生效日期',
                    width: '10%',
                    align: 'left',
                    visible: true,
                    sortable: true
                },{
                    field: 'cz',
                    title: '操作',
                    width: '5%',
                    align: 'left',
                    formatter:wjczformat,
                    visible: true
                }],
            onLoadSuccess:function(map){
                wj_map=map;
                if (wj_map){
                    var  json = []
                    for (var i = wj_map.rows.length-1; i >=0; i--) {
                        json.push(wj_map.rows[i].wjid)
                    }
                    $("#editTrainForm #glwjids").val(JSON.stringify(json))
                }else {
                    var json = [];
                    $("#editTrainForm #glwjids").val(JSON.stringify(json))
                }
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            },
        });
    };
    oTableInit.queryParams=function(params){
        var cskz1=$("#editTrainForm #ssgs option:selected").attr("cskz1");
        if (cskz1==undefined||cskz1==null){
            cskz1 = "";
        }
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            access_token:$("#ac_tk").val(),
            pxid:$("#editTrainForm #pxid").val(),
            fwqz:cskz1,//访问前缀
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit;
}
/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wjczformat(value,row,index){
    var html="";
    html = "<span style='margin-left:5px;' class='btn btn-danger' title='删除' onclick=\"deleteDetail('" + index + "')\" >删除</span>";
    return html;
}
/**
 * 文件查看
 * @param value
 * @param row
 * @param index
 * @returns
 */
function viewwjxxformat(value,row,index){
    var html = "";
    if($('#ac_tk').length > 0){
        html += "<a href='javascript:void(0);' onclick=\"viewwjxx('"+row.wjid+"')\">"+row.wjmc+"</a>"
    }else{
        html += row.wjmc;
    }
    return html;
}
function viewwjxx(wjid){
    var cskz1=$("#editTrainForm #ssgs option:selected").attr("cskz1");
    if (cskz1==undefined||cskz1==null){
        cskz1 = "";
    }
    var url=cskz1+"/production/document/viewDocument?wjid="+wjid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'文件查看',viewWjxxConfig);
}
var viewWjxxConfig = {
    width		: "1000px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function deleteDetail(index){
    wj_map.rows.splice(index,1);
    $("#editTrainForm #glwj_list").bootstrapTable('load',wj_map);
    var  json = []
    for (var i = wj_map.rows.length-1; i >=0; i--) {
        json.push(wj_map.rows[i].wjid)
    }
    $("#editTrainForm #glwjids").val(JSON.stringify(json));
}

function addGlwj() {
    var  ssgs = $("#editTrainForm #ssgs").val();
    if (!ssgs){
        $.confirm("请先选择所属公司！")
        return;
    }
    var cskz1=$("#editTrainForm #ssgs option:selected").attr("cskz1");
    if (cskz1==undefined||cskz1==null){
        cskz1 = "";
    }
    var url = cskz1+"/production/document/pagedataListDocument?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择文件', chooseWjConfig);
}
var chooseWjConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseWjModal",
    formName	: "chooseDocumentForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chooseDocumentForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var sel = $("#chooseDocumentForm #yxwj").tagsinput('items');
                var json = JSON.parse($("#editTrainForm #glwjids").val());
                var ids="";
                for(var i = 0; i < sel.length; i++){
                    var value = sel[i].value;
                    ids+=","+value;
                }
                //如果有选中数据
                if(ids){
                    ids=ids.substring(1);
                    //先将原表数据中未选中的数据删除
                    for (var i = wj_map.rows.length-1; i >=0; i--) {
                        if(wj_map.rows[i].wjid){
                            if(ids.indexOf(wj_map.rows[i].wjid)==-1){
                                json.splice(wj_map.rows[i].wjid,1)
                                wj_map.rows.splice(i,1);
                            }
                        }
                    }
                    var cskz1=$("#editTrainForm #ssgs option:selected").attr("cskz1");
                    if (cskz1==undefined||cskz1==null){
                        cskz1 = "";
                    }
                    $.ajax({
                        async:false,
                        url: cskz1+'/production/document/pagedataGetDocumentDetails',
                        type: 'post',
                        dataType: 'json',
                        data : {"ids":ids, "access_token":$("#ac_tk").val()},
                        success: function(data) {
                            if(data.wjglDtos.length>0){
                                for (var i = 0; i < data.wjglDtos.length; i++) {
                                    var isFind=false;
                                    for (var j = 0; j < wj_map.rows.length; j++) {
                                        if(data.wjglDtos[i].wjid==wj_map.rows[j].wjid){
                                            isFind=true;
                                            break;
                                        }
                                    }
                                    if(!isFind){
                                        var sz = {"wjid":'',"wjbh":'',"wjmc":'',"bbh":'',"sxrq":'',"xh":json.length};
                                        sz.wjbh = data.wjglDtos[i].wjbh;
                                        sz.wjmc = data.wjglDtos[i].wjmc;
                                        sz.bbh = data.wjglDtos[i].bbh;
                                        sz.sxrq = data.wjglDtos[i].sxrq;
                                        sz.wjid = data.wjglDtos[i].wjid;
                                        wj_map.rows.push(sz);
                                        json.push(sz.wjid);
                                    }
                                }
                            }
                        }
                    });
                }else{//没有选中数据则将表中的清除
                    for (var i = wj_map.rows.length-1; i >=0; i--) {
                        if(wj_map.rows[i].wjid){
                            json.splice(wj_map.rows[i].wjid,1)
                            wj_map.rows.splice(i,1);
                        }
                    }
                }
                $("#editTrainForm #glwj_list").bootstrapTable('load',wj_map);
                $("#editTrainForm #glwjids").val(JSON.stringify(json));
                $.closeModal(opts.modalName);
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
$(document).ready(function(){
    var oTable=new QuestionBank_TableInit();
    oTable.Init();
    btnBind();
    //0.初始化fileinput
    var oFileInput = new TrainFileInput();
    oFileInput.Init("editTrainForm","displayUpInfo",2,1,"sign_file",$("#editTrainForm #tpywlx").val());
    var oFileInput_t = new TrainFileInput();
    oFileInput_t.Init("editTrainForm","displayUpInfo_sp",2,1,"sign_file_sp",$("#editTrainForm #spywlx").val());
    var fjid=$("#editTrainForm #fjid").val();
    if(fjid!=null&&fjid!=""){
        $("#editTrainForm #yctp").show();
    }else{
        $("#editTrainForm #yctp").hide();
    }
    var spfj=$("#editTrainForm #spfj").val();
    var spbj=$("#editTrainForm #spbj_t").val();
    if(spbj=="否"){
        $("#editTrainForm #spsc").hide();
        $("#editTrainForm #ycsp").hide();
        $("#editTrainForm #hspxz").hide();
    }
    var sfcs=$("#editTrainForm #sfcs_t").val();
    if(sfcs=="否"){
        $("#editTrainForm #csDiv").hide();
        $("#editTrainForm #danxts").removeAttr("validate");
        $("#editTrainForm #duoxts").removeAttr("validate");
        $("#editTrainForm #jdts").removeAttr("validate");
        $("#editTrainForm #cszf").removeAttr("validate");
        $("#editTrainForm #tgfs").removeAttr("validate");

    }
    var sffshb=$("#editTrainForm #sffshb_t").val();
    if (sffshb=='否'){
        $("#editTrainForm #hbsz").hide();
        $("#editTrainForm #hbszid").removeAttr("validate");
    }
    var formAction=$("#editTrainForm #formAction").val();
    if(formAction=="pagedataAddSaveTrain"){
        $('input:radio[name="sfgk"][value="0"]').prop('checked', true);
        $('input:radio[name="spbj"][value="1"]').prop('checked', true);
        $('input:radio[name="sfcs"][value="1"]').prop('checked', true);
        $('input:radio[name="ckdabj"][value="1"]').prop('checked', true);
        $('input:radio[name="dcckbj"][value="1"]').prop('checked', true);
        $('input:radio[name="spxz"][value="0"]').prop('checked', true);
        $('input:radio[name="sffshb"][value="0"]').prop('checked', true);
        $('input:radio[name="dqtx"][value="1"]').prop('checked', true);
        $("#editTrainForm #hbsz").hide();
        $("#editTrainForm #hbszid").removeAttr("validate");
    }else {
        var spxz=$("#editTrainForm #xgqspxz").val();
        if (spxz!='1'&&spxz!='0'){
            $('input:radio[name="spxz"][value="0"]').prop('checked', true);
        }
    }
    var zf=$("#editTrainForm #zf").val();
    if(zf!=null&&zf!=''){
        $("#editTrainForm #cszf").text(zf);
    }else{
        $("#editTrainForm #cszf").text(0);
    }
    init();
    if (formAction=="pagedataAddSaveTrain"){
        $("#editTrainForm #hbvalue").val("0");
    }else {
       if ($("#editTrainForm #hbvalue").val()=='否'){
           $("#editTrainForm #hbvalue").val("0");
       }else {
           $("#editTrainForm #hbvalue").val("1");
       }
    }

    //所有下拉框添加choose样式
    jQuery('#editTrainForm .chosen-select').chosen({width: '100%'});
    var glwjOTable=new editGlwj_TableInit();
    glwjOTable.Init();
});