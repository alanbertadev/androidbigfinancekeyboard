package com.alanbertadev.bigfinancekeyboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Stack;

public class BigFinanceKeyboard extends Activity {

    public final static String INTENT_EXTRA_KEY = "value";
    public final static String INTENT_SUPPRESS_FINANCIAL_INDICATOR_KEY = "isamountonly";

    private final static int MAX_CURSOR_POSITION = 11; //100,000,000.00
    private final static String DEFAULT_VALUE = "0.00";

    private TextView financialOutputTextView;

    private Stack<Integer> figures = new Stack<>();
    private BigDecimal bigDecimalValue = BigDecimal.ZERO;

    private boolean isAmountOnly = false;

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

        final TextView delButton = (TextView)findViewById(R.id.del_button);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popFigure();
            }
        });

        final TextView okButton = (TextView)findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent("com.alanbertadev.bigfinancekeyboard.RESULT_ACTION");
                result.putExtra(INTENT_EXTRA_KEY, bigDecimalValue.toString());
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.financialOutputTextView = (TextView)findViewById(R.id.financialoutput);

        final Intent intent = getIntent();
        this.isAmountOnly = intent.getBooleanExtra(INTENT_SUPPRESS_FINANCIAL_INDICATOR_KEY, false);
        setupViewWithInput(intent.getStringExtra(INTENT_EXTRA_KEY));
    }

    private void setupViewWithInput(final String input) {
        String currentValue = input;
        // input to the finance keyboard must be less than MAX_CURSOR_POSITION and contain only numbers and 1 decimal
        if ((currentValue == null) ||
                (currentValue.length() > MAX_CURSOR_POSITION)
                || !(currentValue.matches("^[0-9]*\\.?[0-9]*$"))) {
            currentValue = DEFAULT_VALUE;
        }

        // if there is no period, append the current value with 0 change
        if(!currentValue.contains(".")) {
            currentValue = currentValue + ".00";
        }

        // scrub fuzzy decimal placement
        if (currentValue.charAt(currentValue.length() - 1) == '.') {
            currentValue = currentValue + "00";
        } else if (currentValue.charAt(currentValue.length() - 2) == '.') {
            currentValue = currentValue + "0";
        }

        // build entry stack from input
        try {
            for (int i=0; i<currentValue.length(); i++) {
                if(currentValue.charAt(i) != '.') {
                    this.figures.push(Integer.parseInt(String.valueOf(currentValue.charAt(i))));
                }
            }
        } catch (final NumberFormatException error) {
            this.figures.clear();
            currentValue = DEFAULT_VALUE;
        }
        bigDecimalValue = new BigDecimal(currentValue);
        refreshUI();
    }

    private void setupKeyboardKey(final int resourceId, final int figure) {
        final TextView key = (TextView) findViewById(resourceId);
        key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFigure(figure);
            }
        });
    }

    private boolean pushFigure(final int figure) {
        if(this.figures.size()>0 && this.figures.elementAt(0).equals(0)) {
            this.figures.remove(0);
        }
        if (this.figures.size() < MAX_CURSOR_POSITION) {
            this.figures.push(figure);
            refreshUI();
            return true;
        } else {
            return false;
        }
    }

    private boolean popFigure() {
        if (this.figures.empty()) {
            return false;
        } else {
            this.figures.pop();
            refreshUI();
            return true;
        }
    }

    private void refreshUI() {
        bigDecimalValue = BigDecimal.ZERO;
        for (int i=0; i<this.figures.size(); i++) {
            final BigDecimal shiftThisValue = new BigDecimal(this.figures.get((this.figures.size()-1-i)));
            bigDecimalValue = bigDecimalValue.add(shiftThisValue.movePointRight(i));
        }
        bigDecimalValue = bigDecimalValue.movePointLeft(2);
        if(this.isAmountOnly) {
            this.financialOutputTextView.setText(getNumberFormatWithoutCurrencySymbol(bigDecimalValue));
        } else {
            this.financialOutputTextView.setText(NumberFormat.getCurrencyInstance().format(bigDecimalValue));
        }
    }

    private String getNumberFormatWithoutCurrencySymbol(final BigDecimal bigDecimal) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);
        return nf.format(bigDecimal).trim();
    }
}
