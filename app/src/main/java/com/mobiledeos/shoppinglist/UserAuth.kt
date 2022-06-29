package com.mobiledeos.shoppinglist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object UserAuth {
    const val TAG = "UserAuth"
    private const val RC_SIGN_IN = 9001

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var launcher: ActivityResultLauncher<Intent>

    fun init(activity: AppCompatActivity) {
        auth = Firebase.auth

        launcher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(activity, account.idToken!!)
                }
            } catch (e: ApiException) {
                e.message?.let { it1 -> Log.e(TAG, it1.toString()) }
            }
        }
    }

    private fun firebaseAuthWithGoogle(activity: Activity, idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    fun signIn(activity: Activity) {
        val signInClient = getClient(activity)
        launcher.launch(signInClient.signInIntent)
    }

    private fun getClient(activity: Activity): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(activity, gso)
    }
}