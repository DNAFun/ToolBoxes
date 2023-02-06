package com.xmingl.base.anno;

import java.lang.annotation.*;

/**
 * 自动管理的module<br/>
 * 主要包含以下文件的创建：
 * <li>用户文件</li>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoConfModule {
}
