package pl.mgrzech.costfuel.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import pl.mgrzech.costfuel.BuildConfig;
import pl.mgrzech.costfuel.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        String versionName = BuildConfig.VERSION_NAME;

        TextView nameProgram = findViewById(R.id.aboutNameProgram);
        nameProgram.setText(getString(R.string.app_name_pl));

        TextView versionProgram = findViewById(R.id.aboutVersionProgram);
        versionProgram.setText(getString(R.string.about_activit_version) + " " + versionName);

        TextView author = findViewById(R.id.aboutAuthor);
        author.setText(getString(R.string.about_activity_author));

        TextView website = findViewById(R.id.aboutWebsite);
        website.setText(getString(R.string.about_activity_website));

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.about_activity_website_link)));
                startActivity(browserIntent);
            }
        });
    }
}
