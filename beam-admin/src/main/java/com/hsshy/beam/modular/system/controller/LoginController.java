package com.hsshy.beam.modular.system.controller;
import com.google.code.kaptcha.Constants;
import com.hsshy.beam.common.base.controller.BaseAdminController;
import com.hsshy.beam.common.shiro.ShiroUtils;
import com.hsshy.beam.common.util.KaptchaUtil;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.sys.dto.LoginForm;
import com.hsshy.beam.sys.entity.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class LoginController  {



    @PostMapping(value = "/login")
    @ResponseBody
    public Object login(@RequestBody LoginForm loginForm){

        if(new KaptchaUtil().isKaptchaOnOff()){
            String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
            if(!loginForm.getCaptcha().equalsIgnoreCase(kaptcha)){
                return R.fail("验证码不正确");
            }
        }
        try{
            Subject subject = ShiroUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(loginForm.getUsername(), loginForm.getPassword());
            subject.login(token);
        }catch (UnknownAccountException e) {
            return R.fail(e.getMessage());
        }catch (IncorrectCredentialsException e) {
            return R.fail("账号或者密码不正确");
        }catch (LockedAccountException e) {
            return R.fail("账号已被锁定,请联系管理员");
        }catch (AuthenticationException e) {
            return R.fail("账户验证失败");
        }
        return R.ok("登陆成功");
    }



    /**
     * 退出
     */
    @GetMapping(value = "/logout")
    @ResponseBody
    public void logout() {
        ShiroUtils.logout();
    }






}