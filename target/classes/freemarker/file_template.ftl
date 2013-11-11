
<#if userFile>
<h4>My Files</h4>
<#else>
<h4>Files Shared with Me</h4>
</#if>
<table class="table table-hover">
    <tr>
    <th>File Name</th>
    <th>Access Type</th>
    <#if userFile>
    <th>Shared with User IDs</th>
    </#if>
    </tr>                
    <#list files as file>
        <tr id="${file.fileID}t">
        	<td><a href="/dropbox/v1/users/${userID}/files<#if !userFile>Shared</#if>/${file.fileID}">${file.name}</a></td>
			<td>${file.accessType}</td>
			<#if userFile>
			<td><#list file.sharedWithNames as name> ${name!""}</#list></td>
			<td><button id="${file.fileID}" type="button" class="btn btn-danger deleteMe">Delete</button></td>
			<td><button id="${file.fileID}s" type="button" class="btn btn-success shareMeWith">Share</button></td> 	
			<td><form method="post">Share with:<input type="text" name="sharedWith" id="${file.fileID}i" placeholder=" email@domain.com"/></form></td>
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
	        url: "/dropbox/v1/users/${userID}/files/"+myFileID,
	        type: 'DELETE',
	        success: function() {
	            location.reload();
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
	var URL = "/dropbox/v1/users/${userID}/files/"+myFileID+"?sharedWith="+email;
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