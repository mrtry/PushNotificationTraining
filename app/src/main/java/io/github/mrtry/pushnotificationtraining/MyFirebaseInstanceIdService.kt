package io.github.mrtry.pushnotificationtraining

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.iid.FirebaseInstanceId




/**
 * Created by mrtry on 2018/08/04.
 */
class MyFirebaseInstanceIdService: FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        //ここで取得したInstanceIDをサーバー管理者に伝える

        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: " + refreshedToken!!)
    }

    companion object {
        val TAG = MyFirebaseInstanceIdService::class.java.simpleName
    }
}