var codeTimer=null;
var checkfileTimer=null

var PhoneFile =function (){
    var oFile=new Object();
    /**
     *
     * @param ywid 业务id
     * @param ywlx 业务类型
     * @param ywmc  业务名称
     * @param modelName 模态框name
     * @param formid  表单id
     * @param divid   表单种二维码div得id
     * @param fjdivid  附件显示得id
     * @param applicationUrl 路径
     * @constructor
     */
    oFile.InitPhoneFile=function(formid,divid,modelName,fjdivid,ywid,ywlx,ywmc,applicationUrl){
        $("#"+formid+" #"+divid).qrcode({
            render: 'div',
            size: 200,
            text: applicationUrl+"/common/file/pagedataPhoneUpload?access_token="+$("#ac_tk").val()+"&ywid="+ywid+"&ywlx="+ywlx+"&ywmc="+ywmc
        })

        codeTimer= setTimeout(function(){
            commonCodeHidden(formid,divid)
        },1000*60*30)

        checkfileTimer=setTimeout(function (){
            getfile(ywid,ywlx,ywmc,modelName,formid,divid,fjdivid)
        },3000)
        $("#"+modelName).attr("dsq",checkfileTimer+"_"+codeTimer);
    }
    return oFile;
}

function commonCodeHidden(formid,divid){
    $("#"+formid+" #"+divid+" div").eq(0).append("<div id='ewmsx' style='position:relative;font-size: 20px;top:45%;color: red;text-align: center;font-weight:bold'>您的二维码已失效</br>请更换二维码</div>")
    $("#"+formid+" #"+divid+" div").eq(0).find("div").each(function (index) {
        if ($(this).attr("id") != "ewmsx") {
            $(this).css("opacity", "0.3")
        }
    })
}
var oldarr=""

/**
 *
 * @param ywid 业务id
 * @param ywlx 业务类型
 * @param ywmc  业务名称
 * @param modelName 模态框name
 * @param formid  表单id
 * @param divid   表单种二维码div得id
 * @param fjdivid  附件显示得id
 */
function getfile(ywid,ywlx,ywmc,modelName,formid,divid,fjdivid){
    $.ajax({
        url: '/common/file/pagedataPhoneUploadStateCheck?access_token='+$("#ac_tk").val()+"&ywid="+ywid+"&ywlx="+ywlx,
        type: 'post',
        dataType: 'json',
        data: {

        },success(obj){
            if(obj.update=="true"){
                if ( $("#"+formid+" #fjids").val()){
                    if(oldarr!=""&&oldarr!=null){
                        var strArr=oldarr.split(",")
                        var fjidsarr=$("#"+formid+" #fjids").val().split(",");
                        for(var i=0;i<strArr.length;i++){
                            var ppindex=fjidsarr.findIndex(item=>{
                                return item==strArr[i]
                            })
                            if(ppindex!=-1){
                                fjidsarr.splice(ppindex,1)
                            }
                        }
                        if(fjidsarr.length>0){
                            if(obj.fjids!=null){
                                $("#"+formid+" #fjids").val(fjidsarr.toString()+","+obj.fjids)
                            }else{
                                $("#"+formid+" #fjids").val(fjidsarr.toString())
                            }
                        }else{
                            $("#"+formid+" #fjids").val(obj.fjids==null?"":obj.fjids)
                        }
                        oldarr=obj.fjids
                    }else{
                        oldarr=obj.fjids
                        $("#"+formid+" #fjids").val($("#"+formid+" #fjids").val()+","+obj.fjids)
                    }

                }else{
                    oldarr=obj.fjids
                    $("#"+formid+" #fjids").val(obj.fjids);
                }
                $("#"+formid+" #"+fjdivid).empty()
                if (obj.fjcfbDtoList && obj.fjcfbDtoList.length>0){
                    var html = "";
                    for (let i = 0; i < obj.fjcfbDtoList.length; i++) {
                        if(i == 0){
                            html +="<label class=\"col-md-2 col-sm-4 col-xs-12 control-label \">附件：</label>";
                        }else{
                            html +="<label class=\"col-md-2 col-sm-4 col-xs-12 control-label \"></label>";
                        }
                        html += "<div class=\"col-md-10 col-sm-8 col-xs-12\" id=\""+obj.fjcfbDtoList[i].fjid+"\" >";

                        html += " <button style=\"outline:none;margin-bottom:5px;padding:0px;\" onclick=\"xzphoneFileTemporary(\'"+formid+"\',\'"+obj.fjcfbDtoList[i].wjlj+"\',\'"+obj.fjcfbDtoList[i].wjm+"\')\" class=\"btn btn-link\" type=\"button\" title=\"下载\">";
                        html += " <span>"+obj.fjcfbDtoList[i].wjm+"</span>";
                        html += " <span class=\"glyphicon glyphicon glyphicon-save\"></span>";
                        html += " </button>";
                        html += " <button title=\"预览\" class=\"f_button\" type=\"button\" onclick=\"ylphoneFileTemporary(\'"+formid+"\',\'"+obj.fjcfbDtoList[i].fjid+"\',\'"+obj.fjcfbDtoList[i].wjm+"\')\">";
                        html += " <span  class=\"glyphicon glyphicon-eye-open\"></span>";
                        html += " </button>";
                        html += "</div>";
                    }
                    $("#"+formid+" #"+fjdivid).append(html);
                }
            }

            checkfileTimer=setTimeout(function(){
                getfile(ywid,ywlx,ywmc,modelName,formid,divid,fjdivid)
            },3000)
            $("#"+modelName).attr("dsq",checkfileTimer+"_"+codeTimer);
        }
    })
}

function xzphoneFileTemporary(formid,wjlj,wjm){
    jQuery('<form action="'+$("#"+formid+" #urlPrefix").val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="wjlj" value="'+wjlj+'"/>' +
        '<input type="text" name="wjm" value="'+wjm+'"/>' +
        '<input type="text" name="temporary" value="temporary"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}

function delphoneFileTemporary(fromid,fjdivid,fjid){
    $.confirm('您确定要删除所选择的记录吗？',function(result){
        if(result){
            $("#editExceptionForm #redisFj #"+fjid).remove();
            var strings = $("#"+formid+" #fjids").val().split(",");
            if (null!= strings && strings.length>0){
                var string = "";
                var xh=0;
                for (let i = 0; i < strings.length; i++) {
                    if (strings[i] != fjid){
                        string += ","+strings[i];
                        xh+=1;
                    }
                }
                $("#"+formid+" #fjids").val(string.substring(1));
                if(!string){
                    $("#"+formid+" #"+fjdivid).empty();
                }
            }
        }
    });
}

function ylphoneFileTemporary(formid,fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
        var url=$("#"+formid+" #urlPrefix").val()+"/ws/sjxxpripreview/?fjid="+fjid+"&temporary=temporary"
        $.showDialog(url,'图片预览',JPGExceptionMaterConfig);
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}

var JPGExceptionMaterConfig = {
    width		: "800px",
    offAtOnce	: true,
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};