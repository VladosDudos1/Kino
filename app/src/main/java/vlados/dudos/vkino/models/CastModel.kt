package vlados.dudos.vkino.models

data class CastModel(
    val credit_type: String,
    val department: String,
    val id: String,
    val job: String,
    val media: Media,
    val media_type: String,
    val person: Person
)