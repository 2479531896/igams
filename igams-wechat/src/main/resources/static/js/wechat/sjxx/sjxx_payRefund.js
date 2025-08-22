/**
 * 页面初始化
 * @returns
 */
function init(){
	
}

/**
 * 退款按钮点击事件
 * @param qgmxid
 * @param e
 * @returns
 */
function refundClick(zfid,e){
	$("#payRefundForm #refund_"+zfid).attr("disabled", true);
	var tkje = $("#payRefundForm #sfje_"+zfid).text();
	$.ajax({
		url: '/inspection/inspection/refundSavePay',
		type: 'post',
		dataType: 'json',
		data : {"zfid":zfid, "tkje":tkje, "access_token":$("#ac_tk").val()},
		success: function(data) {
			if(data.status == 'success'){
				$.success(data.message, function() {
					// 判断退款状态
					if(data.jg == "1"){// 退款成功
						$("#payRefundForm #tkje_"+zfid).text(parseFloat($("#payRefundForm #tkje_"+zfid).text()) + parseFloat(tkje));
					}else{// 退款中
						$("#payRefundForm #tkzje_"+zfid).text(parseFloat($("#payRefundForm #tkzje_"+zfid).text()) + parseFloat(tkje));
					}
					var sfje = parseFloat($("#payRefundForm #sfje_"+zfid).text()) - parseFloat(tkje);
					$("#payRefundForm #sfje_"+zfid).text(sfje);
					if(sfje == 0){
						$("#payRefundForm #refund_"+zfid).remove();
					}
				});
			}else{
				$.error("退款申请失败！" + data.message);
			}
			$("#payRefundForm #refund_"+zfid).attr("disabled", false);
		}
	});
}

$(function(){
	init();
});