package com.example.thi.Common;

import android.os.CountDownTimer;

import com.example.thi.QuestionFragment;
import com.example.thi.model.CurrentQuestion;
import com.example.thi.model.Question;
import com.example.thi.model.category;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Common {

    public static final int TOTAL_TIME = 10*60*1000;
    public static final String KEY_BACK_FROM_RESULT = "BACK_FROM_RESULT";
    public static final String KEY_GO_TO_QUESTION = "GO_TO_QUESTION";

    public static category SelectedCategory = new category();
    public static List<Question> questionList = new ArrayList<>();
    public static List<CurrentQuestion> answerSheetList = new ArrayList<>();
    public static List<CurrentQuestion> answerSheetListFiltered = new ArrayList<>();

    public static CountDownTimer countDownTimer;
    public static int right_answer_count = 0;
    public static int wrong_answer_count = 0;
    public static ArrayList<QuestionFragment> FragmentsList = new ArrayList<>();
    public static TreeSet<String> selected_values = new TreeSet<>();

    public static int timer = 0;
    public static int no_answer_count = 0;
    public static StringBuilder data_question = new StringBuilder();

    public enum ANSWER_TYPE{
        NO_ANSWER,
        WRONG_ANSWER,
        RIGHT_ANSWER
    }

}
