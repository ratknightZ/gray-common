package com.arms.common.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    //private final static int maxAge = 60 * 60 * 60 * 24 * 365;  
    private final static String uri = "/";

    private CookieUtil() {
    }

    public static Cookie getCookie(String name, HttpServletRequest request) {
        return getCookie(name, request.getCookies());
    }

    public static Cookie getCookie(String name, Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }

    public static String getValue(String name, HttpServletRequest request) {
        return getValue(name, request.getCookies());
    }

    public static String getValue(String name, Cookie[] cookies) {
        Cookie cookie = getCookie(name, cookies);
        if (cookie == null) {
            return null;
        }
        return cookie.getValue();
    }

    public static boolean exist(String name, HttpServletRequest request) {
        return exist(name, request.getCookies());
    }

    public static boolean exist(String name, Cookie[] cookies) {
        return getCookie(name, cookies) == null ? false : true;
    }

    public static String getAttribute(String key, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void addCookie(String name, String value, int maxAge, HttpServletResponse response) {
        setAttribute(name, value, maxAge, response);
    }

    public static void addCookie(String name, String value, HttpServletResponse response) {
        response.addCookie(new Cookie(name, value));
    }

    public static void addCookie(Cookie cookie, HttpServletResponse response) {
        response.addCookie(cookie);
    }

    public static void setAttribute(String key, String value, int maxAge,
                                    HttpServletResponse response) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(uri);
        response.addCookie(cookie);
    }

    public static void removeAllAttribute(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                removeAttribute(cookie.getName(), response);
            }
        }
    }

    public static void removeAttribute(String key, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        cookie.setPath(uri);
        response.addCookie(cookie);
    }
}