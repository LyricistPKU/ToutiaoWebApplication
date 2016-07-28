# ToutiaoWebApplication
项目实现功能：
注册、登录、评论、图片的云存储、站内信互发、异步赞踩、登录邮件通知（异步）
项目未完成：
用户头像上传、注册信息的详细化（需要改数据库），点击更多（load more），新闻排序（目前登录首页新闻按照时间排序），
对评论的评论，用户等级积分...

1.	拦截器：
（implements HandlerInterceptor; override preHandle, postHandle, afterCompletion）
PassportInterceptor：从Cookie中寻找ToutiaoWebApplication所发放的登录Cookie，校验是否有效（status）和是否过期（expired），符合条件则取出用户信息，向hostholder中添加这个user
LoginRequiredInterceptor: 在消息，头条页面进入controller层之前检查是否已经登录，页面上方显示登录按钮或用户姓名。
Hostholder：维护一个ThreadLocal<User>

2.	模型定义
News（新闻）：id,title,link,image,like_count,comment_count,created_date,user_id
User（用户）：id,name,password,salt,head_url
密码由随机生成的salt和pwd经过MMD5加密得到,用户email字段和头像上传的前后端交互未完成
Message（消息、站内信）：id,from_id,to_id,created_date,has_read,conversation_id
Login_ticket（网站登录的Cookie）:id,user_id,ticket,expired,status
Comment（评论）：id,content,user_id,entity_id（评论对象的id）,entity_type（评论对象的类型）,created_date,status      

3.	切面编程AOP
@Before("execution(* com.lyubinbin.controller.*Controller.*(..))")
@After("execution(* com.lyubinbin.controller.*Controller.*(..))")
在controller层的所有函数执行之前与之后执行切面的代码
log出Joitpoint的信息

4.	DAO层
用MyBatis提供的JDBC接口实现Service层与数据库的交互

4.	Service层——提供操作DAO层进而操作数据库的接口
CommentService：获取、增加、删除评论
LikeService：点赞、点踩（Redis数据库实现，根据赞踩对象的类型和id构造key）
MessageService：获取对话信息，未读数量等
NewsService：获取最新新闻，添加新闻，更新评论数
QiniuService：用七牛云来实现图片的上传、存储和缩放
UserService：获取用户信息

5.	Controller层
HomeController：/index, /user/{userId}，访问默认或自己的首页，访问别人首页
LoginController：/reg, /login, /logout，注册，登录，登出
MessageController：/msg/list, /msg/detail（进入detail页面后清除相关未读信息）, /msg/addmessage，个人站内信列表，具体对话框，发送站内信（表单post提交）
NewsController：/news/{newsId}, /user/assNews, /uploadImage, /image, /addComment，新闻的首页、具体页、上传图片，发表评论
LikeController：/like, /dislike，赞踩功能，json AJAX交互

6.	异步队列——赞踩，登录时系统发送邮件
event producer -> event list -> event consumer(deliver event to different event handlers)
——producer将事件转换为JSON格式压入redis队列
——首先为每个handler定义处理的事件类型
——consumer利用Spring自带框架找出所有handler并维护一张事件对应的handler（可一对多）的map
——redis的brpop阻塞读出队列逐个分发给相关的handler
———————考虑事件的优先级来设计Consumer的读出顺序
