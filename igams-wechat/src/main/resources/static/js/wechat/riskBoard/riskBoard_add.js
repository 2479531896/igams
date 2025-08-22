$(function(){
    jQuery('#riskBoardAjaxForm .chosen-select').chosen({width: '100%'});
    laydate.render({
        elem: '#riskBoardAjaxForm #tzrq'
        ,type: 'date'
    });
    var oFileInput = new FileInput();
    oFileInput.Init("riskBoardAjaxForm","displayUpInfo",2,1,"pro_file");
    var phoeFile=new PhoneFile();
    phoeFile.InitPhoneFile("riskBoardAjaxForm","testqrcode","riskBoardModal","testFj",$("#phoneywid").val(),$("#ywlx").val(),"标本退回附件上传",$("#applicationurl").val())
})

function displayUpInfo(fjid){
    if(!$("#riskBoardAjaxForm #fjids").val()){
        $("#riskBoardAjaxForm #fjids").val(fjid);
    }else{
        $("#riskBoardAjaxForm #fjids").val($("#riskBoardAjaxForm #fjids").val()+","+fjid);
    }
}

//点击隐藏文件上传
function cancelfile(){
    $("#fileDiv").hide();
    $("#file_btn").show();
}
//点击文件上传
function editfile(){
    $("#fileDiv").show();
    $("#file_btn").hide();
}
function view(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif" || type.toLowerCase()==".png"){
        var url="/ws/sjxxpripreview/?fjid="+fjid
        $.showDialog(url,'图片预览',viewPreViewConfig);
    }else if(type.toLowerCase()==".pdf"){
        var url= "/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',viewPreViewConfig);
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}
function xz(fjid){
    jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}
function del(fjid,wjlj){
    $.confirm('您确定要删除所选择的记录吗？',function(result){
        if(result){
            jQuery.ajaxSetup({async:false});
            var url= "/common/file/delFile";
            jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
                setTimeout(function(){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            $("#modSubTaskForm #"+fjid).remove();
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
