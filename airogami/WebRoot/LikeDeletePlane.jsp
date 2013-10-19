<%@ page language="java" import="java.util.*, com.airogami.presentation.logic.User,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
  <head>
    <title>Like Delete Planes</title>
	
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    
    <!--<link rel="stylesheet" type="text/css" href="./styles.css">-->
    <style type="text/css">
    .container{
    margin:auto;
    width:300px;
    }
    </style>
  </head>
  <body>
    <div class = "container">    
     <p><% 
    User user = (User)request.getSession(true).getAttribute("user");
    if(user != null){
        out.print("Like, Delete  Plane for " + user.getAccountId());
    }
    else{
        out.print("Please signin");
    }
     %> </p>
     <%
     if(user != null){ %>
    <h2>Like Plane</h2>
    <form action="plane/likePlane.action"  method="get"> 
    <label>PlaneId: </label>
    <input type="text" name="planeId" value="1" />
        <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>ByOwner: </label>
    <input type="text" name="byOwner"  value="true"/>
        <input type="checkbox" checked="checked" onchange="hide(this)"><br>
    <br>
    <button type="button" onclick="print(0)">print</button>
    <input type="reset" name="submit" value="reset" />
    <input type="submit" name="submit" value="submit" />
    <input type="checkbox" checked="checked" onclick="hideAll(this, 0)">
    </form>  
    
    <h2>Clear Plane</h2>
    <form action="plane/clearPlane.action"  method="get"> 
    <label>PlaneId: </label>
    <input type="text" name="planeId" value="1" />
        <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <br>
    <button type="button" onclick="print(0)">print</button>
    <input type="reset" name="submit" value="reset" />
    <input type="submit" name="submit" value="submit" />
    <input type="checkbox" checked="checked" onclick="hideAll(this, 0)">
    </form> 
    
    <h2>Delete Plane</h2>
    <form action="plane/deletePlane.action"  method="get"> 
    <label>PlaneId: </label>
    <input type="text" name="planeId" value="1" />
        <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>ByOwner: </label>
    <input type="text" name="byOwner"  value="true"/>
        <input type="checkbox" checked="checked" onchange="hide(this)"><br>
    <br>
    <button type="button" onclick="print(1)">print</button>
    <input type="reset" name="submit" value="reset" />
    <input type="submit" name="submit" value="submit" />
    <input type="checkbox" checked="checked" onclick="hideAll(this, 1)">
    </form>
    
    <p id="print"></p>
    <%} %>
    </div>
    <script type = "text/javascript">
    function hideAll(the, num){
      var inputs = document.forms[num].getElementsByTagName('input')
      for( i = 0; i < inputs.length; ++i ){
          if(inputs[i].type == 'checkbox'){
             inputs[i].checked = the.checked
             inputs[i].onchange();
          }          
       }
    }
    function hide(the){
       with(the.previousSibling.previousSibling){
            var attr
            if(name.substring(name.length - 1, name.length) == "1" )
               attr = name.substring(0, name.length - 1)
            else attr = name
            if(the.checked){
                name = attr
            }
            else{
                name = attr + "1"
            }
        }       
    }
    
    function print(the){
       var ele = document.getElementById('print')
       var result = ""
       var inputs = document.forms[the].getElementsByTagName('input')
       for( i = 0; i < inputs.length; ++i ){
          if(inputs[i].type == 'text' || inputs[i].type == 'file'){
             result += inputs[i].name + ": " + inputs[i].value + '<br>'
          }
          
       }
       ele.innerHTML = result
    }
    </script>
  </body>
</html>
