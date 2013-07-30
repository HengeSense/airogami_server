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
				var before = firstChild;
				for(var i = 0; i < ret.conditions.length; ++i){
					var condition = ret.conditions[i];
					var input = document.createElement('input');
					input.type = 'hidden';
					for( var idx in condition){
						input.name = idx;
						input.value = condition[idx];
					}
					insertBefore(input, before);
				}
				
			}
		}
	}
};

function getToken(idx) {
	reset(idx);
	info.innerHTML = 'Connecting';
	var method = idx == 0 ? 'upload' : "download";
	var params = 'method=' + method + "&random=" + Math.random();
	xmlhttp.index = idx;
	xmlhttp.open("GET", "/Data/DataManager?" + params, true);
	xmlhttp.setRequestHeader("Cache-Control", "no-cache");
	xmlhttp.send();
}

function reset(idx){
	var inputs = document.forms[idx].getElementsByTagName('input');
	for( var i = 0; i < inputs.lenght;){
		var input = inputs[i];
		if(input.type == 'hidden'){
			input.parentNode.removeChild(input);
		}
		else{
			++i
		}
	}
}
