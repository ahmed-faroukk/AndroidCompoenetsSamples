package com.example.androidcomponentsamples

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.androidcomponentsamples.components.Receiver.ReceiverSample
import com.example.androidcomponentsamples.components.WorkManager.UpdateDbWorker
import com.example.androidcomponentsamples.components.WorkManager.UploadWorker
import com.example.androidcomponentsamples.components.service.ForegroundServiceSample
import com.example.androidcomponentsamples.components.service.ServiceActions
import com.example.androidcomponentsamples.ui.theme.AndroidComponentSamplesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private val myReceiver: ReceiverSample = ReceiverSample()
    private val foregroundServiceSample = ForegroundServiceSample()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStart() {
        super.onStart()
        registerReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(myReceiver)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWorkers()
        setupUI()
        startAppWorking()
    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(myReceiver, intentFilter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupWorkers() {
        val uploadWorker = createUploadWorker()
        val updateCachingWorker = createUpdateCachingWorker()
        WorkManager.getInstance(applicationContext).enqueue(uploadWorker)
        WorkManager.getInstance(applicationContext).enqueue(updateCachingWorker)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createUploadWorker(): OneTimeWorkRequest {
        return OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setInitialDelay(Duration.ofSeconds(10))
            .setInputData(workDataOf("imageUri" to "path_to_your_image"))
            .build()
    }

    private fun createUpdateCachingWorker(): PeriodicWorkRequest {
        return PeriodicWorkRequest.Builder(
            UpdateDbWorker::class.java,
            5,
            TimeUnit.HOURS
        )
            .setConstraints(getConstraints())
            .setInputData(workDataOf("imageUri" to "path_to_your_image"))
            .build()
    }

    private fun setupUI() {
        setContent {
            AndroidComponentSamplesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CustomButton(name = "start foreground Service") {
                            foregroundServiceSample.startService(this@MainActivity)
                        }
                        CustomButton(name = "Stop foreground Service") {
                            foregroundServiceSample.stopService(this@MainActivity)
                        }
                    }
                }
            }
        }
    }


    private fun startAppWorking() {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                println("App is Working ........ ")
                kotlinx.coroutines.delay(2000)
            }
        }
    }

    private fun getConstraints(): Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()
    }
}

@Composable
fun CustomButton(name: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color.Red),
    ) {
        Text(text = name, color = Color.Black)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidComponentSamplesTheme {

    }
}
