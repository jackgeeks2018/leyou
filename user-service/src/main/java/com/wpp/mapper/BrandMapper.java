package com.wpp.mapper;

import com.wpp.entity.Brand;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 品牌表，一个品牌下有多个商品（spu），一对多关系 Mapper 接口
 * </p>
 *
 * @author Mht
 * @since 2019-10-09
 */
@Mapper
public interface BrandMapper extends BaseMapper<Brand> {

    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);
    @Delete("DELETE FROM  tb_category_brand WHERE brand_id=#{bid}")
    int deleteCategoryBrand(@Param("bid") Long bid);

}
