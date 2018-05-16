package name.hudelmaier.adminproxyportal

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
class WebSecurityConfig(private val applicationSettings: ApplicationSettings) : WebSecurityConfigurerAdapter() {

	private val logger: Logger = LoggerFactory.getLogger(WebSecurityConfig::class.java)

	override fun configure(http: HttpSecurity) {
		// @formatter:off
		http
				.csrf().disable()
				.headers()
					.frameOptions().disable()
					.and()
				.authorizeRequests()
					.antMatchers("/__portal__/*.css").permitAll()
					.antMatchers("/__portal__/login").permitAll()
					.antMatchers("/__portal__/handleLogin").permitAll()
					.antMatchers("/**").hasRole("USER")
					.and()
				.formLogin()
					.loginPage("/__portal__/login")
					.loginProcessingUrl("/__portal__/handleLogin")
					.defaultSuccessUrl("/__portal__/index.html")
					.and()
				.logout()
					.logoutUrl("/__portal__/logout")
		// @formatter:on

		if (applicationSettings.oauth != null) {

			logger.debug("Oauth configured, enabling it")

			// @formatter:off
			http
					.addFilterAt(emailSecurityFilter(), OAuth2LoginAuthenticationFilter::class.java)
					.oauth2Login()
						.loginPage("/__portal__/login")
						.authorizationEndpoint()
							.baseUri("/__portal__/oauth2/authorization")
							.and()
						.redirectionEndpoint()
							.baseUri("/__portal__/login/oauth2/client/*")
							.and()
						.defaultSuccessUrl("/__portal__/index.html", true)
			// @formatter:on
		}
	}

	@Bean
	fun emailSecurityFilter() = EmailSecurityFilter(applicationSettings)

	@Autowired
	fun configureGlobal(auth: AuthenticationManagerBuilder) {
		applicationSettings.users.forEach {
			auth
					.inMemoryAuthentication()
					.passwordEncoder(NoOpPasswordEncoder.getInstance())
					.withUser(it.username)
					.password(it.password)
					.roles("USER") }
	}

}
