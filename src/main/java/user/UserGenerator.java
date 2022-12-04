package user;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {
private static final String RANDOM_EMAIL = RandomStringUtils.random(7,true,false)+"@"+RandomStringUtils.random(6,true,false)+".ru";
private static final String RANDOM_PASSWORD = RandomStringUtils.random(5,true,true);
private static final String RANDOM_NAME = RandomStringUtils.random(10,true,false);



    @Step("Наполняем объект User необходимыми значениями")
    public static User generateRandom(){
        return new User(RANDOM_EMAIL, RANDOM_PASSWORD, RANDOM_NAME);
    }
}
