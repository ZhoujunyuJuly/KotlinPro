package com.example.server.realPro.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.server.realPro.db.AppDatabase
import com.example.server.realPro.mapper.CarEntity2ItemModel
import com.example.server.realPro.model.CarBrandItemModel
import com.example.server.realPro.remote.CarBrandMediator
import com.example.server.realPro.remote.CarBrandService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

/**
 * TODO 这个实现可以更简化，使用@Inject constructor
 */
class CarBrandRepositoryImp(
    private val api:CarBrandService,
    private val database: AppDatabase,
    private val carEntity2ItemModel: CarEntity2ItemModel
) : Repository{

    @OptIn(ExperimentalPagingApi::class)
    override fun fetchCarBrandList(): Flow<PagingData<CarBrandItemModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 12,
                //距离底部还有多少距离时，开始预加载下一页
                prefetchDistance = 5,
                //预加载数据，一般为 pageSize*3
                initialLoadSize = 16
            ),
            //1.请求网络数据，放到数据库
            remoteMediator = CarBrandMediator(api,database)
        ){
            //2.从数据库取出数据
            database.carBrandDao().getCarBrand()
        }
        /**
         *  它的源码内部调用了 PagingSource.load() 然后将结果封装成 PagingData，再转换为 Flow。
         *  public fun <Key : Any, Value : Any> Pager<Key, Value>.flow: Flow<PagingData<Value>>
         *
         */
            .flow
            .flowOn(Dispatchers.IO)
            .map {  pagingData ->
            pagingData.map {
                //3.数据库类型转换，转成UI需要的类型
                carEntity2ItemModel.map(it)
            }
        }
    }
}