package name.hudelmaier.adminproxyportal

import com.netflix.zuul.ZuulFilter
import org.springframework.boot.SpringApplication
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Bean

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableZuulProxy
class AdminProxyPortalApplication {

	@Bean
	@ConfigurationProperties
	fun applicationSettings(): ApplicationSettings = ApplicationSettings()

	@Bean
	fun uiController() = UiController(destinationService())

	@Bean
	fun proxyFilter(): ZuulFilter = ProxyFilter(tokenStore())

	@Bean
	fun tokenStore() = TokenStore()

	@Bean
	fun destinationService() = DestinationService(applicationSettings(), tokenStore())

	@Bean
	fun webSecurityConfig() = WebSecurityConfig()

	@Bean
	fun mvcConfig() = MvcConfig()
}

fun main(args: Array<String>) {
	SpringApplication.run(AdminProxyPortalApplication::class.java, *args)
}
