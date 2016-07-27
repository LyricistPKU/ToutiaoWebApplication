# ToutiaoWebApplication
��Ŀʵ�ֹ��ܣ�
ע�ᡢ��¼�����ۡ�ͼƬ���ƴ洢��վ���Ż������첽�޲ȡ���¼�ʼ�֪ͨ���첽��
��Ŀδ��ɣ�
�û�ͷ���ϴ���ע����Ϣ����ϸ������Ҫ�����ݿ⣩��������ࣨload more������������Ŀǰ��¼��ҳ���Ű���ʱ�����򣩣�
�����۵����ۣ��û��ȼ�����...

1.	��������
��implements HandlerInterceptor; override preHandle, postHandle, afterCompletion��
PassportInterceptor����Cookie��Ѱ��ToutiaoWebApplication�����ŵĵ�¼Cookie��У���Ƿ���Ч��status�����Ƿ���ڣ�expired��������������ȡ���û���Ϣ����hostholder��������user
LoginRequiredInterceptor: ����Ϣ��ͷ��ҳ�����controller��֮ǰ����Ƿ��Ѿ���¼��ҳ���Ϸ���ʾ��¼��ť���û�������
Hostholder��ά��һ��ThreadLocal<User>

2.	ģ�Ͷ���
News�����ţ���id,title,link,image,like_count,comment_count,created_date,user_id
User���û�����id,name,password,salt,head_url
������������ɵ�salt��pwd����MMD5���ܵõ�,�û�email�ֶκ�ͷ���ϴ���ǰ��˽���δ���
Message����Ϣ��վ���ţ���id,from_id,to_id,created_date,has_read,conversation_id
Login_ticket����վ��¼��Cookie��:id,user_id,ticket,expired,status
Comment�����ۣ���id,content,user_id,entity_id�����۶����id��,entity_type�����۶�������ͣ�,created_date,status      

3.	������AOP
@Before("execution(* com.lyubinbin.controller.*Controller.*(..))")
@After("execution(* com.lyubinbin.controller.*Controller.*(..))")
log��Joitpoint����Ϣ

4.	DAO��
��MyBatis�ṩ��JDBC�ӿ�ʵ��Service�������ݿ�Ľ���

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
MessageController��/msg/list, /msg/detail������detailҳ���������δ����Ϣ��, /msg/addmessage������վ�����б�����Ի��򣬷���վ���ţ���post�ύ��
NewsController��/news/{newsId}, /user/assNews, /uploadImage, /image, /addComment�����ŵ���ҳ������ҳ���ϴ�ͼƬ����������
LikeController��/like, /dislike���޲ȹ��ܣ�json AJAX����

6.	�첽���С����޲ȣ���¼ʱϵͳ�����ʼ�
event producer -> event list -> event consumer(deliver event to different event handlers)
����producer���¼�ת��ΪJSON��ʽѹ��redis����
��������Ϊÿ��handler���崦����¼�����
����consumer����Spring�Դ�����ҳ�����handler��ά��һ���¼���Ӧ��handler����һ�Զࣩ��map
����redis��brpop����������������ַ�����ص�handler
�������������������¼������ȼ������Consumer�Ķ���˳��
