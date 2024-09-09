package com.secondExample.demo.TASK_2;

import java.util.ArrayList;
import java.util.List;

public class User {
    int id;
    String name;
    String email;

    List<User> followings = new ArrayList<>();
    List<Message> messages = new ArrayList<>();
    List<Post> posts = new ArrayList<>();
}
