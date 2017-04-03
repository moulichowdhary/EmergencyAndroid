package com.example.s525721.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{
    EditText name, email, password, confirmPassword, user919;
    Boolean success = true;
    ImageButton policy;
    int countUpper = 0;
    int countLower = 0;
    int countDigit = 0;
    int countSpecialChar = 0;
    LinearLayout policyPop;

    String toast = "";
    public void toast(String toast){
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
    }

public void popCancel(View view){
    policyPop.setVisibility(View.INVISIBLE);
}

    public static final String SPECIAL_CHARACTERS = "!@#$%^&*()~`-=_+[]{}|:\";',./<>?";
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 20;

    public  boolean isAcceptablePassword(String password) {
        if (password.isEmpty()) {
            System.out.println("empty string.");
            success = false;
        }
        password = password.trim();
        int len = password.length();
        if (len < MIN_PASSWORD_LENGTH || len > MAX_PASSWORD_LENGTH) {
           // toast("wrong size, it must have at least 8 characters and less than 20.");
            success = false;
        } else {
            char[] aC = password.toCharArray();
            for (char c : aC) {
                if (Character.isUpperCase(c)) {
                    System.out.println(c + " is uppercase.");
                    countUpper++;
                } else if (Character.isLowerCase(c)) {
                    System.out.println(c + " is lowercase.");
                    countLower++;
                } else if (Character.isDigit(c)) {
                    System.out.println(c + " is digit.");
                    countDigit++;
                } else if (SPECIAL_CHARACTERS.indexOf(String.valueOf(c)) >= 0) {
                    System.out.println(c + " is valid symbol.");
                    countSpecialChar++;
                } else {
                    toast(c + "  is an invalid character in the password.");
                    success = false;
                    //return success;
                }
            }
            if (countUpper < 1 || countDigit < 1 || countLower < 1 || countSpecialChar < 1) {
                toast = "password doesn't match requirements";
                toast(toast);
                success = false;
            } else {
                toast("password success");
                success = true;
            }

        }
        return success;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        user919 = (EditText) findViewById(R.id.editText8);
        name = (EditText) findViewById(R.id.editText2);
        email = (EditText) findViewById(R.id.editText3);
        password = (EditText) findViewById(R.id.editText4);
        confirmPassword = (EditText) findViewById(R.id.editText6);
         policyPop = (LinearLayout)findViewById(R.id.popLayout);
        policyPop.setVisibility(View.INVISIBLE);
policy = (ImageButton)findViewById(R.id.passwordPolicy);

policy.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        policyPop.setVisibility(View.VISIBLE);

    }
});

//button
        Button button3 = (Button) findViewById(R.id.button3);
password.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
           // initiatePopupWindow();

        return true;
    }
});

        //reset button
        Button reset = (Button) findViewById(R.id.buttonR);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               EditText userId = (EditText) findViewById(R.id.editText8);
                EditText userName = (EditText) findViewById(R.id.editText2);
               EditText emailId = (EditText) findViewById(R.id.editText3);
                EditText pswd = (EditText) findViewById(R.id.editText4);
                EditText cPassword = (EditText) findViewById(R.id.editText6);
                userId.setText("");
                userName.setText("");
                emailId.setText("");
                pswd.setText("");
                cPassword.setText("");
            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (savedata() == true) {

                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);

                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Row not inserted", Toast.LENGTH_SHORT).show();

                }
    }
});


        RelativeLayout regRelative = (RelativeLayout) findViewById(R.id.activity_registration);
        regRelative.setOnClickListener(this);

    }
//pop Over




   // Read more: http://www.androidhub4you.com/2012/07/how-to-create-popup-window-in-android.html#ixzz4aEdErRr1

    public Boolean savedata() {
       // Boolean success = true;

        //address = (EditText) findViewById(R.id.editText5);
        String user919ID = user919.getText().toString();
        String nameOfUser = name.getText().toString();
        String eMail = email.getText().toString();


        String pass = password.getText().toString();
        String cpass = confirmPassword.getText().toString();
       // String addrs = address.getText().toString();

//        ParseObject users = new ParseObject("Users");
//        users.put("name", String.valueOf(nameOfUser));
//        users.put("email", String.valueOf(eMail));
//        users.put("password", String.valueOf(pass));
//        users.put("confirmPassword", String.valueOf(cpass));
//        users.put("address", String.valueOf(addrs));
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();
        ParseUser user = new ParseUser();

        //user.put("name", String.valueOf(nameOfUser));
        //user.put("email", String.valueOf(eMail));
       // user.put("password", String.valueOf(pass));
        user.put("confirmPassword", String.valueOf(cpass));
        //user.put("address", String.valueOf(addrs));
        user.put("user919",user919ID);
        user.setUsername(user919ID);
        //user.setUsername(nameOfUser);
        user.setPassword(pass);
        user.setEmail(eMail);
//Validations

        //email validation
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (user919ID
                .isEmpty()) {
            Toast.makeText(getApplicationContext(), "please enter 919#", Toast.LENGTH_SHORT).show();
            success = false;
        }else if (nameOfUser.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter valid name", Toast.LENGTH_SHORT).show();
            success = false;
        } else if (!eMail.matches(emailPattern)) {
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            success = false;
        } else if (pass.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
            success = false;
        }else if (!pass.isEmpty()){
            success = isAcceptablePassword(pass);
            System.out.print(success);
        }

        if (!cpass.equals(pass)) {
            Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();
            success = false;

        } else if (isAcceptablePassword(pass)) {
//System.out.print("Entereesd");
//data saved to database


//            user.saveInBackground();
//            Toast.makeText(getApplicationContext(), "Row inserted", Toast.LENGTH_SHORT).show();
//            success = true;

            user.signUpInBackground(new SignUpCallback() {

                @Override
                public void done(ParseException e) {

                    if (e != null) {
                        Log.d(e.toString(),"Error occured");
                        Toast.makeText(RegistrationActivity.this,
                                "Saving user failed.", Toast.LENGTH_SHORT).show();


                        if (e.getCode() == 202) {

                            Toast.makeText(
                                    RegistrationActivity.this,
                                    "Username already taken. \n Please choose another username.",
                                    Toast.LENGTH_LONG).show();
                            name.setText("");
                            password.setText("");
                            confirmPassword.setText("");

                        }
                        success = false;


                    } else {

                        Toast.makeText(RegistrationActivity.this, "User Saved",
                                Toast.LENGTH_SHORT).show();
                        success = true;


                    }

                }
            });
        }
        return success;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_registration  ){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }
}