
function queryByGzid(gzid){
    $.ajax({
        type : "POST",
        url :  $("#viewTrainForm #urlPrefix").val()+"/train/train/pagedataReDistribution",
        data : {"gzid":gzid,"access_token":$("#ac_tk").val()},
        dataType : "json",
        success:function(data){
            if(data.status=="success"){
                $.success(data.message);
                $('#viewTrainForm #tb_list').bootstrapTable('refresh');
            }else{
                $.error(data.message);
            }
        }
    });
}

function updateJlbh(gzid){
    var jlbh=$("#viewTrainForm #jlbh_"+gzid).val();
    var bz=$("#viewTrainForm #bz_"+gzid).val();
    if(jlbh||bz){
        $.ajax({
            type : "POST",
            async:false,
            url :  $("#viewTrainForm #urlPrefix").val()+"/train/train/pagedataUpdateJlbh",
            data : {"gzid":gzid,"jlbh":jlbh,"bz":bz,"access_token":$("#ac_tk").val()},
            dataType : "json",
            success:function(data){
                if(data.status=="success"){
                    $.success("更新成功!");
                }else{
                    $.error("更新失败!");
                }
            }
        });
    }
}
/**
 * 点击删除文件
 * @param fjid
 * @param wjlj
 * @returns
 */
function del(fjid,wjlj){
    $.confirm('您确定要删除所选择的记录吗？',function(result){
        if(result){
            jQuery.ajaxSetup({async:false});
            var url= $("#viewTrainForm #urlPrefix").val()+"/common/file/delFile";
            jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
                setTimeout(function(){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            $("#"+fjid).remove();
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
//下载模板
function xz(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    var spxz = $("#viewTrainForm #spxz").val();
    if (spxz=='0'&&type.toLowerCase()==".mp4"){
        $.error("该视频不允许点击下载")
    }else {
        jQuery('<form action="' + $("#viewTrainForm #urlPrefix").val() + '/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
            .appendTo('body').submit().remove();
    }
}
function view(fjid,wjm){

    var gzid = $("#viewTrainForm #gzid").val();
    var spwcbj = "1";
    var flg = $("#viewTrainForm #flg").val();
    var spwcbjT = $("#viewTrainForm #spwcbj").val();
    if(flg=="1" && (spwcbjT=="" || spwcbjT==null) && gzid!="" && gzid!=null ){
     $.ajax({
            async:false,
            url: $('#viewTrainForm #urlPrefix').val()+'/train/task/minidataSaveTrainSituation',
            type: 'post',
            dataType: 'json',
            data : {"gzid":gzid,"spwcbj":spwcbj, "access_token":$("#ac_tk").val()},
            success: function(data) {
            }
        });
    }
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
        var url=$("#viewTrainForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
        $.showDialog(url,'图片预览',JPGMaterConfig);
    }else if(type.toLowerCase()==".pdf"){
        var url= $("#viewTrainForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid+"&syFlag=2";
        $.showDialog(url,'文件预览',JPGMaterConfig);
    }else if(type.toLowerCase()==".mp4"){
        var url= $("#viewTrainForm #urlPrefix").val()+"/ws/videoView?fjid=" + fjid;
        $.showDialog(url,'视频预览',viewVideoConfig);
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}
var viewVideoConfig={
	width		: "1600px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
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

var trainView_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#viewTrainForm #tb_list').bootstrapTable({
            url: $("#viewTrainForm #urlPrefix").val()+'/train/test/pagedataDistributedData',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#viewTrainForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "gzgl.qwwcsj",				//排序字段
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
            showColumns: true,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [
                {
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index+1;
                    },
                    width: '3%',
                    align: 'left',
                    visible:true
                }, {
                    field: 'zsxm',
                    title: '学员',
                    width: '6%',
                    align: 'left',
                    visible: true,
                }, {
                    field: 'jgmc',
                    title: '部门',
                    width: '8%',
                    align: 'left',
                    visible: false,
                }, {
                    field: 'fqwwcsj',
                    title: '期望完成时间',
                    width: '8%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'zt',
                    title: '学习状态',
                    width: '6%',
                    align: 'left',
                    formatter: function (value, row, index) {
                        if(row.zt&&row.zt=="80"){
                            return "<span style='color:green;'>已学习</span>";
                        }else if(row.zt&&row.zt=="00"){
                            return "<span style='color:red;'>未学习</span>";
                        }if(row.zt&&row.zt=="10"){
                            return "<span style='color:blue;'>确认中</span>";
                        }else{
                            return "";
                        }
                    },
                    visible: true,
                }, {
                    field: 'sfcs',
                    title: '是否考试',
                    width: '6%',
                    align: 'left',
                    visible: true,
                }, {
                    field: 'sftg',
                    title: '是否通过',
                    width: '6%',
                    align: 'left',
                    visible: true,
                }, {
                    field: 'lrsj',
                    title: '分发日期',
                    width: '8%',
                    align: 'left',
                    visible: true,
                }, {
                    field: 'lrryxm',
                    title: '分发人员',
                    width: '8%',
                    align: 'left',
                    visible: true,
                }, {
                    field: 'zf',
                    title: '考试得分',
                    width: '8%',
                    align: 'left',
                    visible: true,
                }, {
                    field: 'jlbh',
                    title: '记录编号',
                    width: '10%',
                    align: 'left',
                    formatter:jlbhFormat,
                    visible: true,
                }, {
                    field: 'bz',
                    title: '备注',
                    width: '10%',
                    align: 'left',
                    formatter:bzFormat,
                    visible: true,
                }, {
                    field: 'cz',
                    title: '操作',
                    titleTooltip:'操作',
                    width: '12%',
                    align: 'left',
                    formatter:czFormat,
                    visible: true
                }],
            onLoadSuccess: function (map) {
            },
            onLoadError: function () {
                alert("数据加载失败！");
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
            sortLastName: "", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            ywid:$("#viewTrainForm #pxid").val(),
            //搜索框使用
            //search:params.search
        };
        var cxnr=$.trim(jQuery('#viewTrainForm #cxnr').val());
        if(cxnr){
            map["entire"]=cxnr
        }
        var cxtj = jQuery('#viewTrainForm #cxtj').val();
        map["sftg"] = cxtj;

        var fqwwcsj_start = jQuery('#viewTrainForm #fqwwcsj_start').val();
        map["qwwcsjstart"] = fqwwcsj_start;

        var fqwwcsj_end = jQuery('#viewTrainForm #fqwwcsj_end').val();
        map["qwwcsjend"] = fqwwcsj_end;

        var lrsj_start = jQuery('#viewTrainForm #lrsj_start').val();
        map["lrsjstart"] = lrsj_start;

        var lrsj_end = jQuery('#viewTrainForm #lrsj_end').val();
        map["lrsjend"] = lrsj_end;
        return map;
    };
    return oTableInit;
};

function jlbhFormat(value,row,index) {
    var jlbh="";
    if(row.jlbh){
        jlbh=row.jlbh;
    }
    return "<input type='text' id='jlbh_"+row.gzid+"'   value='"+jlbh+"' class='form-control'/>";
}

function bzFormat(value,row,index) {
    var bz="";
    if(row.bz){
        bz=row.bz;
    }
    return "<input type='text' id='bz_"+row.gzid+"'   value='"+bz+"' class='form-control'/>";
}

function czFormat(value,row,index) {
    if(row.zt=='80'){
        return "<div><span class='btn btn-success' onclick=\"updateJlbh('" + row.gzid + "',event)\" >信息更新</span><span class='btn btn-primary' onclick=\"queryByGzid('" + row.gzid + "',event)\" >重新分发</span></div>";
    }else{
        return "<div><span class='btn btn-success' onclick=\"updateJlbh('" + row.gzid + "',event)\" >信息更新</span></div>";
    }
}

function select(){
    $('#viewTrainForm #tb_list').bootstrapTable('refresh');
}

$(document).ready(function(){
    var tpfj=$("#viewTrainForm #tpfj").val();
    if(tpfj!=null&&tpfj!=""){
        $("#viewTrainForm #yctp").show();
    }else{
        $("#viewTrainForm #yctp").hide();
    }
    var spfj=$("#viewTrainForm #spfj").val();
    if(spfj!=null&&spfj!=""){
        $("#viewTrainForm #ycsp").show();
    }else{
        $("#viewTrainForm #ycsp").hide();
    }
    laydate.render({
        elem: '#fqwwcsj_start'
        , theme: '#2381E9'
        , type: 'date'
    });
    laydate.render({
        elem: '#lrsj_start'
        , theme: '#2381E9'
        , type: 'date'
    });
    laydate.render({
        elem: '#fqwwcsj_end'
        , theme: '#2381E9'
        , type: 'date'
    });
    laydate.render({
        elem: '#lrsj_end'
        , theme: '#2381E9'
        , type: 'date'
    });
    jQuery('#viewTrainForm .chosen-select').chosen({width: '100%'});
    var oTableTrain =trainView_TableInit();
    oTableTrain.Init();
});