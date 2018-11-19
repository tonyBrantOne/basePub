package com.dyc.pub.model;/**
 * @Auther: tony_jaa
 * @Date: 2018/8/19 09:42
 * @Description:
 */

/**
 * @Auther: tony_jaa
 * @Date: 2018/8/19 09:42
 * @Description:
 */
public class User {
    private String name;

    public User(){
        this.name = "77777788";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}
