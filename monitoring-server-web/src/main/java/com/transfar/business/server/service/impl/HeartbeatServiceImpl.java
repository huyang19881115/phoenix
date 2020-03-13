package com.transfar.business.server.service.impl;

import com.transfar.business.server.core.InstancePool;
import com.transfar.business.server.domain.Instance;
import com.transfar.business.server.service.IHeartbeatService;
import com.transfar.dto.HeartbeatPackage;
import com.transfar.property.MonitoringServerWebProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 心跳服务实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/12 10:05
 */
@Service
public class HeartbeatServiceImpl implements IHeartbeatService {

    /**
     * 应用实例池
     */
    @Autowired
    private InstancePool instancePool;

    /**
     * 监控配置属性
     */
    @Autowired
    private MonitoringServerWebProperties config;

    /**
     * <p>
     * 处理心跳包
     * </p>
     *
     * @param heartbeatPackage 心跳包
     * @return boolean
     * @author 皮锋
     * @custom.date 2020/3/12 10:18
     */
    @Override
    public boolean dealHeartbeatPackage(HeartbeatPackage heartbeatPackage) {
        // 把应用实例交给应用实例池，交给应用实例池管理
        String key = heartbeatPackage.getInstanceId();
        // 判读池中是否已经有了这个实例，有就设置成在线，并且更新时间，没有就添加一个
        Instance poolInstance = this.instancePool.get(key);
        if (poolInstance == null) {
            Instance instance = new Instance();
            // 实例本身信息
            instance.setEndpoint(heartbeatPackage.getEndpoint());
            instance.setInstanceId(heartbeatPackage.getInstanceId());
            instance.setInstanceName(heartbeatPackage.getInstanceName());
            // 实例状态信息
            instance.setOnline(true);
            instance.setDateTime(heartbeatPackage.getDateTime());
            instance.setThresholdSecond((int) (heartbeatPackage.getRate() * config.getThreshold()));
            // 加入到应用实例池
            this.instancePool.put(key, instance);
        } else {
            poolInstance.setOnline(true);
            poolInstance.setDateTime(heartbeatPackage.getDateTime());
            this.instancePool.replace(key, poolInstance);
        }
        return true;
    }
}