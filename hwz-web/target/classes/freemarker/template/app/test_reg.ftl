<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>用户注册</title>
</head>
<body>
	<form id="theform" action="/api/user/register.json" method="post">
		<input name="loginUserName" type="text" placeholder="用户名" datatype="*" nullmsg="请输入用户名！" />
		<br />
		<input name="loginPassword" type="password"  datatype="*" placeholder="注册密码" value="" />
		<br />
		<input name="nick_name" type="text" placeholder="昵称" value="" />
		<br />
		<input name="telephone" type="text" placeholder="电话号码" value="" />
		<#if msg != null>
			<span>${msg}</span>
        </#if>
        <br />
        <input type="button" value="提交" onclick="reg();" />
	</form>
</body>
</html>
<script type="text/javascript" src="/static/app/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript">
    function reg() {
        $("#theform").submit();
    }
</script>