package com.gitee.pifeng.monitoring.ui.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorJvmMemoryHistory;
import com.gitee.pifeng.monitoring.ui.business.web.vo.InstanceDetailPageJvmMemoryChartVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * java虚拟机内存历史记录数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-24
 */
public interface IMonitorJvmMemoryHistoryDao extends BaseMapper<MonitorJvmMemoryHistory> {

    /**
     * <p>
     * 获取应用实例详情页面java虚拟机内存图表信息
     * </p>
     *
     * @param params 请求参数
     * @return 应用实例详情页面java虚拟机内存图表信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/16 13:48
     */
    List<InstanceDetailPageJvmMemoryChartVo> getInstanceDetailPageJvmMemoryChartInfo(@Param("params") Map<String, Object> params);

}
