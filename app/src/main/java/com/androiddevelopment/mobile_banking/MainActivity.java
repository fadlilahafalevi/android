package com.androiddevelopment.mobile_banking;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androiddevelopment.mobile_banking.helper.InputValidation;
import com.androiddevelopment.mobile_banking.sql.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    //declare vars
    private final AppCompatActivity activity = MainActivity.this;
    String UserName, Password;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    EditText textInputEditTextEmail;
    EditText textInputEditTextPassword;

    Button myButton;
    AppCompatTextView textRegister;

    int loginCntr = 3;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;

    private void initObjects() {
        databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);
    }

    private void initViews() {
        //reference Button, User Name and Password
        myButton = (Button) findViewById(R.id.loginButton);
        textRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.usernameTextView);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.passwordTextView);

        textInputEditTextEmail = (EditText) findViewById(R.id.usernameEditText);
        textInputEditTextPassword = (EditText) findViewById(R.id.passwordEditText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        initObjects();
        initViews();

        //display welcome msg when app starts
        Toast.makeText(activity, "Welcome User. Please enter your Username and Password.", Toast.LENGTH_LONG).show();

        //register button with Event Listener class, and Event handler method
        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //get user input
                UserName = textInputEditTextEmail.getText().toString();
                Password = textInputEditTextPassword.getText().toString();

                //check how many times unregistered user tried to log in with wrong credentials

                verifyFromSQLite();

            }//end onClick
        });//end setOnClickListener

        textRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
            }
        });

    }//end onCreate

    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_email))) {
            return;
        }

        if (databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim()
                , textInputEditTextPassword.getText().toString().trim())) {


            Intent myIntent = new Intent(MainActivity.this, MenuActivity.class);
            myIntent.putExtra("stringReference", "Access Granted!");
            //display menu activity screen
            startActivity(myIntent);
            emptyInputEditText();


        } else if (loginCntr != 1) {
            //unregistered user, display unregistered user msg and decrease login counter
            loginCntr = loginCntr - 1;

            Toast.makeText(activity, "Access Denied! Please try again.You have " + loginCntr + " attempt(s) remaining", Toast.LENGTH_LONG).show();
        }//end else if
        else {
            //3 login attempts are up, close app
            Toast.makeText(activity, "Access Denied! Closing app!", Toast.LENGTH_LONG).show();
            finish();
        }//end else
    }

    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
}//end MainActivity
