package com.module.login.util

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.helper.develop.util.toast
import com.module.login.R

fun getGoogleLoginIntent(context: Context): Intent?{

  try {
      if(GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)!= ConnectionResult.SUCCESS){
          context.toast(R.string.login_error_google_play_missing)
          return null
      }
      val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
          .requestEmail()
//          .requestIdToken("479008914281-1he3fjaob6b073b0f0crk72e5q8vbpa5.apps.googleusercontent.com")
          .requestIdToken("479008914281-on799intlk4kr1879pdmvcqcdcogughc.apps.googleusercontent.com")
          .build()
      val googleSignInClient = GoogleSignIn.getClient(context, gso)
      return googleSignInClient.signInIntent
  }catch (e: Exception){
      e.printStackTrace()
      return null
  }
}
