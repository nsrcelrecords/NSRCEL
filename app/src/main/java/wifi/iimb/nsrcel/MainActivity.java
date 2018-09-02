package wifi.iimb.nsrcel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    EditText name , phonenumber;
    String[] namesarray , phonenumberarray ,email_inmate, tokenarray_inmate , user_namearray , validity, date_inmate;
    Button b;
    String password , type1,expired_password_inmate,expired_username_inmate, formattedDate , remaining_days,expired_email_inmate;
    private RequestQueue requestQueue;
    private StringRequest request;
    JSONObject jsonArray;
    int inmate , guest, inmateindex;
    TextView guest12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        name = (EditText) findViewById(R.id.myname);
        phonenumber = (EditText) findViewById(R.id.myphone);
        b = (Button) findViewById(R.id.submit);
        guest12 = (TextView) findViewById(R.id.guest);


        request = new StringRequest(Request.Method.GET, Url_info.URL , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    String flag = response;
                    Log.e(flag, "onResponse:dailydarshan ");
                    try {
                        jsonArray = new JSONObject(response);
                        Log.e(String.valueOf(jsonArray), "onResponse:");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    JSONArray obj = null;
                    try {
                        obj = (JSONArray) (jsonArray.get("user"));
                        namesarray = new String[obj.length()];
                        phonenumberarray = new String[obj.length()];
                        tokenarray_inmate = new String[obj.length()];
                        user_namearray = new String[obj.length()];
                        validity = new String[obj.length()];
                        date_inmate = new String[obj.length()];
                        email_inmate = new String[obj.length()];
                        for (int j = 0; j < obj.length(); j++) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = (JSONObject) (obj.get(j));
                                String name = jsonObject.optString("name");
                                String phonenumberget = jsonObject.optString("phone_number");
                                String email = jsonObject.optString("email");
                                String tokenget = jsonObject.optString("token_inmate");
                                String user_name = jsonObject.optString("username");
                                String validity_token = jsonObject.optString("validity");
                                String date = jsonObject.optString("date");
                                date_inmate[j]= date;
                                validity[j] = validity_token;
                                user_namearray[j] = user_name;
                                email_inmate[j]=email;
                                namesarray[j] = name;
                                phonenumberarray[j] = phonenumberget;
                                tokenarray_inmate[j] = tokenget;
                                Log.e( namesarray[j],"onResponse: name" );
                                Log.e(tokenarray_inmate[j], "onResponse: token" );


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Please check your internet connection .",Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();


                return hashMap;

            }
        };

        requestQueue.add(request);



        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final String myname = name.getText().toString().trim();
                Log.e(myname, "onClick: name");
                String myphone = phonenumber.getText().toString().trim();
                final String phone_expired_inmate=myphone;
                int flag = 0;
                final String id = "1L-8iuRCWLaHkwsAbTOLuni3sfJFpXe51DYeOSUSA5Cw";
                if (myname.length() == 0 || myphone.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < namesarray.length; i++) {
                        Log.e(String.valueOf(myname.equals(namesarray[i])), "onClick: comparison");
                        if (namesarray[i].equalsIgnoreCase(myname)) {
                            if (phonenumberarray[i].equals(myphone)) {
                                if( tokenarray_inmate[i].length() != 0 ) {
                                    flag = 1;
                                    myphone = "+91" + myphone;
                                    inmateindex = i + 2;
                                    type1 = "i";
                                    expired_password_inmate = tokenarray_inmate[i];
                                    expired_username_inmate = user_namearray[i];
                                    expired_email_inmate = email_inmate[i];
                                    Log.e(date_inmate[i], "onClick:Before substringing...... ");
                                    final String login_date = date_inmate[i].substring(0, 10);
                                    Log.e(login_date, "onClick: login_date");
                                    String validity_of_token = validity[i];
                                    Log.e(validity_of_token, "onClick: validity");
                                    Date c = Calendar.getInstance().getTime();
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
                                    formattedDate = df.format(c);
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                    Log.e(login_date, "onClick:  this is the login date");
                                    Log.e(formattedDate, "onClick: No clue lol ");

                                    try {
                                        Date date1 = simpleDateFormat.parse(login_date);
                                        Date date2 = simpleDateFormat.parse(formattedDate);

                                        long days = printDifference(date1, date2);
                                        remaining_days = String.valueOf(days);


                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }


                                    if (Integer.parseInt(remaining_days) <= Integer.parseInt(validity_of_token)) {
                                        Intent intent_1 = new Intent(MainActivity.this, Ticket.class);
                                        intent_1.putExtra("Username", user_namearray[i]);
                                        intent_1.putExtra("Password", tokenarray_inmate[i]);
                                        intent_1.putExtra("type", type1);
                                        intent_1.putExtra("number", myphone);
                                        startActivity(intent_1);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Your token has expired, you will get a new one.", Toast.LENGTH_LONG).show();
                                        request = new StringRequest(Request.Method.POST, Url_info.URL_INMATE_EXPIRED, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                try {
                                                    String flag = response;


                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        }) {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Log.e(expired_email_inmate, "getParams: One last time");
                                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                                hashMap.put("token", expired_password_inmate);
                                                hashMap.put("date", login_date);
                                                Log.e(login_date, "getParams: lllllllllllllll");
                                                hashMap.put("UserName", expired_username_inmate);
                                                hashMap.put("accountname", myname);
                                                hashMap.put("number", phone_expired_inmate);
                                                hashMap.put("email", expired_email_inmate);

                                                return hashMap;

                                            }
                                        };

                                        requestQueue.add(request);
                                        Intent intent = new Intent(MainActivity.this, otp.class);
                                        intent.putExtra("Username", myname);
                                        intent.putExtra("type", type1);
                                        intent.putExtra("number", myphone);
                                        intent.putExtra("inmateindex", String.valueOf(inmateindex));
                                        startActivity(intent);


                                    }
                                }
                                else if(tokenarray_inmate[i].length() == 0) {
                                    type1 = "i";
                                    myphone = "+91" + myphone;
                                    inmateindex = i + 2;
                                    flag = 1;
                                    Intent intent = new Intent(MainActivity.this, otp.class);
                                    intent.putExtra("Username", myname);
                                    intent.putExtra("type", type1);
                                    intent.putExtra("number", myphone);
                                    intent.putExtra("inmateindex", String.valueOf(inmateindex));
                                    startActivity(intent);
                                }



                            } else {
                                Toast.makeText(getApplicationContext(), "Phone number is incorrect", Toast.LENGTH_LONG).show();
                            }


                        }
                    }
                    if (flag != 1)
                        Toast.makeText(getApplicationContext(), "User not registered, please contact the administration.", Toast.LENGTH_LONG).show();


                }
            }
        });


        guest12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this,login.class);
                startActivity(intent1);
            }
        });
    }
    public long printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;
        elapsedDays -=1;

        Log.e(String.valueOf(elapsedDays), "onClick: Checking for date");
        return elapsedDays;
    }

}
