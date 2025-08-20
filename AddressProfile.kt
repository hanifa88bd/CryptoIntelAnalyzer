package com.cryptointel.model

data class AddressProfile(
    val chain: String,
    val address: String,
    val labels: List<String>,
    val scores: Scores,
    val features: Map<String, Any>,
    val balances: Balances,
    val top_counterparties: List<Counterparty>,
    val last_active: String
)

data class Scores(
    val realness: Double,
    val exchange_proximity: Double,
    val whale_link: Double
)

data class Balances(
    val native: Double,
    val usd_est: Double
)

data class Counterparty(
    val address: String,
    val txs: Int,
    val total_usd: Double
)