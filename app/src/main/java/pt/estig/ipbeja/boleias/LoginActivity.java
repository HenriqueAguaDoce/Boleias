package pt.estig.ipbeja.boleias;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pt.estig.ipbeja.boleias.data.db.BoleiasDatabase;
import pt.estig.ipbeja.boleias.data.entity.User;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private CheckBox checkBoxKeepIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_login);

        this.emailEditText = findViewById(R.id.editTextLoginEmail);
        this.passwordEditText = findViewById(R.id.editTextLoginPassword);
        this.checkBoxKeepIn = findViewById(R.id.checkBoxKeepIn);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        boolean checked = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("checkBoxKeepIn", false);
        checkBoxKeepIn.setChecked(checked);

        //Login essentials with firebase
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (checkBoxKeepIn.isChecked() & currentUser != null) {
            final String email = emailEditText.getText().toString();
            User u =  BoleiasDatabase.getInstance(LoginActivity.this).userDao().getContact(email);
            MainActivity.start(this, u.getEmail());
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }


    public void createNewAccount(View view) {
        SignUpActivity.start(this);
    }



    public void login(View view){
        final String email = emailEditText.getText().toString();
        String password =  passwordEditText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            MainActivity.start(LoginActivity.this, email);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void checkBoxKeepIn_OnClick(View view) {

        boolean checked = checkBoxKeepIn.isChecked();

        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean("checkBoxKeepIn", checked).commit();
        Toast.makeText(this, "Check: " + checked, Toast.LENGTH_SHORT).show();

    }
}
