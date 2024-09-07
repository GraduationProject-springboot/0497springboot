
package com.controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSONObject;
import java.util.*;
import org.springframework.beans.BeanUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import com.service.TokenService;
import com.utils.*;
import java.lang.reflect.InvocationTargetException;

import com.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import com.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.*;
import com.entity.view.*;
import com.service.*;
import com.utils.PageUtils;
import com.utils.R;
import com.alibaba.fastjson.*;

/**
 * 宠物
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/chongwu")
public class ChongwuController {
    private static final Logger logger = LoggerFactory.getLogger(ChongwuController.class);

    private static final String TABLE_NAME = "chongwu";

    @Autowired
    private ChongwuService chongwuService;


    @Autowired
    private TokenService tokenService;

    @Autowired
    private AddressService addressService;//收货地址
    @Autowired
    private ChongwuYuyueService chongwuYuyueService;//宠物领养申请
    @Autowired
    private ChongwuyongpinService chongwuyongpinService;//宠物用品
    @Autowired
    private ChongwuyongpinCollectionService chongwuyongpinCollectionService;//宠物用品收藏
    @Autowired
    private ChongwuyongpinOrderService chongwuyongpinOrderService;//宠物用品订单
    @Autowired
    private DictionaryService dictionaryService;//字典表
    @Autowired
    private ForumService forumService;//萌宠讨论
    @Autowired
    private LiuyanService liuyanService;//留言反馈
    @Autowired
    private NewsService newsService;//通知公告
    @Autowired
    private YonghuService yonghuService;//用户
    @Autowired
    private UsersService usersService;//管理员


    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永不会进入");
        else if("用户".equals(role))
            params.put("yonghuId",request.getSession().getAttribute("userId"));
        params.put("chongwuDeleteStart",1);params.put("chongwuDeleteEnd",1);
        CommonUtil.checkMap(params);
        PageUtils page = chongwuService.queryPage(params);

        //字典表数据转换
        List<ChongwuView> list =(List<ChongwuView>)page.getList();
        for(ChongwuView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        ChongwuEntity chongwu = chongwuService.selectById(id);
        if(chongwu !=null){
            //entity转view
            ChongwuView view = new ChongwuView();
            BeanUtils.copyProperties( chongwu , view );//把实体数据重构到view中
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody ChongwuEntity chongwu, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,chongwu:{}",this.getClass().getName(),chongwu.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");

        Wrapper<ChongwuEntity> queryWrapper = new EntityWrapper<ChongwuEntity>()
            .eq("chongwu_name", chongwu.getChongwuName())
            .eq("chongwu_types", chongwu.getChongwuTypes())
            .eq("chongwuleix_types", chongwu.getChongwuleixTypes())
            .eq("chongwu_age", chongwu.getChongwuAge())
            .eq("xingbie_types", chongwu.getXingbieTypes())
            .eq("chongwu_tezheng", chongwu.getChongwuTezheng())
            .eq("dongwu_types", chongwu.getDongwuTypes())
            .eq("chongwu_delete", chongwu.getChongwuDelete())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        ChongwuEntity chongwuEntity = chongwuService.selectOne(queryWrapper);
        if(chongwuEntity==null){
            chongwu.setChongwuDelete(1);
            chongwu.setCreateTime(new Date());
            chongwuService.insert(chongwu);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody ChongwuEntity chongwu, HttpServletRequest request) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.debug("update方法:,,Controller:{},,chongwu:{}",this.getClass().getName(),chongwu.toString());
        ChongwuEntity oldChongwuEntity = chongwuService.selectById(chongwu.getId());//查询原先数据

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
        if("".equals(chongwu.getChongwuPhoto()) || "null".equals(chongwu.getChongwuPhoto())){
                chongwu.setChongwuPhoto(null);
        }

            chongwuService.updateById(chongwu);//根据id更新
            return R.ok();
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        List<ChongwuEntity> oldChongwuList =chongwuService.selectBatchIds(Arrays.asList(ids));//要删除的数据
        ArrayList<ChongwuEntity> list = new ArrayList<>();
        for(Integer id:ids){
            ChongwuEntity chongwuEntity = new ChongwuEntity();
            chongwuEntity.setId(id);
            chongwuEntity.setChongwuDelete(2);
            list.add(chongwuEntity);
        }
        if(list != null && list.size() >0){
            chongwuService.updateBatchById(list);
        }

        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName, HttpServletRequest request){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        Integer yonghuId = Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId")));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            List<ChongwuEntity> chongwuList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields= new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if(lastIndexOf == -1){
                return R.error(511,"该文件没有后缀");
            }else{
                String suffix = fileName.substring(lastIndexOf);
                if(!".xls".equals(suffix)){
                    return R.error(511,"只支持后缀为xls的excel文件");
                }else{
                    URL resource = this.getClass().getClassLoader().getResource("static/upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if(!file.exists()){
                        return R.error(511,"找不到上传文件，请联系管理员");
                    }else{
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示
                        for(List<String> data:dataList){
                            //循环
                            ChongwuEntity chongwuEntity = new ChongwuEntity();
//                            chongwuEntity.setChongwuName(data.get(0));                    //宠物名称 要改的
//                            chongwuEntity.setChongwuPhoto("");//详情和图片
//                            chongwuEntity.setChongwuTypes(Integer.valueOf(data.get(0)));   //宠物品种 要改的
//                            chongwuEntity.setChongwuleixTypes(Integer.valueOf(data.get(0)));   //宠物类型 要改的
//                            chongwuEntity.setChongwuAge(Integer.valueOf(data.get(0)));   //宠物年龄 要改的
//                            chongwuEntity.setXingbieTypes(Integer.valueOf(data.get(0)));   //宠物性别 要改的
//                            chongwuEntity.setChongwuTezheng(data.get(0));                    //宠物特征 要改的
//                            chongwuEntity.setDongwuTypes(Integer.valueOf(data.get(0)));   //宠物状态 要改的
//                            chongwuEntity.setChongwuDelete(1);//逻辑删除字段
//                            chongwuEntity.setChongwuContent("");//详情和图片
//                            chongwuEntity.setCreateTime(date);//时间
                            chongwuList.add(chongwuEntity);


                            //把要查询是否重复的字段放入map中
                        }

                        //查询是否重复
                        chongwuService.insertBatch(chongwuList);
                        return R.ok();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error(511,"批量插入数据异常，请联系管理员");
        }
    }




    /**
    * 前端列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("list方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));

        CommonUtil.checkMap(params);
        PageUtils page = chongwuService.queryPage(params);

        //字典表数据转换
        List<ChongwuView> list =(List<ChongwuView>)page.getList();
        for(ChongwuView c:list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段

        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        ChongwuEntity chongwu = chongwuService.selectById(id);
            if(chongwu !=null){


                //entity转view
                ChongwuView view = new ChongwuView();
                BeanUtils.copyProperties( chongwu , view );//把实体数据重构到view中

                //修改对应字典表字段
                dictionaryService.dictionaryConvert(view, request);
                return R.ok().put("data", view);
            }else {
                return R.error(511,"查不到数据");
            }
    }


    /**
    * 前端保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody ChongwuEntity chongwu, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,chongwu:{}",this.getClass().getName(),chongwu.toString());
        Wrapper<ChongwuEntity> queryWrapper = new EntityWrapper<ChongwuEntity>()
            .eq("chongwu_name", chongwu.getChongwuName())
            .eq("chongwu_types", chongwu.getChongwuTypes())
            .eq("chongwuleix_types", chongwu.getChongwuleixTypes())
            .eq("chongwu_age", chongwu.getChongwuAge())
            .eq("xingbie_types", chongwu.getXingbieTypes())
            .eq("chongwu_tezheng", chongwu.getChongwuTezheng())
            .eq("dongwu_types", chongwu.getDongwuTypes())
            .eq("chongwu_delete", chongwu.getChongwuDelete())
//            .notIn("chongwu_types", new Integer[]{102})
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        ChongwuEntity chongwuEntity = chongwuService.selectOne(queryWrapper);
        if(chongwuEntity==null){
            chongwu.setChongwuDelete(1);
            chongwu.setCreateTime(new Date());
        chongwuService.insert(chongwu);

            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

}

