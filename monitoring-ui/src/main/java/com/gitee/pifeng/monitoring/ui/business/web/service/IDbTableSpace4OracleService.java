package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.ui.business.web.vo.DbTableSpaceAll4OracleVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.DbTableSpaceFile4OracleVo;

import java.sql.SQLException;

/**
 * <p>
 * Oracle数据库表空间服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/31 16:10
 */
public interface IDbTableSpace4OracleService {

    /**
     * <p>
     * 获取表空间列表(按文件)
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param id      数据库ID
     * @return 简单分页模型
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2020/12/31 16:15
     */
    Page<DbTableSpaceFile4OracleVo> getTableSpaceListFile(Long current, Long size, Long id) throws SQLException;

    /**
     * <p>
     * 获取表空间列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param id      数据库ID
     * @return 简单分页模型
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2020/12/31 16:15
     */
    Page<DbTableSpaceAll4OracleVo> getTableSpaceListAll(Long current, Long size, Long id) throws SQLException;

}
