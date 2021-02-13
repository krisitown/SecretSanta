package bg.sofia.uni.fmi.mjt.wish.list.models;

import java.util.HashSet;
import java.util.Set;

public class WishList {
    private String student;
    private Set<String> wishList;

    public WishList() {
        this.wishList = new HashSet<>();
    }

    public WishList(String student, Set<String> wishList) {
        this.student = student;
        this.wishList = wishList;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public Set<String> getWishList() {
        return wishList;
    }

    public void setWishList(Set<String> wishList) {
        this.wishList = wishList;
    }
}
