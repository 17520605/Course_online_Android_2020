package Ultilities;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import Model.User;

public class Ultitities {

    public static Date StringToDate(String datestring) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return format.parse(datestring);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String DateToString(Date date){
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public static String FortmatDateString(String datestring){
        return DateToString(StringToDate(datestring));
    }

    public static String ConvertToCurrency(String price){
        return ConvertToCurrency(Long.parseLong(price));
    }

    public static String ConvertToCurrency(long price){
        if(price==0){
            return "Free";
        }
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("EUR"));
        String str = format.format(price);
        str = str.substring(1);
        str = str.concat(" đ");
        return  str;
    }

    public static JSONArray GetCoursesOnCart(SharedPreferences pref) {
        JSONArray courses = null;
        if(pref != null){
            try {
                courses = new JSONArray(pref.getString("coursesoncart", "[]"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return courses;
    }

    public static void ClearCart(SharedPreferences pref) {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("coursesoncart");
        editor.commit();
    }

    public static User GetUser(SharedPreferences pref){
        return new User(
            pref.getString("active",""),
            pref.getString("created_at",""),
            pref.getString("_id",""),
            pref.getString("email",""),
            pref.getString("name",""),
            pref.getString("password",""),
            pref.getString("phone",""),
            pref.getString("address",""),
            pref.getString("description",""),
            pref.getString("role",""),
            pref.getString("image",""),
            pref.getString("gender",""),
            pref.getString("__v",""),
            pref.getString("activeToken",""),
            pref.getString("authToken","")
        );
    }

    public static void SaveUser(SharedPreferences pref, User user){
        if(pref != null && user != null){
            SharedPreferences.Editor editor = pref.edit();
            editor.remove("active");
            editor.remove("created_at");
            editor.remove("_id");
            editor.remove("name");
            editor.remove("email");
            editor.remove("phone");
            editor.remove("address");
            editor.remove("description");
            editor.remove("role");
            editor.remove("image");
            editor.remove("gender");
            editor.remove("__v");
            editor.remove("activeToken");

            editor.putString("active",user.getActive());
            editor.putString("created_at",user.getCreated_at());
            editor.putString("_id",user.get_id());
            editor.putString("name",user.getName());
            editor.putString("email",user.getEmail());
            editor.putString("phone",user.getPhone());
            editor.putString("address",user.getAddress());
            editor.putString("description",user.getDescription());
            editor.putString("role",user.getRole());
            editor.putString("image",user.getImage());
            editor.putString("gender",user.getGender());
            editor.putString("__v",user.get__v());
            editor.putString("activeToken",user.getActiveToken());

            editor.commit();
        }
    }

    public static void RemovePreferences(SharedPreferences pref){
        pref.edit().clear().commit();
    }
}
