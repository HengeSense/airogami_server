<%@ page language="java" import="java.util.*, com.airogami.presentation.logic.User" pageEncoding="UTF-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Airogami Home Page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<style type="text/css">
    .container{
    margin:auto;
    width:400px;
    }
    </style>
  </head>
  
  <body>
  <div class="container">
    <h2>Airogami Home Page</h2>
    <a href="Signup.html">Signup</a> <a href="SigninAndOut.jsp">SigninAndOut</a>
    <p><% 
    User user = (User)request.getSession(true).getAttribute("user");
    if(user != null){
        out.print("Welcome " + user.getAccountId());
    }
    else{
        out.print("Please signin");
    }
     %> </p>
     <% if(user != null) {%>
     <h3>Account:</h3>
     <a href="EditAccount.jsp"><button>Edit Account</button></a>
     <a href="EditAuthenticate.jsp"><button>Edit Authenticate</button></a>
     <a href="ViewAccount.jsp"><button>View Account</button></a>
     <h3>Plane:</h3>
     <a href="SendPlane.jsp"><button>Send Plane</button></a>
     <a href="GetPlanes.jsp"><button>Get Planes</button></a>
     <a href="ObtainPlanes.jsp"><button>Obtain Planes</button></a>
     <a href="ObtainPlaneIds.jsp"><button>Obtain PlaneIds</button></a>
     <a href="LikeDeletePlane.jsp"><button>Like Delete Plane</button></a>
     <a href="PickupBothThrowPlane.jsp"><button>Pickup Both and Throw Plane</button></a>
     <a href="PlaneMessages.jsp"><button>Plane Messages</button></a>
     <h3>Chain:</h3>
     <a href="SendChain.jsp"><button>Send Chain</button></a>
     <a href="GetChains.jsp"><button>Get Chains</button></a>
     <a href="ObtainChains.jsp"><button>Obtain Chains</button></a>
     <a href="ObtainChainIds.jsp"><button>Obtain ChainIds</button></a>
     <a href="DeleteChain.jsp"><button>Delete Chain</button></a>
     <a href="ThrowChain.jsp"><button>Throw Chain</button></a>
     <a href="ChainMessages.jsp"><button>Chain Messages</button></a>
     <h3>Interaction:</h3>
     <a href="Collect.jsp"><button>Collect</button></a>
     <a href="Chats.jsp"><button>Chats</button></a>
     <h3>Connection:</h3>
     <a href="Connection.jsp"><button>Connection</button></a>
     <h3>Data:</h3>
     <a href="DataManager.jsp"><button>Data Manager</button></a>
     <%} %>
     </div>
  </body>
</html>
