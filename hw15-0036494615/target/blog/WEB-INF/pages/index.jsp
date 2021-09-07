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

	<c:choose>
		<c:when test="<%=isLoggedIn %>">
			<div>
				<%=firstName%> <%=lastName %> <a href="logout">Logout</a>
			</div>
		</c:when>
		<c:otherwise>
			<div>Not logged in.</div>
			<form action="main" method="post">
				Nick:<br> 
				<input type="text" name="nick" value="${loginFormKey.nick}"> <br> 
				Password:<br>
				<input type="password" name="password" value=""> <br> 
				<br>
				<p style="color: red">${loginFormKey.errorMessage }</p>
				<br> <input type="submit" value="Submit">
			</form>
			<br><br>
			<a href="register">Register new user.</a>

		</c:otherwise>
	</c:choose>
	<h2>Authors: </h2>
	
		<c:if test="${empty authorsKey}"> No Authors so far... </c:if>

	
	<ul>
		<c:forEach var="author" items="${authorsKey}">
			 <li><a href="author/${author.nick }">${author.nick}</a></li>
		</c:forEach>
	</ul>
</body>
</html>
