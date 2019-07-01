
package pt.estig.ipbeja.boleias;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.logOut:
                Toast.makeText(this, "Loging out...", Toast.LENGTH_SHORT).show();

                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("checkBoxKeepIn", false).commit();
                finish();
                LoginActivity.start(this);
                return true;
            default:
                 return super.onOptionsItemSelected(item);

        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    public void signOut(View view){
        FirebaseAuth.getInstance().signOut();
        LoginActivity.start(this);
    }

    public void btn_findRide_onclick(View view) {
        FindRide.start(this);


    }

    public void btn_offerRide_onclick(View view) {
        OfferRide.start(this);
    }
}
