<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>

<%@page import="edu.ncsu.csc.itrust.EmailUtil"%>
<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.LOINCbean"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.MedicationBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.OverrideReasonBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.action.AddOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.forms.EditOfficeVisitForm"%>
<%@page import="edu.ncsu.csc.itrust.exception.iTrustException"%>
<%@page import="edu.ncsu.csc.itrust.beans.HospitalBean"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.PrescriptionBean"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Document Office Visit";
%>

<%@include file="/header.jsp" %>

<%
	boolean createVisit = false;
	String ovIDString = request.getParameter("ovID");
	String pidString = (String)session.getAttribute("pid");
	
	if(ovIDString == null) {
		createVisit = true;
		AddOfficeVisitAction action = new AddOfficeVisitAction(prodDAO, pidString);
		ovIDString = "" + action.addEmptyOfficeVisit(loggedInMID.longValue());
		loggingAction.logEvent(TransactionType.OFFICE_VISIT_CREATE, loggedInMID, Long.parseLong(pidString), "Office visit ID: " + ovIDString);
	}

	if (pidString == null || 1 > pidString.length()) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/editOfficeVisit.jsp?ovID=" + ovIDString);
	   	return;
	}
	
    EditOfficeVisitAction action = new EditOfficeVisitAction(prodDAO, loggedInMID, pidString, ovIDString);
	long ovID = action.getOvID();
	OfficeVisitBean visit = action.getOfficeVisit();
	String confirm = "";
	String warning = "";
	if (request.getParameter("startDate") != null && request.getParameter("endDate") != null){
		warning = action.hasInteraction(request.getParameter("addMedID"), pidString, request.getParameter("startDate"), request.getParameter("endDate"));
	}
		warning += action.isAllergyOnList(pidString,request.getParameter("addMedID"));
	
	if ("true".equals(request.getParameter("formIsFilled"))) {
		EditOfficeVisitForm form = new BeanBuilder<EditOfficeVisitForm>().build(request.getParameterMap(), new EditOfficeVisitForm());
		if ("".equals(warning) || "true".equals(request.getParameter("checkPresc"))){			
			if ("true".equals(request.getParameter("checkPresc"))){
				form.setAddMedID(request.getParameter("testMed"));
				form.setStartDate(request.getParameter("medStart"));
				form.setDosage(request.getParameter("medDos"));
				form.setEndDate(request.getParameter("medEnd"));
				form.setInstructions(request.getParameter("medInst"));
				form.setOverrideCode(request.getParameterValues("overrideReasonCode"));
				form.setOverrideComment(request.getParameter("overrideComment"));
				String problem = action.hasInteraction(request.getParameter("testMed"), pidString, request.getParameter("medStart"), request.getParameter("medEnd"));
				problem += action.isAllergyOnList(pidString,request.getParameter("testMed"));
				new EmailUtil(prodDAO).sendEmail(action.makeEmailApp(loggedInMID,pidString,problem));
				
			}
			
			form.setHcpID("" + visit.getHcpID());
			form.setPatientID("" + visit.getPatientID());
			try {
				confirm = action.updateInformation(form);
				
				if(!"".equals(request.getParameter("addLabProcID"))) {
					loggingAction.logEvent(TransactionType.OFFICE_VISIT_EDIT, loggedInMID, Long.parseLong(pidString), 
							"Procedure: " + request.getParameter("addLabProcID"));
				}
				
				else if ( request.getParameter("checkPresc").equals("true") || !"".equals(request.getParameter("addMedID"))){
						String loggingExtra = "";
						if(request.getParameterValues("overrideReasonCode") != null) {
							loggingExtra += ", Reason:";
							for(String reason : request.getParameterValues("overrideReasonCode"))
								loggingExtra += " " + reason;
						}
							
						if(request.getParameter("overrideComment") != null) {
							loggingExtra += ", Comment: " + request.getParameter("overrideComment");
						}
						loggingAction.logEvent(TransactionType.PRESCRIPTION_ADD, loggedInMID, Long.parseLong(pidString), 
								"Prescription: " + request.getParameter("testMed") + loggingExtra);
				} else {
					loggingAction.logEvent(TransactionType.OFFICE_VISIT_EDIT, loggedInMID, Long.parseLong(pidString), "Office visit ID: " + ovIDString);
				}
			}
			catch (FormValidationException e) {
				e.printHTML(pageContext.getOut());
				confirm = "Input not valid";
			}
		}
	} else if(!createVisit) {
		loggingAction.logEvent(TransactionType.OFFICE_VISIT_VIEW, loggedInMID, Long.parseLong(pidString), "Office visit ID: " + ovIDString);
	}
	OfficeVisitBean ov = action.getOfficeVisit();
	List<HospitalBean> hcpHospitals = action.getHospitals(ov.getHcpID());
	List <LabProcedureBean> lpBeans = action.getLabProcedures(Long.parseLong(pidString), ovID);
%>

<div align=center>
<%
if (!"".equals(confirm)) {
	if (request.getParameter("checkPresc").equals("false")){ %>
		<span class="iTrustMessage">Operation Canceled</span>
	<% } else if ("success".equals(confirm)) { 
			
	%>
		<span class="iTrustMessage">Information Successfully Updated</span>
<%	}
	else { %>
		<span class="iTrustError"><%= StringEscapeUtils.escapeHtml("" + (confirm)) %></span>		
<%	}
}	
%>
</div>

<script type="text/javascript">
	function removeID(type, value) {
		document.getElementById(type).value = value;
		document.forms[0].submit();
	}

	function setVar(){
		var medID = document.getElementById("addMedID");
		var medIDindex = medID.options.selectedIndex;
		var medIDtxt = medID.options[medIDindex].value;
		var medDostxt = document.getElementById("dosage").value;
		var medStarttxt = document.getElementById("startDate").value;
		var medEndtxt = document.getElementById("endDate").value;
		var medInsttxt = document.getElementById("instructions").value;
		document.getElementById("testMed").value = medIDtxt;
		document.getElementById("medDos").value = medDostxt;
		document.getElementById("medStart").value = medStarttxt;
		document.getElementById("medEnd").value = medEndtxt;
		document.getElementById("medInst").value = medInsttxt;
		document.forms[0].submit();
	}

	function presCont(){
		document.getElementById("checkPresc").value = "true";
		document.forms[0].submit();
	}

	function presCanc(){
		var medID = document.getElementById("addMedID");
		var medIDindex = medID.options.selectedIndex;
		var medIDtxt = medID.options[medIDindex].value;
		var medDostxt = document.getElementById("dosage").value;
		var medStarttxt = document.getElementById("startDate").value;
		var medEndtxt = document.getElementById("endDate").value;
		var medInsttxt = document.getElementById("instructions").value;

		document.getElementById("testMed").value = "";
		document.getElementById("medDos").value = "";
		document.getElementById("medStart").value = "";
		document.getElementById("medEnd").value = "";
		document.getElementById("medInst").value = "";
		document.getElementById("checkPresc").value = "false";
		document.forms[0].submit();
	}
	
</script>

<form action="editOfficeVisit.jsp" method="post" id="mainForm">
	<input type="hidden" name="formIsFilled" value="true" />
	<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + (ovID)) %>" />
	<input type="hidden" id="removeDiagID" name="removeDiagID" value="" />
	<input type="hidden" id="removeMedID" name="removeMedID" value="" />
	<input type="hidden" id="removeProcID" name="removeProcID" value="" />
	<input type="hidden" id="removeImmunizationID" name="removeImmunizationID" value="" />
	<input type="hidden" id="removeLabProcID" name="removeLabProcID" value="" />
	<input type="hidden" id="checkPresc" name="checkPresc" value="" />
	<input type="hidden" id="testMed" name="testMed" value=<%= StringEscapeUtils.escapeHtml("" + (request.getParameter("testMed") )) %> />
	<input type="hidden" id="medDos" name="medDos" value=<%= StringEscapeUtils.escapeHtml("" + (request.getParameter("medDos") )) %> />
	<input type="hidden" id="medStart" name="medStart" value=<%= StringEscapeUtils.escapeHtml("" + (request.getParameter("medStart") )) %> />
	<input type="hidden" id="medEnd" name="medEnd" value=<%= StringEscapeUtils.escapeHtml("" + (request.getParameter("medEnd") )) %> />
	<input type="hidden" id="medInst" name="medInst" value=<%= StringEscapeUtils.escapeHtml("" + (request.getParameter("medInst") )) %> />

<div align=center>
<table class="fTable" align="center">
	<tr>
		<th colspan="2">Office Visit</th>
	</tr>
	<tr>
		<td class="subHeaderVertical">Patient ID:</td>
		<td><%= StringEscapeUtils.escapeHtml("" + (prodDAO.getAuthDAO().getUserName(ov.getPatientID()))) %> </td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Date of Visit:</td>
		<td><input name="visitDate" value="<%= StringEscapeUtils.escapeHtml("" + (ov.getVisitDateStr())) %>" /><input type="button" value="Select Date" onclick="displayDatePicker('visitDate');" /></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Hospital:</td>
		<td><select name="hospitalID">
				<option value="">N/A</option>
				<%for(HospitalBean hos : hcpHospitals) {%>
					<option value="<%= StringEscapeUtils.escapeHtml("" + (hos.getHospitalID())) %>" 
						<%= StringEscapeUtils.escapeHtml("" + (hos.getHospitalID().equals(ov.getHospitalID()) ? "selected=selected" : "")) %> > 
						<%= StringEscapeUtils.escapeHtml("" + (hos.getHospitalName())) %>
					</option>
				<%} %>				
			</select>
		</td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Notes:</td>
		<td><textarea rows="4" style="width: 100%;" name="notes"><%= StringEscapeUtils.escapeHtml("" + (ov.getNotes())) %></textarea></td>
	</tr>
</table>
<br />
<input type="submit" name="update" id="update" value="Update" >
</div>
<br /><br />
<div align=center>
<table class="fTable" align="center">
	<tr>
		<th colspan="6">Prescriptions</th>
	</tr>
	<tr class="subHeader">
		<td>Medication</td>
		<td>Dosage</td>
		<td>Dates</td>
		<td colspan=2>Instructions</td>
		<td style="width: 60px;">Action</td>
	</tr>
	
	<%if(ov.getPrescriptions().size()==0){ %>
		<tr>
			<td colspan="6" style="text-align: center;">No Prescriptions on record</td>
		</tr>
	<%}else{ %>
		<%for(PrescriptionBean pres : ov.getPrescriptions()){ %>
		<tr>
			<td align=center><a href="./editPrescription.jsp?presID=<%= StringEscapeUtils.escapeHtml("" + (pres.getId())) %>&ovID=<%= StringEscapeUtils.escapeHtml("" + (ovIDString)) %>"><%= StringEscapeUtils.escapeHtml("" + (pres.getMedication().getDescription())) %> (<%= StringEscapeUtils.escapeHtml("" + (pres.getMedication().getNDCode())) %>)</a></td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (pres.getDosage())) %>mg</td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (pres.getStartDateStr())) %> to <%= StringEscapeUtils.escapeHtml("" + (pres.getEndDateStr())) %></td>						
			<td align=center colspan=2><%= StringEscapeUtils.escapeHtml("" + (pres.getInstructions())) %></td>						
			<td align=center><a href="javascript:removeID('removeMedID','<%= StringEscapeUtils.escapeHtml("" + (pres.getId())) %>');">Remove</a></td>
		</tr>
		<%}
	}%>
	<tr>
		<th colspan="6" style="text-align: center;">Add New</th>
	</tr>
	 <tr>
	 	<td align=center>
	 		<select name="addMedID" id="addMedID" style="font-size:10px;">
	 			<option value=""> -- Please Select a Medication -- </option>
	 			<%for(MedicationBean med : prodDAO.getNDCodesDAO().getAllNDCodes()){%>
		 			<option value="<%=med.getNDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (med.getNDCode())) %> - <%= StringEscapeUtils.escapeHtml("" + (med.getDescription())) %></option>
		 						 			
	 			<%}%>
	 		</select>
	 	</td>
	 	<td align=center>
	 		<input type="text" name="dosage" id="dosage" maxlength="6" style="width: 50px;"> mg
	 	</td>
	 	<td align=center colspan=2>
	 		<input type="text" name="startDate" id="startDate" style="width: 80px;" 
	 			onclick="displayDatePicker('startDate');" 
	 			onselect="displayDatePicker('startDate');"
	 			value="<%= StringEscapeUtils.escapeHtml("" + (new SimpleDateFormat("MM/dd/yyyy").format(new Date()))) %>"> 
	 		to 
			<input type="text" name="endDate" id="endDate" style="width: 80px;"
				onclick="displayDatePicker('endDate');" 
	 			onselect="displayDatePicker('endDate');"
	 			value="<%= StringEscapeUtils.escapeHtml("" + (new SimpleDateFormat("MM/dd/yyyy").format(new Date()))) %>">
	 	</td>
	 	<td align=center>
	 		<input type="text" name="instructions" id="instructions" value="-- Instructions --" maxlength=500>
	 	</td>
	 	<td>
		 	<input type="button" id="addprescription" onclick="setVar()" value="Add Prescription">
	 	</td>
	 </tr>
</table>
<%
if (!("".equals(warning) )){ %>
<br/>
	<div style="background-color:yellow;color:black" align="center"><%= StringEscapeUtils.escapeHtml("" + (warning )) %></div>
 	<div style="background-color:yellow">
	<select multiple name="overrideReasonCode" title="To select multiple reasons use Control+Click. (Or if on a Mac, Command+Click)" id="overrideReasonCode" style="font-size:10px;">
		<%for(OverrideReasonBean bean : prodDAO.getORCodesDAO().getAllORCodes()){%>
			<option value="<%=bean.getORCode()%>"><%= StringEscapeUtils.escapeHtml("" + (bean.getORCode())) %> - <%= StringEscapeUtils.escapeHtml("" + (bean.getDescription())) %></option>
						 			
		<%}%>
	</select>
	</div>
	<div style="background-color:yellow">
	Other reasons:<br/>
	<textarea name="overrideComment" id="overrideComment" ROWS=5 COLS=40" maxlength=210"></textarea>
	
	</div>
	<div style="background-color:yellow" align="center"><input type="button" onclick="presCont()" value="Continue" name="continue" id="continue"/>
	<input type="button" onclick="location.href='/iTrust/auth/hcp-uap/editPHR.jsp'" value="Cancel" name="cancel" id="cancel"/>
	</div>
	<BR>
	
<%}; %>


</div>
<br /><br />
<div align=center>
	<table class="fTable" align="center">
			<tr>
				<th colspan="5">Laboratory Procedures</th>
			</tr>
			<tr class="subHeader">
				<td>LOINC Code</td>
				<td>Status</td>
				<td>Commentary</td>
				<td>Results</td>
				<td style="width: 60px;">Updated Date</td>
			</tr>
			<%if(lpBeans.size()==0){ %>
			<tr>
				<td colspan="5" style="text-align: center;">No Laboratory Procedures on	record</td>
			</tr>
			<%} else { %>
			<%for(LabProcedureBean labproc : lpBeans){ %>
			<tr>
				<td align=center><%= StringEscapeUtils.escapeHtml("" + (labproc.getLoinc())) %></td>
				<td align=center><%= StringEscapeUtils.escapeHtml("" + (labproc.getStatus())) %></td>
				<td align=center><%= StringEscapeUtils.escapeHtml("" + (labproc.getCommentary())) %></td>
				<td align=center><%= StringEscapeUtils.escapeHtml("" + (labproc.getResults())) %></td>
				<td align=center><%= StringEscapeUtils.escapeHtml("" + (labproc.getTimestamp())) %></td>
				<td ><a
					href="javascript:removeID('removeLabProcID','<%= StringEscapeUtils.escapeHtml("" + (labproc.getProcedureID())) %>');">Remove</a></td>
			</tr>
			<%} %>
			<%} %>
			<tr>
				<th colspan="5" style="text-align: center;">New</th>
			</tr>
			<tr>
				<td colspan="5" align="center">
					<select name="addLabProcID"	style="font-size: 10px;">
						<option value="">-- Please Select a Procedure --</option>
							<% for(LOINCbean loinc : prodDAO.getLOINCDAO().getAllLOINC()) { %>
						<option value="<%=loinc.getLabProcedureCode()%>"> <%= StringEscapeUtils.escapeHtml("" + (loinc.getLabProcedureCode())) %>
					 		- <%= StringEscapeUtils.escapeHtml("" + (loinc.getComponent())) %> - <%= StringEscapeUtils.escapeHtml("" + (loinc.getKindOfProperty())) %> - <%= StringEscapeUtils.escapeHtml("" + (loinc.getTimeAspect())) %>
					 		- <%= StringEscapeUtils.escapeHtml("" + (loinc.getSystem())) %> - <%= StringEscapeUtils.escapeHtml("" + (loinc.getScaleType())) %> 
					 		- <%= StringEscapeUtils.escapeHtml("" + (loinc.getMethodType())) %></option>
							<% } %>
					</select>
					<input	type="submit" name="addLP" value="Add Lab Procedure" >
				</td>
			</tr>
		</table>
	</div>
<br /><br />
<div align=center>
	<div style="display: inline-table; margin-right:10px;">
		<table class="fTable" align="center">
			<tr>
				<th colspan="3">Diagnoses</th>
			</tr>
			<tr class="subHeader">
				<td>ICD Code</td>
				<td>Description</td>
				<td style="width: 60px;">Action</td>
			</tr>

			<%if(ov.getDiagnoses().size()==0){ %>
			<tr>
				<td  colspan="3" style="text-align: center;">No Diagnoses on record</td>
			</tr>
			<%} else { 
					for(DiagnosisBean d : ov.getDiagnoses()) { %>
			<tr>
				<td align=center><%= StringEscapeUtils.escapeHtml("" + (d.getICDCode())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (d.getDescription())) %></td>
				<td ><a
					href="javascript:removeID('removeDiagID','<%= StringEscapeUtils.escapeHtml("" + (d.getOvDiagnosisID())) %>');">Remove</a></td>
			</tr>
			<%		}
				}%>
			<tr>
				<th colspan="3" style="text-align: center;">New</th>
			</tr>
			<tr>
				<td colspan="3" align=center><select name="addDiagID" style="font-size:10">
					<option value="">-- None Selected --</option>
					<%for(DiagnosisBean diag : prodDAO.getICDCodesDAO().getAllICDCodes()) { %>
					<option value="<%=diag.getICDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (diag.getICDCode())) %>
					- <%= StringEscapeUtils.escapeHtml("" + (diag.getDescription())) %></option>
					<%}%>
					</select>
					<input type="submit" value="Add Diagnosis">
				</td>
			</tr>
		</table>
	</div>
	<div style="display: inline-table; margin-right:10px;">
		<table class="fTable" align="center">
			<tr>
				<th colspan="3">Procedures</th>
			</tr>
			<tr class="subHeader">
				<td>CPT Code</td>
				<td>Description</td>
				<td style="width: 60px;">Action</td>
			</tr>
			<% if (0 == ov.getProcedures().size()) { %>
			<tr>
				<td colspan="3" style="text-align: center;">No Procedures on record</td>
			</tr>
			<% } 
			   else { %>
			<% for (ProcedureBean proc : ov.getProcedures()) { 
				if (null == proc.getAttribute() || !proc.getAttribute().equals("immunization")) {%>
			<tr>
				<td align="center"><%= StringEscapeUtils.escapeHtml("" + (proc.getCPTCode())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (proc.getDescription())) %></td>
				<td ><a href="javascript:removeID('removeProcID','<%= StringEscapeUtils.escapeHtml("" + (proc.getOvProcedureID())) %>');">Remove</a></td>
			</tr>
			<% } } } %>
			<tr>
				<th colspan="3" style="text-align: center;">New</th>
			</tr>
			<tr>
				<td colspan="3" align="center">
					<select name="addProcID" style="font-size: 10px;">
						<option value="">-- Please Select a Procedure --</option>
						<% for (ProcedureBean proc : prodDAO.getCPTCodesDAO().getAllCPTCodes()) {
							if (null == proc.getAttribute() || !proc.getAttribute().equals("immunization")) { %>
						<option value="<%=proc.getCPTCode() %>"><%= StringEscapeUtils.escapeHtml("" + (proc.getCPTCode() )) %> - <%= StringEscapeUtils.escapeHtml("" + (proc.getDescription() )) %></option>
						<% } } %>
					</select>
					<input type="submit" name="addP" value="Add Procedure" >
				</td>
			</tr>
		</table>
	</div>
	<div style="display: inline-table;">
		<table class="fTable" align="center">
			<tr>
				<th colspan="3">Immunizations</th>
			</tr>
			<tr class="subHeader">
				<td>CPT Code</td>
				<td>Description</td>
				<td style="width: 60px;">Action</td>
			</tr>
			<% if (0 == ov.getProcedures().size()) { %>
			<tr>
				<td colspan="3" style="text-align: center;">No immunizations on record</td>
			</tr>
			<% } 
			   else { %>
			<%	for (ProcedureBean proc : ov.getProcedures()) { 
					if (null != proc.getAttribute() && proc.getAttribute().equals("immunization")) { %>
			<tr>
				<td align="center"><%= StringEscapeUtils.escapeHtml("" + (proc.getCPTCode())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (proc.getDescription())) %></td>
				<td ><a href="javascript:removeID('removeImmunizationID','<%= StringEscapeUtils.escapeHtml("" + (proc.getOvProcedureID())) %>');">Remove</a></td>
			</tr>
			<% } } } %>
			<tr >
				<th colspan="3" style="text-align: center;">New</th>
			</tr>
			<tr>
				<td colspan="3" align="center">
					<select name="addImmunizationID" style="font-size: 10px;">
						<option value="">-- Please Select a Procedure --</option>
						<% for (ProcedureBean proc : prodDAO.getCPTCodesDAO().getImmunizationCPTCodes()) {%>
							<option value="<%=proc.getCPTCode()%>"><%= StringEscapeUtils.escapeHtml("" + (proc.getCPTCode())) %> - <%= StringEscapeUtils.escapeHtml("" + (proc.getDescription())) %></option>
						<% } %>
					</select>
					<input type="submit" name="addImmu" value="Add Immunization" >
				</td>
			</tr>
		</table>
	</div>
</div>

<br /><br /><br />
<itrust:patientNav />
<br />

</form>

<%@include file="/footer.jsp" %>
