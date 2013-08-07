<%@ page language="java" import="java.util.*, com.airogami.persistence.entities.*, com.airogami.presentation.logic.*, com.airogami.presentation.logic.User, java.text.SimpleDateFormat" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
  <head>
    <title>Edit Account</title>
	
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    
    <!--<link rel="stylesheet" type="text/css" href="./styles.css">-->
    <style type="text/css">
    .container{
    margin:auto;
    width:500px;
    }
    </style>
  </head>
  <body>
    <div class = "container">
    <h2>Edit Account</h2>
     <p><% 
   User user = (User)request.getSession(true).getAttribute("user");
   Profile profile = null;
    if(user != null){
        profile = ManagerUtils.accountManager.obtainProfile(user.getAccountId(), null);
        out.print("Edit account for" + user.getAccountId());
    }
    else{
        out.print("Please signin");
    }
     %> </p>
     <%
     if(user != null){ %>
    <form action="account/editProfile.action"  method="post" enctype="multipart/form-data"> 
    <label>Sex: </label> 
    <input type="text" name="sex" value="<%= profile.getSex() %>" />
        <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Fullname: </label>
    <input type="text" name="fullName" value="<%= profile.getFullName() %>" />
    <input type="checkbox" checked="checked" onchange="hide(this)">
    <!--  <textarea name="fullName" >tianhuyang ä½ å¥½ </textarea>-->
        <br> 
    <label>Birthday: </label>
    <input type="text" name="birthday" value="<%= profile.getBirthday() != null ? new SimpleDateFormat("MM/DD/yyyy").format(profile.getBirthday()) : "" %>" />
        <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Longitude: </label>
    <input type="text" name="longitude" value="<%= profile.getLongitude() %>" />
        <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Latitude: </label>
    <input type="text" name="latitude" value="<%= profile.getLatitude() %>" />
        <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>City: </label>
    <input type="text" name="city" value="<%= profile.getCity() %>" />
        <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Province: </label>
    <input type="text" name="province" value="<%= profile.getProvince() %>" />
        <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Country: </label>
    <input type="text" name="country" value="<%= profile.getCountry() %>" />
        <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Icon: </label>
    <input type="file" name="file" />
        <input type="checkbox" checked="checked" onchange="hide(this)"><br><br>
    <button type="button" onclick="print()">print</button>
    <input type="reset" name="submit" value="reset" />
    <input type="submit" name="submit" value="submit" />
    <input type="checkbox" checked="checked" onclick="hideAll(this)">
    </form>
    <p id="print"></p>
    <%} %>
    </div>
    <script type = "text/javascript">
    function hideAll(the){
      var inputs = document.forms[0].getElementsByTagName('input')
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
    
    function print(){
       var ele = document.getElementById('print')
       var result = ""
       var inputs = document.forms[0].getElementsByTagName('input')
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
