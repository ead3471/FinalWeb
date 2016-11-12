package filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

public class CharsetFilter implements Filter {
    private String serverRequestsEncoding;
    private final static String DEFAULT_ENCODING="UTF-8";



    public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, UnsupportedEncodingException {

        request.setCharacterEncoding(serverRequestsEncoding);
        response.setCharacterEncoding(serverRequestsEncoding);
        chain.doFilter(request, response);
    }

    public void init(FilterConfig fConfig) throws ServletException {
        serverRequestsEncoding = Optional.ofNullable(fConfig.getInitParameter("characterEncoding")).orElse(DEFAULT_ENCODING);
    }
}
