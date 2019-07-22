package com.example.nit_project.activities;



import com.example.nit_project.ClickableActivity.Temporary;

public class ImageInfo {

    private String Title;
    private String Category ;
    private String Description ;
    private String filepath ;
    private boolean isImage;
    private Temporary temporary=new Temporary();
    public ImageInfo() {
    }

    public ImageInfo(String title, String category, String description, String filepath, Boolean isImage) {
        Title = title;
        Category = category;
        Description = description;
        this.filepath = filepath;
        this.isImage=isImage;
    }


    public String getTitle() {
        return Title;
    }

    public String getCategory() {
        return Category;
    }

    public String getDescription() {
        return Description;
    }

    public String getFilepath() {
        return filepath;
    }

    public Boolean getIsImage() {
        return isImage;
    }


    public Temporary getTemporary() {
        return temporary;
    }




    public void setTitle(String title) {
        Title = title;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setFilepath( String fiepath) {
        this.filepath = filepath;
    }

}
