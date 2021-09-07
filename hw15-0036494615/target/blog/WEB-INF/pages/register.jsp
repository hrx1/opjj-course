<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="hr.fer.zemris.java.tecaj_13.web.servlets.util.SessionMethods" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%	boolean isLoggedIn = SessionMethods.isLoggedIn(session);
%>

<html>
  <body>
				<a href="${pageContext.request.contextPath}/index.jsp">Index page</a>

  <c:choose>
    <c:when test="<%=isLoggedIn %>">
      <%
      	response.sendRedirect("index.jsp");
      %>
    </c:when>

    <c:otherwise>
    				<br>Not logged in. 
    		<h4>Registration</h4>
		    <form action="register" method="post">
		  First name:<br>
		  <input type="text" name="fn" value="${formKey.name}">
		  <br>
		  Last name:<br>
		  <input type="text" name="ln" value="${formKey.lastName}">
		  <br>
		  Nick:<br>
		  <input type="text" name="nick" value="${formKey.nick}">
		  <br>
		  Email:<br>
		  <input type="text" name="email" value="${formKey.email}">
		  <br>
		    Password:<br>
		  <input type="password" name="password" value="${formKey.password}">
		  <br>
		  <br>
		  <p style="color:red"> ${formKey.errorMessage } </p>
		  <br>
		  <input type="submit" value="Submit">
		</form> 
		    
		    
		    </c:otherwise>
		  </c:choose>

  </body>
</html>
