package ru.skillbox.diplom.group42.social.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TestingConstant {
    public static final Long TEST_ACCOUNT_ID = 8888L;
    public static final Long ONLINE_ACCOUNT_ID = 77777L;
    public static final Long TEST_ID = 1098L;
    public static final String TEST_AUTHOR = "TEST_AUTHOR";
    public static final String TEST_EMAIL = "test@mail.ru";
    public static final String TEST_PASSWORD = "password";
    public static final String TEST_R_EMAIL = "r_testr@mail.ru";
    public static final String TEST_R_PASSWORD = "r_password";
    public static final String TEST_FIRST_NAME = "t_first_name";
    public static final String TEST_LAST_NAME = "t_last_name";
    public static final String TEST_CAPTCHA = "t_captcha";
    public static final String TEST_R_FIRST_NAME = "r_first_name";
    public static final String TEST_R_LAST_NAME = "r_last_name";
    public static final String TEST_R_CAPTCHA = "r_captcha";

    public static final ZonedDateTime TIME_TEST = ZonedDateTime.of(
            2020, 12, 3, 12, 20, 59,
            90000, ZoneId.systemDefault());
}
