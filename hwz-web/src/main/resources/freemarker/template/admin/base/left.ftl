<#assign currentResources = currentResources()>
<!--	=========================	左边栏开始	========================	-->
  <aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
      <!-- Sidebar user panel -->
      <div class="user-panel">
        <div class="pull-left image">
          <img src="/static/admin/base/dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
        </div>
        <div class="pull-left info">
          <p>
            <#if currentUser?exists>
            	${currentUser.realName}
          	</#if>
          </p>
          <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
        </div>
      </div>
      <!-- sidebar menu: : style can be found in sidebar.less -->
      <ul class="sidebar-menu">
      
        <li class="header">MAIN NAVIGATION</li>
        
        <#if currentResources?exists>
        
          <#assign currentCateogry = "">
          <#list currentResources as r>
            <#if currentCateogry == r.resource_category>
              <li id="resource_${r.resource_id}" class="">
                <a href="${r.resource_url}">
				  <span class="glyphicon glyphicon-hand-right"></span>
				  <span>${r.resource_name}</span>
			    </a>
              </li>
            <#else>
              <#if currentCateogry != "">
	   		      </ul>
			    </li>
			  </#if>
              <li class="treeview ">
	              <a href="#">
	                <i class="fa fa-dashboard"></i> <span>${r.resource_category}</span>
	            	<span class="pull-right-container">
	                  <i class="fa fa-angle-left pull-right"></i>
	                  <span class="label label-primary pull-right">4</span>
	                </span>
	              </a>
              	  <ul class="treeview-menu">
	              	  <li class="active" id="resource_${r.resource_id}">
	              	  	<a href="${r.resource_url}"><i class="fa fa-circle-o"></i>${r.resource_name}</a>
	              	  </li>
              	  <#assign currentCateogry = r.resource_category>
            </#if>
          </#list>
        </#if>

      </ul>
    </section>
    <!-- /.sidebar -->
  </aside>
  <!-- /.main-sidebar -->
  <!--	==============================	左边栏结束	============================	-->