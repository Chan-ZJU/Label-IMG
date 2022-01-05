# Label-IMG 
> toy website 'labelMe'
>
> A website to Label IMG for deep learning training

File Structure:

```shell
.
├── LICENSE
├── README.md
├── label-img-springboot
│   ├── HELP.md
│   ├── label-img-springboot.iml
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   ├── sql.md
│   ├── src
│   ├── target
│   └── video
├── label-img-vue
│   ├── README.md
│   ├── babel.config.js
│   ├── dist
│   ├── node_modules
│   ├── package-lock.json
│   ├── package.json
│   ├── public
│   ├── src
│   └── vue.config.js
└── sql.md

9 directories, 14 files
```

How to run front-end:

```pow
cd label-img-vue
npm install
npm run serve
```

How to run back-end (jdk version is 11):

```po
cd .\label-img-springboot\target
java -jar .\label-img-springboot-0.0.1-SNAPSHOT.jar
```

SQL scripts for creating tables are in `sql.md`



- [ ] integrate shiro for security
- [x] upload file to oss instead
- [ ] Use JavaMailSender to send email when the task is completed
- [ ] Use JavaMailSender to send email containing auth-code when signing up
- [x] Vue uploader check file types (video and image)
