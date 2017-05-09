<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>test_login</title>
</head>
<body>
	<form id="theform">
		<input name="login_username" type="text" placeholder="登录账号" datatype="*" nullmsg="请输入用户名！" />
		<br />
		<input name="login_password" type="password" class="login-text password" datatype="*" placeholder="登录密码" value="" />
		<br />
		<#if msg != null>
			<span>${msg}</span>
        </#if>
        <br />
        <input type="button" value="提交" onclick="login();" />
	</form>
</body>
</html>
<script type="text/javascript" src="/static/app/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript">
    function login() {
    	
		$.ajax({
			type: 'POST',
			dataType: 'json',
			cache: false,
			url: '/api/user/login.json',
			data: $("#theform").serialize(),
			success: function(data){
				alert(data);
			},
			error: function(data){
				alert("异常！");
			}
		});
    	
    }
</script>