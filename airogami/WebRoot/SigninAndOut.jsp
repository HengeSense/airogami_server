<!DOCTYPE html>
<html>
  <head>
    <title>SigninAndOut.html</title>
	
    <meta name="keywords" content="keyword1,keyword2,keyword3">
    <meta name="description" content="this is my page">
    <meta name="content-type" content="text/html; charset=utf-8">
    
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
    <h3>Signin </h3>
    <form action="account/emailSignin.action"  method="GET">
    <label>Email: </label>
    <input type="text" name="email" value="airogami@gmail.com" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Password: </label>
    <input type="text" name="password" value="12345678" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Automatic: </label>
    <input type="text" name="automatic" value="false" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Update Device: </label>
    <input type="text" name="updateDev" value="false" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Signin Count: </label>
    <input type="text" name="signinCount" value="0" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Device Type: </label>
    <input type="text" name="clientAgent.devType" value="1" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Device Version: </label>
    <input type="text" name="clientAgent.devVersion" value="1" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Device Token: </label>
    <input type="text" name="clientAgent.devToken" value="2ed202ac08ea9033665d053a3dc8bc4c5e98f7c6cf8d55910df290567037dcc4" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <br> 
    <input type="submit" name="submit" value="submit" />
    </form>
    <br>
    <form action="account/screenNameSignin.action"  method="GET">
    <label>Screen Name: </label>
    <input type="text" name="screenName" value="tianhuyang" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Password: </label>
    <input type="text" name="password" value="12345678" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Automatic: </label>
    <input type="text" name="automatic" value="false" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Update Device: </label>
    <input type="text" name="updateDev" value="false" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Signin Count: </label>
    <input type="text" name="signinCount" value="0" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
   <label>Device Type: </label>
    <input type="text" name="clientAgent.devType" value="1" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Device Version: </label>
    <input type="text" name="clientAgent.devVersion" value="1" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Device Token: </label>
    <input type="text" name="clientAgent.devToken" value="2ed202ac08ea9033665d053a3dc8bc4c5e98f7c6cf8d55910df290567037dcc4" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <br> 
    <input type="submit" name="submit" value="submit" />
    </form>
    <h3>Sign out</h3>
    <form action="account/signout.action"  method="GET">
    <label>AccountId: </label>
    <input type="text" name="accountId" value="1" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br>
    <label>Signin Count: </label>
    <input type="text" name="signinCount" value="0" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Device Type: </label>
    <input type="text" name="clientAgent.devType" value="1" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Device Version: </label>
    <input type="text" name="clientAgent.devVersion" value="1" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br> 
    <label>Device Token: </label>
    <input type="text" name="clientAgent.devToken" value="2ed202ac08ea9033665d053a3dc8bc4c5e98f7c6cf8d55910df290567037dcc4" />
    <input type="checkbox" checked="checked" onchange="hide(this)"><br>
    <br> 
    <input type="submit" name="submit" value="submit" />
    </form>
    </div>
    <script type = "text/javascript">
    function hide(the){
       with(the.previousSibling.previousSibling){
            var attr
            if(name.substring(name.length - 1, 1) == "1" )
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
    </script>
  </body>
</html>
