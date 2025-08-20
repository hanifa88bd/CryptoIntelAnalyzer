package com.cryptointel.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cryptointel.ApiService
import com.cryptointel.model.AddressProfile
import com.cryptointel.model.SignalResponse

@Composable
fun HomeScreen() {
    var baseUrl by remember { mutableStateOf(BuildConfig.API_BASE_URL) }
    var chain by remember { mutableStateOf("eth") }
    var addr by remember { mutableStateOf("") }
    var asset by remember { mutableStateOf("BTC") }
    var loading by remember { mutableStateOf(false) }
    var profile by remember { mutableStateOf<AddressProfile?>(null) }
    var signal by remember { mutableStateOf<SignalResponse?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    val api = remember(baseUrl) { ApiService.create(baseUrl) }

    Column(Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("Quick Tools", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = baseUrl, onValueChange = { baseUrl = it }, label = { Text("API Base URL") }, singleLine = true)
        Spacer(Modifier.height(8.dp))
        Row {
            OutlinedTextField(
                value = chain, onValueChange = { chain = it },
                label = { Text("Chain (eth/btc/tron)") }, singleLine = true, modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            OutlinedTextField(
                value = asset, onValueChange = { asset = it.uppercase() },
                label = { Text("Asset (BTC/ETH/USDT)") }, singleLine = true, modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = addr, onValueChange = { addr = it }, label = { Text("Address") }, singleLine = true)
        Spacer(Modifier.height(8.dp))
        Row {
            Button(onClick = {
                error = null; loading = true
                profile = null
                kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                    try {
                        val p = api.getProfile(chain, addr)
                        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                            profile = p; loading = false
                        }
                    } catch (e: Exception) {
                        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                            error = e.message; loading = false
                        }
                    }
                }
            }) { Text("Lookup Profile") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                error = null; loading = true
                signal = null
                kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                    try {
                        val s = api.getSignal(asset)
                        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                            signal = s; loading = false
                        }
                    } catch (e: Exception) {
                        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                            error = e.message; loading = false
                        }
                    }
                }
            }) { Text("Get Signal") }
        }

        if (loading) { Spacer(Modifier.height(16.dp)); LinearProgressIndicator(modifier = Modifier.fillMaxWidth()) }
        error?.let { Spacer(Modifier.height(8.dp)); Text("Error: $it", color = MaterialTheme.colorScheme.error) }

        profile?.let { p ->
            Spacer(Modifier.height(16.dp))
            ElevatedCard {
                Column(Modifier.padding(16.dp)) {
                    Text("Address Profile", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Chain: ${p.chain.uppercase()}")
                    Text("Address: ${p.address}")
                    Text("Labels: ${p.labels.joinToString()}")
                    Text("Scores → Realness: ${p.scores.realness}  |  Exchange Proximity: ${p.scores.exchange_proximity}  |  Whale: ${p.scores.whale_link}")
                    Text("Balances → Native: ${p.balances.native}  |  USD est: ${p.balances.usd_est}")
                    Spacer(Modifier.height(8.dp))
                    Text("Top Counterparties:")
                    p.top_counterparties.forEach {
                        Text("• ${it.address} — txs: ${it.txs}, total: $${it.total_usd}")
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Last active: ${p.last_active}")
                }
            }
        }

        signal?.let { s ->
            Spacer(Modifier.height(16.dp))
            ElevatedCard {
                Column(Modifier.padding(16.dp)) {
                    Text("Market Signal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("${s.asset} @ ${s.ts}")
                    Text("Value: ${s.signal_value} | Regime: ${s.vol_regime}")
                    Text("Outlook: ${s.outlook}")
                    Spacer(Modifier.height(8.dp))
                    Text(s.explanation)
                }
            }
        }
    }
}