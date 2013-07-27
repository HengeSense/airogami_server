<%@ page language="java"
	import="java.util.*, com.airogami.persistence.entities.Account,java.text.SimpleDateFormat"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>Collect</title>

<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="this is my page">
<meta http-equiv="content-type" content="text/html; charset=utf-8">

<!--<link rel="stylesheet" type="text/css" href="./styles.css">-->
<style type="text/css">
.CollectTemplate {
}
.container{
  width:500px;
  margin:auto;
}
</style>
</head>
<body>
	<div class="container">
		<p>
			<%
				Account account = (Account) request.getSession(true).getAttribute(
						"account");
				if (account != null) {
					out.print("Receive for " + account.getFullName());
				} else {
					out.print("Please signin");
				}
			%>
		</p>
		<%
			if (account != null) {
		%>
		<table>
			<tr>
				<td><p id="CollectError"></p>
					<button onclick="receivePlanes()">Receive Planes</button>
					<button onclick="receiveChains()">Receive Chains</button>
					<table class="Collect" id="Collect" border="1">
						<caption>Collect</caption>
						<tr>
							<th>PlaneId/ChainId</th>
							<th>Message</th>
							<th>Category</th>
							<th>Owner</th>
							<th>City</th>
							<th>Province</th>
							<th>Country</th>
							<th>UpdatedTime</th>
							<th>Type</th>
							<th>View</th>
						</tr>
						<tr id="CollectTemplate" class="CollectTemplate">
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td><input type="button" value="View" onclick="view(this)"></td>
						</tr>
					</table></td>
				<td></td>
			</tr>
		</table>
		<xmp id="PlaneView" style="display:none"> <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
		<html>
<head>
<title>View Planes</title>
</head>
<body>
	<form action="plane/replyPlane.action">
	    <label>PlaneId: </label>
		<input type="text" name="planeId" readonly="readonly"><br>
		<label>Type: </label>
		<input type="text"
			name="messageVO.type" value="1"><br>
		<label>Message: </label>
	    <textarea name="message" rows="2" cols="50"  readonly="readonly"></textarea><br>
		<label>Content: </label>
		<textarea name="messageVO.content" rows="2" cols="50"></textarea>
		<br> <input type="submit" value="reply">
	</form>
	<form action="plane/throwPlane.action">
	    <label>Type: </label>
		<input type="text" name="planeId" readonly="readonly"><br>
		 <input type="submit"
			value="Toss Back">
	</form>
</body>
		</html>
		</xmp>
		
		<xmp id="ChainView" style="display:none"> <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
		<html>
<head>
<title>View Chains</title>
</head>
<body>
	<form action="chain/replyChain.action">
	    <label>ChainId: </label>
		<input type="text" name="chainId" readonly="readonly"><br>
		<label>Type: </label>
		<input type="text"
			name="chainMessageVO.type" value="1"><br>
		<label>Messages: </label>
	    <textarea name="messages" rows="6" cols="50"  readonly="readonly"></textarea><br>
		<label>Content: </label>
		<textarea name="chainMessageVO.content" rows="2" cols="50"></textarea>
		<br> <input type="submit" value="reply">
	</form>
	<form action="chain/throwChain.action">
	    <label>Type: </label>
		<input type="text" name="chainId" readonly="readonly"><br>
		 <input type="submit"
			value="Toss Back">
	</form>
</body>
		</html>
		</xmp>
		
		<script type="text/javascript" src="jses/collect.js">
			
		</script>
		<%
			}
		%>
	</div>

</body>
</html>
