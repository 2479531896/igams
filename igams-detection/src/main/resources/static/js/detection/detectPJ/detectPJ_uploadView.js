var UpList_timer = null
var flag=false;
var errorFlag=false;
var t_map=[];


var uploadDetectionPJView_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#uploadDetectionPJViewForm #tb_list').bootstrapTable({
            url: '/detectionPJ/detectionPJ/pagedataFluUpViewList',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#uploadDetectionPJViewForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [
                {
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index+1;
                    },
                    width: '3%',
                    align: 'left',
                    visible:true
                }, {
                    field: 'syh',
                    title: '实验号',
                    width: '12%',
                    align: 'left',
                    visible: true,
                }, {
                    field: 'jcxmmc',
                    title: '项目',
                    width: '15%',
                    align: 'left',
                    visible: true,
                }, {
                    field: 'jczxmmc',
                    title: '子项目',
                    width: '10%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'jgmc',
                    title: '普检结果',
                    width: '10%',
                    align: 'left',
                    visible: true,
                }, {
                    field: 'ctz',
                    title: 'CT值',
                    width: '15%',
                    align: 'left',
                    visible: true,
                }, {
                    field: 'jgid',
                    title: '结果',
                    width: '20%',
                    align: 'left',
                    formatter:jgFormat,
                    visible: true,
                }, {
                    field: 'sczt',
                    title: '上传状态',
                    width: '16%',
                    align: 'left',
                    formatter:scztFormat,
                    visible: true,
                    sortable: true
                }],
            onLoadSuccess: function (map) {
                if(map.count){
                    $('#uploadDetectionPJViewForm #num').text(map.count);
                    if(map.count==$('#uploadDetectionPJViewForm #count').text()){
                        flag=true;
                        if(map.rows&&map.rows.length==0){
                            errorFlag=true;
                        }else{
                            t_map=map;
                            initTab();
                        }
                    }
                }
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function(params){
        var map = {
            access_token:$("#ac_tk").val(),
            fjids:$("#uploadDetectionPJViewForm #fjids").val()
        };
        return map;
    };
    return oTableInit;
};

function initTab(){
    var html="";
    if(t_map.fzzkjgDtos&&t_map.fzzkjgDtos.length>0){
        var QC_FAM="";
        var QC_VIC="";
        var QC_CY5="";
        var NC_FAM="";
        var NC_VIC="";
        var NC_CY5="";
        for (var i = 0; i < t_map.fzzkjgDtos.length; i++) {
            if("QC"==t_map.fzzkjgDtos[i].zkmc){
                if("FAM"==t_map.fzzkjgDtos[i].td&&t_map.fzzkjgDtos[i].ctz){
                    QC_FAM=t_map.fzzkjgDtos[i].ctz;
                }else if("VIC"==t_map.fzzkjgDtos[i].td&&t_map.fzzkjgDtos[i].ctz){
                    QC_VIC=t_map.fzzkjgDtos[i].ctz;
                }else if("CY5"==t_map.fzzkjgDtos[i].td&&t_map.fzzkjgDtos[i].ctz){
                    QC_CY5=t_map.fzzkjgDtos[i].ctz;
                }
            }else if("NC"==t_map.fzzkjgDtos[i].zkmc){
                if("FAM"==t_map.fzzkjgDtos[i].td&&t_map.fzzkjgDtos[i].ctz){
                    NC_FAM=t_map.fzzkjgDtos[i].ctz;
                }else if("VIC"==t_map.fzzkjgDtos[i].td&&t_map.fzzkjgDtos[i].ctz){
                    NC_VIC=t_map.fzzkjgDtos[i].ctz;
                }else if("CY5"==t_map.fzzkjgDtos[i].td&&t_map.fzzkjgDtos[i].ctz){
                    NC_CY5=t_map.fzzkjgDtos[i].ctz;
                }
            }
        }

        if(QC_FAM||QC_VIC||QC_CY5){
            html+="<tr>";
            html+="<td style='text-align: center'>QC</td>";
            html+="<td style='text-align: center'>"+QC_FAM+"</td>";
            html+="<td style='text-align: center'>"+QC_VIC+"</td>";
            html+="<td style='text-align: center'>"+QC_CY5+"</td>";
            html+="</tr>";
        }
        if(NC_FAM||NC_VIC||NC_CY5){
            html+="<tr>";
            html+="<td style='text-align: center'>NC</td>";
            html+="<td style='text-align: center'>"+NC_FAM+"</td>";
            html+="<td style='text-align: center'>"+NC_VIC+"</td>";
            html+="<td style='text-align: center'>"+NC_CY5+"</td>";
            html+="</tr>";
        }
    }
    $("#uploadDetectionPJViewForm  #tabBody").empty();
    $("#uploadDetectionPJViewForm  #tabBody").append(html);
}

function jgFormat(value,row,index) {
    var jgid=row.jcjg;
    var str=$("#uploadDetectionPJViewForm #jgList").val();
    var jgList=JSON.parse(str);
    var html="";
    for(var i=0;i<jgList.length;i++){
        html+="<div class='col-md-6 col-sm-6'>";
        if(jgid==jgList[i].csid){
            html+=" <input type='radio'  value='"+jgList[i].csid+"' name='jcjg_"+row.fzjcjgid+"' checked>"+jgList[i].csmc+"</input>";
        }else{
            html+=" <input type='radio'  value='"+jgList[i].csid+"'  name='jcjg_"+row.fzjcjgid+"' >"+jgList[i].csmc+"</input>";
        }
        html+="</div>";
    }
    return html;
}

function scztFormat(value,row,index) {
    if (row.sczt == '80') {
        return "<span  style='color:green;'>上传成功</span>";
    }else if (row.sczt == '15'){
        return "<span  style='color:red;'>实验数据与项目不匹配</span>";
    }else{
        return "";
    }
}

$(function(){
    var oTable = new uploadDetectionPJView_TableInit();
    oTable.Init();
    if (!$("#uploadDetectionPJViewForm #fjids").val())
    return;
    UpList_timer = setInterval(function () {
        $('#uploadDetectionPJViewForm #tb_list').bootstrapTable('refresh');
        if(errorFlag){
            clearInterval(UpList_timer);
            $("#uploadDetectionPJViewForm #loading").remove();
            $("#uploadDetectionPJViewForm #tabDiv").hide();
            $("#uploadDetectionPJViewForm #errorDiv").show();
        }else{
            if(flag){
                clearInterval(UpList_timer);
                $("#uploadDetectionPJViewForm #loading").remove();
                $("#uploadDetectionPJViewForm #tabDiv").show();
                $("#uploadDetectionPJViewForm #errorDiv").hide();
            }
        }
    },2000)
})

$(".bootbox-close").click(function(){
    clearInterval(UpList_timer)
});

$("#btn_cancel").click(function(){
    clearInterval(UpList_timer)
});

function reUpload(){
    $.showDialog("/detectionPJ/detectionPJ/resultuploadDetectionPJ?jclx="+$("#uploadDetectionPJViewForm #jclx").val(),'结果上传',uploadDetectionPJConfig);
    $.closeModal("viewUpViewModal");
}

var uploadDetectionPJConfig = {
    width		: "900px",
    modalName	: "uploadDetectionPJModal",
    formName	: "uploadDetectionPJForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#uploadDetectionPJForm").valid()){
                    return false;
                }
                if(!$("#uploadDetectionPJForm #fjids").val()){
                    $.error("请上传文件！")
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#uploadDetectionPJForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"uploadDetectionPJForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.closeModal(opts.modalName);
                        $.showDialog("/detectionPJ/detectionPJ/pagedataFluUpView?fjid="+responseText["fjids"]+"&jclx="+responseText["jclx"],'上传进度',viewUpViewConfig);
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

