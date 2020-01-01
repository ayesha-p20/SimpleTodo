package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {


    EditText etItem;
    Button button_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etItem = findViewById(R.id.etItem);
        button_save = findViewById(R.id.button_save);

        getSupportActionBar().setTitle("Edit Item");

        etItem.setText(getIntent().getStringExtra(MainActivity.Key_Item_Text));
        //clicked when user is done editing
        button_save.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //create an intent which will contain results of whatever has been modified
                Intent intent = new Intent();

              //pass results
                intent.putExtra(MainActivity.Key_Item_Text,etItem.getText().toString());
                intent.putExtra(MainActivity.Key_Item_Position, getIntent().getExtras().getInt(MainActivity.Key_Item_Position));
              //set result of intent
                setResult(RESULT_OK, intent);
              //finish activity, close current screen and go back
                finish();
            }
        }));

    }
}
