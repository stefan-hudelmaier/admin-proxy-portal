package name.hudelmaier.adminproxyportal

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

class EmailSecurityFilter(private val applicationSettings: ApplicationSettings) : GenericFilterBean() {

	override fun doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain) {
		val email = getEmail()

		if (email == "hudelmaier@gmail.com") {

			logger.debug("Access not allowed for email $email")

			SecurityContextHolder.clearContext()
		}

		filterChain.doFilter(request, response);
	}

	private fun getEmail() =
			(SecurityContextHolder.getContext().authentication?.principal as? DefaultOidcUser?)?.userInfo?.claims?.get("email")
}
