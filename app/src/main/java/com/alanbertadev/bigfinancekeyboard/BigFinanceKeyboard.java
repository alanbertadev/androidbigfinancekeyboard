package com.alanbertadev.bigfinancekeyboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BigFinanceKeyboard extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_finance_keyboard);

        TextView helloWorld = (TextView)findViewById(R.id.helloworldtextview);
        helloWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent("com.alanbertadev.bigfinancekeyboard.RESULT_ACTION");
                result.putExtra("value", 5);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
    }


}
