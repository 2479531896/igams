var Attendance_turnOff=true;
var attendance_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#attendanceformSearch #tb_list').bootstrapTable({
			url: $("#attendanceformSearch #urlPrefix").val()+'/attendance/attendance/pageGetListAttendance',
            method: 'get',                      //请求方式（*）
            toolbar: '#attendanceformSearch #toolbar', //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName:"cq.yhid",					//排序字段
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
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "yhid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
				width: '4%'
            }, {
                field: 'yhid',
                title: '用户ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'yhmc',
                title: '用户名',
                width: '20%',
                align: 'left',
                visible: true
            }, {
				field: 'sjid',
				title: '上级id',
				width: '8%',
				align: 'left',
				visible: false
			}, {
				field: 'sjyhmc',
				title: '上级名称',
				width: '20%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'cqsj',
                title: '出勤时间',
                width: '20%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'tqsj',
                title: '退勤时间',
                width: '20%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'bz',
                title: '备注',
                width: '16%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	attendanceDealById(row.yhid, 'view',$("#attendanceformSearch #btn_view").attr("tourl"));
            },
        });
        $("#attendanceformSearch #tb_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true
        }
        );
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
        	pageSize: params.limit,   //页面大小
        	pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "cq.yhid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
		};
		return getAttendenceSearchData(map);
	};
	return oTableInit;
}

function getAttendenceSearchData(map){
    	var cxbt = $("#attendanceformSearch #cxbt").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#attendanceformSearch #cxnr').val());
    	// '0':'来源编号','1':'外部编号','2':'孕周','3':'体重','4':'备注'
    	if (cxbt == "0") {
    		map["yhmc"] = cxnr;
    	}
    	return map;
}


var addAttendanceConfig = {
	width		: "600px",
	modalName	: "AttendanceModal",
	formName	: "Attendance_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#Attendance_ajaxForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#Attendance_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchAttendanceResult();
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


var viewAttendanceConfig = {
	width		: "600px",
	modalName	: "viewAttendanceModal",
	formName	: "addAttendance_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

//按钮动作函数
function attendanceDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?yhid=" +id;
		$.showDialog(url,'查看用户',viewAttendanceConfig);
	}else if(action =='add'){
		var url=tourl;
		$.showDialog(url,'新增用户',addAttendanceConfig);
	}else if(action =='mod'){
		var url= tourl + "?yyhid=" +id;
		$.showDialog(url,'编辑用户',addAttendanceConfig);
	}
}


var attendance_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_add = $("#attendanceformSearch #btn_add");
    	var btn_mod = $("#attendanceformSearch #btn_mod");
    	var btn_del = $("#attendanceformSearch #btn_del");
    	var btn_view = $("#attendanceformSearch #btn_view");
    	var btn_user_query = $("#btn_user_query");
    	//绑定搜索发送功能
    	if(btn_user_query != null){
    		btn_user_query.unbind("click").click(function(){
    			searchAttendanceResult(true);
    		});
    	}
    	btn_add.unbind("click").click(function(){
    		attendanceDealById(null,"add",btn_add.attr("tourl"));
    	});
    	
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#attendanceformSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			attendanceDealById(sel_row[0].yhid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#attendanceformSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			attendanceDealById(sel_row[0].yhid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		btn_del.unbind("click").click(function(){
    		var sel_row = $('#attendanceformSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		var yhms="";
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].yhid;
    			yhms = yhms + "," + sel_row[i].yhm;
    		}
    		yhms = yhms.substr(1);
    		ids = ids.substr(1);
    		$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= $('#attendanceformSearch #urlPrefix').val() +btn_del.attr("tourl");
    				jQuery.post(url,{ids:ids,yhms:yhms,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								searchAttendanceResult();
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
    };

    return oInit;
};


function searchAttendanceResult(isTurnBack){
	if(isTurnBack){
		$('#attendanceformSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#attendanceformSearch #tb_list').bootstrapTable('refresh');
	}
}

$(function(){

    //1.初始化Table
    var oTable = new attendance_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new attendance_ButtonInit();
    oButtonInit.Init();
    
	//所有下拉框添加choose样式
	jQuery('#attendanceformSearch .chosen-select').chosen({width: '100%'});
});


