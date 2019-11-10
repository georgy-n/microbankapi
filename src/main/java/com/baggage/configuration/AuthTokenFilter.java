package com.baggage.configuration;

import com.baggage.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import static com.baggage.utils.Constants.AUTH_HEADER_NAME;

public class AuthTokenFilter extends GenericFilterBean {

	private UserDetailsService customUserDetailsService;

	public AuthTokenFilter(UserDetailsService userDetailsService) {
		this.customUserDetailsService = userDetailsService;
	}

	private Logger log = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
			String authToken = httpServletRequest.getHeader(AUTH_HEADER_NAME);
			log.info("AUTH TOKEN " + authToken);
			if (StringUtils.hasText(authToken)) {
				String username = TokenUtil.getUserNameFromToken(authToken);

				UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

				if (TokenUtil.validateToken(authToken, userDetails)) {
					log.info("TOKEN is good");

					UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,
							userDetails.getPassword(), userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(token);
					log.info(SecurityContextHolder.getContext().toString());
				} else {
					log.info("TOKEN is BAD");
					SecurityContextHolder.clearContext();
				}

			} else SecurityContextHolder.clearContext();
			filterChain.doFilter(servletRequest, servletResponse);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
