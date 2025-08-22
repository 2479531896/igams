var Hospital_TableInit=function(){
	 var oTableInit = new Object();
	 oTableInit.Init = function (){
		 $('#hospital_formSearch #hospital_list').bootstrapTable({
			 url: '/wechat/hospital/pagedataListHospital',         //请求后台的URL（*）
	            method: 'get',                      //请求方式（*）
//	            toolbar: '#hospital_formSearch #toolbar',                //工具按钮用哪个容器
	            striped: true,                      //是否显示行间隔色
	            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
	            pagination: true,                   //是否显示分页（*）
	            paginationShowPageGo: true,         //增加跳转页码的显示
	            sortable: true,                     //是否启用排序
	            sortName: "yyxx.lrsj",				//排序字段
	            sortOrder: "desc",                   //排序方式
	            queryParams: oTableInit.queryParams,//传递参数（*）
	            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
	            pageNumber:1,                       //初始化加载第一页，默认第一页
	            pageSize: 5,                       //每页的记录行数（*）
	            pageList: [5, 10, 15],        //可供选择的每页的行数（*）
	            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
	            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
	            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
	            strictSearch: true,
//	            showColumns: true,                  //是否显示所有的列
//	            showRefresh: true,                  //是否显示刷新按钮
	            minimumCountColumns: 2,             //最少允许的列数
	            clickToSelect: true,                //是否启用点击选中行
	            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
	            uniqueId: "dwid",                     //每一行的唯一标识，一般为主键列
//	            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
//	            cardView: false,                    //是否显示详细视图
	            detailView: false,                   //是否显示父子表
	            /*rowStyle:rowStyle,*/               //通过自定义函数设置行样式
	            columns: [
//	            	{
//	                  checkbox: true,
//	                  width: '3%'
//	            },
	            {
	                field: 'dwid',
	                title: '单位ID',
	                width: '10%',
	                align: 'left',
	                visible:false
	            },{
	                field: 'dwmc',
	                title: '单位名称',
	                width: '60%',
	                align: 'left',
	                visible:true
	            },{
	                field: 'dwjc',
	                title: '单位简称',
	                width: '40%',
	                align: 'left',
	                visible:true
	            },{
	                field: 'dwqtmc',
	                title: '单位其他名称',
	                width: '33%',
	                align: 'left',
	                visible:false
	            }],
	            onLoadSuccess:function(){
	            },
	            onLoadError:function(){
	            	alert("数据加载失败！");
	            },
	            onClickRow: function (row, $element) {
	            	selectHospital(row.dwid,row.dwmc);
	             },
		 });
		 $("#hospital_formSearch #hospital_list").colResizable({
			 liveDrag:true, 
			 gripInnerHtml:"<div class='grip'></div>", 
			 draggingClass:"dragging", 
			 resizeMode:'fit', 
			 postbackSafe:true,
			 partialRefresh:true}    
	        );
	 }
	 oTableInit.queryParams=function(params){
			var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
	        	pageSize: params.limit,   // 页面大小
	        	pageNumber: (params.offset / params.limit) + 1,  // 页码
	            sortName: params.sort,      // 排序列名
	            sortOrder: params.order, // 排位命令（desc，asc）
	            sortLastName: "yyxx.dwid", // 防止同名排位用
	            sortLastOrder: "asc" // 防止同名排位用
	            // 搜索框使用
	            // search:params.search
	        };
			return getHospitalSearchData(map);
		};
		return oTableInit
}

function getHospitalSearchData(map){
	var cxnr=$.trim(jQuery('#hospital_formSearch #searchParam').val());
	map["searchParam"]=cxnr
	return map
}

function searchHospitaleResult(isTurnBack){
	if(isTurnBack){
		$('#hospital_formSearch #hospital_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#hospital_formSearch #hospital_list').bootstrapTable('refresh');
	}
}

function searchDoctorResult(isTurnBack){
	var ddid=$("#saveFrom #ddid").val();
	var wxid=$("#saveFrom #wxid").val();
	var sjys=$("#searchParamInfo").val();
	$("#mybody").load("/wechat/sjysxx/getSjysxxList?ddid="+ddid+"&wxid="+wxid+"&sjys="+sjys);
}

/**
 * 新建按钮
 * @returns
 */
function createSjkjxx(){
	$("#mybody #btn_addKjxx").hide();
	$("#mybody #tab").hide();
	$("#mybody #tabsearch").hide();
	$("#mybody #fromdiv").hide();
	$("#mybody #hospital_formSearch").show();
}

/**
 * 医院信息返回按钮
 * @returns
 */
function backToFirst(){
	$("#mybody #btn_addKjxx").show();
	$("#mybody #tab").show();
	$("#mybody #fromdiv").hide();
	$("#mybody #hospital_formSearch").hide();
}

/**
 * 选择送检单位信息
 * @param dwid
 * @param dwmc
 * @returns
 */
function selectHospital(dwid,dwmc){
	$("#mybody #btn_addKjxx").hide();
	$("#mybody #tab").hide();
	$("#mybody #hospital_formSearch").hide();
	$("#mybody #fromdiv").show();
	$("#mybody #sjdw").val(dwid);
	$("#mybody #dwmc").val(dwmc);
}
/**
 * 取消按钮
 * @returns
 */
function cancelSave(){
	$("#mybody #btn_addKjxx").show();
	$("#mybody #tab").show();
	$("#mybody #fromdiv").hide();
	$("#mybody #sjdw").val(null);
	$("#mybody #dwmc").val(null);
}
$("#mybody #ks").change('on',function(){
	$("#mybody .ksmc").each(function(){
		if(this.selected){
			if($("#"+this.id).attr("cskz")=="1"){
				$("#qtks_div").show();
				$("#qtks_div #qtks").removeAttr("disabled");
			}else{
				$("#qtks_div").hide();
				$("#qtks_div #qtks").attr("disabled","disabled");
			}
		}
	})
})

/**
 * 切换操作按钮显示
 * @returns
 */
function changeBtnClick(){
	if($("#mybody .copyBtn").is(":hidden")){
		$("#mybody .copyBtn").show();
		$("#mybody .removeBtn").hide();
	}else{
		$("#mybody .copyBtn").hide();
		$("#mybody .removeBtn").show();
	}
}
/**
 * 复制送检医生信息
 * @param event
 * @returns
 */
function copysjyxx(tr,event){
	event.stopPropagation();
	$("#mybody #btn_addKjxx").hide();
	$("#mybody #tab").hide();
	$("#mybody #hospital_formSearch").hide();
	$("#mybody #fromdiv").show();
	var dwmc=tr.children().eq(0).text();
	var ysdh=tr.children().eq(1).text();
	var kskzcs=tr.children().eq(2).attr("kzcs");
	var dwid=tr.children().eq(2).attr("dwid")
	var	qtks=tr.children().eq(2).text();
	var sjys=tr.children().eq(3).text();
	var sjhb=tr.children().eq(4).text();
	var jcdw=tr.children().eq(5).text();
	var sjdw=tr.children().eq(8).text();
	//送检单位
	$("#mybody #fromdiv #sjdw").val(sjdw);
	//单位名称
	$("#mybody #fromdiv #dwmc").val(dwmc);
	//科室
	$("#mybody #fromdiv #ks").find("option[value='"+dwid+"']").prop('selected','true');
	//其他科室
	if(kskzcs=="1"){
		$("#mybody #qtks").val(qtks);
		$("#mybody #qtks_div").show();
		$("#mybody #qtks").removeAttr("disabled","true");
	}else{
		$("#ajaxForm #qtks").val(null);
		$("#mybody #qtks_div").hide();
		$("#mybody #qtks").attr("disabled","true");
		
	}
	//检测单位
	$("#mybody #fromdiv #jcdw").find("option[value='"+jcdw+"']").prop('selected','true');
//	//送检医生
//	$("#mybody #fromdiv #sjys").val(sjys);
//	//医生电话
//	$("#mybody #fromdiv #ysdh").val(ysdh);
	//合作伙伴
	$("#mybody #fromdiv #dbm").val(sjhb);
}


/**
 * 保存按钮
 * @returns
 */
function saveKjxx(){
	if($("#saveFrom #dwmc").val()==""){
			$.alert("请输入医院名称");
	}else if($("#saveFrom #ks").val()==""){
			$.alert("请输入科室");
	}else if($("#saveFrom #jcdw").val()==""){
			$.alert("请选择检测单位");
	}else if($("#saveFrom #sjys").val()==""){
			$.alert("请输入主治医师");
	}else if($("#saveFrom #dbm").val()==""){
			$.alert("请输入代表名");
	}else{
		$("#saveFrom #savekjxx").attr("disabled","true");
		$.ajax({
			url:'/wechat/sjysxx/saveSjysxx',
			type:'post',
			dataType:'json',
			data:$("#fromdiv #saveFrom").serialize(),
			success:function(data){
				if(data.state=="success"){
					$.confirm(data.message,function(){
						$("#mybody").load("/wechat/sjysxx/getSjysxxList?wxid="+$("#saveFrom #wxid").val());
					})
				}else if(data.state=="fail"){
					$.alert(data.message);
					$("#saveFrom #savekjxx").removeAttr("disabled");
				}
			}
		})
	}
}

$("#tab tbody").on("click","tr",function(){
	var flg=$(this).children().eq(0).attr("flg");
	if(flg!=null){
		$(this).removeClass("bgcolor");
		$(this).addClass("checked");
		var dwmc=$(this).children().eq(0).context.getAttribute("dwmc");
		var ysdh=$(this).children().eq(1).text();
		var kzcs=$(this).children().eq(2).attr("kzcs");
		var dwid=$(this).children().eq(2).attr("dwid")
		var	qtks=$(this).children().eq(2).text();
		var sjys=$(this).children().eq(3).text();
		var sjhb=$(this).children().eq(4).text();
		var jcdw=$(this).children().eq(5).text();
		var sjdw=$(this).children().eq(8).text();
		var sjdwbj=$(this).children().eq(9).text();
		$("#ajaxForm  .ks").removeAttr('checked');
		$("#ajaxForm  .qc").removeAttr('checked');
		$("#ajaxForm  input:radio[value='"+dwid+"']").prop('checked','true');
		if(kzcs=="1"){
			$("#ajaxForm #qtks").val(qtks);
			$("#ajaxForm #qtks").attr("style","display:block;margin-top:10px;");
			$("#ajaxForm #qtks").attr("validate","{required:true}");
		}else{
			$("#ajaxForm #qtks").attr("style","display:none;");
			$("#ajaxForm #qtks").removeAttr("validate");
			$("#ajaxForm #qtks").val("");
		}
		if(sjdwbj=='1'){
			$("#ajaxForm #sjdwmc").val(null);
			$("#ajaxForm #otherHospital").show();
		}else{
			$("#ajaxForm #sjdwmc").val(dwmc);
			$("#ajaxForm #otherHospital").hide();
		}
		$("#ajaxForm #sjys").val(sjys);
		$("#ajaxForm #ysdh").val(ysdh);
		$("#ajaxForm #sjdw").val(sjdw);
		$("#ajaxForm #db").val(sjhb);
		$("#ajaxForm #yymc").val(dwmc);
		$("#ajaxForm #sjdw").val(sjdw);
		refreshUnit();
		$("#ajaxForm  input:radio[value='"+jcdw+"']").prop('checked','true');
		//去掉底部页面固定，可以滑动
		$("#body").removeClass("stopBodyScroll")
		setTimeout(function(){
			$.closeModal("modSjkjxxModal");
		}, 500);
	}else{
		var state=$(this).attr("state");
		var dwmc=$(this).attr("dwmc");
		if(state=="up"){
			$(this).siblings().each(function(){
				if(dwmc==$(this).attr("dwmc")){
					$(this).slideUp(50);
				}
			})
			$(this).removeAttr("state");
			$(this).attr("state","down");
			$(this).children().eq(2).children().removeClass("glyphicon-menu-down");
			$(this).children().eq(2).children().addClass("glyphicon-menu-up");
			$(this).children().eq(3).children().removeClass("glyphicon-menu-down");
			$(this).children().eq(3).children().addClass("glyphicon-menu-up");
			$(this).children().eq(6).children().removeClass("glyphicon-menu-down");
			$(this).children().eq(6).children().addClass("glyphicon-menu-up");
		}else if(state=="down"){
			$(this).siblings().each(function(){
				if(dwmc==$(this).attr("dwmc")){
					$(this).slideDown();
				}
			});
			$(this).removeAttr("state");
			$(this).attr("state","up");
			$(this).children().eq(2).children().removeClass("glyphicon-menu-up");
			$(this).children().eq(2).children().addClass("glyphicon-menu-down");
			$(this).children().eq(3).children().removeClass("glyphicon-menu-up");
			$(this).children().eq(3).children().addClass("glyphicon-menu-down");
			$(this).children().eq(6).children().removeClass("glyphicon-menu-up");
			$(this).children().eq(6).children().addClass("glyphicon-menu-down");
		}
	}
})

function delsjyxx(ysid,event){
	 event.stopPropagation();
	 $.confirm("确认删除？", function(result){
			if(result){
				$.ajax({
					url:'/wechat/sjysxx/deleteSjysxx',
					type:'post',
					dataType:'json',
					data:{"ysid":ysid},
					success:function(data){
						if(data.state=="true"){
							$.confirm("删除成功！",function(){
								$("#mybody").load("/wechat/sjysxx/getSjysxxList?wxid="+$("#saveFrom #wxid").val());
							})
						}else if(data.state=="false"){
							$.alert("删除失败！");
						}
					}
				})
			}
	})
	 
}

function select_kjxx(sjys,ysdh,sjdw){
	
}

$(function(){
	$("#tbody .tbody_tr").each(function(){
		var flg=$(this).children().eq(0).attr("flg");
		if(!flg){
			$(this).addClass("bgcolor");
		}
	})
	
	//初始化送检单位列表
	var oTable = new Hospital_TableInit();
 		oTable.Init();
})