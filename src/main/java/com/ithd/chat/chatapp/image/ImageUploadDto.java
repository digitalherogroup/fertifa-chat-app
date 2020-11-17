package com.ithd.chat.chatapp.image;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageUploadDto {
    @SerializedName("fileName")
    @Expose
    public String fileName;
    @SerializedName("fileDownloadUri")
    @Expose
    public String fileDownloadUri;
    @SerializedName("fileType")
    @Expose
    public String fileType;
    @SerializedName("size")
    @Expose
    public Integer size;
    @SerializedName("code")
    @Expose
    public String code;

}
