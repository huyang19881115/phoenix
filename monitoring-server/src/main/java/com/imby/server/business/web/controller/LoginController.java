package com.imby.server.business.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 身份认证
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/5/15 8:57
 */
@Controller
@Api(tags = "身份认证")
public class LoginController {

    /**
     * <p>
     * 进入登录页
     * </p>
     *
     * @return {@link ModelAndView} 登录页
     * @author 皮锋
     * @custom.date 2020/5/15 9:12
     */
    @ApiOperation(value = "进入登录页", notes = "进入登录页")
    @GetMapping("/login")
    public ModelAndView toLoginPage() {
        return new ModelAndView("login");
    }
}