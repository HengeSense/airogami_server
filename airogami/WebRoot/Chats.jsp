<%@ page language="java"
	import="java.util.*, com.airogami.presentation.logic.User,java.text.SimpleDateFormat"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>Chats</title>

<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="this is my page">
<meta http-equiv="content-type" content="text/html; charset=utf-8">

<!--<link rel="stylesheet" type="text/css" href="./styles.css">-->
<style type="text/css">
.CollectTemplate {
}
.container{
  width:800px;
  margin:auto;
}
</style>
</head>
<body>
	<div class="container">
		<p>
			<%
				User user = (User)request.getSession(true).getAttribute("user");
				if (user != null) {
					out.print("Chats for " + user.getAccountId());
				} else {
					out.print("Please signin");
				}
			%>
		</p>
		<%
			if (user != null) {
		%>
		<table>
			<tr>
				<td><p id="CollectError"></p>
					<button onclick="obtainPlanes()">Obtain Planes</button>
					<button onclick="obtainChains()">Obtain Chains</button>
					<table class="Collect" id="Collect" border="1">
						<caption>Collect</caption>
						<tr>
							<th>PlaneId/ChainId</th>
							<th>Target</th>
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
	<form>
	    <label>Status: </label>
	    <input type="text" name = "error" readonly = "readonly"><br>
	    <label>PlaneId: </label>
		<input type="text" name="planeId" readonly="readonly"><br>
		<label>Messages: </label>
	    <textarea name="messages" rows="10" cols="50"  readonly="readonly"></textarea><br>	    	
		<label>Type: </label>	
		<input name="type" value="1"><br>
		<textarea name="content" rows="2" cols="50"></textarea>
		<br> <input name="reply" type="button" value="reply">
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
	<form>
	    <label>ChainId: </label>
		<input type="text" name="chainId" readonly="readonly"><br>
		<label>Messages: </label>
	    <textarea name="messages" rows="10" cols="50"  readonly="readonly"></textarea><br>	    	
	</form>
</body>
		</html>
		</xmp>
		
		<script type="text/javascript" src="jses/chats.js">
			
		</script>
		<%
			}
		%>
	</div>

</body>
</html>
