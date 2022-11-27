package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;

    @Autowired MemberRepository memberRepository;

    @Autowired EntityManager em;

    @Test
    @Rollback(value = false)
    public void memberJoin() throws Exception {
        //given
        Member member = new Member();
        member.setName("park");

        //then
        Long savedId = memberService.join(member);

        //when
        em.flush();
        assertEquals(member, memberRepository.findOne(savedId));

    }

    @Test(expected = IllegalStateException.class)
    public void dupMember() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("park");

        Member member2 = new Member();
        member2.setName("park");

        //then
        memberService.join(member1);
        memberService.join(member2); //예외가 발생하는 시점.

        //when
        fail("예외가 발생해야 합니다.");
    }
}