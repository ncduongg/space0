package com.space.models.ModelsMapping;

import com.space.models.User;

import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public class UserMap {
    public static User DbUserMap(RowSet<Row> rowSet){
        User user = new User();
        rowSet.forEach(userItem ->{
            user.setUserId(userItem.getString("user_id"));
            user.setUserName(userItem.getString("username"));
            user.setName(userItem.getString("name"));
            user.setPhone(userItem.getString("phone"));
            user.setEmail(userItem.getString("email"));
            user.setAddress(userItem.getString("address"));
        });
        return user;
    }
}
