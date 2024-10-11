package com.zkxy.shop.ui.goods

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.zkxy.shop.entity.goods.AddressBookEntity
import com.zkxy.shop.entity.goods.JsonBean
import com.zkxy.shop.entity.goods.PickerEntity
import com.zkxy.shop.entity.goods.PlaceOrderEntity
import com.zkxy.shop.ext.getJson
import com.zkxy.shop.network.request.apiService
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job
import org.json.JSONArray

class PlaceOrderViewModel : BaseViewModel() {

    val pickerData = MutableLiveData<PickerEntity>()
    val placeOrderEntity = MutableLiveData<PlaceOrderEntity>()
    val deliveryAddressListEntity = MutableLiveData<MutableList<AddressBookEntity>>()
    val editAddress = MutableLiveData<Boolean>()
    val createOrderSuccess = MutableLiveData<Boolean>()

    fun initJsonData(context: Context) {
        request<Job>(block = {
            loadingChange.showDialog.value = ""
            val options2Items = ArrayList<ArrayList<String>>()
            val options3Items = ArrayList<ArrayList<ArrayList<String>>>()
            val json = getJson(context, "province.json")
            val options1Items: ArrayList<JsonBean> = ArrayList()
            val data = JSONArray(json)
            val gson = Gson()
            for (i in 0 until data.length()) {
                val entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean::class.java)
                options1Items.add(entity)
            }

            for (i in 0 until options1Items.size) { //遍历省份
                val cityList = ArrayList<String>() //该省的城市列表（第二级）
                val province_AreaList = ArrayList<ArrayList<String>>() //该省的所有地区列表（第三极）

                for (c in 0 until options1Items[i].cityList.size) { //遍历该省份的所有城市
                    val cityName: String = options1Items[i].cityList[c].name
                    cityList.add(cityName) //添加城市
                    val city_AreaList = ArrayList<String>() //该城市的所有地区列表

                    //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                    /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                    city_AreaList.addAll(options1Items[i].cityList[c].area)
                    province_AreaList.add(city_AreaList) //添加该省所有地区数据
                }

                /**
                 * 添加城市数据
                 */
                options2Items.add(cityList)

                /**
                 * 添加地区数据
                 */
                options3Items.add(province_AreaList)
            }
            pickerData.value = PickerEntity(
                options1Items = options1Items,
                options2Items = options2Items,
                options3Items = options3Items
            )
            loadingChange.dismissDialog.value = true
        }, error = {
            loadingChange.dismissDialog.value = true
        })
    }

    fun goodsStockAddressSearch(goodsId: Int, deliveryMode: Int) {
        request<Job>(block = {
            loadingChange.showDialog.value = ""
            placeOrderEntity.value =
                apiService.goodsStockAddressSearch(goodsId = goodsId, deliveryMode = deliveryMode)
                    .apiData()
            loadingChange.dismissDialog.value = true
        }, error = {
            loadingChange.dismissDialog.value = true
        })
    }

    fun getUserAddress() {
        request<Job>(block = {
            deliveryAddressListEntity.value = apiService.getUserAddress().apiData()
        }, error = {
        })
    }

    fun acquiesceAddress(addressId: Int?, acquiesce: Int) {
        request<Job>(block = {
            apiService.acquiesceAddress(addressId = addressId, acquiesce = acquiesce)
            editAddress.value = true
        }, error = {
        })
    }

    fun deleteAddress(addressId: Int?) {
        request<Job>(block = {
            apiService.deleteAddress(addressId = addressId)
            editAddress.value = true
        }, error = {
        })
    }

    fun createOrder(
        consignee: String?,
        consigneeTel: String?,
        goodsId: Int?,
        goodsNum: Int?,
        goodsSpecId: Int?,
        deliveryType: Int?,
        deliveryAddress: String? = null
    ) {
        request<Job>(block = {
            loadingChange.showDialog.value = ""
            apiService.createOrder(
                deliveryAddress = deliveryAddress,
                consignee = consignee,
                consigneeTel = consigneeTel,
                goodsId = goodsId,
                goodsNum = goodsNum,
                goodsSpecId = goodsSpecId,
                deliveryType = deliveryType
            ).apiData()
            createOrderSuccess.value = true
            loadingChange.dismissDialog.value = true
        }, error = {
            loadingChange.dismissDialog.value = true
        })
    }

}