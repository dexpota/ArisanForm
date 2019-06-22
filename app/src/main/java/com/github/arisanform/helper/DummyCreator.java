package com.github.arisanform.helper;

import com.github.arisan.annotation.Form;
import com.github.arisan.helper.FieldUtils;
import com.github.arisan.helper.KotlinTextUtils;
import com.github.arisan.helper.RadioUtils;
import com.github.arisan.model.ArisanFieldModel;

import java.lang.reflect.Field;
import java.util.List;

public class DummyCreator {
    private static String[] DUMMY_ARRAY = {"Banana","Watermelon","Mango","Orange","Apple"};

    public static List<ArisanFieldModel> fillDummyArray(List<ArisanFieldModel> models){
        for(ArisanFieldModel model:models){
            String view_type = model.getViewType();
            if(view_type.equals(Form.RADIO))
                model.setData(RadioUtils.convertToRadio(DUMMY_ARRAY));
            else if(view_type.equals(Form.CHECKBOX))
                model.setData(DUMMY_ARRAY);
            
        }
        return models;
    }

}
