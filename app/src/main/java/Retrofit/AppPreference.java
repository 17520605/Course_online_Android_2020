package Retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import Model.UserAccount;

public class AppPreference {
    public static UserAccount GetUserAccount(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return new UserAccount(
                pref.getString("name", "default"),
                "",
                pref.getString("phone", "default"),
                pref.getString("image", "default"),
                pref.getString("email", "default"),
                pref.getString("id", "default"),
                pref.getString("token", "default"),
                pref.getString("gender", "default"),
                pref.getString("description", "default"),
                pref.getString("address", "default"),
                pref.getString("password", "default")
        );
    }

    public static void Remove(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().clear().commit();
    }
}
