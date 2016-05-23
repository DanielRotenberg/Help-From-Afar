package com.danirg10000gmail.HelpFromAfar.dataBase;

public class ParseData {
    public static final class AppData{
        public  static final String APP_ID = "euxoy8BjmW8FzjRlxW6bRi5mnMg2KMpswnFALuov";
        public static final String CLIENT_KEY = "dHe1RENo7hp5TiAwenyU0JFd07l975TWAONfan7y";
    }

    public static final class AnswersClass {
        public static final String NAME = "parseAnswerClass";

        public static final class AnswerClassCols{
            public static final String USER_NAME = "userName";
            public static final String ANSWERS_CREATED_AT_COLUMN = "createdAt";
            public static final String ANSWERS_ARRAY_COLUMN = "answersArray";
            public static final String UUID_COLUMN = "uuid";
            public static final String COMMENTS_COLUMN = "comments";
        }
    }
    public static final class UserClass{
        public static final String NAME = "_User";

        public static final class UserClassCols{
            public static final String USER_NAME = "username";
            public static final String NAME = "name";
            public static final String GENDER = "gender";
            public static final String AGE = "age";
            public static final String IS_THERAPIST = "isTherapist";
            public static final String COUNTRY = "country";
            public static final String TOTAL_QUESTIONNAIRES = "totalQ";
        }
    }
}
