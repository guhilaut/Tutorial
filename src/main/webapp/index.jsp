<%@ page import="java.security.Principal"%>
<%@ page import="java.io.FileInputStream,java.io.IOException,java.util.Properties"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mercury</title>
	<%
	Properties PROP = new Properties();
	String filePath = System.getenv("PROP_FILES_PATH");
	Principal principal = request.getUserPrincipal();
	FileInputStream fis;
	String baseurl = null;
	String userName = null;
	try {
	if (principal != null) {
			userName= principal.getName();
	    }
	    fis = new FileInputStream(filePath + "/mercury.properties");
	    PROP.load(fis);
	    baseurl = PROP.getProperty("BASE_URL");
	}catch(Exception e){}
%>
    <link href='https://fonts.googleapis.com/css?family=Roboto:500,400' rel='stylesheet' type='text/css'/>
    <link rel="stylesheet" href="./css/app.css"/>
    <script >
    var appurl='<%=baseurl%>';
	  var userName ='<%=userName%>';

    </script>
    <!--[if lte IE 8]>
    <script src="//cdnjs.cloudflare.com/ajax/libs/json2/20110223/json2.js"></script>
    <script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
    <script src="./js/vendor.js"></script>
    <script src="./js/app.js"></script>
    <script src="https://www.amcharts.com/lib/3/amcharts.js"></script>
    <script src="https://www.amcharts.com/lib/3/gauge.js"></script>
    <script src="https://www.amcharts.com/lib/3/themes/light.js"></script>

</head>
<body ng-app="mercuryApp" ng-controller="rootCtrl">
    <div ui-view=""></div>
    <div class="footer" ng-if="false"><span class="copyright">&copy; 1998-2015 Cox Communication, Inc.</span> <span class="powered">Powered by: Xavient</span>
</div>
</body>
</html>
