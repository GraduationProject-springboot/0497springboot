package com.entity.model;

import com.entity.ChongwuYuyueEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;


/**
 * 宠物领养申请
 * 接收传参的实体类
 *（实际开发中配合移动端接口开发手动去掉些没用的字段， 后端一般用entity就够用了）
 * 取自ModelAndView 的model名称
 */
public class ChongwuYuyueModel implements Serializable {
    private static final long serialVersionUID = 1L;




    /**
     * 主键
     */
    private Integer id;


    /**
     * 宠物
     */
    private Integer chongwuId;


    /**
     * 用户
     */
    private Integer yonghuId;


    /**
     * 领养原因
     */
    private String chongwuYuyueText;


    /**
     * 领养状态
     */
    private Integer chongwuYuyueTypes;


    /**
     * 领养审核
     */
    private Integer chongwuYuyueYesnoTypes;


    /**
     * 审核结果
     */
    private String chongwuYuyueYesnoText;


    /**
     * 申请时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    private Date insertTime;


    /**
     * 创建时间 show3 listShow
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
	 * 获取：宠物
	 */
    public Integer getChongwuId() {
        return chongwuId;
    }


    /**
	 * 设置：宠物
	 */
    public void setChongwuId(Integer chongwuId) {
        this.chongwuId = chongwuId;
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
	 * 获取：领养原因
	 */
    public String getChongwuYuyueText() {
        return chongwuYuyueText;
    }


    /**
	 * 设置：领养原因
	 */
    public void setChongwuYuyueText(String chongwuYuyueText) {
        this.chongwuYuyueText = chongwuYuyueText;
    }
    /**
	 * 获取：领养状态
	 */
    public Integer getChongwuYuyueTypes() {
        return chongwuYuyueTypes;
    }


    /**
	 * 设置：领养状态
	 */
    public void setChongwuYuyueTypes(Integer chongwuYuyueTypes) {
        this.chongwuYuyueTypes = chongwuYuyueTypes;
    }
    /**
	 * 获取：领养审核
	 */
    public Integer getChongwuYuyueYesnoTypes() {
        return chongwuYuyueYesnoTypes;
    }


    /**
	 * 设置：领养审核
	 */
    public void setChongwuYuyueYesnoTypes(Integer chongwuYuyueYesnoTypes) {
        this.chongwuYuyueYesnoTypes = chongwuYuyueYesnoTypes;
    }
    /**
	 * 获取：审核结果
	 */
    public String getChongwuYuyueYesnoText() {
        return chongwuYuyueYesnoText;
    }


    /**
	 * 设置：审核结果
	 */
    public void setChongwuYuyueYesnoText(String chongwuYuyueYesnoText) {
        this.chongwuYuyueYesnoText = chongwuYuyueYesnoText;
    }
    /**
	 * 获取：申请时间
	 */
    public Date getInsertTime() {
        return insertTime;
    }


    /**
	 * 设置：申请时间
	 */
    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
    /**
	 * 获取：创建时间 show3 listShow
	 */
    public Date getCreateTime() {
        return createTime;
    }


    /**
	 * 设置：创建时间 show3 listShow
	 */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    }
