package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "cimbdconquest-mobilehub-746289529-cimb")

public class CimbDO {
    private Double _userId;
    private byte[] _2authStatus;
    private String _patternPin;
    private String _userPassword;
    private String _userUsername;


    public CimbDO(Double _userId, byte[] _2authStatus, String _patternPin, String _userPassword, String _userUsername) {
        this._userId = _userId;
        this._2authStatus = _2authStatus;
        this._patternPin = _patternPin;
        this._userPassword = _userPassword;
        this._userUsername = _userUsername;
    }

    @DynamoDBHashKey(attributeName = "user_id")
    @DynamoDBAttribute(attributeName = "user_id")
    public Double getUserId() {
        return _userId;
    }

    public void setUserId(final Double _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "2auth_status")
    public byte[] get2authStatus() {
        return _2authStatus;
    }

    public void set2authStatus(final byte[] _2authStatus) {
        this._2authStatus = _2authStatus;
    }
    @DynamoDBAttribute(attributeName = "pattern_pin")
    public String getPatternPin() {
        return _patternPin;
    }

    public void setPatternPin(final String _patternPin) {
        this._patternPin = _patternPin;
    }
    @DynamoDBAttribute(attributeName = "user_password")
    public String getUserPassword() {
        return _userPassword;
    }

    public void setUserPassword(final String _userPassword) {
        this._userPassword = _userPassword;
    }
    @DynamoDBAttribute(attributeName = "user_username")
    public String getUserUsername() {
        return _userUsername;
    }

    public void setUserUsername(final String _userUsername) {
        this._userUsername = _userUsername;
    }

}
