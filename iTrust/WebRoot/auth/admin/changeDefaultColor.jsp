<%@page import="edu.ncsu.csc.itrust.beans.UserPrefsBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.TransactionBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.TransactionDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.action.UserPrefsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.LinkedList"%>

<%@include file="/global.jsp" %>

<script language="javascript">
// Author: Dion, biab@iinet.net.au, http://biab.howtojs.com/
// Courtesy of SimplytheBest.net - http://simplythebest.net/scripts/

addary=new Array(255,1,1);
clrary=new Array(360);
for(i=0;i<6;i++)
 for(j=0;j<60;j++)
  { clrary[60*i+j]=new Array(3);
    for(k=0;k<3;k++)
     { clrary[60*i+j][k]=addary[k];
       addary[k]+=((Math.floor(65049010/Math.pow(3,i*3+k))%3)-1)*4; }; };

function capture()
 { if(document.layers)
    { with(document.layers['imgdiv'])
       { document.captureEvents(Event.MOUSEMOVE);
         document.onmousemove=moved; }; }
   else { document.all.imgdiv.onmousemove=moved; };
 };

function moved(e)
 { sx=((document.layers)?e.layerY:event.offsetY)-256;
   sy=((document.layers)?e.layerX:event.offsetX)-256;
   quad=new Array(-180,360,180,0);
   xa=Math.abs(sx); ya=Math.abs(sy);
   d=ya*45/xa;
   if(ya>xa) d=90-(xa*45/ya);
   deg=Math.floor(Math.abs(quad[2*((sy<0)?0:1)+((sx<0)?0:1)]-d));
   n=0; c="000000";
   r=Math.sqrt((xa*xa)+(ya*ya));
   if(sx!=0 || sy!=0)
    { for(i=0;i<3;i++)
       { r2=clrary[deg][i]*r/128;
         if(r>128) r2+=Math.floor(r-128)*2;
         if(r2>255) r2=255;
         n=256*n+Math.floor(r2); };
      c=(n.toString(16)).toUpperCase();
      while(c.length<6) c="0"+c; };
   if(document.layers)
    { document.layers['clrdiv'].bgColor="#"+c; }
   else
    { document.all["clrdiv"].style.backgroundColor="#"+c; };
   document.frm.txt.value=c;
   document.frm.hid.value=c;
   return false; };
function setcolor()
 { document.frm.sel.value=document.frm.hid.value; };
</script>

<%
pageTitle = "iTrust - Change Default Theme Color";
%>

<%@include file="/header.jsp" %>
<% 
	UserPrefsAction action = new UserPrefsAction(prodDAO, loggedInMID.longValue());
	
	/* Now take care of updating information */
	UserPrefsBean prefsBean;
	
	if (request.getParameter("sel") != null) {
		prefsBean = new UserPrefsBean();
		prefsBean.setMID(0);
		prefsBean.setThemeColor(request.getParameter("sel"));
		prefsBean.setSecondaryColor("FFFFFF");
		try {
			action.updateGlobalPrefs(prefsBean);
			loggingAction.logEvent(TransactionType.GLOBAL_PREFERENCES_EDIT, loggedInMID, 0, "");
%>
		<div align=center>
			<span class="iTrustMessage">Information Successfully Updated</span>
		</div>
<%
		} catch (FormValidationException e) {
%>
		<div align=center>
			<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage()) %></span>
		</div>
<%
		}
	} else {
		loggingAction.logEvent(TransactionType.GLOBAL_PREFERENCES_VIEW, loggedInMID, 0, "");
	}
%>

<body onload="capture();">
<fieldset>
<legend>User Preferences</legend>
<label>Theme Color</label>
<table border=1 cellpadding=0 cellspacing=0>
 <tr><td><div id=imgdiv><a href="javascript:void(0);" onclick="setcolor(); return false;">
  <img src="/iTrust/colorwheel.jpg" width=512 height=512 border=0></a></div></td></tr>
 <tr><td align="center"><div id=clrdiv>The Color</div></td></tr>
 <tr><form name="frm"><td align="center"><input type="text" name="txt" size=12>
  <input type="text" name="sel" value="<%= action.getDefaultColor().getThemeColor() %>" size=12>
  <input type="hidden" name="hid">
  <input type="submit" value="Update Preferences" /></td></form></tr>
</table>
</fieldset>
</body>

<%@include file="/footer.jsp" %>