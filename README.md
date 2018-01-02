# OAuth2服务端, 参照: https://oauth.net/2, https://github.com/jeansfish/RFC6749.zh-cn/blob/master/index.md

### 下载项目
git clone https://github.com/shijian-ws/oauth2.git

### 编译项目
mvn clean package

### 运行项目
java -Dspring.profiles.active=local -jar target/oauth2-0.1.jar &

### 创建客户凭证
curl -s -H 'Content-Type: application/json' -d '{"client_id":"test","client_secret":"test","redirect_uri":"http://127.0.0.1"}' "http://127.0.0.1:8080/manager/client"

### 创建用户
curl -s -H 'Content-Type: application/json' -d '{"username":"admin","password":"123456"}' "http://127.0.0.1:8080/manager/user"

### 获取授权码, 携带在Location
curl -s -i -u admin:123456 -d "client_id=test&redirect_uri=http://127.0.0.1&response_type=code&scope=scope&state=state" "http://127.0.0.1:8080/oauth2/authorization"

### 获取访问令牌, 在Location提取
#### JSON格式, 去除Accept请求头为普通参数格式
curl -s -H "Accept: application/json" -d "client_id=test&client_secret=test&grant_type=authorization_code&code=" "http://127.0.0.1:8080/oauth2/access_token"

### 刷新访问令牌
#### JSON格式, 去除Accept请求头为普通参数格式
curl -s -H "Accept: application/json" -d "client_id=test&client_secret=test&grant_type=refresh_token&refresh_token=" "http://127.0.0.1:8080/oauth2/refresh_token"

### 获取用户信息
#### JSON格式, 去除Accept请求头为普通参数格式
curl -s -H "Accept: application/json" -d "access_token=" "http://127.0.0.1:8080/oauth2/user_info"

#### Bearer请求, access_token需要Base64加密
curl -s -X POST -H "Authorization: Bearer " -H "Accept: application/json" "http://127.0.0.1:8080/oauth2/user_info"
