package TripToN.TripToN.service;

import org.junit.jupiter.api.*;
public class UserServiceTest {

    @BeforeAll  // 클래스의 모든 테스트 실행 전에 처음 한 번만 실행하는 메서드
    static void 전체_테스트_시작_전() {
        System.out.println("Start all tests");
    }

    @BeforeEach // 각 테스트 메서드 실행 전마다 실행하는 메서드
    void 각_테스트_시작_전() {
        System.out.println("Start a test");
    }

    @Test// 메서드가 테스트임을 JUnit에게 알려준다, @Test가 없다면 실행 x
    void 테스트() {
        System.out.println("This is test");
    }

    @AfterEach // 각 테스트 메서드 실행 후마다 메서드 실행
    void 각_테스트_종료_후() {
        System.out.println("This test is over");
    }

    @AfterAll // 클래스의 모든 테스트 실행 후에 한 번만 실행
    static void 전체_테스트_종료_후() {
        System.out.println("All tests are over");
    }
}
