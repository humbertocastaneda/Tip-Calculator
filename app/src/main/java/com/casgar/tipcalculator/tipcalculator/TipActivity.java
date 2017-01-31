package com.casgar.tipcalculator.tipcalculator;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TipActivity extends AppCompatActivity {

    LinearLayout ll_content;
    Button b_add;
    SeekBar sb_percetage;
    TextView tv_split,tv_percentage;
    Button b_plus, b_minus;
    TextView tv_total;

    int split = 1;

    List<Double> amounts;

    double total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);

        amounts = new ArrayList<>();

        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        b_add = (Button) findViewById(R.id.b_add);
        sb_percetage = (SeekBar) findViewById(R.id.sb_percentage);
        tv_split = (TextView) findViewById(R.id.tv_split);
        b_plus = (Button) findViewById( R.id.b_plus);
        b_minus = (Button) findViewById(R.id.b_minus);
        tv_total = (TextView) findViewById(R.id.tv_total);
        tv_percentage  = (TextView) findViewById(R.id.tv_percentage);

        setPercentage(sb_percetage.getProgress());


        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showItemsDialog();
            }
        });

        sb_percetage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setPercentage(progress);
                calculate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        View.OnClickListener splitListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                split (((Button)v).getText().toString());
                calculate();
            }
        };

        b_minus.setOnClickListener(splitListener);
        b_plus.setOnClickListener(splitListener);
    }

    public void split(String operation){
        if (operation.equals("+")){
            if (split==50){
                return;
            }
            split++;
        }
        else{
            if (split==1){
                return;
            }
            split--;
        }

        tv_split.setText(String.valueOf(split));

    }

    private void setPercentage(int progress){
        tv_percentage.setText(String.valueOf(progress + "%"));
    }

    private void calculate(){
        total = 0;
        for (double amount:amounts){
            total += amount;
        }

        total =  total / split;

        total = total + (total * (sb_percetage.getProgress() / 100.0));

        DecimalFormat df = new DecimalFormat( "#,###,###,##0.00" );;
        tv_total.setText("$ " + df.format(total));
    }

    private void showItemsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.amount_layout, null);

        final EditText editText = (EditText) view.findViewById(R.id.et_amout);

        builder.setTitle("Set Amount");
        builder.setView(view);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Double amount = Double.parseDouble(editText.getText().toString());
                amounts.add(amount);
                addNewAmount( amount);
                calculate();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setCancelable(true);

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void addNewAmount(double amount){
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.amounts_layout, null);

        view.setTag(new Integer (amounts.size()));

        TextView textView = (TextView) view.findViewById(R.id.tv_amount);

        textView.setText(String.valueOf(amount));

        ll_content.addView(view);

    }
}
