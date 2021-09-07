<%@ page import="java.util.Date,java.util.Calendar" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% //postavlja na zadano ako nema atributa
	String picked = (String) session.getAttribute("pickedBgCol");
	if(picked == null) {
		picked = "#FFFFFF";
		session.setAttribute("pickedBgCol", picked);
	}
	
	String errorName = (String) session.getAttribute("error_info");
%>
<html>
<body bgcolor="<%=picked%>">
<p>Error occured while performing: <%=errorName%>
</body>
</html>