DROP TABLE IF EXISTS employees;

CREATE TABLE employees (
  id INT UNSIGNED AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  birthdate DATE NOT NULL,
  department VARCHAR(100) NOT NULL,
  role VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  phone VARCHAR(15) NOT NULL,
  PRIMARY KEY(id)
);

INSERT INTO employees (name, birthdate, department, role, email, phone)
VALUES
("山田太郎", "1985-01-01", "営業部", "営業マネージャー", "taro.yamada@example.com", "012-3456-7890"),
("鈴木花子", "1986-02-02", "総務", "総務マネージャー", "hanako.suzuki@example.com", "098-7654-3210"),
("佐藤一郎", "1990-03-03", "情報システム部", "システムエンジニア", "ichiro.sato@example.com", "012-3456-7890"),
("田中二郎", "1991-04-04", "マーケティング部", "マーケティングマネージャー", "jiro.tanaka@example.com", "098-7654-3210"),
("高橋三郎", "1992-05-05", "経理部", "経理", "saburo.takahashi@example.com", "012-3456-7890"),
("中村四郎", "1993-06-06", "人事部", "人事マネージャー", "shiro.nakamura@example.com", "098-7654-3210"),
("村田五郎", "1994-07-07", "営業部", "営業", "goro.murata@example.com", "012-3456-7890"),
("小林六郎", "1995-08-08", "総務", "総務スタッフ", "rokuro.kobayashi@example.com", "098-7654-3210"),
("佐々木七郎", "1996-09-09", "情報システム部", "プログラマー", "shichiro.sasaki@example.com", "012-3456-7890"),
("斉藤八郎", "1997-10-10", "マーケティング部", "マーケティングスタッフ", "hachiro.saito@example.com", "098-7654-3210"),
("阿部九郎", "1998-11-11", "経理部", "経理スタッフ", "kuro.abe@example.com", "012-3456-7890"),
("吉田十郎", "1999-12-12", "人事部", "人事スタッフ", "juro.yoshida@example.com", "098-7654-3210"),
("佐藤健太", "1987-01-13", "営業部", "営業", "kenta.sato@example.com", "012-3456-7890"),
("田中さくら", "1988-02-14", "総務", "総務スタッフ", "sakura.tanaka@example.com", "098-7654-3210"),
("高橋健一", "1989-03-15", "情報システム部", "システムエンジニア", "kenichi.takahashi@example.com", "012-3456-7890"),
("中村直樹", "1990-04-16", "マーケティング部", "マーケティングスタッフ", "naoki.nakamura@example.com", "098-7654-3210"),
("村田聡", "1991-05-17", "経理部", "経理", "satoshi.murata@example.com", "012-3456-7890"),
("小林太一", "1992-06-18", "人事部", "人事スタッフ", "taichi.kobayashi@example.com", "098-7654-3210"),
("佐々木直人", "1993-07-19", "営業部", "営業マネージャー", "naoto.sasaki@example.com", "012-3456-7890"),
("斉藤康平", "1994-08-20", "総務", "総務マネージャー", "kohei.saito@example.com", "012-3456-7890");

