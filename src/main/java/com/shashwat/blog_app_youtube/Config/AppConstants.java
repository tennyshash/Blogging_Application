package com.shashwat.blog_app_youtube.Config;

public interface AppConstants {
     String PAGE_NUMBER="0";
     String PAGE_SIZE="5";
     String SORT_BY="id";
     String SORT_DIR="asc";
     long JWT_TOKEN_VALIDITY =5 * 60 * 60; // in milli sec
     String SECRET ="jwtTokenKeydfsfghjkiasdfghjkjwtTokenKeydfsfghjkiasdfghjklkjhgfdsxcvbnmkiuytresxcnuytrd" +
             "lkjhgfdsxcvbnmkiuytresxcnuytrd";

     Integer ADMIN_USER=101;
     Integer NORMAL_USER=102;
     String [] PUBLIC_URL={
             "/api-docs",
             "/v2/api-docs",
             "/home/auth/**",
             "/swagger-resources/**",
             "/swagger-ui/**",
             "/webjars/**"
     };



}
