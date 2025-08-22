function getJdlist(){
	var obj = document.getElementById("xmjdid");
	var dqjdxh=$("#dqjdxh").val();
	var maxxh=$(obj[obj.length-1]).attr("xh");
	if(dqjdxh<maxxh){
		obj.options[dqjdxh].selected = true;
	}
}
$(function(){
	getJdlist();
	jQuery('#chageJdForm .chosen-select').chosen({width: '100%'});
});