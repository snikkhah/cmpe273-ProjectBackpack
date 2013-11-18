
<html>
        <head>
            <title>DropBox</title>

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
			          <a class="navbar-brand" href="#">Welcome to DropBox</a>
			        </div>
			        <div class="navbar-collapse collapse">
				        <button type="button" id="logout" class="btn btn-default navbar-btn navbar-right">Log out</button>
						<p class="navbar-text navbar-right" style="margin-right: 10px;"><b>Signed in as ${user.firstName} ${user.lastName}</b></p>
						<form method="post" class="navbar-form navbar-left" role="search"><input type="text" name="name" class="form-control" placeholder="Search" value="${name}"></form>
						<button type="submit" id="searchButton" class="btn btn-primary navbar-btn navbar-left">Submit</button>
			        </div><!--/.navbar-collapse -->
			      </div>
			    </div>		

            <div class="container">
                <div class="jumbotron">
                    <hr>
                    <hr>
					<div id="puplicFiles" class="container">
					</div>
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
				    $('#searchButton').click(function() {
//				        alert(JSON.stringify($('form').serializeObject()));
				    $.ajax({
			        url: "/dropbox/v1/users/${userID}/publicFiles",
			        type: 'POST',    
			        contentType: 'application/json',
			        data:JSON.stringify($('form').serializeObject()), 
			        error:function(){
			        alert("something is wrong!");
			  		},
			        success: function(data) {
			           $("#puplicFiles").empty().append(data);
			  	}
			  	});

				    });
				});
			</script>
        </body>
</html>
