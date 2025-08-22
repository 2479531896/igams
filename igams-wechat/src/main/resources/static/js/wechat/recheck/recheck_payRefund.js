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
	$("#rechcek_payRefundForm #refund_"+zfid).attr("disabled", true);
	var tkje = $("#rechcek_payRefundForm #sfje_"+zfid).text();
	$.ajax({
		url: '/recheck/recheck/refundSavePay',
		type: 'post',
		dataType: 'json',
		data : {"zfid":zfid, "tkje":tkje, "ybbh":$("#rechcek_payRefundForm #ybbh").val(), "access_token":$("#ac_tk").val()},
		success: function(data) {
			if(data.status == 'success'){
				$.success(data.message, function() {
					// 判断退款状态
					if(data.jg == "1"){// 退款成功
						$("#rechcek_payRefundForm #tkje_"+zfid).text(parseFloat($("#rechcek_payRefundForm #tkje_"+zfid).text()) + parseFloat(tkje));
					}else{// 退款中
						$("#rechcek_payRefundForm #tkzje_"+zfid).text(parseFloat($("#rechcek_payRefundForm #tkzje_"+zfid).text()) + parseFloat(tkje));
					}
					var sfje = parseFloat($("#rechcek_payRefundForm #sfje_"+zfid).text()) - parseFloat(tkje);
					$("#rechcek_payRefundForm #sfje_"+zfid).text(sfje);
					if(sfje == 0){
						$("#rechcek_payRefundForm #refund_"+zfid).remove();
					}
				});
			}else{
				$.error("退款申请失败！" + data.message);
			}
			$("#rechcek_payRefundForm #refund_"+zfid).attr("disabled", false);
		}
	});
}

$(function(){
	init();
});