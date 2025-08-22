//=======================================================================
//动态获取服务地址
//=======================================================================
var protocol = window.location.protocol;
var baseService = window.location.host;
var pathName = window.location.pathname;
var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);

//=========================================================================
  
var recorder = null;
var startButton = $("#audiocontainer #btn-start-recording");
var stopButton = $("#audiocontainer #btn-stop-recording");
var playButton = $("#audiocontainer #btn-start-palying");

//var audio = document.querySelector('audio');
var audio = $("#audiocontainer #audioSave");

function startRecording() {
	if(recorder != null) {
		recorder.close();
	}
	Recorder.get(function (rec) {
		recorder = rec;
		recorder.start();
	});
	stopButton.prop("disabled",false);
	playButton.prop("disabled",false);
}

function stopRecording() {
	recorder.stop();
	var svrUrl =  "/ws/audio/trans";
	recorder.trans(svrUrl+"?access_token="+$("#ac_tk").val(), function(responseText,statusText){
	  if(responseText.status != "fail"){
			var $audioinfo=$("#audiocontainer #info");
			//if(responseText.textString.indexOf("结束语音")>-1)
			$audioinfo.html($audioinfo.html()+ responseText.textString);
	  }
	});
}

function playRecording() {
	recorder.play(audio);
}

function audioAutoSend(){
	stopRecording();
	return true;
}

function audioAutoRestart(){
	startRecording();
	return true;
}