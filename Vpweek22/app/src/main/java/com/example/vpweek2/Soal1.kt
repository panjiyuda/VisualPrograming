package com.example.vpweek2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Soal1View(){
    Column(
        modifier = Modifier.padding(bottom = 100.dp)
    ) {

        Image(painter = painterResource(R.drawable.drop_arrow), contentDescription = "DriopArrow",
            modifier = Modifier
                .size(25.dp)
                .offset(y = 50.dp)
                .offset(x = 20.dp))
    Image(painter = painterResource(R.drawable.dot), contentDescription = "Dot",
        modifier = Modifier
            .size(30.dp)
            .offset(y = 25.dp)
            .offset(x = 340.dp))
    Image(painter = painterResource(R.drawable.album), contentDescription = "Album",
        modifier = Modifier
            .size(280.dp)
            .offset(y = 70.dp)
            .offset(x = 70.dp))
        Text(text ="Liked Songs",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(horizontal = 130.dp)
                .offset(y = -285.dp),
            color = Color.Black,)

        Text(text = "Aku Milikmu",
            fontWeight = FontWeight.Bold,
            fontSize = 27.sp,
            color = Color.Black,
            modifier = Modifier
                .offset(y = 60.dp)
                .offset(x = 50.dp))

        Text(text = "Dewa 19",
            fontSize = 20.sp,
            modifier = Modifier
                .offset(y = 60.dp, x = 50.dp))

        Image(painter = painterResource(R.drawable.love), contentDescription = "Love",
            modifier = Modifier
                .size(45.dp)
                .offset(x = 300.dp, y = 10.dp))

        HorizontalDivider(
            modifier = Modifier
                .width(300.dp)
                .offset(y = 30.dp, x = 50.dp),
            thickness = 5.dp,
            color = Color.Black)

        Text(text = "1.05",
            fontSize = 20.sp,
            modifier = Modifier
                .offset(y = 37.dp, x = 50.dp))

        Text(text = "5.32",
            fontSize = 20.sp,
            modifier = Modifier
                .offset(y = 15.dp, x = 310.dp))

        Image(painter = painterResource(R.drawable.skip), contentDescription = "Skip1",
            modifier = Modifier
                .size(50.dp)
                .offset(y = 70.dp, x = 270.dp))

        Image(painter = painterResource(R.drawable.pause), contentDescription = "Pause",
            modifier = Modifier
                .size(70.dp)
                .offset(y = 10.dp, x = 170.dp))
        Image(painter = painterResource(R.drawable.skip), contentDescription = "Skip2",
            modifier = Modifier
                .size(50.dp)
                .offset(y = -50.dp, x = 90.dp)
                .scale(scaleX = -1f, scaleY = 1f))

//        Box(
//            modifier = Modifier
//                .offset(100.dp, y = 700.dp)
//                .width(200.dp)
//                .height(200.dp)
//                .background(color = Color.Blue)
//                .clip(RoundedCornerShape(16.dp))
//        )
    }

    Box(
        modifier = Modifier
            .size(300.dp)
            .offset(50.dp, y = 700.dp)
            .width(300.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(color = Color.Blue)


    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SoalPreview(){
    Soal1View()
}