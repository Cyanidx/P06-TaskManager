package android.myapplicationdev.com.taskmanager;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    EditText etName, etDesc, etTime;
    Button btnAdd, btnCancel;
    int reqCode = 123;
    Task content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etName = (EditText) findViewById(R.id.etName);
        etDesc = (EditText) findViewById(R.id.etDesc);
        etTime = (EditText)findViewById(R.id.etTime);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(AddActivity.this);
                String name = etName.getText().toString();
                String desc = etDesc.getText().toString();
                int time = Integer.parseInt(etTime.getText().toString());
                content = new Task(0, name, desc);
                dbh.insertTask(content);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, time);

                Intent intent = new Intent(AddActivity.this, ScheduledNotificationReceiver.class);
                intent.putExtra("name", name);
                intent.putExtra("desc", desc);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(AddActivity.this, reqCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

                Intent i  = new Intent();

                setResult(RESULT_OK, i);
                finish();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                setResult(RESULT_OK,i);
                finish();
            }
        });
    }
}