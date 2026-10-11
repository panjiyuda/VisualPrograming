package com.example.vpweek3

import android.os.SystemClock
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlin.random.Random


enum class Stage { START, WAITING, READY, TRIAL_DONE, FAIL, FINAL }

enum class Category (val pesan: String, val warna: Color, val gambar: Int) {
    CEPAT("DANG YOU ARE SO FAST BRO!", Color(0xFF00E676), R.drawable.reaction_salute),
    BAGUS("YOUR REFLEX IS GOOD", Color(0xFF2196F3), R.drawable.reaction_thumbs_up),
    BIASA("MEH LIKE OTHER PERSON", Color(0xFFFF9800), R.drawable.reaction_standard),
    LAMBAT("YOU LIKE A SNAIL BRO", Color(0xFFFF5722), R.drawable.reaction_thumbs_down)
}

@Composable
fun TentukanCategory(rata : Int?): Category = when {
    rata == null -> Category.LAMBAT
    rata < 180 -> Category.CEPAT
    rata < 280 -> Category.BAGUS
    rata < 450 -> Category.BIASA
    else -> Category.LAMBAT
}

@Composable
fun ReactionTest() {
    var layar by remember { mutableStateOf(Stage.START) }
    var trialKe by remember { mutableIntStateOf(1) }
    var waktuMulai by remember { mutableLongStateOf(0L) }

    val hasilTampil = remember { mutableStateListOf("-", "-", "-") }
    val waktuValid = remember { mutableStateListOf<Int>() }
    LaunchedEffect(layar) {
        if (layar == Stage.WAITING) {
            delay(Random.nextLong(500, 4501))
            waktuMulai = SystemClock.elapsedRealtime()
            layar = Stage.READY
        }
    }
}

@Composable
fun ClickTest(){
    when(layar) {
        Stage.START -> layar = Stage.WAITING

        Stage.WAITING -> {
            hasilTampil[trialKe - 1] = "FAIL"
            layar = Stage.FAIL
        }

        Stage.READY -> {
            val waktu = (SystemClock.elapsedRealtime() - waktuMulai).toInt()
            hasilTampil[trialKe - 1] = "${waktu}ms"
            waktuValid.add(waktu)
            layar = if (trialKe == 3) Stage.FINAL else Stage.TRIAL_DONE
        }

        Stage.TRIAL_DONE -> {
            trialKe ++
            layar = Stage.WAITING
        }

        Stage.FAIL -> {
            if (trialKe == 3){
                layar = Stage.FINAL
            }else{
                trialKe ++
                layar = Stage.START
            }
        }

        Stage.FINAL -> {
            hasilTampil.indices.forEach {hasilTampil[it] = "-"}
            waktuValid.clear()
            trialKe = 1
            layar = Stage.WAITING
        }
    }
}

val rata: Int? = if (waktuValid.isEmpty()) null else waktuValid.average().roundToInt()
val kategori = tentukanKategori(rata)
var warna = Color.White
var judul = ""
var ikonRes : ImageVector = R.drawable.lightning
var teks1 = ""
var teks2 = ""

when (layar) {
    Stage.START-> {
        warna = Color(0xFF6FCFDF)
        judul = "Reaction"; ikonRes = R.drawable.lightning; teks1 = "Test"; teks2 = "Click to Start"
    }
    Stage.WAITING -> {
        warna = Color(0xFFD6D6D6)
        judul = "Get Ready"; ikonRes = R.drawable.warning; teks1 = "Wait for green light..."; teks2 =
        "DON'T CLICK YET!"
    }
    Stage.READY -> {
        warna = Color(0xFF4CAF50)
        judul = "GO!"; ikonRes = R.drawable.run; teks1 = "CLICK NOW!"; teks2 =
        "TAP AS FAST AS YOU CAN!"
    }
    Stage.TRIAL_DONE -> {
        warna = Color(0xFF4CAF50)
        judul = "Trial $trialKe Complete!"; ikonRes = R.drawable.complete
        teks1 = "Time: ${hasilTampil[trialKe - 1]}"; teks2 = "Continue to Trial ${trialKe + 1}"
    }
    Stage.FAIL -> {
        warna = Color(0xFFF94144)
        judul = "FAIL!"; ikonRes = "👎"
        teks1 = "You clicked too early, TRY TO READ THE RULE BRO"; teks2 = "TRY AGAIN"
    }
    Stage.FINAL -> {
        warna = kategori.warna
        judul = kategori.pesan
        teks1 = "Average: ${rata?.let { "${it}ms" } ?: "-"}"; teks2 = "Click to Start New Test"
    }
}

Box(
    modifier = Modifier
    .fillMaxSize()
    .background(warna)
    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { ClickTest() },
contentAlignment = Alignment.Center
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
        Text(judul, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))

        if (layar == Stage.FINAL) {
            Image(painterResource(kategori.gambar), contentDescription = kategori.pesan, modifier = Modifier.size(140.dp))
        } else {
            Icon(ikon, contentDescription = null, tint = Color.White, modifier = Modifier.size(96.dp))
        }

        Spacer(Modifier.height(24.dp))
        Text(teks1, color = Color.White, fontSize = 16.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text(teks2, color = Color.White, fontSize = 12.sp, textAlign = TextAlign.Center)

        val tampilTabel = when (layar) {
            Layar.AWAL -> trialKe > 1                       // tampil setelah ada trial gagal
            Layar.TRIAL_SELESAI, Layar.GAGAL, Layar.FINAL -> true
            else -> false
        }
        if (tampilTabel) {
            Spacer(Modifier.height(24.dp))
            TabelHasil(hasilTampil, if (layar == Layar.FINAL) rata else null)
        }
    }
}
}







@Preview (showBackground = true, showSystemUi = true)
@Composable
fun SoalPreview() {
    ReactionTest()
}