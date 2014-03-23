<%@ page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>Guestbook (jsp)</title>
    </head>
    <body>
        <h1>Guestbook (jsp)</h1>
        <p><code>jsp/index.jsp</code> backed by 
            <code>PostServlet.java</code> servlet</p>
            <c:url var="htmlHref" value="/index.html" />
            <c:url var="jsfHref" value="/jsf/index.xhtml" />
            <c:url var="primefacesHref" value="/primefaces/index.xhtml" />
        <p><a href="${fn:escapeXml(htmlHref)}">html</a>
            | <b>jsp</b>
            | <a href="${fn:escapeXml(jsfHref)}">jsf</a>
            | <a href="${fn:escapeXml(primefacesHref)}">primefaces</a></p>

        <c:url var="formAction" value="/PostServlet" />
        <form action="${fn:escapeXml(formAction)}" method="post">
            <ul>
                <c:if test="${not empty message}">
                    <li><c:out value="${message}" /></li>
                </c:if>
                <c:forEach items="${validationMessages}" var="validationMessage">
                    <li><c:out value="${validationMessage}" /></li>
                </c:forEach>
            </ul>
            <p>
                <label for="author">Author:</label><br />
                <input id="author" name="author" value="${fn:escapeXml(currentPost.author)}"/>
            </p>
            <p>
                <label for="message">Message:</label><br />
                <textarea id="message" name="message"><c:out value="${currentPost.message}"/></textarea>
            </p>
            <p>
                <button type="submit" name="button" value="create">Create post</button>
            </p>

            <hr />

            <p>
                <label for="word">Word:</label><br />
                <input id="word" name="word" value="${fn:escapeXml(word)}"/>
            </p>
            <p>
                <button type="submit" name="button" value="deleteByWord">Delete posts containing word</button>
            </p>

            <hr />

            <table border="1">
                <thead>
                    <tr>
                        <th>Author</th>
                        <th>Message</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${posts}" var="post">
                        <tr>
                            <td><c:out value="${post.author}"/></td>
                            <td><c:out value="${post.message}"/></td>
                            <td><button type="submit" name="button" value="delete.${fn:escapeXml(post.id)}">Delete post</button>
                                <c:url var="updateHref" value="/PostUpdateServlet">
                                    <c:param name="post" value="${post.id}" />
                                </c:url>
                                <a href="${fn:escapeXml(updateHref)}">Update post</a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <hr />

            <ul>
                <c:forEach items="${parents}" var="parent">
                    <li>Parent: <c:out value="${parent.name}" />
                        <ul>
                            <c:forEach items="${parent.children}" var="child">
                                <li>Child: <c:out value="${child.name}" /></li>
                                </c:forEach>
                        </ul>
                    </li>
                </c:forEach>
            </ul>

        </form>
    </body>
</html>
