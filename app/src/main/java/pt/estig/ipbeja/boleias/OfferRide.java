package pt.estig.ipbeja.boleias;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class OfferRide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_ride);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, OfferRide.class);
        context.startActivity(starter);
    }
}
