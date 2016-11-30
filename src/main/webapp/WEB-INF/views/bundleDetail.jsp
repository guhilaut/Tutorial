<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/main.css">
<title>Bundle Details</title>
</head>
<body>
	<%-- <h1>************Bundle Details**************</h1>
	<br />
	<table class="CSSTableGenerator">
		<tr>
			<th>Bundle Id</th>
			<th>State</th>
			<th>Registered Services</th>
			<th>Imported Packages</th>
			<th>ExportedPackages</th>
			<th>Required Bundles</th>
			<th>ServicesInUse</th>
			<th>LastModified</th>
		</tr>
		<tr>
			<td>
				<c:out value="${bundleDetails.identifier}"/>
			</td>
			<td>
				<c:out value="${bundleDetails.state}"/>
			</td>
			<td>
				<c:forEach var="bundleId" items="${bundleDetails.registeredServices}">
					<c:out value="${bundleMap[bundleId]}" />,&nbsp;&nbsp;
				</c:forEach>
			</td>
			<td>
				<c:forEach var="pName" items="${bundleDetails.importedPackages}">
					<c:out value="${pName}" />,&nbsp;&nbsp;
				</c:forEach>
			</td>
			<td>
				<c:forEach var="pName" items="${bundleDetails.exportedPackages}">
					<c:out value="${pName}" />,&nbsp;&nbsp;
				</c:forEach>
			</td>
			<td>
				<c:forEach var="bundleId" items="${bundleDetails.requiredBundles}">
					<c:out value="${bundleMap[bundleId]}" />,&nbsp;&nbsp;
				</c:forEach>
			</td>
			<td>
				<c:forEach var="bundleId" items="${bundleDetails.servicesInUse}">
					<c:out value="${bundleMap[bundleId]}" />,&nbsp;&nbsp;
				</c:forEach>
			</td>
			<td>
				<fmt:formatDate type="date" value="${bundleDetails.lastModified}" />
			</td>
		</tr>
	</table>
	
	<table class="CSSTableGenerator">
		<tr>
			<th>Header Name</th>
			<th>Header Values</th>
		</tr>
		<c:forEach var="bundleHeader" items="${bundleDetails.bundleHeader}">
			<tr>
				<td><c:out value="${bundleHeader.key}" /></td>
				<td>
					<c:out value="${bundleHeader.value}"/> <c:forEach
						var="entry" items="${bundleHeader.value}">
						<c:out value="${entry.value}" />,
						</c:forEach>
				</td>
			</tr>
		</c:forEach>
	</table> --%>
	
	
	
	<div class="table-responsive margin10px ">
        <table class="table table-striped table-bordered" id="serverLoginTable">
          <thead class="tHeadBg">
            <tr>
              <th>Bundle Id</th>
              <th>State</th>
              <th>Registered Services</th>
              <th>Imported Packages</th>
              <th>ExportedPackages</th>
              <th>Required Bundles</th>
              <th>ServicesInUse</th>
              <th>LastModified</th>
            </tr>
          </thead>
            <tr>
              <td>230</td>
              <td>org.jruby.jruby</td>
              <td>ACTIVE</td>
              <td></td>
              <td>Not Available</td>
              <td>see dependents</td>
              <td>Not Available</td>
              <td>see dependents</td>
            </tr>
          </thead>
          <tbody>

          </tbody>
            </table>
</div>


      <div class="table-responsive margin10px">
        <table class="table table-striped table-bordered" id="serverLoginTable">
          <thead class="tHeadBg">
            <tr>
              <th>Header Name</th>
              <th>Header Values</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>Service-Component</td>
              <td>OSGI-INF</td>
            </tr>
            <tr>
              <td>Service-Component</td>
              <td>OSGI-INF</td>
            </tr>
            <tr>
              <td>Service-Component</td>
              <td>OSGI-INF</td>
            </tr>


          </tbody>
        </table>
      </div>
	
</body>
</html>