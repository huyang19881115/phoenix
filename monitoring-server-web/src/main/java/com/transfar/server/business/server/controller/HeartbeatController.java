package com.transfar.server.business.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.transfar.common.domain.Result;
import com.transfar.common.dto.BaseResponsePackage;
import com.transfar.common.dto.HeartbeatPackage;
import com.transfar.server.business.server.core.PackageConstructor;
import com.transfar.server.business.server.service.IHeartbeatService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 心跳控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午12:16:06
 */
@RestController
@RequestMapping("/heartbeat")
@Api(tags = "心跳")
public class HeartbeatController {

    /**
     * 心跳服务接口
     */
    @Autowired
    private IHeartbeatService heartbeatService;

    /**
     * <p>
     * 监控服务端程序接收监控代理程序或者监控客户端程序发的心跳包，并返回结果
     * </p>
     *
     * @param request 请求参数
     * @return BaseResponsePackage
     * @author 皮锋
     * @custom.date 2020年3月4日 下午12:27:47
     */
    @ApiOperation(value = "监控服务端程序接收监控代理程序或者监控客户端程序发的心跳包，并返回结果", notes = "接收心跳包")
    @PostMapping("/accept-heartbeat-package")
    public BaseResponsePackage acceptHeartbeatPackage(@RequestBody String request) {
        HeartbeatPackage heartbeatPackage = JSON.parseObject(request, HeartbeatPackage.class);
        Result result = this.heartbeatService.dealHeartbeatPackage(heartbeatPackage);
        if (result.isSuccess()) {
            return new PackageConstructor().structureBaseResponsePackageBySuccess();
        } else {
            return new PackageConstructor().structureBaseResponsePackageByFail(result.getMsg());
        }
    }

}
