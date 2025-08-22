function yl(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    var urlPrefix= $("#ajaxForm #urlPrefix").val();
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
        var url=urlPrefix+"/ws/sjxxpripreview?pageflg=1&fjid="+fjid
        window.location.href=url;
    }else if(type.toLowerCase()==".pdf"){
        var url= urlPrefix+"/common/view/displayView?view_url=/ws/file/pdfPreview?fjid=" + fjid;
        window.location.href=url;
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}

function viewmore(htfkmxid){
    $.ajax({
        type:'post',
        url:$("#ajaxForm #urlPrefix").val()+"/ws/production/viewMoreContractConfirm",
        cache: false,
        data: {"htfkmxid":htfkmxid},
        dataType:'json',
        success:function(data){
            $("#ajaxForm #t_div").animate({left:'0px'},"fast");
            $("#ajaxForm .tr").attr("style","background-color:white;");
            $("#ajaxForm #"+htfkmxid).attr("style","background-color:#B3F7F7;");
            var htnbbh=data.htfkmxDto.htnbbh;
            var zfdxmc=data.htfkmxDto.zfdxmc;
            var fkrq=data.htfkmxDto.fkrq;
            var fkje=data.htfkmxDto.fkje;
            var fkbfb=data.htfkmxDto.fkbfb;
            var zje=data.htfkmxDto.zje;
            var yfje=data.htfkmxDto.yfje;
            var yfbfb=data.htfkmxDto.yfbfb;
            var xzje=data.htfkmxDto.xzje;
            if(htnbbh==null){
                htnbbh="";
            }
            if(zfdxmc==null){
                zfdxmc="";
            }
            if(fkrq==null){
                fkrq="";
            }
            if(fkje==null || fkje==''){
                fkje=0.00;
            }
            if(fkbfb==null || fkbfb==''){
                fkbfb=0.00+'%';
            }
            if(zje==null || zje==''){
                zje=0.00;
            }
            if(yfje==null || yfje==''){
                yfje=0.00;
            }
            if(yfbfb==null || yfbfb==''){
                yfbfb=0.00+'%';
            }
            if(xzje==null || xzje==''){
                xzje='';
            }
            //返回值
            $("#ajaxForm #htnbbh").text(htnbbh);
            $("#ajaxForm #zfdx").text(zfdxmc);
            $("#ajaxForm #fkrq").text(fkrq);
            $("#ajaxForm #fkje").text(fkje);
            $("#ajaxForm #fkbfb").text(fkbfb+'%');
            $("#ajaxForm #zje").text(zje);
            $("#ajaxForm #yfje").text(yfje);
            $("#ajaxForm #yfbfb").text(yfbfb+'%');
            $("#ajaxForm #xzje").text(xzje);
        }
    });
}

function closeNav() {
    $("#ajaxForm #t_div").animate({left:'-200px'},"fast");
    $("#ajaxForm .tr").attr("style","background-color:white;");
}

$(document).click(function(event){
    // var _con = $('#t_div');  // 设置目标区域
    // if(!_con.is(event.target) && _con.has(event.target).length === 0){ // Mark 1
    //     closeNav();
    // }
    document.onclick = function (event) {
        var e = event || window.event;
        var elem = e.srcElement || e.target;
        while(elem)
        {
            if(elem.id == "t_div")
            {
                return;
            }
            elem = elem.parentNode;
        }
        //隐藏div的方法
        closeNav();
    }
});