var idCardNoUtil = {
    /*省,直辖市代码表*/
    provinceAndCitys: {
        11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古", 21: "辽宁", 22: "吉林", 23: "黑龙江",
        31: "上海", 32: "江苏", 33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南", 42: "湖北", 43: "湖南", 44: "广东",
        45: "广西", 46: "海南", 50: "重庆", 51: "四川", 52: "贵州", 53: "云南", 54: "西藏", 61: "陕西", 62: "甘肃", 63: "青海", 64: "宁夏",
        65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外"
    },

    /*每位加权因子*/
    powers: ["7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"],

    /*第18位校检码*/
    parityBit: ["1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"],

    /*性别*/
    genders: {male: "1", female: "2"},

    /*校验地址码*/
    checkAddressCode: function (addressCode) {
        var check = /^[1-9]\d{5}$/.test(addressCode);
        if (!check) return false;
        if (idCardNoUtil.provinceAndCitys[parseInt(addressCode.substring(0, 2))]) {
            return true;
        } else {
            return false;
        }
    },

    /*校验日期码*/
    checkBirthDayCode: function (birDayCode) {
        var check = /^[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))$/.test(birDayCode);
        if (!check) return false;
        var yyyy = parseInt(birDayCode.substring(0, 4), 10);
        var mm = parseInt(birDayCode.substring(4, 6), 10);
        var dd = parseInt(birDayCode.substring(6), 10);
        var xdata = new Date(yyyy, mm - 1, dd);
        if (xdata > new Date()) {
            return false;//生日不能大于当前日期
        } else if ((xdata.getFullYear() == yyyy) && (xdata.getMonth() == mm - 1) && (xdata.getDate() == dd)) {
            return true;
        } else {
            return false;
        }
    },

    /*计算校检码*/
    getParityBit: function (idCardNo) {
        var id17 = idCardNo.substring(0, 17);
        /*加权 */
        var power = 0;
        for (var i = 0; i < 17; i++) {
            power += parseInt(id17.charAt(i), 10) * parseInt(idCardNoUtil.powers[i]);
        }
        /*取模*/
        var mod = power % 11;
        return idCardNoUtil.parityBit[mod];
    },

    /*验证校检码*/
    checkParityBit: function (idCardNo) {
        var parityBit = idCardNo.charAt(17).toUpperCase();
        if (idCardNoUtil.getParityBit(idCardNo) == parityBit) {
            return true;
        } else {
            return false;
        }
    },

    /*校验15位或18位的身份证号码*/
    checkIdCardNo: function (idCardNo) {
        //15位和18位身份证号码的基本校验
        var check = /^\d{15}|(\d{17}(\d|x|X))$/.test(idCardNo);
        if (!check) return false;
        //判断长度为15位或18位
        if (idCardNo.length == 15) {
            return idCardNoUtil.check15IdCardNo(idCardNo);
        } else if (idCardNo.length == 18) {
            return idCardNoUtil.check18IdCardNo(idCardNo);
        } else {
            return false;
        }
    },

    //校验15位的身份证号码
    check15IdCardNo: function (idCardNo) {
        //15位身份证号码的基本校验
        var check = /^[1-9]\d{7}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}$/.test(idCardNo);
        if (!check) return false;
        //校验地址码
        var addressCode = idCardNo.substring(0, 6);
        check = idCardNoUtil.checkAddressCode(addressCode);
        if (!check) return false;
        var birDayCode = '19' + idCardNo.substring(6, 12);
        //校验日期码
        check = idCardNoUtil.checkBirthDayCode(birDayCode);
        if (!check) return false;
        //验证校检码
        return idCardNoUtil.checkParityBit(idCardNo);
    },

    //校验18位的身份证号码
    check18IdCardNo: function (idCardNo) {
        //18位身份证号码的基本格式校验
        var check = /^[1-9]\d{5}[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}(\d|x|X)$/.test(idCardNo);
        if (!check) return false;
        //校验地址码
        var addressCode = idCardNo.substring(0, 6);
        check = idCardNoUtil.checkAddressCode(addressCode);
        if (!check) return false;
        //校验日期码
        var birDayCode = idCardNo.substring(6, 14);
        check = idCardNoUtil.checkBirthDayCode(birDayCode);
        if (!check) return false;
        //验证校检码
        return idCardNoUtil.checkParityBit(idCardNo);
    },

    formateDateCN: function (day) {
        var yyyy = day.substring(0, 4);
        var mm = day.substring(4, 6);
        var dd = day.substring(6);
        return yyyy + '-' + mm + '-' + dd;
    },

    //获取信息
    getIdCardInfo: function (idCardNo) {
        var idCardInfo = {
            gender: "",  //性别
            birthday: "" // 出生日期(yyyy-mm-dd)
        };
        if (idCardNo.length == 15) {
            var aday = '19' + idCardNo.substring(6, 12);
            idCardInfo.birthday = idCardNoUtil.formateDateCN(aday);
            if (parseInt(idCardNo.charAt(14)) % 2 == 0) {
                idCardInfo.gender = idCardNoUtil.genders.female;
            } else {
                idCardInfo.gender = idCardNoUtil.genders.male;
            }
        } else if (idCardNo.length == 18) {
            var aday = idCardNo.substring(6, 14);
            idCardInfo.birthday = idCardNoUtil.formateDateCN(aday);
            if (parseInt(idCardNo.charAt(16)) % 2 == 0) {
                idCardInfo.gender = idCardNoUtil.genders.female;
            } else {
                idCardInfo.gender = idCardNoUtil.genders.male;
            }

        }
        return idCardInfo;
    },

    /*18位转15位*/
    getId15: function (idCardNo) {
        if (idCardNo.length == 15) {
            return idCardNo;
        } else if (idCardNo.length == 18) {
            return idCardNo.substring(0, 6) + idCardNo.substring(8, 17);
        } else {
            return null;
        }
    },

    /*15位转18位*/
    getId18: function (idCardNo) {
        if (idCardNo.length == 15) {
            var id17 = idCardNo.substring(0, 6) + '19' + idCardNo.substring(6);
            var parityBit = idCardNoUtil.getParityBit(id17);
            return id17 + parityBit;
        } else if (idCardNo.length == 18) {
            return idCardNo;
        } else {
            return null;
        }
    }
};
var province = [];
var city = [];
var mrprovince = '';
var mrcity = '';
var provinceCsid='';
var cityCsid='';

/*获取省份*/
function getProvinceList() {
    $.ajax({
        type: "post",
        url: "/wechat/getProvince",
        data: {},
        dataType: "json",
        success: function (data) {
            if (data != null && data.length != 0) {
                for (var i = 0; i < data.length; i++) {
                    province.push({label: data[i].csmc, value: data[i].csdm + '-' + data[i].csid})
                    if (data[i].csdm == '330000') {
                        mrprovince = data[i].csdm + '-' + data[i].csid;
                    }
                    /*如果身份信息存在，获取省份城市*/
                    if ($("#sfid").val() != null && $("#sfid").val() != ''){
                        if ($("#sfid").val()==data[i].csid){
                            provinceCsid = data[i].csid
                            getCityList()
                            document.getElementById("sf").value = data[i].csmc
                        }
                    }
                }

            }
        },
        fail: function (result) {
            weui.alert("获取失败！", {
                buttons: [{
                    label: '确定',
                    type: 'primary',
                    onClick: function () {
                    }
                }]
            });
        }
    })
}

/*获取省份对应的城市*/
function getCityList() {
    $.ajax({
        type: "post",
        url: "/wechat/getCtiysByProvince",
        data: {fcsid:provinceCsid},
        dataType: "json",
        success: function (data) {
            city=[];
            if (data != null && data.length != 0) {
                for (var i = 0; i < data.length; i++) {
                    city.push({label: data[i].csmc, value: data[i].csdm + '-' + data[i].csid})
                    /*如果身份信息存在，获取省份城市*/
                    if ($("#csid").val() != null && $("#csid").val() != ''){
                        if ($("#csid").val()==data[i].csid){
                            cityCsid = data[i].csid
                            document.getElementById("cs").value = data[i].csmc
                        }
                    }
                }
                mrcity = city[0].value
            }
        },
        fail: function (result) {
            weui.alert("获取失败！", {
                buttons: [{
                    label: '确定',
                    type: 'primary',
                    onClick: function () {
                    }
                }]
            });
        }
    })
}

/*省份改变事件*/
function changeProvince() {
    weui.picker(
        province,
        {
            className: 'custom-classname',
            container: 'body',
            defaultValue: [mrprovince],
            onChange: function (result) {
            },
            onConfirm: function (result) {
                provinceCsid = result[0].value.split('-')[1];
                getCityList()
                document.getElementById("sf").value = result[0].label
            },
            title: '省份选择',
            id: 'province'
        });
};

/*城市改变事件*/
function changeCity() {
    weui.picker(
        city,
        {
            className: 'custom-classname',
            container: 'body',
            defaultValue: [mrcity],
            onChange: function (result) {
            },
            onConfirm: function (result) {
                cityCsid = result[0].value.split('-')[1];
                document.getElementById("cs").value = result[0].label
            },
            title: '城市选择',
            id: 'city'
        });
};

/*选择发票类型*/
function choseFplx() {
    /*发票类型*/
    if ($('#fplx').val() == "请选择") {
        layer.msg('请选择发票类型!');
        return;
    }

    /*公司专票*/
    if ($('#fplx').val() == "公司/专票") {
        $('#sh').removeAttr("style","display: none;");
        $('#khh').removeAttr("style","display: none;");
        $('#gddh').removeAttr("style","display: none;");
        $('#khhzh').removeAttr("style","display: none;");
        $('#khhspan').text("*");
        $('#gddhspan').text("*");
        $('#khhzhspan').text("*");
        return;
    }
    /*公司普票*/
    if ($('#fplx').val() == "公司/普票") {
        $('#sh').removeAttr("style","display: none;");
        $('#khh').removeAttr("style","display: none;");
        $('#gddh').removeAttr("style","display: none;");
        $('#khhzh').removeAttr("style","display: none;");
        $('#khhspan').text("");
        $('#gddhspan').text("");
        $('#khhzhspan').text("");
        return;
    }
    /*个人*/
    if ($('#fplx').val() == "个人") {
        $('#sh').attr("style","display: none;");
        $('#khh').attr("style","display: none;");
        $('#gddh').attr("style","display: none;");
        $('#khhzh').attr("style","display: none;");
        return;
    }
};

/*提交*/
$('#submitInvoice').on('click', function () {
    /*发票类型*/
    if ($('#fplx').val() == "请选择") {
        layer.msg('请选择发票类型!');
        return;
    }
    /*发票抬头*/
    if (!$('#buyerName').val()) {
        layer.msg('请填写发票抬头!');
        return;
    }
    let fplx = "p";
    if ($('#fplx').val() == "公司/专票") {
        fplx ="s";
        /*联系电话*/
        if (!$('#buyerTel').val()) {
            layer.msg('请填写手固定电话!');
            return;
        }
        /*联系电话*/
        if (!$('#buyerAccounts').val()) {
            layer.msg('请填写开户行!');
            return;
        }
        /*联系电话*/
        if (!$('#buyerAccount').val()) {
            layer.msg('请填写开户行账号!');
            return;
        }
    }
    /*税号*/
    if (!$('#buyerTaxNum').val() && ($('#fplx').val() == "公司/普票" || $('#fplx').val() == "公司/专票") ){
        layer.msg('请填写税号!');
        return;
    }
    /*个人*/
    if ($('#fplx').val() == "个人") {
        $('#buyerTaxNum').val("");
    }
    /*联系电话*/
    if (!$('#buyerPhone').val()) {
        layer.msg('请填写手机号码!');
        return;
    }
    /*邮箱!*/
    if (!$('#email').val()) {
        layer.msg('请填写邮箱!');
        return;
    }
    /*省份*/
    if (!$('#sf').val()) {
        layer.msg('请选择省份!');
        return;
    }
    /*城市*/
    if (!$('#cs').val()) {
        layer.msg('请选择城市!');
        return;
    }
    /*详细地址*/
    if (!$('#xxdz').val()) {
        layer.msg('请输入完整的现居住地!');
        return;
    }
    let wxid = $("#invoiceAppointment_form #wxid").val();
    let fzjcid = $("#invoiceAppointment_form #fzjcid").val();
    let sfje = $("#invoiceAppointment_form #sfje").val();
    $.ajax({
        type: "post",
        url: "/wechat/creatInvoice",
        data: {
            "wxid": wxid,
            "fzjcid": fzjcid,
            "fplx": fplx,
            "sfje": sfje,
            "buyerName": $('#buyerName').val(),
            "buyerTaxNum": $('#buyerTaxNum').val(),
            "buyerPhone": $('#buyerPhone').val(),
            "buyerAccount": $('#buyerAccount').val(),
            "buyerAccounts": $('#buyerAccounts').val(),
            "buyerTel": $('#buyerTel').val(),
            "email": $('#email').val(),
            "buyerAddress": $('#sf').val()+$('#cs').val()+$('#xxdz').val(),
        },
        success: function (result) {
            if ("success" == result.status || "fphm" == result.status) {
                let fphm ="";
                let code ="";
                let kpbj = "0";
                let describe = "";
                if ( "fphm" == result.status){
                    code = "E0000";
                    describe = "发生错误请检测信息是否正确！"
                }else{
                    var invoice = JSON.parse(result.invoiceUrl);
                    code = invoice.code;
                    fphm = invoice.result.invoiceSerialNum;
                    describe =  invoice.describe;
                    kpbj = "1";
                    if ("E0000" == code) {
                        $.ajax({
                            type: "post",
                            url: "/wechat/updateInvoiceInfo",
                            data: {
                                "kpbj": kpbj,
                                "fphm": fphm,
                                "fzjcid": fzjcid,
                            },
                            success: function (data) {
                            },
                            fail: function (result) {
                                weui.alert(result.message, {
                                    buttons: [{
                                        label: '确定',
                                        type: 'primary',
                                        onClick: function () {
                                        }
                                    }]
                                });
                            }
                        })
                    }else{
                        weui.alert(describe, {
                            buttons: [{
                                label: '确定',
                                type: 'primary',
                                onClick: function () {
                                }
                            }]
                        });
                    }

                }
                if ("E0000" == code) {
                    weui.alert("申请成功！", {
                        buttons: [{
                            label: '确定',
                            type: 'primary',
                            onClick: function () {
                                history.go(-1)
                                // if($('#invoiceAppointment_form #czbs').val()){
                                //     window.location.href = '/wechat/detectionReport?wxid=' + $("#wxid").val();
                                // }else{
                                //     history.go(-1)
                                //     // window.location.href = '/wechat/detectionViewAndMod?wxid=' + wxid;
                                // }
                            }
                        }]
                    });
                    // $('.toast').css({display: 'flex'});
                    // $('.toast').css({visibility: 'visible'});
                    // setTimeout(function(){
                    //     $('.toast').css({display: 'none'});
                    //     $('.toast').css({visibility: 'hidden'});
                    //     invoiceInfo(fzjcid,fphm,kpbj);
                    // }, 40000);
                    // invoiceInfo(fzjcid,fphm,kpbj);
                }else{
                    weui.alert(describe, {
                        buttons: [{
                            label: '确定',
                            type: 'primary',
                            onClick: function () {
                            }
                        }]
                    });
                }
            } else {
                weui.alert(result.message, {
                    buttons: [{
                        label: '确定',
                        type: 'primary',
                        onClick: function () {
                        }
                    }]
                });
            }

        },
        fail: function (result) {
            weui.alert(result.message, {
                buttons: [{
                    label: '确定',
                    type: 'primary',
                    onClick: function () {
                    }
                }]
            });
        }
    })
});



/*验证手机号码*/
function PhoneVerify() {
    var phoneNum = $("#sj").val();
    if (phoneNum != null && phoneNum != '') {
        var reg = /^(?:(?:0\d{2,3}-\d{7,8})|\d{7,8}|1\d{10})$/;
        if (!reg.test(phoneNum)) {
            layer.msg('请输入正确的手机号码!');
            return false;
        }
        return true
    }
    return false
}
$(function () {
    getProvinceList();
});


