 sealed class SigningConfigs {
    abstract val name: String
    abstract val storeFile: String
    abstract val storePassword: String
    abstract val keyAlias: String
    abstract val keyPassword: String

    class Offline(
        override val name: String = "Offline",
        override val storeFile: String = "default.jks",
        override val storePassword: String = "123456",
        override val keyAlias: String = "key0",
        override val keyPassword: String = "123456"
    ) : SigningConfigs()

    class Online(
        override val name: String = "Online",
        override val storeFile: String = "layaliya.jks",
        override val storePassword: String = "layaliya",
        override val keyAlias: String = "key0",
        override val keyPassword: String = "layaliya"
    ) : SigningConfigs()
}
