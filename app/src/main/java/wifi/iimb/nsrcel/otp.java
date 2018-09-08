package wifi.iimb.nsrcel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class otp extends AppCompatActivity {

    EditText otpnumber;
    EditText phonenumber;
    Button generate_otp;
    Button verify_otp;
    String inmate_phone;
    String Username_real1;
    String password, username, type, name_count , password_real;
    int password_real1;
    FirebaseAuth auth;
    private String verificationCode;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    final String id = "1L-8iuRCWLaHkwsAbTOLuni3sfJFpXe51DYeOSUSA5Cw";
    String name, emailID, reference;
    private RequestQueue requestQueue;
    private StringRequest request;
    int password_index;
    JSONObject jsonObject;
    String phonenumber_real = "";
    int count=0;
    String[] tokenarray , userName , validity , date;
    String inmateindex , password_exist , login_date,  remaining_days , password_from_database , login_date_from_database;
    int checker=0;
    String formattedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp);
        otpnumber = (EditText) findViewById(R.id.otp);
        phonenumber = (EditText) findViewById(R.id.number);
        final String phonenumber_real = phonenumber.getText().toString();
        Log.e(phonenumber_real, "onCreate: mobile number");
        generate_otp = (Button) findViewById(R.id.generate_otp);
        verify_otp = (Button) findViewById(R.id.verify_otp);
        username = getIntent().getStringExtra("Username");
        type = getIntent().getStringExtra("type");
        Log.e(type, "onCreate:wassup " );
        inmateindex = getIntent().getStringExtra("inmateindex");
        Log.e(inmateindex, "onCreate:inmateindex ");
        reference = getIntent().getStringExtra("reference");
        Log.e(reference, "onCreate: reference" );
        emailID = getIntent().getStringExtra("email");
        password_exist = getIntent().getStringExtra("password_exist");
        requestQueue = Volley.newRequestQueue(this);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);


        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //   Toast.makeText(otp.this, "verification completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(otp.this, "verification failed, please enter a valid number", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                // super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(otp.this, "Code sent", Toast.LENGTH_SHORT).show();
            }

        };
        if( type.equals("g") && (password_exist.length() == 0)) {
            request = new StringRequest(Request.Method.GET, Url_info.token_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        String flag = response;
                        Log.e(flag, "onResponse: tokens");
                        try {
                            jsonObject = new JSONObject(response);
                            Log.e(String.valueOf(jsonObject), "onResponse:");


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JSONArray obj = null;
                    try {
                        obj = (JSONArray) (jsonObject.get("user"));
                        tokenarray = new String[obj.length()];
                        userName = new String[obj.length()];
                        validity = new String[obj.length()];
                        if (tokenarray.length < 3) {
                            Toast.makeText(otp.this, "Please contact help desk for token .", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Log.e(String.valueOf(tokenarray.length), "onResponse: lollllllllllll ");
                            for (int j = 0; j < obj.length(); j++) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = (JSONObject) (obj.get(j));
                                    String token = jsonObject.optString("token");
                                    String userName_real = jsonObject.optString("username");
                                    String validity_token_guest = jsonObject.optString("validity");
                                    validity[j] = validity_token_guest;
                                    userName[j] = userName_real;
                                    tokenarray[j] = token;
                                    Log.e(tokenarray[j], "onResponse: name");


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    catch(JSONException e){
                        e.printStackTrace();
                    }
                    Random rand = new Random();
                    password_real1 = (rand.nextInt(tokenarray.length) + 0);
                    password_real = tokenarray[password_real1];
                    password_index = password_real1+2;
                    Username_real1 = userName[password_index-2];
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

        }
        else if( type.equals("i") && (password_exist.length() == 0) ) {
            request = new StringRequest(Request.Method.GET, Url_info.token_url_inmate, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        String flag = response;
                        Log.e(flag, "onResponse: tokens");
                        try {
                            jsonObject = new JSONObject(response);
                            Log.e(String.valueOf(jsonObject), "onResponse:");


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JSONArray obj = null;
                    try {
                        obj = (JSONArray) (jsonObject.get("user"));
                        tokenarray = new String[obj.length()];
                        userName = new String[obj.length()];
                        validity = new String[obj.length()];
                        if (tokenarray.length < 4)
                            Toast.makeText(otp.this, "Please contact help desk for token .", Toast.LENGTH_LONG).show();
                        else {
                            for (int j = 0; j < obj.length(); j++) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = (JSONObject) (obj.get(j));
                                    String token = jsonObject.optString("token");
                                    String userName_real = jsonObject.optString("username");
                                    String validity_token = jsonObject.optString("validity");
                                    validity[j] = validity_token;
                                    tokenarray[j] = token;
                                    userName[j] = userName_real;
                                    Log.e(tokenarray[j], "onResponse: name");


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Random rand = new Random();
                    password_real1 = (rand.nextInt(tokenarray.length) + 0);
                    password_real = tokenarray[password_real1];
                    password_index = password_real1+2;
                    Username_real1 = userName[password_index-2];

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

        }

        else if( type.equals("i") && (password_exist.length() != 0)) {
            password_from_database = getIntent().getStringExtra("Password");
            remaining_days = getIntent().getStringExtra("remaining_days");
            login_date_from_database = getIntent().getStringExtra("login_date");
        }
        else if( type.equals("g") && (password_exist.length() != 0)) {
            password_from_database = getIntent().getStringExtra("Password");
            remaining_days = getIntent().getStringExtra("remaining_days");
            login_date_from_database = getIntent().getStringExtra("login_date");
        }


    }


    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if( password_exist.length() == 0 ) {
                                Intent intent = new Intent(otp.this, Ticket.class);
                                intent.putExtra("Password", password_real);
                                intent.putExtra("Username", Username_real1);
                                intent.putExtra("type", type);
                                intent.putExtra("number", phonenumber_real);
                                startActivity(intent);
                                if (type.equals("g")) {
                                    Log.e( username , "onComplete: username");
                                    Log.e( reference , "onComplete: reference");
                                    Log.e( emailID , "onComplete: email");
                                    if (emailID.length() != 0 && username.length() != 0 && reference.length() != 0) {
                                        request = new StringRequest(Request.Method.POST, Url_info.URL_guest, new Response.Listener<String>() {
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
                                                hashMap.put("name", username);
                                                hashMap.put("phonenumber", phonenumber_real);
                                                hashMap.put("id", id);
                                                hashMap.put("email", emailID);  // added email
                                                hashMap.put("reference", reference);
                                                hashMap.put("token", password_real); // added refernce
                                                hashMap.put("date", formattedDate);
                                                hashMap.put("username", Username_real1);
                                                hashMap.put("validity", validity[0]);
                                                return hashMap;

                                            }
                                        };


                                        requestQueue.add(request);

                                    }
                                } else if (type.equals("i")) {
                                    Log.e(type, "verify_otp:hi ");
                                    Log.e(password_real, "verify_otp: password real");
                                    Log.e(inmateindex, "verify_otp: inmate index ");
                                    if (username.length() != 0) {
                                        request = new StringRequest(Request.Method.POST, Url_info.URL_inmate, new Response.Listener<String>() {
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
                                                hashMap.put("index", inmateindex); // val in database
                                                hashMap.put("token", password_real); // added refernce
                                                hashMap.put("date", formattedDate);
                                                hashMap.put("UserName", Username_real1);
                                                hashMap.put("validity", validity[0]);
                                                return hashMap;

                                            }
                                        };


                                        requestQueue.add(request);

                                    }
                                }
                                if (type.equals("g") && tokenarray.length >= 3) {
                                    request = new StringRequest(Request.Method.POST, Url_info.token_delete, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            try {
                                                String flag = response;
                                                Log.e(flag, "onResponse: tokens");


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
                                            hashMap.put("token_index", String.valueOf(password_index));


                                            return hashMap;

                                        }
                                    };


                                    requestQueue.add(request);
                                } else if (type.equals("i") && tokenarray.length >= 4) {
                                    request = new StringRequest(Request.Method.POST, Url_info.token_delete_inmate, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            try {
                                                String flag = response;
                                                Log.e(flag, "onResponse: tokens");


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
                                            hashMap.put("token_index", String.valueOf(password_index));


                                            return hashMap;

                                        }
                                    };


                                    requestQueue.add(request);
                                }
                                finish();
                            }
                            else if( password_exist.length() != 0  && type.equals("i")) {
                                final String email = getIntent().getStringExtra("email");
                                final String name = getIntent().getStringExtra("name");
                                if ((Integer.parseInt(remaining_days) < 60)) {
                                    Intent intent_1 = new Intent(otp.this, Ticket.class);
                                    intent_1.putExtra("Username", username);
                                    intent_1.putExtra("Password", password_from_database);
                                    intent_1.putExtra("type", type);
                                    intent_1.putExtra("number", phonenumber_real);
                                    startActivity(intent_1);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Your token has expired , login again to get a new token", Toast.LENGTH_LONG).show();
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
                                            Log.e( password_from_database + login_date_from_database + username + name + phonenumber_real + email, "getParams: parameters----------------------------------------------inmate" );
                                            HashMap<String, String> hashMap = new HashMap<String, String>();
                                            hashMap.put("token", password_from_database);
                                            hashMap.put("date", login_date_from_database);
                                            hashMap.put("UserName", username);
                                            hashMap.put("accountname", name);
                                            hashMap.put("number", phonenumber_real);
                                            hashMap.put("email",email);

                                            return hashMap;

                                        }
                                    };

                                    requestQueue.add(request);
                                }
                            }
                            else if( password_exist.length() != 0  && type.equals("g"))
                            {
                                final String email = getIntent().getStringExtra("email");
                                final String name = getIntent().getStringExtra("name");
                                final String number = getIntent().getStringExtra("number");
                                final String index = getIntent().getStringExtra("delete");
                                if ((Integer.parseInt(remaining_days) < 30)) {
                                    Intent intent_1 = new Intent(otp.this, Ticket.class);
                                    intent_1.putExtra("Username", username);
                                    intent_1.putExtra("Password", password_from_database);
                                    intent_1.putExtra("type", type);
                                    intent_1.putExtra("number", phonenumber_real);
                                    startActivity(intent_1);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Your token has expired , login again to get a new token", Toast.LENGTH_LONG).show();
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
                                            Log.e( password + login_date + username + name + number + email , "getParams: parameters ---------------------------------------------------");
                                            hashMap.put("token",password_from_database);
                                            hashMap.put("date", login_date_from_database);
                                            hashMap.put("UserName",username);
                                            hashMap.put("accountname", name);
                                            hashMap.put("number", number );
                                            hashMap.put("email", email);

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
                                            HashMap<String, String> hashMap = new HashMap<String, String>();
                                            Log.e( index , "getParams: index_delete -----------------------------------" );
                                            hashMap.put("token_index",index);


                                            return hashMap;

                                        }
                                    };

                                    requestQueue.add(request);
                                }

                            }
                        } else {

                            count++;
                            if(count<2)
                                Toast.makeText(otp.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                            else if(count<3)
                                Toast.makeText(otp.this, "Incorrect OTP,After the next Incorrect OTP, you will be taken to the guest login page", Toast.LENGTH_LONG).show();
                            else
                                System.exit(0);
                        }

                    }

                });
    }

    public void generate_otp(View v) {
        phonenumber_real = phonenumber.getText().toString();
        phonenumber_real = "+91" + phonenumber_real;
        if(type.equals("i")) {
            inmate_phone = getIntent().getStringExtra("number");
            if (phonenumber_real.length() == 0) {
                Toast.makeText(getApplicationContext(), "Please fill Phone number first", Toast.LENGTH_SHORT).show();
                Log.e(phonenumber_real, "onClick: mobile number");
            } else if (inmate_phone.equals(phonenumber_real)) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phonenumber_real,                     // Phone number to verify
                        60,                           // Timeout duration
                        TimeUnit.SECONDS,                // Unit of timeout
                        otp.this,        // Activity (for callback binding)
                        mCallback);                      // OnVerificationStateChangedCallbacks
            } else {
                Toast.makeText(getApplicationContext(), "Please enter the registered number.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(type.equals("g"))
        {
            if (phonenumber_real.length() == 0) {
                Toast.makeText(getApplicationContext(), "Please fill Phone number first", Toast.LENGTH_SHORT).show();
                Log.e(phonenumber_real, "onClick: mobile number");
            }
            else {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phonenumber_real,                     // Phone number to verify
                        60,                           // Timeout duration
                        TimeUnit.SECONDS,                // Unit of timeout
                        otp.this,        // Activity (for callback binding)
                        mCallback);                      // OnVerificationStateChangedCallbacks

            }
        }
    }

    public void verify_otp(View v) {
        phonenumber_real = phonenumber.getText().toString();
        if( phonenumber_real.length() == 0 )
        {
            Toast.makeText(getApplicationContext(),"Please fill Phone number first",Toast.LENGTH_SHORT).show();
        }
        else {
            String otpnumber_real = otpnumber.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otpnumber_real);
            SigninWithPhone(credential);
        }

    }
}




