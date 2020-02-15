package com.wpp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wpp.entity.Brand;
import com.wpp.mapper.BrandMapper;
import com.wpp.service.IBrandService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wpp.utils.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 品牌表，一个品牌下有多个商品（spu），一对多关系 服务实现类
 * </p>
 *
 * @author Mht
 * @since 2019-10-09
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {


    @Override
    public PageResult<Brand> queryBrandByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        // 开始分页

        Page<Brand> p= new Page<>(page,rows);
        Wrapper<Brand> wrapper=new EntityWrapper<>();
        if (StringUtils.isNotBlank(key)) {
            wrapper.like(true,"name",key).or("letter",key);

        }
        if (StringUtils.isNotBlank(sortBy)) {

            wrapper.orderBy(desc,"name");
        }


        Page<Brand> brandPage = super.selectPage(p, wrapper);



        // 返回结果
        return new PageResult<>(brandPage.getTotal(), brandPage.getRecords());
    }

    @Transactional
    @Override
    public void saveBrand(Brand brand, List<Long> cids) {
        // 新增品牌信息
             super.insert(brand);
        // 新增品牌和分类中间表
        for (Long cid : cids) {
            baseMapper.insertCategoryBrand(cid, brand.getId());
        }
    }

    @Override
    @Transactional
    public void updateBrand(Brand brand, List<Long> cids) {
        // 更新品牌信息
        super.updateById(brand);
        //删除品牌和分类中间表
        baseMapper.deleteCategoryBrand(brand.getId());
        // 新增品牌和分类中间表
        for (Long cid : cids) {
            baseMapper.insertCategoryBrand(cid, brand.getId());
        }

    }

    @Override
    @Transactional
    public void deleteBrand(long id) {
        baseMapper.deleteCategoryBrand(id);
        super.deleteById(id);

    }
}
