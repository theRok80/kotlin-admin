package theRok.admin.com.admin.common.constants

// 로그인 시도 횟수를 저장하는 시간: 분
const val THROTTLE_TTL = 1L
// 로그인 시도 시 최대 허용 횟수
const val MAX_THROTTLE_COUNT = 10
// 정렬 기본 값
enum class SortDirection {
    ASC, DESC
}
