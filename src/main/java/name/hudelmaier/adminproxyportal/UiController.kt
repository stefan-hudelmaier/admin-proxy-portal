package name.hudelmaier.adminproxyportal

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.Duration
import java.time.temporal.ChronoUnit
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@Controller
class UiController(private val destinationService: DestinationService, private val applicationSettings: ApplicationSettings) {

	@GetMapping(path = ["/__portal__/index.html"])
	fun home(model: Model): String {
		model.addAttribute("destinations", destinationService.getAll())
		return "ui"
	}

	@GetMapping(path = ["/__portal__/login"])
	fun login(model: Model): String {
		model.addAttribute("oauthEnabled", applicationSettings.oauth != null)
		model.addAttribute("oauthProvider", applicationSettings.oauth?.provider?.capitalize())
		return "login"
	}

	// TODO: Rename to setDestination
	@GetMapping(path = ["/__portal__/setCookie"])
	fun handleSetCookie(@RequestParam destinationName: String, session: HttpSession, response: HttpServletResponse): ResponseEntity<Void> {
		val destination = destinationService.getByName(destinationName) ?: throw RuntimeException("Invalid destination")
		session.setAttribute("DESTINATION_URL", destination.url)
		return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/").build()
	}

	private fun createCookie(token: String): Cookie {
		val cookie = Cookie("adminProxyPortalToken", token)
		cookie.path = "/"
		cookie.maxAge = Duration.ofDays(60).get(ChronoUnit.SECONDS).toInt()
		cookie.isHttpOnly = true
		return cookie
	}

}
