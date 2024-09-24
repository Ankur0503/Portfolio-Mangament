package ideas.capstone_pm.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.exception.userexpcetions.InvalidCredentialException;
import ideas.capstone_pm.exception.userexpcetions.TokenExpiredException;
import ideas.capstone_pm.service.ApplicationUserDetailsService;
import ideas.capstone_pm.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private ApplicationUserDetailsService applicationUserDetailsService;
    @Autowired
    JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        HttpServletResponse res = response;
        HttpServletRequest req = request;
        Map<String, String> map = new HashMap<>();
        String originHeader = request.getHeader("origin");
        res.setHeader("Access-Control-Allow-Origin", originHeader);
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        res.setHeader("Access-Control-Max-Age", "3600");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
           jwt = authorizationHeader.substring(7);
           try {
               username = jwtUtil.extractUsername(jwt);
           }
           catch (TokenExpiredException e) {
               response.setStatus(HttpServletResponse.SC_FORBIDDEN);

               Map<String, String> errorResponse = new HashMap<>();
               errorResponse.put("error", "Token expired");
               errorResponse.put("message", e.getMessage());

               ObjectMapper objectMapper = new ObjectMapper();
               String jsonResponse = objectMapper.writeValueAsString(errorResponse);

               response.setContentType("application/json");
               response.getWriter().write(jsonResponse);
               return ;
           }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.applicationUserDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(req, res);
        }
    }
}
