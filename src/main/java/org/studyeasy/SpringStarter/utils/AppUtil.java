package org.studyeasy.SpringStarter.utils;

import java.io.File;

public class AppUtil {
    public static String getUploadPath(String fileName) {
        String basePath = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "uploads";
        return new File(basePath, fileName).getAbsolutePath();
    }
}
