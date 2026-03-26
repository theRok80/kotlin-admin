package theRok.admin.com.admin.common.constants

enum class PermissionConstants {
    HELLO, USER, PAYMENT, COMIC, BANNER, EVENT, GOODS, PRODUCT,
    ;

    companion object {
        fun fromString(value: String): PermissionConstants {
            return PermissionConstants.values().firstOrNull { it.name == value } ?: throw IllegalArgumentException("Invalid permission: $value")
        }
    }
}
