var t_mapT=[];
var setmateriel_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#setmateriel_formSearch #view_list').bootstrapTable({
            url: $('#setmateriel_formSearch #urlPrefix').val() + '/progress/produce/pagedataGetSetmateriel',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#setmateriel_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
//            paginationDetailHAlign: " hidden", //不显示底部文字
            sortable: true,                     //是否启用排序
            sortName: "wlbm",				//排序字段
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
            uniqueId: "szid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
				width: '2%',
            },
            {
            	// 	checkbox: true,
            	// 	width: '2%'
            	// },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'wlbm',
                title: '物料编码',
                titleTooltip:'物料编码',
                width: '8%',
                align: 'left',
				sortable: true,
                visible: true
            }, {
                field: 'wlmc',
                title: '物料名称',
                titleTooltip:'物料名称',
                width: '15%',
                align: 'left',
				visible: true
            },
            {
                field: 'gg',
                title: '规格',
                titleTooltip:'规格',
                width: '15%',
                align: 'left',
                visible: true,
            }, {
                field: 'cksl',
                title: '近两个月出库数量',
                titleTooltip:'近两个月出库数量',
                width: '8%',
                align: 'left',
				sortable: true,
                visible: true
            }, {
                field: 'kcl',
                title: '库存量',
                titleTooltip:'库存量',
                width: '8%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function (map) {
                t_mapT = map;
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {

            },
        });
    };
    //得到查询的参数
    oTableInit.queryParams = function(params){
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   //页面大小
        	pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "szid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
        };

        return setmaterielSearchData(map);
    };
    return oTableInit;
};



function setmaterielSearchData(map){
	var cxtj = $("#setmateriel_formSearch #cxtj").val();
	// 查询条件
	var cxnr = $.trim(jQuery('#setmateriel_formSearch #cxnr').val());
	if (cxtj == "0") {
		map["entire"] = cxnr;
	}else if (cxtj == "1") {
		map["wlbm"] = cxnr;
	}else if (cxtj == "2") {
		map["wlmc"] = cxnr;
	}
	return map;
}


function addWl() {
    var url = $("#setmateriel_formSearch #urlPrefix").val()+"/agreement/agreement/pagedataListMaterial?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择物料', chooseWlConfig);
}
var chooseWlConfig = {
    width : "1200px",
    height : "500px",
    modalName	: "chooseWlModal",
    formName	: "chooseMaterialForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chooseMaterialForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var sel = $("#chooseMaterialForm #yxwl").tagsinput('items');
                var ids="";
                for(var i = 0; i < sel.length; i++){
                    var value = sel[i].value;
                    ids+=","+value;
                }

                if(ids){
                    ids=ids.substring(1);
                    var arr = ids.split(",");
                    for(var i=0;i<arr.length;i++){
                        for(var j=0;j<t_mapT.rows.length;j++){
                            if(arr[i]==t_mapT.rows[j].wlid){
                                arr.splice(i,1);
                                break;
                            }
                        }
                    }
                    ids = arr.join(",")
                    $.ajax({
                        async:false,
                        url: $('#setmateriel_formSearch #urlPrefix').val()+'/progress/produce/pagedataSetGrsz',
                        type: 'post',
                        dataType: 'json',
                        data : {"ids":ids, "access_token":$("#ac_tk").val()},
                        success: function(data) {

                        }
                    })
                }
                $('#setmateriel_formSearch #view_list').bootstrapTable('refresh');

            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
   }
}

function delWl() {
    var sel_row = $('#setmateriel_formSearch #view_list').bootstrapTable('getSelections');//获取选择行数据
    if(sel_row.length==0){
        $.error("请至少选中一行");
        return;
    }else {
        var ids="";
        for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
            ids= ids + ","+ sel_row[i].szid;
        }
        ids=ids.substr(1);
        $.confirm('您确定要删除所选择的信息吗？',function(result){
            if(result){
                jQuery.ajaxSetup({async:false});
                var url= $("#setmateriel_formSearch #urlPrefix").val()+'/progress/produce/pagedataDelGrsz';
                jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                    setTimeout(function(){
                        if(responseText["status"] == 'success'){
                            $.success(responseText["message"],function() {
                                $('#setmateriel_formSearch #view_list').bootstrapTable('refresh');
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
}




$(function(){
    //1.初始化Table
    var oTable = new setmateriel_TableInit();
    oTable.Init();
	//所有下拉框添加choose样式
	jQuery('#setmateriel_formSearch .chosen-select').chosen({width: '100%'});

});
