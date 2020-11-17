package com.ithd.chat.chatapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductCreatedResponse {
    @SerializedName("active")
    @Expose
    public Boolean active;
    @SerializedName("attributes")
    @Expose
    public List<Object> attributes = null;
    @SerializedName("created")
    @Expose
    public Double created;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("images")
    @Expose
    public List<Object> images = null;
    @SerializedName("livemode")
    @Expose
    public Boolean livemode;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("object")
    @Expose
    public String object;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("updated")
    @Expose
    public Double updated;
}
