var access_token=$("#ac_tk").val();

var vm = new Vue({
    el:'#ajaxForm_resfirstEdit',
    data: {
    },
    methods:{
        xz_bg:function(fjid){
            jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
                '<input type="text" name="fjid" value="'+fjid+'"/>' +
                '<input type="text" name="access_token" value="'+access_token+'"/>' +
                '</form>')
                .appendTo('body').submit().remove();
        },
        del_bg:function(fjid,wjlj){
            $.confirm('您确定要删除所选择的记录吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url= "/inspection/pathogen/pagedataDelFile";
                    jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":access_token},function(responseText){
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
    }
})
function selectHospital(){
    url="/wechat/hospital/pagedataCheckUnitView?access_token=" + access_token+"&dwFlag=1";
    $.showDialog(url,'医院名称',SelectResFirstHospitalConfig);
};
//医院列表弹出框
var SelectResFirstHospitalConfig = {
    width		: "1000px",
    modalName	:"SelectResFirstHospitalConfig",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row=$('#hospital_formSearch #hospital_list').bootstrapTable('getSelections');
                if(sel_row.length==1){
                    var dwid=sel_row[0].dwid;
                    var dwmc=sel_row[0].dwmc;
                    var cskz1=sel_row[0].cskz1;
                    $("#ajaxForm_resfirstEdit #sjdw").val(dwid);
                    $("#ajaxForm_resfirstEdit #hospitalname").val(dwmc);

                    $("#ajaxForm_resfirstEdit #sjdwmc").removeAttr("disabled");
                    $("#ajaxForm_resfirstEdit #sjdwmc").val(dwmc);
                    $("#ajaxForm_resfirstEdit #otherHospital").show();
                    // if(cskz1=='1'){
                    //     $("#ajaxForm_resfirstEdit #sjdwmc").val(null);
                    //     $("#ajaxForm_resfirstEdit #otherHospital").show();
                    // }else{
                    //     $("#ajaxForm_resfirstEdit #sjdwmc").val(dwmc);
                    //     $("#ajaxForm_resfirstEdit #otherHospital").hide();
                    // }
                }else{
                    $.error("请选中一行!");
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

/*$("#ajaxForm_resfirstEdit .jcxm").click(function(e){
    checkJcxm();
    // 查询检测子项目
    // getSubDetect(e.currentTarget.id, 1);
})*/
// function checkJcxm(){
//     limitYblx();
//     var cskz1=$("input[name='jcxmids']:checked").attr("cskz1");
//     if(cskz1=="C"){
//         $("#ajaxForm_resfirstEdit #sfzmjc").val("0");
//         $("#ajaxForm_resfirstEdit #sfsf").children().eq(2).removeAttr("disabled");
//         $("#ajaxForm_resfirstEdit #sfsf").children().eq(3).removeAttr("disabled");
//         $("#ajaxForm_resfirstEdit #sfsf").trigger("chosen:updated");
//         $("#ajaxForm_resfirstEdit #zmmdd_div").hide();
//         $("#ajaxForm_resfirstEdit #zmmdd").attr("disabled","disabled");
//         $("#ajaxForm_resfirstEdit #zmmdd").trigger("chosen:updated");
//     }else if(cskz1=="F"||cskz1=="Z"||cskz1=="Z6"||cskz1=="Z8"||cskz1=="Z12"||cskz1=="Z18"||cskz1=="T3"||cskz1=="T6"||cskz1=="K"){
//         $("#ajaxForm_resfirstEdit #sfzmjc").val("1");
//         $("#ajaxForm_resfirstEdit #zmmdd_div").show();
//         $("#ajaxForm_resfirstEdit #zmmdd").removeAttr("disabled");
//         $("#ajaxForm_resfirstEdit #zmmdd").trigger("chosen:updated");
//     }else if(cskz1=="D" || cskz1=="R"){
//         $("#ajaxForm_resfirstEdit #sfzmjc").val("0");
//         $("#ajaxForm_resfirstEdit #sfsf").children().eq(2).attr("disabled","disabled");
//         $("#ajaxForm_resfirstEdit #sfsf").children().eq(3).attr("disabled","disabled");
//         $("#ajaxForm_resfirstEdit #sfsf").children().eq(2).removeAttr("selected");
//         $("#ajaxForm_resfirstEdit #sfsf").children().eq(3).removeAttr("selected");
//         $("#ajaxForm_resfirstEdit #sfsf").trigger("chosen:updated");
//         $("#ajaxForm_resfirstEdit #zmmdd_div").hide();
//         $("#ajaxForm_resfirstEdit #zmmdd").attr("disabled","disabled");
//         $("#ajaxForm_resfirstEdit #zmmdd").trigger("chosen:updated");
//     }
// }
/**
 * 根据检测项目获取检测子项目
 * @returns
 */
// function getSubDetect(jcxmid, gxbj){
//     $.ajax({
//         url: '/inspection/inspection/pagedataSubDetect',
//         type: 'post',
//         data: {
//             "jcxmid": jcxmid,
//             "access_token": access_token,
//         },
//         dataType: 'json',
//         success: function(result) {
//             var data = result.subdetectlist;
//             $("#ajaxForm_resfirstEdit input[name='jczxm']").attr("checked",false);
//             $("#ajaxForm_resfirstEdit #jczxmDiv").empty();
//             var jczxmHtml = "";
//             jczxmHtml += "<option value=''>--请选择--</option>";
//             if(data != null && data.length > 0){
//                 $.each(data,function(i){
//                     jczxmHtml += "<option id='"+data[i].csid+"' value='" + data[i].csid + "'>" +data[i].csmc + "</option>";
//                 });
//                 $("#ajaxForm_resfirstEdit #jczxm_div").show();
//                 $("#ajaxForm_resfirstEdit #jczxm").empty();
//                 $("#ajaxForm_resfirstEdit #jczxm").append(jczxmHtml);
//                 $("#ajaxForm_resfirstEdit #jczxm").trigger("chosen:updated");
//                 $("#ajaxForm_resfirstEdit #jczxm").attr("validate","{required:true}");
//             }else{
//                 $("#ajaxForm_resfirstEdit #jczxm").empty();
//                 $("#ajaxForm_resfirstEdit #jczxm").removeAttr("validate");
//                 $("#ajaxForm_resfirstEdit #jczxm_div").hide();
//                 if(gxbj == 1){
//                     // 清空检测子项目
//                     var sjid = $("#ajaxForm_resfirstEdit #sjid").val();
//                     if(sjid){
//                         $.ajax({
//                             url: '/inspection/inspection/pagedataEmptySubDetect',
//                             type: 'post',
//                             data: {
//                                 "sjid": sjid,
//                                 "access_token": access_token,
//                             },
//                             dataType: 'json',
//                             success: function(result) {
//
//                             }
//                         });
//                     }
//                 }
//             }
//         }
//     });
// }
//限制标本类型
function limitYblx(){
    var yblxlength=$("#ajaxForm_resfirstEdit .t_yblx").length;
    var jcxmid=$("input[name='jcxmids']:checked").attr("id");
    var csdm=$("#ajaxForm_resfirstEdit #"+jcxmid).attr("csdm");
    if(yblxlength>0){
        for(var i=0;i<yblxlength;i++){
            var yblxid=$("#ajaxForm_resfirstEdit .t_yblx")[i].id;
            if(yblxid!=null && yblxid!=""){
                if(jcxmid==null || jcxmid ==""){
                    $("#ajaxForm_resfirstEdit #"+yblxid).attr("style","display:block;");
                }else{
                    var jcxmdm=$("#ajaxForm_resfirstEdit #"+yblxid).attr("cskz2");
                    if(jcxmdm!=null && jcxmdm!=""){
                        if(jcxmdm.indexOf(csdm)>=0){
                            $("#ajaxForm_resfirstEdit #"+yblxid).attr("style","display:block;");
                        }else{
                            $("#ajaxForm_resfirstEdit #"+yblxid).attr("style","display:none;");
                        }
                    }else{
                        $("#ajaxForm_resfirstEdit #"+yblxid).attr("style","display:none;");
                    }
                }
            }
        }
    }
    $('#ajaxForm_resfirstEdit #yblx').trigger("chosen:updated"); //更新下拉框
}
/*function jcxminit(){
    var kzcs=$("#ajaxForm_resfirstEdit #yblx option:selected");
    //限制检测项目
    var csdm=kzcs.attr("cskz2");
    if($("#ajaxForm_resfirstEdit .jcxm").length>0){
        for(var i=0;i<$("#ajaxForm_resfirstEdit .jcxm").length;i++){
            //如果什么都没选
            var id=$("#ajaxForm_resfirstEdit .jcxm")[i].id;
            if(kzcs[0].id==null || kzcs[0].id==""){
                $("#ajaxForm_resfirstEdit #t-"+id).attr("style","display:block;padding-left:0px;");
                $("#ajaxForm_resfirstEdit #"+id).removeAttr('checked');
            }else{
                if(csdm!=null && csdm!=""){
                    if(csdm.indexOf($("#ajaxForm_resfirstEdit #"+id).attr("csdm"))>=0){
                        $("#ajaxForm_resfirstEdit #t-"+id).attr("style","display:block;padding-left:0px;");
                    }else{
                        $("#ajaxForm_resfirstEdit #t-"+id).attr("style","display:none");
                    }
                }else{
                    $("#ajaxForm_resfirstEdit #t-"+id).attr("style","display:none;");
                    $("#ajaxForm_resfirstEdit #"+id).removeAttr('checked');
                }
            }
        }
    }
}*/
// 检测子项目初始化
function jczxminit(){
    if($("#ajaxForm_resfirstEdit #jczxm").children("option").length < 2){
        $("#ajaxForm_resfirstEdit #jczxm").removeAttr("validate");
        $("#ajaxForm_resfirstEdit #jczxm_div").hide();
    }
}
//限制科研项目
function limitKyxm(){
    var sjqf=$('input[name="sjqf"]:checked').val();
    if(sjqf){
        var kyxmlength = $("#ajaxForm_resfirstEdit .t_kyxm").length;
        if(kyxmlength>0){
            var kyxmNum = 0;
            var kyxmShowNum = 0;
            for(var i=0;i<kyxmlength;i++){
                var kyxmid = $("#ajaxForm_resfirstEdit .t_kyxm")[i].id;
                if(!kyxmid){
                    continue
                }
                kyxmNum ++;
                var kyxmfcsid = $("#ajaxForm_resfirstEdit #"+kyxmid).attr("fcsid");
                if(kyxmid!=null && kyxmfcsid!=""){
                    if(sjqf==kyxmfcsid){
                        $("#ajaxForm_resfirstEdit #"+kyxmid).attr("style","display:block;");
                        kyxmShowNum ++;
                    }else{
                        $("#ajaxForm_resfirstEdit #"+kyxmid).attr("style","display:none;");
                    }
                }
            }
            if(kyxmShowNum == 0){
                $("#ajaxForm_resfirstEdit #kyDIV").attr("style","display:none;");
                $("#ajaxForm_resfirstEdit #kyxm").removeAttr("validate");
            }else {
                $("#ajaxForm_resfirstEdit #kyDIV").attr("style","display:block;");
                $("#ajaxForm_resfirstEdit #kyxm").attr("validate","{required:true}");
            }
        }
        $('#ajaxForm_resfirstEdit #kyxm').trigger("chosen:updated"); //更新下拉框
    }else {
        $("#ajaxForm_resfirstEdit #kyDIV").attr("style","display:none;");
        $("#ajaxForm_resfirstEdit #kyxm").removeAttr("validate");
    }
}
/**
 * 标本状态增加取消操作
 */
function calcel(){
    $("#ztDiv input[type='checkbox']").each(function(index){
        if(this.checked){
            var ztmc=$("#"+this.value).next().text();
            $("#"+this.value).removeAttr("checked");
            $("#ajaxForm_resfirstEdit #bz").val($("#ajaxForm_resfirstEdit #bz").val().replace("["+ztmc+"]",""));
        }
    })
}
function updateBz(obj){
    var chk_value =[];//定义一个数组
    $('input[name="zts"]:checked').each(function(){//遍历每一个名字为zts的复选框，其中选中的执行函数
        chk_value.push($(this).val());//将选中的值添加到数组chk_value中
    });
    var ybzts="";
    for(var i=0;i<chk_value.length;i++){
        var ybzt=$("#"+chk_value[i]).next().text();
        ybzts = ybzts+"["+ybzt+"]";
    }
    var bz=$("#ajaxForm_resfirstEdit #bz").val();
    var head = bz.indexOf('[');
    var end = bz.lastIndexOf(']');
    if(head!="-1"&&end!="-1"){
        var text=bz.replace(bz.substring(head,end+1),"");
        $("#ajaxForm_resfirstEdit #bz").val(text+ybzts)
    }else{
        $("#ajaxForm_resfirstEdit #bz").val(bz+ybzts);
    }
}
function displayUpInfo(fjid){
    if(!$("#ajaxForm_resfirstEdit #fjids").val()){
        $("#ajaxForm_resfirstEdit #fjids").val(fjid);
    }else{
        $("#ajaxForm_resfirstEdit #fjids").val($("#ajaxForm_resfirstEdit #fjids").val()+","+fjid);
    }
}
function yl(fjid,wjmhz){
    if(wjmhz.toLowerCase()=="jpg" || wjmhz.toLowerCase()=="jpeg" || wjmhz.toLowerCase()=="jfif" || wjmhz.toLowerCase()=="png"){
        var url="/ws/sjxxpripreview/?fjid="+fjid
        $.showDialog(url,'图片预览',JPGMaterConfig);
    }else if(wjmhz.toLowerCase()=="pdf"){
        var url= "/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',JPGMaterConfig);
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}
var JPGMaterConfig = {
    width		: "800px",
    height		: "500px",
    offAtOnce	: true,
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
function jc(){
    //判断是否显示其他科室
    var kzcs=$("#ks option:selected");
    if (kzcs.attr("kzcs")=="1") {
        $("#ajaxForm_resfirstEdit #qtkscheck").show();
        $("#ajaxForm_resfirstEdit #qtks").removeAttr("disabled");
    }else{
        $("#ajaxForm_resfirstEdit #qtkscheck").hide();
        $("#ajaxForm_resfirstEdit #qtks").attr("disabled","disabled");
        $("#ajaxForm_resfirstEdit #qtks").val("");
    }
    //判断是否显示其他单位
    var yyxxcskz1=$("#ajaxForm_resfirstEdit #yyxxCskz1").val();
    if(yyxxcskz1=='1'){
        $("#ajaxForm_resfirstEdit #otherHospital").show();
        $("#ajaxForm_resfirstEdit #sjdwmc").removeAttr("disabled");
    }else{
        $("#ajaxForm_resfirstEdit #otherHospital").hide();
        $("#ajaxForm_resfirstEdit #sjdwmc").attr("disabled","disabled");
        $("#ajaxForm_resfirstEdit #sjdwmc").val("");
    }
    //判断是否显示其他标本类型
    var kzcs2=$("#yblx option:selected");
    if(kzcs2.attr("kzcs")=="1"){
        $("#ajaxForm_resfirstEdit #yblxmcspan").show();
        $("#ajaxForm_resfirstEdit #yblxmc").removeAttr("disabled");
    }else{
        $("#ajaxForm_resfirstEdit #yblxmcspan").hide();
        $("#ajaxForm_resfirstEdit #yblxmc").attr("disabled","disabled");
        $("#ajaxForm_resfirstEdit #yblxmc").val("");
    }
    //判断快递类型显示快递单号还是取样员
    var cskz2=$("#ajaxForm_resfirstEdit #kdlx").find("option:selected").attr("cskz2");
    var kdlxdm = $("#ajaxForm_resfirstEdit #kdlx").find("option:selected").attr("csdm");
    $("#ajaxForm_resfirstEdit #kdh").removeAttr("placeholder");
    if(cskz2){
        $("#ajaxForm_resfirstEdit #kdh").attr("placeholder"," 如无请填无");
        $("#ajaxForm_resfirstEdit #kdhname").text("快递单号：")
        $("#ajaxForm_resfirstEdit #kdhspan").hide();
    }else{
        if (kdlxdm == "QYY"){
            $("#ajaxForm_resfirstEdit #kdh").attr("placeholder"," 请填写取样员姓名");
            $("#ajaxForm_resfirstEdit #kdhname").text("取样员：")
            $("#ajaxForm_resfirstEdit #kdhspan").show();
        }else {
            $("#ajaxForm_resfirstEdit #kdhname").text("快递单号：")
            $("#ajaxForm_resfirstEdit #kdhspan").show();
        }
    }
}
//科室改变调整其他科室展示科室
$("#ajaxForm_resfirstEdit #ks").change(function(){
    var kzcs=$("#ajaxForm_resfirstEdit #ks option:selected");
    if (kzcs.attr("kzcs")=="1") {
        $("#ajaxForm_resfirstEdit #qtkscheck").show();
        $("#ajaxForm_resfirstEdit #qtks").removeAttr("disabled");
        $("#ajaxForm_resfirstEdit #qtks").val(kzcs.text());
    }else{
        $("#ajaxForm_resfirstEdit #qtkscheck").hide();
        $("#ajaxForm_resfirstEdit #qtks").attr("disabled","disabled");
        $("#ajaxForm_resfirstEdit #qtks").val("");
    }
})
//标本类型调整其他类型
$("#ajaxForm_resfirstEdit #yblx").change(function(){
    var kzcs=$("#ajaxForm_resfirstEdit #yblx option:selected");
    if(kzcs.attr("kzcs")=="1"){
        $("#ajaxForm_resfirstEdit #yblxmcspan").show();
        $("#ajaxForm_resfirstEdit #yblxmc").removeAttr("disabled");
    }else{
        $("#ajaxForm_resfirstEdit #yblxmcspan").hide();
        $("#ajaxForm_resfirstEdit #yblxmc").attr("disabled","disabled");
        $("#ajaxForm_resfirstEdit #yblxmc").val("");
    }
    //限制检测项目
    // jcxminit();
})
//快递类型调整快递号收样员信息
$("#ajaxForm_resfirstEdit #kdlx").change(function(){
    var cskz2=$("#ajaxForm_resfirstEdit #kdlx").find("option:selected").attr("cskz2");
    var kdlxdm = $("#ajaxForm_resfirstEdit #kdlx").find("option:selected").attr("csdm");
    $("#ajaxForm_resfirstEdit #kdh").removeAttr("placeholder");
    if(cskz2){
        $("#ajaxForm_resfirstEdit #kdh").val("");
        $("#ajaxForm_resfirstEdit #kdh").attr("placeholder"," 如无请填无");
        $("#ajaxForm_resfirstEdit #kdhname").text("快递单号：")
        $("#ajaxForm_resfirstEdit #kdhspan").hide();
    }else{
        $("#ajaxForm_resfirstEdit #kdhspan").show();
        if (kdlxdm == "QYY"){
            $("#ajaxForm_resfirstEdit #kdh").val("");
            $("#ajaxForm_resfirstEdit #kdh").attr("placeholder"," 请填写取样员姓名");
            $("#ajaxForm_resfirstEdit #kdhname").text("取样员：")
        }else {
            $("#ajaxForm_resfirstEdit #kdhname").text("快递单号：")
        }
    }
})
$(document).ready(function(){
    //所有下拉框添加choose样式
    jQuery('#ajaxForm_resfirstEdit .chosen-select').chosen({width: '100%'});
    //文件附件初始化
    var oFileInput = new FileInput();
    oFileInput.Init("ajaxForm_resfirstEdit","displayUpInfo",2,1,"sjxx_file",$("#ajaxForm_resfirstEdit #ywlx").val());
    //检测项目限制样本类型
    limitYblx();
    //限制科研项目
    limitKyxm();
    // jcxminit();
    // jczxminit();
    jc();//判断是否显示其他单位、其他科室、其他标本类型
    //监听送检区分改变事件
    $('input:radio[name="sjqf"]').change( function(){
        $("#ajaxForm_resfirstEdit #kyxm").children().removeAttr("selected");
        $('#ajaxForm_resfirstEdit #kyxm').trigger("chosen:updated");
        limitKyxm();
    })
    laydate.render({
        elem: '#ajaxForm_resfirstEdit #cyrq'
    });
    laydate.render({
        elem: '#ajaxForm_resfirstEdit #jsrq'
        ,type: 'datetime'
        ,ready: function(date){
            if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
                var myDate = new Date(); //实例一个时间对象；
                this.dateTime.hours=myDate.getHours();
                this.dateTime.minutes=myDate.getMinutes();
                this.dateTime.seconds=myDate.getSeconds();
            }
        }
    });
    laydate.render({
        elem: '#ajaxForm_resfirstEdit #bgrq'
        ,type: 'datetime'
        ,ready: function(date){
            if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
                var myDate = new Date(); //实例一个时间对象；
                this.dateTime.hours=myDate.getHours();
                this.dateTime.minutes=myDate.getMinutes();
                this.dateTime.seconds=myDate.getSeconds();
            }
        }
    });
});