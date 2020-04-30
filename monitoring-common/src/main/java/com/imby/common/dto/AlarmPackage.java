package com.imby.common.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

import com.imby.common.abs.InstanceBean;
import com.imby.common.domain.Alarm;

/**
 * <p>
 * 监控告警信息传输层对象
 * </p>
 * 用来定义监控告警信息的数据格式
 *
 * @author 皮锋
 * @custom.date 2020/3/3 10:01
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class AlarmPackage extends InstanceBean implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8198453286095801334L;

    /**
     * ID
     */
    private String id;

    /**
     * 告警对象
     */
    private Alarm alarm;

    /**
     * 时间
     */
    private Date dateTime;

}