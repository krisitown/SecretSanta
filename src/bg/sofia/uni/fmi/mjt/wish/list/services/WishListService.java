package bg.sofia.uni.fmi.mjt.wish.list.services;

import bg.sofia.uni.fmi.mjt.wish.list.models.WishList;

import java.util.*;

public class WishListService {
    private Map<String, Set<String>> presents;

    public WishListService(Map<String, Set<String>> presents) {
        this.presents = presents;
    }

    public WishList getWishList(String studentName){
        List<String> students = new ArrayList<>(presents.keySet());
        if(students.size() == 0){
            return new WishList();
        } else {
            if(students.contains(studentName) && students.size() == 1){
                return new WishList();
            }

            Random rnd = new Random();
            List<String> availableStudents = new ArrayList<>(List.copyOf(students));
            availableStudents.remove(studentName);
            String student = availableStudents.get(rnd.nextInt(availableStudents.size()));
            Set<String> gifts = presents.get(student);
            WishList result = new WishList(student, gifts);
            presents.remove(student);
            return result;
        }
    }

    public String postWish(String studentName, String gift) {
        Set<String> gifts = null;
        if(presents.containsKey(studentName)){
            gifts = presents.get(studentName);
        } else {
            gifts = new HashSet<>();
        }

        if(gifts.contains(gift)){
            return String.format("The same gift for student %s was already submitted", studentName);
        } else {
            gifts.add(gift);
            if(presents.containsKey(studentName)){
                presents.replace(studentName, gifts);
            } else {
                presents.put(studentName, gifts);
            }
            return String.format("Gift %s for student %s submitted successfully", gift, studentName);
        }
    }
}
