package mostafa.projects.permissionutils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import mostafa.projects.permissionutils.sweetAlertDialog.SweetAlertDialog

class PermissionUtils : AppCompatActivity() {

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_RQUEST_CODE -> {
                for (i in 0 until permissions.size) {
                    val permList = arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )
                    val permission = permissions[i]
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        // user rejected the permission
                        var showRationale = shouldShowRequestPermissionRationale(permission)
                        if (!showRationale) {
                            // user also CHECKED "never ask again"
                            // you can either enable some fall back,
                            // disable features of your app
                            // or open another dialog explaining
                            // again the permission and directing to
                            // the app setting
                            showGPSNotEnabledDialog()

                        } else if (permList.contains(permission)) {
                            // user did NOT check "never ask again"
                            // this is a good place to explain the user
                            // why you need the permission and ask if he wants
                            // to accept it (the rationale)
                            checkLocationPermission()
                        }
                    }
                }


            }
        }
    }

    fun showGPSNotEnabledDialog() {
        var alertDialog =
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        alertDialog.setCancelable(true)
        alertDialog.setCanceledOnTouchOutside(true)
        alertDialog
            .setTitleText("Location needed")
            .setContentText("App require your permission to continue ?")
            .setConfirmText("Ok")
            .setConfirmClickListener(object : SweetAlertDialog.OnSweetClickListener {
                override fun onClick(sweetAlertDialog: SweetAlertDialog?) {
                    sweetAlertDialog?.dismiss()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, 20)
                }

            })
            .setCancelText("Cancel")
            .setCancelClickListener(object : SweetAlertDialog.OnSweetClickListener {
                override fun onClick(sweetAlertDialog: SweetAlertDialog?) {
                    sweetAlertDialog?.dismiss()
                    finish()
                }
            }).show()


    }

    fun checkLocationPermission() {
        var androidVer = Build.VERSION.SDK_INT
        if (androidVer > 28) {
            val permList = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
            for (i in permList) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        i
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(permList, LOCATION_RQUEST_CODE)
                }
            }
        } else {
            val permList = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            for (i in permList) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        i
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(permList, LOCATION_RQUEST_CODE)
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLocationPermission()
    }

}