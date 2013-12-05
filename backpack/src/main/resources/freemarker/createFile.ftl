
<html>
        <head>

            <title>Upload</title>
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
				        <button type="button" id="logout" class="btn btn-default navbar-btn navbar-right">Log out</button>
						<p class="navbar-text navbar-right" style="margin-right: 10px;"><b>Signed in as ${user.firstName} ${user.lastName}</b></p>
			        </div><!--/.navbar-collapse -->
			      </div>
			    </div>
            <div class="container">
                <div class="jumbotron">
				    <h2>Create a File</h2>
				    
				      <table class="table table-hover" >
				        <tr>
				        <td> 
				        	<form method="post" enctype="multipart/form-data">
				        		<input type="file" id="upload" name="file">
				        		   
<!--				        		<select name="accessType">        
                                    <option value="Private">Private</option>
                                    <option value="Public">Public</option>
                                </select>
 -->                               
					        </form>
					        
				        </td>
				       </tr> 
					</table>
				    
				    
				    <div id="submitButton" class="btn btn-primary">Upload</div>
				    <div id="cancelButton" class="btn btn-warning">Cancel</div>
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
				    	var formData = new FormData($('form')[0]);
//						var accessType = JSON.stringify($('form').serializeObject());
//				        alert(JSON.stringify($('form').serializeObject()));
						
				        $.ajax({
			        url: "/backpack/v1/users/${userID}/files",
			        type: 'POST',    
		          	data:formData,
		          	cache: false,
			        contentType: false,
			        processData: false,
			        success: function(result) {
			         window.location = "/backpack/v1/users/${userID}";
			  	},
			  		error:function(jqXHR,error, errorThrown){
			        var msg ;
			        if (errorThrown=="Not Acceptable"){msg = "File was not selcted!";}
			        else if (errorThrown== "Conflict"){msg="File already exists! Please remove the existing file first or rename the file you are trying to update.";}
			        else {msg="Something doesn't add up!";}
			        $("#errormsg").empty().append(msg);
//			        alert("file already exist");
			  		}
			  		
			  	});

				    });
				});
				
                $('#logout').click(function() {
                 window.location = "/backpack/v1/users/${user.userID}/logout";
                    });
                    
            $('#cancelButton').click(function() {
                 window.location = "/backpack/v1/users/${userID}";
            });                

			</script>
        </body>
</html>

