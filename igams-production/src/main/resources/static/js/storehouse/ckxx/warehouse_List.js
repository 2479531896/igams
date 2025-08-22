var Ckxx_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
			$("#ckxx_formSearch #ckxx_list").bootstrapTable({
				url: $("#ckxx_formSearch #urlPrefix").val()+'/warehouse/view/pageGetListView',         //请求后台的URL（*）
	            method: 'get',                      //请求方式（*）
	            toolbar: '#ckxx_formSearch #toolbar',                //工具按钮用哪个容器
	            striped: true,                      //是否显示行间隔色
	            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
	            pagination: true,                   //是否显示分页（*）
	            paginationShowPageGo: true,         //增加跳转页码的显示
	            sortable: true,                     //是否启用排序
	            sortName: "ck.ckdm",				//排序字段
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
	            uniqueId: "ckid",                     //每一行的唯一标识，一般为主键列
	            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
	            cardView: false,                    //是否显示详细视图
	            detailView: false,                   //是否显示父子表
	            columns: [{
	                checkbox: true,
	                width: '4%'
	            },{
	                field: 'ckid',
	                title: '仓库id',
	                width: '10%',
	                align: 'left',
	                visible:false
	            },{
	                field: 'fckid',
	                title: '父仓库id',
	                width: '10%',
	                align: 'left',
	                visible: false
	            },{
	                field: 'fckmc',
	                title: '父仓库',
	                width: '20%',
	                align: 'left',
	                visible: true
	            },{
	                field: 'ckdm',
	                title: '仓库代码',
	                width: '20%',
	                align: 'left',
	                visible:true
	            },{
	                field: 'ckmc',
	                title: '仓库名称',
	                width: '20%',
	                align: 'left',
	                visible: true
	            },{
	                field: 'cklbmc',
	                title: '仓库类别',
	                width: '20%',
	                align: 'left',
	                visible: true
	            },{
					field: 'ckqxlx',
					title: '仓库分类',
					width: '20%',
					align: 'left',
					visible: true
				},{
	                field: 'bz',
	                title: '备注',
	                width: '16%',
	                align: 'left',
	                visible: true
	            }
//	            ,{
//	                field: 'lrrymc',
//	                title: '录入人员',
//	                width: '10%',
//	                align: 'left',
//	                visible: true
//	            },{
//	                field: 'lrsj',
//	                title: '录入时间',
//	                width: '10%',
//	                align: 'left',
//	                visible: true
//	            }
	            ],
	            onLoadSuccess:function(){
	            },
	            onLoadError:function(){
	            	alert("数据加载失败！");
	            },
	            onDblClickRow: function (row, $element) {
	            	Ckxx_DealById(row.ckid,"view",$("#ckxx_formSearch #btn_view").attr("tourl"));
	             },
		});
			  $("#ckxx_formSearch #ckxx_list").colResizable({
		            liveDrag:true, 
		            gripInnerHtml:"<div class='grip'></div>", 
		            draggingClass:"dragging", 
		            resizeMode:'fit', 
		            postbackSafe:true,
		            partialRefresh:true        
			  });
			  
	};
		oTableInit.queryParams=function(params){
			var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
	        	pageSize: params.limit,   // 页面大小
	        	pageNumber: (params.offset / params.limit) + 1,  // 页码
	            access_token:$("#ac_tk").val(),
	            sortName: params.sort,      // 排序列名
	            sortOrder: params.order, // 排位命令（desc，asc）
	            sortLastName: "ck.ckid", // 防止同名排位用
	            sortLastOrder: "asc" // 防止同名排位用
	            // 搜索框使用
	            // search:params.search
	        };
			return getCkxxSearchData(map);
		}
	return oTableInit;
}
function getCkxxSearchData(map){
	var cxtj=$("#ckxx_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#ckxx_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["ckmc"]=cxnr
	}else if(cxtj=="1"){
		map["fckmc"]=cxnr
	}
	//仓库类别
	var cklbs= jQuery('#ckxx_formSearch #cklb_id_tj').val();
	//仓库分类
	var ckfls= jQuery('#ckxx_formSearch #ckfl_id_tj').val();
	map["cklbs"] = cklbs.replace(/'/g, "");;
	map["ckfls"] = ckfls.replace(/'/g, "");;
	return map;
}
		
var Ckxx_oButtton=function(){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#ckxx_formSearch #btn_query");
		var btn_add = $("#ckxx_formSearch #btn_add");
		var btn_mod = $("#ckxx_formSearch #btn_mod");
		var btn_view = $("#ckxx_formSearch #btn_view");
		var btn_del = $("#ckxx_formSearch #btn_del");
		var btn_batchpermit = $("#ckxx_formSearch #btn_batchpermit");
		/*-----------------------模糊查询------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchCkxxResult(); 
    		});
		}
		/*----------------------权限设置----------------------*/
		btn_batchpermit.unbind("click").click(function(){
			var sel_row=$('#ckxx_formSearch #ckxx_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				Ckxx_DealById(sel_row[0].ckid,"batchpermit",btn_batchpermit.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*-------------------新增------------------*/
		btn_add.unbind("click").click(function(){
			Ckxx_DealById(null,"add",btn_add.attr("tourl"));
		});
		/*------------------修改----------------------*/
		btn_mod.unbind("click").click(function(){
			var sel_row=$('#ckxx_formSearch #ckxx_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				Ckxx_DealById(sel_row[0].ckid,"mod",btn_mod.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*----------------------查看----------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row=$('#ckxx_formSearch #ckxx_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				Ckxx_DealById(sel_row[0].ckid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*-----------------------------删除-------------------------------*/
		btn_del.unbind("click").click(function(){
    		var sel_row = $('#ckxx_formSearch #ckxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else {
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].ckid;
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定要删除所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= $("#ckxx_formSearch #urlPrefix").val()+btn_del.attr("tourl");
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								searchCkxxResult();
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
		// ------------------------高级筛选-----------------------------------------
		$("#ckxx_formSearch #sl_searchMore").on("click",function(ev){
			var ev=ev||event;
			if(ckxx_turnOff){
				$("#ckxx_formSearch #searchMore").slideDown("low");
				ckxx_turnOff = false;
				this.innerHTML="基本筛选";
			}else{
				$("#ckxx_formSearch #searchMore").slideUp("low");
				ckxx_turnOff=true;
				this.innerHTML="高级筛选";
			}
			ev.cancelBubble=true;
		});
	//	--------------------------------------------------------------------------
	}
	return oInit;
}
var ckxx_turnOff=true;
function Ckxx_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl=$("#ckxx_formSearch #urlPrefix").val()+tourl;
	if(action=="add"){
		var url=tourl;
		$.showDialog(url,'新增仓库',addCkxxConfig);
	}else if(action=="mod"){
		var url=tourl+"?ckid="+id;
		$.showDialog(url,'修改信息',addCkxxConfig);
	}else if(action=="view"){
		var url=tourl+"?ckid="+id;
		$.showDialog(url,'查看信息',ViewCkxxConfig);
	}else if(action=="batchpermit"){
		var url=tourl+"?ckid="+id;
		$.showDialog(url,'权限设置',batchpermitCkxxConfig);
	}
}

var batchpermitCkxxConfig = {
	width		: "800px",
	modalName	:"batchpermitCkxxConfig",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var json=[];
				var data=$('#batchpermitCkxxForm #tb_list').bootstrapTable('getData');//获取选择行数据
				for(var i=0;i<data.length;i++){
					var sz={"jgid":''};
					sz.jgid=data[i].jgid;
					json.push(sz);
				}
				$("#batchpermitCkxxForm #ckjgxx_json").val(JSON.stringify(json));
				var $this = this;
				var opts = $this["options"]||{};
				$("#batchpermitCkxxForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"batchpermitCkxxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							searchCkxxResult();
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

var addCkxxConfig = {
		width		: "600px",
		modalName	:"addCkxxConfig",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#CkxxListForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#CkxxListForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"CkxxListForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchCkxxResult();
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

var ViewCkxxConfig = {
		width		: "800px",
		modalName	:"ViewCkxxConfig",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
function searchCkxxResult(isTurnBack){
	if(isTurnBack){
		$('#ckxx_formSearch #ckxx_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#ckxx_formSearch #ckxx_list').bootstrapTable('refresh');
	}
}
$(function(){
	var oTable= new Ckxx_TableInit();
		oTable.Init();
	var oButtonInit = new Ckxx_oButtton();
		oButtonInit.Init();
		jQuery('#ckxx_formSearch .chosen-select').chosen({width: '100%'});
})