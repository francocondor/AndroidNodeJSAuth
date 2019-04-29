package com.example.androidnodejsauth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.androidnodejsauth.Retrofit.INodeJS;
import com.example.androidnodejsauth.Retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    MaterialEditText edt_email,edt_password;
    MaterialButton btn_register,btn_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        btn_login = (MaterialButton)findViewById(R.id.login_button);
        btn_register = (MaterialButton)findViewById(R.id.register_button);

        edt_email = (MaterialEditText) findViewById(R.id.edt_email);
        edt_password = (MaterialEditText) findViewById(R.id.edt_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(edt_email.getText().toString(),edt_password.getText().toString());
            }
        });

    }

    private void loginUser(final String email, String password) {
        compositeDisposable.add(myAPI.loginUser(email,password)
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if(s.contains("encrypted_password")){
                            JSONObject jsonObj = new JSONObject(s);

                            Log.e("Datos1",""+jsonObj.get("id"));
                            Log.e("Datos2",""+jsonObj.get("name"));
                            Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, ""+s, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        );
    }
}