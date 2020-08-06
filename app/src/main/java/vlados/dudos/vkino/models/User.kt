package vlados.dudos.vkino.models

data class User(
    var email: String,
    var nickName: String,
    var phone: String,
    var password: String
) {
    var token: String? = null
}