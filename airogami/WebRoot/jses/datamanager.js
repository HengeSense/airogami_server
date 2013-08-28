var info = document.getElementById('info');
var xmlhttp = new XMLHttpRequest();
xmlhttp.onreadystatechange = function() {
	if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
		eval("ret=" + xmlhttp.responseText);
		if (ret.status != 0) {
			info.innerHTML = ret.message;
		} else {
			info.innerHTML = xmlhttp.responseText;
			with(document.forms[xmlhttp.index]){
				policy.value = ret.result.policy;
				signature.value = ret.result.signature;
				key.value = ret.result.key;
			}
		}
	}
};

function getToken(idx) {
	info.innerHTML = 'Connecting';
	var method = idx == 0 ? 'upload' : "download";
	var params = 'method=' + method + "&random=" + Math.random();
	xmlhttp.index = idx;
	xmlhttp.open("GET", "/data/dataManager?" + params, true);
	xmlhttp.setRequestHeader("Cache-Control", "no-cache");
	xmlhttp.send();
}
