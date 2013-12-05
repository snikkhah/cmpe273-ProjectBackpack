$("#submitButton").click(function() {
			
				alert('About to submit');			
			    $.ajax({
			        url: "/dropbox/v1/users/signup",
			        type: 'POST',    
			        contentType: 'application/json',
			        data:$("form").serialize(), 
			        success: function(result) {
			            alert("success?");
			  	}});
			  	alert($("form").serialize());
			});