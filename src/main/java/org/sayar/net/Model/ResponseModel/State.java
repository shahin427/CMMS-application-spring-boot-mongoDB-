package org.sayar.net.Model.ResponseModel;

import org.sayar.net.Enumes.ResponseKeyWord;

public class State {

    private String message;
    private ResponseKeyWord keyWord;

    public State(String message, ResponseKeyWord keyWord) {
        this.message = message;
        this.keyWord = keyWord;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseKeyWord getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(ResponseKeyWord keyWord) {
        this.keyWord = keyWord;
    }
}
