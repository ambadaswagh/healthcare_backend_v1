package com.healthcare.api.auth;

import java.io.IOException;

import static com.healthcare.api.common.HealthcareConstants.*;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.healthcare.exception.ApplicationException;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.response.Response.ResultCode;
import com.healthcare.service.AdminService;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AdminService adminService;

	@Autowired
	private AuthTokenUtil tokenUtil;

	public static String tokenHeader = "token";

	/**
	 * Do filter all request, if any request dont have token => though error =>
	 * Will enable it later when al API apply secure
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		
		final String path = request.getServletPath();
		if (path.startsWith("/api")) {
			
			if (isAuthApi(request, response, chain, path)) {
				return;
			}

			// If preflight request 
			if (request.getMethod().equals("OPTIONS")) {
				setCorsHeaders(response);
				response.setStatus(HttpServletResponse.SC_ACCEPTED);
				return;
			}
			
			String authToken = request.getHeader(tokenHeader);
			authToken = authToken == null ? request.getParameter(tokenHeader) : authToken;
			
			if (authToken == null ) {
				throw new ApplicationException(ResultCode.INVALID_TOKEN,"Invalid token presented for url: " + path);
			}

			// authToken.startsWith("Bearer ")
			String username = tokenUtil.getUsernameFromToken(authToken);

			if (username == null ) {
				throw new ApplicationException(ResultCode.INVALID_TOKEN,"Invalid token presented for url: " + path);
			}

			// logger.info("checking authentication user " + username);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				// It is not compelling necessary to load the use details from the
				// database. You could also store the information
				// in the token and read it from it. It's up to you ;)
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
				// For simple validation it is completely sufficient to just check
				// the token integrity. You don't have to call
				// the database compellingly. Again it's up to you ;)
				if (tokenUtil.validateToken(authToken, userDetails)) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					// logger.info("authenticated user " + username + ", setting
					// security context");
					final Admin admin = adminService.getUser(username);

					if (admin != null) {
						request.setAttribute(AUTHENTICATED_ADMIN, admin);
					}

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
	
			setCorsHeaders(response);
		}
		chain.doFilter(request, response);
	}

	private boolean isAuthApi(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			final String path) throws IOException, ServletException {
		
		if (path.contains("/api/auth") || path.contains("/api/user/auth/login")
				|| path.contains("/api/auth/logout") || path.contains("/api/user/auth/logout")
				|| path.contains("/api/password")) {
			setCorsHeaders(response);
			chain.doFilter(request, response);
			return true;
		}
		return false;
	}

	private void setCorsHeaders(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT, HEAD");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers",
				"Content-Type, x-requested-with, X-Custom-Header, token, origin, accept, authorization");
	}
}