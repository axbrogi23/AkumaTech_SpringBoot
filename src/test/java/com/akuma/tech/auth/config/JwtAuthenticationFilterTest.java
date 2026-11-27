package com.akuma.tech.auth.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.security.core.context.SecurityContextHolder;

import com.akuma.tech.auth.model.User;
import com.akuma.tech.auth.repository.UserRepository;
import com.akuma.tech.auth.service.JwtService;

import io.jsonwebtoken.JwtException;

class JwtAuthenticationFilterTest {

	private final JwtService jwtService = org.mockito.Mockito.mock(JwtService.class);
	private final UserRepository userRepository = org.mockito.Mockito.mock(UserRepository.class);

	@AfterEach
	void clearSecurityContext() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void adminUserReceivesAdminAuthority() throws Exception {
		User admin = user("admin@example.com", true);
		when(jwtService.extractSubject("valid-token")).thenReturn(admin.getEmail());
		when(userRepository.findByEmailAndActiveTrue(admin.getEmail())).thenReturn(Optional.of(admin));

		filter().doFilter(
			requestWithBearerToken("valid-token"),
			new MockHttpServletResponse(),
			new MockFilterChain()
		);

		assertEquals(
			"ROLE_ADMIN",
			SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority()
		);
	}

	@Test
	void regularUserDoesNotReceiveAdminAuthority() throws Exception {
		User regularUser = user("user@example.com", false);
		when(jwtService.extractSubject("valid-token")).thenReturn(regularUser.getEmail());
		when(userRepository.findByEmailAndActiveTrue(regularUser.getEmail())).thenReturn(Optional.of(regularUser));

		filter().doFilter(
			requestWithBearerToken("valid-token"),
			new MockHttpServletResponse(),
			new MockFilterChain()
		);

		assertEquals(
			"ROLE_USER",
			SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority()
		);
	}

	@Test
	void invalidTokenDoesNotAuthenticateRequest() throws Exception {
		when(jwtService.extractSubject("invalid-token")).thenThrow(new JwtException("Invalid token"));

		filter().doFilter(
			requestWithBearerToken("invalid-token"),
			new MockHttpServletResponse(),
			new MockFilterChain()
		);

		assertNull(SecurityContextHolder.getContext().getAuthentication());
	}

	private JwtAuthenticationFilter filter() {
		return new JwtAuthenticationFilter(jwtService, userRepository);
	}

	private MockHttpServletRequest requestWithBearerToken(String token) {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Authorization", "Bearer " + token);
		return request;
	}

	private User user(String email, boolean admin) {
		User user = new User();
		user.setEmail(email);
		user.setActive(true);
		user.setAdmin(admin);
		return user;
	}
}
