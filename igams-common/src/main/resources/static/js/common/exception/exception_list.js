var exception_turnOff=true;
var t_map=[];
var Exception_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#exception_formSearch #exception_list").bootstrapTable({
			url: $("#exception_formSearch #urlPrefix").val()+'/exception/exception/pageGetListException',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#exception_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "yc.sfjs asc,pxsj",				//排序字段
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
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "ycid",                     //每一行的唯一标识，一般为主键列9
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            },{
                field: 'ycid',
                title: '异常id',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'ycbt',
				titleTooltip:"标题",
                title: '标题',
                width: '15%',
                align: 'left',
                visible: true,
				sortable: true
            },{
                field: 'ycqfmc',
				titleTooltip:"异常区分",
				title: '异常区分',
                width: '8%',
                align: 'left',
                visible:true,
				sortable: true
            },{
				field: 'yczqfmc',
				titleTooltip:"异常子区分",
				title: '异常子区分',
				width: '8%',
				align: 'left',
				visible:false,
				sortable: true
			},{
                field: 'lxmc',
				titleTooltip:"异常类型",
				title: '异常类型',
                width: '8%',
                align: 'left',
                visible: true,
				sortable: true
            },{
                field: 'zlxmc',
				titleTooltip:"异常子类型",
                title: '异常子类型',
                width: '10%',
                align: 'left',
                visible: false,
				sortable: true
            },{
				field: '',
				titleTooltip:"标本编号/产品名称",
				title: '标本编号/产品名称',
				width: '10%',
				align: 'left',
				visible: true,
				sortable: true,
				formatter: mcformat
			},{
				field: 'jcdwmc',
				titleTooltip:"检测单位",
				title: '检测单位',
				width: '10%',
				align: 'left',
				visible: true,
				sortable: true
			},{
                field: 'ycxx',
				titleTooltip:"异常信息",
                title: '异常信息',
                width: '10%',
                align: 'left',
                visible: true,
				sortable: true
            },{
				field: 'lrsj',
				titleTooltip:"录入时间",
				title: '录入时间',
				width: '10%',
				align: 'left',
				visible: true,
				sortable: true
			},{
                field: 'twrmc',
				titleTooltip:"提问人",
                title: '提问人',
                width: '5%',
                align: 'left',
                visible: true,
				sortable: true
            },{
                field: 'qrrmc',
				titleTooltip:"确认人",
                title: '确认人',
                width: '5%',
                align: 'left',
                visible: true,
				sortable: true
            },{
				field: 'zhhfnr',
				titleTooltip:"最后回复内容",
				title: '最后回复内容',
				width: '15%',
				align: 'left',
				visible: true,
				sortable: true
			},{
				field: 'zhhfsj',
				titleTooltip:"最后回复时间",
				title: '最后回复时间',
				width: '10%',
				align: 'left',
				visible: true,
				sortable: true
			},{
				field: 'jssj',
				titleTooltip:"结束时间",
				title: '结束时间',
				width: '10%',
				align: 'left',
				visible: false,
				sortable: true
			},{
				field: 'jsrmc',
				titleTooltip:"结束人名称",
				title: '结束人名称',
				width: '10%',
				align: 'left',
				visible: false,
				sortable: true
			},{
                field: 'sfjsmc',
				titleTooltip:"结束",
                title: '结束',
                width: '3%',
                formatter:sfjsFormat,
                align: 'center',
                visible: true,
            },{
				field: 'unreadnum',
				titleTooltip:"未读个数",
				title: '未读个数',
				width: '5%',
				align: 'center',
				visible: true,
			}],
            onLoadSuccess:function(map){
				t_map=map;
				var item={}
				function cs(key,obj){

					let ycid=$(this).attr("data-uniqueid");
					if(ycid==""||ycid==null){
						return ;
					}
					var url= "/exception/exception/pagedataGetException";
					jQuery.post(url,{ycid:ycid,"access_token":$("#ac_tk").val()},function(responseText){
						setTimeout(function(){
							if(responseText["status"] == 'success'){
								$.success("点击确定复制",function() {

									var copyDom = document.createElement('div');
									var sjyzdto=responseText["sjycDto"]
									var arr=key.split(",")
                                    var str=""
									for(var o=0;o<arr.length;o++){
									    if(str==""){
											if(sjyzdto[arr[o]]==null){
												str+=""
											}else{
												str+=sjyzdto[arr[o]];
											}

                                        }else{
									    	if(sjyzdto[arr[o]]==null){
												str+=",-"
											}else{
												str+=","+sjyzdto[arr[o]];
											}

									    }

									}
                                    copyDom.innerText=str
									copyDom.style.position='absolute';
									copyDom.style.top='0px';
									copyDom.style.right='-9999px';
									document.body.appendChild(copyDom);
									//创建选中范围
									var range = document.createRange();
									range.selectNode(copyDom);
									//移除剪切板中内容
									window.getSelection().removeAllRanges();
									//添加新的内容到剪切板
									window.getSelection().addRange(range);
									//复制
									var successful = document.execCommand('copy');
									copyDom.parentNode.removeChild(copyDom);
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
				}
				var item={}
				item['暂无数据']={name:"暂无数据",callback:function(){
					   return;
					}}

				if(list.length>0){
					item={}
					for(var i=0;i<list.length;i++){
						var _item=list[i]
						item[_item.cskz1]={name:_item.csmc,callback:cs}
					}
				}
				$.contextMenu({
					// define which elements trigger this menu
					selector: "#exception_formSearch tr",
					// define the elements of the menu
					items: item
					// there's more, have a look at the demos and docs...
				});
            },

            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
				removeUnreadData(row);
            	Exception_DealById(row.ycid,'exceptionlist',$("#exception_formSearch #btn_exceptionlist").attr("tourl"));
             },
			rowStyle: function (row, index) {
				//这里有5个取值代表5中颜色['active', 'success', 'info', 'warning', 'danger'];
				if(row.unreadnum){
					var number = parseInt(row.unreadnum);
					if(number>0){
						return { classes: 'warning' }
					}else{
						return {}
					}
				}else{
					return {}
				}
			},
		});
        $("#exception_formSearch #exception_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true}        
        );
	};
		oTableInit.queryParams=function(params){
			var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
	        	pageSize: params.limit,   // 页面大小
	        	pageNumber: (params.offset / params.limit) + 1,  // 页码
	            access_token:$("#ac_tk").val(),
	            sortName: params.sort,      // 排序列名
	            sortOrder: params.order, // 排位命令（desc，asc）
	            sortLastName: "yc.ycid", // 防止同名排位用
	            sortLastOrder: "asc" ,// 防止同名排位用
				personal_flg: $("#exception_formSearch #personal_flg").val(),
				sfjs: $("#exception_formSearch #sfjs").val(),
				ycqfdm: $("#exception_formSearch #ycqfdm").val()
	            // 搜索框使用
	            // search:params.search
	        };
			var ycqf_flg=$("#exception_formSearch #ycqf_flg").val();
			if(ycqf_flg!=""&&ycqf_flg!=null){
				map["ycqfs"]=$("#exception_formSearch #ycqfs").val()
			}
			return getExceptionSearchData(map);
			};
			return oTableInit;
}

/**
 * 格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function mcformat(value,row,index){
	if("NORMAL_EXCEPTION"==row.ycqfdm||"WECHAT_EXCEPTION"==row.ycqfdm){
		return row.ybbh;
	}else if("PRODUCT_EXCEPTION"==row.ycqfdm){
		return row.wlmc;
	}else{
		return "";
	}
}

function sfjsFormat(value,row,index){
	if(row.sfjs=='1'){
		return "<span style='color:#19F845'>是</span>";
	}else{
		return "<span style='color:#F8CB19'>否</span>";
	}
}

function getExceptionSearchData(map){
	var cxtj=$("#exception_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#exception_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["ycbt"]=cxnr
	}else if(cxtj=="1"){
		map["ybbh"]=cxnr
	}else if(cxtj=="2"){
		map["hzxm"]=cxnr
	}else if(cxtj=="3"){
		map["yblxmc"]=cxnr
	}else if(cxtj=="4"){
		map["twrmc"]=cxnr
	}else if(cxtj=="5"){
		map["qrrmc"]=cxnr
	}else if(cxtj=="6"){
		map["ycxx"]=cxnr
	}
	//是否结束
	var sfjss = jQuery('#exception_formSearch #sfjs_id_tj').val();
	map["sfjss"] = sfjss.replace(/'/g, "");
	//创建时间
	var cjsjstart = jQuery('#exception_formSearch #cjsjstart').val();
	map["cjsjstart"] = cjsjstart;
	var cjsjend = jQuery('#exception_formSearch #cjsjend').val();
	map["cjsjend"] = cjsjend;
	//检测单位
	var jcdws = $("#exception_formSearch #jcdw_id_tj").val();
	map["jcdws"]=jcdws
	//异常区分
	var ycqfs = $("#exception_formSearch #ycqf_id_tj").val();
	var _ycqfs = $("#exception_formSearch #ycqfs").val();
	var ycqf_flg = $("#exception_formSearch #ycqf_flg").val();
	if(ycqf_flg!=null&&ycqf_flg!=""){
		map["ycqfs"]=_ycqfs
	}else{
		map["ycqfs"]=ycqfs
	}

	// 最后回复时间
	var zhhfsjstart = jQuery('#exception_formSearch #zhhfsjstart').val();
	map["zhhfsjstart"] = zhhfsjstart;
	var zhhfsjend = jQuery('#exception_formSearch #zhhfsjend').val();
	map["zhhfsjend"] = zhhfsjend;
	var jcdws=$("#exception_formSearch #jcdw_id_tj").val();
	map["jcdws"]=jcdws
	var yclxs=$("#exception_formSearch #yclx_id_tj").val();
	map["yclxs"]=yclxs
	return map;
}
function Exception_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl=$("#exception_formSearch #urlPrefix").val()+tourl
	if(action=="add"){
		var url=tourl;
		$.showDialog(url,'新增异常信息',addExceptionConfig);
	}else if(action=="mod"){
		var url = tourl;
		if (url.indexOf("?") > 0) {
			url += "&ycid=" + id;
		}else {
			url += "?ycid=" + id;
		}
		$.showDialog(url,'修改异常信息',modExceptionConfig);
	}else if(action=="view"){
		var url=tourl+"?ycid="+id
		$.showDialog(url,'查看异常信息',viewExceptionConfig);
	}else if(action=="exceptionlist"){
		var url=tourl+"?ycid="+id
		$.showDialog(url,'异常反馈',ExceptionFeedbackConfig);
	}else if(action=="finish"){
		var url=tourl+"?ids="+id
		$.showDialog(url,'异常结束评价',finishExceptionConfig);
	}
}
var Exception_oButtton= function (){
		var oInit=new Object();
		var postdata = {};
		oInit.Init=function(){
			var btn_query=$("#exception_formSearch #btn_query");
			var btn_add = $("#exception_formSearch #btn_add");
			var btn_mod = $("#exception_formSearch #btn_mod");
			var btn_view = $("#exception_formSearch #btn_view");
			var btn_del = $("#exception_formSearch #btn_del");
			var btn_selectexport = $("#exception_formSearch #btn_selectexport");//选中导出
	    	var btn_searchexport = $("#exception_formSearch #btn_searchexport");//搜索导出
	    	var btn_exceptionlist=$("#exception_formSearch #btn_exceptionlist");//异常清单
	    	var btn_finish=$("#exception_formSearch #btn_finish");
			var ycqfBind = $("#exception_formSearch #ycqf_id ul li a");//初始化异常区分高级筛选点击事件
			//添加日期控件
			laydate.render({
				elem: '#cjsjstart'
				,theme: '#2381E9'
			});
			//添加日期控件
			laydate.render({
				elem: '#cjsjend'
				,theme: '#2381E9'
			});
			//添加日期控件
			laydate.render({
				elem: '#zhhfsjstart'
				,theme: '#2381E9'
			});
			//添加日期控件
			laydate.render({
				elem: '#zhhfsjend'
				,theme: '#2381E9'
			});
	/*-----------------------模糊查询------------------------------------*/
			if(btn_query!=null){
				btn_query.unbind("click").click(function(){
					searchExceptionResult(true); 
	    		});
			}
	/*-----------------------新增------------------------------------*/
			btn_add.unbind("click").click(function(){
					Exception_DealById(null,"add",btn_add.attr("tourl"));
	    	});
	/*-----------------------修改------------------------------------*/
			btn_mod.unbind("click").click(function(){
	    		var sel_row = $('#exception_formSearch #exception_list').bootstrapTable('getSelections');//获取选择行数据
	    		if(sel_row.length==1){
	    			Exception_DealById(sel_row[0].ycid,"mod",btn_mod.attr("tourl"));
	    		}else{
	    			$.error("请选中一行");
	    		}
	    	});
	/*-----------------------查看------------------------------------*/
	    	btn_view.unbind("click").click(function(){
	    		var sel_row = $('#exception_formSearch #exception_list').bootstrapTable('getSelections');//获取选择行数据
	    		if(sel_row.length==1){
	    			Exception_DealById(sel_row[0].ycid,"view",btn_view.attr("tourl"));
	    		}else{
	    			$.error("请选中一行");
	    		}
	    	});
	/*---------------------------异常反馈-----------------------------------*/
	    	btn_exceptionlist.unbind("click").click(function(){
	    		var sel_row = $('#exception_formSearch #exception_list').bootstrapTable('getSelections');//获取选择行数据
	    		if(sel_row.length==1){
					removeUnreadData(sel_row[0]);
	    			Exception_DealById(sel_row[0].ycid,"exceptionlist",btn_exceptionlist.attr("tourl"));
	    		}else{
					$.error("请选中一行!");
					return;
				}
				// else{
				// 	Exception_DealById('',"exceptionlist",btn_exceptionlist.attr("tourl"));
	    		// }
	    	});
	/*-----------------------删除------------------------------------*/
	    	btn_del.unbind("click").click(function(){
	    		var sel_row = $('#exception_formSearch #exception_list').bootstrapTable('getSelections');//获取选择行数据
	    		if (sel_row.length==0) {
					$.error("请至少选中一行");
					return;
				}else{
					var ids="";
	    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
	        			ids= ids + ","+ sel_row[i].ycid;
	        		}
	    			ids=ids.substr(1);
	    			$.confirm('您确定要删除所选择的信息吗？',function(result){
	        			if(result){
	        				jQuery.ajaxSetup({async:false});
	        				var url= $("#exception_formSearch #urlPrefix").val()+btn_del.attr("tourl");
	        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
	        					setTimeout(function(){
	        						if(responseText["status"] == 'success'){
	        							$.success(responseText["message"],function() {
	        								searchExceptionResult();
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
	    	/*-----------------------结束------------------------------------*/
	    	btn_finish.unbind("click").click(function(){
	    		var sel_row = $('#exception_formSearch #exception_list').bootstrapTable('getSelections');//获取选择行数据
	    		if (sel_row.length==0) {
					$.error("请至少选中一行");
					return;
				}else{
					var ids="";
	    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
						if(sel_row[i].sfjs&&sel_row[i].sfjs=='1'){
							$.error("所选数据中有已结束的异常，请重新选择！");
							return;
						}else{
							ids= ids + ","+ sel_row[i].ycid;
						}
	        		}
	    			ids=ids.substr(1);
					Exception_DealById(ids,"finish",btn_finish.attr("tourl"));
				}
	    	});
	    	//---------------------------------选中导出---------------------------------------
	    	btn_selectexport.unbind("click").click(function(){
	    		var sel_row = $('#exception_formSearch #exception_list').bootstrapTable('getSelections');//获取选择行数据
	    		if(sel_row.length>=1){
	    			var ids="";
	        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
	        			ids = ids + ","+ sel_row[i].ycid;
	        		}
	        		ids = ids.substr(1);
	        		$.showDialog($("#exception_formSearch #urlPrefix").val()+exportPrepareUrl+"?ywid=ABNORMAL_SELECT&expType=select&ids="+ids
	        				,"送检异常选中导出", $.extend({},viewConfig,{"width":"1020px"}));
	    		}else{
	    			$.error("请选择数据");
	    		}
	    	});
	    	//搜索导出
	    	btn_searchexport.unbind("click").click(function(){
	    		$.showDialog($("#exception_formSearch #urlPrefix").val()+exportPrepareUrl+"?ywid=ABNORMAL_SEARCH&expType=search&callbackJs=abnormalSearchData"+($("#exception_formSearch #personal_flg").val()?("&personal_flg="+$("#exception_formSearch #personal_flg").val()):'')
	    				,"送检异常搜索导出", $.extend({},viewConfig,{"width":"1020px"}));
	    	});
			/*-----------------------显示隐藏------------------------------------*/
			$("#exception_formSearch #sl_searchMore").on("click", function(ev){
				var ev=ev||event;
				if(exception_turnOff){
					$("#exception_formSearch #searchMore").slideDown("low");
					exception_turnOff=false;
					this.innerHTML="基本筛选";
				}else{
					$("#exception_formSearch #searchMore").slideUp("low");
					exception_turnOff=true;
					this.innerHTML="高级筛选";
				}
				ev.cancelBubble=true;
				//showMore();
			});

			//绑定异常区分的单击事件
			if(ycqfBind!=null){
				ycqfBind.on("click", function(){
					setTimeout(function(){
						getYclxs();
					}, 10);
				});
			}
		}
	 	return oInit;
}

/**
 * 关联异常区分高级筛选
 */
function getYclxs() {
	// 异常区分
	var ycqf = $('#exception_formSearch #ycqf_id_tj').val();
	if (!isEmpty(ycqf)) {
		//不为空，移除异常类型高级筛选的隐藏属性
		ycqf = "'" + ycqf + "'";
		$("#exception_formSearch #yclx_id").removeClass("hidden");
	}else{
		//为空，增加异常类型高级筛选的隐藏属性
		$("#exception_formSearch #yclx_id").addClass("hidden");
	}
	// 异常区分不为空
	if (!isEmpty(ycqf)) {
		var url = "/systemmain/data/ansyGetJcsjListInStop";
		$.ajax({
			type : "POST",
			url : url,
			data : {"fcsids":ycqf,"jclb":"EXCEPTION_TYPE","access_token":$("#ac_tk").val()},
			dataType : "json",
			success : function(data){
				if(data.length > 0) {
					var html = "";
					$.each(data,function(i){
						html += "<li>";
						html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('yclx','" + data[i].csid + "','exception_formSearch');\" id=\"yclx_id_" + data[i].csid + "\"><span>" + data[i].csmc + "</span></a>";
						html += "</li>";
					});
					$("#exception_formSearch #ul_yclx").html(html);
					$("#exception_formSearch #ul_yclx").find("[name='more']").each(function(){
						$(this).on("click", s_showMoreFn);
					});
				} else {
					$("#exception_formSearch #ul_yclx").empty();
				}
				//清本原本选中的异常类型数值以及高级筛选
				$("#exception_formSearch [id^='yclx_li_']").remove();
				$("#exception_formSearch #yclx_id_tj").val("");
			}
		});
	} else {
		//为空，清空所有异常类型相关数据，并增加异常类型高级筛选的隐藏属性
		$("#exception_formSearch #ul_yclx").empty();
		$("#exception_formSearch [id^='yclx_li_']").remove();
		$("#exception_formSearch #yclx_id_tj").val("");
		$("#exception_formSearch #yclx_id").addClass("hidden");
	}
}

function removeUnreadData(row){
	if(row.unreadnum) {
		var number = parseInt(row.unreadnum);
		var unreadnum = parseInt($("#unreadNum").text());
		var finreadNum = parseInt($("#finreadNum").text());
		if(row.sfjs=='1'){
			$("#finreadNum").text(finreadNum-number);
		}else{
			$("#unreadNum").text(unreadnum-number);
			$("#unreadDiv").css("background-color","#00a0e9");
		}

		for(var i=0;i<t_map.rows.length;i++){
			if(t_map.rows[i].ycid==row.ycid){
				t_map.rows[i].unreadnum="0";
				break;
			}
		}
		$("#exception_formSearch #exception_list").bootstrapTable('load',t_map);
	}
}

//提供给导出用的回调函数
function abnormalSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="yc.ycid";
	map["sortLastOrder"]="asc";
	map["sortName"]="yc.lrsj";
	map["sortOrder"]="asc";
	var personal_flg  = $("#personal_flg").val();
	if ("1"==personal_flg){
		map["ryid"]=$("#ryid").val();
	}
	return getExceptionSearchData(map);
}

var finishExceptionConfig = {
	width		: "600px",
	modalName	:"finishExceptionModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#finishExceptionForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#finishExceptionForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"finishExceptionForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchExceptionResult();
							}
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


var addExceptionConfig = {
		width		: "800px",
		modalName	:"addExceptionModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#editExceptionForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					var tzrys=$("#tzrys").val();
					// if(tzrys.length<=0){
					// 	$.confirm("通知人员不能为空");
					// 	return false;
					// }
					$("#editExceptionForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"editExceptionForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									if ($("#editExceptionForm #key").val()){
										$.ajax({
											url: $("#editExceptionForm #urlPrefix").val()+"/sseEmit/connect/pagedataCloseOA",
											data: {
												"access_token": $("#ac_tk").val(),
												"key": $("#editExceptionForm #key").val(),
											}
										});
									}
									$.closeModal(opts.modalName);
								}
								searchExceptionResult();
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

/*查看异常清单模态框*/
var ExceptionFeedbackConfig = {
		width		: "800px",
		height		: "500px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

var modExceptionConfig = {
		width		: "800px",
		modalName	:"modExceptionModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#editExceptionForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					var tzrys=$("#tzrys").val();
					// if(tzrys.length<=0){
					// 	$.confirm("通知人员不能为空");
					// 	return false;
					// }
					var $this = this;
					var opts = $this["options"]||{};
					$("#editExceptionForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"editExceptionForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									if ($("#editExceptionForm #key").val()){
										$.ajax({
											url: $("#editExceptionForm #urlPrefix").val()+"/sseEmit/connect/pagedataCloseOA",
											data: {
												"access_token": $("#ac_tk").val(),
												"key": $("#editExceptionForm #key").val(),
											}
										});
									}
									$.closeModal(opts.modalName);
								}
								searchExceptionResult();
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

var viewExceptionConfig = {
		width		: "800px",
		modalName	:"viewExceptionModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
function searchExceptionResult(isTurnBack){
	if(isTurnBack){
		$('#exception_formSearch #exception_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#exception_formSearch #exception_list').bootstrapTable('refresh');
	}
}

function connectMessage(key) {
	var source=null;
	// 用时间戳模拟登录用户
	if (!!window.EventSource) {
		// 建立连接
		  source = new EventSource($("#exception_formSearch #urlPrefix").val()+"/sseEmit/connect/pagedataExceptionRedis?access_token=" + $("#ac_tk").val()+"&key="+key);
		/**
		 * 连接一旦建立，就会触发open事件
		 * 另一种写法：source.onopen = function (event) {}
		 */

		source.addEventListener('open', function (e) {
			console.log("建立连接。。。");
			setTimeout(function(){
				closeSse();
				if ($("#exception_formSearch #key").val()){
					$.ajax({
						url: $("#exception_formSearch #urlPrefix").val()+"/sseEmit/connect/pagedataCloseOARedis",
						data: {
							"access_token": $("#ac_tk").val(),
							"key": $("#exception_formSearch #key").val(),
						},success:function () {
							connectMessage($("#exception_formSearch #key").val());
						}
					});
				}
			},1000*60*45)
		}, false);
		/**
		 * 客户端收到服务器发来的数据
		 * 另一种写法：source.onmessage = function (event) {}
		 */
		source.addEventListener('message', function (e) {
			setMessageInnerHTML(e.data);
		});
		/**
		 * 如果发生通信错误（比如连接中断），就会触发error事件
		 * 或者：
		 * 另一种写法：source.onerror = function (event) {}
		 */
		source.addEventListener('error', function (e) {
			if (e.readyState === EventSource.CLOSED) {
				console.log("连接关闭");
			} else {
				closeSse();
			}
		}, false);
	} else {
		console.log("你的浏览器不支持SSE");
	}
	// 监听窗口关闭事件，主动去关闭sse连接，如果服务端设置永不过期，浏览器关闭后手动清理服务端数据
	window.onbeforeunload = function () {
		if($("body").attr("isxz")=='true'){
			$("body").attr("isxz","false")
			return
		}
		closeSse();

		if ($("#exception_formSearch #key").val()){
			$.ajax({
				url: $("#exception_formSearch #urlPrefix").val()+"/sseEmit/connect/pagedataCloseOARedis",
				data: {
					"access_token": $("#ac_tk").val(),
					"key": $("#exception_formSearch #key").val(),
				}
			});
		}
	};
	window.addEventListener('message', (event) => {
		$.ajax({
			url: $("#exception_formSearch #urlPrefix").val()+"/sseEmit/connect/pagedataCloseOARedis",
			data: {
				"access_token": $("#ac_tk").val(),
				"key": $("#exception_formSearch #key").val(),
			}
		});
		closeSse();
	})
	// 关闭Sse连接
	function closeSse() {
		source.close();
		// $.ajax({
		// 	url: "/sseEmit/connect/pagedataCloseOA",
		// 	data: {
		// 		"access_token": $("#ac_tk").val(),
		// 		"userId": $("#ajaxForm #yhid").val(),
		// 	},
		// 	success: function (data) {},
		// 	error: function () {
		// 		$.error("连接手机失败，请联系管理员解决原因！");
		// 	}
		// });

		console.log("close");
	}

	// 将消息显示在网页上
	function setMessageInnerHTML( res) {
		//处理读到的数据

		var obj = JSON.parse(res);
		//显示到页面
		if (obj) {
			if (obj.key == $("#exception_formSearch #key").val()){
				$("#unreadNum").text(obj.unreadcount);
				$("#unreadDiv").css("background-color","orange");
				$("#finreadNum").text(obj.finreadcount);
				$("#finreadDiv").css("background-color","orange");
				var exceptionlist = obj.exceptionlist;
				for(var i=0;i<t_map.rows.length;i++){
					for(var j=0;j<Object.keys(exceptionlist).length;j++){
						if(t_map.rows[i].ycid==Object.keys(exceptionlist)[j]){
							t_map.rows[i].unreadnum=exceptionlist[Object.keys(exceptionlist)[j]].ex_unreadcnt.toString();
							break;
						}
					}
				}
				$("#exception_formSearch #exception_list").bootstrapTable('load',t_map);
			}
		}
	}
}

function viewInfo(event){
	var titleHtml ="<tr><th><b>标题</b></th><th><b>异常类型</b></th></tr>";
	$.ajax( {
		"dataType" : 'json',
		"type" : "POST",
		"url" : "/exception/exception/pagedataViewInfo",
		"data" : {
			"access_token" : $("#ac_tk").val()
		},
		"success" : function(map) {

			var ent=  map.list;
			if(ent.length>0){
				for (var i=0;i<ent.length;i++){
					titleHtml = titleHtml +
						"<tr>" +
						"<td>" + ent[i].ycbt + "</td>" +
						"<td>" + ent[i].lxmc + "</td>" +
						"</tr>";
				}
				$("#tbody").html(titleHtml);
				$('#statisticDiv').click(function(){
					if($("#showDiv").css("display")=="block"){
						$("#showDiv").hide();
						$("#tbody").html("");
						$('#sjxxBodyHead').unbind("click");
						return;
					}
				})

				var e_ = window.event || e; // 兼容IE，FF事件源
				var x = e_.clientX,y = e_.clientY; // 获取鼠标位置
				$("#showDiv").css({"left":x -$("#statisticDiv").offset().left,"top":y , "display":"block"});
			}
		}
	});
}

function closeDiv(){
	$("#showDiv").hide();
}


$(function(){
	var oTable=new Exception_TableInit();
		oTable.Init();
	// 初始绑定显示更多的事件
	$("#exception_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	//2.初始化Button的点击事件
    var oButtonInit = new Exception_oButtton();
    	oButtonInit.Init();
	jQuery('#exception_formSearch .chosen-select').chosen({width: '100%'});
	if ($("#exception_formSearch #key").val()) {
		connectMessage($("#exception_formSearch #key").val());
	}
})
