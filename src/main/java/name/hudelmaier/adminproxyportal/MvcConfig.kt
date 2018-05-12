package name.hudelmaier.adminproxyportal

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class MvcConfig : WebMvcConfigurer {

	override fun addViewControllers(registry: ViewControllerRegistry) {
		registry.addViewController("/__portal__/login").setViewName("login")
	}

}
