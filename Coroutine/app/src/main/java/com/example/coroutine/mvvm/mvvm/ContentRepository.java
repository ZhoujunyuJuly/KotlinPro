package com.example.coroutine.mvvm.mvvm;

public class ContentRepository {
    private String lastContent;

    public String requestResult(String content){
        lastContent = content;
        content = new StringBuilder(content).append("->请求到网络了").toString();
        return content;
    }

    public String getLastContent(){
        return lastContent;
    }
}
