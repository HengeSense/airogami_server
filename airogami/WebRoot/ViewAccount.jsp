<%@ page language="java" import="java.util.*, com.airogami.presentation.logic.User,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
  <head>
    <title>View Account</title>
	
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
        out.print("View Account from " + user.getAccountId());
    }
    else{
        out.print("Please signin");
    }
     %> </p>
     <%
     if(user != null){ %>
    <h2>View Account</h2>
    <form action="account/obtainProfile.action"  method="get"> 
    <label>AccountId: </label>
    <input type="text" name="accountId" value="2" />
        <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Update Count: </label>
    <input type="text" name="updateCount"  value="-1"/>
        <input type="checkbox" checked="checked" onchange="hide(this)"><br><br>
    <button type="button" onclick="print(0)">print</button>
    <input type="reset" name="submit" value="reset" />
    <input type="submit" name="submit" value="submit" />
    <input type="checkbox" checked="checked" onclick="hideAll(this, 0)">
    </form>    
    
    <h2>View Hot</h2>
    <form action="account/obtainHot.action"  method="get"> 
    <label>AccountId: </label>
    <input type="text" name="accountId" value="2" />
        <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Update Count: </label>
    <input type="text" name="updateCount"  value="-1"/>
        <input type="checkbox" checked="checked" onchange="hide(this)"><br><br>
    <button type="button" onclick="print(1)">print</button>
    <input type="reset" name="submit" value="reset" />
    <input type="submit" name="submit" value="submit" />
    <input type="checkbox" checked="checked" onclick="hideAll(this, 1)">
    </form>   
    
    <h2>Report Account</h2>
    <form action="account/reportAccount.action"  method="get"> 
    <label>ReportedId: </label>
    <input type="text" name="reportedId" value="2" />
        <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Reason: </label>
    <input type="text" name="reason"  value="Bad words"/>
        <input type="checkbox" checked="checked" onchange="hide(this)"><br><br>
    <button type="button" onclick="print(2)">print</button>
    <input type="reset" name="submit" value="reset" />
    <input type="submit" name="submit" value="submit" />
    <input type="checkbox" checked="checked" onclick="hideAll(this, 2)">
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
