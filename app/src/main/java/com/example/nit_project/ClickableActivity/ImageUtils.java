package com.example.nit_project.ClickableActivity;


public class ImageUtils {
    public static PixelPosition getPixelPosition(float percentX, float percentY, int absW, int absH){
        int absX = Math.round(absW * percentX);
        int absY = Math.round(absH * percentY);
        return new PixelPosition(absX,absY);
    }
}
