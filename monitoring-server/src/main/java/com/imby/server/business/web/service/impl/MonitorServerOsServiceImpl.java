package com.imby.server.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.imby.common.constant.AlarmTypeEnums;
import com.imby.server.business.web.dao.*;
import com.imby.server.business.web.entity.*;
import com.imby.server.business.web.service.IMonitorServerOsService;
import com.imby.server.business.web.vo.HomeServerVo;
import com.imby.server.business.web.vo.LayUiAdminResultVo;
import com.imby.server.business.web.vo.MonitorServerOsVo;
import com.imby.server.constant.WebResponseConstants;
import com.imby.server.core.ThreadPool;
import com.imby.server.inf.IServerMonitoringListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务器服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
@Service
public class MonitorServerOsServiceImpl extends ServiceImpl<IMonitorServerOsDao, MonitorServerOs> implements IMonitorServerOsService {

    /**
     * 服务器信息监听器
     */
    @Autowired
    private List<IServerMonitoringListener> serverMonitoringListeners;

    /**
     * 服务器数据访问对象
     */
    @Autowired
    private IMonitorServerOsDao monitorServerOsDao;

    /**
     * 服务器CPU数据访问对象
     */
    @Autowired
    private IMonitorServerCpuDao monitorServerCpuDao;

    /**
     * 服务器磁盘数据访问对象
     */
    @Autowired
    private IMonitorServerDiskDao monitorServerDiskDao;

    /**
     * 服务器内存数据访问对象
     */
    @Autowired
    private IMonitorServerMemoryDao monitorServerMemoryDao;

    /**
     * 服务器网卡数据访问对象
     */
    @Autowired
    private IMonitorServerNetcardDao monitorServerNetcardDao;

    /**
     * <p>
     * 获取home页的服务器信息
     * </p>
     *
     * @return home页的服务器表现层对象
     * @author 皮锋
     * @custom.date 2020/8/4 16:40
     */
    @Override
    public HomeServerVo getHomeServerOsInfo() {
        // 服务器类型统计
        Map<String, Object> map = this.monitorServerOsDao.getServerOsTypeStatistics();
        return HomeServerVo.builder()
                .serverSum(NumberUtil.parseInt(map.get("serverSum").toString()))
                .windowsSum(NumberUtil.parseInt(map.get("windowsSum").toString()))
                .linuxSum(NumberUtil.parseInt(map.get("linuxSum").toString()))
                .otherSum(NumberUtil.parseInt(map.get("otherSum").toString()))
                .build();
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
     * @custom.date 2020/9/4 12:38
     */
    @Override
    public Page<MonitorServerOsVo> getMonitorServerOsList(Long current, Long size, String ip, String serverName, String osName, String osVersion, String userName) {
        // 查询数据库
        IPage<MonitorServerOs> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorServerOs> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(ip)) {
            lambdaQueryWrapper.like(MonitorServerOs::getIp, ip);
        }
        if (StringUtils.isNotBlank(serverName)) {
            lambdaQueryWrapper.like(MonitorServerOs::getServerName, serverName);
        }
        if (StringUtils.isNotBlank(osName)) {
            lambdaQueryWrapper.like(MonitorServerOs::getOsName, osName);
        }
        if (StringUtils.isNotBlank(osVersion)) {
            lambdaQueryWrapper.like(MonitorServerOs::getOsVersion, osVersion);
        }
        if (StringUtils.isNotBlank(userName)) {
            lambdaQueryWrapper.like(MonitorServerOs::getUserName, userName);
        }
        IPage<MonitorServerOs> monitorServerOsPage = this.monitorServerOsDao.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorServerOs> monitorServerOss = monitorServerOsPage.getRecords();
        // 转换成服务器信息表现层对象
        List<MonitorServerOsVo> monitorServerOsVos = Lists.newLinkedList();
        for (MonitorServerOs monitorServerOs : monitorServerOss) {
            MonitorServerOsVo monitorServerOsVo = MonitorServerOsVo.builder().build().convertFor(monitorServerOs);
            monitorServerOsVos.add(monitorServerOsVo);
        }
        // 设置返回对象
        Page<MonitorServerOsVo> monitorServerOsVoPage = new Page<>();
        monitorServerOsVoPage.setRecords(monitorServerOsVos);
        monitorServerOsVoPage.setTotal(monitorServerOsPage.getTotal());
        return monitorServerOsVoPage;
    }

    /**
     * <p>
     * 删除服务器
     * </p>
     *
     * @param monitorServerOsVos 服务器信息
     * @return layUiAdmin响应对象：如果删除用户成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/9/4 16:13
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public LayUiAdminResultVo deleteMonitorServer(List<MonitorServerOsVo> monitorServerOsVos) {
        List<String> ips = Lists.newArrayList();
        for (MonitorServerOsVo monitorServerOsVo : monitorServerOsVos) {
            ips.add(monitorServerOsVo.getIp());
        }
        // 服务器表
        LambdaUpdateWrapper<MonitorServerOs> serverOsLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverOsLambdaUpdateWrapper.in(MonitorServerOs::getIp, ips);
        this.monitorServerOsDao.delete(serverOsLambdaUpdateWrapper);
        // 服务器CPU表
        LambdaUpdateWrapper<MonitorServerCpu> serverCpuLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverCpuLambdaUpdateWrapper.in(MonitorServerCpu::getIp, ips);
        this.monitorServerCpuDao.delete(serverCpuLambdaUpdateWrapper);
        // 服务器磁盘表
        LambdaUpdateWrapper<MonitorServerDisk> serverDiskLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverDiskLambdaUpdateWrapper.in(MonitorServerDisk::getIp, ips);
        this.monitorServerDiskDao.delete(serverDiskLambdaUpdateWrapper);
        // 服务器内存表
        LambdaUpdateWrapper<MonitorServerMemory> serverMemoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverMemoryLambdaUpdateWrapper.in(MonitorServerMemory::getIp, ips);
        this.monitorServerMemoryDao.delete(serverMemoryLambdaUpdateWrapper);
        // 服务器网卡表
        LambdaUpdateWrapper<MonitorServerNetcard> serverNetcardLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverNetcardLambdaUpdateWrapper.in(MonitorServerNetcard::getIp, ips);
        this.monitorServerNetcardDao.delete(serverNetcardLambdaUpdateWrapper);

        // 调用监听器回调接口
        this.serverMonitoringListeners.forEach(e ->
                ThreadPool.CPU_INTENSIVE_THREAD_POOL_EXECUTOR.execute(() ->
                        e.wakeUpMonitorPool(AlarmTypeEnums.SERVER, ips)));
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 获取服务器操作系统信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return 服务器信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/26 20:26
     */
    @Override
    public MonitorServerOsVo getMonitorServerOsInfo(String ip) {
        LambdaQueryWrapper<MonitorServerOs> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorServerOs::getIp, ip);
        MonitorServerOs monitorServerOs = this.monitorServerOsDao.selectOne(lambdaQueryWrapper);
        return MonitorServerOsVo.builder().build().convertFor(monitorServerOs);
    }

}
