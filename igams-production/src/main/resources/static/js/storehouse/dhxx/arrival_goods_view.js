function queryByhtid(htid){
	var url=$("#ajaxForm #urlPrefix").val()+"/contract/contract/pagedataViewContract?htid="+htid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'合同信息',viewhtConfig);
}
var viewhtConfig = {
				width		: "1500px",
				modalName	:"viewHtModal",
				offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
				buttons		: {
					cancel : {
						label : "关 闭",
						className : "btn-default"
					}
				}
	};
function queryBywlid(wlid){
		var url=$("#ajaxForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
		$.showDialog(url,'物料信息',viewWlConfig);	
	}
	
var viewWlConfig = {
		width		: "1400px",
		height		: "500px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
};
function viewSb(sbysid){
	var url=$("#ajaxForm #urlPrefix").val()+"/device/device/pagedataViewDeviceCheck?sbysid="+sbysid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'设备信息',viewSbConfig);
}
var viewSbConfig = {
	width		: "1400px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
function queryByqgid(qgid){
	var url=$("#ajaxForm #urlPrefix").val()+"/purchase/purchase/viewPurchase?qgid="+qgid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'请购信息',viewWlConfig);	
}


function queryByDhjyid(dhjyid){
		var url=$("#ajaxForm #urlPrefix").val()+"/inspectionGoods/inspectionGoods/viewInspectionGoods?dhjyid="+dhjyid+"&access_token=" + $("#ac_tk").val();
		$.showDialog(url,'检验信息',viewhtConfig);
}
function queryByRkid(rkid){
		var url=$("#ajaxForm #urlPrefix").val()+"/warehouse/putInStorage/pagedataViewPutInStorage?rkid="+rkid+"&access_token=" + $("#ac_tk").val();
		$.showDialog(url,'入库信息',viewhtConfig);
}

//从领料车中删除
function delPickingCar(hwid){

	$.ajax({
		async:false,
		type:'post',
		url:$('#ajaxForm #urlPrefix').val()+"/storehouse/arrivalGoods/pagedataGetCkhwxx",
		cache: false,
		data: {"hwid":hwid,"access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			if(data.hwxxDto.ckhwid!=null&&data.hwxxDto.ckhwid!=""){
				var ckhwid=data.hwxxDto.ckhwid;
				$.ajax({
					type:'post',
					url:$('#ajaxForm #urlPrefix').val()+"/storehouse/stock/pagedataDelFromPickingCar",
					cache: false,
					data: {"ckhwid":ckhwid,"access_token":$("#ac_tk").val()},
					dataType:'json',
					success:function(data){
						//返回值
						if(data.status=='success'){
							pickingCar_addOrDelNum("del");
							$("#ajaxForm #td"+hwid).remove();
							var html="<td style='width:8%;vertical-align:middle;' id='td"+hwid+"'><span  class='btn btn-info' title='加入领料车' onclick=\"addPickingCar('" + hwid + "')\" >领料</span></td>";
							$("#ajaxForm #t"+hwid).append(html);
						}
					}
				});
			}
		}
	});
}

function queryByHwid(hwid){
	$.ajax({
		async:false,
		type:'post',
		url:$('#ajaxForm #urlPrefix').val()+"/storehouse/arrivalGoods/pagedataGetCkhwxx",
		cache: false,
		data: {"hwid":hwid,"access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			if(data.hwxxDto.ckhwid!=null&&data.hwxxDto.ckhwid!=""){
				var ckhwid=data.hwxxDto.ckhwid;
				$.ajax({
					async:false,
					type:'post',
					url:$('#ajaxForm #urlPrefix').val()+"/storehouse/stock/pagedataCheckToPickingCar",
					cache: false,
					data: {"ckhwid":ckhwid,"access_token":$("#ac_tk").val()},
					dataType:'json',
					success:function(data){
						//返回值
						if(data.llcglDto!=null){
							$("#ajaxForm #td"+hwid).remove();
							var html="<td style='width:8%;vertical-align:middle;' id='td"+hwid+"'><span  class='btn btn-danger' title='移出领料车' onclick=\"delPickingCar('" + hwid + "')\" >取消</span></td>";
							$("#ajaxForm #t"+hwid).append(html);
						}else{
							$("#ajaxForm #td"+hwid).remove();
							var html="<td style='width:8%;vertical-align:middle;' id='td"+hwid+"'><span  class='btn btn-info' title='加入领料车' onclick=\"addPickingCar('" + hwid + "')\" >领料</span></td>";
							$("#ajaxForm #t"+hwid).append(html);
						}
					}
				});
			}
		}
	});
}



//添加至入库车
function addPickingCar(hwid){
	$.ajax({
		async:false,
		type:'post',
		url:$('#ajaxForm #urlPrefix').val()+"/storehouse/arrivalGoods/pagedataGetCkhwxx",
		cache: false,
		data: {"hwid":hwid,"access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			if(data.hwxxDto.ckhwid!=null&&data.hwxxDto.ckhwid!=""){
				var ckhwid=data.hwxxDto.ckhwid;
				$.ajax({
					async:false,
					type:'post',
					url:$('#ajaxForm #urlPrefix').val()+"/storehouse/requisition/pagedataQueryWlxxByWlid",
					cache: false,
					data: {"ckhwid":ckhwid,"access_token":$("#ac_tk").val()},
					dataType:'json',
					success:function(data){
						//返回值
						if(data.ckhwxxDto!=null && data.ckhwxxDto!=''){
							//kcbj=1 有库存，kcbj=0 库存未0
							if(data.ckhwxxDto.kcbj!='1'){
								$.confirm("该物料库存不足！");
							}else{
								$.ajax({
									type:'post',
									url:$('#ajaxForm #urlPrefix').val()+"/storehouse/stock/pagedataAddToPickingCar",
									cache: false,
									data: {"ckhwid":ckhwid,"access_token":$("#ac_tk").val()},
									dataType:'json',
									success:function(data){
										//返回值
										if(data.status=='success'){
											pickingCar_addOrDelNum("add");
											$("#ajaxForm #td"+hwid).remove();
											var html="<td style='width:8%;vertical-align:middle;' id='td"+hwid+"'><span  class='btn btn-danger' title='移出领料车' onclick=\"delPickingCar('" + hwid + "')\" >取消</span></td>";
											$("#ajaxForm #t"+hwid).append(html);
										}
									}
								});
							}
						}else{
							$.confirm("该物料不存在!");
						}
					}
				});
			}
		}
	});
}

/**
 * 领料车数字加减
 * @param sfbj
 * @returns
 */
function pickingCar_addOrDelNum(sfbj){
	if(sfbj=='add'){
		ll_count=parseInt(ll_count)+1;
	}else{
		ll_count=parseInt(ll_count)-1;
	}
	if((ll_count==1 && sfbj=='add') || (ll_count==0 && sfbj=='del')){
		ll_btnOinit();
	}
	$("#ll_num").text(ll_count);
}

var ll_count=0;
function ll_btnOinit(){
	if(ll_count>0){
		var strnum=ll_count;
		if(ll_count>99){
			strnum='99+';
		}
		var html="";
		html+="<span id='ll_num' style='position:absolute;background-color:red;color:white;border-radius: 15px;border:0px;width:20px;height:20px;top:-10px;font-size:10px;line-height:150%;'>"+strnum+"</span>";
		$("#stock_formSearch #btn_pickingcar").append(html);
	}else{
		$("#stock_formSearch #ll_num").remove();
	}
}
/**
 * 点击图片预览
 * @param fjid
 * @param wjm
 * @returns
 */
function view(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url=$('#ajaxForm #urlPrefix').val()+"/ws/sjxxpripreview?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= $('#ajaxForm #urlPrefix').val()+"/common/file/pdfPreview?fjid=" + fjid;
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
/**
 * 点击文件下载
 * @param fjid
 * @returns
 */
function xz(fjid){
	jQuery('<form action="'+$('#ajaxForm #urlPrefix').val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
		'<input type="text" name="fjid" value="'+fjid+'"/>' +
		'<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
		'</form>')
		.appendTo('body').submit().remove();
}
$(document).ready(function() {

	var val = $('#ajaxForm #hwxx_json').val();
	var parse = JSON.parse(val);
	for (var i = 0; i < parse.length; i++) {
		if(parse[i].zt=='99'){
			queryByHwid(parse[i].hwid);
		}
	}
});
