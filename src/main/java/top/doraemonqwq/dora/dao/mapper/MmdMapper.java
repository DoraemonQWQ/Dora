package top.doraemonqwq.dora.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.doraemonqwq.dora.entity.pojo.MmdPojo;

import java.util.List;
import java.util.Map;

/**
 * @author Doraemon
 * mmd表的Mapper接口
 */
@Mapper
@Repository
public interface MmdMapper {

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
    List<MmdPojo> selectMmdsByFuzzyText(@Param("text") String text);

    /**
     * 通过mmd的id查询一个mmd资源
     * @param id 需要查询的id
     * @return 返回查询到的mmd资源
     */
    MmdPojo selectMmdById(@Param("id") Integer id);

    /**
     * 添加一个mmd资源
     * @param mmdPojo 需要添加的mmd的实体类
     * @return 返回成功数
     */
    int addMmd(MmdPojo mmdPojo);

    /**
     * 更新一个mmd资源
     * @param map 更新的内容
     * @return 返回成功数
     */
    int updateMmd(Map<String,Object> map);

    /**
     * 删除一个mmd资源
     * @param id 需要删除的mmd的id
     * @return 返回成功数
     */
    int delMmd(@Param("id") Integer id);

}
