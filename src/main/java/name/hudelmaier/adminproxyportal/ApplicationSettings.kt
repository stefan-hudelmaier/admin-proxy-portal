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

	@NotBlank
	lateinit var username: String

	@NotBlank
	lateinit var password: String

	var validEmailsForOauth: List<String> = listOf()

	@Valid
	var users: List<User> = listOf()

	@Valid
	var oauth: OauthSettings? = null
}

@Validated
class Destination {

	@NotBlank
	lateinit var name: String

	@NotBlank
	lateinit var url: String

	var icon: String = "home"
}

@Validated
class User {

	@NotBlank
	lateinit var username: String

	@NotBlank
	lateinit var password: String
}

@Validated
class OauthSettings {

	@NotBlank
	lateinit var clientId: String

	@NotBlank
	lateinit var clientSecret: String

	@NotBlank
	lateinit var provider: String

}
