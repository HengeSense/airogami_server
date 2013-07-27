<%@ page language="java" import="java.util.*, com.airogami.presentation.logic.User, java.text.SimpleDateFormat" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
  <head>
    <title>Send Plane</title>
	
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
        out.print("Plane for " + user.getAccountId());
    }
    else{
        out.print("Please signin");
    }
     %> </p>
     <%
     if(user != null){ %>
    <h2>Send Plane</h2>
    <form action="plane/sendPlane.action"  method="get"> 
    <label>CategoryId: </label>
    <input type="text" name="categoryVO.categoryId" value="1" />
        <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Message Content: </label>
    <input type="text" name="messageVO.content"  value="Hello, there!"/>
        <input type="checkbox" checked="checked" onchange="hide(this)"><br>
    <label>Message Type: </label>
    <input type="text" name="messageVO.type"  value="1"/>
        <input type="checkbox" checked="checked" onchange="hide(this)"><br>
    <label>Longitude: </label>
    <input type="text" name="longitude"  value="1.0"/>
        <input type="checkbox" checked="checked" onchange="hide(this)"><br>
    <label>Latitude: </label>
    <input type="text" name="latitude"  value="1.0"/>
        <input type="checkbox" checked="checked" onchange="hide(this)"><br>
    <label>City: </label>
    <input type="text" name="city"  value="Los Angeles"/>
        <input type="checkbox" checked="checked" onchange="hide(this)"><br>
    <label>Province: </label>
    <input type="text" name="province"  value="California"/>
        <input type="checkbox" checked="checked" onchange="hide(this)"><br>
    <label>Country: </label>
    <input type="text" name="country"  value="USA"/>
        <input type="checkbox" checked="checked" onchange="hide(this)"><br>
    <label>Sex: </label>
    <input type="text" name="sex"  value="0"/>
        <input type="checkbox" checked="checked" onchange="hide(this)">
    <br><br>
    <button type="button" onclick="print(0)">print</button>
    <input type="reset" name="submit" value="reset" />
    <input type="submit" name="submit" value="submit" />
    <input type="checkbox" checked="checked" onclick="hideAll(this, 0)">
    </form> 
    <h2>Reply Plane</h2>
    <form action="plane/replyPlane.action"  method="get"> 
    <label>PlaneId: </label>
    <input type="text" name="planeId" value="1" />
        <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Message Content: </label>
    <input type="text" name="messageVO.content"  value="Reply, there!"/>
        <input type="checkbox" checked="checked" onchange="hide(this)"><br>
    <label>Message Type: </label>
    <input type="text" name="messageVO.type"  value="1"/>
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
