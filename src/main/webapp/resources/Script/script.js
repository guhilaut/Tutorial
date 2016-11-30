function login(){
	$("#login").modal('show');
}

function checkVersions(location){
	$("body").css("cursor", "progress");
	$.ajax({
		url: location,
		type: "GET",
		dataType: "json",
		success: function(data, textStatus, jqXHR){
			drawVersionTable(data);
		}
	})
}

function drawVersionTable(data){
	$("body").css("cursor", "default");
	str= "";
	for (key in data) {
		str+="<tr>"
			str+="<td>"+data[key].serverName+"</td>";
			str+="<td>"+data[key].version+"</td>";
		str+="</tr>"
	}
	$("#bundleVersions").html(str);
	$('#versionCheckDef').modal('show');
	
}

function getRouteDefinitions(location) {
//	$("body").css("cursor", "progress");
	location = location+".";
//	console.log(location);
	$.ajax({
	    url: location,
	    type: "GET",
	    dataType: "json",
	    success: function(data, textStatus, jqXHR) {
	    	$("body").css("cursor", "default");
	    	drawRouteDef(data);
	    }
	});
}


function dataSourceDetails(location) {
	$("body").css("cursor", "progress");
	$.ajax({
	    url: location,
	    type: "GET",
	    dataType: "json",
	    success: function(data, textStatus, jqXHR) {
	    	$("body").css("cursor", "default");
	    	drawDSproperty(data);
	        $('#dataSource').modal('show');
	    }
	});
	
}
function drawDSproperty(data) {
	var str= "";
	
	str+="<tr><td>MaxTotal</td><td>"+data.MaxTotal+"</td></tr>";
	str+="<tr><td>NumActive</td><td>"+data.NumActive+"</td></tr>";
	str+="<tr><td>NumIdle</td><td>"+data.NumIdle+"</td></tr>";
	str+="<tr><td>MaxIdle</td><td>"+data.MaxIdle+"</td></tr>";
	str+="<tr><td>MinIdle</td><td>"+data.MinIdle+"</td></tr>";
	$("#sourcetable").html(str);
	
}

function drawRouteDef(data) {
	$("body").css("cursor", "default");
	var str= "";
	var iserror = false;
	if(data.length < 1){
		iserror = true;
    	str = "<div class='alert alert-danger'>Route Not Found</div>";;
	}
	for (key in data) {
		var operationWrapper = "";
		str+="<tr><td>"+data[key].routeId+"</td><td>"+data[key].endpoint+"</td>";
		for(operationId in data[key].operations){
			operationWrapper += data[key].operations[operationId];
		}
		str+="<td>"+operationWrapper+"</td></tr>";
	}
	if(iserror){
		$('#ConfirmMessage').html(str);
    	$('#Confirm').modal('show');
	}else{
		$("#routetable").html(str);
		$('#routeDef').modal('show');
	}
	
}

function openDepenDS(location){
	$("body").css("cursor", "progress");
	$.ajax({
	    url: location,
	    type: "GET",
	    dataType: "json",
	    success: function(data, textStatus, jqXHR) {
	    	drawDSList(data);
	    }
	});
}

function drawDSList(data){
	var str= "";
	var iserror = false;
	if(data.length < 1){
		iserror = true;
    	str = "<div class='alert alert-danger'>Dependent Not Found</div>";
	}
	for(key in data){
		str+="<li>"+data[key]+"</li>";
	}
	if(iserror){
		$("body").css("cursor", "default");
		$('#ConfirmMessage').html(str);
    	$('#Confirm').modal('show');
	}else{
		$("body").css("cursor", "default");
		$("#dependencyList").html(str);
		$('#dependency').modal('show');
	}
}
	
function openDepen(location, id, name, flag) {
	if(flag){
		$("body").css("cursor", "progress");
	}
	$.ajax({
	    url: location+"/"+name+"/"+id,
	    type: "GET",
	    dataType: "json",
	    success: function(data, textStatus, jqXHR) {
	    	drawList(data, location, id, flag);
	    	/*if(flag){
	    		$('#dependency').modal('show');
	    	}*/
	    }
	});
	
}

function drawList(data, location, id, flag){
	var str= "";
	//location = location.replace(/'/g, '&#39;')
	var iserror = false;
	if(data.length < 1){
		iserror = true;
    	str = "<div class='alert alert-danger'>Dependent Not Found</div>";
	}
	$("body").css("cursor", "default");
	for (key in data) {
		str+="<li id=list_"+data[key].bundleId+"><a href='#' onclick=\"openDepen('"+location+"','"+data[key].bundleId+"','"+data[key].bundleName+"',"+false+")\" class='btn-btn-sm'><i class='fa fa-plus-square-o'></i></a>&nbsp;"+data[key].bundleName;
		str+="<ul class=\"list-group\" id=\"dependencyChild_"+data[key].bundleId+"\" style='display: none;'></ul></li>";
		console.log(str);
	}
	console.log(str);
	if(flag && !iserror){
		$("#dependencyList").html(str);
		$('#dependency').modal('show');
	}else if(flag && iserror){
		$('#ConfirmMessage').html(str);
    	$('#Confirm').modal('show');
	}
	else{
		var myClass = $("#list_"+id+" i").attr("class");
		if(myClass == 'fa fa-plus-square-o'){
			$("#list_"+id+" i").removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
			$("#dependencyChild_"+id).show();
			$("#dependencyChild_"+id).html(str);
		}else{
			$("#dependencyChild_"+id).each(function() {
			   $(this).hide();
			});
			$("#dependencyChild_"+id).hide();
			$("#list_"+id+" i").removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
		}
	}
}

function openPpUp(location) {
	$('#dependentBundle').modal('show');
	
	
	$.ajax({
	    url: location,
	    type: "GET",
	    dataType: "json",
	    success: function(data, textStatus, jqXHR) {
	        drawTable(data);
	    }
	});
	$('#serverLoginTable')
}

function testCheck(location) {
	$("body").css("cursor", "default");
	$.ajax({
	    url: location,
	    type: "GET",
	    dataType: "text",
	    success: function(data, textStatus, jqXHR) {
	    	$("body").css("cursor", "default");
	    	var str = "<div class='alert alert-danger'>Connection not found</div>";;
	    	if(data ==0)
	    		str= "<div class='alert alert-success'>Connection Successful</div>";
	    	
	    	$('#ConfirmMessage').html(str);
	    	$('#Confirm').modal('show');
	    }
	
	});
	
}

/*function showConfig(location) {
	$("body").css("cursor", "default");
	$.ajax({
	    url: location,
	    type: "GET",
	    dataType: "text",
	    success: function(data, textStatus, jqXHR) {
	    	$("body").css("cursor", "default");
	    	console.log(data);
	    	$('#configData').html(data);
	    }
	});
	$('#configModal').modal('show');
	
}*/

function showConfig(location) {
	$("body").css("cursor", "progress");
	$.ajax({
	    url: location,
	    type: "GET",
	    dataType: "json",
	    success: function(data, textStatus, jqXHR) {
	    	$("body").css("cursor", "default");
	    	drawConfig(data);
	    }
	});
	
}

function drawConfig(data){
	str = "";
	for(key in data){
		str+="<tr><td>"+data[key].name+"</td><td>"+data[key].value+"</td></tr>";
	}
	$('#configData').html(str);
	if(str.length > 1){
		$('#configModal').modal('show');
	}else{
		str = "<div class='alert alert-danger'>Configuration not available!</div>";
		$('#ConfirmMessage').html(str);
    	$('#Confirm').modal('show');
	}
}

/*$.ajax({
    url: '/echo/json/',
    type: "post",
    dataType: "json",
    data: {
        json: JSON.stringify([
            {
            id: 1,
            firstName: "Peter",
            lastName: "Jhons"},
        {
            id: 2,
            firstName: "David",
            lastName: "Bowie"}
        ]),
        delay: 3
    },
    success: function(data, textStatus, jqXHR) {
    	
        drawTable(data);
    }
});*/

function drawTable(data) {
	$("#Bundle").html(data.bundleId);
	var str= "";
	for (key in data.exportedPackages) {
		str+="<tr>"
			str+="<td>"+data.exportedPackages[key]+"</td>";
		str+="</tr>"
	}
	$("#ExportedPackages").html(str);
	
	str= "";
	for (key in data.importedPackages) {
		str+="<tr>"
			str+="<td>"+data.importedPackages[key]+"</td>";
		str+="</tr>"
	}
	$("#Imported").html(str);
	str= "";
	if(data.registeredServices.length >1){
		for (key in data.registeredServices) {
			str+="<tr>"
				str+="<td>"+data.registeredServices[key]+"</td>";
			str+="</tr>"
		}
		$("#Registered").html(str);
	}
	$("#State").html(data.state);

	str= "";
	for (key in data.bundleHeader) {
		str+="<tr>"
			str+="<td>"+data.bundleHeader[key].Key+"</td>";
			str+="<td>"+data.bundleHeader[key].Value+"</td>";
		str+="</tr>"
	}
	$("#headertable tbody").html(str);
	str= "";
	if(data.requiredBundles.length >1){
		for (key in data.requiredBundles) {
			str+="<tr>"
				str+="<td>"+data.requiredBundles[key]+"</td>";
			str+="</tr>"
		}
	}
	$("#Required").html(str);
	
	str= "";
	if(data.servicesInUse.length >1){
		for (key in data.servicesInUse) {
			str+="<tr>"
				str+="<td>"+data.servicesInUse[key]+"</td>";
			str+="</tr>"
		}
	}
	$("#ServicesInUse").html(str);
	$("#LastModified").html(data.lastModified);
}

function drawRow(rowData) {
	console.log(rowData);
    var row = $("<tr />")
    $("#serverLoginTable").append(row); //this will append tr element to table... keep its reference for a while since we will add cels into it
    row.append($("<td>" + rowData.id + "</td>"));
    row.append($("<td>" + rowData.firstName + "</td>"));
    row.append($("<td>" + rowData.lastName + "</td>"));
}