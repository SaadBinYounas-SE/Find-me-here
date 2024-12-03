package com.example.findmehere;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class viewPostDetails extends AppCompatActivity {

//    ImageView ivItemPicDetail;
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
        String itemPostImageUrl = getIntent().getStringExtra("itemPostImageUrl");

        init();

        tvItemNameDetail.setText(itemName);
        tvDescriptionDetail.setText(description);
        tvLocationDetail.setText(location);
        tvMessegeDetail.setText(messege);
        tvStatusDetail.setText(status);

//        Glide.with(getApplicationContext())
//                .load(itemPostImageUrl)
//                .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
//                .into(ivItemPicDetail);


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

    }
    void init(){

//        ivItemPicDetail=findViewById(R.id.ivItemPicDetails);
        tvItemNameDetail=findViewById(R.id.tvItemNameDetails);
        tvDescriptionDetail=findViewById(R.id.tvDescripitonDetail);
        tvLocationDetail=findViewById(R.id.tvLocationDetail);
        tvStatusDetail=findViewById(R.id.tvStatusDetails);
        tvMessegeDetail=findViewById(R.id.tvMessegeDetail);
        btnMessegeDetail=findViewById(R.id.btnMessegeDetail);
        btnCallDetail=findViewById(R.id.btnCallDetail);

    }
}
