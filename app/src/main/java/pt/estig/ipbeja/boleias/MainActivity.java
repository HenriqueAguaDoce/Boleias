
package pt.estig.ipbeja.boleias;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import pt.estig.ipbeja.boleias.data.db.BoleiasDatabase;
import pt.estig.ipbeja.boleias.data.entity.Vehicle;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private CarAdapter carAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView carCompleteList;
    private LinearLayout layout_img;
    private static final int PHOTO_REQUEST_CODE = 123;
    private static final String PHOTO_BITMAP_KEY = "photoBytes";
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private Bitmap userPhotoBitmap = null;
    private ImageView userPhoto;
    private Bitmap thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Se a instance state não é null, terá alguma coisa lá guardada
        if(savedInstanceState != null) {
            // idêntico aos extras dos intents (de facto os Intents guardam um Bundle mas oferecem métodos de conveniência para acesso a estes campos)
            this.userPhotoBitmap = savedInstanceState.getParcelable(PHOTO_BITMAP_KEY);
        }

        userPhoto = findViewById(R.id.profImage);
        userPhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectImage();
                return true;
            }
        });

        layout_img = findViewById(R.id.add_img_hint_wrapper);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.profileSettings);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        carCompleteList = findViewById(R.id.carsListRecyclerView);
        carAdapter = new CarAdapter();
        linearLayoutManager = new LinearLayoutManager(this);
        carCompleteList.setAdapter(carAdapter);
        carCompleteList.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.bottom_navigation_menu, menu);
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
                FirebaseAuth.getInstance().signOut();
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

    @Override
    protected void onStart() {
        super.onStart();

        List<Vehicle> vehicles = BoleiasDatabase.getInstance(this).vehicleDao().getAllVehicles();

        carAdapter.setData(vehicles);

        if(userPhotoBitmap != null) {
            userPhoto.setImageBitmap(userPhotoBitmap);
            userPhoto.setVisibility(View.VISIBLE);
            layout_img.setVisibility(View.GONE);

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.findRide:
                Toast.makeText(this, "Find Ride...", Toast.LENGTH_SHORT).show();
                FindRide.start(this);
                return true;
            case R.id.createRide:
                Toast.makeText(this, "Create Ride...", Toast.LENGTH_SHORT).show();
                OfferRide.start(this);
                return true;
            case R.id.profileSettings:
                Toast.makeText(this, "Profile Settings...", Toast.LENGTH_SHORT).show();
                MainActivity.start(this);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);

        }
    }

    public void addNewCar_onClick(View view) {
        CreateCarActivity.start(this);

    }

    public void addNewProfImg_onClick(View view) {
        selectImage();
    }

    private void selectImage(){
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // também podiamos ir buscar o bitmap directamente à imageview:
        // BitmapDrawable drawable = (BitmapDrawable) contactPhoto.getDrawable();
        // Bitmap bitmap = drawable.getBitmap();
        if(userPhotoBitmap != null) outState.putParcelable(PHOTO_BITMAP_KEY, userPhotoBitmap);

        // No final, chamamos o super já com o bundle composto
        super.onSaveInstanceState(outState);
    }

    //Handles the result of the photo taken and the image choosed from the gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            // Getting the thumbnail of the photo
            this.thumbnail = (Bitmap) extras.get("data");
            // Set bitmap on ImageView
            this.userPhoto.setImageBitmap(this.thumbnail);
            // Save bitmap in case the activity gets destroid (see onSaveInstanceState)
            this.userPhotoBitmap = this.thumbnail;
        } else if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            Uri uri = data.getData();

            //Set image to image view
            this.userPhoto.setImageURI(uri);
            layout_img.setVisibility(View.GONE);

            //TODO

            //Convert uri into bitmap
//            if (thumbnail == null){
//                try {
//                    thumbnail = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                    this.userPhotoBitmap = thumbnail;
//                    if (thumbnail != null){
//                        return;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

        }
    }

    // handle result of runtime permission
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

    private void showDeleteContactDialog(final Vehicle vehicle) {

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.delete_car_dialog_title)
                .setMessage(getString(R.string.delete_car_dialog_message, vehicle.getBrand() + "" + vehicle.getModel()))
                .setPositiveButton(R.string.delete_car_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteCar(vehicle);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.show();
    }

    private void deleteCar(Vehicle vehicle) {
        BoleiasDatabase.getInstance(this).vehicleDao().delete(vehicle);
        carAdapter.remove(vehicle);
    }

    public void createCars(View view) {
        CreateCarActivity.start(this);
    }


    class CarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        Vehicle vehicle;
        final ImageView carImage;
        final TextView carName;


        public CarViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            carImage = itemView.findViewById(R.id.cardImageCar);
            carName = itemView.findViewById(R.id.carName);

        }

        private void bind(Vehicle vehicle) {
            this.vehicle = vehicle;

            carName.setText(vehicle.getBrand() + " " + vehicle.getModel());

            if(vehicle.getPhoto() != null && vehicle.getPhoto().length != 0) {
                carImage.setImageBitmap(bitmapFromBytes(vehicle.getPhoto()));
                carImage.setVisibility(View.VISIBLE);
            }
        }

        private Bitmap bitmapFromBytes(byte[] photoBytes) {
            ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(photoBytes);
            Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
            return bitmap;
        }

        @Override
        public void onClick(View v) {
            showDeleteContactDialog(vehicle);
            Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
        }


        @Override
        public boolean onLongClick(View v) {
            // um click longo, invocamos o método para apagar o contacto
            return true; // devolvemos true se tratámos o evento
        }
    }


    class CarAdapter extends RecyclerView.Adapter<CarViewHolder>{

        private List<Vehicle> data = new ArrayList<>();

        private void setData(List<Vehicle> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public CarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.car_list, viewGroup, false);
            return new CarViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CarViewHolder carViewHolder, int i) {
            Vehicle vehicle = data.get(i);
            carViewHolder.bind(vehicle);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        private void remove(Vehicle vehicle) {
            int index = data.indexOf(vehicle);
            if(index != -1) {
                data.remove(index);
                notifyItemRemoved(index);
            }
        }

        private void removeAll() {
            int count = data.size();
            if(count > 0) {
                data.clear();
                notifyItemRangeRemoved(0, count);
            }
        }
    }

}
