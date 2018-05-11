package name.hudelmaier.adminproxyportal

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import java.util.Random
import java.util.concurrent.TimeUnit
import kotlin.streams.asSequence

class TokenStore {

	private val tokens: Cache<String, String> = Caffeine
			.newBuilder()
			.maximumSize(10000)
			.expireAfterAccess(1, TimeUnit.HOURS).build()

	fun createToken(destination: String): String {

		val source = ('A'..'Z')
		val token: String = Random().ints(32, 0, source.count())
				.asSequence()
				.map(source::elementAt)
				.joinToString("")

		tokens.put(token, destination)

		return token
	}

	fun getDestination(token: String): String? = tokens.getIfPresent(token)

	fun deleteToken(token: String) = tokens.invalidate(token)
}
