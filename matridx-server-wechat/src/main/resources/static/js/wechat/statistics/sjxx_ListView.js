
var species_TableInit = function () {
	var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function (wzfl,tableid,url) {
		$('#ajaxForm #'+tableid).bootstrapTable({
			url: url,         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#ajaxForm #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   // 是否显示分页（*）
			sortable: true,                     // 是否启用排序
			sortName:"xxjgid",					// 排序字段
			sortOrder: "asc",                   // 排序方式
			queryParams: oTableInit.queryParams,// 传递参数（*）
			sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
			pageNumber:1,                       // 初始化加载第一页，默认第一页
			pageSize: 15,                       // 每页的记录行数（*）
			pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
			paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
			search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: false,
			showColumns: false,                  // 是否显示所有的列
			showRefresh: false,                  // 是否显示刷新按钮
			minimumCountColumns: 2,             // 最少允许的列数
			clickToSelect: false,                // 是否启用点击选中行
			// height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "xxjgid",                     // 每一行的唯一标识，一般为主键列
			showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			isForceTable:true,
			idField: 'jdid',//这里是标志jdid  和 parentIdField有关系
			columns: [{
				field: 'fldj',
				title: 'Rank',
				titleTooltip: 'Rank',
				width: '7%',
				align:'center',
			}, {
				field: 'jdid',
				title: 'TaxID',
				titleTooltip: 'TaxID',
				width: '8%',
				valign:'middle',
				align:'center',
				visible: false
			}, {
				field: 'wzywm',
				title: 'Name',
				titleTooltip: 'Name',
				width: '10%',
				valign:'middle',
				align:'center',
				visible: false
			}, {
				field: 'wzzwm',//合并中文名和英文名
				title: 'CN/EN</br>Name',
				titleTooltip: 'CN/EN Name',
				width: '14%',
				valign:'middle',
				align:'center',
				formatter: nameformat,
				visible: true
			},{
				field: 'dqs',//合并dqs和zdqs
				title: 'Reads</br>Count /</br>Accum',
				titleTooltip: 'Reads Count/Accum',
				width: '8%',
				formatter : readsformat,
				valign:'middle',
				align:'center',
				visible: true,
			},{
				field: 'zdqs',
				title: 'Reads</br>Accum',
				titleTooltip: 'Reads Accum',
				width: '10%',
				visible: false,
				valign:'middle',
				align:'center',
				sortable: true
			}, {
				field: 'dsbfb',//合并Ratio和Coverage
				title: 'Ratio /</br>Coverage/</br>Attention',
				titleTooltip: 'Ratio/Coverage/Attention',
				width: '10%',
				valign:'middle',
				align:'center',
				formatter: ratioformat,
				visible: true
			}, {
				field: 'jyzfgd',
				title: 'Coverage',
				titleTooltip: 'Coverage',
				width: '10%',
				valign:'middle',
				align:'center',
				visible: false
			}, {
				field: 'gzd',
				title: 'Attention',
				titleTooltip: 'Attention',
				width: '10%',
				valign:'middle',
				align:'center',
				visible: false
			}],
			//最主要的就是下面  定义哪一列作为展开项  定义父级标志 这里是fjdid
			//定义的列一定是要在表格中展现的  换言之就是上方有这个列 不然报错
			treeShowField: 'fldj',//在哪一列展开树形
			parentIdField: 'fjdid',
			onLoadSuccess: function (row, $element) {
				//通过替换解决多次打开页面出现true的问题
				var table=$("#"+tableid)[0].innerHTML.replace(RegExp("true", "g"),'')
				$("#"+tableid).empty();
				$("#"+tableid).append(table);
				$('#ajaxForm #'+tableid).treegrid({
					initialState: 'collapsed',// 所有节点都折叠，expanded展开
					treeColumn: 0,//指明第几列数据改为树形
					expanderExpandedClass: 'glyphicon glyphicon-triangle-bottom',//展开和收缩样式
					expanderCollapsedClass: 'glyphicon glyphicon-triangle-right',
					onChange: function() {
						$('#ajaxForm #'+tableid).bootstrapTable('resetWidth');
					}
				});
			},
			onLoadError: function () {
			},
		});
	};
	return oTableInit;
};

function nameformat(value,row,index){
	var wzzwm=row.wzzwm;
	var wzywm=row.wzywm;
	if(wzzwm==null){
		wzzwm="";
	}
	if(wzywm==null){
		wzywm="";
	}
	return row.wzzwm+"</br>"+row.wzywm;
}
function readsformat(value,row,index){
	var dqs=row.dqs;
	var zdqs=row.zdqs;
	if(dqs==null){
		dqs="";
	}
	if(zdqs==null){
		zdqs="";
	}
	return row.dqs+" / "+row.zdqs;
}
function ratioformat(value,row,index){
	var dsbfb=row.dsbfb;
	var jyzfgd=row.jyzfgd;
	var gzd=row.gzd;
	if(dsbfb==null){
		dsbfb="";
	}
	if((jyzfgd!=null || gzd!=null) && dsbfb!=null && dsbfb!=''){
		dsbfb=dsbfb+"</br>";
	}
	if(jyzfgd==null){
		jyzfgd="";
	}
	if(gzd!=null && jyzfgd!=null && jyzfgd!=''){
		jyzfgd=jyzfgd+"</br>"
	}
	return dsbfb+jyzfgd+gzd;
}

//事件绑定
function btnBind() {

}
function initPage(){
}
function view(fjid){
	$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
	var url= "/common/file/pdfPreview?fjid=" + fjid;
	$.showDialog(url,'送检报告',PdfMaterConfig);
}
$("#mzsm").click(function(){
	var url="/inspection/sjxx/pagedataMzsm?access_token="+$("#ac_tk").val();
	$.showDialog(url,'免责声明',MzsmConfig);
})
$(".ckzbjs").click(function(){
	var url = "/inspection/sjxx/getckzbjs?access_token="+$("#ac_tk").val();
	$.showDialog(url,'参考指标解释',CkzbjsConfig);
})
$("#tqxx").click(function(){
	var sjid=$("#ajaxForm #sjid").val();
	$.ajax({
		type:'post',
		url:"/inspection/inspection/pagedataTqxx",
		cache: false,
		data: {"sjid":sjid,"access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			//返回值
			var html="";
			var nbbh;
			var hsnd;
			var cdna;
			//提取信息表
			for(var i=0;i<data.tqxx.length;i++){
				nbbh=data.tqxx[i].nbbh;
				if(nbbh==null){
					nbbh="";
				}
				hsnd=data.tqxx[i].hsnd;
				if(hsnd==null){
					hsnd="";
				}
				cdna=data.tqxx[i].cdna;
				if(cdna==null){
					cdna="";
				}
				html=html+"<tr>"+
					"<td>"+(i+1)+"</td>"+
					"<td title='"+nbbh+"'>"+nbbh+"</td>"+
					"<td title='"+hsnd+"'>"+hsnd+"</td>"+
					"<td title='"+cdna+"'>"+cdna+"</td>"+
					"<td title='"+Math.round(data.tqxx[i].jkyl)+"'>"+Math.round(data.tqxx[i].jkyl)+"</td>"+
					"<td title='"+Math.round(data.tqxx[i].hcyyl)+"'>"+Math.round(data.tqxx[i].hcyyl)+"</td>"+
					"<td title='"+data.tqxx[i].lrsj+"'>"+data.tqxx[i].lrsj+"</td>"+
					"</tr>"
			}
			$("#tq").empty();
			$("#tq").append(html);
			//文库信息表
			var wkhtml="";
			for(var i=0;i<data.wkxx.length;i++){
				nbbh=data.wkxx[i].nbbh;
				if(nbbh==null){
					nbbh="";
				}
				jtxx=data.wkxx[i].jtxx;
				if(jtxx==null){
					jtxx="";
				}
				quantity=data.wkxx[i].quantity;
				if(quantity==null){
					quantity="";
				}
				wkhtml=wkhtml+"<tr>"+
					"<td>"+(i+1)+"</td>"+
					"<td title='"+nbbh+"'>"+nbbh+"</td>"+
					"<td title='"+jtxx+"'>"+jtxx+"</td>"+
					"<td title='"+quantity+"'>"+quantity+"</td>"+
					"<td title='"+data.wkxx[i].lrsj+"'>"+data.wkxx[i].lrsj+"</td>"+
					"</tr>"
			}
			$("#wk").empty();
			$("#wk").append(wkhtml);

			$.each(data.wksjxx,function(i,value){
				xpmc=value.xpmc;
				if(xpmc==null){
					xpmc="";
				}
				cxy=value.cxy;
				if(cxy==null){
					cxy="";
				}
				sjsj=value.sjsj;
				if(sjsj==null){
					sjsj="";
				}
				xjsj=value.xjsj;
				if(xjsj==null){
					xjsj="";
				}
				yjxjsj=value.yjxjsj;
				if(yjxjsj==null){
					yjxjsj="";
				}
				fwq=value.fwq;
				if(fwq==null){
					fwq="";
				}
				sssys=value.sssys;
				if(sssys==null){
					sssys="";
				}
				var ht =
					"<div >" +
					"<div class='text-grey col-md-4 col-sm-12' style='padding: 5px;'>序号：<span>"+(i+1)+"</span></div>" +
					"<div class='text-grey col-md-8 col-sm-12' style='padding: 5px;'>芯片名称：<span>"+xpmc+"</span></div>" +
					"<div class='text-grey col-md-4 col-sm-12' style='padding: 5px;'>测序仪：<span>"+cxy+"</span></div>" +
					"<div class='text-grey col-md-8 col-sm-12' style='padding: 5px;'>上机时间：<span>"+sjsj+"</span></div>" +
					"<div class='text-grey col-md-4 col-sm-12' style='padding: 5px;'>所属实验室：<span>"+sssys+"</span></div>"+
					"<div class='text-grey col-md-8 col-sm-12' style='padding: 5px;'>预计下机时间：<span>"+yjxjsj+"</span></div>" +
					"<div class='text-grey col-md-4 col-sm-12' style='padding: 5px;'>服务器：<span>"+fwq+"</span></div>" +
					"<div class='text-grey col-md-8 col-sm-12' style='padding: 5px;'>下机时间：<span>"+xjsj+"</span></div>" +
					"<div class='text-grey col-md-12 col-sm-12'style='border-bottom:1px solid black;'></div>"+
					"</div>";
				$("#wksj").append(ht);
			});
			/*for(var i=0;i<data.wksjxx.length;i++){
                xpmc=data.wksjxx[i].xpmc;
                if(xpmc==null){
                    xpmc="";
                }
                cxy=data.wksjxx[i].cxy;
                if(cxy==null){
                    cxy="";
                }
                sjsj=data.wksjxx[i].sjsj;
                if(sjsj==null){
                    sjsj="";
                }
                xjsj=data.wksjxx[i].xjsj;
                if(xjsj==null){
                    xjsj="";
                }
                yjxjsj=data.wksjxx[i].yjxjsj;
                if(yjxjsj==null){
                    yjxjsj="";
                }
                fwq=data.wksjxx[i].fwq;
                if(fwq==null){
                    fwq="";
                }
                sssys=data.wksjxx[i].sssys;
                if(sssys==null){
                    sssys="";
                }
                var wksjhtml="";

                wksjhtml = wksjhtml+
                "<div class='cu-form-group'>" +
                    "<div  id='wksj'>" +
                            "<div class='text-grey'>标题</div>" +
                            "<div class='text-grey' style='padding: 5px'>芯片名称：<span th:text='"++"'/></div>" +
                    "</div>" +
                "</div>"

                wksjhtml=wksjhtml+"<tr>"+
                    "<td>"+(i+1)+"</td>"+
                    "<td title='"+xpmc+"'>"+xpmc+"</td>"+
                    "<td title='"+cxy+"'>"+cxy+"</td>"+
                    "<td title='"+sjsj+"'>"+sjsj+"</td>"+
                    "<td title='"+xjsj+"'>"+xjsj+"</td>"+
                    "<td title='"+yjxjsj+"'>"+yjxjsj+"</td>"+
                    "<td title='"+fwq+"'>"+fwq+"</td>"+
                    "<td title='"+sssys+"'>"+sssys+"</td>"+
                    "</tr>";
                $("#wksj").empty();
                $("#wksj").append(wksjhtml);
            }*/
		}
	});
})


function toswxx(){
	var xpxx=$("#ww").val();
	window.open("http://172.17.60.227:8000/core/review/"+xpxx+"/");
}
function xz(fjid){
	jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
		'<input type="text" name="fjid" value="'+fjid+'"/>' +
		'<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
		'</form>')
		.appendTo('body').submit().remove();
}

function xzword(fjid,bgrq){
	jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
		'<input type="text" name="fjid" value="'+fjid+'"/>' +
		'<input type="text" name="bgrq" value="'+bgrq+'"/>' +
		'<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
		'</form>')
		.appendTo('body').submit().remove();
}

function toViewExpress(kdh){
	var url="/express/express/viewExpress?mailno="+kdh+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'物流详情',viewExpressConfig);
}

/*查看详情信息模态框*/
var viewExpressConfig = {
	width		: "800px",
	height		: "1000px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function yl(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url="/ws/sjxxpripreview/?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= "/common/file/pdfPreview?fjid=" + fjid;
		$.showDialog(url,'文件预览',viewPreViewConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}
var PdfMaterConfig = {
	width		: "800px",
	height		: "500px",
	offAtOnce	: true,
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var MzsmConfig = {
//	width		: "800px",
//	height		: "500px",
	offAtOnce	: true,
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
var CkzbjsConfig = {
	width		: "800px",
	height		: "500px",
	offAtOnce	: true,
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
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
var viewPreViewConfig={
	width		: "800px",
	offAtOnce	: true,
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}
function toaddbr(){
	var ggzbsmxx=$("#gg").text();
	var reg = new RegExp("\n","g");
	var ggzbsm=ggzbsmxx.replace(reg,"<br><br>");
	$("#gg").html(ggzbsm);
	var ckwxxx=$("#ckwx").text();
	var ckwx=ckwxxx.replace(reg,"<br>");
	$("#ckwx").html(ckwx);
}

$(function () {
	var t_class=$(".panel-collapse");
	for(var i=0;i<t_class.length;i++){
		$("#"+t_class[i].id).collapse('hide');
	}
});

function clickBacteria(jclx){
	var classname=$("#clickBacteria"+jclx).attr("class");
	var collid=$("#clickBacteria"+jclx).attr("href");
	//判断为收起还是展开
	if(classname.indexOf("open")!=-1){
		$("#clickBacteria"+jclx).removeClass("open");
		$(collid).collapse('hide');
	}else{
		$("#clickBacteria"+jclx).addClass("open");
		//若已经请求一次，则不再请求后台
		if(classname.indexOf("getgenusdata")==-1 && classname.indexOf("getspeciesdata")==-1){
			var sjid=$("#sjid").val();
			var tableid="Bacteria_tb_list"+jclx;
			var jclx=jclx;
			var wzfl="Bacteria";
			var url="/inspection/pagedataGenussjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx+"&access_token="+$("#ac_tk").val();

			$("#Bacteria_tb_list"+jclx).bootstrapTable('destroy');
			var oTable = new species_TableInit();
			oTable.Init(wzfl,tableid,url);
			$("#clickBacteria"+jclx).addClass("getgenusdata");
		}
		$(collid).collapse('show');
	}
}

function clickFungi(jclx){
	var classname=$("#clickFungi"+jclx).attr("class");
	var collid=$("#clickFungi"+jclx).attr("href");
	//判断为收起还是展开
	if(classname.indexOf("open")!=-1){
		$("#clickFungi"+jclx).removeClass("open");
		$(collid).collapse('hide');
	}else{
		$("#clickFungi"+jclx).addClass("open");
		//若已经请求一次，则不再请求后台
		if(classname.indexOf("getgenusdata")==-1 && classname.indexOf("getspeciesdata")==-1){
			var sjid=$("#sjid").val();
			var tableid="Fungi_tb_list"+jclx;
			var jclx=jclx;
			var wzfl="Fungi";
			var url="/inspection/pagedataGenussjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx+"&access_token="+$("#ac_tk").val();
			$("#Fungi_tb_list"+jclx).bootstrapTable('destroy');
			var oTable = new species_TableInit();
			oTable.Init(wzfl,tableid,url);
			$("#clickFungi"+jclx).addClass("getgenusdata");
		}
		$(collid).collapse('show');
	}
}

function clickVirus(jclx){
	var classname=$("#clickVirus"+jclx).attr("class");
	var collid=$("#clickVirus"+jclx).attr("href");
	//判断为收起还是展开
	if(classname.indexOf("open")!=-1){
		$("#clickVirus"+jclx).removeClass("open");
		$(collid).collapse('hide');
	}else{
		$("#clickVirus"+jclx).addClass("open");
		//若已经请求一次，则不再请求后台
		if(classname.indexOf("getgenusdata")==-1 && classname.indexOf("getspeciesdata")==-1){
			var sjid=$("#sjid").val();
			var tableid="Virus_tb_list"+jclx;
			var jclx=jclx;
			var wzfl="Virus";
			var url="/inspection/pagedataGenussjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx+"&access_token="+$("#ac_tk").val();
			$("#Virus_tb_list"+jclx).bootstrapTable('destroy');
			var oTable = new species_TableInit();
			oTable.Init(wzfl,tableid,url);
			$("#clickVirus"+jclx).addClass("getgenusdata");
		}
		$(collid).collapse('show');
	}
}

function clickParasite(jclx){
	var classname=$("#clickParasite"+jclx).attr("class");
	var collid=$("#clickParasite"+jclx).attr("href");
	//判断为收起还是展开
	if(classname.indexOf("open")!=-1){
		$("#clickParasite"+jclx).removeClass("open");
		$(collid).collapse('hide');
	}else{
		$("#clickParasite"+jclx).addClass("open");
		//若已经请求一次，则不再请求后台
		if(classname.indexOf("getgenusdata")==-1 && classname.indexOf("getspeciesdata")==-1){
			var sjid=$("#sjid").val();
			var tableid="Parasite_tb_list"+jclx;
			var jclx=jclx;
			var wzfl="Parasite";
			var url="/inspection/pagedataGenussjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx+"&access_token="+$("#ac_tk").val();
			$("#Parasite_tb_list"+jclx).bootstrapTable('destroy');
			var oTable = new species_TableInit();
			oTable.Init(wzfl,tableid,url);
			$("#clickParasite"+jclx).addClass("getgenusdata");
		}
		$(collid).collapse('show');
	}
}

//点击speciess按钮获取列表
function getBacteriaSpecies(jclx){
	var collid=$("#clickBacteria"+jclx).attr("href");
	$(collid).collapse('show');
	$("#clickBacteria"+jclx).removeClass("getgenusdata");
	var classname=$("#clickBacteria"+jclx).attr("class");
	//判断是否已访问过后台
	if(classname.indexOf("getspeciesdata")==-1){
		var sjid=$("#sjid").val();
		var tableid="Bacteria_tb_list"+jclx;
		var jclx=jclx;
		var wzfl="Bacteria";
		var url="/inspection/getspeciessjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx+"&access_token="+$("#ac_tk").val();
		$("#Bacteria_tb_list"+jclx).bootstrapTable('destroy');
		var oTable = new species_TableInit();
		oTable.Init(wzfl,tableid,url);
		$("#clickBacteria"+jclx).addClass("getspeciesdata");
	}
}
function getFungiSpecies(jclx){
	var collid=$("#clickFungi"+jclx).attr("href");
	$(collid).collapse('show');
	$("#clickFungi"+jclx).removeClass("getgenusdata");
	var classname=$("#clickFungi"+jclx).attr("class");
	//判断是否已访问过后台
	if(classname.indexOf("getspeciesdata")==-1){
		var sjid=$("#sjid").val();
		var tableid="Fungi_tb_list"+jclx;
		var jclx=jclx;
		var wzfl="Fungi";
		var url="/inspection/getspeciessjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx+"&access_token="+$("#ac_tk").val();
		$("#Fungi_tb_list"+jclx).bootstrapTable('destroy');
		var oTable = new species_TableInit();
		oTable.Init(wzfl,tableid,url);
		$("#clickFungi"+jclx).addClass("getspeciesdata");
	}
}
function getVirusSpecies(jclx){
	var collid=$("#clickVirus"+jclx).attr("href");
	$(collid).collapse('show');
	$("#clickVirus"+jclx).removeClass("getgenusdata");
	var classname=$("#clickVirus"+jclx).attr("class");
	//判断是否已访问过后台
	if(classname.indexOf("getspeciesdata")==-1){
		var sjid=$("#sjid").val();
		var tableid="Virus_tb_list"+jclx;
		var jclx=jclx;
		var wzfl="Virus";
		var url="/inspection/getspeciessjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx+"&access_token="+$("#ac_tk").val();
		$("#Virus_tb_list"+jclx).bootstrapTable('destroy');
		var oTable = new species_TableInit();
		oTable.Init(wzfl,tableid,url);
		$("#clickVirus"+jclx).addClass("getspeciesdata");
	}
}
function getParasiteSpecies(jclx){
	var collid=$("#clickParasite"+jclx).attr("href");
	$(collid).collapse('show');
	$("#clickParasite"+jclx).removeClass("getgenusdata");
	var classname=$("#clickParasite"+jclx).attr("class");
	//判断是否已访问过后台
	if(classname.indexOf("getspeciesdata")==-1){
		var sjid=$("#sjid").val();
		var tableid="Parasite_tb_list"+jclx;
		var jclx=jclx;
		var wzfl="Parasite";
		var url="/inspection/getspeciessjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx+"&access_token="+$("#ac_tk").val();
		$("#Parasite_tb_list"+jclx).bootstrapTable('destroy');
		var oTable = new species_TableInit();
		oTable.Init(wzfl,tableid,url);
		$("#clickParasite"+jclx).addClass("getspeciesdata");
	}
}

//点击genus按钮获取列表
function getBacteriaGenus(jclx){
	var collid=$("#clickBacteria"+jclx).attr("href");
	$(collid).collapse('show');
	$("#clickBacteria"+jclx).removeClass("getspeciesdata");
	var classname=$("#clickBacteria"+jclx).attr("class");
	//判断是否已访问过后台
	if(classname.indexOf("getgenusdata")==-1){
		var sjid=$("#sjid").val();
		var tableid="Bacteria_tb_list"+jclx;
		var jclx=jclx;
		var wzfl="Bacteria";
		var url="/inspection/pagedataGenussjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx+"&access_token="+$("#ac_tk").val();
		$("#Bacteria_tb_list"+jclx).bootstrapTable('destroy');
		var oTable = new species_TableInit();
		oTable.Init(wzfl,tableid,url);
		$("#clickBacteria"+jclx).addClass("getgenusdata");
	}
}
function getFungiGenus(jclx){
	var collid=$("#clickFungi"+jclx).attr("href");
	$(collid).collapse('show');
	$("#clickFungi"+jclx).removeClass("getspeciesdata");
	var classname=$("#clickFungi"+jclx).attr("class");
	//判断是否已访问过后台
	if(classname.indexOf("getgenusdata")==-1){
		var sjid=$("#sjid").val();
		var tableid="Fungi_tb_list"+jclx;
		var jclx=jclx;
		var wzfl="Fungi";
		var url="/inspection/pagedataGenussjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx+"&access_token="+$("#ac_tk").val();
		$("#Fungi_tb_list"+jclx).bootstrapTable('destroy');
		var oTable = new species_TableInit();
		oTable.Init(wzfl,tableid,url);
		$("#clickFungi"+jclx).addClass("getgenusdata");
	}
}
function getVirusGenus(jclx){
	var collid=$("#clickVirus"+jclx).attr("href");
	$(collid).collapse('show');
	$("#clickVirus").removeClass("getspeciesdata");
	var classname=$("#clickVirus"+jclx).attr("class");
	//判断是否已访问过后台
	if(classname.indexOf("getgenusdata")==-1){
		var sjid=$("#sjid").val();
		var tableid="Virus_tb_list"+jclx;
		var jclx=jclx;
		var wzfl="Virus";
		var url="/inspection/pagedataGenussjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx+"&access_token="+$("#ac_tk").val();
		$("#Virus_tb_list"+jclx).bootstrapTable('destroy');
		var oTable = new species_TableInit();
		oTable.Init(wzfl,tableid,url);
		$("#clickVirus"+jclx).addClass("getgenusdata");
	}
}
function getParasiteGenus(jclx){
	var collid=$("#clickParasite"+jclx).attr("href");
	$(jclx).collapse('show');
	$("#clickParasite").removeClass("getspeciesdata");
	var classname=$("#clickParasite"+jclx).attr("class");
	//判断是否已访问过后台
	if(classname.indexOf("getgenusdata")==-1){
		var sjid=$("#sjid").val();
		var tableid="Parasite_tb_list"+jclx;
		var jclx=jclx;
		var wzfl="Parasite";
		var url="/inspection/pagedataGenussjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx+"&access_token="+$("#ac_tk").val();
		$("#Parasite_tb_list"+jclx).bootstrapTable('destroy');
		var oTable = new species_TableInit();
		oTable.Init(wzfl,tableid,url);
		$("#clickParasite"+jclx).addClass("getgenusdata");
	}
}

$(document).ready(function(){
	toaddbr();
	//绑定事件
	btnBind();
	//初始化页面数据
	initPage();
	//获取年龄信息 判断是否显示年龄单位
	var nlxx=$("#nl").text();
	if(/^[0-9]+$/.test(nlxx)){
		$("#nldw").show();
	}else{
		$("#nldw").hide();
	}
});