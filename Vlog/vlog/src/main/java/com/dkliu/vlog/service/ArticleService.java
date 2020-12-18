package com.dkliu.vlog.service;

import com.dkliu.vlog.model.entity.Article;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName ArticleService
 * @Description TODO
 * @Author Hinoki
 * @Date 2020/12/15
 **/

public interface ArticleService {
    /**
     * 批量新增文章
     *
     * @param articles 文章集合
     */
    void insertArticles(List<Article> articles);

    /**
     * 查询推荐的6篇文章
     *
     * @param userId 用户id
     * @return List<Article>
     */
    List<Article> getRecommendArticles(@Param(value = "userId") int userId);

    /**
     * 根据用户id查找数并分页
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param userId 用户id
     * @return 返回结果
     */
    PageInfo<Article> selectByPage(int pageNum, int pageSize, int userId);

    /**
     * 根据文章id查找文章详情
     *
     * @param id 文章id
     * @return Article详情
     */
    Article getDetail(@Param(value = "id") String id);
}
