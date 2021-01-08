package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorial_v1.R;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONException;
import org.json.JSONObject;

import Ultilities.Ultitities;
import es.dmoral.toasty.Toasty;
import Retrofit.ICourseService;
import io.reactivex.rxjava3.annotations.NonNull;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import Retrofit.ServiceClient;

public class PaymentActivity extends AppCompatActivity {
    CardInputWidget cardinput;
    Button paybtn;
    TextView result;
    JSONObject _model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initUIs();
        initEvents();

    }

    private void initUIs(){
        cardinput = findViewById(R.id.Payment_CardInput);
        paybtn = findViewById(R.id.Payment_PayButton);
        result = findViewById(R.id.Payment_result);
    }

    private void initEvents(){
        paybtn.setOnClickListener(v -> {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Card card = cardinput.getCard();
            if(card != null){
                Stripe stripe=new Stripe(PaymentActivity.this,"pk_test_y8urHXEikr7ysm3tk7uRilcp00aTSdh57w");
                stripe.createCardToken(
                        card,
                        new ApiResultCallback<Token>() {
                            public void onSuccess(@NonNull Token token) {

                                try {
                                    _model = new JSONObject();
                                    _model.put("idUser", pref.getString("_id", ""));
                                    _model.put("cart", Ultitities.GetCoursesOnCart(pref));
                                    _model.put("token", new JSONObject()
                                            .put("id", token.getId())
                                            .put("type", "card")
                                            .put("card", new JSONObject()
                                                    .put("id", token.getCard().getId())
                                                    .put("object", "card")
                                            ));
                                    Pay();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toasty.error(PaymentActivity.this, "Thanh toán thất bại !", Toast.LENGTH_SHORT, true).show();
                                }

                            }
                            public void onError(@NonNull Exception error) {


                            }
                        }
                );
            }
        });
    }

    private void Pay(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ICourseService service = ServiceClient.getInstance().create(ICourseService.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), _model.toString());
        service.pay(body).enqueue(new Callback<Response<String>>() {
            @Override
            public void onResponse(Call<Response<String>> call, Response<Response<String>> response) {
                if(response.code() == 200){
                    Ultitities.ClearCart(pref);
                    Intent intent=new Intent(PaymentActivity.this,HomeActivity.class);
                    startActivity(intent);
                    Toasty.success(PaymentActivity.this, "Thanh toán thành công !", Toast.LENGTH_SHORT, true).show();
                }
                else {
                    Toasty.error(PaymentActivity.this, "Thanh toán thất bại !", Toast.LENGTH_SHORT, true).show();
                }
            }
            @Override
            public void onFailure(Call<Response<String>> call, Throwable t) {
                Toasty.error(PaymentActivity.this, "Thanh toán thất bại !", Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}