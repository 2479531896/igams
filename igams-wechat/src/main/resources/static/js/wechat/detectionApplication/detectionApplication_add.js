
$('input[type=radio][name=lx]').change(function() {
    if (this.value == '1') {
        $("#addDetectionApplicationForm #hbDiv").show();
        $("#addDetectionApplicationForm #ybxxDiv").hide();
        $("#addDetectionApplicationForm #wkxxDiv").hide();
        $("#addDetectionApplicationForm #bbDiv").hide();
    } else if (this.value == '0') {
        $("#addDetectionApplicationForm #hbDiv").hide();
        $("#addDetectionApplicationForm #ybxxDiv").show();
        $("#addDetectionApplicationForm #wkxxDiv").show();
        $("#addDetectionApplicationForm #bbDiv").show();
    }
});



//打开文件上传界面
function editfile(divName,btnName){
    $("#addDetectionApplicationForm"+"  #"+btnName).hide();
    $("#addDetectionApplicationForm"+"  #"+divName).show();
}
//关闭文件上传界面
function cancelfile(divName,btnName){
    $("#addDetectionApplicationForm"+"  #"+btnName).show();
    $("#addDetectionApplicationForm"+"  #"+divName).hide();
}
function displayUpInfo(fjid){
    if(!$("#addDetectionApplicationForm #fjids").val()){
        $("#addDetectionApplicationForm #fjids").val(fjid);
    }else{
        $("#addDetectionApplicationForm #fjids").val($("#addDetectionApplicationForm #fjids").val()+","+fjid);
    }
    analysisExcel();
}
function analysisExcel(){
    $.ajax({
        type:'post',
        async: false,
        url:"/application/application/pagedataAnalysisFile",
        cache: false,
        data: {"fjids":$("#addDetectionApplicationForm #fjids").val(),"xzbj":$("#addDetectionApplicationForm #xzbj").val(),"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(newData){
            if(newData.list!=null&&newData.list.length>0){
                t_map=[];
                var ybxx="";
                var wkxx="";
                var hbxx="";
                for(var i = 0; i <newData.list.length; i++){
                    ybxx+="["+newData.list[i].ybbh+"-"+newData.list[i].hzxm+"]";
                    if(hbxx.indexOf(newData.list[i].db)==-1){
                        hbxx+=","+newData.list[i].db;
                    }
                    if(newData.list[i].wkbh){
                        wkxx+=","+newData.list[i].wkbh;
                    }
                    var sz = {"sjid":'',"ybbh":'',"hzxm":'',"yblxmc":'',"wkbh":''};
                    sz.sjid = newData.list[i].sjid;
                    sz.ybbh = newData.list[i].ybbh;
                    sz.hzxm = newData.list[i].hzxm;
                    sz.yblxmc = newData.list[i].yblxmc;
                    sz.wkbh = newData.list[i].wkbh;
                    t_map.push(sz);
                }
                $("#addDetectionApplicationForm #ybxx").text(ybxx);
                $("#addDetectionApplicationForm #ybxx").val(ybxx);
                $("#addDetectionApplicationForm #hbxx").text(hbxx?hbxx.substring(1):"");
                $("#addDetectionApplicationForm #hbxx").val(hbxx?hbxx.substring(1):"");
                $("#addDetectionApplicationForm #wkxx").text(wkxx?wkxx.substring(1):"");
                $("#addDetectionApplicationForm #wkxx").val(wkxx?wkxx.substring(1):"");
                $("#addDetectionApplicationForm #tb_list").bootstrapTable('load',t_map);
            }
        }
    });
}

var t_map=[];
var tb_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#addDetectionApplicationForm #tb_list").bootstrapTable({
            url:   '/application/application/pagedataGetDetails',         //请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#addDetectionApplicationForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"jcsqmx.lrsj",					// 排序字段
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
            uniqueId: "sqmxid",                     // 每一行的唯一标识，一般为主键列
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
                    width: '8%',
                    align: 'left',
                    visible:true
                },{
                    field: 'ybbh',
                    title: '标本编号',
                    width: '28%',
                    align: 'left',
                    visible: true
                },{
                    field: 'hzxm',
                    title: '患者姓名',
                    width: '26%',
                    align: 'left',
                    visible: true
                },{
                    field: 'yblxmc',
                    title: '样本类型',
                    width: '28%',
                    align: 'left',
                    visible: true
                },{
                    field: 'cz',
                    title: '操作',
                    width: '10%',
                    align: 'left',
                    formatter:czformat,
                    visible: true
                }],
            onLoadSuccess:function(map){
                t_map=map;
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
            access_token:$("#ac_tk").val(),
            sqglid: $('#addDetectionApplicationForm #sqglid').val()
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
function czformat(value,row,index){
    var html="";
    html = "<span style='margin-left:5px;' class='btn btn-danger' title='删除' onclick=\"deleteDetail('" + index + "')\" >删除</span>";
    return html;
}

/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function deleteDetail(index){
    t_map.rows.splice(index,1);
    if(t_map.rows){
        var ybxx="";
        var wkxx="";
        var hbxx="";
        for (var i = 0; i < t_map.rows.length; i++) {
            ybxx+="["+t_map.rows[i].ybbh+"-"+t_map.rows[i].hzxm+"]";
            if(hbxx.indexOf(t_map.rows[i].db)==-1){
                hbxx+=","+t_map.rows[i].db;
            }
            if(t_map.rows[i].wkbh){
                wkxx+=","+t_map.rows[i].wkbh;
            }
        }
        $("#addDetectionApplicationForm #ybxx").text(ybxx);
        $("#addDetectionApplicationForm #ybxx").val(ybxx);
        $("#addDetectionApplicationForm #hbxx").text(hbxx?hbxx.substring(1):"");
        $("#addDetectionApplicationForm #hbxx").val(hbxx?hbxx.substring(1):"");
        $("#addDetectionApplicationForm #wkxx").text(wkxx?wkxx.substring(1):"");
        $("#addDetectionApplicationForm #wkxx").val(wkxx?wkxx.substring(1):"");
    }else{
        $("#addDetectionApplicationForm #ybxx").text("");
        $("#addDetectionApplicationForm #ybxx").val("");
        $("#addDetectionApplicationForm #hbxx").text("");
        $("#addDetectionApplicationForm #hbxx").val("");
        $("#addDetectionApplicationForm #wkxx").text("");
        $("#addDetectionApplicationForm #wkxx").val("");
    }
    $("#addDetectionApplicationForm #tb_list").bootstrapTable('load',t_map);
}

function addSample() {
    var url = "/application/application/pagedataChooseSamples?access_token=" + $("#ac_tk").val()+"&xzbj="+ $("#addDetectionApplicationForm #xzbj").val();
    $.showDialog(url, '选择标本', chooseSampleConfig);
}
var chooseSampleConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseSampleModal",
    formName	: "chooseSampleForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {

                var $this = this;
                var opts = $this["options"]||{};
                var sel = $("#chooseSampleForm #yxbb").tagsinput('items');
                var ids="";
                for(var i = 0; i < sel.length; i++){
                    var value = sel[i].value;
                    ids+=","+value;
                }
                //如果有选中数据
                if(ids){
                    ids=ids.substring(1);
                    //先将原表数据中未选中的数据删除
                    for (var i = t_map.rows.length-1; i >=0; i--) {
                        if(t_map.rows[i].sjid){
                            if(ids.indexOf(t_map.rows[i].sjid)==-1){
                                t_map.rows.splice(i,1);
                            }
                        }
                    }
                    $.ajax({
                        async:false,
                        url: '/application/application/pagedataGetSamples',
                        type: 'post',
                        dataType: 'json',
                        data : {"ids":ids, "access_token":$("#ac_tk").val()},
                        success: function(data) {
                            if(data.sjxxDtos.length>0){
                                for (var i = 0; i < data.sjxxDtos.length; i++) {
                                    var isFind=false;
                                    for (var j = 0; j < t_map.rows.length; j++) {
                                        if(data.sjxxDtos[i].sjid==t_map.rows[j].sjid){
                                            isFind=true;
                                            break;
                                        }
                                    }
                                    if(!isFind){
                                        var sz = {"sjid":'',"ybbh":'',"hzxm":'',"yblxmc":'',"wkbh":'','db':''};
                                        sz.sjid = data.sjxxDtos[i].sjid;
                                        sz.ybbh = data.sjxxDtos[i].ybbh;
                                        sz.hzxm = data.sjxxDtos[i].hzxm;
                                        sz.yblxmc = data.sjxxDtos[i].yblxmc;
                                        sz.wkbh = data.sjxxDtos[i].wkbh;
                                        sz.db = data.sjxxDtos[i].db;
                                        t_map.rows.push(sz);
                                    }
                                }
                            }
                        }
                    });
                }else{//没有选中数据则将表中的物料清除
                    for (var i = t_map.rows.length-1; i >=0; i--) {
                        if(t_map.rows[i].wlid){
                            t_map.rows.splice(i,1);
                        }
                    }
                }
                if(t_map.rows){
                    var ybxx="";
                    var wkxx="";
                    var hbxx="";
                    for (var i = 0; i < t_map.rows.length; i++) {
                        ybxx+="["+t_map.rows[i].ybbh+"-"+t_map.rows[i].hzxm+"]";
                        if(hbxx.indexOf(t_map.rows[i].db)==-1){
                            hbxx+=","+t_map.rows[i].db;
                        }
                        if(t_map.rows[i].wkbh){
                            wkxx+=","+t_map.rows[i].wkbh;
                        }
                    }
                    $("#addDetectionApplicationForm #ybxx").text(ybxx);
                    $("#addDetectionApplicationForm #ybxx").val(ybxx);
                    $("#addDetectionApplicationForm #hbxx").text(hbxx?hbxx.substring(1):"");
                    $("#addDetectionApplicationForm #hbxx").val(hbxx?hbxx.substring(1):"");
                    $("#addDetectionApplicationForm #wkxx").text(wkxx?wkxx.substring(1):"");
                    $("#addDetectionApplicationForm #wkxx").val(wkxx?wkxx.substring(1):"");
                }
                $("#addDetectionApplicationForm #tb_list").bootstrapTable('load',t_map);
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

function xz(fjid){
    jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}
/**
 * 点击删除文件
 * @param fjid
 * @param wjlj
 * @returns
 */
function del(fjid,wjlj){
    $.confirm('您确定要删除所选择的记录吗？',function(result){
        if(result){
            jQuery.ajaxSetup({async:false});
            var url= "/common/file/delFile";
            jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
                setTimeout(function(){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            $("#"+fjid).remove();
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

function yl(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
        var url="/ws/sjxxpripreview?pageflg=1&fjid="+fjid
        window.open(url);
    }else if(type.toLowerCase()==".pdf"){
        var url="/common/view/displayView?view_url=/ws/file/pdfPreview?fjid=" + fjid;
        window.open(url);
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}

$(function () {
    var oTable_t=new tb_TableInit();
    oTable_t.Init();
    var oFileInput = new FileInput();
    oFileInput.Init("addDetectionApplicationForm","displayUpInfo",2,1,"sign_file");
    var formAction = $("#addDetectionApplicationForm #formAction").val();
    if("addSaveDetectionApplication"==formAction){
        $(":radio[name='lx'][value='0']").prop("checked", "checked");
        $("#addDetectionApplicationForm #hbDiv").hide();
        $("#addDetectionApplicationForm #ybxxDiv").show();
        $("#addDetectionApplicationForm #wkxxDiv").show();
        $("#addDetectionApplicationForm #bbDiv").show();
    }else{
        var lx = $('#addDetectionApplicationForm input:radio[name="lx"]:checked').val();
        if ('1'==lx) {
            $("#addDetectionApplicationForm #hbDiv").show();
            $("#addDetectionApplicationForm #ybxxDiv").hide();
            $("#addDetectionApplicationForm #wkxxDiv").hide();
            $("#addDetectionApplicationForm #bbDiv").hide();
        } else if ('0'==lx) {
            $("#addDetectionApplicationForm #hbDiv").hide();
            $("#addDetectionApplicationForm #ybxxDiv").show();
            $("#addDetectionApplicationForm #wkxxDiv").show();
            $("#addDetectionApplicationForm #bbDiv").show();
        }
    }

    $("#addDetectionApplicationForm #wzxxdiv").hide();
    if( $("#addDetectionApplicationForm #sqlx_csdm").val()=="WZ"){

        $("#addDetectionApplicationForm #wzxxdiv").show();
    }
     $("#addDetectionApplicationForm #sqlx").change(function(){
        var csdm = $('#sqlx option:selected').attr("csdm");
        if (csdm){
            if (csdm == 'WZ'){
                $("#addDetectionApplicationForm #wzxxdiv").show();
            }else {
                $("#addDetectionApplicationForm #wzxx").val("");
                $("#addDetectionApplicationForm #wzxxdiv").hide();
            }
        }
    });
    jQuery('#addDetectionApplicationForm .chosen-select').chosen({width: '100%'});
});