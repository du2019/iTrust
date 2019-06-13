<%@page contentType="text/css" %>

<%@page import="edu.ncsu.csc.itrust.action.ViewHelperAction"%>

<%@include file="/global.jsp" %>

<%
String primaryColor = "C7C7C7"; //prefsBean.getThemeColor(); //"A1BFFF"; //
String secondaryColor = "FFFFFF";

//Calculate the inverse of the secondary color
int r = 255 - Integer.parseInt(secondaryColor.substring(0,2), 16);
int g = 255 - Integer.parseInt(secondaryColor.substring(2,4), 16);
int b = 255 - Integer.parseInt(secondaryColor.substring(4,6), 16);
String rStr = Integer.toHexString(r);
String gStr = Integer.toHexString(r);
String bStr = Integer.toHexString(r);
rStr = rStr.length() == 1 ? "0" + rStr : rStr;
gStr = gStr.length() == 1 ? "0" + gStr : gStr;
bStr = bStr.length() == 1 ? "0" + bStr : bStr;
String secondaryColorInv = rStr + gStr + bStr;

String backgroundColor = "#F0F0F0"; //ViewHelperAction.calculateColor(primaryColor, secondaryColor, 0.75);
String linkColor = ViewHelperAction.calculateColor(primaryColor, secondaryColorInv, 0.5);
String borderColor = ViewHelperAction.calculateColor(primaryColor, secondaryColor, 0.5);
String calendarHeadingColor = ViewHelperAction.calculateColor(primaryColor, secondaryColor, 0.1);
String calendarBgColor = ViewHelperAction.calculateColor(primaryColor, secondaryColor, 0.875);
String calendarTodayColor = ViewHelperAction.calculateColor(primaryColor, secondaryColor, 0.425);
String formTitleColor = ViewHelperAction.calculateColor(primaryColor, secondaryColorInv, 0.42);
String formBackgroundColor = ViewHelperAction.calculateColor(primaryColor, secondaryColor, 0.9);
String formHeadingBgColor = ViewHelperAction.calculateColor(primaryColor, secondaryColor, 0.4);
String formHeadingBgColor2 = ViewHelperAction.calculateColor(primaryColor, secondaryColor, 0.6);
%>

/* General Content classes */
body {
	margin: 0px;
	padding: 0px;
	background-color: <%= backgroundColor %>;
	color: #<%= formTitleColor %>;
}

a {
	color: #<%= linkColor %>;
	font-size: 14px;
}

input:focus {
	border: 2px solid #CB9949;
}

fieldset {
	background-color: #<%= backgroundColor %>;
	border: 1px solid #<%= borderColor %>;
}

legend {
	letter-spacing: 0.5pt;
	color: #<%= formTitleColor %>;
	font-size: 140%;
}

.zebraOn {
	padding:5px;
}

.zebraOff {
	background-color:  #<%= borderColor %>;
	padding:5px;
}

.contentBlock {
	font-family: verdana;
	font-size: 0.8em;
	padding-left: 10px;
}
.contentBlock a{
	font-family: verdana;
	font-size: 0.9em;
	padding-left: 10px;
	color: #<%= linkColor %>;
}

.subheading {
	font-family: verdana;
	font-size: 0.8em;
	font-weight: bold;
	padding-bottom: 10px;
	padding-left: 5px;
}

.menuItem {
	padding: 0em;
	margin: 0em;
	width: 100%;
}

.menuItem:hover {
	background-color: #<%= formBackgroundColor %>;
}


/* iTrust Header */
#title {
	font-size: 25pt;
	float: left;
	margin-right: 1em;
}

#iTrustHeader {
	width: 100%;
	height: 125px;
	padding: 0em;
	background: #<%=primaryColor %>;
	vertical-align: bottom;
}

/* Main navigation formatting */
#iTrustLogo {
	width: 40%;
	float: left;
	padding-top: 10px;
}

#iTrustUserInfo {
	width: 60%;
	float: right;
	text-align: right;
	valign: top;
}

#iTrustSelectedPatient {
	width: 100%;
	float: right;
	text-align: right;
	background: #<%=primaryColor %>;
	border-top: 2px solid black;
}

.selectedPatient {
	color:black;
}

.selectedPatient a{
	color:black;
}

#iTrustNavLink {
	color: <%= linkColor %>;
	font-size: 11pt;
	text-decoration: none;
	vertical-align:bottom;
}

/* iTrust Menu */
#iTrustMenu {
	margin: 0em;
	float: left;
	position: relative;
	width: 12em;
	margin-right: 1em;
	height: 100%;
	background: #<%=primaryColor %>;	
}

#iTrustMenu a {
	color: <%= linkColor %>;
	font-weight: normal;
	font-size: 10.5pt;
	text-decoration: none;
}


.iTrustMenuContents {
	min-height: 80px;
}

#iTrustMain {
	float: left;
	position: relative;
	margin: 0em;
	width: 100%;
	min-width: 800px;
}

/* Content Page */

#iTrustPage {
	position: relative;
	width: 100% - 13em;
	margin: 0em;
	margin-left: 12em;
	height: 100%;
}

#leftBorder, #rightBorder {
	float: left;
	min-width: 4em;
	min-height: 5em;
	height: 100%;
}

#rightBorder {
	float: right;
}

#iTrustContent {
	margin-right: 1em;
	padding: 1em;
	position: relative;
	width: 100%-1em;
	height: 100%;
	-moz-border-radius: 0px 0px 15px 0px;
	border-radius: 0px 0px 15px 0px;
}

#iTrustFooter {
	float: left;
	position: relative;
	border-top: 1px solid #336699;
	margin-top: 1em;
	height: 2em;
	width: 100%;
	min-width: 800px;
	background-color: #<%=primaryColor%>;
}


/* Formated table stuff */
.fTable {
	background-color: #<%= formBackgroundColor %>;
	border: 1px solid black;
	border-collapse: collapse;
}

.fTable tr {
	border: 1px solid black;
}

.fTable tr th {
	border: 1px solid black;
	background-color: #<%= formHeadingBgColor %>;
	font-size: 16px;
	padding-left: 4px;
	padding-right: 4px;
	white-space: nowrap;
}

.fTable tr td {
	border: 1px solid black;
	font-size: 16px;
	padding-left: 4px;
	padding-right: 4px;
}

.fTable .subHeader td {
	color: #<%= formTitleColor %>;
	background: #<%= formHeadingBgColor2%>;
	font-weight: bold;
	text-align: center;
	white-space: nowrap;
}

.fTable .subHeader th {
	background: #<%= formHeadingBgColor2%>;
	font-weight: bold;
	text-align: center;
	white-space: nowrap;
}

.fTable .subHeaderVertical {
	background: #<%= formHeadingBgColor2%>;
	font-weight: bold;
	text-align: left;
}

.iTrustError {
	color: #FF0000;
	font-weight: bold;
}

.iTrustMessage {
	color: #00AA88;
	font-weight: bold;
}

.filterEdit {
	border: 2px solid #292929;
	padding: 10px;
	background-color: silver;	
}

/* Calendar Formatting */
#calendarTable {
	font-family:"Trebuchet MS", Arial, Helvetica, sans-serif;
	border-collapse:collapse;
}

#calendarTable td, #calendarTable th {
	font-size:1em;
	background-color: #<%= "000000"%>;
	border:1px solid #<%= calendarHeadingColor%>;
	padding:3px 7px 2px 7px;
}

#calendarTable tr th {
	font-family:"Trebuchet MS", Arial, Helvetica, sans-serif;
	background-color: #<%= calendarHeadingColor%>;
	color: #<%= secondaryColor%>;
}

#calendarTable tr td {
	height: 100px; 
	min-width: 120px;
	width: 150px;
	background-color: #<%= calendarBgColor%>;
}

#calendarTable tr td div.cell {
	height: 100px;
	min-width: 120px;
	width: 99%;
	vertical-align: top;
	overflow: auto;
}

#calendarTable tr td.today {
	background-color: #<%= calendarTodayColor%>;
}

#calendarTable .calendarEntry {
	padding: 5px 0px 0px 5px;
	font-size: 14px;
}

#calendarTable .conflict {
	font-weight: bold;
}

#calendarTable .calendarEntry a {
	font-size: 12px;
}
/* Transaction Page */

.transactionForm .right{
	text-align: right;
}

.transactionForm .left{
	text-align: left;
}

.transactionForm .center{
	text-align: center;
}

#transactionTable {
	margin-top: 5px;	
}

.transactionChart .yaxis {
	vertical-align: 100px;	
}

#fancyTable {
	font-family:"Trebuchet MS", Arial, Helvetica, sans-serif;
	width:99%;
	border-collapse:collapse;
}

.fancyTable td, .fancyTable th {
	font-size:1em;
	background-color: #<%= calendarBgColor%>;
	border:1px solid #<%= calendarHeadingColor%>;
	padding:3px 7px 2px 7px;
}

.fancyTable th {
	font-size:1.1em;
	text-align:left;
	padding-top:5px;
	padding-bottom:4px;
	background-color:#<%= calendarHeadingColor%>;
	color:#<%= secondaryColor%>;
}

.fancyTable tr.alt td {
	color:#<%= calendarBgColor%>;
	background-color:#<%= calendarTodayColor%>;
}

/* Menu formatting */
.menu_category {
	padding-bottom: 0.5em;
	padding-left: 1em; 
}
			
.menu_category a {
	font-size: 14px;
	color: black;
	text-decoration: none;
}

.menu_category a:hover {	
	text-decoration: underline;
}

.menu_category span {
	font-size: 16px;
	font-weight: bold;
	color: black;
}

/* These links are only for testing purposes and are indicated such */
a.iTrustTestNavlink {
	color: black;
	text-decoration: none;
}

#UserSearch {
	padding:10px;
}
