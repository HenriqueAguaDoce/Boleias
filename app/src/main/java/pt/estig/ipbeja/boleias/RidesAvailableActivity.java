package pt.estig.ipbeja.boleias;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pt.estig.ipbeja.boleias.data.entity.User;

public class RidesAvailableActivity extends AppCompatActivity {
    private String txtStart, txtEnd, selectedDate;
    private RecyclerView rideCompleteList;
    private RideAdapter rideAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides_available);

        txtStart = getIntent().getStringExtra("start");
        txtEnd = getIntent().getStringExtra("end");
        selectedDate = getIntent().getStringExtra("date");

/*
        TextView s = (TextView) findViewById(R.id.startTxt);
        s.setText(txtStart);

        TextView e = (TextView) findViewById(R.id.endTxt);
        e.setText(txtEnd);

        TextView d = (TextView) findViewById(R.id.dateTxt);
        d.setText(selectedDate);
*/

        rideCompleteList = findViewById(R.id.ridesList);

        rideAdapter = new RideAdapter();
        linearLayoutManager = new LinearLayoutManager(this);

        rideCompleteList.setAdapter(rideAdapter);
        rideCompleteList.setLayoutManager(linearLayoutManager);
    }

    private void sendInfo(){

    }

    class RideViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        User user;
        final TextView name;
        final TextView startRide;
        final TextView endRide;
        final TextView seats;
        final TextView price;
        final TextView date;
        final ImageView photo;


        public RideViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.txtProfileName);
            startRide = itemView.findViewById(R.id.txtStartRideLocation);
            endRide = itemView.findViewById(R.id.txtEndRideLocation);
            seats = itemView.findViewById(R.id.txtSeatsnumber);
            price = itemView.findViewById(R.id.txtPriceTag);
            date = itemView.findViewById(R.id.txtTimeOfTravel);
            photo = itemView.findViewById(R.id.imgProfile);

        }

        private void bind(User user) {
            this.user = user;

            name.setText(user.getName());

            if(user.getPhoto() != null && user.getPhoto().length != 0) {
                photo.setImageBitmap(bitmapFromBytes(user.getPhoto()));
                photo.setVisibility(View.VISIBLE);
            }
        }

        private Bitmap bitmapFromBytes(byte[] photoBytes) {
            ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(photoBytes);
            Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
            return bitmap;
        }

        @Override
        public void onClick(View v) {

        }
    }

    class RideAdapter extends RecyclerView.Adapter<RideViewHolder> {

        private List<User> data = new ArrayList<>();

        private void setData(List<User> data, boolean sort) {
            this.data = data;
            sort(sort);
        }

        private void sort(final boolean asc) {

            Collections.sort(data, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    int sort = o1.getName().compareTo(o2.getName());
                    if(asc) return sort;
                    else return -sort;
                }
            });
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RideViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_list, viewGroup, false);
            return new RideViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RideViewHolder contactViewHolder, int i) {
            User user = data.get(i);
            contactViewHolder.bind(user);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
