<%@page import="edu.ncsu.csc.itrust.DBUtil"%>

<%
if(!DBUtil.canObtainProductionInstance()){
	response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
}


if(request.getUserPrincipal() != null) {
	long mid = Long.valueOf(request.getUserPrincipal().getName());
	userName = authDAO.getUserName(mid);
	
	if(session.getAttribute("pid") != null)
	{
		String pid = (String)session.getAttribute("pid");
		selectedPatientName = authDAO.getUserName(Long.parseLong(pid));
	}
}
else
{
	if (null != userRole)
	{
		userRole = null;
		response.sendRedirect("/iTrust/errors/reboot.jsp");
	}
}


if (request.getAuthType() != null) {
			
	if (request.getSession(false) != null) {
		boolean isValidLogin = loginFailureAction.isValidForLogin();
		if(!isValidLogin) {
			session.invalidate();
			response.sendRedirect("/iTrust/login.jsp");
			return;
		}
		else {
			loggedInMID = new Long(Long.valueOf(request.getUserPrincipal().getName()));
			session.setAttribute("loggedInMID", loggedInMID);
			loginFailureAction.resetFailures();
		}
	}
}

%>
