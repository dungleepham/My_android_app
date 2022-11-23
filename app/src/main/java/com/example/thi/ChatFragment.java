package com.example.thi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatFragment extends Fragment {


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://appthi-27f94-default-rtdb.firebaseio.com/");

    MainActivity mMainActivity;
    Button login, register;
    TextView textView;
    FirebaseAuth auth;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mMainActivity = (MainActivity) getActivity();
        login = view.findViewById(R.id.login);
        register = view.findViewById(R.id.register);
        textView = view.findViewById(R.id.txt_or);
        auth = FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mMainActivity, Login.class));

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mMainActivity, Register.class));
            }
        });




        return view;
    }
}
