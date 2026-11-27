package com.akuma.tech.auth.config;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.akuma.tech.auth.model.User;
import com.akuma.tech.auth.repository.UserRepository;
import com.akuma.tech.auth.service.JwtService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserRepository userRepository;

	public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
		this.jwtService = jwtService;
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String authorization = request.getHeader("Authorization");
		if (authorization != null && authorization.startsWith("Bearer ")
			&& SecurityContextHolder.getContext().getAuthentication() == null) {
			authenticate(authorization.substring(7));
		}

		filterChain.doFilter(request, response);
	}

	private void authenticate(String token) {
		try {
			String email = jwtService.extractSubject(token);
			Optional<User> user = userRepository.findByEmailAndActiveTrue(email);
			if (user.isEmpty()) {
				return;
			}

			String role = user.get().isAdmin()
				? "ROLE_ADMIN"
				: "ROLE_USER";
			var authentication = new UsernamePasswordAuthenticationToken(
				user.get().getEmail(),
				null,
				List.of(new SimpleGrantedAuthority(role))
			);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (JwtException | IllegalArgumentException exception) {
			SecurityContextHolder.clearContext();
		}
	}
}
