package com.imby.plug;

import java.nio.charset.Charset;

import com.imby.common.constant.AlarmLevelEnums;
import com.imby.common.domain.Alarm;
import com.imby.common.domain.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 测试监控客户端
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午7:43:38
 */
@Slf4j
public class MonitorTest {

	public static void main(String[] args) throws InterruptedException {
		Monitor.start("", "monitor.properties");
		// 发送告警
		// while (true) {
		Alarm alarm = new Alarm();
		alarm.setTest(true);
		alarm.setAlarmLevel(AlarmLevelEnums.WARN);
		alarm.setTitle("测试");
		alarm.setMsg("测试监控系统短信功能！");
		alarm.setCharset(Charset.forName("gbk"));
		Result result = Monitor.sendAlarm(alarm);
		log.info("发送告警：{}", result.toJsonString());
		Thread.sleep(60000);
		// }
	}

}