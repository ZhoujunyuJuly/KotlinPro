package com.example.coroutine.realPro.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.coroutine.realPro.db.AppDatabase
import com.example.coroutine.realPro.model.CarBrandEntity
import com.example.coroutine.realPro.model.CommonConstant
import com.example.coroutine.realPro.net.isConnectedNetwork

/**
 * Mediator
 *
 * ‼️Paging 是一直从数据库的 PagingSource 读取数据的
 * ‼️RemoteMediator 只是用来更新数据库数据。
 * 不论你 RemoteMediator 返回什么，UI 总是通过数据库查询流拿数据。
 *
 * RemoteMediator 的结果 🌟只是用来告知是否需要继续加载网络，控制“加载状态”和“是否触底”。
 *
 * 📒这里也可以用@Inject实现，同时在 Repository 里增加 Mediator 的入参就可以直接用
 */
@OptIn(ExperimentalPagingApi::class)
class CarBrandMediator(
    private val api : CarBrandService,
    private val database:AppDatabase) : RemoteMediator<Int,CarBrandEntity>(){

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CarBrandEntity>
    ): MediatorResult {
        try {
            /**
             * 🌟第一步，判断LoadType,根据LoadType计算当前页
             */
            val page  = when(loadType){
                //首次访问，页码为空
                LoadType.REFRESH -> null
                //向下滑动到顶部，加载上一页的数据，如旧历史记录
                LoadType.PREPEND -> return MediatorResult.Success(true)
                //向上滑动到底部，更多加载
                LoadType.APPEND -> {
                    val lastItem =
                        state.lastItemOrNull() ?: return MediatorResult.Success(true)
                    lastItem.page
                }
            }?:0


            if(!CommonConstant.mContext.isConnectedNetwork()){
                return MediatorResult.Success(true)
            }

            /**
             * 🌟第二步，请求网络分页数据
             */
            val result = api.fetchData(page * state.config.pageSize, state.config.pageSize)
            val databaseResult = result.map {
                CarBrandEntity(
                    id = it.id,
                    name = it.name,
                    icon = it.icon?:"",
                    page = page + 1
                )
            }

            /**
             * 🌟第三步，插入数据库
             */
            val carBrandDao = database.carBrandDao()
            database.withTransaction{
                //数据库保持事务性操作，确保清除和插入操作同时成功或失败
                if( loadType == LoadType.REFRESH){
                    carBrandDao.clear()
                }
                carBrandDao.insertCarBrand(databaseResult)
            }

            //Success的参数决定是否继续调用 RemoteMediator.load()方法
            return MediatorResult.Success(result.isEmpty())

        }catch (e :Exception){
            return MediatorResult.Error(e)
        }

    }
}