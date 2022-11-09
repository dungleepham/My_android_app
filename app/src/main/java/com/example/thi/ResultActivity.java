package com.example.thi;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thi.Adapter.ResultGridAdapter;
import com.example.thi.Common.Common;
import com.example.thi.Common.SpaceDecoration;

import java.util.concurrent.TimeUnit;

public class ResultActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView txt_timer, txt_result, txt_right_answer;
    Button btn_filter_total, btn_filter_right, btn_filter_wrong, btn_filter_no_answer;
    RecyclerView recycler_result;
    ResultGridAdapter adapter, filtered_adapter;


    BroadcastReceiver backToQuestion = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().toString().equals(Common.KEY_BACK_FROM_RESULT))
            {
                int question = intent.getIntExtra(Common.KEY_BACK_FROM_RESULT, -1);
                goBackActivityWithQuestion(question);
            }
        }
    };

    private void goBackActivityWithQuestion(int question) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Common.KEY_BACK_FROM_RESULT, question);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(backToQuestion, new IntentFilter(Common.KEY_BACK_FROM_RESULT));


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Kết quả");
        //setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        txt_result = (TextView) findViewById(R.id.txt_result);
        txt_right_answer = (TextView) findViewById(R.id.txt_right_answer);
        txt_timer = (TextView) findViewById(R.id.txt_time);

        btn_filter_total = (Button) findViewById(R.id.btn_filter_total);
        btn_filter_right = (Button) findViewById(R.id.btn_filter_right_answer);
        btn_filter_wrong = (Button) findViewById(R.id.btn_filter_wrong_answer);
        btn_filter_no_answer = (Button) findViewById(R.id.btn_filter_no_answer);


        recycler_result = (RecyclerView) findViewById(R.id.recycler_result);
        recycler_result.setHasFixedSize(true);
        recycler_result.setLayoutManager(new GridLayoutManager(this, 2));


        adapter = new ResultGridAdapter(this, Common.answerSheetList);
        recycler_result.addItemDecoration(new SpaceDecoration(4));
        recycler_result.setAdapter(adapter);

        txt_timer.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(Common.timer),
                TimeUnit.MILLISECONDS.toSeconds(Common.timer)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Common.timer))));

        txt_right_answer.setText(new StringBuilder("").append(Common.right_answer_count).append("/").append(Common.questionList.size()));
        btn_filter_total.setText(new StringBuilder("").append(Common.questionList.size()));
        btn_filter_right.setText(new StringBuilder("").append(Common.right_answer_count));
        btn_filter_wrong.setText(new StringBuilder("").append(Common.wrong_answer_count));
        btn_filter_no_answer.setText(new StringBuilder("").append(Common.no_answer_count));

        int percent = (Common.right_answer_count*100/Common.questionList.size());
        if(percent > 90)
        {
            txt_result.setText("Rất tốt");
        }
        else if(percent > 70)
        {
            txt_result.setText("Tốt");
        }
        else if(percent > 50){
            txt_result.setText("Đạt");
        }
        else {
            txt_result.setText("trượt");
        }

        btn_filter_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter == null)
                {
                    adapter = new ResultGridAdapter(ResultActivity.this, Common.answerSheetList);
                    recycler_result.setAdapter(adapter);
                }
                else
                {
                    recycler_result.setAdapter(adapter);
                }
            }
        });

        btn_filter_no_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.answerSheetListFiltered.clear();
                for(int i = 0; i < Common.answerSheetList.size(); i++){
                    if(Common.answerSheetList.get(i).getType() == Common.ANSWER_TYPE.NO_ANSWER )
                        Common.answerSheetListFiltered.add(Common.answerSheetList.get(i));
                }
                filtered_adapter = new ResultGridAdapter(ResultActivity.this, Common.answerSheetListFiltered);
                recycler_result.setAdapter(filtered_adapter);
            }
        });

        btn_filter_wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.answerSheetListFiltered.clear();
                for(int i = 0; i < Common.answerSheetList.size(); i++){
                    if(Common.answerSheetList.get(i).getType() == Common.ANSWER_TYPE.WRONG_ANSWER )
                        Common.answerSheetListFiltered.add(Common.answerSheetList.get(i));
                }
                filtered_adapter = new ResultGridAdapter(ResultActivity.this, Common.answerSheetListFiltered);
                recycler_result.setAdapter(filtered_adapter);
            }
        });

        btn_filter_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.answerSheetListFiltered.clear();
                for(int i = 0; i < Common.answerSheetList.size(); i++){
                    if(Common.answerSheetList.get(i).getType() == Common.ANSWER_TYPE.RIGHT_ANSWER )
                        Common.answerSheetListFiltered.add(Common.answerSheetList.get(i));
                }
                filtered_adapter = new ResultGridAdapter(ResultActivity.this, Common.answerSheetListFiltered);
                recycler_result.setAdapter(filtered_adapter);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.result_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menu_do_quiz_again:
                doQuizAgain();
                break;
            case R.id.menu_view_answer:
                viewQuizAnswer();
                break;
            case R.id.action_home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

        }
        return true;
    }

    private void viewQuizAnswer() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("action", "viewquizanswer");
        setResult(Activity.RESULT_OK, returnIntent);
    }

    private void doQuizAgain() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        //windowAttributes.gravity = position;
        window.setAttributes(windowAttributes);


        Button btnNo = dialog.findViewById(R.id.btn_no);
        Button btnFinish = dialog.findViewById(R.id.btn_finish);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
               Intent returnIntent = new Intent();
               returnIntent.putExtra("action", "doitagain");
               setResult(Activity.RESULT_OK, returnIntent);
               finish();
            }
        });
        dialog.show();

    }
}