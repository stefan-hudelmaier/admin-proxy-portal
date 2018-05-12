package name.hudelmaier.adminproxyportal

class DestinationService(
		private val applicationSettings: ApplicationSettings
) {

	fun getByName(name: String): Destination? = applicationSettings.destinations.find { it.name == name }

	fun getAll() = applicationSettings.destinations
}
