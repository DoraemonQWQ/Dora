package top.doraemonqwq.dora.service;

import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.entity.pojo.GamePojo;

import java.util.List;

/**
 * game的服务接口
 * @author Doraemon
 */
@Service
public interface GameService {

    /**
     * 查询所有的game资源
     * @return 返回全部game资源的list
     */
    List<GamePojo> selectGames();

    /**
     * 通过模糊查询game的名称，进行筛选的game资源
     * @param text 需要进行模糊查询的文本
     * @return 返回筛选后的mmd资源的list
     */
    List<GamePojo> selectGamesByFuzzyText(String text);

    /**
     * 通过game的id查询一个game资源
     * @param id 需要查询的id
     * @return 返回查询到的game资源
     */
    GamePojo selectGameById(Integer id);


    /**
     * 添加一个game资源
     * @param gamePojo 需要添加的mmd的实体类
     * @return 返回boolean，true为成功，false为失败
     */
    boolean addGame(GamePojo gamePojo);

    /**
     * 更新一个game资源
     * @param gameId 需要更新的id
     * @param newName 新的mmd名字
     * @return 返回boolean，true为成功，false为失败
     */
    boolean updateGameName(Integer gameId, String newName);

    /**
     * 更新一个game资源
     * @param gameId 更新的内容
     * @param newIntroduction 新的简介
     * @return 返回boolean，true为成功，false为失败
     */
    boolean updateGameIntroduction(Integer gameId, String newIntroduction);

    /**
     * 删除一个game资源
     * @param id 需要删除的mmd的id
     * @return 返回boolean，true为成功，false为失败
     */
    boolean delGame(Integer id);
}
