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
var jcxmmc = '';
var jcxmid = '';
var province = [];
var city = [];
var mrprovince = '';
var mrcity = '';
var provinceCsid='';
var cityCsid='';
var url = '';
var jcddzList = [];
var certificateTypeData = [];
var index = 0;
var nowYear;
var nowMonth;
var nowDay
/*获取当前时间*/
function getNowDate() {
    d = new Date();
    nowYear = d.getFullYear();
    nowMonth = d.getMonth() < 9 ? '0' + (d.getMonth() + 1) : (d.getMonth() + 1);
    nowDay = d.getDate() < 10 ? '0' + d.getDate() : d.getDate();
    var nowDateTime = nowYear + '-' + nowMonth + '-' + nowDay;
    return nowDateTime;
}

/*根据身份证号，获取年龄*/
function getAge(idCard) {
    var strBirthday = idCardNoUtil.getIdCardInfo(idCard).birthday;
    var returnAge;
    var strBirthdayArr = strBirthday.split("-");
    var birthYear = strBirthdayArr[0];
    var birthMonth = strBirthdayArr[1];
    var birthDay = strBirthdayArr[2];

    d = new Date();
    var nowYear = d.getFullYear();
    var nowMonth = d.getMonth() + 1;
    var nowDay = d.getDate();

    if (nowYear == birthYear) {
        returnAge = 0;//同年 则为0岁
    } else {
        var ageDiff = nowYear - birthYear; //年之差
        if (ageDiff > 0) {
            if (nowMonth == birthMonth) {
                var dayDiff = nowDay - birthDay;//日之差
                if (dayDiff < 0) {
                    returnAge = ageDiff - 1;
                } else {
                    returnAge = ageDiff;
                }
            } else {
                var monthDiff = nowMonth - birthMonth;//月之差
                if (monthDiff < 0) {
                    returnAge = ageDiff - 1;
                } else {
                    returnAge = ageDiff;
                }
            }
        } else {
            returnAge = -1;//返回-1 表示出生日期输入错误 晚于今天
        }
    }
    return returnAge;//返回周岁年龄
}

/*身份证判断*/
function IDCardNumberComplete() {
    $("#zjh").val($("#zjh").val().toUpperCase().replace(" ",""))
    var zjlx = $("#zjlx").val();
    var zjlxdm = '';
    for (var i = 0; i < certificateTypeData.length; i++) {
        if ($("#zjlx").val() == certificateTypeData[i].label) {
            zjlxdm = certificateTypeData[i].csdm;
            break;
        }
    }
    var idCard = $("#zjh").val().toUpperCase();
    if (idCard != null && idCard != '') {
        if (zjlxdm == '1') {
            /*居民身份证*/
            /*使用 * 验证身份证的正确性*/
            // true 验证通过，身份证号码正确
            // false 身份证号码错误
            if (!idCardNoUtil.checkIdCardNo(idCard)) {
                layer.msg('身份证长度为:'+idCard.length+'号码错误!');
                return false;
            }
            /*根据身份证号，获取性别*/
            // 1 男
            // 2 女
            document.getElementById("xb").value = idCardNoUtil.getIdCardInfo(idCard).gender;
            /*根据身份证号，获取年龄*/
            document.getElementById("nl").value = getAge(idCard);
            return true
        } else if (zjlxdm == '2') {
            /*护照*/
            // if (!idCard || !/^((1[45]\d{7})|(G\d{8})|(P\d{7})|(S\d{7,8}))?$/.test(idCard)) {
            //     layer.msg('护照号码错误!');
            //     return false;
            // }
            return true
        } else if (zjlxdm == '55') {
            /*港澳居住证*/
            // if (!idCard || !/^[HMhm]{1}([0-9]{10}|[0-9]{8})$/.test(idCard)) {
            //     layer.msg('证件号码错误!');
            //     return false;
            // }
            return true
        }else if (zjlxdm == '54'){
            /*台湾居住证*/
            // if (!idCard || !/^830000(?:19|20)\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])\d{3}[\dX]$/.test(idCard)){
            //     layer.msg('证件号码错误!');
            //     return false;
            // }
            return true
        }
        return true
    }
    return false
}

/*证件类型*/
function getZjlx() {
    if ($("#sfqr").val() != '1' && $("#updateFlag").val() != 'updateFlag') {
        weui.picker(
            certificateTypeData,
            {
                onChange: function (result) {
                },
                onConfirm: function (result) {
                    document.getElementById("zjlx").value = result[0].label
                    completeAll()
                },
                title: '证件类型',
                defaultValue: [certificateTypeData[index].value],
            });
    }
};

/*姓名*/
function getXm(){
    var zjlx = $("#zjlx").val();
    var zjlxdm = '';
    for (var i = 0; i < certificateTypeData.length; i++) {
        if ($("#zjlx").val() == certificateTypeData[i].label) {
            zjlxdm = certificateTypeData[i].csdm;
            break;
        }
    }
    var xm = $("#xm").val();
    if (xm != null && xm != '') {
        if (zjlxdm == '1') {
            /*居民身份证*/
            if (!xm || ! /^[\u4e00-\u9fa5]+(·[\u4e00-\u9fa5]+)*$/.test(xm)) {
                layer.msg('姓名有误!');
                return false;
            }
            return true
        }
        return true
    }
    return false
}

/*检测日期*/
function getYyjcrq() {
    weui.datePicker({
        start: getNowDate(),
        defaultValue: [nowYear, nowMonth, nowDay],
        onChange: function (result) {
        },
        onConfirm: function (result) {
            var strM = result[1].value < 10 ? '0' + result[1].value : result[1].value;
            var strD = result[2].value < 10 ? '0' + result[2].value : result[2].value;
            document.getElementById("yyjcrq").value = result[0].value + '-' + strM + '-' + strD;
        },
        title: '检测日期'
    });
};

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
                    /*如果身份信息存在，获取省份城市*/
                    if ($("#sfid").val() != null && $("#sfid").val() != ''){
                        if ($("#sfid").val()==data[i].csid){
                            provinceCsid = data[i].csid
                            mrprovince = data[i].csdm + '-' + data[i].csid
                            getCityList()
                            document.getElementById("sf").value = data[i].csmc
                        }
                    }else {
                        if (data[i].csdm == '330000') {
                            mrprovince = data[i].csdm + '-' + data[i].csid;
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
                            mrcity = data[i].csdm + '-' + data[i].csid
                            document.getElementById("cs").value = data[i].csmc
                        }
                    }else {
                        mrcity = city[0].value
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
                completeAll()
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
                completeAll()
            },
            title: '城市选择',
            id: 'city'
        });
};

/*获取证件类型*/
function getIdcardCategory() {
    $.ajax({
        type: "post",
        url: "/wechat/getIdcardCategory",
        data: {},
        dataType: "json",
        success: function (data) {
            if (data != null && data.length != 0) {
                for (var i = 0; i < data.length; i++) {
                    certificateTypeData.push({label: data[i].csmc, value: data[i].csid, csdm: data[i].csdm, cskz1: data[i].cskz1})
                    /*if($("#cyd").val()!=null && $("#cyd").val()!=''){
                        if ($("#cyd").val() == data[i].csid){
                            document.getElementById("jcddz").innerText = data[i].csmc
                        }
                    }*/
                }
            }else {
                certificateTypeData = [{
                    label: '暂无',
                    value: 0,
                    disabled: true // 不可用
                }];
            }

            /*默认证件类型为居民身份证*/
            if ($("#zjlxid").val()!=null && $("#zjlxid").val() !=''){
                for (var i = 0; i < certificateTypeData.length; i++) {
                    if ($("#zjlxid").val() == certificateTypeData[i].value) {
                        index = i;
                        document.getElementById("zjlx").value = certificateTypeData[i].label;
                        break;
                    }
                }
            }else {
                for (var i = 0; i < certificateTypeData.length; i++) {
                    if ("1" == certificateTypeData[i].csdm) {
                        index = i;
                        document.getElementById("zjlx").value = certificateTypeData[i].label;
                        break;
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

/*修改采样点*/
/*function changeJcddz() {
    weui.picker(
        jcddzList,
        {
            className: 'custom-classname',
            container: 'body',
            defaultValue: [$("#cyd").val()],
            onChange: function (result) {
            },
            onConfirm: function (result) {
                document.getElementById("cyd").value = result[0].value
                document.getElementById("jcddz").innerText = result[0].label
                completeAll()
            },
            title: '采样点地址',
        });
};*/

/*检测项目*/
function getJcxms() {
    var length = document.getElementsByName("jcxms").length;
    var j = 0;
    jcxmmc = '';
    jcxmid = '';
    for (var i = 0; i < length; i++) {
        if ($("#" + document.getElementsByName("jcxms")[i].id).prop("checked")) {
            j++;
            jcxmid = jcxmid + document.getElementsByName("jcxms")[i].id + ',';
            jcxmmc = jcxmmc + document.getElementsByName("jcxms")[i].value + ',';
        }
    }
    if (j == 0) {
        layer.msg('请选择检测项目!');
        return false;
    }
    jcxmid = jcxmid.substr(0, jcxmid.length - 1);
    jcxmmc = jcxmmc.substr(0, jcxmmc.length - 1);
    return true;

}

/*是否同意*/
function getWeuiAgree() {
    $("#appointment").hide()
    $("#agreeBook").show()
}

/*关闭 知情同意书*/
function closeWeuiAgree() {
    $("#agreeBook").hide()
    $("#appointment").show()
}

// /*诺诺支付提交*/
// $('#submitAppointment').on('click', function () {
//     /*姓名*/
//     if (!$('#xm').val()) {
//         layer.msg('请填写姓名!');
//         return;
//     }
//     /*证件类型*/
//     if (!$('#zjlx').val()) {
//         layer.msg('请选择证件类型!');
//         return;
//     }
//     /*证件号码*/
//     if (!$('#zjh').val()) {
//         layer.msg('证请填写件号码!');
//         return;
//     }
//     /*联系电话*/
//     if (!$('#sj').val()) {
//         layer.msg('请填写联系电话!');
//         return;
//     }
//     /*省份*/
//     if (!$('#sf').val()) {
//         layer.msg('请选择省份!');
//         return;
//     }
//     /*城市*/
//     if (!$('#cs').val()) {
//         layer.msg('请选择城市!');
//         return;
//     }
//     /*详细地址*/
//     if (!$('#xxdz').val()) {
//         layer.msg('请输入完整的现居住地!');
//         return;
//     }
//     /*检测日期*/
//     if (!$('#yyjcrq').val()) {
//         layer.msg('请选择检测日期!');
//         return;
//     }
//     getJcxms()
//     /*是否同意*/
//     if (!$('#weuiAgreeCheckbox').prop("checked")) {
//         $('#weuiAgree').addClass('weui-agree_animate');
//         setTimeout(function () {
//             $('#weuiAgree').removeClass('weui-agree_animate');
//         }, 1000);
//         return;
//     }
//     $('#submitAppointment').attr("disabled", true);
//     $('#submitAppointment').addClass("weui-btn_disabled");
//     var zjlxid = certificateTypeData[0].value;
//     for (var i = 0; i < certificateTypeData.length; i++) {
//         if (certificateTypeData[i].label == $("#zjlx").val()) {
//             zjlxid = certificateTypeData[i].value;
//             break;
//         }
//     }
//     if ($("#updateFlag").val() != 'updateFlag'){
//         url = "/wechat/detectionAppointmentSave";
//     }else {
//         url = "/wechat/detectionAppointmentEdit";
//     }
//     let amount = 0.01;
//     $.ajax({
//         type: "post",
//         url: url,
//         data: {
//             "amount": amount,
//             "subject": jcxmmc,
//             "wxid": $("#wxid").val(),
//             "xm": $("#xm").val(),
//             "xb": $("#xb").val(),
//             "nl": $("#nl").val(),
//             "zjlx": zjlxid,
//             "zjh": $("#zjh").val().toUpperCase(),
//             "sj": $("#sj").val(),
//             "sf": provinceCsid,
//             "cs": cityCsid,
//             "xxdz": $("#xxdz").val(),
//             "yyjcrq": $("#yyjcrq").val(),
//             "jcxmmc": jcxmmc,
//             "jcxmid": jcxmid,
//             "hzid": $("#hzid").val(),
//             "fzjcid": $("#fzjcid").val(),
//             "cyd":$("#cyd").val(),
//         },
//         success: function (result) {
//             if ("success" == result.status) {
//                 // weui.alert(result.message, {
//                 //     buttons: [{
//                 //         label: '确定',
//                 //         type: 'primary',
//                 //         onClick: function () {
//                 if($("#updateFlag").val() != 'updateFlag'){
//                     var pay = JSON.parse(result.payUrl);
//                     if ("JH200" == pay.code) {
//                         window.location.href = pay.result.payUtl;
//                     }else{
//                         window.location.href = '/wechat/detectionViewAndMod?wxid=' + $("#wxid").val();
//                     }
//                 }else{
//                     window.location.href = '/wechat/detectionViewAndMod?wxid=' + $("#wxid").val();
//                 }
//
//                 //         }
//                 //     }]
//                 // });
//             } else {
//                 weui.alert(result.message, {
//                     buttons: [{
//                         label: '确定',
//                         type: 'primary',
//                         onClick: function () {
//                         }
//                     }]
//                 });
//                 $('#submitAppointment').removeAttr("disabled", true);
//                 $('#submitAppointment').removeClass("weui-btn_disabled");
//             }
//
//         },
//         fail: function (result) {
//             weui.alert(result.message, {
//                 buttons: [{
//                     label: '确定',
//                     type: 'primary',
//                     onClick: function () {
//                     }
//                 }]
//             });
//             $('#submitAppointment').removeAttr("disabled", true);
//             $('#submitAppointment').removeClass("weui-btn_disabled");
//         }
//     })
// });

/*无支付提交*/
// $('#submitAppointment').on('click', function () {
//     /*姓名*/
//     if (!$('#xm').val()) {
//         layer.msg('请填写姓名!');
//         return;
//     }
//     /*证件类型*/
//     if (!$('#zjlx').val()) {
//         layer.msg('请选择证件类型!');
//         return;
//     }
//     /*证件号码*/
//     if (!$('#zjh').val()) {
//         layer.msg('证请填写件号码!');
//         return;
//     }
//     /*联系电话*/
//     if (!$('#sj').val()) {
//         layer.msg('请填写联系电话!');
//         return;
//     }
//     /*省份*/
//     if (!$('#sf').val()) {
//         layer.msg('请选择省份!');
//         return;
//     }
//     /*城市*/
//     if (!$('#cs').val()) {
//         layer.msg('请选择城市!');
//         return;
//     }
//     /*详细地址*/
//     if (!$('#xxdz').val()) {
//         layer.msg('请输入完整的现居住地!');
//         return;
//     }
//     /*检测日期*/
//     if (!$('#yyjcrq').val()) {
//         layer.msg('请选择检测日期!');
//         return;
//     }
//     getJcxms()
//     /*是否同意*/
//     if (!$('#weuiAgreeCheckbox').prop("checked")) {
//         $('#weuiAgree').addClass('weui-agree_animate');
//         setTimeout(function () {
//             $('#weuiAgree').removeClass('weui-agree_animate');
//         }, 1000);
//         return;
//     }
//     $('#submitAppointment').attr("disabled", true);
//     $('#submitAppointment').addClass("weui-btn_disabled");
//     var zjlxid = certificateTypeData[0].value;
//     for (var i = 0; i < certificateTypeData.length; i++) {
//         if (certificateTypeData[i].label == $("#zjlx").val()) {
//             zjlxid = certificateTypeData[i].value;
//             break;
//         }
//     }
//     if ($("#updateFlag").val() != 'updateFlag'){
//         url = "/wechat/detectionAppointmentSave";
//     }else {
//         url = "/wechat/detectionAppointmentEdit";
//     }
//     $.ajax({
//         type: "post",
//         url: url,
//         data: {
//             "wxid": $("#wxid").val(),
//             "xm": $("#xm").val(),
//             "xb": $("#xb").val(),
//             "nl": $("#nl").val(),
//             "zjlx": zjlxid,
//             "zjh": $("#zjh").val().toUpperCase(),
//             "sj": $("#sj").val(),
//             "sf": provinceCsid,
//             "cs": cityCsid,
//             "xxdz": $("#xxdz").val(),
//             "yyjcrq": $("#yyjcrq").val(),
//             "jcxmmc": jcxmmc,
//             "jcxmid": jcxmid,
//             "hzid": $("#hzid").val(),
//             "fzjcid": $("#fzjcid").val(),
//             "cyd":$("#cyd").val(),
//         },
//         success: function (result) {
//             if ("success" == result.status) {
//                 weui.alert(result.message, {
//                     buttons: [{
//                         label: '确定',
//                         type: 'primary',
//                         onClick: function () {
//                             window.location.href = '/wechat/detectionViewAndMod?wxid=' + $("#wxid").val();
//                         }
//                     }]
//                 });
//             } else {
//                 weui.alert(result.message, {
//                     buttons: [{
//                         label: '确定',
//                         type: 'primary',
//                         onClick: function () {
//                         }
//                     }]
//                 });
//                 $('#submitAppointment').removeAttr("disabled", true);
//                 $('#submitAppointment').removeClass("weui-btn_disabled");
//             }
//
//         },
//         fail: function (result) {
//             weui.alert(result.message, {
//                 buttons: [{
//                     label: '确定',
//                     type: 'primary',
//                     onClick: function () {
//                     }
//                 }]
//             });
//             $('#submitAppointment').removeAttr("disabled", true);
//             $('#submitAppointment').removeClass("weui-btn_disabled");
//         }
//     })
// });

/*银行支付提交*/
let fkfs = 0;
function editconfirm(event){
    /*姓名*/
    if (!$('#xm').val()) {
        layer.msg('请填写姓名!');
        return;
    }
    /*证件类型*/
    if (!$('#zjlx').val()) {
        layer.msg('请选择证件类型!');
        return;
    }
    /*证件号码*/
    if (!$('#zjh').val()) {
        layer.msg('证请填写件号码!');
        return;
    }
    /*联系电话*/
    if (!$('#sj').val()) {
        layer.msg('请填写联系电话!');
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
    /*检测日期*/
    if (!$('#yyjcrq').val()) {
        layer.msg('请选择检测日期!');
        return;
    }
    getJcxms()
    /*是否同意*/
    if (!$('#weuiAgreeCheckbox').prop("checked")) {
        $('#weuiAgree').addClass('weui-agree_animate');
        setTimeout(function () {
            $('#weuiAgree').removeClass('weui-agree_animate');
        }, 1000);
        return;
    }
    $('#submitAppointment').attr("disabled", true);
    $('#submitAppointment').addClass("weui-btn_disabled");
    var zjlxid = certificateTypeData[0].value;
    for (var i = 0; i < certificateTypeData.length; i++) {
        if (certificateTypeData[i].label == $("#zjlx").val()) {
            zjlxid = certificateTypeData[i].value;
            break;
        }
    }

    url = "/ws/detectionAppointmentSave";
    $.ajax({
        type: "post",
        url: url,
        data: {
            "amount": $("#fkje").val(),
            "wxid": $("#wxid").val(),
            "xm": $("#xm").val(),
            "xb": $("#xb").val(),
            "nl": $("#nl").val(),
            "zjlx": zjlxid,
            "zjh": $("#zjh").val().toUpperCase(),
            "sj": $("#sj").val(),
            "sf": provinceCsid,
            "cs": cityCsid,
            "xxdz": $("#xxdz").val(),
            "yyjcrq": $("#yyjcrq").val(),
            "jcxmmc": jcxmmc,
            "jcxmid": jcxmid,
            "hzid": $("#hzid").val(),
            "fzjcid": $("#fzjcid").val(),
            // "cyd":$("#cyd").val(),
        },
        success: function (result) {
            if ("success" == result.status) {
                $("#fzjcid").val(result.fzjcid);
                if ($("#updateFlag").val() != 'updateFlag'){
                    $("#qrcode").qrcode({
                        render: 'div',
                        size: 200,
                        text: $("#fzjcid").val().toUpperCase().toString()
                    });
                    $("#info").html($("#xm").val()+"</br>"+$("#yyjcrq").val()+"</br>"+$("#zjh").val().toUpperCase().substring(0,6)+"********"+$("#zjh").val().toUpperCase().substring(14));
                    // $("#cyddz").html("采样点地址："+$("#jcddz").text());
                    $('#qrCodePage').show();
                    $("#appointment").hide();
                }else{
                    weui.alert(result.message, {
                        buttons: [{
                            label: '确定',
                            type: 'primary',
                            onClick: function () {
                                window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
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
                $('#submitAppointment').removeAttr("disabled", true);
                $('#submitAppointment').removeClass("weui-btn_disabled");
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
            $('#submitAppointment').removeAttr("disabled", true);
            $('#submitAppointment').removeClass("weui-btn_disabled");
        }
    });

}

function goBackToMod(){
    $("#goPayWechat").attr("disabled", true);
    $("#goPayWechat").addClass("weui-btn_disabled");
    $("#goPayAliPay").attr("disabled", true);
    $("#goPayAliPay").addClass("weui-btn_disabled");
    window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
}

function goPay(dqfkfs){
    fkfs = dqfkfs;
    $("#goPayWechat").attr("disabled", true);
    $("#goPayWechat").addClass("weui-btn_disabled");
    $("#goPayAliPay").attr("disabled", true);
    $("#goPayAliPay").addClass("weui-btn_disabled");
    pay(event);
}
// 支付方法
function pay(event){
    let amount = $("#fkje").val();
    // 判断支付方式
    if(fkfs == "1"){
        // 支付宝支付 判断是否为微信环境
        var ua = navigator.userAgent.toLowerCase();
        if (ua.indexOf('micromessenger') != -1) {
            // 打开引导页
            window.location.href="/wechat/pay/alipayGuide?ywid="+  $("#fzjcid").val() +"&ybbh=XGBGJC&fkje="+amount+"&wxid="+  $("#wxid").val() +"&ywlx="+ $("#ywlx").val();
            $("#fastPay_ajaxForm .preBtn").attr("disabled", false);
        }else{
            // 创建支付宝native订单
            $.ajax({
                url: '/wechat/pay/alipayNative',
                type: 'post',
                dataType: 'json',
                data : {"ywid" : $("#fzjcid").val(), "ybbh" : "XGBGJC","fkje": amount,"wxid": $("#wxid").val(),"ywlx":  $("#ywlx").val()},
                success: function(data) {
                    if(data.status == 'success'){
                        // 唤起支付宝路径 data.qrCode
                        window.location.href = data.qrCode;
                    }else{
                        weui.alert(data.message, {
                            buttons: [{
                                label: '确定',
                                type: 'primary',
                                onClick: function () {
                                    window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                }
                            }]
                        });
                    }
                }
            });
        }
    }
    else if(fkfs == "2"){
        let jclx="TYPE_COVID"
        $.ajax({
            type: "post",
            url: "/wechat/nuonuoWxPay?jclx="+jclx,
            data: {
                "amount": amount,
                "subject": "检测服务费",
                "wxid": $("#wxid").val(),
                "fzjcid": $("#fzjcid").val(),
            },
            success: function (result) {
                if ("success" == result.status) {
                    if($("#updateFlag").val() != 'updateFlag'){
                        var pay = JSON.parse(result.payUrl);
                        if ("JH200" == pay.code) {
                            window.location.href = pay.result.payUtl;
                        }else{
                            setTimeout(function(){
                                weui.alert("系统发生错误！", {
                                    buttons: [{
                                        label: '确定',
                                        type: 'primary',
                                        onClick: function () {
                                            window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                        }
                                    }]
                                });
                            },500)
                        }
                    }else{
                        setTimeout(function(){
                            weui.alert("系统发生错误！", {
                                buttons: [{
                                    label: '确定',
                                    type: 'primary',
                                    onClick: function () {
                                        window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                                    }
                                }]
                            });
                        },500)
                    }

                    //         }
                    //     }]
                    // });
                } else {
                    weui.alert(result.message, {
                        buttons: [{
                            label: '确定',
                            type: 'primary',
                            onClick: function () {
                            }
                        }]
                    });
                    $('#submitAppointment').removeAttr("disabled", true);
                    $('#submitAppointment').removeClass("weui-btn_disabled");
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
                $('#submitAppointment').removeAttr("disabled", true);
                $('#submitAppointment').removeClass("weui-btn_disabled");
            }
        })
    }else{
        weui.alert("未取得支付方式", {
            buttons: [{
                label: '确定',
                type: 'primary',
                onClick: function () {
                    window.location.replace('/wechat/detectionViewAndMod?wxid=' + $("#wxid").val());
                }
            }]
        });
    }

}

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

/*验证所有*/
function completeAll() {
    if (getXm() &&
        $("#zjlx").val() != null && $("#zjlx").val() != '' &&
        IDCardNumberComplete() &&
        PhoneVerify() &&
        $("#sf").val() != null && $("#sf").val() != '' &&
        $("#cs").val() != null && $("#cs").val() != '' &&
        $("#xxdz").val() != null && $("#xxdz").val() != '' &&
        // $("#cyd").val() != null && $("#cyd").val() != '' &&
        $("#yyjcrq").val() != null && $("#yyjcrq").val() != '') {
        var length = document.getElementsByName("jcxms").length;
        var j = 0;
        for (var i = 0; i < length; i++) {
            if ($("#" + document.getElementsByName("jcxms")[i].id).prop("checked")) {
                j++;
            }
        }
        if (j != 0) {
            $('#submitAppointment').removeAttr("disabled", true);
            $('#submitAppointment').removeClass("weui-btn_disabled");
        } else {
            $('#submitAppointment').attr("disabled", true);
            $('#submitAppointment').addClass("weui-btn_disabled");
        }
    } else {
        $('#submitAppointment').attr("disabled", true);
        $('#submitAppointment').addClass("weui-btn_disabled");
    }
}

$(function () {
    $("#appointment").show();
    $("#qrCodePage").hide();

    getProvinceList();
    // getJcddzList();
    getIdcardCategory();
    $('#submitAppointment').attr("disabled", true);
    $('#submitAppointment').addClass("weui-btn_disabled");
    /*默认检测日期为当天*/
    completeAll()
})


