package com.example.o2o.service.impl;

import com.example.o2o.constants.ConfigureConstant;
import com.example.o2o.dao.ProductDao;
import com.example.o2o.dao.ProductImgDao;
import com.example.o2o.dto.CodeMsg;
import com.example.o2o.dto.ImageHolder;
import com.example.o2o.dto.ProductExecution;
import com.example.o2o.entity.Product;
import com.example.o2o.entity.ProductImg;
import com.example.o2o.exceptions.ProductOperationException;
import com.example.o2o.service.ProductService;
import com.example.o2o.util.FileUploadUtil;
import com.example.o2o.util.ImageUtil;
import com.example.o2o.util.PageCalculator;
import com.example.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.service.impl
 * @date:2019/10/9
 **/
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;

    //1、处理缩略图，获取缩略图相对路径并赋值给Product
    //2、往tb_product写入商品信息，获取ProductId
    //3、结合productId批量处理商品详情图
    //4、将商品详情图列表批量插入tb_product_img中
    @Override
    @Transactional
    public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList) throws ProductOperationException {
        //空值判断
        if(product !=null && product.getShop()!=null &&product.getShop().getShopId()!=null){
            //给商品设置创建日期和更新日期
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            //默认为上架的状态
            product.setStatus(1);
            //若商品缩略图不为空，则添加
            if (thumbnail!=null)
                addThumbnail(product,thumbnail);
            try{
                //创建商品信息
                int effectedNum = productDao.insertProduct(product);
                if (effectedNum<0)
                    throw new  ProductOperationException("创建商品失败");
            }catch (Exception e){
                    throw new  ProductOperationException("创建商品失败:"+e.toString());
            }
            //若商品详情图不为空，则添加
            if (productImgHolderList !=null && productImgHolderList.size()>0)
                addProductImgList(product,productImgHolderList);
            return new ProductExecution(CodeMsg.PRODUCT_ADD_SUCCESS,product);
        }else{
            //传参为空则返回空值错误信息
            return new ProductExecution(CodeMsg.PRODUCT_ADD_EMPTY);
        }
    }

    @Override
    public Product getProductById(long productId) {
        return productDao.queryProductById(productId);
    }

    @Override
    @Transactional
    //1、若缩略图有值，则处理缩略图
    //若原先存在缩略图则先删除再添加新图，之后获取缩略图相对路径并赋值给product
    //2、若商品详情图列表参数有值，对商品详情图列表进行同样的操作
    //3、将tb_product_img下面该商品原先的商品详情图记录全部清除
    //4、更新tb_product
    public ProductExecution modifyProduct(Product product, ImageHolder imageHolder, List<ImageHolder> productImages) throws ProductOperationException {
        //空值判断
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null){
            //给商品设置上默认属性
            product.setLastEditTime(new Date());
            //若商品缩略图不为空且原有缩略图不为空则先删除再添加
            if (imageHolder != null){
                //先获取一遍原有信息，取到原图片地址
                Product originalProduct = productDao.queryProductById(product.getProductId());
                if (originalProduct.getImgAddr() != null)
                    ImageUtil.deleteFileOrPath(originalProduct.getImgAddr());
                addThumbnail(product,imageHolder);
            }
            //若果有新存入的商品详情图，则将原先的删除，并添加新的图片
            if (productImages != null && productImages.size() > 0){
                deleteProductImages(product.getProductId());
                addProductImgList(product,productImages);
            }
            try {
                //更新商品信息
                int effectNum = productDao.updateProduct(product);
                if (effectNum <= 0)
                    throw new ProductOperationException("更新商品信息失败");
                return new ProductExecution(CodeMsg.PRODUCT_UPDATE_SUCCESS,product);
            }catch (Exception e){
                throw new ProductOperationException(CodeMsg.PRODUCT_UPDATE_SUCCESS+e.toString());
            }
        }else {
            return new ProductExecution(CodeMsg.PARAMETER_ISNULL);
        }
    }

    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        //页码转换成数据库的行码，并调用dao层取出指定页码的商品列表
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
        List<Product> productList = productDao.queryProductList(productCondition,rowIndex,pageSize);
        //基于同样的查询条件返回该条件下的商品总数
        int count = productDao.queryProductCount(productCondition);
        ProductExecution pe = new ProductExecution();
        pe.setProductList(productList);
        pe.setCount(count);
        return pe;
    }

    @Override
    public int getProductCount(Product productCondition) {
        return productDao.queryProductCount(productCondition);
    }

    //删除某商品下的所有详情图片
    private void deleteProductImages(Long productId) {
        //根据productId获取原来的图片
        List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
        //删除原来的图片
        for (ProductImg productImg : productImgList)
            ImageUtil.deleteFileOrPath(productImg.getImgAddr());
        //删除数据库中原图片信息
        productImgDao.deleteProductImgByProductId(productId);
    }

    //批量添加图片
    private void addProductImgList(Product product, List<ImageHolder> productImgHolderList) {
        //获取图片存储路径
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgList = new ArrayList<>();
        //遍历图片一次去处理，并添加进productImg实体类里
        for (ImageHolder productImgHolder:productImgHolderList){
            String imgAddr;
            if (ConfigureConstant.isDeployLocally){
                imgAddr = ImageUtil.generateThumbnail(productImgHolder,dest);
            }else {
                imgAddr = FileUploadUtil.uploadFile(productImgHolder,product.getShop());
            }
            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(imgAddr);
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new Date());
            productImgList.add(productImg);
        }
        //如果确实是有图片需要添加的，就执行批量添加操作
        if (productImgList.size()>0){
            try {
                int effectedNum = productImgDao.batchInsertProductImg(productImgList);
                if (effectedNum<0)
                    throw new ProductOperationException("创建商品详情图片失败");
            }catch (Exception e){
                throw  new ProductOperationException("创建商品详情图失败："+e.toString());
            }
        }
    }

    //添加缩略图
    private void addThumbnail(Product product, ImageHolder thumbnail) {
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        String thumbnailAddr;
        if (ConfigureConstant.isDeployLocally){
            thumbnailAddr = ImageUtil.generateThumbnail(thumbnail,dest);
        }else {
            thumbnailAddr = FileUploadUtil.uploadFile(thumbnail,product.getShop());
        }
        product.setImgAddr(thumbnailAddr);
    }
}
