package com.example.thi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.thi.Adapter.AnswerSheetAdapter;
import com.example.thi.Adapter.QuestionFragmentAdapter;
import com.example.thi.Common.Common;
import com.example.thi.DBHelper.DBHelper;
import com.example.thi.databinding.ActivityQuestionBinding;
import com.example.thi.model.CurrentQuestion;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;


public class QuestionActivity extends AppCompatActivity {

    private static final int CODE_GET_RESULT = 9999;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityQuestionBinding binding;


    int time_play = Common.TOTAL_TIME;
    boolean isAnswerModelView = false;


    RecyclerView answer_sheet_view;
    AnswerSheetAdapter answerSheetAdapter;
    TextView txt_right_answer, txt_timer, txt_wrong_answer;

    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onDestroy() {
        if(Common.countDownTimer != null)
            Common.countDownTimer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarQuestion.toolbar);

        binding.appBarQuestion.toolbar.setTitle(Common.SelectedCategory.getName());
        setSupportActionBar(binding.appBarQuestion.toolbar);
        //fab.setOnClickListener(new View.OnClickListener() {
            /*@Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = binding.drawerLayout;
        //NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
       // NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_question);
       // NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
       // NavigationUI.setupWithNavController(navigationView, navController);


        //lay DL tu DB
        takeQuestion();
        if(Common.questionList.size() > 0){

            txt_right_answer = (TextView) findViewById(R.id.txt_question_right);
            txt_timer = (TextView) findViewById(R.id.txt_timer);


            //txt_right_answer.setVisibility(View.VISIBLE);
            txt_timer.setVisibility(View.VISIBLE);

            txt_right_answer.setText(new StringBuilder(String.format("%d/%d", Common.right_answer_count, Common.questionList.size())).toString());

            CountTimer();


           // answer_sheet_view = (RecyclerView) findViewById(R.id.grid_answer);
           // answer_sheet_view.setHasFixedSize(true);
            if(Common.questionList.size() > 5)
//                answer_sheet_view.setLayoutManager(new GridLayoutManager(this, Common.questionList.size()/2));
//            answerSheetAdapter = new AnswerSheetAdapter(this, Common.answerSheetList);
//            answer_sheet_view.setAdapter(answerSheetAdapter);

            viewPager = (ViewPager) findViewById(R.id.view_pager);
            viewPager.setOffscreenPageLimit(Common.questionList.size());
            tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

            genFragmentList();

            QuestionFragmentAdapter questionFragmentAdapter = new QuestionFragmentAdapter(getSupportFragmentManager(), this, Common.FragmentsList);
            viewPager.setAdapter(questionFragmentAdapter);

            tabLayout.setupWithViewPager(viewPager);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                int SCROLLING_RIGHT = 0;
                int SCROLLING_LEFT = 1;
                int SCROLLING_UNDETERMINED = 2;

                int currentScrollDirection = 2;
                private void setCurrentScrollDirection(float positionOffset){
                    if((1-positionOffset) >= 0.5){
                        this.currentScrollDirection = SCROLLING_RIGHT;
                    }
                    else if((1-positionOffset) <= 0.5){
                        this.currentScrollDirection = SCROLLING_LEFT;
                    }

                }

                private boolean isScrollDirectionUndetermined(){
                    return currentScrollDirection == SCROLLING_UNDETERMINED;
                }

                private boolean isScrollDirectionRight(){
                    return currentScrollDirection == SCROLLING_RIGHT;
                }

                private boolean isScrollDirectionLeft(){
                    return currentScrollDirection == SCROLLING_LEFT;
                }

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if(isScrollDirectionUndetermined()){
                        setCurrentScrollDirection(positionOffset);
                    }
                }

                @Override
                public void onPageSelected(int position) {
                    QuestionFragment questionFragment;
                    int subPosition = position-1;
                    if(position > 0)
                    {
                        if(isScrollDirectionRight()){
                            questionFragment = Common.FragmentsList.get(position-1);
                            subPosition = position-1;
                        }
                        else if(isScrollDirectionLeft()){
                            questionFragment = Common.FragmentsList.get(position+1);
                            subPosition = position + 1;
                        }
                        else{
                            questionFragment = Common.FragmentsList.get(subPosition);
                        }
                    }
                    else {
                        questionFragment = Common.FragmentsList.get(0);
                        subPosition = 0;
                    }
                    CurrentQuestion question_state = questionFragment.getSelectedAnswer();
                    Common.answerSheetList.set(subPosition, question_state);
//                    answerSheetAdapter.notifyDataSetChanged();

                    countCorrectAnswer();

                    txt_right_answer.setText(new StringBuilder(String.format("%d", Common.right_answer_count))
                            .append("/")
                            .append(String.format("%d", Common.questionList.size())).toString());
                    //txt_wrong_answer.setText(String.valueOf(Common.wrong_answer_count));

                    if(question_state.getType() == Common.ANSWER_TYPE.NO_ANSWER){
                       // questionFragment.showCorrectAnswer();
                       // questionFragment.disableAnswer();
                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                        if(state == ViewPager.SCROLL_STATE_IDLE)
                            this.currentScrollDirection = SCROLLING_UNDETERMINED;
                }
            });

        }



    }



    private void finishGame() {

        int position = viewPager.getCurrentItem();
        QuestionFragment questionFragment = Common.FragmentsList.get(position);

        CurrentQuestion question_state = questionFragment.getSelectedAnswer();
        Common.answerSheetList.set(position, question_state);
        //answerSheetAdapter.notifyDataSetChanged();

        countCorrectAnswer();

        txt_right_answer.setText(new StringBuilder(String.format("%d", Common.right_answer_count))
                .append("/")
                .append(String.format("%d", Common.questionList.size())).toString());
        //txt_wrong_answer.setText(String.valueOf(Common.wrong_answer_count));
        if(question_state.getType() == Common.ANSWER_TYPE.NO_ANSWER){
            questionFragment.showCorrectAnswer();
            questionFragment.disableAnswer();
        }
        Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
        Common.timer = Common.TOTAL_TIME - time_play;
        Common.no_answer_count = Common.questionList.size() - (Common.right_answer_count + Common.wrong_answer_count);
        Common.data_question = new StringBuilder(new Gson().toJson(Common.answerSheetList));

        startActivityForResult(intent, CODE_GET_RESULT);
    }

    private void countCorrectAnswer() {
        Common.right_answer_count = Common.wrong_answer_count = 0;
        for(CurrentQuestion item : Common.answerSheetList)
        {
            if(item.getType() == Common.ANSWER_TYPE.RIGHT_ANSWER){
                Common.right_answer_count++;

            }
            else if(item.getType() == Common.ANSWER_TYPE.WRONG_ANSWER){
                Common.wrong_answer_count++;
            }
        }
    }

    private void genFragmentList() {

        for(int i = 0; i < Common.questionList.size(); i++){
            Bundle bundle = new Bundle();
            bundle.putInt("index", i);
            QuestionFragment fragment = new QuestionFragment();
            fragment.setArguments(bundle);

            Common.FragmentsList.add(fragment);
        }
    }


    private void CountTimer() {
        if(Common.countDownTimer == null){
            Common.countDownTimer = new CountDownTimer(Common.TOTAL_TIME, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    txt_timer.setText(String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                            ));
                    time_play -= 1000;
                }

                @Override
                public void onFinish() {

                }
            }.start();
        }
        else{
            Common.countDownTimer.cancel();
            Common.countDownTimer = new CountDownTimer(Common.TOTAL_TIME, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    txt_timer.setText(String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                    ));
                    time_play -= 1000;
                }

                @Override
                public void onFinish() {

                }
            }.start();
        }
    }

    private void takeQuestion() {
        Common.questionList = DBHelper.getInstance(this).getQuestionByCategory(Common.SelectedCategory.getId());
        if((Common.questionList.size() == 0)){

        }
         else{
            if(Common.answerSheetList.size() > 0)
                Common.answerSheetList.clear();
             for(int i = 0; i < Common.questionList.size(); i++){
                 Common.answerSheetList.add(new CurrentQuestion(i, Common.ANSWER_TYPE.NO_ANSWER));

             }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question, menu);
        return true;
    }

    private void openFinishDialog(int position){
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
        windowAttributes.gravity = position;
        window.setAttributes(windowAttributes);

        if(Gravity.BOTTOM == position){
            dialog.setCancelable(true);
        }
        else{
            dialog.setCancelable(false);
        }

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
                finishGame();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.finish_game){
            if(!isAnswerModelView){
            openFinishDialog(id);

            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CODE_GET_RESULT)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                String action = data.getStringExtra("action");
                if(action == null || TextUtils.isEmpty(action))
                {
                    int questionNum = data.getIntExtra(Common.KEY_BACK_FROM_RESULT, -1);
                    viewPager.setCurrentItem(questionNum);
                    isAnswerModelView = true;
                    Common.countDownTimer.cancel();

                    //txt_right_answer.setVisibility(View.GONE);
                    txt_timer.setVisibility(View.GONE);
                }
                else
                {
                    if(action.equals("viewquizanswer"))
                    {
                        viewPager.setCurrentItem(0);
                        isAnswerModelView = true;
                        Common.countDownTimer.cancel();

                        //txt_right_answer.setVisibility(View.GONE);
                        txt_timer.setVisibility(View.GONE);

                        for(int i = 0; i< Common.FragmentsList.size(); i++)
                        {
                            Common.FragmentsList.get(i).showCorrectAnswer();
                            Common.FragmentsList.get(i).disableAnswer();
                        }
                    }
                    else if(action.equals("doitagain"))
                    {
                        viewPager.setCurrentItem(0);
                        isAnswerModelView = false;
                        CountTimer();

                       // txt_right_answer.setVisibility(View.VISIBLE);
                        txt_timer.setVisibility(View.VISIBLE);

                        for(CurrentQuestion item:Common.answerSheetList)
                            item.setType(Common.ANSWER_TYPE.NO_ANSWER);
                        //answerSheetAdapter.notifyDataSetChanged();
                        //AnswerSheetHelperAdapter.notifyDataSetChanged();

                        for(int i = 0; i < Common.FragmentsList.size(); i++)
                        {
                            Common.FragmentsList.get(i).resetQuestion();

                        }

                    }
                }
            }
        }
    }

    /* @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_question);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }*/
}