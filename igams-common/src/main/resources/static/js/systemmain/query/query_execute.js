//导出页面表格
function export_query_csv(){
	var jsonData=[];
	for(var i=0;i<$("#nr tr").length;i++){
		var data={};
		for(var j=0;j<$("#bt th").length;j++){
			data[$("#bt th")[j].title]=$("#nr tr")[i].children[j].title;
		}
		jsonData.push(data);
	}
    //列标题，逗号隔开，每一个逗号就是隔开一个单元格
	let str="";
	for(var i=0;i<$("#bt th").length;i++){
		str=str+","+$("#bt th")[i].title;
	}
	str=str+"\n";
	str=str.substr(1);
	//增加\t为了不让表格显示科学计数法或者其他格式
    for(let i = 0 ; i < jsonData.length ; i++ ){
      for(let item in jsonData[i]){
          str+=`${jsonData[i][item] + '\t'},`;     
      }
      str+='\n';
    }
  //encodeURIComponent解决中文乱码
    let uri = 'data:text/csv;charset=utf-8,\ufeff' + encodeURIComponent(str);
    //通过创建a标签实现
    let link = document.createElement("a");
    link.href = uri;
    //对下载的文件命名
    link.download =  "临时查询表.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

function export_query_xlsx(){
	   $("#excTable").table2excel({
           //exclude: ".id",  不被导出的表格行的CSS class类。
           name: "Excel Document Name",  //导出的Excel文档的名称。
           filename: "临时查询表", //Excel文件的名称。
           fileext: ".xls",
           //columns: "0,1,7,9,10,11,12",//指定不导出列 实例：columns: "0,1,2,3",下标从0开始，代表不导出第一列--第四列
           exclude_img: false,//是否导出图片 
           exclude_links: false,//是否导出超链接
           exclude_inputs: false//是否导出输入框中的内容。
       });
}