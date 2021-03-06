package com.github.arisan.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by wijaya on 3/27/2018.
 */

public class ArisanAdapter {
    /*private static List<ArisanFieldModel> fieldList = new ArrayList<>();
    private Activity activity;
    private ArisanForm form;
    private OnSubmitListener onSubmitListener;
    private PreferenceHelper preference;
    private String blank_message = "cannot blank!!!";
    private boolean no_blank = false;
    private int buttonBackground;
    private int label_color;
    private boolean useTitle;
    private boolean useSubmit;
    private boolean isChild = false;
    private int index_child = -1;
    private String parent_field;
    private ProgressListener progressListener;

    public ArisanAdapter(Activity activity, ArisanForm form) {
        this.activity = activity;
        this.form = form;

        useTitle = form.isUse_title();
        useSubmit = form.isUse_submit();
        blank_message = form.getBlankMessage()==null ? blank_message:form.getBlankMessage();
        buttonBackground = form.getBackground();
        label_color = form.getLabelColor();

        Collections.sort(form.getFieldData(),SortField.getInstance());

        preference = new PreferenceHelper(activity);

        if(isUseTitle()){
            ArisanFieldModel model = new ArisanFieldModel();
            model.setName("Title");
            model.setLabel(form.getTitle());
            fieldList.add(model);//For Title
        }

        fieldList.addAll(form.getFieldData());

        if(isUseSubmit()){
            ArisanFieldModel model = new ArisanFieldModel();
            model.setName("Submit");
            model.setLabel(form.getSubmitText());
            fieldList.add(model);//For Title

            try{
                setSubmitBackground(form.getBackground());
            }catch (Exception ignore){}
        }

        System.out.println("Arisan : Form Adapter Constructor");
    }

    @Override
    public ArisanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("Arisan : OnCreateViewHolder Adapter");
        ViewHolder holder =new ViewHolder(MyInflater.inflate(parent,viewType,isChild,new FormConfig()));
        return holder;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MyView view;
        MyTextWatcher textWatcher;

        ViewHolder(View v) {
            super(v);
            view = new MyView(v);
            textWatcher = new MyTextWatcher(this);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0&&useTitle) return 0;
        else if(position== fieldList.size()-1&&useSubmit) return Model.BUTTON;
        else return fieldList.get(position).getViewType();
    }

    @Override
    public void onBindViewHolder(ArisanAdapter.ViewHolder holder, int position) {
        ArisanFieldModel data = fieldList.get(position);
        int color = data.getColor();
        System.out.println("Arisan : onBind Adapter");
        holder.textWatcher.updatePostition(position);
        System.out.printf("__TYPE : %s",data.getViewType());
        if(position==0&&useTitle){
            ViewTitle(holder, data, color);
        }else if(position== fieldList.size()-1&&useSubmit){
            if(isChild)
                ViewDelete(holder,data,color);
            else
                ViewSubmit(holder, data, color);
        }else {
                switch (data.getViewType()){
                    case Form.BOOLEAN:ViewBoolean(holder, data, color);break;
                    case Form.SLIDER:ViewSlider(holder, data);break;
                    case Form.RADIO:ViewRadio(holder, position, data);break;
                    case Form.DATE:ViewDate(holder, data, color);break;
                    case Form.TIME:ViewTime(holder, data, color);break;
                    case Form.DATETIME:ViewDateTime(holder, data, color);break;
                    case Form.SPINNER:ViewSpinner(holder, data);break;
                    case Form.FILE:ViewFile(holder, data, color);break;
                    case Form.CHECKBOX:ViewCheckbox(holder, position, data);break;
                    case Form.ONETOMANY:ViewOneToMany(holder, data);break;
                    case Form.SEARCH:ViewSearch(holder, data);break;
                    case Form.IMAGE: ViewImage(holder, data);break;
                    case Form.AUTOCOMPLETE:ViewAutocomplete(holder, data);break;
                    case Form.PASSWORD:ViewPassword(holder, data);break;
                    case Form.ONELINETEXT :ViewOneLineText(holder,data,color);break;
                    case Form.FLOWTEXT:ViewEditText2(holder,data,color);break;
                    default:ViewEditText(holder, data, color);
                }
        }
    }

    @Override
    public int getItemCount() {
        if(fieldList !=null) return fieldList.size();
        else if(useTitle && useSubmit) return fieldList.size()+2;
        else if(useSubmit) return fieldList.size()+1;
        else if(useTitle) return fieldList.size()+1;
        else return 0;
    }

    public void setOnSubmitListener(OnSubmitListener onSubmitListener){
        this.onSubmitListener = onSubmitListener;
    }

    public interface OnSubmitListener{
        void onSubmit(String response);
    }

    public void updateFile(String fieldName, Uri uri){
        UriUtils utils = new UriUtils(activity,uri);
        for(ArisanFieldModel a: fieldList) {
            if(a.getName()!=null)
                if(a.getName().equals(fieldName)){
                    a.setData(utils.getFilename());
                    a.setValue(utils.getRealPath());
                    this.notifyDataSetChanged();
                    break;
                }
        }
    }

    public void updateImage(ImagePickerUtils imagePickerUtils){
        Uri uri = imagePickerUtils.getUri();
        UriUtils uriTools = new UriUtils(activity,uri);
        String pick_type = preference.load("pick_image");

        if(pick_type.equals(String.valueOf(Form.IMAGE))) uriTools = new UriUtils(activity, uri);

        if(imagePickerUtils.getChild_position()!=-1){
            //UPDATE DATA
            for(ArisanFieldModel parent: fieldList) {
                if(parent.getName().equals(imagePickerUtils.getParent_name())) {
                    for(ArisanFieldModel a:parent.getChildFieldModel().get(imagePickerUtils.getChild_position())) {
                        if (a.getName() != null && a.getName().equals(imagePickerUtils.getFieldName())) {
                            a.setData(uriTools.getFilename_with_ex());
                            a.setThumbnail(imagePickerUtils.getBitmap());
                            a.setValue(uriTools.getRealPath());

                            notifyDataSetChanged();
                            Log.e("__Arisan", "Update image FOUND, index : " + imagePickerUtils.getChild_position());
                            break;
                        } else {
                            Log.e("__Arisan", "Update image NOT FOUND, index : " + imagePickerUtils.getChild_position());
                        }
                    }
                }
            }
        }else{
            //UPDATE DATA
            for(ArisanFieldModel a: fieldList) {
                if(a.getName()!=null)
                    if(a.getName().equals(imagePickerUtils.getFieldName())){
                        a.setData(uriTools.getFilename_with_ex());
                        a.setThumbnail(imagePickerUtils.getBitmap());
                        a.setValue(uriTools.getRealPath());

                        notifyDataSetChanged();
                        break;
                    }
            }
        }
    }

    public void addModel(ArisanFieldModel add_model){
        List<ArisanFieldModel> new_list = new ArrayList<>(getListField());
        new_list.add(add_model);
        Collections.sort(new_list,SortField.getInstance());

        if(useTitle){
            ArisanFieldModel title_model = fieldList.get(0);
            new_list.add(0,title_model);
        }

        if(useSubmit){
            ArisanFieldModel submit_model = fieldList.get(fieldList.size()-1);
            new_list.add(submit_model);
        }

        setFieldList(new_list);
        notifyDataSetChanged();
    }

    public void removeModel(String name){
        FieldUtils.removeField(name, fieldList);
        notifyDataSetChanged();
    }

    private void setFieldList(List<ArisanFieldModel> fieldList) {
        this.fieldList = fieldList;
    }

    public void setSubmitBackground(int submitBackground) {
        fieldList.get(fieldList.size()-1).setBackground(submitBackground);
    }

    private String checkBlankCondition(){
        StringBuilder blank = new StringBuilder();
        List<ArisanFieldModel> models = getListField();//Parent
        List<String> arr_string = FieldUtils.countBlank(models);
        no_blank = arr_string.size() == 0;
        blank.append(TextUtils.join(arr_string));
        blank.append(" ").append(blank_message);
        return blank.toString();
    }

    *//*SETTER GETTER*//*

    public String getResult(){
        return FieldAssembler.toJson(fieldList);
    }

    public List<ArisanFieldModel> getData() {
        return fieldList;
    }

    public List<ArisanFieldModel> getListField() {
        List<ArisanFieldModel> models = new ArrayList<>();
        for(ArisanFieldModel model:fieldList) models.add(model.renew());

        if(useSubmit) models.remove(models.size()-1);
        if(useTitle) models.remove(0);

        return models;
    }

    *//*=================VIEW CONDITION=====================*//*

    private void ViewEditText(final ViewHolder holder,ArisanFieldModel data, int color) {
        holder.view.mEditTextLabel.setText(data.getLabel());
        Log.d("__TYPE", String.valueOf(data.getViewType()));
        holder.view.mEditText.addTextChangedListener(holder.textWatcher);
        switch (data.getViewType()) {
            case Form.NUMBER: {
//                Log.d("__TYPE","NUMBER");
                holder.view.mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                if (data.getValue() != null && data.getValue().toString().equals("0.0")) {
                    holder.view.mEditText.setText("0");
                    data.setValue(0);
                } else if (data.getValue() != null) {
                    holder.view.mEditText.setText(data.getValue().toString().replace(".0", ""));
                }
            }
            break;
            case Form.EMAIL: {
//                Log.d("__TYPE","EMAIL");
                holder.view.mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                if (data.getValue() != null) {
                    holder.view.mEditText.setText(data.getValue().toString());
                }
            }
            break;
            default: {
//                Log.d("__TYPE","NORMAL");
                //Input Type Text
                if (data.getValue() != null) {
                    Log.d("__DATA",new Gson().toJson(data.getValue()));
                    holder.view.mEditText.setText(data.getValue().toString());
                }

                if (data.getError_message() != null && data.getValue() != "") {
                    holder.view.mEditText.setError(data.getError_message());
                }
            }break;
        }

        if(label_color!=0) holder.view.mEditTextLabel.setTextColor(activity.getResources().getColor(label_color));
    }

    private void ViewEditText2(final ViewHolder holder, final ArisanFieldModel data, int color) {
        holder.view.mEditText2Layout.setHint(data.getLabel());
        Log.d("__TYPE", String.valueOf(data.getViewType()));
        //Input Type Text
        if (data.getValue() != null) {
            Log.d("__DATA",new Gson().toJson(data.getValue()));
            holder.view.mEditText2.setText(data.getValue().toString());
        }

        if (data.getError_message() != null && data.getValue() != "") {
            holder.view.mEditText2.setError(data.getError_message());
        }

        holder.view.mEditText2.addTextChangedListener(holder.textWatcher);

        if(label_color!=0) holder.view.mEditText2.setHintTextColor(activity.getResources().getColor(label_color));
    }

    private void ViewOneLineText(final ViewHolder holder, final ArisanFieldModel data, int color) {
        holder.view.mOneLineText.setHint(data.getLabel());
        //Log.d("__TYPE", String.valueOf(data.getViewType()));
        //Input Type Text
        if (data.getValue() != null) {
            Log.d("__DATA",new Gson().toJson(data.getValue()));
            holder.view.mOneLineText.setText(data.getValue().toString());
        }

        if (data.getError_message() != null && data.getValue() != "") {
            holder.view.mOneLineText.setError(data.getError_message());
        }

        holder.view.mOneLineText.addTextChangedListener(holder.textWatcher);

//        if(label_color!=0) holder.view.mOneLineText.setHintTextColor(activity.getResources().getColor(label_color));
    }

    private class MyTextWatcher implements TextWatcher{
        int position;
        ViewHolder vh;

        public MyTextWatcher(ViewHolder _vh){
            this.vh = _vh;
        }

        void updatePostition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.d("__beforeChange",fieldList.get(position).getName()+":"+s.toString());
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String value = s.toString();

            if(value.equals("")) {
                fieldList.get(position).setValue(null);
            } else {
                fieldList.get(position).setValue(value);
                Log.d("__OnTextChange",fieldList.get(position).getName()+":"+value);
            }

            try{
                fieldList.get(position).doListener(value,ArisanAdapter.this);
            }catch (Exception ignore){
                //Log.e("Arisan","Listener Not Found!!!");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d("__afterChange",fieldList.get(position).getName()+":"+s.toString());
        }
    }

    private void ViewPassword(final ViewHolder holder, final ArisanFieldModel data) {
        holder.view.mPasswordLabel.setText(data.getLabel());
        holder.view.mPassword.addTextChangedListener(holder.textWatcher);
        holder.view.mPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (holder.view.mPassword.getRight() - holder.view.mPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if(data.getData()!=null){
                            String bool = new Gson().toJson(data.getData());
                            if(bool.equals("true")){
                                //hide password
                                data.setData(false);
                                holder.view.mPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                holder.view.mPassword.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_eye,0);
                            }else{
                                //show password
                                data.setData(true);
                                holder.view.mPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                                        InputType.TYPE_TEXT_VARIATION_NORMAL);
                                holder.view.mPassword.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_eye_hide,0);
                            }
                        }else{
                            //show
                            data.setData(true);
                            holder.view.mPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                                    InputType.TYPE_TEXT_VARIATION_NORMAL);
                            holder.view.mPassword.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_eye_hide,0);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        data.setData(true);

        if(label_color!=0) holder.view.mPasswordLabel.setTextColor(activity.getResources().getColor(label_color));
    }

    private void ViewAutocomplete(ViewHolder holder, final ArisanFieldModel data) {
        holder.view.mAutocompleteLabel.setText(data.getLabel());
        final List<String> datas = FieldUtils.convertArrayToList(data.getData());
        AutocompleteAdapter adapter = new AutocompleteAdapter(activity, android.R.layout.select_dialog_item, datas);
        holder.view.mAutocomplete.setThreshold(1); //will start working from first character
        holder.view.mAutocomplete.setAdapter(adapter);
        holder.view.mAutocomplete.addTextChangedListener(holder.textWatcher);

        holder.view.mAutocomplete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                data.setValue(datas.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(data.getValue()!=null) holder.view.mAutocomplete.setText(data.getValue().toString());
        if(label_color!=0) holder.view.mAutocompleteLabel.setTextColor(activity.getResources().getColor(label_color));
    }

    private void ViewImage(ViewHolder holder, final ArisanFieldModel data) {
        holder.view.mImageLabel.setText(data.getLabel());
        holder.view.mImagePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDialog.build(activity, new ImageDialog.OnImageDialog() {
                    @Override
                    public void selected(boolean gallery) {
                        ImagePickerUtils utils = new ImagePickerUtils(activity,data);
                        if(isChild) {
                            utils.setChild(isChild);
                            utils.setChild_position(index_child);
                            utils.setParent_name(parent_field);
                        }

                        if(gallery) utils.pickFromGallery();
                        else utils.pickFromCamera();
                    }
                });
            }
        });

        if(data.getThumbnail()!=null) holder.view.mImage.setImageBitmap(data.getThumbnail());
        if(data.getData()!=null) holder.view.mImageName.setText(data.getData().toString());

        if(buttonBackground!=0)holder.view.mImagePick.setBackgroundResource(buttonBackground);
        if(label_color!=0) holder.view.mImageLabel.setTextColor(activity.getResources().getColor(label_color));
        if(form.getButtonColor()!=0) holder.view.mImagePick.setTextColor(activity.getResources().getColor(form.getButtonColor()));
    }

    private void ViewSearch(final ViewHolder holder, final ArisanFieldModel data) {
        holder.view.mSearchLabel.setText(data.getLabel());
        holder.view.mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ERROR CONDITION
                try{
                    String value = holder.view.mEditTextSearch.getText().toString();
                    ListenerModel listenerModel = data.doListener(value,ArisanAdapter.this);;
                    if(!listenerModel.isCondition()){
                        holder.view.mEditTextSearch.setError(listenerModel.message,activity.getResources().getDrawable(R.drawable.ic_clear));
                    }
                }catch (Exception ignored){ }
            }
        });
        try {
            holder.view.mEditTextSearch = (EditText) data.doViewMod(holder.view.mEditTextSearch);
        }catch (Exception ignored){ }

        //holder.view.mEditTextSearch.addTextChangedListener(holder.textWatcher);

        if(data.getValue()!=null) holder.view.mEditTextSearch.setText(data.getValue().toString());
        if(label_color!=0) holder.view.mSearchLabel.setTextColor(activity.getResources().getColor(label_color));
        if(buttonBackground!=0)holder.view.mSearchButton.setBackgroundResource(buttonBackground);
    }

    private void ViewOneToMany(ViewHolder holder, ArisanFieldModel data) {
        holder.view.mOnetoManyLabel.setText(data.getLabel());

        final ChildAdapter childAdapter = new ChildAdapter(data, activity, form);
        holder.view.mOnetoManyList.setLayoutManager(new LinearLayoutManager(activity));
        holder.view.mOnetoManyList.setAdapter(childAdapter);
        holder.view.mOnetoManyAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add blank child to adapter
                childAdapter.mList.add(ChildUtils.listValueRemover(childAdapter.mList));
                childAdapter.notifyDataSetChanged();
            }
        });

        if(buttonBackground!=0)holder.view.mOnetoManyAdd.setBackgroundResource(buttonBackground);
        if(label_color!=0) holder.view.mOnetoManyLabel.setTextColor(activity.getResources().getColor(label_color));
    }

    private void ViewCheckbox(final ViewHolder holder, final int position, final ArisanFieldModel data) {
        List<String> dataList = FieldUtils.convertArrayToList(data.getData());
        List<String> valueList = FieldUtils.convertArrayToList(data.getValue());
        holder.view.mCheckboxParent.setLayoutManager(new LinearLayoutManager(activity));
        final CheckboxAdapter adapter = new CheckboxAdapter(dataList, valueList);

        holder.view.mCheckboxText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> dataList = FieldUtils.convertArrayToList(data.getData());
                List<String> valueList = FieldUtils.convertArrayToList(data.getValue());

                //remove others value that is not availabel in data
                try{
                    List<String> no_from_data = FieldUtils.convertArrayToList(data.getValue());
                    no_from_data.removeAll(dataList);
                    String edittext_value = no_from_data.size() > 0 ? no_from_data.get(0) : null; //edit text value
                    valueList.remove(edittext_value);
                }catch(Exception ignore){}

                String new_value = holder.view.mCheckboxText.getText().toString();

                if(!new_value.equals(""))
                    valueList.add(new_value);

                valueList.remove(Model.OTHERS);
                data.setValue(valueList);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        adapter.setOnCheckedListener(new CheckboxAdapter.OnCheckedListener() {
            @Override
            public void onChecked(String value,List<String> checked) {
                data.setValue(checked);
                try{
                    preference.save("saved_position", String.valueOf(position));
                    data.doCheckboxListener(value,checked,ArisanAdapter.this);
                }catch (Exception ignore){}
                if(checked.contains(Model.OTHERS)){
                    holder.view.mCheckboxText.setVisibility(View.VISIBLE);
                }else{
                    holder.view.mCheckboxText.setVisibility(View.GONE);
                }
            }
        });
        holder.view.mCheckboxLabel.setText(data.getLabel());
        holder.view.mCheckboxParent.setAdapter(adapter);

        if(label_color!=0) holder.view.mCheckboxLabel.setTextColor(activity.getResources().getColor(label_color));
    }

    private void ViewFile(ViewHolder holder, ArisanFieldModel data, int color) {
        holder.view.mFileLabel.setText(data.getLabel());

        if(data.getData()!=null)
            holder.view.mFileName.setText((String)data.getData());
        holder.view.mFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                activity.startActivityForResult(intent, ArisanCode.REQUEST_FILE);
            }
        });

        if(buttonBackground!=0)holder.view.mFile.setBackgroundResource(buttonBackground);
        if(label_color!=0) holder.view.mFileLabel.setTextColor(activity.getResources().getColor(label_color));
    }

    private void ViewSpinner(ViewHolder holder, final ArisanFieldModel data) {
        holder.view.mSpinnerLabel.setText(data.getLabel());
        //ArrayAdapter mAdapter = new ArrayAdapter(activity,android.R.layout.simple_spinner_item,(List)data.getData());
        final List<String> dataArray = FieldUtils.convertArrayToList(data.getData());
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(activity,android.R.layout.simple_spinner_dropdown_item,dataArray);
        holder.view.mSpinner.setAdapter(mAdapter);
        holder.view.mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                data.setValue(dataArray.get(pos));
                try{
                    data.doListener(dataArray.get(pos),ArisanAdapter.this);
                }catch (Exception ignored){}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(data.getValue() != null){
            holder.view.mSpinner.setSelection(dataArray.indexOf(data.getValue()));
        }

        if(label_color!=0) holder.view.mSpinnerLabel.setTextColor(activity.getResources().getColor(label_color));
    }

    private void ViewDateTime(final ViewHolder holder, final ArisanFieldModel data, int color) {
        holder.view.mDateLabel.setText(data.getLabel());
        final Calendar calendar;
        if (data.getValue() != null) {
            calendar = new DateConverter(data.getValue().toString()).from("MMMM dd, yyyy").calendar;
            holder.view.mDate.setText(new DateConverter(calendar).to(data.getDateFormat()));
        } else {
            calendar = Calendar.getInstance();
            holder.view.mDate.setText(new DateConverter(calendar).to(data.getDateFormat()));
        }
        String result = "";
        holder.view.mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String result = NumberUtils.from(dayOfMonth) + "-" + NumberUtils.from(month + 1) + "-" + year;
                        data.setValue(new DateConverter(result).from("dd-MM-yyyy").to("dd-MM-yyyy"));
                        new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String result = data.getValue()+" "+ NumberUtils.from(hourOfDay)+":"+ NumberUtils.from(minute);
                                String convertedResult = new DateConverter(result).from("dd-MM-yyyy HH:mm").to(data.getDateFormat());
                                holder.view.mDate.setText(convertedResult);
                                data.setValue(convertedResult);
                            }
                        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(activity)).show();
                    }
                }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        if(buttonBackground!=0)holder.view.mDate.setBackgroundResource(buttonBackground);
        if(form.getButtonColor()!=0) holder.view.mDate.setTextColor(activity.getResources().getColor(form.getButtonColor()));
        if(label_color!=0) holder.view.mDateLabel.setTextColor(activity.getResources().getColor(label_color));
    }

    private void ViewTime(final ViewHolder holder, final ArisanFieldModel data, int color) {
        final Calendar calendar;
        if (data.getValue() != null) {
            calendar = new DateConverter(data.getValue().toString()).from("MMMM dd, yyyy HH:mm:ss").calendar;
            holder.view.mDate.setText(new DateConverter(calendar).to(data.getDateFormat()));
        } else {
            calendar = Calendar.getInstance();
            holder.view.mDate.setText("12:34");
        }
        holder.view.mDateLabel.setText(data.getLabel());
        holder.view.mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String result = NumberUtils.from(hourOfDay)+":"+ NumberUtils.from(minute);
                        String convertedResult = new DateConverter(result).from("HH:mm").to(data.getDateFormat());
                        holder.view.mDate.setText(convertedResult);
                        data.setValue(convertedResult);
                        try {
                            data.doListener(convertedResult, ArisanAdapter.this);
                        }catch (Exception ignore){
                            Log.e("ArisanForm","Listener not found");
                        }
                    }
                },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(activity)).show();
            }
        });

        if(buttonBackground!=0)holder.view.mDate.setBackgroundResource(buttonBackground);
        if(label_color!=0) holder.view.mDateLabel.setTextColor(activity.getResources().getColor(label_color));
    }

    private void ViewDate(final ViewHolder holder, final ArisanFieldModel data, int color) {
        holder.view.mDateLabel.setText(data.getLabel());
        final Calendar calendar;
        if (data.getValue() != null) {
            calendar = new DateConverter(data.getValue().toString()).from("MMMM dd, yyyy").calendar;
            holder.view.mDate.setText(new DateConverter(calendar).to(data.getDateFormat()));
        } else {
            calendar = Calendar.getInstance();
            holder.view.mDate.setText(new DateConverter(calendar).to(data.getDateFormat()));
        }
        holder.view.mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String result = NumberUtils.from(dayOfMonth) + "-" + NumberUtils.from(month + 1) + "-" + year;
                        holder.view.mDate.setText(new DateConverter(result).from("dd-MM-yyyy").to(data.getDateFormat()));
                        String value = new DateConverter(result).from("dd-MM-yyyy").to(data.getDateFormat());
                        data.setValue(value);
                        try{
                            data.doListener(value,ArisanAdapter.this);
                        }catch (Exception ignore){
                            Log.e("ArisanForm","Listener not found");
                        }
                    }
                }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        if(buttonBackground!=0)holder.view.mDate.setBackgroundResource(buttonBackground);
        if(label_color!=0) holder.view.mDateLabel.setTextColor(activity.getResources().getColor(label_color));
    }

    private void ViewRadio(final ViewHolder holder, final int position, final ArisanFieldModel data) {
        String value = (String) data.getValue();
        List<RadioModel> dataList = FieldUtils.convertDataToRadio(data.getData());
        holder.view.mRadioGroup.removeAllViews();
        for(RadioModel mData:dataList){
            RadioButton btn = new RadioButton(activity);
            btn.setId(mData.getId());
            Log.d("__ID",String.valueOf(mData.getId()));
            btn.setText(mData.getValue());
            Log.d("__VAL",mData.getValue());
            if(mData.getValue().equals(value)){
                btn.setChecked(true);
            }
            btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        String result = buttonView.getText().toString();
                        if (!result.equals(Model.OTHERS)) {
                            Log.d("Arisan","Show others form");
                            holder.view.mRadioText.setVisibility(View.GONE);
                        } else {
                            holder.view.mRadioText.setVisibility(View.VISIBLE);
                            data.setValue(result);
                            holder.view.mRadioText.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    String value = holder.view.mRadioText.getText().toString();
                                    data.setValue(value);
                                }

                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                                @Override
                                public void afterTextChanged(Editable s) { }
                            });
                        }
                        //CONDITION
                        try {
                            data.setValue(result);
                            preference.save("saved_position", String.valueOf(position));
                            data.doListener(result,ArisanAdapter.this);

                        } catch (Exception ignored){}
                    }
                }
            });
            holder.view.mRadioGroup.addView(btn);
        }
        holder.view.mRadioLabel.setText(data.getLabel());

        if(label_color!=0) holder.view.mRadioLabel.setTextColor(activity.getResources().getColor(label_color));
    }

    private void ViewSlider(final ViewHolder holder, final ArisanFieldModel data) {
        try {
            int value = NumberUtils.doubleToInt((Double) data.getValue());
            if (value != 0)
                holder.view.mSlide.setProgress(value);
        }catch(Exception ignore){ }

        holder.view.mSlideLabel.setText(data.getLabel());

        holder.view.mSlide.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                data.setValue(progress);
                holder.view.mSlideValue.setText(String.valueOf(progress));
                if(data.getArisanListener()!=null) data.doListener(String.valueOf(progress),ArisanAdapter.this);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        if(label_color!=0) holder.view.mSlideLabel.setTextColor(activity.getResources().getColor(label_color));
    }

    private void ViewBoolean(ViewHolder holder, final ArisanFieldModel data, int color) {
        holder.view.aSwitch.setText(data.getLabel());
        if (data.getValue() != null) {
            if (data.getValue().toString().equals("true")) {
                holder.view.aSwitch.setChecked(true);
                data.setValue(true);
            } else {
                holder.view.aSwitch.setChecked(false);
                data.setValue(false);
            }
        } else {
            holder.view.aSwitch.setChecked(false);
        }
        holder.view.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setValue(isChecked);
                try{
                    data.doListener(Boolean.toString(isChecked),ArisanAdapter.this);
                }catch (NullPointerException e){e.printStackTrace();}
            }
        });

        if(label_color!=0) holder.view.aSwitch.setTextColor(activity.getResources().getColor(label_color));
    }

    public void showSubmitProgress(boolean roll){
        if(isUseSubmit())
            fieldList.get(fieldList.size()-1).setData(roll);
        notifyDataSetChanged();
    }

    private void ViewSubmit(final ViewHolder holder, ArisanFieldModel data, int color) {
        boolean roll = data.getData() != null && (boolean) data.getData();

        if(roll) holder.view.mProgress.setVisibility(View.VISIBLE);
        else holder.view.mProgress.setVisibility(View.GONE);

        //SUBMIT BUTTON
        holder.view.mSubmitText.setText(data.getLabel());
        holder.view.mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = FieldAssembler.toJson(getListField());
                Log.d("__Result ",json);
                try {
                    checkBlankCondition();
                }catch (Exception ignored){}

                if(no_blank){
                    holder.view.mBlankText.setVisibility(View.GONE);
                    onSubmitListener.onSubmit(json);
                }else{
                    holder.view.mBlankText.setVisibility(View.VISIBLE);
                    holder.view.mBlankText.setText(checkBlankCondition());
                }
            }
        });

        if(buttonBackground!=0) holder.view.mSubmit.setBackgroundResource(buttonBackground);
        if(form.getButtonColor()!=0) holder.view.mSubmitText.setTextColor(activity.getResources().getColor(form.getButtonColor()));
    }

    private void ViewDelete(final ViewHolder holder, ArisanFieldModel data, int color) {
        //DELETE BUTTON
        holder.view.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitListener.onSubmit("json");
            }
        });
    }

    private void ViewTitle(ViewHolder holder, ArisanFieldModel data, int color) {
        //TITLE
        holder.view.mTitle.setText(data.getLabel());
        if(label_color!=0) holder.view.mTitle.setTextColor(activity.getResources().getColor(label_color));
    }

//    =============END OF VIEW===================

    public int getBackground() {
        return buttonBackground;
    }

    public void setBackground(int buttonBackground) {
        this.buttonBackground = buttonBackground;
    }

    public String getBlank_message() {
        return blank_message;
    }

    public void setBlank_message(String blank_message) {
        this.blank_message = blank_message;
    }

    public boolean isUseTitle() {
        return useTitle;
    }

    private void setUseTitle(boolean useTitle) {
        this.useTitle = useTitle;
    }

    public boolean isUseSubmit() {
        return useSubmit;
    }

    private void setUseSubmit(boolean useSubmit) {
        this.useSubmit = useSubmit;
    }

    public ArisanForm getForm() {
        return form;
    }

    public void setForm(ArisanForm form) {
        this.form = form;
    }

    public boolean isChild() {
        return isChild;
    }

    public void setChild(boolean child) {
        isChild = child;
    }

    public int getIndex_child() {
        return index_child;
    }

    public void setIndex_child(int index_child) {
        this.index_child = index_child;
    }

    public String getParent_field() {
        return parent_field;
    }

    public void setParent_field(String parent_field) {
        this.parent_field = parent_field;
    }

    public interface ProgressListener{
        public boolean showProgress();
    }

    public ProgressListener getProgressListener() {
        return progressListener;
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
        progressListener.showProgress();
    }*/
}
