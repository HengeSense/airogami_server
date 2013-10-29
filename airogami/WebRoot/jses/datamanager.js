var info = document.getElementById('info');
var xmlhttp = new XMLHttpRequest();
xmlhttp.onreadystatechange = function() {
	if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
		eval("ret=" + xmlhttp.responseText);
		if (ret.status != 0) {
			info.innerHTML = ret.message;
		} else {
			info.innerHTML = xmlhttp.responseText;
			eval("token=" + ret.result.token);
			with(document.forms[0]){
				policy.value = token.policy;
				signature.value = token.signature;
				key.value = token.key;
				acl.value = token.acl;
				AWSAccessKeyId.value = token.AWSAccessKeyId;
			}
		}
	}
};

function getToken(idx) {
	info.innerHTML = 'Connecting';
	var actions = ["accountIconTokens", "messageDataToken", "chainDataToken"];
	var action = actions[idx];
	var params = "";
	if(idx > 0){
		params = "type=" + document.forms[0].type.value;
		if(idx == 2){
			params += "&chainId=" + document.forms[0].chainId.value;
		}
	}
	params = params + "&random=" + Math.random();
	//xmlhttp.index = 0;
	xmlhttp.open("GET", "/data/" + action + ".action?" + params, true);
	xmlhttp.setRequestHeader("Cache-Control", "no-cache");
	xmlhttp.send();
}
