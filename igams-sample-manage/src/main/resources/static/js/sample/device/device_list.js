var devicetreeTable;
$(document).ready(function(){
	devicetreeTable = $('#devicetable').bootstrapTreeTable({
	    toolbar: "#device-toolbar",    //顶部工具条
	    expandColumn : 1,            // 在哪一列上面显示展开按钮
	    id : "sbid",               // 选取记录返回的值
	    fid : "fsbid",
        code : "sbid",               // 用于设置父子关系
        parentCode : "fsbid",       // 用于设置父子关系
        expandAll : false, 
        expandFirst : false,
	    columns: [{
	        field: 'selectItem', 
	        radio: true
	     },{
	        title: '设备类型',
	        field: 'sblxmc',
	        width: '20%',
	        formatter: function(value,row, index) {
	            if (row.sblx == 'bx') {
	            	return '<i class="glyphicon glyphicon-folder-close"></i> <span class="nav-label">' + row.sblxmc + '</span>';
	            } else if(row.sblx == 'ct'){
	                return '<i class="fa fa-server"></i> <span class="nav-label">' + row.sblxmc + '</span>';
	            } else if(row.sblx == 'hz'){
	                return '<i class="fa fa-inbox"></i> <span class="nav-label">' + row.sblxmc + '</span>';
	            }else
	                return row.sblxmc;
	        }
	    },
	    {
	        title: '设备ID',
	        field: 'sbid',
	        width: '1%',
	        invisible:true
	    },
	    {
	        title: '父设备ID',
	        field: 'fsbid',
	        width: '1%',
	        invisible:true
	    },
	    {
	        title: 'sblx',
	        field: 'sblx',
	        width: '1%',
	        invisible:true
	    },
	    {
	        field: 'wz',
	        title: '位置',
	        width: '10%',
	        align: "center"
	    },
	    {
	        field: 'sbh',
	        title: '设备号',
	        width: '10%',
	        align: "center"
	    },
	    {
	        field: 'jcdwmc',
	        title: '存储单位',
	        width: '10%',
	        align: "center"
	    },
	    {
	        title: '标本类型',
	        field: 'yblxmc',
	        width: '10%',
	        align: "center",
	        formatter: function(value,item, index) {
	            if (value == 'null') {
	            	return "";
	            }
	            return value;
	        }
	    },
	    {
	        field: 'cfs',
	        title: '存放数',
	        width: '10%',
	        align: "center",
	        formatter: function(value,item, index) {
	        	if(value == null){
	        		return "";
	        	}
	        	return value;
	        }
	    },
	    {
	        field: 'px',
	        title: '排序',
	        width: '10%',
	        align: "center"
	    },
	    {
	        field: 'bz',
	        title: '备注',
	        width: '20%',
	        align: "center",
	        formatter: function(value,item, index) {
	        	if(value == null){
	        		return "";
	        	}
	        	return value;
	        }
	    },
	    {
	        title: '操作',
	        width: '20%',
	        align: "center",
	        formatter: function(value,row, index) {
	            var actions = '';
	            actions +="<div class='btn-group'>";
	            if (row.sblx == 'bx') {
	            	actions +='<span class="btn btn-primary glyphicon glyphicon-plus" onclick="addSb(\'ct\',\'新增抽屉\',this)" title="新增抽屉">新增抽屉</span> ';
	            }else if (row.sblx == 'ct') {
	            	actions +='<span class="btn btn-info glyphicon glyphicon-plus"  onclick="addSb(\'hz\',\'新增抽屉\',this)" title="新增盒子">新增盒子</span> ';
	            }
	            actions +='<span class="btn btn-success glyphicon glyphicon-edit"  onclick="modifySb(this)" title="编辑">编辑</span> ';
	            actions +='<span class="btn btn-danger glyphicon glyphicon-remove"  onclick="deleteSb(this)" title="删除">删除</span>';
	            return actions +='</div>';
	        }
	    }],
	    data:[],
	    url:"/sample/device/pageGetListDeviceTree",
	    ajaxParams:{"access_token":$("#ac_tk").val()}
	});
});

var addSbConfig = {
	width		: "500px",
	modalName	: "addBxModal",
	formName	: "editbx_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#editbx_ajaxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
		        var tmpInput=$("<input type='hidden' name='access_token'/>");
		        tmpInput.attr("value", $("#ac_tk").val());
		        var myform=$('#'+opts["formName"]||"ajaxForm");
			    myform.append(tmpInput);
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					$this.reset();
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							// devicetreeTable.load();
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
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

$("#btn-addbx").click(function(){
	var url="/sample/device/pagedataAddSb?sblx=bx";
	$.showDialog(url,'新增冰箱',addSbConfig);
})

function modifySb(_this){
	var selecteds = _this.parentElement.parentElement.parentElement.children;
	var url="/sample/device/pagedataModSb?sblx="+selecteds.sblx.title+"&sbid="+selecteds.sbid.title;
	$.showDialog(url,'修改'+selecteds.sblxmc.title,addSbConfig);
}

function addSb(lx,title,_this){
	var selecteds = _this.parentElement.parentElement.parentElement.children;
	var url="/sample/device/pagedataAddSb?sblx="+lx+"&fsbid="+selecteds.sbid.title;
	$.showDialog(url,title,addSbConfig);
}

function deleteSb(_this){
	var selecteds = _this.parentElement.parentElement.parentElement.children;
	var sbid =selecteds.sbid.title;
	$.confirm('您确定要删除所选择的记录吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url="/sample/device/pagedataDelSb";
			jQuery.post(url,{sbid:sbid,"access_token":$("#ac_tk").val()},function(responseText){
				setTimeout(function(){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							devicetreeTable.load();
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