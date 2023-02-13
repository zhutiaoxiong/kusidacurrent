package model.safety;

/**
 * Created by qq522414074 on 2016/10/27.
 */
public class DataSafeTy {
    public static int from;//23457表示   从安全页面点击   1修改密码2更改手机号3绑定邮箱4安全问题//控制修改密码等页面的跳转
    public static int back;// 表示  两个布局layout 手机号码和回答安全问题验证结束到修改手机号，密码，邮箱，安全问题返回到哪里；//
    public static int entrance;//1_6 1注册2修改手机号3修改密码，4修改邮箱5修改安全问题6提交新手机号 收到验证码7跳转到登陆页面找回密码哪里//控制收到验证码返回的跳转
}
