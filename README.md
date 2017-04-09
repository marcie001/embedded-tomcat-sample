# Embedded Tomcat のサンプル

Embedded Tomcat でサーブレットアプリケーションを動かすサンプル。

## 事前準備

以下をインストールしておくこと

* JDK 1.8
* PostgreSQL

## 設定

`src/main/resources/application.conf` が設定ファイルになっているので、環境に合わせる。

## 起動方法

jar を作成し、起動する。

```
$ ./mvnw clean package
$ java -jar target/employees-app-1.0-jar-with-dependencies.jar
```

また、Eclipse では、 `com.example.Main` クラスを Java アプリケーションとして実行することができる。

## 起動確認

ブラウザで [http://127.0.0.1:8080/employees](http://127.0.0.1:8080/employees) にアクセスする。
