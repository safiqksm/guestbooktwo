package be.crydust.guestbooktwo.web;

import java.io.IOException;
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

    @Inject
    PostBacking postBacking;

    void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("author", postBacking.getAuthor());
        request.setAttribute("message", postBacking.getMessage());
        request.setAttribute("word", postBacking.getWord());
        request.setAttribute("posts", postBacking.getPosts());
        request
                .getRequestDispatcher("/WEB-INF/index.jsp")
                .forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        postBacking.init();
        doRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        postBacking.setAuthor((String) request.getParameter("author"));
        postBacking.setMessage((String) request.getParameter("message"));
        postBacking.setWord((String) request.getParameter("word"));
        postBacking.init();
        performAction((String) request.getParameter("button"));
        doRequest(request, response);
    }

    // Helpers
    Long parseId(String actionAndId) {
        if (actionAndId != null && actionAndId.contains(".")) {
            try {
                return Long.valueOf(actionAndId.substring(actionAndId.indexOf(".") + 1), 10);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
        return null;
    }

    String parseAction(String actionAndId) {
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
