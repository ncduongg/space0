package com.space.models.ModelsMapping;

import java.util.ArrayList;
import java.util.List;

import com.space.models.Donate;

import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public class DonateMap {
    public static List<Donate> DBDonateMap(RowSet<Row> rowSet) {
        List<Donate> lDonate = new ArrayList<>();
        rowSet.forEach(row -> {
            Donate donate = new Donate();
            donate.setDoId(row.getString("do_id"));
            donate.setUserId(row.getString("user_id"));
            donate.setDoAmount(row.getDouble("do_amount"));
            donate.setDoData(row.getString("do_data"));
            donate.setDoState(row.getString("do_state"));
            lDonate.add(donate);
        });
        return lDonate;
    }
}
