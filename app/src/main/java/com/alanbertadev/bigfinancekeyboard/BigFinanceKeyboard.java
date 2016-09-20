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

    private String currentValue = "0.00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_finance_keyboard);

        // setup numeric keys
        setupKeyboardKey(R.id.zero_button, 0);
        setupKeyboardKey(R.id.one_button, 1);
        setupKeyboardKey(R.id.two_button, 2);
        setupKeyboardKey(R.id.three_button, 3);
        setupKeyboardKey(R.id.four_button, 4);
        setupKeyboardKey(R.id.five_button, 5);
        setupKeyboardKey(R.id.six_button, 6);
        setupKeyboardKey(R.id.seven_button, 7);
        setupKeyboardKey(R.id.eight_button, 8);
        setupKeyboardKey(R.id.nine_button, 9);

        TextView delButton = (TextView)findViewById(R.id.del_button);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popFigure();
            }
        });

        TextView okButton = (TextView)findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent("com.alanbertadev.bigfinancekeyboard.RESULT_ACTION");
                result.putExtra("value", currentValue);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.financialOutputTextView = (TextView)findViewById(R.id.financialoutput);
    }

    private void setupKeyboardKey(final int resourceId, final int figure)
    {
        final TextView key = (TextView)findViewById(resourceId);
        key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFigure(figure);
            }
        });
    }

    private boolean pushFigure(final int figure) {
        if(this.figures.size() < MAX_CURSOR_POSITION) {
            this.figures.push(figure);
            refreshUI();
            return true;
        } else {
            return false;
        }
    }

    private boolean popFigure() {
        if(this.figures.empty()) {
            return false;
        } else {
            this.figures.pop();
            refreshUI();
            return true;
        }
    }

    private void refreshUI() {
        StringBuilder value = new StringBuilder();
        switch(this.figures.size()) {
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
        for(int i=0; i<this.figures.size(); i++) {
            if(i==(this.figures.size()-2)) {
                value.append(".");
            }
            value.append(this.figures.elementAt(i));
        }
        this.currentValue = value.toString();
        value.insert(0,"$");
        this.financialOutputTextView.setText(value.toString());
    }
}
