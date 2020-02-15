package com.wpp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.wpp.entity.Category;
import com.wpp.mapper.CategoryMapper;
import com.wpp.service.ICategoryService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * <p>
 * 商品类目表，类目和商品(spu)是一对多关系，类目与品牌是多对多关系 服务实现类
 * </p>
 *
 * @author Mht
 * @since 2019-10-09
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Override
    public List<Category> queryCategoryBypid(long pid) {
        Wrapper<Category> wrapper=new EntityWrapper<>();

        wrapper.eq("parent_id",pid);
        List<Category> categories = super.selectList(wrapper);
        return categories;
    }

    @Override
    public List<Category> queryByBrandId(Long bid) {
        return this.baseMapper.queryByBrandId(bid);
    }
}
