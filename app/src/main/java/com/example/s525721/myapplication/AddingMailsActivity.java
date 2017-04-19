package com.example.s525721.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

public class AddingMailsActivity extends AppCompatActivity {
    EditText recipientMail;
    Button addRecipientBTN;
    ListView recipientMailsList;
    ArrayList<String> emails;
    ArrayAdapter emailArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_mails);

        recipientMail = (EditText) findViewById(R.id.addEmailET);
        addRecipientBTN = (Button) findViewById(R.id.addEmailBTN);
        recipientMailsList = (ListView) findViewById(R.id.mailsListView);
        emails = new ArrayList<String>();

        emailArray = new ArrayAdapter(this,android.R.layout.simple_list_item_1,emails);
        recipientMailsList.setAdapter(emailArray);




        fetchData();

        addRecipientBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailArray.notifyDataSetChanged();
                //emails.add(recipientMail.getText().toString());
                ParseObject recipients = new ParseObject("EmailRecipients");
                String mailString = recipientMail.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (mailString.matches(emailPattern)) {
                    recipients.put("Username", ParseUser.getCurrentUser().getUsername());
                    recipients.put("recipientEmail", mailString);
                    emails.add(mailString);



                    //recipients.put("email", recipientMail.getText().toString());


                    recipients.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                //fetchData();
                                Toast.makeText(AddingMailsActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                            } else {
                                //Log.i("Error: ", e.getMessage());
                                Toast.makeText(AddingMailsActivity.this, "Email not added to the list", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(AddingMailsActivity.this,"Please enter correct email",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void fetchData(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("EmailRecipients");
        query.whereEqualTo("Username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                if (e==null) {


                    //Toast.makeText(AddingMailsActivity.this, "Retrieved", Toast.LENGTH_SHORT).show();

                    if (list.size() > 0) {
                        for (ParseObject object : list) {
                            String mail=  object.getString("recipientEmail");
                           // Log.i("Retrivd",mail);
                            emails.add(mail);
                            emailArray.notifyDataSetChanged();
                        }
                    }
                }

            }


        });

    }
}
