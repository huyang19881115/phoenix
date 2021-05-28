package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerMemoryHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerMemoryChartVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 * 服务器内存历史记录
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-24
 */
@Api(tags = "服务器.服务器内存历史记录")
@Controller
@RequestMapping("/monitor-server-memory-history")
public class MonitorServerMemoryHistoryController {

    /**
     * 服务器内存历史记录服务类
     */
    @Autowired
    private IMonitorServerMemoryHistoryService monitorServerMemoryHistoryService;

    /**
     * <p>
     * 获取服务器详情页面服务器内存图表信息
     * </p>
     *
     * @param ip   服务器IP地址
     * @param time 时间
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/10/21 12:35
     */
    @ApiOperation(value = "获取服务器详情页面服务器内存图表信息")
    @ResponseBody
    @GetMapping("/get-server-detail-page-server-memory-chart-info")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ip", value = "服务器IP地址", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "time", value = "时间", paramType = "query", dataType = "string")})
    public LayUiAdminResultVo getServerDetailPageServerMemoryChartInfo(@RequestParam(name = "ip") String ip,
                                                                       @RequestParam(name = "time", required = false) String time) {
        List<ServerDetailPageServerMemoryChartVo> monitorJvmMemoryChartVos = this.monitorServerMemoryHistoryService.getServerDetailPageServerMemoryChartInfo(ip, time);
        return LayUiAdminResultVo.ok(monitorJvmMemoryChartVos);
    }

}

