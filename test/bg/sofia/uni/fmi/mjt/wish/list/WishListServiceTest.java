package bg.sofia.uni.fmi.mjt.wish.list;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import bg.sofia.uni.fmi.mjt.wish.list.models.WishList;
import bg.sofia.uni.fmi.mjt.wish.list.services.WishListService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class WishListServiceTest {
    private String principal;
    private WishListService service;

    @Mock
    private Map<String, Set<String>> mockedPresents;

    @Before
    public void setup(){
        this.principal = "Peter";
        this.service = new WishListService(mockedPresents);
    }

    @Test
    public void getWishWhenPresentsEmptyWishlistShouldReturnEmptyObject(){
        when(mockedPresents.keySet()).thenReturn(new HashSet<>());
        WishList expected = new WishList();

        WishList result = this.service.getWishList(this.principal);

        assertEquals(expected.getStudent(), result.getStudent());
        assertArrayEquals(expected.getWishList().toArray(), result.getWishList().toArray());
    }

    @Test
    public void getWishwhenPresentsContainOnlyPrinicipalShouldReturnEmptyObject(){
        when(mockedPresents.keySet()).thenReturn(Set.of(this.principal));
        WishList expected = new WishList();

        WishList result = this.service.getWishList(this.principal);

        assertEquals(expected.getStudent(), result.getStudent());
        assertArrayEquals(expected.getWishList().toArray(), result.getWishList().toArray());
    }

    @Test
    public void getWishWhenPresentContainStudentDifferentThanPrincipalShouldReturnWishList(){
        String otherStudent = "George";
        String gift = "Bike";
        when(mockedPresents.keySet()).thenReturn(Set.of(this.principal, otherStudent));
        when(mockedPresents.get(otherStudent)).thenReturn(Set.of(gift));
        WishList expected = new WishList(otherStudent, Set.of(gift));

        WishList result = this.service.getWishList(this.principal);

        assertEquals(expected.getStudent(), result.getStudent());
        assertArrayEquals(expected.getWishList().toArray(), result.getWishList().toArray());
    }

    @Test
    public void postWishWhenStudentDoesNotHaveTheGiftShouldAddTheGift(){
        String student = "George";
        String gift = "Bike";

        HashMap<String, Set<String>> presents = new HashMap<>();
        presents.put(student, new HashSet<>());
        WishListService postWishService = new WishListService(presents);
        String expected = String.format("Gift %s for student %s submitted successfully", gift, student);

        String result = postWishService.postWish(student, gift);

        assertEquals(expected, result);
        assert(presents.get(student).contains(gift));
    }

    @Test
    public void postWishWhenStudentHasTheGiftShouldNotAddTheGift(){
        String student = "George";
        String gift = "Bike";

        HashMap<String, Set<String>> presents = new HashMap<>();
        presents.put(student, Set.of(gift));
        WishListService postWishService = new WishListService(presents);
        String expected = String.format("The same gift for student %s was already submitted", student);

        String result = postWishService.postWish(student, gift);

        assertEquals(expected, result);
        assert(presents.get(student).contains(gift));
    }
}
