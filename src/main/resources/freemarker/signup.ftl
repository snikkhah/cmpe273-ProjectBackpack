
<html>
        <head>
            <title>SignUp</title>
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
        <body>
            <div class="container">
                <div class="jumbotron">
                    Already a user? <a href="/dropbox/v1/users/login">Login</a><p>
				    <h2>Signup</h2>
				    <form method="post">
				      <table class="table table-hover" >
				        <tr>
				          <td class="label" color="black">Email</td>
				          <td><input type="text" name="email" value="${email}"></td>
				          <td class="error">${email_error!""}</td>
				        </tr>
				        <tr>
				          <td class="label">Password</td>
				          <td><input type="password" name="password" value="${password}"></td>
				          <td class="error">${password_error!""}</td>
				        </tr>
				        <tr>
				          <td class="label">Confirm Password</td>
				          <td><input type="password" name="confirmPassword" value="${confirmPassword}"></td>
				          <td class="error">${password_error!""}</td>
				        </tr>				
						<tr>
				          <td class="label">First Name</td>
				          <td><input type="text" name="firstName" value="${firstName}"></td>
				        </tr>
						<tr>
				          <td class="label">Last Name</td>
				          <td><input type="text" name="lastName" value="${lastName}"></td>
				        </tr>
						<tr>
				          <td class="label">Designation</td>
				          <td><input type="text" name="designation" value="${designation}"></td>
				        </tr>				      
					</table>
<!--				      <input type="submit">  -->
				    </form>
				    
				    <div id="submitButton" class="btn btn-primary">Submit</div>
				    <h5 id ="errormsg" class="error"></h5>
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
				    $('#submitButton').click(function() {
//				        alert(JSON.stringify($('form').serializeObject()));
				        $.ajax({
			        url: "/dropbox/v1/users/signup",
			        type: 'POST',    
			        contentType: 'application/json',
			        data:JSON.stringify($('form').serializeObject()), 
			        error:function(jqXHR,error, errorThrown){
			        var msg ;
			        if (errorThrown=="Bad Request"){msg = "Passwords don't match. Please try again!";}
			        if (errorThrown== "Conflict"){msg="User already exists!";}
			        $("#errormsg").empty().append(msg);
			  		},
			        success: function(result) {
			        	window.location = "/dropbox/v1/users/login";
//			            alert("success?");
			  	}
			  	});

				    });
				});
			</script>
        </body>
</html>

