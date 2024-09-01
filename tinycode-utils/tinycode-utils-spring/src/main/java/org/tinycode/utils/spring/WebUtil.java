package org.tinycode.utils.spring;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.tinycode.utils.common.obj.ObjectUtil;
import org.tinycode.utils.common.string.StringUtil;
import org.tinycode.utils.common.string.ValueUtil;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * web工具类.
 *
 * @author littlehui
 * @date 2013-2-8 下午5:53:44
 */
public class WebUtil {
	/**
	 * 判断是否是ajax请求.
	 * 
	 * @param request
	 * @return
	 * @author littlehui
	 * @date 2012-7-30 下午02:59:10
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		return !ObjectUtil.isEmpty(request.getHeader("X-Requested-With"));
	}

	/**
	 * 获取远程访问的IP地址.
	 * 
	 * @param request
	 * @return
	 * @author littlehui
	 * @date 2012-9-18 上午09:02:09
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if (ip.equals("127.0.0.1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					throw new RuntimeException(e);
				}
				ip = inet.getHostAddress();
			}
		}

		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ip != null && ip.length() > 15) { // "***.***.***.***".length() = 15
			if (ip.indexOf(",") > 0) {
				ip = ip.substring(0, ip.indexOf(","));
			}
		}

		return ip;
	}

	/**
	 * 除去数组中的空值和签名参数.
	 * 
	 * @param sArray
	 *            签名参数组
	 * @param filterKeys
	 *            过滤的key
	 * @return 去掉空值与过滤key参数后的新签名参数组
	 * @author qingwu
	 * @date 2013-4-9 上午09:00:00
	 */
	public static Map<String, Object> paraFilter(Map<String, Object> sArray,
			String[] filterKeys) {

		Map<String, Object> result = new HashMap<String, Object>();

		if (sArray == null || sArray.size() <= 0) {
			return result;
		}

		for (String key : sArray.keySet()) {
			Object value = sArray.get(key);
			if (ObjectUtil.isEmpty(value)
					|| StringUtil.contans(filterKeys, key)) {
				continue;
			}
			result.put(key, value);
		}

		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 * @author qingwu
	 * @date 2013-4-9 上午09:00:00
	 */
	public static String createLinkString(Map<String, Object> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = ValueUtil.getString(params.get(key));

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

	/**
	 * 获得编码后的url地址
	 * 
	 * @param url
	 *            url地址
	 * @return
	 * @author qingwu
	 * @date 2013-9-3 上午09:00:00
	 */
	public static String encodedUri(String url) {
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url)
				.build();
		String encodedUri = uriComponents.encode().toUriString();
		return encodedUri;
	}

	/**
	 * 获得自动提交表单html代码.
	 * 
	 * @param params
	 *            表单参数
	 * @return 自动提交form表单
	 * @author qingwu
	 * @date 2013-10-15 上午10:52:51
	 */
	public static String buildForm(Map<String, Object> params,
			String actionUrl, String method) {
		StringBuffer sbHtml = new StringBuffer();
		sbHtml.append("<form id=\"autoPaySubmit\" name=\"autoPaySubmit\" action=\""
				+ actionUrl + "\" METHOD=" + method + ">\n");
		Set<String> set = params.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String paramName = it.next();
			String paramValue = ValueUtil.getString(params.get(paramName));
			sbHtml.append("<input type=hidden name=\"" + paramName
					+ "\" value=\"" + paramValue + "\"/>\n");
		}
		// submit按钮控件请不要含有name属性
		sbHtml.append("<input type=\"submit\" value=\"确认\" style=\"display:none;\"/>\n</form>");
		sbHtml.append("<script>document.forms['autoPaySubmit'].submit();</script>");
		return sbHtml.toString();
	}
}
