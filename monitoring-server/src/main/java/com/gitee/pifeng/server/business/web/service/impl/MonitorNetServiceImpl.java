package com.gitee.pifeng.server.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.common.exception.NetException;
import com.gitee.pifeng.common.util.NetUtils;
import com.gitee.pifeng.server.business.web.dao.IMonitorNetDao;
import com.gitee.pifeng.server.business.web.entity.MonitorNet;
import com.gitee.pifeng.server.business.web.service.IMonitorNetService;
import com.gitee.pifeng.server.business.web.vo.HomeNetVo;
import com.gitee.pifeng.server.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.server.business.web.vo.MonitorNetVo;
import com.gitee.pifeng.server.constant.WebResponseConstants;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网络信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/1 15:09
 */
@Service
public class MonitorNetServiceImpl extends ServiceImpl<IMonitorNetDao, MonitorNet> implements IMonitorNetService {

    /**
     * 网络信息数据访问对象
     */
    @Autowired
    private IMonitorNetDao monitorNetDao;

    /**
     * <p>
     * 获取home页的网络信息
     * </p>
     *
     * @return home页的网络信息表现层对象
     * @author 皮锋
     * @custom.date 2020/9/1 15:20
     */
    @Override
    public HomeNetVo getHomeNetInfo() {
        // 网络正常率统计
        Map<String, Object> map = this.monitorNetDao.getNetNormalRateStatistics();
        return HomeNetVo.builder()
                .netSum(NumberUtil.parseInt(map.get("netSum").toString()))
                .netConnectSum(NumberUtil.parseInt(map.get("netConnectSum").toString()))
                .netDisconnectSum(NumberUtil.parseInt(map.get("netDisconnectSum").toString()))
                .netUnsentSum(NumberUtil.parseInt(map.get("netUnsentSum").toString()))
                .netConnectRate(map.get("netConnectRate").toString())
                .build();
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
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2020/9/26 13:28
     */
    @Override
    public Page<MonitorNetVo> getMonitorNetList(Long current, Long size, String ipSource, String ipTarget, String status) {
        // 查询数据库
        IPage<MonitorNet> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorNet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(ipSource)) {
            lambdaQueryWrapper.like(MonitorNet::getIpSource, ipSource);
        }
        if (StringUtils.isNotBlank(ipTarget)) {
            lambdaQueryWrapper.like(MonitorNet::getIpTarget, ipTarget);
        }
        if (StringUtils.isNotBlank(status)) {
            // -1 用来表示状态未知
            if (StringUtils.equals(status, ZeroOrOneConstants.MINUS_ONE)) {
                // 状态为 null 或 空字符串
                lambdaQueryWrapper.and(wrapper -> wrapper.isNull(MonitorNet::getStatus).or().eq(MonitorNet::getStatus, ""));
            } else {
                lambdaQueryWrapper.eq(MonitorNet::getStatus, status);
            }
        }
        IPage<MonitorNet> monitorNetPage = this.monitorNetDao.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorNet> monitorNets = monitorNetPage.getRecords();
        // 转换成网络信息表现层对象
        List<MonitorNetVo> monitorNetVos = Lists.newLinkedList();
        for (MonitorNet monitorNet : monitorNets) {
            MonitorNetVo monitorNetVo = MonitorNetVo.builder().build().convertFor(monitorNet);
            monitorNetVos.add(monitorNetVo);
        }
        // 设置返回对象
        Page<MonitorNetVo> monitorNetVoPage = new Page<>();
        monitorNetVoPage.setRecords(monitorNetVos);
        monitorNetVoPage.setTotal(monitorNetPage.getTotal());
        return monitorNetVoPage;
    }

    /**
     * <p>
     * 删除网络
     * </p>
     *
     * @param monitorNetVos 删除网络
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/9/26 14:02
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public LayUiAdminResultVo deleteMonitorNet(List<MonitorNetVo> monitorNetVos) {
        List<String> ipSources = Lists.newArrayList();
        List<String> ipTargets = Lists.newArrayList();
        for (MonitorNetVo monitorNetVo : monitorNetVos) {
            ipSources.add(monitorNetVo.getIpSource());
            ipTargets.add(monitorNetVo.getIpTarget());
        }
        LambdaUpdateWrapper<MonitorNet> monitorNetLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorNetLambdaUpdateWrapper.in(MonitorNet::getIpSource, ipSources);
        monitorNetLambdaUpdateWrapper.in(MonitorNet::getIpTarget, ipTargets);
        this.monitorNetDao.delete(monitorNetLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
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
     * @custom.date 2020/11/20 13:58
     */
    @Override
    public LayUiAdminResultVo editMonitorNetwork(MonitorNetVo monitorNetVo) {
        // 根据目标IP，查询数据库中是否已经存在此目标IP的记录
        LambdaQueryWrapper<MonitorNet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 去掉它自己这条记录
        lambdaQueryWrapper.ne(MonitorNet::getId, monitorNetVo.getId());
        lambdaQueryWrapper.eq(MonitorNet::getIpTarget, monitorNetVo.getIpTarget());
        MonitorNet dbMonitorNet = this.monitorNetDao.selectOne(lambdaQueryWrapper);
        if (dbMonitorNet != null) {
            return LayUiAdminResultVo.ok(WebResponseConstants.EXIST);
        }
        MonitorNet monitorNet = monitorNetVo.convertTo();
        monitorNet.setUpdateTime(new Date());
        int result = this.monitorNetDao.updateById(monitorNet);
        if (result == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
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
     * @custom.date 2020/11/20 15:30
     */
    @Override
    public LayUiAdminResultVo addMonitorNetwork(MonitorNetVo monitorNetVo) throws NetException {
        // 根据目标IP，查询数据库中是否已经存在此目标IP的记录
        LambdaQueryWrapper<MonitorNet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorNet::getIpTarget, monitorNetVo.getIpTarget());
        MonitorNet dbMonitorNet = this.monitorNetDao.selectOne(lambdaQueryWrapper);
        if (dbMonitorNet != null) {
            return LayUiAdminResultVo.ok(WebResponseConstants.EXIST);
        }
        MonitorNet monitorNet = monitorNetVo.convertTo();
        monitorNet.setIpSource(NetUtils.getLocalIp());
        monitorNet.setIsAlarm(ZeroOrOneConstants.ZERO);
        monitorNet.setInsertTime(new Date());
        int result = this.monitorNetDao.insert(monitorNet);
        if (result == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

}
