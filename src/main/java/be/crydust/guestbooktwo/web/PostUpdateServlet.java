package be.crydust.guestbooktwo.web;

import be.crydust.guestbooktwo.ejb.PostBoundary;
import be.crydust.guestbooktwo.entities.Post;
import java.io.IOException;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kristof
 */
public class PostUpdateServlet extends HttpServlet {

    private static final long serialVersionUID = 42L;

    @Inject
    PostBoundary postBoundary;
    @Inject
    PostConverter postConverter;

    Post currentPost = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        currentPost = (Post) postConverter.getAsObject(null, null,
                ServletUtil.emptyToNull(request.getParameter("post")));
        request.setAttribute("currentPost", currentPost);
        request
                .getRequestDispatcher("/jsp/update.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        currentPost = (Post) postConverter
                .getAsObject(null, null, ServletUtil.emptyToNull(request.getParameter("post")));
        currentPost.setAuthor(ServletUtil.emptyToNull(request.getParameter("author")));
        currentPost.setMessage(ServletUtil.emptyToNull(request.getParameter("message")));
        try {
            postBoundary.update(currentPost);
            ServletUtil.redirect(request, response,
                    "PostServlet",
                    "message", "Post updated successfully");
        } catch (EJBException e) {
            request.setAttribute("currentPost", currentPost);
            request.setAttribute("validationMessages", ServletUtil.convertToValidationMessages(e));
            request
                    .getRequestDispatcher("/jsp/update.jsp")
                    .forward(request, response);
        }
    }

}
