package dev.mednikov.accounting.ledger.shared.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public final class AuthzTokenFilterImpl implements TokenFilter{

    private final AuthzService authzService;

    public AuthzTokenFilterImpl(AuthzService authzService) {
        this.authzService = authzService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String authHeader = request.getHeader("X-APP-AUTHZ-TOKEN");
        if (authHeader != null) {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                AuthorizedUser user = this.authzService.getAuthorizedUser(authHeader);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities()
                );
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }
        chain.doFilter(request, servletResponse);
    }

}
