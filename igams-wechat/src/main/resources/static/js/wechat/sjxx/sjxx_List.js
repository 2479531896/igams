var Inspection_turnOff=true;
var sfzjezdqx = 0;
var tkzjezdqx = 0;
var showTableFlag=true
var Inspection_TableInit = function (fieldList,firstFlg) {
    var oTableInit = new Object();
    $('#tab_030101').on('scroll',function(){
    	if($('#sjxx_formSearch #sjxx_list').offset() && $('#sjxx_formSearch #sjxx_list').offset()!= "null"){
			if($('#sjxx_formSearch #sjxx_list').offset().top - 122 < 0){
				if(!$('#sjxx_formSearch .js-affix').hasClass("affix"))
				{
					$('#sjxx_formSearch .js-affix').removeClass("affix-top").addClass("affix");
				}
				$('#sjxx_formSearch .js-affix').css({
					'top': '100px',
					"z-index":1000,
					"width":'100%'
				});
			}else{
				if(!$('#sjxx_formSearch .js-affix').hasClass("affix-top"))
				{
					$('#sjxx_formSearch .js-affix').removeClass("affix").addClass("affix-top");
				}
				$('#sjxx_formSearch .js-affix').css({
					'top': '0px',
					"z-index":1,
					"width":'100%'
				});
			}
		}
    })
    //初始化Table
    oTableInit.Init = function (){
		let tableUrl = "/inspection/inspection/pageGetListInspection";
		if ($('#sjxx_formSearch #single_flag').val()){
			if (tableUrl.indexOf("?")>0){
				tableUrl = tableUrl + "&single_flag="+$("#sjxx_formSearch #single_flag").val()
			} else {
				tableUrl = tableUrl + "?single_flag="+$("#sjxx_formSearch #single_flag").val()
			}
		}
        $('#sjxx_formSearch #sjxx_list').bootstrapTable({
            url: tableUrl,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#sjxx_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "fjsrq desc,sj.sfjs desc,coalesce(sj.dsyrq,sj.syrq) desc,sj.bgrq desc,sj.jsrq desc,sj.lrsj ",//排序字段
            sortOrder:"DESC",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 50,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "sjid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: fieldList,
            onLoadSuccess: function (map) {
                $("#sjxx_formSearch #sfzje").text(map.sfzje)
                $("#sjxx_formSearch #tkzje").text(map.tkzje)
                showTableFlag=true
            },
            onLoadError: function () {
            },
           onDblClickRow: function (row, $element) {
			   if ($("#sjxx_formSearch #btn_viewmore").length>0){
				   InspectionDealById(row.sjid,null,'viewmore',$("#sjxx_formSearch #btn_viewmore").attr("tourl"));
			   }else{
				   InspectionDealById(row.sjid,null,'view',$("#sjxx_formSearch #btn_view").attr("tourl"));
			   }
            },
        });
        $("#sjxx_formSearch #sjxx_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true,
            isFirst:firstFlg}        
        );
    };
	// 得到查询的参数
	oTableInit.queryParams = function(params){
	// 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
	// 例如 toolbar 中的参数
	// 如果 queryParamsType = ‘limit’ ,返回参数必须包含
	// limit, offset, search, sort, order
	// 否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
	// 返回false将会终止请求
    var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
    	pageSize: params.limit,   // 页面大小
    	pageNumber: (params.offset / params.limit) + 1,  // 页码
        access_token:$("#ac_tk").val(),
        sortName: params.sort,      // 排序列名
        sortOrder: params.order, // 排位命令（desc，asc）
        sortLastName: "sj.sjid", // 防止同名排位用
        sortLastOrder: "asc", // 防止同名排位用
		limitColumns: $("#sjxx_formSearch #limitColumns").val(),
        sfzjezdqx: sfzjezdqx,
        tkzjezdqx: tkzjezdqx
    };
    return getSjxxSearchData(map);
	};
    return oTableInit;
}

function getSjxxSearchData(map){
	var cxtj=$("#sjxx_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#sjxx_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["hzxm"]=cxnr
	}else if(cxtj=="1"){
		map["db"]=cxnr
	}else if(cxtj=="2"){
		map["hospitalname"]=cxnr
	}else if(cxtj=="3"){
		map["cskz5"]=cxnr
	}else if(cxtj=="4"){
		map["zyh"]=cxnr
	}else if(cxtj=="5"){
		map["ybbh"]=cxnr
	}else if(cxtj=="6"){
		map["nbbm"]=cxnr
	}else if(cxtj=="7"){
		map["zsxm"]=cxnr
	}else if(cxtj=="8"){
		map["lcfk"]=cxnr
	}else if(cxtj=="9"){
		map["kdh"]=cxnr
	}else if(cxtj=="10"){
		map["sjys"]=cxnr
	}else if(cxtj=="11"){
		map["kyxm"]=cxnr
	}else if(cxtj=="12"){
		map["jcjg"]=cxnr
	}
	var cxtj1=$("#sjxx_formSearch #cxtj1").val();
	var cxnr1=$.trim(jQuery('#sjxx_formSearch #cxnr1').val());
	if(cxtj1=="0"){
		map["hzxm"]=cxnr1
	}else if(cxtj1=="1"){
		map["db"]=cxnr1
	}else if(cxtj1=="2"){
		map["hospitalname"]=cxnr1
	}else if(cxtj1=="3"){
		map["cskz5"]=cxnr1
	}else if(cxtj1=="4"){
		map["zyh"]=cxnr1
	}else if(cxtj1=="5"){
		map["ybbh"]=cxnr1
	}else if(cxtj1=="6"){
		map["nbbm"]=cxnr1
	}else if(cxtj1=="7"){
		map["zsxm"]=cxnr1
	}else if(cxtj1=="8"){
		map["lcfk"]=cxnr1
	}else if(cxtj1=="9"){
		map["kdh"]=cxnr1
	}else if(cxtj1=="10"){
		map["sjys"]=cxnr1
	}else if(cxtj1=="11"){
		map["kyxm"]=cxnr1
	}else if(cxtj1=="12"){
		map["jcjg"]=cxnr1
	}
	var cxtj2=$("#sjxx_formSearch #cxtj2").val();
	var cxnr2=$.trim(jQuery('#sjxx_formSearch #cxnr2').val());
	if(cxtj2=="0"){
		map["hzxm"]=cxnr2
	}else if(cxtj2=="1"){
		map["db"]=cxnr2
	}else if(cxtj2=="2"){
		map["hospitalname"]=cxnr2
	}else if(cxtj2=="3"){
		map["cskz5"]=cxnr2
	}else if(cxtj2=="4"){
		map["zyh"]=cxnr2
	}else if(cxtj2=="5"){
		map["ybbh"]=cxnr2
	}else if(cxtj2=="6"){
		map["nbbm"]=cxnr2
	}else if(cxtj2=="7"){
		map["zsxm"]=cxnr2
	}else if(cxtj2=="8"){
		map["lcfk"]=cxnr2
	}else if(cxtj2=="9"){
		map["kdh"]=cxnr2
	}else if(cxtj2=="10"){
		map["sjys"]=cxnr2
	}else if(cxtj2=="11"){
		map["kyxm"]=cxnr2
	}else if(cxtj2=="12"){
		map["jcjg"]=cxnr2
	}
		// 接收日期开始日期
	var jsrqstart = jQuery('#sjxx_formSearch #jsrqstart').val();
		map["jsrqstart"] = jsrqstart;
	    // 接收日期结束日期
	var jsrqend = jQuery('#sjxx_formSearch #jsrqend').val();
		map["jsrqend"] = jsrqend;
	    // 报告日期开始日期
	var bgrqstart = jQuery('#sjxx_formSearch #bgrqstart').val();
		map["bgrqstart"] = bgrqstart;
	    // 报告日期结束日期
	var bgrqend = jQuery('#sjxx_formSearch #bgrqend').val();
		map["bgrqend"] = bgrqend;
		// 起运日期开始时间
	var qyrqstart = jQuery('#sjxx_formSearch #qyrqstart').val();
		map["qyrqstart"] = qyrqstart;
	    // 起运日期结束时间
	var qyrqend = jQuery('#sjxx_formSearch #qyrqend').val();
		map["qyrqend"] = qyrqend;
		// 运达日期开始时间
	var ydrqstart = jQuery('#sjxx_formSearch #ydrqstart').val();
		map["ydrqstart"] = ydrqstart;
	    // 运达日期结束时间
	var ydrqend = jQuery('#sjxx_formSearch #ydrqend').val();
		map["ydrqend"] = ydrqend;
	// 付款日期开始时间
	var fkrqstart = jQuery('#sjxx_formSearch #fkrqstart').val();
		map["fkrqstart"] = fkrqstart;
	// 付款日期结束时间
	var fkrqend = jQuery('#sjxx_formSearch #fkrqend').val();
		map["fkrqend"] = fkrqend;
	//科室
	var dwids = jQuery('#sjxx_formSearch #sjdw_id_tj').val();
		map["dwids"] = dwids;
	//合作伙伴分类
	var sjhbfls = jQuery('#sjxx_formSearch #sjhbfl_id_tj').val();
		map["sjhbfls"] = sjhbfls;
	//合作伙伴子分类
	var sjhbzfls = jQuery('#sjxx_formSearch #sjhbzfl_id_tj').val();
		map["sjhbzfls"] = sjhbzfls;
	//标本类型
	var yblxs = jQuery('#sjxx_formSearch #yblx_id_tj').val();
		map["yblxs"] = yblxs;	
	//是否合作关系
	var dbm= jQuery('#sjxx_formSearch #dbm_id_tj').val();
		map["dbm"] = dbm;
	//是否付款
	var fkbj=jQuery('#sjxx_formSearch #fkbj_id_tj').val()
		map["fkbj"]=fkbj;
	// //是否收费
	// var sfsf=jQuery('#sjxx_formSearch #sfsf_id_tj').val()
	// 	map["sfsf"]=sfsf;
	//是否上传
	var sfsc=jQuery('#sjxx_formSearch #sfsc_id_tj').val()
	    map["sfsc"]=sfsc;
	//是否RNA检测
	var jcbj=jQuery('#sjxx_formSearch #jcbj_id_tj').val()
	map["jcbj"]=jcbj;
	//是否DNA检测
	var djcbj=jQuery('#sjxx_formSearch #djcbj_id_tj').val()
	map["djcbj"]=djcbj;
	//是否其他检测
	var qtjcbj=jQuery('#sjxx_formSearch #qtjcbj_id_tj').val()
	map["qtjcbj"]=qtjcbj;
	//是否自免检测
	var sfzmjc=jQuery('#sjxx_formSearch #sfzmjc_id_tj').val()
	map["sfzmjc"]=sfzmjc;

	var sfjs=jQuery('#sjxx_formSearch #sfjs_id_tj').val()
		map["sfjs"]=sfjs;
	var sftj=jQuery('#sjxx_formSearch #sftj_id_tj').val()
		map["sftj"]=sftj;
	//扩展参数
	var sjkzcs1=jQuery('#sjxx_formSearch #cskz1_id_tj').val()
		map["sjkzcs1"]=sjkzcs1
	var sjkzcs2=jQuery('#sjxx_formSearch #cskz2_id_tj').val()
		map["sjkzcs2"]=sjkzcs2
	var sjkzcs3=jQuery('#sjxx_formSearch #cskz3_id_tj').val()
		map["sjkzcs3"]=sjkzcs3
	var sjkzcs4=jQuery('#sjxx_formSearch #cskz4_id_tj').val()
		map["sjkzcs4"]=sjkzcs4
	var jcxm=jQuery('#sjxx_formSearch #jcxm_id_tj').val()
		map["jcxms"]=jcxm
    var jczxm=jQuery('#sjxx_formSearch #jczxm_id_tj').val()
        map["jczxms"]=jczxm
	var jyjgs=jQuery('#sjxx_formSearch #jyjg_id_tj').val()
		map["jyjgs"]=jyjgs
	var sfsfs=jQuery('#sjxx_formSearch #sfsf_id_tj').val()
		map["sfsfs"]=sfsfs
	var sfzq=jQuery('#sjxx_formSearch #sfzq_id_tj').val();
		map["sfzq"]=sfzq
		//快递类型
	var kdlxs=$("#sjxx_formSearch #kdlx_id_tj").val();
		map["kdlxs"]=kdlxs
		//盖章类型
	var gzlxs=$("#sjxx_formSearch #gzlx_id_tj").val();
		map["gzlxs"]=gzlxs
		//检测单位
	var jcdws=$("#sjxx_formSearch #jcdw_id_tj").val();
		map["jcdws"]=jcdws
	//科研项目类型
	var kylxs=$("#sjxx_formSearch #kylx_id_tj").val();
	map["kylxs"]=kylxs
	//科研项目
	var kyxms=$("#sjxx_formSearch #kyxm_id_tj").val();
	map["kyxms"]=kyxms
	//送检区分
	var sjqfs=$("#sjxx_formSearch #sjqf_id_tj").val();
	map["sjqfs"]=sjqfs
	//医院重点等级
	var dwzddjs=$("#sjxx_formSearch #dwzddj_id_tj").val().replace("＋","+");
	map["yyzddjs"]=dwzddjs
		// 实验日期开始日期
	var syrqstart = jQuery('#sjxx_formSearch #syrqstart').val();
		map["syrqstart"] = syrqstart;
	    // 实验日期结束日期
	var syrqend = jQuery('#sjxx_formSearch #syrqend').val();
		map["syrqend"] = syrqend;
	if(jQuery('#sjxx_formSearch #syrqflg').is(":checked")){
		map["syrqflg"] = "1";
	}
	//平台归属
	var ptgss=$("#sjxx_formSearch #ptgs_id_tj").val();
	map["ptgss"]=ptgss;
	//销售部门
	var xsbms=$("#sjxx_formSearch #xsbm_id_tj").val();
    map["xsbms"]=xsbms;
	map["jcxmids"] = $("#sjxx_formSearch #jcxmids").val();
	map["jczxmids"] = $("#sjxx_formSearch #jczxmids").val();
	map["jclx"] = $("#sjxx_formSearch #jclx").val();
	 return map;
}

//最终金额格式化
function zzjeformat(value,row,index){
	if(row.sfje != null && row.sfje != '' ){
		if ( row.tkje != null && row.tkje != ''){
			return ((parseInt(row.sfje*100) - parseInt(row.tkje*100))/100).toFixed(2);
		}else {
			return row.sfje;
		}
	}
}
  //性别格式化
function xbformat(value,row,index){
	if(row.xb=='1'){
		return '男';
	}else if(row.xb=='2'){
		return '女';
	}else{
		return '未知';
	}
}
//RNA检测标记格式化
function jcbjformat(value,row,index){
	if(row.jcxmkzcs=="D" || row.jcxmkzcs=="Z" || row.jcxmkzcs=="F"){
		var sfjc="<span style='color:red;'>--</span>";
		return sfjc;
	}else {
		if(row.jcbj=='0'|| row.jcbj==null){
			var sfjc="<span style='color:red;'>否</span>";
			return sfjc;
		}else if(row.jcbj=='1'){
			return '是';
		}
	}
}
//检验结果格式化
function jyjgformat(value,row,index){
	if(row.jyjg=="1"){
		var jyjg="<span style='color:red;font-weight:bold;'>阳性</span>";
		return jyjg;
	}else if(row.jyjg=="2"){
		var jyjg="<span style='color:#0000cc;font-weight:bold;'>疑似</span>";
		return jyjg;
	}else if(row.jyjg=="0"){
		var jyjg="<span style='font-weight:bold;'>阴性</span>";
		return jyjg;
	}
}
//DNA检测标记格式化
function djcbjformat(value,row,index){
	if(row.jcxmkzcs=="R" || row.jcxmkzcs=="Z" || row.jcxmkzcs=="F"){
		var dsfjc="<span style='color:red;'>--</span>";
		return dsfjc;
	}else{
		if(row.djcbj=='0'|| row.djcbj==null){
			var dsfjc="<span style='color:red;'>否</span>";
			return dsfjc;
		}else if(row.djcbj=='1'){
			return '是';
		}
	}
}
//其他检测标记格式化
function qtjcbjformat(value,row,index){
	if(row.jcxmkzcs!="Z" && row.jcxmkzcs!="F"){
		var qtsfjc="<span style='color:red;'>--</span>";
		return qtsfjc;
	}else{
		if(row.qtjcbj=='0'|| row.qtjcbj==null){
			var qtsfjc="<span style='color:red;'>否</span>";
			return qtsfjc;
		}else if(row.qtjcbj=='1'){
			return '是';
		}
	}
}
//是否自免检测标记格式化
function sfzmjcformat(value,row,index){
	if(row.sfzmjc=="1"){
		var sfzmjc="<span>是</span>"
		return sfzmjc;
	}else if(row.sfzmjc=='0'|| row.sfzmjc==null){
		var sfzmjc="<span style='color:red;'>否</span>";
		return sfzmjc;
	}
}
function sfjsformat(value,row,index){
	if(row.sfjs=='0'){
		var sfjs="<span style='color:red;'>否</span>";
		return sfjs;
	}else if(row.sfjs=='1'){
		return '是';
	}else if(row.sfjs=='2'){
		return '其他';
	}
}

function sfvipformat(value,row,index){
	if(row.sfvip=='0'){
		var sfvip="<span style='color:red;'>否</span>";
		return sfvip;
	}else if(row.sfvip=='1'){
		return '是';
	}
}
function sftjformat(value,row,index){
	if(row.sftj=='0'){
		var sftj="<span style='color:red;'>否</span>";
		return sftj;
	}else if(row.sftj=='1'){
		return '是';
	}
}
//付款标记格式化
function fkbjformat(value,row,index){
	if(row.fkbj=='1'){
		return '是';
		
	}else{
		return '否';
	}
}
//是否收費格式化
function sfsfformat(value,row,index){
	if(row.sfsf=='1'){
		return '是';
	}else if(row.sfsf=='0'){
		var sfsf="<span style='color:red;'>否</span>"
		return sfsf;
	}else if(row.sfsf=='2'){
		var sfsf="<span style='color:#8E388E;font-weight:bold'>免            D</span>"
		return sfsf;
	}else if(row.sfsf=='3'){
		var sfsf="<span style='color:#8470FF;font-weight:bold'>免         R</span>"
		return sfsf;
	}
}
	//是否上传格式化
function wjformat(value,row,index){
	if(row.sfsc=="1"){
		var sfsc="<span>是</span>"
		return sfsc;
	}else{
		var sfsc="<span style='color:red;'>否</span>"
		return sfsc;
	}
}

/**
 * 反馈结果格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function lcfkformat(value,row,index){
	if(row.sfzq=="1"){
		var sfzq="<span>正确</span>"
			return sfzq;
	}else if(row.sfzq=="0"){
		var sfzq="<span style='color:red;'>错误</span>"
			return sfzq;
	}else{
		var sfzq="<span'>未反馈</span>"
			return sfzq;
	}
}

/**
 * 检测项目格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function jcxmformat(value,row,index){
	/*if(row.jcxmkzcs=="D"){
		return null;
	}else{*/
		return row.jcxmmc;
	//}
}
/**
 * 检测子项目格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function jczxmformat(value,row,index){
	/*if(row.jcxmkzcs=="D"){
		return null;
	}else{*/
		return row.jczxmmc;
	//}
}

    // 按钮动作函数
function InspectionDealById(id,ks,action,tourl){
	var url= tourl;
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?sjid=" +id +"&ks=" +ks;
		$.showDialog(url,'送检详细信息',	viewSjxxConfig);
	}else if(action =='viewmore'){
		var url= tourl + "?sjid=" +id +"&ks=" +ks;
		$.showDialog(url,'送检详细信息',viewmoreSjxxConfig);
	}else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'新增送检信息',addSjxxConfig);
	}else if(action =='adkadd'){
        var url= tourl;
        $.showDialog(url,'新增送检信息',addSjxxConfig);
    }else if(action =='mod'){
		var url=tourl + "?sjid=" +id+"&xg_flg=1";
		$.showDialog(url,'修改送检信息',modSjxxConfig);
	}else if(action =='adjust'){
		var url=tourl + "?sjid=" +id;
		$.showDialog(url,'调整送检信息',adjustSjxxConfig);
	}else if(action=="sendreport"){
		var url=tourl+"?sjid="+id;
		$.showDialog(url,'结核耐药发送报告',sendReportConfig);
	}else if(action =='upload'){
		var url=tourl + "?sjid=" +id;
		$.showDialog(url,'检测结果上传',uploadInspectionConfig);
	}else if(action=='moddb'){
		var url=tourl + "?ids=" +id;
		$.showDialog(url,'更改合作伙伴',moddbInspectionConfig);
	}else if(action =='confirm'){
		if(id!=null){
			var url= tourl+"?sjid="+id;
		}else{
			var url= tourl;
		}
		$.showDialog(url,'送检收样确认',confirmInspectionConfig);
	}else if(action=='detection'){
		var url=tourl + "?ids=" +id;
		$.showDialog(url,'修改检测状态',modDetectionConfig);
    }else if(action =='advancedmod'){
        var url=tourl + "?sjid=" +id;
        $.showDialog(url,'修改送检信息',modSjxxConfig);
    }else if(action =='extend'){
    	var url=tourl + "?ids=" +id;
    	$.showDialog(url,'修改送检信息',modkzcsConfig);
    }else if(action=="recheck"){
    	var url=tourl+"?sjid="+id;
    	$.showDialog(url,'复测加测申请',recheckInspectionConfig);
    }else if(action=="feedback"){
    	var url=tourl+"?sjid="+id;
    	$.showDialog(url,'反馈',feedbackConfig);
    }else if(action=="reportdownload"){
    	var url=tourl + "?ids=" +id;
    	$.showDialog(url,'报告下载',reportDownloadConfig);
    }else if(action=="exceptionlist"){
    	var url=tourl;
    	if(id!=null){
    	if(id.indexOf(",")>-1){
    		url=tourl+"?sjids="+id+"&listflg=1";
    	}else{
    		url=tourl+"?ywid="+id+"&listflg=1";
    	}
    	}else{
			url=tourl+"?listflg=1";
		}
    	$.showDialog(url,'异常清单',exceptionListConfig);
    }else if(action=="verif"){
    	var url=tourl+"?sjid="+id; 
    	$.showDialog(url,'送检验证',verifSjxxConfig);
    }else if(action=="specialapply"){
		var url=tourl+"?sjid="+id;
		$.showDialog(url,'特殊申请',specialApplySjxxConfig);
	}else if(action=="send"){
    	var url=tourl + "?ids=" +id;
    	$.showDialog(url,'发送',sendMessageSjxxConfig);
    }else if(action=="refund"){
    	var url=tourl + "?sjid=" +id;
    	$.showDialog(url,'退款申请',payRefundConfig);
    }else if(action=="resend"){
	var url=tourl + "?ids=" +id;
	$.showDialog(url,'重发报告',resendReportConfig);
    }else if(action=="paperapply"){
	var url=tourl+"?ids="+id;
	$.showDialog(url,'纸质申请',paperApplyConfig);
    }else if(action =='print'){
		var url= tourl+"?sjid="+id;
		$.showDialog(url,'打印',printInspectionConfig);
	}else if(action=="rfssend"){
		var url=tourl+"?sjid="+id;
		$.showDialog(url,'ResFirst重发报告',resendConfig);
	}else if(action=="riskboard"){
		var url=tourl+"?ywid="+id;
		$.showDialog(url,'标本退回',riskBoardConfig);
	}else if(action=="deal"){
		var url=tourl+"?sjid="+id;
		$.showDialog(url,'标本处理',dealSjxxConfig);
	}else if(action =='dataget'){
		var url= tourl;
		$.showDialog(url,'获取送检信息',addSjxxConfig);
	}
}
function delphoneredis(){
 jQuery.ajaxSetup({async:false});
                    var url= "/common/file/pagedataDelPhoneUpload";
                    jQuery.post(url,{"ywlx":$("#ywlx").val(),"ywid":$("#phoneywid").val(),"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){

                        },1);

                    },'json');
                    jQuery.ajaxSetup({async:true});
}
var riskBoardConfig = {
	width		: "900px",
	modalName	: "riskBoardModal",
	formName	: "riskBoardAjaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {

				if(!$("#riskBoardAjaxForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#riskBoardAjaxForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"riskBoardAjaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							//提交审核
							if(responseText["auditType"]!=null){
								showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){});
							}
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
						$.alert(responseText["message"],function() {
						});
					}
				},".modal-footer > button");
				var timers=$("#riskBoardModal").attr("dsq")
                if(timers){
                   var arr=timers.split("_")
                       for(var i=0;i<arr.length;i++){
                          if(arr[i]!=""){
                               window.clearTimeout(parseInt(arr[i]))
                                  }
                           }
                       }

				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default",
			callback : function() {
			var timers=$("#riskBoardModal").attr("dsq")
			delphoneredis();
                if(timers){
                   var arr=timers.split("_")
                   for(var i=0;i<arr.length;i++){
                       if(arr[i]!=""){
                           window.clearTimeout(parseInt(arr[i]))
                       }

                   }
                }
			}

		}
	}
};

var resendConfig = {
	width		: "800px",
	modalName	: "resendModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#resend_formSearch #resend_list').bootstrapTable('getSelections');// 获取选择行数据
				if(sel_row.length==1){
					var $this = this;
					var opts = $this["options"]||{};
					$("#resend_formSearch input[name='access_token']").val($("#ac_tk").val());
					$("#resend_formSearch input[name='fjid']").val(sel_row[0].fjid);
					$("#resend_formSearch input[name='flg_qf']").val(sel_row[0].flg);
					submitForm(opts["formName"]||"resend_formSearch",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchSjxxResult();
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
							preventResubmitForm(".modal-footer > button", false);
							$.alert(responseText["message"],function() {
							});
						}
					},".modal-footer > button");
					return false;
				}else{
					$.error("请选中一行");
					return false;
				}
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
var paperApplyConfig = {
	width : "800px",
	modalName : "paperApplyConfigModal",
	formName : "addPaperReportsApplyForm",
	offAtOnce : true,  // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确　定",
			className : "btn-primary",
			callback : function() {
				if(!$("#addPaperReportsApplyForm").valid()){// 表单验证
					$.alert("请填写完整信息");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#addPaperReportsApplyForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"addPaperReportsApplyForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							//提交审核
							if(responseText["auditType"]!=null){
								showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
									searchSjxxResult();
								});
							}else{
								searchSjxxResult();
							}
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {});
					} else{
						preventResubmitForm(".modal-footer > button", false);
						$.alert(responseText["message"],function() {});
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
}
function dataMerg(sjid1,sjid2){
    $.ajax({
        type:'post',
        url:"/inspection/inspection/dataMerging",
        cache: false,
        data: {"ids":sjid1+","+sjid2,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
        if(data.status=='success'){
            $.alert(data.message,function() {
            });
            searchSjxxResult();
        }else{
            $.error(data.message,function() {
            });
        }

        }
    });
}
var Inspection_ButtonInit = function(){
    	var oInit = new Object();
    	var postdata = {};
    	oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_add = $("#sjxx_formSearch #btn_add");
    	var btn_mod = $("#sjxx_formSearch #btn_mod");
    	var btn_deal = $("#sjxx_formSearch #btn_deal");
    	var btn_adjust = $("#sjxx_formSearch #btn_adjust");
    	var btn_del = $("#sjxx_formSearch #btn_del");
    	var btn_view = $("#sjxx_formSearch #btn_view");//
    	var btn_viewmore = $("#sjxx_formSearch #btn_viewmore");//
		var btn_riskboard = $("#sjxx_formSearch #btn_riskboard");
    	var btn_query =$("#sjxx_formSearch #btn_query");//模糊查询
    	var btn_upload=$("#sjxx_formSearch #btn_upload");//上传
    	var btn_moddb=$("#sjxx_formSearch #btn_moddb");//修改合作伙伴
    	var btn_selectexport = $("#sjxx_formSearch #btn_selectexport");//选中导出
    	var btn_searchexport = $("#sjxx_formSearch #btn_searchexport");//搜索导出
    	var btn_confirm = $("#sjxx_formSearch #btn_confirm");//送检收样确认
    	var btn_preview=$("#sjxx_formSearch #btn_preview");//图片预览
    	var btn_advancedmod=$("#sjxx_formSearch #btn_advancedmod");//高级修改
    	var btn_detection=$("#sjxx_formSearch #btn_detection");//检测
    	var btn_send=$("#sjxx_formSearch #btn_send");//检测
    	var btn_sendreport=$("#sjxx_formSearch #btn_sendreport");//耐药发送报告
    	var initTableField = $("#sjxx_formSearch #initTableField");//字段选择
    	var btn_statis=$("#sjxx_formSearch #btn_statis");
    	var btn_extend=$("#sjxx_formSearch #btn_extend");//扩展修改
    	var btn_recheck=$("#sjxx_formSearch #btn_recheck"); //复检
    	var btn_amountselectexport = $("#sjxx_formSearch #btn_amountselectexport");//选中导出
    	var btn_amountsearchexport = $("#sjxx_formSearch #btn_amountsearchexport");//搜索导出
    	var btn_feedback=$("#sjxx_formSearch #btn_feedback"); //反馈
    	var btn_reportdownload = $("#sjxx_formSearch #btn_reportdownload");//报告下载
    	var btn_exceptionlist=$("#sjxx_formSearch #btn_exceptionlist");//异常清单
    	var btn_verif=$("#sjxx_formSearch #btn_verif");//送检验证
		var btn_specialapply=$("#sjxx_formSearch #btn_specialapply");//特殊申请
		//送检伙伴点击事件
    	var sjhbflBind = $("#sjxx_formSearch #sjhbfl_id ul li a");
    	//检测项目点击事件
    	var jcxmBind = $("#sjxx_formSearch #jcxm_id ul li a");
    	var btn_resend=$("#sjxx_formSearch #btn_resend");//重发报告
    	var btn_refund = $("#sjxx_formSearch #btn_refund"); // 退款
		var btn_paperapply=$("#sjxx_formSearch #btn_paperapply");//纸质申请
		var btn_rfssend=$("#sjxx_formSearch #btn_rfssend");//重发报告
		var btn_print=$("#sjxx_formSearch #btn_print");//打印
		var btn_datamerg=$("#sjxx_formSearch #btn_datamerg");//数据合并
		var btn_dataget=$("#sjxx_formSearch #btn_dataget");//数据合并
        var btn_adkadd = $("#sjxx_formSearch #btn_adkadd");
    	btn_datamerg.unbind("click").click(function(){
            var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==2){
                dataMerg(sel_row[0].sjid,sel_row[1].sjid);
            }else{
                $.error("请选中两行");
            }
        });
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #jsrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #jsrqend'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #bgrqstart'
		   ,type: 'date'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #bgrqend'
		   ,type: 'date'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #qyrqstart'
    	   ,type: 'datetime'
		   ,ready: function(date){
				if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
					var myDate = new Date(); //实例一个时间对象；
					this.dateTime.hours=myDate.getHours();
					this.dateTime.minutes=myDate.getMinutes();
		        	this.dateTime.seconds=myDate.getSeconds();
				}
   	    	}
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #qyrqend'
    	   ,type: 'datetime'
		   ,ready: function(date){
				if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
					var myDate = new Date(); //实例一个时间对象；
					this.dateTime.hours=myDate.getHours();
					this.dateTime.minutes=myDate.getMinutes();
		        	this.dateTime.seconds=myDate.getSeconds();
				}
		   }
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #ydrqstart'
    	   ,type: 'datetime'
		   ,ready: function(date){
				if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
					var myDate = new Date(); //实例一个时间对象；
					this.dateTime.hours=myDate.getHours();
					this.dateTime.minutes=myDate.getMinutes();
		        	this.dateTime.seconds=myDate.getSeconds();
				}
		   }
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #ydrqend'
    	   ,type: 'datetime'
		   ,ready: function(date){
				if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
					var myDate = new Date(); //实例一个时间对象；
					this.dateTime.hours=myDate.getHours();
					this.dateTime.minutes=myDate.getMinutes();
		        	this.dateTime.seconds=myDate.getSeconds();
				}
		    }
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #syrqstart'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #syrqend'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #fkrqstart'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjxx_formSearch #fkrqend'
    	});
    	
    	if(btn_query!=null){
    		btn_query.unbind("click").click(function(){
    			searchSjxxResult(true); 
    		});
    	};
    	btn_statis.unbind("click").click(function(){
    			window.open("/ws/statistics/getDaily?jsrq=2019-08-05");
    	});

		btn_rfssend.unbind("click").click(function(){
			var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				InspectionDealById(sel_row[0].sjid,null,"rfssend",btn_rfssend.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
			/*---------------------------打印-----------------------------------*/
			btn_print.unbind("click").click(function(){
				var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					InspectionDealById(sel_row[0].sjid,null,"print",btn_print.attr("tourl"));
				}else{
					$.error("请选中一行");
				}
			});

		/*---------------------------查看送检详细信息表-----------------------------------*/
		btn_viewmore.unbind("click").click(function(){
			var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				InspectionDealById(sel_row[0].sjid,sel_row[0].ks,"viewmore",btn_viewmore.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
    	
          /*---------------------------查看送检信息表-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			InspectionDealById(sel_row[0].sjid,sel_row[0].ks,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	//-------------------------------------图片预览-----------------------------------
    	btn_preview.unbind("click").click(function(){
    		var sel_row =$('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			var ybbh=sel_row[0].ybbh;
    			window.open("/ws/preview?ybbh="+ybbh);
    		}else{
    			$.error("请选中一行");
    		}
    	})
    	 /*---------------------------修改送检信息-----------------------------------*/
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			InspectionDealById(sel_row[0].sjid,null,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	 /*---------------------------处理送检信息-----------------------------------*/
    	btn_deal.unbind("click").click(function(){
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			InspectionDealById(sel_row[0].sjid,null,"deal",btn_deal.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});

		/*---------------------------调整送检信息-----------------------------------*/
		btn_adjust.unbind("click").click(function(){
			var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				InspectionDealById(sel_row[0].sjid,null,"adjust",btn_adjust.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*---------------------------标本退回-----------------------------------*/
		btn_riskboard.unbind("click").click(function(){
			var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				InspectionDealById(sel_row[0].sjid,null,"riskboard",btn_riskboard.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});

    	 /*---------------------------反馈送检信息-----------------------------------*/
    	btn_feedback.unbind("click").click(function(){
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			InspectionDealById(sel_row[0].sjid,null,"feedback",btn_feedback.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	 /* ------------------------------添加送检信息-----------------------------*/
    	btn_add.unbind("click").click(function(){
    		InspectionDealById(null,null,"add",btn_add.attr("tourl"));
    	});
    	btn_dataget.unbind("click").click(function(){
    		InspectionDealById(null,null,"dataget",btn_dataget.attr("tourl"));
    	});
    	btn_adkadd.unbind("click").click(function(){
            InspectionDealById(null,null,"adkadd",btn_adkadd.attr("tourl"));
        });
    	/*--------------------------------修改检测状态-----------------------------*/
    	btn_detection.unbind("click").click(function(){
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else{
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {
    				ids= ids + ","+ sel_row[i].sjid;
				}
    			ids=ids.substr(1);
    			var checkjcxm=true;
    			$.ajax({ 
    			    type:'post',  
    			    url:"/inspection/detection/pagedataCheckJcxm",
    			    cache: false,
    			    data: {"ids":ids,"access_token":$("#ac_tk").val()},  
    			    dataType:'json', 
    			    success:function(data){
    			    	//返回值
    			    	if(data==false){
    			    		$.error("检测项目必须相同!");
    			    	}else{
    			    		InspectionDealById(ids,null,"detection",btn_detection.attr("tourl"));
    			    	}
    			    }
    			});
    		}
    		/*if(sel_row.length==1){
    			if(sel_row[0].jcxmmc=="" ||sel_row[0].jcxmmc==null){
    				$.error("检测项目为空，不能修改检测信息");
    			}else{
    				InspectionDealById(sel_row[0].sjid,null,"detection",btn_detection.attr("tourl"));
    			}
    			
    		}else{
    			$.error("请选中一行");
    		} */
    	})
    	 /* ------------------------------删除送检信息-----------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else {
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].sjid;
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定要删除所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= btn_del.attr("tourl");
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								searchSjxxResult();
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
    	});

		btn_sendreport.unbind("click").click(function(){
			var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length!=1){
				$.error("请选中一行");
				return;
			}else {
				InspectionDealById(sel_row[0].sjid,null,"sendreport",btn_sendreport.attr("tourl"));
			}
		});

    	 /* ------------------------------发送-----------------------------*/
    	btn_send.unbind("click").click(function(){
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else {
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].ybbh;
        		}
    			ids=ids.substr(1);
    			InspectionDealById(ids,null,"send",btn_send.attr("tourl"));
    			/*$.confirm('您确定要发送所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= btn_send.attr("tourl");
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								searchSjxxResult();
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
        		});*/
    		}
    	});
    	//-------------------------------修改合作伙伴-------------------------------
    	btn_moddb.unbind("click").click(function(){
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else{
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {
    				ids= ids + ","+ sel_row[i].sjid;
				}
    			ids=ids.substr(1);
    			InspectionDealById(ids,null,"moddb",btn_moddb.attr("tourl"));
    		}
    	})
    	/*----------------------------高级修改-----------------*/
    	btn_advancedmod.unbind("click").click(function(){
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			InspectionDealById(sel_row[0].sjid,null,"advancedmod",btn_advancedmod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
    	if(initTableField!=null){
    		initTableField.unbind("click").click(function(){

				let ywid = "INSPECT";
				if ($('#sjxx_formSearch #single_flag').val() && "2" == $('#sjxx_formSearch #single_flag').val()){
					ywid = "AIDIKANG_INSPECT";
				}
    			$.showDialog(titleSelectUrl+"?ywid="+ywid
        				,"列表字段设置", $.extend({},setSjxxListFieldsConfig,{"width":"1020px"}));
    		});
    	}
    	
    	//---------------------------------检验结果上传----------------------------------
    	btn_upload.unbind("click").click(function(){
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			InspectionDealById(sel_row[0].sjid,null,"upload",btn_upload.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	//---------------------------------选中导出---------------------------------------
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].sjid;
        		}
        		ids = ids.substr(1);
				let ywid = "INSPECT_SELECT";
				if ($('#sjxx_formSearch #single_flag').val() && "2" == $('#sjxx_formSearch #single_flag').val()){
					ywid = "AIDIKANG_SELECT";
				}
				let tmpExportPrepareUrl = exportPrepareUrl +"?ywid="+ywid+"&expType=select&ids="+ids
				if ($('#sjxx_formSearch #single_flag').val()){
					if (tmpExportPrepareUrl.indexOf("?")>0){
						tmpExportPrepareUrl = tmpExportPrepareUrl + "&single_flag="+$("#sjxx_formSearch #single_flag").val()
					} else {
						tmpExportPrepareUrl = tmpExportPrepareUrl + "?single_flag=" + $("#sjxx_formSearch #single_flag").val()
					}
				}
        		$.showDialog(tmpExportPrepareUrl
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});
    	//搜索导出
    	btn_searchexport.unbind("click").click(function(){
			let ywid = "INSPECT_SEARCH";
			if ($('#sjxx_formSearch #single_flag').val() && "2" == $('#sjxx_formSearch #single_flag').val()){
				ywid = "AIDIKANG_SEARCH";
			}
			let tmpExportPrepareUrl = exportPrepareUrl +"?ywid="+ywid+"&expType=search&callbackJs=inspectSearchData"
			if ($('#sjxx_formSearch #single_flag').val()){
				if (tmpExportPrepareUrl.indexOf("?")>0){
					tmpExportPrepareUrl = tmpExportPrepareUrl + "&single_flag="+$("#sjxx_formSearch #single_flag").val()
				} else {
					tmpExportPrepareUrl = tmpExportPrepareUrl + "?single_flag=" + $("#sjxx_formSearch #single_flag").val()
				}
			}
			$.showDialog(tmpExportPrepareUrl
				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});
    	//---------------------------------金额选中导出---------------------------------------
    	btn_amountselectexport.unbind("click").click(function(){
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].sjid;
        		}
        		ids = ids.substr(1);
        		$.showDialog(exportPrepareUrl+"?ywid=INSPECT_AMOUNT_SELECT&expType=select&ids="+ids
        				,btn_amountselectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});
    	//金额搜索导出
    	btn_amountsearchexport.unbind("click").click(function(){
    		$.showDialog(exportPrepareUrl+"?ywid=INSPECT_AMOUNT_SEARCH&expType=search&callbackJs=inspectSearchData"
    				,btn_amountsearchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});
    	//送检收样确认
    	btn_confirm.unbind("click").click(function(){
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			InspectionDealById(sel_row[0].sjid,null,"confirm",btn_confirm.attr("tourl"));	
    		}else{
    			InspectionDealById(null,null,"confirm",btn_confirm.attr("tourl"));
    		}
    	});
    	/**显示隐藏**/      
    	$("#sjxx_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(Inspection_turnOff){
    			$("#sjxx_formSearch #searchMore").slideDown("low");
    			Inspection_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#sjxx_formSearch #searchMore").slideUp("low");
    			Inspection_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});
    	//-------------------------------修改扩展参数-------------------------------
    	btn_extend.unbind("click").click(function(){
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else{
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {
    				ids= ids + ","+ sel_row[i].sjid;
				}
    			ids=ids.substr(1);
    			InspectionDealById(ids,null,"extend",btn_extend.attr("tourl"));
    		}
    	})
    	//-------------------------------------复检---------------------------------------
    	btn_recheck.unbind("click").click(function(){
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			InspectionDealById(sel_row[0].sjid,null,"recheck",btn_recheck.attr("tourl"));
    		}else{
    			$.error("请选中一行!");
    			return;
    		}
    	})
    	//------------------------------------- 报告下载 ---------------------------------------
    	btn_reportdownload.unbind("click").click(function(){
    		//判断有选中的采用选中导出，没有采用选择导出
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].sjid;
    		}
    		ids = ids.substr(1);
    		InspectionDealById(ids,null,"reportdownload",btn_reportdownload.attr("tourl"));
    	});
    	
    	   /*---------------------------查看异常清单-----------------------------------*/
    	btn_exceptionlist.unbind("click").click(function(){
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			InspectionDealById(sel_row[0].sjid,null,"exceptionlist",btn_exceptionlist.attr("tourl"));
    		}else if (sel_row.length==0){
				InspectionDealById(null,null,"exceptionlist",btn_exceptionlist.attr("tourl"));
    		}else{
    		    var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].sjid;
                }
                ids = ids.substr(1);
				InspectionDealById(ids,null,"exceptionlist",btn_exceptionlist.attr("tourl"));
			}
    	});
    	 /*---------------------------送检验证-----------------------------------*/
    	btn_verif.unbind("click").click(function(){
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
				var nbbm = sel_row[0].nbbm;
				if (nbbm != null && nbbm != "") {
					var lastStr = nbbm.substring(nbbm.length-1);
					if("B"==lastStr || "b"==lastStr){
						$.error("当前样本类型为“血”,不可发起PCR验证新增!");
						return;
					}
				}
    			InspectionDealById(sel_row[0].sjid,null,"verif",btn_verif.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/*---------------------------特殊申请-----------------------------------*/
		btn_specialapply.unbind("click").click(function(){
			var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				InspectionDealById(sel_row[0].sjid,null,"specialapply",btn_specialapply.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		})
		/*---------------------------纸质申请-----------------------------------*/
		btn_paperapply.unbind("click").click(function(){
			var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==0){
				$.error("请至少选中一行");
				return;
			}else{
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {
					ids= ids + ","+ sel_row[i].sjid;
				}
				ids=ids.substr(1);
				InspectionDealById(ids,null,"paperapply",btn_paperapply.attr("tourl"));
			}
		})
    	/*---------------------------重新发送报告-----------------------------------*/
    	btn_resend.unbind("click").click(function(){
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else{
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {
    				ids= ids + ","+ sel_row[i].sjid;
				}
    			ids=ids.substr(1);
				InspectionDealById(ids,null,"resend",btn_resend.attr("tourl"));
    		}
    	})
    	/*--------------------------- 退款 -----------------------------------*/
    	btn_refund.unbind("click").click(function(){
    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			InspectionDealById(sel_row[0].sjid,null,"refund",btn_refund.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	})
    	
    	//绑定合作伙伴分类的单击事件
    	if(sjhbflBind!=null){
    		sjhbflBind.on("click", function(){
    			setTimeout(function(){
    				getSjhbzfls();
    			}, 10);
    		});
    	}
    	//绑定检测项目的单击事件
    	if(jcxmBind!=null){
            jcxmBind.on("click", function(){
                setTimeout(function(){
                    getSjjczxms();
                }, 10);
            });
        }
    };
    return oInit;
};
//自动检查报告重新发送进度
var checkReportResendStatus = function(intervalTime,redisKey){
	$.ajax({
		type : "POST",
		url : "/common/export/commCheckExport",
		data : {"wjid" : redisKey +"","access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.cancel){//取消导出则直接return
				return;
			}
			if(data.result == false){
				if(data.msg && data.msg!=""){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}else{
					if(intervalTime < 5000)
						intervalTime = intervalTime + 1000;
					if($("#exportCount")){
						$("#exportCount").html(data.currentCount);
					}
					setTimeout("checkReportResendStatus("+intervalTime+",'"+ redisKey +"')",intervalTime);
				}
			}else{
				if(data.msg && data.msg!=""){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}
				else{
					$.success("发送成功！");
					if($("#cardiv")) $("#cardiv").remove();
				}
			}
		}
	});
}


//合作伙伴子分类的取得
function getSjhbzfls() {
	// 合作伙伴分类
	var sjhbfl = jQuery('#sjxx_formSearch #sjhbfl_id_tj').val();
	if (!isEmpty(sjhbfl)) {
		sjhbfl = "'" + sjhbfl + "'";
		jQuery("#sjxx_formSearch #sjhbzfl_id").removeClass("hidden");
	}else{
		jQuery("#sjxx_formSearch #sjhbzfl_id").addClass("hidden");
	}
	// 项目类别不为空
	if (!isEmpty(sjhbfl)) {
		var url = "/systemmain/data/ansyGetJcsjListInStop";
		$.ajax({
			type : "POST",
			url : url,
			data : {"fcsids":sjhbfl,"access_token":$("#ac_tk").val()},
			dataType : "json",
			success : function(data){
				if(data.length > 0) {
					var html = "";
					$.each(data,function(i){
						html += "<li>";
						if(data[i].scbj=="2"){
							html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('sjhbzfl','" + data[i].csid + "','sjxx_formSearch');\" id=\"sjhbzfl_id_" + data[i].csid + "\"><span style='color:red'>" + data[i].csmc + "(停用)" +"</span></a>";
						}else
							html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('sjhbzfl','" + data[i].csid + "','sjxx_formSearch');\" id=\"sjhbzfl_id_" + data[i].csid + "\"><span>" + data[i].csmc + "</span></a>";
						html += "</li>";
					});
					jQuery("#sjxx_formSearch #ul_sjhbzfl").html(html);
					jQuery("#sjxx_formSearch #ul_sjhbzfl").find("[name='more']").each(function(){
						$(this).on("click", s_showMoreFn);
					});
				} else {
					jQuery("#sjxx_formSearch #ul_sjhbzfl").empty();

				}
				$("#sjxx_formSearch #sjhbzfl_id_tj").val("");
			}
		});
	} else {
		jQuery("#sjxx_formSearch #div_sjhbzfl").empty();
		$("#sjxx_formSearch #sjhbzfl_id_tj").val("");
	}
}
//合作伙伴子分类的取得
function getSjjczxms() {
	// 合作伙伴分类
	var jcxm = jQuery('#sjxx_formSearch #jcxm_id_tj').val();
	if (!isEmpty(jcxm)) {
		jcxm = "'" + jcxm + "'";
		jQuery("#sjxx_formSearch #jczxm_id").removeClass("hidden");
	}else{
		jQuery("#sjxx_formSearch #jczxm_id").addClass("hidden");
	}
	// 项目类别不为空
	if (!isEmpty(jcxm)) {
		var url = "/systemmain/data/ansyGetJcsjListInStop";
		$.ajax({
			type : "POST",
			url : url,
			data : {"fcsids":jcxm,"access_token":$("#ac_tk").val()},
			dataType : "json",
			success : function(data){
				if(data.length > 0) {
					var html = "";
					$.each(data,function(i){
						html += "<li>";
						if(data[i].scbj=="2"){
							html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('jczxm','" + data[i].csid + "','sjxx_formSearch');\" id=\"jczxm_id_" + data[i].csid + "\"><span style='color:red'>" + data[i].csmc + "(停用)" +"</span></a>";
						}else
							html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('jczxm','" + data[i].csid + "','sjxx_formSearch');\" id=\"jczxm_id_" + data[i].csid + "\"><span>" + data[i].csmc + "</span></a>";
						html += "</li>";
					});
					jQuery("#sjxx_formSearch #ul_jczxm").html(html);
					jQuery("#sjxx_formSearch #ul_jczxm").find("[name='more']").each(function(){
						$(this).on("click", s_showMoreFn);
					});
				} else {
					jQuery("#sjxx_formSearch #ul_jczxm").empty();
				}
				$("#sjxx_formSearch #jczxm_id_tj").val("");
			}
		});
	} else {
		jQuery("#sjxx_formSearch #div_jczxm").empty();
		$("#sjxx_formSearch #jczxm_id_tj").val("");
	}
}

//自动检查报告压缩进度
var checkReportZipStatus = function(intervalTime,redisKey){
	$.ajax({
		type : "POST",
		url : "/common/export/commCheckExport",
		data : {"wjid" : redisKey +"","access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.cancel){//取消导出则直接return
				return;
			}
			if(data.result == false){
				if(data.msg && data.msg!=""){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}else{
					if(intervalTime < 5000)
						intervalTime = intervalTime + 1000;
					if($("#exportCount")){
						$("#exportCount").html(data.currentCount)
						if(("/"+data.currentCount) == $("#totalCount").text()){
							$("#totalCount").html($("#totalCount").text()+" 压缩中....")
						}
					}
					setTimeout("checkReportZipStatus("+intervalTime+",'"+ redisKey +"')",intervalTime);
				}
			}else{
				if(data.msg && data.msg!=""){
					$.error(data.msg);
					if($("#cardiv")) $("#cardiv").remove();
				}
				else{
					if($("#cardiv")) $("#cardiv").remove();
					window.open("/common/export/commDownloadExport?wjid="+redisKey + "&access_token="+$("#ac_tk").val());
				}
			}
		}
	});
}

//提供给导出用的回调函数
function inspectSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="sj.sjid";
	map["sortLastOrder"]="asc";
	map["sortName"]="sj.lrsj";
	map["sortOrder"]="asc";
	return getSjxxSearchData(map);
}

var resendReportConfig = {
	width		: "1000px",
	modalName	: "resendReportModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#resendReportForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#resendReportForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"resendReportForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							searchSjxxResult();
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
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

/*查看送检信息模态框*/
var viewSjxxConfig = {
		width		: "1000px",
		height		: "500px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
/*查看详细送检信息模态框*/
var viewmoreSjxxConfig = {
	width		: "1000px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

/*查看异常清单模态框*/
var exceptionListConfig = {
	width		: "800px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	modalName	: "exceptionListModalSy",
	buttons		: {
		cancel: {
			label: "关 闭",
			className: "btn-default",
			callback: function () {
				if ($("#exceptionList_Form #key").val()) {
					$.ajax({
						url: "/sseEmit/connect/pagedataCloseOA",
						data: {
							"access_token": $("#ac_tk").val(),
							"key": $("#exceptionList_Form #key").val(),
						}
					});
				}
				return true;
			}
		}
	}
};

/*   修改送检信息模态框*/
var verifSjxxConfig = {
		width		: "1000px",
		modalName	: "verifSjxxModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if($("#verificationForm #isB").val()=="true"){
						$.error("当前样本类型为“血”,不可发起PCR验证新增!");
						return false;
					}
					if(!$("#verificationForm").valid()){
						$.alert("请填写完整信息!");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					let data = JSON.parse($("#verificationForm #yzjgjson").val());
					let yzjg = "";
					$.each(data,function(i){
						let jg = ''
						if (typeof($("#verificationForm input[name='"+data[i].csid+"']:checked").val()) != 'undefined'){
							jg = $("#verificationForm input[name='"+data[i].csid+"']:checked").val();
						}else{
							jg = $("#verificationForm input[name='"+data[i].csid+"']").val();
							let str = jg.split(":")
							jg = str[0]+":";
						}
						yzjg += jg;
						if (i != data.length-1){
							yzjg +=",";
						}
					});
					$("#verificationForm input[name='yzjg']").val(yzjg);
					$("#verificationForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"verificationForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								//提交审核
								if(responseText["auditType"]!=null){
									showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
										searchSjxxResult();
									});
								}else{
									searchSjxxResult();
								}
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
							preventResubmitForm(".modal-footer > button", false);
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

/*   修改送检信息模态框*/
var specialApplySjxxConfig = {
	width		: "1000px",
	modalName	: "specialApplySjxxModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#applicationForm").valid()){
					$.alert("请填写完整信息!");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#applicationForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"applicationForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							//提交审核
							if(responseText["auditType"]!=null){
								showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
									searchSjxxResult();
								});
							}else{
								searchSjxxResult();
							}
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
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

/*   发送报告模态框*/
var sendReportConfig = {
	width		: "1200px",
	modalName	: "sendReportModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success1 : {
			label : "保 存",
			className : "btn-success",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				$("#sendReportAjaxForm input[name='access_token']").val($("#ac_tk").val());
				$("#sendReportAjaxForm #bcbj").val("1");
				var json = [];
				var nyjson = [];
				var sjwzxx_jsonStr = $("#sjwzxx_json").val();
				var sjnyx_jsonStr = $("#sjnyx_json").val();
				if (sjwzxx_jsonStr){
					var sjwzxx_json = JSON.parse(sjwzxx_jsonStr);
					for(var i=0;i<sjwzxx_json.length;i++){
						if(sjwzxx_json[i].wzid != "" && sjwzxx_json[i].wzid != null){
							var sz = {"sjwzid" : "","jglx" : "","wzfl" : "","wzid" : "","wzzwm" : "","wzywm" : "","mappingReads" : "","reads" : "","refANI" : "","target" : "","sfhb":""};
                                sz.sjwzid = sjwzxx_json[i].sjwzid;
                                sz.wzid = sjwzxx_json[i].wzid;
                                sz.mappingReads = sjwzxx_json[i].mappingReads;
                                sz.reads = sjwzxx_json[i].reads;
                                sz.refANI = sjwzxx_json[i].refANI;
                                sz.target = sjwzxx_json[i].target;
                                sz.wzzwm = sjwzxx_json[i].wzzwm;
                                sz.wzywm = sjwzxx_json[i].wzywm;
                                sz.jglx = sjwzxx_json[i].jglx;
                                sz.wzfl = sjwzxx_json[i].wzfl;
                                sz.sfhb = sjwzxx_json[i].sfhb;
                                json.push(sz);
						}
					}
				}
				if (sjnyx_jsonStr){
                    var sjnyx_json = JSON.parse(sjnyx_jsonStr);
                    for(var i=0;i<sjnyx_json.length;i++){
                        var sz = {"sjnyxid":"","wkbh":"","tbjy" : "","tbjg" : "","nyx" : "","tbsd" : "","tbpl" : "","bjtb" : "","sfhb":"","fl":""};
                        sz.sjnyxid = sjnyx_json[i].sjnyxid;
                        sz.wkbh = sjnyx_json[i].wkbh;
                        sz.tbjy = sjnyx_json[i].tbjy;
                        sz.tbjg = sjnyx_json[i].tbjg;
                        sz.nyx = sjnyx_json[i].nyx;
                        sz.tbsd = sjnyx_json[i].tbsd;
                        sz.tbpl = sjnyx_json[i].tbpl;
                        sz.bjtb = sjnyx_json[i].bjtb;
                        sz.sfhb = sjnyx_json[i].sfhb;
                        sz.fl = sjnyx_json[i].fl;
                        nyjson.push(sz);
                    }
                }
				$('#sendReportAjaxForm #jyr').removeAttr("validate");
				$('#sendReportAjaxForm #shr').removeAttr("validate");
				$("#sendReportAjaxForm #sjwzxx_json").val(JSON.stringify(json));
				$("#sendReportAjaxForm #sjnyx_json").val(JSON.stringify(nyjson));
				submitForm(opts["formName"]||"sendReportAjaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
							// 恢复必填验证规则
							$('#sendReportAjaxForm #jyr').attr("validate","{required:true}");
							$('#sendReportAjaxForm #shr').attr("validate","{required:true}");
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
						$.alert(responseText["message"],function() {
							// 恢复必填验证规则
							$('#sendReportAjaxForm #jyr').attr("validate","{required:true}");
							$('#sendReportAjaxForm #shr').attr("validate","{required:true}");
						});
					}
				},".modal-footer > button");
				return false;
			}
		},
		success : {
			label : "TBtNGS发送",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				$("#sendReportAjaxForm input[name='access_token']").val($("#ac_tk").val());
				$("#sendReportAjaxForm #bcbj").val("0");
				var json = [];
				var nyjson = [];
				var sjwzxx_jsonStr = $("#sjwzxx_json").val();
				var sjnyx_jsonStr = $("#sjnyx_json").val();
				if (sjwzxx_jsonStr){
					var sjwzxx_json = JSON.parse(sjwzxx_jsonStr);
					for(var i=0;i<sjwzxx_json.length;i++){
						if(sjwzxx_json[i].wzid != "" && sjwzxx_json[i].wzid != null){
							var sz = {"sjwzid" : "","jglx" : "","wzfl" : "","wzid" : "","wzzwm" : "","wzywm" : "","mappingReads" : "","reads" : "","refANI" : "","target" : "","sfhb":""};
                                sz.sjwzid = sjwzxx_json[i].sjwzid;
                                sz.wzid = sjwzxx_json[i].wzid;
                                sz.mappingReads = sjwzxx_json[i].mappingReads;
                                sz.reads = sjwzxx_json[i].reads;
                                sz.refANI = sjwzxx_json[i].refANI;
                                sz.target = sjwzxx_json[i].target;
                                sz.wzzwm = sjwzxx_json[i].wzzwm;
                                sz.wzywm = sjwzxx_json[i].wzywm;
                                sz.jglx = sjwzxx_json[i].jglx;
                                sz.wzfl = sjwzxx_json[i].wzfl;
                                sz.sfhb = sjwzxx_json[i].sfhb;
                                json.push(sz);
						}
					}
				}
				if (sjnyx_jsonStr){
                    var sjnyx_json = JSON.parse(sjnyx_jsonStr);
                    for(var i=0;i<sjnyx_json.length;i++){
                        var sz = {"sjnyxid":"","wkbh":"","tbjy" : "","tbjg" : "","nyx" : "","tbsd" : "","tbpl" : "","bjtb" : "","sfhb":"","fl":""};
                        sz.sjnyxid = sjnyx_json[i].sjnyxid;
                        sz.wkbh = sjnyx_json[i].wkbh;
                        sz.tbjy = sjnyx_json[i].tbjy;
                        sz.tbjg = sjnyx_json[i].tbjg;
                        sz.nyx = sjnyx_json[i].nyx;
                        sz.tbsd = sjnyx_json[i].tbsd;
                        sz.tbpl = sjnyx_json[i].tbpl;
                        sz.bjtb = sjnyx_json[i].bjtb;
                        sz.sfhb = sjnyx_json[i].sfhb;
                        sz.fl = sjnyx_json[i].fl;
                        nyjson.push(sz);
                    }
                }
				$("#sendReportAjaxForm #sjwzxx_json").val(JSON.stringify(json));
				$("#sendReportAjaxForm #sjnyx_json").val(JSON.stringify(nyjson));
				submitForm(opts["formName"]||"sendReportAjaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
						$.alert(responseText["message"],function() {
						});
					}
				},".modal-footer > button");
				return false;
			}
		},
		success2 : {
            label : "TBtNGS + tNGS发送",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                $("#sendReportAjaxForm input[name='access_token']").val($("#ac_tk").val());
                $("#sendReportAjaxForm #bcbj").val("2");
                var json = [];
                var nyjson = [];
                var sjwzxx_jsonStr = $("#sjwzxx_json").val();
                var sjnyx_jsonStr = $("#sjnyx_json").val();
                if (sjwzxx_jsonStr){
                    var sjwzxx_json = JSON.parse(sjwzxx_jsonStr);
                    for(var i=0;i<sjwzxx_json.length;i++){
                        if(sjwzxx_json[i].wzid != "" && sjwzxx_json[i].wzid != null){
                            var sz = {"sjwzid" : "","jglx" : "","wzfl" : "","wzid" : "","wzzwm" : "","wzywm" : "","mappingReads" : "","reads" : "","refANI" : "","target" : "","sfhb":""};
                                sz.sjwzid = sjwzxx_json[i].sjwzid;
                                sz.wzid = sjwzxx_json[i].wzid;
                                sz.mappingReads = sjwzxx_json[i].mappingReads;
                                sz.reads = sjwzxx_json[i].reads;
                                sz.refANI = sjwzxx_json[i].refANI;
                                sz.target = sjwzxx_json[i].target;
                                sz.wzzwm = sjwzxx_json[i].wzzwm;
                                sz.wzywm = sjwzxx_json[i].wzywm;
                                sz.jglx = sjwzxx_json[i].jglx;
                                sz.wzfl = sjwzxx_json[i].wzfl;
                                sz.sfhb = sjwzxx_json[i].sfhb;
                                json.push(sz);
                        }
                    }
                }
                if (sjnyx_jsonStr){
                    var sjnyx_json = JSON.parse(sjnyx_jsonStr);
                    for(var i=0;i<sjnyx_json.length;i++){
                        var sz = {"sjnyxid":"","wkbh":"","tbjy" : "","tbjg" : "","nyx" : "","tbsd" : "","tbpl" : "","bjtb" : "","sfhb":"","fl":""};
                        sz.sjnyxid = sjnyx_json[i].sjnyxid;
                        sz.wkbh = sjnyx_json[i].wkbh;
                        sz.tbjy = sjnyx_json[i].tbjy;
                        sz.tbjg = sjnyx_json[i].tbjg;
                        sz.nyx = sjnyx_json[i].nyx;
                        sz.tbsd = sjnyx_json[i].tbsd;
                        sz.tbpl = sjnyx_json[i].tbpl;
                        sz.bjtb = sjnyx_json[i].bjtb;
                        sz.sfhb = sjnyx_json[i].sfhb;
                        sz.fl = sjnyx_json[i].fl;
                        nyjson.push(sz);
                    }
                }
                $("#sendReportAjaxForm #sjwzxx_json").val(JSON.stringify(json));
                $("#sendReportAjaxForm #sjnyx_json").val(JSON.stringify(nyjson));
                submitForm(opts["formName"]||"sendReportAjaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        preventResubmitForm(".modal-footer > button", false);
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

/*   调整送检信息模态框*/
var adjustSjxxConfig = {
	width		: "1200px",
	modalName	: "adjustSjxxModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				if(t_map.rows != null && t_map.rows.length > 0){
					var json = [];
					for(var i=0;i<t_map.rows.length;i++){
						if (t_map.rows[i].jcdw == ''){
							$.alert("检测单位不能为空！");
							return false;
						}
						var sz = {"syglid":'',"jcdw":''};
						sz.syglid = t_map.rows[i].syglid;
						sz.jcdw = t_map.rows[i].jcdw;
						json.push(sz);
					}
					$("#ajaxFormAdjust #json").val(JSON.stringify(json));
					$("#ajaxFormAdjust input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"ajaxFormAdjust",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
							preventResubmitForm(".modal-footer > button", false);
							$.alert(responseText["message"],function() {
							});
						}
					},".modal-footer > button");

				}else{
					$.alert("未获取到数据！");
					return false;
				}
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
/*   处理送检信息模态框*/
var dealSjxxConfig = {
		width		: "1000px",
		modalName	: "dealSjxxModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {

				    if($("#ajaxForm #nbbm").val() == "" || $("#ajaxForm #nbbm").val() == ""){
				        $.alert("请填写内部编码！");
                        return false;
				    }
				    else if($("#ajaxForm .jcxm").length >0){
                        var json = [];
                        for (let i = 0; i <= $("#ajaxForm .jcxm").length -1; i++) {
                            if ($("#ajaxForm #"+$("#ajaxForm .jcxm")[i].id).is(':checked')){
                                var sz = {"jcxmid":'',"jczxmid":'',"flag":'',"csmc":'',"zcsmc":''};
                                sz.jcxmid = $("#ajaxForm .jcxm")[i].id;
                                sz.jczxmid = null;
                                sz.csmc = $("#ajaxForm #" + $("#ajaxForm .jcxm")[i].id).attr("csmc");
                                sz.zcsmc = null;
                                sz.flag = $("#ajaxForm #" + $("#ajaxForm .jcxm")[i].id).attr("flag");
                                json.push(sz)
                            }
                        }
                        if (json.length == 0){
                            $.alert("请选择检测项目！");
                            return false;
                        }
                        if($("#ajaxForm .jczxm").length >0){
                            for (let i = 0; i <= $("#ajaxForm .jczxm").length -1; i++) {
                                if ($("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).is(':checked')) {
                                    var jcxmid = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("jcxmid")
                                    if (json.length > 0) {
                                        for (let k = json.length - 1; k >= 0; k--) {
                                            if (json[k].jcxmid == jcxmid && json[k].jczxmid == null) {
                                                json.splice(k, 1);
                                            }
                                        }
                                        var sz = {"xmglid":'',"jcxmid": '', "jczxmid": '',"flag":'',"csmc":'',"zcsmc":'',"sfsf":'',"yfje":'',"sfje":'',"fkrq":''};
                                        sz.jcxmid = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("jcxmid");
                                        sz.jczxmid = $("#ajaxForm .jczxm")[i].id;
                                        sz.csmc = $("#ajaxForm #" + sz.jcxmid).attr("csmc");
                                        sz.zcsmc = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("csmc");
                                        sz.flag = $("#ajaxForm #" + sz.jcxmid).attr("flag");
                                        json.push(sz)
                                    }
                                }
                            }
                            for (let k = json.length - 1; k >= 0; k--) {
                                if (json[k].flag == "1"){
                                    if (json[k].jczxmid == null){
                                        $.alert("请选择检测子项目！");
                                        return false;
                                    }
                                }
                            }
                        }
                        var csjcxmList = JSON.parse($("#ajaxForm #csjcxm").val());
                        var jcxmmc = "";
                        for (let i = 0; i < json.length; i++) {
                            if (json[i].zcsmc){
								if (jcxmmc.indexOf(json[i].csmc + "-"+json[i].zcsmc)<0){ 
	                                jcxmmc+=","+json[i].csmc + "-"+json[i].zcsmc;
	                            }
                            }else{
								if (jcxmmc.indexOf(json[i].csmc)<0){ 
	                                jcxmmc+=","+json[i].csmc;
	                            }
							}
                            for (let j = csjcxmList.length-1; j >= 0; j--) {
                                if (json[i].jcxmid == csjcxmList[j].jcxmid && (!json[i].jczxmid || !csjcxmList[j].jczxmid || json[i].jczxmid == csjcxmList[j].jczxmid)) {
                                    json[i].xmglid = csjcxmList[j].xmglid;
                                    json[i].sfsf = csjcxmList[j].sfsf;
                                    json[i].yfje = csjcxmList[j].yfje;
                                    json[i].sfje = csjcxmList[j].sfje;
                                    json[i].fkrq = csjcxmList[j].fkrq;
                                }
                            }
                        }
                        $("#ajaxForm #jcxmmc").val(jcxmmc.substring(1));
                        $("#ajaxForm #jcxm").val(JSON.stringify(json))
				    }
                    else if($("#ajaxForm #yblx option:selected").text()=="--请选择--"){
                        $.alert("请选择标本类型！");
                        return false;
                    }
                    else if($("#ajaxForm #yblx option:selected").val() == "" || $("#ajaxForm #yblx option:selected").val() == ""){
                         $.alert("请选择标本类型！");
                         return false;
                    }
                    else if($("#ajaxForm #yblxmc").val() == "" || $("#ajaxForm #yblxmc").val() == ""){
                         $.alert("请填写其他类型！");
                         return false;
                    }
                    var sjsylength = $("#ajaxForm #editInfo .panel-default").length;
                    var sjsyJson = [];
                    for (var i=0;i<sjsylength;i++){
                        var sjsy = {"xmglid":'',
                                    "dyid": $("#ajaxForm #editInfo #"+i+"_dyid").val(),
                                    "sjid": $("#ajaxForm #editInfo #"+i+"_sjid").val(),
                                    "jcxmid": $("#ajaxForm #editInfo #"+i+"_jcxmid").val(),
                                    "jcxmmc": $("#ajaxForm #editInfo #"+i+"_jcxmmc").val(),
                                    "jczxmid": $("#ajaxForm #editInfo #"+i+"_jczxmid").val(),
                                    "jclxid": $("#ajaxForm #editInfo #"+i+"_jclxid").val(),
                                    "jcdw": $("#ajaxForm #editInfo #"+i+"_jcdw").val(),
                                    "nbzbm": $("#ajaxForm #editInfo #"+i+"_nbzbm").val(),
                                    "sfjs": ($("#ajaxForm #editInfo #"+i+"_jsrq").val()?"1":"0"),
                                    "jsrq": $("#ajaxForm #editInfo #"+i+"_jsrq").val(),
                                    "jsry": $("#ajaxForm #editInfo #"+i+"_jsry").val(),
                                    "jcbj": ($("#ajaxForm #editInfo #"+i+"_jcsj").val()?"1":"0"),
                                    "jcsj": $("#ajaxForm #editInfo #"+i+"_jcsj").val(),
                                    "syrq": $("#ajaxForm #editInfo #"+i+"_syrq").val(),
                                    "sjsj": $("#ajaxForm #editInfo #"+i+"_sjsj").val(),
                                    "xjsj": $("#ajaxForm #editInfo #"+i+"_xjsj").val(),
                                    "qyry": $("#ajaxForm #editInfo #"+i+"_qyry").val(),
                                    "qysj": $("#ajaxForm #editInfo #"+i+"_qysj").val(),
                                    "jt": $("#ajaxForm #editInfo #"+i+"_jt").val(),
                                    "wksxbm": $("#ajaxForm #editInfo #"+i+"_wksxbm").val()
                                    };
                        sjsyJson.push(sjsy)
                    }
                    $("#ajaxForm #sjsyglJson").val(JSON.stringify(sjsyJson))
                    var $this = this;
                    var opts = $this["options"]||{};
                    $.ajax({
                        type:'post',
                        url:$("#ajaxForm").attr("action"),
                        cache: false,
                        data: {
                            "sjid":$("#ajaxForm #sjid").val(),
                            "access_token":$("#ac_tk").val(),
                            "jcxm":$("#ajaxForm #jcxm").val(),
                            "jcxmmc":$("#ajaxForm #jcxmmc").val(),
                            "jczxmmc":$("#ajaxForm #jczxmmc").val(),
                            "yblx":$("#ajaxForm #yblx").val(),
                            "yblxmc":$("#ajaxForm #yblxmc").val(),
                            "sjsyglJson":$("#ajaxForm #sjsyglJson").val(),
                        },
                        dataType:'json',
                        success:function(responseText){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    if(opts.offAtOnce){
                                        $.closeModal(opts.modalName);
                                    }
                                });
                            }else if(responseText["status"] == "fail"){
                                preventResubmitForm(".modal-footer > button", false);
                                $.error(responseText["message"],function() {
                                });
                            } else{
                                preventResubmitForm(".modal-footer > button", false);
                                $.alert(responseText["message"],function() {
                                });
                            }

                        }
                    });
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

/*   修改送检信息模态框*/
var modSjxxConfig = {
		width		: "1000px",
		modalName	: "modSjxxModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					$("#ajaxForm #syfjInfoTab #ywlx").remove();
					$("#ajaxForm #db").val($("#ajaxForm #db").val().replace(/\s+/g,""));
					var sfws =$("#ajaxForm #sfws").val();
					if($("#ajaxForm #ybbh").val()==""||$("#ajaxForm #ybbh").val()==null){
						$.alert("请输入标本编号！");
						return false;
					}else if($("#ajaxForm #hzxm").val()==""||$("#ajaxForm #hzxm").val()==null){
						$.alert("请输入患者姓名！");
						return false;
					}else if($("#ajaxForm input:radio[name='xb']:checked").val()==null){
						$.alert("请选择性别！");
						return false;
					}else if($("#ajaxForm #nl").val()==""||$("#ajaxForm #nl").val()==null){
						$.alert("请输入年龄！");
						return false;
					}else if($("#ajaxForm #db").val()==""||$("#ajaxForm #db").val()==null){
						$.alert("请输入合作伙伴！");
						return false;
					}else if($("#ajaxForm .jcxm").length >0){
						var json = [];
						for (let i = 0; i <= $("#ajaxForm .jcxm").length -1; i++) {
							if ($("#ajaxForm #"+$("#ajaxForm .jcxm")[i].id).is(':checked')){
								var sz = {"xmglid":'',"jcxmid":'',"jczxmid":'',"flag":'',"csmc":'',"zcsmc":'',"sfsf":'',"yfje":'',"sfje":'',"fkrq":''};
								sz.jcxmid = $("#ajaxForm .jcxm")[i].id;
								sz.jczxmid = null;
								sz.csmc = $("#ajaxForm #" + $("#ajaxForm .jcxm")[i].id).attr("csmc");
								sz.zcsmc = null;
								sz.flag = $("#ajaxForm #" + $("#ajaxForm .jcxm")[i].id).attr("flag");
								json.push(sz)
							}
						}
						if (json.length == 0){
							$.alert("请选择检测项目！");
							return false;
						}
						if($("#ajaxForm .jczxm").length >0){
							for (let i = 0; i <= $("#ajaxForm .jczxm").length -1; i++) {
								if ($("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).is(':checked')) {
									var jcxmid = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("jcxmid")
									if (json.length > 0) {
										for (let k = json.length - 1; k >= 0; k--) {
											if (json[k].jcxmid == jcxmid && json[k].jczxmid == null) {
												json.splice(k, 1);
											}
										}
										var sz = {"xmglid":'',"jcxmid": '', "jczxmid": '',"flag":'',"csmc":'',"zcsmc":'',"sfsf":'',"yfje":'',"sfje":'',"fkrq":''};
										sz.jcxmid = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("jcxmid");
										sz.jczxmid = $("#ajaxForm .jczxm")[i].id;
										sz.csmc = $("#ajaxForm #" + sz.jcxmid).attr("csmc");
										sz.zcsmc = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("csmc");
										sz.flag = $("#ajaxForm #" + sz.jcxmid).attr("flag");
										json.push(sz)
									}
								}
							}
							for (let k = json.length - 1; k >= 0; k--) {
								if (json[k].flag == "1"){
									if (json[k].jczxmid == null){
										$.alert("请选择检测子项目！");
										return false;
									}
								}
							}
						}
						var dtoList = JSON.parse($("#ajaxForm #dtoList").val());
						var jcxmmc = "";
						for (let i = 0; i < json.length; i++) {
							if (json[i].zcsmc){
								if (jcxmmc.indexOf(json[i].csmc + "-"+json[i].zcsmc)<0){ 
	                                jcxmmc+=","+json[i].csmc + "-"+json[i].zcsmc;
	                            }
                            }else{
								if (jcxmmc.indexOf(json[i].csmc)<0){ 
	                                jcxmmc+=","+json[i].csmc;
	                            }
							}
							for (let j = dtoList.length-1; j >= 0; j--) {
								if (json[i].jcxmid == dtoList[j].jcxmid && (!json[i].jczxmid || !dtoList[j].jczxmid || json[i].jczxmid == dtoList[j].jczxmid)) {
									json[i].xmglid = dtoList[j].xmglid;
									json[i].sfsf = dtoList[j].sfsf;
									json[i].yfje = dtoList[j].yfje;
									json[i].sfje = dtoList[j].sfje;
									json[i].fkrq = dtoList[j].fkrq;
								}
							}
						}
						if("1"!=$("#ajaxForm #xg_flg").val()){
							for (let k = 0; k <json.length; k++) {
								for (var i = 0; i < t_map.rows.length; i++) {
									if (json[k].jcxmid == t_map.rows[i].jcxmid) {
										if(json[k].jczxmid){
											if (json[k].jczxmid == t_map.rows[i].jczxmid) {
												if(t_map.rows[i].sfsf){
													json[k].sfsf=t_map.rows[i].sfsf;
												}else{
													json[k].sfsf="1";
												}

												if(t_map.rows[i].yfje){
													json[k].yfje=t_map.rows[i].yfje;
												}else{
													json[k].yfje="0";
												}
												if(t_map.rows[i].sfsf&&t_map.rows[i].sfsf=='0'){
													json[k].yfje="0";
												}
												if(t_map.rows[i].sfje){
													json[k].sfje=t_map.rows[i].sfje;
												}else{
													json[k].sfje="0";
												}
												json[k].fkrq=t_map.rows[i].fkrq;
												break;
											}
										}else{
											if(t_map.rows[i].sfsf){
												json[k].sfsf=t_map.rows[i].sfsf;
											}else{
												json[k].sfsf="1";
											}
											if(t_map.rows[i].yfje){
												json[k].yfje=t_map.rows[i].yfje;
											}else{
												json[k].yfje="0";
											}
											if(t_map.rows[i].sfsf&&t_map.rows[i].sfsf=='0'){
												json[k].yfje="0";
											}
											if(t_map.rows[i].sfje){
												json[k].sfje=t_map.rows[i].sfje;
											}else{
												json[k].sfje="0";
											}
											json[k].fkrq=t_map.rows[i].fkrq;
											break;
										}
									}
								}
							}
						}
						$("#ajaxForm #jcxmmc").val(jcxmmc.substring(1));
						$("#ajaxForm #jcxm").val(JSON.stringify(json))
					}else if($("#ajaxForm input:radio[name='sjqf']:checked").val()==null){
						$.alert("请选择送检区分！");
						return false;
					}else if(!$("#ajaxForm #kyDIV").is(":hidden") && $("#ajaxForm #kyxm option:selected").text()=="--请选择--"){
						$.alert("请选择立项编号！");
						return false;
					}else if($("#ajaxForm #yblx option:selected").text()=="--请选择--"){
						$.alert("请选择标本类型！");
						return false;
					}else if($("#ajaxForm #cyrq").val()==""||$("#ajaxForm #cyrq").val()==null){
						$.alert("请输入采样日期！");
						return false;
					}
					if (sfws==3){
						    if($("#ajaxForm #hospitalname").val()==""||$("#ajaxForm #hospitalname").val()==null){
								$.alert("请选择医院名称！");
								return false;
							}else if($("#ajaxForm #sjdw").val()==""||$("#ajaxForm #sjdw").val()==null){
								$.alert("医院名称不正确！");
								return false;
							}else if($("#ajaxForm #sjdwmc").val()==""||$("#ajaxForm #sjdwmc").val()==null){
								$.alert("请输入报告显示单位！");
								return false;
							}else if($("#ajaxForm #sjys").val()==""||$("#ajaxForm #sjys").val()==null){
								$.alert("请输入主治医师！");
								return false;
							}else if($("#ajaxForm #ks option:selected").text()=="--请选择--"){
								$.alert("请选择科室！");
								return false;
							}else if($("#ajaxForm #ybtj").val()==""||$("#ajaxForm #ybtj").val()==null){
								$.alert("请输入标本体积！");
								return false;
							}else if($("#ajaxForm #kdlx").val()==""||$("#ajaxForm #kdlx").val()==null){
								$.alert("请选择快递类型！");
								return false;
							}else if($("#ajaxForm #lczz").val()==""||$("#ajaxForm #lczz").val()==null){
								$.alert("请输入主诉！");
								return false;
							}else if($("#ajaxForm #qqzd").val()==""||$("#ajaxForm #qqzd").val()==null){
								$.alert("请输入前期诊断！");
								return false;
							}
					}

					if(/[^\a-\z\A-\Z0-9 -]/g.test($("#ajaxForm #ybbh").val())){
						$.error("请输入正确样本编号,禁用特殊字符！");
						return false;
					}
					//验证开票申请
					if($("#ajaxForm #sfsf").val()&&$("#ajaxForm #sfsf").val()!=0){
						var i=0;
						$("#ajaxForm .kpsq").each(function(){
							if(this.checked==true){
								i++;
							}
						})
						if(i==0){
							$.alert("请填写开票申请！");
							return false;
						}
					}
					if($("#ajaxForm input:radio[name='sfjs']:checked").val()=='1'&&($("#ajaxForm #jsrq").val()==""||$("#ajaxForm #jsrq").val()==null)){
						$.alert("接收日期为空,样本还未接收,是否检测请不要选择‘是’！");
						return false;
					}
					//验证快递单号
					var cskz2=$("#ajaxForm #kdlx").find("option:selected").attr("cskz2");
					if(!cskz2){
						if($("#ajaxForm #kdh").val()==""){
							$.alert("请填写快递单号！");
							return false;
						}
					}

					if($("#ajaxForm #nbbm").val()!=null){
						var nbbm=$("#ajaxForm #nbbm").val();
						$("#ajaxForm #px").val(nbbm.substring(1,nbbm.length-1));
					}
					if ($("#Sjxx_Save #formAction").val() == 'modSaveSjxx'){
						if($("#ajaxForm #wbsjxx_wbqtxx") && $("#ajaxForm #wbsjxx_wbqtxx").length > 0){
							var wbsjxxDtos = [];
							$("#ajaxForm #wbsjxx_wbqtxx").length
							for (var i = 0; i < $("#ajaxForm #wbsjxx_wbqtxx").length; i++) {
								try {
									// 新增 JSON 格式验证
									JSON.parse($("#ajaxForm #wbsjxx_wbqtxx")[i].value);
								} catch (e) {
								}
								var wbsjxxDto = {};
								wbsjxxDto["id"] = $("#ajaxForm #wbsjxx_wbqtxx")[i].name;
								wbsjxxDto["wbqtxx"] = $("#ajaxForm #wbsjxx_wbqtxx")[i].value;
								wbsjxxDtos.push(wbsjxxDto);
							}
							$("#ajaxForm #wbsjxxsJson").val(JSON.stringify(wbsjxxDtos));
						}
						if($("#ajaxForm #qtxx").val()){
							// 判断$("#ajaxForm #sjkzxx_qtxx").val()的值是否符合json格式
							try {
								// 新增 JSON 格式验证
								JSON.parse($("#ajaxForm #qtxx").val());
							} catch (e) {
							}
						}

					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
					if($("#ajaxForm #sfsf").val()=="0"&&$("#ajaxForm #sfje").val()>0){
						$.confirm('该标本已付费：'+$("#ajaxForm #sfje").val()+'元！，是否继续操作？',function(result){
							if(result){
								submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
									if(responseText["status"] == 'success'){
										$.success(responseText["message"],function() {
											if(opts.offAtOnce){
												$.closeModal(opts.modalName);
											}
											PCRAudit(responseText["bys"],responseText["jcdw"],responseText["sjid"],responseText["jcxmids"],responseText["ygzbys"]);
										});
									}else if(responseText["status"] == "fail"){
										preventResubmitForm(".modal-footer > button", false);
										$.error(responseText["message"],function() {
										});
									} else{
										preventResubmitForm(".modal-footer > button", false);
										$.alert(responseText["message"],function() {
										});
									}
								},".modal-footer > button");
							}
						})
					}else{
						submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
							if(responseText["status"] == 'success'){
								$.success(responseText["message"],function() {
									if(opts.offAtOnce){
										$.closeModal(opts.modalName);
									}
									PCRAudit(responseText["bys"],responseText["jcdw"],responseText["sjid"],responseText["jcxmids"],responseText["ygzbys"]);
								});
							}else if(responseText["status"] == "fail"){
								preventResubmitForm(".modal-footer > button", false);
								$.error(responseText["message"],function() {
								});
							} else{
								preventResubmitForm(".modal-footer > button", false);
								$.alert(responseText["message"],function() {
								});
							}
						},".modal-footer > button");
					}
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
//扩展参数修改
var modkzcsConfig = {
		width		: "800px",
		modalName	: "modkzcsConfig",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定", 
				className : "btn-primary",
				callback : function() {
					var $this = this;
					var opts = $this["options"]||{};
					$("#cskzFrom input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"cskzFrom",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchSjxxResult();
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
							preventResubmitForm(".modal-footer > button", false);
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

//反馈
var feedbackConfig = {
		width		: "800px",
		modalName	: "feedbackConfig",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定", 
				className : "btn-primary",
				callback : function() {
					var $this = this;
					var opts = $this["options"]||{};
					$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchSjxxResult();
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
							preventResubmitForm(".modal-footer > button", false);
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
//
var modDetectionConfig = {
		width		: "800px",
		modalName	: "modDetectionConfig",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					var $this = this;
					var opts = $this["options"]||{};
					var sfsytz="0";
					if($("#detectionForm #djcbj")=='1'){
			            if($("#detectionForm #dsyrqfirst").val()==null || $("#detectionForm #dsyrqfirst").val()==''){
			              $.confirm("请选择DNA实验日期！");
			              return false;
			            }
			          }
			          if($("#detectionForm #jcbj")=='1'){
			            if($("#detectionForm #syrqfirst").val()==null || $("#detectionForm #syrqfirst").val()==''){
			              $.confirm("请选择RNA实验日期！");
			              return false;
			            }
			          }
			          if($("#detectionForm #qtjcbj")=='1'){
			            if($("#detectionForm #qtsyrqfirst").val()==null || $("#detectionForm #qtsyrqfirst").val()==''){
			              $.confirm("请选择其他实验日期！");
			              return false;
			            }
			          }
					if(($("#detectionForm #syrqfirst").val()==null || $("#detectionForm #syrqfirst").val()=='') && ($("#detectionForm #syrq").val()!=null && $("#detectionForm #syrq").val()!='')){
						$("#detectionForm #sfsytz").val("1");
					}
					if(($("#detectionForm #dsyrqfirst").val()==null || $("#detectionForm #dsyrqfirst").val()=='') && ($("#detectionForm #dsyrq").val()!=null && $("#detectionForm #dsyrq").val()!='')){
						$("#detectionForm #sfsytz").val("1");
					}
					if(($("#detectionForm #qtsyrqfirst").val()==null || $("#detectionForm #qtsyrqfirst").val()=='') && ($("#detectionForm #qtsyrq").val()!=null && $("#detectionForm #qtsyrq").val()!='')){
						$("#detectionForm #sfsytz").val("1");
					}
					$("#detectionForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"detectionForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								if(responseText["ywid"]!=null && responseText["ywid"]!=''){
									//提交审核
									var auditType = responseText["auditType"];
									showAuditFlowDialog(auditType,responseText["ywid"],function(){
										searchSjxxResult();
									});
								}else{
									searchSjxxResult();
								}
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
							preventResubmitForm(".modal-footer > button", false);
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

    //添加送检信息模态框
var addSjxxConfig = {
		width		: "1000px",
		modalName	: "addSjxxModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
				    if(!$("#ajaxForm").valid()){// 表单验证
                        $.alert("请填写完整信息");
                        return false;
                    }
					$("#ajaxForm #syfjInfoTab #ywlx").remove();
					//去除空格
					$("#ajaxForm #db").val($("#ajaxForm #db").val().replace(/\s+/g,""));
					$("#ajaxForm #sjys").val($("#ajaxForm #sjys").val().trim());
					
					var cskz2=$("#ajaxForm #kdlx").find("option:selected").attr("cskz2");
					
					if($("#ajaxForm #ybbh").val()==""||$("#ajaxForm #ybbh").val()==null){
						$.alert("请输入标本编号！");
						return false;
					}else if($("#ajaxForm #hzxm").val()==""||$("#ajaxForm #hzxm").val()==null){
						$.alert("请输入患者姓名！");
						return false;
					}else if($("#ajaxForm input:radio[name='xb']:checked").val()==null){
						$.alert("请选择性别！");
						return false;
					}else if($("#ajaxForm #nl").val()==""||$("#ajaxForm #nl").val()==null){
						$.alert("请输入年龄！");
						return false;
					}else if($("#ajaxForm #sjdw").val()==""||$("#ajaxForm #sjdw").val()==null){
						$.alert("医院名称不正确！");
						return false;
					}else if($("#ajaxForm #sjys").val()==""||$("#ajaxForm #sjys").val()==null){
						$.alert("请输入主治医师！");
						return false;
					}else if($("#ajaxForm #sjdwmc").val()==""||$("#ajaxForm #sjdwmc").val()==null){
						$.alert("请输入报告显示单位！");
						return false;
					}else if($("#ajaxForm #ks option:selected").text()=="--请选择--"){
						$.alert("请选择科室！");
						return false;
					}else if($("#ajaxForm #qqzd").val()==""||$("#ajaxForm #qqzd").val()==null){
						$.alert("请输入前期诊断！");
						return false;
					}else if($("#ajaxForm #db").val()==""||$("#ajaxForm #db").val()==null){
						$.alert("请输入合作伙伴！");
						return false;
					}else if($("#ajaxForm .jcxm").length >0){
						var json = [];
						for (let i = 0; i <= $("#ajaxForm .jcxm").length -1; i++) {
							if ($("#ajaxForm #"+$("#ajaxForm .jcxm")[i].id).is(':checked')){
								var sz = {"jcxmid":'',"jczxmid":'',"flag":'',"csmc":'',"zcsmc":''};
								sz.jcxmid = $("#ajaxForm .jcxm")[i].id;
								sz.jczxmid = null;
								sz.csmc = $("#ajaxForm #" + $("#ajaxForm .jcxm")[i].id).attr("csmc");
								sz.zcsmc = null;
								sz.flag = $("#ajaxForm #" + $("#ajaxForm .jcxm")[i].id).attr("flag");
								json.push(sz)
							}
						}
						if (json.length == 0){
							$.alert("请选择检测项目！");
							return false;
						}
						if($("#ajaxForm .jczxm").length >0){
							for (let i = 0; i <= $("#ajaxForm .jczxm").length -1; i++) {
								if ($("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).is(':checked')) {
									var jcxmid = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("jcxmid")
									if (json.length > 0) {
										for (let k = json.length - 1; k >= 0; k--) {
											if (json[k].jcxmid == jcxmid && json[k].jczxmid == null) {
												json.splice(k, 1);
											}
										}
										var sz = {"jcxmid": '', "jczxmid": '',"flag":'',"csmc":'',"zcsmc":''};
										sz.jcxmid = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("jcxmid");
										sz.jczxmid = $("#ajaxForm .jczxm")[i].id;
										sz.csmc = $("#ajaxForm #" + sz.jcxmid).attr("csmc");
										sz.zcsmc = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("csmc");
										sz.flag = $("#ajaxForm #" + sz.jcxmid).attr("flag");
										json.push(sz)
									}
								}
							}
							for (let k = json.length - 1; k >= 0; k--) {
								if (json[k].flag == "1"){
									if (json[k].jczxmid == null){
										$.alert("请选择检测子项目！");
										return false;
									}
								}
							}
						}
						var jcxmmc = "";
						for (let i = 0; i < json.length; i++) {
							if (json[i].zcsmc){
								if (jcxmmc.indexOf(json[i].csmc + "-"+json[i].zcsmc)<0){ 
	                                jcxmmc+=","+json[i].csmc + "-"+json[i].zcsmc;
	                            }
                            }else{
								if (jcxmmc.indexOf(json[i].csmc)<0){ 
	                                jcxmmc+=","+json[i].csmc;
	                            }
							}
						}
						$("#ajaxForm #jcxmmc").val(jcxmmc.substring(1));
						$("#ajaxForm #jcxm").val(JSON.stringify(json))
					}else if($("#ajaxForm input:radio[name='sjqf']:checked").val()==null){
						$.alert("请选择送检区分！");
						return false;
					}else if(!$("#ajaxForm #kyDIV").is(":hidden") && $("#ajaxForm #kyxm option:selected").text()=="--请选择--"){
						$.alert("请选择立项编号！");
						return false;
					}else if($("#ajaxForm #yblx option:selected").text()=="--请选择--"){
						$.alert("请选择标本类型！");
						return false;
					}else if($("#ajaxForm #ybtj").val()==""||$("#ajaxForm #ybtj").val()==null){
						$.alert("请输入标本体积！");
						return false;
					}else if($("#ajaxForm #cyrq").val()==""||$("#ajaxForm #cyrq").val()==null){
						$.alert("请输入采样日期！");
						return false;
					}else if($("#ajaxForm #kdlx").val()==""||$("#ajaxForm #kdlx").val()==null){
						$.alert("请选择快递类型！");
						return false;
					}else if($("#ajaxForm #lczz").val()==""||$("#ajaxForm #lczz").val()==null){
						$.alert("请输入主诉！");
						return false;
					}else if(!cskz2){
						if($("#ajaxForm #kdh").val()==""){
							$.alert("请填写快递单号！");
							return false;
						}
					}
					if($("#ajaxForm input:radio[name='sfjs']:checked").val()=='1'&&($("#ajaxForm #jsrq").val()==""||$("#ajaxForm #jsrq").val()==null)){
						$.alert("接收日期为空,样本还未接收,是否检测请不要选择‘是’！");
						return false;
					}
//					var lczzs=0;
//					$("#ajaxForm .lczzs").each(function(){
//						if(this.checked){
//							lczzs++;
//						}
//					});
//					if(lczzs==0){
//						$.alert("请选择主诉！");
//						return false;
//					}
					if(/[^\a-\z\A-\Z0-9 -]/g.test($("#ajaxForm #ybbh").val())){
						$.error("样本编号只能包含英文、数字以及-");
						return false;
					}
					var qf=$("#ajaxForm input:radio[name='sjqf']:checked").val();
					var divisionList=JSON.parse($("#ajaxForm #divisionListJson").val());
					var kycsid;
					for (let i = 0; i < divisionList.length; i++) {
						if(divisionList[i].csmc=='科研'){
							kycsid=divisionList[i].csid;
						}
					}
					if(kycsid!=null||kycsid!=undefined){
						if(kycsid!=qf){
							$("#ajaxForm #kyxm").val("")
						}
					}

					$("#ajaxForm #divisionListJson").val()
					var $this = this;
					var opts = $this["options"]||{};
					$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								PCRAudit(responseText["bys"],responseText["jcdw"],responseText["sjid"],responseText["jcxmids"],"");
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
							preventResubmitForm(".modal-footer > button", false);
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
 	//修改合作伙伴
var moddbInspectionConfig={
		width		: "700px",
		modalName	: "moddbInspectionConfig",
		formName	: "ajaxForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		:	{
			success : 	{
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#ajaxForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					
					$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
					
					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									searchSjxxResult();
								}
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
							preventResubmitForm(".modal-footer > button", false);
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
		
}
var uploadInspectionConfig = {
	width		: "900px",
	modalName	: "uploadInspectionModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
				    $.alert("请填写完整信息!");
					return false;
				}
				if(!$("#ajaxForm #fjids").val() && !$("#ajaxForm #w_fjids").val() && !$("#ajaxForm #fjids_q").val() && !$("#ajaxForm #w_fjids_q").val() && !$("#ajaxForm #w_fjids_z").val()){
					$.error("请上传检测报告！")
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchSjxxResult();
							}
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
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
var confirmInspectionConfig = {
	width		: "1200px",
	modalName	: "confirmInspectionModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
				    $.alert("请填写完整信息!");
					return false;
				}
				if($("#ajaxForm #sfjs1").is(":checked")){
					if($("#ajaxForm #nbbm").val()==""){
						$.alert("您已选择接收，请填写内部编码");
						return false;
					}
				}
				var json = '';
				$("#ajaxForm #jclx input[type='checkbox']").each(function(index){
					if (this.checked){
						json+=","+$("#ajaxForm #"+this.value).attr("nbzbm")
					}
				});
				if (json==''){
					$.alert("请选择检测类型！");
					return false;
				}
				$("#ajaxForm #jclxs").val(json.substring(1));
				var nbbm=$("#ajaxForm #nbbm").val();
				$("#ajaxForm #px").val(nbbm.substring(1,nbbm.length-1));
				var $this = this;
				var opts = $this["options"]||{};
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				var szz=$('#ajaxForm input:radio[name="szz"]:checked').val();
				if(szz==$("#ajaxForm #ysszz").text()){
					$("#ajaxForm #grsz_flg").val(0);
				}else {
					$("#ajaxForm #grsz_flg").val(1);
				}
                var sfdy=$('#ajaxForm input:radio[name="sfdy"]:checked').val();
				if(sfdy==$("#ajaxForm #ysprintszz").text()){
					$("#ajaxForm #grsz_flg_two").val(0);
				}else {
					$("#ajaxForm #grsz_flg_two").val(1);
				}
                var flag=0;
                var fxsj_flag=false;
                $("#ajaxForm .ybztclass").each(function(){
                    if(this.checked==true){
                    	flag++;
                    	if ($(this).attr("cskz3")){
							fxsj_flag = true
						}
                    }
                });
                if(flag>0){
                	$.confirm("当前标本状态异常，是否继续确认？",function(result){
                		if(result){
                			submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
            					if(responseText["status"] == 'success'){
            						if (fxsj_flag){
										$.confirm("将发起标本退回操作，是否继续确认？",function(_result){
											if (_result){
												$.ajax({
													type:'post',
													url:"/risk/board/pagedataAddBoard",
													cache: false,
													data: {
														"ywid":$("#ajaxForm #sjid").val(),
														"access_token":$("#ac_tk").val(),
														"bz":$("#ajaxForm #bz").val(),
													},
													dataType:'json',
													success:function(data){
														if (data.status = 'success' && data.auditType && data.ywid){
															showAuditFlowDialog(data.auditType,data.ywid,function(_data){});
															clearInfo(responseText)
														}else{
															$.alert("标本退回通知发起失败！");
														}
													}
												});
											}else{
												clearInfo(responseText)
											}

										})
									}else{
										clearInfo(responseText)
									}
            					}else if(responseText["status"] == "fail"){
									preSelectYbbh = null;
									$("#ajaxForm #ybbh").val("");
            						preventResubmitForm(".modal-footer > button", false);
            						$.error(responseText["message"],function() {
            						});
            					} else{
            						preventResubmitForm(".modal-footer > button", false);
            						$.alert("不好意思！出错了！");
            					}
            				},".modal-footer > button");
                		}
                	});
                }else{
                	submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                	    console.log("开始请求")
    					if(responseText["status"] == 'success'){
							clearInfo(responseText);
    					}else if(responseText["status"] == "fail"){
							preSelectYbbh = null;
							$("#ajaxForm #ybbh").val("");
    						preventResubmitForm(".modal-footer > button", false);
    						$.error(responseText["message"],function() {
    						});
    					} else{
    						preventResubmitForm(".modal-footer > button", false);
    						$.alert("不好意思！出错了！");
    					}
    				},".modal-footer > button");
                }
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default",
			callback : function() {
				if (!$("#ajaxForm #key").val())
					return true;
				$.ajax({
					url: "/sseEmit/connect/pagedataCloseOA",
					data: {
						"access_token": $("#ac_tk").val(),
						"key": $("#ajaxForm #key").val(),
					}
				});
				return true;
			}
		}
	}
};

function clearInfo(responseText){
	preventResubmitForm(".modal-footer > button", false);
	if(responseText["print"] && responseText["sz_flg"] && "1"== responseText["sfdy"]){
		var host=responseText["print"];
		var sz_flg=responseText["sz_flg"];
		print_nbbm_confirm(host,sz_flg);
	}

	$.success(responseText["message"],function() {
		$("#ajaxForm #jclxs").val("")
		$("#ajaxForm #ybbh").val("");
		$("#ajaxForm #nbbm").val("");
		$("#ajaxForm #hzxm").text("");
		$("#ajaxForm #fjids").val("");
		$("#ajaxForm #bs").val("1");
		$("#ajaxForm #redisFj").remove();
		$("#ajaxForm #nl").text("");
		$("#ajaxForm #xbmc").text("");
		$("#ajaxForm #sjdw").text("");
		$("#ajaxForm #sjys").text("");
		$("#ajaxForm #xgsj").val("");
		$("#ajaxForm #ks").text("");
		$("#ajaxForm #db").text("");
		$("#ajaxForm #ybtj").text("");
		$("#ajaxForm #yblxmc").text("");
		$("#ajaxForm #yblxdm").val("");
		$("#ajaxForm #jcdwmc").val("");
		$("#ajaxForm #jcdw").val("");
		$("#ajaxForm #sjqfmc").val("");
		$("#ajaxForm #yblx_flg").val("");
		$("#ajaxForm #state").val("");
		$("#ajaxForm #jcxm").text("");
		$("#ajaxForm #lczz").text("");
		$("#ajaxForm #qqzd").text("");
		$("#ajaxForm #gzbymc").text("");
		$("#ajaxForm #sjid").val("");
		$("#ajaxForm .ybztclass").prop("checked",false)
		$("#ajaxForm .sfjs").prop("checked",false)
		$("#ajaxForm #bbsdwd").val("");
		$("#ajaxForm #bz").val("");
		$("#ajaxForm #nbbmspan").show();
		$("#ajaxForm #jstj").val("");
		$("#ajaxForm #print_flg").val("");
		$("#ajaxForm #ybzt_flg").val("");
		$("#ajaxForm #ybztmc").val("");
		$("#ajaxForm #ysdh").val("");
		$("#ajaxForm #kyxmmc").text("")
		$("#ajaxForm .fileinput-remove-button").click();
		$("#ajaxForm label[name='jclxs']").remove()
		$("#ajaxForm #button").remove()
		$("#ajaxForm #button2").remove()
		searchSjxxResult();
	});
}

var printInspectionConfig = {
	width		: "800px",
	modalName	: "printInspectionModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#printInspectionForm").valid()){
				    $.alert("请填写完整信息!");
					return false;
				}
				var json = '';
				$("#printInspectionForm #jclx input[type='checkbox']").each(function(index){
					if (this.checked){
						json+=","+$("#printInspectionForm #"+this.value).attr("nbzbm")
					}
				});
				if (json==''){
					$.alert("请选择检测类型！");
					return false;
				}
				$("#printInspectionForm #jclxs").val(json.substring(1));
				var $this = this;
				var opts = $this["options"]||{};
				$("#printInspectionForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"printInspectionForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						preventResubmitForm(".modal-footer > button", false);

						if(responseText["print"] && responseText["sz_flg"]){
							var host=responseText["print"];
							var sz_flg=responseText["sz_flg"];
							print_nbbm_confirm(host,sz_flg);
						}
						if(opts.offAtOnce){
							$.closeModal(opts.modalName);
						}
						searchSjxxResult();
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
						$.alert("不好意思！出错了！");
					}
				},".modal-footer > button");
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default",
		}
	}
};

/*   发送模态框*/
var sendMessageSjxxConfig = {
		width		: "1000px",
		modalName	: "sendMessageModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#sendMessageForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#sendMessageForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"sendMessageForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchSjxxResult();
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
							preventResubmitForm(".modal-footer > button", false);
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

var setSjxxListFieldsConfig = {
	width : "1020px",
	modalName : "setSjxxListFieldsConfig",
	buttons : {
		reelect : {
			label : "恢复默认",
			className : "btn-info",
			callback : function() {
				$("#ajaxForm").attr("action","/common/title/commTitleDefaultSave");
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				submitForm("ajaxForm", function(responseText,statusText) {
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							//关闭Modal
							$.closeModal("setSjxxListFieldsConfig");
							$('#sjxx_formSearch #sjxx_list').bootstrapTable('destroy');
							t_sjxxfieldList = getSjxxDataColumn(responseText.choseList,responseText.waitList);
							
							var oTable = new Inspection_TableInit(t_sjxxfieldList,false);
							oTable.Init();
						});
					}else{
					$.error(responseText["message"],function() {
                                            });
					}					
					preventResubmitForm(".modal-footer > button", false);
				}, ".modal-footer > button");
				return false;
			}
		},
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var _choseListDiv = $("#ajaxForm #list2 div[id='choseListDiv']");
				if(_choseListDiv.length < 1 ){
					$.error("请选择显示字段。")
					return false;
				}
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				submitForm("ajaxForm", function(responseText,statusText) {
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							//关闭Modal
							$.closeModal("setSjxxListFieldsConfig");
							$('#sjxx_formSearch #sjxx_list').bootstrapTable('destroy');
							t_sjxxfieldList = getSjxxDataColumn(responseText.choseList,responseText.waitList);
							
							var oTable = new Inspection_TableInit(t_sjxxfieldList,false);
							oTable.Init();
						});
					}else{
						$.error(responseText["message"],function() {
                                                                    });
					}					
					preventResubmitForm(".modal-footer > button", false);
				}, ".modal-footer > button");
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
var recheckInspectionConfig={
		width		: "800px",
		modalName	: "recheckInspectionConfig",
		formName	: "recheck_Form",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		:	{
			success : 	{
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#recheck_Form").valid()){
						$.alert("请填写完整信息！");
						return false;
					}
					var bgbjval = $("#recheck_Form input:radio[name='bgbj']:checked").val()//重发为是：1，否：0
					var sfffval = $("#recheck_Form #sfff").val()//收费1：不收费0
					if ( (sfffval=='1' && bgbjval=='0') ){
						$.alert("报告重发和是否付费匹配不符合规范！");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#recheck_Form input[name='access_token']").val($("#ac_tk").val());
					var ybztcskz = $("#recheck_Form #ybztcskz").val();
					if(ybztcskz=="1"){
						$.alert("量仅一次");
						return false;
					}
					if($("#recheck_Form .yy")!=null && $("#recheck_Form .yy").length>0){
						var yys="";
						var yymcs="";
						for(var i=0;i<$("#recheck_Form .yy").length;i++){
							if($("#recheck_Form .yy")[i].checked){
								yys=yys+","+$("#recheck_Form .yy")[i].value;
								yymcs=yymcs+";"+$("#recheck_Form #yy_"+$("#recheck_Form .yy")[i].value+" span").text();
							}
						}
						$("#recheck_Form #yys").val(yys.substring(1));
						$("#recheck_Form #yy").val(yymcs.substring(1));
					}
					if(!$("#recheck_Form #jczxmDiv").is(":hidden")){
						if(!$("#recheck_Form #jczxm").val()){
							$.alert("请选择子项目！");
							return false;
						}
					}
					submitForm(opts["formName"]||"recheck_Form",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							//提交审核
							if(responseText["auditType"]!=null){
								showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
									searchSjxxResult();
								});
							}else{
								searchSjxxResult();
							}
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
							preventResubmitForm(".modal-footer > button", false);
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
}
var reportDownloadConfig={
		width		: "800px",
		modalName	: "reportDownloadConfig",
		formName	: "recheck_Form",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		:	{
		success 	: 	{
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var bglxbj = $("#ajaxForm input[type='radio']:checked").val();
	    		var map = {
	    			access_token:$("#ac_tk").val(),
	    			bglxbj:bglxbj,
	    		}; 
	    		//判断有选中的采用选中导出，没有采用选择导出
	    		var sel_row = $('#sjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
	    		var ids="";
	    		if(sel_row.length>=1){
	        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
	        			ids = ids + ","+ sel_row[i].sjid;
	        		}
	        		ids = ids.substr(1);
	        		map["ids"] = ids;
	    		}else{
	    			getSjxxSearchData(map);
	    		}
	    		var url= $("#ajaxForm #action").val();
				$.post(url,map,function(data){
					if(data){
						if(data.status == 'success'){
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							//创建objectNode
							var cardiv =document.createElement("div");
							cardiv.id="cardiv";
							var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span id="totalCount" style="color:red;font-weight:600;">/' + data.count + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
							cardiv.innerHTML=s_str;
							//将上面创建的P元素加入到BODY的尾部
							document.body.appendChild(cardiv);
							setTimeout("checkReportZipStatus(2000,'"+ data.redisKey + "')",1000);
							//绑定导出取消按钮事件
							$("#exportCancel").click(function(){
								//先移除导出提示，然后请求后台
								if($("#cardiv").length>0) $("#cardiv").remove();
								$.ajax({
									type : "POST",
									url : "/common/export/commCancelExport",
									data : {"wjid" : data.redisKey+"","access_token":$("#ac_tk").val()},
									dataType : "json",
									success:function(res){
										if(res != null && res.result==false){
											if(res.msg && res.msg!="")
												$.error(res.msg);
										}

									}
								});
							});
						}else if(data.status == 'fail'){
							$.error(data.error,function() {
							});
						}else{
							preventResubmitForm(".modal-footer > button", false);
							if(data.error){
								$.alert(data.error,function() {
								});
							}
						}
					}
				},'json');
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}	
}

// 退款模态框
var payRefundConfig = {
	width		: "1200px",
	modalName	: "payRefundModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

//获取表格显示形式
function getSjxxDataColumn(zdList,waitList){
	var flelds = [];
	var map = {};
	var item = null;
	map = {};
	map["checkbox"] = true;
	flelds.push(map);
	$.each(zdList,function(i){
		item = zdList[i];
		map = {};
		map["title"] = item.xszdmc;
		map["titleTooltip"] = item.zdsm;
		map["field"] = item.xszd;
		if(item.pxzd&&item.pxzd!="")
			map["sortable"] = true;
		map["sortName"] = item.pxzd;
		map["width"] = item.xskd + "%";
		if(item.xsgs){
			map["formatter"] = eval(item.xsgs);
		}
		map["visible"]=true;
		flelds.push(map);
	});
	$.each(waitList,function(i){
		item = waitList[i];
		map = {};
		map["title"] = item.xszdmc;
		map["titleTooltip"] = item.zdsm;
		map["field"] = item.xszd;
		if(item.pxzd&&item.pxzd!="")
			map["sortable"] = true;
		map["sortName"] = item.pxzd;
		map["width"] = item.xskd + "%";
		if(item.xsgs){
			map["formatter"] = eval(item.xsgs);
		}
		if(zdList.length ==0){
			if(waitList[i].mrxs == '0'){
				map["visible"] = false;
			}else if(waitList[i].mrxs == '9'){
				map["visible"] = false;
			}
		}else
			map["visible"]=false;
		flelds.push(map);
	});
	return flelds;
}

runEvery10Sec = function () {
	if($("#sjNum").length > 0){
		$.ajax({
			"dataType" : 'json',
	        "type" : "POST",
	        "url" : "/inspection/inspection/pagedataTotal",
	        "data" : { "access_token" : $("#ac_tk").val(),"jcxmids" : $("#sjxx_formSearch #jcxmids").val(),"jczxmids" : $("#sjxx_formSearch #jczxmids").val(),"jclx":$("#sjxx_formSearch #jclx").val(),"jczlx":$("#sjxx_formSearch #jczlx").val(),"querydivision":$("#sjxx_formSearch #querydivision").val(),"zyid":$("#sjxx_formSearch #zyid").val()},
	        "success" : function(data) {
	        	$("#sjNum").text(data.sjxxNum);
	        	$("#scNum").text(data.fjNum);
	        	$("#fjyzNumBefore").text(data.fjyzNumBefore);
	        	$("#fjyzNumAfter").text(data.fjyzNumAfter);
	        	if(data.sjxxNum != 0 || data.fjNum != 0){
	        		setTimeout( runEvery10Sec, 1000 * 60 );
	        	}

	        	if(data.listmap){
	        	    for(var i=0;i<data.listmap.length;i++){
	        	    var Html="";
	        	    if(data.listmap[i].listtable){
	        	        Html += "<div class='col-xs-4 col-sm-3 col-md-1 col-lg-1 div1' onclick='lookdatamap(\""+data.listmap[i].listtable+"\")'>";
	        	    }else{
	        	        Html += "<div class='col-xs-4 col-sm-3 col-md-1 col-lg-1 div1' >";
	        	    }
                    Html +="<div class='divText'>"+data.listmap[i].title+"</div>"+
                    "<div class='divNum'>"+data.listmap[i].value+"</div>"+
                    "</div>";
                    $("#listmap").empty();
                    $("#listmap").append(Html);
	        	    }
	        	}
	        }
		});
	}
}

function lookdatamap(listable){
    var titleHtml ="<tr><th><b>标本编码</b></th><th><b>内部编码</b></th><th><b>患者姓名</b></th><th><b>类型</b></th><th style='padding-left:5px;'><b>状态</b></th></tr>";
    	$.ajax( {
    	       "dataType" : 'json',
    	       "type" : "POST",
    	       "url" : "/inspection/inspection/pagedataQueryListTable",
    	       "data" : {
    	    	   		"access_token" : $("#ac_tk").val(),
    			   		"cxmc" : listable
    	    	   		},
    	      "success" : function(map) {
    	    	  var ent=  map.listtable;
    	    	  if(ent.length>0){
    	    		  for (var i=0;i<ent.length;i++){
    	    			  titleHtml = titleHtml +
    		    	            	"<tr>" +
    		    	                "<td>" + ent[i].ybbh + "</td>" +
    		    	                "<td>" + ent[i].nbbm + "</td>" +
    		    	                "<td>" + ent[i].hzxm + "</td>" +
    		    	                "<td>" + ent[i].lxmc + "</td>" +
    		    	                "<td style='padding-left:5px;'>" + ent[i].zt+ "</td>" +
    		    	                "</tr>";
    	    		  }
        			  $("#tbody").html(titleHtml);
    	    		  $('#sjxxBodyHead').click(function(){
    		    			if($("#showName").css("display")=="block"){
    		    				$("#showName").hide();
    		    				$("#tbody").html("");
    		    				$('#sjxxBodyHead').unbind("click");
    		    				return;
    		    			}
    	    		  })

    	    		  $('#sjxxBody').click(function(){
    	    			  if($("#showName").css("display")=="block"){
    	    				  $("#showName").hide();
    	    				  $("#tbody").html("");
    	    				  $('#sjxxBody').unbind("click");
    	    				  return;
    	    			  }
    	    		  })
    	    		  var e_ = window.event || e; // 兼容IE，FF事件源
    	    		  var x = e_.clientX,y = e_.clientY; // 获取鼠标位置
    	    		  $("#tabxs").text("只显示前5条");
    	    		  $("#showName").css({"left":x -$("#sjxxBodyHead").offset().left,"top":y , "display":"block"});
    	    	  }
    	      }
    	});
}

function lookSj(event){
	var titleHtml ="<tr><th><b>标本编码</b></th><th><b>内部编码</b></th><th><b>患者姓名</b></th><th><b>类型</b></th><th style='padding-left:5px;'><b>PCR情况</b></th></tr>";
	$.ajax( {
	       "dataType" : 'json',
	       "type" : "POST",
	       "url" : "/inspection/inspection/pagedataQuerySjxx",
	       "data" : { 
	    	   		"access_token" : $("#ac_tk").val(),
			   		"jcxmids" : $("#sjxx_formSearch #jcxmids").val(),
			   		"jczxmids" : $("#sjxx_formSearch #jczxmids").val()
	    	   		},
	      "success" : function(map) {  
	    	  $("#sjNum").text(map.sjxxNum);
	    	  $("#scNum").text(map.fjNum);
	    	  if(map.sjxxDtos_t == null){
	    		  return;
	    	  }
	    	  var ent=  map.sjxxDtos_t;
	    	  if(ent.length>0){
	    		  for (var i=0;i<ent.length;i++){
	    			  titleHtml = titleHtml + 
		    	            	"<tr>" +
		    	                "<td>" + ent[i].ybbh + "</td>" +
		    	                "<td>" + ent[i].nbbm + "</td>" +
		    	                "<td>" + ent[i].hzxm + "</td>" +
		    	                "<td>" + ent[i].lxmc + "</td>" +
		    	                "<td style='padding-left:5px;'>" + ent[i].shxx_dqgwmc + "</td>" +
		    	                "</tr>";
	    		  }
    			  $("#tbody").html(titleHtml);
	    		  $('#sjxxBodyHead').click(function(){
		    			if($("#showName").css("display")=="block"){
		    				$("#showName").hide();
		    				$("#tbody").html("");
		    				$('#sjxxBodyHead').unbind("click");
		    				return;
		    			}
	    		  })

	    		  $('#sjxxBody').click(function(){
	    			  if($("#showName").css("display")=="block"){
	    				  $("#showName").hide();
	    				  $("#tbody").html("");
	    				  $('#sjxxBody').unbind("click");
	    				  return;
	    			  }
	    		  })
	    		  var e_ = window.event || e; // 兼容IE，FF事件源
	    		  var x = e_.clientX,y = e_.clientY; // 获取鼠标位置
	    		  $("#tabxs").text("只显示前5条");
	    		  $("#showName").css({"left":x -$("#sjxxBodyHead").offset().left,"top":y , "display":"block"});
	    	  }
	      }
	});
}

function lookFjyz(event,id){
	var titleHtml ="<tr><th><b>标本编码</b></th><th><b>内部编码</b></th><th><b>类型</b></th><th><b>申请时间</b></th><th style='padding-left:5px;'><b>状态</b></th></tr>";
	$.ajax( {
	       "dataType" : 'json',
	       "type" : "POST",
	       "url" : "/inspection/inspection/pagedataQuerySjxx",
	       "data" : {
	    	   		"access_token" : $("#ac_tk").val(),
			   		"jcxmids" : $("#sjxx_formSearch #jcxmids").val(),
			   		"jczxmids" : $("#sjxx_formSearch #jczxmids").val(),
	    	   		"flg_qf":id
	    	   		},
	      "success" : function(map) {
	    	  $("#fjyzNumBefore").text(map.fjyzNumBefore);
	    	   $("#fjyzNumAfter").text(map.fjyzNumAfter);
	    	  if(map.sjxxDtos_t == null){
	    		  return;
	    	  }
	    	  var ent=  map.sjxxDtos_t;
	    	  if(ent.length>0){
	    		  for (var i=0;i<ent.length;i++){
	    			  titleHtml = titleHtml +
		    	            	"<tr>" +
		    	                "<td>" + ent[i].ybbh + "</td>" +
		    	                "<td>" + ent[i].nbbm + "</td>" +
		    	                "<td>" + ent[i].lxmc + "</td>" +
		    	                "<td>" + ent[i].shxx_sqsj + "</td>" +
		    	                "<td style='padding-left:5px;'>" + ent[i].shxx_dqgwmc + "</td>" +
		    	                "</tr>";
	    		  }
    			  $("#tbody").html(titleHtml);
	    		  $('#sjxxBodyHead').click(function(){
		    			if($("#showName").css("display")=="block"){
		    				$("#showName").hide();
		    				$("#tbody").html("");
		    				$('#sjxxBodyHead').unbind("click");
		    				return;
		    			}
	    		  })

	    		  $('#sjxxBody').click(function(){
	    			  if($("#showName").css("display")=="block"){
	    				  $("#showName").hide();
	    				  $("#tbody").html("");
	    				  $('#sjxxBody').unbind("click");
	    				  return;
	    			  }
	    		  })
	    		  var e_ = window.event || e; // 兼容IE，FF事件源
	    		  var x = e_.clientX,y = e_.clientY; // 获取鼠标位置
	    		  $("#tabxs").text("只显示前10条");
	    		  $("#showName").css({"left":x -$("#sjxxBodyHead").offset().left,"top":y , "display":"block"});
	    	  }
	      }
	});
}


function dowDiv(){
	$("#showName").hide();
}
function lookWord(event){
	var titleHtml ="<tr><th><b>标本编码</b></th><th><b>内部编码</b></th><th><b>患者姓名</b></th><th><b>类型</b></th><th style='padding-left:5px;'><b>PCR情况</b></th></tr>";
	$.ajax( {
	       "dataType" : 'json',
	       "type" : "POST",
	       "url" : "/inspection/inspection/pagedataQueryWord",
	       "data" : { 
	    	   		"access_token" : $("#ac_tk").val(),
			   		"jcxmids" : $("#sjxx_formSearch #jcxmids").val(),
			   		"jczxmids" : $("#sjxx_formSearch #jczxmids").val(),
			   		"jclx" : $("#sjxx_formSearch #jclx").val(),
                    "jczlx" : $("#sjxx_formSearch #jczlx").val()
	    	   		},
	      "success" : function(map) {  
	    	  $("#sjNum").text(map.sjxxNum);
	    	  $("#scNum").text(map.fjNum);
	    	  var ent=  map.sjxxDtos_t;
	    	  if(map.sjxxDtos_t == null){
	    		  return;
	    	  }	    		  		    	      
    		  for (var i=0;i<ent.length;i++){
    			  titleHtml = titleHtml + 
	            	"<tr>" +
	                "<td>" + ent[i].ybbh + "</td>" +
	                "<td>" + ent[i].nbbm + "</td>" +
	                "<td>" + ent[i].hzxm + "</td>" +
	                "<td>" + ent[i].lxmc + "</td>" +
	                "<td style='padding-left:5px;'>" + ent[i].shxx_dqgwmc + "</td>" +
	                "</tr>";
    			  $("#tbody").html(titleHtml);
	    		  $('#sjxxBodyHead').click(function(){
		    			if($("#showName").css("display")=="block"){
		    				$("#showName").hide();
		    				$("#tbody").html("");
		    				$('#sjxxBodyHead').unbind("click");
		    				return;
		    			}
	    		  })
	    		  $('#sjxxBody').click(function(){
	    			  if($("#showName").css("display")=="block"){
	    				  $("#showName").hide();
	    				  $("#tbody").html("");
	    				  $('#sjxxBody').unbind("click");
	    				  return;
	    			  }
	    		  })
	    		  var e_ = window.event || e; // 兼容IE，FF事件源
	    		  var x = e_.clientX,y = e_.clientY; // 获取鼠标位置
	    		  $("#tabxs").text("只显示前5条");
	    		  $("#showName").css({"left":x -$("#sjxxBodyHead").offset().left,"top":y , "display":"block"});
	    	  }
	      }
	});
}



function print_ceshi(host,sz_flg){
	var print_url=null
	if(sz_flg=="0"){
		print_url="http://localhost"+host;
	}else if(sz_flg=="1"){
		var glxx=$("#ajaxForm #glxx").val();
		print_url="http://"+glxx+host;
	}
	var xhr = new XMLHttpRequest();
//连接地址，准备数据
	xhr.open("get",print_url,true);
//接收数据完成触发的事件
	xhr.onload =function(){}
//发送数据
	xhr.send();
}

function print_nbbm_confirm(host,sz_flg){
	var print_url=null
	if(sz_flg=="0"){
		print_url="http://localhost"+host;
		var openWindow = window.open(print_url);
		setTimeout(function(){
			openWindow.close();
		}, 600);
	}else if(sz_flg=="1"){
		var glxx=$("#ajaxForm #glxx").val();
		print_url="http://"+glxx+host;
		var openWindow = window.open(print_url);
		setTimeout(function(){
			openWindow.close();
		}, 600);
	}
}
function searchSjxxResult(isTurnBack){
	dealSpaceQuery("#sjxx_formSearch #cxnr");//查询截取空格处理方法
	//关闭高级搜索条件
	$("#sjxx_formSearch #searchMore").slideUp("low");
	Inspection_turnOff=true;
	$("#sjxx_formSearch #sl_searchMore").html("高级筛选");

	if(isTurnBack){
	    if(this.showTableFlag==true){
            this.showTableFlag=false
            $('#sjxx_formSearch #sjxx_list').bootstrapTable('refresh',{pageNumber:1});
        }
	}else{
        if(this.showTableFlag==true){
            this.showTableFlag=false
		    $('#sjxx_formSearch #sjxx_list').bootstrapTable('refresh');
        }
	}
}

document.onkeydown=function(e){
	if(e.keyCode==123)
		return false;
};

$("#sjxx_formSearch #cxnr").on('keydown',function(e){
	if(e.keyCode==74 && $("#cxnr").val().length >=9){
		return false;
	}
});

function PCRAudit(bys,jcdw,sjid,jcxmids,ygzbys) {
	if (bys!=null && bys!= ''){
		$.ajax({
			type: "post",
			url: "/inspection/auditProcess/pagedataPCRAudit",
			data: {"bys" : bys.toString(),"jcdw" : jcdw.toString(),"sjid" : sjid.toString(),"jcxmids" : jcxmids.toString(),"access_token":$("#ac_tk").val(),"ygzbys":ygzbys.toString()},
			dataType: "json",
			success: function (responseText) {
				if(responseText["status"] == 'success'){
					preventResubmitForm(".modal-footer > button", false);

				}else if(responseText["status"] == "fail"){
					preventResubmitForm(".modal-footer > button", false);

				}
			},
			fail: function (result) {

			}
		})
	}
	searchSjxxResult();
}
//是否显示实付总金额
function sfxsSfzje(){
    var xszds =  $("#sjxx_formSearch #xszds").val().split(",");
    var flag = false;
    for (var i = 0; i < xszds.length; i++) {
        if (xszds[i]=='sfzje'){
            flag=true;
        }
    }
    if (flag){
        $("#sjxx_formSearch #sfmoney").removeClass("hidden");
        sfzjezdqx = 1;
    } else {
        $("#sjxx_formSearch #sfmoney").addClass("hidden");
        sfzjezdqx = 0;
    }
}
//是否显示退款总金额
function sfxsTkzje(){
    var xszds =  $("#sjxx_formSearch #xszds").val().split(",");
    var flag = false;
    for (var i = 0; i < xszds.length; i++) {
        if (xszds[i]=='tkzje'){
            flag=true;
        }
    }
    if (flag){
        $("#sjxx_formSearch #tkmoney").removeClass("hidden");
        tkzjezdqx = 1;
    } else {
        $("#sjxx_formSearch #tkmoney").addClass("hidden");
        tkzjezdqx = 0;
    }
}

$(function(){
	changoption();
    //是否显示实付总金额
    sfxsSfzje();
    //是否显示退款总金额
    sfxsTkzje();
	//0.界面初始化
    // 1.初始化Table
    var oTable = new Inspection_TableInit(sjxxFieldList,true);
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new Inspection_ButtonInit();
    oButtonInit.Init();
	runEvery10Sec();
	// 所有下拉框添加choose样式
    jQuery('#sjxx_formSearch .chosen-select').chosen({width: '100%'});
	// 初始绑定显示更多的事件
	$("#sjxx_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	if($("#sjxx_formSearch #menu_jump").val()){
		$("#sjxx_formSearch #btn_"+$("#sjxx_formSearch #menu_jump").val()).click()
	}
});


function changoption(){
	var val1=$("#cxtj1").val()
	var val2=$("#cxtj2").val()
	var val=$("#cxtj").val()
	$("#cxtj option").each(function(){
		$(this).attr("disabled",false)
	})
	$("#cxtj1 option").each(function(){
		$(this).attr("disabled",false)
	})
	$("#cxtj2 option").each(function(){
		$(this).attr("disabled",false)
	})
	if(val){
		$("#sjxx_select1_"+val).attr("disabled",true)
		$("#sjxx_select2_"+val).attr("disabled",true)
	}
	if(val1){
		$("#sjxx_select_"+val1).attr("disabled",true)
		$("#sjxx_select2_"+val1).attr("disabled",true)
	}
	if(val2){
		$("#sjxx_select_"+val2).attr("disabled",true)
		$("#sjxx_select1_"+val2).attr("disabled",true)
	}
	$("#sjxx_select_"+val).attr("selected","selected");
	$("#sjxx_select1_"+val1).attr("selected","selected");
	$("#sjxx_select2_"+val2).attr("selected","selected");

	$("#sjxx_formSearch #cxtj").trigger("chosen:updated");
	$("#sjxx_formSearch #cxtj1").trigger("chosen:updated");
	$("#sjxx_formSearch #cxtj2").trigger("chosen:updated");
}