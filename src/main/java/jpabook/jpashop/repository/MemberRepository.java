package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


//Repository에서의 연속적인 코드를 extends JpaRepository : Spring data jpa에서 자동으로 구현체를 만들어준다
public interface MemberRepository extends JpaRepository<Member, Long> {

    //select m from member m where m.name = :name 으로 자동으로 날려준다.
    List<Member> findByName(String name);
}
