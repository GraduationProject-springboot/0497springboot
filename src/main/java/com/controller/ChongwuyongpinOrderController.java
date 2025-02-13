
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
 * 宠物用品订单
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/chongwuyongpinOrder")
public class ChongwuyongpinOrderController {
    private static final Logger logger = LoggerFactory.getLogger(ChongwuyongpinOrderController.class);

    private static final String TABLE_NAME = "chongwuyongpinOrder";

    @Autowired
    private ChongwuyongpinOrderService chongwuyongpinOrderService;


    @Autowired
    private TokenService tokenService;

    @Autowired
    private AddressService addressService;//收货地址
    @Autowired
    private ChongwuService chongwuService;//宠物
    @Autowired
    private ChongwuYuyueService chongwuYuyueService;//宠物领养申请
    @Autowired
    private ChongwuyongpinService chongwuyongpinService;//宠物用品
    @Autowired
    private ChongwuyongpinCollectionService chongwuyongpinCollectionService;//宠物用品收藏
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
        CommonUtil.checkMap(params);
        PageUtils page = chongwuyongpinOrderService.queryPage(params);

        //字典表数据转换
        List<ChongwuyongpinOrderView> list =(List<ChongwuyongpinOrderView>)page.getList();
        for(ChongwuyongpinOrderView c:list){
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
        ChongwuyongpinOrderEntity chongwuyongpinOrder = chongwuyongpinOrderService.selectById(id);
        if(chongwuyongpinOrder !=null){
            //entity转view
            ChongwuyongpinOrderView view = new ChongwuyongpinOrderView();
            BeanUtils.copyProperties( chongwuyongpinOrder , view );//把实体数据重构到view中
            //级联表 收货地址
            //级联表
            AddressEntity address = addressService.selectById(chongwuyongpinOrder.getAddressId());
            if(address != null){
            BeanUtils.copyProperties( address , view ,new String[]{ "id", "createTime", "insertTime", "updateTime", "yonghuId"});//把级联的数据添加到view中,并排除id和创建时间字段,当前表的级联注册表
            view.setAddressId(address.getId());
            }
            //级联表 用户
            //级联表
            YonghuEntity yonghu = yonghuService.selectById(chongwuyongpinOrder.getYonghuId());
            if(yonghu != null){
            BeanUtils.copyProperties( yonghu , view ,new String[]{ "id", "createTime", "insertTime", "updateTime", "yonghuId"});//把级联的数据添加到view中,并排除id和创建时间字段,当前表的级联注册表
            view.setYonghuId(yonghu.getId());
            }
            //级联表 宠物用品
            //级联表
            ChongwuyongpinEntity chongwuyongpin = chongwuyongpinService.selectById(chongwuyongpinOrder.getChongwuyongpinId());
            if(chongwuyongpin != null){
            BeanUtils.copyProperties( chongwuyongpin , view ,new String[]{ "id", "createTime", "insertTime", "updateTime", "yonghuId"});//把级联的数据添加到view中,并排除id和创建时间字段,当前表的级联注册表
            view.setChongwuyongpinId(chongwuyongpin.getId());
            }
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
    public R save(@RequestBody ChongwuyongpinOrderEntity chongwuyongpinOrder, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,chongwuyongpinOrder:{}",this.getClass().getName(),chongwuyongpinOrder.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");
        else if("用户".equals(role))
            chongwuyongpinOrder.setYonghuId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));

        chongwuyongpinOrder.setCreateTime(new Date());
        chongwuyongpinOrder.setInsertTime(new Date());
        chongwuyongpinOrderService.insert(chongwuyongpinOrder);

        return R.ok();
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody ChongwuyongpinOrderEntity chongwuyongpinOrder, HttpServletRequest request) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.debug("update方法:,,Controller:{},,chongwuyongpinOrder:{}",this.getClass().getName(),chongwuyongpinOrder.toString());
        ChongwuyongpinOrderEntity oldChongwuyongpinOrderEntity = chongwuyongpinOrderService.selectById(chongwuyongpinOrder.getId());//查询原先数据

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
//        else if("用户".equals(role))
//            chongwuyongpinOrder.setYonghuId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));

            chongwuyongpinOrderService.updateById(chongwuyongpinOrder);//根据id更新
            return R.ok();
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        List<ChongwuyongpinOrderEntity> oldChongwuyongpinOrderList =chongwuyongpinOrderService.selectBatchIds(Arrays.asList(ids));//要删除的数据
        chongwuyongpinOrderService.deleteBatchIds(Arrays.asList(ids));

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
            List<ChongwuyongpinOrderEntity> chongwuyongpinOrderList = new ArrayList<>();//上传的东西
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
                            ChongwuyongpinOrderEntity chongwuyongpinOrderEntity = new ChongwuyongpinOrderEntity();
//                            chongwuyongpinOrderEntity.setChongwuyongpinOrderUuidNumber(data.get(0));                    //订单号 要改的
//                            chongwuyongpinOrderEntity.setAddressId(Integer.valueOf(data.get(0)));   //收货地址 要改的
//                            chongwuyongpinOrderEntity.setChongwuyongpinId(Integer.valueOf(data.get(0)));   //宠物用品 要改的
//                            chongwuyongpinOrderEntity.setYonghuId(Integer.valueOf(data.get(0)));   //用户 要改的
//                            chongwuyongpinOrderEntity.setBuyNumber(Integer.valueOf(data.get(0)));   //购买数量 要改的
//                            chongwuyongpinOrderEntity.setChongwuyongpinOrderTruePrice(data.get(0));                    //实付价格 要改的
//                            chongwuyongpinOrderEntity.setChongwuyongpinOrderTypes(Integer.valueOf(data.get(0)));   //订单类型 要改的
//                            chongwuyongpinOrderEntity.setChongwuyongpinOrderPaymentTypes(Integer.valueOf(data.get(0)));   //支付类型 要改的
//                            chongwuyongpinOrderEntity.setInsertTime(date);//时间
//                            chongwuyongpinOrderEntity.setCreateTime(date);//时间
                            chongwuyongpinOrderList.add(chongwuyongpinOrderEntity);


                            //把要查询是否重复的字段放入map中
                                //订单号
                                if(seachFields.containsKey("chongwuyongpinOrderUuidNumber")){
                                    List<String> chongwuyongpinOrderUuidNumber = seachFields.get("chongwuyongpinOrderUuidNumber");
                                    chongwuyongpinOrderUuidNumber.add(data.get(0));//要改的
                                }else{
                                    List<String> chongwuyongpinOrderUuidNumber = new ArrayList<>();
                                    chongwuyongpinOrderUuidNumber.add(data.get(0));//要改的
                                    seachFields.put("chongwuyongpinOrderUuidNumber",chongwuyongpinOrderUuidNumber);
                                }
                        }

                        //查询是否重复
                         //订单号
                        List<ChongwuyongpinOrderEntity> chongwuyongpinOrderEntities_chongwuyongpinOrderUuidNumber = chongwuyongpinOrderService.selectList(new EntityWrapper<ChongwuyongpinOrderEntity>().in("chongwuyongpin_order_uuid_number", seachFields.get("chongwuyongpinOrderUuidNumber")));
                        if(chongwuyongpinOrderEntities_chongwuyongpinOrderUuidNumber.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(ChongwuyongpinOrderEntity s:chongwuyongpinOrderEntities_chongwuyongpinOrderUuidNumber){
                                repeatFields.add(s.getChongwuyongpinOrderUuidNumber());
                            }
                            return R.error(511,"数据库的该表中的 [订单号] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                        chongwuyongpinOrderService.insertBatch(chongwuyongpinOrderList);
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
        PageUtils page = chongwuyongpinOrderService.queryPage(params);

        //字典表数据转换
        List<ChongwuyongpinOrderView> list =(List<ChongwuyongpinOrderView>)page.getList();
        for(ChongwuyongpinOrderView c:list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段

        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        ChongwuyongpinOrderEntity chongwuyongpinOrder = chongwuyongpinOrderService.selectById(id);
            if(chongwuyongpinOrder !=null){


                //entity转view
                ChongwuyongpinOrderView view = new ChongwuyongpinOrderView();
                BeanUtils.copyProperties( chongwuyongpinOrder , view );//把实体数据重构到view中

                //级联表
                    AddressEntity address = addressService.selectById(chongwuyongpinOrder.getAddressId());
                if(address != null){
                    BeanUtils.copyProperties( address , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setAddressId(address.getId());
                }
                //级联表
                    YonghuEntity yonghu = yonghuService.selectById(chongwuyongpinOrder.getYonghuId());
                if(yonghu != null){
                    BeanUtils.copyProperties( yonghu , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setYonghuId(yonghu.getId());
                }
                //级联表
                    ChongwuyongpinEntity chongwuyongpin = chongwuyongpinService.selectById(chongwuyongpinOrder.getChongwuyongpinId());
                if(chongwuyongpin != null){
                    BeanUtils.copyProperties( chongwuyongpin , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setChongwuyongpinId(chongwuyongpin.getId());
                }
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
    public R add(@RequestBody ChongwuyongpinOrderEntity chongwuyongpinOrder, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,chongwuyongpinOrder:{}",this.getClass().getName(),chongwuyongpinOrder.toString());
            ChongwuyongpinEntity chongwuyongpinEntity = chongwuyongpinService.selectById(chongwuyongpinOrder.getChongwuyongpinId());
            if(chongwuyongpinEntity == null){
                return R.error(511,"查不到该宠物用品");
            }
            // Double chongwuyongpinNewMoney = chongwuyongpinEntity.getChongwuyongpinNewMoney();

            if(false){
            }
            else if(chongwuyongpinEntity.getChongwuyongpinNewMoney() == null){
                return R.error(511,"现价不能为空");
            }
            else if((chongwuyongpinEntity.getChongwuyongpinKucunNumber() -chongwuyongpinOrder.getBuyNumber())<0){
                return R.error(511,"购买数量不能大于库存数量");
            }

            //计算所获得积分
            Double buyJifen =0.0;
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            YonghuEntity yonghuEntity = yonghuService.selectById(userId);
            if(yonghuEntity == null)
                return R.error(511,"用户不能为空");
            if(yonghuEntity.getNewMoney() == null)
                return R.error(511,"用户金额不能为空");
            double balance = yonghuEntity.getNewMoney() - chongwuyongpinEntity.getChongwuyongpinNewMoney()*chongwuyongpinOrder.getBuyNumber();//余额
            if(balance<0)
                return R.error(511,"余额不够支付");
            chongwuyongpinOrder.setChongwuyongpinOrderTypes(101); //设置订单状态为已支付
            chongwuyongpinOrder.setChongwuyongpinOrderTruePrice(chongwuyongpinEntity.getChongwuyongpinNewMoney()*chongwuyongpinOrder.getBuyNumber()); //设置实付价格
            chongwuyongpinOrder.setYonghuId(userId); //设置订单支付人id
            chongwuyongpinOrder.setChongwuyongpinOrderUuidNumber(String.valueOf(new Date().getTime()));
            chongwuyongpinOrder.setChongwuyongpinOrderPaymentTypes(1);
            chongwuyongpinOrder.setInsertTime(new Date());
            chongwuyongpinOrder.setCreateTime(new Date());
                chongwuyongpinEntity.setChongwuyongpinKucunNumber( chongwuyongpinEntity.getChongwuyongpinKucunNumber() -chongwuyongpinOrder.getBuyNumber());
                chongwuyongpinService.updateById(chongwuyongpinEntity);
                chongwuyongpinOrderService.insert(chongwuyongpinOrder);//新增订单
            //更新第一注册表
            yonghuEntity.setNewMoney(balance);//设置金额
            yonghuService.updateById(yonghuEntity);


            return R.ok();
    }


    /**
    * 退款
    */
    @RequestMapping("/refund")
    public R refund(Integer id, HttpServletRequest request){
        logger.debug("refund方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        String role = String.valueOf(request.getSession().getAttribute("role"));

            ChongwuyongpinOrderEntity chongwuyongpinOrder = chongwuyongpinOrderService.selectById(id);//当前表service
            Integer buyNumber = chongwuyongpinOrder.getBuyNumber();
            Integer chongwuyongpinId = chongwuyongpinOrder.getChongwuyongpinId();
            if(chongwuyongpinId == null)
                return R.error(511,"查不到该宠物用品");
            ChongwuyongpinEntity chongwuyongpinEntity = chongwuyongpinService.selectById(chongwuyongpinId);
            if(chongwuyongpinEntity == null)
                return R.error(511,"查不到该宠物用品");
            Double chongwuyongpinNewMoney = chongwuyongpinEntity.getChongwuyongpinNewMoney();
            if(chongwuyongpinNewMoney == null)
                return R.error(511,"宠物用品价格不能为空");

            Integer userId = (Integer) request.getSession().getAttribute("userId");
            YonghuEntity yonghuEntity = yonghuService.selectById(userId);
            if(yonghuEntity == null)
                return R.error(511,"用户不能为空");
            if(yonghuEntity.getNewMoney() == null)
            return R.error(511,"用户金额不能为空");
            Double zhekou = 1.0;

                //计算金额
                Double money = chongwuyongpinEntity.getChongwuyongpinNewMoney() * buyNumber  * zhekou;
                //计算所获得积分
                Double buyJifen = 0.0;
                yonghuEntity.setNewMoney(yonghuEntity.getNewMoney() + money); //设置金额



            chongwuyongpinEntity.setChongwuyongpinKucunNumber(chongwuyongpinEntity.getChongwuyongpinKucunNumber() + buyNumber);


            chongwuyongpinOrder.setChongwuyongpinOrderTypes(102);//设置订单状态为退款
            chongwuyongpinOrderService.updateById(chongwuyongpinOrder);//根据id更新
            yonghuService.updateById(yonghuEntity);//更新用户信息
            chongwuyongpinService.updateById(chongwuyongpinEntity);//更新订单中宠物用品的信息

            return R.ok();
    }

    /**
     * 发货
     */
    @RequestMapping("/deliver")
    public R deliver(Integer id ){
        logger.debug("refund:,,Controller:{},,ids:{}",this.getClass().getName(),id.toString());
        ChongwuyongpinOrderEntity  chongwuyongpinOrderEntity = chongwuyongpinOrderService.selectById(id);
        chongwuyongpinOrderEntity.setChongwuyongpinOrderTypes(103);//设置订单状态为已发货
        chongwuyongpinOrderService.updateById( chongwuyongpinOrderEntity);

        return R.ok();
    }


    /**
     * 收货
     */
    @RequestMapping("/receiving")
    public R receiving(Integer id){
        logger.debug("refund:,,Controller:{},,ids:{}",this.getClass().getName(),id.toString());
        ChongwuyongpinOrderEntity  chongwuyongpinOrderEntity = chongwuyongpinOrderService.selectById(id);
        chongwuyongpinOrderEntity.setChongwuyongpinOrderTypes(104);//设置订单状态为收货
        chongwuyongpinOrderService.updateById( chongwuyongpinOrderEntity);
        return R.ok();
    }

}

