package com.example.todo1.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.todo1.R;
import com.example.todo1.addEdit.AddEditTodoActivity;
import com.example.todo1.data.MyDatabase;
import com.example.todo1.data.TodoItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView main_rcv;
    private FloatingActionButton main_fab;
    private MainTodoAdapter adapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("TODO APP");
        }

        main_rcv = findViewById(R.id.main_rcv);
        main_fab = findViewById((R.id.main_fab_intent));

        adapter = new MainTodoAdapter();


        main_rcv.setAdapter(adapter);
        main_rcv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        final MyDatabase myDatabase = MyDatabase.getInstance(this);
        List<TodoItem> list_item = myDatabase.todoDao().getAllTodo();
        adapter.submitAll(list_item);

        main_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddEditTodoActivity.class);
                intent.putExtra("mode",0);
                startActivity(intent);

            }
        });
        //지울것
        // FloatingActionButton test  = findViewById(R.id.main_fab_intent);
        // test.setOnClickListener(new View.OnClickListener() {


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_item:
                adapter.removeAllITem();
                MyDatabase myDatabase = MyDatabase.getInstance(this);
                myDatabase.todoDao().deleteAllTodo();
                break;

            case R.id.delete_selected_item:

                MyDatabase myDatabase1 = MyDatabase.getInstance(this);
                List<TodoItem> itemList = myDatabase1.todoDao().getAllTodo();
                List<TodoItem> newList = new ArrayList<>();
                for (int i = 0; i < itemList.size(); i++) {
                    if (adapter.checkItem(itemList.get(i))) {
                        myDatabase1 = MyDatabase.getInstance(this);
                        myDatabase1.todoDao().deleteTodo(itemList.get(i));

                    }else{
                        newList.add(itemList.get(i));
                    }

                }
                adapter.removeAllItem(newList);

                break;

        }
        adapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MyDatabase myDatabase = MyDatabase.getInstance(this);
        adapter.submitAll(myDatabase.todoDao().getAllTodo());
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyDatabase myDatabase = MyDatabase.getInstance(this);
        adapter.submitAll(myDatabase.todoDao().getAllTodo());
    }
}