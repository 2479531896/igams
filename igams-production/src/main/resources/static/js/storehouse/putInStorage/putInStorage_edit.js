var t_map = [];
var kwlist = [];
var rklbbj = $("#editPutInStorageForm #rklbbj").val();
var bs =  $('#editPutInStorageForm #bs').val();
var columnsArray =[];
var columnsArray_t = [
	{
		title: '序号',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序',
		width: '3%',
        align: 'left',
        visible:true
    }, {
        field: 'hwid',
        title: '货物ID',
        titleTooltip:'货物ID',
        width: '8%',
        align: 'left',
        visible: false
    },{
        field: 'wlid',
        title: '物料ID',
        titleTooltip:'物料ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '6%',
        align: 'left',
        visible: true
    }, {
        field: 'wlmc',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'dhlxmc',
        title: '到货类型',
        titleTooltip:'到货类型',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'rklbmc',
        title: '入库类别',
        titleTooltip:'入库类别',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'gg',
        title: '规格',
        titleTooltip:'规格',
        width: '4%',
        align: 'left',
        visible: true
    },{
        field: 'jldw',
        title: '单位',
        titleTooltip:'单位',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'sl',
        title: '数量',
        titleTooltip:'数量',
        width: '4%',
        align: 'left',
        formatter:editPutInStorage_slformat,
        visible: true
    }, {
        field: 'zsh',
        title: '追溯号*',
        titleTooltip:'追溯号',
        width: '5%',
        align: 'left',
        formatter:zshformat,
        visible: true,
    }, {
        field: 'scph',
        title: '生产批号',
        titleTooltip:'生产批号',
        width: '5%',
        align: 'left',
        formatter:scphformat,
        visible: true,
    }, {
        field: 'cpzch',
        title: '注册号',
        titleTooltip:'注册号',
        width: '5%',
        align: 'left',
        formatter:cpzchformat,
        visible: true,
    }, {
        field: 'scrq',
        title: '生产日期*',
        titleTooltip:'生产日期',
        width: '8%',
        align: 'left',
        formatter:scrqformat,
        visible: true
    }, {
        field: 'yxq',
        title: '失效日期*',
        titleTooltip:'生产日期',
        width: '8%',
        align: 'left',
        formatter:yxqformat,
        visible: true
    },{
        field: 'kwbh',
        title: '库位编号',
        titleTooltip:'库位编号',
        width: '6%',
        align: 'left',
        formatter:editPutInStorage_kwformat,
        visible: true
    }, {
        field: 'rkbz',
        title: '入库备注',
        titleTooltip:'入库备注',
        width: '12%',
        align: 'left',
        formatter:bzformat,
        visible: true
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '4%',
        align: 'left',
        formatter:czformat,
        visible: true
    }
];
// 显示字段
var columnsArray_h = [
	{
    	title: '序',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序',
		width: '2%',
        align: 'left',
        visible:true
    }, {
        field: 'hwid',
        title: '货物id',
        titleTooltip:'货物id',
        width: '4%',
        align: 'left',
        visible: false
    }, {
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'wlmc',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'rklbmc',
        title: '入库类别',
        titleTooltip:'入库类别',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'gg',
        title: '规格',
        titleTooltip:'规格',
        width: '8%',
        align: 'left',
        visible: true
    }, {
        field: 'jldw',
        title: '单位',
        titleTooltip:'单位',
        width: '3%',
        align: 'left',
        visible: true
    }, {
        field: 'lbbj',
        title: '类别标记',
        titleTooltip:'类别标记',
        width: '4%',
        align: 'left',
        visible: false
    },{
        field: 'lbcsmc',
        title: '物料类别',
        titleTooltip:'物料类别',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'dhdh',
        title: '到货单号',
        titleTooltip:'到货单号',
        width: '7%',
        align: 'left',
        visible: true
    }, {
        field: 'zsh',
        title: '追溯号',
        titleTooltip:'追溯号',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'scph',
        title: '批号',
        titleTooltip:'生产批号',
        width: '6%',
        align: 'left',
        visible: true
    }, {
        field: 'cpzch',
        title: '注册号',
        titleTooltip:'注册号',
        width: '5%',
        align: 'left',
        formatter:cpzchformat,
        visible: true,
    },{
        field: 'sl',
        title: '数量',
        titleTooltip:'数量',
        width: '4%',
        align: 'left',
        // formatter:editPutInStorage_slformat,
        visible: bs!='1'?true:false,
    },{
        field: 'dclsl',
        title: '数量',
        titleTooltip:'数量',
        width: '4%',
        align: 'left',
        visible: bs=='1'?true:false,
    }, {
        field: 'htnbbh',
        title: '合同号',
        titleTooltip:'合同号',
        width: '7%',
        align: 'left',
        visible: false
    }, {
        field: 'ckid',
        title: '仓库id',
        titleTooltip:'所在仓库id',
        width: '4%',
        align: 'left',
        visible: false
    },{
        field: 'dhckmc',
        title: '到货仓库',
        titleTooltip:'所在仓库',
        width: '4%',
        align: 'left',
        visible: true
    },{
        field: 'kwbh',
        title: '库位编号',
        titleTooltip:'库位编号',
        width: '4%',
        align: 'left',
        formatter:editPutInStorage_kwformat,
        visible: true
    },{
        field: 'rkbz',
        title: '入库备注',
        titleTooltip:'入库备注',
        width: '12%',
        align: 'left',
        formatter:bzformat,
        visible: true
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '4%',
        align: 'left',
        formatter:czformat,
        visible: true
    }];
var putInStorageEdit_TableInit = function () {
	var ckid=$("#editPutInStorageForm #ckid").val();
	$.ajax({ 
		async: false,
	    type:'post',  
	    url:$('#editPutInStorageForm #urlPrefix').val()+"/storehouse/arrivalGoods/pagedataGetkwlist", 
	    cache: false,
	    data: {"ckid":ckid,"access_token":$("#ac_tk").val()},
	    dataType:'json', 
	    success:function(data){
	    	if(data.kwlist!=null && data.kwlist.length>0){
	    		kwlist=data.kwlist;
	    	}
	    }
	});
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#editPutInStorageForm #tb_list').bootstrapTable({
            url: $('#editPutInStorageForm #urlPrefix').val()+$('#editPutInStorageForm #url').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#editPutInStorageForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "rkrq",				//排序字段
            sortOrder: "desc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "hwid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray_h,
            onLoadSuccess: function (map) {
            	t_map = map;
            	if(t_map.rows){
            		// 初始化hwxx_json
            		var json = [];
            		var ck="";
            		var lbbj="";
            		//0 类别标记相同，1:类别标记不同
            		var result_lb="0";
            		//0 仓库相同，1:仓库不同
            		var result_ck="0";
            		ck=t_map.rows[0].ckid;
            		lbbj=t_map.rows[0].lbbj;
            		//判断供应商，部门是否未空,为空自动生成
            		if($("#editPutInStorageForm #bm").val()==null || $("#editPutInStorageForm #bm").val()==""){
            			$("#editPutInStorageForm #bm").val(t_map.rows[0].sqbm);
            			$("#editPutInStorageForm #sqbmmc").val(t_map.rows[0].sqbmmc);
            		}
            		if($("#editPutInStorageForm #gysid").val()==null || $("#editPutInStorageForm #gysid").val()==""){
            			$("#editPutInStorageForm #gysid").val(t_map.rows[0].gysid);
            			$("#editPutInStorageForm #gysmc").val(t_map.rows[0].gysmc);
            		}
                    var ybz = $("#editPutInStorageForm #bz").val();
                    var dhids = [];
            		for (var i = 0; i < t_map.rows.length; i++) {
            			var sz = {"hwid":'',"rkbz":'',"kwbh":''};
						sz.hwid = t_map.rows[i].hwid;
						sz.rkbz = t_map.rows[i].rkbz;
						sz.kwbh = t_map.rows[i].kwbh;
						json.push(sz);
						if(lbbj!=t_map.rows[i].lbbj){
							result_lb="1";
						}else{
							if(lbbj!="1" && ck!=t_map.rows[i].ckid){
								result_ck="1";
							}
						}
                        if (dhids.indexOf(t_map.rows[i].dhid)==-1){
                            dhids.push(t_map.rows[i].dhid);
                            if (t_map.rows[i].dhzbz!=null){
                                if (ybz!=''){
                                    ybz = ybz + "//"+t_map.rows[i].dhdh+":"+t_map.rows[i].dhzbz;
                                }else {
                                    ybz = t_map.rows[i].dhdh+":"+t_map.rows[i].dhzbz;
                                }
                            }
                        }
    				}
                    $("#editPutInStorageForm #bz").val(ybz);
            		$("#editPutInStorageForm #hwxx_json").val(JSON.stringify(json));
            		if(result_lb=="1"){
            			$.alert("入库物料需要区分ABC,不允许ABC类物料和类别为无的物料一起入库！");
            		}
            		if(result_ck=="1"){
            			$.alert("入库物料的仓库需要一致！");
            		}
            	}
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	return;
            },
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function(params){
    	//请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
    	//例如 toolbar 中的参数 
    	//如果 queryParamsType = ‘limit’ ,返回参数必须包含 
    	//limit, offset, search, sort, order 
    	//否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder. 
    	//返回false将会终止请求
        var hwxxids =  $('#editPutInStorageForm #hwxxids').val();
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: 1,   //页面大小
        	pageNumber: 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "hwid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            rkid: $("#editPutInStorageForm #rkid").val(),
            ids: hwxxids,
            //搜索框使用
            //search:params.search
        };
    	return map;
    };
    return oTableInit;
};
/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
    var formAction=$('#editPutInStorageForm #formAction').val()
    let html = "";
    if (formAction != "advancedModSavePutInStorage"){
        html = "<span style='margin-left:5px;height:24px !important;' class='btn btn-default' title='删除货物信息' onclick=\"delHwDetail('" + index + "',event)\" >删除</span>";
    }else{
        html = "-";
    }
    return html;
}
/**
 * 产品注册号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function cpzchformat(value,row,index){
    if(row.cpzch == null){
        row.cpzch = "" ;
    }
    return row.cpzch;
}
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delHwDetail(index,event){
	var preJson = JSON.parse($("#editPutInStorageForm #hwxx_json").val());	
	var hwid = "," + preJson[index].hwid;
	var hwids = $("#editPutInStorageForm #hwids").val();
	$("#editPutInStorageForm #hwids").val(hwids + hwid);
	preJson.splice(index,1);
	t_map.rows.splice(index,1);
	$("#editPutInStorageForm #tb_list").bootstrapTable('load',t_map);
	$("#editPutInStorageForm #hwxx_json").val(JSON.stringify(preJson));
}

/**
 * 备注格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function bzformat(value,row,index){
	if(row.rkbz == null){
		row.rkbz = row.dhbz ;
        if(row.rkbz == null){
            row.rkbz = "" ;
        }
	}
	var html="<input id='rkbz_"+index+"' name='rkbz_"+index+"' value='"+row.rkbz+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'rkbz\')\"></input>";
	return html;
}

/**
 * 数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function editPutInStorage_slformat(value,row,index){
	var html = "";
	if(rklbbj == "1"){
		if(row.sl==null){
			row.sl="";
		}
		html="<input id='sl_"+index+"' name='sl_"+index+"' value='"+row.sl+"' validate='{rkslNumber:true}' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'sl\')\"></input>";
	}else{
		html="<span>"+row.sl+"</span>"
	}
	return html;
}

/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("rkslNumber", function (value, element){
	if(!value){
		$.error("请填写数量!");
		return false;
	}else{
		if(!/^\d+(\.\d{1,2})?$/.test(value)){
			$.error("请填写正确数量格式，可保留两位小数!");
		}
	}
    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");

/**
 * 库位编号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function editPutInStorage_kwformat(value,row,index){ 
	var html="";
	// if (bs=="1"){
	//     html="<input id='kw_"+index+"' name='kw_"+index+"' value='"+row.cskwmc+"' readonly=\"readonly\"  style='width:100%;border-radius:5px;border:1px solid #D5D5D5;'></input>";
    // }else{
	html="<select name='kw' onchange=\"editPutInStorage_changekw('"+index+"')\" class='form-control chosen-select' validate='{required:true}' id='kw_"+index+"' style='padding:0px;'" ;
        if (bs=="1" || bs == '2'){
            html +=" disabled=\"disabled\""
        }
        html +=">";
	if(kwlist!=null && kwlist.length>0){
		html+="<option selected='true'>--请选择--</option>";
		for(var i=0;i<kwlist.length;i++){
			if(row.kwbh){
				if(row.kwbh==kwlist[i].ckid){
					html+="<option selected='true' id='"+kwlist[i].ckid+"' value='"+kwlist[i].ckid+"' index='"+i+"'>"+kwlist[i].ckmc+"</option>";
				}else{
					html+="<option id='"+kwlist[i].ckid+"' value='"+kwlist[i].ckid+"' index='"+i+"'>"+kwlist[i].ckmc+"</option>";
				}
			}else if(row.cskw){
                if(row.cskw==kwlist[i].ckid){
                    html+="<option selected='true' id='"+kwlist[i].ckid+"' value='"+kwlist[i].ckid+"' index='"+i+"'>"+kwlist[i].ckmc+"</option>";
                }else{
                    html+="<option id='"+kwlist[i].ckid+"' value='"+kwlist[i].ckid+"' index='"+i+"'>"+kwlist[i].ckmc+"</option>";
                }
                row.kwbh=row.cskw;
            }else{
				html+="<option id='"+kwlist[i].ckid+"' value='"+kwlist[i].ckid+"' index='"+i+"'>"+kwlist[i].ckmc+"</option>";
				row.cskw=kwlist[i].ckid;
			}
		}
	}else{
		html+="";
	}
	html+="</select>";
    // }
	return html;
}
/**
 * 入库类型改变事件
 * @returns
 */
function addcheckrklx(){
    let rklb = $('#editPutInStorageForm #rklb').val();
    let cskz2= $("#editPutInStorageForm #rklb").find("option:selected").attr("cskz2");
    if (cskz2 == "1"){
        $('#editPutInStorageForm #cglx').val("");
        return;
    }
    $.ajax({
        type:'post',
        url:$('#editPutInStorageForm #urlPrefix').val() + "/systemmain/data/ansyGetJcsjList",
        cache: false,
        data: {"fcsid":rklb,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            if(data != null && data.length != 0){
                $('#editPutInStorageForm #cglx').val(data[0].csid);
            }
        }
    });
}
function editPutInStorage_changekw(index){
	var kwid=$("#kw_"+index+"  option:selected").val();
	var kwmc=$("#kw_"+index+"  option:selected").text();
	var data=JSON.parse($("#editPutInStorageForm #hwxx_json").val());
	if(data!=null && data.length>0){
		for(var i=0;i<data.length;i++){
			if(data[i].index=index){
				data[i].kwbh=kwid;
				data[i].kwmc=kwmc;
				t_map.rows[index].kwbh=kwid;
				t_map.rows[index].kwmc=kwmc;
			}
		}
	}
	$("#editPutInStorageForm #hwxx_json").val(JSON.stringify(data));
	//$("#editPutInStorageForm #tb_list").bootstrapTable('load',t_map);
}

/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkSum(index, e, zdmc) {
	if(zdmc=="rkbz"){
		t_map.rows[index].rkbz = e.value;
	}else if(zdmc=="sl"){
		t_map.rows[index].sl = e.value;
	}else if(zdmc=="zsh"){
		t_map.rows[index].zsh = e.value;
	}else if(zdmc=="scrq"){
		t_map.rows[index].scrq = e.value;
	}else if(zdmc=="yxq"){
		t_map.rows[index].yxq = e.value;
	}else{
		t_map.rows[index].scph = e.value;
	}
	if(t_map.rows != null && t_map.rows.length > 0){
		var hwJson = [];
		for (var i = 0; i < t_map.rows.length; i++) {
			var sz={"hwid":'',"rkbz":'',"kwbh":'',"sl":"","rklb":""};
			sz.hwid = t_map.rows[i].hwid;
			sz.rkbz = t_map.rows[i].rkbz;
			sz.kwbh = t_map.rows[i].kwbh;
			sz.sl = t_map.rows[i].sl;
            sz.rklb = t_map.rows[i].rklb;
			hwJson.push(sz);
		}
		$("#editPutInStorageForm #hwxx_json").val(JSON.stringify(hwJson));
	}
}

/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkKw(index, e, zdmc) {
	t_map.rows[index].kwbh = e.value;
	if(t_map.rows != null && t_map.rows.length > 0){
		var hwJson = [];
		for (var i = 0; i < t_map.rows.length; i++) {
			var sz={"hwid":'',"rkbz":'',"kwbh":'',"sl":''};
			sz.hwid = t_map.rows[i].hwid;
			sz.rkbz = t_map.rows[i].rkbz;
			sz.kwbh = t_map.rows[i].kwbh;
			if(rklbbj=="1"){
				sz.sl = $("#sl_"+index).val();
			}else{
				sz.sl = t_map.rows[i].sl;
			}	
			hwJson.push(sz);
		}
		$("#editPutInStorageForm #hwxx_json").val(JSON.stringify(hwJson));
	}
}

/**
 * 追溯号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function zshformat(value,row,index){
	if(row.zsh == null){
		row.zsh = "";
	}
	var html = "<input id='zsh_"+index+"' autocomplete='off' value='"+row.zsh+"' name='zsh_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{zshReq:true,stringMaxLength:32}' onblur=\"checkSum('"+index+"',this,\'zsh\')\"></input>";
	return html;
}

/**追溯号生产日期
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("zshReq", function (value, element){
	if(!value){
		$.error("请填写追溯号!");
		return false;
	}
    return this.optional(element) || value;
},"请填写追溯号。");


/**
 * 生产批号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function scphformat(value,row,index){
	if(row.scph == null){
		row.scph = "";
	}
	var html = "<input id='scph_"+index+"' autocomplete='off' value='"+row.scph+"' name='scph_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{stringMaxLength:32}' onblur=\"checkSum('"+index+"',this,\'scph\')\"></input>";
	return html;
}


/**
 * 生产日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function scrqformat(value,row,index){
	if(row.scrq == null){
		row.scrq = "";
	}
	var html = "<input id='scrq_"+index+"' autocomplete='off' value='"+row.scrq+"' name='scrq_"+index+"' style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' validate='{scrqReq:true}' onchange=\"checkSum('"+index+"',this,\'scrq\')\"><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='复制当前日期并替换所有'  onclick=\"copyTime('"+index+"',\'scrq\')\"><span></input>";
	setTimeout(function() {
		laydate.render({
			elem: '#editPutInStorageForm #scrq_'+index
		  	,theme: '#2381E9'
		  	,max: 0
	  		,done: function(value, date, endDate){
	  			var now = new Date(value);
	  			if(t_map.rows[index].bzqflg=='0'){
	  				var nowmonth=now.getMonth();
	  				var number=parseInt(t_map.rows[index].bzq);
	  				now.setMonth(nowmonth+number);
	  				var nowdate=now.getDate();
	  				now.setDate(nowdate-1);
	  				var finaldate=now.getFullYear()+"-"+(now.getMonth()+1)+"-"+now.getDate();
	  				$("#yxq_"+index).val(finaldate);
	  				t_map.rows[index].yxq=finaldate;
	  			}else{
	  				var nowyear=now.getFullYear();
	  				var number=99;
	  				now.setFullYear(nowyear+number);
	  				var nowdate=now.getDate();
	  				now.setDate(nowdate-1);
	  				var finaldate=now.getFullYear()+"-"+(now.getMonth()+1)+"-"+now.getDate();
	  				$("#yxq_"+index).val(finaldate);
	  				t_map.rows[index].yxq=finaldate;
	  			}
	  			t_map.rows[index].scrq = value;
	  		}
		});
    }, 100);
	return html;
}



/**
 * 验证生产日期
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("scrqReq", function (value, element){
	if(!value){
		$.error("请填写生产日期!");
		return false;
	}
    return this.optional(element) || value;
},"请填写生产日期。");


/**
 * 复制当前日期并替换全部
 * @param wlid
 * @returns
 */
function copyTime(index,zdmc){
	var dqrq=$("#editPutInStorageForm #"+zdmc+"_"+index).val();
	if(dqrq==null || dqrq==""){
		$.confirm("请先选择日期！");
	}else{
		for(var i=0;i<t_map.rows.length;i++){
			$("#editPutInStorageForm #"+zdmc+"_"+i).val(dqrq);
		}
	}
	if(t_map.rows.length>0){
		for(var j=0;j<t_map.rows.length;j++){
			if(zdmc=="scrq"){
				var now = new Date(dqrq);
	  			if(t_map.rows[j].bzqflg=='0'){
	  				var nowmonth=now.getMonth();
	  				var number=parseInt(t_map.rows[j].bzq);
	  				now.setMonth(nowmonth+number);
	  				var nowdate=now.getDate();
	  				now.setDate(nowdate-1);
	  				var finaldate=now.getFullYear()+"-"+(now.getMonth()+1)+"-"+now.getDate();
	  				$("#yxq_"+j).val(finaldate);
	  				t_map.rows[j].yxq=finaldate;
	  			}else{
	  				var nowyear=now.getFullYear();
	  				var number=99;
	  				now.setFullYear(nowyear+number);
	  				var nowdate=now.getDate();
	  				now.setDate(nowdate-1);
	  				var finaldate=now.getFullYear()+"-"+(now.getMonth()+1)+"-"+now.getDate();
	  				$("#yxq_"+j).val(finaldate);
	  				t_map.rows[j].yxq=finaldate;
	  			}
				t_map.rows[j].scrq=dqrq;
			}else if(zdmc=="yxq"){
				t_map.rows[j].yxq=dqrq;
			}
		}
	}
}


/**
 * 有效日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function yxqformat(value,row,index){
	if(row.yxq == null){
		row.yxq = "";
	}
	var html = "<input id='yxq_"+index+"' autocomplete='off' value='"+row.yxq+"' name='yxq_"+index+"'  style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' validate='{yxqReq:true}' onchange=\"checkSum('"+index+"',this,\'yxq\')\"><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='复制当前日期并替换所有'  onclick=\"copyTime('"+index+"',\'yxq\')\"><span></input>";
	setTimeout(function() {
		laydate.render({
			elem: '#editPutInStorageForm #yxq_'+index
		  	,theme: '#2381E9'
	  		,done: function(value, date, endDate){
	  			t_map.rows[index].yxq = value;
	  		}
		});
    }, 100);
	return html;
}



/**
 * 验证生产日期
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("yxqReq", function (value, element){
	if(!value){
		$.error("请填写有效期!");
		return false;
	}
    return this.optional(element) || value;
},"请填写y有效期。");




























/**
 * 初始化页面
 * @returns
 */
function init(){
	//新增显示控制
	var xsbj = $("#editPutInStorageForm #xsbj").val();
	if(xsbj=="1"){
		$("#editPutInStorageForm #addDhd").show();		
	}else{		
		$("#editPutInStorageForm #addDhd").hide();
	}
  	//添加日期控件
	laydate.render({
	   elem: '#rkrq'
	  ,theme: '#2381E9'
	});
}

/**
 * 选择供应商列表
 * @returns
 */
function chooseGys(){
	var url=$('#editPutInStorageForm #urlPrefix').val() + "/warehouse/supplier/pagedataSupplier?access_token=" + $("#ac_tk").val()+"&scbj="+"0";
	$.showDialog(url,'选择供应商',addGysConfig);	
}
var addGysConfig = {
	width		: "1200px",
	modalName	:"addGysModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row=$('#choosesupplier_formSearch #tb_list').bootstrapTable('getSelections');
				if(sel_row.length == 1){
					var gysid = sel_row[0].gysid;
					var gysmc = sel_row[0].gysmc;
					$("#gysmc").val(gysmc);
					$("#gysid").val(gysid);
				}else{
					$.error("请选中一行");
					return false;
				}
			},
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

/**
 * 选择申请部门列表
 * @returns
 */
function chooseSqbm(){
	var url=$('#editPutInStorageForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择申请部门',addSqbmConfig);	
}
var addSqbmConfig = {
	width		: "800px",
	modalName	:"addSqbmModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#editPutInStorageForm #bm').val(sel_row[0].jgid);
					$('#editPutInStorageForm #sqbmmc').val(sel_row[0].jgmc);
	    		}else{
	    			$.error("请选中一行");
	    			return false;
	    		}
			},
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};


/**
 * 选择到货单号(及明细)
 * @returns
 */
function chooseDhxx(){
	var hwids =$("#editPutInStorageForm #hwids").val().substr(1);
	var url = $('#editPutInStorageForm #urlPrefix').val();
	url = url +  "/warehouse/putInStorage/pagedataChooseListHwxx?zt=03&hwids="+ hwids+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择到货单号', chooseDhxxConfig);
}
var chooseDhxxConfig = {
	width : "1600px",
	modalName	: "chooseHwxxModal",
	formName	: "chooseHwxxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				//判断入库类别标记
				// if(rklbbj!=null && rklbbj=="0"){
					// 判断货物信息是否选中
					if($("#chooseHwxxForm input[name='checkHwxx']:checked").length == 0){
						$.alert("未选中货物信息！");
						return false;
					}
					var hwxxJson = [];

					//获取hwxx_json
					if($("#editPutInStorageForm #hwxx_json").val()){
						hwxxJson = JSON.parse($("#editPutInStorageForm #hwxx_json").val());
					}
					var hwxx_Json = [];
					$("#chooseHwxxForm input[name='checkHwxx']").each(function(){
						if($(this).is(":checked")){
							var sz={"hwid":''};
							sz.hwid = this.dataset.hwid;
							hwxx_Json.push(sz);
						}
					})
					// 获取删除明细
					for (var j = hwxxJson.length-1; j >=0; j--) {
						var isDel = true;
						for (var i = hwxx_Json.length-1; i>=0; i--) {
							if(hwxxJson[j].hwid == hwxx_Json[i].hwid){
								isDel = false;
								break;
							}
						}
						if(!isDel){
							// 根据明细ID直接更新页面列表(删除)
							for (var i = 0; i < t_map.rows.length; i++) {
								if(t_map.rows[i].hwid == hwxxJson[j].hwid){
									hwxxJson.splice(j,1);
									t_map.rows.splice(i,1);
									break;
								}
							}
						}
					}
					//组装查询条件 hwid
					var ids="";
					for (var i = 0; i < hwxx_Json.length; i++) {
						var isAdd = true;
						for (var j = 0; j < hwxxJson.length; j++) {
							if(hwxxJson[j].hwid == hwxx_Json[i].hwid){
								isAdd = false;
								break;
							}
						}
						if(isAdd){
							ids = ids + ","+ hwxx_Json[i].hwid;
						}
					}
					if(ids.length > 0){
						ids = ids.substr(1);
						// 查询货物信息并更新到页面
						$.post($('#editPutInStorageForm #urlPrefix').val() + "/warehouse/putInStorage/pagedataGetListByhwid",{ids:ids,"access_token":$("#ac_tk").val()},function(data){
							var hwxxDtos = data.rows;
                            var ybz = $("#editPutInStorageForm #bz").val();
							if(hwxxDtos != null && hwxxDtos.length > 0){
								// 更新页面列表(新增)
								var gyscfbj = "";
                                var dhids = [];
								for (var i = 0; i < hwxxDtos.length; i++) {
									if(i == 0){
										if(!t_map.rows){
											t_map.rows= [];
										}
									}
									
									t_map.rows.push(hwxxDtos[i]);				
									var sz = {"hwid":''};
									sz.hwid = hwxxDtos[i].hwid;
									hwxxJson.push(sz);
									//判断供应商是否重复
									for (var j = 0; j < hwxxDtos.length; j++) {
										if(hwxxDtos[i].gysid!=hwxxDtos[j].gysid){
											gyscfbj="1";
										}
									}
                                    if (dhids.indexOf(hwxxDtos[i].dhid)==-1){
                                        dhids.push(hwxxDtos[i].dhid);
                                        if (hwxxDtos[i].dhzbz!=null){
                                            if (ybz!=''){
                                                ybz = ybz + "//"+hwxxDtos[i].dhdh+":"+hwxxDtos[i].dhzbz;
                                            }else {
                                                ybz = hwxxDtos[i].dhdh+":"+hwxxDtos[i].dhzbz;
                                            }
                                        }
                                    }
								}
								$("#editPutInStorageForm #gyscfbj").val(gyscfbj);
								$("#editPutInStorageForm #bz").val(ybz);
								$("#editPutInStorageForm #bm").val(hwxxDtos[0].sqbm);
								$("#editPutInStorageForm #sqbmmc").val(hwxxDtos[0].sqbmmc);
								$("#editPutInStorageForm #gysid").val(hwxxDtos[0].gysid);
								$("#editPutInStorageForm #gysmc").val(hwxxDtos[0].gysmc);
								$("#editPutInStorageForm #tb_list").bootstrapTable('load',t_map);
								$("#editPutInStorageForm #hwxx_json").val(JSON.stringify(hwxxJson));
								$.closeModal(opts.modalName);
							}
	    				},'json');
					}else{
						$("#editPutInStorageForm #tb_list").bootstrapTable('load',t_map);
						$("#editPutInStorageForm #hwxx_json").val(JSON.stringify(hwxxJson));
						$.closeModal(opts.modalName);
					}
				// }else if(rklbbj!=null && rklbbj=="1"){
					// var sel_row = $('#finish_formSearch #tb_list').bootstrapTable('getSelections');// 获取选择行数据
					// var ids="";
	    			// for (var i = 0; i < sel_row.length; i++) {// 循环读取选中行数据
	        		// 	ids= ids + ","+ sel_row[i].wlid;
	        		// }
	    			// if(ids.length > 0){
	    			// 	ids=ids.substr(1);
		    		// 	// 查询货物信息并更新到页面
					// 	$.post($('#editPutInStorageForm #urlPrefix').val() + "/warehouse/putInStorage/getListBywlid",{ids:ids,"access_token":$("#ac_tk").val()},function(data){
					// 		var hwxxJson = [];
					// 		//获取hwxx_json
					// 		if($("#editPutInStorageForm #hwxx_json").val()){
					// 			hwxxJson = JSON.parse($("#editPutInStorageForm #hwxx_json").val());
					// 		}
					// 		var hwxxDtos = data.rows;
					// 		if(hwxxDtos != null && hwxxDtos.length > 0){
					// 			// 更新页面列表(新增)
					// 			for (var i = 0; i < hwxxDtos.length; i++) {
					// 				if(i == 0){
					// 					if(!t_map.rows){
					// 						t_map.rows= [];
					// 					}
					// 				}
					// 				t_map.rows.push(hwxxDtos[i]);
					// 				var sz = {"hwid":''};
					// 				sz.hwid = hwxxDtos[i].hwid;
					// 				hwxxJson.push(sz);
					// 			}
					// 				$("#editPutInStorageForm #tb_list").bootstrapTable('load',t_map);
					// 				$("#editPutInStorageForm #hwxx_json").val(JSON.stringify(hwxxJson));
					// 				$.closeModal(opts.modalName);
					// 		}
		    		// 	},'json');
					// }else{
					// 	$("#editPutInStorageForm #tb_list").bootstrapTable('load',t_map);
					// 	$("#editPutInStorageForm #hwxx_json").val(JSON.stringify(hwxxJson));
					// 	$.closeModal(opts.modalName);
					// }
	    		// }else{
				// 	$.alert("请先选择入库类别！");
				// 	return false;
				// }
	    			
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
/**
 * 刷新入库单号
 * @returns
 */
function refreshRkdh(){
	$.ajax({
	    type:'post',
	    url:$('#editPutInStorageForm #urlPrefix').val() + "/warehouse/putInStorage/pagedataGetPutInStorageCode", 
	    cache: false,
	    data: {"access_token":$("#ac_tk").val()},
	    dataType:'json',
	    success:function(result){
	    	$("#editPutInStorageForm #rkdh").val(result.rkglDto.rkdh);
	    }
	});
}

function editPutInStorage_btnBind(){
	var sel_ck=$("#editPutInStorageForm #ckid");
	//仓库下拉框事件
	sel_ck.unbind("change").change(function(){
		editPutInStorage_ckEvent();
	});
}

//仓库下拉事件
function editPutInStorage_ckEvent(){
	var ckid=$("#editPutInStorageForm #ckid").val();
    let success = true;
    let lbbj = "0";
    $.each(t_map.rows,function (i) {
        if ('1' == t_map.rows[i].lbbj){
            lbbj = "1";
        }
    })
    if (lbbj == "1"){
        $.each(t_map.rows,function (i) {
            success = true;
            if (ckid == t_map.rows[i].dhckid){
                success = false;
                $.confirm("不能与到货时的仓库相同！");
                return false;
            }
        })
    }
    if (success){
        $.ajax({
            type:'post',
            url:$('#editPutInStorageForm #urlPrefix').val()+"/storehouse/arrivalGoods/pagedataGetkwlist",
            cache: false,
            data: {"ckid":ckid,"access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(data){
                if(data.kwlist!=null && data.kwlist.length>0){
                    kwlist=data.kwlist;
                    $("#editPutInStorageForm #tb_list").bootstrapTable('load',t_map);
                }else{
                    $.confirm("该仓库下库位信息为空，请先进行维护！");
                }
            }
        });
    }
}

$(document).ready(function(){
	editPutInStorage_btnBind();
	//初始化列表
	var oTable=new putInStorageEdit_TableInit();
	oTable.Init();
	// 初始化页面
	init();
	//所有下拉框添加choose样式
	jQuery('#editPutInStorageForm .chosen-select').chosen({width: '100%'});
	if(bs=='1' || bs=='2'){
        $("#editPutInStorageForm #rk").attr("style","pointer-events: none;");
        $("#editPutInStorageForm #ck").attr("style","pointer-events: none;");
    }
});