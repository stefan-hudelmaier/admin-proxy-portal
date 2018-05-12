package name.hudelmaier.adminproxyportal

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.NoOpPasswordEncoder

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

	override fun configure(http: HttpSecurity) {
		// @formatter:off
		http
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
				.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())
				.withUser("user").password("password").roles("USER")
	}

}
