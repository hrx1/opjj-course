<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
import="hr.fer.zemris.java.tecaj_13.web.servlets.util.SessionMethods" import="hr.fer.zemris.java.tecaj_13.web.servlets.util.SessionKeys"
import="hr.fer.zemris.java.tecaj_13.web.servlets.AuthorHome"
import="hr.fer.zemris.java.tecaj_13.model.BlogUser" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%	boolean isLoggedIn = SessionMethods.isLoggedIn(session);
	String firstName = null;
	String lastName = null;
	
	boolean isAuthor = false;
	if(isLoggedIn) {
		firstName = (String) session.getAttribute(SessionKeys.userFirstName);
		lastName = (String) session.getAttribute(SessionKeys.userLastName);
		long authorID = ((BlogUser) request.getAttribute(AuthorHome.authorKey)).getId();
		isAuthor = session.getAttribute(SessionKeys.userID).equals(authorID);
	}
%>

<html>
<body>
				<a href="${pageContext.request.contextPath}/index.jsp">Index page</a>

	<c:choose>
		<c:when test="<%=isLoggedIn %>">
			<div>
				<%=firstName%> <%=lastName %> <a href="${pageContext.request.contextPath}/servleti/logout">Logout</a>
			</div>
		</c:when>
		<c:otherwise>
			<p>Not logged in</p>]
		</c:otherwise>
		
	</c:choose>
	<h2>Blogs of author "${author.nick}" </h2>
	<c:if test="${empty author.createdBlogs}"> No blogs posted so far... </c:if>
	<ul>
	<c:forEach var="blog" items="${author.createdBlogs}">
		<li><a href="${author.nick}/${blog.id }">${blog.title }</a></li>
	</c:forEach>
	</ul>
	
	<c:if test="<%=isAuthor %>">
		<a href="${author.nick}/new">Create new blog </a>
	</c:if>
</body>
</html>
			