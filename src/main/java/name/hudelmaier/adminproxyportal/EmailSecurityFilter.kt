package name.hudelmaier.adminproxyportal

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

class EmailSecurityFilter(private val applicationSettings: ApplicationSettings) : GenericFilterBean() {

	override fun doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain) {

		if (SecurityContextHolder.getContext().authentication is OAuth2AuthenticationToken) {
			val email = getVerifiedEmail()

			if (email == null) {
				logger.debug("No email address could be determined, denying access")
				throw AccessDeniedException("No email address could be determined, denying access")
			}

			if (!applicationSettings.validEmailsForOauth.contains(email)) {
				logger.debug("Access not allowed for email $email")
				throw AccessDeniedException("Email $email not acceptable")
			}
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * Returns email if an OpenID principal is present and email has been verified
	 */
	private fun getVerifiedEmail(): String? {
		val claims = (SecurityContextHolder.getContext().authentication?.principal as? DefaultOidcUser?)?.userInfo?.claims

		if (claims == null) {
			return null
		}

		val email = claims.get("email") as String?
		val emailVerified = claims.get("email_verified") as Boolean?

		if (emailVerified != true) {
			return null
		}

		return email
	}
}
