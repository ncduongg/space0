package com.space.models.ModelsMapping;

import java.util.ArrayList;
import java.util.List;

import com.space.models.Category;
import com.space.util.Util;

import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public class CategoryMap {

    public static List<Category> DBCategoryMap(RowSet<Row> rowSet) {
        List<Category> lCategory = new ArrayList<>();
        rowSet.forEach(row -> {
            Category category = new Category();
            category.setId(row.getString("cate_id"));
            category.setName(row.getString("cate_name"));
            category.setNote(row.getString("cate_note"));
            category.setDes(row.getString("cate_des"));
            category.setDate(Util.getDate(row.getLocalDate("cate_date")));
            category.setDateUpdate(Util.getDate(row.getLocalDate("cate_date_update")));
            category.setSub(row.getInteger("cate_sub").toString());
            lCategory.add(category);
        });
        return lCategory;
    }
}
