package com.cryptointel.model

data class SignalResponse(
    val asset: String,
    val ts: String,
    val signal_value: Double,
    val vol_regime: String,
    val outlook: String,
    val explanation: String
)