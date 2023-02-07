package com.space.models;

import java.util.Date;

public class Product {
    private String prodId;
    private String cateId;
    private String prodName;
    private String prodDes;
    private String prodImage;
    private String prodUrl;
    private String prodType;
    private String prodNote;
    private Date prodDate;
    private Date prodDateUpdate;
    private Integer prodQuantity;
    private String prodData;
    private String prodState;
    private String prodActiveAuction;
    private Double prodAmount;
    private Double prodAmountDis;
    public Product() {
    }
    public Product(String prodId, String cateId, String prodName, String prodDes, String prodImage, String prodUrl,
            String prodType, String prodNote, Date prodDate, Date prodDateUpdate, Integer prodQuantity, String prodData,
            String prodState, String prodActiveAuction, Double prodAmount, Double prodAmountDis) {
        this.prodId = prodId;
        this.cateId = cateId;
        this.prodName = prodName;
        this.prodDes = prodDes;
        this.prodImage = prodImage;
        this.prodUrl = prodUrl;
        this.prodType = prodType;
        this.prodNote = prodNote;
        this.prodDate = prodDate;
        this.prodDateUpdate = prodDateUpdate;
        this.prodQuantity = prodQuantity;
        this.prodData = prodData;
        this.prodState = prodState;
        this.prodActiveAuction = prodActiveAuction;
        this.prodAmount = prodAmount;
        this.prodAmountDis = prodAmountDis;
    }
    public String getProdId() {
        return prodId;
    }
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }
    public String getCateId() {
        return cateId;
    }
    public void setCateId(String cateId) {
        this.cateId = cateId;
    }
    public String getProdName() {
        return prodName;
    }
    public void setProdName(String prodName) {
        this.prodName = prodName;
    }
    public String getProdDes() {
        return prodDes;
    }
    public void setProdDes(String prodDes) {
        this.prodDes = prodDes;
    }
    public String getProdImage() {
        return prodImage;
    }
    public void setProdImage(String prodImage) {
        this.prodImage = prodImage;
    }
    public String getProdUrl() {
        return prodUrl;
    }
    public void setProdUrl(String prodUrl) {
        this.prodUrl = prodUrl;
    }
    public String getProdType() {
        return prodType;
    }
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }
    public String getProdNote() {
        return prodNote;
    }
    public void setProdNote(String prodNote) {
        this.prodNote = prodNote;
    }
    public Date getProdDate() {
        return prodDate;
    }
    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }
    public Date getProdDateUpdate() {
        return prodDateUpdate;
    }
    public void setProdDateUpdate(Date prodDateUpdate) {
        this.prodDateUpdate = prodDateUpdate;
    }
    public Integer getProdQuantity() {
        return prodQuantity;
    }
    public void setProdQuantity(Integer prodQuantity) {
        this.prodQuantity = prodQuantity;
    }
    public String getProdData() {
        return prodData;
    }
    public void setProdData(String prodData) {
        this.prodData = prodData;
    }
    public String getProdState() {
        return prodState;
    }
    public void setProdState(String prodState) {
        this.prodState = prodState;
    }
    public String getProdActiveAuction() {
        return prodActiveAuction;
    }
    public void setProdActiveAuction(String prodActiveAuction) {
        this.prodActiveAuction = prodActiveAuction;
    }
    public Double getProdAmount() {
        return prodAmount;
    }
    public void setProdAmount(Double prodAmount) {
        this.prodAmount = prodAmount;
    }
    public Double getProdAmountDis() {
        return prodAmountDis;
    }
    public void setProdAmountDis(Double prodAmountDis) {
        this.prodAmountDis = prodAmountDis;
    }
    
}
