package theRok.admin.com.admin.provider

data class ParsedAccessToken(
    val subject: String,
    val permissions: List<String>,
)
