# jdbc fix


## command

* install local  maven  repo (or use add lib path)

```shell
mvn install:install-file -Dfile=./inceptor-sdk-transwarp-6.1.0-SNAPSHOT.jar -DgroupId=com.hiveapp -DartifactId=transwarp2  -Dversion=6.1.0-SNAPSHOT -Dpackaging=jar -DgeneratePom=true 
```

* build

```shell
mvn clean package
```

* replace

```shell
jar -xf inceptor-sdk-transwarp-6.1.0-SNAPSHOT.jar

jar -cf inceptor-sdk-transwarp-6.1.0-SNAPSHOT.jar -C <path to inceptor-sdk-transwarp jars>/ .
```