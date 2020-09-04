package com.example.todo1.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo1.R;
import com.example.todo1.addEdit.AddEditTodoActivity;
import com.example.todo1.data.MyDatabase;
import com.example.todo1.data.TodoItem;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainTodoAdapter extends RecyclerView.Adapter<MainTodoViewHolder> {
    private List<TodoItem> itemList = new ArrayList<>();



    public void submitAll(List<TodoItem> list){
        itemList.clear();
        itemList.addAll(list);
        Collections.sort(itemList);
        notifyDataSetChanged();
    }

    public void removeAllITem(){
        itemList.clear();
        notifyDataSetChanged();
    }
    public void removeItem(int position ){
        itemList.remove(position);
        notifyDataSetChanged();
    }
    public void removeAllItem(List<TodoItem> newItemList){
        itemList.clear();
        itemList = newItemList;
        notifyDataSetChanged();
    }



    public boolean checkItem(TodoItem item){
        return item.getChecked();
    }



    @NonNull
    @Override
    public MainTodoViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final MainTodoViewHolder viewHolder =
                new MainTodoViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_todo, parent, false));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TodoItem temp = itemList.get(viewHolder.getAdapterPosition());
                temp.setChecked(!temp.getChecked());
                MyDatabase myDatabase = MyDatabase.getInstance(parent.getContext());
                myDatabase.todoDao().updateTodo(temp);
                notifyItemChanged(viewHolder.getAdapterPosition());
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                final TodoItem temp = itemList.get(viewHolder.getAdapterPosition());
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(temp.getTitle());
                final String[] items = {"수정", "삭제","취소"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (items[position]){
                            case "수정":
                                Intent intent = new Intent(parent.getContext(), AddEditTodoActivity.class);
                                intent.putExtra("mode",1);
                                intent.putExtra("todo_id",temp.getId());
                                parent.getContext().startActivity(intent);
                                break;
                            case "삭제":
                                itemList.remove(temp);
                                MyDatabase myDatabase = MyDatabase.getInstance(parent.getContext());
                                myDatabase.todoDao().deleteTodo(temp);
                                notifyItemRemoved(viewHolder.getAdapterPosition());
                                break;
                            case "취소":
                                break;
                        }

                    }
                });
                builder.show();

                return false;
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainTodoViewHolder holder, int position) {
        try {
            holder.onBind(itemList.get(position));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
