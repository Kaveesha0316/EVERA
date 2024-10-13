/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "product")
public class Product implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "qty", nullable = false)
    private int qty;

    @Column(name = "date_time", nullable = false)
    private Date date_time;

    @ManyToOne
    @JoinColumn(name = "size_id")
    private Sizes sizes;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private brand brand;

    @Column(name = "shipping", nullable = false)
    private double shipping;

    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private subCategory subCategory;

    @ManyToOne
    @JoinColumn(name = "product_status_id")
    private productStatus product_status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Product() {
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the qty
     */
    public int getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(int qty) {
        this.qty = qty;
    }

    /**
     * @return the date_time
     */
    public Date getDate_time() {
        return date_time;
    }

    /**
     * @param date_time the date_time to set
     */
    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }

    /**
     * @return the sizes
     */
    public Sizes getSizes() {
        return sizes;
    }

    /**
     * @param sizes the sizes to set
     */
    public void setSizes(Sizes sizes) {
        this.sizes = sizes;
    }

    /**
     * @return the brand
     */
    public brand getBrand() {
        return brand;
    }

    /**
     * @param brand the brand to set
     */
    public void setBrand(brand brand) {
        this.brand = brand;
    }

    /**
     * @return the shipping
     */
    public double getShipping() {
        return shipping;
    }

    /**
     * @param shipping the shipping to set
     */
    public void setShipping(double shipping) {
        this.shipping = shipping;
    }

    /**
     * @return the subCategory
     */
    public subCategory getSubCategory() {
        return subCategory;
    }

    /**
     * @param subCategory the subCategory to set
     */
    public void setSubCategory(subCategory subCategory) {
        this.subCategory = subCategory;
    }

    /**
     * @return the product_status
     */
    public productStatus getProduct_status() {
        return product_status;
    }

    /**
     * @param product_status the product_status to set
     */
    public void setProduct_status(productStatus product_status) {
        this.product_status = product_status;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

}
