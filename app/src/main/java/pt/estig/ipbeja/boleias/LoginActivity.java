package pt.estig.ipbeja.boleias;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.emailEditText = findViewById(R.id.editTextLoginEmail);
        this.passwordEditText = findViewById(R.id.editTextLoginPassword);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Observe database DAO with Livedata?
        /* NoteDatabase.getInstance(getApplicationContext())
                .noteDao()
                .getNotes()
                .observe(this, new Observer<List<Note>>() {
                    @Override
                    public void onChanged(@android.support.annotation.Nullable List<Note> notes) {
                        adapter.setData(notes);
                    }
                });
         */


        //Login essentials with firebase
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            //TODO pass user info?
            MainActivity.start(this);
        }
        //updateUI(currentUser);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        //starter.putExtra();
        context.startActivity(starter);
    }


    public void createNewAccount(View view) {
        SignInActivity.start(this);
    }

    public void login(View view){
        //TODO login form validation
        String email = emailEditText.getText().toString();
        String password =  passwordEditText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // TODO send user info to main activity
                            FirebaseUser user = mAuth.getCurrentUser();
                            MainActivity.start(LoginActivity.this);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
