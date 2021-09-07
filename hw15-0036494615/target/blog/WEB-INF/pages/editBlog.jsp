<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
import="hr.fer.zemris.java.tecaj_13.web.servlets.util.SessionMethods" import="hr.fer.zemris.java.tecaj_13.web.servlets.util.SessionKeys" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%	boolean isLoggedIn = SessionMethods.isLoggedIn(session);
	String firstName = null;
	String lastName = null;
	if(isLoggedIn) {
		firstName = (String) session.getAttribute(SessionKeys.userFirstName);
		lastName = (String) session.getAttribute(SessionKeys.userLastName);
	}
%>

<html>
<body>
				<a href="${pageContext.request.contextPath}/index.jsp">Index page</a>

		<div>
			<%=firstName%> <%=lastName %> <a href="${pageContext.request.contextPath}/servleti/logout">Logout</a>
		</div>
	
				
		<h4>Edit blog:</h4>
		<form method="post">
			Title:<br> 
			<input type="text" name="title" value="${blogForm.title}"> <br> 
			Text:<br>
			<textarea name="text">${blogForm.text}</textarea> <br>
			<br>
			<p style="color: red">${blogForm.errorMessage }</p>
			<input type="submit" value="Submit">
		</form>
		<br>
</body>
</html>
