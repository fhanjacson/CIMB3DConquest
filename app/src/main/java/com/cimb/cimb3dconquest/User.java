package com.cimb.cimb3dconquest;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.CimbDO;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class User {
    private String username;
    private String password;
    private Boolean _2auth;

    public User(){}

    public User(String username, String password, Boolean _2auth) {
        this.username = username;
        this.password = password;
        this._2auth = _2auth;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void set_2auth(Boolean _2auth) {
        this._2auth = _2auth;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean get_2auth() {
        return _2auth;
    }
}
