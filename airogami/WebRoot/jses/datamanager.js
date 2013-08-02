var info = document.getElementById('info');
var xmlhttp = new XMLHttpRequest();
xmlhttp.onreadystatechange = function() {
	if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
		eval("ret=" + xmlhttp.responseText);
		if (ret.status != 0) {
			info.innerHTML = ret.message;
		} else {
			info.innerHTML = ret.conditions;
			with(document.forms[xmlhttp.index]){
				policy.value = ret.policy;
				signature.value = ret.signature;
				key.value = ret.conditions[0].key;
			}
		}
	}
};

function getToken(idx) {
	info.innerHTML = 'Connecting';
	var method = idx == 0 ? 'upload' : "download";
	var params = 'method=' + method + "&random=" + Math.random();
	xmlhttp.index = idx;
	xmlhttp.open("GET", "/Data/DataManager?" + params, true);
	xmlhttp.setRequestHeader("Cache-Control", "no-cache");
	xmlhttp.send();
}
