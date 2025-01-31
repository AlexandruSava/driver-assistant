package com.example.driverassistant.model

enum class Notification(
    val title: String,
    val message: String
) {
    GOOD_DRIVING(
        "Drive carefully",
        "We noticed you're quite a good driver. No mistakes yes."
    ),
    SPEEDING(
        "Respect Speed Limit",
        "We just noticed that you are speeding. Please slow down!"
    )
}