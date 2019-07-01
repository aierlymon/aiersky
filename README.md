# aiersky
该工程结合 rxjava+mvp+retrofit进行网络请求,通过butterKnife绑定view和对象，优化了rxjava的内存泄漏问题，而且BaseActivity添加了eventBus事件的使用和状态栏颜色的改变
还有keyboard的展示和隐藏，带有leakcanary进行内存检测，BasePresenter也有事件总线方便数据传输,除此之外还有glide框架进行图片加载，rxpermisssion进行
权限设置，还添加了tcp客户端（client）的连接和收发消息

<h1>baselib</h1>
这个就是rxjava+mvp+retrofit+eventbus+glide+butterknife+leakcanary

<h1>mytcpandws</h1>
这个就是tcp的封装包</br>
ps:要启动是否有网络时候可以调用这个包里面的一个网络状态广播
