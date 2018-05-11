package name.hudelmaier.adminproxyportal

class DestinationService(
		private val applicationSettings: ApplicationSettings,
		private val tokenStore: TokenStore
) {

	fun getByName(name: String): Destination? = applicationSettings.destinations.find { it.name == name }

	fun getTokenForDestination(destination: Destination): String = tokenStore.createToken(destination.url)

	fun getAll() = applicationSettings.destinations
}
