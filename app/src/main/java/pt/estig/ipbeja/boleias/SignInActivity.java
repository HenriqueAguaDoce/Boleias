package pt.estig.ipbeja.boleias;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import pt.estig.ipbeja.boleias.data.BoleiasDatabase;
import pt.estig.ipbeja.boleias.data.User;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailEditText = findViewById(R.id.editTextSignInEmail);
        passwordEditText = findViewById(R.id.editTextSignInPass);
        username = findViewById(R.id.editTextSignInName);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Register with firebase essentials
        mAuth = FirebaseAuth.getInstance();

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, SignInActivity.class);
        context.startActivity(starter);
    }

    public void goBack(View view) {
        this.finish();
    }

    public void finishRegister(View view) {
        //TODO sign in form validation
        final String email = emailEditText.getText().toString();
        String password =  passwordEditText.getText().toString();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(0, username.getText().toString(), email);
                            FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            //TODO send user info to main activity
                                            //documentReference.getId();
                                            MainActivity.start(SignInActivity.this);
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }
}
