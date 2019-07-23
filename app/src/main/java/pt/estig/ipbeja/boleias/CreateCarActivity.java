package pt.estig.ipbeja.boleias;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import pt.estig.ipbeja.boleias.data.db.BoleiasDatabase;
import pt.estig.ipbeja.boleias.data.entity.Vehicle;

public class CreateCarActivity extends AppCompatActivity {

    // Activity request codes
    private static final int PHOTO_REQUEST_CODE = 123;

    // Instance State Bundle keys
    private static final String PHOTO_BITMAP_KEY = "photoBytes";

    // Photo bitmap
    private Bitmap carPhotoBitmap = null;

    // Views
    private EditText brandNameInput;
    private EditText modelNameInput;
    private EditText fuelNameInput;
    private EditText availableSeatsInput;
    private ImageView carPhoto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_car);

        // Se a instance state não é null, terá alguma coisa lá guardada
        if(savedInstanceState != null) {
            // idêntico aos extras dos intents (de facto os Intents guardam um Bundle mas oferecem métodos de conveniência para acesso a estes campos)
            this.carPhotoBitmap = savedInstanceState.getParcelable(PHOTO_BITMAP_KEY);
        }

        brandNameInput = findViewById(R.id.car_name_input);
        modelNameInput = findViewById(R.id.txtCarModel);
        fuelNameInput = findViewById(R.id.txtFuelType);
        availableSeatsInput = findViewById(R.id.txtSeatsAvailable);
        carPhoto = findViewById(R.id.car_photo);

        carPhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                removePhoto();
                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(carPhotoBitmap != null) {
            carPhoto.setImageBitmap(carPhotoBitmap);
            carPhoto.setVisibility(View.VISIBLE);
        }
        else {
            carPhoto.setVisibility(View.GONE);
        }

    }

    private void removePhoto() {
        carPhotoBitmap = null;
        carPhoto.setImageBitmap(null);
        carPhoto.setVisibility(View.GONE);
    }

    public void takePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Para verificar que de facto existe uma aplicação que dê conta do nosso pedido
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Se sim, lançamos o Intent
            startActivityForResult(intent, PHOTO_REQUEST_CODE);
        }
        else {
            // Se não existir, podemos mostrar uma mensagem de erro ao utilizador
            // TODO error
        }
    }

    public void createCar(View view) {
        String brand = brandNameInput.getText().toString();
        String model = modelNameInput.getText().toString();
        String fuel = fuelNameInput.getText().toString();
        String seats = availableSeatsInput.getText().toString();
        if(!brand.isEmpty() && !model.isEmpty() && !fuel.isEmpty() && !seats.isEmpty()) {
            byte[] photoBytes = getBytesFromBitmap(carPhotoBitmap);

            // Mesmo que não exista foto, não há problema em guardar null no campo dos bytes! Tratamos o caso de ser null quando utilizarmos a foto
            BoleiasDatabase.getInstance(this).vehicleDao().insert(new Vehicle(0, brand, model, fuel, Integer.parseInt(seats), photoBytes));
            finish();
        }
        else {
            if(brand.isEmpty()) Snackbar.make(findViewById(android.R.id.content), R.string.create_brand_empty_name_alert, Toast.LENGTH_SHORT).show();
            else if (model.isEmpty()) Snackbar.make(findViewById(android.R.id.content), R.string.create_model_empty_name_alert, Toast.LENGTH_SHORT).show();
            else if (fuel.isEmpty()) Snackbar.make(findViewById(android.R.id.content), R.string.create_fuel_empty_name_alert, Toast.LENGTH_SHORT).show();
            else Snackbar.make(findViewById(android.R.id.content), R.string.create_seats_empty_alert, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Aqui guardamos no Bundle as coisas que queremos guardar antes da Activity ser destruida
        // para quando ela for reconstruida as podermos recuperar (ver onCreate)

        // também podiamos ir buscar o bitmap directamente à imageview:
        // BitmapDrawable drawable = (BitmapDrawable) contactPhoto.getDrawable();
        // Bitmap bitmap = drawable.getBitmap();
        if(carPhotoBitmap != null) outState.putParcelable(PHOTO_BITMAP_KEY, carPhotoBitmap);

        // No final, chamamos o super já com o bundle composto
        super.onSaveInstanceState(outState);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, CreateCarActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            // Vamos buscar o thumbnail da foto
            Bitmap thumbnail = (Bitmap) extras.get("data");
            // Colocamos esse Bitmap na ImageView
            this.carPhoto.setImageBitmap(thumbnail);
            // E podemos guardar o Bitmap para o caso de a Activity ser destruida (ver onSaveInstanceState)
            this.carPhotoBitmap = thumbnail;
        }
    }

    /**
     * Decodes a Bitmap into an array of bytes
     * @param bmp The source Bitmap
     * @return An array of bytes or null if the Bitmap was null
     */
    private byte[] getBytesFromBitmap(Bitmap bmp) {
        if(bmp == null) return null;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
