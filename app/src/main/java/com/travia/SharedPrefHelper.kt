package com.travia

import android.content.Context
import androidx.core.content.edit
import com.travia.utils.SHARED_PREF_NAME

class SharedPrefHelper(private val context: Context) {

    private var sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    fun setPreferences(uuid: String, user_role: String){
        sharedPreferences.edit {
            putString(TAG_uuid, uuid)
            putString(TAG_user_role, user_role)
            apply()
        }
    }

    fun deletePreferences(){
        sharedPreferences.edit {
            putString(TAG_uuid, null)
            putString(TAG_user_role, null)
            apply()
        }
    }

    fun getPrefencesRole(): String? = sharedPreferences.getString(TAG_user_role, null)

    companion object {
        var TAG_uuid = "uuid"
        var TAG_user_role = "user_role"
    }

}