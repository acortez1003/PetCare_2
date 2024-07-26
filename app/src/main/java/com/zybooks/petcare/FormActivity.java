package com.zybooks.petcare;

import android.graphics.Color;
import android.os.Bundle;
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

import com.zybooks.petcare.model.User;
import com.zybooks.petcare.repo.UserRepository;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormActivity extends AppCompatActivity {

    private UserRepository userRepository;
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
        setContentView(R.layout.activity_form);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.form_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the database and DAO
        userRepository = UserRepository.getInstance(this);

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
        boolean isValid = true;
        String microchipID = microEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String gender = genderRadioGroup.getCheckedRadioButtonId() == R.id.male_radio_group ? "Male" : "Female";
        String email = emailEditText.getText().toString();
        String access = accessEditText.getText().toString();
        String confirm = confirmEditText.getText().toString();
        String breed = breedSpinner.getSelectedItem().toString();
        boolean neutered = neuterCheckbox.isChecked();

        // clear previous errors
        clearErrors();

        // validate each field and update the validity status
        isValid &= validateMicrochipID(microchipID);
        isValid &= validateName(name);
        isValid &= validateEmail(email);
        isValid &= validateCodes(access, confirm);

        // check validity
        if (isValid) {
            User newUser = new User(microchipID, name, gender, email, access, breed, neutered);
            boolean isAdded = userRepository.addUser(newUser);
            if(isAdded) {
                resetForm();
                Toast.makeText(this, "SUCCESS: Form sent to database", Toast.LENGTH_SHORT).show();
            } else {
                microEditText.setError("Already exists");
                Toast.makeText(this, "Microchip ID already exists", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Errors detected", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearErrors() {
        microEditText.setTextColor(Color.BLACK);
        microEditText.setError(null);
        nameEditText.setTextColor(Color.BLACK);
        nameEditText.setError(null);
        emailEditText.setTextColor(Color.BLACK);
        emailEditText.setError(null);
        accessEditText.setTextColor(Color.BLACK);
        accessEditText.setError(null);
        confirmEditText.setTextColor(Color.BLACK);
        confirmEditText.setError(null);
    }

    private boolean validateMicrochipID(String microchipID) {
        String microchipId = microEditText.getText().toString();

        if (microchipID.isEmpty()) {
            microEditText.setTextColor(Color.RED);
            microEditText.setError("Microchip ID cannot be empty");
            return false;
        } else if (microchipID.length() < 5 || microchipID.length() > 15) {
            microEditText.setTextColor(Color.RED);
            microEditText.setError("Microchip ID must be between 5 and 15 characters");
            return false;
        } else if (!microchipID.matches("[A-Z0-9]+")) {
            microEditText.setTextColor(Color.RED);
            microEditText.setError("Microchip ID must be alphanumeric and uppercase");
            return false;
        }
        return true;
    }

    private boolean validateName(String name) {
        if (name.isEmpty()) {
            nameEditText.setTextColor(Color.RED);
            nameEditText.setError("Name cannot be empty");
            return false;
        } else if (!isNameValid(name)) {
            nameEditText.setTextColor(Color.RED);
            nameEditText.setError("Name must start with a capital letter");
            return false;
        }
        return true;
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            emailEditText.setTextColor(Color.RED);
            emailEditText.setError("Email cannot be empty");
            return false;
        } else if (!isEmailValid(email)) {
            emailEditText.setTextColor(Color.RED);
            emailEditText.setError("Invalid email address");
            return false;
        }
        return true;
    }

    private boolean validateCodes(String access, String confirm) {
        if (access.isEmpty()) {
            accessEditText.setTextColor(Color.RED);
            accessEditText.setError("Access code cannot be empty");
            return false;
        }
        if (confirm.isEmpty()) {
            confirmEditText.setTextColor(Color.RED);
            confirmEditText.setError("Confirm code cannot be empty");
            return false;
        } else if (!access.equals(confirm)) {
            accessEditText.setTextColor(Color.RED);
            confirmEditText.setTextColor(Color.RED);
            accessEditText.setError("Access codes do not match");
            confirmEditText.setError("Access codes do not match");
            return false;
        }
        return true;
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