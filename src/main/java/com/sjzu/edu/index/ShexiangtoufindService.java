package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Page;
import com.sjzu.edu.common.model.Restaurant;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: https://jfinal.com/club
 *
 * BlogService
 * 所有 sql 与业务逻辑写在 Service 中，不要放在 Model 中，更不
 * 要放在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class ShexiangtoufindService {

    /**
     * 所有的 dao 对象也放在 Service 中，并且声明为 private，避免 sql 满天飞
     * sql 只放在业务层，或者放在外部 sql 模板，用模板引擎管理：
     * https://jfinal.com/doc/5-13
     */
    private Restaurant dao = new Restaurant().dao();

    // 定义服务器地址
    private static final String SERVER_8100 = "http://39.98.221.201:8100";
    private static final String SERVER_8101 = "http://39.98.221.201:8101";

    public Page<Restaurant> paginate(int pageNumber, int pageSize) {
        Page<Restaurant> page = dao.paginate(pageNumber, pageSize, "select *", "from restaurant order by id asc");
        // 处理图片路径
        processImageUrls(page);
        return page;
    }

    /**
     * 处理图片URL，确保图片能够正常显示
     */
    private void processImageUrls(Page<Restaurant> page) {
        if (page != null && page.getList() != null) {
            for (Restaurant restaurant : page.getList()) {
                // 假设Restaurant模型中有存储图片路径的字段，比如imageUrl
                // 这里需要根据您的实际字段名进行调整
                String originalPath = restaurant.getStr("imageUrl"); // 请根据实际字段名调整
                if (originalPath != null && !originalPath.trim().isEmpty()) {
                    String correctedPath = getAvailableImageUrl(originalPath);
                    restaurant.set("imageUrl", correctedPath); // 请根据实际字段名调整
                }
            }
        }
    }

    /**
     * 获取可用的图片URL
     */
    private String getAvailableImageUrl(String originalPath) {
        // 如果路径已经是完整URL，直接验证
        if (originalPath.startsWith("http")) {
            return getAvailableFullUrl(originalPath);
        }

        // 如果是相对路径，构建完整URL
        String path = originalPath.startsWith("/") ? originalPath : "/" + originalPath;

        // 先尝试8100端口
        String url8100 = SERVER_8100 + path;
        if (isUrlAccessible(url8100)) {
            return url8100;
        }

        // 再尝试8101端口
        String url8101 = SERVER_8101 + path;
        if (isUrlAccessible(url8101)) {
            return url8101;
        }

        // 如果两个都不可用，返回默认的8100（或者根据您的需求调整）
        return url8100;
    }

    /**
     * 获取可用的完整图片URL
     */
    private String getAvailableFullUrl(String originalUrl) {
        // 如果已经是8100端口，检查是否可用，不可用时切换到8101
        if (originalUrl.contains(":8100/")) {
            if (isUrlAccessible(originalUrl)) {
                return originalUrl;
            } else {
                // 切换到8101端口
                return originalUrl.replace(":8100/", ":8101/");
            }
        }
        // 如果已经是8101端口，检查是否可用，不可用时切换到8100
        else if (originalUrl.contains(":8101/")) {
            if (isUrlAccessible(originalUrl)) {
                return originalUrl;
            } else {
                // 切换到8100端口
                return originalUrl.replace(":8101/", ":8100/");
            }
        }

        return originalUrl;
    }

    /**
     * 检查URL是否可访问
     */
    private boolean isUrlAccessible(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            return false;
        }
    }
}