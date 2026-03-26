package theRok.admin.com.admin.security

/**
 * 로그인 유저별 권한 문자열 목록.
 * 데모: userId 패턴별 임의 권한 — 이후 DB·캐시 조회 구현으로 교체하면 됨.
 */
fun interface UserPermissionProvider {
    fun permissionsFor(userId: String): List<String>
}
