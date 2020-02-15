package com.wpp.service;

import com.wpp.entity.Brand;
import com.baomidou.mybatisplus.service.IService;
import com.wpp.utils.PageResult;

import java.util.List;

/**
 * <p>
 * 品牌表，一个品牌下有多个商品（spu），一对多关系 服务类
 * </p>
 *
 * @author Mht
 * @since 2019-10-09
 */
public interface IBrandService extends IService<Brand> {

     PageResult<Brand> queryBrandByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key);

    void saveBrand(Brand brand, List<Long> cids);

    void updateBrand(Brand brand, List<Long> cids);

    void deleteBrand(long id);
}
