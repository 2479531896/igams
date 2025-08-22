// 根据样本编号查询患者信息
function getHzxxInfoByYbbh(){
    var ybbh = $("#addPaperReportsApplyForm #ybbh").val();
    if( ybbh.length >= 8){
        var url = "/inspection/pathogen/pagedataInspection";
        $.ajax({
            type : "POST",
            url : url,
            async:false,
            data : {"ybbh":ybbh,"access_token":$("#ac_tk").val()},
            dataType : "json",
            success : function(data){
                if(data.sjxxDto) {
                    if(data.sjxxDto.cskz3s !=null){
                        $("#addPaperReportsApplyForm #cskz3s").val(data.sjxxDto.cskz3s);
                    }
                    // 送检ID
                    if(data.sjxxDto.sjid){
                        $("#addPaperReportsApplyForm #sjid").val(data.sjxxDto.sjid);
                    }
                    // 患者姓名
                    if(data.sjxxDto.hzxm){
                        $("#addPaperReportsApplyForm #hzxm").text(data.sjxxDto.hzxm);
                    }
                    // 年龄
                    if(data.sjxxDto.nl){
                        $("#addPaperReportsApplyForm #nl").text(data.sjxxDto.nl);
                    }
                    // 性别
                    if(data.sjxxDto.xbmc){
                        $("#addPaperReportsApplyForm #xbmc").text(data.sjxxDto.xbmc);
                    }
                    // 电话
                    if(data.sjxxDto.dh){
                        $("#addPaperReportsApplyForm #lxdh").text(data.sjxxDto.dh);
                    }
                    // 送检单位
                    if(data.sjxxDto.sjdw){
                        $("#addPaperReportsApplyForm #sjdw").text(data.sjxxDto.hospitalname);
                    }
                    // 合作伙伴
                    if(data.sjxxDto.db){
                        $("#addPaperReportsApplyForm #db").text(data.sjxxDto.db);
                    }
                    // 样本类型
                    if(data.sjxxDto.yblxmc){
                        $("#addPaperReportsApplyForm #yblxmc").text(data.sjxxDto.yblxmc);
                    }
                    // 检测项目
                    if(data.sjxxDto.jcxmmc){
                        $("#addPaperReportsApplyForm #jcxm").text(data.sjxxDto.jcxmmc);
                    }
                    // 备注
                    if(data.sjxxDto.bz){
                        $("#addPaperReportsApplyForm #hzxxbz").text(data.sjxxDto.bz);
                    }
                } else {
                    $.error("此标本不存在");
                }
            }
        });
    }
}

$(function () {
    var ybbh = $("#addPaperReportsApplyForm #ybbh").val();
    if(ybbh!=null&&ybbh!=''){
        getHzxxInfoByYbbh();
    }
});