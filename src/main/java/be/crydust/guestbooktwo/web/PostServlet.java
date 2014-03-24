package be.crydust.guestbooktwo.web;

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
public class PostServlet extends HttpServlet {

    private static final long serialVersionUID = 42L;

    @Inject
    PostBacking postBacking;
    @Inject
    FamilyBacking familyBacking;
    String message = null;

    void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("currentPost", postBacking.getCurrentPost());
        request.setAttribute("word", postBacking.getWord());
        request.setAttribute("posts", postBacking.getPosts());
        request.setAttribute("parents", familyBacking.getParents());
        request.setAttribute("message", message);
        request
                .getRequestDispatcher("/jsp/index.jsp")
                .forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO use a whitelist for these messages
        message = ServletUtil.emptyToNull(request.getParameter("message"));
        postBacking.init();
        doRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        postBacking.setCurrentPost(new Post(
                ServletUtil.emptyToNull(request.getParameter("author")),
                ServletUtil.emptyToNull(request.getParameter("message"))));
        postBacking.setWord(
                ServletUtil.emptyToNull(request.getParameter("word")));
        postBacking.init();
        try {
            performAction(request.getParameter("button"));
        } catch (EJBException e) {
            request.setAttribute("validationMessages", ServletUtil.convertToValidationMessages(e));
        }
        doRequest(request, response);
    }

    static Long parseId(String actionAndId) {
        if (actionAndId != null && actionAndId.contains(".")) {
            try {
                return Long.valueOf(actionAndId.substring(actionAndId.indexOf(".") + 1), 10);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
        return null;
    }

    static String parseAction(String actionAndId) {
        if (actionAndId != null && actionAndId.contains(".")) {
            return actionAndId.substring(0, actionAndId.indexOf("."));
        }
        return actionAndId;
    }

    void performAction(String actionAndId) {
        switch (parseAction(actionAndId)) {
            case "create":
                postBacking.create();
                break;
            case "delete":
                postBacking.delete(parseId(actionAndId));
                break;
            case "deleteByWord":
                postBacking.deleteByWord();
                break;
            default:
                throw new IllegalArgumentException("unknown action");
        }
    }

}
