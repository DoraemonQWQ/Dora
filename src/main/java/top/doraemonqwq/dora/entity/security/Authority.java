package top.doraemonqwq.dora.entity.security;


import java.util.ArrayList;
import java.util.List;

/**
 * @author Doraemon
 * authority权限表的实体类
 */
public class Authority {
    /**
     * authorityId      权限id
     * url              权限所允许访问的url get的返回类型和set的接收类型是数组
     * permission       权限标识 表示权限的范围 get的返回类型和set的接收类型暂定为数组
     */
    private Integer authorityId;
    private String url;
    private String permission;

    public Authority() {
    }

    public Authority(int authorityId, String url, String permission) {
        this.authorityId = authorityId;
        this.url = url;
        this.permission = permission;
    }

    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getUrlList() {
        List<String> listUrl = new ArrayList<>();
        String[] split = url.split(",");
        for (String s : split) {
            if (s.charAt(0) == ' ') {
                s = s.substring(1, s.length());
            }
            listUrl.add(s);
        }

        return listUrl;
    }

    public void setUrlList(List<String> listUrl) {
        String url = "";

        for (int i = 0; i < listUrl.size(); i++) {
            url += listUrl.get(i);
            if (listUrl.size() - 1 > i) {
                url += ", ";
            }
        }

        this.url = url;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "authorityId=" + authorityId +
                ", url='" + url + '\'' +
                ", permission='" + permission + '\'' +
                '}';
    }
}
