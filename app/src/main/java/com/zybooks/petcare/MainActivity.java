package com.zybooks.petcare;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText microEditText, nameEditText, emailEditText, accessEditText, confirmEditText;
    private RadioGroup genderRadioGroup;
    private Spinner breedSpinner;
    private CheckBox neuterCheckbox;
    private String[] chipsArray;
    private String[] domainArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.relative);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.relative_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // assigning all input fields

        microEditText = findViewById(R.id.micro_edit_text);         // EditText
        nameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        accessEditText = findViewById(R.id.access_edit_text);
        confirmEditText = findViewById(R.id.confirm_edit_text);

        genderRadioGroup = findViewById(R.id.gender_radio_group);   // RadioGroup
        breedSpinner = findViewById(R.id.breed_spinner);            // Spinner
        neuterCheckbox = findViewById(R.id.neuter_checkbox);        // CheckBox

        Button resetButton = findViewById(R.id.reset_button);              // Buttons
        Button submitButton = findViewById(R.id.submit_button);

        // spinner array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.breed_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        breedSpinner.setAdapter(adapter);

        // microchipID array
        chipsArray = getResources().getStringArray(R.array.chips);

        // domain types array
        domainArray = getResources().getStringArray(R.array.domain_list);

        // TextWatchers
        setupTextWatcher(microEditText);
        setupTextWatcher(nameEditText);
        setupTextWatcher(emailEditText);
        setupTextWatcher(accessEditText);
        setupTextWatcher(confirmEditText);

        // button listeners
        resetButton.setOnClickListener(v -> resetForm());
        submitButton.setOnClickListener(v -> submitForm());
    }

    // resets all form fields
    private void resetForm() {
        microEditText.setText("");      // reset EditText to empty
        nameEditText.setText("");
        emailEditText.setText("");
        accessEditText.setText("");
        confirmEditText.setText("");

        genderRadioGroup.check(R.id.female_radio_group);
        breedSpinner.setSelection(0);
        neuterCheckbox.setChecked(true);
    }

    // submit form and error checking
    private void submitForm() {
        List<String> errorMessages = new ArrayList<>();

        microEditText.setTextColor(Color.BLACK);
        nameEditText.setTextColor(Color.BLACK);
        emailEditText.setTextColor(Color.BLACK);
        accessEditText.setTextColor(Color.BLACK);
        confirmEditText.setTextColor(Color.BLACK);

        String microchipID = microEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String access = accessEditText.getText().toString();
        String confirm = confirmEditText.getText().toString();

        // Microchip ID Validation
        if (microchipID.isEmpty()) {
            microEditText.setTextColor(Color.RED);
            errorMessages.add("Microchip ID cannot be empty");
        } else {
            if (microchipID.length() < 5 || microchipID.length() > 15) {
                microEditText.setTextColor(Color.RED);
                errorMessages.add("Microchip ID must be between 5 and 15 characters");
            } else if (!microchipID.matches("[A-Z0-9]+")) {
                microEditText.setTextColor(Color.RED);
                errorMessages.add("Microchip ID must be alphanumeric and uppercase");
            } else if (!Arrays.asList(chipsArray).contains(microchipID)) {
                microEditText.setTextColor(Color.RED);
                errorMessages.add("Microchip ID is not valid");
            }
        }

        // Name
        if (name.isEmpty()) {
            nameEditText.setTextColor(Color.RED);
            errorMessages.add("Name cannot be empty");
        } else if (!isNameValid(name)) {
            nameEditText.setTextColor(Color.RED);
            errorMessages.add("Name must start with a capital letter");
        }

        // Email address
        if (email.isEmpty()) {
            emailEditText.setTextColor(Color.RED);
            errorMessages.add("Email cannot be empty");
        } else if (!isEmailValid(email)) {
            emailEditText.setTextColor(Color.RED);
            errorMessages.add("Invalid email address");
        }

        // Access code and Confirm code
        if (access.isEmpty()) {
            accessEditText.setTextColor(Color.RED);
            errorMessages.add("Access code cannot be empty");
        }
        if (confirm.isEmpty()) {
            confirmEditText.setTextColor(Color.RED);
            errorMessages.add("Confirm code cannot be empty");
        }
        if (!access.equals(confirm)) {
            accessEditText.setTextColor(Color.RED);
            confirmEditText.setTextColor(Color.RED);
            errorMessages.add("Access codes do not match");
        }

        // Display errors
        if(!errorMessages.isEmpty()) {
            showErrorMessages(errorMessages);
        } else {
            // No errors
            resetForm();
            Toast.makeText(this, "SUCCESS: Form sent to database", Toast.LENGTH_SHORT).show();
        }
    }

    // every first letter in Name has to be capital
    private boolean isNameValid(String name) {
        String namePattern = "^([A-Z][a-z]*)(\\s[A-Z][a-z]*)*$";
        Pattern pattern = Pattern.compile(namePattern);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    // prefix 3+ char and valid domain type
    private boolean isEmailValid(String email) {
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()) {
            return false;
        }

        String[] emailParts = email.split("@");
        String prefix = emailParts[0];
        String[] domainParts = emailParts[1].split("\\.");
        String domainType = domainParts[domainParts.length - 1];

        if(prefix.length() < 3) {
            return false;
        }

        if(!Arrays.asList(domainArray).contains(domainType)) {
            return false;
        }
        return true;
    }

    // displays error messages with a delay
    private void showErrorMessages(List<String> errorMessages) {
        Handler handler = new Handler();
        for(int i = 0; i < errorMessages.size(); i++) {
            String message = errorMessages.get(i);
            handler.postDelayed(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show(),i * 1000L);
        }
    }

    private void setupTextWatcher(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            // Reset text color when user starts typing
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText.setTextColor(Color.BLACK);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}