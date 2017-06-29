package com.sharewisesc.inbroadheart.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhjj on 2016/7/29.
 */
public class UserRequest {
    @SerializedName("username")
    public String user_id;
    public String passwd;
    public String MEID;
}
