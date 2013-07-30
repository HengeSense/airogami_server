var info = document.getElementById('info');
var xmlhttp = new XMLHttpRequest();
xmlhttp.onreadystatechange = function() {
	if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
		eval("ret=" + xmlhttp.responseText);
		if (ret.status != 0) {
			info.innerHTML = ret.message;
		} else {
			info.innerHTML = 'OK';
		}
	}
};

function connection() {
	info.innerHTML = 'Connecting';
	var params = 'forward=true' + "&random=" + Math.random();
	xmlhttp.open("GET", "/plane/obtainPlanes.action?" + params, true);
	xmlhttp.setRequestHeader("Cache-Control", "no-cache");
	xmlhttp.send();
}
