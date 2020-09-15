package vlados.dudos.vkino

import vlados.dudos.vkino.models.*

object Case {

    var item: Result? = null
    var itemFull: ItemModel? = null

    var id: Int? = null

    var filterId: Int ?= null
    var filterList = mutableListOf<Result>()

    val googleKey = "AIzaSyC0nAQI3hBdEAGoRz5mNfNy3FaKL77bdiE"
    var key = ""

    var userToken = ""

    var showShimmer = true

    var castItem: Cast ?= null

    var bodyElement: RateBodyModel = RateBodyModel(0.0)

    var rate_item: Result? = null
}