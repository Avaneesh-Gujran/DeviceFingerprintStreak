package com.example.myapplication

import android.Manifest.permission.READ_PHONE_STATE
import android.content.pm.PackageManager
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.IOException
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.myapplication.ui.theme.MyApplicationTheme
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.text.format.Formatter
import android.Manifest

import android.os.Build
import android.provider.Settings.Secure
import android.app.Activity


import androidx.core.content.ContextCompat

import android.provider.Settings
import androidx.core.content.getSystemService

fun getIPAddress(context: Context): String {
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiInfo: WifiInfo = wifiManager.connectionInfo
    val ip = wifiInfo.ipAddress
    val ipAddress: String = Formatter.formatIpAddress(ip)
    return ipAddress
}
private const val PERMISSION_REQUEST_CODE = 123

fun getIMEI(): String? {
    try {
        val process = Runtime.getRuntime().exec("getprop ro.serialno")
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val serialNumber = reader.readLine()
        reader.close()

        return serialNumber+" this is the serial"
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

fun getDeviceFingerprint(context: Context): String {
    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val macAddress = wifiManager.connectionInfo.macAddress ?: ""

    val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: ""

    val deviceModel = Build.MODEL ?: ""
    val manufacturer = Build.MANUFACTURER ?: ""
    val sdkVersion = Build.VERSION.SDK_INT.toString()

    val fingerprint = "$macAddress$androidId$deviceModel$manufacturer$sdkVersion"

    return fingerprint
}



fun getSimInfo(context: Context): String {
    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    val simOperatorName = telephonyManager.simOperatorName
    val simSerialNumber = telephonyManager.simSerialNumber
    val networkOperatorName = telephonyManager.networkOperatorName
    val networkCountryIso = telephonyManager.networkCountryIso

    val simInfo = """
        SIM Operator Name: $simOperatorName
        SIM Serial Number: $simSerialNumber
        Network Operator Name: $networkOperatorName
        Network Country ISO: $networkCountryIso
    """.trimIndent()

    return simInfo
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")





                }
            }
        }

    }



    @Composable
    fun Greeting(name: String?, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        val deviceId = getIMEI()
        val ipAddress = getIPAddress(this)
        //val simInfo = getSimInfo(this)
// Use the IP address as needed

        if (deviceId != null) {
            // Use the device ID as needed...
            Text(
                text = "\n MainActivity"+ "Device ID: $deviceId"

            )
            Text(
                text = "\n \n IP Address: $ipAddress"
            )
            //Text(
            //    text = "\n \n \n Sim Info: $simInfo"
            //)



        } else {
            // Device ID retrieval failed or permission not granted
            Text(

                text = " \n MainActivity"+ "Device ID: $deviceId"
            )
        }


    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MyApplicationTheme {
            Greeting("Android")
        }
    }


}