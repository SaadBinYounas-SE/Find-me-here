package com.example.findmehere;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class viewPostDetails extends AppCompatActivity {

    TextView tvItemNameDetail, tvDescriptionDetail, tvLocationDetail,tvStatusDetail,tvMessegeDetail;
    Button btnMessegeDetail,btnCallDetail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postdetails);


        modelPosts post = getIntent().getParcelableExtra("thisPost");


        String userId=getIntent().getStringExtra("userid");
        String itemName =  getIntent().getStringExtra("itemName");
        String description = getIntent().getStringExtra("description");
        String location = getIntent().getStringExtra("location");
        String status=getIntent().getStringExtra("status");
        String messege = getIntent().getStringExtra("messege");


        init();

        tvItemNameDetail.setText(itemName);
        tvDescriptionDetail.setText(description);
        tvLocationDetail.setText(location);
        tvMessegeDetail.setText(messege);
        tvStatusDetail.setText(status);



        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User").child(userId);


        btnMessegeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if the fragment is already added
                Fragment messegesContactFragment = getSupportFragmentManager().findFragmentByTag("MessegesContact");
                if (messegesContactFragment == null) {
                    // If it's not added, create a new instance
                    messegesContactFragment = new MessegesContact();

                    // Set arguments if needed
                    Bundle args = new Bundle();
                    args.putString("userId", userId);
                    args.putString("flag", "true");
                    messegesContactFragment.setArguments(args);

                    // Start the fragment transaction
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_messeges, messegesContactFragment, "MessegesContact")
                            .addToBackStack(null)  // Add this line to retain fragment state
                            .commit();
                }
            }
        });

        btnCallDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef.child("phone").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        String phoneNumber = task.getResult().getValue(String.class);
                        if (phoneNumber != null) {
                            // Start the call intent
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + phoneNumber));
                            startActivity(callIntent);
                        }
                    } else {
                        // Handle the case where the phone number is not found
                        Toast.makeText(viewPostDetails.this, "Phone number not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    void init(){

        tvItemNameDetail=findViewById(R.id.tvItemNameDetails);
        tvDescriptionDetail=findViewById(R.id.tvDescripitonDetail);
        tvLocationDetail=findViewById(R.id.tvLocationDetail);
        tvStatusDetail=findViewById(R.id.tvStatusDetails);
        tvMessegeDetail=findViewById(R.id.tvMessegeDetail);
        btnMessegeDetail=findViewById(R.id.btnMessegeDetail);
        btnCallDetail=findViewById(R.id.btnCallDetail);

    }
}
