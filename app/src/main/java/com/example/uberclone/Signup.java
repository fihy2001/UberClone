package com.example.uberclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class Signup extends AppCompatActivity implements View.OnClickListener {
    enum State {
        SIGNUP, LOGIN
    }

    private State state;
    private EditText edtUsername, edtPassword, edtOption;
    private RadioButton rbPassenger, rbDriver;
    private Button btnSignUp, btnOneTimeLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Uber Clone");
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtOption = findViewById(R.id.edtDOrP);
        rbPassenger = findViewById(R.id.rbPassenger);
        rbDriver = findViewById(R.id.rbDriver);
        btnSignUp = findViewById(R.id.btnSignUpLogin);
        btnOneTimeLogin = findViewById(R.id.btnOneTimeLogin);
        btnOneTimeLogin.setOnClickListener(this);

        state = State.SIGNUP;

        ParseInstallation.getCurrentInstallation().saveInBackground();
        if (ParseUser.getCurrentUser() != null) {
            //ParseUser.logOut();
            transitionToPassengerActivity();
            transitionToDriverRequestListActivity();
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((state == State.SIGNUP)){
                    if (rbDriver.isChecked() == false && rbPassenger.isChecked() == false){
                        Toast.makeText(Signup.this, "Are you a driver or passenger?", Toast.LENGTH_LONG).show();
                        return;
                    }
                    ParseUser appUser = new ParseUser();
                    appUser.setUsername(edtUsername.getText().toString());
                    appUser.setPassword(edtPassword.getText().toString());
                    if (rbDriver.isChecked()){
                        appUser.put("as","Driver");
                    } else if (rbPassenger.isChecked()){
                        appUser.put("as", "Passenger");
                    }
                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if ((e == null)){
                                Toast.makeText(Signup.this, "User has been signed up", Toast.LENGTH_LONG).show();
                                transitionToPassengerActivity();
                                transitionToDriverRequestListActivity();
                            }
                        }
                    });
                } else if (state == State.LOGIN){
                    ParseUser.logInInBackground(edtUsername.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            Toast.makeText(Signup.this, "User logged in", Toast.LENGTH_LONG).show();
                            transitionToPassengerActivity();
                            transitionToDriverRequestListActivity();

                        }
                    });
                }
            }
        });

    }

    @Override
    public void onClick(View view) {

        if (edtOption.getText().toString().equals("Driver") || edtOption.getText().toString().equals("Passenger")){
            if (ParseUser.getCurrentUser() == null){
                ParseAnonymousUtils.logIn(new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null && e == null){
                            Toast.makeText(Signup.this, "We have an anonymous user", Toast.LENGTH_LONG).show();
                            user.put("as", edtOption.getText().toString());
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    transitionToPassengerActivity();
                                    transitionToDriverRequestListActivity();
                                }
                            });

                        }
                    }
                });
            }

        } else {
            Toast.makeText(Signup.this, "Are you a Driver or Passenger", Toast.LENGTH_LONG).show();
        }

        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_login:
                if (state == state.SIGNUP){
                    state = State.LOGIN;
                    item.setTitle("SIGN UP");
                    btnSignUp.setText("LOG IN");
                } else if (state == State.LOGIN){
                    state = State.SIGNUP;
                    item.setTitle("LOG IN");
                    btnSignUp.setText("SIGN UP");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void transitionToPassengerActivity(){
        if (ParseUser.getCurrentUser() != null) {
            if (ParseUser.getCurrentUser().get("as").equals("Passenger")){
                Intent intent = new Intent(Signup.this, PassengerActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
    private void transitionToDriverRequestListActivity(){
        if (ParseUser.getCurrentUser() != null) {
            if (ParseUser.getCurrentUser().get("as").equals("Driver")){
                Intent intent = new Intent(Signup.this, DriverRequestListActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}