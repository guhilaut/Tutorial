<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>FAME (Fuse Application Monitoring Engine)</title>
<link href="${pageContext.request.contextPath}/resources/Style/bootstrap.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/resources/Style/font-awesome.min.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/resources/Style/style.css" rel="stylesheet" />

<script type="text/javascript">
	function loadServers(env){
		$("#env").val(env);
		$("#login").modal('show');
		//window.location = "${pageContext.request.contextPath}/auth/loadServers?env="+env;
	}
	function closeSelf(){
	    var username = $("#username").val();
		var password = $("#password").val();
		if(username == undefined || username == null || username == ""){
			$('#dis').slideDown().html("<span><font color='red'>Please enter a valid username</font></span>");
			return false;
		}
		if(password == undefined || password == null || password == ""){
			$('#dis').slideDown().html("<span><font color='red'>Please enter a valid password</font></span>");
			return false;
		}else{	
	       document.forms['loginForm'].submit();
	       window.close();
		}
	}
</script>

</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          
          <!--<a class="navbar-brand" href="#">Project name</a>-->
        </div>
         <div id="navbar" align="center" class="navbar-collapse collapse">
          <ul class="nav navbar-nav row">
          	<li class="col-lg-4 noleftPadding"></li>
            <li class="col-lg-4"><h4><font color="white">FAME (Fuse Application Monitoring Engine)</font></h4></li>
            <li class="col-lg-4 text-right noRightPadding"></li>
          </ul>

        </div>
        <!-- <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li><a href="index.html">LogOut</a></li>
          </ul>

        </div> -->
      </div>
    </nav>
    <div class="container">
      <div class="row main">
        <h1 class="text-center">Please Select Environment</h1>
        <h3 class="text-center"><font color="red">${msg}</font></h3>
      </div>
      <div class="row">
      <c:forEach var="env" items="${envList}">
        <div class="col-lg-3 col-md-4 col-sm-6 col-xs-12">
          <a href="#" onclick="loadServers('${env}')">
          <div class="box text-center">
            <i class="fa fa-server wColorF iconFont26px"></i>
            <c:out value="${env}" />
          </div>
        </a>
        </div>
        </c:forEach>
       </div>
    </div>
    <!-- Modal -->
<div class="modal fade serverLoginModal text-center" id="login" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
    <form name="loginForm" action="loadServers" method="post">
      <div class="modal-header serverLoginHeader">
        <!--<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>-->
        <h4 class="modal-title" id="myModalLabel">Login</h4>
      </div>
      <label id="dis"></label><br>
      <div class="modal-body">
		    <div class="col-lg-8 col-lg-offset-2">
		      <input type="text" id="username" name="username" class="form-control marginTop15px " placeholder="Username" />
		      <input type="password" id="password" name="password" class="form-control marginTop15px " placeholder="Password" />
		      <input type="hidden" id="env" name="env" value="" />
		    </div>
      </div>
<div class="col-lg-8 col-lg-offset-2">
<button type="button" class="btn btn-primary btn-sm marginTop15px paddingLR40px" id="showTableData" onclick="closeSelf();">Login</button>

      <div class="col-lg-12 marginTop15px marginBottom15px">  <label class="pull-left">
      <input type="checkbox"> Remember me
    </label>   <a href="#" class="pull-right"> Forgot password ? </a>

  </div>
</div>
<div class="clearfix"></div>

</form>
    </div>
    
  </div>
</div>
    <footer class="footer">
        <div class="container">
          <p class="text-muted">&copy Xavient Information System</p>
        </div>
    </footer>
<script src="${pageContext.request.contextPath}/resources/Script/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/Script/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/Script/script.js"></script>
</body>
</html>