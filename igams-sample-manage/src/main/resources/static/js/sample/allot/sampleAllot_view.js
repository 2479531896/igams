

function openByt(sjid,event) {
    var url=$("#sampleAllotViewForm #urlPrefix").val()+"/inspection/inspection/pagedataViewSjxx?sjid="+sjid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'送检详细信息',viewBytConfig);
}
/**
 * 病原体查看页面模态框
 */
var viewBytConfig={
    width		: "800px",
    modalName	:"viewBytModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}


function viewHzInfo(hzid){
    tourl = ($("#sampleAllotViewForm #urlPrefix").val()?$("#sampleAllotViewForm #urlPrefix").val():($("#urlPrefix").val()?$("#urlPrefix").val():"")) + "/sampleAllot/pagedataSampleAllotListView";
    var url = tourl + "?dchz=" +hzid;
    $.showDialog(url,'详情查看',viewHzInfoConfig);
}

//查看弹框初始化
var viewHzInfoConfig = {
	width		: "1200px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};