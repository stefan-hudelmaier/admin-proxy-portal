package name.hudelmaier.adminproxyportal

import com.netflix.zuul.ZuulFilter
import org.springframework.boot.SpringApplication
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Bean


// TODO: Make it possible to configure a logo for login
// TODO: Add GitFlow
@SpringBootConfiguration
@EnableAutoConfiguration
@EnableZuulProxy
class AdminProxyPortalApplication() {

	@Bean
	@ConfigurationProperties(prefix = "admin-proxy-portal")
	fun applicationSettings(): ApplicationSettings = ApplicationSettings()

	@Bean
	fun uiController() = UiController(destinationService(), applicationSettings())

	@Bean
	fun proxyFilter(): ZuulFilter = ProxyFilter()

	@Bean
	fun destinationService() = DestinationService(applicationSettings())

	@Bean
	fun webSecurityConfig() = WebSecurityConfig(applicationSettings())

	@Bean
	fun mvcConfig() = MvcConfig()
}

fun main(args: Array<String>) {
	SpringApplication.run(AdminProxyPortalApplication::class.java, *args)
}
