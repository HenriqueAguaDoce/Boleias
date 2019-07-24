package pt.estig.ipbeja.boleias;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import pt.estig.ipbeja.boleias.data.db.BoleiasDatabase;
import pt.estig.ipbeja.boleias.data.entity.User;

/**
 * @author henriquead
 * Contem a actividade Login
 * Aqui o utilizar realiza o seu login, atraves dos dados escolhidos durante o registo
 * Para a verificacao do utilizador e utilizada a firebase, como base de dados externa
 * A firebase tambem facilita a autenticacao do utilizador
 */
public class SignUpActivity extends AppCompatActivity {

    //firebase
    private FirebaseAuth mAuth;

    //variaveis globais
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
        Intent starter = new Intent(context, SignUpActivity.class);
        context.startActivity(starter);
    }


    /**
     * Verificar dados introduzidos pelo utilizador e depois de aceites
     * os dados sao enviados para a firebase e para a base de dados local
     * @param view vista
     */
    public void finishRegister(View view) {
        final String email = emailEditText.getText().toString();
        String password =  passwordEditText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(0, username.getText().toString(), email,0, "", "", "", null);
                            BoleiasDatabase.getInstance(SignUpActivity.this).userDao().insert(user);
                            FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .add(user);
                            Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Faz o utilizador recuar para a actividade anterior(login)
     * @param view vista
     */
    public void goBack(View view) {
        finish();
    }
}
