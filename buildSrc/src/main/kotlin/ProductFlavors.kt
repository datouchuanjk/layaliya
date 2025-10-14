sealed class ProductFlavors {
    companion object {
        val all = listOf(Online(), Offline())
    }

    abstract val signingConfigs: SigningConfigs
    abstract val applicationId: String
    abstract val versionCode: Int
    abstract val versionName: String
    abstract val name: String

    class Online(
        override val name: String = "Online",
        override val signingConfigs: SigningConfigs = SigningConfigs.Online(),
        override val applicationId: String = "com.layaliya.chat",
        override val versionCode: Int = 1,
        override val versionName: String = "1.0"
    ) : ProductFlavors()

    class Offline(
        override val name: String = "Offline",
        override val signingConfigs: SigningConfigs = SigningConfigs.Offline(),
        override val applicationId: String = "com.test.lychat",
        override val versionCode: Int = 1,
        override val versionName: String = "1.0"
    ) : ProductFlavors()
}