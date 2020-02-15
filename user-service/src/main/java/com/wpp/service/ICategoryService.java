package com.wpp.service;

import com.wpp.entity.Category;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 商品类目表，类目和商品(spu)是一对多关系，类目与品牌是多对多关系 服务类
 * </p>
 *
 * @author Mht
 * @since 2019-10-09
 */
public interface ICategoryService extends IService<Category> {

    List<Category> queryCategoryBypid(long pid);

    List<Category> queryByBrandId(Long bid);
}
