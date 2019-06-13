package com.github.arisanform.model;

import com.github.arisan.annotation.Form;
import com.github.arisan.helper.ObjectReader;
import com.github.arisan.model.ArisanFieldModel;

public class Radio {
    @Form(type = Form.RADIO)
    String radio;

    public static ArisanFieldModel getField(){
        return ObjectReader.getField(new Radio()).get(0);
    }

    public Radio() {
    }

    public String getRadio() {
        return radio;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }
}
