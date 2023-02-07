package com.space.models.ModelsMapping;

import java.util.ArrayList;
import java.util.List;

import com.space.models.Product;
import com.space.util.Util;

import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public class ProductMap {
    public static List<Product> DBProductMap(RowSet<Row> rowSet) {
        List<Product> lProduct = new ArrayList<>();
        rowSet.forEach(row -> {
            Product product = new Product();
            product.setProdId(row.getString("prod_id"));
            product.setCateId(row.getString("cate_id"));
            product.setProdName(row.getString("prod_name"));
            product.setProdDes(row.getString("prod_des"));
            product.setProdImage(row.getString("prod_images_name")); // lay ten anh 
            product.setProdUrl(row.getString("prod_url"));
            product.setProdType(row.getString("prod_type"));
            product.setProdNote(row.getString("prod_notes"));
            product.setProdDate(Util.getDate(row.getLocalDateTime("prod_date")));
            product.setProdDateUpdate(Util.getDate(row.getLocalDateTime("prod_date_update")));
            product.setProdQuantity(row.getInteger("prod_quantity"));
            product.setProdData(row.getString("prod_data"));
            product.setProdState(row.getString("prod_state"));
            product.setProdActiveAuction(row.getString("prod_active_auction"));
            product.setProdAmount(row.getDouble("prod_amount"));
            product.setProdAmountDis(row.getDouble("prod_amount_dis"));
            lProduct.add(product);
        });
        return lProduct;
    }
}
