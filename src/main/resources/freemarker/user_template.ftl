
<html>
        <head>
            <title>Backpack</title>

            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <!-- Latest compiled and minified CSS -->
            <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" media="screen">
            <!-- Optional theme -->
            <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap-theme.min.css">
            <style type="text/css">
			      .label {text-align: right; color:#000;}
			      .error {color: red}
		    </style>
        </head>
        <body style="background:#123;">
			<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
			      <div class="container">
			        <div class="navbar-header">
<!--			        <img src="https://cdn1.iconfinder.com/data/icons/SOPHISTIQUE/education_icons/png/400/backpack.png" alt="logo" width="32" height="32">  -->
			          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
			            <span class="sr-only">Toggle navigation</span>
			            <span class="icon-bar"></span>
			            <span class="icon-bar"></span>
			            <span class="icon-bar"></span>
			          </button>
			          <a class="navbar-brand" href="#">Welcome to Backpack</a>
			        </div>
			        <div class="navbar-collapse collapse">
				        <button type="button" id="logout" class="btn btn-default navbar-btn navbar-right">Log out</button>
						<p class="navbar-text navbar-right" style="margin-right: 10px;"><b>Signed in as ${user.firstName} ${user.lastName}</b></p>
						<form method="post" class="navbar-form navbar-left" role="search"><input type="text" name="name" class="form-control" placeholder="Search" value="${name}"></form>
						<button type="submit" id="searchButton" class="btn btn-primary navbar-btn navbar-left">Submit</button>
			        </div><!--/.navbar-collapse -->
			      </div>
			    </div>		
<!--    <div class="navbar-collapse collapse">
		<button type="button" id="logout" class="btn btn-default navbar-btn navbar-right">Log out</button>
		<p class="navbar-text navbar-right"><b>Signed in as ${user.firstName} ${user.lastName}</b></p>
		</div>
-->
            <div class="container">
                <div class="jumbotron">
<!--                    <p class="navbar-text">Signed in as ${user.firstName} ${user.lastName}</p>	-->
                    <hr>
                    <hr>
					<div id="userFiles" class="container">
					</div>
					<h5 id ="errormsg" class="error"></h5>
					<div id="filesShared" class="container"> 
					</div>
					<div id="publicFiles" class="container">
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
			        url: "/backpack/v1/users/${user.userID}/files",
			        type: 'GET',
			        success: function(data) {
			            $("#userFiles").empty().append(data);
					  	}
					  	});
				});	  	
					  	
				$(function() {
				    $('#showSharedFiles').click(function() {
				    $.ajax({
			        url: "/backpack/v1/users/${user.userID}/filesShared",
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
					        url: "/backpack/v1/users/${user.userID}",
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
				 window.location = "/backpack/v1/users/${user.userID}/files/createFile";
				    });
				    
				    $('#logout').click(function() {
				 window.location = "/backpack/v1/users/login";
				    });
				    
				    						$.fn.serializeObject = function()
				{
				    var o = {};
				    var a = this.serializeArray();
				    $.each(a, function() {
				        if (o[this.name] !== undefined) {
				            if (!o[this.name].push) {
				                o[this.name] = [o[this.name]];
				            }
				            o[this.name].push(this.value || '');
				        } else {
				            o[this.name] = this.value || '';
				        }
				    });
				    return o;
				};
				
				
				$(function() {
				    $('#searchButton').click(function() {
//				        alert(JSON.stringify($('form').serializeObject()));
				    $.ajax({
			        url: "/backpack/v1/users/${user.userID}/publicFiles",
			        type: 'POST',    
			        contentType: 'application/json',
			        data:JSON.stringify($('form').serializeObject()), 
			        error:function(){
			        alert("something is wrong!");
			  		},
			        success: function(data) {
			           $("#publicFiles").empty().append(data);
			  	}
			  	});

				    });
				});
			</script>
        </body>
</html>
