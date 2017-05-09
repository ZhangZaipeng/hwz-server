<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>移动端API测试</title>
</head>
<body>

<li><a href="/app/test_login.htm">测试登录</a></li>
<li><a href="/app/test_reg.htm">测试注册</a></li>
<li><a href="javascript:void(0)" onclick='current_user()'>current_user</a></li>
<li><a href="javascript:void(0)" onclick='modify_user()'>modifyuser</a></li>


</body>
</html>
<script type="text/javascript" src="/static/app/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript">
	
	function current_user() {
		alert('a');
		var pams = [];		
		$('<form>', {
		    method: 'get',
		    action: '/api/user/getCurrentUser.json',
		    target: '_blank'
		}).append(pams).submit();
	}
	
    function modify_user() {
		alert('a');
		$.ajax({
	 		url: '/api/modify_cust.json' ,
	 		type: 'POST',
			dataType: 'json',
			data: { 'real_name':'zhang' ,'nick_name':'nihao' ,'gender':'女','birthdate':'1970-01-02' } ,
			success: function(data){
				alert(data.msg);
			} ,
			error : function() {  
	      		alert("异常！");  
	      	}
		});
	}
	
</script>