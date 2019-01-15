package com.github.arisan.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.github.arisan.R;
import com.github.arisan.annotation.Form;
import com.github.arisan.helper.DateConverter;
import com.github.arisan.helper.FieldAssembler;
import com.github.arisan.helper.TwoDigit;
import com.github.arisan.model.ArisanFieldModel;
import com.github.arisan.model.TypeForm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by wijaya on 3/27/2018.
 */

public class ArisanAdapter extends RecyclerView.Adapter<ArisanAdapter.ViewHolder> implements View.OnClickListener{
    List<ArisanFieldModel> mList = new ArrayList<>();
    Context mContext;
    Object mResult;
    OnSubmitListener onSubmitListener;
    String title;
    String submitText;

    public void setSubmitText(String submitText) {
        this.submitText = submitText;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArisanAdapter(Context context, List<ArisanFieldModel> fieldList) {
        this.mContext = context;
        mList.add(new ArisanFieldModel());//For Title
        mList.addAll(fieldList);
        mList.add(new ArisanFieldModel());//For Submit Button
        System.out.println("Form Adapter Constructor");
    }

    @Override
    public ArisanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("On Create"+viewType);
        TypeForm type = new TypeForm();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        if(viewType==0){
            v = inflater.inflate(com.github.arisan.R.layout.item_text, parent, false);
        }else if(viewType==mList.size()){
            v = inflater.inflate(com.github.arisan.R.layout.item_button, parent, false);
        }else if(viewType==type.get(Form.BOOLEAN)){
            v = inflater.inflate(com.github.arisan.R.layout.item_switch, parent, false);
        }else if(viewType==type.get(Form.DATE)){
            v = inflater.inflate(R.layout.item_date, parent, false);
        }else if(viewType== type.get(Form.SPINNER)){
            v = inflater.inflate(R.layout.item_spinner, parent, false);
        }else{
            v = inflater.inflate(com.github.arisan.R.layout.item_edittext, parent, false);
        }
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mInputTextLabel;
        TextView mDateLabel;
        TextView mSpinnerLabel;
        EditText mEditText;
        TextView mTitle;
        Button mSubmit;
        Switch aSwitch;
        Button mDate;
        Spinner mSpinner;

        public ViewHolder(View v) {
            super(v);
            System.out.println("ViewHolder");
            mInputTextLabel = v.findViewById(com.github.arisan.R.id.arisan_text_label);
            mEditText = v.findViewById(com.github.arisan.R.id.arisan_text);
            mDateLabel = v.findViewById(R.id.arisan_date_label);
            mDate = v.findViewById(R.id.arisan_date);
            mSpinnerLabel = v.findViewById(R.id.arisan_spinner_label);
            mSpinner = v.findViewById(R.id.arisan_spinner);
            mTitle = v.findViewById(com.github.arisan.R.id.arisan_title);
            mSubmit = v.findViewById(com.github.arisan.R.id.arisan_button);
            aSwitch = v.findViewById(com.github.arisan.R.id.arisan_switch);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public int getItemViewType(int position) {
        TypeForm type = new TypeForm();
        if(position==0){
            return 0;
        }else if(position==mList.size()-1){
            return mList.size();
        }else if(mList.get(position).getViewType().equals(Form.DATE)){
            return type.get(Form.DATE);
        }else if(mList.get(position).getViewType().equals(Form.PASSWORD)){
            return type.get(Form.PASSWORD);
        }else if(mList.get(position).getViewType().equals(Form.CHECKBOX)){
            return type.get(Form.CHECKBOX);
        }else if(mList.get(position).getViewType().equals(Form.NUMBER)){
            return type.get(Form.NUMBER);
        }else if(mList.get(position).getViewType().equals(Form.SPINNER)){
            return type.get(Form.SPINNER);
        }else if(mList.get(position).getViewType().equals(Form.TIME)){
            return type.get(Form.TIME);
        }else if(mList.get(position).getViewType().equals(Form.BOOLEAN)){
            return type.get(Form.BOOLEAN);
        }else{
            return type.get(Form.TEXT);
        }
    }

    @Override
    public void onBindViewHolder(final ArisanAdapter.ViewHolder holder, final int position) {

        final ArisanFieldModel data = mList.get(position);
//        System.out.println("On Bind");
        if(position==0){
            //TITLE
            holder.mTitle.setText(title);
        }else if(position==mList.size()-1){
            //SUBMIT BUTTON
            holder.mSubmit.setText(submitText);
            holder.mSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.remove(0);
                    mList.remove(mList.size()-1);
                    onSubmitListener.onSubmit(FieldAssembler.toJson(mList));
                }
            });
        }else if(data.getViewType().equals(Form.BOOLEAN)){
            holder.aSwitch.setText(data.getLabel());
            if(data.getValue()!= null) {
                if (data.getValue().toString().equals("true")) {
                    holder.aSwitch.setChecked(true);
                    mList.get(position).setValue(true);
                } else {
                    holder.aSwitch.setChecked(false);
                    mList.get(position).setValue(false);
                }
            }else{
                holder.aSwitch.setChecked(false);
            }
            holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mList.get(position).setValue(isChecked);
                }
            });
        }else if(data.getViewType().equals(Form.DATE)){
            holder.mDateLabel.setText(data.getLabel());
            final Calendar calendar;
            if(data.getValue()!=null){
                calendar = new DateConverter(data.getValue().toString()).from("MMMM dd, yyyy").calendar;
                holder.mDate.setText(new DateConverter(calendar).to(data.getDateFormat()));
            }else{
                calendar = Calendar.getInstance();
                holder.mDate.setText(new DateConverter(calendar).to(data.getDateFormat()));
            }
            holder.mDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String result = TwoDigit.from(dayOfMonth)+"-" + TwoDigit.from(month+1) + "-" + year ;
                        holder.mDate.setText(new DateConverter(result).from("dd-MM-yyyy").to(data.getDateFormat()));
                        mList.get(position).setValue(result);
                    }
                }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        }else if(data.getViewType().equals(Form.SPINNER)){
            holder.mSpinnerLabel.setText(data.getLabel());
            //ArrayAdapter mAdapter = new ArrayAdapter(mContext,android.R.layout.simple_spinner_item,(List)data.getData());
            final ArrayList<String> dataArray = (ArrayList<String>) data.getData();
            ArrayAdapter mAdapter = new ArrayAdapter<>(mContext,android.R.layout.simple_spinner_item,dataArray);
            holder.mSpinner.setAdapter(mAdapter);
            holder.mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    mList.get(position).setValue(dataArray.get(pos));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            if(data.getValue() != null){
                holder.mSpinner.setSelection(dataArray.indexOf(data.getValue()));
            }
        }else{
            //Edit Text
            holder.mInputTextLabel.setText(data.getLabel());
            if(data.getViewType().equals(Form.PASSWORD)){
                holder.mEditText.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }else if(data.getViewType().equals(Form.NUMBER)){
                holder.mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                if(data.getValue()!=null&&data.getValue()!=""){
                    holder.mEditText.setText(data.getValue().toString());
                }
            }else{
                //Input Type Text
                if(data.getValue()!=null&&data.getValue()!=""){
                    holder.mEditText.setText(data.getValue().toString());
                }
                if(data.getError_message()!=null&&data.getValue()!=""){
                    holder.mEditText.setError(data.getError_message());
                }
            }

            holder.mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mList.get(position).setValue(holder.mEditText.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(mList!=null)
            return mList.size();
        else
            return 0;
    }

    public ArisanAdapter setOnSubmitListener(OnSubmitListener onSubmitListener){
        this.onSubmitListener = onSubmitListener;
        return this;
    }

    public interface OnSubmitListener{
        void onSubmit(String response);
    }
}