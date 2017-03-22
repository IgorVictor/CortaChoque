package com.example.igor.cortachoqueapp;

import android.content.Context;
import android.widget.Switch;

/**
 * Created by Igor on 22/03/2017.
 */

public class OnOffSwitch extends Switch {

    String endereco;

    public OnOffSwitch(Context context, String endereco) {
        super(context);
        this.endereco = endereco;
    }

    public String getEndereco() {
        return this.endereco;
    }
}
