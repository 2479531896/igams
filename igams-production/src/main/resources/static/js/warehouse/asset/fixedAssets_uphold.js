//添加日期控件
laydate.render({
    elem: ' #upholdAssetForm #kssyrq'
    , theme: '#2381E9'
});

$("#upholdAssetForm #lb").change(function(){
    var val=$("#upholdAssetForm #lb").find("option:selected").attr("csdm");
    $("#upholdAssetForm #lbdm").val(val);
});

$("#upholdAssetForm #zjfs").change(function(){
    var val=$("#upholdAssetForm #zjfs").find("option:selected").attr("csdm");
    var cskz1=$("#upholdAssetForm #zjfs").find("option:selected").attr("cskz1");
    var csmc=$("#upholdAssetForm #zjfs").find("option:selected").attr("csmc");
    $("#upholdAssetForm #zjfsdm").val(val);
    $("#upholdAssetForm #zjfscskz1").val(cskz1);
    $("#upholdAssetForm #zjfsmc").val(csmc);
});

$("#upholdAssetForm #syzk").change(function(){
    var val=$("#upholdAssetForm #syzk").find("option:selected").attr("csdm");
    $("#upholdAssetForm #syzkdm").val(val);
});

$("#upholdAssetForm #zjff").change(function(){
    var val=$("#upholdAssetForm #zjff").find("option:selected").attr("csdm");
    $("#upholdAssetForm #zjffdm").val(val);
});

$("#upholdAssetForm #biz").change(function(){
    var val=$("#upholdAssetForm #biz").find("option:selected").attr("csmc");
    $("#upholdAssetForm #bizmc").val(val);
});

$("#upholdAssetForm #zcz").change(function(){
    var val=$("#upholdAssetForm #zcz").find("option:selected").attr("csdm");
    $("#upholdAssetForm #zczdm").val(val);
});

$("#upholdAssetForm #dyzjkm").change(function(){
    var val=$("#upholdAssetForm #dyzjkm").find("option:selected").attr("csdm");
    var csmc=$("#upholdAssetForm #dyzjkm").find("option:selected").attr("csmc");
    $("#upholdAssetForm #dyzjkmdm").val(val);
    $("#upholdAssetForm #dyzjkmmc").val(csmc);
});

$("#upholdAssetForm #xm").change(function(){
    var val=$("#upholdAssetForm #xm").find("option:selected").attr("csdm");
    var csmc=$("#upholdAssetForm #xm").find("option:selected").attr("csmc");
    $("#upholdAssetForm #xmdm").val(val);
    $("#upholdAssetForm #xmmc").val(csmc);
});

$(function(){
    //所有下拉框添加choose样式
    jQuery('#upholdAssetForm .chosen-select').chosen({width: '100%'});
});