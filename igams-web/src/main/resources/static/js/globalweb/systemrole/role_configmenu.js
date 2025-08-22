


$(function(){
	 var sel_zt = $("#system #zt");
	sel_zt.unbind("change").change(function(){
		ztEvent();
    });
    jQuery('#system .chosen-select').chosen({width: '100%'});
})


/**
 * 账套下拉框事件
 */
function ztEvent(){
    var zt = $("#system #zt").val();
    if(zt == null || zt==""){
        var mkHtml = "";
        mkHtml += "<option value=''>--请选择--</option>";
        $("#system #mk").empty();
        $("#system #mk").append(mkHtml);
        $("#system #mk").trigger("chosen:updated");
        //清除js树
        $.jstree.destroy ();
        return;
    }
    var cskz1 = $("#system #"+zt).attr("cskz1");
    var url="/systemrole/role/pagedataQueryMk";
    $.ajax({
        type:'post',
        url:url,
        cache: false,
        data: {"cdcc":cskz1,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            if(data != null && data.length != 0){
                var mkHtml = "";
                mkHtml += "<option value=''>--请选择--</option>";
                $.each(data,function(i){
                	mkHtml += "<option value='" + data[i].zyid + "' id='"+data[i].zyid+"' zybt='"+data[i].zybt + "' zyid='"+data[i].zyid +"'>" + data[i].zybt + "</option>";
                });
                $("#system #mk").empty();
                $("#system #mk").append(mkHtml);
                $("#system #mk").trigger("chosen:updated");
            }else{
                var mkHtml = "";
                mkHtml += "<option value=''>--请选择--</option>";
                $("#system #mk").empty();
                $("#system #mk").append(mkHtml);
                $("#system #mk").trigger("chosen:updated");
            }
        }
    });
}

$("#search_input").keyup(function () {
    console.log($('#search_input').val())
    $('#menuTree').jstree(true).search($('#search_input').val());
});
$("#system #mk").change(function(){
	var zyid=$("#system #mk").find("option:selected").attr("zyid");
	var fbsqz=$("#system #zt").find("option:selected").attr("cskz2");
	
	$.jstree.destroy ();
	$('#menuTree').jstree( {'plugins':["wholerow","checkbox", "search"],
        'search': {
            "case_insensitive": true,
            "show_only_matches" : true,
            "show_only_matches_children" : true,
        },
		'core' : {
			'data' : {
				'url' : '/systemrole/role/pagedataMenuTree',
				'data' : function (node) {
					var map = {};
					map['jsid'] = jQuery("#menutreeDiv #jsid").val();
					map['access_token'] = $("#ac_tk").val();
					map['zyid'] = zyid;
					map['fbsqz'] = fbsqz;
					return map;
	            }
			}
		}
	}).on('loaded.jstree', function(e, data){
		var wH  = $(window).height();
		$(this).parents('.modal-dialog').height(550);
		$(this).parents('.bootbox-body').height(550);
		var dialogH = $(this).parents('.modal-dialog').height();
		var top = (wH - dialogH)/2;
		$(this).parents('.modal-dialog').css("top",top);
	});

});

