package com.gitee.pifeng.server.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.common.exception.NetException;
import com.gitee.pifeng.common.util.NetUtils;
import com.gitee.pifeng.server.business.web.entity.MonitorNet;
import com.gitee.pifeng.server.business.web.service.IMonitorNetService;
import com.gitee.pifeng.server.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.server.business.web.vo.MonitorNetVo;
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
 * 网络
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/26 13:19
 */
@Controller
@RequestMapping("/monitor-network")
@Api(tags = "网络")
public class MonitorNetworkController {

    /**
     * 网络信息服务类
     */
    @Autowired
    private IMonitorNetService monitorNetService;

    /**
     * <p>
     * 访问网络列表页面
     * </p>
     *
     * @return {@link ModelAndView} 网络列表页面
     * @throws NetException 自定义获取网络信息异常
     * @author 皮锋
     * @custom.date 2020/9/26 10:53
     */
    @ApiOperation(value = "访问网络列表页面")
    @GetMapping("/list")
    public ModelAndView list() throws NetException {
        ModelAndView mv = new ModelAndView("network/network");
        // 源IP
        mv.addObject("ipSource", NetUtils.getLocalIp());
        return mv;
    }

    /**
     * <p>
     * 获取网络列表
     * </p>
     *
     * @param current  当前页
     * @param size     每页显示条数
     * @param ipSource IP地址（来源）
     * @param ipTarget IP地址（目的地）
     * @param status   状态（0：网络不通，1：网络正常）
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/26 10:59
     */
    @ApiOperation(value = "获取网络列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "ipSource", value = "IP地址（来源）", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "ipTarget", value = "IP地址（目的地）", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "status", value = "状态（0：网络不通，1：网络正常）", paramType = "query", dataType = "string")})
    @GetMapping("/get-monitor-network-list")
    @ResponseBody
    public LayUiAdminResultVo getMonitorNetList(Long current, Long size, String ipSource, String ipTarget, String status) {
        Page<MonitorNetVo> page = this.monitorNetService.getMonitorNetList(current, size, ipSource, ipTarget, status);
        return LayUiAdminResultVo.ok(page);
    }

    /**
     * <p>
     * 删除网络
     * </p>
     *
     * @param monitorNetVos 删除网络
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/9/26 12:59
     */
    @ApiOperation(value = "删除网络")
    @PostMapping("/delete-monitor-network")
    @ResponseBody
    public LayUiAdminResultVo deleteMonitorNet(@RequestBody List<MonitorNetVo> monitorNetVos) {
        return this.monitorNetService.deleteMonitorNet(monitorNetVos);
    }

    /**
     * <p>
     * 访问编辑网络信息表单页面
     * </p>
     *
     * @param id 网络ID
     * @return {@link ModelAndView} 编辑网络信息表单页面
     * @author 皮锋
     * @custom.date 2020/11/20 9:17
     */
    @ApiOperation(value = "访问编辑网络信息表单页面")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "网络ID", required = true, paramType = "query", dataType = "long")})
    @GetMapping("/edit-monitor-network-form")
    public ModelAndView editMonitorNetworkForm(@RequestParam(name = "id") Long id) {
        MonitorNet monitorNet = this.monitorNetService.getById(id);
        MonitorNetVo monitorNetVo = MonitorNetVo.builder().build().convertFor(monitorNet);
        ModelAndView mv = new ModelAndView("network/edit-network");
        mv.addObject(monitorNetVo);
        return mv;
    }

    /**
     * <p>
     * 访问新增网络信息表单页面
     * </p>
     *
     * @return {@link ModelAndView} 新增网络信息表单页面
     * @author 皮锋
     * @custom.date 2020/11/20 14:54
     */
    @ApiOperation(value = "访问新增网络信息表单页面")
    @GetMapping("/add-monitor-network-form")
    public ModelAndView addMonitorNetworkForm() {
        return new ModelAndView("network/add-network");
    }

    /**
     * <p>
     * 编辑网络信息
     * </p>
     *
     * @param monitorNetVo 网络信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/11/20 13:56
     */
    @ApiOperation(value = "编辑网络信息")
    @PostMapping("/edit-monitor-network")
    @ResponseBody
    public LayUiAdminResultVo editMonitorNetwork(MonitorNetVo monitorNetVo) {
        return this.monitorNetService.editMonitorNetwork(monitorNetVo);
    }

    /**
     * <p>
     * 添加网络信息
     * </p>
     *
     * @param monitorNetVo 网络信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @throws NetException 自定义获取网络信息异常
     * @author 皮锋
     * @custom.date 2020/11/20 15:05
     */
    @ApiOperation(value = "添加网络信息")
    @PostMapping("/add-monitor-network")
    @ResponseBody
    public LayUiAdminResultVo addMonitorNetwork(MonitorNetVo monitorNetVo) throws NetException {
        return this.monitorNetService.addMonitorNetwork(monitorNetVo);
    }

}
