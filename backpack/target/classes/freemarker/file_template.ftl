
<#if userFile>
<h4>My Files</h4>
<#elseif publicFile>
<h4>Search Results</h4>
<#else>
<h4>Files Shared with Me</h4>
</#if>
<table class="table table-hover">
    <tr>
    <th>File Name</th>
    <th>Access Type</th>
    <#if userFile>
    <th>Shared with</th>
    <#else>
    <th>Owner</th>
    </#if>
    </tr>                
    <#list files as file>
        <tr id="${file.fileID}t">
        	<td><a href="/backpack/v1/users/${userID}/<#if publicFile>public</#if>files<#if !userFile && !publicFile>Shared</#if>/${file.fileID}">${file.name}</a></td>
			<td>${file.accessType}</td>
			<#if userFile>
			<td><#list file.sharedWithNames as name> ${name!""}</#list></td>
			<td><div class="btn-group btn-group-sm">
			<button id="${file.fileID}" type="button" class="btn btn-danger deleteMe">Delete</button>
			  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
			    AccessType <span class="caret"></span>
			  </button>
			  <ul class="dropdown-menu" role="menu">
			    <li><div id="${file.fileID}u" class="btn accessType">Public</div></li>
			    <li><div id="${file.fileID}r" class="btn accessType">Private</div></li>
			  </ul>
			  <button id="${file.fileID}s" type="button" class="btn btn-success shareMeWith">Share</button>
			</div></td>
			<td><form method="post">with:<input type="text" name="sharedWith" id="${file.fileID}i" placeholder=" email@domain.com"/></form></td>   
			<#else>
			<td>${file.ownerName}</td>
			</#if>
			</tr>
    </#list>
                   
</table>

<#if userFile>


<script>

$(".deleteMe").click(function() {
	var myFileID = this.id;
	var r=confirm("Are you sure you want to delete this file?");
	if (r==true)
	  {
		$.ajax({
	        url: "/backpack/v1/users/${userID}/files/"+myFileID,
	        type: 'DELETE',
	        success: function() {
	            location.reload();
		  	},
            error:function(jqXHR,error, errorThrown){
			var msg ;
			if (errorThrown=="Gone"){msg = "File doesn't exist!";}
			else if (errorThrown=="Unauthorized"){msg = "You don't have the permission to Delete the file!";}
			else {msg="Something doesn't add up!";}
			$("#errormsg").empty().append(msg);
//			alert("file already exist");
			}
	  	});
    }
    else{}
});
$(".shareMeWith").click(function() {
	var s = this.id;
	var myFileID = s.replace("s","");
//	alert(myFileID);
//	alert(myFileID+"i");
	var email = $("#"+myFileID+"i").val();
//	alert(email);
	var URL = "/backpack/v1/users/${userID}/files/"+myFileID+"?sharedWith="+email;
	$.ajax({
        url: URL,
        type: 'PUT',    
        contentType: 'application/json',
        success: function(result) {
//            alert("success?");
            location.reload();
        },
            error:function(jqXHR,error, errorThrown){
			var msg ;
			if (errorThrown=="Not Acceptable"){msg = "The email address you entered is not valid.";}
			else if (errorThrown=="Unauthorized"){msg = "You don't have the permission to share the file!";}
			else {msg="Something doesn't add up!";}
			$("#errormsg").empty().append(msg);
//			alert("file already exist");
			}    
    });
});

    $('.accessType').click(function() {
    var s = this.id;
    var expression = /(r|u)/g;
	var myFileID = s.replace(expression,"");
    var accessType = document.getElementById(s).innerHTML;
    var URL = "/backpack/v1/users/${userID}/files/"+myFileID+"?accessType="+accessType;
	$.ajax({
        url: URL,
        type: 'PUT',    
        contentType: 'application/json',
        success: function(result) {
//            alert("success?");
            location.reload();
        }
    });     
    });

</script>
</#if>