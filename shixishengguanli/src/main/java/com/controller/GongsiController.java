
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
 * 公司
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/gongsi")
public class GongsiController {
    private static final Logger logger = LoggerFactory.getLogger(GongsiController.class);

    private static final String TABLE_NAME = "gongsi";

    @Autowired
    private GongsiService gongsiService;


    @Autowired
    private TokenService tokenService;

    @Autowired
    private DictionaryService dictionaryService;//字典
    @Autowired
    private GonggaoService gonggaoService;//公告
    @Autowired
    private JianliService jianliService;//简历
    @Autowired
    private LaoshiService laoshiService;//老师
    @Autowired
    private ShixiService shixiService;//实习
    @Autowired
    private ShixirizhiService shixirizhiService;//实习日志
    @Autowired
    private TongzhiService tongzhiService;//通知
    @Autowired
    private XueshengService xueshengService;//学生
    @Autowired
    private ZhaopinService zhaopinService;//职位招聘
    @Autowired
    private ZhaopinCollectionService zhaopinCollectionService;//职位收藏
    @Autowired
    private ZhaopinLiuyanService zhaopinLiuyanService;//职位留言
    @Autowired
    private ZhaopinToudiService zhaopinToudiService;//简历投递
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
        else if("学生".equals(role))
            params.put("xueshengId",request.getSession().getAttribute("userId"));
        else if("公司".equals(role))
            params.put("gongsiId",request.getSession().getAttribute("userId"));
        else if("老师".equals(role))
            params.put("laoshiId",request.getSession().getAttribute("userId"));
        params.put("gongsiDeleteStart",1);params.put("gongsiDeleteEnd",1);
        CommonUtil.checkMap(params);
        PageUtils page = gongsiService.queryPage(params);

        //字典表数据转换
        List<GongsiView> list =(List<GongsiView>)page.getList();
        for(GongsiView c:list){
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
        GongsiEntity gongsi = gongsiService.selectById(id);
        if(gongsi !=null){
            //entity转view
            GongsiView view = new GongsiView();
            BeanUtils.copyProperties( gongsi , view );//把实体数据重构到view中
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
    public R save(@RequestBody GongsiEntity gongsi, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,gongsi:{}",this.getClass().getName(),gongsi.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");

        Wrapper<GongsiEntity> queryWrapper = new EntityWrapper<GongsiEntity>()
            .eq("username", gongsi.getUsername())
            .or()
            .eq("gongsi_phone", gongsi.getGongsiPhone())
            .andNew()
            .eq("gongsi_delete", 1)
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        GongsiEntity gongsiEntity = gongsiService.selectOne(queryWrapper);
        if(gongsiEntity==null){
            gongsi.setGongsiDelete(1);
            gongsi.setCreateTime(new Date());
            gongsi.setPassword("123456");
            gongsiService.insert(gongsi);
            return R.ok();
        }else {
            return R.error(511,"账户或者联系方式已经被使用");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody GongsiEntity gongsi, HttpServletRequest request) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.debug("update方法:,,Controller:{},,gongsi:{}",this.getClass().getName(),gongsi.toString());
        GongsiEntity oldGongsiEntity = gongsiService.selectById(gongsi.getId());//查询原先数据

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
        if("".equals(gongsi.getGongsiPhoto()) || "null".equals(gongsi.getGongsiPhoto())){
                gongsi.setGongsiPhoto(null);
        }

            gongsiService.updateById(gongsi);//根据id更新
            return R.ok();
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        List<GongsiEntity> oldGongsiList =gongsiService.selectBatchIds(Arrays.asList(ids));//要删除的数据
        ArrayList<GongsiEntity> list = new ArrayList<>();
        for(Integer id:ids){
            GongsiEntity gongsiEntity = new GongsiEntity();
            gongsiEntity.setId(id);
            gongsiEntity.setGongsiDelete(2);
            list.add(gongsiEntity);
        }
        if(list != null && list.size() >0){
            gongsiService.updateBatchById(list);
        }

        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName, HttpServletRequest request){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        Integer xueshengId = Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId")));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            List<GongsiEntity> gongsiList = new ArrayList<>();//上传的东西
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
                            GongsiEntity gongsiEntity = new GongsiEntity();
//                            gongsiEntity.setUsername(data.get(0));                    //账户 要改的
//                            //gongsiEntity.setPassword("123456");//密码
//                            gongsiEntity.setGongsiName(data.get(0));                    //公司名称 要改的
//                            gongsiEntity.setGongsiTypes(Integer.valueOf(data.get(0)));   //公司类型 要改的
//                            gongsiEntity.setGongsiPhone(data.get(0));                    //联系方式 要改的
//                            gongsiEntity.setGongsiEmail(data.get(0));                    //邮箱 要改的
//                            gongsiEntity.setGongsiPhoto("");//详情和图片
//                            gongsiEntity.setGongsiChenglishijianTime(sdf.parse(data.get(0)));          //公司成立时间 要改的
//                            gongsiEntity.setGongsiContent("");//详情和图片
//                            gongsiEntity.setGongsiDelete(1);//逻辑删除字段
//                            gongsiEntity.setCreateTime(date);//时间
                            gongsiList.add(gongsiEntity);


                            //把要查询是否重复的字段放入map中
                                //账户
                                if(seachFields.containsKey("username")){
                                    List<String> username = seachFields.get("username");
                                    username.add(data.get(0));//要改的
                                }else{
                                    List<String> username = new ArrayList<>();
                                    username.add(data.get(0));//要改的
                                    seachFields.put("username",username);
                                }
                                //联系方式
                                if(seachFields.containsKey("gongsiPhone")){
                                    List<String> gongsiPhone = seachFields.get("gongsiPhone");
                                    gongsiPhone.add(data.get(0));//要改的
                                }else{
                                    List<String> gongsiPhone = new ArrayList<>();
                                    gongsiPhone.add(data.get(0));//要改的
                                    seachFields.put("gongsiPhone",gongsiPhone);
                                }
                        }

                        //查询是否重复
                         //账户
                        List<GongsiEntity> gongsiEntities_username = gongsiService.selectList(new EntityWrapper<GongsiEntity>().in("username", seachFields.get("username")).eq("gongsi_delete", 1));
                        if(gongsiEntities_username.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(GongsiEntity s:gongsiEntities_username){
                                repeatFields.add(s.getUsername());
                            }
                            return R.error(511,"数据库的该表中的 [账户] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                         //联系方式
                        List<GongsiEntity> gongsiEntities_gongsiPhone = gongsiService.selectList(new EntityWrapper<GongsiEntity>().in("gongsi_phone", seachFields.get("gongsiPhone")).eq("gongsi_delete", 1));
                        if(gongsiEntities_gongsiPhone.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(GongsiEntity s:gongsiEntities_gongsiPhone){
                                repeatFields.add(s.getGongsiPhone());
                            }
                            return R.error(511,"数据库的该表中的 [联系方式] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                        gongsiService.insertBatch(gongsiList);
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
    * 登录
    */
    @IgnoreAuth
    @RequestMapping(value = "/login")
    public R login(String username, String password, String captcha, HttpServletRequest request) {
        GongsiEntity gongsi = gongsiService.selectOne(new EntityWrapper<GongsiEntity>().eq("username", username));
        if(gongsi==null || !gongsi.getPassword().equals(password))
            return R.error("账号或密码不正确");
        else if(gongsi.getGongsiDelete() != 1)
            return R.error("账户已被删除");
        String token = tokenService.generateToken(gongsi.getId(),username, "gongsi", "公司");
        R r = R.ok();
        r.put("token", token);
        r.put("role","公司");
        r.put("username",gongsi.getGongsiName());
        r.put("tableName","gongsi");
        r.put("userId",gongsi.getId());
        return r;
    }

    /**
    * 注册
    */
    @IgnoreAuth
    @PostMapping(value = "/register")
    public R register(@RequestBody GongsiEntity gongsi, HttpServletRequest request) {
//    	ValidatorUtils.validateEntity(user);
        Wrapper<GongsiEntity> queryWrapper = new EntityWrapper<GongsiEntity>()
            .eq("username", gongsi.getUsername())
            .or()
            .eq("gongsi_phone", gongsi.getGongsiPhone())
            .andNew()
            .eq("gongsi_delete", 1)
            ;
        GongsiEntity gongsiEntity = gongsiService.selectOne(queryWrapper);
        if(gongsiEntity != null)
            return R.error("账户或者联系方式已经被使用");
        gongsi.setGongsiDelete(1);
        gongsi.setCreateTime(new Date());
        gongsiService.insert(gongsi);

        return R.ok();
    }

    /**
     * 重置密码
     */
    @GetMapping(value = "/resetPassword")
    public R resetPassword(Integer  id, HttpServletRequest request) {
        GongsiEntity gongsi = gongsiService.selectById(id);
        gongsi.setPassword("123456");
        gongsiService.updateById(gongsi);
        return R.ok();
    }

	/**
	 * 修改密码
	 */
	@GetMapping(value = "/updatePassword")
	public R updatePassword(String  oldPassword, String  newPassword, HttpServletRequest request) {
        GongsiEntity gongsi = gongsiService.selectById((Integer)request.getSession().getAttribute("userId"));
		if(newPassword == null){
			return R.error("新密码不能为空") ;
		}
		if(!oldPassword.equals(gongsi.getPassword())){
			return R.error("原密码输入错误");
		}
		if(newPassword.equals(gongsi.getPassword())){
			return R.error("新密码不能和原密码一致") ;
		}
        gongsi.setPassword(newPassword);
		gongsiService.updateById(gongsi);
		return R.ok();
	}



    /**
     * 忘记密码
     */
    @IgnoreAuth
    @RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request) {
        GongsiEntity gongsi = gongsiService.selectOne(new EntityWrapper<GongsiEntity>().eq("username", username));
        if(gongsi!=null){
            gongsi.setPassword("123456");
            gongsiService.updateById(gongsi);
            return R.ok();
        }else{
           return R.error("账号不存在");
        }
    }


    /**
    * 获取用户的session用户信息
    */
    @RequestMapping("/session")
    public R getCurrGongsi(HttpServletRequest request){
        Integer id = (Integer)request.getSession().getAttribute("userId");
        GongsiEntity gongsi = gongsiService.selectById(id);
        if(gongsi !=null){
            //entity转view
            GongsiView view = new GongsiView();
            BeanUtils.copyProperties( gongsi , view );//把实体数据重构到view中

            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }
    }


    /**
    * 退出
    */
    @GetMapping(value = "logout")
    public R logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return R.ok("退出成功");
    }



    /**
    * 前端列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("list方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));

        CommonUtil.checkMap(params);
        PageUtils page = gongsiService.queryPage(params);

        //字典表数据转换
        List<GongsiView> list =(List<GongsiView>)page.getList();
        for(GongsiView c:list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段

        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        GongsiEntity gongsi = gongsiService.selectById(id);
            if(gongsi !=null){


                //entity转view
                GongsiView view = new GongsiView();
                BeanUtils.copyProperties( gongsi , view );//把实体数据重构到view中

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
    public R add(@RequestBody GongsiEntity gongsi, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,gongsi:{}",this.getClass().getName(),gongsi.toString());
        Wrapper<GongsiEntity> queryWrapper = new EntityWrapper<GongsiEntity>()
            .eq("username", gongsi.getUsername())
            .or()
            .eq("gongsi_phone", gongsi.getGongsiPhone())
            .andNew()
            .eq("gongsi_delete", 1)
//            .notIn("gongsi_types", new Integer[]{102})
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        GongsiEntity gongsiEntity = gongsiService.selectOne(queryWrapper);
        if(gongsiEntity==null){
            gongsi.setGongsiDelete(1);
            gongsi.setCreateTime(new Date());
            gongsi.setPassword("123456");
        gongsiService.insert(gongsi);

            return R.ok();
        }else {
            return R.error(511,"账户或者联系方式已经被使用");
        }
    }

}

