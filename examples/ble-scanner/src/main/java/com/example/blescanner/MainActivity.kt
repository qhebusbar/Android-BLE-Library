package com.example.blescanner


import android.Manifest
import android.os.Bundle
import android.os.ParcelUuid
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import no.nordicsemi.android.support.v18.scanner.*
import permissions.dispatcher.*
import java.util.*
import kotlin.collections.ArrayList


@RuntimePermissions
class MainActivity : AppCompatActivity() {

    private val myScanCallback = MyScanCallback()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showLocationWithPermissionCheck()



    }


    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showLocation() {
        Toast.makeText(this, "showLocation", Toast.LENGTH_SHORT).show()
        startScanner()
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showRationaleForLocation(request: PermissionRequest) {
//        showRationaleDialog(R.string.permission_camera_rationale, request)
        Toast.makeText(this, "showRationaleForCamera", Toast.LENGTH_SHORT).show()
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onLocationDenied() {
        Toast.makeText(this, "onCameraDenied", Toast.LENGTH_SHORT).show()
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onLocationNeverAskAgain() {
        Toast.makeText(this, "onCameraNeverAskAgain", Toast.LENGTH_SHORT).show()
    }

    private fun startScanner(){
        val scanner = BluetoothLeScannerCompat.getScanner()
        val settings: ScanSettings = ScanSettings.Builder()
                .setLegacy(false)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(5000)
                .setUseHardwareBatchingIfSupported(true)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .build()
        val filters: MutableList<ScanFilter> = ArrayList()
       val pu1 = ParcelUuid(UUID.fromString("0000fe89-0000-1000-8000-00805f9b34fb"))
        filters.add(ScanFilter.Builder().setServiceUuid(pu1).build())
        scanner.startScan(filters, settings, myScanCallback)
    }
    private fun stopScanner(){
        val scanner = BluetoothLeScannerCompat.getScanner()
        scanner.stopScan(myScanCallback)
    }

   inner class MyScanCallback: ScanCallback() {

       override fun onScanResult(callbackType: Int, result: ScanResult) {
           super.onScanResult(callbackType, result)
           Log.d("MyScanCallback onScanResult ->",result.toString())
       }

       override fun onScanFailed(errorCode: Int) {
           super.onScanFailed(errorCode)
           Log.d("MyScanCallback onScanFailed ->",errorCode.toString())
       }

       override fun onBatchScanResults(results: MutableList<ScanResult>) {
           super.onBatchScanResults(results)
           Log.d("MyScanCallback onBatchScanResults ->",results.toString())
       }

    }

    override fun onDestroy() {
        super.onDestroy()
        stopScanner()
    }





}

