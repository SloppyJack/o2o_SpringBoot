package com.example.o2o.service.impl;

import com.example.o2o.constants.ConfigureConstant;
import com.example.o2o.dao.ShopDao;
import com.example.o2o.dto.CodeMsg;
import com.example.o2o.dto.ImageHolder;
import com.example.o2o.dto.ShopExecution;
import com.example.o2o.entity.PersonInfo;
import com.example.o2o.entity.Shop;
import com.example.o2o.exceptions.ShopOperationException;
import com.example.o2o.service.ShopService;
import com.example.o2o.util.FileUploadUtil;
import com.example.o2o.util.ImageUtil;
import com.example.o2o.util.PageCalculator;
import com.example.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.service.impl
 * @date:2019/7/25
 **/
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;


    @Override
    public ShopExecution getShopListOfPage(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
        List<Shop> shopList = shopDao.queryShopListOfPage(shopCondition,rowIndex,pageSize);
        int count = shopDao.queryShopCountByShopCondition(shopCondition);
        ShopExecution se = new ShopExecution();
        if (shopList != null){
            se.setShopList(shopList);
            se.setCount(count);
            se.setState(CodeMsg.SHOP_OPERATION_SUCCESS.getRetCode());
            se.setStateInfo(CodeMsg.SHOP_OPERATION_SUCCESS.getMessage());
        }else {
            se.setState(CodeMsg.SERVER_EXCEPTION.getRetCode());
        }
        return se;
    }

    /**
     *根据Id查询店铺
     * @param shopId 店铺id
     * @return
     */
    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    /**
     * 修改店铺信息
     * @param shop 店铺类
     * @param imageHolder 图片持有者对象
     * @return ShopExecution
     * @throws ShopOperationException 商铺操作异常
     */
    @Override
    @Transactional
    public ShopExecution modifyShop(Shop shop, ImageHolder imageHolder) throws ShopOperationException {
        if (shop == null || shop.getShopId() == null){
            return new ShopExecution(CodeMsg.NULL_SHOP);
        }else {
            //1、判断是否需要处理图片
            try{
                if (imageHolder.getImage() !=null && imageHolder.getImageName() != null && !"".equals(imageHolder.getImageName())){
                    Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                    //删除原来的图片
                    if (tempShop.getShopImg()!=null){
                        ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                    }
                    addShopImg(shop,imageHolder);
                }
                //2、更新店铺信息
                shop.setLastEditTime(new Date());
                int effectedNum = shopDao.updateShop(shop);
                if (effectedNum <= 0){
                    return new ShopExecution(CodeMsg.SERVER_EXCEPTION);
                }else {
                    shop = shopDao.queryByShopId(shop.getShopId());
                    return new ShopExecution(CodeMsg.SHOP_OPERATION_SUCCESS,shop);
                }
            }catch (Exception e){
                throw new ShopOperationException("modifyShop error:"+ e.getMessage());
            }
        }
    }

    /**
     * 添加店铺
     * @param shop  店铺类
     * @param imageHolder 图片持有者对象
     * @return ShopExecution
     */
    @Override
    @Transactional
    public ShopExecution addShop(Shop shop,ImageHolder imageHolder) {
        if (shop==null){
            return new ShopExecution(CodeMsg.NULL_SHOP);
        }
        try{
            shop.setStatus(CodeMsg.SHOP_STATUS_CHECK.getRetCode());
            shop.setAdvice(CodeMsg.SHOP_STATUS_CHECK.getMessage());
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            PersonInfo personInfo = new PersonInfo();
            personInfo.setUserId(1L);
            shop.setOwner(personInfo);
            int effectNum = shopDao.insertShop(shop);
            if (effectNum<=0){
                //tips:仅RuntimeException才会导致事务回滚
                throw new ShopOperationException("店铺创建失败");
            }else {
                if (imageHolder.getImage() !=null){
                    try{
                        if (ConfigureConstant.isDeployLocally){
                            //如在本地部署，则存储图片
                            addShopImg(shop,imageHolder);
                        }else {
                            String imageAddr = FileUploadUtil.uploadFile(imageHolder,shop);
                            shop.setShopImg(imageAddr);
                        }
                    }catch (Exception e){
                        throw new ShopOperationException("addShopImg error:"+e.getMessage());
                    }
                    //更新店铺的图片地址
                    effectNum = shopDao.updateShop(shop);
                    if (effectNum<=0){
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }
            }
        }catch (Exception e){
            throw new ShopOperationException("addShop error"+e.getMessage());
        }

        return new ShopExecution(CodeMsg.SHOP_STATUS_CHECK,shop);
    }

    private void addShopImg(Shop shop,ImageHolder imageHolder) {
        //获取shop图片目录的相对值路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(imageHolder,dest);
        shop.setShopImg(shopImgAddr);
    }
}
