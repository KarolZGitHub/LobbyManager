package pl.coderslab.lobbymanager.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.coderslab.lobbymanager.entity.User;
import pl.coderslab.lobbymanager.repository.UserRepository;

import java.io.IOException;
import java.util.Optional;

@Component
@Order(2)
@RequiredArgsConstructor
public class BanFilter implements Filter {
    private final UserRepository userRepository;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUserName(authentication.getName());
        if (user.isPresent()) {
            if (!user.get().isActive()) {
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                if (!httpServletResponse.isCommitted() && !isBannedPage(request)) {
                    httpServletResponse.sendRedirect("/banned");
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isBannedPage(ServletRequest request) {
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        return requestURI.equals("/banned");
    }

    public void destroy() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
