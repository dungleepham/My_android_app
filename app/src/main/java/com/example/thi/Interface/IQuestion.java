package com.example.thi.Interface;

import com.example.thi.model.CurrentQuestion;

public interface IQuestion {
    CurrentQuestion getSelectedAnswer();

    void showCorrectAnswer();
    void disableAnswer();
    void resetQuestion();
}
