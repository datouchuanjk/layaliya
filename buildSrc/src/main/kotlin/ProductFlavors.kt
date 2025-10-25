sealed class ProductFlavors {
    companion object {
        val all = listOf(Online(), Offline())
    }

    abstract val signingConfigs: SigningConfigs
    abstract val applicationId: String
    open val versionCode: Int = 6
    open val versionName: String ="1.0.6"
    abstract val name: String

    class Online(
        override val name: String = "Online",
        override val signingConfigs: SigningConfigs = SigningConfigs.Online(),
        override val applicationId: String = "com.layaliya.chat",
    ) : ProductFlavors()

    class Offline(
        override val name: String = "Offline",
        override val signingConfigs: SigningConfigs = SigningConfigs.Offline(),
        override val applicationId: String = "com.test.lychat",
    ) : ProductFlavors()
}