package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String Key_Item_Text = "item_text";
    public static final String Key_Item_Position = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button button_add;
    EditText edit_item;
    RecyclerView rv;
    itemsAdapter itemsadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_add = findViewById(R.id.button_add);
        edit_item = findViewById(R.id.edit_item);
        rv = findViewById(R.id.rv);

        loadItems();
       // items.add("Buy milk");
       // items.add("Go to the gym");
       // items.add("Have fun");

        itemsAdapter.OnLongClickListener onLongClickListener = new itemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                // delete item from the model
                items.remove(position);
                // notify the adapter
                itemsadapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed",Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        itemsAdapter.OnClickListener onClickListener = new itemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity","Single click at position" + position);
            // create new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
            // pass data being edited
                i.putExtra(Key_Item_Text, items.get(position));
                i.putExtra(Key_Item_Position, position);
            //display activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };
        itemsadapter = new itemsAdapter(items, onLongClickListener, onClickListener);
        rv.setAdapter(itemsadapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        button_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String todoItem  = edit_item.getText().toString();
                //Add item to the model
                items.add(todoItem);
                //Notify adapter that item is inserted
                itemsadapter.notifyItemInserted(items.size() -1 );
                edit_item.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }

        });
    }

    // handle result of edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            // retrieve the updated text value
            String itemText = data.getStringExtra(Key_Item_Text);
            //extract original position of edited item from key position
            int position = data.getExtras().getInt(Key_Item_Position);
            //update model with new item Text at the right position
            items.set(position, itemText);
            //notify adapter of the changes
            itemsadapter.notifyItemChanged(position);
            //persist to changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
        }else{
            Log.w("MainActivity","Unknown call to onActivityResult");
        }
    }



    private File getDataFile()
    {
        return new File(getFilesDir(), "data.txt");
    }
    // function to load items by reading every line of data file
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity","Error reading items",e);
            items = new ArrayList<>();
        }
    }

    private void saveItems(){
        try{
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity","Error writing items",e);
            items = new ArrayList<>();
        }
    }

}
