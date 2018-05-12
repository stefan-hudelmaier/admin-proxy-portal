package name.hudelmaier.adminproxyportal

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL

class ProxyFilter : ZuulFilter() {

	private val logger: Logger = LoggerFactory.getLogger(ProxyFilter::class.java)

	override fun run() {
		val context = RequestContext.getCurrentContext()
		val destinationUrl: String = context.request.session.getAttribute("DESTINATION_URL") as String? ?: throw RuntimeException("No destination URL set")
		logger.info("Forwarding to $destinationUrl")

		context.routeHost = URL(destinationUrl)

		context.addZuulResponseHeader("Cache-Control", "no-cache, no-store, must-revalidate")
		context.addZuulResponseHeader("Pragma", "no-cache")
		context.addZuulResponseHeader("Expires", "0")
	}

	override fun shouldFilter() = true

	override fun filterType() = "route"

	override fun filterOrder() = 0
}
