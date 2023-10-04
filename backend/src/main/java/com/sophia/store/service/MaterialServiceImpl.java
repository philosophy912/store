package com.sophia.store.service;

import com.philosophy.base.util.StringsUtils;
import com.sophia.store.entity.po.Material;
import com.sophia.store.entity.vo.MaterialVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MaterialServiceImpl extends BaseService implements MaterialService {

    private MaterialVo convert(Material material) {
        MaterialVo vo = new MaterialVo();
        vo.setId(material.getId());
        vo.setName(material.getName());
        vo.setCapacity(material.getCapacity());
        vo.setPrice(material.getPrice());
        vo.setPricePerUnit(material.getPricePerUnit());
        vo.setUnit(material.getUnit());
        return vo;
    }


    @Override
    public List<MaterialVo> findAll() {
        List<MaterialVo> materialVos = new ArrayList<>();
        List<Material> materials = materialDao.findAll();
        materials.forEach(material -> materialVos.add(convert(material)));
        return materialVos;
    }

    @Override
    public List<MaterialVo> find(Pageable pageable, String name) {
        List<MaterialVo> materialVos = new ArrayList<>();
        Page<Material> materials = materialDao.findAll((Specification<Material>) (root, query, criteriaBuilder) -> {
            // 1. 创建集合 存储查询条件
            List<Predicate> queryList = new ArrayList<>();
            // 2. 添加查询条件
            if (StringsUtils.isNotEmpty(name)) {
                queryList.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            query.where(queryList.toArray(new Predicate[0]));
            return null;
        }, pageable);
        materials.forEach(material -> materialVos.add(convert(material)));
        return materialVos;
    }

    @Override
    public long findAllCount() {
        return materialDao.count();
    }

    @Override
    public long findCountByName(String name) {
        return materialDao.findByNameLike(name).size();
    }

    @Override
    public MaterialVo add(MaterialVo vo) {
        List<Material> materials = materialDao.findByName(vo.getName());
        if (materials.isEmpty()) {
            Material material = new Material();
            material.setCapacity(vo.getCapacity());
            material.setPrice(vo.getPrice());
            // 计算单位价格
            material.setPricePerUnit(vo.getPrice() / vo.getCapacity());
            material.setUnit(vo.getUnit());
            material.setName(vo.getName());
            Material dpt = materialDao.saveAndFlush(material);
            return convert(dpt);
        }
        return null;
    }

    @Override
    public MaterialVo update(MaterialVo vo) {
        Optional<Material> optionalMaterial = materialDao.findById(vo.getId());
        Material material = optionalMaterial.orElseGet(optionalMaterial::get);
        material.setCapacity(vo.getCapacity());
        material.setPrice(vo.getPrice());
        material.setUnit(vo.getUnit());
        material.setName(vo.getName());
        material.setPricePerUnit(material.getPrice()/material.getCapacity());
        Material dpt = materialDao.saveAndFlush(material);
        return convert(dpt);
    }

    @Override
    public MaterialVo delete(MaterialVo vo) {
        int id = vo.getId();
        log.debug("material id = " + id);
        Optional<Material> optionalMaterial = materialDao.findById(id);
        Material material = optionalMaterial.orElseGet(optionalMaterial::get);
        materialDao.delete(material);
        return vo;
    }
}
