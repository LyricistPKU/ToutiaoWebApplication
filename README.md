# ToutiaoWebApplication

1.	拦截器：
（implements HandlerInterceptor; override preHandle, postHandle, afterCompletion）
PassportInterceptor：从Cookie中寻找ToutiaoWebApplication所发放的登录Cookie，校验是否有效（status）和是否过期（expired），符合条件则取出用户信息，向hostholder中添加这个user
LoginRequiredInterceptor: 在消息，头条页面进入controller层之前检查是否已经登录，页面上方显示登录按钮或用户姓名。
Hostholder：维护一个ThreadLocal<User>

2.	模型定义
News         				User						Message								Login_ticket   		Comment
Id           				Id							Id										Id								Id
Title        				Name						From_id								User_id			      Content
Link         				Password				To_id									Ticket						User_id
Image        				Salt						Created_date					Expired						Entity_id
Like_count   				Head_url				Has_read							status						Entity_type
Comment_count												Conversation_id													Created_date
Created_date 																																status
User_id      

3.	切面编程AOP
@Before("execution(* com.lyubinbin.controller.*Controller.*(..))")
@After("execution(* com.lyubinbin.controller.*Controller.*(..))")
log出Joitpoint的信息

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
MessageController：/msg/list, /msg/detail, /msg/addmessage，个人站内信列表，具体对话框，发送站内信
NewsController：/news/{newsId}, /user/assNews, /uploadImage, /image, /addComment，新闻的首页、具体页、上传图片，发表评论
LikeController：/like, /dislike，赞踩功能，json AJAX交互

6.	异步队列——赞踩，登录时系统发送邮件
event producer -> event list -> event consumer(deliver event to different event handlers)
