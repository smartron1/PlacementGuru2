package com.yourstrulyssj.placementguru.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yourstrulyssj.placementguru.R;
import com.yourstrulyssj.placementguru.helper.SQLiteHandler;
import com.yourstrulyssj.placementguru.helper.SessionManager;

public class AlumnaeHome extends AppCompatActivity {


    private SessionManager session;
    private SQLiteHandler db;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnae_home);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        button = (Button) findViewById(R.id.btnLogout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();

            }
        });
    }


    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(AlumnaeHome.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
