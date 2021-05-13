package com.example.calculatrice;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import org.mariuszgromada.math.mxparser.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText display;
    private ArrayList<String> data = new ArrayList<String>();
    private ArrayAdapter matching;
    private ListView liste;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.displayEditText);
        display.setShowSoftInputOnFocus(false);
        liste = (ListView)findViewById(R.id.previousCalculationView);
        try {
            loadHistory();
        } catch (IOException e) {
            e.printStackTrace();
        }
        matching = new ArrayAdapter(this, android.R.layout.simple_list_item_1,data);
        liste.setAdapter(matching);
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = data.get(position).split("\n")[0];
                display.setText(str);
                matching.notifyDataSetChanged();
            }
        });

        liste.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                data.remove(position);
                matching.notifyDataSetChanged();
                try {
                    saveHistory();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        for (int i = 0; i < liste.getChildCount(); i++) {
            ((TextView)liste.getChildAt(i)).setTextColor(getResources().getColor(R.color.white));
        }
    }


    public void zeroButtonPush(View view){
        updateText("0");
    }

    public void oneButtonPush(View view){
        updateText("1");
    }

    public void twoButtonPush(View view){
        updateText("2");
    }

    public void threeButtonPush(View view){
        updateText("3");
    }

    public void fourButtonPush(View view){
        updateText("4");
    }

    public void fiveButtonPush(View view){
        updateText("5");
    }

    public void sixButtonPush(View view){
        updateText("6");
    }

    public void sevenButtonPush(View view){
        updateText("7");
    }

    public void eightButtonPush(View view){
        updateText("8");
    }

    public void nineButtonPush(View view){
        updateText("9");
    }

    public void parOpenButtonPush(View view){
        updateText("(");
    }

    public void parCloseButtonPush(View view){
        updateText(")");
    }

    public void decimalButtonPush(View view){
        updateText(".");
    }

    public void addButtonPush(View view){
        updateText("+");
    }

    public void subtractButtonPush(View view){
        updateText("-");
    }

    public void multiplyButtonPush(View view){
        updateText("*");
    }

    public void divideButtonPush(View view){
        updateText("/");
    }

    public void clearButtonPush(View view){
        display.setText("");
    }

    public void equalButtonPush(View view) throws IOException {
        String s = display.getText().toString();
        Expression e = new Expression(s);
        String res = String.valueOf(e.calculate());
        display.setText(res);
        data.add(s+"\n = "+res+"\n");
        matching.notifyDataSetChanged();
        saveHistory();
    }

    public void backspaceButtonPush(View view){
        int cursor = display.getSelectionStart();
        int textLength = display.getText().length();

        if(cursor != 0 && textLength != 0){
            SpannableStringBuilder selection = (SpannableStringBuilder) display.getText();
            selection.replace(cursor -1, cursor, "");
            display.setText(selection);
            display.setSelection(cursor-1);
        }
    }

    public void sinButtonPush(View view){
        updateText("sin(");
    }

    public void cosButtonPush(View view){
        updateText("cos(");
    }

    public void tanButtonPush(View view){
        updateText("tan(");
    }

    public void randButtonPush(View view){
        updateText(String.valueOf(Math.random()));
    }

    public void primeButtonPush(View view){
        updateText("ispr(");
    }

    public void roundButtonPush(View view){
        updateText("round(");
    }

    public void expButtonPush(View view){
        updateText("exp(");
    }

    public void logButtonPush(View view){
        updateText("ln(");
    }

    public void log10ButtonPush(View view){
        updateText("log10(");
    }

    public void cbrtButtonPush(View view){
        updateText("");
    }

    public void sqrtButtonPush(View view){
        updateText("sqrt(");
    }

    public void squareButtonPush(View view){
        updateText("^2");
    }

    public void cubeButtonPush(View view){
        updateText("^3");
    }

    public void powButtonPush(View view){
        updateText("^");
    }

    public void piButtonPush(View view){
        updateText("pi");
    }


    public void saveHistory() throws IOException {
        FileOutputStream out = openFileOutput("monFichier.txt", Context.MODE_APPEND);
        for (String str:data) {
            out.write(str.getBytes());
        }
        out.close();
    }

    public void loadHistory() throws IOException {
        FileInputStream in = openFileInput("monFichier.txt");
        InputStreamReader reader = new InputStreamReader(in);
        BufferedReader buf = new BufferedReader(reader);

        String lines;
        String str = "";
        int i = 0;
        while((lines = buf.readLine()) != null){
            i++;
            str += lines+"\n";
            if(i != 0 && i%2 == 0){
                data.add(str);
                str = "";
            }
        }
        in.close();
    }

    private void updateText(String s) {
        String current = display.getText().toString();
        int cursor = display.getSelectionStart();
        String left = current.substring(0, cursor);
        String right = current.substring(cursor);
        display.setText(String.format("%s%s%s", left, s, right));
        display.setSelection(cursor + s.length());
    }



}