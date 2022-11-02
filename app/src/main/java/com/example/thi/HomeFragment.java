package com.example.thi;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thi.Adapter.CategoryAdapter;
import com.example.thi.Common.SpaceDecoration;
import com.example.thi.DBHelper.DBHelper;

public class HomeFragment extends Fragment {


    //Toolbar toolbar;
    RecyclerView recycler_category;
    MainActivity mMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        recycler_category = view.findViewById(R.id.recycler_category);
        recycler_category.setHasFixedSize(true);
        mMainActivity = (MainActivity) getActivity();
        recycler_category.setLayoutManager(new GridLayoutManager(mMainActivity, 2));

        DisplayMetrics displayMetrics =  new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels / 8;
        CategoryAdapter adapter = new CategoryAdapter(mMainActivity, DBHelper.getInstance(mMainActivity).getAllCategory());
        int spaceInPixel = 4;
        recycler_category.addItemDecoration(new SpaceDecoration(spaceInPixel));
        recycler_category.setAdapter(adapter);


        return view;
    }


    


}
