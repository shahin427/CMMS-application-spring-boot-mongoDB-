package org.sayar.net.Model.ResponseModel;

import org.sayar.net.Enumes.ResponseKeyWord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseContent {

    private boolean flag;
    private Object data;
    private List<State> states = new ArrayList<>();

    public ResponseContent() {
    }

    public ResponseContent(boolean flag, State state) {
        this.flag = flag;
        states = new ArrayList<>();
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Object getData() {
        return data;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public ResponseEntity<?> sendErrorResponseEntity(String message, ResponseKeyWord keyWord, int statusCode) {
        State state = new State(message, keyWord);
        states.add(state);
        flag = false;

        if (statusCode == 401) {
            return new ResponseEntity<>(this, HttpStatus.UNAUTHORIZED);
        } else if (statusCode == 403) {
            return new ResponseEntity<>(this, HttpStatus.FORBIDDEN);
        } else {
            return ResponseEntity.status(statusCode).body(this);
        }

    }

    public ResponseEntity<?> sendOkResponseEntity(String message, Object data) {
        State state = new State(message, ResponseKeyWord.OK);
        states.add(state);
        flag = true;
        this.data = data;

        return ResponseEntity.ok().body(this);
    }
}
