package com.example.dimtz.calcexchange;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    private int numberOne, numberTwo;
    private EditText numberText, fromCur, toCur;
    private Spinner rateSpinner, rateSpinnerTo;
    private Map<String, String> spinnerMap;
    private TableLayout buttons;
    private List<String> curs;


    private List<String> curs2;
    private String itemSelected;
    private double result;
    private boolean clacFlag;
    private char selectedAction;
    private ArrayAdapter<String> adapter, adapterTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fromCur = (EditText) findViewById(R.id.fromCur);
        fromCur.setInputType(0);
        toCur = (EditText) findViewById(R.id.toCur);
        toCur.setInputType(0);

        numberText = (EditText) findViewById(R.id.numberText);
        numberText.setInputType(0);
        result = 0;
        clacFlag = false;
        buttons = (TableLayout) findViewById(R.id.buttonsContainer);
        for (int i = 2; i < 6; i++) {
            TableRow row = (TableRow) buttons.getChildAt(i);
            for (int j = 0; j < 4; j++) {
                final Button b = (Button) row.getChildAt(j);
                if (Character.isDigit(b.getText().charAt(0))) {
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (fromCur.hasFocus()) {
                                fromCur.setText(fromCur.getText().append(b.getText()));
                            }
                            if (numberText.hasFocus()) {
                                if (!clacFlag) {
                                    numberText.setText(numberText.getText().toString() + b.getText());
                                    if (result == 0) result += Double.parseDouble(numberText.getText().toString());
                                }

                                else {
                                    numberText.setText("" + b.getText());

                                    clacFlag = false;
                                }
                            }

                        }
                    });
                } else {
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (b.getText().charAt(0)) {
                                case '.':
                                    if (numberText.getText().toString().isEmpty())
                                        numberText.setText("0.");
                                    else
                                        numberText.setText(numberText.getText().append('.'));
                                    break;
                                case '+':
                                    if (!clacFlag && selectedAction != 0) {
                                        clacFlag = true;
                                        result = calculate(result, Double.parseDouble(numberText.getText().toString()), selectedAction);
                                        selectedAction = '+';
                                        numberText.setText(String.valueOf(result));

                                    } else {
                                        selectedAction = '+';
                                        clacFlag = true;
                                    }
                                    break;
                                case '-':
                                    if (!clacFlag && selectedAction != 0) {
                                        clacFlag = true;
                                        result = calculate(result, Double.parseDouble(numberText.getText().toString()), selectedAction);
                                        selectedAction = '-';
                                        numberText.setText(String.valueOf(result));
                                    } else {
                                        selectedAction = '-';
                                        clacFlag = true;
                                    }
                                    break;
                                case 'x':
                                    if (!clacFlag && selectedAction != 0) {
                                        clacFlag = true;
                                        result = calculate(result, Double.parseDouble(numberText.getText().toString()), selectedAction);
                                        selectedAction = 'x';
                                        numberText.setText(String.valueOf(result));
                                    } else {
                                        selectedAction = 'x';
                                        clacFlag = true;
                                    }
                                    break;
                                case '/':
                                    if (!clacFlag && selectedAction != 0) {
                                        clacFlag = true;
                                        try  {
                                            result = calculate(result, Double.parseDouble(numberText.getText().toString()), selectedAction);
                                            selectedAction = '/';
                                            numberText.setText(String.valueOf(result));
                                        }
                                        catch (NumberFormatException ex){
                                            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        selectedAction = '/';
                                        clacFlag = true;
                                    }
                                    break;
                                case '=':
                                    if (!clacFlag && selectedAction != 0) {
                                        clacFlag = true;
                                        try {
                                            result = calculate(result, Double.parseDouble(numberText.getText().toString()), selectedAction);
                                            selectedAction = 0;
                                            numberText.setText(String.valueOf(result));
                                        } catch (NumberFormatException e){
                                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        result = calculate(result, Double.parseDouble(numberText.getText().toString()), selectedAction);
                                        clacFlag = true;
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    });

                }
            }

        }

        findViewById(R.id.clearBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberText.setText("");
                fromCur.setText("");
                toCur.setText("");
                result = 0;
                clacFlag = false;
            }
        });

        findViewById(R.id.delBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(getCurrentFocus().getId());
                int length = numberText.getText().length();
                int length2 = fromCur.getText().length();
                int length3 = toCur.getText().length();
                if (length > 0 && numberText.hasFocus())
                    numberText.getText().delete(length - 1, length);
                if (length2 > 0 && fromCur.hasFocus())
                    fromCur.getText().delete(length2 - 1, length2);
                if (length3 > 0 && toCur.hasFocus())
                    toCur.getText().delete(length3 - 1, length3);
            }
        });


        spinnerMap = new HashMap<>();

        curs = new ArrayList<>();
        curs2 = new ArrayList<>();

        makeTheRequest(itemSelected);


        rateSpinner = (Spinner) findViewById(R.id.rateSpinnerFrom);
        rateSpinnerTo = (Spinner) findViewById(R.id.rateSpinnerTo);


        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, curs){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setTextColor(Color.BLACK);
                return  textView;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.BLACK);
                return  textView;
            }
        };
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        rateSpinner.setAdapter(adapter);

        adapterTo = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, curs2){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setTextColor(Color.BLACK);
                return  textView;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.BLACK);
                return  textView;
            }
        };

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        rateSpinnerTo.setAdapter(adapterTo);


        rateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int count = 0;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (count >= 1) {
                    itemSelected = rateSpinner.getSelectedItem().toString();
                    makeTheRequest(itemSelected);
                    System.out.println(getSpinnerMap());
                    rateSpinner.setSelection(0);
                }
                count++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void handleChangeCurrency() {


        fromCur.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Double res = Double.parseDouble(fromCur.getText().toString()) * Double.parseDouble(spinnerMap.get(rateSpinnerTo.getSelectedItem().toString()));
                    toCur.setText(res.toString());
                } catch (NumberFormatException ex){

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private double calculate(double a, double b, char sl) throws NumberFormatException{
        switch (sl) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case 'x':
                return a * b;
            case '/':
                if (b != 0)
                    return a / b;
                else
                    throw new NumberFormatException("Cannot divide with 0");
            default:
                return -0;

        }
    }


    private void makeTheRequest(String baseRate) {

        spinnerMap = new HashMap<>();
        curs = new ArrayList<>();
        curs2 = new ArrayList<>();


        String url = "http://api.fixer.io/latest?base=" + baseRate;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String base = "";
                        String date = "";
                        JSONObject currencies = null;


                        try {
                            base = response.getString("base");
                            date = response.getString("date");
                            currencies = response.getJSONObject("rates");
                            for (int i = 0; i < currencies.names().length(); i++) {
                                String currency = currencies.names().getString(i);

                                curs.add(i, currency);
                                curs2.add(i, currency);
                                String rate = currencies.getString(currency);
                                spinnerMap.put(currency, rate);

                            }
                            curs.add(0, base);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setCurs(curs);
                        setCurs2(curs2);
                        setSpinnerMap(spinnerMap);


                        adapter.clear();
                        adapter.addAll(curs);
                        adapter.notifyDataSetChanged();

                        adapterTo.clear();
                        adapterTo.addAll(curs2);
                        adapterTo.notifyDataSetChanged();
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        makeTheRequest("EUR");

                    }
                });
        // Access the RequestQueue through your singleton class.
        ConnectionSnigleton.getInstance(this).addToRequestQueue(jsObjRequest);

    }

    @Override
    protected void onStart() {
        super.onStart();


        handleChangeCurrency();


    }

    public List<String> getCurs() {
        return curs;
    }

    public void setCurs(List<String> curs) {
        this.curs = curs;
    }

    public Map<String, String> getSpinnerMap() {
        return spinnerMap;
    }

    public void setSpinnerMap(Map<String, String> spinnerMap) {
        this.spinnerMap = spinnerMap;
    }

    public List<String> getCurs2() {

        return curs2;
    }

    public void setCurs2(List<String> curs2) {

        this.curs2 = curs2;
    }
}
