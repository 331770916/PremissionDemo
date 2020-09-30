package com.premissionabstractprocessorlib;

import java.util.HashMap;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class MethodInfo {

    public String packageName;
    private String className;

    private static final String PROXY_NAME = "PermissionProxy";
    public String fileName;

    public HashMap<Integer, String> grantMethodMap = new HashMap<>();

    public HashMap<Integer, String> deniedMethodMap = new HashMap<>();

    public HashMap<Integer, String> rationalMethodMap = new HashMap<>();

    public MethodInfo(Elements elementUtils, TypeElement typeElement) {
        PackageElement packageElement = elementUtils.getPackageOf(typeElement);
        packageName = packageElement.getQualifiedName().toString();
        className = ClassValidator.getClassName(typeElement, packageName);

        if (null != className && !"".equals(className) && className.contains("$")) {
            className = className.replace("$", "");
        }

        fileName = className + "$$" + PROXY_NAME;
    }

    public String generateJavaCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("// generate code. do not modify\n");
        sb.append("package ").append(packageName).append(";\n\n");
        sb.append("import com.premissionhelper.*;");
        sb.append("\n");

        sb.append("public class ").append(fileName).append(" implements " + PROXY_NAME + "<" + className + ">");
        sb.append(" {\n");

        genreateMethods(sb);
        sb.append("\n");

        sb.append(" }\n");

        return sb.toString();
    }

    private void genreateMethods(StringBuilder sb) {

        genreateGrantMethods(sb);
        genreateDeniedMethods(sb);
        genreateRationalMethods(sb);

    }

    private void genreateGrantMethods(StringBuilder sb) {

        sb.append("@Override\n");
        sb.append("public void grant (int requestCode, " + className + " source, String[] permissions) {\n");
        sb.append("switch(requestCode){\n");
        for (int requestCode : grantMethodMap.keySet()) {
            sb.append("case " + requestCode + ":\n");
            sb.append("source." + grantMethodMap.get(requestCode) + "(permissions);\n");
            sb.append("break;\n");
        }

        sb.append("}\n");
        sb.append("}\n");

    }

    private void genreateDeniedMethods(StringBuilder sb) {
        sb.append("@Override\n");
        sb.append("public void denied (int requestCode, " + className + " source, String[] permissions) {\n");
        sb.append("switch(requestCode){\n");
        for (int requestCode : deniedMethodMap.keySet()) {
            sb.append("case " + requestCode + ":\n");
            sb.append("source." + deniedMethodMap.get(requestCode) + "(permissions);\n");
            sb.append("break;\n");
        }

        sb.append("}\n");
        sb.append("}\n");

    }

    private void genreateRationalMethods(StringBuilder sb) {
        sb.append("@Override\n");
        sb.append("public boolean rational (int requestCode, " + className + " source, String[] permissions, PermissionRationalCallback callback) {\n");
        sb.append("switch(requestCode){\n");
        for (int requestCode : rationalMethodMap.keySet()) {
            sb.append("case " + requestCode + ":\n");
            sb.append("source." + rationalMethodMap.get(requestCode) + "(permissions, callback);\n");
            sb.append("return true;\n");
        }
        sb.append("}\n");
        sb.append("return false;\n");
        sb.append("}\n");
    }

}
