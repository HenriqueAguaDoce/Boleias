package pt.estig.ipbeja.boleias;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import pt.estig.ipbeja.boleias.data.db.BoleiasDatabase;
import pt.estig.ipbeja.boleias.data.entity.User;
import pt.estig.ipbeja.boleias.data.entity.Vehicle;

public class CreateCarActivity extends AppCompatActivity {

    public static final String USER_ID = "userId";

    private long userId;

    // Activity request codes
    private static final int PHOTO_REQUEST_CODE = 123;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

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

        // Vamos buscar o id do contacto ao intent
        this.userId = getIntent().getLongExtra(USER_ID, 0);

        if(this.userId < 1) {
            finish();
            return;
        }

        // Se a instance state nao e null, tera alguma coisa la guardada
        if(savedInstanceState != null) {
            // identico aos extras dos intents (de facto os Intents guardam um Bundle mas oferecem metodos de conveniencia para acesso a estes campos)
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

        final String takePhoto = getString(R.string.takePhoto);
        final String selectPhoto = getString(R.string.selectPhoto);
        final String cancel = getString(R.string.cancel);

        final CharSequence[] items = { takePhoto, selectPhoto, cancel };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.addPhotoTitle));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals(takePhoto)){
                    photoIntent();

                } else if (items[which].equals(selectPhoto)){
                    galleryIntent();

                } else if (items[which].equals(cancel)){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void createCar(View view) {
        String brand = brandNameInput.getText().toString();
        String model = modelNameInput.getText().toString();
        String fuel = fuelNameInput.getText().toString();
        String seats = availableSeatsInput.getText().toString();
        if(!brand.isEmpty() && !model.isEmpty() && !fuel.isEmpty() && !seats.isEmpty()) {
            byte[] photoBytes = getBytesFromBitmap(carPhotoBitmap);


            // Mesmo que nao exista foto, nao ha problema em guardar null no campo dos bytes! Tratamos o caso de ser null quando utilizarmos a foto
            BoleiasDatabase.getInstance(this).vehicleDao().insert(new Vehicle(0,this.userId,brand, model, fuel, Integer.parseInt(seats), photoBytes));
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

        // tambem podiamos ir buscar o bitmap directamente a imageview:
        // BitmapDrawable drawable = (BitmapDrawable) contactPhoto.getDrawable();
        // Bitmap bitmap = drawable.getBitmap();
        if(carPhotoBitmap != null) outState.putParcelable(PHOTO_BITMAP_KEY, carPhotoBitmap);

        // No final, chamamos o super ja com o bundle composto
        super.onSaveInstanceState(outState);
    }

    public static void start(Context context, long userId) {
        Intent starter = new Intent(context, CreateCarActivity.class);
        starter.putExtra(USER_ID, userId);
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
        } else if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){

            Bitmap thumbnail = null;
            Uri uri = data.getData();

            //Set image to image view
            this.carPhoto.setImageURI(uri);

            //Convert uri into bitmap
            if (thumbnail == null){
                try {
                    thumbnail = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    this.carPhotoBitmap = thumbnail;
                    if (thumbnail != null){
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE: {
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permission was granted
                    picImageFromGallery();
                } else {
                    //Permission was denied
                    Toast.makeText(this, "Permission denied !", Toast.LENGTH_SHORT).show();
                }
            }
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

    private void picImageFromGallery(){
        //Intent to pick image from gallary
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, IMAGE_PICK_CODE);
    }

    private void galleryIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE )
                    == PackageManager.PERMISSION_DENIED){
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_CODE);
            } else {
                picImageFromGallery();
            }
        } else {

            picImageFromGallery();
        }
    }

    private void photoIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Para verificar que de facto existe uma aplicacao que de conta do nosso pedido
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Se sim, lancamos o Intent
            startActivityForResult(intent, PHOTO_REQUEST_CODE);
        }
        else {
            // Se nao existir, podemos mostrar uma mensagem de erro ao utilizador
            // TODO error
        }
    }


}


