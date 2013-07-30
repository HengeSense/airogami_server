<%@ page language="java" import="java.util.*, com.airogami.presentation.logic.User,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
  <head>
    <title>Data Manager</title>
	
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    
    <!--<link rel="stylesheet" type="text/css" href="./styles.css">-->
    <style type="text/css">
    .container{
    margin:auto;
    width:600px;
    }
    </style>
  </head>
  <body>
    <div class = "container">    
     <p><% 
    User user = (User)request.getSession(true).getAttribute("user");
    if(user != null){
        out.print("Data Manager from " + user.getAccountId());
    }
    else{
        out.print("Please signin");
    }
     %> </p>
     <%
     if(user != null){ %>
    <h2>Upload Data</h2>
    <p id='info'></p>
    <form action="https://airogami-user-bucket.s3-us-west-2.amazonaws.com"  method="post" enctype="multipart/form-data"> 
    <label>AccessKeyId: </label>
    <input type="text" name="AWSAccessKeyId" value="AKIAIZ2JO6JPGKMSGQZA" readonly="readonly" />
        <input type="checkbox" checked="checked" onchange="hide(this)"><br>  
    <label>Policy: </label>
    <input type="text" name="policy" value="" />
        <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Signature: </label>
    <input type="text" name="signature"  value=""/>
        <input type="checkbox" checked="checked" onchange="hide(this)"><br>
    <input type="file" name="file"  value=""/>
        <input type="checkbox" checked="checked" onchange="hide(this)"><br>
    <br>
    <button type="button" onclick="getToken(0)">Get Token</button>
    <button type="button" onclick="print(0)">print</button>
    <input type="reset" name="reset" value="reset" />
    <input type="submit" name="submit" value="submit" />
    <input type="checkbox" checked="checked" onclick="hideAll(this, 0)">
    </form>    

    <p id="print"></p>
    <%} %>
    </div>
    <script type = "text/javascript" src="jses/datamanager.js"></script>
    <script type = "text/javascript">
    function hideAll(the, num){
      var inputs = document.forms[num].getElementsByTagName('input')
      for(var i = 0; i < inputs.length; ++i ){
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
       var ele = document.getElementById('print');
       var result = "";
       var inputs = document.forms[the].getElementsByTagName('input');
       for( var i = 0; i < inputs.length; ++i ){
          if(inputs[i].type == 'text' || inputs[i].type == 'file' || inputs[i].type == 'hidden'){
             result += inputs[i].name + ": " + inputs[i].value + '<br>'
          }
          
       }
       ele.innerHTML = result;
    }
    </script>
  </body>
</html>
