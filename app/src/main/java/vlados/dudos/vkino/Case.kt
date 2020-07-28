package vlados.dudos.vkino

import vlados.dudos.vkino.models.ItemModel
import vlados.dudos.vkino.models.Result

object Case {

    var item: Result? = null
    var itemFull: ItemModel? = null

    var video_key = ""
    var id: Int? = null

    var filterId: Int ?= null
    val filterList = mutableListOf<Result>()

    val googleKey = "AIzaSyC0nAQI3hBdEAGoRz5mNfNy3FaKL77bdiE"
    var key = ""

}