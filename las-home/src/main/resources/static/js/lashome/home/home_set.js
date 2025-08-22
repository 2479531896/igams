/**
 * 初始化页面
 * @returns
 */
function initPage() {
	// 初始化实验室背景图
	var fjid = $("#homeSet_Form #fjid").val();
	if(fjid){
		$(".lab_bg").css("background-image","url(/common/file/getFileInfo?fjid="+fjid+"&access_token="+$("#ac_tk").val()+")");
		$(".lab_bg").css("background-size","100% 100%");
	}
	mydrag();
	window.dragMoveListener = dragMoveListener;
}

/**
 * 初始化仪器信息
 * @returns
 */
function initDevice(){
	var sysid = $("#homeSet_Form #sysid").val();
	// 初始化仪器信息
	$.ajax({
		type : 'post',
		url :"/lashome/home/getDeviceList",
		cache : false,
		data : {
			"access_token" : $("#ac_tk").val(),
			"sysid" : sysid,
			"sybj": '1',
		},
		dataType : 'json',
		success : function(data) {
			if(data.status == "success"){
				var bg_width = $(".lab_bg").width();
				var bg_height = $(".lab_bg").height();
				for(var i=0; i< data.yqxxDtos.length; i++){
					var top = (bg_height*Number.parseFloat(data.yqxxDtos[i].dbjl)).toFixed(0);
					var left = (bg_width*Number.parseFloat(data.yqxxDtos[i].zcjl)).toFixed(0);
					var width = (bg_width*Number.parseFloat(data.yqxxDtos[i].kd)).toFixed(0);
					var height = (bg_height*Number.parseFloat(data.yqxxDtos[i].gd)).toFixed(0);
					$('.lab_bg').append('<div class="draggable" id="drag'+data.yqxxDtos[i].yqid+'" data-fid="'+data.yqxxDtos[i].yqid+'"  style="background-size: 100% 100%;background-image:url(/common/file/getFileInfo?fjid='+data.yqxxDtos[i].fjid+'&access_token='+$("#ac_tk").val()+');transform: translate('+left+'px, '+top+'px);width:'+width+'px;height:'+height+'px;" data-x="'+left+'" data-y="'+top+'">\n' +
				            '<h4 style="height:50px;line-height:50px;">'+data.yqxxDtos[i].yqmc+'</h4>\n' +
				            '<p class="lt"></p>\n' +
				            '<p class="wh"></p>\n' +
				            '<p class="close">关闭</p>\n' +
				            '</div>');
				}
			}
		}
	});
}

/**
 * 保存当前设置
 * @returns
 */
function save(){
	$.confirm('您确定要保存当前设置吗？',function(result){
		if(result){
			
		}
	});
}

/**
 * 实验室信息设置
 * @returns
 */
function config(){
	var url = "/lashome/home/labConfigView?access_token="+$("#ac_tk").val();
	$.showDialog(url,'实验室信息设置',viewSettingConfig);
}
var viewSettingConfig = {
	width		: "600px",
	modalName	: "viewSettingModel",
	formName	: "labConfig_Form",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#labConfig_Form").valid()){
					return false;
				}
				if(!$("#labConfig_Form #fjids").val() && $("#labConfig_Form #fjcfbDto").children().length == 0){
					$.alert("请上传实验室结构图！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#labConfig_Form input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"labConfig_Form",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				},".modal-footer > button");
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
/**
 * 新增设备信息
 * @returns
 */
function addDevice(){
	var url = "/lashome/home/addDeviceView?access_token="+$("#ac_tk").val()+"&sysid="+$("#sysid").val();
	$.showDialog(url,'新增设备信息',viewAddDeviceConfig);
}
var viewAddDeviceConfig = {
	width		: "600px",
	modalName	: "viewAddDeviceModel",
	formName	: "editDevice_Form",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#editDevice_Form").valid()){
					return false;
				}
				if(!$("#editDevice_Form #fjids").val() && $("#editDevice_Form #fjcfbDto").children().length == 0){
					$.alert("请上传仪器图片！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#editDevice_Form input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"editDevice_Form",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								var yqxxDto = responseText["yqxxDto"];
								var deviceHtml = "";
								deviceHtml += "<div class='col-sm-12 col-sm-12 col-xs-12' style='padding:10px;'><div class='btn-group' style='width:100%;height:40px;'>";
								deviceHtml += "<button type='button' id='"+yqxxDto.yqid+"' class='btn btn-primary dragBtn' style='height:100% !important;width: calc(100% - 70px);'>";
								deviceHtml += "<span>"+yqxxDto.yqmc+"</span></button>";
								deviceHtml += "<button type='button' class='btn btn-success dropdown-toggle editBtn' data-yqid='"+yqxxDto.yqid+"' style='height:100% !important;width:40px;'>";
								deviceHtml += "<span class='glyphicon glyphicon-pencil'></span></button>";
								deviceHtml += "<button type='button' class='btn btn-danger dropdown-toggle delBtn' data-yqid='"+yqxxDto.yqid+"' style='height:100% !important;width:30px;'>";
								deviceHtml += "<span class='glyphicon glyphicon-remove'></span></button>";
								deviceHtml += "</div></div>";
								$("#deviceList").append(deviceHtml);
							}
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				},".modal-footer > button");
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
/**
 * 编辑设备信息
 */
var deviceClick = $('#homeSet_Form').on('click','.editBtn',function(e){
	var url = "/lashome/home/modDeviceView?access_token="+$("#ac_tk").val()+"&yqid="+e.currentTarget.dataset.yqid;
	$.showDialog(url,'编辑设备信息',viewModDeviceConfig);
});
var viewModDeviceConfig = {
	width		: "600px",
	modalName	: "viewAddDeviceModel",
	formName	: "editDevice_Form",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#editDevice_Form").valid()){
					return false;
				}
				if(!$("#editDevice_Form #fjids").val() && $("#editDevice_Form #fjcfbDto").children().length == 0){
					$.alert("请上传仪器图片！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#editDevice_Form input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"editDevice_Form",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								$("#"+responseText["yqxxDto"].yqid+" span").text(responseText["yqxxDto"].yqmc);
							}
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				},".modal-footer > button");
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
/**
 * 删除设备信息
 */
var delClick = $('#homeSet_Form').on('click','.delBtn',function(e){
	var yqid = e.currentTarget.dataset.yqid;
	$.confirm('您确定要删除选中设备吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url= "/lashome/home/delDevice";
			jQuery.post(url,{"yqid":yqid, "access_token":$("#ac_tk").val()},function(responseText){
				setTimeout(function(){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							$("#"+yqid).parent().parent().remove();
							$("#drag"+yqid).remove();
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
});

/**
 * 选中设备
 */
var deviceClick = $('.dragBtn').unbind("click").click(function(e){
	var text = $("#"+e.currentTarget.id).text();
	var fjid = e.currentTarget.dataset.fjid;
	$('.lab_bg').append('<div class="draggable" id="drag' + e.currentTarget.id + '" data-fid="'+e.currentTarget.id+'"  style="background-size: 100% 100%;background-image:url(/common/file/getFileInfo?fjid='+fjid+'&access_token='+$("#ac_tk").val()+')">\n' +
			'<h4 style="height:50px;line-height:50px;">' + text + '</h4>\n' +
            '<p class="lt"></p>\n' +
            '<p class="wh"></p>\n' +
            '<p class="close">关闭</p>\n' +
            '</div>');
	$(this).attr('disabled',true);
	// 保存默认信息, 计算参数
	var bg_width = $(".lab_bg").width();
	var bg_height = $(".lab_bg").height();
	var kd = (150/bg_width).toFixed(5);
	var gd = (150/bg_height).toFixed(5);
	$.ajax({
		type : 'post',
		url :"/lashome/home/updateDeviceInfo",
		cache : false,
		data : {
			"access_token" : $("#ac_tk").val(),
			"yqid" : e.currentTarget.id,
			"kd": kd,
			"gd": gd,
			"sybj": '1',
		},
		dataType : 'json',
		success : function(data) {
			console.log(data.status+" "+data.message);
		}
	});
});

/**
 * 初始化窗口操作
 * @returns
 */
function mydrag() {
	interact('.draggable').draggable({
            // 启用惯性投掷
            inertia: false,
            // 将元素保留在其父元素的区域内
        	modifiers: [      
        		interact.modifiers.restrictRect({        
        			restriction: 'parent',        
        			endOnly: true
        			// elementRect: { top: 0, left: 0, bottom: 1, right: 1 }
        		})
        	], 
        	// 启用自动滚动    
        	autoScroll: true, 
        	listeners: {      
        		// 在每个dragmove事件上调用此函数      
        		move: dragMoveListener,      
        		// 在每个dragend事件中调用此函数      
        		end (event) {
        			dragMoveEnd(event);
        		} 
        	}
            // maxPerElement: 100,
        })
        .resizable({
            // 从所有边缘和角落调整大小
            edges: { left: true, right: true, bottom: true, top: true },
            modifiers: [      
            	//将边缘保留在父级内部      
            	interact.modifiers.restrictEdges({        
            		outer: 'parent'      
            	}),      
            	// 最小尺寸      
            	interact.modifiers.restrictSize({        
            		min: { width: 5, height: 5 }      
            	})    
            ],
            inertia: false
        })
        .on('resizemove', function (event) {
        	resizeMoveListener(event);
        })
        .on('resizeend', function (event) {
        	resizeMoveEnd(event);
        });
};
/**
 * 监听仪器拖动
 * @param event
 * @returns
 */
function dragMoveListener(event) {
	// keep the dragged position in the data-x/data-y attributes
    var target = event.target;
    var x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx;
    var y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;
    // translate the element
    target.style.webkitTransform = target.style.transform = 'translate(' + x + 'px, ' + y + 'px)';
    target.setAttribute('data-x', x);
    target.setAttribute('data-y', y);
}
/**
 * 仪器拖动完成
 * @param event
 * @returns
 */
function dragMoveEnd(event){
	var textEl = event.target.querySelector('p.lt');
    textEl && (textEl.textContent = '左' + event.target.getAttribute('data-x') + "上" + event.target.getAttribute('data-y') + 'px');
    var closeEl = event.target.querySelector('p.close');
    closeEl.style.display = 'block';
    var yqid = event.target.dataset.fid;
	var left = event.target.getAttribute('data-x');
	var top = event.target.getAttribute('data-y');
	// 计算外部距离，转换为百分比,保留3位小数
	var bg_width = $(".lab_bg").width();
	var bg_height = $(".lab_bg").height();
	var zcjl = (Number.parseFloat(left)/bg_width).toFixed(5);
	var dbjl = (Number.parseFloat(top)/bg_height).toFixed(5);
	$.ajax({
		type : 'post',
		url :"/lashome/home/updateDeviceInfo",
		cache : false,
		data : {
			"access_token" : $("#ac_tk").val(),
			"yqid" : yqid,
			"dbjl": dbjl,
			"zcjl": zcjl,
		},
		dataType : 'json',
		success : function(data) {
			console.log(data.status+" "+data.message);
		}
	});
}
/**
 * 仪器缩放监听
 * @param event
 * @returns
 */
function resizeMoveListener(event){
	var s = $(event.target);
    var target = event.target;
    var x = (parseFloat(target.getAttribute('data-x')) || 0);
    var y = (parseFloat(target.getAttribute('data-y')) || 0);
    var xr = (parseFloat(target.getAttribute('data-x')) || 0) + s.width();
    var yb = (parseFloat(target.getAttribute('data-y')) || 0) + s.height();
    // translate when resizing from top or left edges
    x += event.deltaRect.left;
    y += event.deltaRect.top;
    xr += event.deltaRect.right;
    yb += event.deltaRect.bottom;

    target.style.width = (xr - x) + 'px';
    target.style.height = (yb - y) + 'px';
    target.style.webkitTransform = target.style.transform ='translate(' + x + 'px,' + y + 'px)';
    
    target.setAttribute('data-x', x);
    target.setAttribute('data-y', y);
    target.querySelector('p.wh').textContent = (xr - x) + '\u00D7' + (yb - y) + 'px';
    var textEl = event.target.querySelector('p.lt');
    textEl && (textEl.textContent = '左' + event.target.getAttribute('data-x') + "上" + event.target.getAttribute('data-y') + 'px');
}
/**
 * 仪器缩放完成
 * @param event
 * @returns
 */
function resizeMoveEnd(event){
	var yqid = event.target.dataset.fid;
	var width = $(event.target).width();
	var height = $(event.target).height();
	var bg_width = $(".lab_bg").width();
	var bg_height = $(".lab_bg").height();
	var kd = (Number.parseFloat(width)/bg_width).toFixed(5);
	var gd = (Number.parseFloat(height)/bg_height).toFixed(5);
	$.ajax({
		type : 'post',
		url :"/lashome/home/updateDeviceInfo",
		cache : false,
		data : {
			"access_token" : $("#ac_tk").val(),
			"yqid" : yqid,
			"kd": kd,
			"gd": gd,
		},
		dataType : 'json',
		success : function(data) {
			console.log(data.status+" "+data.message);
		}
	});
}

/**
 * 关闭按钮点击事件
 */
var closeClick = $('#homeSet_Form').on('click','.close',function(e){
    var yqid = e.currentTarget.parentElement.dataset.fid;
	$.ajax({
		type : 'post',
		url :"/lashome/home/updateDeviceInfo",
		cache : false,
		data : {
			"access_token" : $("#ac_tk").val(),
			"yqid" : yqid,
			"sybj": '0',
		},
		dataType : 'json',
		success : function(data) {
			console.log(data.status+" "+data.message);
		}
	});
	$("#"+$(this).parent().attr('data-fid')).attr('disabled',false);
    $(this).parent().remove();
});

$(document).ready(function() {
	var width = $('#homeSet_Form .lab_bg').width();
	var height = $('#homeSet_Form .lab_bg').height();
	initPage();
	initDevice();
});