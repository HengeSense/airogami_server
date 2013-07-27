collectLimit = 10;
collectPlaneStart = "";
collectChainStart = "";
collect = document.getElementById('Collect');
collectTemplate = document.getElementById('CollectTemplate');
collectError = document.getElementById('CollectError');

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
					collectPlaneStart = plane.updatedTime;
				}
				if(ret.result.more){
					receivePlanes();
				}
			}
		}
	};
	var params = 'forward=true&limit=' + collectLimit + '&start=' + collectPlaneStart;
	xmlhttp.open("GET", "/plane/obtainPlanes.action?" + params, true);
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
				for ( var i = 0; i < ret.result.chains.length; ++i) {
					var chain = ret.result.chains[i];
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
					chain.type = 'chain';
					chainRow.data = chain;
					collect.rows[1].parentNode.insertBefore(chainRow, collect.rows[1]);
					//chainRow.style.display = '';
					collectChainStart = chain.updatedTime;
				}
				if(ret.result.more){
					receivePlanes();
				}
			}
		}
	};
	var params = 'forward=true&limit=' + collectLimit + '&start=' + collectChainStart;
	xmlhttp.open("GET", "/chain/obtainChains.action?" + params, true);
	xmlhttp.send();
}

function view(the) {
	var row = the.parentNode.parentNode;
	var win = window.open('', "View planes or chains", 'width=300,height=300');
	win.data = row.data;
	if(row.data.type == 'plane')
	{
		win.document.write(planeView.innerHTML);
		win.document.close();
		win.document.forms[0].planeId.value = row.data.planeId;
		win.document.forms[1].planeId.value = row.data.planeId;		
	}
	else
	{
		win.document.write(chainView.innerHTML);
		win.document.close();
		win.document.forms[0].chainId.value = row.data.chainId;
		win.document.forms[1].chainId.value = row.data.chainId;	
	}
	
}

function reply()
{
}

function toss()
{
}