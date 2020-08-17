package com.unis.kotlin.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.unis.kotlin.R
import com.unis.kotlin.cache.UserCenter
import com.unis.kotlin.utils.LinPermission

class LoginActivity : AppCompatActivity() {

    private var passWordFragment: PassWordFragment? = null
    private var userIDFragment: UserIDFragment? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //首先申请权限
        LinPermission.requestMultiplePermission(this@LoginActivity, intArrayOf(7, 8))

        passWordFragment = supportFragmentManager.findFragmentById(R.id.fragment_password_login) as PassWordFragment

        userIDFragment = supportFragmentManager.findFragmentById(R.id.fragment_sms_login) as UserIDFragment

        val transaction = supportFragmentManager.beginTransaction()
        transaction.show(userIDFragment).hide(passWordFragment).commit()

        UserCenter.get()!!.deleteDownFile()


    }

    fun changeFragment(type: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        if (type == 1) {
            transaction.show(passWordFragment).hide(userIDFragment).commit()
        } else if (type == 2) {
            transaction.show(userIDFragment).hide(passWordFragment).commit()
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        LinPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, object : LinPermission.PermissionsResultListener {
            override fun onRequestPermissionSuccess(pos: Int, permission: String) {

            }

            override fun onRequestPermissionFailure(pos: Int, permission: String) {
                finish()
            }
        })
    }

}
