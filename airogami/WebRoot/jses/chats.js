collectLimit = 100;
collectPlaneStart = "";
collectChainStart = "";
collect = document.getElementById('Collect');
collectTemplate = document.getElementById('CollectTemplate');
collectError = document.getElementById('CollectError');
collectPlaneStartIdx = 0;
collectChainStartIdx = 0;

planeView = document.getElementById('PlaneView');
chainView = document.getElementById('ChainView');


function obtainPlanes() {
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			eval("ret=" + xmlhttp.responseText);
			if (ret.status != 0) {
				collectError.innerHTML = ret.message;
			} else {
				for ( var i = 0; i < ret.result.planes.length; ++i) {
					var plane = ret.result.planes[i];
					var rows = collect.rows;
					var planeRow=null; 
					for(var j=0;j<rows.length;++j){
						if(rows[j].type == 'plane' && rows[j].data.planeId == plane.planeId){
							rows[j].parentNode.removeChild(rows[j]);
							planeRow = rows[j];
							break;
						}
					}
					if(planeRow == null){
						planeRow = collectTemplate.cloneNode(true);	
					}				
					planeRow.id = '';
					planeRow.cells[0].innerHTML = plane.planeId;
					planeRow.cells[1].innerHTML =plane.accountByTargetId.fullName;
					planeRow.cells[2].innerHTML = plane.category.name;
					planeRow.cells[3].innerHTML = plane.accountByOwnerId.fullName;
					planeRow.cells[4].innerHTML = plane.city;
					planeRow.cells[5].innerHTML = plane.province;
					planeRow.cells[6].innerHTML = plane.country;
					planeRow.cells[7].innerHTML = plane.updatedTime;
					planeRow.cells[8].innerHTML = 'plane';
					planeRow.type = 'plane';
					planeRow.data = plane;
					collect.rows[1].parentNode.insertBefore(planeRow, collect.rows[1]);
					//planeRow.style.display = '';
					if(collectPlaneStart == plane.updatedTime){
						//++collectPlaneStartIdx;
					}
					else{
						collectPlaneStartIdx = 0;
					}
					collectPlaneStart = plane.updatedTime;
					receivePlaneMessages(planeRow);
				}
				if(ret.result.more){
					obtainPlanes();
				}
			}
		}
	};
	var params = 'forward=true&limit=' + collectLimit + '&start='
	+ collectPlaneStart + "&startIdx=" + collectPlaneStartIdx + "&random=" + Math.random();	
	xmlhttp.open("GET", "/plane/obtainPlanes.action?" + params, true);
	xmlhttp.setRequestHeader("Cache-Control","no-cache");
	xmlhttp.send();
}

function obtainChains() {
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			eval("ret=" + xmlhttp.responseText);
			if (ret.status != 0) {
				collectError.innerHTML = ret.message;
			} else {
				var chains = ret.result.chains;
				for ( var i = 0; i < chains.length; ++i) {
					var chain = chains[i];
					var rows = collect.rows;
					var chainRow=null; 
					for(var j=0;j<rows.length;++j){
						if(rows[j].type == 'plane' && rows[j].data.chainId == chain.chainId){
							rows[j].parentNode.removeChild(rows[j]);
							chainRow = rows[j];
							break;
						}
					}
					if(chainRow == null){
						chainRow = collectTemplate.cloneNode(true);	
					}				
					chainRow.id = '';
					chainRow.cells[0].innerHTML = chain.chainId;
					chainRow.cells[1].innerHTML = 'N.A.';
					chainRow.cells[2].innerHTML = 'N.A.';
					chainRow.cells[3].innerHTML = chain.account.fullName;
					chainRow.cells[4].innerHTML = chain.city;
					chainRow.cells[5].innerHTML = chain.province;
					chainRow.cells[6].innerHTML = chain.country;
					chainRow.cells[7].innerHTML = chain.updatedTime;
					chainRow.cells[8].innerHTML = 'chain';
					chain.cell = chainRow.cells[1];
					chainRow.data = chain;
					chainRow.type = 'chain';
					collect.rows[1].parentNode.insertBefore(chainRow, collect.rows[1]);
					//chainRow.style.display = '';
					if(collectChainStart == chain.updatedTime){
						//++collectChainStartIdx;
					}
					else{
						collectChainStartIdx = 0;
					}
					collectChainStart = chain.updatedTime;
					receiveChainMessages(chainRow);
				}
				if(ret.result.more){
					obtainChains();
				}
			}
		}
	};
	var params = 'forward=true&limit=' + collectLimit + '&start=' + collectChainStart + "&startIdx=" + collectChainStartIdx;
	xmlhttp.open("GET", "/chain/obtainChains.action?" + params, true);
	xmlhttp.send();
}

function receivePlaneMessages(planeRow) {
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			eval("ret=" + xmlhttp.responseText);
			if (ret.status != 0) {
				collectError.innerHTML = ret.message;
			} else {
				planeRow.data.messages = ret.result.messages;
				if(typeof(planeRow.win)!='undefined'){
					var content = '';
					var messages = ret.result.messages;
					for(var i = 0; i < messages.length; ++i){
						//content += messages[i].id.accountId + ": ";
						content += messages[i].createdTime + "\n";
						content += messages[i].content + "\n";
					}
					planeRow.win.document.forms[0].messages.value = content;
				}
				
			}
		}
	};
	var params = 'planeId=' + planeRow.data.planeId;
	xmlhttp.open("GET", "/plane/obtainMessages.action?" + params, true);
	xmlhttp.send();
}

function receiveChainMessages(chainRow) {
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			eval("ret=" + xmlhttp.responseText);
			if (ret.status != 0) {
				collectError.innerHTML = ret.message;
			} else {
				chainRow.data.chainMessages = ret.result.chainMessages;
				//chain.cell.innerHTML = chain.chainMessages[0].content;
			}
		}
	};
	var params = 'chainId=' + chainRow.data.chainId;
	xmlhttp.open("GET", "/chain/obtainChainMessages.action?" + params, true);
	xmlhttp.send();
}

function view(the) {
	var row = the.parentNode.parentNode;
	var win = window.open('', "Reply planes or chains", 'width=400,height=400');
	win.data = row.data;
	row.win = win;
	if(row.type == 'plane')
	{
		win.document.write(planeView.innerHTML);
		win.document.close();
		win.document.forms[0].planeId.value = row.data.planeId;
		var messages = row.data.messages;
		var content = '';
		for(var i = 0; i < messages.length; ++i){
			//content += messages[i].id.accountId + ": ";
			content += messages[i].createdTime + "\n";
			content += messages[i].content + "\n";
		}
		win.document.forms[0].reply.onclick = replyPlaneMessages;
		win.document.forms[0].reply.win = win;
		win.document.forms[0].messages.value = content;		
		}
	else
	{
		win.document.write(chainView.innerHTML);
		win.document.close();
		win.document.forms[0].chainId.value = row.data.chainId;
		var chainMessages = row.data.chainMessages;
		var content = '';
		for(var i = 0; i < chainMessages.length; ++i){
			content += chainMessages[i].id.accountId + ": ";
			content += chainMessages[i].updatedTime + "\n";
			content += chainMessages[i].content + "\n";
		}
		win.document.forms[0].messages.value = content;
	}	
}

function replyPlaneMessages() {
	var win = this.win;
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			eval("ret=" + xmlhttp.responseText);
			if (ret.status != 0) {
				win.document.forms[0].error.value = ret.message;
			} else {
				win.document.forms[0].error.value = 'OK';
				win.document.forms[0]['content'].value = '';
				win.document.forms[0].messages.value += ret.result.createdTime + '\n'
				+ ret.result.content + '\n';
				var content = win.document.forms[0].messages;
				content.scrollTop = content.scrollHeight;
			}
		}
	};
	var params = 'planeId=' + win.data.planeId + '&messageVO.type=' 
	+ win.document.forms[0].type.value + '&messageVO.content='
	+ win.document.forms[0]['content'].value;
	xmlhttp.open("GET", "/plane/replyPlane.action?" + params, true);
	xmlhttp.send();
}

function notification() {
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			eval("ret=" + xmlhttp.responseText);
			if (ret.status != 0) {
				win.document.forms[0].error.value = ret.message;
			} else {
				win.document.forms[0].error.value = 'OK';
				win.document.forms[0]['content'].value = '';
				win.document.forms[0].messages.value += ret.result.createdTime + '\n'
				+ ret.result.content + '\n';
				var content = win.document.forms[0].messages;
				content.scrollTop = content.scrollHeight;
			}
		}
	};
	var params = '';
	xmlhttp.open("GET", "Notification.jsp" + params, true);
	xmlhttp.send();
}
function update()
{
	obtainPlanes();
	obtainChains();	
	setTimeout(update, 5000);
}
update();


