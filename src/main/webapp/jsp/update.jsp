<%@ page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>Guestbook update post (jsp)</title>
    </head>
    <body>
        <h1>Guestbook update post (jsp)</h1>
        <p><code>jsp/update.jsp</code> backed by 
            <code>PostUpdateServlet.java</code> servlet</p>

        <c:url var="formAction" value="/PostUpdateServlet" />
        <form action="${fn:escapeXml(formAction)}" method="post">
            <c:if test="${not empty validationMessages}">
                <ul>
                    <c:forEach items="${validationMessages}" var="validationMessage">
                        <li><c:out value="${validationMessage}" /></li>
                    </c:forEach>
                </ul>
            </c:if>
            <p>
                <label for="author">Author:</label><br />
                <input id="author" name="author" value="${fn:escapeXml(currentPost.author)}"/>
            </p>
            <p>
                <label for="message">Message:</label><br />
                <textarea id="message" name="message"><c:out value="${currentPost.message}"/></textarea>
            </p>
            <p>
                <input type="hidden" name="post" value="${fn:escapeXml(currentPost.id)}" />
                <button type="submit" name="button" value="update">Update post</button>
                <c:url var="cancelHref" value="/PostServlet" />
                <a href="${fn:escapeXml(cancelHref)}">Cancel</a>
            </p>
        </form>
    </body>
</html>
