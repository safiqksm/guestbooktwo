package be.crydust.guestbooktwo.web;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.webjars.RequireJS;

/**
 *
 * @author kristof
 */
public class RequireJsServlet extends HttpServlet {

    private static final long serialVersionUID = 42L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ROOT);
//        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        long maxAgeInDays = 14;
        long maxAge = TimeUnit.SECONDS.convert(maxAgeInDays, TimeUnit.DAYS);
        long expires = System.currentTimeMillis()
                + TimeUnit.MILLISECONDS.convert(maxAgeInDays, TimeUnit.DAYS);
//        String expiresAsString = df.format(new Date(expires));

        response.setContentType("text/javascript");

        response.setDateHeader("Expires", expires);
//        response.setHeader("Expires", expiresAsString);
        response.setHeader("Cache-Control", "max-age=" + maxAge);

        response.getWriter().print(RequireJS.getSetupJavaScript(request.getContextPath() + "/webjars/"));
        response.getWriter().print("/*");
        response.getWriter().print(RequireJS.getSetupJson(request.getContextPath() + "/webjars/"));
        response.getWriter().print("*/");
    }

}
