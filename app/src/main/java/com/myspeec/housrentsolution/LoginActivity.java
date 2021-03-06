package com.myspeec.housrentsolution;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

public class LoginActivity extends AppCompatActivity {
    Button login;
    TextView signup;
    boolean authorization;
    private AwesomeValidation awesomeValidation;
    ProgressDialog progressDialog;
    String EmailHolder, PasswordHolder ;
    TextView dataEmail; // a text field where the data to be sent is entered
    TextView dataPassword; // a text field where the data to be sent is entered

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        login = (Button) findViewById(R.id.buttonLogin);
        signup = (TextView) findViewById(R.id.button_signup);
        login.setOnClickListener(mMyListener);
        signup.setOnClickListener(mMyListener);

        dataEmail = (TextView) findViewById(R.id.editTextEmail);
        dataPassword = (TextView) findViewById(R.id.editPassword);

        awesomeValidation.addValidation(this, R.id.editTextEmail, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        progressDialog = new ProgressDialog(LoginActivity.this);


    }
    private View.OnClickListener mMyListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId() /*to get clicked view id**/) {
                case R.id.buttonLogin:
                    if (awesomeValidation.validate()) {
                        //Toast.makeText(LoginActivity.this, "Checking", Toast.LENGTH_LONG).show();
                         regUser();
                    }
                    break;
                case R.id.button_signup:
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    break;
                default:
                    break;
            }
        }
    };

    public void regUser(){
        progressDialog.setMessage("Please Wait, Your Email and Password is Matching");
        progressDialog.show();

        EmailHolder = dataEmail.getText().toString().trim();
        PasswordHolder = dataPassword.getText().toString().trim();

        String myurl = "http://192.168.43.97/myphp/login.php?email=" +
                "&email=" + EmailHolder + "&password=" + PasswordHolder;

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, myurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();
                        if (ServerResponse.equals("1")){
                            authorization = true;
                            Toast.makeText(LoginActivity.this, ServerResponse, Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(), HomePage.class);
                            startActivity(i);
                        }
                        else {
                            progressDialog.setMessage("Your Email or Password incorrect");
                            progressDialog.show();
                            authorization = false;
                            //Toast.makeText(LoginActivity.this, "Email or password invalide", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();
                        // Showing error message if something goes wrong.
                        Toast.makeText(LoginActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(stringRequest);
    }
}
