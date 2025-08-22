var flag = 0;
$("#wksjxxTitle").click(function(){
	if (flag == 0){
		var wkid = $("#ajaxForm #wkid").val();
		var wksjid = $("#ajaxForm #wksjid").val();
		$.ajax({
			type:'post',
			url:"/experiment/library/pagedataWksjxxData",
			cache: false,
			data: {"wkid":wkid,"wksjid":wksjid,"access_token":$("#ac_tk").val()},
			dataType:'json',
			success:function(data){
				flag = 1;
				var wksjglDto = data.wksjgl;

				$.each(data.wksjmxlist,function(i,value){
					ybbh=value.ybbh;
					if(ybbh==null){
						ybbh="";
					}
					wkbm=value.wkbm;
					if(wkbm==null){
						wkbm="";
					}
					xm=value.xm;
					if(xm==null){
						xm="";
					}
					jt1=value.jt1;
					if(jt1==null){
						jt1="";
					}
					jt2=value.jt2;
					if(jt2==null){
						jt2="";
					}
					dnand=value.dnand;
					if(dnand==null){
						dnand="";
					}
					hsnd=value.hsnd;
					if(hsnd==null){
						hsnd="";
					}
					wknd=value.wknd;
					if(wknd==null){
						wknd="";
					}
					tj=value.tj;
                    if(tj==null){
                        tj="";
                    }
                    hbnd=value.hbnd;
                    if(hbnd==null){
                        hbnd="";
                    }
                    nddw=value.nddw;
                    if(nddw==null){
                        nddw="";
                    }
                    ndpm=value.ndpm;
                    if(ndpm==null){
                        ndpm="";
                    }
					yy=value.yy;
					if(yy==null){
						yy="";
					}
					yjxjsjl=value.yjxjsjl;
					if(yjxjsjl==null){
						yjxjsjl="";
					}
					xsbs=value.xsbs;
					if(xsbs==null){
						xsbs="";
					}
					project=value.project_type;
                    if(project==null){
                        project="";
                    }
					// var ht =
					// 	"<div >" +
					// 	"<div class='text-grey col-md-4 col-sm-12' style='padding: 5px;'>序号：<span>"+(i+1)+"</span></div>" +
					// 	"<div class='text-grey col-md-8 col-sm-12' style='padding: 5px;'>样本编号：<span>"+ybbh+"</span></div>" +
					// 	"<div class='text-grey col-md-4 col-sm-12' style='padding: 5px;'>文库编码：<span>"+wkbm+"</span></div>" +
					// 	"<div class='text-grey col-md-8 col-sm-12' style='padding: 5px;'>项目：<span>"+xm+"</span></div>" +
					// 	"<div class='text-grey col-md-4 col-sm-12' style='padding: 5px;'>接头1：<span>"+jt1+"</span></div>"+
					// 	"<div class='text-grey col-md-8 col-sm-12' style='padding: 5px;'>接头2：<span>"+jt2+"</span></div>" +
					// 	"<div class='text-grey col-md-4 col-sm-12' style='padding: 5px;'>DNA浓度：<span>"+dnand+"</span></div>" +
					// 	"<div class='text-grey col-md-8 col-sm-12' style='padding: 5px;'>核酸浓度：<span>"+hsnd+"</span></div>" +
					// 	"<div class='text-grey col-md-4 col-sm-12' style='padding: 5px;'>文库浓度：<span>"+wknd+"</span></div>" +
					// 	"<div class='text-grey col-md-8 col-sm-12' style='padding: 5px;'>预计下机数据：<span>"+yjxjsjl+"</span></div>" +
					// 	"<div class='text-grey col-md-4 col-sm-12' style='padding: 5px;'>稀释倍数：<span>"+xsbs+"</span></div>" +
					// 	"<div class='text-grey col-md-12 col-sm-12'style='border-bottom:1px solid black;'></div>"+
					// 	"</div>";

					var ht =
						"<tr>"+
						"<td>"+(i+1)+"</td>"+
						"<td title='"+ybbh+"'>"+ybbh+"</td>"+
						"<td title='"+wkbm+"'>"+wkbm+"</td>"+
						// "<td title='"+xm+"'>"+xm+"</td>"+
						"<td title='"+jt1+"'>"+jt1+"</td>"+
						"<td title='"+jt2+"'>"+jt2+"</td>"+
						"<td title='"+dnand+"'>"+dnand+"</td>"+
						"<td title='"+hsnd+"'>"+hsnd+"</td>"+
						"<td title='"+wknd+"'>"+wknd+"</td>"+
						"<td title='"+tj+"'>"+tj+"</td>"+
						"<td title='"+hbnd+"'>"+hbnd+"</td>"+
						"<td title='"+nddw+"'>"+nddw+"</td>"+
						"<td title='"+ndpm+"'>"+ndpm+"</td>"+
						"<td title='"+yy+"'>"+yy+"</td>"+
						"<td title='"+yjxjsjl+"'>"+yjxjsjl+"</td>"+
						"<td title='"+xsbs+"'>"+xsbs+"</td>"+
						"<td title='"+project+"'>"+project+"</td>"+
						"</tr>"

					$("#wksjxx").append(ht);
				});
			}
		});
	}

})

function view(wkid){
	var url= "/experiment/library/viewLibrary?wkid=" + wkid+"&flag=merge";
	$.showDialog(url,'文库信息',viewMergeLibraryConfig);
}

var viewMergeLibraryConfig = {
	width		: "1200px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};


$(function(){
	var wkid=$("#wkid").val();
	$.ajax({
		type:'post',
		url:"/experiment/library/pagedataCkxx",
		cache: false,
		data: {"wkid":wkid,"access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			//返回值
			var list=data;
			var lie;
			var num;
			for(var i=0;i<list.length;i++){
				if(list[i].xh%8==0){
					lie=parseInt(list[i].xh/8);
					num=8;
					$("#lie"+lie+"-"+num).text(list[i].jtxx+"--"+list[i].nbbh);
					$("#lie"+lie+"-"+num).attr("title",list[i].jtxx+"--"+list[i].nbbh);
					if(list[i].quantity!=null && list[i].quantity!=''){
						$("#liend"+lie+"-"+num).text(list[i].quantity);
						$("#liend"+lie+"-"+num).attr("title",list[i].quantity);
					}
				}else{
					lie=parseInt(list[i].xh/8)+1;
					num=list[i].xh%8;
					$("#lie"+lie+"-"+num).text(list[i].jtxx+"--"+list[i].nbbh);
					$("#lie"+lie+"-"+num).attr("title",list[i].jtxx+"--"+list[i].nbbh);
					if(list[i].quantity!=null && list[i].quantity!=''){
						$("#liend"+lie+"-"+num).text(list[i].quantity);
						$("#liend"+lie+"-"+num).attr("title",list[i].quantity);
					}
				}
			}
		}
	});
})
function printWk(){
    window.open("/experiment/library/exportLibrary?access_token="+$("#ac_tk").val()+"&wkid="+$("#wkid").val())
}