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
		<div class="login-logo">
			<b>Zzp</b>Project
		</div><!-- /.login-logo -->
		<div class="login-box-body">
			<div id="msgbox" style="display: none" class="alert alert-danger alert-dismissable">
	  			<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
	  			<h4>  <i class="icon fa fa-close"></i>登录发生错误</h4>
  					<p id ="msgcontext"></p>
			</div>
		<p id="flash_alert" class="login-box-msg">登录</p>
        <form class="new_user" id="new_user" action="/staff/login.htm" accept-charset="UTF-8" method="post">
			<input name="utf8" type="hidden" value="&#x2713;" /><input type="hidden" name="authenticity_token" value="KYxfDqOiVwixF9ZSukjKxl+SukqyXSBvrSOnSOzQYPpUqfrJ5wHyXs/LrIPT9sHKcy/cNgSuuGaD9RlR0UsBXA==" />
			<div class="form-group has-feedback">
				<input autofocus="autofocus" class="form-control" placeholder="邮箱/手机/登录名" value="" name="loginName" id="user_email" />
				<span class="glyphicon glyphicon-envelope form-control-feedback"></span>
			</div>
			<div class="form-group has-feedback">
				<input autocomplete="off" class="form-control" type="password" name="loginPwd" id="user_password" />
				<span class="glyphicon glyphicon-lock form-control-feedback"></span>
			</div>
			<div class="row" style="line-height:34px;">
				<div class="col-xs-8">
		  			<a href="/staff/toReg.htm">员工注册</a><br />
		    	</div><!-- /.col -->
		    	<div class="col-xs-4">
					<input type="button" id="commit" value="登入" class="btn btn-primary btn-block btn-flat" />
				</div><!-- /.col -->
			</div>
		</form>
	</div><!-- /.login-box-body -->
</div><!-- /.login-box -->

<script src="/static/admin/base/js/html5shiv.min.js"></script>
<script src="/static/admin/base/js/respond.min.js"></script>
<!-- jQuery 2.1.4 -->
<script src="/static/admin/base/js/jquery.js"></script>
<!-- Bootstrap 3.3.5 -->
<script src="/static/admin/base/js/bootstrap.min.js"></script>
<!-- iCheck -->
<script src="/static/admin/base/js/icheck.min.js"></script>
<script type="text/javascript">
	$(function () {
		$('input').iCheck({
			checkboxClass: 'icheckbox_square-blue',
			radioClass: 'iradio_square-blue',
			increaseArea: '20%' // optional
		});
		
		$('#commit').click(function(){
			$.ajax({
				type: 'POST',
				dataType: 'json',
				cache: false,
				url: '/staff/login.htm',
				data: $("#new_user").serialize(),
				success: function(data){
					if (data.success) {
						window.location='/staff/main.htm';
					} else {
						$("#msgcontext").html(data.msgbox);
						$("#msgbox").show();
					}
				},
				error: function(data){
					alert("注册时发生异常!");
				}
			});
		});
		
	});
	
</script>
</body>
</html>