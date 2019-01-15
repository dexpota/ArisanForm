package com.github.arisanform.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.github.arisan.ArisanPreparation;
import com.github.arisan.helper.PreferenceHelper;
import com.github.arisanform.R;
import com.github.arisanform.model.DataMaster;
import com.github.arisanform.model.Todo;

import java.util.List;

/**
 * Created by wijaya on 5/5/2018.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    List<Todo> todoList;
    Context context;
    PreferenceHelper preference;

    public TodoAdapter(Context context, List<Todo> todos) {
        this.context = context;
        this.todoList = todos;
        preference = new PreferenceHelper(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_todo, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Todo todo = todoList.get(position);
        holder.title.setText(todo.getTitle());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArisanPreparation arisanPreparation = new ArisanPreparation(context);
                arisanPreparation.setModel(todo);
                arisanPreparation.fillData("type",DataMaster.DUMMY_STRING_ARRAY);
                context.startActivity(new Intent(context,FormActivity.class));
            }
        });
        holder.note.setText(todo.getNote());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    todoList.remove(position);
                    preference.saveObj("todo",todoList);
                    ((Activity)context).finish();
                    context.startActivity(((Activity) context).getIntent());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (todoList != null)
            return todoList.size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView title, note;
        CheckBox checkBox;

        public ViewHolder(View v) {
            super(v);
            System.out.println("ViewHolder");
            title = v.findViewById(R.id.todo_title);
            note = v.findViewById(R.id.todo_description);
            checkBox = v.findViewById(R.id.todo_checkbox);
        }
    }
}