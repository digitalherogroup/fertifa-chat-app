package com.ithd.chat.chatapp.orders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter

public class Dates {

    @SerializedName("dates")
    @Expose
    public List<DateDate> dates = null;

}
