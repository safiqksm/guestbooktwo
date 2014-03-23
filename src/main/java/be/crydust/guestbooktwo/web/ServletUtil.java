package be.crydust.guestbooktwo.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author kristof
 */
public final class ServletUtil {

    private ServletUtil() {
    }

    static String emptyToNull(String s) {
        if (s != null && s.isEmpty()) {
            return null;
        }
        return s;
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
                    validationMessages.add(String.format("%s: %s", violationPath, violationMessage));
                }
            }
        } else if (cause != null) {
            validationMessages.add(cause.getMessage());
        }
        return validationMessages;
    }

    private static String urlencode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            // UTF-8 is supported, ignore this impossible exception.
        }
        return "";
    }

    static void redirect(HttpServletRequest request, HttpServletResponse response, String servlet, String... args) throws IOException {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("odd parameter number can't be converted to map");
        }
        Map<String, String> params = new LinkedHashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            String key = args[i];
            String value = args[i + 1];
            params.put(key, value);
        }
        redirect(request, response, servlet, params);
    }

    static void redirect(HttpServletRequest request, HttpServletResponse response, String servlet, Map<String, String> params) throws IOException {
        StringBuilder url = new StringBuilder()
                .append(request.getContextPath())
                .append('/')
                .append(urlencode(servlet));
        if (!params.isEmpty()) {
            url.append('?');
            for (Map.Entry<String, String> param : params.entrySet()) {
                String key = param.getKey();
                String value = param.getValue();
                url
                        .append(urlencode(key))
                        .append('=')
                        .append(urlencode(value))
                        .append('&');
            }
            url.setLength(url.length() - 1);
        }
        response.sendRedirect(response.encodeRedirectURL(url.toString()));
    }
}
