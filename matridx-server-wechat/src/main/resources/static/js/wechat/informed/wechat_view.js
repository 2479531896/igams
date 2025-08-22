var species_turnOff=true;

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
				valign:'middle',
				align:'center',
				visible: true
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
			}, {
				field: 'dsbfb',//合并Ratio和Coverage
				title: 'Ratio /</br>Coverage/</br>Attention',
				titleTooltip: 'Ratio/Coverage/Attention',
				width: '10%',
				valign:'middle',
				align:'center',
				formatter: ratioformat,
				visible: true
			}],
		    //最主要的就是下面  定义哪一列作为展开项  定义父级标志 这里是fjdid
	        //定义的列一定是要在表格中展现的  换言之就是上方有这个列 不然报错
            treeShowField: 'fldj',//在哪一列展开树形
            parentIdField: 'fjdid',
			onLoadSuccess: function (row) {
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

function view(fjid, sign){
	$("#ajaxForm #fjid").val(fjid);
	$("#ajaxForm #sign").val(sign);
	document.getElementById("ajaxForm").submit();
}
function share(fjid){
	alert("点击分享按钮--fjid:"+fjid);
	$.ajax({
		url: '/wechat/getJsApiInfo',
		type: 'post',
		data: {
			"url":location.href.split('#')[0],
			"wbcxdm":$("#wbcxdm").val()
		},
		dataType: 'json',
		success: function(result) {
			alert(result);
			//注册信息
			wx.config({
				debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
				appId: result.appid, // 必填，公众号的唯一标识
				timestamp: result.timestamp, // 必填，生成签名的时间戳
				nonceStr: result.noncestr, // 必填，生成签名的随机串
				signature: result.sign, // 必填，签名，见附录1
				jsApiList: ['checkJsApi','updateAppMessageShareData','updateTimelineShareData']
				// 必填，需要使用的JS接口列表，所有JS接口列表见附录2
			});
			wx.error(function(res) {
				alert("error");
			});
			//config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作
			alert("准备检测-------applicationurl："+$("#applicationurl").val());
			wx.ready(function() {
				wx.checkJsApi({
					jsApiList: ['updateAppMessageShareData','updateTimelineShareData'],
					success: function(res) {
						alert("检测方法成功--applicationurl："+$("#applicationurl").val());
						//分享到微信、QQ好友
						wx.updateAppMessageShareData({ 
					        title: '送检报告', // 分享标题
					        desc: '描述', // 分享描述
					        link: $("#applicationurl").val()+'/wechat/file/loadFile?fjid='+fjid, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
					        imgUrl: '/images/wx_logo.png', // 分享图标
					        success: function () {
					        	// 设置成功
					        	alert("分享成功");
					        }
					    });
					}
				});
			});
		}
	});
}
function yl(fjid,wjm,sign){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" ||type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif" || type.toLowerCase()==".png"){
		var url="/wechat/wechatpreview?fjid="+fjid+"&sign="+encodeURIComponent(sign);
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else{
		$.alert("仅支持图片预览");
		
	}
}
var JPGMaterConfig = {
		offAtOnce	: true,  
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

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
			var url="/wechat/pagedataGenussjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx;

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
			var url="/wechat/pagedataGenussjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx;
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
			var url="/wechat/pagedataGenussjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx;
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
			var url="/wechat/pagedataGenussjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx;
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
		var url="/wechat/getspeciessjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx;
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
		var url="/wechat/getspeciessjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx;
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
		var url="/wechat/getspeciessjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx;
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
		var url="/wechat/getspeciessjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx;
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
		var url="/wechat/pagedataGenussjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx;
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
		var url="/wechat/pagedataGenussjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx;
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
		var url="/wechat/pagedataGenussjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx;
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
		var url="/wechat/pagedataGenussjxxjg?sjid="+sjid+"&wzfl="+wzfl+"&jclx="+jclx;
		$("#Parasite_tb_list"+jclx).bootstrapTable('destroy');
		var oTable = new species_TableInit();
		oTable.Init(wzfl,tableid,url);
		$("#clickParasite"+jclx).addClass("getgenusdata");
	}
}

function initSfqx(){
	var sfqx = $("#ajaxForm #sfqx").val();
	if(sfqx != "1"){
		$(".sfqx").remove();
	}
}

$(document).ready(function(){
	//初始化收费显示
	initSfqx();
	
	toaddbr();
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
	//查看是否为空的div
	//ckeckhtml();
});
