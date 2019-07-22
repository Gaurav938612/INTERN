package com.example.nit_project.model;

public class Anime {

    private String user_id;
    private String first_name;
    private String middle_name;
    private String last_name;
    private String email;
    private String contact_number;
    private String batch_number;
    private String address;
    private String status;
    private String code;
    private String image_url;


    public Anime() {
    }

    public Anime(String user_id,String first_name, String middle_name, String last_name, String email, String contact_number,
                 String batch_number, String address,String status,String code,String image_url) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.email = email;
        this.contact_number = contact_number;
        this.address = address;
        this.status=status;
        this.code=code;
        this.batch_number=batch_number;
        this.image_url=image_url;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getFirst_name() { return first_name; }

    public String getMiddle_name() {
        return middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getContact_number() {
        return contact_number;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getBatch_number() {
        return batch_number;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public void setLast_name(String last_name) {
        this.last_name =last_name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBatch_number(String batch_number) {
        this.batch_number = batch_number;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContact_number(String contact_number) {
        this.contact_number= contact_number;
    }

    public void setCode(String code) {
        this.code= code;
    }

    public void setEmail(String email) {
        this.email= email;
    }

    public void setImage_url(String image_url) {
        this.image_url= image_url;
    }

}
