-- デッキリストテーブル。取得/シャッフルしたデッキリスト内のカードが1枚1レコードとして挿入される
create table DECKLIST (
  DECKLIST_ID INT
  , CARD_NAME VARCHAR(100) not null
  , CARD_ORDER INT
  , REGIST_DATE datetime
  , DELETE_FLG char(1) not null
  , constraint DECKLIST_PKC primary key (DECKLIST_ID,CARD_ORDER)
) ;

--
-- シャッフルテーブル。シャッフル一回を1単位としてレコードを持つ
create table SHUFFLE (
  SHUFFLE_ID INT
  , SESSION_ID INT not null
  , DECKLIST_ID INT not null
  , SHUFFLE_TYPE VARCHAR(3) not null
  , DEAL_NUM INT
  , REGIST_DATE Datetime
  , DELETE_FLG Char(1) not null
  , constraint SHUFFLE_PKC primary key (SHUFFLE_ID)
) ;

create index SHUFFLE_IX1
  on SHUFFLE(SESSION_ID);

-- シャッフルセッションテーブル。
-- ユーザーのセッション(デッキ取り込み～セッション自動破棄orデッキ再取り込み)を1単位としてレコードを持つ
create table SHUFFLE_SESSION (
  SESSION_ID INT
  , INITIAL_DECKLIST_ID INT not null
  , REGIST_DATE DATETIME
  , DELETE_FLG CHAR(1) not null
  , constraint SHUFFLE_SESSION_PKC primary key (SESSION_ID)
) ;
