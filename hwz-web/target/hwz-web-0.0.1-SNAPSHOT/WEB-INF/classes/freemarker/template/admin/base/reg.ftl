<#assign roles = currentAllRole()>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>zzpproject</title>
	<!-- Tell the browser to be responsive to screen width -->
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<!-- Bootstrap 3.3.5 -->
	<link rel="stylesheet" href="/static/admin/base/css/bootstrap.css">
	<!-- Font Awesome -->
	<link rel="stylesheet" href="/static/admin/base/css/font-awesome.min.css">
	<!-- Ionicons -->
	<link rel="stylesheet" href="/static/admin/base/css/ionicons.min.css">
	<!-- DataTables -->
	<link rel="stylesheet" href="/static/admin/base/css/dataTables.bootstrap.css">
	<!-- Theme style -->
	<link rel="stylesheet" href="/static/admin/base/css/AdminLTE.min.css">

  </head>
  <body class="hold-transition register-page">
        	<div class="login-box">
      <div class="login-logo">Zzp Project</div>
	  
      <div class="login-box-body">
		  
<#if msg != null>
<div class="alert alert-danger alert-dismissable">
  <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
  <h4>  <i class="icon fa fa-close"></i>登录发生错误</h4>
  ${msg}
</div>
</#if>
		  
		  <p id="flash_alert" class="login-box-msg">员工登记</p>
<form class="new_user" id="theform">
			
        <div class="form-group has-feedback">
		    <label for="staff_code">工号</label><br />
		    <input autofocus="autofocus" class="form-control" type="text" name="staffCode" id="staff_code" />
			<span class="glyphicon glyphicon-copyright-mark form-control-feedback"></span>
		  </div>
		  
		   <div class="form-group has-feedback">
		    <label for="real_name">姓名</label><br />
		    <input type="hidden" id="roleIds" name="roleIds"/>
		    <input autofocus="autofocus" class="form-control" type="text" name="realName" id="real_name" />
			<span class="glyphicon glyphicon-user form-control-feedback"></span>
		  </div>
		   <div class="form-group has-feedback">
		    <label for="email">电子邮箱</label><br />
		    <input autofocus="autofocus" class="form-control" type="email" name="email" id="email" />
			<span class="glyphicon glyphicon-envelope form-control-feedback"></span>
		  </div>
		   <div class="form-group has-feedback">
		    <label for="telephone">手机号</label><br />
		    <input autofocus="autofocus" class="form-control" type="text" name="telephone" id="telephone" />
			<span class="glyphicon glyphicon-phone form-control-feedback"></span>
		  </div>
		   <div class="form-group has-feedback">
		    <label for="pass1">密码</label>
		    <br />
		    <input autocomplete="off" class="form-control" type="password" name="pass1" id="pass1" />
			<span class="glyphicon glyphicon-lock form-control-feedback"></span>
		  </div>
		   <div class="form-group has-feedback">
		    <label for="pass2">密码确认</label><br />
		    <input autocomplete="off" class="form-control" type="password" name="pass2" id="pass2" />
			<span class="glyphicon glyphicon-lock form-control-feedback"></span>
		  </div>
		  <div class="form-group has-feedback">
		    <label for="role_id">角色</label><br />
		    <select class="form-control" name="roleId" id="roleId">
			    <#if roles?exists>
			    	<#list roles as role>
			    	<option value="${role.role_id}">${role.role_name}</option>
			    </#list>
			    </#if>
		    </select>
		  </div>
          <div class="row" style="line-height:34px;">
            <div class="col-xs-8">
                <a href="/staff/login.htm">登入</a><br />
            </div><!-- /.col -->
            <div class="col-xs-4">
				<input id="commitit" type="button" value="注册" class="btn btn-primary btn-block btn-flat" />
            </div><!-- /.col -->
          </div>
</form>
      </div><!-- /.login-box-body -->
</div><!-- /.login-box -->

  </body>
</html>
<script src="/static/admin/base/js/html5shiv.min.js"></script>
<script src="/static/admin/base/js/respond.min.js"></script>
<!-- jQuery 2.1.4 -->
<script src="/static/admin/base/js/jquery.js"></script>
<!-- Bootstrap 3.3.5 -->
<script src="/static/admin/base/js/bootstrap.min.js"></script>
<!-- iCheck -->
<script src="/static/admin/base/js/icheck.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$('#commitit').click(function(){
		$.ajax({
			type: 'POST',
			dataType: 'json',
			cache: false,
			url: '/staff/register.htm',
			data: $("#theform").serialize(),
			success: function(data){
				alert(data.msgbox);
				if(data.success){
					window.location='/staff/login.htm';
				}
			},
			error: function(data){
				alert("注册时发生异常!");
			}
		});
	});
});
</script>
