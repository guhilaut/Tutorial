<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/main.css">
<title>Connection Details</title>
</head>
<body>
	<table class="CSSTableGenerator">
		<c:forEach var="entry" items="${connectionAttrMap}">
		<tr>
			<td><c:out value="${entry.key}"/></td>
			<td><c:out value="${entry.value}"/></td>	
		</tr>
		</c:forEach>
	</table>
</body>
</html>