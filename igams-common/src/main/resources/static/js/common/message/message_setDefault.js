function xxOnclick(glxx,csmc,e){
    var jsonString = $("#setForm #dyxxJson").val();
    var dyxxJson = JSON.parse(jsonString);
    var dyxx = dyxxJson[csmc];
    for(var i=0;i<dyxx.length;i++){
        if(dyxx[i].glxx==glxx){
            dyxx[i].szz=dyxx[i].szz=="0"?"1":"0";
        }
    }
    $("#setForm #dyxxJson").val(JSON.stringify(dyxxJson));
}