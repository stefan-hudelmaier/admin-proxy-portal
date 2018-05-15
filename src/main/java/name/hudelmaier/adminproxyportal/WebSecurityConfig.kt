package name.hudelmaier.adminproxyportal

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.NoOpPasswordEncoder

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
class WebSecurityConfig(private val applicationSettings: ApplicationSettings) : WebSecurityConfigurerAdapter() {

	override fun configure(http: HttpSecurity) {
		//configureForLoginForm()
		configureForOAuth2()
	}

	private fun configureForOAuth2() {

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
				.oauth2Login()
					.loginPage("/__portal__/login")
					.authorizationEndpoint()
						.baseUri("/__portal__/oauth2/authorization")
						.and()
					.redirectionEndpoint()
						.baseUri("/__portal__/login/oauth2/client/*")
						.and()
					.defaultSuccessUrl("/__portal__/index.html", true)
					.and()
				.logout()
					.logoutUrl("/__portal__/logout")

		// @formatter:on

	}

	private fun configureForLoginForm() {

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

	}

	@Autowired
	fun configureGlobal(auth: AuthenticationManagerBuilder) {
		auth
				.inMemoryAuthentication()
				.passwordEncoder(NoOpPasswordEncoder.getInstance())
				.withUser(applicationSettings.username)
				.password(applicationSettings.password)
				.roles("USER")
	}

}
