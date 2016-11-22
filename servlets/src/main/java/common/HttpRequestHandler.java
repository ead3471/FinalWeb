package common;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Created by Freemind on 2016-11-18.
 */
public interface HttpRequestHandler {
    default Optional<Integer> getIntParameterAsOptional(HttpServletRequest request, String parameterName) {
        return Optional.ofNullable(request.getParameter(parameterName)).flatMap(id->{
            try {
                return Optional.of(Integer.parseInt(id));
            } catch (NumberFormatException ex) {
                return Optional.empty();
            }
        });
    }

}
