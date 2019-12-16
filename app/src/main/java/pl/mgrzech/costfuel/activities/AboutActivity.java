package pl.mgrzech.costfuel.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

import pl.mgrzech.costfuel.BuildConfig;
import pl.mgrzech.costfuel.R;

public class AboutActivity extends AppCompatActivity {

    private TextView modeversionProgram;
    private TextView nameProgram;
    private TextView author;
    private TextView website;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        String versionName = BuildConfig.VERSION_NAME;

        nameProgram = findViewById(R.id.aboutNameProgram);
        nameProgram.setText("Kalkulator spalania");

        modeversionProgram = findViewById(R.id.aboutVersionProgram);
        modeversionProgram.setText("ver. " + versionName);

        author = findViewById(R.id.aboutAuthor);
        author.setText("Michał Grzech");

        website = findViewById(R.id.aboutWebsite);
        website.setText("mgrzech.pl");

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mgrzech.pl"));
                startActivity(browserIntent);
            }
        });

    }
}
