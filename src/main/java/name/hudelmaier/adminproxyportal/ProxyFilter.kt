package name.hudelmaier.adminproxyportal

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL

class ProxyFilter(private val tokenStore: TokenStore) : ZuulFilter() {

	private val logger: Logger = LoggerFactory.getLogger(ProxyFilter::class.java)

	override fun run() {
		val context = RequestContext.getCurrentContext()
		val token = context.request.cookies.find { it.name == "adminProxyPortalToken" }?.let { it.value }
				?: throw RuntimeException("No Cookie")

		val destination: String = tokenStore.getDestination(token) ?: throw RuntimeException("Invalid token $token")

		logger.info(">>>>>>>> $token $destination")

		context.routeHost = URL(destination)

		context.addZuulResponseHeader("Cache-Control", "no-cache, no-store, must-revalidate")
		context.addZuulResponseHeader("Pragma", "no-cache")
		context.addZuulResponseHeader("Expires", "0")
	}

	override fun shouldFilter() = true

	override fun filterType() = "route"

	override fun filterOrder() = 0
}
