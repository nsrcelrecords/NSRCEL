package wifi.iimb.nsrcel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import static wifi.iimb.nsrcel.Url_info.URL_GUEST_EXPIRED;
import static wifi.iimb.nsrcel.Url_info.URL_Guest_delete;
import static wifi.iimb.nsrcel.Url_info.URL_check;

public class login extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    private RequestQueue requestQueue;
    private StringRequest request;
    String password , type1, formattedDate,login_date,remaining_days,Expired_username,Expired_phonenumber,expired_email_inmate;
    int guest;
    EditText name , phonenumber , email , reference;   // added email, refered person
    Button b;
    int flag1 = 0;
    JSONObject jsonArray;
    String[] emailarray , tokenarray , user_name_array , validity, logindate,phonenumber_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        name = (EditText) findViewById(R.id.myname);
        email = (EditText) findViewById(R.id.email);  // edit text needed
        reference = (EditText) findViewById(R.id.reference); //  edit text needed
        b = (Button) findViewById(R.id.submit);
        requestQueue = Volley.newRequestQueue(this);
        sharedpreferences = getSharedPreferences("Count", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String myname = name.getText().toString();
                Log.e( myname,"onClick: name");
                final String emailID = email.getText().toString();
                Log.e( emailID ,"onClick: email");
                int flag = 0;
                final String myreference = reference.getText().toString();  // added new reference
                request = new StringRequest(Request.Method.GET, Url_info.URL_check , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            String flag = response;
                            try {
                                jsonArray = new JSONObject(response);
                                Log.e(String.valueOf(jsonArray), "onResponse:");


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            JSONArray obj = null;
                            try {
                                obj = (JSONArray) (jsonArray.get("user"));
                                emailarray = new String[obj.length()];
                                tokenarray = new String[obj.length()];
                                user_name_array = new String[obj.length()];
                                phonenumber_array = new String[obj.length()];
                                validity = new String[obj.length()];
                                logindate = new String[obj.length()];
                                for (int j = 0; j < obj.length(); j++) {
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = (JSONObject) (obj.get(j));
                                        String email = jsonObject.optString("email");
                                        String token = jsonObject.optString("token");
                                        String user_name = jsonObject.optString("username");
                                        String validity_token_guest = jsonObject.optString("validity");
                                        String phonenumeber = jsonObject.optString("phonenumber");
                                        String login= jsonObject.optString("date");
                                        validity[j] = validity_token_guest;
                                        logindate[j]= login;
                                        phonenumber_array[j]=phonenumeber;
                                        user_name_array[j] = user_name;
                                        emailarray[j] = email;
                                        tokenarray[j] = token;
                                        Log.e( emailarray[j],"onResponse: name" );


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                Date c = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                formattedDate = df.format(c);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");




                                for( int i = 0; i < emailarray.length; i++ )
                                {
                                    if(emailID.equals(emailarray[i]))
                                    {
                                        flag1 = 1;
                                        password = tokenarray[i];
                                        type1 = "g";
                                        Expired_username= user_name_array[i];
                                        Expired_phonenumber= phonenumber_array[i];
                                        expired_email_inmate=emailarray[i];
                                        login_date=logindate[i].substring(0,10);
                                        final String index_delete = String.valueOf(i+2);
                                        String validity_today = validity[i];
                                        try {
                                            Date date1 = simpleDateFormat.parse(login_date);
                                            Date date2 = simpleDateFormat.parse(formattedDate);

                                            long days= printDifference(date1, date2);
                                            remaining_days=String.valueOf(days);


                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        String password_already_present = "true";

                                            Intent intent_here = new Intent(login.this, otp.class);
                                            intent_here.putExtra("Username", user_name_array[i]);
                                            intent_here.putExtra("Password", password);
                                            intent_here.putExtra("type", type1);
                                            intent_here.putExtra("number", Expired_phonenumber);
                                            intent_here.putExtra("password_exist", password_already_present);
                                            intent_here.putExtra("remaining_days", remaining_days);
                                            intent_here.putExtra("name", myname);
                                            intent_here.putExtra("email", expired_email_inmate);
                                            intent_here.putExtra("delete", index_delete);
                                            intent_here.putExtra("reference",myreference );
                                            intent_here.putExtra("login_date", login_date );
                                            startActivity(intent_here);

                                        /* {
                                            Toast.makeText(getApplicationContext(),"Your token has expired, you will get a new one.",Toast.LENGTH_LONG).show();
                                            request = new StringRequest(Request.Method.POST, Url_info.URL_GUEST_EXPIRED , new Response.Listener<String>() {
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
                                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                                    hashMap.put("token",password);
                                                    hashMap.put("date", login_date);
                                                    hashMap.put("UserName",Expired_username);
                                                    hashMap.put("accountname", myname);
                                                    hashMap.put("number",Expired_phonenumber);
                                                    Log.e(Expired_phonenumber, "getParams: Phone number to be passed in expired guest page");
                                                    hashMap.put("email", expired_email_inmate);

                                                    return hashMap;

                                                }
                                            };

                                            requestQueue.add(request);

                                            request = new StringRequest(Request.Method.POST, Url_info.URL_Guest_delete , new Response.Listener<String>() {
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
                                                    hashMap.put("token_index",index_delete);


                                                    return hashMap;

                                                }
                                            };

                                            requestQueue.add(request);








                                            Intent intent = new Intent(login.this , otp.class);
                                            intent.putExtra("Username", myname);
                                            intent.putExtra("type", type1);
                                            intent.putExtra("email", emailID);
                                            intent.putExtra("reference", myreference);
                                            startActivity(intent);
                                        } */
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

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<String, String>();


                        return hashMap;

                    }
                };

                requestQueue.add(request);



                guest += 1;
                editor.putString("count_var", String.valueOf(guest));
                editor.commit();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String name_count = preferences.getString("count_var", "");
                type1 = "g";
                String password_already_present = "";
                Intent intent = new Intent(login.this , otp.class);
                if(email.length() == 0||name.length() == 0 || reference.length() ==0 )
                {
                    Toast.makeText(getApplicationContext(),"Please fill all fields",Toast.LENGTH_LONG).show();
                }
                else if( flag1 != 1 )
                {
                    intent.putExtra("Username", myname);
                    intent.putExtra("type", type1);
                    intent.putExtra("email", emailID);
                    intent.putExtra("reference", myreference);
                    intent.putExtra("password_exist", password_already_present);
                    intent.putExtra("remaining_days", remaining_days);
                    intent.putExtra("name", myname);
                    startActivity(intent);
                }
            }
        });

    }
    public long printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

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


