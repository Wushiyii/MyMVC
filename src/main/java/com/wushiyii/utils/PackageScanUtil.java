package com.wushiyii.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class PackageScanUtil {

    public static List<String> getClassName(String packageName) {
        List<String> classNames = new ArrayList<String>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            String resourceName = packageName.replaceAll("\\.", "/");
            URL url = loader.getResource(resourceName);
            File urlFile = new File(Objects.requireNonNull(url).toURI());
            File[] files = urlFile.listFiles();
            for (File f : Objects.requireNonNull(files))
                getClassName(packageName, f, classNames);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return classNames;
    }

    private static void getClassName(String packageName, File packageFile, List<String> list) {
        if (packageFile.isFile()) {
            list.add(packageName + "." + packageFile.getName().replace(".class", ""));
        } else {
            File[] files = packageFile.listFiles();
            String tmPackageName = packageName + "." + packageFile.getName();
            for (File f : Objects.requireNonNull(files)) {
                getClassName(tmPackageName, f, list);
            }
        }
    }

}
