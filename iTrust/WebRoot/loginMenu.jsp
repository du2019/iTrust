<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaImpl" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaResponse" %>
    
<div class="menu_category">
	<fieldset>
		<legend>Login</legend>
<script type="text/javascript">
function fillLoginFields(u,p) {
	document.getElementById("j_username").value = u;
	document.getElementById("j_password").value = p;		
	document.forms[0].submit();	
}
</script>
<%
	int failureCount = loginFailureAction.getFailureCount();
	if(failureCount != 0) {
		String msg = "Failed login attempts: " + failureCount;
%>
	<div style="align: center; margin-bottom: 10px;">
		<span class="iTrustError" style="font-size: 16px;"><%= StringEscapeUtils.escapeHtml("" + (msg)) %></span>
	</div>
<%
	}
%>
<%
	if(!loginFailureAction.needsCaptcha()){

%>
	<form method="post" action="/iTrust/login.jsp">
	<span>MID</span><br />
	<input type="text" maxlength="10" id="j_username" name="j_username"><br />
	<span>Password</span><br />
	<input type="password" maxlength="20" id="j_password" name="j_password"><br /><br />
	<input type="submit" value="Login"><br /><br />

	<a style="font-size: 80%;" href="/iTrust/util/resetPassword.jsp">Reset Password</a>

	</form>
	</fieldset>
<%
	}
if( ! "true".equals(System.getProperty("itrust.production") ) ) { 
%>
	<!-- This section is for testing purposes only!! -->
	<br/>
	<div align="center">Sample Users</div>
	<table style="width:100%;">
		<tr>
			<td>
				<a class="iTrustTestNavlink" onclick="fillLoginFields('1','pw')" href="javascript:void(0)">Patient 1</a>
			</td>
			<td>
				<a class="iTrustTestNavlink" onclick="fillLoginFields('2','pw')" href="javascript:void(0)">Patient 2</a>
			</td>
		</tr>
		<tr>
			<td>
				<a class="iTrustTestNavlink" onclick="fillLoginFields('9000000000','pw')" href="javascript:void(0)">HCP</a>
			</td>
			<td>
				<a class="iTrustTestNavlink" onclick="fillLoginFields('8000000009','uappass1')" href="javascript:void(0)">UAP</a>
			</td>
		</tr>
		<tr>
			<td>
				<a class="iTrustTestNavlink" onclick="fillLoginFields('9000000006','pw')" href="javascript:void(0)">ER</a>
			</td>
			<td>
				<a class="iTrustTestNavlink" onclick="fillLoginFields('7000000001','pw')" href="javascript:void(0)">PHA</a>
			</td>
		</tr>
		<tr>
			<td>
				<a class="iTrustTestNavlink" onclick="fillLoginFields('9000000001','pw')" href="javascript:void(0)">Admin</a>
			</td>
			<td>
				<a class="iTrustTestNavlink" onclick="fillLoginFields('9999999999','pw')" href="javascript:void(0)">Tester</a>
			</td>
		</tr>
	</table>
<% 
} 
%>
</div>
<script type="text/javascript">
	document.forms[0].j_username.focus();
</script>
<%
%>
