

### 核心概念

* **RS(ReplicaSet)** 
    * 用户期望的pod数量
    * 标签选择器.判断哪个pod归属自己管理
    * 当现存pod数量不够时,根据pod资源模板进行新建
* **Deployment**
  * 管理无状态应用
  * 管理Pod和ReplicaSet
  * 具有上线部署、副本设定、滚动升级、回滚等功能
  * 应用场景：web服务
* **StatefulSet**
  * 管理有状态应用
  * 保持Pod启动顺序和唯一性
  * 优雅的部署和扩展、删除和终止（例如：mysql主从关系，先启动主，再启动从）
  * 应用场景：数据库
* **DaemonSet**
  * 确保集群中的每一个节点都运行这个特定的pod
* **Job&CronJob**
  * Job只要完成就立即退出，不需要重启或重建
  * CronJob其实就是在Job的基础上加上了时间调度
  * kk

      
          






