package com.imby.server.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imby.server.business.web.service.IMonitorServerOsService;
import com.imby.server.business.web.vo.LayUiAdminResultVo;
import com.imby.server.business.web.vo.MonitorServerOsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>
 * 服务器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/2 21:50
 */
@Controller
@RequestMapping("/server")
@Api(tags = "服务器")
public class MonitorServerController {

    /**
     * 服务器服务类
     */
    @Autowired
    private IMonitorServerOsService monitorServerOsService;

    /**
     * <p>
     * 访问服务器列表页面
     * </p>
     *
     * @return {@link ModelAndView} 服务器列表页面
     * @author 皮锋
     * @custom.date 2020/9/2 21:46
     */
    @ApiOperation(value = "访问服务器列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        return new ModelAndView("server/server");
    }

    /**
     * <p>
     * 获取服务器列表
     * </p>
     *
     * @param current    当前页
     * @param size       每页显示条数
     * @param ip         IP
     * @param serverName 服务器名
     * @param osName     操作系统名称
     * @param osVersion  操作系统版本
     * @param userName   用户名称
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/4 12:34
     */
    @ApiOperation(value = "获取服务器列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "ip", value = "IP", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "serverName", value = "服务器名", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "osName", value = "操作系统名称", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "osVersion", value = "操作系统版本", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "userName", value = "用户名称", paramType = "query", dataType = "string")})
    @GetMapping("/get-monitor-server-os-list")
    @ResponseBody
    public LayUiAdminResultVo getMonitorServerOsList(Long current, Long size, String ip, String serverName, String osName, String osVersion, String userName) {
        Page<MonitorServerOsVo> page = this.monitorServerOsService.getMonitorServerOsList(current, size, ip, serverName, osName, osVersion, userName);
        return LayUiAdminResultVo.ok(page);
    }

    /**
     * <p>
     * 删除服务器
     * </p>
     *
     * @param monitorServerOsVos 服务器信息
     * @return layUiAdmin响应对象：如果删除用户成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/9/4 16:10
     */
    @ApiOperation(value = "删除服务器")
    @PostMapping("/delete-monitor-server")
    @ResponseBody
    public LayUiAdminResultVo deleteMonitorServer(@RequestBody List<MonitorServerOsVo> monitorServerOsVos) {
        return this.monitorServerOsService.deleteMonitorServer(monitorServerOsVos);
    }

}