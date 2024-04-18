package com.example.androidcomponentsamples.components.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings

class ReceiverSample(
) : BroadcastReceiver() {
    var state = false
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            val isTurnedOn = Settings.Global.getInt(
                context?.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON
            ) != 0

            println("airplane mode is $isTurnedOn")

            if (state != isTurnedOn)
                state = isTurnedOn

        }


    }


}
