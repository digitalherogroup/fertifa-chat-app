package com.ithd.chat.chatapp.orders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateDate {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("value")
    @Expose
    public String value;

}