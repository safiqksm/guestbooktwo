package be.crydust.guestbooktwo.rest;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "VaryAcceptFilter", urlPatterns = {"/rest/*"})
public class VaryAcceptFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // NOOP
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (response instanceof HttpServletResponse) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            if (httpResponse.getHeader("Vary") == null) {
                httpResponse.addHeader("Vary", "Accept");
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // NOOP
    }
}
