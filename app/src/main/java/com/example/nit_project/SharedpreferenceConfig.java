package com.example.nit_project;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedpreferenceConfig {
    SharedPreferences sharedPreferences;
    Context context;
    SharedpreferenceConfig(Context context)
    {
        this.context=context;
        sharedPreferences=context.getSharedPreferences("com.example.nit_project.loginpreferneces",Context.MODE_PRIVATE);
    }
    public void writeloginstatus(boolean status)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("com.example.nit_project.loginstatus",status);
        editor.commit();
    }
    public void writeusername(String name)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("com.example.nit_project.loginstatusname",name);
        editor.commit();
    }
    public boolean readloginstatus()
    {
        boolean status=sharedPreferences.getBoolean("com.example.nit_project.loginstatus",false);
        return status;
    }
    public String readusername()
    {
        String  name=sharedPreferences.getString("com.example.nit_project.loginstatusname",null);
        return name;
    }
}
