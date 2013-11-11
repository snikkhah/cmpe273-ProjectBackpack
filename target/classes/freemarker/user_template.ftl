
<html>
        <head>
            <title>DropBox</title>
                <style type="text/css">
			      .label {text-align: left}
			      .error {color: red}
			    </style>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <!-- Latest compiled and minified CSS -->
            <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" media="screen">
            <!-- Optional theme -->
            <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap-theme.min.css">
        </head>
        <body>
            <div class="container">
                <div class="jumbotron">
                    <p class="navbar-text">Signed in as ${user.firstName} ${user.lastName}</p>
                    <hr>
                    <hr>
					<div id="userFiles" class="container">
					</div>
					<div id="filesShared" class="container"> 
					</div>
				    <div id="showSharedFiles" class="btn btn-primary">Files Shared with Me</div>
				    <div id="uploadFile" class="btn btn-primary">Upload</div>
				    <div id="deleteUser" class="btn btn-danger">Delete User</div>
                    <!-- calls getBooks() from HomeResource -->
                </div>
            </div> <!-- end of container -->
           
            <!-- script tags -->
            <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
            <script src="//code.jquery.com/jquery.js"></script>
            <!-- Latest compiled and minified JavaScript -->
            <script src="//netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
            <!-- application ui scripts -->
			
			<script>
			$(function() {
 				$.ajax({
			        url: "/dropbox/v1/users/${user.userID}/files",
			        type: 'GET',
			        success: function(data) {
			            $("#userFiles").empty().append(data);
					  	}
					  	});
				});	  	
					  	
				$(function() {
				    $('#showSharedFiles').click(function() {
				    $.ajax({
			        url: "/dropbox/v1/users/${user.userID}/filesShared",
			        type: 'GET',
			        success: function(data) {
			            $("#filesShared").empty().append(data);
			  	}
			  	});

				    });
				    
				    $('#deleteUser').click(function() {
				    var r=confirm("Are you sure you want to delete your account with all it's files?");
					if (r==true)
					  {
						  $.ajax({
					        url: "/dropbox/v1/users/${user.userID}",
					        type: 'DELETE',
					        success: function(data) {
					            alert("Your account was successfully deleted!");
						  	}
					  	});
					  }
					else{}
				    });
				});
				$('#uploadFile').click(function() {
				 window.location = "/dropbox/v1/users/${user.userID}/files/createFile";
				    });
			</script>
        </body>
</html>
