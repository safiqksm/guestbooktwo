package be.crydust.guestbooktwo.web;

import be.crydust.guestbooktwo.entities.Post;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

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

    void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("currentPost", postBacking.getCurrentPost());
        request.setAttribute("word", postBacking.getWord());
        request.setAttribute("posts", postBacking.getPosts());
        request.setAttribute("parents", familyBacking.getParents());
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
        postBacking.setCurrentPost(new Post(
                emptyToNull(request.getParameter("author")),
                emptyToNull(request.getParameter("message"))));
        postBacking.setWord(
                emptyToNull(request.getParameter("word")));
        postBacking.init();
        try {
            performAction(request.getParameter("button"));
        } catch (EJBException e) {
            request.setAttribute("validationMessages", convertToValidationMessages(e));
        }
        doRequest(request, response);
    }

    // Helpers
    static String emptyToNull(String s) {
        if (s != null && s.isEmpty()) {
            return null;
        }
        return s;
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

    static List<String> convertToValidationMessages(EJBException e) {
        List<String> validationMessages = new ArrayList<>();
        Throwable cause = e.getCause();
        if (cause instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) cause).getConstraintViolations();
            for (ConstraintViolation<?> violation : constraintViolations) {
                String violationPath = violation.getPropertyPath().toString();
                String violationMessage = violation.getMessage();
                if ("".equals(violationPath)) {
                    validationMessages.add(violationMessage);
                } else {
                    validationMessages.add(String.format(
                            "%s: %s", violationPath, violationMessage));
                }
            }
        } else if (cause != null) {
            validationMessages.add(cause.getMessage());
        }
        return validationMessages;
    }
}
