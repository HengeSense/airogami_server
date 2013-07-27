collectLimit = 1;
collectPlaneStart = "";
collectChainStart = "";
collect = document.getElementById('Collect');
collectTemplate = document.getElementById('CollectTemplate');
collectError = document.getElementById('CollectError');
collectPlaneStartIdx = 0;
collectChainStartIdx = 0;

planeView = document.getElementById('PlaneView');
chainView = document.getElementById('ChainView');

function receivePlanes() {
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			eval("ret=" + xmlhttp.responseText);
			if (ret.status != 0) {
				collectError.innerHTML = ret.message;
			} else {
				for ( var i = 0; i < ret.result.planes.length; ++i) {
					var plane = ret.result.planes[i];
					var planeRow = collectTemplate.cloneNode(true);					
					planeRow.id = '';
					planeRow.cells[0].innerHTML = plane.planeId;
					planeRow.cells[1].innerHTML = plane.messages[0].content;
					planeRow.cells[2].innerHTML = plane.category.name;
					planeRow.cells[3].innerHTML = plane.accountByOwnerId.fullName;
					planeRow.cells[4].innerHTML = plane.city;
					planeRow.cells[5].innerHTML = plane.province;
					planeRow.cells[6].innerHTML = plane.country;
					planeRow.cells[7].innerHTML = plane.updatedTime;
					planeRow.cells[8].innerHTML = 'plane';
					plane.type = 'plane';
					planeRow.data = plane;
					collect.rows[1].parentNode.insertBefore(planeRow, collect.rows[1]);
					//planeRow.style.display = '';
					if(collectPlaneStart == plane.updatedTime){
						++collectPlaneStartIdx;
					}
					else{
						collectPlaneStartIdx = 0;
					}
					collectPlaneStart = plane.updatedTime;
				}
				if(ret.result.more){
					receivePlanes();
				}
			}
		}
	};
	var params = 'forward=true&limit=' + collectLimit + '&start=' + collectPlaneStart + "&startIdx=" + collectPlaneStartIdx;
	xmlhttp.open("GET", "/plane/receivePlanes.action?" + params, true);
	xmlhttp.send();
}

function receiveChains() {
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
					var chainRow = collectTemplate.cloneNode(true);					
					chainRow.id = '';
					chainRow.cells[0].innerHTML = chain.chainId;
					chainRow.cells[1].innerHTML = 'wating';
					chainRow.cells[2].innerHTML = 'not applicable';
					chainRow.cells[3].innerHTML = chain.account.fullName;
					chainRow.cells[4].innerHTML = chain.city;
					chainRow.cells[5].innerHTML = chain.province;
					chainRow.cells[6].innerHTML = chain.country;
					chainRow.cells[7].innerHTML = chain.updatedTime;
					chainRow.cells[8].innerHTML = 'chain';
					chain.cell = chainRow.cells[1];
					chain.type = 'chain';
					chainRow.data = chain;
					collect.rows[1].parentNode.insertBefore(chainRow, collect.rows[1]);
					//chainRow.style.display = '';
					if(collectChainStart == chain.updatedTime){
						++collectPlaneStartIdx;
					}
					else{
						collectChainStartIdx = 0;
					}
					collectChainStart = chain.updatedTime;
					receiveChainMessages(chain);
				}
				if(ret.result.more){
					receiveChains();
				}
			}
		}
	};
	var params = 'forward=true&limit=' + collectLimit + '&start=' + collectChainStart + "&startIdx=" + collectChainStartIdx;
	xmlhttp.open("GET", "/chain/receiveChains.action?" + params, true);
	xmlhttp.send();
}

function receiveChainMessages(chain) {
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			eval("ret=" + xmlhttp.responseText);
			if (ret.status != 0) {
				collectError.innerHTML = ret.message;
			} else {
				chain.chainMessages = ret.result.chainMessages;
				chain.cell.innerHTML = chain.chainMessages[0].content;
			}
		}
	};
	var params = 'chainId=' + chain.chainId;
	xmlhttp.open("GET", "/chain/obtainChainMessages.action?" + params, true);
	xmlhttp.send();
}

function view(the) {
	var row = the.parentNode.parentNode;
	var win = window.open('', "View planes or chains", 'width=400,height=400');
	win.data = row.data;
	if(row.data.type == 'plane')
	{
		win.document.write(planeView.innerHTML);
		win.document.close();
		win.document.forms[0].planeId.value = row.data.planeId;
		win.document.forms[1].planeId.value = row.data.planeId;	
		win.document.forms[0].message.value = row.data.accountByOwnerId.fullName + ": " + row.data.messages[0].content;	
	}
	else
	{
		win.document.write(chainView.innerHTML);
		win.document.close();
		win.document.forms[0].chainId.value = row.data.chainId;
		win.document.forms[1].chainId.value = row.data.chainId;	
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
