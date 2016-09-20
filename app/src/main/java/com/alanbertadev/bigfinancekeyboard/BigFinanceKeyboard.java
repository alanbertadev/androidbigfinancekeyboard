package com.alanbertadev.bigfinancekeyboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Stack;

public class BigFinanceKeyboard extends Activity {

    private final static int MAX_CURSOR_POSITION = 11; //100,000,000.00

    private TextView financialOutputTextView;

    private Stack<Integer> figures = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_finance_keyboard);

        TextView helloWorld = (TextView)findViewById(R.id.ok_button);
        helloWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent("com.alanbertadev.bigfinancekeyboard.RESULT_ACTION");
                result.putExtra("value", "0.00");
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        financialOutputTextView = (TextView)findViewById(R.id.financialoutput);
    }

    private boolean pushFigure(final Integer figure) {
        if(figures.size() < MAX_CURSOR_POSITION) {
            figures.push(figure);
            refreshUI();
            return true;
        } else {
            return false;
        }
    }

    private boolean popFigure() {
        if(figures.empty()) {
            return false;
        } else {
            figures.pop();
            refreshUI();
            return true;
        }
    }

    private void refreshUI() {
        StringBuilder value = new StringBuilder();
        value.append("$");
        switch(figures.size()) {
            case 2:
                value.append("0");
                break;
            case 1:
                value.append("0.0");
                break;
            case 0:
                value.append("0.00");
                break;
            default:
                break;
        }
        for(int i=0; i<figures.size(); i++) {
            if(i==(figures.size()-2)) {
                value.append(".");
            }
            value.append(figures.elementAt(i));
        }
        financialOutputTextView.setText(value.toString());
    }
}
