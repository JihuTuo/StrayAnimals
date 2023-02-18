package com.shiep;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shiep.mapper.ILocationDao;
import com.shiep.mapper.IAnimalAdoptMapper;
import com.shiep.mapper.ICountyMapper;
import com.shiep.mapper.IUserMapper;
import com.shiep.entity.County;
import com.shiep.entity.User;
import com.shiep.jwt.config.JwtConfig;
import com.shiep.jwt.pojo.UserInfo;
import com.shiep.service.IAnimalAdoptService;
import com.shiep.service.IMailService;
import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DBtest {
    @Autowired
    private DataSource dataSource;

    @Resource
    private ICountyMapper countyMapper;

    @Resource
    private JwtConfig jwtConfig;


    @Resource
    private IAnimalAdoptMapper animalAdoptMapper;

    @Test
    public void testDBConnection() throws SQLException {
        System.out.println("--------" + dataSource.getConnection());
    }

    @Resource
    private IAnimalAdoptService animalAdoptService;

    @Resource
    private IUserMapper userMapper;

    @Resource
    private ILocationDao locationDao;

    @Test
    public void select(){
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    @Test
    public void select2(){
        User user = userMapper.selectById(1);
        System.out.println("-------------------" + user) ;
    }


    @Test
    public void testUsername() {
        String str = "898809898098";
        boolean matches =str.matches("^[0-9]+$");
        System.out.println(matches);
    }

    @Test
    public void testMethod() {
        QueryWrapper<County> qw = new QueryWrapper<>();
        qw.lambda().eq(County::getCityId, 1);
        List<County> counties = countyMapper.selectList(qw);
        counties.forEach(System.out::println);
    }

    @Test
    public void testToken() {
        UserInfo userInfo = this.jwtConfig.getUserInfoFromToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJhZG1pbiIsImlhdCI6MTU4MTA0NzY4NywiZXhwIjoxNTgxMDUxMjg3fQ.YCsr2TAR5Ju_kxH9GuzQzAeI-O2Ivn10_dN0hVx1layGzE5wA-0Q4a1d34-YxHwo2FA0xF_nOeLF9kkFvsCQtg");
        Claims claim = this.jwtConfig.getTokenClaim("" + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJhZG1pbiIsImlhdCI6MTU4MTA0NzY4NywiZXhwIjoxNTgxMDUxMjg3fQ.YCsr2TAR5Ju_kxH9GuzQzAeI-O2Ivn10_dN0hVx1layGzE5wA-0Q4a1d34-YxHwo2FA0xF_nOeLF9kkFvsCQtg");
        System.out.println("cliam" + claim) ;
        System.out.println( "test" + userInfo.getId()) ;
        System.out.println("test" + userInfo.getUsername()) ;
        System.out.println("test" + userInfo.getPhoto()) ;
    }

   /* @Test
    public void testTime() {
        AnimalAdopt animalAdopt = new AnimalAdopt();
        animalAdopt.setCreateTime("2020-02-08 14:45:15");
        System.out.println(new Date()) ;
    }*/

   /*@Test
    public void testQueryAllAdoptVO() {
       List<AnimalAdoptVo> animalAdopts = this.animalAdoptService.queryAll();
       animalAdopts.forEach(System.out::println);
   }*/

   /* @Test
    public void testSQLLocation() {
        Location location = this.locationDao.getLocation(1L);
        System.out.println(location) ;
    }*/

   @Test
   public void testSQL() {
       int code = (int)((Math.random()*9+1)*100000);
       System.out.println(code);
   }


    @Resource
    private JavaMailSender javaMailSender ;
    @Test
    public void TestSendMail() {
        SimpleMailMessage message = new SimpleMailMessage() ;
        message.setFrom("tuojihu@qq.com");  // 你自己的邮箱地址
        message.setTo("tuojihu@163.com"); //接受者的邮箱
        message.setSubject("测试邮件，来自老李的祝福，tuojihuo.qq.com");
        message.setText("好好学习，天天向上！");
        this.javaMailSender.send(message);
    }

    @Resource
    private IMailService mailService;
    @Test
    public void testMail() {
        Boolean aBoolean = this.mailService.sendMailAndGenerateCode("tuojihu@163.com");
        System.out.println(aBoolean) ;
    }


    @Test
    public void  teststr() {
        String str = "<img src=\"http://www.sa.com/upload/ueditor/f6707879-2f8f-427e-b37c-d800e5ce8be4.jpg";
        String str2 = "<img src=\"http://www.sa.com/upload/ueditor/f6707879-2f8f-427e-b37c-d800e5ce8be4.jpg\" <img src=\"http://www.sa.com/upload/ueditor/f6707879-2f8f-427e-b37c-d800e5ce8be4.jpg\"title=\"f6707879-2f8f-427e-b37c-d800e5ce8be4.jpg\" alt=\"3.jpg\"/></p><p style=\"white-space: normal;\">&nbsp;&nbsp;&nbsp;&nbsp;这也太看了吧！这到底是是谁呀。！！！！好的</p><p style=\"white-space: normal;\"><br/></p><p style=\"white-space: normal;\"><br/></p><p style=\"white-space: normal;\"><br/></p><p style=\"white-space: normal;\">";
        //System.out.println(str.split("src=\"")[1]) ;


        Pattern  pattern = Pattern.compile("http://www.sa.com/upload/ueditor/.{40}");
        Matcher matcher = pattern.matcher(str2);
        if (matcher.find()) {
            System.out.println(matcher.group()) ;
        }
    }

    @Test
    public  void printAllBeans() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(StaryAnimalsApplication.class);
        String[] beans =  applicationContext.getBeanDefinitionNames();
        for (String beanName : beans) {
            System.out.println("BeanName:" + beanName);
        }
    }
}
