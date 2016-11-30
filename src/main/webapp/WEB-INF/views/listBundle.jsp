<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>FAME (Fuse Application Monitoring Engine)</title>
<link href="${pageContext.request.contextPath}/resources/Style/bootstrap.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/resources/Style/font-awesome.min.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/resources/Style/style.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/resources/Style/searchableOptionList.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/resources/Style/multiselect.css" rel="stylesheet" />

<script type="text/javascript">
function loadUrl(path){
	window.location = path;
}
function listBundles(url) {
	window.location = "${pageContext.request.contextPath}/getServerInfo?url="+ url;
}
function loadBundles(){
	document.getElementById("filterForm").submit();
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

        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav row">
            <li class="col-lg-4 noleftPadding"><i class="fa fa-user wColorF"></i>
            <c:choose>
            <c:when test="${userName ne null}"><c:out value="${userName}" /></c:when>
            <c:otherwise>Guest</c:otherwise>
            </c:choose>
            </li>
            <li class="col-lg-4"><h4><font color="white">FAME (Fuse Application Monitoring Engine)</font></h4></li>
            <li class="col-lg-4 text-right noRightPadding">
            <a href="javascript:void(0)" onclick="loadUrl('${pageContext.request.contextPath}')">LogOut</a>
            </li>
          </ul>

        </div>
      </div>
    </nav>
    <div class="container-fluid">
      <div class="row">


        <div class="col-sm-3 col-md-2 sidebar">
          <ul class="nav nav-sidebar">
            <li class="heading"><a href="javascript:void(0)" onclick="loadUrl('${pageContext.request.contextPath}')" class="main-list-heading"> <i class="fa fa-chevron-left"></i>${env}</a></li>
            <c:forEach var="server" items="${serverList}" varStatus="status">
            	<li><a class="serverlist" href="#" data-toggle="modal" id="serverlist_${status.index}" onclick="listBundles('${server}')" data-target="#myModal"><c:out value="${server}" /></a></li>
            	<%-- <li><a href="#" onclick="login()"><c:out value="${server}" /></a></li> --%>
            </c:forEach>
          </ul>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 mainpage">
          <div class="row">
<div class="col-lg-12">
            <div class="table-responsive">
              <table class="table table-striped table-bordered" id="dSTable">
                <thead class="tHeadBg">
                  <tr>
                    <th>DataSources Names</th>
                    <th>Dependency</th>
                    <th>Test Check</th>
                 </tr>
                </thead>
                <tbody>
                  <c:forEach var="name" items="${dataSouceNames}">
                  	<tr>
                  		<td><a href="javascript:void(0)" onclick="dataSourceDetails('${pageContext.request.contextPath}/bundle/getDbInfo/${name}')" ><c:out value="${name}" /></a></td>
                  		<td><a href="javascript:void(0)" onclick="openDepenDS('${pageContext.request.contextPath}/bundle/getDSDependent/${name}')">see dependents</a></td>
                  		<td><a href="javascript:void(0)" onclick="testCheck('${pageContext.request.contextPath}/bundle/testConnection/${name}')">Test Connection</a></td>
                  	</tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
            
            <div class="table-responsive">
              <table class="table table-striped table-bordered" id="dSTable">
                <thead class="tHeadBg">
                  <tr>
                    <th>Test AS400 Connection</th>
                    <th>Test ICOMS Connection</th>
                 </tr>
                </thead>
                <tbody>
                  <tr>
                  	<td><a href="javascript:void(0)" onclick="testCheck('${pageContext.request.contextPath}/bundle/testAS400Connection')">Test Connection</a></td>
                  	<td><a href="javascript:void(0)" onclick="testCheck('${pageContext.request.contextPath}/bundle/testICOMConnection')">Test Connection</a></td>
                  </tr>
                </tbody>
              </table>
            </div>

            <form class="form-horizontal" action="${pageContext.request.contextPath}/bundle/list" method="get" id="filterForm">
               <label for="exampleInputName2" class="col-lg-1 noleftPadding noRightPadding marginTop5px">Filter</label>
               <div class="col-lg-2 noleftPadding ">
                <select id="my-select" name="filter" onchange="loadBundles()">
                   <option value="com.cox.bis" <c:if test="${cox}">selected="selected"</c:if>>Cox Bundles</option>
                   <option value="all" <c:if test="${all}">selected="selected"</c:if>>All Bundles</option>
                </select>
              </div>
            </form>

<div class="clearfix"></div>
        <h2 class="sub-header">Bundle List</h2>
          <div class="table-responsive">
            <table class="table table-striped table-bordered" id="BundleTable">
              <thead class="tHeadBg">
                <tr>
                  <th>Bundle Id</th>
                  <th>Bundle Name</th>
                  <th>Status</th>
                  <th>Blue Print Status</th>
                  <th>Routes</th>
                  <th>Load Configuration</th>
                  <th>Dependent Bundles</th>
                  <th>Check Version</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="entry" items="${bundleMap}">
					<tr>
						<td>${entry.value.id}</td>
						<td><a href="javascript:void(0)" onclick="openPpUp('${pageContext.request.contextPath}/bundle/getBundleInfo/${entry.value.id}')" >${entry.value.name} (${entry.value.version})</a></td>
						<td>${entry.value.state}</td>
						<td>
							<c:choose>
								<c:when test="${entry.value.blueprint eq 'Failure'}"><font color="red"><b>${entry.value.blueprint}</b></font></c:when>
								<c:when test="${entry.value.blueprint eq 'Created'}"><font color="green"><b>${entry.value.blueprint}</b></font></c:when>
								<c:otherwise>${entry.value.blueprint}</c:otherwise>
							</c:choose>
						</td>
						<td><a href="javascript:void(0)" onclick="getRouteDefinitions('${pageContext.request.contextPath}/bundle/getRoute/${entry.value.name}')">Show Routes</a></td>
						<td>
							<a href="javascript:void(0)" onclick="showConfig('${pageContext.request.contextPath}/bundle/loadConfig/${entry.value.name}')"><b>Show Config</b></a>
						</td>
						<td><a  href="javascript:void(0)" onclick="openDepen('${pageContext.request.contextPath}/bundle/getDependent','${entry.value.id}','${entry.value.name}',true)">see dependents</a></td>
						<td><a  href="javascript:void(0)" onclick="checkVersions('${pageContext.request.contextPath}/bundle/checkVersion/${entry.value.name}')">Check Versions</a></td>
						<!-- <td><a href="getDependent/${entry.value.id}" target="">see dependents</a></td>-->
					</tr>
				</c:forEach>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
<footer class="footer">
    <div class="container">
      <p class="text-muted">&copy Xavient Information System</p>
    </div>
</footer>



<div class="modal fade bs-example-modal-lg"  id="dependentBundle" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header serverLoginHeader">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Bundle Details</h4>
      </div>
	<div style="height: 450px; overflow:auto;">
      <div class="table-responsive margin10px">
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
              <td id="Bundle" ></td>
              <td id="State"></td>
              <td id="Registered"></td>
              <td id="Imported"></td>
              <td id="ExportedPackages"></td>
              <td id ="Required"></td>
              <td id="ServicesInUse"></td>
              <td id="LastModified"></td>
            </tr>  
          <tbody>

          </tbody>
            </table>
</div>


      <div class="table-responsive margin10px">
        <table class="table table-striped table-bordered" id="headertable">
          <thead class="tHeadBg">
            <tr>
              <th>Header Name</th>
              <th>Header Values</th>
            </tr>
          </thead>
          <tbody>

          </tbody>
        </table>
      </div>
</div>
    </div>
  </div>
</div>


<div class="modal fade bs-example-modal-lg"  id="configModal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header serverLoginHeader">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Config Details</h4>
      </div>
      <div style="height: 450px; overflow:auto;">
      <div class="col-lg-12">
      	<table class="table table-striped table-bordered" id="headertable">
          <thead class="tHeadBg">
            <tr>
              <th>Property Name</th>
              <th>Property Value</th>
            </tr>
          </thead>
          <tbody id="configData">

          </tbody>
        </table>
	  </div>
	  </div>
<div class="clearfix"></div>
    </div>
  </div>
</div>



<div class="modal fade bs-example-modal-sm" id="Confirm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header serverLoginHeader">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Alert Message</h4>
      </div>
      <div class="col-lg-12 marginTop15px" id="ConfirmMessage">
      
      
     </div>
  <div class="clearfix"></div>
    </div>
  </div>
</div>

<div class="modal fade bs-example-modal-md"  id="dependency" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
  <div class="modal-dialog modal-md">
    <div class="modal-content">
      <div class="modal-header serverLoginHeader">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Dependency</h4>
      </div>
      <div style="max-height:400px; overflow:auto;">
      <div class="container-fluid" style='margin:10px;'>
	        <ul class="list-group" id="dependencyList"></ul>
      </div>
	  </div>
    </div>
  </div>
</div>

<div class="modal fade bs-example-modal-md"  id="dataSource" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
  <div class="modal-dialog modal-md">
    <div class="modal-content">
      <div class="modal-header serverLoginHeader">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Bundle Details</h4>
      </div>
      <div class="table-responsive margin10px">
        <table class="table table-striped table-bordered">
          <thead class="tHeadBg">
            <tr>
              <th>Property Name</th>
              <th>Property Values</th>
            </tr>
          </thead>
          <tbody id="sourcetable">
          </tbody>
        </table>
      </div>

    </div>
  </div>
</div>

<div class="modal fade bs-example-modal-lg"  id="routeDef" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header serverLoginHeader">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Route Definitions</h4>
      </div>
      <div class="table-responsive margin10px">
        <table class="table table-striped table-bordered">
          <thead class="tHeadBg">
            <tr>
              <th>Route Id</th>
              <th>Endpoint Uri</th>
              <th>Operations</th>
            </tr>
          </thead>
          <tbody id="routetable">
          </tbody>
        </table>
      </div>

    </div>
  </div>
</div>

<div class="modal fade bs-example-modal-md"  id="versionCheckDef" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
  <div class="modal-dialog modal-md">
    <div class="modal-content">
      <div class="modal-header serverLoginHeader">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Version Mismatch</h4>
      </div>
      <div style="max-height:400px; overflow:auto;">
      <div class="table-responsive margin10px">
        <table class="table table-striped table-bordered">
          <thead class="tHeadBg">
            <tr>
              <th>Server Ip</th>
              <th>Version</th>
            </tr>
          </thead>
          <tbody id="bundleVersions">
          </tbody>
        </table>
      </div>
	</div>
    </div>
  </div>
</div>

<script src="${pageContext.request.contextPath}/resources/Script/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/Script/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/Script/script.js"></script>
<script src="${pageContext.request.contextPath}/resources/Script/multiselect.js"></script>

<script type="text/javascript">

var time = new Date().getTime();
$(document.body).bind("mousemove keypress", function(e) {
    time = new Date().getTime();
});

function refresh() {
    if(new Date().getTime() - time >= 60000) 
        window.location.reload(true);
    else 
        setTimeout(refresh, 10000);
}
setTimeout(refresh, 10000);

$(function() {

    $('#my-select').searchableOptionList({
        scrollTarget: $('#my-content-area'),
        events: {

            onScroll: function () {

                var posY = this.$input.offset().top - this.config.scrollTarget.scrollTop() + this.$input.outerHeight(),
                    selectionContainerWidth = this.$innerContainer.outerWidth(false) - parseInt(this.$selectionContainer.css('border-left-width'), 10) - parseInt(this.$selectionContainer.css('border-right-width'), 10);

                if (this.$innerContainer.css('display') !== 'block') {

                    selectionContainerWidth = Math.ceil(selectionContainerWidth * 1.2);
                } else {

                    this.$selectionContainer
                        .css('border-top-right-radius', 'initial');

                    if (this.$actionButtons) {
                        this.$actionButtons
                            .css('border-top-right-radius', 'initial');
                    }
                }

                this.$selectionContainer
                    .css('top', Math.floor(posY))
                    .css('left', Math.floor(this.$container.offset().left))
                    .css('width', selectionContainerWidth);
            }
        }
    });

});
</script>
</body>
</html>