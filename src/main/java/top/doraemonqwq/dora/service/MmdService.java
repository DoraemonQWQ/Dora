package top.doraemonqwq.dora.service;

import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.entity.pojo.MmdPojo;

import java.util.List;

/**
 * @author Doraemon
 * mmd的服务接口
 */
@Service
public interface MmdService {

    /**
     * 查询所有的mmd资源
     * @return 返回全部mmd资源的list
     */
    List<MmdPojo> selectMmds();

    /**
     * 通过模糊查询mmd的名称，进行筛选的mmd资源
     * @param text 需要进行模糊查询的文本
     * @return 返回筛选后的mmd资源的list
     */
    List<MmdPojo> selectMmdsByFuzzyText(String text);

    /**
     * 通过mmd的id查询一个mmd资源
     * @param id 需要查询的id
     * @return 返回查询到的mmd资源
     */
    MmdPojo selectMmdById(Integer id);

    /**
     * 添加一个mmd资源
     * @param mmdPojo 需要添加的mmd的实体类
     * @return 返回boolean，true为成功，false为失败
     */
    boolean addMmd(MmdPojo mmdPojo);

    /**
     * 更新一个mmd资源
     * @param mmdId 需要更新的id
     * @param newName 新的mmd名字
     * @return 返回boolean，true为成功，false为失败
     */
    boolean updateMmdName(Integer mmdId, String newName);

    /**
     * 更新一个mmd资源
     * @param mmdId 更新的内容
     * @param newIntroduction 新的简介
     * @return 返回boolean，true为成功，false为失败
     */
    boolean updateMmdIntroduction(Integer mmdId, String newIntroduction);

    /**
     * 删除一个mmd资源
     * @param id 需要删除的mmd的id
     * @return 返回boolean，true为成功，false为失败
     */
    boolean delMmd(Integer id);

}
