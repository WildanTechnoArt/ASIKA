package com.unpi.asika.model

import java.util.*

data class NotificationModel(
    var title: String? = null,
    var message: String? = null,
    var datetime: String? = null,
    var date: Date? = null
)
