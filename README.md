# ToutiaoWebApplication

1.	��������
��implements HandlerInterceptor; override preHandle, postHandle, afterCompletion��
PassportInterceptor����Cookie��Ѱ��ToutiaoWebApplication�����ŵĵ�¼Cookie��У���Ƿ���Ч��status�����Ƿ���ڣ�expired��������������ȡ���û���Ϣ����hostholder��������user
LoginRequiredInterceptor: ����Ϣ��ͷ��ҳ�����controller��֮ǰ����Ƿ��Ѿ���¼��ҳ���Ϸ���ʾ��¼��ť���û�������
Hostholder��ά��һ��ThreadLocal<User>

2.	ģ�Ͷ���
News         				User						Message								Login_ticket   		Comment
Id           				Id							Id										Id								Id
Title        				Name						From_id								User_id			      Content
Link         				Password				To_id									Ticket						User_id
Image        				Salt						Created_date					Expired						Entity_id
Like_count   				Head_url				Has_read							status						Entity_type
Comment_count												Conversation_id													Created_date
Created_date 																																status
User_id      

3.	������AOP
@Before("execution(* com.lyubinbin.controller.*Controller.*(..))")
@After("execution(* com.lyubinbin.controller.*Controller.*(..))")
log��Joitpoint����Ϣ

4.	Service�㡪���ṩ����DAO������������ݿ�Ľӿ�
CommentService����ȡ�����ӡ�ɾ������
LikeService�����ޡ���ȣ�Redis���ݿ�ʵ�֣������޲ȶ�������ͺ�id����key��
MessageService����ȡ�Ի���Ϣ��δ��������
NewsService����ȡ�������ţ�������ţ�����������
QiniuService������ţ����ʵ��ͼƬ���ϴ����洢������
UserService����ȡ�û���Ϣ

5.	Controller��
HomeController��/index, /user/{userId}������Ĭ�ϻ��Լ�����ҳ�����ʱ�����ҳ
LoginController��/reg, /login, /logout��ע�ᣬ��¼���ǳ�
MessageController��/msg/list, /msg/detail, /msg/addmessage������վ�����б�����Ի��򣬷���վ����
NewsController��/news/{newsId}, /user/assNews, /uploadImage, /image, /addComment�����ŵ���ҳ������ҳ���ϴ�ͼƬ����������
LikeController��/like, /dislike���޲ȹ��ܣ�json AJAX����

6.	�첽���С����޲ȣ���¼ʱϵͳ�����ʼ�
event producer -> event list -> event consumer(deliver event to different event handlers)
