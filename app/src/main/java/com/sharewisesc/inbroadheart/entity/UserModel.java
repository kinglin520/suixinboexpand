package com.sharewisesc.inbroadheart.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wl on 2016/7/29.
 */
public class UserModel implements Parcelable {


    /**
     * user_id :
     * username :
     * sex : 0
     * birthday :
     * mail :
     * phone :
     * last_login_date :
     * telNo :
     * depName :
     * postName :
     * name :
     */

    private String user_id;
    private String username;
    private int sex;
    private String birthday;
    private String mail;
    private String phone;
    private String last_login_date;
    private String telNo;
    private String depName;
    private String postName;
    private String name;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLast_login_date() {
        return last_login_date;
    }

    public void setLast_login_date(String last_login_date) {
        this.last_login_date = last_login_date;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeString(this.username);
        dest.writeInt(this.sex);
        dest.writeString(this.birthday);
        dest.writeString(this.mail);
        dest.writeString(this.phone);
        dest.writeString(this.last_login_date);
        dest.writeString(this.telNo);
        dest.writeString(this.depName);
        dest.writeString(this.postName);
        dest.writeString(this.name);
    }

    public UserModel() {
    }

    protected UserModel(Parcel in) {
        this.user_id = in.readString();
        this.username = in.readString();
        this.sex = in.readInt();
        this.birthday = in.readString();
        this.mail = in.readString();
        this.phone = in.readString();
        this.last_login_date = in.readString();
        this.telNo = in.readString();
        this.depName = in.readString();
        this.postName = in.readString();
        this.name = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
}
