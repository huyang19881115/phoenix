package com.transfar.business.plug.service;

import com.transfar.dto.BaseResponsePackage;
import com.transfar.dto.ServerPackage;

/**
 * <p>
 * 服务器信息服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:11:17
 */
public interface IServerService {

	/**
	 * <p>
	 * 处理服务器信息包
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月7日 下午5:14:29
	 * @param serverPackage 服务器信息包
	 * @return BaseResponsePackage
	 */
	BaseResponsePackage dealServerPackage(ServerPackage serverPackage);

}
