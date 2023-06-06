package esprims.gi2.ma_pharmacie.useCase

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import esprims.gi2.ma_pharmacie.presentation.onBoarding.dataStore
import esprims.gi2.ma_pharmacie.presentation.shared.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



     suspend fun saveJwtLocally(jwt: String,context:Context) {


         val jwtKey = stringPreferencesKey("jwt")

         context.dataStore.edit { pref ->
                pref[jwtKey] = jwt
                Log.d("save jwt",jwt)
                Constants.JWT=jwt

        }
    }
suspend fun deleteJwtLocally(context: Context) {
    val jwtKey = stringPreferencesKey("jwt")

    context.dataStore.edit { pref ->
        pref.remove(jwtKey)
        Log.d("delete jwt", "JWT deleted")
        Constants.JWT = ""
    }
}

