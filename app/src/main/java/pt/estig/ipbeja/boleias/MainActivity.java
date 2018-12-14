
package pt.estig.ipbeja.boleias;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        //starter.putExtra();
        context.startActivity(starter);
    }

    public void signOut(View view){
        FirebaseAuth.getInstance().signOut();
        LoginActivity.start(this);
    }
}
