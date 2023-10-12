/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.clickselection.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PickerGroup
import androidx.wear.compose.material.PickerGroupItem
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberPickerGroupState
import androidx.wear.compose.material.rememberPickerState
import com.example.clickselection.presentation.network.SocketRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            pageManager()
        }
    }

    @Composable
    fun pageManager() {
        var ip by rememberSaveable { mutableStateOf("") }
        val pickerGroupState = rememberPickerGroupState(3)
        val pickerStateOne = rememberPickerState(initialNumberOfOptions = 999, initiallySelectedOption = 192)
        val pickerStateTwo = rememberPickerState(initialNumberOfOptions = 999, initiallySelectedOption = 168)
        val pickerStateThree = rememberPickerState(initialNumberOfOptions = 999)
        val pickerStateFour = rememberPickerState(initialNumberOfOptions = 999)
        if ( ip.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                verticalArrangement = Arrangement.Center

            ) {
                Text(
                    text = "IPV4",
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Button(
                    onClick = {
                        ip = pickerStateOne.selectedOption.toString() + "." + pickerStateTwo.selectedOption.toString() + "." + pickerStateThree.selectedOption.toString() + "." + pickerStateFour.selectedOption.toString()
                        println(ip)
                    },
                    enabled = true,
                    shape = RectangleShape,
                    colors = ButtonDefaults.primaryButtonColors(backgroundColor = Color.Blue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)

                ) {
                    Text(
                        text = "Connect"
                        //fontSize = 15.sp
                    )
                }
                PickerGroup(
                    PickerGroupItem(
                        pickerState = pickerStateOne,
                        option = {optionIndex, _ -> Text(text = optionIndex.toString())},
                    ),
                    PickerGroupItem(
                        pickerState = pickerStateTwo,
                        option = {optionIndex, _ -> Text(text = optionIndex.toString())},
                    ),
                    PickerGroupItem(
                        pickerState = pickerStateThree,
                        option = {optionIndex, _ -> Text(text = optionIndex.toString())},
                    ),
                    PickerGroupItem(
                        pickerState = pickerStateFour,
                        option = {optionIndex, _ -> Text(text = optionIndex.toString())},
                    ),
                    pickerGroupState = pickerGroupState,
                    autoCenter = false,
                    separator = {_ -> Text(text = " . ")}
                )
            }
        }
        else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .pointerInput(ip) {
                        detectTapGestures(onPress = {_ ->
                            awaitRelease()
                            sendClick(ip)
                        },)
                    },
                verticalArrangement = Arrangement.Center

            ) {
                Text(
                    text = "",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }

    private fun sendClick(ip: String) = runBlocking {
        launch {
            SocketRepository.sendUDP("0.000000", "0.000000", "U", ip)
        }
    }
}
