package com.entity.model;

import com.entity.ChongwuyongpinOrderEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;


/**
 * 宠物用品订单
 * 接收传参的实体类
 *（实际开发中配合移动端接口开发手动去掉些没用的字段， 后端一般用entity就够用了）
 * 取自ModelAndView 的model名称
 */
public class ChongwuyongpinOrderModel implements Serializable {
    private static final long serialVersionUID = 1L;




    /**
     * 主键
     */
    private Integer id;


    /**
     * 订单号
     */
    private String chongwuyongpinOrderUuidNumber;


    /**
     * 收货地址
     */
    private Integer addressId;


    /**
     * 宠物用品
     */
    private Integer chongwuyongpinId;


    /**
     * 用户
     */
    private Integer yonghuId;


    /**
     * 购买数量
     */
    private Integer buyNumber;


    /**
     * 实付价格
     */
    private Double chongwuyongpinOrderTruePrice;


    /**
     * 订单类型
     */
    private Integer chongwuyongpinOrderTypes;


    /**
     * 支付类型
     */
    private Integer chongwuyongpinOrderPaymentTypes;


    /**
     * 订单创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    private Date insertTime;


    /**
     * 创建时间 show3
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    private Date createTime;


    /**
	 * 获取：主键
	 */
    public Integer getId() {
        return id;
    }


    /**
	 * 设置：主键
	 */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
	 * 获取：订单号
	 */
    public String getChongwuyongpinOrderUuidNumber() {
        return chongwuyongpinOrderUuidNumber;
    }


    /**
	 * 设置：订单号
	 */
    public void setChongwuyongpinOrderUuidNumber(String chongwuyongpinOrderUuidNumber) {
        this.chongwuyongpinOrderUuidNumber = chongwuyongpinOrderUuidNumber;
    }
    /**
	 * 获取：收货地址
	 */
    public Integer getAddressId() {
        return addressId;
    }


    /**
	 * 设置：收货地址
	 */
    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }
    /**
	 * 获取：宠物用品
	 */
    public Integer getChongwuyongpinId() {
        return chongwuyongpinId;
    }


    /**
	 * 设置：宠物用品
	 */
    public void setChongwuyongpinId(Integer chongwuyongpinId) {
        this.chongwuyongpinId = chongwuyongpinId;
    }
    /**
	 * 获取：用户
	 */
    public Integer getYonghuId() {
        return yonghuId;
    }


    /**
	 * 设置：用户
	 */
    public void setYonghuId(Integer yonghuId) {
        this.yonghuId = yonghuId;
    }
    /**
	 * 获取：购买数量
	 */
    public Integer getBuyNumber() {
        return buyNumber;
    }


    /**
	 * 设置：购买数量
	 */
    public void setBuyNumber(Integer buyNumber) {
        this.buyNumber = buyNumber;
    }
    /**
	 * 获取：实付价格
	 */
    public Double getChongwuyongpinOrderTruePrice() {
        return chongwuyongpinOrderTruePrice;
    }


    /**
	 * 设置：实付价格
	 */
    public void setChongwuyongpinOrderTruePrice(Double chongwuyongpinOrderTruePrice) {
        this.chongwuyongpinOrderTruePrice = chongwuyongpinOrderTruePrice;
    }
    /**
	 * 获取：订单类型
	 */
    public Integer getChongwuyongpinOrderTypes() {
        return chongwuyongpinOrderTypes;
    }


    /**
	 * 设置：订单类型
	 */
    public void setChongwuyongpinOrderTypes(Integer chongwuyongpinOrderTypes) {
        this.chongwuyongpinOrderTypes = chongwuyongpinOrderTypes;
    }
    /**
	 * 获取：支付类型
	 */
    public Integer getChongwuyongpinOrderPaymentTypes() {
        return chongwuyongpinOrderPaymentTypes;
    }


    /**
	 * 设置：支付类型
	 */
    public void setChongwuyongpinOrderPaymentTypes(Integer chongwuyongpinOrderPaymentTypes) {
        this.chongwuyongpinOrderPaymentTypes = chongwuyongpinOrderPaymentTypes;
    }
    /**
	 * 获取：订单创建时间
	 */
    public Date getInsertTime() {
        return insertTime;
    }


    /**
	 * 设置：订单创建时间
	 */
    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
    /**
	 * 获取：创建时间 show3
	 */
    public Date getCreateTime() {
        return createTime;
    }


    /**
	 * 设置：创建时间 show3
	 */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    }
