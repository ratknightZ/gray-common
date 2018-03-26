package com.arms.common.security;

import org.apache.commons.lang.StringUtils;

public class XSSFilterUtils {

    private static final String IGNORE_CASE          = "(?i)";

    private static final String SCRIPT               = "script";

    private static final String IGNORE_CASE_SCRIPT   = IGNORE_CASE + SCRIPT;

    private static final String LEFT_ANGLE_BRACKETS  = "<";

    private static final String RIGHT_ANGLE_BRACKETS = ">";

    public static void main(String[] args) {
        String a = "<sCripT sdfafd></scriPT>";
        System.out.println(filter(a));
    }

    public static String filter(String content) {
        if (StringUtils.isBlank(content)) {
            return content;
        }
        while (content.length() != content.replaceAll(IGNORE_CASE_SCRIPT, "").length()) {
            content = content.replaceAll(IGNORE_CASE_SCRIPT, "");
            content = content.replaceAll(LEFT_ANGLE_BRACKETS, "");
            content = content.replaceAll(RIGHT_ANGLE_BRACKETS, "");
        }
        return content;
    }
}