package com.dkliu.vlog.Controller;

import com.dkliu.vlog.common.ResponseResult;
import com.dkliu.vlog.common.ResultCode;
import com.dkliu.vlog.model.dto.LoginDto;
import com.dkliu.vlog.model.dto.PhoneLoginDto;
import com.dkliu.vlog.service.RedisService;
import com.dkliu.vlog.service.UserService;
import com.dkliu.vlog.util.SmsUtil;
import com.dkliu.vlog.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author Hinoki
 * @Date 2020/12/5
 **/

@RestController
@RequestMapping(value = "/api/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private SmsUtil smsUtil;
    @Resource
    private RedisService redisService;

    @PostMapping(value = "/login")
    ResponseResult login(@RequestBody LoginDto loginDto) {
        log.info("loginDto:" + loginDto);
        boolean flag = userService.login(loginDto);
        if (flag) {
            return ResponseResult.success(userService.getUser(loginDto.getPhone()));
        } else {
            return ResponseResult.failure(ResultCode.USER_SIGN_IN_FAIL);
        }
    }

    @PostMapping(value = "/sendcode")
    public ResponseResult sendCode(@RequestParam String phone) {
        //随机验证码
        String code = StringUtil.getVerifyCode().toString();
        System.out.println(code);
        //给入参手机号发送短信
        boolean flag = smsUtil.sendSms(phone, code);
        if (flag) {
            //验证码存入redis，1分钟有效
            redisService.set(phone, code, 1L);
            return ResponseResult.success(code);
        } else {
            redisService.set(phone, code, 1L);
            return ResponseResult.failure(ResultCode.SMS_ERROR);
        }
    }

    @PostMapping(value = "/phonelogin")
    public ResponseResult login(@RequestBody PhoneLoginDto phoneLoginDto) {
        log.info("phoneLoginDto:" + phoneLoginDto);
        boolean flag = userService.phoneLogin(phoneLoginDto);
        if (flag) {
            return ResponseResult.success(userService.getUser(phoneLoginDto.getPhone()));
        } else {
            return ResponseResult.failure(ResultCode.USER_VERIFY_CODE_ERROR);
        }
    }
}
