var t_map = [];
var t_index="";
//取样标记
var qybj = $("#borrowForm #qybj").val();
// 显示字段
var columnsArray = [
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
        field: 'wlid',
        title: '物料id',
        titleTooltip:'物料id',
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
    }, {
		field: 'ckqxmc',
		title: '仓库分类',
		titleTooltip:'仓库分类',
		width: '4%',
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
        field: 'kcl',
        title: '库存数',
        titleTooltip:'库存数',
        width: '5%',
        align: 'left',
        visible: true
    }, {
        field: 'jcsl',
        title: '借出数量',
        titleTooltip:'借出数量',
        width: '6%',
        align: 'left',
        formatter:borrowForm_jcslformat,
        visible: true
	},{
		field: 'yjghrq',
		title: '预计归还日期',
		titleTooltip:'预计归还日期',
		width: '6%',
		align: 'left',
		formatter:yjghrqformat,
		visible: true
    }, {
		field: 'cplx',
		title: '产品类型',
		titleTooltip:'产品类型',
		width: '6%',
		align: 'left',
		formatter:borrowForm_cplxformat,
		visible: true
	},
	{
		field: 'bz',
		title: '备注',
		titleTooltip:'备注',
		width: '6%',
		align: 'left',
		formatter:borrowForm_bzformat,
		visible: true
	},
	{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '4%',
        align: 'left',
        formatter:borrowForm_czformat,
        visible: true
    }];
var borrow_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#borrowForm #jcjygl_list').bootstrapTable({
            url: $('#borrowForm #urlPrefix').val()+$('#borrowForm #url').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#borrowForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "wlmc",					//排序字段
            sortOrder: "asc",                   //排序方式
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
            uniqueId: "ckhwid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
            	t_map = map;
            	if(t_map.length>0){
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
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: 1,   //页面大小
        	pageNumber: 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "wlid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            jcjyid: $("#borrowForm #jcjyid").val(),
            //搜索框使用
            //search:params.search
        };
    	return map;
    };
    return oTableInit;
};
/**
 * 预计归还日期
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("yjghrqReq", function (value, element){
	if(!value){
		return false;
	}
	return this.optional(element) || value;
},"请填写计划到货日期。");

/**
 * 复制当前日期并替换全部
 * @param wlid
 * @returns
 */
function copyTime(index,zdmc) {
	var yjghrq = $("#borrowForm #" + zdmc + "_" + index).val();
	if (yjghrq == null || yjghrq == "") {
		$.confirm("请先选择日期！");
	} else {
		for (var i = 0; i < t_map.rows.length; i++) {
			$("#borrowForm #" + zdmc + "_" + i).val(yjghrq);
			t_map.rows[i].yjghrq = yjghrq;
		}
	}
}
/**
 * 预计归还日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function yjghrqformat(value,row,index){
	if(row.yjghrq == null){
		row.yjghrq = "";
	}
	var html = "<input id='yjghrq_"+index+"' value='"+row.yjghrq+"' name='yjghrq_"+index+"' style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' validate='{yjghrqReq:true}' onchange=\"checkSum('"+index+"',this,\'yjghrq\')\"><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='复制当前日期并替换所有'  onclick=\"copyTime('"+index+"',\'yjghrq\')\"><span></input>";
	setTimeout(function() {
		laydate.render({
			elem: '#borrowForm #yjghrq_'+index
			,theme: '#2381E9'
			,min: 1
			,btns: ['clear', 'confirm']
			,done: function(value, date, endDate){
				t_map.rows[index].yjghrq = value;
			}
		});
	}, 100);
	return html;
}
/**
 * 请领数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function borrowForm_jcslformat(value,row,index){
	var hhbj = $("#borrowForm #hhbj").val();
	var kqls="";
	if(row.kqls!=null){
		kqls=row.kqls;	
	}
	if(row.klsl!=null){
		kqls=row.klsl;
	}
	var html="";
	if (hhbj!="1"){
		if (row.jcsl !=null){
			kqls = row.jcsl*1 + kqls*1;
		}
	}
	html ="<div id='jcsldiv_"+index+"' name='jcsldiv' isBeyond='false' style='background:darkcyan;'>";
	html += "<input id='jcsl_"+index+"' type='number' autocomplete='off' value='"+(row.jcsl==null?"0":row.jcsl)+"' name='jcsl_"+index+"'  min='0'  max='"+kqls+"' style='width:55%;border:1px solid #D5D5D5;'";
	html += "onblur=\"checkSum('"+index+"',this,\'jcsl\','"+kqls+"')\" ";
	html += "></input>";
	html += "<span id='kqls_"+index+"' style='font-size:13px;width:45%;margin-left:3px;'>/"+kqls+"</span>";
	html += "</div>";
	return html;
}


/**
 * borrowForm_cplxformat
 * @param value
 * @param row
 * @param index
 * @returns
 */
function borrowForm_cplxformat(value,row,index){
	let cplxlist = JSON.parse($("#borrowForm #cplxlist").val())

	var html="";
	html +="<div id='cplxdiv_"+index+"' name='cplxdiv' style='width:85%;display:inline-block'>";
	html+="<select name='cplx' onchange=\"changecplx('"+index+"')\" class='form-control chosen-select' validate='{required:true}' id='cplx_"+index+"' style='padding:0px;width:85%'" ;
	html +=">";
	if(cplxlist!=null&&cplxlist.length>0){
		html+="<option selected='true'>--请选择--</option>";
		for(var i=0;i<cplxlist.length;i++){
			if(row.cplx){
				if(row.cplx==cplxlist[i].csid){
					html+="<option selected='true' id='"+cplxlist[i].csid+"' value='"+cplxlist[i].csid+"' index='"+i+"'>"+cplxlist[i].csmc+"</option>";
				}else{
					html+="<option id='"+cplxlist[i].csid+"' value='"+cplxlist[i].csid+"' index='"+i+"'>"+cplxlist[i].csmc+"</option>";
				}
			}
			else{
				html+="<option id='"+cplxlist[i].csid+"' value='"+cplxlist[i].csid+"' index='"+i+"'>"+cplxlist[i].csmc+"</option>";
				row.csmc=cplxlist[i].csid;
			}
		}

	}
	html+="</select></div>";
	html+="<div style='width:10%;float: right;margin-top: 3px;'><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;cursor:pointer;' title='复制当前产品类型并替换所有'  onclick=\"copyCplx('"+index+"')\" autocomplete='off'><span></div>"
	return html;
}
function changecplx(index){
	var cplx=$("#cplx_"+index+"  option:selected").val();
	for (var i = 0; i < t_map.rows.length; i++) {
			t_map.rows[i].cplx=cplx;
	}
}
/**
 * 复制当前产品类型并替换全部
 * @param wlid
 * @returns
 */
function copyCplx(index){
	var cplx=$("#borrowForm #cplx_"+index).val();
	if(cplx==null || cplx==""){
		$.confirm("请先选择产品类型！");
	}else{
		for(var i=0;i<t_map.rows.length;i++){
			$("#borrowForm #cplx_"+i).val(cplx);
		}
	}
	var data=JSON.parse($("#borrowForm #jymx_json").val());
	if(data!=null && data.length>0){
		for(var j=0;j<data.length;j++){
			data[j].cplx=cplx;
			t_map.rows[j].cplx=cplx;
		}
	}
	$("#borrowForm #jymx_json").val(JSON.stringify(data));
}
/**
 * 备注格式化
 */
function borrowForm_bzformat(value,row,index){
	var html="";
	if (!row.hwllxxbz){
		row.hwllxxbz="";
	}
	if (row.bz){
		row.hwllxxbz=row.bz;
	}
	html ="<div id='bzdiv_"+index+"' name='bzdiv'>";
	html += "<input id='bz_"+index+"' value='"+row.hwllxxbz+"' name='bz_"+index+"' onblur=\"checkBz('"+index+"',this,\'bz\')\"  validate='{stringMaxLength:64}'></input>";
	html += "</div>";
	return html;
}

/**
 * 刷新借用单号
 */

function sxjydh(){
	$.ajax({
		type:'post',
		url: $('#borrowForm #urlPrefix').val()+'/borrowing/borrowing/sxjydhOA',
		cache: false,
		data: {"access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			if(data != null && data.length != 0){
				if (data.jydh){
					$("#borrowForm #jydh").val(data.jydh);
				}
			}
		}
	});
}


/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function borrowForm_czformat(value,row,index){
	var html = "<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除物料信息' onclick=\"delWlDetail('" + index + "',event)\" >删除</span>";
	return html;
}
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delWlDetail(index,event){
	t_map.rows.splice(index,1);
	$("#borrowForm #jcjygl_list").bootstrapTable('load',t_map);
}

/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkSum(index, e, zdmc,kqls) {
	//当前物料可请领数
	if (zdmc == "yjghrq"){
		t_map.rows[index].yjghrq = e.value;
	}else if(zdmc == "jcsl"){
		if (e.value*1 >kqls*1){
			$.error("借出数量不能大于可借数量");
			$("#borrowForm #jcsl_"+index).val("0")
		}else if(e.value*1 < 0){
			$.error("借出数量不能小于0");
			$("#borrowForm #jcsl_"+index).val("0")
		}else{
			t_map.rows[index].jcsl = e.value
		}
	}
}
/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkBz(index, e, zdmc) {
	t_map.rows[index].hwllxxbz = e.value;
}
/**
 * 选择营销合同列表
 * @returns
 */
function chooseHtbh(){
	var url=$('#borrowForm #urlPrefix').val() + "/marketingContract/marketingContract/pagedataChooseMarketingContract?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择营销合同',addHtbhConfig);
}
var addHtbhConfig = {
	width		: "1000px",
	modalName	:"addHtbhModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#chooseMarketingContractForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#borrowForm #htid').val(sel_row[0].htid);
					$('#borrowForm #htbh').val(sel_row[0].htbh);
					$('#borrowForm #ywymc').val(sel_row[0].ssywymc);
					$('#borrowForm #ywy').val(sel_row[0].ssywy);
					$('#borrowForm #zd').val(sel_row[0].zd);
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
 * 初始化页面
 * @returns
 */
function init(){
  	//添加日期控件
	laydate.render({
	   elem: '#jyrq'
	  ,theme: '#2381E9'
	});
	
	var json=[];
	var data=$('#borrowForm #jcjygl_list').bootstrapTable('getData');//获取选择行数据
}


/**
 * 更改单位类别
 * @returns
 */
function choseDwlx(){
	$('#borrowForm #dwdm').val("");
	$('#borrowForm #dwid').val("");
	$('#borrowForm #dwmc').val("");
}
/**
 * 选择申请部门列表
 * @returns
 */
function choosedw(){
	var cskz1 =$('#borrowForm #dwlx').find("option:selected").attr("cskz1");
	if(cskz1){
		if (cskz1 == "KH"){
			var url=$('#borrowForm #urlPrefix').val() + "/systemmain/client/pagedataListClient?access_token=" + $("#ac_tk").val();
			$.showDialog(url,'选择客户',addKhConfig);
		}else if(cskz1 == "GYS"){
			var url=$('#borrowForm #urlPrefix').val() + "/warehouse/supplier/pagedataSupplier?access_token=" + $("#ac_tk").val();
			$.showDialog(url,'选择供应商',addGysConfig);

		}else if(cskz1 == "BM"){
			var url=$('#borrowForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
			$.showDialog(url,'选择部门',addDwConfig);
		}
	}else{
		$.alert("请先选择单位类别！");
		return false;
	}

}
var addKhConfig = {
	width		: "1200px",
	modalName	:"addKhModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row=$('#client_list_ajaxForm #tb_list').bootstrapTable('getSelections');
				if(sel_row.length == 1){
					$('#borrowForm #dwid').val(sel_row[0].khid);
					$('#borrowForm #dwmc').val(sel_row[0].khjc);
					$('#borrowForm #dwdm').val(sel_row[0].khdm);
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
					$('#borrowForm #dwid').val(sel_row[0].gysid);
					$('#borrowForm #dwmc').val(sel_row[0].gysmc);
					$('#borrowForm #dwdm').val(sel_row[0].gysdm);
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
var addDwConfig = {
	width		: "800px",
	modalName	:"addDwModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					if (sel_row[0].jgdm){
						$('#borrowForm #dwdm').val(sel_row[0].jgdm);
					}else{
						$('#borrowForm #dwdm').val("");
						$.alert("U8不存在此部门！");
						return false;
					}
					$('#borrowForm #dwid').val(sel_row[0].jgid);
					$('#borrowForm #dwmc').val(sel_row[0].jgmc);
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
	var url=$('#borrowForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择部门',addSqbmConfig);
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
					if (sel_row[0].jgdm){
						$('#borrowForm #bmdm').val(sel_row[0].jgdm);
					}else{
						$('#borrowForm #bmdm').val("");
						$.alert("U8不存在此部门！");
						return false;
					}
					$('#borrowForm #bm').val(sel_row[0].jgid);
					$('#borrowForm #bmmc').val(sel_row[0].jgmc);
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
 * 选择业务员
 * @returns
 */
var lx="";
function selectywy(flag){
	var url=$('#borrowForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择业务员',addYwyConfig);
	lx = flag;
}
var addYwyConfig = {
	width		: "800px",
	modalName	:"addFzrModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					if (lx=='ywy')
					{
						$('#borrowForm #ywy').val(sel_row[0].yhid);
						$('#borrowForm #ywymc').val(sel_row[0].zsxm);
						$('#borrowForm #fzr').val(sel_row[0].yhid);
						$('#borrowForm #fzrmc').val(sel_row[0].zsxm);
					}else if(lx=='fzr'){
						$('#borrowForm #fzr').val(sel_row[0].yhid);
						$('#borrowForm #fzrmc').val(sel_row[0].zsxm);
					}
					//保存操作习惯
					$.ajax({
						type:'post',
						url:$('#borrowForm #urlPrefix').val() + "/common/habit/commonModUserHabit",
						cache: false,
						data: {"ywy":sel_row[0].yhid,"access_token":$("#ac_tk").val()},
						dataType:'json',
						success:function(data){}
					});
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
 * 根据物料名称模糊查询
 */
$('#borrowForm #addnr').typeahead({
    source : function(query, process) {
        return $.ajax({
            url : $('#borrowForm #urlPrefix').val()+'/storehouse/requisition/pagedataQueryWlxx',
            type : 'post',
            data : {
                "entire" : query,
                "access_token" : $("#ac_tk").val()
            },
            dataType : 'json',
            success : function(result) {
                var resultList = result.ckhwxxDtos
                        .map(function(item) {
                            var aItem = {
                                id : item.ckhwid,
                                name : item.wlbm+"-"+item.wlmc+"-"+item.scs+"-"+item.gg+"-"+item.ckmc
                            };
                            return JSON.stringify(aItem);
                        });
                return process(resultList);
            }
        });
    },
    matcher : function(obj) {
        var item = JSON.parse(obj);
        return ~item.name.toLowerCase().indexOf(
                this.query.toLowerCase())
    },
    sorter : function(items) {
        var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
        while (aItem = items.shift()) {
            var item = JSON.parse(aItem);
            if (!item.name.toLowerCase().indexOf(
                    this.query.toLowerCase()))
                beginswith.push(JSON.stringify(item));
            else if (~item.name.indexOf(this.query))
                caseSensitive.push(JSON.stringify(item));
            else
                caseInsensitive.push(JSON.stringify(item));
        }
        return beginswith.concat(caseSensitive,
                caseInsensitive)
    },
    highlighter : function(obj) {
        var item = JSON.parse(obj);
        var query = this.query.replace(
                /[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
        return item.name.replace(new RegExp('(' + query
                + ')', 'ig'), function($1, match) {
            return '<strong>' + match + '</strong>'
        })
    },
    updater : function(obj) {
        var item = JSON.parse(obj);
        $('#borrowForm #addwlid').attr('value', item.id);
        return item.name;
    }
});

/**
 * 回车键添加物料至请购车
 * @param event
 * @returns
 */
var addkeydown = $("#addnr").bind("keydown",function(event){
	var ckhwid=$('#borrowForm #addwlid').val();
	if (event.keyCode == "13") {
		//判断新增输入框是否有内容
		if(!$('#borrowForm #addnr').val()){
			return false;
		}
		setTimeout(function(){ if(ckhwid != null && ckhwid != ''){
			$.ajax({ 
			    type:'post',  
		    		url:$('#borrowForm #urlPrefix').val()+"/storehouse/requisition/pagedataQueryWlxxByWlid", 
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
				    		// var json=JSON.parse($("#borrowForm #llmx_json").val());
				    		if(t_map.rows==null || t_map.rows ==""){
				    			t_map.rows=[];
				    		}
				    		t_map.rows.push(data.ckhwxxDto);
				    		// var sz={"index":'',"ckhwid":'',"wlid":'',"qlsl":'',"xmdl":'',"xmbm":''};
				    		// sz.index=json.length;
				    		// sz.ckhwid=data.ckhwxxDto.ckhwid;
							// sz.wlid=data.ckhwxxDto.wlid;
				    		// sz.qlsl=data.ckhwxxDto.qlsl;
				    		// sz.xmdl=data.ckhwxxDto.xmdl;
				    		// sz.xmbm=data.ckhwxxDto.xmbm;
				    		// json.push(sz);
							let temp = [];
							let pos =0;
							do {
								temp[pos++] = t_map.rows[t_map.rows.length-1];
								t_map.rows = delByTargetByCkhwid(t_map.rows,t_map.rows[t_map.rows.length-1])
							} while (t_map.rows.length != 0);
							t_map.rows = temp;
				    		// $("#borrowForm #llmx_json").val(JSON.stringify(json));
				    		$("#borrowForm #jcjygl_list").bootstrapTable('load',t_map);
				    		$('#borrowForm #addwlid').val("");
				    		$('#borrowForm #addnr').val("");
			    		}
			    	}else{
			    		$.confirm("该物料不存在!");
			    	}
			    }
			});
		}else{
			var addnr = $('#borrowForm #addnr').val();
			if(addnr != null && addnr != ''){
				$.confirm("请选择物料信息!");
			}
		}}, '200')
	}
})

/**
 * 根据具体元素删除
 */
function delByTargetByCkhwid(arr,target){
	let temp = [];
	let pos =0;
	for (let i = 0; i < arr.length; i++) {
		if (arr[i].ckhwid != target.ckhwid){
			temp[pos++] = arr[i]
		}
	}
	return temp;
}

/**
 * 点击确定添加物料
 * @param 
 * @param 
 * @returns
 */
function borrowing_cofirmaddwl(){
	var ckhwid=$('#borrowForm #addwlid').val();
	//判断新增输入框是否有内容
	if(!$('#borrowForm #addnr').val()){
		return false;
	}
	setTimeout(function(){ if(ckhwid != null && ckhwid != ''){
		$.ajax({ 
		    type:'post',  
	    		url:$('#borrowForm #urlPrefix').val()+"/storehouse/requisition/pagedataQueryWlxxByWlid", 
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
						if(t_map.rows==null || t_map.rows ==""){
							t_map.rows=[];
						}
			    		// var json=JSON.parse($("#borrowForm #llmx_json").val());
			    		t_map.rows.push(data.ckhwxxDto);
			    		// var sz={"index":'',"ckhwid":'',"wlid":'',"qlsl":'',"xmdl":'',"xmbm":''};
			    		// sz.index=json.length;
			    		// sz.ckhwid=data.ckhwxxDto.ckhwid;
						// sz.wlid=data.ckhwxxDto.wlid;
			    		// sz.qlsl=data.ckhwxxDto.qlsl;
			    		// sz.xmdl=data.ckhwxxDto.xmdl;
			    		// sz.xmbm=data.ckhwxxDto.xmbm;
			    		// json.push(sz);
						let temp = [];
						let pos =0;
						do {
							temp[pos++] = t_map.rows[t_map.rows.length-1];
							t_map.rows = delByTargetByCkhwid(t_map.rows,t_map.rows[t_map.rows.length-1])
						} while (t_map.rows.length != 0);
						t_map.rows = temp;
			    		// $("#borrowForm #llmx_json").val(JSON.stringify(json));
			    		$("#borrowForm #jcjygl_list").bootstrapTable('load',t_map);
			    		$('#borrowForm #addwlid').val("");
			    		$('#borrowForm #addnr').val("");
		    		}
		    	}else{
		    		$.confirm("该物料不存在!");
		    	}
		    }
		});
	}else{
		var addnr = $('#borrowForm #addnr').val();
		if(addnr != null && addnr != ''){
			$.confirm("请选择物料信息!");
		}
	}}, '200')
}

$(document).ready(function(){
	//初始化列表
	var oTable=new borrow_TableInit();
	oTable.Init();
	// 初始化页面
	init();	
	// //0.初始化fileinput
	// var oFileInput = new FileInput();
	// var picking_params=[];
	// picking_params.prefix=$("#borrowForm #urlPrefix").val();
	// oFileInput.Init("borrowForm","displayUpInfo",2,1,"pro_file",null,picking_params);
	//所有下拉框添加choose样式
	jQuery('#borrowForm .chosen-select').chosen({width: '100%'});
});