package com.ithd.chat.chatapp.model.additional;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataArray {

    @SerializedName("count")
    @Expose
    public Integer count;
    @SerializedName("serviceName")
    @Expose
    public String serviceName;
    @SerializedName("price")
    @Expose
    public Float price;
    @SerializedName("serviceId")
    @Expose
    public Integer serviceId;
    @SerializedName("type")
    @Expose
    public Integer type;

    public class JsonData {

        @SerializedName("dataArray")
        @Expose
        public List<DataArray> dataArray = null;
        @SerializedName("taxAmount")
        @Expose
        public Integer taxAmount;
        @SerializedName("finalTotal")
        @Expose
        public String finalTotal;
        @SerializedName("finalMainTotal")
        @Expose
        public String finalMainTotal;

    }
}
