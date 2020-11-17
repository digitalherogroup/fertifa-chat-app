package com.ithd.chat.chatapp.time;

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
public class ServiceDateTime {
    private int id;
    private int user_id;
    private int date_id;
    private int service_id;
    private String appointment;
    private String clinic;
    private String address;
    private int order_id;
    private String time;
    private String date;
    private String date_time;
    private int Status;
    private String serviceName;
    private float getServicePrice;
    private String firstName;
    private String lastName;
    private String companyName;

}