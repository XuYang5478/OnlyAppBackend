# 项目简介
平时会写一些小应用，用这个项目当后端，目前有一个[Web前端](https://github.com/XuYang5478/only-apps)。这个后端只提供一些接口，没有任何界面，需要配合前端项目使用。
点[这里](http://www.onlyapps.cn/)可以看到已部署好的web界面。
- 密码加密：Spring Security
- 数据库：MySQL/MariaDB - JPA
# 目前进度
- [x] Todo
- [x] 疫情数据展示
- [x] Note
- [ ] ...
# 配置运行
将`resource`文件夹下的`application-template.yaml`重命名为需要的配置文件名，如`application.yaml`，然后修改其中的内容为自己的环境信息。最后运行时哪里报错改哪里，就可以啦。
# 致谢
- 感谢Git和Github为代码同步和存储提供支持
- 感谢七牛云对象存储对图床和文件暂存提供支持
- 感谢[BlankerL/DXY-COVID-19-Crawler](https://github.com/BlankerL/DXY-COVID-19-Crawler)爬虫项目为疫情数据展示提供数据支持