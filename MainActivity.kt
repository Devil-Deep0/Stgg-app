package com.stgg.esports

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF080B14)
private val Panel = Color(0xFF121827)
private val Red = Color(0xFFFF1744)
private val Gold = Color(0xFFFFC928)
private val Muted = Color(0xFF9AA6BD)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { STGGApp() }
    }
}

data class Tournament(val id: Int, val title: String, val mode: String, val time: String, val entry: String, val prize: String, val slots: String)
data class Player(val name: String, val points: Int, val wins: Int)

@Composable
fun STGGApp() {
    var page by remember { mutableStateOf("Home") }
    var registered by remember { mutableStateOf(setOf<Int>()) }
    var gamerName by remember { mutableStateOf("STGG_Player") }
    var uid by remember { mutableStateOf("") }
    var toast by remember { mutableStateOf("") }
    var adminMode by remember { mutableStateOf(false) }
    val tournaments = remember { mutableStateListOf(
        Tournament(1, "Solo Rush", "Solo • Battle Royale", "Today • 7:00 PM", "Free", "₹500 (demo)", "32 slots"),
        Tournament(2, "Duo Clash", "Duo • Battle Royale", "Tomorrow • 6:30 PM", "₹20 (demo only)", "₹1,000 (demo)", "24 teams"),
        Tournament(3, "Squad War", "Squad • Battle Royale", "Sunday • 5:00 PM", "Free", "₹2,000 (demo)", "12 squads")
    ) }
    val players = listOf(Player("STGG_Devil", 2450, 8), Player("RDXxGAMER", 2320, 6), Player("LegendFF", 2190, 5), Player("MAXX_07", 2010, 4), Player("FireBoy", 1760, 3))
    MaterialTheme(colorScheme = darkColorScheme(primary = Red, secondary = Gold, background = Bg, surface = Panel)) {
        Scaffold(containerColor = Bg, bottomBar = {
            NavigationBar(containerColor = Panel, contentColor = Color.White) {
                listOf("Home", "Tournaments", "Leaderboard", "Profile").forEach { label ->
                    NavigationBarItem(selected = page == label, onClick = { page = label; toast = "" }, icon = {
                        when (label) {
                            "Home" -> Icon(Icons.Default.Home, null)
                            "Tournaments" -> Icon(Icons.Default.EmojiEvents, null)
                            "Leaderboard" -> Icon(Icons.Default.Leaderboard, null)
                            else -> Icon(Icons.Default.Person, null)
                        }
                    }, label = { Text(label, fontSize = 10.sp) }, colors = NavigationBarItemDefaults.colors(selectedIconColor = Red, selectedTextColor = Red, indicatorColor = Color(0xFF2B1724), unselectedIconColor = Muted, unselectedTextColor = Muted))
                }
            }
        }) { pad ->
            Column(Modifier.fillMaxSize().padding(pad).background(Bg)) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("STGG", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                        Text("E-SPORTS  •  PLAY. COMPETE. EARN.", color = Red, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    IconButton(onClick = { page = "Notifications" }) { Icon(Icons.Default.Notifications, "Notifications", tint = Gold) }
                }
                if (toast.isNotBlank()) {
                    Text(toast, color = Gold, modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 4.dp), fontSize = 12.sp)
                }
                when (page) {
                    "Home" -> HomeScreen(tournaments, registered, onOpen = { page = "Tournaments" }, onRegister = { t -> registered = registered + t.id; toast = "Registered for ${t.title} (demo)." })
                    "Tournaments" -> TournamentScreen(tournaments, registered, onRegister = { t -> registered = registered + t.id; toast = "Registered for ${t.title} (demo)." }, onRoom = { t -> page = "Room:${t.id}" })
                    "Leaderboard" -> LeaderboardScreen(players)
                    "Profile" -> ProfileScreen(gamerName, { gamerName = it }, uid, { uid = it }, registered.size, { page = "Payments" }, { page = "Admin" })
                    "Notifications" -> SimpleScreen("Notifications", "You're all caught up.", "Tournament updates will appear here in a production version.")
                    "Payments" -> SimpleScreen("Payments & Entry Fees", "Payments are disabled in this prototype.", "No real money is collected. Before launch, add a compliant payment provider, secure backend verification, refund rules and required guardian/business approvals.")
                    "Admin" -> AdminScreen(adminMode, { adminMode = it }, tournaments, onAdd = { tournaments.add(Tournament((tournaments.maxOfOrNull { it.id } ?: 0) + 1, "Community Cup", "Squad • Custom", "Set date in admin", "Free", "Prize TBA", "16 squads")); toast = "Demo tournament added." })
                    else -> {
                        val id = page.removePrefix("Room:").toIntOrNull()
                        val t = tournaments.firstOrNull { it.id == id }
                        SimpleScreen("Room Details", if (t != null && t.id in registered) "Room ID: Will be shared by organiser" else "Room details locked", if (t != null && t.id in registered) "Password: Shared shortly before match time. Never publish room credentials publicly." else "Register for this tournament first. Real room credentials are not configured in this demo.")
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(tournaments: List<Tournament>, registered: Set<Int>, onOpen: () -> Unit, onRegister: (Tournament) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF30101E)), shape = RoundedCornerShape(22.dp)) {
                Column(Modifier.fillMaxWidth().padding(20.dp)) {
                    Text("FREE FIRE", color = Gold, fontWeight = FontWeight.Black, fontSize = 13.sp)
                    Text("TOURNAMENTS", color = Color.White, fontWeight = FontWeight.Black, fontSize = 28.sp)
                    Text("Find your squad. Prove your skill.", color = Muted, fontSize = 13.sp)
                    Spacer(Modifier.height(14.dp))
                    Button(onClick = onOpen, colors = ButtonDefaults.buttonColors(containerColor = Red)) { Text("Explore tournaments", fontWeight = FontWeight.Bold) }
                }
            }
        }
        item { SectionTitle("UPCOMING TOURNAMENTS") }
        items(tournaments.take(3)) { t -> TournamentCard(t, t.id in registered, { onRegister(t) }, null) }
        item { Text("Prototype only • sample tournament data • no real prize or payment", color = Muted, fontSize = 11.sp) }
    }
}

@Composable
fun TournamentScreen(tournaments: List<Tournament>, registered: Set<Int>, onRegister: (Tournament) -> Unit, onRoom: (Tournament) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { SectionTitle("ALL TOURNAMENTS") }
        items(tournaments) { t -> TournamentCard(t, t.id in registered, { onRegister(t) }, { onRoom(t) }) }
    }
}

@Composable
fun TournamentCard(t: Tournament, isRegistered: Boolean, onRegister: () -> Unit, onRoom: (() -> Unit)?) {
    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(48.dp).background(Color(0xFF321325), RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) { Icon(Icons.Default.EmojiEvents, null, tint = Gold, modifier = Modifier.size(27.dp)) }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(t.title, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Text(t.mode, color = Muted, fontSize = 12.sp)
                }
                Text(t.entry, color = Gold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("◷ ${t.time}", color = Muted, fontSize = 11.sp)
                Text("${t.slots}  •  Prize ${t.prize}", color = Color.White, fontSize = 11.sp)
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onRegister, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (isRegistered) Color(0xFF245C45) else Red), shape = RoundedCornerShape(12.dp)) {
                    Text(if (isRegistered) "Registered ✓" else "Register", fontWeight = FontWeight.Bold)
                }
                if (onRoom != null) OutlinedButton(onClick = onRoom, shape = RoundedCornerShape(12.dp)) { Text("Room info", color = Gold) }
            }
        }
    }
}

@Composable
fun LeaderboardScreen(players: List<Player>) {
    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { SectionTitle("TOP PLAYERS") }
        item { Text("Demo scores only • rankings are not connected to match results", color = Muted, fontSize = 11.sp) }
        items(players) { p ->
            Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(14.dp)) {
                Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("#${players.indexOf(p) + 1}", color = Gold, fontWeight = FontWeight.Black, fontSize = 18.sp, modifier = Modifier.width(42.dp))
                    Icon(Icons.Default.AccountCircle, null, tint = Red, modifier = Modifier.size(32.dp))
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) { Text(p.name, color = Color.White, fontWeight = FontWeight.Bold); Text("${p.wins} wins", color = Muted, fontSize = 12.sp) }
                    Text("${p.points} pts", color = Gold, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(name: String, onName: (String) -> Unit, uid: String, onUid: (String) -> Unit, count: Int, onPayments: () -> Unit, onAdmin: () -> Unit) {
    var editName by remember(name) { mutableStateOf(name) }
    var editUid by remember(uid) { mutableStateOf(uid) }
    LazyColumn(contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(20.dp)) {
                Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.AccountCircle, null, tint = Red, modifier = Modifier.size(72.dp))
                    Text("PLAYER PROFILE", color = Gold, fontWeight = FontWeight.Black, fontSize = 12.sp)
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(editName, { editName = it }, label = { Text("In-game name") }, singleLine = true)
                    OutlinedTextField(editUid, { editUid = it }, label = { Text("Free Fire UID (optional)") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    Button(onClick = { onName(editName.ifBlank { "STGG_Player" }); onUid(editUid) }, colors = ButtonDefaults.buttonColors(containerColor = Red), modifier = Modifier.fillMaxWidth()) { Text("Save profile") }
                }
            }
        }
        item { StatCard("My registrations", count.toString(), "This device only; not synced") }
        item { OutlinedButton(onClick = onPayments, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.AccountBalanceWallet, null); Spacer(Modifier.width(8.dp)); Text("Payments & sponsorship info") } }
        item { OutlinedButton(onClick = onAdmin, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.AdminPanelSettings, null); Spacer(Modifier.width(8.dp)); Text("Demo admin panel") } }
    }
}

@Composable
fun AdminScreen(adminMode: Boolean, onToggle: (Boolean) -> Unit, tournaments: List<Tournament>, onAdd: () -> Unit) {
    LazyColumn(contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { SectionTitle("ADMIN CONSOLE — DEMO") }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Panel)) {
                Column(Modifier.padding(16.dp)) {
                    Text("This is not secure admin authentication.", color = Gold, fontWeight = FontWeight.Bold)
                    Text("Do not publish this demo as a live admin system. Production admin actions must be protected by server-side roles and login.", color = Muted, fontSize = 12.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) { Text("Enable demo controls", color = Color.White, modifier = Modifier.weight(1f)); Switch(checked = adminMode, onCheckedChange = onToggle) }
                }
            }
        }
        item { Button(onClick = onAdd, enabled = adminMode, colors = ButtonDefaults.buttonColors(containerColor = Red), modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.Add, null); Spacer(Modifier.width(8.dp)); Text("Add sample tournament") } }
        item { Text("Tournament count: ${tournaments.size}", color = Muted) }
        item { Text("Production tasks: verify results, manage registrations, publish room credentials to registered players, review reports and manage sponsor banners.", color = Color.White, fontSize = 13.sp) }
    }
}

@Composable
fun SimpleScreen(title: String, subtitle: String, detail: String) {
    Column(Modifier.fillMaxSize().padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        SectionTitle(title.uppercase())
        Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.padding(18.dp)) {
                Text(subtitle, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(10.dp))
                Text(detail, color = Muted, fontSize = 13.sp, lineHeight = 20.sp)
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) { Text(title, color = Gold, fontWeight = FontWeight.Black, fontSize = 13.sp, letterSpacing = 1.3.sp) }

@Composable
fun StatCard(label: String, value: String, note: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(16.dp)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) { Text(label, color = Muted, fontSize = 12.sp); Text(value, color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Black); Text(note, color = Muted, fontSize = 11.sp) }
            Icon(Icons.Default.EmojiEvents, null, tint = Gold, modifier = Modifier.size(32.dp))
        }
    }
}
