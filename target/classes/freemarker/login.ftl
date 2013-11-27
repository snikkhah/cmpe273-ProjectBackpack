
<html>
        <head>
            <title>Login</title>
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
			          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
			            <span class="sr-only">Toggle navigation</span>
			            <span class="icon-bar"></span>
			            <span class="icon-bar"></span>
			            <span class="icon-bar"></span>
			          </button>
			          <a class="navbar-brand" href="#">Welcome to Backpack</a>
			        </div>
			        <div class="navbar-collapse collapse">
				        <button type="button" id="signup" class="btn btn-default navbar-btn navbar-right">Sign up</button>
						<p class="navbar-text navbar-right" style="margin-right: 10px;"><b>New user?</b></p>
			        </div><!--/.navbar-collapse -->
			      </div>
			    </div>
            <div class="container">
                <div class="jumbotron">
				    <h2>Login</h2>
				    <form method="post">
				      <table class="table table-hover">
				        <tr>
				          <td class="label">Email</td>
				          <td><input type="text" name="email" value="${email}"></td>
				        </tr>
				        
				        <tr>
				          <td class="label">Password</td>
				          <td><input type="password" name="password" value=""></td>
				        </tr>
					</table>
<!--				      <input type="submit">  -->
				    </form>
				    <h5 id ="errormsg" class="error"></h5>
				    <div id="loginButton" class="btn btn-primary">Login</div>
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
				    $('#loginButton').click(function() {
//				        alert(JSON.stringify($('form').serializeObject()));
				    $.ajax({
			        url: "/backpack/v1/users/login",
			        type: 'POST',    
			        contentType: 'application/json',
			        data:JSON.stringify($('form').serializeObject()), 
			        error:function(){
			        $("#errormsg").empty().append("Wrong Username or Password");
			  		},
			        success: function(data) {
			            window.location = "/backpack/v1/users/"+data;
			  	}
			  	});

				    });
				});
				
	
				    $("#signup").click(function() {
				    window.location = "/backpack/v1/users/signup";
			  	});

			</script>
        </body>
</html>
