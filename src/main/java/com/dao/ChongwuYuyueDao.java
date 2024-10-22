package com.dao;

import com.entity.ChongwuYuyueEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.view.ChongwuYuyueView;

/**
 * 宠物领养申请 Dao 接口
 *
 * @author 
 */
public interface ChongwuYuyueDao extends BaseMapper<ChongwuYuyueEntity> {

   List<ChongwuYuyueView> selectListView(Pagination page,@Param("params")Map<String,Object> params);

}
