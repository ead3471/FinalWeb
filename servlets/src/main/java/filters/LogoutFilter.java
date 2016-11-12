package filters;

import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Created by Freemind on 2016-11-11.
 */
@SuppressWarnings("Duplicates")
public class LogoutFilter extends HttpFilter {

    private static final String DEFAULT_AFTER_LOGOUT_PAGE = "/index.jsp";
    private final static Logger logger = LogManager.getLogger(AuthFilter.class);

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession();
        User sessionUser=(User)request.getSession().getAttribute("user");
        if (sessionUser != null) {
            logger.debug("Logout user " + sessionUser.getLogin());
            session.removeAttribute("user");
            session.removeAttribute("userName");
            session.invalidate();
        }
        request.getRequestDispatcher(DEFAULT_AFTER_LOGOUT_PAGE).forward(request, response);
    }


}
