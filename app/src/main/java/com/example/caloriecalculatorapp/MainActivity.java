package com.example.caloriecalculatorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText editAge, editWeight, editHeight;
    private RadioGroup radioGroupGender;
    private Spinner spinnerActivity;
    private Button btnCalculate;
    private TextView textResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);
        editAge = findViewById(R.id.editAge);
        editWeight = findViewById(R.id.editWeight);
        editHeight = findViewById(R.id.editHeight);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        spinnerActivity = findViewById(R.id.spinnerActivity);
        btnCalculate = findViewById(R.id.btnCalculate);
        textResult = findViewById(R.id.textResult);
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateCalories();
            }
        });
        String[] activityLevels = {
                "Sedentary (little or no exercise)",
                "Lightly active (light exercise/sports 1-3 days/week)",
                "Moderately active (moderate exercise/sports 3-5 days/week)",
                "Very active (hard exercise/sports 6-7 days a week)",
                "Extra active (very hard exercise/job & physical training)"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, activityLevels);
        spinnerActivity.setAdapter(adapter);

    }

    private void calculateCalories() {
        String ageStr = editAge.getText().toString().trim();
        String weightStr = editWeight.getText().toString().trim();
        String heightStr = editHeight.getText().toString().trim();

        if (ageStr.isEmpty() || weightStr.isEmpty() || heightStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        double weight = Double.parseDouble(weightStr);
        double height = Double.parseDouble(heightStr);

        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        boolean isMale = (selectedGenderId == R.id.radioMale);

        // Calculate BMR using Harris-Benedict Equation
        double bmr;
        if (isMale) {
            bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        } else {
            bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        }

        // Apply Activity Multiplier to find TDEE (Total Daily Energy Expenditure)
        int spinnerPosition = spinnerActivity.getSelectedItemPosition();
        double multiplier;
        switch (spinnerPosition) {
            case 0: multiplier = 1.2; break;       // Sedentary
            case 1: multiplier = 1.375; break;   // Light
            case 2: multiplier = 1.55; break;    // Moderate
            case 3: multiplier = 1.725; break;   // Very active
            case 4: multiplier = 1.9; break;     // Extra active
            default: multiplier = 1.2;
        }

        double tdee = bmr * multiplier;

        // Display results
        String resultText = String.format("BMR: %.0f kcal/day\nMaintenance: %.0f kcal/day\nWeight Loss: %.0f kcal/day",
                bmr, tdee, (tdee - 500));

        textResult.setText(resultText);
    }
}