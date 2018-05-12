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

@Controller
class UiController(private val destinationService: DestinationService) {

	//@GetMapping(path = ["/__portal__/login"])
	//fun login() = "login"

	@GetMapping(path = ["/__portal__/index.html"])
	fun home(model: Model): String {
		model.addAttribute("destinations", destinationService.getAll())
		return "ui"
	}

	@GetMapping(path = ["/__portal__/setCookie"])
	fun handleSetCookie(@RequestParam destinationName: String, response: HttpServletResponse): ResponseEntity<Void> {
		val destination = destinationService.getByName(destinationName) ?: throw RuntimeException("Invalid destination")
		val token = destinationService.getTokenForDestination(destination)
		response.addCookie(createCookie(token))
		return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).header("Location", "http://127.0.0.1:8080").build()
	}

	private fun createCookie(token: String): Cookie {
		val cookie = Cookie("adminProxyPortalToken", token)
		cookie.path = "/"
		cookie.maxAge = Duration.ofDays(60).get(ChronoUnit.SECONDS).toInt()
		cookie.isHttpOnly = true
		return cookie
	}

}
