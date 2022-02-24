# MONEA-DEVTOOL
[MONEA](https://github.com/datt16/MONEA)の開発用ツール(Androidアプリ)  
メンバー全員Androiderだったのとアプリ開いて直ぐにデータとかを変更できるようにしたかった為ネイティブアプリとして作成。

![Twitter post - 1](https://user-images.githubusercontent.com/44765362/155550627-3576387d-1c71-472f-88e8-dbde0bb9635c.png)

RDB = Firebase Realtime Database

### 機能
- ~~RDB上の特定の位置に、任意のサンプルデータを追加する機能~~ → 廃止
- 設置しているセンサーの表示設定の変更
- 部屋情報の変更(センサーの割り当てなど)

### 未実装機能
- 部屋・センサーの追加機能及び削除機能 → 必要なかった
- サンプルデータ編集機能 → 必要なかった
- ユーザー認証機能 → 必要なかった

### 利用している技術
- Firebase : Realtime Database上のデータを操作してる
- ViewBinding : `R.id.~~~`で要素を指定しなくて良い → 開発効率アップ
- Compose(Jetpack) : XMLじゃなくてコード上にUIを書ける → UI組みやすくなって開発効率アップ
- navigation(Jetpack) : Fragmentを繋げてナビゲーションを簡単に作れる　→ 開発効率アップ
- Flow : RDB上のデータを参照する方法がリスナーをアタッチする方法しかなかったため、Flowでデータを受け渡すようにした  
