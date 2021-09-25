package com.secure_bank.bank.Util;

import android.graphics.LinearGradient;
import android.graphics.Shader;

public class ColorHelper {
    public static LinearGradient getLinearGradient(float x0, float y0, float width, float height, int start, int end, Shader.TileMode shader) {
        double angleInRadians = Math.toRadians(0);
        float newWidth = (float) (Math.cos(angleInRadians) * width);
        float newHeight = (float) (Math.sin(angleInRadians) * height);
        return new LinearGradient(x0, y0, newWidth, newHeight, start, end, shader);
    }
}
