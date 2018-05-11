package name.hudelmaier.adminproxyportal

import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

@Validated
class ApplicationSettings {

	@Valid
	@NotEmpty
	var destinations: List<Destination> = listOf()

}

@Validated
class Destination {

	@NotBlank
	lateinit var name: String

	@NotBlank
	lateinit var url: String

	var icon: String = "home"

}
