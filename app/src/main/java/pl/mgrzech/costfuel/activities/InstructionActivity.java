package pl.mgrzech.costfuel.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import pl.mgrzech.costfuel.R;

public class InstructionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ExpandableTextView instructionGeneralExpandableText = findViewById(R.id.instructionGeneral)
                .findViewById(R.id.instructionGeneral);

        instructionGeneralExpandableText.setText("Aplikacja KALKULATOR SPALANIA jest prostą aplikacją pozwalającą" +
                " na sprawdzanie średniego spalania samochodu i średniego kosztu przejechania 100 km. " +
                "Wszystkie te dane wyliczane sa na podstawie danych zawartych na paragonie ze stacji paliw i " +
                "aktualnego przebiegu samochodu.");

        ExpandableTextView instructionAddCarExpandableText = findViewById(R.id.instructionAddCar)
                .findViewById(R.id.instructionAddCar);

        instructionAddCarExpandableText.setText("Do zapisania nowego samochodu niezbędne są jego dane: " +
                "Marka, model, rodzaj paliwa. Dodatkowo użytkownik może wybrać z jakiego okresu aplikacja " +
                "będzie brała dane do wyliczania średnich wartości spalania i kosztów przejechania 100 km. " +
                "Rodzaj paliwa i okres czasu dla obliczeń można edytować w dowolnym momencie.");

        ExpandableTextView instructionAddFuelExpandableText = findViewById(R.id.instructionAddFuel)
                .findViewById(R.id.instructionAddFuel);

        instructionAddFuelExpandableText.setText("Do zapisania nowego tankowania, dla zapisanego wcześniej samochodu, niezbędne są dane:" +
                " data, rodzaj tankowanego paliwa, ilośc paliwa, cena tankowania." +
                " Dla wykonania pierwszych obliczeń niezbędne są dwa tankowania tego samego paliwa.");
    }
}
