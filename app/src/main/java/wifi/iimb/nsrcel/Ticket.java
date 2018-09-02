package wifi.iimb.nsrcel;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class Ticket extends AppCompatActivity {

    TextView uname, upassword, countdown;
    CountDownTimer countDownTimer;
    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    private final String SENT = "SMS_SENT";
    private final String DELIVERED = "SMS_DELIVERED";
    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticketpage);
        String username = getIntent().getStringExtra("Username");
        String password = getIntent().getStringExtra("Password");
        String type_user = getIntent().getStringExtra("type");
        String phone_number = getIntent().getStringExtra("number");
        Log.e( phone_number, "onCreate: phonr number '''''''''''''''");
        Log.e(type_user, "onCreate: user_type");
        String message = "Welcome to NSRCEL please connect to SSID: IIMB-Guest and login to www.iimb.ac.in, using the following credentials." + "\n" + "Username:" + " " + username + "\n" + "Password:" + " " + password + "\n" + "For help contact Tel:3072/3330" + "\n" + "Email: ithelpdesk@iiimb.ernet.in";
        uname = (TextView) findViewById(R.id.username);
        upassword = (TextView) findViewById(R.id.password);
        //   countdown = (TextView) findViewById(R.id.countdown);
        //   countdown = (TextView) findViewById(R.id.countdown);
        uname.setText("Username: " + username);
        upassword.setText("Password: " + password);





    }

}





