//自动检查服务器插入数据库进度
var checkDocument = function(){
	var formname = "#pooling_confirm_formSearch";
	var filePath=$(formname+" #filePath").val();
	$.ajax({
		type : "POST",
		url : "/experiment/library/pagedataConfrimExport",
		data : {"filePath":filePath,"access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.pooling!=null && data.pooling.length>0){
				var pooling=data.pooling;
				for (var i = 0; i < pooling.length; i++) {
					var tr="<tr>" +
								"<td>"+pooling[i].wkmc+"</td>" +
								"<td>"+pooling[i].Quantity+"</td>" +
								"<td><input  name='listmap["+i+"].pooling' placeholder='pooiling系数手动修正' value='"+pooling[i].csxz+"' style='width:100%;' class='form-control pooling-revise'></input></td>" +
							  /*"<td>"+pooling[i].yyjytj+"</td>" +
								"<td>"+pooling[i].ysyjytj+"</td>" +*/
							"</tr>";
					$("#pooling_confirm_formSearch #tbody1").append(tr);
				}
			}
			if(data.hyxs !=null && data.hyxs != ''){
				$("#pooling_confirm_formSearch #hyxs").val(data.hyxs);
	            $('.custom-input1 label').addClass('active');
			}
			if(data.hhwkcl !=null && data.hhwkcl != ''){
				$("#pooling_confirm_formSearch #hhwkcl").val(data.hhwkcl);
	            $('.custom-input2 label').addClass('active');
			}
			if(data.sjhh !=null && data.sjhh != ''){
				$("#pooling_confirm_formSearch #sjhh").val(data.sjhh);
                 $('.custom-input3 label').addClass('active');
			}
		}
	});
};

/**
 * 获取计算后的数据
 * @returns
 */
function getAllPooling(){
//	var flg=0;
//	$("#pooling_confirm_formSearch .pooling-revise").each(function(){
//		if(this.value!=null||this.value!=""){
//			flg++;
//		}
//	})
//	if($("#pooling_confirm_formSearch #hyxs").val()==""&&$("#pooling_confirm_formSearch #hhwkcl").val()==""&&$("#pooling_confirm_formSearch #sjhh").val()==""&&flg==0){
//		return false;
//	}else{
//		$("#pooling_confirm_formSearch #access_token").val($("#ac_tk").val());
//		$.ajax({
//			url:"/experiment/library/pagedataGetAllPooling",
//			type:"post",
//			dataType:"JSON",
//			data:$("#pooling_confirm_formSearch").serialize(),
//			success:function(data){
//
//			}
//		})
//	}
}

//下载模板按钮
$("#pooling_download #template_dw").unbind("click").click(function(){
	$("#pooling_download #access_token").val($("#ac_tk").val());
	$("#pooling_download").submit();
});

$(document).ready(function() {
	$('.custom-input1 input').phAnim();
	$('.custom-input2 input').phAnim();
	$('.custom-input3 input').phAnim();
	checkDocument();
})