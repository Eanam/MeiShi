package com.meishi.services;

import com.meishi.infobean.CollectShopInfo;
import com.meishi.infobean.CommentInfo;
import com.meishi.infobean.GoodInfo;
import com.meishi.infobean.LoginAndRegistInfo;
import com.meishi.infobean.MeInfo;
import com.meishi.infobean.PrivateComInfo;
import com.meishi.infobean.ShopInfo;
import com.meishi.infobean.SubmitInfo;
import com.meishi.infobean.UploadPicInfo;


import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface InfoService {

    /**
     * 登陆
     */
    @POST("user/login.action")
    @FormUrlEncoded
    Observable<LoginAndRegistInfo> login( @Field("username") String username, @Field("password") String password);

    /**
     * 注册
     */
    @POST("user/register.action")
    @FormUrlEncoded
    Observable<LoginAndRegistInfo> regist(@Field("username")String username,@Field("password")String password);


    /**
     * 获取店铺信息
     * @return
     */
    @POST("shop/list.action")
    Call<ShopInfo> getShopList();

    /**
     * 获取收藏店铺信息
     * @return
     */
    @POST("shop/fav.action")
    @FormUrlEncoded
    Call<CollectShopInfo> getCollectShopList(@Field("access_token")String token);

    /**
     * 获取收藏店铺信息,仅供欢迎界面使用
     * @return
     */
    @POST("shop/fav.action")
    @FormUrlEncoded
    Observable<CollectShopInfo> getCollectShop(@Field("access_token")String token);


    /**
     * 获取店铺菜品
     */
    @POST("shop/getgoods.action")
    @FormUrlEncoded
    Call<GoodInfo> getGoodList(@Field("access_token")String token, @Field("shopid")int shop_id);


    /**
     * 获取商品评论
     */
    @POST("shop/getcomment.action")
    @FormUrlEncoded
    Call<CommentInfo> getCommentList(@Field("access_token")String token, @Field("goodid")int good_id);

    /**
     * 提交评论
     */
    @POST("user/comment.action")
    @FormUrlEncoded
    Observable<SubmitInfo> submitComment(@Field("access_token")String token,@Field("comtime")String date,@Field("shopid")int shopId,
                                         @Field("goodid")int goodid,@Field("star")float star,@Field("comdesc")String comdesc);


    /**
     * 取消评论
     */
    @POST("user/cancelfav.action")
    @FormUrlEncoded
    Observable<SubmitInfo> cancelCollect(@Field("access_token")String token,@Field("shopid")int shopId);

    /**
     * 提交收藏店铺user/addfav.action
     */
    @POST("user/addfav.action")
    @FormUrlEncoded
    Observable<SubmitInfo> submitCollection(@Field("access_token")String token,@Field("shopid")int shopId);


    /**
     * 上传照片
     */
    @POST("filesUpload/upload")
    @Multipart
    Observable<UploadPicInfo> uploadPic(@Part MultipartBody.Part file);


    /**
     * 获取个人资料
     */
    @POST("user/info.action")
    @FormUrlEncoded
    Call<MeInfo> getProfile(@Field("access_token")String token);

    /**
     * 修改个人资料
     * @param token
     * @param headpicurl
     * @return
     */

    //修改头像
    @POST("user/edit.action")
    @FormUrlEncoded
    Observable<SubmitInfo> editHeadPic(@Field("access_token")String token, @Field("headpicurl")String headpicurl);

    //修改名字
    @POST("user/edit.action")
    @FormUrlEncoded
    Observable<SubmitInfo> editNickname(@Field("access_token")String token,@Field("nickname")String nickname);
    //修改性别
    @POST("user/edit.action")
    @FormUrlEncoded
    Observable<SubmitInfo> editSex(@Field("access_token")String token,@Field("sex")int sex);
    //修改生日
    @POST("user/edit.action")
    @FormUrlEncoded
    Observable<SubmitInfo> editBirthdate(@Field("access_token")String token,@Field("birthdate")String birthdate);
    //修改个性签名
    @POST("user/edit.action")
    @FormUrlEncoded
    Observable<SubmitInfo> editDesc(@Field("access_token")String token,@Field("description")String desc);


    /**
     * 获取历史评论
     */
    @POST("user/getCom.action")
    @FormUrlEncoded
    Call<PrivateComInfo> getPrivateCom(@Field("access_token")String token);

    /**
     * 删除评论
     */
    @POST("user/delCom.action")
    @FormUrlEncoded
    Observable<SubmitInfo> cancelCom(@Field("access_token")String token,@Field("comid")int comid);
}
