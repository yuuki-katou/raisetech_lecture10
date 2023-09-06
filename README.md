# Lecture10 課題 CRUD処理をすべて備えたREST APIの作成
## 概要
このREST APIは従業員の情報を管理するためのものです。
枠組みは、第九回課題の従業員の情報を管理するREST APIを流用しています。
今回のアップデートでは、READ処理に加えて**CREATE処理**を新たに実装しました。

## 実装内容
- **EmployControllerクラス**：従業員の情報を登録するメソッドaddEmployeeを追加<br>
1. リクエストボディからEmployeeの情報を取得します。
2. Serviceを通じて、新しいEmployeeをデータベースに保存します。
3. 保存されたEmployeeのIDを取得し、それを使用して新しく作成されたEmployeeのURIを生成します。
4. HTTP 201 (Created) ステータスとともに、新しく作成されたEmployeeのURIをレスポンスヘッダーに追加します。

- **EmployeeCreateFromクラス**：新しい従業員情報を登録する際に使用するデータフォーム
- **EmployeeMapperインターフェース**：従業員の情報を登録するSQLクエリを追記
- **EmployeeServiceImpl**：従業員の情報を登録するためのメソッドを追記

## 使い方
#### 1. Postman を使って、以下のエンドポイントにリクエストを送信します。
・POST http://localhost:8080/employees<br>

・Bodyの内容
````JSON
    {
        "name": "加藤勇希",
        "birthdate": "1998-09-14",
        "department": "クラウド基盤",
        "role": "システムエンジニア",
        "email": "yuki.kato@example.com",
        "phone": "010-202-30340"
    }
````
## 実行結果
HTTP 201ステータスとメッセージ"a new employee is created"、レスポンスヘッダーにEmployeeのURIが記載されていることが確認できました。
![image](https://github.com/yuuki-katou/raisetech_lecture10/assets/142807995/184dd2b2-ab18-4f87-a495-c9e0301bb3a4)

![image](https://github.com/yuuki-katou/raisetech_lecture10/assets/142807995/c06eda5d-10c9-4403-8882-a1323d0ac975)







