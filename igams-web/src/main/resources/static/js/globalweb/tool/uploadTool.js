
$(function () { $('.collapse').collapse('hide')});

function successBtn() {
    const btn = document.querySelector('.btn.btn-sm.btn-primary');
    btn.disabled = true;
    if(!$("#uploadToolForm #ids").val() && ($("#uploadToolForm #zjmc").val()==null || $("#uploadToolForm #zjmc").val()=="")){
        $.error("请填写名称!");
        btn.disabled = false;
        return false;
    }
    if(!$("#uploadToolForm #ids").val() && ($("#uploadToolForm #zylj").val()==null || $("#uploadToolForm #zylj").val()=="")){
        $.error("请填写网址!");
        btn.disabled = false;
        return false;
    }
    var url = "/systemrole/user/pagedataSaveUploadTool";
    $.ajax({
        url: url,
        type: 'post',
        dataType: 'json',
        data : {"access_token" : $("#ac_tk").val(),"zylj" : $("#uploadToolForm #zylj").val(),"zjmc" : $("#uploadToolForm #zjmc").val(),"ids" : $("#uploadToolForm #ids").val()},
        success: function(data) {
            btn.disabled = false;
            if(data.status == 'success'){
                $.success(data.message);
                if(data.zjglDtoList!=null && data.zjglDtoList.length>0){
                    for(var i=0;i<data.zjglDtoList.length;i++){
                        const row = document.getElementById("targetRow");
                        const newRow = document.createElement("tr");
                        newRow.id = "row_" + data.zjglDtoList[i].zjid;
                        const newCell = document.createElement("td");
                        newCell.className = "col-md-11 col-sm-11";
                        newCell.innerHTML = '<a href="javascript:;" onclick="onloadTool(\'' + data.zjglDtoList[i].zylj + '\',\'' + data.zjglDtoList[i].zjlx + '\')">'+data.zjglDtoList[i].zjmc+'</a>' ;
                        newRow.appendChild(newCell);
                        const newCell2 = document.createElement("td");
                        newCell2.className = "col-md-1 col-sm-1";
                        newCell2.innerHTML = '<span style="margin-left:5px;" class="btn btn-danger" title="删除" onclick=(\'' + data.zjglDtoList[i].zjid + '\') >删除</span>';
                        newRow.appendChild(newCell2);
                        row.appendChild(newRow);
                    }
                }
                $("#uploadToolForm #zylj").val("");
                $("#uploadToolForm #zjmc").val("");
                $("#uploadToolForm #ids").val("");
                const div = document.getElementById("fileDiv");
                div.innerHTML = " <input id='pro_file' name='pro_file' type='file'>";
                var oFileInput = new FileInput();
                var uploadShopping_params = [];
                oFileInput.Init("uploadToolForm","displayUpInfo",2,1,"pro_file",null,uploadShopping_params,"ywlx");
            }else{
                btn.disabled = false;
                $.error(data.message);
            }
        }
    });
}

function delTool(zjid){
    var url = "/systemrole/user/pagedataDelTool";
    $.ajax({
        url: url,
        type: 'post',
        dataType: 'json',
        data : {"access_token" : $("#ac_tk").val(),"zjid" : zjid},
        success: function(data) {
            if(data.status =='success'){
                $.success(data.message);
                const row = document.getElementById(zjid);
                if (row) {
                    row.parentNode.removeChild(row);
                }
            }
        }
    });
}

function onloadTool(zylj,zjlx) {
    if(zjlx=="external"){
        window.open(zylj);
        return;
    }
    if(zjlx=="tool"){
        var url = "/homePage/homePage/pagedataGetHtml";
        $.ajax({
            url: url,
            type: 'post',
            dataType: 'json',
            data : {"access_token" : $("#ac_tk").val(),"zylj" : zylj},
            success: function(data) {
                if(data.status =='success' && data.dataString!=null && data.dataString!=""){
                	const newWindow = window.open('', '_blank');
                    newWindow.document.write(data.dataString);
                    newWindow.document.close();
                }
            }
        });
    }
}

function displayUpInfo(fjid){
	if(!$("#uploadToolForm #ids").val()){
		$("#uploadToolForm #ids").val(fjid);
	}else{
		$("#uploadToolForm #ids").val($("#uploadToolForm #ids").val()+","+fjid);
	}
}

$(function(){
	var oFileInput = new FileInput();
	var uploadShopping_params = [];
	oFileInput.Init("uploadToolForm","displayUpInfo",2,1,"pro_file",null,uploadShopping_params,"ywlx");
})