package com.liepin.auth.util;

import org.apache.commons.codec.cli.Digest;
import org.springframework.util.DigestUtils;

public class Crypto {

    private static final String salt = "114514";

    public static String md5(String password){
        return DigestUtils.md5DigestAsHex((password + salt).getBytes());
    }
}
