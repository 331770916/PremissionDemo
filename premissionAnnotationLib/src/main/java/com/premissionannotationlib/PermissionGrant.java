package com.premissionannotationlib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 权限通过注解
 * 作用 用于将权限结果返回到自定义方法中，而不是返回系统中的onRequestPremissionResult()方法
 */
@Target(ElementType.METHOD)
public @interface PermissionGrant {
    //拿到申请权限的requestCode
    int value();
}
