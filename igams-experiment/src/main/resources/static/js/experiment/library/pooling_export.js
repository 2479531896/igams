var settingStr;
$(function(){
    var oFileInput = new FileInput();
	oFileInput.Init("pooling_formSearch","displayUpInfo",2,1,"pro_file",$("#pooling_formSearch #ywlx").val());
	jQuery('#pooling_formSearch .chosen-select').chosen({width: '100%'});
    generateSetting();
    $("#pooling_formSearch #showmbid").change(function(){
        var csdm = $('#showmbid option:selected').attr("csdm");
        if (csdm){
            if (csdm == '01'){
                $("#pooling_formSearch #isHaveTpool").val("false");
            }else if (csdm == '02'){
                $("#pooling_formSearch #isHaveTpool").val("true");
            }
        }
        if ($('#showmbid option:selected').val()){
            $("#pooling_formSearch #mbid").val($('#showmbid option:selected').val());
        }
        generateSetting()
    });
    /*“设置保存”按钮 点击事件*/
    $("#pooling_formSearch #save_pooling").click(function(){
        //防止重复点击
        if($("#pooling_formSearch #save_pooling").attr("disabled") == "disabled"){
            return;
        }
        //设置按钮禁止点击
        $("#pooling_formSearch #save_pooling").attr("disabled",true);
        var map = new Map(Object.entries(JSON.parse(settingStr)))
        if ( $("#tsum").val() > 0 && $("#qsum").val() >0 ){
            map.set("Item","ALL")
        } else if ( $("#tsum").val() > 0){
            map.set("Item","T")
        } else {
            map.set("Item","Q")
        }
        //下机数据量
        //var v_sjzl = $("#pooling_formSearch #sjzl");
        //map.set("Flux",v_sjzl)
        var v_TheMinimum = $("#pooling_formSearch #TheMinimum").val();
        map.set("TheMinimum",v_TheMinimum)
        //最小上机次数
        var settingInputs = $("#settingForm input");
        if (settingInputs && settingInputs.length > 0){
            for(var i = 0; i < settingInputs.length; i++){
                var inputhtml = settingInputs[i];
                var key = inputhtml.id;
                var value = inputhtml.value;
                if ("Item,TemplateDm,ReagentName,tpool_ReagentName,TheMinimum".indexOf(key) > -1) {
                    map.set(key,value)
                }else if ("Flux,Coefficient".indexOf(key) == -1){
                    if (map[key]){
                        var newVar = new Map(map.get(key));
                        newVar.set("value",value);
                        map.set(key,newVar)
                    }else {
                        var newVar = new Map();
                        newVar.set("value",value);
                        //往map中设置key值
                        map.set(key,newVar);
                    }
                }
            }
        }
        $("#pooling_formSearch input[name='access_token']").val($("#ac_tk").val());
        $.ajax({
            type: "POST",
            url: "/experiment/library/pagedataSavePoolingSetting",
            data: { "access_token": $("#ac_tk").val(),"yqlx": $("#yqlx").val(), "json": mapToMultiLevelJson(map) },
            dataType: "json",
            success: function(data){
                $("#pooling_formSearch #save_pooling").removeAttr("disabled");
                if (data.status=='success'){
                    $.success(data.message);
                }else{
                    $.error(data.message);
                }
            }
        });


    });
})
function mapToJsonRecursive(inputMap) {
    const obj = {};

    inputMap.forEach((value, key) => {
        if (value instanceof Map) {
            // 如果值是Map，则递归调用
            obj[key] = mapToJsonRecursive(value);
        } else {
            // 否则直接赋值
            obj[key] = value;
        }
    });

    return obj;
}

// 将Map转换为JSON字符串
function mapToMultiLevelJson(map) {
    const jsonObj = mapToJsonRecursive(map);
    return JSON.stringify(jsonObj); // 第三个参数是缩进量，使输出更易读
}
function generateSetting(){
    settingStr = $("#pooling_formSearch #settingStr").val();
    var coefficient = $("#pooling_formSearch #coefficient").val();
    var detectionExtendStr = $("#pooling_formSearch #detectionExtend").val();
    var isHaveTpool = $("#pooling_formSearch #isHaveTpool").val();
    var html = '';
    var setting = JSON.parse(settingStr);
    var detectionExtend = JSON.parse(detectionExtendStr);
    if(isHaveTpool == 'true'){
        html += '<div class="col-md-6 col-sm-6 col-xs-6 text-center">';
        html += '<h5 style="background-color:#F3F5F8;height:50px;line-height: 50px;text-align: center;color:#1767AD;margin-bottom: 15px;">sheet:tpool</h5>';
        html += '<div class="col-md-12 col-sm-12 col-xs-12 form-group">'+
            '<label class="col-md-6 col-sm-6 col-xs-6 control-label">试剂选择：</label>'+
            '<div class="col-sm-6 col-md-6 col-xs-6">'+
            '<input type="text" id="tpool_ReagentName" name="tpool_ReagentName" value="' + (setting?(setting.tpool_ReagentName?setting.tpool_ReagentName:(setting.ReagentName?setting.ReagentName:'')):$("#pooling_formSearch #sjxz").val()) + '" class="form-control" style="height:30px;"/>'+
            '</div>'+
            '</div>';
        html += '<div class="col-md-12 col-sm-12 col-xs-12 form-group">'+
            '<label class="col-md-6 col-sm-6 col-xs-6 control-label" style="color:red;">预pooling终体积(uL)：</label>'+
            '<div class="col-sm-6 col-md-6 col-xs-6">'+
            '<input type="text" id="tpool_PoolVolume" name="tpool_PoolVolume" value="' + (setting?(setting.tpool_PoolVolume?setting.tpool_PoolVolume.value:(setting.PoolVolume && setting.PoolVolume.value?setting.PoolVolume.value:detectionExtend.ypztj)):detectionExtend.ypztj) + '" class="form-control" style="height:30px;"/>'+
            '</div>'+
            '</div>';
        html += '<div class="col-md-12 col-sm-12 col-xs-12 form-group">'+
            '<label class="col-md-6 col-sm-6 col-xs-6 control-label" style="color:red;">上机混合文库终浓度(pM)：</label>'+
            '<div class="col-sm-6 col-md-6 col-xs-6">'+
            '<input type="text" id="tpool_Concentration" name="tpool_Concentration" value="' + (setting?(setting.tpool_Concentration?setting.tpool_Concentration.value:(setting.Concentration && setting.Concentration.value?setting.Concentration.value:detectionExtend.sjhhwkznd)):detectionExtend.sjhhwkznd) + '" class="form-control" style="height:30px;"/>'+
            '</div>'+
            '</div>';
        html += '<div class="col-md-12 col-sm-12 col-xs-12 form-group">'+
            '<label class="col-md-6 col-sm-6 col-xs-6 control-label">文库参考体积：</label>'+
            '<div class="col-sm-6 col-md-6 col-xs-6">'+
            '<input type="text" id="tpool_LibraryReferenceVolume" name="tpool_LibraryReferenceVolume" value="' + (setting?(setting.tpool_LibraryReferenceVolume?setting.tpool_LibraryReferenceVolume.value:(setting.LibraryReferenceVolume && setting.LibraryReferenceVolume.value?setting.LibraryReferenceVolume.value:detectionExtend.wkcktj)):detectionExtend.wkcktj) + '" class="form-control" style="height:30px;"/>'+
            '</div>'+
            '</div>';
        html += '<div class="col-md-12 col-sm-12 col-xs-12 form-group">'+
            '<label class="col-md-6 col-sm-6 col-xs-6 control-label">稀释文库参考体积：</label>'+
            '<div class="col-sm-6 col-md-6 col-xs-6">'+
            '<input type="text" id="tpool_DilutedLibraryReferenceVolume" name="tpool_DilutedLibraryReferenceVolume" value="' + (setting?(setting.tpool_DilutedLibraryReferenceVolume?setting.tpool_DilutedLibraryReferenceVolume.value:(setting.DilutedLibraryReferenceVolume && setting.DilutedLibraryReferenceVolume.value?setting.DilutedLibraryReferenceVolume.value:detectionExtend.xswkcktj)):detectionExtend.xswkcktj) + '" class="form-control" style="height:30px;"/>'+
            '</div>'+
            '</div>';
        html += '<div class="col-md-12 col-sm-12 col-xs-12 form-group">'+
            '<label class="col-md-6 col-sm-6 col-xs-6 control-label">稀释倍数：</label>'+
            '<div class="col-sm-6 col-md-6 col-xs-6">'+
            '<input type="text" id="tpool_DilutionFactor" name="tpool_DilutionFactor" value="' + (setting?(setting.tpool_DilutionFactor?setting.tpool_DilutionFactor.value:(setting.DilutionFactor && setting.DilutionFactor.value?setting.DilutionFactor.value:detectionExtend.xsbs)):detectionExtend.xsbs) + '" class="form-control" style="height:30px;"/>'+
            '</div>'+
            '</div>';
        html += '<div class="col-md-12 col-sm-12 col-xs-12 form-group">'+
            '<label class="col-md-6 col-sm-6 col-xs-6 control-label">数据量/pooling 系数（M reads）：</label>'+
            '<div class="col-sm-6 col-md-6 col-xs-6">'+
            '<input type="text" id="tpool_MReadsMultiple" name="tpool_MReadsMultiple" value="' + (setting?(setting.tpool_MReadsMultiple?setting.tpool_MReadsMultiple.value:(setting.MReadsMultiple && setting.MReadsMultiple.value?setting.MReadsMultiple.value:detectionExtend.cnsd)):detectionExtend.cnsd)  + '" class="form-control" style="height:30px;"/>'+
            '</div>'+
            '</div>';
        html += '<div class="col-md-12 col-sm-12 col-xs-12 form-group">'+
            '<label class="col-md-6 col-sm-6 col-xs-6 control-label">混合文库稀释体积(ul)：</label>'+
            '<div class="col-sm-6 col-md-6 col-xs-6">'+
            '<input type="text" id="tpool_MixDilutedLibraryReferenceVolume" name="tpool_MixDilutedLibraryReferenceVolume" value="' + (setting?(setting.tpool_MixDilutedLibraryReferenceVolume?setting.tpool_MixDilutedLibraryReferenceVolume.value:(setting.MixDilutedLibraryReferenceVolume && setting.MixDilutedLibraryReferenceVolume.value?setting.MixDilutedLibraryReferenceVolume.value:detectionExtend.hhwkxstj)):detectionExtend.hhwkxstj) + '" class="form-control" style="height:30px;"/>'+
            '</div>'+
            '</div>';
        html += '<div class="col-md-12 col-sm-12 col-xs-12 form-group">'+
            '<label class="col-md-6 col-sm-6 col-xs-6 control-label">混合文库稀释浓度设定(pM)：</label>'+
            '<div class="col-sm-6 col-md-6 col-xs-6">'+
            '<input type="text" id="tpool_MixDilutedLibraryReferenceConcentration" name="tpool_MixDilutedLibraryReferenceConcentration" value="' + (setting?(setting.tpool_MixDilutedLibraryReferenceConcentration?setting.tpool_MixDilutedLibraryReferenceConcentration.value:(setting.MixDilutedLibraryReferenceConcentration && setting.MixDilutedLibraryReferenceConcentration.value?setting.MixDilutedLibraryReferenceConcentration.value:detectionExtend.hhwkxsnd)):detectionExtend.hhwkxsnd) + '" class="form-control" style="height:30px;"/>'+
            '</div>'+
            '</div>';
        html += '</div>';
        html += '<div class="col-md-6 col-sm-6 col-xs-6 text-center">';
    }else{
        html += '<div class="col-md-12 col-sm-12 col-xs-12 text-center">';
    }
    html += '<h5 style="background-color:#F3F5F8;height:50px;line-height: 50px;text-align: center;color:#1767AD;margin-bottom: 15px;">sheet:全</h5>';
    html += '<div class="col-md-12 col-sm-12 col-xs-12 form-group">'+
        '<label class="col-md-6 col-sm-6 col-xs-6 control-label">试剂选择：</label>'+
        '<div class="col-sm-6 col-md-6 col-xs-6">'+
        '<input type="text" id="ReagentName" name="ReagentName" value="' + ($("#pooling_formSearch #ReagentName").val()?$("#pooling_formSearch #ReagentName").val():(setting?(setting.ReagentName?setting.ReagentName:''):$("#pooling_formSearch #sjxz").val())) + '" class="form-control" style="height:30px;"/>'+
        '</div>'+
        '</div>';
    html += '<div class="col-md-12 col-sm-12 col-xs-12 form-group">'+
        '<label class="col-md-6 col-sm-6 col-xs-6 control-label" style="color:red;">预pooling终体积(uL)：</label>'+
        '<div class="col-sm-6 col-md-6 col-xs-6">'+
        '<input type="text" id="PoolVolume" name="PoolVolume" value="' + ($("#pooling_formSearch #PoolVolume").val()?$("#pooling_formSearch #PoolVolume").val():(setting?(setting.PoolVolume && setting.PoolVolume.value?setting.PoolVolume.value:detectionExtend.ypztj):detectionExtend.ypztj)) + '" class="form-control" style="height:30px;"/>'+
        '</div>'+
        '</div>';
    html += '<div class="col-md-12 col-sm-12 col-xs-12 form-group">'+
        '<label class="col-md-6 col-sm-6 col-xs-6 control-label" style="color:red;">上机混合文库终浓度(pM)：</label>'+
        '<div class="col-sm-6 col-md-6 col-xs-6">'+
        '<input type="text" id="Concentration" name="Concentration" value="' + ($("#pooling_formSearch #Concentration").val()?$("#pooling_formSearch #Concentration").val():(setting?(setting.Concentration && setting.Concentration.value?setting.Concentration.value:detectionExtend.sjhhwkznd):detectionExtend.sjhhwkznd)) + '" class="form-control" style="height:30px;"/>'+
        '</div>'+
        '</div>';
    html += '<div class="col-md-12 col-sm-12 col-xs-12 form-group">'+
        '<label class="col-md-6 col-sm-6 col-xs-6 control-label">文库参考体积：</label>'+
        '<div class="col-sm-6 col-md-6 col-xs-6">'+
        '<input type="text" id="LibraryReferenceVolume" name="LibraryReferenceVolume" value="' + ($("#pooling_formSearch #LibraryReferenceVolume").val()?$("#pooling_formSearch #LibraryReferenceVolume").val():(setting?(setting.LibraryReferenceVolume && setting.LibraryReferenceVolume.value?setting.LibraryReferenceVolume.value:detectionExtend.wkcktj):detectionExtend.wkcktj)) + '" class="form-control" style="height:30px;"/>'+
        '</div>'+
        '</div>';
    html += '<div class="col-md-12 col-sm-12 col-xs-12 form-group">'+
        '<label class="col-md-6 col-sm-6 col-xs-6 control-label">稀释文库参考体积：</label>'+
        '<div class="col-sm-6 col-md-6 col-xs-6">'+
        '<input type="text" id="DilutedLibraryReferenceVolume" name="DilutedLibraryReferenceVolume" value="' + ($("#pooling_formSearch #DilutedLibraryReferenceVolume").val()?$("#pooling_formSearch #DilutedLibraryReferenceVolume").val():(setting?(setting.DilutedLibraryReferenceVolume && setting.DilutedLibraryReferenceVolume.value?setting.DilutedLibraryReferenceVolume.value:detectionExtend.xswkcktj):detectionExtend.xswkcktj))  + '" class="form-control" style="height:30px;"/>'+
        '</div>'+
        '</div>';
    html += '<div class="col-md-12 col-sm-12 col-xs-12 form-group">'+
        '<label class="col-md-6 col-sm-6 col-xs-6 control-label">稀释倍数：</label>'+
        '<div class="col-sm-6 col-md-6 col-xs-6">'+
        '<input type="text" id="DilutionFactor" name="DilutionFactor" value="' + ($("#pooling_formSearch #DilutionFactor").val()?$("#pooling_formSearch #DilutionFactor").val():(setting?(setting.DilutionFactor && setting.DilutionFactor.value?setting.DilutionFactor.value:detectionExtend.xsbs):detectionExtend.xsbs)) + '" class="form-control" style="height:30px;"/>'+
        '</div>'+
        '</div>';
    html += '<div class="col-md-12 col-sm-12 col-xs-12 form-group">'+
        '<label class="col-md-6 col-sm-6 col-xs-6 control-label">数据量/pooling 系数（M reads）：</label>'+
        '<div class="col-sm-6 col-md-6 col-xs-6">'+
        '<input type="text" id="MReadsMultiple" name="MReadsMultiple" value="' + ($("#pooling_formSearch #MReadsMultiple").val()?$("#pooling_formSearch #MReadsMultiple").val():(setting?(setting.MReadsMultiple && setting.MReadsMultiple.value?setting.MReadsMultiple.value:detectionExtend.cnsd):detectionExtend.cnsd)) + '" class="form-control" style="height:30px;"/>'+
        '</div>'+
        '</div>';
    html += '<div class="col-md-12 col-sm-12 col-xs-12 form-group">'+
        '<label class="col-md-6 col-sm-6 col-xs-6 control-label">混合文库稀释体积(ul)：</label>'+
        '<div class="col-sm-6 col-md-6 col-xs-6">'+
        '<input type="text" id="MixDilutedLibraryReferenceVolume" name="MixDilutedLibraryReferenceVolume" value="' + ($("#pooling_formSearch #MixDilutedLibraryReferenceVolume").val()?$("#pooling_formSearch #MixDilutedLibraryReferenceVolume").val():(setting?(setting.MixDilutedLibraryReferenceVolume && setting.MixDilutedLibraryReferenceVolume.value?setting.MixDilutedLibraryReferenceVolume.value:detectionExtend.hhwkxstj):detectionExtend.hhwkxstj)) + '" class="form-control" style="height:30px;"/>'+
        '</div>'+
        '</div>';
    html += '<div class="col-md-12 col-sm-12 col-xs-12 form-group">'+
        '<label class="col-md-6 col-sm-6 col-xs-6 control-label">混合文库稀释浓度设定(pM)：</label>'+
        '<div class="col-sm-6 col-md-6 col-xs-6">'+
        '<input type="text" id="MixDilutedLibraryReferenceConcentration" name="MixDilutedLibraryReferenceConcentration" value="' + ($("#pooling_formSearch #MixDilutedLibraryReferenceConcentration").val()?$("#pooling_formSearch #MixDilutedLibraryReferenceConcentration").val():(setting?(setting.MixDilutedLibraryReferenceConcentration && setting.MixDilutedLibraryReferenceConcentration.value?setting.MixDilutedLibraryReferenceConcentration.value:detectionExtend.hhwkxsnd):detectionExtend.hhwkxsnd)) + '" class="form-control" style="height:30px;"/>'+
        '</div>'+
        '</div>';
    html += '</div>';
    $("#pooling_formSearch #settingForm").html(html);
}

//点击文件上传
function editfile(){
	$("#fileDiv").show();
	$("#file_btn").hide();
}
//点击隐藏文件上传
function cancelfile(){
	$("#fileDiv").hide();
	$("#file_btn").show();
}

function displayUpInfo(fjid){
	if(!$("#pooling_formSearch #fjids").val()){
		$("#pooling_formSearch #fjids").val(fjid);
	}else{
		$("#pooling_formSearch #fjids").val($("#pooling_formSearch #fjids").val()+","+fjid);
	}
}

function yl(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url="/ws/sjxxpripreview/?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url="/common/file/pdfPreview?fjid=" + fjid;
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
