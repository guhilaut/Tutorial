<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Authentication</title>
<style>
.error {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #a94442;
	background-color: #f2dede;
	border-color: #ebccd1;
}

.msg {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #31708f;
	background-color: #d9edf7;
	border-color: #bce8f1;
}

</style>
</head>
<body>
<h2>Login with Username and Password</h2>
<form:form action="j_spring_security_check" method="POST">
<div class="col-lg-8 col-lg-offset-2">
   <form:input type="text" path="j_username" class="form-control marginTop15px " placeholder="Username" />
   <form:input type="password" path="j_password" class="form-control marginTop15px " placeholder="Password" />
</div>
<div class="col-lg-8 col-lg-offset-2">
<form:button type="submit" class="btn btn-primary btn-sm marginTop15px paddingLR40px" data-dismiss="modal" id="showTableData">Login</form:button>
</div>
</form:form>
</body>
</html>