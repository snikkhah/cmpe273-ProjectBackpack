
<html>
        <head>
            <title>Home</title>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <!-- Latest compiled and minified CSS -->
            <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" media="screen">
            <!-- Optional theme -->
            <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap-theme.min.css">
        </head>
        <body>
            <div class="container">
                <div class="jumbotron">
                    <!-- calls getBooks() from HomeResource -->
                    <table class="table table-hover">
                    
                    <#list users as user>
                        <tr id="${user.userID}t">
                        	<td>${user.userID}</td>
							<td>${user.firstName}</td>
							<td>${user.lastName}</td>
							<td>${user.email}</td>
							<td>${user.status}</td>
							<td>${user.designation}</td>
							<td><button id="${user.userID}" type="button" class="btn btn-primary">Report Lost</button></td>
                        </tr>
                        </#list>
                    
                    </table>
                </div>
            </div> <!-- end of container -->
           
            <!-- script tags -->
            <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
            <script src="//code.jquery.com/jquery.js"></script>
            <!-- Latest compiled and minified JavaScript -->
            <script src="//netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
            <!-- application ui scripts -->
			<script src="../assets/js/app.js"></script>
        </body>
</html>