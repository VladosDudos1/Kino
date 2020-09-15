package vlados.dudos.vkino.models

data class GuestModel(
    val expires_at: String,
    val guest_session_id: String,
    val success: Boolean
)