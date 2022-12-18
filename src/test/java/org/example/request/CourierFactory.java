package org.example.request;

public class CourierFactory {

    public static CreateCourierRequest createCourierRequest(String login) {
        var request = new CreateCourierRequest();
        request.setLogin(login);
        request.setPassword("qwerty");
        request.setFirstName("Any");
        return request;
    }


    public static LoginCourierRequest loginCourierRequest(String login) {
        var request = new LoginCourierRequest();
        request.setLogin(login);
        request.setPassword("qwerty");
        return request;
    }
}
